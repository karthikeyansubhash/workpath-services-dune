package com.hp.jetadvantage.link.device.services.standard;

import static com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelMessageTestHelper.makeTestChannelSetupMessage;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import android.text.TextUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.JsonObject;
import com.hp.ext.types.actions.NonAction;
import com.hp.ext.types.authentication.AuthenticationCanceled;
import com.hp.ext.types.authentication.AuthenticationContinued;
import com.hp.ext.types.authentication.AuthenticationFailed;
import com.hp.ext.types.authentication.AuthenticationSuccess;
import com.hp.ext.types.authentication.PostPromptResult;
import com.hp.ext.types.authentication.PostPromptResultValue;
import com.hp.ext.types.authentication.PrePromptResult;
import com.hp.ext.types.authentication.PrePromptResultValue;
import com.hp.ext.types.authentication.PromptResultAction;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceAuthSessionChangeCallback;
import com.hp.jetadvantage.link.device.services.interfaces.IE2ServiceCallback;
import com.hp.jetadvantage.link.device.services.standard.common.StandardJsonParser;
import com.hp.jetadvantage.link.device.services.standard.services.SystemManagementMessageHandler;
import com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelMessageHandler;
import com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelServiceThreadPool;
import com.hp.jetadvantage.link.device.services.utils.Utils;
import com.hp.ws.websocket.AppChannelServiceResponse;
import com.hp.ws.websocket.JsonTypedObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@RunWith(MockitoJUnitRunner.class)
public class StandardDeviceAuthenticationServiceUnitTest extends StandardDeviceUnitTest {
    private StandardDeviceAuthenticationService deviceAuthService;
    private IDeviceAuthSessionChangeCallback mockCallback;
    private SystemManagementMessageHandler messageHandler;
    private static final String SERVICE_CALL_ID = "81c7d95d-1da8-4b19-a70f-a4423267e385";
    public static final String E2SERVICE_AUTHENTICATION_PRE_PROMPT_GUN = "com.hp.ext.types.authentication.version.1.type.prePromptResultRequest";
    public static final String E2SERVICE_AUTHENTICATION_POST_PROMPT_GUN = "com.hp.ext.types.authentication.version.1.type.postPromptResultRequest";
    public static final String E2SERVICE_AUTHENTICATION_SIGN_OUT_NOTIFICATION_GUN = "com.hp.ext.types.authentication.version.1.type.signoutNotificationRequest";


    @Before
    public void setUp() {
        messageHandler = new SystemManagementMessageHandler();
        deviceAuthService = new StandardDeviceAuthenticationService();
        mockCallback = mock(IDeviceAuthSessionChangeCallback.class);
    }

    @After
    public void tearDown() {
        messageHandler.shutdown();
    }

    @Test
    public void GivenStandardDeviceAuthenticationService_WhenRegisterAuthSessionChangeCallbackCalledAndLogInEventOccurs_ThenCallbackShouldBeInvoked() {
        deviceAuthService.registerAuthSessionChangeCallback(mockCallback);

        messageHandler.onReceived(1, "{\"systemManagement\": {\"details\": {\"authnSessionChange\": {\"event\": " +
                "\"asFrontPanelLogin\"}},\"traceId\": 15}}");

        verify(mockCallback, timeout(1000)).onSignIn();
        verify(mockCallback, never()).onSignOut();
    }

    @Test
    public void GivenStandardDeviceAuthenticationService_WhenRegisterAuthSessionChangeCallbackCalledAndLogOutEventOccurs_ThenCallbackShouldBeInvoked() {
        deviceAuthService.registerAuthSessionChangeCallback(mockCallback);

        messageHandler.onReceived(1, "{\"systemManagement\": {\"details\": {\"authnSessionChange\": {\"event\": " +
                "\"asFrontPanelLogout\"}},\"traceId\": 15}}");

        verify(mockCallback, timeout(1000)).onSignOut();
        verify(mockCallback, never()).onSignIn();
    }

    @Test
    public void GivenStandardDeviceAuthenticationService_WhenUnRegisterAuthSessionChangeCallbackCalledAndLogInEventOccurs_ThenCallbackShouldNotBeInvoked() {
        deviceAuthService.registerAuthSessionChangeCallback(mockCallback);
        deviceAuthService.unRegisterAuthSessionChangeCallback();

        messageHandler.onReceived(1, "{\"systemManagement\": {\"details\": {\"authnSessionChange\": {\"event\": " +
                "\"asFrontPanelLogin\"}},\"traceId\": 15}}");

        verify(mockCallback, never()).onSignIn();
    }

