package com.hp.jetadvantage.link.services.scanlet.service.postprocessor;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.text.TextUtils;

import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.api.scanner.ScanAttributesReader;
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

public class ScanToUsbProcessorUnitTest {

    private ScanToUsbProcessor scanToUsbProcessor;

    @Mock
    private BaseJobIntentServiceStateMachine mockStateMachine;
    @Mock
    ScanAttributesReader mockScanAttributesReader;
    @Mock
    private Context mockContext;
    @Mock
    private JobInfo mockJobInfo;

    private MockedStatic<TextUtils> mockedTextUtils;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Mock TextUtils.isEmpty to behave correctly in unit tests
        mockedTextUtils = Mockito.mockStatic(TextUtils.class);
        mockedTextUtils.when(() -> TextUtils.isEmpty(any())).thenAnswer(invocation -> {
            CharSequence str = invocation.getArgument(0);
            return str == null || str.length() == 0;
        });

        when(mockStateMachine.getContext()).thenReturn(mockContext);
        when(mockJobInfo.getJobId()).thenReturn("test-job-id");
        when(mockContext.getFilesDir()).thenReturn(new File("/fake/context/files/dir"));

        scanToUsbProcessor = spy(new ScanToUsbProcessor(mockStateMachine));
        when(scanToUsbProcessor.getScanAttributesReader()).thenReturn(mockScanAttributesReader);
        when(mockScanAttributesReader.getUsbLocation()).thenReturn("/fake/usb/location");
    }

    @After
    public void tearDown() {
        if (mockedTextUtils != null) mockedTextUtils.close();
    }

    @Test
    public void GivenScanToUsbProcessor_WhenPostProcessFilesCalled_ThenReturnListOfFilePaths() throws IOException {
        // Arrange
        File tempFile = File.createTempFile("test", ".pdf");
        tempFile.deleteOnExit();
        List<File> scannedFileList = Collections.singletonList(tempFile);

        // Act
        List<String> result = scanToUsbProcessor.postProcessFiles(scannedFileList);

        // Assert
        assertEquals(1, result.size());
        assertEquals(tempFile.getAbsolutePath(), result.get(0));
    }

    @Test
    public void GivenScanToUsbProcessor_WhenGetFileNameCalled_ThenUseJobNamePdf() {
        // Arrange
        File originalFile = new File("usb-scan_20260101_190432.pdf");
        String timeStamp = "20260101190432";
        String expectedFileName = "usb-scan-001.pdf";

        // Act
        String newFilename = scanToUsbProcessor.getFileName(originalFile, timeStamp);

        // Assert
        assertEquals(expectedFileName, newFilename);
    }

    @Test
    public void GivenScanToUsbProcessor_WhenGetFileNameCalled_ThenUseJobNameJpeg() {
        // Arrange
        File originalFile = new File("usb-scan_20260101_190432.jpeg");
        String timeStamp = "20260101190432";
        String expectedFileName = "usb-scan-001.jpeg";

        // Act
        String newFilename = scanToUsbProcessor.getFileName(originalFile, timeStamp);

        // Assert
        assertEquals(expectedFileName, newFilename);
    }

    @Test
    public void GivenScanToUsbProcessor_WhenGetFileNameCalled_ThenUseJobId() {
        // Arrange
        File originalFile = new File("2026010119042851_20260101_190432.pdf");
        String timeStamp = "20260101190432";
        String expectedFileName = "2026010119042851-001.pdf";

        // Act
        String newFilename = scanToUsbProcessor.getFileName(originalFile, timeStamp);

        // Assert
        assertEquals(expectedFileName, newFilename);
    }
}
