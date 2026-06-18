/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.standard.services.appchannel;

import static com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelMessageHandler.ERR_INVALID_MESSAGE;
import static com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelMessageHandler.ERR_JSON_SYNTAX;
import static com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelMessageHandler.ERR_PROCESS_EXCEPTION;
import static com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelMessageHandler.ERR_UNKNOWN_MESSAGE;
import static com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelMessageTestHelper.makeServiceChannelSetupMessage;
import static com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelMessageTestHelper.makeTestAppChannelServiceMessage;
import static com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelMessageTestHelper.makeTestChannelPayloadMessage;
import static com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelMessageTestHelper.makeTestChannelSetupMessage;
import static com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelMessageTestHelper.makeTestChannelTeardownMessage;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import android.os.RemoteException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.JsonObject;
import com.hp.ext.types.actions.NonAction;
import com.hp.ext.types.authentication.AuthenticationContinued;
import com.hp.ext.types.authentication.PrePromptResult;
import com.hp.ext.types.authentication.PrePromptResultValue;
import com.hp.ext.types.authentication.PromptResultAction;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardJsonParser;
import com.hp.jetadvantage.link.device.services.standard.services.StandardWebsocketCallbackService;
import com.hp.ws.websocket.AppChannelService;
import com.hp.ws.websocket.AppChannelServiceResponse;
import com.hp.ws.websocket.AppChannelSetup;
import com.hp.ws.websocket.JsonTypedObject;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(MockitoJUnitRunner.class)
public class AppChannelMessageHandlerUnitTest extends TestCase {

    // constants for service channel tests
    private static final String CHANNEL_ID = "223762e2-8bf7-4e29-a1d7-0790103e83a7";
    private static final String SERVICE_CALL_ID = "81c7d95d-1da8-4b19-a70f-a4423267e385";
    private static final String E2_SERVICE_GUN = "com.hp.ext.service.authentication.version.1";
    private static final String SERVICE_GUN = "com.hp.ext.service.authentication.version.1.clientService.serviceTarget";
    private static final String SERVICE_PACKAGE_ID = "app1";
    private static final String SERVICE_PATH = "prePromptResult";

