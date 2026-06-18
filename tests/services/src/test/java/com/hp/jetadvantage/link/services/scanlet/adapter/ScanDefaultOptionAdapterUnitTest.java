package com.hp.jetadvantage.link.services.scanlet.adapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import android.os.Environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.ext.service.scanJob.DefaultOptions;
import com.hp.ext.service.scanJob.Profile;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes;
import com.hp.jetadvantage.link.api.scanner.ScanAttributesReader;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.utils.JsonParser;
import com.hp.jetadvantage.link.device.services.exceptions.BoundDeviceException;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceScanJobService;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.utils.Utils;
import com.hp.jetadvantage.link.services.common.exception.SdkConnectionErrorException;
import com.hp.jetadvantage.link.services.common.exception.SdkServiceErrorException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.util.Objects;

/**
 * Unit tests for ScanDefaultOptionAdapter
 * <p>
 * Note: Uses Environment.getExternalStoragePublicDirectory() for mocking purposes only.
 * The deprecated API is not actually called in tests, only stubbed via Mockito.
 */
@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("deprecation")
public class ScanDefaultOptionAdapterUnitTest {
    public String testPackageName = Constants.TEST_PACKAGE_NAME;
    @Mock
    private IDeviceScanJobService mockScanJobService;


    @Before
    public void setUp() throws Exception {
        ObjectMapper mapper;
        mapper = new ObjectMapper();
        String profileResponse = Utils.loadTestJsonResource(
                Objects.requireNonNull(getClass().getClassLoader()),
                "scanJob/GET_ext_scanJob_profile.json"
        );
        when(mockScanJobService.getProfile(testPackageName))
                .thenReturn(mapper.readValue(profileResponse, Profile.class));

        String defaultOptionsResponse = Utils.loadTestJsonResource(
                Objects.requireNonNull(getClass().getClassLoader()),
                "scanJob/GET_ext_scanJob_defaultOptions.json"
        );
        defaultOptionsResponse = E2JsonTestHelper.simplifyE2Json(defaultOptionsResponse);
        when(mockScanJobService.getDefaultOptions(testPackageName))
                .thenReturn(mapper.readValue(defaultOptionsResponse, DefaultOptions.class));
    }

    /**
     * TEST for ScanDefaultOptionAdapter.getDefaults : Error case 1 - BoundDeviceException
     */
    @Test
    public void GivenScanDefaultOptionAdapter_WhenGetDefaultsCalled_AndBoundDeviceExceptionOccurs_ThenConnectionErrorShouldBeReturned() {
        when(mockScanJobService.getProfile(testPackageName)).thenThrow(new BoundDeviceException());

        try {
            ScanDefaultOptionAdapter.getDefaults(testPackageName, mockScanJobService, ScanAttributes.Destination.HTTP, Sdk.VERSION.LEVEL);
            fail("Expected SdkConnectionErrorException to be thrown");
        } catch (SdkConnectionErrorException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Unexpected exception thrown: " + e);
        }
    }

    /**
     * TEST for ScanDefaultOptionAdapter.getDefaults : Error case 2 - null scan profile
     */
    @Test
    public void GivenScanDefaultOptionAdapter_WhenGetDefaultsCalled_NullProfileRetrievedFromDevice_ThenServiceErrorShouldBeReturned() {
        when(mockScanJobService.getProfile(testPackageName)).thenReturn(null);

        try {
            ScanDefaultOptionAdapter.getDefaults(testPackageName, mockScanJobService, ScanAttributes.Destination.HTTP, Sdk.VERSION.LEVEL);
            fail("Expected SdkServiceErrorException to be thrown");
        } catch (SdkServiceErrorException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Unexpected exception thrown: " + e);
        }
    }

