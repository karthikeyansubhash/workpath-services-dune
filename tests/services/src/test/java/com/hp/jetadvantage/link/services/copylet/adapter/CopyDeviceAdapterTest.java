/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.copylet.adapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import android.os.Bundle;
import android.os.Environment;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.ext.service.copy.DefaultOptions;
import com.hp.ext.service.copy.Profile;
import com.hp.ext.types.optionProfile.OptionProfile;
import com.hp.jetadvantage.link.api.copier.CopyAttributes;
import com.hp.jetadvantage.link.api.copier.CopyAttributesReader;
import com.hp.jetadvantage.link.api.copier.Copylet;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.model.ApiType;
import com.hp.jetadvantage.link.common.model.PrinterInfo;
import com.hp.jetadvantage.link.common.model.PrinterState;
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
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Objects;

@RunWith(MockitoJUnitRunner.class)
public class CopyDeviceAdapterTest {
    public String testPackageName = Constants.TEST_PACKAGE_NAME;
    @Mock
    private IDeviceCopyJobService mockCopyJobService;

    @Before
    public void setUp() {
        //setup tests
    }


    /**
     * TEST for CopyDeviceAdapter.getDefaults : Error case 1 - BoundDeviceException
     */
    @Test
    public void GivenCopyDeviceAdapter_WhenGetDefaultsCalled_AndBoundDeviceExceptionOccurs_ThenConnectionErrorShouldBeReturned() {

        //1. Define the mocked JobService behavior : throw BoundDeviceException
        when(mockCopyJobService.getProfile(testPackageName)).thenThrow(new BoundDeviceException());

        //2. Setup necessary attributes for the test
        Bundle bundle = new Bundle();
        bundle.putInt(Copylet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION_LEVEL.EIGHT);

        //3. Call the target device adapter method and validate result
        try {
            CopyDeviceAdapter.getDefaults(testPackageName, mockCopyJobService, Sdk.VERSION_LEVEL.EIGHT);
            fail("Expected exception does not occur");
        } catch (SdkConnectionErrorException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Expected exception does not occur, e=" + e);
        }
    }

    /**
     * TEST for CopyDeviceAdapter.getDefaults : Error case 2 - null copy profile
     */
    @Test
    public void GivenCopyDeviceAdapter_WhenGetDefaultsCalled_NullProfileRetrievedFromDevice_ThenServiceErrorShouldBeReturned() {

        //1. Define the mocked JobService behavior : return null profile
        when(mockCopyJobService.getProfile(testPackageName)).thenReturn(null);

        //2. Setup necessary attributes for the test

        //3. Call the target device adapter method and validate result
        try {
            CopyDeviceAdapter.getDefaults(testPackageName, mockCopyJobService, Sdk.VERSION_LEVEL.EIGHT);
            fail("Expected exception does not occur");
        } catch (SdkServiceErrorException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Expected exception does not occur, e=" + e);
        }
    }

