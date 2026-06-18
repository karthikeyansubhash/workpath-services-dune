package com.hp.jetadvantage.link.services.copylet.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.os.Bundle;

import com.hp.ext.service.copy.CopyJob_Cancel;
import com.hp.jetadvantage.link.common.intents.ScanJobCancelIntent;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceCopyJobService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CopyJobIntentServiceUnitTest {
    private static final String EXTRA_JOB_ID = "JOB_ID";
    private static final String EXTRA_APP_PACKAGE_NAME = "APP_PACKAGE_NAME";
    @Mock
    ScanJobCancelIntent mockCancelIntent;
    @Mock
    CopyJobIntentServiceStateMachine mockHandler;
    @Mock
    IDeviceCopyJobService mockCopyJobService;
    @Mock
    Context mockContext;
    private CopyJobIntentService copyJobIntentService;


    @Before
    public void setUp() {
        copyJobIntentService = new CopyJobIntentService();
        copyJobIntentService.setHandler(mockHandler);
        copyJobIntentService.setScanJobService(mockCopyJobService);
    }

    @Test
    public void GivenCopyJobIntentService_WhenCancelJobCalledWithEmptyIntent_ThenReturnFalse() {
        when(mockCancelIntent.getStringExtra(EXTRA_JOB_ID)).thenReturn(null);
        boolean result = copyJobIntentService.cancelJob(mockCancelIntent);

        assertFalse(result);
        verify(mockCopyJobService, never()).cancelCopyJob(anyString(), anyString());
    }

    @Test
    public void GivenCopyJobIntentService_WhenCancelJobCalledWithMismatchedJobId_ThenReturnFalse() {
        when(mockCancelIntent.getStringExtra(EXTRA_JOB_ID)).thenReturn("misMatchJobId");
        when(mockHandler.getJobId()).thenReturn("testJobId");

        boolean result = copyJobIntentService.cancelJob(mockCancelIntent);

        assertFalse(result);
        verify(mockCopyJobService, never()).cancelCopyJob(anyString(), anyString());
    }

    @Test
    public void GivenCopyJobIntentService_WhenCancelJobCalledWithEmptyPackageName_ThenReturnFalse() {
        String jobId = "testJobId";
        when(mockCancelIntent.getStringExtra(EXTRA_JOB_ID)).thenReturn(jobId);
        when(mockHandler.getJobId()).thenReturn(jobId);

        boolean result = copyJobIntentService.cancelJob(mockCancelIntent);

        assertFalse(result);
        verify(mockCopyJobService, never()).cancelCopyJob(anyString(), anyString());
    }

    @Test
    public void GivenCopyJobIntentService_WhenCancelJobCalledWithValidIntent_ThenReturnTrue() {
        String jobId = "testJobId";
        String packageName = "testpackageName";
        when(mockCancelIntent.getStringExtra(EXTRA_JOB_ID)).thenReturn(jobId);
        when(mockCancelIntent.getStringExtra(EXTRA_APP_PACKAGE_NAME)).thenReturn(packageName);
        when(mockHandler.getJobId()).thenReturn(jobId);
        when(mockCopyJobService.cancelCopyJob(packageName, jobId)).thenReturn(new CopyJob_Cancel());

        boolean result = copyJobIntentService.cancelJob(mockCancelIntent);

        verify(mockCopyJobService, times(1)).cancelCopyJob(eq(packageName), eq(jobId));
        assertTrue(result);
    }
}
