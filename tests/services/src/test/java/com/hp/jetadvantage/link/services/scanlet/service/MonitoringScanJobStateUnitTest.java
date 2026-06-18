package com.hp.jetadvantage.link.services.scanlet.service;

import static com.hp.ext.service.scanJob.ScanJobIdentifier.createScanJobIdentifier;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.os.Bundle;

import com.hp.ext.service.scanJob.ScanJobNotificationContent;
import com.hp.ext.service.scanJob.ScanJobStatus;
import com.hp.ext.service.scanJob.ScanNotification;
import com.hp.ext.types.job.JobActivityEvent;
import com.hp.ext.types.job.JobActivityState;
import com.hp.ext.types.job.JobDoneStatus;
import com.hp.ext.types.protocol.Unsigned32;
import com.hp.jetadvantage.link.api.ILetObserver;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.api.job.Joblet;
import com.hp.jetadvantage.link.api.job.ScanJobData;
import com.hp.jetadvantage.link.api.job.ScanJobState;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceScanJobService;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MonitoringScanJobStateUnitTest {
    private final String VALID_JOB_ID = "854c5692-fb79-4701-abec-2673feb510c3";
    private final String INVALID_JOB_ID = "5187d0aa-f86e-4f4e-a970-9595680752b4";
    @Mock
    private ScanJobIntentServiceStateMachine mockStateMachine;
    @Mock
    private IDeviceScanJobService mockScanJobService;
    @Mock
    private ScanNotification mockScanNotification;
    @Mock
    private ScanJobNotificationContent mockScanJobNotificationContent;
    @Mock
    private ScanJobStatus mockScanJobStatus;
    @Mock
    private Bundle mockJobBundle;
    private MonitoringScanJobState monitoringScanJobState;

    @Before
    public void setUp() {
        monitoringScanJobState = new MonitoringScanJobState(VALID_JOB_ID);
        monitoringScanJobState.scanJobService = mockScanJobService;
    }

    @Test
    public void GivenMonitoringScanJobState_WhenRegisterNotificationCallback_ThenScanNotificationCallbackCreated() {
        monitoringScanJobState.registerNotificationCallback(mockStateMachine);

        verify(mockScanJobService, times(1)).registerNotificationCallback(any());
        assertNotNull(monitoringScanJobState.scanNotificationCallback);

//        monitoringScanJobStateSpy.scanNotificationCallback.onReceiveNotification("testPackageId", mockScanNotification);
//        verify(monitoringScanJobStateSpy, times(1)).updateJobStatus(any(), any());
    }

    @Test
    public void GivenMonitoringScanJobState_WhenUnregisterNotificationCallback_ThenScanNotificationCallbackShouldBeUnregistered() {
        monitoringScanJobState.unregisterNotificationCallback();

        verify(mockScanJobService, times(1)).unRegisterNotificationCallback();
    }

    @Test
    public void GivenMonitoringScanJobState_WhenUpdateJobStatus_ThenJobCountersShouldBeUpdatedInJobBundle() {
        int totalImagesScanned = 10;
        int totalImagesTransmitted = 9;
        ScanJobStatus scanJobStatus = new ScanJobStatus();
        scanJobStatus.setTotalImagesScanned(new Unsigned32(((long) totalImagesScanned)));
        scanJobStatus.setTotalImagesTransmitted(new Unsigned32(((long) totalImagesTransmitted)));

        monitoringScanJobState.updateJobStatus(mockJobBundle, scanJobStatus);

        verify(mockJobBundle).putInt(eq(Joblet.Keys.KEY_SCAN_IMAGE_COUNT), eq(totalImagesScanned));
        verify(mockJobBundle).putInt(eq(Joblet.Keys.KEY_DST_TOTAL), eq(totalImagesTransmitted));
    }

    @Test
    public void GivenMonitoringScanJobState_WhenUpdateJobStatus_ThenJobCountersShouldBeUpdatedInJobData() {
        int totalImagesScanned = 100;
        int totalImagesTransmitted = 99;
        ScanJobStatus scanJobStatus = new ScanJobStatus();
        scanJobStatus.setTotalImagesScanned(new Unsigned32(((long) totalImagesScanned)));
        scanJobStatus.setTotalImagesTransmitted(new Unsigned32(((long) totalImagesTransmitted)));

        ScanJobData scanJobData = new ScanJobData();
        JobInfo jobInfo = new JobInfo();
        jobInfo.setJobData(scanJobData);

        when(mockJobBundle.getParcelable(ILetObserver.Keys.KEY_JOB_INFO)).thenReturn(jobInfo);

        monitoringScanJobState.updateJobStatus(mockJobBundle, scanJobStatus);

        assertEquals("totalImagesScanned", totalImagesScanned, scanJobData.getImagesScanned());
        assertEquals("totalImagesTransmitted", totalImagesTransmitted, scanJobData.getImagesTransmitted());
    }

    @Test
    public void GivenMonitoringScanJobState_WhenUpdateJobStatus_ThenScanJobStateShouldBeUpdated() {
        ScanJobStatus scanJobStatus = new ScanJobStatus();

        // set E2 scanning activity to JasStarted
        java.util.List<JobActivityEvent> jobActivityEvents = new java.util.ArrayList<>();
        JobActivityEvent jobStartedActivityEvent = new JobActivityEvent();
        jobStartedActivityEvent.setActivity(JobActivityState.JasStarted);
        jobActivityEvents.add(jobStartedActivityEvent);
        scanJobStatus.setScanningActivity(jobActivityEvents);

        ScanJobData scanJobData = new ScanJobData();
        JobInfo jobInfo = new JobInfo();
        jobInfo.setJobData(scanJobData);

        when(mockJobBundle.getParcelable(ILetObserver.Keys.KEY_JOB_INFO)).thenReturn(jobInfo);
        monitoringScanJobState.updateJobStatus(mockJobBundle, scanJobStatus);

        // verify updated scanning state in scanJobData
        assertEquals("ScanningState", ScanJobState.ActivityState.STARTED, scanJobData.getJobState().getScanningState());
        assertEquals("ProcessingState", ScanJobState.ActivityState.NOT_STARTED, scanJobData.getJobState().getProcessingState());
        assertEquals("TransmittingState", ScanJobState.ActivityState.NOT_STARTED, scanJobData.getJobState().getTransmittingState());

        // set E2 scanning and transmitting activity to JasCompleted
        JobActivityEvent jobCompletedActivityEvent = new JobActivityEvent();
        jobCompletedActivityEvent.setActivity(JobActivityState.JasCompleted);
        jobActivityEvents.add(jobCompletedActivityEvent);
        scanJobStatus.setScanningActivity(jobActivityEvents);
        scanJobStatus.setTransmittingActivity(jobActivityEvents);

        monitoringScanJobState.updateJobStatus(mockJobBundle, scanJobStatus);
        // verify updated scanning state in scanJobData
        assertEquals("ScanningState", ScanJobState.ActivityState.COMPLETED, scanJobData.getJobState().getScanningState());
        assertEquals("ProcessingState", ScanJobState.ActivityState.COMPLETED, scanJobData.getJobState().getProcessingState());
        assertEquals("TransmittingState", ScanJobState.ActivityState.COMPLETED, scanJobData.getJobState().getTransmittingState());
    }

    @Test
    public void GivenMonitoringScanJobState_WhenOnReceiveNotificationCalledWithJobDoneSuccess_ThenCompletedReportMsgShouldBeSent() {
        monitoringScanJobState.registerNotificationCallback(mockStateMachine);

        when(mockScanNotification.getJobNotification()).thenReturn(mockScanJobNotificationContent);
        when(mockScanNotification.isJobNotification()).thenReturn(true);
        when(mockScanJobNotificationContent.getScanJobId()).thenReturn(createScanJobIdentifier(VALID_JOB_ID));
        when(mockScanJobNotificationContent.getScanJobStatus()).thenReturn(mockScanJobStatus);
        when(mockScanJobStatus.getJobDoneStatus()).thenReturn(JobDoneStatus.JdsSucceeded);
        when(mockStateMachine.sendMessage(any())).thenReturn(true);

        monitoringScanJobState.scanNotificationCallback.onReceiveNotification("testPackageId", mockScanNotification);

        verify(mockStateMachine, times(1)).obtainMessage(eq(BaseJobIntentServiceStateMachine.MSG_REPORT_COMPLETED), eq(Result.RESULT_OK), eq(-1), any());
        verify(mockStateMachine, times(1)).sendMessage(any());
    }

    @Test
    public void GivenMonitoringScanJobState_WhenOnReceiveNotificationCalledWithJobDoneSuccessWithInvalidJobId_ThenCompletedReportMsgShouldNotBeSent() {
        monitoringScanJobState.registerNotificationCallback(mockStateMachine);

        when(mockScanNotification.getJobNotification()).thenReturn(mockScanJobNotificationContent);
        when(mockScanNotification.isJobNotification()).thenReturn(true);
        when(mockScanJobNotificationContent.getScanJobId()).thenReturn(createScanJobIdentifier(INVALID_JOB_ID));

        monitoringScanJobState.scanNotificationCallback.onReceiveNotification("testPackageId", mockScanNotification);

        verify(mockStateMachine, never()).obtainMessage(eq(BaseJobIntentServiceStateMachine.MSG_REPORT_COMPLETED), eq(Result.RESULT_OK), eq(-1), any());
        verify(mockStateMachine, never()).sendMessage(any());
    }

    @Test
    public void GivenMonitoringScanJobState_WhenOnReceiveNotificationCalledWithJobDoneSuccessWithEmptyJobId_ThenCompletedReportMsgShouldNotBeSent() {
        monitoringScanJobState.registerNotificationCallback(mockStateMachine);

        when(mockScanNotification.getJobNotification()).thenReturn(mockScanJobNotificationContent);
        when(mockScanNotification.isJobNotification()).thenReturn(true);
        when(mockScanJobNotificationContent.getScanJobId()).thenReturn(null);

        monitoringScanJobState.scanNotificationCallback.onReceiveNotification("testPackageId", mockScanNotification);

        verify(mockStateMachine, never()).obtainMessage(eq(BaseJobIntentServiceStateMachine.MSG_REPORT_COMPLETED), eq(Result.RESULT_OK), eq(-1), any());
        verify(mockStateMachine, never()).sendMessage(any());
    }

    @Test
    public void GivenMonitoringScanJobState_WhenOnReceiveNotificationCalledWithJobDoneCanceled_ThenCancelReportMsgShouldBeSent() {
        monitoringScanJobState.registerNotificationCallback(mockStateMachine);

        when(mockScanNotification.getJobNotification()).thenReturn(mockScanJobNotificationContent);
        when(mockScanNotification.isJobNotification()).thenReturn(true);
        when(mockScanJobNotificationContent.getScanJobId()).thenReturn(createScanJobIdentifier(VALID_JOB_ID));
        when(mockScanJobNotificationContent.getScanJobStatus()).thenReturn(mockScanJobStatus);
        when(mockScanJobStatus.getJobDoneStatus()).thenReturn(JobDoneStatus.JdsCanceled);
        when(mockStateMachine.sendMessage(any())).thenReturn(true);

        monitoringScanJobState.scanNotificationCallback.onReceiveNotification("testPackageId", mockScanNotification);

        verify(mockStateMachine, times(1)).obtainMessage(eq(BaseJobIntentServiceStateMachine.MSG_REPORT_CANCELLED), eq(Result.RESULT_FAIL), eq(-1), any());
        verify(mockStateMachine, times(1)).sendMessage(any());
    }

    @Test
    public void GivenMonitoringScanJobState_WhenOnReceiveNotificationCalledWithJobDoneFailed_ThenFailedReportMsgShouldBeSent() {
        monitoringScanJobState.registerNotificationCallback(mockStateMachine);

        when(mockScanNotification.getJobNotification()).thenReturn(mockScanJobNotificationContent);
        when(mockScanNotification.isJobNotification()).thenReturn(true);
        when(mockScanJobNotificationContent.getScanJobId()).thenReturn(createScanJobIdentifier(VALID_JOB_ID));
        when(mockScanJobNotificationContent.getScanJobStatus()).thenReturn(mockScanJobStatus);
        when(mockScanJobStatus.getJobDoneStatus()).thenReturn(JobDoneStatus.JdsFailed);
        when(mockStateMachine.sendMessage(any())).thenReturn(true);

        monitoringScanJobState.scanNotificationCallback.onReceiveNotification("testPackageId", mockScanNotification);

        verify(mockStateMachine, times(1)).obtainMessage(eq(BaseJobIntentServiceStateMachine.MSG_REPORT_FAIL), eq(Result.RESULT_FAIL), eq(3), any());
        verify(mockStateMachine, times(1)).sendMessage(any());
    }

    @Test
    public void GivenMonitoringScanJobState_WhenOnReceiveNotificationCalledWithNullNotificationContent_ThenIgnore() {
        when(mockScanNotification.getJobNotification()).thenReturn(null);

        monitoringScanJobState.registerNotificationCallback(mockStateMachine);
        monitoringScanJobState.scanNotificationCallback.onReceiveNotification("testPackageId", mockScanNotification);

        verify(mockStateMachine, never()).obtainMessage(anyInt(), anyInt(), anyInt(), any());
        verify(mockStateMachine, never()).sendMessage(any());
    }

    @Test
    public void GivenMonitoringScanJobState_WhenOnReceiveNotificationCalledWithoutJobNotification_ThenIgnore() {
        when(mockScanNotification.getJobNotification()).thenReturn(mockScanJobNotificationContent);
        when(mockScanNotification.isJobNotification()).thenReturn(false);

        monitoringScanJobState.registerNotificationCallback(mockStateMachine);
        monitoringScanJobState.scanNotificationCallback.onReceiveNotification("testPackageId", mockScanNotification);

        verify(mockStateMachine, never()).obtainMessage(anyInt(), anyInt(), anyInt(), any());
        verify(mockStateMachine, never()).sendMessage(any());
    }
}
