package com.hp.jetadvantage.link.services.scanlet.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.os.Bundle;
import android.os.Looper;

import com.hp.ext.service.scanJob.ScanJob_Cancel;
import com.hp.jetadvantage.link.common.intents.ScanJobCancelIntent;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceScanJobService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ScanJobIntentServiceUnitTest {
    public static String EXTRA_JOB_ID = "JOB_ID";
    public static String EXTRA_APP_PACKAGE_NAME = "APP_PACKAGE_NAME";
    @Mock
    Looper mockLooper;
    @Mock
    Bundle mockBundle;
    @Mock
    ScanJobCancelIntent mockCancelIntent;
    @Mock
    ScanJobIntentServiceStateMachine mockHandler;
    @Mock
    IDeviceScanJobService mockScanJobService;
    private ScanJobIntentService scanJobIntentService;

    @Before
    public void setUp() {
        scanJobIntentService = new ScanJobIntentService();
        scanJobIntentService.setHandler(mockHandler);
        scanJobIntentService.setScanJobService(mockScanJobService);
    }

    @Test
    public void GivenScanJobIntentService_WhenCancelJobCalledWithEmptyIntent_ThenReturnsFalse() {
        when(mockCancelIntent.getStringExtra(EXTRA_JOB_ID)).thenReturn(null);
        boolean result = scanJobIntentService.cancelJob(mockCancelIntent);

        assertFalse(result);
        verify(mockScanJobService, never()).cancelScanJob(anyString(), anyString());
    }

    @Test
    public void GivenScanJobIntentService_WhenCancelJobCalledWithMismatchJobId_ThenReturnsFalse() {
        when(mockCancelIntent.getStringExtra(EXTRA_JOB_ID)).thenReturn("misMatchJobId");
        when(mockHandler.getJobId()).thenReturn("testJobId");

        boolean result = scanJobIntentService.cancelJob(mockCancelIntent);

        assertFalse(result);
        verify(mockScanJobService, never()).cancelScanJob(anyString(), anyString());
    }

    @Test
    public void GivenScanJobIntentService_WhenCancelJobCalledWithEmptyPackageName_ThenReturnsFalse() {
        String jobId = "testJobId";
        when(mockCancelIntent.getStringExtra(EXTRA_JOB_ID)).thenReturn(jobId);
        when(mockHandler.getJobId()).thenReturn(jobId);

        boolean result = scanJobIntentService.cancelJob(mockCancelIntent);

        assertFalse(result);
        verify(mockScanJobService, never()).cancelScanJob(anyString(), anyString());
    }

    @Test
    public void GivenScanJobIntentService_WhenCancelJobCalledWithValidIntent_thenReturnsTrue() {
        String jobId = "testJobId";
        String packageName = "testpackageName";
        when(mockCancelIntent.getStringExtra(EXTRA_JOB_ID)).thenReturn(jobId);
        when(mockCancelIntent.getStringExtra(EXTRA_APP_PACKAGE_NAME)).thenReturn(packageName);
        when(mockHandler.getJobId()).thenReturn(jobId);
        when(mockScanJobService.cancelScanJob(packageName, jobId)).thenReturn(new ScanJob_Cancel());

        boolean result = scanJobIntentService.cancelJob(mockCancelIntent);

        verify(mockScanJobService, times(1)).cancelScanJob(eq(packageName), eq(jobId));
        assertTrue(result);
    }
}
