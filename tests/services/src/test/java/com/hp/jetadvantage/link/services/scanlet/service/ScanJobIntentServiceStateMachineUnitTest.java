package com.hp.jetadvantage.link.services.scanlet.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;

import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.api.job.ScanJobData;
import com.hp.jetadvantage.link.api.job.ScanJobState;
import com.hp.jetadvantage.link.services.common.ssp.AbstractReporter;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.CreatingJobState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.InitState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.JobCompletedState;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ScanJobIntentServiceStateMachineUnitTest {
    @Mock
    ScanJobIntentService mockScanJobIntentService;

    @Mock
    Looper mockLooper;

    @Mock
    AbstractReporter mockReporter;
    @Mock
    Bundle mockJobBundle;
    @Mock
    Intent mockIntent;

    private ScanJobIntentServiceStateMachine scanJobIntentServiceStateMachine;

    @Before
    public void setUp() {
        scanJobIntentServiceStateMachine = new ScanJobIntentServiceStateMachine(mockScanJobIntentService, mockLooper, "testScan", mockReporter, new InitState());
    }

    @Test
    public void GivenScanJobIntentServiceStateMachine_WhenUpdateCompletedJobState_ThenSetJobState() {
        JobInfo jobInfo = new JobInfo();
        ScanJobData scanJobData = new ScanJobData();
        jobInfo.setJobData(scanJobData);
        when(mockJobBundle.getParcelable(anyString())).thenReturn(jobInfo);

        scanJobIntentServiceStateMachine.setJobBundle(mockJobBundle);
        scanJobIntentServiceStateMachine.updateCompletedJobState();

        assertEquals(scanJobData.getJobState().getState(), ScanJobState.State.COMPLETED);
    }

    @Test
    public void GivenScanJobIntentServiceStateMachine_WhenUpdateFailedJobState_ThenSetJobState() {
        JobInfo jobInfo = new JobInfo();
        ScanJobData scanJobData = new ScanJobData();
        jobInfo.setJobData(scanJobData);
        when(mockJobBundle.getParcelable(anyString())).thenReturn(jobInfo);

        scanJobIntentServiceStateMachine.setJobBundle(mockJobBundle);
        scanJobIntentServiceStateMachine.updateFailedJobState();

        assertEquals(scanJobData.getJobState().getState(), ScanJobState.State.FAILED);
    }

    @Test
    public void GivenScanJobIntentServiceStateMachine_WhenUpdateCanceledJobState_ThenSetJobState() {
        JobInfo jobInfo = new JobInfo();
        ScanJobData scanJobData = new ScanJobData();
        jobInfo.setJobData(scanJobData);
        when(mockJobBundle.getParcelable(anyString())).thenReturn(jobInfo);

        scanJobIntentServiceStateMachine.setJobBundle(mockJobBundle);
        scanJobIntentServiceStateMachine.updateCanceledJobState();

        assertEquals(scanJobData.getJobState().getState(), ScanJobState.State.CANCELED);
    }

    @Test
    public void GivenScanJobIntentServiceStateMachine_WhenCreateCreatingJobState_ThenReturnsCreatingScanJobState() {
        CreatingJobState result = scanJobIntentServiceStateMachine.createCreatingJobState(mockIntent);

        assertNotNull(result);
        assertTrue(result instanceof CreatingScanJobState);
    }

    @Test
    public void GivenScanJobIntentServiceStateMachine_WhenCreateJobCompletedState_ThenReturnsScanJobCompletedState() {
        JobCompletedState result = scanJobIntentServiceStateMachine.createJobCompletedState();

        assertNotNull(result);
        assertTrue(result instanceof ScanJobCompletedState);
    }
}