    AppChannelMessageHandler handler;
    @Mock
    StandardWebsocketCallbackService mockWsCallbackService;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        handler = new AppChannelMessageHandler(mockWsCallbackService);
    }

    @After
    public void tearDown() throws Exception {
        handler.clearChannelMap();
        AppChannelCallbackRegistry.clear();
    }

    @Test
    public void GivenAppChannelMessageHandler_WhenConstructorCalled_ThenObjectCreated() {
        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWsCallbackService);
        assertNotNull(handler);
    }

    @Test
    public void GivenAppChannelMessageHandler_WhenNullMessageReceived_ThenDiscardSilently() {
        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWsCallbackService);
        handler.onReceived(0, null);
        assertEquals("onReceiveProcessLastError", ERR_INVALID_MESSAGE, handler.getOnReceiveProcessLastError());
    }

    @Test
    public void GivenAppChannelMessageHandler_WhenEmptyMessageReceived_ThenDiscardSilently() {
        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWsCallbackService);
        handler.onReceived(0, "{}");
        assertEquals("onReceiveProcessLastError", ERR_INVALID_MESSAGE, handler.getOnReceiveProcessLastError());
    }

    @Test
    public void GivenAppChannelMessageHandler_WhenInvalidJsonMessageReceived_ThenDiscardSilently() {
        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWsCallbackService);
        handler.onReceived(0, "{}}");
        assertEquals("onReceiveProcessLastError", ERR_JSON_SYNTAX, handler.getOnReceiveProcessLastError());
    }

    @Test
    public void GivenAppChannelMessageHandler_WhenEmptyChannelMessageReceived_ThenDiscardSilently() {
        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWsCallbackService);
        handler.onReceived(0, "{\"channelMessage\": {}}");
        assertEquals("onReceiveProcessLastError", ERR_INVALID_MESSAGE, handler.getOnReceiveProcessLastError());
    }

    @Test
    public void GivenAppChannelMessageHandler_WhenUnknownMessageReceived_ThenDiscardSilently() {
        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWsCallbackService);
        handler.onReceived(0, "{\"channelMessage\": {\"channelId\":\"223762e2-8bf7-4e29-a1d7-0790103e83a7\", " +
                "\"message\": {\"unknown\": {}}}}");
        assertEquals("onReceiveProcessLastError", ERR_UNKNOWN_MESSAGE, handler.getOnReceiveProcessLastError());
    }

    @Test
    public void GivenAppChannelMessageHandler_WhenChannelSetupMessageReceived_ThenChannelCreated() {
        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWsCallbackService);
        String channelId = "223762e2-8bf7-4e29-a1d7-0790103e83a7";
        String e2ServiceGun = "com.ext.service.dummy.version.1";
        String packageId = "app1";

        handler.onReceived(0, makeTestChannelSetupMessage(channelId, packageId, e2ServiceGun));

        //verify channel setup
        AppChannelSetup setup = handler.getChannel(channelId);
        assertNotNull(setup);
        assertEquals(e2ServiceGun, setup.getDetails().getPayloadDetails().getE2ServiceGun());
        assertEquals(packageId, setup.getPackageId());
    }

    @Test
    public void GivenAppChannelMessageHandler_WhenDuplicatedChannelSetupMessageReceived_ThenChannelError() throws RemoteException {
        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWsCallbackService);
        String channelId = "223762e2-8bf7-4e29-a1d7-0790103e83a7";
        String e2ServiceGun = "com.ext.service.dummy.version.1";
        String packageId = "app1";

        handler.onReceived(0, makeTestChannelSetupMessage(channelId, packageId, e2ServiceGun));
        handler.onReceived(0, makeTestChannelSetupMessage(channelId, packageId, e2ServiceGun));

        int what = 0;
        String errorMessage = "{\"channelMessage\":{\"channelId\":\"223762e2-8bf7-4e29-a1d7-0790103e83a7\"," +
                "\"message\":{\"error\":{}}}}";
        verify(mockWsCallbackService).sendMessage(what, errorMessage);
    }

    @Test
    public void GivenAppChannelMessageHandler_WhenChannelTeardownMessageReceived_ThenChannelDeleted() {
        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWsCallbackService);
        String channelId = "223762e2-8bf7-4e29-a1d7-0790103e83a7";
        String e2ServiceGun = "com.ext.service.dummy.version.1";
        String packageId = "app1";

        //1) setup message received
        handler.onReceived(0, makeTestChannelSetupMessage(channelId, packageId, e2ServiceGun));
        assertNotNull(handler.getChannel(channelId));

        //2) teardown message received
        handler.onReceived(0, makeTestChannelTeardownMessage(channelId));
        assertNull(handler.getChannel(channelId));
    }

    ////////////////////////////// Tests for Payload AppChannel Message //////////////////////////////
    @Test
    public void GivenAppChannelMessageHandler_WhenChannelPayloadMessageReceived_AndCallbackNotRegistered_ThenSkipMessage() {
        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWsCallbackService);
        String channelId = "223762e2-8bf7-4e29-a1d7-0790103e83a7";
        String e2ServiceGun = "com.ext.service.dummy.version.1";
        String packageId = "app1";

        String typeGun = "com.hp.TestPayloadType";
        String message = "This is a test app message.";

        //1) setup message received
        handler.onReceived(0, makeTestChannelSetupMessage(channelId, packageId, e2ServiceGun));
        assertNotNull(handler.getChannel(channelId));
        assertEquals(0, handler.getOnReceiveProcessLastError());

        //2) payload message received
        handler.onReceived(0, makeTestChannelPayloadMessage(channelId, typeGun, message));
        assertEquals(0, handler.getOnReceiveProcessLastError());

        //3) teardown message received
        handler.onReceived(0, makeTestChannelTeardownMessage(channelId));
        assertNull(handler.getChannel(channelId));
        assertEquals(0, handler.getOnReceiveProcessLastError());
    }

    @Test
    public void GivenAppChannelMessageHandler_WhenChannelPayloadMessageReceived_AndCallbackRegistered_ThenCallbackShouldBeInvokedWithPayload() {
        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWsCallbackService);
        String channelId = "223762e2-8bf7-4e29-a1d7-0790103e83a7";
        String e2ServiceGun = "com.ext.service.dummy.version.1";
        String packageId = "app1";

        String typeGun = "com.hp.TestPayloadType";
        String message = "This is a test app message.";

        AtomicInteger callbackCount = new AtomicInteger(0);

        //1) create payload callback and register it
        StandardDeviceService.IPayloadCallback callback = new StandardDeviceService.IPayloadCallback() {
            @Override
            public void onReceiveNotification(String appPackageId, JsonTypedObject notification) {
                callbackCount.incrementAndGet();
                assertEquals(packageId, appPackageId);
                assertEquals(typeGun, notification.getTypeGUN());
            }
        };

        AppChannelCallbackRegistry.registerPayloadCallback(e2ServiceGun, callback);

        //2) setup message received
        handler.onReceived(0, makeTestChannelSetupMessage(channelId, packageId, e2ServiceGun));
        assertNotNull(handler.getChannel(channelId));
        assertEquals(0, handler.getOnReceiveProcessLastError());

        //3) payload message received
        handler.onReceived(0, makeTestChannelPayloadMessage(channelId, typeGun, message));
        assertEquals(0, handler.getOnReceiveProcessLastError());
        assertEquals(1, callbackCount.get());

        //4) teardown message received
        handler.onReceived(0, makeTestChannelTeardownMessage(channelId));
        assertNull(handler.getChannel(channelId));
        assertEquals(0, handler.getOnReceiveProcessLastError());
    }

    @Test
    public void GivenAppChannelMessageHandler_WhenChannelPayloadMessageReceived_AndCallbackThrowException_ThenExceptionShouldBeHandled() {
        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWsCallbackService);
        String channelId = "223762e2-8bf7-4e29-a1d7-0790103e83a7";
        String e2ServiceGun = "com.ext.service.dummy.version.1";
        String packageId = "app1";

        String typeGun = "com.hp.TestPayloadType";
        String message = "This is a test app message.";

        AtomicInteger callbackCount = new AtomicInteger(0);

        //1) create payload callback and register it
        StandardDeviceService.IPayloadCallback callback = new StandardDeviceService.IPayloadCallback() {
            @Override
            public void onReceiveNotification(String appPackageId, JsonTypedObject notification) {
                callbackCount.incrementAndGet();
                assertEquals(packageId, appPackageId);
                assertEquals(typeGun, notification.getTypeGUN());
                throw new RuntimeException("Test exception in callback");
            }
        };

        AppChannelCallbackRegistry.registerPayloadCallback(e2ServiceGun, callback);

        //2) setup message received
        handler.onReceived(0, makeTestChannelSetupMessage(channelId, packageId, e2ServiceGun));
        assertNotNull(handler.getChannel(channelId));
        assertEquals("getOnReceiveProcessLastError for setup", 0, handler.getOnReceiveProcessLastError());

        //3) payload message received
        handler.onReceived(0, makeTestChannelPayloadMessage(channelId, typeGun, message));
        assertEquals("ERR_PROCESS_EXCEPTION", ERR_PROCESS_EXCEPTION, handler.getOnReceiveProcessLastError());
        assertEquals("callbackCount", 1, callbackCount.get());

        //4) teardown message received
        handler.onReceived(0, makeTestChannelTeardownMessage(channelId));
        assertNull(handler.getChannel(channelId));
        assertEquals("getOnReceiveProcessLastError for teardown", 0, handler.getOnReceiveProcessLastError());
    }

    @Test
    public void GivenAppChannelMessageHandler_WhenChannelPayloadMessageReceived_AndMultiCallbackRegistered_ThenCallbacksShouldBeInvokedWithPayload() {
        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWsCallbackService);
        String channelId = "223762e2-8bf7-4e29-a1d7-0790103e83a7";
        String e2ServiceGun = "com.ext.service.dummy.version.1";
        String packageId = "app1";

        String typeGun = "com.hp.TestPayloadType";
        String message = "This is a test app message.";

        AtomicInteger callbackCount = new AtomicInteger(0);

        //1) create payload callback and register it
        StandardDeviceService.IPayloadCallback callback = new StandardDeviceService.IPayloadCallback() {
            @Override
            public void onReceiveNotification(String appPackageId, JsonTypedObject notification) {
                callbackCount.incrementAndGet();
                assertEquals(packageId, appPackageId);
                assertEquals(typeGun, notification.getTypeGUN());
            }
        };
        AppChannelCallbackRegistry.registerPayloadCallback(e2ServiceGun, callback);

        StandardDeviceService.IPayloadCallback callback2 = new StandardDeviceService.IPayloadCallback() {
            @Override
            public void onReceiveNotification(String appPackageId, JsonTypedObject notification) {
                callbackCount.incrementAndGet();
                assertEquals(packageId, appPackageId);
                assertEquals(typeGun, notification.getTypeGUN());
            }
        };
        AppChannelCallbackRegistry.registerPayloadCallback(e2ServiceGun, callback2);

        //2) setup message received
        handler.onReceived(0, makeTestChannelSetupMessage(channelId, packageId, e2ServiceGun));
        assertNotNull(handler.getChannel(channelId));
        assertEquals(0, handler.getOnReceiveProcessLastError());

        //3) payload message received
        handler.onReceived(0, makeTestChannelPayloadMessage(channelId, typeGun, message));
        assertEquals(0, handler.getOnReceiveProcessLastError());
        assertEquals(2, callbackCount.get());

        //4) unregister a callback
        AppChannelCallbackRegistry.unregisterPayloadCallback(e2ServiceGun, callback);

        //5) 2nd payload message received
        handler.onReceived(0, makeTestChannelPayloadMessage(channelId, typeGun, message));
        assertEquals(0, handler.getOnReceiveProcessLastError());
        assertEquals(3, callbackCount.get());

        //6) teardown message received
        handler.onReceived(0, makeTestChannelTeardownMessage(channelId));
        assertNull(handler.getChannel(channelId));
        assertEquals(0, handler.getOnReceiveProcessLastError());

        AppChannelCallbackRegistry.unregisterPayloadCallback(e2ServiceGun, callback2);
    }

    /**
     * When channel payload message is received without channel setup, the message still needs to be passed to the
     * registered callbacks
     * This scenario is a bit tricky, as the channel setup message is not received before the payload message.
     * But in some cases like sdk service is restarted by android OS, the channel setup caches may be cleared before
     * the payload message.
     * In that case, applicationId for the channel is not available but,we need to make sure that the payload message
     * is still passed to the registered callbacks.
     * Otherwise, job notification will be missing and the job state machine will be stuck.
     * ....Consider revisit this later to send error report to the E2 service in this case to make a channel setup
     * again.
     */
    @Test
    public void GivenAppChannelMessageHandler_WhenChannelPayloadMessageReceivedWithoutChannelSetup_ThenCallbacksShouldBeInvokedWithPayload() {
        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWsCallbackService);
        String channelId = "223762e2-8bf7-4e29-a1d7-0790103e83a7";
        String e2ServiceGun = "com.ext.service.dummy.version.1";
        String packageId = "app1";

        String typeGun = "com.ext.service.dummy.version.1.type.jobNotification";
        String message = "This is a test app message.";

        AtomicInteger callbackCount = new AtomicInteger(0);

        //1) create payload callback and register it
        StandardDeviceService.IPayloadCallback callback = new StandardDeviceService.IPayloadCallback() {
            @Override
            public void onReceiveNotification(String appPackageId, JsonTypedObject notification) {
                callbackCount.incrementAndGet();
                assertEquals(typeGun, notification.getTypeGUN());
            }
        };
        AppChannelCallbackRegistry.registerPayloadCallback(e2ServiceGun, callback);

        StandardDeviceService.IPayloadCallback callback2 = new StandardDeviceService.IPayloadCallback() {
            @Override
            public void onReceiveNotification(String appPackageId, JsonTypedObject notification) {
                callbackCount.incrementAndGet();
                assertEquals(typeGun, notification.getTypeGUN());
            }
        };
        AppChannelCallbackRegistry.registerPayloadCallback(e2ServiceGun, callback2);

        //2) no setup message received

        //3) payload message received
        handler.onReceived(0, makeTestChannelPayloadMessage(channelId, typeGun, message));
        assertEquals(0, handler.getOnReceiveProcessLastError());
        assertEquals(2, callbackCount.get());

        AppChannelCallbackRegistry.unregisterPayloadCallback(e2ServiceGun, callback);
        AppChannelCallbackRegistry.unregisterPayloadCallback(e2ServiceGun, callback2);
    }


    ////////////////////////////// Tests for Service AppChannel Message //////////////////////////////
    @Test
    public void GivenAppChannelMessageHandler_WhenServiceChannelSetupMessageReceived_ThenChannelCreated() {
        handler.onReceived(0, makeServiceChannelSetupMessage(CHANNEL_ID, SERVICE_PACKAGE_ID, E2_SERVICE_GUN,
                SERVICE_GUN));

        //verify channel setup
        AppChannelSetup setup = handler.getChannel(CHANNEL_ID);
        assertNotNull(setup);
        assertEquals(E2_SERVICE_GUN, setup.getDetails().getServiceDetails().getE2ServiceGun());
        assertEquals(SERVICE_PACKAGE_ID, setup.getPackageId());
    }

    @Test
    public void GivenAppChannelMessageHandler_WhenServiceChannelTeardownMessageReceived_ThenChannelDeleted() {
        //1) setup message received
        handler.onReceived(0, makeServiceChannelSetupMessage(CHANNEL_ID, SERVICE_PACKAGE_ID, E2_SERVICE_GUN,
                SERVICE_GUN));
        assertNotNull(handler.getChannel(CHANNEL_ID));

        //2) teardown message received
        handler.onReceived(0, makeTestChannelTeardownMessage(CHANNEL_ID));
        assertNull(handler.getChannel(CHANNEL_ID));
    }

    //@Test //removing this test as the message will be queued and processed later without error
    public void GivenAppChannelMessageHandler_WhenServiceMessageReceivedWithoutCallback_ThenResponse404NotFound() throws RemoteException {
        String expected404Response = getServiceErrorResponse(404);

        // Setup service channel using the helper method
        setupServiceChannel(handler);
        // 2) service message received
        handler.onReceived(0, makeTestAppChannelServiceMessage(CHANNEL_ID, SERVICE_CALL_ID, SERVICE_PATH));
        assertEquals(0, handler.getOnReceiveProcessLastError());

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockWsCallbackService).sendMessage(anyInt(), messageCaptor.capture());
        assertEquals("404Response", expected404Response, messageCaptor.getValue());

        // 3) teardown
        handler.onReceived(0, makeTestChannelTeardownMessage(CHANNEL_ID));
        assertNull(handler.getChannel(CHANNEL_ID));
        assertEquals(0, handler.getOnReceiveProcessLastError());
    }

    @Test
    public void GivenAppChannelMessageHandler_WhenChannelServiceMessageReceivedAndCallbackRegistered_ThenResponse200Ok() {
        String expectedResponse = getServiceResponse200OkPrePromptResult(SERVICE_CALL_ID);
        CountDownLatch latch = new CountDownLatch(1);

        // create and register payload callback
        StandardDeviceService.IServiceCallback callback = new StandardDeviceService.IServiceCallback() {
            @Override
            public AppChannelServiceResponse onServiceCall(String appPackageId, AppChannelService serviceMessage) {
                assertEquals("packageId", SERVICE_PACKAGE_ID, appPackageId);
                assertEquals("serviceCallId", SERVICE_CALL_ID, serviceMessage.getServiceCallId());
                assertEquals("path", SERVICE_PATH, serviceMessage.getPath());
                assertNotNull("getRequestBody", serviceMessage.getRequestBody());
                assertTrue("isRunning in processing", AppChannelServiceThreadPool.isRunning(SERVICE_CALL_ID));
                latch.countDown();
                return createAppChannelResponseForPrePromptResult(SERVICE_CALL_ID);
            }
        };

        AppChannelCallbackRegistry.registerServiceCallback(E2_SERVICE_GUN, SERVICE_PATH, callback);

        setupServiceChannel(handler);

        // service message received
        handler.onReceived(0, makeTestAppChannelServiceMessage(CHANNEL_ID, SERVICE_CALL_ID, SERVICE_PATH));
        assertEquals(0, handler.getOnReceiveProcessLastError());

        //wait until the callback is finished receiving message processing in a separate AppChannelServiceThread
        try {
            boolean result = latch.await(1, TimeUnit.SECONDS);
            assertTrue("callback occurred", result);
            Future<?> future = AppChannelServiceThreadPool.getFuture(SERVICE_CALL_ID);
            if (future != null) {
                future.get(1, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            fail("Exception occurred while waiting for latch" + e);
        }

        try {
            ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
            verify(mockWsCallbackService).sendMessage(anyInt(), messageCaptor.capture());
            assertEquals("response", expectedResponse, messageCaptor.getValue());
            assertFalse("isRunning after finished", AppChannelServiceThreadPool.isRunning(SERVICE_CALL_ID));
        } catch (Exception e) {
            fail("Exception occurred while verifying message" + e);
        }

        // teardown message received
        handler.onReceived(0, makeTestChannelTeardownMessage(CHANNEL_ID));
        assertNull(handler.getChannel(CHANNEL_ID));
        assertEquals(0, handler.getOnReceiveProcessLastError());
        AppChannelCallbackRegistry.unregisterServiceCallback(E2_SERVICE_GUN, SERVICE_PATH);
        AppChannelServiceThreadPool.clear();
    }

    @Test
    public void GivenAppChannelMessageHandler_WhenMultiplePathCallbacksRegistered_ThenCorrectCallbackInvoked() throws ExecutionException, InterruptedException, TimeoutException, RemoteException {
        String serviceCallId2 = "21c7d95d-1da8-4b19-a70f-a4423267e382";
        AtomicInteger callbackCountPath1 = new AtomicInteger(0);
        AtomicInteger callbackCountPath2 = new AtomicInteger(0);

        // Register callback for path1
        StandardDeviceService.IServiceCallback callbackPath1 = (appPackageId, serviceMessage) -> {
            callbackCountPath1.incrementAndGet();
            assertEquals(SERVICE_PATH, serviceMessage.getPath());
            return createAppChannelResponseForPrePromptResult(SERVICE_CALL_ID);
        };
        AppChannelCallbackRegistry.registerServiceCallback(E2_SERVICE_GUN, SERVICE_PATH, callbackPath1);

        // Register callback for path2
        String servicePath2 = "anotherPath";
        StandardDeviceService.IServiceCallback callbackPath2 = (appPackageId, serviceMessage) -> {
            callbackCountPath2.incrementAndGet();
            assertEquals(servicePath2, serviceMessage.getPath());
            return createAppChannelResponseForPrePromptResult(serviceCallId2);
        };
        AppChannelCallbackRegistry.registerServiceCallback(E2_SERVICE_GUN, servicePath2, callbackPath2);

        // Setup service channel
        setupServiceChannel(handler);

        // Send service message for path1
        handler.onReceived(0, makeTestAppChannelServiceMessage(CHANNEL_ID, SERVICE_CALL_ID, SERVICE_PATH));

        Future<?> future = AppChannelServiceThreadPool.getFuture(SERVICE_CALL_ID);
        if (future != null) {
            future.get(1, TimeUnit.SECONDS);
        }

        assertEquals(1, callbackCountPath1.get());
        assertEquals(0, callbackCountPath2.get());

        // Send service message for path2
        handler.onReceived(0, makeTestAppChannelServiceMessage(CHANNEL_ID, serviceCallId2, servicePath2));
        future = AppChannelServiceThreadPool.getFuture(serviceCallId2);
        if (future != null) {
            future.get(1, TimeUnit.SECONDS);
        }
        assertEquals(1, callbackCountPath1.get());
        assertEquals(1, callbackCountPath2.get());

        //Verify response messages
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockWsCallbackService, times(2)).sendMessage(anyInt(), messageCaptor.capture());
        List<String> capturedMessages = messageCaptor.getAllValues();
        assertEquals("First message verification", getServiceResponse200OkPrePromptResult(SERVICE_CALL_ID),
                capturedMessages.get(0));
        assertEquals("Second message verification", getServiceResponse200OkPrePromptResult(serviceCallId2),
                capturedMessages.get(1));

        // Cleanup
        AppChannelCallbackRegistry.unregisterServiceCallback(E2_SERVICE_GUN, SERVICE_PATH);
        AppChannelCallbackRegistry.unregisterServiceCallback(E2_SERVICE_GUN, servicePath2);
    }

    /**
     * Response 500 Internal Server Error in case that the callback returns null
     */
    @Test
    public void GivenAppChannelMessageHandler_WhenServiceCallbackReturnsNull_ThenResponse500Error() {
        String expectedResponse = getServiceErrorResponse(500);
        CountDownLatch latch = new CountDownLatch(1);

        // create and register service callback that returns null
        StandardDeviceService.IServiceCallback callback = new StandardDeviceService.IServiceCallback() {
            @Override
            public AppChannelServiceResponse onServiceCall(String appPackageId, AppChannelService serviceMessage) {
                latch.countDown();
                return null;
            }
        };

        AppChannelCallbackRegistry.registerServiceCallback(E2_SERVICE_GUN, SERVICE_PATH, callback);

        setupServiceChannel(handler);
        // service message received
        handler.onReceived(0, makeTestAppChannelServiceMessage(CHANNEL_ID, SERVICE_CALL_ID, SERVICE_PATH));
        assertEquals(0, handler.getOnReceiveProcessLastError());

        // wait until the callback is finished receiving message processing in a separate AppChannelServiceThread
        try {
            boolean result = latch.await(1, TimeUnit.SECONDS);
            assertTrue("callback occurred", result);
            Future<?> future = AppChannelServiceThreadPool.getFuture(SERVICE_CALL_ID);
            if (future != null) {
                future.get(1, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            fail("Exception occurred while waiting for latch" + e);
        }

        try {
            ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
            verify(mockWsCallbackService).sendMessage(anyInt(), messageCaptor.capture());
            assertEquals("500Response", expectedResponse, messageCaptor.getValue());
            assertFalse("isRunning after finished", AppChannelServiceThreadPool.isRunning(SERVICE_CALL_ID));
        } catch (Exception e) {
            fail("Exception occurred while verifying message" + e);
        }

        // teardown message received
        handler.onReceived(0, makeTestChannelTeardownMessage(CHANNEL_ID));
        assertNull(handler.getChannel(CHANNEL_ID));
        assertEquals(0, handler.getOnReceiveProcessLastError());
    }

    @Test
    public void GivenAppChannelMessageHandler_WhenCallbackThrowsException_ThenResponse500Error() throws RemoteException {
        String expectedResponse = getServiceErrorResponse(500);

        CountDownLatch latch = new CountDownLatch(1);

        // Register a callback that throws an exception
        StandardDeviceService.IServiceCallback callback = new StandardDeviceService.IServiceCallback() {
            @Override
            public AppChannelServiceResponse onServiceCall(String appPackageId, AppChannelService serviceMessage) {
                latch.countDown();
                throw new RuntimeException("Simulated exception");
            }
        };
        AppChannelCallbackRegistry.registerServiceCallback(E2_SERVICE_GUN, SERVICE_PATH, callback);

        // Setup service channel
        setupServiceChannel(handler);

        // Send service message
        handler.onReceived(0, makeTestAppChannelServiceMessage(CHANNEL_ID, SERVICE_CALL_ID, SERVICE_PATH));

        // Wait for callback execution
        try {
            latch.await(1, TimeUnit.SECONDS);
            Future<?> future = AppChannelServiceThreadPool.getFuture(SERVICE_CALL_ID);
            if (future != null) {
                future.get(1, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            fail("Exception: " + e);
        }

        // Verify response
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockWsCallbackService).sendMessage(anyInt(), messageCaptor.capture());
        assertEquals("500Response", expectedResponse, messageCaptor.getValue());
    }

    @Test
    public void GivenAppChannelMessageHandler_WhenMultipleCallbacksRegisteredForSamePath_ThenLaterCallbackInvoked() {
        AtomicInteger callbackCount1 = new AtomicInteger(0);
        AtomicInteger callbackCount2 = new AtomicInteger(0);

        // Register multiple callbacks for the same path
        StandardDeviceService.IServiceCallback callback1 = (appPackageId, serviceMessage) -> {
            callbackCount1.incrementAndGet();
            return createAppChannelResponseForPrePromptResult(SERVICE_CALL_ID);
        };
        StandardDeviceService.IServiceCallback callback2 = (appPackageId, serviceMessage) -> {
            callbackCount2.incrementAndGet();
            return createAppChannelResponseForPrePromptResult(SERVICE_CALL_ID);
        };
        AppChannelCallbackRegistry.registerServiceCallback(E2_SERVICE_GUN, SERVICE_PATH, callback1);
        AppChannelCallbackRegistry.registerServiceCallback(E2_SERVICE_GUN, SERVICE_PATH, callback2);

        // Setup service channel
        setupServiceChannel(handler);

        // Send service message
        handler.onReceived(0, makeTestAppChannelServiceMessage(CHANNEL_ID, SERVICE_CALL_ID, SERVICE_PATH));

        try {
            Future<?> future = AppChannelServiceThreadPool.getFuture(SERVICE_CALL_ID);
            if (future != null) {
                future.get(1, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
        }

        // Verify that both callbacks were invoked
        assertEquals("first callback", 0, callbackCount1.get());
        assertEquals("second callback", 1, callbackCount2.get());

        // Cleanup
        AppChannelCallbackRegistry.unregisterServiceCallback(E2_SERVICE_GUN, SERVICE_PATH);
    }

    @Test
    public void GivenAppChannelMessageHandler_WhenDuplicatedServiceCallIdReceived_ThenResponse409Conflict() throws RemoteException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger callbackCount = new AtomicInteger(0);
        String expected409Response = getServiceErrorResponse(409);
        String expected200Response = getServiceResponse200OkPrePromptResult(SERVICE_CALL_ID);

        // Register multiple callbacks for the same path
        StandardDeviceService.IServiceCallback callback = (appPackageId, serviceMessage) -> {
            //block the callback to simulate a long-running operation
            latch.await(10, TimeUnit.SECONDS);
            callbackCount.incrementAndGet();
            return createAppChannelResponseForPrePromptResult(SERVICE_CALL_ID);
        };
        AppChannelCallbackRegistry.registerServiceCallback(E2_SERVICE_GUN, SERVICE_PATH, callback);

        // Setup service channel
        setupServiceChannel(handler);

        // receive first service message
        handler.onReceived(0, makeTestAppChannelServiceMessage(CHANNEL_ID, SERVICE_CALL_ID, SERVICE_PATH));

        // receive second service message with the same service call id
        handler.onReceived(0, makeTestAppChannelServiceMessage(CHANNEL_ID, SERVICE_CALL_ID, SERVICE_PATH));

        latch.countDown();
        try {
            Future<?> future = AppChannelServiceThreadPool.getFuture(SERVICE_CALL_ID);
            if (future != null) {
                future.get(1, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
        }


        assertEquals("callback count", 1, callbackCount.get());

        //  Verify responses
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockWsCallbackService, times(2)).sendMessage(anyInt(), messageCaptor.capture());
        assertEquals("200Response", expected200Response, messageCaptor.getValue());
        List<String> capturedMessages = messageCaptor.getAllValues();
        assertEquals("First message verification", expected409Response, capturedMessages.get(0));
        assertEquals("Second message verification", expected200Response, capturedMessages.get(1));

        // Cleanup
        AppChannelCallbackRegistry.unregisterServiceCallback(E2_SERVICE_GUN, SERVICE_PATH);
    }

    // Helper method to setup a service channel
    private void setupServiceChannel(AppChannelMessageHandler handler) {
        handler.onReceived(0, makeServiceChannelSetupMessage(CHANNEL_ID, SERVICE_PACKAGE_ID, E2_SERVICE_GUN,
                SERVICE_GUN));
        assertNotNull(handler.getChannel(CHANNEL_ID));
        assertEquals(0, handler.getOnReceiveProcessLastError());
    }

    private AppChannelServiceResponse createAppChannelResponseForPrePromptResult(String serviceCallId) {
        AppChannelServiceResponse response = new AppChannelServiceResponse(serviceCallId, 200);
        try {
            PrePromptResult prePromptResult = new PrePromptResult();
            prePromptResult.setAction(new PromptResultAction(new NonAction()));
            prePromptResult.setResult(new PrePromptResultValue(new AuthenticationContinued()));
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            String prePromptResultString = objectMapper.writeValueAsString(prePromptResult);

            JsonTypedObject responseBody = new JsonTypedObject(prePromptResult.getTypeGUN());
            responseBody.setValue(StandardJsonParser.INSTANCE.fromJson(prePromptResultString, JsonObject.class));
            response.setResponseBody(responseBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            fail("Exception occurred while creating AppChannelServiceResponse" + e);
        }
        return response;
    }

    private String getServiceErrorResponse(int httpStatus) {
        return "{\"channelMessage\":{\"channelId\":\"" + CHANNEL_ID + "\"," +
                "\"message\":{\"service\":{\"serviceCallId\":\"" + SERVICE_CALL_ID + "\"," +
                "\"httpStatus\":" + httpStatus + "}}}}";
    }

    private String getServiceResponse200OkPrePromptResult(String serviceCallId) {
        return "{\"channelMessage\":{\"channelId\":\"" + CHANNEL_ID + "\",\"message\":{\"service" +
                "\":{\"serviceCallId\":\"" + serviceCallId +
                "\",\"httpStatus\":200,\"responseBody\":{\"typeGUN\":\"com.hp.ext.types.authentication.version.1.type" +
                ".prePromptResult\",\"value\":{\"action\":{\"none\":{}}," +
                "\"result\":{\"continue\":{}}}}}}}}";
    }
}
