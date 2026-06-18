/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.statisticslet.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.Intent;

import com.hp.ext.service.jobStatistics.SequenceNumber;
import com.hp.ext.service.jobStatistics.StatisticsCallbackPayload;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceStatisticsService;
import com.hp.jetadvantage.link.device.services.interfaces.IE2PayloadCallback;
import com.hp.jetadvantage.link.device.services.standard.common.PackageManagerHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StatisticsNotificationObserverServiceUnitTest {

    @Mock
    private Context mockContext;
    @Mock
    private IDeviceStatisticsService mockDeviceStatisticsService;
    @Mock
    private PackageManagerHelper mockPackageManagerHelper;

    private StatisticsNotificationObserverService service;

    @Before
    public void setUp() {
        service = spy(new StatisticsNotificationObserverService());
        doReturn(mockContext).when(service).getApplicationContext();
        service.setDeviceStatisticsService(mockDeviceStatisticsService);
        service.setPackageManagerHelper(mockPackageManagerHelper);
    }

    // ===== subscribeStatisticsNotificationEvents tests =====

    @Test
    public void GivenService_WhenSubscribeStatisticsNotificationEvents_ThenDeviceServiceCreatedAndCallbackRegistered() {
        IDeviceStatisticsService mockService = mock(IDeviceStatisticsService.class);
        doReturn(mockService).when(service).createDeviceStatisticsService();

        service.subscribeStatisticsNotificationEvents();

        verify(mockService, times(1)).registerNotificationCallback(any());
        assertNotNull(service.getDeviceStatisticsService());
    }

    @Test
    public void GivenService_WhenSubscribeStatisticsNotificationEventsThrowsException_ThenExceptionHandled() {
        doReturn(mockDeviceStatisticsService).when(service).createDeviceStatisticsService();
        Mockito.doThrow(new RuntimeException("test exception"))
                .when(mockDeviceStatisticsService).registerNotificationCallback(any());

        // should not throw
        service.subscribeStatisticsNotificationEvents();
    }

    @Test
    public void GivenService_WhenSubscribeCalledTwice_ThenSecondCallOverwritesFirst() {
        IDeviceStatisticsService firstMock = mock(IDeviceStatisticsService.class);
        IDeviceStatisticsService secondMock = mock(IDeviceStatisticsService.class);

        // First subscription
        doReturn(firstMock).when(service).createDeviceStatisticsService();
        service.subscribeStatisticsNotificationEvents();
        verify(firstMock, times(1)).registerNotificationCallback(any());

        // Second subscription (e.g., onStartCommand after service restart)
        doReturn(secondMock).when(service).createDeviceStatisticsService();
        service.setDeviceStatisticsService(null);  // simulate cleared state
        service.subscribeStatisticsNotificationEvents();
        verify(secondMock, times(1)).registerNotificationCallback(any());
    }

    @Test
    public void GivenService_WhenAlreadySubscribed_ThenOnStartCommandSkipsResubscription() {
        // mDeviceStatisticsService is already set in setUp()
        assertNotNull(service.getDeviceStatisticsService());

        // createDeviceStatisticsService should NOT be called since already subscribed
        verify(service, never()).createDeviceStatisticsService();
    }

    @Test
    public void GivenService_WhenNotSubscribed_ThenOnStartCommandCanResubscribe() {
        service.setDeviceStatisticsService(null);
        assertNull(service.getDeviceStatisticsService());

        // If onStartCommand triggers re-subscription when mDeviceStatisticsService is null
        IDeviceStatisticsService mockService = mock(IDeviceStatisticsService.class);
        doReturn(mockService).when(service).createDeviceStatisticsService();

        service.subscribeStatisticsNotificationEvents();
        assertNotNull(service.getDeviceStatisticsService());
        verify(mockService, times(1)).registerNotificationCallback(any());
    }

    // ===== processStatisticsNotification tests =====

    @Test
    public void GivenService_WhenProcessStatisticsNotificationWithValidServiceMessage_ThenBroadcastsSent() {
        String appPackageId = "com.hp.workpath.sample.statisticsample";
        String agentId = "test-agent-id";

        StatisticsCallbackPayload serviceMessage = createTestServiceMessage(3L, 5L);

        when(mockPackageManagerHelper.getAgentId(eq(mockContext), eq(appPackageId), anyString()))
                .thenReturn(agentId);
        doNothing().when(service).sendBroadcastToApp(any(), anyString(), anyLong(), anyLong(), anyString());

        service.processStatisticsNotification(appPackageId, serviceMessage);

        verify(service, times(1)).sendBroadcastToApp(
                eq(mockContext), eq(agentId), eq(3L), eq(5L), eq(appPackageId));
    }

    @Test
    public void GivenService_WhenProcessStatisticsNotificationWithNullServiceMessage_ThenNoBroadcastSent() {
        service.processStatisticsNotification("com.hp.test.app", null);

        verify(service, never()).sendBroadcastToApp(any(), anyString(), anyLong(), anyLong(), anyString());
    }

    @Test
    public void GivenService_WhenProcessStatisticsNotificationWithNullAgentId_ThenNoBroadcastSent() {
        String appPackageId = "com.hp.workpath.sample.statisticsample";
        StatisticsCallbackPayload serviceMessage = createTestServiceMessage(3L, 5L);

        when(mockPackageManagerHelper.getAgentId(eq(mockContext), eq(appPackageId), anyString()))
                .thenReturn(null);

        service.processStatisticsNotification(appPackageId, serviceMessage);

        verify(service, never()).sendBroadcastToApp(any(), anyString(), anyLong(), anyLong(), anyString());
    }

    @Test
    public void GivenService_WhenProcessStatisticsNotificationWithNullSequenceNumbers_ThenBroadcastSentWithDefaults() {
        String appPackageId = "com.hp.workpath.sample.statisticsample";
        String agentId = "test-agent-id";

        StatisticsCallbackPayload serviceMessage = new StatisticsCallbackPayload();
        // sequence numbers will be null

        when(mockPackageManagerHelper.getAgentId(eq(mockContext), eq(appPackageId), anyString()))
                .thenReturn(agentId);
        doNothing().when(service).sendBroadcastToApp(any(), anyString(), anyLong(), anyLong(), anyString());

        service.processStatisticsNotification(appPackageId, serviceMessage);

        // should default to 0 for null sequence numbers
        verify(service, times(1)).sendBroadcastToApp(
                eq(mockContext), eq(agentId), eq(0L), eq(0L), eq(appPackageId));
    }

    @Test
    public void GivenService_WhenProcessStatisticsNotificationWithZeroSequenceNumbers_ThenBroadcastSentWithZeros() {
        String appPackageId = "com.hp.workpath.sample.statisticsample";
        String agentId = "test-agent-id";
        StatisticsCallbackPayload serviceMessage = createTestServiceMessage(0L, 0L);

        when(mockPackageManagerHelper.getAgentId(eq(mockContext), eq(appPackageId), anyString()))
                .thenReturn(agentId);
        doNothing().when(service).sendBroadcastToApp(any(), anyString(), anyLong(), anyLong(), anyString());

        service.processStatisticsNotification(appPackageId, serviceMessage);

        verify(service, times(1)).sendBroadcastToApp(
                eq(mockContext), eq(agentId), eq(0L), eq(0L), eq(appPackageId));
    }

    @Test
    public void GivenService_WhenProcessStatisticsNotificationWithLargeSequenceNumbers_ThenBroadcastSentCorrectly() {
        String appPackageId = "com.hp.workpath.sample.statisticsample";
        String agentId = "test-agent-id";
        StatisticsCallbackPayload serviceMessage = createTestServiceMessage(255L, 256L);

        when(mockPackageManagerHelper.getAgentId(eq(mockContext), eq(appPackageId), anyString()))
                .thenReturn(agentId);
        doNothing().when(service).sendBroadcastToApp(any(), anyString(), anyLong(), anyLong(), anyString());

        service.processStatisticsNotification(appPackageId, serviceMessage);

        verify(service, times(1)).sendBroadcastToApp(
                eq(mockContext), eq(agentId), eq(255L), eq(256L), eq(appPackageId));
    }

    // ===== sendBroadcastToApp tests =====

    @Test
    public void GivenService_WhenSendBroadcastToApp_ThenBroadcastSentWithCorrectExtras() {
        ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);

        service.sendBroadcastToApp(mockContext, "agent-123", 10L, 20L, "com.hp.test.app");

        verify(mockContext).sendBroadcast(intentCaptor.capture(), anyString());
        Intent capturedIntent = intentCaptor.getValue();
        assertNotNull(capturedIntent);
    }

    @Test
    public void GivenService_WhenSendBroadcastToAppWithNullContext_ThenExceptionHandled() {
        // should not throw
        service.sendBroadcastToApp(null, "agent-123", 10L, 20L, "com.hp.test.app");
    }

    @Test
    public void GivenClampHighestSeqToInt_WhenValueExceedsIntRange_ThenReturnMaxInt() {
        long overflowValue = (long) Integer.MAX_VALUE + 1;
        assertEquals(Integer.MAX_VALUE,
                StatisticsNotificationObserverService.clampHighestSeqToInt(overflowValue));
    }

    @Test
    public void GivenClampHighestSeqToInt_WhenValueWithinIntRange_ThenReturnAsIs() {
        assertEquals(256,
                StatisticsNotificationObserverService.clampHighestSeqToInt(256L));
    }

    @Test
    public void GivenClampHighestSeqToInt_WhenValueIsMaxInt_ThenReturnMaxInt() {
        assertEquals(Integer.MAX_VALUE,
                StatisticsNotificationObserverService.clampHighestSeqToInt((long) Integer.MAX_VALUE));
    }

    // ===== Callback registration integration tests =====

    @Test
    public void GivenService_WhenSubscribeAndCallbackInvoked_ThenProcessNotificationCalled() {
        IDeviceStatisticsService mockService = mock(IDeviceStatisticsService.class);
        doReturn(mockService).when(service).createDeviceStatisticsService();

        ArgumentCaptor<IE2PayloadCallback> callbackCaptor =
                ArgumentCaptor.forClass(IE2PayloadCallback.class);

        service.subscribeStatisticsNotificationEvents();

        verify(mockService).registerNotificationCallback(callbackCaptor.capture());
        IE2PayloadCallback<StatisticsCallbackPayload> registeredCallback = callbackCaptor.getValue();

        // simulate E2 notification
        StatisticsCallbackPayload serviceMessage = createTestServiceMessage(1L, 2L);

        when(mockPackageManagerHelper.getAgentId(eq(mockContext), eq("com.test.app"), anyString()))
                .thenReturn("agent-id");
        doNothing().when(service).sendBroadcastToApp(any(), anyString(), anyLong(), anyLong(), anyString());

        registeredCallback.onReceiveNotification("com.test.app", serviceMessage);

        verify(service, times(1)).sendBroadcastToApp(
                eq(mockContext), eq("agent-id"), eq(1L), eq(2L), eq("com.test.app"));
    }

    // ===== Cleanup tests =====

    @Test
    public void GivenService_WhenSetDeviceStatisticsServiceCalled_ThenServiceIsSet() {
        IDeviceStatisticsService mockService = mock(IDeviceStatisticsService.class);
        service.setDeviceStatisticsService(mockService);
        assertNotNull(service.getDeviceStatisticsService());
    }

    @Test
    public void GivenService_WhenSetDeviceStatisticsServiceWithNull_ThenServiceIsNull() {
        service.setDeviceStatisticsService(null);
        assertNull(service.getDeviceStatisticsService());
    }

    @Test
    public void GivenService_WhenProcessStatisticsNotificationAndGetAgentIdThrows_ThenExceptionHandled() {
        String appPackageId = "com.hp.workpath.sample.statisticsample";
        StatisticsCallbackPayload serviceMessage = createTestServiceMessage(3L, 5L);

        when(mockPackageManagerHelper.getAgentId(eq(mockContext), eq(appPackageId), anyString()))
                .thenThrow(new RuntimeException("database error"));

        // should not throw
        service.processStatisticsNotification(appPackageId, serviceMessage);

        verify(service, never()).sendBroadcastToApp(any(), anyString(), anyLong(), anyLong(), anyString());
    }

    @Test
    public void GivenService_WhenMultipleNotificationsProcessed_ThenEachBroadcastSentCorrectly() {
        String appPackageId = "com.hp.workpath.sample.statisticsample";
        String agentId = "test-agent-id";

        when(mockPackageManagerHelper.getAgentId(eq(mockContext), eq(appPackageId), anyString()))
                .thenReturn(agentId);
        doNothing().when(service).sendBroadcastToApp(any(), anyString(), anyLong(), anyLong(), anyString());

        // process first notification
        StatisticsCallbackPayload serviceMessage1 = createTestServiceMessage(1L, 3L);
        service.processStatisticsNotification(appPackageId, serviceMessage1);

        // process second notification
        StatisticsCallbackPayload serviceMessage2 = createTestServiceMessage(3L, 7L);
        service.processStatisticsNotification(appPackageId, serviceMessage2);

        // verify both broadcasts were sent
        verify(service, times(1)).sendBroadcastToApp(
                eq(mockContext), eq(agentId), eq(1L), eq(3L), eq(appPackageId));
        verify(service, times(1)).sendBroadcastToApp(
                eq(mockContext), eq(agentId), eq(3L), eq(7L), eq(appPackageId));
    }

    // ===== Helper methods =====

    private StatisticsCallbackPayload createTestServiceMessage(Long lastProcessed, Long lastNotified) {
        StatisticsCallbackPayload serviceMessage = new StatisticsCallbackPayload();
        try {
            java.lang.reflect.Field processedField =
                    StatisticsCallbackPayload.class.getDeclaredField("lastSequenceNumberProcessed");
            processedField.setAccessible(true);
            processedField.set(serviceMessage, new SequenceNumber(lastProcessed));

            java.lang.reflect.Field notifiedField =
                    StatisticsCallbackPayload.class.getDeclaredField("lastSequenceNumberNotified");
            notifiedField.setAccessible(true);
            notifiedField.set(serviceMessage, new SequenceNumber(lastNotified));
        } catch (Exception e) {
            throw new RuntimeException("Failed to create test service message", e);
        }
        return serviceMessage;
    }
}
