package com.hp.jetadvantage.link.services.copylet.service;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceCopyJobService;
import com.hp.jetadvantage.link.services.common.ssp.AbstractReporter;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.ReportErrorState;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CreatingCopyJobStateStoredTest {
    static final String EXTRA_REQ_ID = "reqIDExtra";
    static final String EXTRA_APP_PACKAGE_NAME = "appPackageName";
    static final String TEST_REQ_ID = "cf36d642-cef3-45e5-a520-3588b60268c7";
    static final String TEST_PACKAGE = "com.hp.test";

    @Mock private Intent mockIntent;
    @Mock private BaseJobIntentServiceStateMachine mockStateMachine;
    @Mock private Bundle mockBundle;
    @Mock private AbstractReporter mockReporter;
    @Mock private IDeviceCopyJobService mockCopyJobService;
    @Mock private Context mockContext;

    private CreatingCopyJobState state;

    @Before
    public void setUp() {
        when(mockStateMachine.getContentResolver()).thenReturn(null);
        lenient().when(mockStateMachine.getContext()).thenReturn(mockContext);
        state = new CreatingCopyJobState(true, mockIntent, mockCopyJobService);
    }

    // ===== RELEASE tests =====

    @Test
    public void GivenNullStoredJobAttributes_WhenProcessRelease_ThenReportErrorReturned() {
        setupBasicIntentMocks(CopyJobIntentService.PARAMS_TYPE_RELEASE);
        // storedJobAttrExtra not in bundle → defaults to false → reqParams.getStoredJobAttributes() == null

        BaseJobIntentServiceState result = state.initializeJob(mockIntent, mockStateMachine);
        assertTrue(result instanceof ReportErrorState);
        assertEquals(Result.ErrorCode.INVALID_PARAM, ((ReportErrorState) result).getErrorCode());
    }

    // ===== Unknown params type =====

    @Test
    public void GivenUnknownParamsType_WhenInitialize_ThenReportErrorReturned() {
        setupBasicIntentMocks("UNKNOWN_TYPE");

        BaseJobIntentServiceState result = state.initializeJob(mockIntent, mockStateMachine);
        assertTrue(result instanceof ReportErrorState);
        assertEquals(Result.ErrorCode.INVALID_PARAM, ((ReportErrorState) result).getErrorCode());
    }

    // ===== Helper Methods =====

    private void setupBasicIntentMocks(String paramsType) {
        when(mockIntent.getStringExtra(CopyJobIntentService.PARAMS_TYPE)).thenReturn(paramsType);
        when(mockIntent.getBundleExtra(CopyJobIntentService.EXTRA_PARAMS)).thenReturn(mockBundle);
        when(mockBundle.containsKey(EXTRA_REQ_ID)).thenReturn(true);
        when(mockBundle.getString(EXTRA_REQ_ID)).thenReturn(TEST_REQ_ID);
        when(mockBundle.containsKey(EXTRA_APP_PACKAGE_NAME)).thenReturn(true);
        when(mockBundle.getString(EXTRA_APP_PACKAGE_NAME)).thenReturn(TEST_PACKAGE);
        when(mockStateMachine.getReporterToApp()).thenReturn(mockReporter);
    }
}
