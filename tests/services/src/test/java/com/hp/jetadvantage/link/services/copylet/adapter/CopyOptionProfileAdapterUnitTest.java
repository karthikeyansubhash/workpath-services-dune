package com.hp.jetadvantage.link.services.copylet.adapter;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import android.os.Bundle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.ext.service.copy.Profile;
import com.hp.ext.types.optionProfile.OptionProfile;
import com.hp.jetadvantage.link.api.copier.CopyAttributes;
import com.hp.jetadvantage.link.api.copier.CopyAttributesCaps;
import com.hp.jetadvantage.link.api.copier.Copylet;
import com.hp.jetadvantage.link.api.copier.Range;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.utils.JsonParser;
import com.hp.jetadvantage.link.device.services.exceptions.BoundDeviceException;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceCopyJobService;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.utils.Utils;
import com.hp.jetadvantage.link.services.common.exception.SdkConnectionErrorException;
import com.hp.jetadvantage.link.services.common.exception.SdkServiceErrorException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RunWith(MockitoJUnitRunner.class)
public class CopyOptionProfileAdapterUnitTest {
    public String testPackageName = Constants.TEST_PACKAGE_NAME;
    @Mock
    private IDeviceCopyJobService mockCopyJobService;

    @Before
    public void setUp() {

    }

    /**
     * TEST for CopyDeviceAdapter.getCaps : Error case 1 - BoundDeviceException
     */
    @Test
    public void GivenCopyDeviceAdapter_WhenGetCapsCalled_AndBoundDeviceExceptionOccurs_ThenConnectionErrorShouldBeReturned() {

        //1. Define the mocked JobService behavior : throw BoundDeviceException
        when(mockCopyJobService.getProfile(testPackageName)).thenThrow(new BoundDeviceException());

        //2. Setup necessary attributes for the test

        //3. Call the target device adapter method and validate result
        try {
            CopyOptionProfileAdapter.getCaps(testPackageName, mockCopyJobService, Sdk.VERSION_LEVEL.EIGHT);
            fail("Expected exception does not occur");
        } catch (SdkConnectionErrorException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Expected exception does not occur, e=" + e);
        }
    }

    /**
     * TEST for CopyDeviceAdapter.getCaps : Error case 2 - null copy profile
     */
    @Test
    public void GivenCopyDeviceAdapter_WhenGetCapsCalled_NullProfileRetrievedFromDevice_ThenServiceErrorShouldBeReturned() {

        //1. Define the mocked JobService behavior : return null profile
        when(mockCopyJobService.getProfile(testPackageName)).thenReturn(null);

        //2. Setup necessary attributes for the test

        //3. Call the target device adapter method and validate result
        try {
            CopyOptionProfileAdapter.getCaps(testPackageName, mockCopyJobService, Sdk.VERSION_LEVEL.EIGHT);
            fail("Expected exception does not occur");
        } catch (SdkServiceErrorException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Expected exception does not occur, e=" + e);
        }
    }

