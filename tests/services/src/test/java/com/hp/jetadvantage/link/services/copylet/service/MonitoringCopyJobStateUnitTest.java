package com.hp.jetadvantage.link.services.copylet.service;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.os.Bundle;

import com.hp.ext.service.copy.CopyJobIdentifier;
import com.hp.ext.service.copy.CopyJobNotificationContent;
import com.hp.ext.service.copy.CopyJobStatus;
import com.hp.ext.service.copy.CopyNotification;
import com.hp.ext.types.job.JobActivityEvent;
import com.hp.ext.types.job.JobActivityState;
import com.hp.ext.types.job.JobDoneStatus;
import com.hp.ext.types.protocol.Unsigned32;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.job.CopyJobState;
import com.hp.jetadvantage.link.api.job.Joblet;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceCopyJobService;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public class MonitoringCopyJobStateUnitTest {

    private static final String TEST_JOB_ID = "b2c3d4e5-f6a7-8901-bcde-f12345678901";
    @Mock
    private CopyJobIntentServiceStateMachine mockStateMachine;
    @Mock
    private IDeviceCopyJobService mockCopyJobService;
    @Mock
    private CopyNotification mockCopyNotification;
    @Mock
    private CopyJobNotificationContent mockCopyJobNotificationContent;
    @Mock
    private CopyJobStatus mockCopyJobStatus;
    @Mock
    private Bundle mockJobBundle;

    private MonitoringCopyJobState monitoringCopyJobState;

    @Before
    public void setUp() {
        monitoringCopyJobState = new MonitoringCopyJobState(TEST_JOB_ID);
        monitoringCopyJobState.copyJobService = mockCopyJobService;
    }

    @Test
    public void GivenMonitoringCopyJobState_WhenRegisterNotificationCallback_ThenCopyNotificationCallbackCreated() {
        monitoringCopyJobState.registerNotificationCallback(mockStateMachine);

        verify(mockCopyJobService, times(1)).registerNotificationCallback(any());
        assertNotNull(monitoringCopyJobState.copyNotificationCallback);
    }

    @Test
    public void GivenMonitoringCopyJobState_WhenUnregisterNotificationCallback_ThenCopyNotificationCallbackShouldBeUnregistered() {
        monitoringCopyJobState.unregisterNotificationCallback();

        verify(mockCopyJobService, times(1)).unRegisterNotificationCallback();
    }


    @Test
    public void GivenMonitoringCopyJobState_WhenUpdateJobStatus_ThenJobCountersShouldBeUpdated() {
        CopyJobStatus copyJobStatus = new CopyJobStatus();

        monitoringCopyJobState.updateJobStatus(mockJobBundle, copyJobStatus);

        verify(mockJobBundle).putInt(eq(Joblet.Keys.KEY_SCAN_IMAGE_COUNT), eq(0));
        verify(mockJobBundle).putInt(eq(Joblet.Keys.KEY_DST_TOTAL), eq(0));
    }

    @Test
    public void GivenMonitoringCopyJobState_WhenUpdateJobStatus_ThenJobCountersShouldBeUpdated2() {
        int totalImagesScanned = 1;
        CopyJobStatus copyJobStatus = new CopyJobStatus();

        copyJobStatus.setTotalImagesScanned(new Unsigned32(((long) totalImagesScanned)));

        monitoringCopyJobState.updateJobStatus(mockJobBundle, copyJobStatus);

        verify(mockJobBundle).putInt(eq(Joblet.Keys.KEY_SCAN_IMAGE_COUNT), eq(totalImagesScanned));
        verify(mockJobBundle).putInt(eq(Joblet.Keys.KEY_DST_TOTAL), eq(0));
    }

    @Test
    public void GivenMonitoringCopyJobState_WhenUpdateJobStatus_ThenJobCountersShouldBeUpdated3() {
        int totalImagesScanned = 10;
        int totalSheetsPrinted = 9;
        CopyJobStatus copyJobStatus = new CopyJobStatus();

        copyJobStatus.setTotalImagesScanned(new Unsigned32(((long) totalImagesScanned)));
        copyJobStatus.setTotalSheetsPrinted(new Unsigned32(((long) totalSheetsPrinted)));

        monitoringCopyJobState.updateJobStatus(mockJobBundle, copyJobStatus);

        verify(mockJobBundle).putInt(eq(Joblet.Keys.KEY_SCAN_IMAGE_COUNT), eq(totalImagesScanned));
        verify(mockJobBundle).putInt(eq(Joblet.Keys.KEY_DST_TOTAL), eq(totalSheetsPrinted));
    }

    @Test
    public void GivenMonitoringCopyJobState_WhenGetCopyJobStateWithScanningActivity_ThenCopyJobStateIsReturned() {
        CopyJobStatus copyJobStatus = new CopyJobStatus();

        java.util.List<com.hp.ext.types.job.JobActivityEvent> jobActivityEvents = new java.util.ArrayList<>();
        copyJobStatus.setScanningActivity(jobActivityEvents);

        CopyJobState copyJobState = monitoringCopyJobState.getCopyJobState(CopyJobState.State.ACTIVE, copyJobStatus);
        assertNull(copyJobState.getScanningState());

        JobActivityEvent startJobActivityEvent = new JobActivityEvent();
        startJobActivityEvent.setActivity(JobActivityState.JasStarted);
        jobActivityEvents.add(startJobActivityEvent);
        copyJobStatus.setScanningActivity(jobActivityEvents);

        copyJobState = monitoringCopyJobState.getCopyJobState(CopyJobState.State.ACTIVE, copyJobStatus);
        assertEquals(CopyJobState.ActivityState.STARTED, copyJobState.getScanningState());

        JobActivityEvent completedJobActivityEvent = new JobActivityEvent();
        completedJobActivityEvent.setActivity(JobActivityState.JasCompleted);
        jobActivityEvents.add(completedJobActivityEvent);
        copyJobStatus.setScanningActivity(jobActivityEvents);

        copyJobState = monitoringCopyJobState.getCopyJobState(CopyJobState.State.ACTIVE, copyJobStatus);
        assertEquals(CopyJobState.ActivityState.COMPLETED, copyJobState.getScanningState());
    }

    @Test
    public void GivenMonitoringCopyJobState_WhenGetCopyJobStateWithPrintingActivity_ThenCopyJobStateIsReturned() {
        CopyJobStatus copyJobStatus = new CopyJobStatus();

        java.util.List<com.hp.ext.types.job.JobActivityEvent> jobActivityEvents = new java.util.ArrayList<>();

        CopyJobState copyJobState = monitoringCopyJobState.getCopyJobState(CopyJobState.State.ACTIVE, copyJobStatus);
        assertNull(copyJobState.getPrintingState());

        JobActivityEvent startJobActivityEvent = new JobActivityEvent();
        startJobActivityEvent.setActivity(JobActivityState.JasStarted);
        jobActivityEvents.add(startJobActivityEvent);
        copyJobStatus.setPrintingActivity(jobActivityEvents);

        copyJobState = monitoringCopyJobState.getCopyJobState(CopyJobState.State.ACTIVE, copyJobStatus);
        assertEquals(CopyJobState.ActivityState.STARTED, copyJobState.getPrintingState());

        JobActivityEvent completedJobActivityEvent = new JobActivityEvent();
        completedJobActivityEvent.setActivity(JobActivityState.JasCompleted);
        jobActivityEvents.add(completedJobActivityEvent);
        copyJobStatus.setPrintingActivity(jobActivityEvents);

        copyJobState = monitoringCopyJobState.getCopyJobState(CopyJobState.State.ACTIVE, copyJobStatus);
        assertEquals(CopyJobState.ActivityState.COMPLETED, copyJobState.getPrintingState());
    }

    @Test
    public void GivenMonitoringCopyJobState_WhenOnReceiveNotificationCalledWithJobDoneSuccess_ThenCompletedReportMsgShouldBeSent() {
        monitoringCopyJobState.registerNotificationCallback(mockStateMachine);

        when(mockCopyNotification.getJobNotification()).thenReturn(mockCopyJobNotificationContent);
        when(mockCopyNotification.isJobNotification()).thenReturn(true);
        when(mockCopyJobNotificationContent.getCopyJobId())
                .thenReturn(new CopyJobIdentifier(UUID.fromString(TEST_JOB_ID)));
        when(mockCopyJobNotificationContent.getCopyJobStatus()).thenReturn(mockCopyJobStatus);
        when(mockCopyJobStatus.getJobDoneStatus()).thenReturn(JobDoneStatus.JdsSucceeded);
        when(mockStateMachine.sendMessage(any())).thenReturn(true);

        monitoringCopyJobState.copyNotificationCallback.onReceiveNotification("testPackageId", mockCopyNotification);

        verify(mockStateMachine, times(1)).obtainMessage(eq(BaseJobIntentServiceStateMachine.MSG_REPORT_COMPLETED), eq(Result.RESULT_OK), eq(-1), any());
        verify(mockStateMachine, times(1)).sendMessage(any());
    }

    @Test
    public void GivenMonitoringCopyJobState_WhenOnReceiveNotificationCalledWithJobDoneCanceled_ThenCancelReportMsgShouldBeSent() {
        monitoringCopyJobState.registerNotificationCallback(mockStateMachine);

        when(mockCopyNotification.getJobNotification()).thenReturn(mockCopyJobNotificationContent);
        when(mockCopyNotification.isJobNotification()).thenReturn(true);
        when(mockCopyJobNotificationContent.getCopyJobId())
                .thenReturn(new CopyJobIdentifier(UUID.fromString(TEST_JOB_ID)));
        when(mockCopyJobNotificationContent.getCopyJobStatus()).thenReturn(mockCopyJobStatus);
        when(mockCopyJobStatus.getJobDoneStatus()).thenReturn(JobDoneStatus.JdsCanceled);
        when(mockStateMachine.sendMessage(any())).thenReturn(true);

        monitoringCopyJobState.copyNotificationCallback.onReceiveNotification("testPackageId", mockCopyNotification);

        verify(mockStateMachine, times(1)).obtainMessage(eq(BaseJobIntentServiceStateMachine.MSG_REPORT_CANCELLED), eq(Result.RESULT_FAIL), eq(-1), any());
        verify(mockStateMachine, times(1)).sendMessage(any());
    }

    @Test
    public void GivenMonitoringCopyJobState_WhenOnReceiveNotificationCalledWithJobDoneFailed_ThenFailedReportMsgShouldBeSent() {
        monitoringCopyJobState.registerNotificationCallback(mockStateMachine);

        when(mockCopyNotification.getJobNotification()).thenReturn(mockCopyJobNotificationContent);
        when(mockCopyNotification.isJobNotification()).thenReturn(true);
        when(mockCopyJobNotificationContent.getCopyJobId())
                .thenReturn(new CopyJobIdentifier(UUID.fromString(TEST_JOB_ID)));
        when(mockCopyJobNotificationContent.getCopyJobStatus()).thenReturn(mockCopyJobStatus);
        when(mockCopyJobStatus.getJobDoneStatus()).thenReturn(JobDoneStatus.JdsFailed);
        when(mockStateMachine.sendMessage(any())).thenReturn(true);

        monitoringCopyJobState.copyNotificationCallback.onReceiveNotification("testPackageId", mockCopyNotification);

        verify(mockStateMachine, times(1)).obtainMessage(eq(BaseJobIntentServiceStateMachine.MSG_REPORT_FAIL), eq(Result.RESULT_FAIL), eq(3), any());
        verify(mockStateMachine, times(1)).sendMessage(any());
    }
}
