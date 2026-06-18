package com.hp.jetadvantage.link.services.copylet.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;

import com.hp.jetadvantage.link.api.job.CopyJobData;
import com.hp.jetadvantage.link.api.job.CopyJobState;
import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.services.common.ssp.AbstractReporter;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.CreatingJobState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.InitState;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CopyJobIntentServiceStateMachineUnitTest {
    @Mock
    CopyJobIntentService mockCopyJobIntentService;
    @Mock
    Looper mockLooper;
    @Mock
    AbstractReporter mockReporter;
    @Mock
    Bundle mockJobBundle;
    @Mock
    Intent mockIntent;

    private CopyJobIntentServiceStateMachine copyJobIntentServiceStateMachine;

    @Before
    public void setUp() {
        copyJobIntentServiceStateMachine = new CopyJobIntentServiceStateMachine(mockCopyJobIntentService, mockLooper, "testCopy", mockReporter, new InitState());
    }

    @Test
    public void GivenCopyJobIntentServiceStateMachine_WhenUpdateCompletedJobStateCalled_ThenSetJobState() {
        JobInfo jobInfo = new JobInfo();
        CopyJobData copyJobData = new CopyJobData();
        jobInfo.setJobData(copyJobData);
        when(mockJobBundle.getParcelable(anyString())).thenReturn(jobInfo);

        copyJobIntentServiceStateMachine.setJobBundle(mockJobBundle);
        copyJobIntentServiceStateMachine.updateCompletedJobState();

        assertEquals(CopyJobState.State.COMPLETED, copyJobData.getJobState().getState());
    }

    @Test
    public void GivenCopyJobIntentServiceStateMachine_WhenUpdateFailedJobStateCalled_ThenSetJobState() {
        JobInfo jobInfo = new JobInfo();
        CopyJobData copyJobData = new CopyJobData();
        jobInfo.setJobData(copyJobData);
        when(mockJobBundle.getParcelable(anyString())).thenReturn(jobInfo);

        copyJobIntentServiceStateMachine.setJobBundle(mockJobBundle);
        copyJobIntentServiceStateMachine.updateFailedJobState();

        assertEquals(CopyJobState.State.FAILED, copyJobData.getJobState().getState());
    }

    @Test
    public void GivenCopyJobIntentServiceStateMachine_WhenUpdateCanceledJobState_ThenSetJobState() {
        JobInfo jobInfo = new JobInfo();
        CopyJobData copyJobData = new CopyJobData();
        jobInfo.setJobData(copyJobData);
        when(mockJobBundle.getParcelable(anyString())).thenReturn(jobInfo);

        copyJobIntentServiceStateMachine.setJobBundle(mockJobBundle);
        copyJobIntentServiceStateMachine.updateCanceledJobState();

        assertEquals(CopyJobState.State.CANCELED, copyJobData.getJobState().getState());
    }

    @Test
    public void GivenCopyJobIntentServiceStateMachine_WhenCreateCreatingJobStateCalled_ThenReturnCreatingScanJobState() {
        CreatingJobState result = copyJobIntentServiceStateMachine.createCreatingJobState(mockIntent);

        assertNotNull(result);
        assertTrue(result instanceof CreatingCopyJobState);
    }
}