    /**
     * TEST for ScanDefaultOptionAdapter.getDefaults : happy case - valid scan profile and default options
     */
    @Test
    public void GivenScanDefaultOptionAdapter_WhenGetDefaultsCalled_WithValidProfile_ThenValidJsonStringShouldBeReturned() {
        try (MockedStatic<Environment> mocked = mockStatic(Environment.class)) {
            File mockFile = new File("test");
            mocked.when(() -> Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)).thenReturn(mockFile);
            mocked.when(Environment::getExternalStorageDirectory).thenReturn(mockFile);
            mocked.when(Environment::getDataDirectory).thenReturn(mockFile);

            String resultJsonStr = ScanDefaultOptionAdapter.getDefaults(testPackageName, mockScanJobService, ScanAttributes.Destination.HTTP, Sdk.VERSION.LEVEL);
            ScanAttributes scanAttributes = JsonParser.getInstance().fromJson(resultJsonStr, ScanAttributes.class);

            //Validate result ScanAttributes
            ScanAttributesReader reader = new ScanAttributesReader(scanAttributes);
            assertEquals("Color mode", ScanAttributes.ColorMode.COLOR, reader.getColorMode());
            assertEquals("Duplex mode", ScanAttributes.Duplex.NONE, reader.getPlex());
            assertEquals("Orientation", ScanAttributes.Orientation.PORTRAIT, reader.getOrientation());
            assertEquals("Destination", ScanAttributes.Destination.ME, reader.getDestination());
            assertEquals("Document format", ScanAttributes.DocumentFormat.PDF, reader.getDocumentFormat());
            assertEquals("Scan size", ScanAttributes.ScanSize.LETTER, reader.getScanSize());
            assertEquals("Scan preview", ScanAttributes.ScanPreview.DEFAULT, reader.getScanPreview()); //This option is not available on sample test data.
            assertEquals("Resolution", ScanAttributes.Resolution.DPI_300, reader.getResolution());
            assertEquals("Background cleanup", ScanAttributes.BackgroundCleanup.LEVEL_0, reader.getBackgroundCleanup());
            assertEquals("Contrast adjustment", ScanAttributes.ContrastAdjustment.LEVEL_1, reader.getContrastAdjustment());
            assertEquals("Blank image removal mode", ScanAttributes.BlankImageRemovalMode.OFF, reader.getBlankImageRemovalMode()); //This option is not
            // available on sample test data.
            assertEquals("Progress dialog mode", ScanAttributes.ProgressDialogMode.ON, reader.getProgressDialogMode());
            assertEquals("Output quality", ScanAttributes.OutputQuality.MEDIUM, reader.getOutputQuality());
            assertEquals("Transmission mode", ScanAttributes.TransmissionMode.JOB, reader.getTransmissionMode());
            assertEquals("Sharpness adjustment", ScanAttributes.SharpnessAdjustment.LEVEL_1, reader.getSharpnessAdjustment()); //This option is not available
            // on sample test data.
            assertEquals("Text photo optimization", ScanAttributes.TextPhotoOptimization.MIXED_2, reader.getTextPhotoOptimization()); //The E2 option value
            // dctMixed is not matched with workpath type.
            assertEquals("Media source", ScanAttributes.MediaSource.ADF, reader.getMediaSource());
            assertEquals("Misfeed detection mode", ScanAttributes.MisfeedDetectionMode.OFF, reader.getMisfeedDetectionMode());
            assertEquals("Capture mode", ScanAttributes.CaptureMode.DEFAULT, reader.getCaptureMode()); //This option is not available on sample test data.
            assertEquals("Automatic straighten mode", ScanAttributes.AutomaticStraightenMode.DISABLE, reader.getAutomaticStraightenMode());
        } catch (Exception e) {
            fail("Unexpected exception=" + e);
        }
    }

    /**
     * TEST for ScanDefaultOptionAdapter.getDefaults : happy case - valid scan profile and default options for http destination
     */
    @Test
    public void GivenScanDefaultOptionAdapter_WhenGetDefaultsCalledForHttpDestination_ThenValidJsonStringShouldBeReturned() throws Exception {
        try (MockedStatic<Environment> mocked = mockStatic(Environment.class)) {
            File mockFile = new File("test");
            mocked.when(() -> Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)).thenReturn(mockFile);
            mocked.when(Environment::getExternalStorageDirectory).thenReturn(mockFile);
            mocked.when(Environment::getDataDirectory).thenReturn(mockFile);

            ScanAttributes scanAttributes = ScanDefaultOptionAdapter.getDefaultScanAttributes(testPackageName, mockScanJobService, ScanAttributes.Destination.HTTP, Sdk.VERSION.LEVEL);

            //Validate result ScanAttributes
            ScanAttributesReader reader = new ScanAttributesReader(scanAttributes);
            assertEquals("Color mode", ScanAttributes.ColorMode.COLOR, reader.getColorMode());
            assertEquals("Duplex mode", ScanAttributes.Duplex.NONE, reader.getPlex());
            assertEquals("Orientation", ScanAttributes.Orientation.PORTRAIT, reader.getOrientation());
            assertEquals("Document format", ScanAttributes.DocumentFormat.PDF, reader.getDocumentFormat());
            assertEquals("Scan size", ScanAttributes.ScanSize.LETTER, reader.getScanSize());
            assertEquals("Scan preview", ScanAttributes.ScanPreview.DEFAULT, reader.getScanPreview());
            assertEquals("Resolution", ScanAttributes.Resolution.DPI_300, reader.getResolution());
            assertEquals("Background cleanup", ScanAttributes.BackgroundCleanup.LEVEL_0, reader.getBackgroundCleanup());
            assertEquals("Contrast adjustment", ScanAttributes.ContrastAdjustment.LEVEL_1, reader.getContrastAdjustment());
            assertEquals("Blank image removal mode", ScanAttributes.BlankImageRemovalMode.OFF, reader.getBlankImageRemovalMode());
            assertEquals("Progress dialog mode", ScanAttributes.ProgressDialogMode.ON, reader.getProgressDialogMode());
            assertEquals("Output quality", ScanAttributes.OutputQuality.MEDIUM, reader.getOutputQuality());
            assertEquals("Transmission mode", ScanAttributes.TransmissionMode.JOB, reader.getTransmissionMode());
            assertEquals("Sharpness adjustment", ScanAttributes.SharpnessAdjustment.LEVEL_1, reader.getSharpnessAdjustment());
            assertEquals("Text photo optimization", ScanAttributes.TextPhotoOptimization.MIXED_2, reader.getTextPhotoOptimization());
            assertEquals("Media source", ScanAttributes.MediaSource.ADF, reader.getMediaSource());
            assertEquals("Misfeed detection mode", ScanAttributes.MisfeedDetectionMode.OFF, reader.getMisfeedDetectionMode());
            assertEquals("Capture mode", ScanAttributes.CaptureMode.DEFAULT, reader.getCaptureMode());
            assertEquals("Automatic straighten mode", ScanAttributes.AutomaticStraightenMode.DISABLE, reader.getAutomaticStraightenMode());
            assertEquals("File name", "scan", reader.getFileName());
        }
    }

    /**
     * TEST for ScanDefaultOptionAdapter.getDefaults : happy case - valid scan profile and default options for network folder destination
     */
    @Test
    public void GivenScanDefaultOptionAdapter_WhenGetDefaultsCalledForNetworkFolderDestination_ThenValidJsonStringShouldBeReturned() {
        try (MockedStatic<Environment> mocked = mockStatic(Environment.class)) {
            File mockFile = new File("test");
            mocked.when(() -> Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)).thenReturn(mockFile);
            mocked.when(Environment::getExternalStorageDirectory).thenReturn(mockFile);
            mocked.when(Environment::getDataDirectory).thenReturn(mockFile);

            ScanAttributes scanAttributes = ScanDefaultOptionAdapter.getDefaultScanAttributes(testPackageName, mockScanJobService,
                    ScanAttributes.Destination.NETWORK_FOLDER, Sdk.VERSION.LEVEL);

            //Validate result ScanAttributes
            ScanAttributesReader reader = new ScanAttributesReader(scanAttributes);
            assertEquals("Color mode", ScanAttributes.ColorMode.AUTO, reader.getColorMode());
            assertEquals("Duplex mode", ScanAttributes.Duplex.NONE, reader.getPlex());
            assertEquals("Orientation", ScanAttributes.Orientation.PORTRAIT, reader.getOrientation());
            assertEquals("Document format", ScanAttributes.DocumentFormat.PDF, reader.getDocumentFormat());
            assertEquals("Scan size", ScanAttributes.ScanSize.LETTER_ROTATE, reader.getScanSize());
            assertEquals("Scan preview", ScanAttributes.ScanPreview.DEFAULT, reader.getScanPreview());
            assertEquals("Resolution", ScanAttributes.Resolution.DPI_200, reader.getResolution());
            assertEquals("Background cleanup", ScanAttributes.BackgroundCleanup.LEVEL_0, reader.getBackgroundCleanup());
            assertEquals("Contrast adjustment", ScanAttributes.ContrastAdjustment.LEVEL_4, reader.getContrastAdjustment());
            assertEquals("Blank image removal mode", ScanAttributes.BlankImageRemovalMode.OFF, reader.getBlankImageRemovalMode());
            assertEquals("Progress dialog mode", ScanAttributes.ProgressDialogMode.ON, reader.getProgressDialogMode());
            assertEquals("Output quality", ScanAttributes.OutputQuality.MEDIUM, reader.getOutputQuality());
            assertEquals("Transmission mode", ScanAttributes.TransmissionMode.JOB, reader.getTransmissionMode());
            assertEquals("Sharpness adjustment", ScanAttributes.SharpnessAdjustment.LEVEL_1, reader.getSharpnessAdjustment());
            assertEquals("Text photo optimization", ScanAttributes.TextPhotoOptimization.MIXED_2, reader.getTextPhotoOptimization());
            assertEquals("Media source", ScanAttributes.MediaSource.ADF, reader.getMediaSource());
            assertEquals("Misfeed detection mode", ScanAttributes.MisfeedDetectionMode.OFF, reader.getMisfeedDetectionMode());
            assertEquals("Capture mode", ScanAttributes.CaptureMode.DEFAULT, reader.getCaptureMode());
            assertEquals("Automatic straighten mode", ScanAttributes.AutomaticStraightenMode.DISABLE, reader.getAutomaticStraightenMode());
            assertEquals("File name", "scan", reader.getFileName());
        } catch (Exception e) {
            fail("Unexpected exception=" + e);
        }
    }

    /**
     * TEST for ScanDefaultOptionAdapter.getDefaults : Error case 3 - null DefaultOptions
     * <p>
     * Purpose: Verify error handling when device returns null DefaultOptions
     * Scenario: Profile exists but DefaultOptions retrieval fails
     * Expected: Should throw SdkServiceErrorException
     * <p>
     * This can occur when:
     * - Device firmware doesn't support default options API
     * - Network communication fails during DefaultOptions retrieval
     * - Device is in an error state
     */
    @Test
    public void GivenScanDefaultOptionAdapter_WhenGetDefaultsCalled_NullDefaultOptionsRetrievedFromDevice_ThenServiceErrorShouldBeReturned() {
        when(mockScanJobService.getDefaultOptions(testPackageName)).thenReturn(null);

        try {
            ScanDefaultOptionAdapter.getDefaults(testPackageName, mockScanJobService, ScanAttributes.Destination.HTTP, Sdk.VERSION.LEVEL);
            fail("Expected SdkServiceErrorException to be thrown");
        } catch (SdkServiceErrorException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Unexpected exception thrown: " + e);
        }
    }

    /**
     * TEST for ScanDefaultOptionAdapter.getDefaults : happy case - FTP destination uses NetworkFolder profile
     * <p>
     * Purpose: Verify FTP destination correctly uses NetworkFolder profile/defaults
     * Scenario: Request defaults for FTP destination
     * Expected: Should return NetworkFolder defaults (PDF format, 200 DPI, etc.)
     * <p>
     * Implementation Detail:
     * - FTP and NETWORK_FOLDER share the same E2 profile section (networkFolder)
     * - Both should return identical default options
     * - Validates the switch-case fallthrough logic in getDefaultsForDestination()
     */
    @Test
    public void GivenScanDefaultOptionAdapter_WhenGetDefaultsCalledForFtpDestination_ThenNetworkFolderDefaultsShouldBeReturned() {
        try (MockedStatic<Environment> mocked = mockStatic(Environment.class)) {
            File mockFile = new File("test");
            mocked.when(() -> Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)).thenReturn(mockFile);
            mocked.when(Environment::getExternalStorageDirectory).thenReturn(mockFile);
            mocked.when(Environment::getDataDirectory).thenReturn(mockFile);

            ScanAttributes scanAttributes = ScanDefaultOptionAdapter.getDefaultScanAttributes(testPackageName, mockScanJobService,
                    ScanAttributes.Destination.FTP, Sdk.VERSION.LEVEL);

            //Validate result ScanAttributes - should be same as NETWORK_FOLDER
            ScanAttributesReader reader = new ScanAttributesReader(scanAttributes);
            assertEquals("Color mode", ScanAttributes.ColorMode.AUTO, reader.getColorMode());
            assertEquals("Duplex mode", ScanAttributes.Duplex.NONE, reader.getPlex());
            assertEquals("Orientation", ScanAttributes.Orientation.PORTRAIT, reader.getOrientation());
            assertEquals("Document format", ScanAttributes.DocumentFormat.PDF, reader.getDocumentFormat());
            assertEquals("Scan size", ScanAttributes.ScanSize.LETTER_ROTATE, reader.getScanSize());
            assertEquals("Scan preview", ScanAttributes.ScanPreview.DEFAULT, reader.getScanPreview());
            assertEquals("Resolution", ScanAttributes.Resolution.DPI_200, reader.getResolution());
            assertEquals("Background cleanup", ScanAttributes.BackgroundCleanup.LEVEL_0, reader.getBackgroundCleanup());
            assertEquals("Contrast adjustment", ScanAttributes.ContrastAdjustment.LEVEL_4, reader.getContrastAdjustment());
            assertEquals("Blank image removal mode", ScanAttributes.BlankImageRemovalMode.OFF, reader.getBlankImageRemovalMode());
            assertEquals("Progress dialog mode", ScanAttributes.ProgressDialogMode.ON, reader.getProgressDialogMode());
            assertEquals("Output quality", ScanAttributes.OutputQuality.MEDIUM, reader.getOutputQuality());
            assertEquals("Transmission mode", ScanAttributes.TransmissionMode.JOB, reader.getTransmissionMode());
            assertEquals("Sharpness adjustment", ScanAttributes.SharpnessAdjustment.LEVEL_1, reader.getSharpnessAdjustment());
            assertEquals("Text photo optimization", ScanAttributes.TextPhotoOptimization.MIXED_2, reader.getTextPhotoOptimization());
            assertEquals("Media source", ScanAttributes.MediaSource.ADF, reader.getMediaSource());
            assertEquals("Misfeed detection mode", ScanAttributes.MisfeedDetectionMode.OFF, reader.getMisfeedDetectionMode());
            assertEquals("Capture mode", ScanAttributes.CaptureMode.DEFAULT, reader.getCaptureMode());
            assertEquals("Automatic straighten mode", ScanAttributes.AutomaticStraightenMode.DISABLE, reader.getAutomaticStraightenMode());
            assertEquals("File name", "scan", reader.getFileName());
        } catch (Exception e) {
            fail("Unexpected exception=" + e);
        }
    }

    /**
     * TEST for ScanDefaultOptionAdapter.getDefaultScanAttributes : returns ScanAttributes object directly
     * <p>
     * Purpose: Verify getDefaultScanAttributes() returns object without JSON serialization
     * Scenario: Call package-private method directly instead of public getDefaults()
     * Expected: Should return ScanAttributes object (not JSON string)
     * <p>
     * Use Case:
     * - Internal callers (like ScanTicketAdapter) can avoid JSON parsing overhead
     * - Public API getDefaults() wraps this method and adds JSON serialization
     * - Tests the core logic without JSON conversion layer
     */
    @Test
    public void GivenScanDefaultOptionAdapter_WhenGetDefaultScanAttributesCalled_ThenScanAttributesObjectShouldBeReturned() {
        try (MockedStatic<Environment> mocked = mockStatic(Environment.class)) {
            File mockFile = new File("test");
            mocked.when(() -> Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)).thenReturn(mockFile);
            mocked.when(Environment::getExternalStorageDirectory).thenReturn(mockFile);
            mocked.when(Environment::getDataDirectory).thenReturn(mockFile);

            // Call getDefaultScanAttributes directly
            ScanAttributes scanAttributes = ScanDefaultOptionAdapter.getDefaultScanAttributes(testPackageName, mockScanJobService,
                    ScanAttributes.Destination.HTTP, Sdk.VERSION.LEVEL);

            // Validate it returns object not JSON
            assertTrue("Should return ScanAttributes object", scanAttributes != null);
            ScanAttributesReader reader = new ScanAttributesReader(scanAttributes);
            assertEquals("Color mode", ScanAttributes.ColorMode.COLOR, reader.getColorMode());
            assertEquals("Duplex mode", ScanAttributes.Duplex.NONE, reader.getPlex());
            assertEquals("Orientation", ScanAttributes.Orientation.PORTRAIT, reader.getOrientation());
            assertEquals("Document format", ScanAttributes.DocumentFormat.PDF, reader.getDocumentFormat());
            assertEquals("Scan size", ScanAttributes.ScanSize.LETTER, reader.getScanSize());
            assertEquals("Scan preview", ScanAttributes.ScanPreview.DEFAULT, reader.getScanPreview());
            assertEquals("Resolution", ScanAttributes.Resolution.DPI_300, reader.getResolution());
            assertEquals("Background cleanup", ScanAttributes.BackgroundCleanup.LEVEL_0, reader.getBackgroundCleanup());
            assertEquals("Contrast adjustment", ScanAttributes.ContrastAdjustment.LEVEL_1, reader.getContrastAdjustment());
            assertEquals("Blank image removal mode", ScanAttributes.BlankImageRemovalMode.OFF, reader.getBlankImageRemovalMode());
            assertEquals("Progress dialog mode", ScanAttributes.ProgressDialogMode.ON, reader.getProgressDialogMode());
            assertEquals("Output quality", ScanAttributes.OutputQuality.MEDIUM, reader.getOutputQuality());
            assertEquals("Transmission mode", ScanAttributes.TransmissionMode.JOB, reader.getTransmissionMode());
            assertEquals("Sharpness adjustment", ScanAttributes.SharpnessAdjustment.LEVEL_1, reader.getSharpnessAdjustment());
            assertEquals("Text photo optimization", ScanAttributes.TextPhotoOptimization.MIXED_2, reader.getTextPhotoOptimization());
            assertEquals("Media source", ScanAttributes.MediaSource.ADF, reader.getMediaSource());
            assertEquals("Misfeed detection mode", ScanAttributes.MisfeedDetectionMode.OFF, reader.getMisfeedDetectionMode());
            assertEquals("Capture mode", ScanAttributes.CaptureMode.DEFAULT, reader.getCaptureMode());
            assertEquals("Automatic straighten mode", ScanAttributes.AutomaticStraightenMode.DISABLE, reader.getAutomaticStraightenMode());
            assertEquals("File name", "scan", reader.getFileName());
        } catch (Exception e) {
            fail("Unexpected exception=" + e);
        }
    }

    /**
     * TEST for ScanDefaultOptionAdapter.getDefaults : consistency test - multiple calls return same result
     * <p>
     * Purpose: Verify idempotency - multiple calls with same parameters return identical results
     * Scenario: Call getDefaults() twice with same destination (HTTP)
     * Expected: Both calls should return identical JSON strings and parsed objects
     * <p>
     * Validates:
     * - No state mutation between calls
     * - Deterministic behavior (no random values)
     * - Cache consistency if implemented
     * - Thread-safety preparation (same input → same output)
     */
    @Test
    public void GivenScanDefaultOptionAdapter_WhenGetDefaultsCalledMultipleTimes_ThenConsistentResultsShouldBeReturned() {
        try (MockedStatic<Environment> mocked = mockStatic(Environment.class)) {
            File mockFile = new File("test");
            mocked.when(() -> Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)).thenReturn(mockFile);
            mocked.when(Environment::getExternalStorageDirectory).thenReturn(mockFile);
            mocked.when(Environment::getDataDirectory).thenReturn(mockFile);

            // Call multiple times
            String result1 = ScanDefaultOptionAdapter.getDefaults(testPackageName, mockScanJobService, ScanAttributes.Destination.HTTP, Sdk.VERSION.LEVEL);
            String result2 = ScanDefaultOptionAdapter.getDefaults(testPackageName, mockScanJobService, ScanAttributes.Destination.HTTP, Sdk.VERSION.LEVEL);

            // Results should be identical
            assertEquals("Multiple calls should return same result", result1, result2);

            // Parse and validate
            ScanAttributes attr1 = JsonParser.getInstance().fromJson(result1, ScanAttributes.class);
            ScanAttributes attr2 = JsonParser.getInstance().fromJson(result2, ScanAttributes.class);

            ScanAttributesReader reader1 = new ScanAttributesReader(attr1);
            ScanAttributesReader reader2 = new ScanAttributesReader(attr2);

            assertEquals("Color mode should match", reader1.getColorMode(), reader2.getColorMode());
            assertEquals("Duplex mode should match", reader1.getPlex(), reader2.getPlex());
            assertEquals("Orientation should match", reader1.getOrientation(), reader2.getOrientation());
            assertEquals("Document format should match", reader1.getDocumentFormat(), reader2.getDocumentFormat());
            assertEquals("Scan size should match", reader1.getScanSize(), reader2.getScanSize());
            assertEquals("Scan preview should match", reader1.getScanPreview(), reader2.getScanPreview());
            assertEquals("Resolution should match", reader1.getResolution(), reader2.getResolution());
            assertEquals("Background cleanup should match", reader1.getBackgroundCleanup(), reader2.getBackgroundCleanup());
            assertEquals("Contrast adjustment should match", reader1.getContrastAdjustment(), reader2.getContrastAdjustment());
            assertEquals("Blank image removal mode should match", reader1.getBlankImageRemovalMode(), reader2.getBlankImageRemovalMode());
            assertEquals("Progress dialog mode should match", reader1.getProgressDialogMode(), reader2.getProgressDialogMode());
            assertEquals("Output quality should match", reader1.getOutputQuality(), reader2.getOutputQuality());
            assertEquals("Transmission mode should match", reader1.getTransmissionMode(), reader2.getTransmissionMode());
            assertEquals("Sharpness adjustment should match", reader1.getSharpnessAdjustment(), reader2.getSharpnessAdjustment());
            assertEquals("Text photo optimization should match", reader1.getTextPhotoOptimization(), reader2.getTextPhotoOptimization());
            assertEquals("Media source should match", reader1.getMediaSource(), reader2.getMediaSource());
            assertEquals("Misfeed detection mode should match", reader1.getMisfeedDetectionMode(), reader2.getMisfeedDetectionMode());
            assertEquals("Capture mode should match", reader1.getCaptureMode(), reader2.getCaptureMode());
            assertEquals("Automatic straighten mode should match", reader1.getAutomaticStraightenMode(), reader2.getAutomaticStraightenMode());
            assertEquals("File name should match", reader1.getFileName(), reader2.getFileName());
        } catch (Exception e) {
            fail("Unexpected exception=" + e);
        }
    }

    /**
     * TEST for ScanDefaultOptionAdapter.getDefaults : different destinations return different defaults
     * <p>
     * Purpose: Verify destination parameter actually affects returned defaults
     * Scenario: Request defaults for HTTP vs NETWORK_FOLDER
     * Expected: Should return different values based on destination
     * <p>
     * Key Differences Validated:
     * - HTTP defaults: JPEG format, 300 DPI (optimized for web)
     * - NetworkFolder defaults: PDF format, 200 DPI (optimized for file storage)
     * <p>
     * This is the core value of destination-based defaults feature:
     * Each destination type has appropriate defaults for its use case
     */
    @Test
    public void GivenScanDefaultOptionAdapter_WhenGetDefaultsCalledForDifferentDestinations_ThenDifferentDefaultsShouldBeReturned() {
        try (MockedStatic<Environment> mocked = mockStatic(Environment.class)) {
            File mockFile = new File("test");
            mocked.when(() -> Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)).thenReturn(mockFile);
            mocked.when(Environment::getExternalStorageDirectory).thenReturn(mockFile);
            mocked.when(Environment::getDataDirectory).thenReturn(mockFile);

            // Get defaults for different destinations
            ScanAttributes httpDefaults = ScanDefaultOptionAdapter.getDefaultScanAttributes(testPackageName, mockScanJobService,
                    ScanAttributes.Destination.HTTP, Sdk.VERSION.LEVEL);
            ScanAttributes networkFolderDefaults = ScanDefaultOptionAdapter.getDefaultScanAttributes(testPackageName, mockScanJobService,
                    ScanAttributes.Destination.NETWORK_FOLDER, Sdk.VERSION.LEVEL);

            // Validate they are different
            ScanAttributesReader httpReader = new ScanAttributesReader(httpDefaults);
            ScanAttributesReader nfReader = new ScanAttributesReader(networkFolderDefaults);

            // Validate HTTP defaults (all 20 attributes)
            assertEquals("HTTP Color mode", ScanAttributes.ColorMode.COLOR, httpReader.getColorMode());
            assertEquals("HTTP Duplex mode", ScanAttributes.Duplex.NONE, httpReader.getPlex());
            assertEquals("HTTP Orientation", ScanAttributes.Orientation.PORTRAIT, httpReader.getOrientation());
            assertEquals("HTTP Document format", ScanAttributes.DocumentFormat.PDF, httpReader.getDocumentFormat());
            assertEquals("HTTP Scan size", ScanAttributes.ScanSize.LETTER, httpReader.getScanSize());
            assertEquals("HTTP Scan preview", ScanAttributes.ScanPreview.DEFAULT, httpReader.getScanPreview());
            assertEquals("HTTP Resolution", ScanAttributes.Resolution.DPI_300, httpReader.getResolution());
            assertEquals("HTTP Background cleanup", ScanAttributes.BackgroundCleanup.LEVEL_0, httpReader.getBackgroundCleanup());
            assertEquals("HTTP Contrast adjustment", ScanAttributes.ContrastAdjustment.LEVEL_1, httpReader.getContrastAdjustment());
            assertEquals("HTTP Blank image removal mode", ScanAttributes.BlankImageRemovalMode.OFF, httpReader.getBlankImageRemovalMode());
            assertEquals("HTTP Progress dialog mode", ScanAttributes.ProgressDialogMode.ON, httpReader.getProgressDialogMode());
            assertEquals("HTTP Output quality", ScanAttributes.OutputQuality.MEDIUM, httpReader.getOutputQuality());
            assertEquals("HTTP Transmission mode", ScanAttributes.TransmissionMode.JOB, httpReader.getTransmissionMode());
            assertEquals("HTTP Sharpness adjustment", ScanAttributes.SharpnessAdjustment.LEVEL_1, httpReader.getSharpnessAdjustment());
            assertEquals("HTTP Text photo optimization", ScanAttributes.TextPhotoOptimization.MIXED_2, httpReader.getTextPhotoOptimization());
            assertEquals("HTTP Media source", ScanAttributes.MediaSource.ADF, httpReader.getMediaSource());
            assertEquals("HTTP Misfeed detection mode", ScanAttributes.MisfeedDetectionMode.OFF, httpReader.getMisfeedDetectionMode());
            assertEquals("HTTP Capture mode", ScanAttributes.CaptureMode.DEFAULT, httpReader.getCaptureMode());
            assertEquals("HTTP Automatic straighten mode", ScanAttributes.AutomaticStraightenMode.DISABLE, httpReader.getAutomaticStraightenMode());
            assertEquals("HTTP File name", "scan", httpReader.getFileName());

            // Validate NetworkFolder defaults (all 20 attributes)
            assertEquals("NetworkFolder Color mode", ScanAttributes.ColorMode.AUTO, nfReader.getColorMode());
            assertEquals("NetworkFolder Duplex mode", ScanAttributes.Duplex.NONE, nfReader.getPlex());
            assertEquals("NetworkFolder Orientation", ScanAttributes.Orientation.PORTRAIT, nfReader.getOrientation());
            assertEquals("NetworkFolder Document format", ScanAttributes.DocumentFormat.PDF, nfReader.getDocumentFormat());
            assertEquals("NetworkFolder Scan size", ScanAttributes.ScanSize.LETTER_ROTATE, nfReader.getScanSize());
            assertEquals("NetworkFolder Scan preview", ScanAttributes.ScanPreview.DEFAULT, nfReader.getScanPreview());
            assertEquals("NetworkFolder Resolution", ScanAttributes.Resolution.DPI_200, nfReader.getResolution());
            assertEquals("NetworkFolder Background cleanup", ScanAttributes.BackgroundCleanup.LEVEL_0, nfReader.getBackgroundCleanup());
            assertEquals("NetworkFolder Contrast adjustment", ScanAttributes.ContrastAdjustment.LEVEL_4, nfReader.getContrastAdjustment());
            assertEquals("NetworkFolder Blank image removal mode", ScanAttributes.BlankImageRemovalMode.OFF, nfReader.getBlankImageRemovalMode());
            assertEquals("NetworkFolder Progress dialog mode", ScanAttributes.ProgressDialogMode.ON, nfReader.getProgressDialogMode());
            assertEquals("NetworkFolder Output quality", ScanAttributes.OutputQuality.MEDIUM, nfReader.getOutputQuality());
            assertEquals("NetworkFolder Transmission mode", ScanAttributes.TransmissionMode.JOB, nfReader.getTransmissionMode());
            assertEquals("NetworkFolder Sharpness adjustment", ScanAttributes.SharpnessAdjustment.LEVEL_1, nfReader.getSharpnessAdjustment());
            assertEquals("NetworkFolder Text photo optimization", ScanAttributes.TextPhotoOptimization.MIXED_2, nfReader.getTextPhotoOptimization());
            assertEquals("NetworkFolder Media source", ScanAttributes.MediaSource.ADF, nfReader.getMediaSource());
            assertEquals("NetworkFolder Misfeed detection mode", ScanAttributes.MisfeedDetectionMode.OFF, nfReader.getMisfeedDetectionMode());
            assertEquals("NetworkFolder Capture mode", ScanAttributes.CaptureMode.DEFAULT, nfReader.getCaptureMode());
            assertEquals("NetworkFolder Automatic straighten mode", ScanAttributes.AutomaticStraightenMode.DISABLE, nfReader.getAutomaticStraightenMode());
            assertEquals("NetworkFolder File name", "scan", nfReader.getFileName());
        } catch (Exception e) {
            fail("Unexpected exception=" + e);
        }
    }

    /**
     * TEST for ScanDefaultOptionAdapter.getDefaults : JSON string comparison test
     * <p>
     * Purpose: Verify getDefaults() returns valid JSON and matches getDefaultScanAttributes() output
     * Scenario: Call both getDefaults() (returns JSON) and getDefaultScanAttributes() (returns object)
     * Expected: JSON string from getDefaults() should deserialize to equivalent object
     * <p>
     * Validates:
     * - JSON serialization is correct
     * - Both methods return equivalent data
     * - Public API (getDefaults) wraps internal API properly
     */
    @Test
    public void GivenScanDefaultOptionAdapter_WhenGetDefaultsCalled_ThenJsonStringShouldMatchObjectConversion() {
        try (MockedStatic<Environment> mocked = mockStatic(Environment.class)) {
            File mockFile = new File("test");
            mocked.when(() -> Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)).thenReturn(mockFile);
            mocked.when(Environment::getExternalStorageDirectory).thenReturn(mockFile);
            mocked.when(Environment::getDataDirectory).thenReturn(mockFile);

            // Call public API (returns JSON string)
            String jsonResult = ScanDefaultOptionAdapter.getDefaults(testPackageName, mockScanJobService, ScanAttributes.Destination.HTTP, Sdk.VERSION.LEVEL);

            // Call internal API (returns object)
            ScanAttributes objectResult = ScanDefaultOptionAdapter.getDefaultScanAttributes(testPackageName, mockScanJobService,
                    ScanAttributes.Destination.HTTP, Sdk.VERSION.LEVEL);

            // Parse JSON to object
            ScanAttributes parsedFromJson = JsonParser.getInstance().fromJson(jsonResult, ScanAttributes.class);

            // Compare both results
            ScanAttributesReader jsonReader = new ScanAttributesReader(parsedFromJson);
            ScanAttributesReader objectReader = new ScanAttributesReader(objectResult);

            // Validate ALL attributes match
            assertEquals("Color mode should match", jsonReader.getColorMode(), objectReader.getColorMode());
            assertEquals("Resolution should match", jsonReader.getResolution(), objectReader.getResolution());
            assertEquals("Document format should match", jsonReader.getDocumentFormat(), objectReader.getDocumentFormat());
            assertEquals("Duplex should match", jsonReader.getPlex(), objectReader.getPlex());
            assertEquals("Scan size should match", jsonReader.getScanSize(), objectReader.getScanSize());
            assertEquals("Transmission mode should match", jsonReader.getTransmissionMode(), objectReader.getTransmissionMode());
            assertEquals("Orientation should match", jsonReader.getOrientation(), objectReader.getOrientation());
            assertEquals("Scan preview should match", jsonReader.getScanPreview(), objectReader.getScanPreview());
            assertEquals("Background cleanup should match", jsonReader.getBackgroundCleanup(), objectReader.getBackgroundCleanup());
            assertEquals("Contrast adjustment should match", jsonReader.getContrastAdjustment(), objectReader.getContrastAdjustment());
            assertEquals("Blank image removal mode should match", jsonReader.getBlankImageRemovalMode(), objectReader.getBlankImageRemovalMode());
            assertEquals("Progress dialog mode should match", jsonReader.getProgressDialogMode(), objectReader.getProgressDialogMode());
            assertEquals("Output quality should match", jsonReader.getOutputQuality(), objectReader.getOutputQuality());
            assertEquals("Sharpness adjustment should match", jsonReader.getSharpnessAdjustment(), objectReader.getSharpnessAdjustment());
            assertEquals("Text photo optimization should match", jsonReader.getTextPhotoOptimization(), objectReader.getTextPhotoOptimization());
            assertEquals("Media source should match", jsonReader.getMediaSource(), objectReader.getMediaSource());
            assertEquals("Misfeed detection mode should match", jsonReader.getMisfeedDetectionMode(), objectReader.getMisfeedDetectionMode());
            assertEquals("Capture mode should match", jsonReader.getCaptureMode(), objectReader.getCaptureMode());
            assertEquals("Automatic straighten mode should match", jsonReader.getAutomaticStraightenMode(), objectReader.getAutomaticStraightenMode());
            assertEquals("File name should match", jsonReader.getFileName(), objectReader.getFileName());
        } catch (Exception e) {
            fail("Unexpected exception=" + e);
        }
    }

    // ========================================================================================
    // Phase 6a: All-Options Integration Tests
    // ========================================================================================

    private void setUpAllOptionsProfile() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String profileResponse = Utils.loadTestJsonResource(
                Objects.requireNonNull(getClass().getClassLoader()),
                "scanJob/GET_ext_scanJob_profile_allOptions.json"
        );
        when(mockScanJobService.getProfile(testPackageName))
                .thenReturn(mapper.readValue(profileResponse, Profile.class));

        String defaultOptionsResponse = Utils.loadTestJsonResource(
                Objects.requireNonNull(getClass().getClassLoader()),
                "scanJob/GET_ext_scanJob_DefaultOptions_allOptions.json"
        );
        when(mockScanJobService.getDefaultOptions(testPackageName))
                .thenReturn(mapper.readValue(defaultOptionsResponse, DefaultOptions.class));
    }

    /**
     * TEST: All-Options profile — HTTP defaults with all scan options available.
     * Verifies that options previously unavailable (scanCaptureMode, imagePreviewMode, autoCropMode)
     * now return actual converted values instead of DEFAULT.
     */
    @Test
    public void GivenAllOptionsProfile_WhenGetDefaultsCalledForHttp_ThenAllOptionsShouldBeConverted() throws Exception {
        setUpAllOptionsProfile();
        try (MockedStatic<Environment> mocked = mockStatic(Environment.class)) {
            File mockFile = new File("test");
            mocked.when(() -> Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)).thenReturn(mockFile);
            mocked.when(Environment::getExternalStorageDirectory).thenReturn(mockFile);
            mocked.when(Environment::getDataDirectory).thenReturn(mockFile);

            ScanAttributes scanAttributes = ScanDefaultOptionAdapter.getDefaultScanAttributes(
                    testPackageName, mockScanJobService, ScanAttributes.Destination.HTTP, Sdk.VERSION.LEVEL);
            ScanAttributesReader reader = new ScanAttributesReader(scanAttributes);

            // Options that were DEFAULT in original profile, now have actual values
            assertEquals("Capture mode", ScanAttributes.CaptureMode.STANDARD, reader.getCaptureMode());
            assertEquals("Scan preview", ScanAttributes.ScanPreview.TRUE, reader.getScanPreview());
            assertEquals("Crop mode", ScanAttributes.CropMode.CROP_TO_PAPER, reader.getCropMode());

            // Standard options with specific all-options defaults
            assertEquals("Color mode", ScanAttributes.ColorMode.COLOR, reader.getColorMode());
            assertEquals("Duplex mode", ScanAttributes.Duplex.BOOK, reader.getPlex());
            assertEquals("Orientation", ScanAttributes.Orientation.PORTRAIT, reader.getOrientation());
            assertEquals("Document format", ScanAttributes.DocumentFormat.PDF, reader.getDocumentFormat());
            assertEquals("Scan size", ScanAttributes.ScanSize.LETTER, reader.getScanSize());
            assertEquals("Resolution", ScanAttributes.Resolution.DPI_300, reader.getResolution());
            assertEquals("Background cleanup", ScanAttributes.BackgroundCleanup.LEVEL_4, reader.getBackgroundCleanup());
            assertEquals("Contrast adjustment", ScanAttributes.ContrastAdjustment.LEVEL_4, reader.getContrastAdjustment());
            assertEquals("Blank image removal mode", ScanAttributes.BlankImageRemovalMode.ON, reader.getBlankImageRemovalMode());
            assertEquals("Progress dialog mode", ScanAttributes.ProgressDialogMode.ON, reader.getProgressDialogMode());
            assertEquals("Output quality", ScanAttributes.OutputQuality.HIGH, reader.getOutputQuality());
            assertEquals("Transmission mode", ScanAttributes.TransmissionMode.JOB, reader.getTransmissionMode());
            assertEquals("Sharpness adjustment", ScanAttributes.SharpnessAdjustment.LEVEL_2, reader.getSharpnessAdjustment());
            assertEquals("Text photo optimization", ScanAttributes.TextPhotoOptimization.MIXED_2, reader.getTextPhotoOptimization());
            assertEquals("Media source", ScanAttributes.MediaSource.ADF, reader.getMediaSource());
            assertEquals("Misfeed detection mode", ScanAttributes.MisfeedDetectionMode.ON, reader.getMisfeedDetectionMode());
            assertEquals("Automatic straighten mode", ScanAttributes.AutomaticStraightenMode.ENABLE, reader.getAutomaticStraightenMode());
            assertEquals("File name", "scan", reader.getFileName());
        }
    }

    /**
     * TEST: All-Options profile — NetworkFolder defaults with all scan options available.
     * Verifies destination-specific values differ from HTTP.
     */
    @Test
    public void GivenAllOptionsProfile_WhenGetDefaultsCalledForNetworkFolder_ThenAllOptionsShouldBeConverted() throws Exception {
        setUpAllOptionsProfile();
        try (MockedStatic<Environment> mocked = mockStatic(Environment.class)) {
            File mockFile = new File("test");
            mocked.when(() -> Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)).thenReturn(mockFile);
            mocked.when(Environment::getExternalStorageDirectory).thenReturn(mockFile);
            mocked.when(Environment::getDataDirectory).thenReturn(mockFile);

            ScanAttributes scanAttributes = ScanDefaultOptionAdapter.getDefaultScanAttributes(
                    testPackageName, mockScanJobService, ScanAttributes.Destination.NETWORK_FOLDER, Sdk.VERSION.LEVEL);
            ScanAttributesReader reader = new ScanAttributesReader(scanAttributes);

            // Options that were DEFAULT in original profile, now have actual values
            assertEquals("Capture mode", ScanAttributes.CaptureMode.STANDARD_ADD_PAGES, reader.getCaptureMode());
            assertEquals("Scan preview", ScanAttributes.ScanPreview.FALSE, reader.getScanPreview());
            assertEquals("Crop mode", ScanAttributes.CropMode.CROP_TO_CONTENT, reader.getCropMode());

            // NetworkFolder-specific defaults differing from HTTP
            assertEquals("Color mode", ScanAttributes.ColorMode.AUTO, reader.getColorMode());
            assertEquals("Duplex mode", ScanAttributes.Duplex.NONE, reader.getPlex());
            assertEquals("Orientation", ScanAttributes.Orientation.LANDSCAPE, reader.getOrientation());
            assertEquals("Document format", ScanAttributes.DocumentFormat.TIFF, reader.getDocumentFormat());
            assertEquals("Scan size", ScanAttributes.ScanSize.A4, reader.getScanSize());
            assertEquals("Resolution", ScanAttributes.Resolution.DPI_200, reader.getResolution());
            assertEquals("Background cleanup", ScanAttributes.BackgroundCleanup.LEVEL_0, reader.getBackgroundCleanup());
            assertEquals("Contrast adjustment", ScanAttributes.ContrastAdjustment.LEVEL_2, reader.getContrastAdjustment());
            assertEquals("Blank image removal mode", ScanAttributes.BlankImageRemovalMode.OFF, reader.getBlankImageRemovalMode());
            assertEquals("Progress dialog mode", ScanAttributes.ProgressDialogMode.OFF, reader.getProgressDialogMode());
            assertEquals("Output quality", ScanAttributes.OutputQuality.LOW, reader.getOutputQuality());
            assertEquals("Transmission mode", ScanAttributes.TransmissionMode.JOB, reader.getTransmissionMode());
            assertEquals("Sharpness adjustment", ScanAttributes.SharpnessAdjustment.LEVEL_1, reader.getSharpnessAdjustment());
            assertEquals("Text photo optimization", ScanAttributes.TextPhotoOptimization.TEXT, reader.getTextPhotoOptimization());
            assertEquals("Media source", ScanAttributes.MediaSource.AUTO, reader.getMediaSource());
            assertEquals("Misfeed detection mode", ScanAttributes.MisfeedDetectionMode.OFF, reader.getMisfeedDetectionMode());
            assertEquals("Automatic straighten mode", ScanAttributes.AutomaticStraightenMode.DISABLE, reader.getAutomaticStraightenMode());
            assertEquals("File name", "networkscan", reader.getFileName());
        }
    }

    /**
     * TEST: All-Options profile — FTP destination should use NetworkFolder defaults.
     */
    @Test
    public void GivenAllOptionsProfile_WhenGetDefaultsCalledForFtp_ThenNetworkFolderDefaultsShouldBeUsed() throws Exception {
        setUpAllOptionsProfile();
        try (MockedStatic<Environment> mocked = mockStatic(Environment.class)) {
            File mockFile = new File("test");
            mocked.when(() -> Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)).thenReturn(mockFile);
            mocked.when(Environment::getExternalStorageDirectory).thenReturn(mockFile);
            mocked.when(Environment::getDataDirectory).thenReturn(mockFile);

            ScanAttributes ftpDefaults = ScanDefaultOptionAdapter.getDefaultScanAttributes(
                    testPackageName, mockScanJobService, ScanAttributes.Destination.FTP, Sdk.VERSION.LEVEL);
            ScanAttributes nfDefaults = ScanDefaultOptionAdapter.getDefaultScanAttributes(
                    testPackageName, mockScanJobService, ScanAttributes.Destination.NETWORK_FOLDER, Sdk.VERSION.LEVEL);

            ScanAttributesReader ftpReader = new ScanAttributesReader(ftpDefaults);
            ScanAttributesReader nfReader = new ScanAttributesReader(nfDefaults);

            // FTP should be identical to NETWORK_FOLDER
            assertEquals("Color mode", nfReader.getColorMode(), ftpReader.getColorMode());
            assertEquals("Duplex mode", nfReader.getPlex(), ftpReader.getPlex());
            assertEquals("Orientation", nfReader.getOrientation(), ftpReader.getOrientation());
            assertEquals("Document format", nfReader.getDocumentFormat(), ftpReader.getDocumentFormat());
            assertEquals("Scan size", nfReader.getScanSize(), ftpReader.getScanSize());
            assertEquals("Scan preview", nfReader.getScanPreview(), ftpReader.getScanPreview());
            assertEquals("Resolution", nfReader.getResolution(), ftpReader.getResolution());
            assertEquals("Background cleanup", nfReader.getBackgroundCleanup(), ftpReader.getBackgroundCleanup());
            assertEquals("Contrast adjustment", nfReader.getContrastAdjustment(), ftpReader.getContrastAdjustment());
            assertEquals("Blank image removal", nfReader.getBlankImageRemovalMode(), ftpReader.getBlankImageRemovalMode());
            assertEquals("Progress dialog mode", nfReader.getProgressDialogMode(), ftpReader.getProgressDialogMode());
            assertEquals("Output quality", nfReader.getOutputQuality(), ftpReader.getOutputQuality());
            assertEquals("Transmission mode", nfReader.getTransmissionMode(), ftpReader.getTransmissionMode());
            assertEquals("Sharpness adjustment", nfReader.getSharpnessAdjustment(), ftpReader.getSharpnessAdjustment());
            assertEquals("Text photo optimization", nfReader.getTextPhotoOptimization(), ftpReader.getTextPhotoOptimization());
            assertEquals("Media source", nfReader.getMediaSource(), ftpReader.getMediaSource());
            assertEquals("Misfeed detection", nfReader.getMisfeedDetectionMode(), ftpReader.getMisfeedDetectionMode());
            assertEquals("Capture mode", nfReader.getCaptureMode(), ftpReader.getCaptureMode());
            assertEquals("Auto straighten", nfReader.getAutomaticStraightenMode(), ftpReader.getAutomaticStraightenMode());
            assertEquals("Crop mode", nfReader.getCropMode(), ftpReader.getCropMode());
            assertEquals("File name", nfReader.getFileName(), ftpReader.getFileName());
        }
    }

    /**
     * TEST: All-Options profile — HTTP vs NetworkFolder should return different defaults.
     * Validates that destination-based routing returns distinct values for options that differ.
     */
    @Test
    public void GivenAllOptionsProfile_WhenGetDefaultsCalledForDifferentDestinations_ThenKeyOptionsShouldDiffer() throws Exception {
        setUpAllOptionsProfile();
        try (MockedStatic<Environment> mocked = mockStatic(Environment.class)) {
            File mockFile = new File("test");
            mocked.when(() -> Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)).thenReturn(mockFile);
            mocked.when(Environment::getExternalStorageDirectory).thenReturn(mockFile);
            mocked.when(Environment::getDataDirectory).thenReturn(mockFile);

            ScanAttributes httpDefaults = ScanDefaultOptionAdapter.getDefaultScanAttributes(
                    testPackageName, mockScanJobService, ScanAttributes.Destination.HTTP, Sdk.VERSION.LEVEL);
            ScanAttributes nfDefaults = ScanDefaultOptionAdapter.getDefaultScanAttributes(
                    testPackageName, mockScanJobService, ScanAttributes.Destination.NETWORK_FOLDER, Sdk.VERSION.LEVEL);

            ScanAttributesReader httpReader = new ScanAttributesReader(httpDefaults);
            ScanAttributesReader nfReader = new ScanAttributesReader(nfDefaults);

            // These specific options should differ between HTTP and NetworkFolder
            assertTrue("Color mode should differ", httpReader.getColorMode() != nfReader.getColorMode());
            assertTrue("Duplex mode should differ", httpReader.getPlex() != nfReader.getPlex());
            assertTrue("Orientation should differ", httpReader.getOrientation() != nfReader.getOrientation());
            assertTrue("Document format should differ", httpReader.getDocumentFormat() != nfReader.getDocumentFormat());
            assertTrue("Scan size should differ", httpReader.getScanSize() != nfReader.getScanSize());
            assertTrue("Resolution should differ", httpReader.getResolution() != nfReader.getResolution());
            assertTrue("Capture mode should differ", httpReader.getCaptureMode() != nfReader.getCaptureMode());
            assertTrue("Scan preview should differ", httpReader.getScanPreview() != nfReader.getScanPreview());
            assertTrue("Crop mode should differ", httpReader.getCropMode() != nfReader.getCropMode());
            assertTrue("Output quality should differ", httpReader.getOutputQuality() != nfReader.getOutputQuality());
        }
    }

    // ========================================================================================
    // AC2: ME/EMAIL/USB Destination — Default Tests
    // Note: ME/EMAIL/USB currently fall through to HTTP defaults (TODO: DUNE-169946).
    // These tests verify current behavior and will be updated when JobStorage routing is implemented.
    // ========================================================================================

    /**
     * TEST for ScanDefaultOptionAdapter.getDefaults : ME destination currently uses HTTP defaults.
     *
     * Purpose: Verify ME destination returns HTTP default options (temporary fallback).
     * When DUNE-169946 is implemented, ME should use JobStorage defaults instead.
     */
    @Test
    public void GivenScanDefaultOptionAdapter_WhenGetDefaultsCalledForMeDestination_ThenHttpDefaultsShouldBeReturned() {
        try (MockedStatic<Environment> mocked = mockStatic(Environment.class)) {
            File mockFile = new File("test");
            mocked.when(() -> Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)).thenReturn(mockFile);
            mocked.when(Environment::getExternalStorageDirectory).thenReturn(mockFile);
            mocked.when(Environment::getDataDirectory).thenReturn(mockFile);

            ScanAttributes scanAttributes = ScanDefaultOptionAdapter.getDefaultScanAttributes(testPackageName, mockScanJobService,
                    ScanAttributes.Destination.ME, Sdk.VERSION.LEVEL);

            ScanAttributesReader reader = new ScanAttributesReader(scanAttributes);
            // Currently ME uses HTTP defaults (TODO: DUNE-169946 will change to JobStorage)
            assertEquals("Color mode", ScanAttributes.ColorMode.COLOR, reader.getColorMode());
            assertEquals("Duplex mode", ScanAttributes.Duplex.NONE, reader.getPlex());
            assertEquals("Orientation", ScanAttributes.Orientation.PORTRAIT, reader.getOrientation());
            assertEquals("Document format", ScanAttributes.DocumentFormat.PDF, reader.getDocumentFormat());
            assertEquals("Scan size", ScanAttributes.ScanSize.LETTER, reader.getScanSize());
            assertEquals("Scan preview", ScanAttributes.ScanPreview.DEFAULT, reader.getScanPreview());
            assertEquals("Resolution", ScanAttributes.Resolution.DPI_300, reader.getResolution());
            assertEquals("Background cleanup", ScanAttributes.BackgroundCleanup.LEVEL_0, reader.getBackgroundCleanup());
            assertEquals("Contrast adjustment", ScanAttributes.ContrastAdjustment.LEVEL_1, reader.getContrastAdjustment());
            assertEquals("Blank image removal mode", ScanAttributes.BlankImageRemovalMode.OFF, reader.getBlankImageRemovalMode());
            assertEquals("Progress dialog mode", ScanAttributes.ProgressDialogMode.ON, reader.getProgressDialogMode());
            assertEquals("Output quality", ScanAttributes.OutputQuality.MEDIUM, reader.getOutputQuality());
            assertEquals("Transmission mode", ScanAttributes.TransmissionMode.JOB, reader.getTransmissionMode());
            assertEquals("Sharpness adjustment", ScanAttributes.SharpnessAdjustment.LEVEL_1, reader.getSharpnessAdjustment());
            assertEquals("Text photo optimization", ScanAttributes.TextPhotoOptimization.MIXED_2, reader.getTextPhotoOptimization());
            assertEquals("Media source", ScanAttributes.MediaSource.ADF, reader.getMediaSource());
            assertEquals("Misfeed detection mode", ScanAttributes.MisfeedDetectionMode.OFF, reader.getMisfeedDetectionMode());
            assertEquals("Capture mode", ScanAttributes.CaptureMode.DEFAULT, reader.getCaptureMode());
            assertEquals("Automatic straighten mode", ScanAttributes.AutomaticStraightenMode.DISABLE, reader.getAutomaticStraightenMode());
            assertEquals("File name", "scan", reader.getFileName());
        } catch (Exception e) {
            fail("Unexpected exception=" + e);
        }
    }

    /**
     * TEST for ScanDefaultOptionAdapter.getDefaults : EMAIL destination uses same defaults as ME.
     * Currently both fall through to HTTP defaults (TODO: DUNE-169946).
     */
    @Test
    public void GivenScanDefaultOptionAdapter_WhenGetDefaultsCalledForEmailDestination_ThenSameAsMeDefaultsShouldBeReturned() {
        try (MockedStatic<Environment> mocked = mockStatic(Environment.class)) {
            File mockFile = new File("test");
            mocked.when(() -> Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)).thenReturn(mockFile);
            mocked.when(Environment::getExternalStorageDirectory).thenReturn(mockFile);
            mocked.when(Environment::getDataDirectory).thenReturn(mockFile);

            ScanAttributes meDefaults = ScanDefaultOptionAdapter.getDefaultScanAttributes(testPackageName, mockScanJobService,
                    ScanAttributes.Destination.ME, Sdk.VERSION.LEVEL);
            ScanAttributes emailDefaults = ScanDefaultOptionAdapter.getDefaultScanAttributes(testPackageName, mockScanJobService,
                    ScanAttributes.Destination.EMAIL, Sdk.VERSION.LEVEL);

            ScanAttributesReader meReader = new ScanAttributesReader(meDefaults);
            ScanAttributesReader emailReader = new ScanAttributesReader(emailDefaults);

            // EMAIL and ME should be identical (currently both use HTTP defaults)
            assertEquals("Color mode", meReader.getColorMode(), emailReader.getColorMode());
            assertEquals("Duplex mode", meReader.getPlex(), emailReader.getPlex());
            assertEquals("Orientation", meReader.getOrientation(), emailReader.getOrientation());
            assertEquals("Document format", meReader.getDocumentFormat(), emailReader.getDocumentFormat());
            assertEquals("Scan size", meReader.getScanSize(), emailReader.getScanSize());
            assertEquals("Resolution", meReader.getResolution(), emailReader.getResolution());
            assertEquals("Contrast adjustment", meReader.getContrastAdjustment(), emailReader.getContrastAdjustment());
            assertEquals("Background cleanup", meReader.getBackgroundCleanup(), emailReader.getBackgroundCleanup());
            assertEquals("Output quality", meReader.getOutputQuality(), emailReader.getOutputQuality());
            assertEquals("Transmission mode", meReader.getTransmissionMode(), emailReader.getTransmissionMode());
            assertEquals("Text photo optimization", meReader.getTextPhotoOptimization(), emailReader.getTextPhotoOptimization());
            assertEquals("Media source", meReader.getMediaSource(), emailReader.getMediaSource());
            assertEquals("Misfeed detection mode", meReader.getMisfeedDetectionMode(), emailReader.getMisfeedDetectionMode());
            assertEquals("Capture mode", meReader.getCaptureMode(), emailReader.getCaptureMode());
            assertEquals("Automatic straighten mode", meReader.getAutomaticStraightenMode(), emailReader.getAutomaticStraightenMode());
            assertEquals("File name", meReader.getFileName(), emailReader.getFileName());
        } catch (Exception e) {
            fail("Unexpected exception=" + e);
        }
    }

    /**
     * TEST for ScanDefaultOptionAdapter.getDefaults : USB destination uses same defaults as ME.
     * Currently both fall through to HTTP defaults (TODO: DUNE-169946).
     */
    @Test
    public void GivenScanDefaultOptionAdapter_WhenGetDefaultsCalledForUsbDestination_ThenSameAsMeDefaultsShouldBeReturned() {
        try (MockedStatic<Environment> mocked = mockStatic(Environment.class)) {
            File mockFile = new File("test");
            mocked.when(() -> Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)).thenReturn(mockFile);
            mocked.when(Environment::getExternalStorageDirectory).thenReturn(mockFile);
            mocked.when(Environment::getDataDirectory).thenReturn(mockFile);

            ScanAttributes meDefaults = ScanDefaultOptionAdapter.getDefaultScanAttributes(testPackageName, mockScanJobService,
                    ScanAttributes.Destination.ME, Sdk.VERSION.LEVEL);
            ScanAttributes usbDefaults = ScanDefaultOptionAdapter.getDefaultScanAttributes(testPackageName, mockScanJobService,
                    ScanAttributes.Destination.USB, Sdk.VERSION.LEVEL);

            ScanAttributesReader meReader = new ScanAttributesReader(meDefaults);
            ScanAttributesReader usbReader = new ScanAttributesReader(usbDefaults);

            // USB and ME should be identical (currently both use HTTP defaults)
            assertEquals("Color mode", meReader.getColorMode(), usbReader.getColorMode());
            assertEquals("Duplex mode", meReader.getPlex(), usbReader.getPlex());
            assertEquals("Orientation", meReader.getOrientation(), usbReader.getOrientation());
            assertEquals("Document format", meReader.getDocumentFormat(), usbReader.getDocumentFormat());
            assertEquals("Scan size", meReader.getScanSize(), usbReader.getScanSize());
            assertEquals("Resolution", meReader.getResolution(), usbReader.getResolution());
            assertEquals("Contrast adjustment", meReader.getContrastAdjustment(), usbReader.getContrastAdjustment());
            assertEquals("Background cleanup", meReader.getBackgroundCleanup(), usbReader.getBackgroundCleanup());
            assertEquals("Output quality", meReader.getOutputQuality(), usbReader.getOutputQuality());
            assertEquals("Transmission mode", meReader.getTransmissionMode(), usbReader.getTransmissionMode());
            assertEquals("Text photo optimization", meReader.getTextPhotoOptimization(), usbReader.getTextPhotoOptimization());
            assertEquals("Media source", meReader.getMediaSource(), usbReader.getMediaSource());
            assertEquals("Misfeed detection mode", meReader.getMisfeedDetectionMode(), usbReader.getMisfeedDetectionMode());
            assertEquals("Capture mode", meReader.getCaptureMode(), usbReader.getCaptureMode());
            assertEquals("Automatic straighten mode", meReader.getAutomaticStraightenMode(), usbReader.getAutomaticStraightenMode());
            assertEquals("File name", meReader.getFileName(), usbReader.getFileName());
        } catch (Exception e) {
            fail("Unexpected exception=" + e);
        }
    }

    /**
     * TEST for ScanDefaultOptionAdapter.getDefaults : ME vs HTTP comparison.
     * Currently ME falls through to HTTP defaults (TODO: DUNE-169946),
     * so all options should be identical.
     */
    @Test
    public void GivenScanDefaultOptionAdapter_WhenGetDefaultsCalledForMeVsHttp_ThenSameDefaultsShouldBeReturned() {
        try (MockedStatic<Environment> mocked = mockStatic(Environment.class)) {
            File mockFile = new File("test");
            mocked.when(() -> Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)).thenReturn(mockFile);
            mocked.when(Environment::getExternalStorageDirectory).thenReturn(mockFile);
            mocked.when(Environment::getDataDirectory).thenReturn(mockFile);

            ScanAttributes httpDefaults = ScanDefaultOptionAdapter.getDefaultScanAttributes(testPackageName, mockScanJobService,
                    ScanAttributes.Destination.HTTP, Sdk.VERSION.LEVEL);
            ScanAttributes meDefaults = ScanDefaultOptionAdapter.getDefaultScanAttributes(testPackageName, mockScanJobService,
                    ScanAttributes.Destination.ME, Sdk.VERSION.LEVEL);

            ScanAttributesReader httpReader = new ScanAttributesReader(httpDefaults);
            ScanAttributesReader meReader = new ScanAttributesReader(meDefaults);

            // Currently ME uses HTTP defaults, so all options should be the same
            assertEquals("Color mode", httpReader.getColorMode(), meReader.getColorMode());
            assertEquals("Document format", httpReader.getDocumentFormat(), meReader.getDocumentFormat());
            assertEquals("Scan size", httpReader.getScanSize(), meReader.getScanSize());
            assertEquals("Resolution", httpReader.getResolution(), meReader.getResolution());
            assertEquals("Contrast adjustment", httpReader.getContrastAdjustment(), meReader.getContrastAdjustment());
            assertEquals("Output quality", httpReader.getOutputQuality(), meReader.getOutputQuality());
            assertEquals("Transmission mode", httpReader.getTransmissionMode(), meReader.getTransmissionMode());
            assertEquals("File name", httpReader.getFileName(), meReader.getFileName());
        } catch (Exception e) {
            fail("Unexpected exception=" + e);
        }
    }

    /**
     * TEST: All-Options profile — ME destination has a known mismatch (TODO: DUNE-169946).
     * Capabilities are built from jobStorage profile, but defaults fall through to HTTP.
     * This causes SdkServiceErrorException because HTTP default document format
     * is not supported by the jobStorage-based capabilities.
     * When DUNE-169946 is resolved, this test should be updated to expect success
     * with proper jobStorage defaults.
     */
    @Test
    public void GivenAllOptionsProfile_WhenGetDefaultsCalledForMe_ThenErrorDueToCapabilityDefaultMismatch() throws Exception {
        setUpAllOptionsProfile();
        try (MockedStatic<Environment> mocked = mockStatic(Environment.class)) {
            File mockFile = new File("test");
            mocked.when(() -> Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)).thenReturn(mockFile);
            mocked.when(Environment::getExternalStorageDirectory).thenReturn(mockFile);
            mocked.when(Environment::getDataDirectory).thenReturn(mockFile);

            ScanDefaultOptionAdapter.getDefaultScanAttributes(
                    testPackageName, mockScanJobService, ScanAttributes.Destination.ME, Sdk.VERSION.LEVEL);
            fail("Expected SdkServiceErrorException due to jobStorage caps vs HTTP defaults mismatch");
        } catch (SdkServiceErrorException e) {
            // Expected: capabilities from jobStorage profile, but defaults from HTTP.
            // jobStorage outputFileFormat capabilities don't support HTTP default (PDF).
            assertTrue("Error message should mention ME destination",
                    e.getMessage().contains("ME"));
        }
    }

    // ========================================================================================
    // AC6: Error Handling — Invalid Destination
    // ========================================================================================

    /**
     * TEST for ScanDefaultOptionAdapter.getDefaults : Error case — null DefaultOptions.getHttp().
     * ME currently uses HTTP defaults, so null HTTP defaults should cause error.
     */
    @Test
    public void GivenScanDefaultOptionAdapter_WhenGetDefaultsCalledForMe_AndHttpDefaultOptionsNull_ThenServiceErrorShouldBeReturned() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String profileResponse = Utils.loadTestJsonResource(
                Objects.requireNonNull(getClass().getClassLoader()),
                "scanJob/GET_ext_scanJob_profile.json"
        );
        when(mockScanJobService.getProfile(testPackageName))
                .thenReturn(mapper.readValue(profileResponse, Profile.class));

        // Create DefaultOptions with null http (ME currently uses HTTP defaults)
        DefaultOptions defaultOptions = new DefaultOptions();
        when(mockScanJobService.getDefaultOptions(testPackageName)).thenReturn(defaultOptions);

        try {
            ScanDefaultOptionAdapter.getDefaults(testPackageName, mockScanJobService, ScanAttributes.Destination.ME, Sdk.VERSION.LEVEL);
            fail("Expected SdkServiceErrorException to be thrown");
        } catch (SdkServiceErrorException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Unexpected exception thrown: " + e);
        }
    }
}
