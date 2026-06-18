package com.hp.jetadvantage.link.services.copylet.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import android.os.Bundle;

import com.hp.ext.service.scanJob.ScanJobIdentifier;
import com.hp.ext.service.scanJob.ScanJobNotificationContent;
import com.hp.ext.service.scanJob.ScanJobStatus;
import com.hp.ext.service.scanJob.ScanNotification;
import com.hp.ext.types.protocol.Unsigned32;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceScanJobService;
import com.hp.jetadvantage.link.device.services.interfaces.IE2PayloadCallback;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public class MonitoringStoredCopyJobStateTest {

    private static final String TEST_JOB_ID = "a1b2c3d4-e5f6-7890-abcd-ef1234567890";

    @Mock private IDeviceScanJobService mockScanJobService;
    @Mock private BaseJobIntentServiceStateMachine mockStateMachine;
    @Mock private ScanNotification mockNotification;
    @Mock private ScanJobNotificationContent mockScanJobNotificationContent;
    @Mock private ScanJobStatus mockScanJobStatus;
    @Mock private Bundle mockJobBundle;

    private MonitoringStoredCopyJobState state;

    @Before
    public void setUp() {
        state = new MonitoringStoredCopyJobState(TEST_JOB_ID);
        state.scanJobService = mockScanJobService;
    }

    @Test
    public void GivenState_WhenCreated_ThenStateIsNotNull() {
        assertNotNull(state);
    }

    @Test
    public void GivenState_WhenRegisterCallback_ThenScanNotificationCallbackRegistered() {
        state.registerNotificationCallback(mockStateMachine);
        verify(mockScanJobService).registerNotificationCallback(any(IE2PayloadCallback.class));
    }

    @Test
    public void GivenState_WhenUnregisterCallback_ThenCallbackUnregistered() {
        state.unregisterNotificationCallback();
        verify(mockScanJobService).unRegisterNotificationCallback();
    }

    // ===== Callback execution tests =====

    @Test
    public void GivenCallback_WhenNullPackageId_ThenNoProcessing() {
        state.registerNotificationCallback(mockStateMachine);

        IE2PayloadCallback<ScanNotification> callback = captureCallback();
        callback.onReceiveNotification(null, mockNotification);

        // Should return early — no interaction with notification
        verify(mockNotification, never()).isJobNotification();
    }

    @Test
    public void GivenCallback_WhenNullNotification_ThenNoProcessing() {
        state.registerNotificationCallback(mockStateMachine);

        IE2PayloadCallback<ScanNotification> callback = captureCallback();
        callback.onReceiveNotification("com.hp.test", null);

        // Both null and non-null package triggers early return if notification is null
    }

    @Test
    public void GivenCallback_WhenNotJobNotification_ThenNoStatusUpdate() {
        when(mockNotification.isJobNotification()).thenReturn(false);
        state.registerNotificationCallback(mockStateMachine);

        IE2PayloadCallback<ScanNotification> callback = captureCallback();
        callback.onReceiveNotification("com.hp.test", mockNotification);

        verify(mockStateMachine, never()).getJobBundle();
    }

    @Test
    public void GivenCallback_WhenJobNotificationWithNullContent_ThenNoStatusUpdate() {
        when(mockNotification.isJobNotification()).thenReturn(true);
        when(mockNotification.getJobNotification()).thenReturn(null);
        state.registerNotificationCallback(mockStateMachine);

        IE2PayloadCallback<ScanNotification> callback = captureCallback();
        callback.onReceiveNotification("com.hp.test", mockNotification);

        verify(mockStateMachine, never()).getJobBundle();
    }

    @Test
    public void GivenCallback_WhenValidJobNotification_WithNullScanJobStatus_ThenNoStatusUpdate() {
        when(mockNotification.isJobNotification()).thenReturn(true);
        when(mockNotification.getJobNotification()).thenReturn(mockScanJobNotificationContent);
        when(mockScanJobNotificationContent.getScanJobId())
                .thenReturn(new ScanJobIdentifier(UUID.fromString(TEST_JOB_ID)));
        when(mockScanJobNotificationContent.getScanJobStatus()).thenReturn(null);
        state.registerNotificationCallback(mockStateMachine);

        IE2PayloadCallback<ScanNotification> callback = captureCallback();
        callback.onReceiveNotification("com.hp.test", mockNotification);

        // scanJobStatus == null → no updateJobStatus call
        verify(mockStateMachine, never()).getJobBundle();
    }

    @Test
    public void GivenCallback_WhenValidJobNotification_WithScanJobStatus_ThenUpdateJobStatus() {
        when(mockNotification.isJobNotification()).thenReturn(true);
        when(mockNotification.getJobNotification()).thenReturn(mockScanJobNotificationContent);
        when(mockScanJobNotificationContent.getScanJobId())
                .thenReturn(new ScanJobIdentifier(UUID.fromString(TEST_JOB_ID)));
        when(mockScanJobNotificationContent.getScanJobStatus()).thenReturn(mockScanJobStatus);
        when(mockStateMachine.getJobBundle()).thenReturn(mockJobBundle);
        when(mockScanJobStatus.getTotalImagesScanned()).thenReturn(new Unsigned32(5L));

        state.registerNotificationCallback(mockStateMachine);

        IE2PayloadCallback<ScanNotification> callback = captureCallback();
        callback.onReceiveNotification("com.hp.test", mockNotification);

        // Should reach updateJobStatus and call getJobBundle
        verify(mockStateMachine).getJobBundle();
    }

    @Test
    public void GivenCallback_WhenValidNotification_WithNullBundle_ThenEarlyReturn() {
        when(mockNotification.isJobNotification()).thenReturn(true);
        when(mockNotification.getJobNotification()).thenReturn(mockScanJobNotificationContent);
        when(mockScanJobNotificationContent.getScanJobId())
                .thenReturn(new ScanJobIdentifier(UUID.fromString(TEST_JOB_ID)));
        when(mockScanJobNotificationContent.getScanJobStatus()).thenReturn(mockScanJobStatus);
        when(mockStateMachine.getJobBundle()).thenReturn(null);

        state.registerNotificationCallback(mockStateMachine);

        IE2PayloadCallback<ScanNotification> callback = captureCallback();
        callback.onReceiveNotification("com.hp.test", mockNotification);

        // updateJobStatus with null bundle should return early
        verify(mockStateMachine).getJobBundle();
    }

    @Test
    public void GivenCallback_WhenMismatchedJobId_ThenNoStatusUpdate() {
        when(mockNotification.isJobNotification()).thenReturn(true);
        when(mockNotification.getJobNotification()).thenReturn(mockScanJobNotificationContent);
        when(mockScanJobNotificationContent.getScanJobId())
                .thenReturn(new ScanJobIdentifier(UUID.randomUUID()));
        state.registerNotificationCallback(mockStateMachine);

        IE2PayloadCallback<ScanNotification> callback = captureCallback();
        callback.onReceiveNotification("com.hp.test", mockNotification);

        verify(mockStateMachine, never()).getJobBundle();
    }

    @Test
    public void GivenCallback_WhenNullScanJobId_ThenNoStatusUpdate() {
        when(mockNotification.isJobNotification()).thenReturn(true);
        when(mockNotification.getJobNotification()).thenReturn(mockScanJobNotificationContent);
        when(mockScanJobNotificationContent.getScanJobId()).thenReturn(null);
        state.registerNotificationCallback(mockStateMachine);

        IE2PayloadCallback<ScanNotification> callback = captureCallback();
        callback.onReceiveNotification("com.hp.test", mockNotification);

        verify(mockStateMachine, never()).getJobBundle();
    }

    // ===== Helper =====

    @SuppressWarnings("unchecked")
    private IE2PayloadCallback<ScanNotification> captureCallback() {
        ArgumentCaptor<IE2PayloadCallback> captor = ArgumentCaptor.forClass(IE2PayloadCallback.class);
        verify(mockScanJobService).registerNotificationCallback(captor.capture());
        return captor.getValue();
    }
}
