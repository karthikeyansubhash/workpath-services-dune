package com.hp.jetadvantage.link.services.copylet.service;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Intent;
import android.os.Bundle;

import com.hp.ext.service.copy.CopyJob;
import com.hp.ext.service.copy.CopyJobIdentifier;
import com.hp.ext.service.copy.CopyJob_Create;
import com.hp.ext.service.copy.DefaultOptions;
import com.hp.jetadvantage.link.api.CapabilitiesExceededException;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.copier.CopyAttributes;
import com.hp.jetadvantage.link.api.copier.CopyAttributesCaps;
import com.hp.jetadvantage.link.api.copier.CopyAttributesCapsCreator;
import com.hp.jetadvantage.link.api.copier.CopyAttributesReader;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceCopyJobService;
import com.hp.jetadvantage.link.services.common.ssp.AbstractReporter;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.EndState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.ReportErrorState;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public class CreatingCopyJobStateTest {
    static final String EXTRA_REQ_ID = "reqIDExtra";
    static final String EXTRA_APP_PACKAGE_NAME = "appPackageName";
    static final String EXTRA_COPY_ATTRIBUTES = "copyAttrExtra";
    static final String TEST_REQ_ID = "cf36d642-cef3-45e5-a520-3588b60268c7";
    static final String TEST_JOB_ID = "c0a6b306-ff89-49d9-863a-8a3ab374c70b";

    @Mock
    private Intent mockIntent;
    @Mock
    private BaseJobIntentServiceStateMachine mockStateMachine;
    @Mock
    private Bundle mockBundle;
    @Mock
    private AbstractReporter mockReporter;
    @Mock
    private IDeviceCopyJobService mockDeviceCopyJobService;

    private CreatingCopyJobState creatingCopyJobState;

    @Before
    public void setUp() {
        //MockitoAnnotations.initMocks(this);
        when(mockStateMachine.getContentResolver()).thenReturn(null);
        creatingCopyJobState = new CreatingCopyJobState(true, mockIntent, mockDeviceCopyJobService);
    }

    @Test
    public void GivenCreatingCopyJobState_WhenInitializeJobIsCalled_WithNullExtraParams_ThenEndStateIsReturned() {
        when(mockIntent.getBundleExtra(CopyJobIntentService.EXTRA_PARAMS)).thenReturn(null);

        BaseJobIntentServiceState result = creatingCopyJobState.initializeJob(mockIntent, mockStateMachine);
        assertTrue(result instanceof EndState);
    }

    @Test
    public void GivenCreatingCopyJobState_WhenInitializeJobIsCalled_WithEmptyRID_ThenEndStateIsReturned() {
        when(mockIntent.getBundleExtra(CopyJobIntentService.EXTRA_PARAMS)).thenReturn(mockBundle);
        when(mockBundle.containsKey(EXTRA_REQ_ID)).thenReturn(false);

        BaseJobIntentServiceState result = creatingCopyJobState.initializeJob(mockIntent, mockStateMachine);
        assertTrue(result instanceof EndState);
    }

    @Test
    public void GivenCreatingCopyJobState_WhenInitializeJobIsCalled_WithEmptyPackageName_ThenEndStateIsReturned() {
        when(mockIntent.getBundleExtra(CopyJobIntentService.EXTRA_PARAMS)).thenReturn(mockBundle);
        when(mockBundle.containsKey(EXTRA_REQ_ID)).thenReturn(true);
        when(mockBundle.getString(EXTRA_REQ_ID)).thenReturn(TEST_REQ_ID);
        when(mockBundle.containsKey(EXTRA_APP_PACKAGE_NAME)).thenReturn(false);

        BaseJobIntentServiceState result = creatingCopyJobState.initializeJob(mockIntent, mockStateMachine);
        assertTrue(result instanceof EndState);
    }

    @Test
    public void GivenCreatingCopyJobState_WhenInitializeJobIsCalled_WithRidAndPackageName_ThenRidAndPackageNameSetInStateMachine() {
        when(mockIntent.getStringExtra(CopyJobIntentService.PARAMS_TYPE)).thenReturn(null);
        when(mockIntent.getBundleExtra(CopyJobIntentService.EXTRA_PARAMS)).thenReturn(mockBundle);
        when(mockBundle.containsKey(EXTRA_REQ_ID)).thenReturn(true);
        when(mockBundle.getString(EXTRA_REQ_ID)).thenReturn(TEST_REQ_ID);
        when(mockBundle.containsKey(EXTRA_APP_PACKAGE_NAME)).thenReturn(true);
        when(mockBundle.getString(EXTRA_APP_PACKAGE_NAME)).thenReturn("testPackageName");
        when(mockStateMachine.getReporterToApp()).thenReturn(mockReporter);

        BaseJobIntentServiceState result = creatingCopyJobState.initializeJob(mockIntent, mockStateMachine);
        assertTrue(result instanceof ReportErrorState);

        verify(mockStateMachine).setJobRid(TEST_REQ_ID);
        verify(mockStateMachine).setTargetPackageName("testPackageName");
        verify(mockReporter).setTargetPackage("testPackageName");
    }

    @Test
    public void GivenCreatingCopyJobState_WhenInitializeJobIsCalled_WithInvalidParamType_ThenReportErrorStateIsReturned() {
        when(mockIntent.getStringExtra(CopyJobIntentService.PARAMS_TYPE)).thenReturn("");
        when(mockIntent.getBundleExtra(CopyJobIntentService.EXTRA_PARAMS)).thenReturn(mockBundle);
        when(mockBundle.containsKey(EXTRA_REQ_ID)).thenReturn(true);
        when(mockBundle.getString(EXTRA_REQ_ID)).thenReturn(TEST_REQ_ID);
        when(mockBundle.containsKey(EXTRA_APP_PACKAGE_NAME)).thenReturn(true);
        when(mockBundle.getString(EXTRA_APP_PACKAGE_NAME)).thenReturn("testPackageName");
        when(mockStateMachine.getReporterToApp()).thenReturn(mockReporter);

        BaseJobIntentServiceState result = creatingCopyJobState.initializeJob(mockIntent, mockStateMachine);
        assertTrue(result instanceof ReportErrorState);
        assertEquals(Result.RESULT_FAIL, ((ReportErrorState) result).getResult());
        assertEquals(Result.ErrorCode.INVALID_PARAM, ((ReportErrorState) result).getErrorCode());
    }

    @Test
    public void GivenCreatingCopyJobState_WhenInitializeJobIsCalled_WithCopyParamTypeEmptyCopyAttributes_ThenReportErrorStateIsReturned() {
        when(mockIntent.getStringExtra(CopyJobIntentService.PARAMS_TYPE)).thenReturn(CopyJobIntentService.PARAMS_TYPE_COPY);
        when(mockIntent.getBundleExtra(CopyJobIntentService.EXTRA_PARAMS)).thenReturn(mockBundle);
        when(mockBundle.containsKey(EXTRA_REQ_ID)).thenReturn(true);
        when(mockBundle.getString(EXTRA_REQ_ID)).thenReturn(TEST_REQ_ID);
        when(mockBundle.containsKey(EXTRA_APP_PACKAGE_NAME)).thenReturn(true);
        when(mockBundle.getString(EXTRA_APP_PACKAGE_NAME)).thenReturn("testPackageName");
        when(mockBundle.containsKey(EXTRA_COPY_ATTRIBUTES)).thenReturn(false);
        when(mockStateMachine.getReporterToApp()).thenReturn(mockReporter);


        BaseJobIntentServiceState result = creatingCopyJobState.initializeJob(mockIntent, mockStateMachine);
        assertTrue(result instanceof ReportErrorState);
        assertEquals(Result.RESULT_FAIL, ((ReportErrorState) result).getResult());
        assertEquals(Result.ErrorCode.INVALID_PARAM, ((ReportErrorState) result).getErrorCode());
    }

    @Test
    public void GivenCreatingCopyJobState_WhenInitializeJobIsCalled_WithValidParam_ThenMonitoringStateIsReturned() {

        DefaultOptions defaultCopyOptions = new DefaultOptions();
        CopyJob copyJob = new CopyJob();
        copyJob.setCopyJobId(new CopyJobIdentifier(UUID.fromString(TEST_JOB_ID)));

        when(mockIntent.getStringExtra(CopyJobIntentService.PARAMS_TYPE)).thenReturn(CopyJobIntentService.PARAMS_TYPE_COPY);
        when(mockIntent.getBundleExtra(CopyJobIntentService.EXTRA_PARAMS)).thenReturn(mockBundle);
        when(mockBundle.containsKey(EXTRA_REQ_ID)).thenReturn(true);
        when(mockBundle.getString(EXTRA_REQ_ID)).thenReturn(TEST_REQ_ID);
        when(mockBundle.containsKey(EXTRA_APP_PACKAGE_NAME)).thenReturn(true);
        when(mockBundle.getString(EXTRA_APP_PACKAGE_NAME)).thenReturn("testPackageName");
        when(mockBundle.containsKey(EXTRA_COPY_ATTRIBUTES)).thenReturn(true);
        when(mockStateMachine.getReporterToApp()).thenReturn(mockReporter);
        when(mockDeviceCopyJobService.getDefaultOptions(anyString())).thenReturn(defaultCopyOptions);
        when(mockDeviceCopyJobService.createCopyJob(anyString(), any(CopyJob_Create.class))).thenReturn(copyJob);
        when(mockBundle.getParcelable(EXTRA_COPY_ATTRIBUTES)).thenReturn(getTestCopyAttributes());

        doAnswer(invocation -> {
            CopyAttributesReader reader = invocation.getArgument(0);
            when(mockStateMachine.getJobAttributesReader(CopyAttributesReader.class)).thenReturn(reader);
            return null;
        }).when(mockStateMachine).setJobAttributesReader(any(CopyAttributesReader.class));


        BaseJobIntentServiceState result = creatingCopyJobState.initializeJob(mockIntent, mockStateMachine);
        assertTrue(result instanceof MonitoringCopyJobState);
        verify(mockStateMachine).setJobId(TEST_JOB_ID);
    }

    @Test
    public void GivenCreatingCopyJobState_WhenInitializeJobIsCalled_CopyJobCreationFailed_ThenReportErrorStateIsReturned() {
        DefaultOptions defaultCopyOptions = new DefaultOptions();

        when(mockIntent.getStringExtra(CopyJobIntentService.PARAMS_TYPE)).thenReturn(CopyJobIntentService.PARAMS_TYPE_COPY);
        when(mockIntent.getBundleExtra(CopyJobIntentService.EXTRA_PARAMS)).thenReturn(mockBundle);
        when(mockBundle.containsKey(EXTRA_REQ_ID)).thenReturn(true);
        when(mockBundle.getString(EXTRA_REQ_ID)).thenReturn(TEST_REQ_ID);
        when(mockBundle.containsKey(EXTRA_APP_PACKAGE_NAME)).thenReturn(true);
        when(mockBundle.getString(EXTRA_APP_PACKAGE_NAME)).thenReturn("testPackageName");
        when(mockBundle.containsKey(EXTRA_COPY_ATTRIBUTES)).thenReturn(true);
        when(mockStateMachine.getReporterToApp()).thenReturn(mockReporter);
        when(mockDeviceCopyJobService.getDefaultOptions(anyString())).thenReturn(defaultCopyOptions);
        when(mockDeviceCopyJobService.createCopyJob(anyString(), any(CopyJob_Create.class))).thenThrow(new RuntimeException(new IOException("Test Exception")));
        when(mockBundle.getParcelable(EXTRA_COPY_ATTRIBUTES)).thenReturn(getTestCopyAttributes());

        doAnswer(invocation -> {
            CopyAttributesReader reader = invocation.getArgument(0);
            when(mockStateMachine.getJobAttributesReader(CopyAttributesReader.class)).thenReturn(reader);
            return null;
        }).when(mockStateMachine).setJobAttributesReader(any(CopyAttributesReader.class));


        BaseJobIntentServiceState result = creatingCopyJobState.initializeJob(mockIntent, mockStateMachine);
        assertTrue(result instanceof ReportErrorState);
        assertEquals(Result.RESULT_FAIL, ((ReportErrorState) result).getResult());
        assertEquals(Result.ErrorCode.SERVICE_ERROR, ((ReportErrorState) result).getErrorCode());
    }

    private CopyAttributes getTestCopyAttributes() {
        CopyAttributesCapsCreator.Builder builder = new CopyAttributesCapsCreator.Builder();
        builder.addJobExecutionMode(CopyAttributes.JobExecutionMode.NORMAL);
        builder.addStapleOption(CopyAttributes.StapleOption.NONE);
        builder.addPunchMode(CopyAttributes.PunchMode.NONE);
        builder.addFoldMode(CopyAttributes.FoldMode.NONE);

        CopyAttributesCaps copyAttributesCaps = new CopyAttributesCaps(builder.build());
        CopyAttributes.CopyBuilder copyBuilder = new CopyAttributes.CopyBuilder().setColorMode(CopyAttributes.ColorMode.DEFAULT).setScanSize(CopyAttributes.ScanSize.DEFAULT);

        CopyAttributes copyAttributes = null;
        try {
            copyAttributes = copyBuilder.build(copyAttributesCaps);
        } catch (CapabilitiesExceededException e) {
            fail();
        }
        return copyAttributes;
    }
}