    /**
     * TEST for CopyDeviceAdapter.getDefaults : happy case - valid copy profile and default options
     */
    @Test
    public void GivenCopyDeviceAdapter_WhenGetDefaultsCalled_WithValidProfile_ThenValidJsonStringShouldBeReturned() throws Exception {
        String sampleProfile = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()),
                "copyJob" + "/GET_ext_copy_profile.json");
        String sampleDefaults = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()),
                "copyJob" + "/GET_ext_copy_defaultOptions2.json");
        // 1. Define the mocked JobService behavior : return test copy Profile
        ObjectMapper mapper = new ObjectMapper();
        Profile copyProfile = mapper.readValue(sampleProfile, Profile.class);
        when(mockCopyJobService.getProfile(testPackageName)).thenReturn(copyProfile);

        DefaultOptions defaultOptions = mapper.readValue(sampleDefaults, DefaultOptions.class);
        when(mockCopyJobService.getDefaultOptions(testPackageName)).thenReturn(defaultOptions);

        //2. Setup necessary attributes for the test
        Bundle bundle = new Bundle();
        bundle.putInt(Copylet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION_LEVEL.EIGHT);

        try {
            try (MockedStatic<Environment> mocked = mockStatic(Environment.class)) {

                //3. Call the target device adapter method: getDefaults
                String resultJsonStr = CopyDeviceAdapter.getDefaults(testPackageName, mockCopyJobService,
                        Sdk.VERSION_LEVEL.EIGHT);
                CopyAttributes copyAttributes = JsonParser.getInstance().fromJson(resultJsonStr, CopyAttributes.class);

                //4. Validate result CopyAttributes
                //      if the "isAvailable" property is false in the profile from E2, then the default value should be
                //      returned without error

                CopyAttributesReader reader = new CopyAttributesReader(copyAttributes);
                assertEquals("Copies", 1, reader.getCopies());
                assertEquals(CopyAttributes.ColorMode.COLOR, reader.getColorMode());
                assertEquals(CopyAttributes.ScanSize.LETTER, reader.getScanSize());
                assertEquals(CopyAttributes.ScanSource.ADF, reader.getScanSource());
                assertEquals(CopyAttributes.Duplex.DEFAULT, reader.getScanDuplex());
                assertEquals(CopyAttributes.CollateMode.COLLATED, reader.getCollateMode());
                assertEquals(CopyAttributes.Orientation.DEFAULT, reader.getOrientation());
                assertEquals(CopyAttributes.TextGraphicsOptimization.TEXT, reader.getTextGraphicsOptimization());
                assertEquals(CopyAttributes.CopyPreview.DEFAULT, reader.getCopyPreview());
                assertEquals(CopyAttributes.Duplex.NONE, reader.getPrintDuplex());
                assertEquals(CopyAttributes.OutputBin.DEFAULT, reader.getOutputBin());
                assertEquals(CopyAttributes.PaperSize.LETTER, reader.getPrintSize());
                assertEquals(CopyAttributes.PaperSource.AUTO, reader.getPaperSource());
                assertEquals(CopyAttributes.PaperType.DEFAULT, reader.getPaperType());
                assertEquals(CopyAttributes.NumberUpMode.DEFAULT, reader.getNumberUpMode());
                assertEquals(CopyAttributes.ScaleMode.AUTO, reader.getScaleMode());
                assertEquals(CopyAttributes.CaptureMode.DEFAULT, reader.getCaptureMode());
                assertEquals(CopyAttributes.ProgressDialogMode.OFF, reader.getProgressDialogMode());

            }
        } catch (Exception e) {
            fail("Unexpected exception=" + e);
        }
    }

    /**
     * TEST for CopyDeviceAdapter.getDefaults :
     * - valid copy profile  : which contains '"isAvailable": false' properties
     * if a property is '"isAvailable": false' in profile, then the default value should be returned without error
     *
     * @throws Exception
     */
    @Test
    public void GivenCopyDeviceAdapter_WhenGetDefaultsCalled_WithProfileContainsUnAvailableProperties_ThenValidJsonStringShouldBeReturned() throws Exception {

        String sampleProfile = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()),
                "copyJob" + "/GET_ext_copy_profile.json");
        String sampleDefaultOptions = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()),
                "copyJob" + "/GET_ext_copy_defaultOptions.json");

        // 1. Define the mocked JobService behavior : return test copy Profile
        ObjectMapper mapper = new ObjectMapper();
        Profile copyProfile = mapper.readValue(sampleProfile, Profile.class);
        when(mockCopyJobService.getProfile(testPackageName)).thenReturn(copyProfile);

        DefaultOptions defaultOptions = mapper.readValue(sampleDefaultOptions, DefaultOptions.class);
        when(mockCopyJobService.getDefaultOptions(testPackageName)).thenReturn(defaultOptions);

        //2. Setup necessary attributes for the test

        try {
            try (MockedStatic<Environment> mocked = mockStatic(Environment.class)) {

                //3. Call the target device adapter method: getDefaults
                String resultJsonStr = CopyDeviceAdapter.getDefaults(testPackageName, mockCopyJobService,
                        Sdk.VERSION_LEVEL.EIGHT);
                CopyAttributes copyAttributes = JsonParser.getInstance().fromJson(resultJsonStr, CopyAttributes.class);

                //4. Validate result CopyAttributes
                CopyAttributesReader reader = new CopyAttributesReader(copyAttributes);

                assertEquals(CopyAttributes.OutputBin.DEFAULT, reader.getOutputBin());
                assertEquals(CopyAttributes.PaperSize.DEFAULT, reader.getPrintSize());
                assertEquals(CopyAttributes.PaperType.DEFAULT, reader.getPaperType());
                assertEquals(CopyAttributes.NumberUpMode.DEFAULT, reader.getNumberUpMode());

            }
        } catch (Exception e) {
            fail("Unexpected exception=" + e);
        }
    }

    private PrinterInfo getTestPrinterInfo() {
        return new PrinterInfo.Builder()
                .api(ApiType.OXP)
                .ip("localhost")
                .name("LOCAL OXPd")
                //.baseUri(Uri.parse(OXPdDevice.Constants.SCHEME_HTTP + "://" + OXPdDevice.Constants
                // .HOSTNAME_INTERNAL + ":" +
                // OXPdDevice.Constants.OXPD_PORT_INTERNAL + "/"))
                .printerState(PrinterState.CONNECTED)
                .build();
    }

    private String getTestDefaultOptions() {
        return "{\n" +
                "  \"$opMeta\": {\n" +
                "    \"contentFilter\": [\n" +
                "      \"*\"\n" +
                "    ]\n" +
                "  },\n" +
                "  \"autoColorDetect\": \"acdmDetectOnly\",\n" +
                "  \"autoDeskew\": true,\n" +
                "  \"autoExposure\": true,\n" +
                "  \"backgroundCleanup\": 0,\n" +
                "  \"backgroundColorRemoval\": true,\n" +
                "  \"backgroundColorRemovalLevel\": 0,\n" +
                "  \"backgroundNoiseRemoval\": true,\n" +
                "  \"binaryRendering\": \"brHalftone\",\n" +
                "  \"blackBackground\": true,\n" +
                "  \"blackEnhancementLevel\": 0,\n" +
                "  \"blankPageSuppression\": \"bpdDisable\",\n" +
                "  \"ccdChannel\": \"cctNtsc\",\n" +
                "  \"collationType\": \"sctCollated\",\n" +
                "  \"colorMode\": \"cmColor\",\n" +
                "  \"colorRange\": 0,\n" +
                "  \"colorSensitivity\": 0,\n" +
                "  \"compressionFactor\": 0,\n" +
                "  \"contentOrientation\": \"coAutoDetect\",\n" +
                "  \"contentType\": \"dctText\",\n" +
                "  \"contrast\": 0,\n" +
                "  \"copies\": 1,\n" +
                "  \"descreen\": true,\n" +
                "  \"edgeToEdgeScan\": true,\n" +
                "  \"exposureLevel\": 0,\n" +
                "  \"gamma\": 0,\n" +
                "  \"highlightLevel\": 0,\n" +
                "  \"imagePreviewMode\": \"ipmNone\",\n" +
                "  \"invertColors\": true,\n" +
                "  \"links\": [\n" +
                "    {\n" +
                "      \"href\": \"/ext/copy/v1/defaultOptions\",\n" +
                "      \"rel\": \"self\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"longPlotScan\": true,\n" +
                "  \"mediaBindingFormat\": \"bfFlipLeft\",\n" +
                "  \"misfeedDetection\": true,\n" +
                "  \"originalMediaSize\": \"msANSI_A_8point5x11in\",\n" +
                "  \"originalMediaSource\": \"miAdf\",\n" +
                "  \"originalMediaType\": \"mtBond\",\n" +
                "  \"originalPlexMode\": \"pmAutoDetect\",\n" +
                "  \"outputCanvasAnchor\": \"ocaTopLeft\",\n" +
                "  \"outputCanvasMediaSize\": \"msANSI_A_8point5x11in\",\n" +
                "  \"outputCanvasMediaSource\": \"miAdf\",\n" +
                "  \"outputCanvasOrientation\": \"coAutoDetect\",\n" +
                "  \"outputCanvasXExtent\": 0,\n" +
                "  \"outputCanvasYExtent\": 0,\n" +
                "  \"outputDuplexBinding\": \"dbfOneSided\",\n" +
                "  \"outputMediaSize\": \"msANSI_A_8point5x11in\",\n" +
                "  \"outputMediaSource\": \"miAuto\",\n" +
                "  \"outputMediaType\": \"mtAny\",\n" +
                "  \"outputPlexMode\": \"pmAutoDetect\",\n" +
                "  \"outputQuality\": \"qmNormal\",\n" +
                "  \"pagesPerSheet\": \"ppsOneUp\",\n" +
                "  \"resolution\": \"dpi75\",\n" +
                "  \"scaleSelection\": \"sstNone\",\n" +
                "  \"scaleToFitEnabled\": true,\n" +
                "  \"scaleToOutput\": \"miAdf\",\n" +
                "  \"scaleToSize\": \"msANSI_A_8point5x11in\",\n" +
                "  \"scanCaptureMode\": \"scmStandard\",\n" +
                "  \"scanProgressMode\": \"spmNone\",\n" +
                "  \"scanningSpeed\": \"ssAuto\",\n" +
                "  \"shadowLevel\": 0,\n" +
                "  \"sharpness\": 0,\n" +
                "  \"threshold\": 0,\n" +
                "  \"xOffset\": 0,\n" +
                "  \"xScalePercent\": 0,\n" +
                "  \"yOffset\": 0,\n" +
                "  \"yScalePercent\": 0\n" +
                "}";
    }
}
