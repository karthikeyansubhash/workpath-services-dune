package com.hp.jetadvantage.link.services.scanlet.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import java.io.File;

import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes;
import com.hp.jetadvantage.link.api.scanner.ScanAttributesReader;
import com.hp.jetadvantage.link.api.scanner.Scanlet;
import com.hp.jetadvantage.link.api.scanner.intent.ScanToRequestIntent;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.helper.SelectedPrinterHelper;
import com.hp.jetadvantage.link.common.model.PrinterInfo;
import com.hp.jetadvantage.link.services.common.ssp.AbstractReporter;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.EndState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.MonitoringJobState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.ReportErrorState;
import com.hp.jetadvantage.link.services.scanlet.adapter.ScanTicketAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.MockedConstruction;
import static org.mockito.Mockito.mockConstruction;
import android.net.Uri;

import com.hp.jetadvantage.link.api.scanner.ScanAttributesCaps;
import com.hp.jetadvantage.link.api.scanner.ScanAttributesCapsCreator;
import com.hp.jetadvantage.link.api.scanner.ScanletAttributes;
import com.hp.jetadvantage.link.common.model.ApiType;
import java.util.Collections;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CreatingScanJobStateUnitTest {
    @Mock
    private BaseJobIntentServiceStateMachine mockStateMachine;
    @Mock
    private Intent mockIntent;
    private CreatingScanJobState creatingScanJobState;
    @Mock
    private Bundle mockBundle;
    @Mock
    private ContentResolver mockContentResolver;
    @Mock
    private ScanAttributes mockScanAttributes;
    @Mock
    private AbstractReporter mockReporter;

    @Before
    public void setUp() {
    }

    @Test
    public void GivenCreatingScanJobState_WhenIntentParamsAreNull_ReturnsEndState() {
        Mockito.when(mockIntent.getBundleExtra(anyString())).thenReturn(mockBundle);
        Mockito.when(mockBundle.containsKey(anyString())).thenReturn(false);

        creatingScanJobState = new CreatingScanJobState(mockIntent);
        BaseJobIntentServiceState result = creatingScanJobState.initializeJob(mockIntent, mockStateMachine);

        assertTrue(result instanceof EndState);
    }

    @Test
    public void GivenCreatingScanJobState_WhenPrinterInfoIsNull_ReturnsReportErrorState() {
        Mockito.when(mockIntent.getBundleExtra(anyString())).thenReturn(mockBundle);
        Mockito.when(mockBundle.containsKey(eq("scanAttrExtra"))).thenReturn(true);
        Mockito.when(mockBundle.getParcelable(eq("scanAttrExtra"))).thenReturn(mockScanAttributes);
        Mockito.when(mockStateMachine.getReporterToApp()).thenReturn(mockReporter);
        Mockito.when(mockStateMachine.getContentResolver()).thenReturn(mockContentResolver);

        creatingScanJobState = new CreatingScanJobState(mockIntent);
        BaseJobIntentServiceState result = creatingScanJobState.initializeJob(mockIntent, mockStateMachine);

        assertTrue(result instanceof ReportErrorState);
        ReportErrorState reportErrorState = (ReportErrorState) result;
        assertEquals(Result.RESULT_FAIL, reportErrorState.getResult());
        assertEquals(Result.ErrorCode.CONNECTION_ERROR, reportErrorState.getErrorCode());
    }

    @Test
    public void GivenScanToMe_WhenClientVersionIsUnsupported_ThenInitializeJobReturnsError() throws Exception {
        try (MockedStatic<ScanToRequestIntent> mockedScanToRequestIntent = mockStatic(ScanToRequestIntent.class);
             MockedStatic<SelectedPrinterHelper> mockedSelectedPrinterHelper = mockStatic(SelectedPrinterHelper.class);
             MockedStatic<PrinterInfo> mockedPrinterInfo = mockStatic(PrinterInfo.class);
             MockedStatic<Environment> mockedEnvironment = mockStatic(Environment.class);
             MockedConstruction<Uri.Builder> mockedUriBuilder = mockConstruction(Uri.Builder.class, (mock, context) -> {
                 when(mock.scheme(anyString())).thenReturn(mock);
                 when(mock.authority(anyString())).thenReturn(mock);
                 when(mock.appendPath(anyString())).thenReturn(mock);
                 
                 Uri mockUri = mock(Uri.class);
                 when(mock.build()).thenReturn(mockUri);
             })) {

            // Environment mock setup
            File mockFile = mock(File.class);
            when(mockFile.getPath()).thenReturn("/sdcard/Download");
            mockedEnvironment.when(() -> Environment.getExternalStoragePublicDirectory(any())).thenReturn(mockFile);
            mockedEnvironment.when(Environment::getExternalStorageDirectory).thenReturn(mockFile);
            mockedEnvironment.when(Environment::getDataDirectory).thenReturn(mockFile);
            
            // setup
            Mockito.when(mockIntent.getBundleExtra(ScanJobIntentService.EXTRA_PARAMS)).thenReturn(mockBundle);

            ScanAttributes attributes = new ScanAttributes.MeBuilder()
                    .setFileName("test")
                    .build(createPermissiveScanAttributesCaps());

            ScanToRequestIntent.IntentParams params = new ScanToRequestIntent.IntentParams(
                attributes,
                null,
                "1",
                "com.test",
                null,
                null,
                null,
                null,
                Sdk.VERSION_LEVEL.THREE
            );
            
            mockedScanToRequestIntent.when(() -> ScanToRequestIntent.getIntentParams(mockBundle)).thenReturn(params);
            
            PrinterInfo mockPi = mock(PrinterInfo.class);
            mockedSelectedPrinterHelper.when(() -> SelectedPrinterHelper.get(any(ContentResolver.class))).thenReturn(mockPi);
            mockedPrinterInfo.when(() -> PrinterInfo.isEmpty(mockPi)).thenReturn(false);
            
            
            when(mockStateMachine.getContentResolver()).thenReturn(mockContentResolver);
            // getReporterToApp is needed
            when(mockStateMachine.getReporterToApp()).thenReturn(mockReporter);
            
            creatingScanJobState = spy(new CreatingScanJobState(mockIntent));
            doReturn(true).when(creatingScanJobState).isSupported(any());
            
            // Act
            BaseJobIntentServiceState result = creatingScanJobState.initializeJob(mockIntent, mockStateMachine);

            // Assert
            assertTrue(result instanceof ReportErrorState);
            ReportErrorState errorState = (ReportErrorState) result;
            assertEquals(Result.ErrorCode.NOT_SUPPORTED, errorState.getErrorCode());
        }
    }

    @Test
    public void GivenScanToMe_WhenClientVersionIsSupported_ThenInitializeJobReturnsMonitoringState() throws Exception {
        try (MockedStatic<ScanToRequestIntent> mockedScanToRequestIntent = mockStatic(ScanToRequestIntent.class);
             MockedStatic<SelectedPrinterHelper> mockedSelectedPrinterHelper = mockStatic(SelectedPrinterHelper.class);
             MockedStatic<PrinterInfo> mockedPrinterInfo = mockStatic(PrinterInfo.class);
             MockedStatic<ScanTicketAdapter> mockedScanTicketAdapter = mockStatic(ScanTicketAdapter.class);
             MockedStatic<Environment> mockedEnvironment = mockStatic(Environment.class);
             MockedConstruction<Uri.Builder> mockedUriBuilder = mockConstruction(Uri.Builder.class, (mock, context) -> {
                 when(mock.scheme(anyString())).thenReturn(mock);
                 when(mock.authority(anyString())).thenReturn(mock);
                 when(mock.appendPath(anyString())).thenReturn(mock);
                 
                 Uri mockUri = mock(Uri.class);
                 when(mock.build()).thenReturn(mockUri);
             })) {

            // Environment mock setup
            File mockFile = mock(File.class);
            when(mockFile.getPath()).thenReturn("/sdcard/Download");
            mockedEnvironment.when(() -> Environment.getExternalStoragePublicDirectory(any())).thenReturn(mockFile);
            mockedEnvironment.when(Environment::getExternalStorageDirectory).thenReturn(mockFile);
            mockedEnvironment.when(Environment::getDataDirectory).thenReturn(mockFile);
             
            // setup
            Mockito.when(mockIntent.getBundleExtra(ScanJobIntentService.EXTRA_PARAMS)).thenReturn(mockBundle);
            JobInfo mockJobInfo = mock(JobInfo.class);
            when(mockStateMachine.getJobInfo()).thenReturn(mockJobInfo);

            ScanAttributes attributes = new ScanAttributes.MeBuilder()
                    .setFileName("test")
                    .build(createPermissiveScanAttributesCaps());
            
            ScanToRequestIntent.IntentParams params = new ScanToRequestIntent.IntentParams(
                attributes,
                null,
                "1",
                "com.test",
                null,
                null,
                null,
                null,
                Sdk.VERSION_LEVEL.FOUR
            );
            
            mockedScanToRequestIntent.when(() -> ScanToRequestIntent.getIntentParams(mockBundle)).thenReturn(params);
            
            PrinterInfo mockPi = mock(PrinterInfo.class);
            when(mockPi.getApiType()).thenReturn(ApiType.OXP);
            mockedSelectedPrinterHelper.when(() -> SelectedPrinterHelper.get(any(ContentResolver.class))).thenReturn(mockPi);
            mockedPrinterInfo.when(() -> PrinterInfo.isEmpty(mockPi)).thenReturn(false);
            
            when(mockStateMachine.getContentResolver()).thenReturn(mockContentResolver);
            when(mockStateMachine.getReporterToApp()).thenReturn(mockReporter);
             // isSupported mocking is handled by spy
                
            mockedScanTicketAdapter.when(() -> ScanTicketAdapter.createScanJob(anyString(), any(), any(), any()))
                .thenReturn("jobId");
            
            creatingScanJobState = spy(new CreatingScanJobState(mockIntent));
            doReturn(true).when(creatingScanJobState).isSupported(any());
            
            // Act
            BaseJobIntentServiceState result = creatingScanJobState.initializeJob(mockIntent, mockStateMachine);

            // Assert
            assertTrue(result instanceof MonitoringScanJobState);
        }
    }

    private ScanAttributesCaps createPermissiveScanAttributesCaps() {
        return new ScanAttributesCaps(new ScanAttributesCapsCreator.Builder().build());
    }
}