    @Test
    public void GivenStandardDeviceAuthenticationService_WhenPrePromptEventOccurs_ThenCallbackShouldBeInvoked() throws IOException {
        String channelId = "2777402e-86d1-4a4e-8aed-79c77ccbccdf";
        String authnTarget = "com.hp.workpath.sample.authenticationagentsample";
        CountDownLatch latch = new CountDownLatch(1);

        // 1. Define test callback
        IE2ServiceCallback callback = (appPackageId, serviceMessage) -> {
            latch.countDown();
            // When the callback is invoked, return a predefined response.
            return createAppChannelResponseForPrePromptResult(SERVICE_CALL_ID);
        };

        // 2. Register callback
        StandardDeviceAuthenticationService authenticationService =
                new StandardDeviceAuthenticationService(mockDeviceManagementService);
        authenticationService.registerPrePromptCallback(callback);

        // 3. Simulate event
        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWsCallbackService);
        if (!TextUtils.isEmpty(authnTarget)) {
            handler.onReceived(0, makeTestChannelSetupMessage(channelId, authnTarget,
                    StandardDeviceAuthenticationService.E2SERVICE_AUTHENTICATION_CANONICAL_GUN,
                    StandardDeviceAuthenticationService.E2SERVICE_AUTHENTICATION_CANONICAL_GUN));
        }
        handler.onReceived(0, makeTestAppChannelServiceMessage(channelId, SERVICE_CALL_ID, E2SERVICE_AUTHENTICATION_PRE_PROMPT_GUN, "prePromptResult"));

        // 4. Wait for callback invocation
        try {
            boolean result = latch.await(1, TimeUnit.SECONDS);
            assertTrue("Callback was not invoked within the timeout period.", result);
            Future<?> future = AppChannelServiceThreadPool.getFuture(SERVICE_CALL_ID);
            if (future != null) {
                future.get(1, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            fail("Exception occurred while waiting for latch: " + e.getMessage());
        }

        // 5. Verify the result (JSON object comparison)
        try {
            String expectedResponseStr = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "authentication/ExpectResult_prePromptResult.json");
            ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
            verify(mockWsCallbackService).sendMessage(anyInt(), messageCaptor.capture());
            String actualResponseStr = messageCaptor.getValue();

            // Parse JSON strings into JsonObjects for comparison
            JsonObject expectedJson = StandardJsonParser.INSTANCE.fromJson(expectedResponseStr, JsonObject.class);
            JsonObject actualJson = StandardJsonParser.INSTANCE.fromJson(actualResponseStr, JsonObject.class);

            assertEquals("The response JSON does not match the expected JSON.", expectedJson, actualJson);
            assertFalse("Thread pool should not be running after completion.", AppChannelServiceThreadPool.isRunning(SERVICE_CALL_ID));
        } catch (Exception e) {
            fail("Exception occurred while verifying the message: " + e.getMessage());
        }
    }

