/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.scanlet.adapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.ext.service.scanJob.DefaultOptions;
import com.hp.ext.service.scanJob.Profile;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes;
import com.hp.jetadvantage.link.api.scanner.ScanAttributesCaps;
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
 * Tests for multi-destination support (jobStorage, networkFolder, localFolder).
 * Verifies that capabilities are correctly generated per destination and that
 * jobStorage overrides from profile are properly merged with base definitions.
 *
 * Destination mapping:
 *   - HTTP → profile.http + defaultOptions.http
 *   - ME/USB/EMAIL → profile.jobStorage (merged with base) + defaultOptions.jobStorage
 *   - FTP/NETWORK_FOLDER → profile.networkFolder (merged with base) + defaultOptions.networkFolder
 *
 * Note: This class does NOT use mockStatic(Environment.class) to avoid
 * Byte Buddy / Java 21 incompatibility issues.
 */
@RunWith(MockitoJUnitRunner.class)
public class ScanDestinationValidationUnitTest {
    public String testPackageName = Constants.TEST_PACKAGE_NAME;

    @Mock
    private IDeviceScanJobService mockScanJobService;

    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
    }

    // ========================================================================================
    // Helper methods
    // ========================================================================================

    private void loadProfileAndDefaults(String profileFile, String defaultOptionsFile) throws Exception {
        String profileResponse = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()),
                "scanJob/" + profileFile);
        when(mockScanJobService.getProfile(testPackageName))
                .thenReturn(mapper.readValue(profileResponse, Profile.class));
        String defaultOptionsResponse = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()),
                "scanJob/" + defaultOptionsFile);
        defaultOptionsResponse = E2JsonTestHelper.simplifyE2Json(defaultOptionsResponse);
        when(mockScanJobService.getDefaultOptions(testPackageName))
                .thenReturn(mapper.readValue(defaultOptionsResponse, DefaultOptions.class));
    }

    private void loadStandardProfileAndDefaults() throws Exception {
        loadProfileAndDefaults("GET_ext_scanJob_profile.json", "GET_ext_scanJob_defaultOptions.json");
    }

    private void loadAllOptionsProfileAndDefaults() throws Exception {
        loadProfileAndDefaults("GET_ext_scanJob_profile_allOptions.json", "GET_ext_scanJob_DefaultOptions_allOptions.json");
    }

    private ScanAttributesCaps getCapabilitiesForDestination(ScanAttributes.Destination destination) throws Exception {
        String capabilitiesJson = ScanOptionProfileAdapter.getCapabilities(testPackageName, mockScanJobService,
                ScanAttributes.TransmissionMode.JOB, destination, Sdk.VERSION.LEVEL);
        return JsonParser.getInstance().fromJson(capabilitiesJson, ScanAttributesCaps.class);
    }

    // ========================================================================================
    // Part 1: Standard profile — existing HTTP destination vs ME/USB (jobStorage)
    // Standard profile has NO jobStorage section, so ME/USB uses base-only definitions.
    // ========================================================================================

    /**
     * HTTP destination with standard profile should produce correct capabilities.
     * This is the baseline for comparison.
     */
    @Test
    public void GivenStandardProfile_WhenHttpDestination_ThenCapabilitiesShouldMatchHttpProfile() throws Exception {
        loadStandardProfileAndDefaults();
        ScanAttributesCaps httpCaps = getCapabilitiesForDestination(ScanAttributes.Destination.HTTP);

        assertNotNull("HTTP capabilities should not be null", httpCaps);
        // Standard profile: scanCaptureMode isAvailable=false → only DEFAULT
        assertEquals("HTTP CaptureModeList should only contain DEFAULT",
                1, httpCaps.getCaptureModeList().size());
        // Standard profile: resolution has 6 values + DEFAULT = 7
        assertEquals("HTTP ResolutionList should have 7 entries",
                7, httpCaps.getResolutionList().size());
    }

    /**
     * ME destination with standard profile (no jobStorage section).
     * jobStorage merge with base: base definitions only → same as HTTP for most options.
     */
    @Test
    public void GivenStandardProfile_WhenMeDestination_ThenCapabilitiesShouldUseBaseDefinitions() throws Exception {
        loadStandardProfileAndDefaults();
        ScanAttributesCaps meCaps = getCapabilitiesForDestination(ScanAttributes.Destination.ME);

        assertNotNull("ME capabilities should not be null", meCaps);
        // No jobStorage overrides → uses base definitions
        // scanCaptureMode isAvailable=false in base → only DEFAULT
        assertEquals("ME CaptureModeList should only contain DEFAULT",
                1, meCaps.getCaptureModeList().size());
    }

    /**
     * USB destination with standard profile should behave same as ME.
     */
    @Test
    public void GivenStandardProfile_WhenUsbDestination_ThenCapabilitiesShouldMatchMeDestination() throws Exception {
        loadStandardProfileAndDefaults();
        ScanAttributesCaps usbCaps = getCapabilitiesForDestination(ScanAttributes.Destination.USB);
        ScanAttributesCaps meCaps = getCapabilitiesForDestination(ScanAttributes.Destination.ME);

        assertNotNull("USB capabilities should not be null", usbCaps);
        assertEquals("USB and ME should have same ResolutionList size",
                meCaps.getResolutionList().size(), usbCaps.getResolutionList().size());
        assertEquals("USB and ME should have same CaptureModeList size",
                meCaps.getCaptureModeList().size(), usbCaps.getCaptureModeList().size());
    }

    /**
     * EMAIL destination with standard profile should behave same as ME (both use jobStorage).
     */
    @Test
    public void GivenStandardProfile_WhenEmailDestination_ThenCapabilitiesShouldMatchMeDestination() throws Exception {
        loadStandardProfileAndDefaults();
        ScanAttributesCaps emailCaps = getCapabilitiesForDestination(ScanAttributes.Destination.EMAIL);
        ScanAttributesCaps meCaps = getCapabilitiesForDestination(ScanAttributes.Destination.ME);

        assertNotNull("EMAIL capabilities should not be null", emailCaps);
        assertEquals("EMAIL and ME should have same CaptureModeList size",
                meCaps.getCaptureModeList().size(), emailCaps.getCaptureModeList().size());
    }

    /**
     * NETWORK_FOLDER destination with standard profile (no networkFolder section in profile).
     * Uses base definitions only (networkFolder profile section is null).
     */
    @Test
    public void GivenStandardProfile_WhenNetworkFolderDestination_ThenCapabilitiesShouldUseBaseDefinitions() throws Exception {
        loadStandardProfileAndDefaults();
        ScanAttributesCaps nfCaps = getCapabilitiesForDestination(ScanAttributes.Destination.NETWORK_FOLDER);

        assertNotNull("NETWORK_FOLDER capabilities should not be null", nfCaps);
        // No networkFolder overrides → base definitions
        assertEquals("NETWORK_FOLDER CaptureModeList should only contain DEFAULT (base: isAvailable=false)",
                1, nfCaps.getCaptureModeList().size());
    }

    /**
     * FTP destination should behave same as NETWORK_FOLDER (both use networkFolder profile helper).
     */
    @Test
    public void GivenStandardProfile_WhenFtpDestination_ThenCapabilitiesShouldMatchNetworkFolder() throws Exception {
        loadStandardProfileAndDefaults();
        ScanAttributesCaps ftpCaps = getCapabilitiesForDestination(ScanAttributes.Destination.FTP);
        ScanAttributesCaps nfCaps = getCapabilitiesForDestination(ScanAttributes.Destination.NETWORK_FOLDER);

        assertNotNull("FTP capabilities should not be null", ftpCaps);
        assertEquals("FTP and NETWORK_FOLDER should have same ResolutionList size",
                nfCaps.getResolutionList().size(), ftpCaps.getResolutionList().size());
    }

    // ========================================================================================
    // Part 2: allOptions profile — all destinations with varied capability sets
    // allOptions profile has jobStorage section that overrides some base definitions.
    // ========================================================================================

    /**
     * allOptions profile: HTTP destination should have all options available.
     */
    @Test
    public void GivenAllOptionsProfile_WhenHttpDestination_ThenAllOptionsShouldBeAvailable() throws Exception {
        loadAllOptionsProfileAndDefaults();
        ScanAttributesCaps httpCaps = getCapabilitiesForDestination(ScanAttributes.Destination.HTTP);

        assertNotNull("HTTP capabilities should not be null", httpCaps);
        // allOptions: all options are available for HTTP (base + http merged)
        assertTrue("HTTP CaptureModeList should have > 1 entry",
                httpCaps.getCaptureModeList().size() > 1);
        assertTrue("HTTP ScanPreviewList should have > 1 entry",
                httpCaps.getScanPreviewList().size() > 1);
        assertTrue("HTTP CropModeList should have > 1 entry",
                httpCaps.getCropModeList().size() > 1);
        assertTrue("HTTP MediaSourceList should contain AUTO",
                httpCaps.getMediaSourceList().contains(ScanAttributes.MediaSource.AUTO));
    }

    /**
     * allOptions profile: ME destination should use jobStorage overrides.
     * jobStorage overrides: resolution has only [dpi150, dpi200, dpi300],
     * outputFileFormat isAvailable=false.
     */
    @Test
    public void GivenAllOptionsProfile_WhenMeDestination_ThenJobStorageOverridesShouldApply() throws Exception {
        loadAllOptionsProfileAndDefaults();
        ScanAttributesCaps meCaps = getCapabilitiesForDestination(ScanAttributes.Destination.ME);

        assertNotNull("ME capabilities should not be null", meCaps);

        // jobStorage overrides resolution with [dpi150, dpi200, dpi300] → 3 + DEFAULT = 4
        assertEquals("ME ResolutionList should have 4 entries (3 + DEFAULT)",
                4, meCaps.getResolutionList().size());
        assertTrue("ME should contain DPI_150",
                meCaps.getResolutionList().contains(ScanAttributes.Resolution.DPI_150));
        assertTrue("ME should contain DPI_200",
                meCaps.getResolutionList().contains(ScanAttributes.Resolution.DPI_200));
        assertTrue("ME should contain DPI_300",
                meCaps.getResolutionList().contains(ScanAttributes.Resolution.DPI_300));
        assertFalse("ME should NOT contain DPI_600 (not in jobStorage override)",
                meCaps.getResolutionList().contains(ScanAttributes.Resolution.DPI_600));

        // jobStorage overrides outputFileFormat with isAvailable=false → only DEFAULT
        assertEquals("ME DocumentFormatList should only contain DEFAULT",
                1, meCaps.getDocumentFormatList(ScanAttributes.Destination.ME).size());
    }

    /**
     * allOptions profile: ME destination — options NOT overridden by jobStorage
     * should inherit base values (e.g., CaptureMode, CropMode, MediaSource).
     */
    @Test
    public void GivenAllOptionsProfile_WhenMeDestination_ThenNonOverriddenOptionsShouldInheritFromBase() throws Exception {
        loadAllOptionsProfileAndDefaults();
        ScanAttributesCaps meCaps = getCapabilitiesForDestination(ScanAttributes.Destination.ME);
        ScanAttributesCaps httpCaps = getCapabilitiesForDestination(ScanAttributes.Destination.HTTP);

        // CaptureMode: not overridden by jobStorage → inherits from base (isAvailable=true)
        assertTrue("ME CaptureModeList should have > 1 entry (inherited from base)",
                meCaps.getCaptureModeList().size() > 1);
        assertTrue("ME should contain STANDARD",
                meCaps.getCaptureModeList().contains(ScanAttributes.CaptureMode.STANDARD));

        // CropMode: not overridden by jobStorage → inherits from base
        assertTrue("ME CropModeList should have > 1 entry",
                meCaps.getCropModeList().size() > 1);

        // MediaSource: not overridden → inherits base (miAuto available)
        assertTrue("ME MediaSourceList should contain AUTO",
                meCaps.getMediaSourceList().contains(ScanAttributes.MediaSource.AUTO));

        // ENUM_RANGE options should still work (inherited from base)
        assertEquals("ME ContrastAdjustmentList should have 10 entries",
                10, meCaps.getContrastAdjustmentList().size());
    }

    /**
     * allOptions profile: Compare HTTP vs ME to verify jobStorage differences.
     */
    @Test
    public void GivenAllOptionsProfile_WhenComparingHttpAndMe_ThenJobStorageOverridedOptionsShouldDiffer() throws Exception {
        loadAllOptionsProfileAndDefaults();
        ScanAttributesCaps httpCaps = getCapabilitiesForDestination(ScanAttributes.Destination.HTTP);
        ScanAttributesCaps meCaps = getCapabilitiesForDestination(ScanAttributes.Destination.ME);

        // HTTP resolution: all 9 values + DEFAULT = 10
        assertTrue("HTTP ResolutionList should be larger than ME",
                httpCaps.getResolutionList().size() > meCaps.getResolutionList().size());

        // HTTP outputFileFormat: isAvailable=true → multiple formats
        assertTrue("HTTP DocumentFormatList should have > 1 entry",
                httpCaps.getDocumentFormatList(ScanAttributes.Destination.HTTP).size() > 1);
        // ME outputFileFormat: isAvailable=false → only DEFAULT
        assertEquals("ME DocumentFormatList should have only DEFAULT",
                1, meCaps.getDocumentFormatList(ScanAttributes.Destination.ME).size());
    }

    /**
     * allOptions profile: NETWORK_FOLDER destination.
     * No networkFolder section in profile → uses base definitions only.
     * Base has all options available → same as ME (except resolution is base, not jobStorage).
     */
    @Test
    public void GivenAllOptionsProfile_WhenNetworkFolderDestination_ThenCapabilitiesShouldUseBaseDefinitions() throws Exception {
        loadAllOptionsProfileAndDefaults();
        ScanAttributesCaps nfCaps = getCapabilitiesForDestination(ScanAttributes.Destination.NETWORK_FOLDER);

        assertNotNull("NETWORK_FOLDER capabilities should not be null", nfCaps);
        // allOptions base: resolution has 9 values + DEFAULT = 10 (no NF override)
        assertEquals("NETWORK_FOLDER ResolutionList should have all resolutions from base (10)",
                10, nfCaps.getResolutionList().size());
        // allOptions base: outputFileFormat isAvailable=true → multiple formats
        assertTrue("NETWORK_FOLDER DocumentFormatList should have > 1 entry",
                nfCaps.getDocumentFormatList(ScanAttributes.Destination.NETWORK_FOLDER).size() > 1);
    }

    /**
     * allOptions profile: FTP destination should behave same as NETWORK_FOLDER.
     */
    @Test
    public void GivenAllOptionsProfile_WhenFtpDestination_ThenCapabilitiesShouldMatchNetworkFolder() throws Exception {
        loadAllOptionsProfileAndDefaults();
        ScanAttributesCaps ftpCaps = getCapabilitiesForDestination(ScanAttributes.Destination.FTP);
        ScanAttributesCaps nfCaps = getCapabilitiesForDestination(ScanAttributes.Destination.NETWORK_FOLDER);

        assertEquals("FTP and NETWORK_FOLDER ResolutionList should match",
                nfCaps.getResolutionList().size(), ftpCaps.getResolutionList().size());
        assertEquals("FTP and NETWORK_FOLDER CaptureModeList should match",
                nfCaps.getCaptureModeList().size(), ftpCaps.getCaptureModeList().size());
    }

    /**
     * allOptions profile: jobStorage fileTransmissionMode only has [tmJob].
     * Verify this restricts TransmissionMode for ME destination.
     */
    @Test
    public void GivenAllOptionsProfile_WhenMeDestination_ThenTransmissionModeShouldBeRestrictedByJobStorage() throws Exception {
        loadAllOptionsProfileAndDefaults();
        ScanAttributesCaps meCaps = getCapabilitiesForDestination(ScanAttributes.Destination.ME);

        // jobStorage overrides fileTransmissionMode: only [tmJob] → 1 + DEFAULT = 2
        assertEquals("ME TransmissionModeList should have 2 entries (tmJob + DEFAULT)",
                2, meCaps.getTransmissionModeList().size());
        assertTrue("ME should contain JOB",
                meCaps.getTransmissionModeList().contains(ScanAttributes.TransmissionMode.JOB));
        assertFalse("ME should NOT contain IMAGE (not in jobStorage override)",
                meCaps.getTransmissionModeList().contains(ScanAttributes.TransmissionMode.IMAGE));
    }

    // ========================================================================================
    // Part 4: Cross-destination capability comparison
    // ========================================================================================

    /**
     * Verify all 6 destinations produce non-null, valid capabilities.
     */
    @Test
    public void GivenAllOptionsProfile_WhenAllDestinationsQueried_ThenCapabilitiesShouldBeNonNull() throws Exception {
        loadAllOptionsProfileAndDefaults();

        ScanAttributes.Destination[] allDestinations = {
                ScanAttributes.Destination.HTTP,
                ScanAttributes.Destination.ME,
                ScanAttributes.Destination.USB,
                ScanAttributes.Destination.EMAIL,
                ScanAttributes.Destination.FTP,
                ScanAttributes.Destination.NETWORK_FOLDER
        };

        for (ScanAttributes.Destination dest : allDestinations) {
            ScanAttributesCaps caps = getCapabilitiesForDestination(dest);
            assertNotNull(dest.name() + " capabilities should not be null", caps);
            assertTrue(dest.name() + " should have at least DEFAULT in ResolutionList",
                    caps.getResolutionList().size() >= 1);
            assertTrue(dest.name() + " should have at least DEFAULT in ColorModeList",
                    caps.getColorModeList().size() >= 1);
        }
    }

    /**
     * ME, USB, and EMAIL should all produce identical capabilities
     * since they all use jobStorage profile helper.
     */
    @Test
    public void GivenAllOptionsProfile_WhenMeUsbEmailCompared_ThenCapabilitiesShouldBeIdentical() throws Exception {
        loadAllOptionsProfileAndDefaults();
        ScanAttributesCaps meCaps = getCapabilitiesForDestination(ScanAttributes.Destination.ME);
        ScanAttributesCaps usbCaps = getCapabilitiesForDestination(ScanAttributes.Destination.USB);
        ScanAttributesCaps emailCaps = getCapabilitiesForDestination(ScanAttributes.Destination.EMAIL);

        // All three use jobStorage profile helper → should have same capability lists
        assertEquals("ME and USB ResolutionList size should match",
                meCaps.getResolutionList().size(), usbCaps.getResolutionList().size());
        assertEquals("ME and EMAIL ResolutionList size should match",
                meCaps.getResolutionList().size(), emailCaps.getResolutionList().size());
        assertEquals("ME and USB CaptureModeList size should match",
                meCaps.getCaptureModeList().size(), usbCaps.getCaptureModeList().size());
        assertEquals("ME and EMAIL CaptureModeList size should match",
                meCaps.getCaptureModeList().size(), emailCaps.getCaptureModeList().size());
        assertEquals("ME and USB ContrastAdjustmentList size should match",
                meCaps.getContrastAdjustmentList().size(), usbCaps.getContrastAdjustmentList().size());
    }

    /**
     * FTP and NETWORK_FOLDER should produce identical capabilities
     * since they both use networkFolder profile helper.
     */
    @Test
    public void GivenAllOptionsProfile_WhenFtpAndNetworkFolderCompared_ThenCapabilitiesShouldBeIdentical() throws Exception {
        loadAllOptionsProfileAndDefaults();
        ScanAttributesCaps ftpCaps = getCapabilitiesForDestination(ScanAttributes.Destination.FTP);
        ScanAttributesCaps nfCaps = getCapabilitiesForDestination(ScanAttributes.Destination.NETWORK_FOLDER);

        assertEquals("FTP and NF ColorModeList size should match",
                nfCaps.getColorModeList().size(), ftpCaps.getColorModeList().size());
        assertEquals("FTP and NF ContrastAdjustmentList size should match",
                nfCaps.getContrastAdjustmentList().size(), ftpCaps.getContrastAdjustmentList().size());
        assertEquals("FTP and NF CropModeList size should match",
                nfCaps.getCropModeList().size(), ftpCaps.getCropModeList().size());
    }
}
