/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.scanlet.adapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.ext.service.scanJob.DefaultOptions;
import com.hp.ext.service.scanJob.Profile;
import com.hp.jetadvantage.link.api.CapabilitiesExceededException;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes;
import com.hp.jetadvantage.link.api.scanner.ScanAttributesCaps;
import com.hp.jetadvantage.link.api.scanner.ScanAttributesReader;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.utils.JsonParser;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceScanJobService;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.utils.Utils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Objects;

/**
 * Validation-focused tests for ScanTicket generation.
 * Tests isAvailable filtering, possibleValues enforcement, range boundaries,
 * and silent fallback behavior.
 * <p>
 * Note: This class does NOT use mockStatic(Environment.class) to avoid
 * Byte Buddy / Java 21 incompatibility issues with Android distribution classes.
 */
@RunWith(MockitoJUnitRunner.class)
public class ScanTicketValidationUnitTest {
    public String testPackageName = Constants.TEST_PACKAGE_NAME;

    @Mock
    private IDeviceScanJobService mockScanJobService;

    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        // Load standard profile and default options
        String profileResponse = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()),
                "scanJob/GET_ext_scanJob_profile.json");
        when(mockScanJobService.getProfile(testPackageName))
                .thenReturn(mapper.readValue(profileResponse, Profile.class));
        String defaultOptionsResponse = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()),
                "scanJob/GET_ext_scanJob_defaultOptions.json");
        defaultOptionsResponse = E2JsonTestHelper.simplifyE2Json(defaultOptionsResponse);
        when(mockScanJobService.getDefaultOptions(testPackageName))
                .thenReturn(mapper.readValue(defaultOptionsResponse, DefaultOptions.class));
    }

    // Helper methods
    private ScanAttributesCaps getScanAttributesCaps() throws Exception {
        String capabilitiesJson = ScanOptionProfileAdapter.getCapabilities(testPackageName, mockScanJobService,
                ScanAttributes.TransmissionMode.JOB, ScanAttributes.Destination.HTTP, Sdk.VERSION.LEVEL);
        return JsonParser.getInstance().fromJson(capabilitiesJson, ScanAttributesCaps.class);
    }

    private void loadAllOptionsProfileAndDefaults() throws Exception {
        String profileResponse = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()),
                "scanJob/GET_ext_scanJob_profile_allOptions.json");
        when(mockScanJobService.getProfile(testPackageName))
                .thenReturn(mapper.readValue(profileResponse, Profile.class));
        String defaultOptionsResponse = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()),
                "scanJob/GET_ext_scanJob_DefaultOptions_allOptions.json");
        when(mockScanJobService.getDefaultOptions(testPackageName))
                .thenReturn(mapper.readValue(defaultOptionsResponse, DefaultOptions.class));
    }

    // ========================================================================================
    // Test 1: isAvailable: false option → CapabilitiesExceededException
    // ========================================================================================

    /**
     * scanCaptureMode is isAvailable: false in the standard profile.
     * The CaptureModeList only contains DEFAULT.
     * Attempting to set CaptureMode.STANDARD should throw CapabilitiesExceededException.
     */
    @Test(expected = CapabilitiesExceededException.class)
    public void GivenStandardProfile_WhenUnavailableCaptureModeSelected_ThenCapabilitiesExceededExceptionShouldBeThrown() throws Exception {
        ScanAttributesCaps scanAttributeCaps = getScanAttributesCaps();

        // scanCaptureMode is isAvailable: false → only DEFAULT in capabilities
        assertEquals("CaptureModeList should only contain DEFAULT",
                1, scanAttributeCaps.getCaptureModeList().size());
        assertTrue("CaptureModeList should contain DEFAULT",
                scanAttributeCaps.getCaptureModeList().contains(ScanAttributes.CaptureMode.DEFAULT));

        // Setting CaptureMode.STANDARD should fail during build()
        new ScanAttributes.HttpBuilder(null)
                .setCaptureMode(ScanAttributes.CaptureMode.STANDARD)
                .setFileName("test")
                .build(scanAttributeCaps);
    }

    /**
     * imagePreviewMode is isAvailable: false in the standard profile.
     * Setting ScanPreview.TRUE should throw CapabilitiesExceededException.
     */
    @Test(expected = CapabilitiesExceededException.class)
    public void GivenStandardProfile_WhenUnavailableScanPreviewSelected_ThenCapabilitiesExceededExceptionShouldBeThrown() throws Exception {
        ScanAttributesCaps scanAttributeCaps = getScanAttributesCaps();

        // imagePreviewMode is isAvailable: false → only DEFAULT
        assertEquals("ScanPreviewList should only contain DEFAULT",
                1, scanAttributeCaps.getScanPreviewList().size());

        new ScanAttributes.HttpBuilder(null)
                .setScanPreview(ScanAttributes.ScanPreview.TRUE)
                .setFileName("test")
                .build(scanAttributeCaps);
    }

    // ========================================================================================
    // Test 2: possibleValues에 없는 option → CapabilitiesExceededException
    // ========================================================================================

    /**
     * Standard profile resolution possibleValues: [dpi75, dpi150, dpi200, dpi300, dpi400, dpi600]
     * DPI_500 is NOT in possibleValues → not in capabilities → build() should throw
     */
    @Test(expected = CapabilitiesExceededException.class)
    public void GivenStandardProfile_WhenResolutionNotInPossibleValues_ThenCapabilitiesExceededExceptionShouldBeThrown() throws Exception {
        ScanAttributesCaps scanAttributeCaps = getScanAttributesCaps();

        assertFalse("ResolutionList should NOT contain DPI_500",
                scanAttributeCaps.getResolutionList().contains(ScanAttributes.Resolution.DPI_500));

        new ScanAttributes.HttpBuilder(null)
                .setResolution(ScanAttributes.Resolution.DPI_500)
                .setFileName("test")
                .build(scanAttributeCaps);
    }

    /**
     * Standard profile mediaSource possibleValues: [miFlatbed, miAdf] (no miAuto)
     * MediaSource.AUTO is NOT in possibleValues → not in capabilities → build() should throw
     */
    @Test(expected = CapabilitiesExceededException.class)
    public void GivenStandardProfile_WhenMediaSourceNotInPossibleValues_ThenCapabilitiesExceededExceptionShouldBeThrown() throws Exception {
        ScanAttributesCaps scanAttributeCaps = getScanAttributesCaps();

        assertFalse("MediaSourceList should NOT contain AUTO",
                scanAttributeCaps.getMediaSourceList().contains(ScanAttributes.MediaSource.AUTO));

        new ScanAttributes.HttpBuilder(null)
                .setMediaSource(ScanAttributes.MediaSource.AUTO)
                .setFileName("test")
                .build(scanAttributeCaps);
    }

    /**
     * Standard profile contentOrientation possibleValues: [coPortrait, coLandscape] (no coAutoDetect)
     * Orientation.AUTO_DETECT is NOT in possibleValues → build() should throw
     */
    @Test(expected = CapabilitiesExceededException.class)
    public void GivenStandardProfile_WhenOrientationNotInPossibleValues_ThenCapabilitiesExceededExceptionShouldBeThrown() throws Exception {
        ScanAttributesCaps scanAttributeCaps = getScanAttributesCaps();

        assertFalse("OrientationList should NOT contain AUTO_DETECT",
                scanAttributeCaps.getOrientationList().contains(ScanAttributes.Orientation.AUTO_DETECT));

        new ScanAttributes.HttpBuilder(null)
                .setOrientation(ScanAttributes.Orientation.AUTO_DETECT)
                .setFileName("test")
                .build(scanAttributeCaps);
    }

    /**
     * Resolution DPI_100 does not exist in standard profile values.
     */
    @Test(expected = CapabilitiesExceededException.class)
    public void GivenStandardProfile_WhenResolutionDpi100NotInDeviceRange_ThenCapabilitiesExceededExceptionShouldBeThrown() throws Exception {
        ScanAttributesCaps scanAttributeCaps = getScanAttributesCaps();

        assertFalse("ResolutionList should NOT contain DPI_100",
                scanAttributeCaps.getResolutionList().contains(ScanAttributes.Resolution.DPI_100));

        new ScanAttributes.HttpBuilder(null)
                .setResolution(ScanAttributes.Resolution.DPI_100)
                .setFileName("test")
                .build(scanAttributeCaps);
    }

    // ========================================================================================
    // Test 3: ENUM_RANGE boundaries and filtering
    // ========================================================================================

    /**
     * Verify ENUM_RANGE options produce correct capability list sizes.
     * contrast range: 0-8, enum LEVEL_0~LEVEL_8 → 9 levels + DEFAULT = 10
     * backgroundCleanup range: 0-100, enum LEVEL_0~LEVEL_8 → 9 levels + DEFAULT = 10
     * sharpness range: 0-8, enum LEVEL_0~LEVEL_4 → 5 levels + DEFAULT = 6
     * exposureLevel range: 0-8, enum LEVEL_0~LEVEL_8 → 9 levels + DEFAULT = 10
     */
    @Test
    public void GivenStandardProfile_WhenGetCapabilitiesCalled_ThenEnumRangeOptionsShouldHaveCorrectSize() throws Exception {
        ScanAttributesCaps scanAttributeCaps = getScanAttributesCaps();

        assertEquals("ContrastAdjustmentList should have 10 entries (LEVEL_0~8 + DEFAULT)",
                10, scanAttributeCaps.getContrastAdjustmentList().size());
        assertEquals("BackgroundCleanupList should have 10 entries (LEVEL_0~8 + DEFAULT)",
                10, scanAttributeCaps.getBackgroundCleanupList().size());
        assertEquals("SharpnessAdjustmentList should have 6 entries (LEVEL_0~4 + DEFAULT)",
                6, scanAttributeCaps.getSharpnessAdjustmentList().size());
        assertEquals("DarknessAdjustmentList should have 10 entries (LEVEL_0~8 + DEFAULT)",
                10, scanAttributeCaps.getDarknessAdjustmentList().size());
    }

    // ========================================================================================
    // Test 4: isAvailable filtering verification + possibleValues accuracy
    // ========================================================================================

    /**
     * isAvailable: false options should only produce DEFAULT in capability lists.
     */
    @Test
    public void GivenStandardProfile_WhenGetCapabilitiesCalled_ThenUnavailableOptionsShouldOnlyHaveDefault() throws Exception {
        ScanAttributesCaps scanAttributeCaps = getScanAttributesCaps();

        // scanCaptureMode: isAvailable = false → only DEFAULT
        assertEquals("CaptureModeList should only contain DEFAULT",
                1, scanAttributeCaps.getCaptureModeList().size());
        assertTrue(scanAttributeCaps.getCaptureModeList().contains(ScanAttributes.CaptureMode.DEFAULT));

        // imagePreviewMode: isAvailable = false → only DEFAULT
        assertEquals("ScanPreviewList should only contain DEFAULT",
                1, scanAttributeCaps.getScanPreviewList().size());
        assertTrue(scanAttributeCaps.getScanPreviewList().contains(ScanAttributes.ScanPreview.DEFAULT));

        // autoCropMode: not in standard profile → only DEFAULT
        assertEquals("CropModeList should only contain DEFAULT",
                1, scanAttributeCaps.getCropModeList().size());
        assertTrue(scanAttributeCaps.getCropModeList().contains(ScanAttributes.CropMode.DEFAULT));
    }

    /**
     * Resolution capabilities should only include values from possibleValues.
     * Standard profile resolution: [dpi75, dpi150, dpi200, dpi300, dpi400, dpi600]
     * DPI_100, DPI_240, DPI_500 should be filtered out.
     */
    @Test
    public void GivenStandardProfile_WhenGetCapabilitiesCalled_ThenResolutionListShouldOnlyContainPossibleValues() throws Exception {
        ScanAttributesCaps scanAttributeCaps = getScanAttributesCaps();

        assertEquals("ResolutionList should have 7 entries (6 + DEFAULT)", 7, scanAttributeCaps.getResolutionList().size());
        assertTrue("Should contain DEFAULT", scanAttributeCaps.getResolutionList().contains(ScanAttributes.Resolution.DEFAULT));
        assertTrue("Should contain DPI_75", scanAttributeCaps.getResolutionList().contains(ScanAttributes.Resolution.DPI_75));
        assertTrue("Should contain DPI_150", scanAttributeCaps.getResolutionList().contains(ScanAttributes.Resolution.DPI_150));
        assertTrue("Should contain DPI_200", scanAttributeCaps.getResolutionList().contains(ScanAttributes.Resolution.DPI_200));
        assertTrue("Should contain DPI_300", scanAttributeCaps.getResolutionList().contains(ScanAttributes.Resolution.DPI_300));
        assertTrue("Should contain DPI_400", scanAttributeCaps.getResolutionList().contains(ScanAttributes.Resolution.DPI_400));
        assertTrue("Should contain DPI_600", scanAttributeCaps.getResolutionList().contains(ScanAttributes.Resolution.DPI_600));
        // Not in possibleValues → filtered out
        assertFalse("Should NOT contain DPI_100", scanAttributeCaps.getResolutionList().contains(ScanAttributes.Resolution.DPI_100));
        assertFalse("Should NOT contain DPI_240", scanAttributeCaps.getResolutionList().contains(ScanAttributes.Resolution.DPI_240));
        assertFalse("Should NOT contain DPI_500", scanAttributeCaps.getResolutionList().contains(ScanAttributes.Resolution.DPI_500));
    }

    /**
     * MediaSource capabilities should only include values from possibleValues.
     * Standard profile has [miFlatbed, miAdf], no miAuto.
     */
    @Test
    public void GivenStandardProfile_WhenGetCapabilitiesCalled_ThenMediaSourceListShouldMatchPossibleValues() throws Exception {
        ScanAttributesCaps scanAttributeCaps = getScanAttributesCaps();

        assertEquals("MediaSourceList should have 3 entries (2 + DEFAULT)", 3, scanAttributeCaps.getMediaSourceList().size());
        assertTrue("Should contain FLATBED", scanAttributeCaps.getMediaSourceList().contains(ScanAttributes.MediaSource.FLATBED));
        assertTrue("Should contain ADF", scanAttributeCaps.getMediaSourceList().contains(ScanAttributes.MediaSource.ADF));
        assertTrue("Should contain DEFAULT", scanAttributeCaps.getMediaSourceList().contains(ScanAttributes.MediaSource.DEFAULT));
        assertFalse("Should NOT contain AUTO", scanAttributeCaps.getMediaSourceList().contains(ScanAttributes.MediaSource.AUTO));
    }

    /**
     * When DEFAULT values are used for all options, build(caps) should succeed
     * since DEFAULT is always included in every capability list.
     */
    @Test
    public void GivenStandardProfile_WhenDefaultOptionSelected_ThenBuildShouldSucceedWithoutException() throws Exception {
        ScanAttributesCaps scanAttributeCaps = getScanAttributesCaps();

        // Build with all DEFAULT values (no explicit settings except fileName)
        ScanAttributes scanAttributes = new ScanAttributes.HttpBuilder(null)
                .setFileName("test")
                .build(scanAttributeCaps);

        ScanAttributesReader scanAttributesReader = new ScanAttributesReader(scanAttributes);
        assertEquals("Destination should be HTTP",
                ScanAttributes.Destination.HTTP, scanAttributesReader.getDestination());
        assertEquals("ColorMode should be DEFAULT",
                ScanAttributes.ColorMode.DEFAULT, scanAttributesReader.getColorMode());
    }

    // ========================================================================================
    // Test 5: allOptions profile — previously unavailable options become available
    // ========================================================================================

    /**
     * With allOptions profile, previously isAvailable:false options become available.
     * CaptureMode.STANDARD should be in capabilities and build should succeed.
     */
    @Test
    public void GivenAllOptionsProfile_WhenCaptureModeSet_ThenBuildShouldSucceedWithoutException() throws Exception {
        loadAllOptionsProfileAndDefaults();
        ScanAttributesCaps scanAttributeCaps = getScanAttributesCaps();

        // Verify CaptureMode is now available (from allOptions profile)
        assertTrue("CaptureModeList should contain STANDARD",
                scanAttributeCaps.getCaptureModeList().contains(ScanAttributes.CaptureMode.STANDARD));

        // Build with CaptureMode.STANDARD — should NOT throw CapabilitiesExceededException
        ScanAttributes scanAttributes = new ScanAttributes.HttpBuilder(null)
                .setCaptureMode(ScanAttributes.CaptureMode.STANDARD)
                .setFileName("test")
                .build(scanAttributeCaps);

        ScanAttributesReader scanAttributesReader = new ScanAttributesReader(scanAttributes);
        assertEquals("CaptureMode should be STANDARD",
                ScanAttributes.CaptureMode.STANDARD, scanAttributesReader.getCaptureMode());
    }

    /**
     * With allOptions profile, ScanPreview and CropMode become available.
     * Verify these options are correctly included in capabilities.
     */
    @Test
    public void GivenAllOptionsProfile_WhenGetCapabilitiesCalled_ThenPreviouslyUnavailableOptionsShouldBeAvailable() throws Exception {
        loadAllOptionsProfileAndDefaults();
        ScanAttributesCaps scanAttributeCaps = getScanAttributesCaps();

        // scanCaptureMode: now isAvailable = true → should have 4 values + DEFAULT = 5
        assertTrue("CaptureModeList should have > 1 entry",
                scanAttributeCaps.getCaptureModeList().size() > 1);
        assertTrue("Should contain STANDARD",
                scanAttributeCaps.getCaptureModeList().contains(ScanAttributes.CaptureMode.STANDARD));

        // imagePreviewMode: now isAvailable = true → should have values
        assertTrue("ScanPreviewList should have > 1 entry",
                scanAttributeCaps.getScanPreviewList().size() > 1);
        assertTrue("Should contain TRUE",
                scanAttributeCaps.getScanPreviewList().contains(ScanAttributes.ScanPreview.TRUE));

        // autoCropMode: now isAvailable = true
        assertTrue("CropModeList should have > 1 entry",
                scanAttributeCaps.getCropModeList().size() > 1);

        // mediaSource: now has miAuto in addition to miFlatbed, miAdf
        assertTrue("MediaSourceList should contain AUTO",
                scanAttributeCaps.getMediaSourceList().contains(ScanAttributes.MediaSource.AUTO));
    }

    /**
     * Verify ENUM_RANGE values are correctly included in capability lists and build succeeds.
     * BackgroundCleanup.LEVEL_4 should be in the capability list and build should not throw.
     */
    @Test
    public void GivenStandardProfile_WhenBackgroundCleanupLevel4Set_ThenBuildShouldSucceedWithinRange() throws Exception {
        ScanAttributesCaps scanAttributeCaps = getScanAttributesCaps();

        // Verify LEVEL_4 is available in capabilities
        assertTrue("BackgroundCleanupList should contain LEVEL_4",
                scanAttributeCaps.getBackgroundCleanupList().contains(ScanAttributes.BackgroundCleanup.LEVEL_4));

        // Build should succeed — LEVEL_4 is within the E2 range [0-100]
        ScanAttributes scanAttributes = new ScanAttributes.HttpBuilder(null)
                .setBackgroundCleanup(ScanAttributes.BackgroundCleanup.LEVEL_4)
                .setFileName("test")
                .build(scanAttributeCaps);

        ScanAttributesReader scanAttributesReader = new ScanAttributesReader(scanAttributes);
        assertEquals("BackgroundCleanup should be LEVEL_4",
                ScanAttributes.BackgroundCleanup.LEVEL_4, scanAttributesReader.getBackgroundCleanup());
    }

    /**
     * Verify contrast LEVEL_0 (lowest boundary) is in capabilities and build succeeds.
     */
    @Test
    public void GivenStandardProfile_WhenContrastLevel0Set_ThenBuildShouldSucceedAtLowerBoundary() throws Exception {
        ScanAttributesCaps scanAttributeCaps = getScanAttributesCaps();

        assertTrue("ContrastAdjustmentList should contain LEVEL_0",
                scanAttributeCaps.getContrastAdjustmentList().contains(ScanAttributes.ContrastAdjustment.LEVEL_0));

        ScanAttributes scanAttributes = new ScanAttributes.HttpBuilder(null)
                .setContrastAdjustment(ScanAttributes.ContrastAdjustment.LEVEL_0)
                .setFileName("test")
                .build(scanAttributeCaps);

        ScanAttributesReader scanAttributesReader = new ScanAttributesReader(scanAttributes);
        assertEquals("ContrastAdjustment should be LEVEL_0",
                ScanAttributes.ContrastAdjustment.LEVEL_0, scanAttributesReader.getContrastAdjustment());
    }

    /**
     * Verify contrast LEVEL_8 (upper boundary) is in capabilities and build succeeds.
     */
    @Test
    public void GivenStandardProfile_WhenContrastLevel8Set_ThenBuildShouldSucceedAtUpperBoundary() throws Exception {
        ScanAttributesCaps scanAttributeCaps = getScanAttributesCaps();

        assertTrue("ContrastAdjustmentList should contain LEVEL_8",
                scanAttributeCaps.getContrastAdjustmentList().contains(ScanAttributes.ContrastAdjustment.LEVEL_8));

        ScanAttributes scanAttributes = new ScanAttributes.HttpBuilder(null)
                .setContrastAdjustment(ScanAttributes.ContrastAdjustment.LEVEL_8)
                .setFileName("test")
                .build(scanAttributeCaps);

        ScanAttributesReader scanAttributesReader = new ScanAttributesReader(scanAttributes);
        assertEquals("ContrastAdjustment should be LEVEL_8",
                ScanAttributes.ContrastAdjustment.LEVEL_8, scanAttributesReader.getContrastAdjustment());
    }
}
