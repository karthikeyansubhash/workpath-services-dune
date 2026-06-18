package com.hp.jetadvantage.link.services.scanlet.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.os.Bundle;

import com.hp.jetadvantage.link.api.ILetObserver;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.api.job.ScanJobData;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes;
import com.hp.jetadvantage.link.api.scanner.ScanAttributesReader;
import com.hp.jetadvantage.link.common.utils.JsonParser;
import com.hp.jetadvantage.link.device.services.standard.common.PackageManagerHelper;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.JobFailedState;
import com.hp.jetadvantage.link.services.scanlet.ScanConstants;
import com.hp.jetadvantage.link.services.scanlet.model.Metadata;
import com.hp.jetadvantage.link.services.scanlet.model.MetadataFile;
import com.hp.jetadvantage.link.services.scanlet.service.postprocessor.LocalDestinationProcessor;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ScanJobCompletedStateUnitTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Mock ScanJobIntentServiceStateMachine mockStateMachine;
    @Mock Bundle mockJobBundle;
    @Mock JobInfo mockJobInfo;
    @Mock ScanJobData mockScanJobData;
    @Mock Context mockContext;
    @Mock ScanAttributesReader mockScanAttributesReader;
    @Mock LocalDestinationProcessor mockLocalProcessor;
    @Mock Metadata mockMetadata;
    @Mock MetadataFile mockMetadataFile;
    @Mock JsonParser mockJsonParser;

    private ScanJobCompletedState scanJobCompletedState;
    private MockedStatic<ScanConstants> mockedScanConstants;
    private MockedStatic<JsonParser> mockedJsonParserStatic;
    private MockedConstruction<PackageManagerHelper> mockedPmHelperConstruction;

    private File jobDir;
    private File metadataFile;
    private final String SOLUTION_UUID = "test-solution-uuid";
    private final String JOB_ID = "job-123";

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Setup common mocks
        when(mockStateMachine.getJobBundle()).thenReturn(mockJobBundle);
        when(mockJobBundle.getParcelable(ILetObserver.Keys.KEY_JOB_INFO)).thenReturn(mockJobInfo);
        when(mockJobInfo.getJobData()).thenReturn(mockScanJobData);
        when(mockJobInfo.getJobId()).thenReturn(JOB_ID);
        when(mockStateMachine.getContext()).thenReturn(mockContext);
        when(mockStateMachine.getTargetPackageName()).thenReturn("com.test.app");
        // Mock ScanAttributesReader logic
        when(mockStateMachine.getJobAttributesReader(ScanAttributesReader.class)).thenReturn(mockScanAttributesReader);

        // Setup Static mocks
        mockedScanConstants = mockStatic(ScanConstants.class);
        // We need to allow real access to constant fields if necessary, but fields are initialized at class load
        // checking LOCAL_FOLDER_DESTINATIONS logic
        // We can just rely on the real ScanConstants fields being loaded. 
        // We only stub the methods.
        mockedScanConstants.when(() -> ScanConstants.getOriginalFiles(anyString(), anyString()))
                .thenAnswer(invocation -> jobDir);

        mockedJsonParserStatic = mockStatic(JsonParser.class);
        mockedJsonParserStatic.when(JsonParser::getInstance).thenReturn(mockJsonParser);

        mockedPmHelperConstruction = mockConstruction(PackageManagerHelper.class, (mock, context) -> {
            when(mock.getSolutionId(any(), anyString())).thenReturn(SOLUTION_UUID);
        });

        // Setup Files
        jobDir = tempFolder.newFolder("jobDir");
        // Create metadata file from resources
        metadataFile = new File(jobDir, ScanConstants.METADATA_PREFIX + JOB_ID + ".json");
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("scanJob/metadata_scan_result.json")) {
            if (is == null) {
                throw new RuntimeException("metadata_scan_result.json not found in test resources");
            }
            Files.copy(is, metadataFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        // Setup JsonParser behavior
        when(mockJsonParser.fromJson(anyString(), eq(Metadata.class))).thenReturn(mockMetadata);
        when(mockMetadata.getMetadataFile()).thenReturn(mockMetadataFile);

        // Mock MetadataFile methods to prevent NPE
        when(mockMetadataFile.getJobName()).thenReturn("Test Job");
        when(mockMetadataFile.getUsername()).thenReturn("testuser");
        when(mockMetadataFile.getScanSource()).thenReturn("NONE");
        when(mockMetadataFile.getScanSize()).thenReturn("A4");

        // Create Instance and inject mock LocalProcessor
        scanJobCompletedState = new ScanJobCompletedState();
    }

    @After
    public void tearDown() {
        if (mockedScanConstants != null) mockedScanConstants.close();
        if (mockedJsonParserStatic != null) mockedJsonParserStatic.close();
        if (mockedPmHelperConstruction != null) mockedPmHelperConstruction.close();
    }

    @Test
    public void GivenCompletedJob_WhenMetadataProcessingFails_ThenReturnJobFailedState() throws Exception {
        // Given
        when(mockScanAttributesReader.getDestination()).thenReturn(ScanAttributes.Destination.ME);
        // Force exception during metadata parsing
        when(mockJsonParser.fromJson(anyString(), eq(Metadata.class))).thenThrow(new RuntimeException("Parsing Error"));

        // When
        scanJobCompletedState.processCompletedJob(mockStateMachine);

        // Then
        // Check next state is JobFailedState
        Field nextStateField = BaseJobIntentServiceState.class.getDeclaredField("nextState");
        nextStateField.setAccessible(true);
        BaseJobIntentServiceState nextState = (BaseJobIntentServiceState) nextStateField.get(scanJobCompletedState);
        assertTrue(nextState instanceof JobFailedState);

        JobFailedState failedState = (JobFailedState) nextState;
        Field errorCodeField = JobFailedState.class.getDeclaredField("mErrorCode");
        errorCodeField.setAccessible(true);
        Result.ErrorCode errorCode = (Result.ErrorCode) errorCodeField.get(failedState);
        assertEquals(Result.ErrorCode.JOB_FAILURE, errorCode);
    }

    @Test
    public void GivenCompletedJob_WhenMetadataFileMissing_ThenReturnJobFailedState() throws Exception {
        // Given
        // Delete metadata file
        metadataFile.delete();
        when(mockScanAttributesReader.getDestination()).thenReturn(ScanAttributes.Destination.ME);

        // When
        scanJobCompletedState.processCompletedJob(mockStateMachine);

        // Then
        // Check next state is JobFailedState
        Field nextStateField = BaseJobIntentServiceState.class.getDeclaredField("nextState");
        nextStateField.setAccessible(true);
        BaseJobIntentServiceState nextState = (BaseJobIntentServiceState) nextStateField.get(scanJobCompletedState);
        assertTrue(nextState instanceof JobFailedState);
    }

    @Test
    public void GivenCompletedJob_WhenJobBundleIsNull_ThenReturnJobFailedState() throws Exception {
        // Given
        when(mockStateMachine.getJobBundle()).thenReturn(null);
        when(mockScanAttributesReader.getDestination()).thenReturn(ScanAttributes.Destination.ME);

        // When
        scanJobCompletedState.processCompletedJob(mockStateMachine);

        // Then
        // Check next state is JobFailedState
        Field nextStateField = BaseJobIntentServiceState.class.getDeclaredField("nextState");
        nextStateField.setAccessible(true);
        BaseJobIntentServiceState nextState = (BaseJobIntentServiceState) nextStateField.get(scanJobCompletedState);
        assertTrue(nextState instanceof JobFailedState);
    }

    @Test
    public void GivenCompletedJob_WhenJobInfoIsNull_ThenReturnJobFailedState() throws Exception {
        // Given
        when(mockJobBundle.getParcelable(ILetObserver.Keys.KEY_JOB_INFO)).thenReturn(null);
        when(mockScanAttributesReader.getDestination()).thenReturn(ScanAttributes.Destination.ME);

        // When
        scanJobCompletedState.processCompletedJob(mockStateMachine);

        // Then
        // Check next state is JobFailedState
        Field nextStateField = BaseJobIntentServiceState.class.getDeclaredField("nextState");
        nextStateField.setAccessible(true);
        BaseJobIntentServiceState nextState = (BaseJobIntentServiceState) nextStateField.get(scanJobCompletedState);
        assertTrue(nextState instanceof JobFailedState);
    }

    @Test
    public void GivenCompletedJob_WhenJobDataIsNull_ThenReturnJobFailedState() throws Exception {
        // Given
        when(mockJobInfo.getJobData()).thenReturn(null);
        when(mockScanAttributesReader.getDestination()).thenReturn(ScanAttributes.Destination.ME);

        // When
        scanJobCompletedState.processCompletedJob(mockStateMachine);

        // Then
        // Check next state is JobFailedState
        Field nextStateField = BaseJobIntentServiceState.class.getDeclaredField("nextState");
        nextStateField.setAccessible(true);
        BaseJobIntentServiceState nextState = (BaseJobIntentServiceState) nextStateField.get(scanJobCompletedState);
        assertTrue(nextState instanceof JobFailedState);
    }
}
