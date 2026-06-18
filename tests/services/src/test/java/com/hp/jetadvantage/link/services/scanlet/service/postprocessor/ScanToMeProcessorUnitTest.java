package com.hp.jetadvantage.link.services.scanlet.service.postprocessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import androidx.core.content.FileProvider;

import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ScanToMeProcessorUnitTest {

    @Mock
    BaseJobIntentServiceStateMachine mockStateMachine;
    @Mock
    Context mockContext;
    @Mock
    JobInfo mockJobInfo;
    @Mock
    Uri mockUri;

    private ScanToMeProcessor scanToMeProcessor;
    private MockedStatic<TextUtils> mockedTextUtils;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(mockStateMachine.getContext()).thenReturn(mockContext);
        scanToMeProcessor = new ScanToMeProcessor(mockStateMachine);

        // Mock TextUtils.isEmpty to behave correctly in unit tests
        mockedTextUtils = Mockito.mockStatic(TextUtils.class);
        mockedTextUtils.when(() -> TextUtils.isEmpty(any())).thenAnswer(invocation -> {
            CharSequence str = invocation.getArgument(0);
            return str == null || str.length() == 0;
        });
    }

    @After
    public void tearDown() {
        if (mockedTextUtils != null) mockedTextUtils.close();
    }

    @Test
    public void GivenScanToMeProcessor_WhenGetOutputPathCalled_ThenReturnCorrectOutputPath() {
        // Arrange
        File mockFile = mock(File.class);
        String expectedPath = "/data/data/com.hp.jetadvantage.link/files";
        when(mockFile.toString()).thenReturn(expectedPath);
        when(mockContext.getFilesDir()).thenReturn(mockFile);
        when(mockJobInfo.getJobId()).thenReturn("test-job-id");

        // Act
        String outputPath = scanToMeProcessor.getOutputPath(mockJobInfo);

        // Assert
        assertNotNull(outputPath);
        assertEquals(expectedPath + "/.tmp/" + mockJobInfo.getJobId(), outputPath);
    }

    @Test
    public void GivenScanToMeProcessor_WhenPostProcessFilesCalled_ThenGrantsPermissionAndReturnsUri() throws IOException {
        // Arrange
        File mockFile = mock(File.class);
        when(mockFile.exists()).thenReturn(true);
        List<File> scannedFileList = Collections.singletonList(mockFile);

        when(mockContext.getPackageName()).thenReturn("com.hp.jetadvantage.link");
        when(mockStateMachine.getTargetPackageName()).thenReturn("com.hp.jetadvantage.me");
        when(mockUri.toString()).thenReturn("content://com.hp.jetadvantage.link.provider/files/test.pdf");

        try (MockedStatic<FileProvider> mockedFileProvider = mockStatic(FileProvider.class)) {
            mockedFileProvider.when(() -> FileProvider.getUriForFile(any(Context.class), anyString(), any(File.class)))
                    .thenReturn(mockUri);

            // Act
            List<String> result = scanToMeProcessor.postProcessFiles(scannedFileList);

            // Assert
            assertEquals(1, result.size());
            assertEquals("content://com.hp.jetadvantage.link.provider/files/test.pdf", result.get(0));
            verify(mockContext).grantUriPermission("com.hp.jetadvantage.me", mockUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
    }

    @Test
    public void GivenScanToMeProcessor_WhenPostProcessFilesCalled_ThenReturnEmptyList() {
        // Arrange
        File mockFile = mock(File.class);
        when(mockFile.exists()).thenReturn(false);
        List<File> scannedFileList = Collections.singletonList(mockFile);

        // Act
        List<String> result = scanToMeProcessor.postProcessFiles(scannedFileList);

        // Assert
        assertTrue(result.isEmpty());
        verify(mockContext, never()).grantUriPermission(anyString(), any(Uri.class), anyInt());
    }

    @Test
    public void GivenScanToMeProcessor_WhenGetFileNameCalled_ThenReturnFormattedFileNamePdf() {
        // Arrange
        File originalFile = new File("my-scan_20260101_190432.pdf");
        String timeStamp = "20260101190432";
        String expectedFileName = "my-scan-001.pdf";
        // Act
        String newFilename = scanToMeProcessor.getFileName(originalFile, timeStamp);

        // Assert
        assertEquals(expectedFileName, newFilename);
    }

    @Test
    public void GivenScanToMeProcessor_WhenGetFileNameCalled_ThenReturnFormattedFileNameJpeg() {
        // Arrange
        File originalFile = new File("my-scan_20260101_190432.jpeg");
        String timeStamp = "20260101190432";
        String expectedFileName = "my-scan-001.jpeg";
        // Act
        String newFilename = scanToMeProcessor.getFileName(originalFile, timeStamp);

        // Assert
        assertEquals(expectedFileName, newFilename);
    }

    @Test
    public void GivenScanToMeProcessor_WhenGetFileNameCalled_ThenUseJobId() {
        // Arrange
        File originalFile = new File("2026010119042851_20260101_190432.jpeg");
        String timeStamp = "20260101190432";
        String expectedFileName = "2026010119042851-001.jpeg";

        // Act
        String newFilename = scanToMeProcessor.getFileName(originalFile, timeStamp);

        // Assert
        assertEquals(expectedFileName, newFilename);
    }
}