    @Test
    public void GivenStandardDeviceAuthenticationService_WhenPostPromptEventOccurs_ThenCallbackShouldBeInvoked() throws IOException {
        String channelId = "2777402e-86d1-4a4e-8aed-79c77ccbccdf";
        String authnTarget = "com.hp.workpath.sample.authenticationagentsample/com.hp.workpath.sample" +
                ".authenticationagentsample.MainActivity";

        CountDownLatch latch = new CountDownLatch(1);

        // Define test callback
        IE2ServiceCallback callback = (appPackageId, serviceMessage) -> {
            latch.countDown();
            return createAppChannelResponseForPostPromptResult("81c7d95d-1da8-4b19-a70f-a4423267e385");
        };

        // Register callback
        StandardDeviceAuthenticationService authenticationService =
                new StandardDeviceAuthenticationService(mockDeviceManagementService);
        authenticationService.registerPostPromptCallback(callback);

        // Test for valid channel payload message for notification
        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWsCallbackService);
        if (!TextUtils.isEmpty(authnTarget)) {
            handler.onReceived(0, makeTestChannelSetupMessage(channelId, authnTarget,
                    StandardDeviceAuthenticationService.E2SERVICE_AUTHENTICATION_CANONICAL_GUN,
                    StandardDeviceAuthenticationService.E2SERVICE_AUTHENTICATION_CANONICAL_GUN));
        }
        handler.onReceived(0, makeTestAppChannelServiceMessage(channelId, SERVICE_CALL_ID, E2SERVICE_AUTHENTICATION_POST_PROMPT_GUN, "postPromptResult"));


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
            String expectedResponse = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "authentication/ExpectResult_postPromptResult.json").trim();
            ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
            verify(mockWsCallbackService).sendMessage(anyInt(), messageCaptor.capture());
            assertEquals("response", expectedResponse, messageCaptor.getValue());
            assertFalse("isRunning after finished", AppChannelServiceThreadPool.isRunning(SERVICE_CALL_ID));
        } catch (Exception e) {
            fail("Exception occurred while verifying message" + e);
        }
    }

    @Test
    public void GivenStandardDeviceAuthenticationService_WhenPrePromptEventOccursWithSuccess_ThenCallbackShouldBeInvoked() throws IOException {
        String channelId = "2777402e-86d1-4a4e-8aed-79c77ccbccdf";
        String authnTarget = "com.hp.workpath.sample.authenticationagentsample/.MainActivity";
        CountDownLatch latch = new CountDownLatch(1);

        // Define test callback
        IE2ServiceCallback callback = (appPackageId, serviceMessage) -> {
            latch.countDown();
            return createAppChannelResponseForPrePromptResult_Success(SERVICE_CALL_ID);
        };

        // Register callback
        StandardDeviceAuthenticationService authenticationService =
                new StandardDeviceAuthenticationService(mockDeviceManagementService);
        authenticationService.registerPrePromptCallback(callback);

        // Simulate event
        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWsCallbackService);
        if (!TextUtils.isEmpty(authnTarget)) {
            handler.onReceived(0, makeTestChannelSetupMessage(channelId, authnTarget,
                    StandardDeviceAuthenticationService.E2SERVICE_AUTHENTICATION_CANONICAL_GUN,
                    StandardDeviceAuthenticationService.E2SERVICE_AUTHENTICATION_CANONICAL_GUN));
        }
        handler.onReceived(0, makeTestAppChannelServiceMessage(channelId, SERVICE_CALL_ID, E2SERVICE_AUTHENTICATION_PRE_PROMPT_GUN, "prePromptResult"));

        // Wait for callback invocation
        try {
            boolean result = latch.await(1, TimeUnit.SECONDS);
            assertTrue("Callback was not invoked within the timeout period.", result);
            Future<?> future = AppChannelServiceThreadPool.getFuture(SERVICE_CALL_ID);
            if (future != null) {
                future.get(1, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            fail("Exception occurred while waiting for latch: " + e.getMessage());
        }

        // Verify the result (JSON object comparison)
        try {
            ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
            verify(mockWsCallbackService).sendMessage(anyInt(), messageCaptor.capture());
            String actualResponseStr = messageCaptor.getValue();

            // Parse JSON strings into JsonObjects for comparison
            String expectedResponseStr = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "authentication/ExpectResult_prePromptResult_Success.json");
            JsonObject expectedJson = StandardJsonParser.INSTANCE.fromJson(expectedResponseStr, JsonObject.class);
            JsonObject actualJson = StandardJsonParser.INSTANCE.fromJson(actualResponseStr, JsonObject.class);

            assertEquals("The response JSON does not match the expected JSON.", expectedJson, actualJson);
            assertFalse("Thread pool should not be running after completion.", AppChannelServiceThreadPool.isRunning(SERVICE_CALL_ID));
        } catch (Exception e) {
            fail("Exception occurred while verifying the message: " + e.getMessage());
        }
    }

    @Test
    public void GivenStandardDeviceAuthenticationService_WhenPostPromptEventOccursWithSuccess_ThenCallbackShouldBeInvoked() throws IOException {
        String channelId = "2777402e-86d1-4a4e-8aed-79c77ccbccdf";
        String authnTarget = "com.hp.workpath.sample.authenticationagentsample";
        CountDownLatch latch = new CountDownLatch(1);

        // Define test callback
        IE2ServiceCallback callback = (appPackageId, serviceMessage) -> {
            latch.countDown();
            return createAppChannelResponseForPostPromptResult_Success(SERVICE_CALL_ID);
        };

        // Register callback
        StandardDeviceAuthenticationService authenticationService =
                new StandardDeviceAuthenticationService(mockDeviceManagementService);
        authenticationService.registerPostPromptCallback(callback);

        // Simulate event
        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWsCallbackService);
        if (!TextUtils.isEmpty(authnTarget)) {
            handler.onReceived(0, makeTestChannelSetupMessage(channelId, authnTarget,
                    StandardDeviceAuthenticationService.E2SERVICE_AUTHENTICATION_CANONICAL_GUN,
                    StandardDeviceAuthenticationService.E2SERVICE_AUTHENTICATION_CANONICAL_GUN));
        }
        handler.onReceived(0, makeTestAppChannelServiceMessage(channelId, SERVICE_CALL_ID, E2SERVICE_AUTHENTICATION_POST_PROMPT_GUN, "postPromptResult"));

        // Wait for callback invocation
        try {
            boolean result = latch.await(1, TimeUnit.SECONDS);
            assertTrue("Callback was not invoked within the timeout period.", result);
            Future<?> future = AppChannelServiceThreadPool.getFuture(SERVICE_CALL_ID);
            if (future != null) {
                future.get(1, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            fail("Exception occurred while waiting for latch: " + e.getMessage());
        }

        // Verify the result (JSON object comparison)
        try {
            ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
            verify(mockWsCallbackService).sendMessage(anyInt(), messageCaptor.capture());
            String actualResponseStr = messageCaptor.getValue();

            // Parse JSON strings into JsonObjects for comparison
            String expectedResponseStr = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "authentication/ExpectResult_postPromptResult_Success.json");
            JsonObject expectedJson = StandardJsonParser.INSTANCE.fromJson(expectedResponseStr, JsonObject.class);
            JsonObject actualJson = StandardJsonParser.INSTANCE.fromJson(actualResponseStr, JsonObject.class);

            assertEquals("The response JSON does not match the expected JSON.", expectedJson, actualJson);
            assertFalse("Thread pool should not be running after completion.", AppChannelServiceThreadPool.isRunning(SERVICE_CALL_ID));
        } catch (Exception e) {
            fail("Exception occurred while verifying the message: " + e.getMessage());
        }
    }

    @Test
    public void GivenStandardDeviceAuthenticationService_WhenPrePromptEventOccursWithCanceled_ThenCallbackShouldBeInvoked() throws IOException {
        String channelId = "2777402e-86d1-4a4e-8aed-79c77ccbccdf";
        String authnTarget = "com.hp.workpath.sample.authenticationagentsample/.MainActivity";
        CountDownLatch latch = new CountDownLatch(1);

        // Define test callback
        IE2ServiceCallback callback = (appPackageId, serviceMessage) -> {
            latch.countDown();
            return createAppChannelResponseForPrePromptResult_Canceled(SERVICE_CALL_ID);
        };

        // Register callback
        StandardDeviceAuthenticationService authenticationService =
                new StandardDeviceAuthenticationService(mockDeviceManagementService);
        authenticationService.registerPrePromptCallback(callback);

        // Simulate event
        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWsCallbackService);
        if (!TextUtils.isEmpty(authnTarget)) {
            handler.onReceived(0, makeTestChannelSetupMessage(channelId, authnTarget,
                    StandardDeviceAuthenticationService.E2SERVICE_AUTHENTICATION_CANONICAL_GUN,
                    StandardDeviceAuthenticationService.E2SERVICE_AUTHENTICATION_CANONICAL_GUN));
        }
        handler.onReceived(0, makeTestAppChannelServiceMessage(channelId, SERVICE_CALL_ID, E2SERVICE_AUTHENTICATION_PRE_PROMPT_GUN, "prePromptResult"));

        // Wait for callback invocation
        try {
            boolean result = latch.await(1, TimeUnit.SECONDS);
            assertTrue("Callback was not invoked within the timeout period.", result);
            Future<?> future = AppChannelServiceThreadPool.getFuture(SERVICE_CALL_ID);
            if (future != null) {
                future.get(1, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            fail("Exception occurred while waiting for latch: " + e.getMessage());
        }

        // Verify the result (JSON object comparison)
        try {
            ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
            verify(mockWsCallbackService).sendMessage(anyInt(), messageCaptor.capture());
            String actualResponseStr = messageCaptor.getValue();

            // Parse JSON strings into JsonObjects for comparison
            String expectedResponseStr = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "authentication/ExpectResult_prePromptResult_Canceled.json");
            JsonObject expectedJson = StandardJsonParser.INSTANCE.fromJson(expectedResponseStr, JsonObject.class);
            JsonObject actualJson = StandardJsonParser.INSTANCE.fromJson(actualResponseStr, JsonObject.class);

            assertEquals("The response JSON does not match the expected JSON.", expectedJson, actualJson);
            assertFalse("Thread pool should not be running after completion.", AppChannelServiceThreadPool.isRunning(SERVICE_CALL_ID));
        } catch (Exception e) {
            fail("Exception occurred while verifying the message: " + e.getMessage());
        }
    }

    @Test
    public void GivenStandardDeviceAuthenticationService_WhenPrePromptEventOccursWithFailed_ThenCallbackShouldBeInvoked() throws IOException {
        String channelId = "2777402e-86d1-4a4e-8aed-79c77ccbccdf";
        String authnTarget = "com.hp.workpath.sample.authenticationagentsample/com.hp.workpath.sample.authenticationagentsample.MainActivity";
        CountDownLatch latch = new CountDownLatch(1);

        // Define test callback
        IE2ServiceCallback callback = (appPackageId, serviceMessage) -> {
            latch.countDown();
            return createAppChannelResponseForPrePromptResult_Failed(SERVICE_CALL_ID);
        };

        // Register callback
        StandardDeviceAuthenticationService authenticationService =
                new StandardDeviceAuthenticationService(mockDeviceManagementService);
        authenticationService.registerPrePromptCallback(callback);

        // Simulate event
        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWsCallbackService);
        if (!TextUtils.isEmpty(authnTarget)) {
            handler.onReceived(0, makeTestChannelSetupMessage(channelId, authnTarget,
                    StandardDeviceAuthenticationService.E2SERVICE_AUTHENTICATION_CANONICAL_GUN,
                    StandardDeviceAuthenticationService.E2SERVICE_AUTHENTICATION_CANONICAL_GUN));
        }
        handler.onReceived(0, makeTestAppChannelServiceMessage(channelId, SERVICE_CALL_ID, E2SERVICE_AUTHENTICATION_PRE_PROMPT_GUN, "prePromptResult"));

        // Wait for callback invocation
        try {
            boolean result = latch.await(1, TimeUnit.SECONDS);
            assertTrue("Callback was not invoked within the timeout period.", result);
            Future<?> future = AppChannelServiceThreadPool.getFuture(SERVICE_CALL_ID);
            if (future != null) {
                future.get(1, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            fail("Exception occurred while waiting for latch: " + e.getMessage());
        }

        // Verify the result (JSON object comparison)
        try {
            ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
            verify(mockWsCallbackService).sendMessage(anyInt(), messageCaptor.capture());
            String actualResponseStr = messageCaptor.getValue();

            // Parse JSON strings into JsonObjects for comparison
            String expectedResponseStr = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "authentication/ExpectResult_prePromptResult_Failed.json");
            JsonObject expectedJson = StandardJsonParser.INSTANCE.fromJson(expectedResponseStr, JsonObject.class);
            JsonObject actualJson = StandardJsonParser.INSTANCE.fromJson(actualResponseStr, JsonObject.class);

            assertEquals("The response JSON does not match the expected JSON.", expectedJson, actualJson);
            assertFalse("Thread pool should not be running after completion.", AppChannelServiceThreadPool.isRunning(SERVICE_CALL_ID));
        } catch (Exception e) {
            fail("Exception occurred while verifying the message: " + e.getMessage());
        }
    }

    @Test
    public void GivenStandardDeviceAuthenticationService_WhenPostPromptEventOccursWithFailed_ThenCallbackShouldBeInvoked() throws IOException {
        String channelId = "2777402e-86d1-4a4e-8aed-79c77ccbccdf";
        String authnTarget = "com.hp.workpath.sample.authenticationagentsample";
        CountDownLatch latch = new CountDownLatch(1);

        // Define test callback
        IE2ServiceCallback callback = (appPackageId, serviceMessage) -> {
            latch.countDown();
            return createAppChannelResponseForPostPromptResult_Failed(SERVICE_CALL_ID);
        };

        // Register callback
        StandardDeviceAuthenticationService authenticationService =
                new StandardDeviceAuthenticationService(mockDeviceManagementService);
        authenticationService.registerPostPromptCallback(callback);

        // Simulate event
        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWsCallbackService);
        if (!TextUtils.isEmpty(authnTarget)) {
            handler.onReceived(0, makeTestChannelSetupMessage(channelId, authnTarget,
                    StandardDeviceAuthenticationService.E2SERVICE_AUTHENTICATION_CANONICAL_GUN,
                    StandardDeviceAuthenticationService.E2SERVICE_AUTHENTICATION_CANONICAL_GUN));
        }
        handler.onReceived(0, makeTestAppChannelServiceMessage(channelId, SERVICE_CALL_ID, E2SERVICE_AUTHENTICATION_POST_PROMPT_GUN, "postPromptResult"));

        // Wait for callback invocation
        try {
            boolean result = latch.await(1, TimeUnit.SECONDS);
            assertTrue("Callback was not invoked within the timeout period.", result);
            Future<?> future = AppChannelServiceThreadPool.getFuture(SERVICE_CALL_ID);
            if (future != null) {
                future.get(1, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            fail("Exception occurred while waiting for latch: " + e.getMessage());
        }

        // Verify the result (JSON object comparison)
        try {
            ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
            verify(mockWsCallbackService).sendMessage(anyInt(), messageCaptor.capture());
            String actualResponseStr = messageCaptor.getValue();

            // Parse JSON strings into JsonObjects for comparison
            String expectedResponseStr = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "authentication/ExpectResult_postPromptResult_Failed.json");
            JsonObject expectedJson = StandardJsonParser.INSTANCE.fromJson(expectedResponseStr, JsonObject.class);
            JsonObject actualJson = StandardJsonParser.INSTANCE.fromJson(actualResponseStr, JsonObject.class);

            assertEquals("The response JSON does not match the expected JSON.", expectedJson, actualJson);
            assertFalse("Thread pool should not be running after completion.", AppChannelServiceThreadPool.isRunning(SERVICE_CALL_ID));
        } catch (Exception e) {
            fail("Exception occurred while verifying the message: " + e.getMessage());
        }
    }

    @Test
    public void GivenStandardDeviceAuthenticationService_WhenSignInEventOccurs_ThenCallbackShouldBeInvoked() throws IOException {
        CountDownLatch latch = new CountDownLatch(1);

        // Define test callback
        IDeviceAuthSessionChangeCallback callback = new IDeviceAuthSessionChangeCallback() {
            @Override
            public void onSignIn() {
                latch.countDown();
            }

            @Override
            public void onSignOut() {

            }
        };

        // Register callback
        StandardDeviceAuthenticationService authenticationService =
                new StandardDeviceAuthenticationService(mockDeviceManagementService);
        authenticationService.registerAuthSessionChangeCallback(callback);

        // Simulate event
        SystemManagementMessageHandler handler = new SystemManagementMessageHandler();
        handler.onReceived(0, "{\"systemManagement\":{\"details\":{\"authnSessionChange\":{\"event\":\"asFrontPanelLogin\"}},\"traceId\":40}}");

        // Wait for callback invocation
        try {
            boolean result = latch.await(1, TimeUnit.SECONDS);
            assertTrue("Callback was not invoked within the timeout period.", result);
            Future<?> future = AppChannelServiceThreadPool.getFuture(SERVICE_CALL_ID);
            if (future != null) {
                future.get(1, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            fail("Exception occurred while waiting for latch: " + e.getMessage());
        }
    }

    @Test
    public void GivenStandardDeviceAuthenticationService_WhenSignOutEventOccurs_ThenCallbackShouldBeInvoked() throws IOException {
        String channelId = "2777402e-86d1-4a4e-8aed-79c77ccbccdf";
        String authnTarget = "com.hp.workpath.sample.authenticationagentsample";
        CountDownLatch latch = new CountDownLatch(1);

        // Define test callback
        IE2ServiceCallback callback = (appPackageId, serviceMessage) -> {
            latch.countDown();
            return new AppChannelServiceResponse(SERVICE_CALL_ID, 200);
        };

        // Register callback
        StandardDeviceAuthenticationService authenticationService =
                new StandardDeviceAuthenticationService(mockDeviceManagementService);
        authenticationService.registerSignOutNotificationCallback(callback);

        // Simulate event
        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWsCallbackService);
        if (!TextUtils.isEmpty(authnTarget)) {
            handler.onReceived(0, makeTestChannelSetupMessage(channelId, authnTarget,
                    StandardDeviceAuthenticationService.E2SERVICE_AUTHENTICATION_CANONICAL_GUN,
                    StandardDeviceAuthenticationService.E2SERVICE_AUTHENTICATION_CANONICAL_GUN));
        }
        handler.onReceived(0, makeTestAppChannelServiceMessage(channelId, SERVICE_CALL_ID, E2SERVICE_AUTHENTICATION_SIGN_OUT_NOTIFICATION_GUN, "signoutNotification"));

        // Wait for callback invocation
        try {
            boolean result = latch.await(1, TimeUnit.SECONDS);
            assertTrue("Callback was not invoked within the timeout period.", result);
            Future<?> future = AppChannelServiceThreadPool.getFuture(SERVICE_CALL_ID);
            if (future != null) {
                future.get(1, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            fail("Exception occurred while waiting for latch: " + e.getMessage());
        }
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
            fail("Exception occurred while creating AppChannelServiceResponse" + e);
        }
        return response;
    }

    private AppChannelServiceResponse createAppChannelResponseForPostPromptResult(String serviceCallId) {
        AppChannelServiceResponse response = new AppChannelServiceResponse(serviceCallId, 200);
        try {
            PostPromptResult postPromptResult = new PostPromptResult();
            postPromptResult.setAction(new PromptResultAction(new NonAction()));
            postPromptResult.setResult(new PostPromptResultValue(new AuthenticationCanceled()));
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            String postPromptResultString = objectMapper.writeValueAsString(postPromptResult);
            JsonTypedObject responseBody = new JsonTypedObject(postPromptResult.getTypeGUN());
            responseBody.setValue(StandardJsonParser.INSTANCE.fromJson(postPromptResultString, JsonObject.class));
            response.setResponseBody(responseBody);
        } catch (JsonProcessingException e) {
            fail("Exception occurred while creating AppChannelServiceResponse" + e);
        }
        return response;
    }

    private AppChannelServiceResponse createAppChannelResponseForPrePromptResult_Success(String serviceCallId) {
        AppChannelServiceResponse response = new AppChannelServiceResponse(serviceCallId, 200);
        try {
            PrePromptResult prePromptResult = new PrePromptResult();
            prePromptResult.setAction(new PromptResultAction(new NonAction()));
            PrePromptResultValue resultValue = new PrePromptResultValue();
            resultValue.setSucceeded(new AuthenticationSuccess());
            prePromptResult.setResult(resultValue);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            String prePromptResultString = objectMapper.writeValueAsString(prePromptResult);
            JsonTypedObject responseBody = new JsonTypedObject(prePromptResult.getTypeGUN());
            responseBody.setValue(StandardJsonParser.INSTANCE.fromJson(prePromptResultString, JsonObject.class));
            response.setResponseBody(responseBody);
        } catch (JsonProcessingException e) {
            fail("Exception occurred while creating AppChannelServiceResponse" + e);
        }
        return response;
    }

    private AppChannelServiceResponse createAppChannelResponseForPostPromptResult_Success(String serviceCallId) {
        AppChannelServiceResponse response = new AppChannelServiceResponse(serviceCallId, 200);
        try {
            PostPromptResult postPromptResult = new PostPromptResult();
            postPromptResult.setAction(new PromptResultAction(new NonAction()));
            PostPromptResultValue resultValue = new PostPromptResultValue();
            resultValue.setSucceeded(new AuthenticationSuccess());
            postPromptResult.setResult(resultValue);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            String postPromptResultString = objectMapper.writeValueAsString(postPromptResult);
            JsonTypedObject responseBody = new JsonTypedObject(postPromptResult.getTypeGUN());
            responseBody.setValue(StandardJsonParser.INSTANCE.fromJson(postPromptResultString, JsonObject.class));
            response.setResponseBody(responseBody);
        } catch (JsonProcessingException e) {
            fail("Exception occurred while creating AppChannelServiceResponse" + e);
        }
        return response;
    }

    private AppChannelServiceResponse createAppChannelResponseForPrePromptResult_Canceled(String serviceCallId) {
        AppChannelServiceResponse response = new AppChannelServiceResponse(serviceCallId, 200);
        try {
            PrePromptResult prePromptResult = new PrePromptResult();
            prePromptResult.setAction(new PromptResultAction(new NonAction()));
            PrePromptResultValue resultValue = new PrePromptResultValue();
            resultValue.setCanceled(new AuthenticationCanceled());
            prePromptResult.setResult(resultValue);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            String prePromptResultString = objectMapper.writeValueAsString(prePromptResult);
            JsonTypedObject responseBody = new JsonTypedObject(prePromptResult.getTypeGUN());
            responseBody.setValue(StandardJsonParser.INSTANCE.fromJson(prePromptResultString, JsonObject.class));
            response.setResponseBody(responseBody);
        } catch (JsonProcessingException e) {
            fail("Exception occurred while creating AppChannelServiceResponse" + e);
        }
        return response;
    }

    private AppChannelServiceResponse createAppChannelResponseForPrePromptResult_Failed(String serviceCallId) {
        AppChannelServiceResponse response = new AppChannelServiceResponse(serviceCallId, 200);
        try {
            PrePromptResult prePromptResult = new PrePromptResult();
            prePromptResult.setAction(new PromptResultAction(new NonAction()));
            PrePromptResultValue resultValue = new PrePromptResultValue();
            resultValue.setFailed(new AuthenticationFailed());
            prePromptResult.setResult(resultValue);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            String prePromptResultString = objectMapper.writeValueAsString(prePromptResult);
            JsonTypedObject responseBody = new JsonTypedObject(prePromptResult.getTypeGUN());
            responseBody.setValue(StandardJsonParser.INSTANCE.fromJson(prePromptResultString, JsonObject.class));
            response.setResponseBody(responseBody);
        } catch (JsonProcessingException e) {
            fail("Exception occurred while creating AppChannelServiceResponse" + e);
        }
        return response;
    }

    private AppChannelServiceResponse createAppChannelResponseForPostPromptResult_Failed(String serviceCallId) {
        AppChannelServiceResponse response = new AppChannelServiceResponse(serviceCallId, 200);
        try {
            PostPromptResult postPromptResult = new PostPromptResult();
            postPromptResult.setAction(new PromptResultAction(new NonAction()));
            PostPromptResultValue resultValue = new PostPromptResultValue();
            resultValue.setFailed(new AuthenticationFailed());
            postPromptResult.setResult(resultValue);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            String postPromptResultString = objectMapper.writeValueAsString(postPromptResult);
            JsonTypedObject responseBody = new JsonTypedObject(postPromptResult.getTypeGUN());
            responseBody.setValue(StandardJsonParser.INSTANCE.fromJson(postPromptResultString, JsonObject.class));
            response.setResponseBody(responseBody);
        } catch (JsonProcessingException e) {
            fail("Exception occurred while creating AppChannelServiceResponse" + e);
        }
        return response;
    }

    public static String makeTestAppChannelServiceMessage(String channelId, String serviceCallId, String typeGun, String path) {
        return "{\n" +
                "    \"channelMessage\": {\n" +
                "        \"channelId\": \"" + channelId + "\",\n" +
                "        \"message\": {\n" +
                "            \"service\": {\n" +
                "                \"attachments\": [],\n" +
                "                \"httpMethod\": \"POST\",\n" +
                "                \"path\": \"" + path + "\",\n" +
                "                \"requestBody\": {\n" +
                "                    \"typeGUN\": \"" + typeGun + "\",\n" +
                "                    \"value\": {\n" +
                "                        \"languageCode\": \"en\",\n" +
                "                        \"sessionAccessToken\": \"eyJhbGciOiAiZGlyIiwgImVuYyI6ICJBMTI4R0NNIiwgIk9BVVRIMlNUQU5EQVJEX0tFWV9JRCI6ICJ7NDNhNGJjZjctNjZkYi00MTY2LTgwZGQtN2NlOTkzNzhhNzUyfSJ9..AYQ1dpmoRntmEt4s.yIw2kqTBRFWbnP-Ef_orow-ZvqxnRdBl3JWCrYwzSYgTkwOypS-oh4D-lbwhiuxu9y0TpuYihNZFuzT0N6Est8f8X5_fZjqjnM-CbyZfO5zsyx3_E6o6OxmTETFzbdpnqI7c9Qbsf3c-H_em2QntC13kTpIZ48zza5ZWoyUmt0qgJ-GZpZy6zPt0Zc9avpez5YQZga4jOPyuVdYnZ64qZS6rOmQw2NGWxeyWqCNPNtl2YUuBN8gELNLfh_uHbAnV0bXcA3rvnLd4GUxGu5P41URjL6sXdtCK4kGkHA_O2ww7Vjw0lccUabrkmL6WSs1R01I2nivTfmZ712xCTC3DqdlFuttiRRJbT4HxdULWAQFKzSjMW3Mdx5h7JuKOoudC8rplTkkFNJTWhjDAyIcZ015FzfuUA5eeZkkHco_2n52j7bwq11KCu84oh9uYjH_RPDVMQUReVCuzYgl7WiSNMyej5boO7KAKwrC8Id__QgSsOggbhGVPfBYFpLmKUwkOyvgBS9xrz2MN73Y6eNy6E2pn3NO-5kpX3xvGVxhEi_9jSb7soR2mmgfW0_0qMljg6fOh8-M_gepDxWaeiGUVsHJkPcjv1_9FpXCVEs-kHvMh9C_guEhMWZNgCXRUwfby2b0Bz00GLrzrvBrfObFXBOOAR7ysSPMbfdl-jxn2MKiwq9fcTOEPk-IrWybWHM6W.NK5uSYZIl9BunjwXmM7rpQ\",\n" +
                "                        \"sessionId\": \"4cb1f5ec-aa0a-4560-971c-c073b89f5b59\"\n" +
                "                    }\n" +
                "                },\n" +
                "                \"serviceCallId\": \"" + serviceCallId + "\"\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";
    }
}
