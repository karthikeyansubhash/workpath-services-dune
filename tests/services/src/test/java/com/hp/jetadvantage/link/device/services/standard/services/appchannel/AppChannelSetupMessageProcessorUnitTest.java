package com.hp.jetadvantage.link.device.services.standard.services.appchannel;

import static com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelMessageTestHelper.createPayloadMessage;
import static com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelMessageTestHelper.makeTestChannelSetupMessage;
import static com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelMessageTestHelper.makeTestChannelTeardownMessage;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

import android.os.RemoteException;

import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardJsonParser;
import com.hp.jetadvantage.link.device.services.standard.services.StandardWebsocketCallbackService;
import com.hp.ws.websocket.AppChannelMessage;
import com.hp.ws.websocket.AppChannelSetup;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AppChannelSetupMessageProcessorUnitTest {
    public static final String VALID_CHANNEL_ID = "223762e2-8bf7-4e29-a1d7-0790103e83a1";
    public static final String VALID_CHANNEL_ID2 = "223762e2-8bf7-4e29-a1d7-0790103e83a2";
    public static final String VALID_CHANNEL_ID3 = "223762e2-8bf7-4e29-a1d7-0790103e83a3";

    public static final String TEST_PAYLOAD_TYPE_USB = "com.hp.ext.service.usbAccessories.version.1.type" +
            ".usbRegistrationPayload";
    public static final String TEST_PAYLOAD_TYPE_SCAN = "com.hp.ext.service.scanJob.version.1.type.scanNotification";
    public static final String TEST_PAYLOAD_TYPE_COPY = "com.hp.ext.service.copy.version.1.type.copyNotification";

    public static final String TEST_SERVICE_TYPE_USB = "com.hp.ext.service.usbAccessories.version.1";
    public static final String TEST_SERVICE_TYPE_SCAN = "com.hp.ext.service.scanJob.version.1";
    public static final String TEST_SERVICE_TYPE_COPY = "com.hp.ext.service.copy.version.1";

    @Mock
    StandardWebsocketCallbackService mockWsService;

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
        AppChannelCallbackRegistry.clear();
    }

    @Test
    public void GivenAppChannelSetupMessageProcessor_WhenCanProcessMessageCalled_ThenReturnsTrueOnlyForSetupMessages() {
        // Arrange
        AppChannelContext ctx = new AppChannelContext(mockWsService);
        AppChannelSetupMessageProcessor processor = new AppChannelSetupMessageProcessor(ctx);

        // Act & Assert: Setup message
        String data = makeTestChannelSetupMessage(VALID_CHANNEL_ID, "packageId", TEST_SERVICE_TYPE_USB,
                TEST_PAYLOAD_TYPE_USB);
        AppChannelMessage appChannelMessage = StandardJsonParser.INSTANCE.fromJson(data, AppChannelMessage.class);
        boolean result = processor.canProcessMessage(appChannelMessage.getChannelMessage());
        assertTrue("Should process setup message", result);

        // Act & Assert: Payload message
        AppChannelMessage.ChannelMessage payloadMessage = createPayloadMessage(VALID_CHANNEL_ID,
                TEST_PAYLOAD_TYPE_USB, "");
        result = processor.canProcessMessage(payloadMessage);
        assertFalse("Should not process payload message", result);

        // Act & Assert: Teardown message
        String tearDownStr = makeTestChannelTeardownMessage(VALID_CHANNEL_ID);
        AppChannelMessage teardownMessage = StandardJsonParser.INSTANCE.fromJson(tearDownStr, AppChannelMessage.class);
        result = processor.canProcessMessage(teardownMessage.getChannelMessage());
        assertFalse("Should not process teardown message", result);

        // Act & Assert: Null message
        result = processor.canProcessMessage(null);
        assertFalse("Should not process null message", result);
    }

    @Test
    public void GivenAppChannelSetupMessageProcessor_WhenProcessMessageCalled_ThenCallbackShouldBeInvoked() {
        // Arrange
        AppChannelContext ctx = new AppChannelContext(mockWsService);
        StandardDeviceService.ISetupCallback mockCallback =
                Mockito.mock(StandardDeviceService.ISetupCallback.class);
        AppChannelCallbackRegistry.registerSetupCallback(TEST_SERVICE_TYPE_USB, mockCallback);
        AppChannelSetupMessageProcessor processor = new AppChannelSetupMessageProcessor(ctx);

        String appPackageId = "com.hp.test.app";
        String data = makeTestChannelSetupMessage(VALID_CHANNEL_ID, appPackageId, TEST_SERVICE_TYPE_USB,
                TEST_PAYLOAD_TYPE_USB);
        AppChannelMessage channelSetupMessage = StandardJsonParser.INSTANCE.fromJson(data, AppChannelMessage.class);

        // Act
        processor.processMessage(VALID_CHANNEL_ID, channelSetupMessage.getChannelMessage());

        // Assert
        Mockito.verify(mockCallback, Mockito.times(1))
                .onSetupNotification(eq(appPackageId), any(AppChannelSetup.class));
    }

    @Test
    public void GivenAppChannelSetupMessageProcessor_WhenProcessMessageCalledWithExistingChannelId_ThenSendError() throws RemoteException {
        // Arrange
        AppChannelContext ctx = new AppChannelContext(mockWsService);
        StandardDeviceService.ISetupCallback mockCallback =
                Mockito.mock(StandardDeviceService.ISetupCallback.class);
        AppChannelCallbackRegistry.registerSetupCallback(TEST_SERVICE_TYPE_USB, mockCallback);
        AppChannelSetupMessageProcessor processor = new AppChannelSetupMessageProcessor(ctx);

        String appPackageId = "com.hp.test.app";
        String data = makeTestChannelSetupMessage(VALID_CHANNEL_ID, appPackageId, TEST_SERVICE_TYPE_USB,
                TEST_PAYLOAD_TYPE_USB);
        AppChannelMessage channelSetupMessage = StandardJsonParser.INSTANCE.fromJson(data, AppChannelMessage.class);

        // Act: First setup
        processor.processMessage(VALID_CHANNEL_ID, channelSetupMessage.getChannelMessage());
        Mockito.verify(mockCallback, Mockito.times(1))
                .onSetupNotification(eq(appPackageId), any(AppChannelSetup.class));
        Mockito.reset(mockCallback);

        // Act: Duplicate setup
        processor.processMessage(VALID_CHANNEL_ID, channelSetupMessage.getChannelMessage());

        // Assert
        Mockito.verify(mockWsService, Mockito.times(1))
                .sendMessage(eq(0), anyString());
        Mockito.verify(mockCallback, Mockito.times(0))
                .onSetupNotification(eq(appPackageId), any(AppChannelSetup.class));
    }

    @Test
    public void GivenAppChannelSetupMessageProcessor_WhenRegisterSetupCallbackAfterProcessingSetup_ThenCallbackShouldBeInvoked() {
        // Arrange
        AppChannelContext ctx = new AppChannelContext(mockWsService);
        AppChannelSetupMessageProcessor processor = new AppChannelSetupMessageProcessor(ctx);

        String appPackageId = "com.hp.test.app";
        String data = makeTestChannelSetupMessage(VALID_CHANNEL_ID, appPackageId, TEST_SERVICE_TYPE_USB,
                TEST_PAYLOAD_TYPE_USB);
        AppChannelMessage channelSetupMessage = StandardJsonParser.INSTANCE.fromJson(data, AppChannelMessage.class);

        // Act: Process setup before callback registration
        processor.processMessage(VALID_CHANNEL_ID, channelSetupMessage.getChannelMessage());

        StandardDeviceService.ISetupCallback mockCallback =
                Mockito.mock(StandardDeviceService.ISetupCallback.class);
        AppChannelCallbackRegistry.registerSetupCallback(TEST_SERVICE_TYPE_USB, mockCallback);

        // Assert
        Mockito.verify(mockCallback, Mockito.times(1))
                .onSetupNotification(eq(appPackageId), any(AppChannelSetup.class));
    }

    @Test
    public void GivenAppChannelSetupMessageProcessor_WhenProcessMessageWithDifferentServiceType_ThenCallbackShouldNotBeInvoked() {
        // Arrange
        AppChannelContext ctx = new AppChannelContext(mockWsService);
        AppChannelSetupMessageProcessor processor = new AppChannelSetupMessageProcessor(ctx);
        StandardDeviceService.ISetupCallback mockCallback =
                Mockito.mock(StandardDeviceService.ISetupCallback.class);
        AppChannelCallbackRegistry.registerSetupCallback(TEST_SERVICE_TYPE_SCAN, mockCallback);

        String appPackageId = "com.hp.test.app";
        String data = makeTestChannelSetupMessage(VALID_CHANNEL_ID, appPackageId, TEST_SERVICE_TYPE_USB,
                TEST_PAYLOAD_TYPE_USB);
        AppChannelMessage channelSetupMessage = StandardJsonParser.INSTANCE.fromJson(data, AppChannelMessage.class);

        // Act
        processor.processMessage(VALID_CHANNEL_ID, channelSetupMessage.getChannelMessage());

        // Assert
        Mockito.verify(mockCallback, Mockito.times(0))
                .onSetupNotification(eq(appPackageId), any(AppChannelSetup.class));
    }

    @Test
    public void GivenAppChannelSetupMessageProcessor_WhenRegisterSetupCallbackAfterProcessingSetupWithDifferentServiceType_ThenCallbackShouldNotBeInvoked() {
        // Arrange
        AppChannelContext ctx = new AppChannelContext(mockWsService);
        AppChannelSetupMessageProcessor processor = new AppChannelSetupMessageProcessor(ctx);

        String appPackageId = "com.hp.test.app";
        String data = makeTestChannelSetupMessage(VALID_CHANNEL_ID, appPackageId, TEST_SERVICE_TYPE_USB,
                TEST_PAYLOAD_TYPE_USB);
        AppChannelMessage channelSetupMessage = StandardJsonParser.INSTANCE.fromJson(data, AppChannelMessage.class);

        // Act: Process setup before callback registration
        processor.processMessage(VALID_CHANNEL_ID, channelSetupMessage.getChannelMessage());

        StandardDeviceService.ISetupCallback mockCallback =
                Mockito.mock(StandardDeviceService.ISetupCallback.class);
        AppChannelCallbackRegistry.registerSetupCallback(TEST_SERVICE_TYPE_SCAN, mockCallback);

        // Assert
        Mockito.verify(mockCallback, Mockito.times(0))
                .onSetupNotification(eq(appPackageId), any(AppChannelSetup.class));
    }

    @Test
    public void GivenAppChannelSetupMessageProcessor_WhenMultipleChannelsSetup_ThenCallbackShouldBeInvoked() {
        // Arrange
        AppChannelContext ctx = new AppChannelContext(mockWsService);
        AppChannelSetupMessageProcessor processor = new AppChannelSetupMessageProcessor(ctx);
        StandardDeviceService.ISetupCallback mockCallback =
                Mockito.mock(StandardDeviceService.ISetupCallback.class);
        AppChannelCallbackRegistry.registerSetupCallback(TEST_SERVICE_TYPE_USB, mockCallback);

        String appPackageId = "com.hp.test.app";
        String data = makeTestChannelSetupMessage(VALID_CHANNEL_ID, appPackageId, TEST_SERVICE_TYPE_USB,
                TEST_PAYLOAD_TYPE_USB);
        AppChannelMessage channelSetupMessage = StandardJsonParser.INSTANCE.fromJson(data, AppChannelMessage.class);

        data = makeTestChannelSetupMessage(VALID_CHANNEL_ID2, appPackageId, TEST_SERVICE_TYPE_SCAN,
                TEST_PAYLOAD_TYPE_SCAN);
        AppChannelMessage channelSetupMessage2 = StandardJsonParser.INSTANCE.fromJson(data, AppChannelMessage.class);

        data = makeTestChannelSetupMessage(VALID_CHANNEL_ID3, appPackageId, TEST_SERVICE_TYPE_COPY,
                TEST_PAYLOAD_TYPE_COPY);
        AppChannelMessage channelSetupMessage3 = StandardJsonParser.INSTANCE.fromJson(data, AppChannelMessage.class);

        // Act: Setup multiple channels
        processor.processMessage(VALID_CHANNEL_ID3, channelSetupMessage3.getChannelMessage());
        processor.processMessage(VALID_CHANNEL_ID2, channelSetupMessage2.getChannelMessage());
        processor.processMessage(VALID_CHANNEL_ID, channelSetupMessage.getChannelMessage());

        // Assert
        Mockito.verify(mockCallback, Mockito.times(1))
                .onSetupNotification(eq(appPackageId), any(AppChannelSetup.class));
    }

    @Test
    public void GivenAppChannelSetupMessageProcessor_WhenMultipleChannelsSetupAndMultipleCallbacksRegistered_ThenEachCallbackIsInvokedForCorrespondingService() {
        // Arrange
        AppChannelContext ctx = new AppChannelContext(mockWsService);
        AppChannelSetupMessageProcessor processor = new AppChannelSetupMessageProcessor(ctx);

        String appPackageId = "com.hp.test.app";
        String data = makeTestChannelSetupMessage(VALID_CHANNEL_ID, appPackageId, TEST_SERVICE_TYPE_USB,
                TEST_PAYLOAD_TYPE_USB);
        AppChannelMessage channelSetupMessage = StandardJsonParser.INSTANCE.fromJson(data, AppChannelMessage.class);

        data = makeTestChannelSetupMessage(VALID_CHANNEL_ID2, appPackageId, TEST_SERVICE_TYPE_SCAN,
                TEST_PAYLOAD_TYPE_SCAN);
        AppChannelMessage channelSetupMessage2 = StandardJsonParser.INSTANCE.fromJson(data, AppChannelMessage.class);

        data = makeTestChannelSetupMessage(VALID_CHANNEL_ID3, appPackageId, TEST_SERVICE_TYPE_COPY,
                TEST_PAYLOAD_TYPE_COPY);
        AppChannelMessage channelSetupMessage3 = StandardJsonParser.INSTANCE.fromJson(data, AppChannelMessage.class);

        // Act: Setup multiple channels
        processor.processMessage(VALID_CHANNEL_ID3, channelSetupMessage3.getChannelMessage());
        processor.processMessage(VALID_CHANNEL_ID2, channelSetupMessage2.getChannelMessage());
        processor.processMessage(VALID_CHANNEL_ID, channelSetupMessage.getChannelMessage());

        // Register and verify callbacks
        StandardDeviceService.ISetupCallback mockCallback =
                Mockito.mock(StandardDeviceService.ISetupCallback.class);
        AppChannelCallbackRegistry.registerSetupCallback(TEST_SERVICE_TYPE_USB, mockCallback);
        Mockito.verify(mockCallback, Mockito.times(1))
                .onSetupNotification(eq(appPackageId), any(AppChannelSetup.class));

        StandardDeviceService.ISetupCallback mockCallback2 =
                Mockito.mock(StandardDeviceService.ISetupCallback.class);
        AppChannelCallbackRegistry.registerSetupCallback(TEST_SERVICE_TYPE_SCAN, mockCallback2);
        Mockito.verify(mockCallback2, Mockito.times(1))
                .onSetupNotification(eq(appPackageId), any(AppChannelSetup.class));
    }
}
