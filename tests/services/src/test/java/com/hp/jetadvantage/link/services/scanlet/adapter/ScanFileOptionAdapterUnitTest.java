package com.hp.jetadvantage.link.services.scanlet.adapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import android.os.Bundle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.ext.service.scanJob.Profile;
import com.hp.jetadvantage.link.api.scanner.FileOptionsAttributesCaps;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes;
import com.hp.jetadvantage.link.api.scanner.Scanlet;
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

@RunWith(MockitoJUnitRunner.class)
public class ScanFileOptionAdapterUnitTest {
    public String testPackageName = Constants.TEST_PACKAGE_NAME;
    @Mock
    private IDeviceScanJobService mockScanJobService;

    @Before
    public void setUp() {
        //setup tests
    }

    /**
     * TEST for ScanFileOptionAdapter.getFileOptions : happy case1 PdfA - valid scan profile
     */
    @Test
    public void GivenScanFileOptionAdapter_WhenGetFileOptionsCalled_WithValidProfile_WithPdfA_ThenValidJsonStringShouldBeReturned() throws Exception {
        //1. Define the mocked ScanJobService behavior : return test scan Profile
        ObjectMapper mapper = new ObjectMapper();
        String profileResponse = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "scanJob/GET_ext_scanJob_profile.json");
        when(mockScanJobService.getProfile(testPackageName)).thenReturn(mapper.readValue(profileResponse, Profile.class));

        //2. Setup necessary attributes for the test
        Bundle bundle = new Bundle();
        bundle.putInt(Scanlet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION_LEVEL.EIGHT);

