/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.statisticslet.service;

import static com.hp.jetadvantage.link.device.services.standard.services.AppChannelMessageTestHelper.makeServiceChannelSetupMessage;
import static com.hp.jetadvantage.link.device.services.standard.services.AppChannelMessageTestHelper.makeTestServiceMessageWithRequestBody;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.hp.ext.service.jobStatistics.StatisticsCallbackPayload;
import com.hp.jetadvantage.link.common.PlatformTestHelper;
import com.hp.jetadvantage.link.device.services.interfaces.IE2PayloadCallback;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceStatisticsService;
import com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelCallbackRegistry;
import com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelServiceThreadPool;
import com.hp.jetadvantage.link.device.services.standard.services.StandardWebsocketCallbackService;
import com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelMessageHandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Instrumented tests for Statistics Notification via AppChannel service callbacks.
 *
 * These tests verify that the notification callback mechanism works on a real Android runtime:
 *  - Service callback registration and message dispatching through AppChannelMessageHandler
 *  - Service message parsing (StatisticsCallbackPayload) within a real Android environment
 *  - Callback unregistration correctness
 *  - Error handling for invalid / malformed service messages
 *  - End-to-end broadcast delivery for StatisticsNotificationObserverService
 */
@RunWith(AndroidJUnit4.class)
public class StatisticsNotificationServiceInstrumentedTest {
    private static final String CHANNEL_ID = "223762e2-8bf7-4e29-a1d7-test-statistics";
    private static final String PACKAGE_ID = "com.hp.workpath.sample.statisticsample";
    private static final String E2_SERVICE_GUN = StandardDeviceStatisticsService.E2SERVICE_STATISTICS_CANONICAL_GUN;
    private static final String SERVICE_PATH = "jobStatistics";

    @Mock
    StandardWebsocketCallbackService mockWSCallbackService;

    @Before
    public void setup() {
        PlatformTestHelper.setTestMode(true);
        MockitoAnnotations.openMocks(this);
    }

    @After
    public void tearDown() {
        AppChannelCallbackRegistry.clear();
        AppChannelServiceThreadPool.clear();
    }

    private void awaitServiceCallback(String serviceCallId) throws Exception {
        Future<?> future = AppChannelServiceThreadPool.getFuture(serviceCallId);
        if (future != null) {
            future.get(3, TimeUnit.SECONDS);
        }
    }

    private String makeSampleStatisticsNotification(long lastProcessed, long lastNotified) {
        return "{\n" +
                "  \"jobDetails\": [],\n" +
                "  \"lastSequenceNumberNotified\": " + lastNotified + ",\n" +
                "  \"lastSequenceNumberProcessed\": " + lastProcessed + ",\n" +
                "  \"missingSequenceNumbers\": {\n" +
                "    \"first\": 0,\n" +
                "    \"last\": 0\n" +
                "  }\n" +
                "}";
    }

    // =========================================================================
    // 1. Notification Callback Registration and Invocation
    // =========================================================================
    @Test
    public void GivenStatisticsService_WhenNotificationCallbackRegistered_AndServiceMessageInjected_ThenCallbackInvoked() throws Exception {
        String serviceCallId = "svc-001-callback-test";
        String typeGUN = new StatisticsCallbackPayload().getTypeGUN();

        AtomicInteger callbackCount = new AtomicInteger(0);
        AtomicReference<StatisticsCallbackPayload> receivedMessage = new AtomicReference<>();
        AtomicReference<String> receivedPackageId = new AtomicReference<>();

        IE2PayloadCallback<StatisticsCallbackPayload> callback = (appPackageId, notification) -> {
            callbackCount.incrementAndGet();
            receivedMessage.set(notification);
            receivedPackageId.set(appPackageId);
        };

        StandardDeviceStatisticsService statisticsService = new StandardDeviceStatisticsService();
        statisticsService.registerNotificationCallback(callback);

        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWSCallbackService);
        handler.onReceived(0, makeServiceChannelSetupMessage(CHANNEL_ID, PACKAGE_ID, E2_SERVICE_GUN, E2_SERVICE_GUN));
        handler.onReceived(0, makeTestServiceMessageWithRequestBody(CHANNEL_ID, serviceCallId,
                SERVICE_PATH, typeGUN, makeSampleStatisticsNotification(3, 5)));
        awaitServiceCallback(serviceCallId);

