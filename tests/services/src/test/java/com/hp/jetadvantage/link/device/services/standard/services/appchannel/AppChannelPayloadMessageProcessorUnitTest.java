package com.hp.jetadvantage.link.device.services.standard.services.appchannel;

import static com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelMessageTestHelper.makeTestChannelPayloadMessage;
import static com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelMessageTestHelper.makeTestChannelSetupMessage;
import static junit.framework.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardJsonParser;
import com.hp.jetadvantage.link.device.services.standard.services.StandardWebsocketCallbackService;
import com.hp.ws.websocket.AppChannelMessage;
import com.hp.ws.websocket.AppChannelMessage.ChannelMessage;
import com.hp.ws.websocket.AppChannelSetup;
import com.hp.ws.websocket.JsonTypedObject;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AppChannelPayloadMessageProcessorUnitTest extends TestCase {

    public static final String VALID_CHANNEL_ID = "223762e2-8bf7-4e29-a1d7-0790103e83a1";
    public static final String VALID_CHANNEL_ID2 = "223762e2-8bf7-4e29-a1d7-0790103e83a2";
    public static final String VALID_CHANNEL_ID3 = "223762e2-8bf7-4e29-a1d7-0790103e83a3";
    public static final String VALID_CHANNEL_ID4 = "223762e2-8bf7-4e29-a1d7-0790103e83a4";
    public static final String TEST_PAYLOAD_TYPE_GUN = "com.hp.ext.service.usbAccessories.version.1.type" +
            ".usbRegistrationPayload";
    public static final String TEST_SERVICE_TYPE_GUN = "com.hp.ext.service.usbAccessories.version.1";

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
    public void GivenAppChannelPayloadMessageProcessor_WhenCanProcessMessageCalled_ThenReturnIfPayloadMessage() {
        // Arrange
        AppChannelContext ctx = new AppChannelContext(mockWsService);
        AppChannelPayloadMessageProcessor processor = new AppChannelPayloadMessageProcessor(ctx);


        // Act & Verify
        ChannelMessage payloadMessage = createPayloadMessage(VALID_CHANNEL_ID, TEST_PAYLOAD_TYPE_GUN, "");
        boolean result = processor.canProcessMessage(payloadMessage);
        assertTrue("payloadMessage", result);

        // Act & Verify
        String data = makeTestChannelSetupMessage(VALID_CHANNEL_ID, "packageId", TEST_SERVICE_TYPE_GUN,
                TEST_PAYLOAD_TYPE_GUN);
        AppChannelMessage appChannelMessage = StandardJsonParser.INSTANCE.fromJson(data, AppChannelMessage.class);
        result = processor.canProcessMessage(appChannelMessage.getChannelMessage());
        assertFalse("setupMessage", result);

        // Act & Verify : Check if it can process a null message
        result = processor.canProcessMessage(null);
        assertFalse("null Message", result);
    }

    @Test
    public void GivenAppChannelPayloadMessageProcessor_WhenProcessMessageCalled_ThenCallbackShouldBeInvoked() {
        // Arrange
        AppChannelContext ctx = new AppChannelContext(mockWsService);
        String appPackageId = "com.hp.test.app";
        ctx.getAppChannelRegistry().addChannel(VALID_CHANNEL_ID, createAppChannelSetup(VALID_CHANNEL_ID, appPackageId
                , TEST_SERVICE_TYPE_GUN, TEST_PAYLOAD_TYPE_GUN));

        StandardDeviceService.IPayloadCallback mockCallback =
                Mockito.mock(StandardDeviceService.IPayloadCallback.class);
        AppChannelCallbackRegistry.registerPayloadCallback(TEST_SERVICE_TYPE_GUN, mockCallback);

        AppChannelPayloadMessageProcessor processor = new AppChannelPayloadMessageProcessor(ctx);

        String value = "testPayloadValue";
        ChannelMessage payloadMessage = createPayloadMessage(VALID_CHANNEL_ID, TEST_PAYLOAD_TYPE_GUN, value);

        // Act
        processor.processMessage(VALID_CHANNEL_ID, payloadMessage);

        // Assert
        Mockito.verify(mockCallback, Mockito.times(1))
                .onReceiveNotification(eq(appPackageId), any(JsonTypedObject.class));
    }

    @Test
    public void GivenAppChannelPayloadMessageProcessor_WhenProcessMessageCalledWithoutChannelSetup_ThenCallbackShouldBeInvoked() {
        // Arrange
        AppChannelContext ctx = new AppChannelContext(mockWsService);

        StandardDeviceService.IPayloadCallback mockCallback =
                Mockito.mock(StandardDeviceService.IPayloadCallback.class);
        AppChannelCallbackRegistry.registerPayloadCallback(TEST_SERVICE_TYPE_GUN, mockCallback);

        AppChannelPayloadMessageProcessor processor = new AppChannelPayloadMessageProcessor(ctx);

        String value = "testPayloadValue";
        ChannelMessage payloadMessage = createPayloadMessage(VALID_CHANNEL_ID, TEST_PAYLOAD_TYPE_GUN, value);

        // Act
        processor.processMessage(VALID_CHANNEL_ID, payloadMessage);

        // Assert
        Mockito.verify(mockCallback, Mockito.times(1))
                .onReceiveNotification(eq(""), any(JsonTypedObject.class));
    }

    @Test
    public void GivenAppChannelPayloadMessageProcessor_WhenProcessMessageCalledWithoutCallback_ThenMessageShouldBeQueued() {
        // Arrange
        AppChannelContext ctx = new AppChannelContext(mockWsService);
        String appPackageId = "com.hp.test.app";
        ctx.getAppChannelRegistry().addChannel(VALID_CHANNEL_ID, createAppChannelSetup(VALID_CHANNEL_ID, appPackageId
                , TEST_SERVICE_TYPE_GUN, TEST_PAYLOAD_TYPE_GUN));

        AppChannelPayloadMessageProcessor processor = new AppChannelPayloadMessageProcessor(ctx);

        String value = "testPayloadValue";
        ChannelMessage payloadMessage = createPayloadMessage(VALID_CHANNEL_ID, TEST_PAYLOAD_TYPE_GUN, value);

        // Act
        processor.processMessage(VALID_CHANNEL_ID, payloadMessage);

        // Assert
        ChannelMessage msg = processor.msgQueue.pollMessage(TEST_SERVICE_TYPE_GUN);
        assertNotNull("Message should be queued when no callback is registered", msg);
        assertEquals("Message payload type should match", TEST_PAYLOAD_TYPE_GUN,
                msg.getMessage().getPayload().getValue().getTypeGUN());
    }

    @Test
    public void GivenAppChannelPayloadMessageProcessor_WhenMessageIsInQueueAndCallbackRegistered_ThenDeliverQueuedMessagesToCallbackShouldBeExecuted() {
        // Arrange
        AppChannelContext ctx = new AppChannelContext(mockWsService);
        String appPackageId = "com.hp.test.app";
        ctx.getAppChannelRegistry().addChannel(
                VALID_CHANNEL_ID,
                createAppChannelSetup(VALID_CHANNEL_ID, appPackageId, TEST_SERVICE_TYPE_GUN, TEST_PAYLOAD_TYPE_GUN)
        );
        AppChannelPayloadMessageProcessor processor = new AppChannelPayloadMessageProcessor(ctx);

        // Queue one message
        String jsonValue = "usbAttached";
        ChannelMessage payloadMessage = createPayloadMessage(VALID_CHANNEL_ID, TEST_PAYLOAD_TYPE_GUN, jsonValue);
        processor.processMessage(VALID_CHANNEL_ID, payloadMessage);

        // Act: now register callback, which should trigger delivery of the queued message
        StandardDeviceService.IPayloadCallback mockCallback =
                Mockito.mock(StandardDeviceService.IPayloadCallback.class);
        AppChannelCallbackRegistry.registerPayloadCallback(TEST_SERVICE_TYPE_GUN, mockCallback);

        // Assert
        Mockito.verify(mockCallback, Mockito.times(1))
                .onReceiveNotification(
                        eq(appPackageId),
                        any(JsonTypedObject.class)
                );
    }

    @Test
    public void GivenAppChannelPayloadMessageProcessor_WhenMessagesAreInQueueAndCallbackRegistered_ThenMultipleQueuedMessagesAreAllDelivered() {
        // Arrange
        AppChannelContext ctx = new AppChannelContext(mockWsService);
        String appPackageId = "com.hp.test.app";
        ctx.getAppChannelRegistry().addChannel(
                VALID_CHANNEL_ID,
                createAppChannelSetup(VALID_CHANNEL_ID, appPackageId, TEST_SERVICE_TYPE_GUN, TEST_PAYLOAD_TYPE_GUN)
        );
        AppChannelPayloadMessageProcessor processor = new AppChannelPayloadMessageProcessor(ctx);

        // Queue one message
        String jsonValue1 = "usbAttached1";
        ChannelMessage payloadMessage1 = createPayloadMessage(VALID_CHANNEL_ID, TEST_PAYLOAD_TYPE_GUN, jsonValue1);
        processor.processMessage(VALID_CHANNEL_ID, payloadMessage1);

        String jsonValue2 = "usbAttached2";
        ChannelMessage payloadMessage2 = createPayloadMessage(VALID_CHANNEL_ID, TEST_PAYLOAD_TYPE_GUN, jsonValue2);
        processor.processMessage(VALID_CHANNEL_ID, payloadMessage2);

        String jsonValue3 = "usbAttached3";
        ChannelMessage payloadMessage3 = createPayloadMessage(VALID_CHANNEL_ID, TEST_PAYLOAD_TYPE_GUN, jsonValue3);
        processor.processMessage(VALID_CHANNEL_ID, payloadMessage3);

        // Act: now register callback, which should trigger delivery of the queued message
        StandardDeviceService.IPayloadCallback mockCallback =
                Mockito.mock(StandardDeviceService.IPayloadCallback.class);
        AppChannelCallbackRegistry.registerPayloadCallback(TEST_SERVICE_TYPE_GUN, mockCallback);

        // Assert
        Mockito.verify(mockCallback, Mockito.times(3))
                .onReceiveNotification(
                        eq(appPackageId),
                        any(JsonTypedObject.class)
                );
    }

    @Test
    public void GivenAppChannelPayloadMessageProcessor_WhenMessagesAreInQueueForMultipleChannelsAndCallbackRegistered_ThenMultipleQueuedMessagesAreAllDelivered() {
        // Arrange
        AppChannelContext ctx = new AppChannelContext(mockWsService);
        String appPackageId = "com.hp.test.app1";
        ctx.getAppChannelRegistry().addChannel(
                VALID_CHANNEL_ID,
                createAppChannelSetup(VALID_CHANNEL_ID, appPackageId, TEST_SERVICE_TYPE_GUN, TEST_PAYLOAD_TYPE_GUN)
        );


        String appPackageId2 = "com.hp.test.app2";
        ctx.getAppChannelRegistry().addChannel(
                VALID_CHANNEL_ID2,
                createAppChannelSetup(VALID_CHANNEL_ID2, appPackageId2, TEST_SERVICE_TYPE_GUN, TEST_PAYLOAD_TYPE_GUN)
        );

        String appPackageId3 = "com.hp.test.app3";
        ctx.getAppChannelRegistry().addChannel(
                VALID_CHANNEL_ID3,
                createAppChannelSetup(VALID_CHANNEL_ID3, appPackageId3, TEST_SERVICE_TYPE_GUN, TEST_PAYLOAD_TYPE_GUN)
        );

        AppChannelPayloadMessageProcessor processor = new AppChannelPayloadMessageProcessor(ctx);


        // Queue one message
        String jsonValue1 = "usbAttached1";
        ChannelMessage payloadMessage1 = createPayloadMessage(VALID_CHANNEL_ID, TEST_PAYLOAD_TYPE_GUN, jsonValue1);
        processor.processMessage(VALID_CHANNEL_ID, payloadMessage1);

        String jsonValue2 = "usbAttached2";
        ChannelMessage payloadMessage2 = createPayloadMessage(VALID_CHANNEL_ID2, TEST_PAYLOAD_TYPE_GUN, jsonValue2);
        processor.processMessage(VALID_CHANNEL_ID2, payloadMessage2);

        String jsonValue3 = "usbAttached3";
        ChannelMessage payloadMessage3 = createPayloadMessage(VALID_CHANNEL_ID3, TEST_PAYLOAD_TYPE_GUN, jsonValue3);
        processor.processMessage(VALID_CHANNEL_ID3, payloadMessage3);

        // Act: now register callback, which should trigger delivery of the queued message
        StandardDeviceService.IPayloadCallback mockCallback =
                Mockito.mock(StandardDeviceService.IPayloadCallback.class);
        AppChannelCallbackRegistry.registerPayloadCallback(TEST_SERVICE_TYPE_GUN, mockCallback);

        // Assert
        Mockito.verify(mockCallback, Mockito.times(1))
                .onReceiveNotification(
                        eq(appPackageId),
                        any(JsonTypedObject.class)
                );
        Mockito.verify(mockCallback, Mockito.times(1))
                .onReceiveNotification(
                        eq(appPackageId2),
                        any(JsonTypedObject.class)
                );
        Mockito.verify(mockCallback, Mockito.times(1))
                .onReceiveNotification(
                        eq(appPackageId3),
                        any(JsonTypedObject.class)
                );

        // Now let's add another message for a different channel
        Mockito.reset(mockCallback);
        ChannelMessage payloadMessage4 = createPayloadMessage(VALID_CHANNEL_ID3, TEST_PAYLOAD_TYPE_GUN, jsonValue3);
        processor.processMessage(VALID_CHANNEL_ID3, payloadMessage4);

        Mockito.verify(mockCallback, Mockito.times(1))
                .onReceiveNotification(
                        eq(appPackageId3),
                        any(JsonTypedObject.class)
                );

    }

    private ChannelMessage createPayloadMessage(String channelId, String typeGun, String message) {
        String data = makeTestChannelPayloadMessage(channelId, typeGun, message);
        AppChannelMessage callbackMsg = StandardJsonParser.INSTANCE.fromJson(data, AppChannelMessage.class);
        return callbackMsg.getChannelMessage();
    }

    private AppChannelSetup createAppChannelSetup(String channelId, String packageId, String serviceTypeGun,
                                                  String payloadGun) {
        String data = makeTestChannelSetupMessage(channelId, packageId, serviceTypeGun, payloadGun);
        AppChannelMessage appChannelMessage = StandardJsonParser.INSTANCE.fromJson(data, AppChannelMessage.class);
        return appChannelMessage.getChannelMessage().getMessage().getSetup();
    }
}
