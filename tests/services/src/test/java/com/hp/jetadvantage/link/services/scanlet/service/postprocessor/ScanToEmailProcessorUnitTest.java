package com.hp.jetadvantage.link.services.scanlet.service.postprocessor;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.text.TextUtils;

import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.api.scanner.ScanAttributesReader;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceEmailService;
import com.hp.jetadvantage.link.device.services.types.EmailSettingsData;
import com.hp.jetadvantage.link.services.emaillet.util.EmailLetUtils;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class ScanToEmailProcessorUnitTest {

    private ScanToEmailProcessor scanToEmailProcessor;

    @Mock
    private BaseJobIntentServiceStateMachine mockStateMachine;
    @Mock
    private ScanAttributesReader mockScanAttributesReader;
    @Mock
    private Context mockContext;
    @Mock
    private JobInfo mockJobInfo;
    @Mock
    private File mockFile;
    @Mock
    private com.hp.jetadvantage.link.api.scanner.SmtpAttributes mockSmtpAttributes;
    @Mock
    private com.hp.jetadvantage.link.api.scanner.EmailAttributes mockEmailAttributes;
    @Mock
    private EmailSettingsData mockEmailSettingsData;

    private MockedStatic<EmailLetUtils> mockedEmailLetUtils;
    private MockedConstruction<StandardDeviceEmailService> mockedDeviceEmailServiceConstruction;
    private MockedStatic<TextUtils> mockedTextUtils;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        when(mockStateMachine.getContext()).thenReturn(mockContext);
        when(mockJobInfo.getJobId()).thenReturn("test-job-id");
        when(mockContext.getFilesDir()).thenReturn(new File("/fake/context/files/dir"));

        // Mock TextUtils.isEmpty to behave correctly in unit tests
        mockedTextUtils = Mockito.mockStatic(TextUtils.class);
        mockedTextUtils.when(() -> TextUtils.isEmpty(any())).thenAnswer(invocation -> {
            CharSequence str = invocation.getArgument(0);
            return str == null || str.length() == 0;
        });

        // Connect mocked ScanAttributesReader to StateMachine
        when(mockStateMachine.getJobAttributesReader(ScanAttributesReader.class)).thenReturn(mockScanAttributesReader);

        // ScanAttributesReader returns values
        when(mockScanAttributesReader.getSmtpAttributes()).thenReturn(mockSmtpAttributes);
        when(mockScanAttributesReader.getEmailAttributes()).thenReturn(mockEmailAttributes);

        // SmtpAttributes defaults
        when(mockSmtpAttributes.getHost()).thenReturn("smtp.test.com");
        when(mockSmtpAttributes.getPort()).thenReturn(25);
        when(mockSmtpAttributes.getConnectTimeout()).thenReturn(30);
        when(mockSmtpAttributes.getReadTimeout()).thenReturn(60);
        when(mockSmtpAttributes.getTransportMode()).thenReturn(com.hp.jetadvantage.link.api.scanner.SmtpAttributes.TransportMode.PLAIN);

        scanToEmailProcessor = new ScanToEmailProcessor(mockStateMachine);

        mockedEmailLetUtils = Mockito.mockStatic(EmailLetUtils.class);

        mockedDeviceEmailServiceConstruction = mockConstruction(StandardDeviceEmailService.class, (mock, context) -> {
            when(mock.getEmailSettings()).thenReturn(mockEmailSettingsData);
        });
    }

    @After
    public void tearDown() {
        mockedEmailLetUtils.close();
        mockedDeviceEmailServiceConstruction.close();
        if (mockedTextUtils != null) mockedTextUtils.close();
    }

    @Test
    public void GivenScanToEmailProcessor_WhenGetOutputPathCalled_ThenReturnCorrectTmpPath() {
        // Arrange
        File filesDir = new File("/fake/context/files/dir");
        String expectedPath = filesDir + "/.tmp/test-job-id";

        // Act
        String actualPath = scanToEmailProcessor.getOutputPath(mockJobInfo);

        // Assert
        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void GivenScanToEmailProcessor_WhenPostProcessFilesCalled_ThenSendEmailAndReturnFileNames() throws Exception {
        // Arrange
        File tempFile = File.createTempFile("test", ".pdf");
        tempFile.deleteOnExit();
        List<File> scannedFileList = Collections.singletonList(tempFile);

        // Act
        List<String> result = scanToEmailProcessor.postProcessFiles(scannedFileList);

        // Assert
        assertEquals(1, result.size());
        assertEquals(tempFile.getName(), result.get(0));

        mockedEmailLetUtils.verify(() -> EmailLetUtils.send(
                any(),
                any(),
                any()));
    }

    @Test
    public void GivenScanToEmailProcessor_WhenGetFileNameCalled_ThenReturnFormattedFileNamePdf() {
        // Arrange
        File originalFile = new File("email-scan_20260101_190432.pdf");
        String timeStamp = "20260101190432";
        String expectedFileName = "email-scan-001.pdf";

        // Act
        String newFilename = scanToEmailProcessor.getFileName(originalFile, timeStamp);

        // Assert
        assertEquals(expectedFileName, newFilename);
    }

    @Test
    public void GivenScanToEmailProcessor_WhenGetFileNameCalled_ThenReturnFormattedFileNameJpeg() {
        // Arrange
        File originalFile = new File("email-scan_20260101_190432.jpeg");
        String timeStamp = "20260101190432";
        String expectedFileName = "email-scan-001.jpeg";

        // Act
        String newFilename = scanToEmailProcessor.getFileName(originalFile, timeStamp);

        // Assert
        assertEquals(expectedFileName, newFilename);
    }

    @Test
    public void GivenScanToEmailProcessor_WhenGetFileNameCalled_ThenUseJobId() {
        // Arrange
        File originalFile = new File("2026010119042851_20260101_190432.jpeg");
        String timeStamp = "20260101190432";
        String expectedFileName = "2026010119042851-001.jpeg";

        // Act
        String newFilename = scanToEmailProcessor.getFileName(originalFile, timeStamp);

        // Assert
        assertEquals(expectedFileName, newFilename);
    }
}