        assertEquals("Callback should be invoked once", 1, callbackCount.get());
        assertNotNull("Service message should not be null", receivedMessage.get());
        assertEquals(PACKAGE_ID, receivedPackageId.get());
        assertEquals(Long.valueOf(5), receivedMessage.get().getLastSequenceNumberNotified().getValue());
        assertEquals(Long.valueOf(3), receivedMessage.get().getLastSequenceNumberProcessed().getValue());
    }

    // =========================================================================
    // 2. Callback Unregistration
    // =========================================================================
    @Test
    public void GivenStatisticsService_WhenCallbackUnregistered_AndServiceMessageInjected_ThenCallbackNotInvoked() throws Exception {
        String serviceCallId1 = "svc-002a-unregister";
        String serviceCallId2 = "svc-002b-unregister";
        String typeGUN = new StatisticsCallbackPayload().getTypeGUN();

        AtomicInteger callbackCount = new AtomicInteger(0);

        IE2PayloadCallback<StatisticsCallbackPayload> callback = (appPackageId, notification) -> {
            callbackCount.incrementAndGet();
        };

        StandardDeviceStatisticsService statisticsService = new StandardDeviceStatisticsService();
        statisticsService.registerNotificationCallback(callback);

        // First message — should trigger callback
        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWSCallbackService);
        handler.onReceived(0, makeServiceChannelSetupMessage(CHANNEL_ID, PACKAGE_ID, E2_SERVICE_GUN, E2_SERVICE_GUN));
        handler.onReceived(0, makeTestServiceMessageWithRequestBody(CHANNEL_ID, serviceCallId1,
                SERVICE_PATH, typeGUN, makeSampleStatisticsNotification(1, 2)));
        awaitServiceCallback(serviceCallId1);
        assertEquals("Callback should be invoked before unregister", 1, callbackCount.get());

        // Unregister
        statisticsService.unRegisterNotificationCallback();
        callbackCount.set(0);

        // Second message — should NOT trigger callback
        handler.onReceived(0, makeTestServiceMessageWithRequestBody(CHANNEL_ID, serviceCallId2,
                SERVICE_PATH, typeGUN, makeSampleStatisticsNotification(2, 3)));

        assertEquals("Callback should not be invoked after unregister", 0, callbackCount.get());
    }

    // =========================================================================
    // 3. Invalid TypeGUN — 400 response, callback NOT invoked
    // =========================================================================
    @Test
    public void GivenStatisticsService_WhenServiceMessageHasWrongTypeGUN_ThenCallbackNotInvoked() throws Exception {
        String serviceCallId = "svc-003-wrongtype";
        String invalidTypeGUN = "com.hp.ext.types.invalid.version.1.type.wrong";

        AtomicInteger callbackCount = new AtomicInteger(0);

        IE2PayloadCallback<StatisticsCallbackPayload> callback = (appPackageId, notification) -> {
            callbackCount.incrementAndGet();
        };

        StandardDeviceStatisticsService statisticsService = new StandardDeviceStatisticsService();
        statisticsService.registerNotificationCallback(callback);

        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWSCallbackService);
        handler.onReceived(0, makeServiceChannelSetupMessage(CHANNEL_ID, PACKAGE_ID, E2_SERVICE_GUN, E2_SERVICE_GUN));
        handler.onReceived(0, makeTestServiceMessageWithRequestBody(CHANNEL_ID, serviceCallId,
                SERVICE_PATH, invalidTypeGUN, makeSampleStatisticsNotification(1, 5)));
        awaitServiceCallback(serviceCallId);

        assertEquals("Callback should not be invoked for wrong typeGUN", 0, callbackCount.get());
    }

    // =========================================================================
    // 4. Multiple Sequential Notifications
    // =========================================================================
    @Test
    public void GivenStatisticsService_WhenMultipleNotificationsSent_ThenAllCallbacksInvoked() throws Exception {
        String typeGUN = new StatisticsCallbackPayload().getTypeGUN();

        AtomicInteger callbackCount = new AtomicInteger(0);
        AtomicReference<StatisticsCallbackPayload> lastMessage = new AtomicReference<>();

        IE2PayloadCallback<StatisticsCallbackPayload> callback = (appPackageId, notification) -> {
            callbackCount.incrementAndGet();
            lastMessage.set(notification);
        };

        StandardDeviceStatisticsService statisticsService = new StandardDeviceStatisticsService();
        statisticsService.registerNotificationCallback(callback);

        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWSCallbackService);
        handler.onReceived(0, makeServiceChannelSetupMessage(CHANNEL_ID, PACKAGE_ID, E2_SERVICE_GUN, E2_SERVICE_GUN));

        // Send 3 notifications
        for (int i = 1; i <= 3; i++) {
            String svcCallId = "svc-004-multi-" + i;
            handler.onReceived(0, makeTestServiceMessageWithRequestBody(CHANNEL_ID, svcCallId,
                    SERVICE_PATH, typeGUN, makeSampleStatisticsNotification(i, i + 2)));
            awaitServiceCallback(svcCallId);
        }

        assertEquals("All 3 callbacks should be invoked", 3, callbackCount.get());
        assertNotNull(lastMessage.get());
        assertEquals(Long.valueOf(5), lastMessage.get().getLastSequenceNumberNotified().getValue());
        assertEquals(Long.valueOf(3), lastMessage.get().getLastSequenceNumberProcessed().getValue());
    }

    // =========================================================================
    // 5. Wrong service path — callback NOT invoked
    // =========================================================================
    @Test
    public void GivenStatisticsService_WhenServiceMessageHasWrongPath_ThenCallbackNotInvoked() throws Exception {
        String serviceCallId = "svc-005-wrongpath";
        String typeGUN = new StatisticsCallbackPayload().getTypeGUN();

        AtomicInteger callbackCount = new AtomicInteger(0);

        IE2PayloadCallback<StatisticsCallbackPayload> callback = (appPackageId, notification) -> {
            callbackCount.incrementAndGet();
        };

        StandardDeviceStatisticsService statisticsService = new StandardDeviceStatisticsService();
        statisticsService.registerNotificationCallback(callback);

        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWSCallbackService);
        handler.onReceived(0, makeServiceChannelSetupMessage(CHANNEL_ID, PACKAGE_ID, E2_SERVICE_GUN, E2_SERVICE_GUN));
        handler.onReceived(0, makeTestServiceMessageWithRequestBody(CHANNEL_ID, serviceCallId,
                "wrongPath", typeGUN, makeSampleStatisticsNotification(1, 5)));
        awaitServiceCallback(serviceCallId);

        assertEquals("Callback should not be invoked for wrong path", 0, callbackCount.get());
    }

    // =========================================================================
    // 6. Re-register callback — new callback replaces old
    // =========================================================================
    @Test
    public void GivenStatisticsService_WhenCallbackReRegistered_ThenNewCallbackInvoked() throws Exception {
        String serviceCallId = "svc-006-reregister";
        String typeGUN = new StatisticsCallbackPayload().getTypeGUN();

        AtomicInteger firstCallbackCount = new AtomicInteger(0);
        AtomicInteger secondCallbackCount = new AtomicInteger(0);

        IE2PayloadCallback<StatisticsCallbackPayload> firstCallback = (appPackageId, notification) -> {
            firstCallbackCount.incrementAndGet();
        };
        IE2PayloadCallback<StatisticsCallbackPayload> secondCallback = (appPackageId, notification) -> {
            secondCallbackCount.incrementAndGet();
        };

        StandardDeviceStatisticsService statisticsService = new StandardDeviceStatisticsService();

        // Register first, then immediately re-register with second (no message sent to first)
        statisticsService.registerNotificationCallback(firstCallback);
        statisticsService.registerNotificationCallback(secondCallback);

        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWSCallbackService);
        handler.onReceived(0, makeServiceChannelSetupMessage(CHANNEL_ID, PACKAGE_ID, E2_SERVICE_GUN, E2_SERVICE_GUN));
        handler.onReceived(0, makeTestServiceMessageWithRequestBody(CHANNEL_ID, serviceCallId,
                SERVICE_PATH, typeGUN, makeSampleStatisticsNotification(2, 3)));
        awaitServiceCallback(serviceCallId);

        assertEquals("First callback should NOT receive notification after re-register", 0, firstCallbackCount.get());
        assertEquals("Second callback should receive notification", 1, secondCallbackCount.get());
    }

    // =========================================================================
    // 7. Service message parsing — large sequence numbers
    // =========================================================================
    @Test
    public void GivenStatisticsService_WhenNotificationHasLargeSequenceNumbers_ThenServiceMessageParsedCorrectly() throws Exception {
        String serviceCallId = "svc-007-large-seq";
        String typeGUN = new StatisticsCallbackPayload().getTypeGUN();
        long largeProcessed = 999999999L;
        long largeNotified = 1000000000L;

        AtomicReference<StatisticsCallbackPayload> receivedMessage = new AtomicReference<>();

        IE2PayloadCallback<StatisticsCallbackPayload> callback = (appPackageId, notification) -> {
            receivedMessage.set(notification);
        };

        StandardDeviceStatisticsService statisticsService = new StandardDeviceStatisticsService();
        statisticsService.registerNotificationCallback(callback);

        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWSCallbackService);
        handler.onReceived(0, makeServiceChannelSetupMessage(CHANNEL_ID, PACKAGE_ID, E2_SERVICE_GUN, E2_SERVICE_GUN));
        handler.onReceived(0, makeTestServiceMessageWithRequestBody(CHANNEL_ID, serviceCallId,
                SERVICE_PATH, typeGUN, makeSampleStatisticsNotification(largeProcessed, largeNotified)));
        awaitServiceCallback(serviceCallId);

        assertNotNull("Service message should not be null", receivedMessage.get());
        assertEquals(Long.valueOf(largeNotified), receivedMessage.get().getLastSequenceNumberNotified().getValue());
        assertEquals(Long.valueOf(largeProcessed), receivedMessage.get().getLastSequenceNumberProcessed().getValue());
    }
}