    /**
     * TEST for CopyDeviceAdapter.getCaps : happy case - valid copy profile
     */
    @Test
    public void GivenCopyDeviceAdapter_WhenGetCapsCalled_ValidProfileRetrievedFromDevice_ThenValidStringShouldBeReturned() throws Exception {

        String sampleProfile = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()),
                "copyJob" + "/GET_ext_copy_profile.json");

        //1. Define mocked JobService to return a valid test copy Profile
        ObjectMapper mapper = new ObjectMapper();
        Profile copyProfile = mapper.readValue(sampleProfile, Profile.class);
        when(mockCopyJobService.getProfile(testPackageName)).thenReturn(copyProfile);

        //2. Setup necessary attributes for the test

        //3. Call the target device adapter method and validate result
        try {
            String resultJsonStr = CopyOptionProfileAdapter.getCaps(testPackageName, mockCopyJobService,
                    Sdk.VERSION_LEVEL.EIGHT);
            CopyAttributesCaps copyAttributeCaps = JsonParser.getInstance().fromJson(resultJsonStr,
                    CopyAttributesCaps.class);

            //validate result copyAttributesCaps
            validateColorMode(copyAttributeCaps);
            validateScanSize(copyAttributeCaps);
            validateCopyPreview(copyAttributeCaps);
            validateScanSource(copyAttributeCaps);
            validateCopiesRange(copyAttributeCaps);

            //TODO : add validation when each capability option is implemented. below is just default cap list
            assertFalse(copyAttributeCaps.getBackgroundCleanupList().isEmpty());
            assertFalse(copyAttributeCaps.getContrastAdjustmentList().isEmpty());
            assertFalse(copyAttributeCaps.getDarknessAdjustmentList().isEmpty());
            assertFalse(copyAttributeCaps.getProgressDialogModeList().isEmpty());
            assertFalse(copyAttributeCaps.getJobAssemblyModeList().isEmpty());
            assertFalse(copyAttributeCaps.getSharpnessAdjustmentList().isEmpty());
            assertFalse(copyAttributeCaps.getEraseMarginUnitList().isEmpty());

        } catch (Exception e) {
            fail("Unexpected exception=" + e);
        }
    }

    private void validateColorMode(CopyAttributesCaps copyAttributeCaps) {
        List<CopyAttributes.ColorMode> expectedColorModes = Arrays.asList(CopyAttributes.ColorMode.DEFAULT,
                CopyAttributes.ColorMode.AUTO, CopyAttributes.ColorMode.COLOR, CopyAttributes.ColorMode.GRAY);
        assertFalse("ColorModeList empty", copyAttributeCaps.getColorModeList().isEmpty());
        assertEquals("ColorModeList size", 4, copyAttributeCaps.getColorModeList().size());
        assertTrue("ColorModeList elements", copyAttributeCaps.getColorModeList().containsAll(expectedColorModes));
    }

    private void validateCopiesRange(CopyAttributesCaps copyAttributeCaps) {
        assertNotNull("CopiesRange null", copyAttributeCaps.getCopiesRange());
        assertEquals("CopiesRange - LowerBound", 1, copyAttributeCaps.getCopiesRange().getLowerBound());
        assertEquals("CopiesRange - UpperBound", 9999, copyAttributeCaps.getCopiesRange().getUpperBound());
    }

    private void validateCopyPreview(CopyAttributesCaps copyAttributeCaps) {
        // Validate options: if "isAvailable" property is false from E2, then the capability should not be in the
        // list and only
        // DEFAULT value should be set
        List<CopyAttributes.CopyPreview> expectedCopyPreview = Arrays.asList(CopyAttributes.CopyPreview.DEFAULT);
        assertFalse("CopyPreviewList empty", copyAttributeCaps.getCopyPreviewList().isEmpty());
        assertEquals("CopyPreviewList size", 1, copyAttributeCaps.getCopyPreviewList().size());
        assertTrue("CopyPreviewList elements", copyAttributeCaps.getCopyPreviewList().containsAll(expectedCopyPreview));
    }

    private void validateScanSize(CopyAttributesCaps copyAttributeCaps) {
        List<CopyAttributes.ScanSize> expectedScanSizes = Arrays.asList(CopyAttributes.ScanSize.DEFAULT,
                CopyAttributes.ScanSize.AUTO, CopyAttributes.ScanSize.LETTER, CopyAttributes.ScanSize.LETTER_ROTATE,
                CopyAttributes.ScanSize.LEGAL, CopyAttributes.ScanSize.EXECUTIVE, CopyAttributes.ScanSize.STATEMENT,
                CopyAttributes.ScanSize.LEDGER, CopyAttributes.ScanSize.GENERAL_5X7in,
                CopyAttributes.ScanSize.GENERAL_5X8in, CopyAttributes.ScanSize.INCH8POINT5X13,
                CopyAttributes.ScanSize.OFICIO, CopyAttributes.ScanSize.A3, CopyAttributes.ScanSize.A4,
                CopyAttributes.ScanSize.A4_ROTATE, CopyAttributes.ScanSize.A5, CopyAttributes.ScanSize.A5_ROTATE,
                CopyAttributes.ScanSize.A6, CopyAttributes.ScanSize.RA4, CopyAttributes.ScanSize.JDOUBLE_POSTCARD,
                CopyAttributes.ScanSize.JB4, CopyAttributes.ScanSize.JB5, CopyAttributes.ScanSize.JB5_ROTATE,
                CopyAttributes.ScanSize.JB6, CopyAttributes.ScanSize.K16, CopyAttributes.ScanSize.K16_184X260mm,
                CopyAttributes.ScanSize.PK16);
        assertFalse("ScanSizeList empty", copyAttributeCaps.getScanSizeList().isEmpty());
        assertEquals("ScanSizeList size", 33, copyAttributeCaps.getScanSizeList().size());
        assertTrue("ScanSizeList elements", copyAttributeCaps.getScanSizeList().containsAll(expectedScanSizes));
    }

    /**
     * TEST for CopyOptionProfileAdapter.getCaps : STORE job execution mode included
     */
    @Test
    public void GivenCopyAttributeCaps_WhenBuilt_ThenStoreJobExecutionModeIncluded() throws Exception {
        String sampleProfile = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()),
                "copyJob" + "/GET_ext_copy_profile.json");

        ObjectMapper mapper = new ObjectMapper();
        Profile copyProfile = mapper.readValue(sampleProfile, Profile.class);
        when(mockCopyJobService.getProfile(testPackageName)).thenReturn(copyProfile);

        String resultJsonStr = CopyOptionProfileAdapter.getCaps(testPackageName, mockCopyJobService,
                Sdk.VERSION_LEVEL.EIGHT);
        CopyAttributesCaps copyAttributeCaps = JsonParser.getInstance().fromJson(resultJsonStr,
                CopyAttributesCaps.class);

        assertNotNull("JobExecutionModeList null", copyAttributeCaps.getJobExecutionModeList());
        assertTrue("JobExecutionModeList should contain STORE",
                copyAttributeCaps.getJobExecutionModeList().contains(CopyAttributes.JobExecutionMode.STORE));
        assertTrue("JobExecutionModeList should contain NORMAL",
                copyAttributeCaps.getJobExecutionModeList().contains(CopyAttributes.JobExecutionMode.NORMAL));
    }

    /**
     * TEST: ScalePercentRange must exist for every scan source reported by the device profile.
     * Without this, apps crash with NPE when selecting FLATBED or AUTO.
     */
    @Test
    public void GivenCopyAttributeCaps_WhenBuilt_ThenScalePercentRangeExistsForAllScanSources() throws Exception {
        String sampleProfile = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()),
                "copyJob" + "/GET_ext_copy_profile.json");

        ObjectMapper mapper = new ObjectMapper();
        Profile copyProfile = mapper.readValue(sampleProfile, Profile.class);
        when(mockCopyJobService.getProfile(testPackageName)).thenReturn(copyProfile);

        String resultJsonStr = CopyOptionProfileAdapter.getCaps(testPackageName, mockCopyJobService,
                Sdk.VERSION_LEVEL.EIGHT);
        CopyAttributesCaps caps = JsonParser.getInstance().fromJson(resultJsonStr, CopyAttributesCaps.class);

        Map<CopyAttributes.ScanSource, Range> rangeMap = caps.getScalePercentRangeByScanSource();
        for (CopyAttributes.ScanSource source : caps.getScanSourceList()) {
            assertNotNull("ScalePercentRange missing for ScanSource=" + source, rangeMap.get(source));
        }
        // Verify specific sources from the test profile
        assertNotNull("ADF range", rangeMap.get(CopyAttributes.ScanSource.ADF));
        assertNotNull("FLATBED range", rangeMap.get(CopyAttributes.ScanSource.FLATBED));
        assertNotNull("AUTO range", rangeMap.get(CopyAttributes.ScanSource.AUTO));
        assertNotNull("DEFAULT range", rangeMap.get(CopyAttributes.ScanSource.DEFAULT));
    }

    private void validateScanSource(CopyAttributesCaps copyAttributeCaps) {
        List<CopyAttributes.ScanSource> expectedScanSources = Arrays.asList(CopyAttributes.ScanSource.DEFAULT,
                CopyAttributes.ScanSource.FLATBED, CopyAttributes.ScanSource.ADF, CopyAttributes.ScanSource.AUTO);
        assertFalse("ScanSourceList empty", copyAttributeCaps.getScanSourceList().isEmpty());
        assertEquals("ScanSourceList size", expectedScanSources.size(), copyAttributeCaps.getScanSourceList().size());
        assertTrue("ScanSourceList elements", copyAttributeCaps.getScanSourceList().containsAll(expectedScanSources));
    }
}