        try {
            //3. Call the target device adapter method: getFileOptions
            String resultJsonStr = ScanFileOptionAdapter.getFileOptions(testPackageName, mockScanJobService, ScanAttributes.ColorMode.MONO,
                    ScanAttributes.DocumentFormat.PDF_A);
            FileOptionsAttributesCaps fileOptionsAttributesCaps = JsonParser.getInstance().fromJson(resultJsonStr, FileOptionsAttributesCaps.class);

            //4. Validate result ScanAttributes
            assertFalse(fileOptionsAttributesCaps.getOcrLanguageList().isEmpty());
            assertEquals(1, fileOptionsAttributesCaps.getOcrLanguageList().size());
            assertFalse(fileOptionsAttributesCaps.getPdfCompressionModeList().isEmpty());
            assertEquals(3, fileOptionsAttributesCaps.getPdfCompressionModeList().size());
            assertFalse(fileOptionsAttributesCaps.getTiffCompressionModeList().isEmpty());
            assertEquals(1, fileOptionsAttributesCaps.getTiffCompressionModeList().size());
            assertFalse(fileOptionsAttributesCaps.getXpsCompressionModeList().isEmpty());
            assertEquals(1, fileOptionsAttributesCaps.getXpsCompressionModeList().size());
            assertFalse(fileOptionsAttributesCaps.isPdfEncryptionPasswordSupported());
        } catch (Exception e) {
            fail("Unexpected exception=" + e);
        }
    }

    /**
     * TEST for ScanFileOptionAdapter.getFileOptions : happy case2 Pdf - valid scan profile
     */
    @Test
    public void GivenScanFileOptionAdapter_WhenGetFileOptionsCalled_WithValidProfile_WithPdf_ThenValidJsonStringShouldBeReturned() throws Exception {
        //1. Define the mocked ScanJobService behavior : return test scan Profile
        ObjectMapper mapper = new ObjectMapper();
        String profileResponse = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "scanJob/GET_ext_scanJob_profile.json");
        when(mockScanJobService.getProfile(testPackageName)).thenReturn(mapper.readValue(profileResponse, Profile.class));

        //2. Setup necessary attributes for the test

        try {
            //3. Call the target device adapter method: getFileOptions
            String resultJsonStr = ScanFileOptionAdapter.getFileOptions(testPackageName, mockScanJobService, ScanAttributes.ColorMode.COLOR,
                    ScanAttributes.DocumentFormat.PDF);
            FileOptionsAttributesCaps fileOptionsAttributesCaps = JsonParser.getInstance().fromJson(resultJsonStr, FileOptionsAttributesCaps.class);

            //4. Validate result ScanAttributes
            assertFalse(fileOptionsAttributesCaps.getOcrLanguageList().isEmpty());
            assertEquals(1, fileOptionsAttributesCaps.getOcrLanguageList().size());
            assertFalse(fileOptionsAttributesCaps.getPdfCompressionModeList().isEmpty());
            assertEquals(1, fileOptionsAttributesCaps.getPdfCompressionModeList().size());
            assertFalse(fileOptionsAttributesCaps.getTiffCompressionModeList().isEmpty());
            assertEquals(1, fileOptionsAttributesCaps.getTiffCompressionModeList().size());
            assertFalse(fileOptionsAttributesCaps.getXpsCompressionModeList().isEmpty());
            assertEquals(1, fileOptionsAttributesCaps.getXpsCompressionModeList().size());
            assertTrue(fileOptionsAttributesCaps.isPdfEncryptionPasswordSupported());
        } catch (Exception e) {
            fail("Unexpected exception=" + e);
        }
    }

    /**
     * TEST for ScanFileOptionAdapter.getFileOptions : happy case3 Color Tiff - valid scan profile
     */
    @Test
    public void GivenScanFileOptionAdapter_WhenGetFileOptionsCalled_WithValidProfile_WithColorTiff_ThenValidJsonStringShouldBeReturned() throws Exception {
        //1. Define the mocked ScanJobService behavior : return test scan Profile
        ObjectMapper mapper = new ObjectMapper();
        String profileResponse = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "scanJob/GET_ext_scanJob_profile.json");
        when(mockScanJobService.getProfile(testPackageName)).thenReturn(mapper.readValue(profileResponse, Profile.class));

        //2. Setup necessary attributes for the test
        Bundle bundle = new Bundle();
        bundle.putInt(Scanlet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION_LEVEL.EIGHT);

        try {
            //3. Call the target device adapter method: getFileOptions
            String resultJsonStr = ScanFileOptionAdapter.getFileOptions(testPackageName, mockScanJobService, ScanAttributes.ColorMode.COLOR,
                    ScanAttributes.DocumentFormat.TIFF);
            FileOptionsAttributesCaps fileOptionsAttributesCaps = JsonParser.getInstance().fromJson(resultJsonStr, FileOptionsAttributesCaps.class);

            //4. Validate result ScanAttributes
            assertFalse(fileOptionsAttributesCaps.getOcrLanguageList().isEmpty());
            assertEquals(1, fileOptionsAttributesCaps.getOcrLanguageList().size());
            assertFalse(fileOptionsAttributesCaps.getPdfCompressionModeList().isEmpty());
            assertEquals(1, fileOptionsAttributesCaps.getPdfCompressionModeList().size());
            assertFalse(fileOptionsAttributesCaps.getTiffCompressionModeList().isEmpty());
            assertEquals(3, fileOptionsAttributesCaps.getTiffCompressionModeList().size());
            assertFalse(fileOptionsAttributesCaps.getXpsCompressionModeList().isEmpty());
            assertEquals(1, fileOptionsAttributesCaps.getXpsCompressionModeList().size());
            assertFalse(fileOptionsAttributesCaps.isPdfEncryptionPasswordSupported());
        } catch (Exception e) {
            fail("Unexpected exception=" + e);
        }
    }

    /**
     * TEST for ScanFileOptionAdapter.getFileOptions : happy case1 Mono Tiff - valid scan profile
     */
    @Test
    public void GivenScanFileOptionAdapter_WhenGetFileOptionsCalled_WithValidProfile_WithMonoTiff_ThenValidJsonStringShouldBeReturned() throws Exception {
        //1. Define the mocked ScanJobService behavior : return test scan Profile
        ObjectMapper mapper = new ObjectMapper();
        String profileResponse = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "scanJob/GET_ext_scanJob_profile.json");
        when(mockScanJobService.getProfile(testPackageName)).thenReturn(mapper.readValue(profileResponse, Profile.class));

        //2. Setup necessary attributes for the test

        try {
            //3. Call the target device adapter method: getFileOptions
            String resultJsonStr = ScanFileOptionAdapter.getFileOptions(testPackageName, mockScanJobService, ScanAttributes.ColorMode.MONO,
                    ScanAttributes.DocumentFormat.TIFF);
            FileOptionsAttributesCaps fileOptionsAttributesCaps = JsonParser.getInstance().fromJson(resultJsonStr, FileOptionsAttributesCaps.class);

            //4. Validate result ScanAttributes
            assertFalse(fileOptionsAttributesCaps.getOcrLanguageList().isEmpty());
            assertEquals(1, fileOptionsAttributesCaps.getOcrLanguageList().size());
            assertFalse(fileOptionsAttributesCaps.getPdfCompressionModeList().isEmpty());
            assertEquals(1, fileOptionsAttributesCaps.getPdfCompressionModeList().size());
            assertFalse(fileOptionsAttributesCaps.getTiffCompressionModeList().isEmpty());
            assertEquals(3, fileOptionsAttributesCaps.getTiffCompressionModeList().size());
            assertFalse(fileOptionsAttributesCaps.getXpsCompressionModeList().isEmpty());
            assertEquals(1, fileOptionsAttributesCaps.getXpsCompressionModeList().size());
            assertFalse(fileOptionsAttributesCaps.isPdfEncryptionPasswordSupported());
        } catch (Exception e) {
            fail("Unexpected exception=" + e);
        }
    }
}
