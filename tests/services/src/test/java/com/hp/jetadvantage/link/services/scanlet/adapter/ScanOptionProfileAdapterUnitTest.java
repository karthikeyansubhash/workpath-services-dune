package com.hp.jetadvantage.link.services.scanlet.adapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.ext.service.scanJob.Profile;
import com.hp.jetadvantage.link.api.massstorage.MassStorageInfo;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes;
import com.hp.jetadvantage.link.api.scanner.ScanAttributesCaps;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.utils.JsonParser;
import com.hp.jetadvantage.link.device.services.exceptions.BoundDeviceException;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceScanJobService;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.utils.Utils;
import com.hp.jetadvantage.link.services.common.exception.SdkConnectionErrorException;
import com.hp.jetadvantage.link.services.common.exception.SdkServiceErrorException;
import com.hp.jetadvantage.link.services.storagelet.IStorage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RunWith(MockitoJUnitRunner.class)
public class ScanOptionProfileAdapterUnitTest {
    public String testPackageName = Constants.TEST_PACKAGE_NAME;
    @Mock
    private IDeviceScanJobService mockScanJobService;

    @Before
    public void setUp() {
        //setup tests
    }

    /**
     * TEST for ScanOptionProfileAdapter.getCaps : Error case 1 - BoundDeviceException
     */
    @Test
    public void GivenScanOptionProfileAdapter_WhenGetCapabilitiesCalled_AndBoundDeviceExceptionOccurs_ThenConnectionErrorShouldBeReturned() {

        //1. Define the mocked ScanJobService behavior : throw BoundDeviceException
        when(mockScanJobService.getProfile(testPackageName)).thenThrow(new BoundDeviceException());

        //2. Setup necessary attributes for the test

        //3. Call the target device adapter method and validate result
        try {
            ScanOptionProfileAdapter.getCapabilities(testPackageName, mockScanJobService, ScanAttributes.TransmissionMode.JOB, ScanAttributes.Destination.HTTP, Sdk.VERSION.LEVEL);
            fail("Expected exception does not occur");
        } catch (SdkConnectionErrorException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Expected exception does not occur, e=" + e);
        }
    }

    /**
     * TEST for ScanDeviceAdapter.getCaps : Error case 2 - null scan profile
     */
    @Test
    public void GivenScanOptionProfileAdapter_WhenGetCapabilitiesCalled_NullProfileRetrievedFromDevice_ThenServiceErrorShouldBeReturned() {

        //1. Define the mocked ScanJobService behavior : return null profile
        when(mockScanJobService.getProfile(testPackageName)).thenReturn(null);

        //2. Setup necessary attributes for the test

        //3. Call the target device adapter method and validate result
        try {
            ScanOptionProfileAdapter.getCapabilities(testPackageName, mockScanJobService, ScanAttributes.TransmissionMode.JOB, ScanAttributes.Destination.HTTP, Sdk.VERSION.LEVEL);
            fail("Expected exception does not occur");
        } catch (SdkServiceErrorException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Expected exception does not occur, e=" + e);
        }
    }

    /**
     * TEST for ScanDeviceAdapter.getCaps : happy case - valid scan profile
     */
    @Test
    public void GivenScanOptionProfileAdapter_WhenGetCapabilitiesCalled_ValidProfileRetrievedFromDevice_ThenValidStringShouldBeReturned() throws Exception {

        //1. Define mocked ScanJobService to return a valid test scan Profile
        ObjectMapper mapper = new ObjectMapper();
        String response = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "scanJob/GET_ext_scanJob_profile.json");
        when(mockScanJobService.getProfile(testPackageName)).thenReturn(mapper.readValue(response, Profile.class));

        //2. Setup necessary attributes for the test

        //3. Call the target device adapter method and validate result
        try {
            String resultJsonStr = ScanOptionProfileAdapter.getCapabilities(testPackageName, mockScanJobService, ScanAttributes.TransmissionMode.JOB, ScanAttributes.Destination.HTTP, Sdk.VERSION.LEVEL);
            ScanAttributesCaps scanAttributeCaps = JsonParser.getInstance().fromJson(resultJsonStr, ScanAttributesCaps.class);

            //validate result scanAttributesCaps
            validateColorMode(scanAttributeCaps);

            assertFalse("DuplexList is empty", scanAttributeCaps.getDuplexList().isEmpty());
            assertEquals("DuplexList size mismatch", 4, scanAttributeCaps.getDuplexList().size());
            assertFalse("OrientationList is empty", scanAttributeCaps.getOrientationList().isEmpty());
            assertEquals("OrientationList size mismatch", 3, scanAttributeCaps.getOrientationList().size());
            assertFalse("DestinationList is empty", scanAttributeCaps.getDestinationList().isEmpty());
            assertEquals("DestinationList size mismatch", 5, scanAttributeCaps.getDestinationList().size());
            assertFalse("DocumentFormatList is empty", scanAttributeCaps.getDocumentFormatList(ScanAttributes.Destination.ME).isEmpty());
            assertEquals("DocumentFormatList size mismatch", 6, scanAttributeCaps.getDocumentFormatList(ScanAttributes.Destination.ME).size());
            assertFalse("ScanSizeList is empty", scanAttributeCaps.getScanSizeList().isEmpty());
            assertEquals("ScanSizeList size mismatch", 23, scanAttributeCaps.getScanSizeList().size());
            assertFalse("ScanPreviewList is empty", scanAttributeCaps.getScanPreviewList().isEmpty());
            assertFalse("ResolutionList is empty", scanAttributeCaps.getResolutionList().isEmpty());
            assertEquals("ResolutionList size mismatch", 7, scanAttributeCaps.getResolutionList().size());
            assertFalse("BackgroundCleanupList is empty", scanAttributeCaps.getBackgroundCleanupList().isEmpty());
            assertEquals("BackgroundCleanupList size mismatch", 10, scanAttributeCaps.getBackgroundCleanupList().size());
            assertFalse("ContrastAdjustmentList is empty", scanAttributeCaps.getContrastAdjustmentList().isEmpty());
            assertEquals("ContrastAdjustmentList size mismatch", 10, scanAttributeCaps.getContrastAdjustmentList().size());
            assertFalse("BlankImageRemovalModeList is empty", scanAttributeCaps.getBlankImageRemovalModeList().isEmpty());
            assertFalse("ProgressDialogModeList is empty", scanAttributeCaps.getProgressDialogModeList().isEmpty());
            assertEquals("ProgressDialogModeList size mismatch", 3, scanAttributeCaps.getProgressDialogModeList().size());
            assertFalse("OutputQualityList is empty", scanAttributeCaps.getOutputQualityList().isEmpty());
            assertEquals("OutputQualityList size mismatch", 4, scanAttributeCaps.getOutputQualityList().size());
            assertFalse("TransmissionModeList is empty", scanAttributeCaps.getTransmissionModeList().isEmpty());
            assertEquals("TransmissionModeList size mismatch", 3, scanAttributeCaps.getTransmissionModeList().size());
            assertFalse("DocumentFormatsByColorMode is empty", scanAttributeCaps.getDocumentFormatsByColorMode().isEmpty());
            assertEquals("DocumentFormatsByColorMode size mismatch, actual:" + scanAttributeCaps.getDocumentFormatsByColorMode(), 5,
                    scanAttributeCaps.getDocumentFormatsByColorMode().size());
            assertEquals("DocumentFormatsByColorMode MONO size mismatch", 4,
                    scanAttributeCaps.getDocumentFormatsByColorMode().get(ScanAttributes.ColorMode.MONO).size());
            assertEquals("DocumentFormatsByColorMode COLOR size mismatch", 5,
                    scanAttributeCaps.getDocumentFormatsByColorMode().get(ScanAttributes.ColorMode.COLOR).size());
            assertFalse("SharpnessAdjustmentList is empty", scanAttributeCaps.getSharpnessAdjustmentList().isEmpty());
            assertFalse("TextPhotoOptimizationList is empty", scanAttributeCaps.getTextPhotoOptimizationList().isEmpty());
            assertEquals("TextPhotoOptimizationList size mismatch, actual:" + scanAttributeCaps.getTextPhotoOptimizationList(), 4,
                    scanAttributeCaps.getTextPhotoOptimizationList().size());
            assertFalse("MediaSourceList is empty", scanAttributeCaps.getMediaSourceList().isEmpty());
            assertEquals("MediaSourceList size mismatch", 3, scanAttributeCaps.getMediaSourceList().size());
            assertFalse("MisfeedDetectionModeList is empty", scanAttributeCaps.getMisfeedDetectionModeList().isEmpty());
            assertEquals("MisfeedDetectionModeList size mismatch", 3, scanAttributeCaps.getMisfeedDetectionModeList().size());
            assertFalse("CaptureModeList is empty", scanAttributeCaps.getCaptureModeList().isEmpty());
            assertFalse("AutomaticStraightenModeList is empty", scanAttributeCaps.getAutomaticStraightenModeList().isEmpty());
            assertEquals("AutomaticStraightenModeList size mismatch", 3, scanAttributeCaps.getAutomaticStraightenModeList().size());
            assertFalse("DarknessAdjustmentList is empty", scanAttributeCaps.getDarknessAdjustmentList().isEmpty());
            assertFalse("ColorDropoutModeList is empty", scanAttributeCaps.getColorDropoutModeList().isEmpty());
            assertFalse("CropModeList is empty", scanAttributeCaps.getCropModeList().isEmpty());
            assertFalse("JobAssemblyModeList is empty", scanAttributeCaps.getJobAssemblyModeList().isEmpty());
            assertFalse("MediaWeightAdjustmentList is empty", scanAttributeCaps.getMediaWeightAdjustmentList().isEmpty());
            assertFalse("SplitAttachmentByPageList is empty", scanAttributeCaps.getSplitAttachmentByPageList().isEmpty());
            assertFalse("EraseMarginUnitList is empty", scanAttributeCaps.getEraseMarginUnitList().isEmpty());

        } catch (Exception e) {
            fail("Unexpected exception=" + e);
        }
    }

    /**
     * TEST for ScanOptionProfileAdapter.getCaps : happy case with TransmissionMode.IMAGE - valid scan profile
     */
    @Test
    public void GivenScanOptionProfileAdapter_WhenGetCapsCalledWithImageTransmissionMode_ThenScanAttributesCapabilitiesJsonStringShouldBeReturned() throws Exception {

        //1. Define mocked ScanJobService to return a valid test scan Profile
        ObjectMapper mapper = new ObjectMapper();
        String response = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "scanJob/GET_ext_scanJob_profile.json");
        when(mockScanJobService.getProfile(testPackageName)).thenReturn(mapper.readValue(response, Profile.class));

        //2. Setup necessary attributes for the test

        //3. Call the target device adapter method and validate result
        try {
            String resultJsonStr = ScanOptionProfileAdapter.getCapabilities(testPackageName, mockScanJobService, ScanAttributes.TransmissionMode.IMAGE, ScanAttributes.Destination.HTTP, Sdk.VERSION.LEVEL);
            ScanAttributesCaps scanAttributeCaps = JsonParser.getInstance().fromJson(resultJsonStr, ScanAttributesCaps.class);

            //validate result scanAttributesCaps
            validateColorMode(scanAttributeCaps);

            assertFalse("DuplexList is empty", scanAttributeCaps.getDuplexList().isEmpty());
            assertEquals("DuplexList size mismatch", 4, scanAttributeCaps.getDuplexList().size());
            assertFalse("OrientationList is empty", scanAttributeCaps.getOrientationList().isEmpty());
            assertEquals("OrientationList size mismatch", 3, scanAttributeCaps.getOrientationList().size());
            assertFalse("DestinationList is empty", scanAttributeCaps.getDestinationList().isEmpty());
            assertEquals("DestinationList size mismatch, NetworkFolder/FTP should not be supported for Image transmission mode", 3,
                    scanAttributeCaps.getDestinationList().size());
            assertFalse("DocumentFormatList is empty", scanAttributeCaps.getDocumentFormatList(ScanAttributes.Destination.ME).isEmpty());
        } catch (Exception e) {
            fail("Unexpected exception=" + e);
        }
    }

    /**
     * TEST for ScanOptionProfileAdapter.getCaps : FTP destination - valid scan profile
     */
    @Test
    public void GivenScanOptionProfileAdapter_WhenGetCapsCalledWithFtpDestination_ThenValidCapabilitiesShouldBeReturned() throws Exception {

        //1. Define mocked ScanJobService to return a valid test scan Profile
        ObjectMapper mapper = new ObjectMapper();
        String response = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "scanJob/GET_ext_scanJob_profile.json");
        when(mockScanJobService.getProfile(testPackageName)).thenReturn(mapper.readValue(response, Profile.class));

        //2. Setup necessary attributes for the test

        //3. Call the target device adapter method and validate result
        try {
            String resultJsonStr = ScanOptionProfileAdapter.getCapabilities(testPackageName, mockScanJobService, ScanAttributes.TransmissionMode.JOB, ScanAttributes.Destination.FTP, Sdk.VERSION.LEVEL);
            ScanAttributesCaps scanAttributeCaps = JsonParser.getInstance().fromJson(resultJsonStr, ScanAttributesCaps.class);

            //validate result scanAttributesCaps - FTP should use NetworkFolder profile
            validateColorMode(scanAttributeCaps);

            assertFalse("DuplexList is empty", scanAttributeCaps.getDuplexList().isEmpty());
            assertEquals("DuplexList size mismatch", 4, scanAttributeCaps.getDuplexList().size());
            assertFalse("OrientationList is empty", scanAttributeCaps.getOrientationList().isEmpty());
            assertEquals("OrientationList size mismatch", 3, scanAttributeCaps.getOrientationList().size());
            assertFalse("DestinationList is empty", scanAttributeCaps.getDestinationList().isEmpty());
            assertEquals("DestinationList size mismatch", 5, scanAttributeCaps.getDestinationList().size());
            assertFalse("DocumentFormatList is empty", scanAttributeCaps.getDocumentFormatList(ScanAttributes.Destination.FTP).isEmpty());
            assertEquals("DocumentFormatList size mismatch", 6, scanAttributeCaps.getDocumentFormatList(ScanAttributes.Destination.FTP).size());
            assertFalse("ScanSizeList is empty", scanAttributeCaps.getScanSizeList().isEmpty());
            assertEquals("ScanSizeList size mismatch", 23, scanAttributeCaps.getScanSizeList().size());
            assertFalse("ScanPreviewList is empty", scanAttributeCaps.getScanPreviewList().isEmpty());
            assertFalse("ResolutionList is empty", scanAttributeCaps.getResolutionList().isEmpty());
            assertEquals("ResolutionList size mismatch", 7, scanAttributeCaps.getResolutionList().size());
            assertFalse("BackgroundCleanupList is empty", scanAttributeCaps.getBackgroundCleanupList().isEmpty());
            assertEquals("BackgroundCleanupList size mismatch", 10, scanAttributeCaps.getBackgroundCleanupList().size());
            assertFalse("ContrastAdjustmentList is empty", scanAttributeCaps.getContrastAdjustmentList().isEmpty());
            assertEquals("ContrastAdjustmentList size mismatch", 10, scanAttributeCaps.getContrastAdjustmentList().size());
            assertFalse("BlankImageRemovalModeList is empty", scanAttributeCaps.getBlankImageRemovalModeList().isEmpty());
            assertFalse("ProgressDialogModeList is empty", scanAttributeCaps.getProgressDialogModeList().isEmpty());
            assertEquals("ProgressDialogModeList size mismatch", 3, scanAttributeCaps.getProgressDialogModeList().size());
            assertFalse("OutputQualityList is empty", scanAttributeCaps.getOutputQualityList().isEmpty());
            assertEquals("OutputQualityList size mismatch", 4, scanAttributeCaps.getOutputQualityList().size());
            assertFalse("TransmissionModeList is empty", scanAttributeCaps.getTransmissionModeList().isEmpty());
            assertEquals("TransmissionModeList size mismatch", 2, scanAttributeCaps.getTransmissionModeList().size());
            assertFalse("DocumentFormatsByColorMode is empty", scanAttributeCaps.getDocumentFormatsByColorMode().isEmpty());
            assertEquals("DocumentFormatsByColorMode size mismatch, actual:" + scanAttributeCaps.getDocumentFormatsByColorMode(), 5,
                    scanAttributeCaps.getDocumentFormatsByColorMode().size());
            assertEquals("DocumentFormatsByColorMode MONO size mismatch", 4,
                    scanAttributeCaps.getDocumentFormatsByColorMode().get(ScanAttributes.ColorMode.MONO).size());
            assertEquals("DocumentFormatsByColorMode COLOR size mismatch", 5,
                    scanAttributeCaps.getDocumentFormatsByColorMode().get(ScanAttributes.ColorMode.COLOR).size());
            assertFalse("SharpnessAdjustmentList is empty", scanAttributeCaps.getSharpnessAdjustmentList().isEmpty());
            assertFalse("TextPhotoOptimizationList is empty", scanAttributeCaps.getTextPhotoOptimizationList().isEmpty());
            assertEquals("TextPhotoOptimizationList size mismatch, actual:" + scanAttributeCaps.getTextPhotoOptimizationList(), 4,
                    scanAttributeCaps.getTextPhotoOptimizationList().size());
            assertFalse("MediaSourceList is empty", scanAttributeCaps.getMediaSourceList().isEmpty());
            assertEquals("MediaSourceList size mismatch", 3, scanAttributeCaps.getMediaSourceList().size());
            assertFalse("MisfeedDetectionModeList is empty", scanAttributeCaps.getMisfeedDetectionModeList().isEmpty());
            assertEquals("MisfeedDetectionModeList size mismatch", 3, scanAttributeCaps.getMisfeedDetectionModeList().size());
            assertFalse("CaptureModeList is empty", scanAttributeCaps.getCaptureModeList().isEmpty());
            assertFalse("AutomaticStraightenModeList is empty", scanAttributeCaps.getAutomaticStraightenModeList().isEmpty());
            assertEquals("AutomaticStraightenModeList size mismatch", 3, scanAttributeCaps.getAutomaticStraightenModeList().size());
            assertFalse("DarknessAdjustmentList is empty", scanAttributeCaps.getDarknessAdjustmentList().isEmpty());
            assertFalse("ColorDropoutModeList is empty", scanAttributeCaps.getColorDropoutModeList().isEmpty());
            assertFalse("CropModeList is empty", scanAttributeCaps.getCropModeList().isEmpty());
            assertFalse("JobAssemblyModeList is empty", scanAttributeCaps.getJobAssemblyModeList().isEmpty());
            assertFalse("MediaWeightAdjustmentList is empty", scanAttributeCaps.getMediaWeightAdjustmentList().isEmpty());
            assertFalse("SplitAttachmentByPageList is empty", scanAttributeCaps.getSplitAttachmentByPageList().isEmpty());
            assertFalse("EraseMarginUnitList is empty", scanAttributeCaps.getEraseMarginUnitList().isEmpty());
        } catch (Exception e) {
            fail("Unexpected exception=" + e);
        }
    }

    /**
     * TEST for ScanOptionProfileAdapter.getCaps : NETWORK_FOLDER destination - valid scan profile
     */
    @Test
    public void GivenScanOptionProfileAdapter_WhenGetCapsCalledWithNetworkFolderDestination_ThenValidCapabilitiesShouldBeReturned() throws Exception {

        //1. Define mocked ScanJobService to return a valid test scan Profile
        ObjectMapper mapper = new ObjectMapper();
        String response = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "scanJob/GET_ext_scanJob_profile.json");
        when(mockScanJobService.getProfile(testPackageName)).thenReturn(mapper.readValue(response, Profile.class));

        //2. Setup necessary attributes for the test

        //3. Call the target device adapter method and validate result
        try {
            String resultJsonStr = ScanOptionProfileAdapter.getCapabilities(testPackageName, mockScanJobService, ScanAttributes.TransmissionMode.JOB, ScanAttributes.Destination.NETWORK_FOLDER, Sdk.VERSION.LEVEL);
            ScanAttributesCaps scanAttributeCaps = JsonParser.getInstance().fromJson(resultJsonStr, ScanAttributesCaps.class);

            //validate result scanAttributesCaps - NETWORK_FOLDER should use NetworkFolder profile
            validateColorMode(scanAttributeCaps);

            assertFalse("DuplexList is empty", scanAttributeCaps.getDuplexList().isEmpty());
            assertEquals("DuplexList size mismatch", 4, scanAttributeCaps.getDuplexList().size());
            assertFalse("OrientationList is empty", scanAttributeCaps.getOrientationList().isEmpty());
            assertEquals("OrientationList size mismatch", 3, scanAttributeCaps.getOrientationList().size());
            assertFalse("DestinationList is empty", scanAttributeCaps.getDestinationList().isEmpty());
            assertEquals("DestinationList size mismatch", 5, scanAttributeCaps.getDestinationList().size());
            assertFalse("DocumentFormatList is empty", scanAttributeCaps.getDocumentFormatList(ScanAttributes.Destination.NETWORK_FOLDER).isEmpty());
            assertEquals("DocumentFormatList size mismatch", 6, scanAttributeCaps.getDocumentFormatList(ScanAttributes.Destination.NETWORK_FOLDER).size());
            assertFalse("ScanSizeList is empty", scanAttributeCaps.getScanSizeList().isEmpty());
            assertEquals("ScanSizeList size mismatch", 23, scanAttributeCaps.getScanSizeList().size());
            assertFalse("ScanPreviewList is empty", scanAttributeCaps.getScanPreviewList().isEmpty());
            assertFalse("ResolutionList is empty", scanAttributeCaps.getResolutionList().isEmpty());
            assertEquals("ResolutionList size mismatch", 7, scanAttributeCaps.getResolutionList().size());
            assertFalse("BackgroundCleanupList is empty", scanAttributeCaps.getBackgroundCleanupList().isEmpty());
            assertEquals("BackgroundCleanupList size mismatch", 10, scanAttributeCaps.getBackgroundCleanupList().size());
            assertFalse("ContrastAdjustmentList is empty", scanAttributeCaps.getContrastAdjustmentList().isEmpty());
            assertEquals("ContrastAdjustmentList size mismatch", 10, scanAttributeCaps.getContrastAdjustmentList().size());
            assertFalse("BlankImageRemovalModeList is empty", scanAttributeCaps.getBlankImageRemovalModeList().isEmpty());
            assertFalse("ProgressDialogModeList is empty", scanAttributeCaps.getProgressDialogModeList().isEmpty());
            assertEquals("ProgressDialogModeList size mismatch", 3, scanAttributeCaps.getProgressDialogModeList().size());
            assertFalse("OutputQualityList is empty", scanAttributeCaps.getOutputQualityList().isEmpty());
            assertEquals("OutputQualityList size mismatch", 4, scanAttributeCaps.getOutputQualityList().size());
            assertFalse("TransmissionModeList is empty", scanAttributeCaps.getTransmissionModeList().isEmpty());
            assertEquals("TransmissionModeList size mismatch", 2, scanAttributeCaps.getTransmissionModeList().size());
            assertFalse("DocumentFormatsByColorMode is empty", scanAttributeCaps.getDocumentFormatsByColorMode().isEmpty());
            assertEquals("DocumentFormatsByColorMode size mismatch, actual:" + scanAttributeCaps.getDocumentFormatsByColorMode(), 5,
                    scanAttributeCaps.getDocumentFormatsByColorMode().size());
            assertEquals("DocumentFormatsByColorMode MONO size mismatch", 4,
                    scanAttributeCaps.getDocumentFormatsByColorMode().get(ScanAttributes.ColorMode.MONO).size());
            assertEquals("DocumentFormatsByColorMode COLOR size mismatch", 5,
                    scanAttributeCaps.getDocumentFormatsByColorMode().get(ScanAttributes.ColorMode.COLOR).size());
            assertFalse("SharpnessAdjustmentList is empty", scanAttributeCaps.getSharpnessAdjustmentList().isEmpty());
            assertFalse("TextPhotoOptimizationList is empty", scanAttributeCaps.getTextPhotoOptimizationList().isEmpty());
            assertEquals("TextPhotoOptimizationList size mismatch, actual:" + scanAttributeCaps.getTextPhotoOptimizationList(), 4,
                    scanAttributeCaps.getTextPhotoOptimizationList().size());
            assertFalse("MediaSourceList is empty", scanAttributeCaps.getMediaSourceList().isEmpty());
            assertEquals("MediaSourceList size mismatch", 3, scanAttributeCaps.getMediaSourceList().size());
            assertFalse("MisfeedDetectionModeList is empty", scanAttributeCaps.getMisfeedDetectionModeList().isEmpty());
            assertEquals("MisfeedDetectionModeList size mismatch", 3, scanAttributeCaps.getMisfeedDetectionModeList().size());
            assertFalse("CaptureModeList is empty", scanAttributeCaps.getCaptureModeList().isEmpty());
            assertFalse("AutomaticStraightenModeList is empty", scanAttributeCaps.getAutomaticStraightenModeList().isEmpty());
            assertEquals("AutomaticStraightenModeList size mismatch", 3, scanAttributeCaps.getAutomaticStraightenModeList().size());
            assertFalse("DarknessAdjustmentList is empty", scanAttributeCaps.getDarknessAdjustmentList().isEmpty());
            assertFalse("ColorDropoutModeList is empty", scanAttributeCaps.getColorDropoutModeList().isEmpty());
            assertFalse("CropModeList is empty", scanAttributeCaps.getCropModeList().isEmpty());
            assertFalse("JobAssemblyModeList is empty", scanAttributeCaps.getJobAssemblyModeList().isEmpty());
            assertFalse("MediaWeightAdjustmentList is empty", scanAttributeCaps.getMediaWeightAdjustmentList().isEmpty());
            assertFalse("SplitAttachmentByPageList is empty", scanAttributeCaps.getSplitAttachmentByPageList().isEmpty());
            assertFalse("EraseMarginUnitList is empty", scanAttributeCaps.getEraseMarginUnitList().isEmpty());
        } catch (Exception e) {
            fail("Unexpected exception=" + e);
        }
    }

    /**
     * TEST for ScanOptionProfileAdapter destination logic: all destinatnions
     */
    @Test
    public void GivenScanOptionProfileAdapter_WhenGetCapabilities_ThenPossibleDestinationsShouldBeIncluded() throws Exception {
        setStorageProviderMock(true, true);

        ObjectMapper mapper = new ObjectMapper();
        String response = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "scanJob/GET_ext_scanJob_profile.json");
        when(mockScanJobService.getProfile(testPackageName)).thenReturn(mapper.readValue(response, Profile.class));

        String resultJsonStr = ScanOptionProfileAdapter.getCapabilities(testPackageName,
                mockScanJobService, ScanAttributes.TransmissionMode.JOB, ScanAttributes.Destination.HTTP, Sdk.VERSION.LEVEL);
        ScanAttributesCaps caps = JsonParser.getInstance().fromJson(resultJsonStr, ScanAttributesCaps.class);

        assertTrue("HTTP should be included",
                caps.getDestinationList().contains(ScanAttributes.Destination.HTTP));
        assertTrue("ME should be included",
                caps.getDestinationList().contains(ScanAttributes.Destination.ME));
        assertTrue("EMAIL should be included",
                caps.getDestinationList().contains(ScanAttributes.Destination.EMAIL));
        assertTrue("USB should be included",
                caps.getDestinationList().contains(ScanAttributes.Destination.USB));
        assertTrue("FTP should be in destination list for JOB mode",
                caps.getDestinationList().contains(ScanAttributes.Destination.FTP));
        assertTrue("NETWORK_FOLDER should be in destination list for JOB mode",
                caps.getDestinationList().contains(ScanAttributes.Destination.NETWORK_FOLDER));
    }

    /**
     * TEST for ScanOptionProfileAdapter destination logic: HTTP always included regardless of transmission mode
     */
    @Test
    public void GivenScanOptionProfileAdapter_WhenGetCapabilitiesWithDifferentTransmissionModes_ThenHttpShouldAlwaysBeIncluded() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String response = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "scanJob/GET_ext_scanJob_profile.json");
        when(mockScanJobService.getProfile(testPackageName)).thenReturn(mapper.readValue(response, Profile.class));

        // Test with JOB mode
        String resultJobMode = ScanOptionProfileAdapter.getCapabilities(testPackageName,
                mockScanJobService, ScanAttributes.TransmissionMode.JOB, ScanAttributes.Destination.HTTP, Sdk.VERSION.LEVEL);
        ScanAttributesCaps capsJob = JsonParser.getInstance().fromJson(resultJobMode, ScanAttributesCaps.class);
        assertTrue("HTTP should be included for JOB mode",
                capsJob.getDestinationList().contains(ScanAttributes.Destination.HTTP));

        // Test with IMAGE mode
        String resultImageMode = ScanOptionProfileAdapter.getCapabilities(testPackageName,
                mockScanJobService, ScanAttributes.TransmissionMode.IMAGE, ScanAttributes.Destination.HTTP, Sdk.VERSION.LEVEL);
        ScanAttributesCaps capsImage = JsonParser.getInstance().fromJson(resultImageMode, ScanAttributesCaps.class);
        assertTrue("HTTP should be included for IMAGE mode",
                capsImage.getDestinationList().contains(ScanAttributes.Destination.HTTP));
    }

    /**
     * TEST for ScanOptionProfileAdapter destination logic: IMAGE mode excludes network folder destinations
     */
    @Test
    public void GivenScanOptionProfileAdapter_WhenGetCapabilitiesWithImageMode_ThenNetworkFolderDestinationsShouldBeExcluded() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String response = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "scanJob/GET_ext_scanJob_profile.json");
        when(mockScanJobService.getProfile(testPackageName)).thenReturn(mapper.readValue(response, Profile.class));

        String resultJsonStr = ScanOptionProfileAdapter.getCapabilities(testPackageName,
                mockScanJobService, ScanAttributes.TransmissionMode.IMAGE, ScanAttributes.Destination.HTTP, Sdk.VERSION.LEVEL);
        ScanAttributesCaps caps = JsonParser.getInstance().fromJson(resultJsonStr, ScanAttributesCaps.class);

        assertFalse("FTP should not be included for IMAGE mode",
                caps.getDestinationList().contains(ScanAttributes.Destination.FTP));
        assertFalse("NETWORK_FOLDER should not be included for IMAGE mode",
                caps.getDestinationList().contains(ScanAttributes.Destination.NETWORK_FOLDER));
    }

    /**
     * TEST for ScanOptionProfileAdapter destination logic: JOB mode includes network folder destinations
     */
    @Test
    public void GivenScanOptionProfileAdapter_WhenGetCapabilitiesWithJobMode_ThenNetworkFolderDestinationsShouldBeIncluded() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String response = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "scanJob/GET_ext_scanJob_profile.json");
        when(mockScanJobService.getProfile(testPackageName)).thenReturn(mapper.readValue(response, Profile.class));

        String resultJsonStr = ScanOptionProfileAdapter.getCapabilities(testPackageName,
                mockScanJobService, ScanAttributes.TransmissionMode.JOB, ScanAttributes.Destination.HTTP, Sdk.VERSION.LEVEL);
        ScanAttributesCaps caps = JsonParser.getInstance().fromJson(resultJsonStr, ScanAttributesCaps.class);

        assertTrue("FTP should be included for JOB mode",
                caps.getDestinationList().contains(ScanAttributes.Destination.FTP));
        assertTrue("NETWORK_FOLDER should be included for JOB mode",
                caps.getDestinationList().contains(ScanAttributes.Destination.NETWORK_FOLDER));
    }

    /**
     * TEST for ScanOptionProfileAdapter destination logic: ME and EMAIL always included
     */
    @Test
    public void GivenScanOptionProfileAdapter_WhenGetCapabilitiesWithAnyTransmissionMode_ThenMeAndEmailShouldBeIncluded() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String response = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "scanJob/GET_ext_scanJob_profile.json");
        when(mockScanJobService.getProfile(testPackageName)).thenReturn(mapper.readValue(response, Profile.class));

        // Test with JOB mode
        String resultJobMode = ScanOptionProfileAdapter.getCapabilities(testPackageName,
                mockScanJobService, ScanAttributes.TransmissionMode.JOB, ScanAttributes.Destination.HTTP, Sdk.VERSION.LEVEL);
        ScanAttributesCaps capsJob = JsonParser.getInstance().fromJson(resultJobMode, ScanAttributesCaps.class);
        assertTrue("ME should be included for JOB mode",
                capsJob.getDestinationList().contains(ScanAttributes.Destination.ME));
        assertTrue("EMAIL should be included for JOB mode",
                capsJob.getDestinationList().contains(ScanAttributes.Destination.EMAIL));

        // Test with IMAGE mode
        String resultImageMode = ScanOptionProfileAdapter.getCapabilities(testPackageName,
                mockScanJobService, ScanAttributes.TransmissionMode.IMAGE, ScanAttributes.Destination.HTTP, Sdk.VERSION.LEVEL);
        ScanAttributesCaps capsImage = JsonParser.getInstance().fromJson(resultImageMode, ScanAttributesCaps.class);
        assertTrue("ME should be included for IMAGE mode",
                capsImage.getDestinationList().contains(ScanAttributes.Destination.ME));
        assertTrue("EMAIL should be included for IMAGE mode",
                capsImage.getDestinationList().contains(ScanAttributes.Destination.EMAIL));
    }

    /**
     * TEST for ScanOptionProfileAdapter destination logic: USB destination included when mounted storage exists
     */
    @Test
    public void GivenScanOptionProfileAdapter_WhenGetCapabilitiesWithMountedUsb_ThenUsbDestinationShouldBeIncluded() throws Exception {
        setStorageProviderMock(true, true);
        ObjectMapper mapper = new ObjectMapper();
        String response = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "scanJob/GET_ext_scanJob_profile.json");
        when(mockScanJobService.getProfile(testPackageName)).thenReturn(mapper.readValue(response, Profile.class));

        String resultJsonStr = ScanOptionProfileAdapter.getCapabilities(testPackageName,
                mockScanJobService, ScanAttributes.TransmissionMode.JOB, ScanAttributes.Destination.HTTP, Sdk.VERSION.LEVEL);
        ScanAttributesCaps caps = JsonParser.getInstance().fromJson(resultJsonStr, ScanAttributesCaps.class);

        assertTrue("USB should be included when mounted storage exists",
                caps.getDestinationList().contains(ScanAttributes.Destination.USB));
    }

    /**
     * TEST for ScanOptionProfileAdapter destination logic: USB destination excluded when no mounted storage
     */
    @Test
    public void GivenScanOptionProfileAdapter_WhenGetCapabilitiesWithoutMountedUsb_ThenUsbDestinationShouldNotBeIncluded() throws Exception {
        setStorageProviderMock(true, false);
        ObjectMapper mapper = new ObjectMapper();
        String response = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "scanJob/GET_ext_scanJob_profile.json");
        when(mockScanJobService.getProfile(testPackageName)).thenReturn(mapper.readValue(response, Profile.class));

        String resultJsonStr = ScanOptionProfileAdapter.getCapabilities(testPackageName,
                mockScanJobService, ScanAttributes.TransmissionMode.JOB, ScanAttributes.Destination.HTTP, Sdk.VERSION.LEVEL);
        ScanAttributesCaps caps = JsonParser.getInstance().fromJson(resultJsonStr, ScanAttributesCaps.class);

        assertFalse("USB should not be included when no mounted storage exists",
                caps.getDestinationList().contains(ScanAttributes.Destination.USB));
    }

    /**
     * TEST for ScanOptionProfileAdapter destination logic: USB excluded when storage list is empty
     */
    @Test
    public void GivenScanOptionProfileAdapter_WhenGetCapabilitiesWithEmptyStorageList_ThenUsbDestinationShouldNotBeIncluded() throws Exception {
        setStorageProviderMock(false, true);
        ObjectMapper mapper = new ObjectMapper();
        String response = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "scanJob/GET_ext_scanJob_profile.json");
        when(mockScanJobService.getProfile(testPackageName)).thenReturn(mapper.readValue(response, Profile.class));

        String resultJsonStr = ScanOptionProfileAdapter.getCapabilities(testPackageName,
                mockScanJobService, ScanAttributes.TransmissionMode.JOB, ScanAttributes.Destination.HTTP, Sdk.VERSION.LEVEL);
        ScanAttributesCaps caps = JsonParser.getInstance().fromJson(resultJsonStr, ScanAttributesCaps.class);

        assertFalse("USB should not be included when storage list is empty",
                caps.getDestinationList().contains(ScanAttributes.Destination.USB));
    }


    /**
     * TEST for ScanOptionProfileAdapter destination logic: Verify all possible destinations for JOB mode
     */
    @Test
    public void GivenScanOptionProfileAdapter_WhenGetCapabilitiesWithJobMode_ThenAllRelevantDestinationsShouldBeReturned() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String response = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "scanJob/GET_ext_scanJob_profile.json");
        when(mockScanJobService.getProfile(testPackageName)).thenReturn(mapper.readValue(response, Profile.class));

        String resultJsonStr = ScanOptionProfileAdapter.getCapabilities(testPackageName,
                mockScanJobService, ScanAttributes.TransmissionMode.JOB, ScanAttributes.Destination.HTTP, Sdk.VERSION.LEVEL);
        ScanAttributesCaps caps = JsonParser.getInstance().fromJson(resultJsonStr, ScanAttributesCaps.class);

        List<ScanAttributes.Destination> destinations = caps.getDestinationList();
        assertFalse("Destination list should not be empty", destinations.isEmpty());

        // HTTP is always included
        assertTrue("HTTP must be in destination list", destinations.contains(ScanAttributes.Destination.HTTP));

        // ME and EMAIL should be included
        assertTrue("ME should be in destination list", destinations.contains(ScanAttributes.Destination.ME));
        assertTrue("EMAIL should be in destination list", destinations.contains(ScanAttributes.Destination.EMAIL));

        // Document destinations should be included for JOB mode
        assertTrue("FTP should be in destination list for JOB mode",
                destinations.contains(ScanAttributes.Destination.FTP));
        assertTrue("NETWORK_FOLDER should be in destination list for JOB mode",
                destinations.contains(ScanAttributes.Destination.NETWORK_FOLDER));
    }

    private void setStorageProviderMock(boolean hasStorage, boolean mounted) {
        // Mock storage provider
        ScanOptionProfileAdapter.IStorageProvider mockStorageProvider = mock(ScanOptionProfileAdapter.IStorageProvider.class);
        IStorage mockStorage = mock(IStorage.class);
        MassStorageInfo mockStorageInfo = mock(MassStorageInfo.class);

        if (hasStorage) {
            when(mockStorage.getInfo()).thenReturn(mockStorageInfo);
            when(mockStorageInfo.isMounted()).thenReturn(mounted);
            when(mockStorageProvider.getStorageList(MassStorageInfo.StorageType.USB))
                    .thenReturn(Arrays.asList(mockStorage));
        } else {
            when(mockStorageProvider.getStorageList(MassStorageInfo.StorageType.USB))
                    .thenReturn(Arrays.asList());
        }

        // Inject mock
        ScanOptionProfileAdapter.setStorageProvider(mockStorageProvider);
    }

    private void validateColorMode(ScanAttributesCaps scanAttributeCaps) {
        List<ScanAttributes.ColorMode> expectedColorModes = Arrays.asList(
                ScanAttributes.ColorMode.DEFAULT,
                ScanAttributes.ColorMode.COLOR,
                ScanAttributes.ColorMode.MONO,
                ScanAttributes.ColorMode.AUTO,
                ScanAttributes.ColorMode.GRAY
        );
        assertFalse("ColorModeList is empty", scanAttributeCaps.getColorModeList().isEmpty());
        assertEquals("ColorModeList size mismatch", 5, scanAttributeCaps.getColorModeList().size());
        assertTrue("ColorModeList values mismatch", scanAttributeCaps.getColorModeList().containsAll(expectedColorModes));
    }

    // ========================================================================================
    // Phase 6b: All-Options Profile — getCapabilities Integration Tests
    // ========================================================================================

    private ScanAttributesCaps getCapabilitiesWithAllOptions(ScanAttributes.Destination destination) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String response = Utils.loadTestJsonResource(
                Objects.requireNonNull(getClass().getClassLoader()),
                "scanJob/GET_ext_scanJob_profile_allOptions.json"
        );
        when(mockScanJobService.getProfile(testPackageName))
                .thenReturn(mapper.readValue(response, Profile.class));

        String resultJsonStr = ScanOptionProfileAdapter.getCapabilities(
                testPackageName, mockScanJobService,
                ScanAttributes.TransmissionMode.JOB, destination, Sdk.VERSION.LEVEL);
        return JsonParser.getInstance().fromJson(resultJsonStr, ScanAttributesCaps.class);
    }

    /**
     * TEST: All-Options profile — all capability lists should be populated.
     * Verifies that every ScanTypeMapping-related list is non-empty when all options are available.
     */
    @Test
    public void GivenAllOptionsProfile_WhenGetCapabilitiesCalled_ThenAllListsShouldBePopulated() throws Exception {
        ScanAttributesCaps caps = getCapabilitiesWithAllOptions(ScanAttributes.Destination.HTTP);

        assertFalse("ColorModeList is empty", caps.getColorModeList().isEmpty());
        assertFalse("DuplexList is empty", caps.getDuplexList().isEmpty());
        assertFalse("OrientationList is empty", caps.getOrientationList().isEmpty());
        assertFalse("DocumentFormatList is empty", caps.getDocumentFormatList(ScanAttributes.Destination.ME).isEmpty());
        assertFalse("ScanSizeList is empty", caps.getScanSizeList().isEmpty());
        assertFalse("ScanPreviewList is empty", caps.getScanPreviewList().isEmpty());
        assertFalse("ResolutionList is empty", caps.getResolutionList().isEmpty());
        assertFalse("BackgroundCleanupList is empty", caps.getBackgroundCleanupList().isEmpty());
        assertFalse("ContrastAdjustmentList is empty", caps.getContrastAdjustmentList().isEmpty());
        assertFalse("BlankImageRemovalModeList is empty", caps.getBlankImageRemovalModeList().isEmpty());
        assertFalse("ProgressDialogModeList is empty", caps.getProgressDialogModeList().isEmpty());
        assertFalse("OutputQualityList is empty", caps.getOutputQualityList().isEmpty());
        assertFalse("TransmissionModeList is empty", caps.getTransmissionModeList().isEmpty());
        assertFalse("SharpnessAdjustmentList is empty", caps.getSharpnessAdjustmentList().isEmpty());
        assertFalse("TextPhotoOptimizationList is empty", caps.getTextPhotoOptimizationList().isEmpty());
        assertFalse("MediaSourceList is empty", caps.getMediaSourceList().isEmpty());
        assertFalse("MisfeedDetectionModeList is empty", caps.getMisfeedDetectionModeList().isEmpty());
        assertFalse("CaptureModeList is empty", caps.getCaptureModeList().isEmpty());
        assertFalse("AutomaticStraightenModeList is empty", caps.getAutomaticStraightenModeList().isEmpty());
        assertFalse("DarknessAdjustmentList is empty", caps.getDarknessAdjustmentList().isEmpty());
        assertFalse("CropModeList is empty", caps.getCropModeList().isEmpty());
    }

    /**
     * TEST: All-Options profile — scanCaptureMode has all 4 mapped values + DEFAULT.
     */
    @Test
    public void GivenAllOptionsProfile_WhenGetCapabilitiesCalled_ThenCaptureModeListHasAllValues() throws Exception {
        ScanAttributesCaps caps = getCapabilitiesWithAllOptions(ScanAttributes.Destination.HTTP);

        List<ScanAttributes.CaptureMode> captureModes = caps.getCaptureModeList();
        assertEquals("CaptureModeList size", 5, captureModes.size());
        assertTrue("Should contain STANDARD", captureModes.contains(ScanAttributes.CaptureMode.STANDARD));
        assertTrue("Should contain STANDARD_ADD_PAGES", captureModes.contains(ScanAttributes.CaptureMode.STANDARD_ADD_PAGES));
        assertTrue("Should contain BOOK_CAPTURE", captureModes.contains(ScanAttributes.CaptureMode.BOOK_CAPTURE));
        assertTrue("Should contain ID_CAPTURE_PROMPT_BOTH_SIDES", captureModes.contains(ScanAttributes.CaptureMode.ID_CAPTURE_PROMPT_BOTH_SIDES));
        assertTrue("Should contain DEFAULT", captureModes.contains(ScanAttributes.CaptureMode.DEFAULT));
    }

    /**
     * TEST: All-Options profile — imagePreviewMode has all values.
     */
    @Test
    public void GivenAllOptionsProfile_WhenGetCapabilitiesCalled_ThenScanPreviewListHasAllValues() throws Exception {
        ScanAttributesCaps caps = getCapabilitiesWithAllOptions(ScanAttributes.Destination.HTTP);

        List<ScanAttributes.ScanPreview> previews = caps.getScanPreviewList();
        assertEquals("ScanPreviewList size", 3, previews.size());
        assertTrue("Should contain TRUE", previews.contains(ScanAttributes.ScanPreview.TRUE));
        assertTrue("Should contain FALSE", previews.contains(ScanAttributes.ScanPreview.FALSE));
        assertTrue("Should contain DEFAULT", previews.contains(ScanAttributes.ScanPreview.DEFAULT));
    }

    /**
     * TEST: All-Options profile — autoCropMode has all values (version >= SIX).
     */
    @Test
    public void GivenAllOptionsProfile_WhenGetCapabilitiesCalled_ThenCropModeListHasAllValues() throws Exception {
        ScanAttributesCaps caps = getCapabilitiesWithAllOptions(ScanAttributes.Destination.HTTP);

        List<ScanAttributes.CropMode> cropModes = caps.getCropModeList();
        assertFalse("CropModeList should not be empty", cropModes.isEmpty());
        assertTrue("Should contain OFF", cropModes.contains(ScanAttributes.CropMode.OFF));
        assertTrue("Should contain CROP_TO_PAPER", cropModes.contains(ScanAttributes.CropMode.CROP_TO_PAPER));
        assertTrue("Should contain CROP_TO_CONTENT", cropModes.contains(ScanAttributes.CropMode.CROP_TO_CONTENT));
    }

    /**
     * TEST: All-Options profile — resolution has all 9 DPI values + DEFAULT.
     */
    @Test
    public void GivenAllOptionsProfile_WhenGetCapabilitiesCalled_ThenResolutionListHasAll9Values() throws Exception {
        ScanAttributesCaps caps = getCapabilitiesWithAllOptions(ScanAttributes.Destination.HTTP);

        List<ScanAttributes.Resolution> resolutions = caps.getResolutionList();
        assertEquals("ResolutionList size", 10, resolutions.size());
        assertTrue("Should contain DPI_75", resolutions.contains(ScanAttributes.Resolution.DPI_75));
        assertTrue("Should contain DPI_100", resolutions.contains(ScanAttributes.Resolution.DPI_100));
        assertTrue("Should contain DPI_150", resolutions.contains(ScanAttributes.Resolution.DPI_150));
        assertTrue("Should contain DPI_200", resolutions.contains(ScanAttributes.Resolution.DPI_200));
        assertTrue("Should contain DPI_240", resolutions.contains(ScanAttributes.Resolution.DPI_240));
        assertTrue("Should contain DPI_300", resolutions.contains(ScanAttributes.Resolution.DPI_300));
        assertTrue("Should contain DPI_400", resolutions.contains(ScanAttributes.Resolution.DPI_400));
        assertTrue("Should contain DPI_500", resolutions.contains(ScanAttributes.Resolution.DPI_500));
        assertTrue("Should contain DPI_600", resolutions.contains(ScanAttributes.Resolution.DPI_600));
        assertTrue("Should contain DEFAULT", resolutions.contains(ScanAttributes.Resolution.DEFAULT));
    }

    /**
     * TEST: All-Options profile — outputFileFormat has all 12 formats + DEFAULT.
     */
    @Test
    public void GivenAllOptionsProfile_WhenGetCapabilitiesCalled_ThenDocumentFormatListHasAll12Formats() throws Exception {
        ScanAttributesCaps caps = getCapabilitiesWithAllOptions(ScanAttributes.Destination.HTTP);

        List<ScanAttributes.DocumentFormat> formats = caps.getDocumentFormatList(ScanAttributes.Destination.ME);
        assertTrue("DocumentFormatList should have >= 13 entries (12 + DEFAULT)", formats.size() >= 13);
        assertTrue("Should contain JPEG", formats.contains(ScanAttributes.DocumentFormat.JPEG));
        assertTrue("Should contain PDF", formats.contains(ScanAttributes.DocumentFormat.PDF));
        assertTrue("Should contain TIFF", formats.contains(ScanAttributes.DocumentFormat.TIFF));
        assertTrue("Should contain MTIFF", formats.contains(ScanAttributes.DocumentFormat.MTIFF));
        assertTrue("Should contain PDF_A", formats.contains(ScanAttributes.DocumentFormat.PDF_A));
        assertTrue("Should contain OCR_PDF_TEXT_UNDER_IMAGE", formats.contains(ScanAttributes.DocumentFormat.OCR_PDF_TEXT_UNDER_IMAGE));
        assertTrue("Should contain OCR_PDF_A_TEXT_UNDER_IMAGE", formats.contains(ScanAttributes.DocumentFormat.OCR_PDF_A_TEXT_UNDER_IMAGE));
        assertTrue("Should contain OCR_CSV", formats.contains(ScanAttributes.DocumentFormat.OCR_CSV));
        assertTrue("Should contain OCR_HTML", formats.contains(ScanAttributes.DocumentFormat.OCR_HTML));
        assertTrue("Should contain OCR_RTF", formats.contains(ScanAttributes.DocumentFormat.OCR_RTF));
        assertTrue("Should contain OCR_TEXT", formats.contains(ScanAttributes.DocumentFormat.OCR_TEXT));
        assertTrue("Should contain OCR_UNICODE_TEXT", formats.contains(ScanAttributes.DocumentFormat.OCR_UNICODE_TEXT));
    }

    /**
     * TEST: All-Options profile — mediaSize has all 56 mapped entries + DEFAULT.
     */
    @Test
    public void GivenAllOptionsProfile_WhenGetCapabilitiesCalled_ThenScanSizeListHasAllValues() throws Exception {
        ScanAttributesCaps caps = getCapabilitiesWithAllOptions(ScanAttributes.Destination.HTTP);

        List<ScanAttributes.ScanSize> sizes = caps.getScanSizeList();
        assertTrue("ScanSizeList should have >= 57 entries (56 + DEFAULT)", sizes.size() >= 57);
        assertTrue("Should contain LETTER", sizes.contains(ScanAttributes.ScanSize.LETTER));
        assertTrue("Should contain A4", sizes.contains(ScanAttributes.ScanSize.A4));
        assertTrue("Should contain A3", sizes.contains(ScanAttributes.ScanSize.A3));
        assertTrue("Should contain LEGAL", sizes.contains(ScanAttributes.ScanSize.LEGAL));
        assertTrue("Should contain AUTO", sizes.contains(ScanAttributes.ScanSize.AUTO));
        assertTrue("Should contain LEDGER", sizes.contains(ScanAttributes.ScanSize.LEDGER));
        assertTrue("Should contain BUSINESS_CARD", sizes.contains(ScanAttributes.ScanSize.BUSINESS_CARD));
        assertTrue("Should contain ENVELOPE_DL", sizes.contains(ScanAttributes.ScanSize.ENVELOPE_DL));
        assertTrue("Should contain SRA3", sizes.contains(ScanAttributes.ScanSize.SRA3));
        assertTrue("Should contain SRA4", sizes.contains(ScanAttributes.ScanSize.SRA4));
        assertTrue("Should contain DEFAULT", sizes.contains(ScanAttributes.ScanSize.DEFAULT));
    }

    /**
     * TEST: All-Options profile — contentOrientation includes AUTO_DETECT.
     */
    @Test
    public void GivenAllOptionsProfile_WhenGetCapabilitiesCalled_ThenOrientationListHasAutoDetect() throws Exception {
        ScanAttributesCaps caps = getCapabilitiesWithAllOptions(ScanAttributes.Destination.HTTP);

        List<ScanAttributes.Orientation> orientations = caps.getOrientationList();
        assertEquals("OrientationList size", 4, orientations.size());
        assertTrue("Should contain PORTRAIT", orientations.contains(ScanAttributes.Orientation.PORTRAIT));
        assertTrue("Should contain LANDSCAPE", orientations.contains(ScanAttributes.Orientation.LANDSCAPE));
        assertTrue("Should contain AUTO_DETECT", orientations.contains(ScanAttributes.Orientation.AUTO_DETECT));
        assertTrue("Should contain DEFAULT", orientations.contains(ScanAttributes.Orientation.DEFAULT));
    }

    /**
     * TEST: All-Options profile — mediaSource includes AUTO.
     */
    @Test
    public void GivenAllOptionsProfile_WhenGetCapabilitiesCalled_ThenMediaSourceListHas3Values() throws Exception {
        ScanAttributesCaps caps = getCapabilitiesWithAllOptions(ScanAttributes.Destination.HTTP);

        List<ScanAttributes.MediaSource> sources = caps.getMediaSourceList();
        assertEquals("MediaSourceList size", 4, sources.size());
        assertTrue("Should contain ADF", sources.contains(ScanAttributes.MediaSource.ADF));
        assertTrue("Should contain FLATBED", sources.contains(ScanAttributes.MediaSource.FLATBED));
        assertTrue("Should contain AUTO", sources.contains(ScanAttributes.MediaSource.AUTO));
        assertTrue("Should contain DEFAULT", sources.contains(ScanAttributes.MediaSource.DEFAULT));
    }

    /**
     * TEST: All-Options profile — fileTransmissionMode includes IMAGE mode.
     */
    @Test
    public void GivenAllOptionsProfile_WhenGetCapabilitiesCalled_ThenTransmissionModeListHasImageMode() throws Exception {
        ScanAttributesCaps caps = getCapabilitiesWithAllOptions(ScanAttributes.Destination.HTTP);

        List<ScanAttributes.TransmissionMode> modes = caps.getTransmissionModeList();
        assertTrue("Should contain IMAGE", modes.contains(ScanAttributes.TransmissionMode.IMAGE));
        assertTrue("Should contain JOB", modes.contains(ScanAttributes.TransmissionMode.JOB));
        assertTrue("Should contain DEFAULT", modes.contains(ScanAttributes.TransmissionMode.DEFAULT));
    }

    /**
     * TEST: All-Options profile — outputQualityVsSize has all 3 values.
     */
    @Test
    public void GivenAllOptionsProfile_WhenGetCapabilitiesCalled_ThenOutputQualityListHasAllValues() throws Exception {
        ScanAttributesCaps caps = getCapabilitiesWithAllOptions(ScanAttributes.Destination.HTTP);

        List<ScanAttributes.OutputQuality> qualities = caps.getOutputQualityList();
        assertEquals("OutputQualityList size", 4, qualities.size());
        assertTrue("Should contain LOW", qualities.contains(ScanAttributes.OutputQuality.LOW));
        assertTrue("Should contain MEDIUM", qualities.contains(ScanAttributes.OutputQuality.MEDIUM));
        assertTrue("Should contain HIGH", qualities.contains(ScanAttributes.OutputQuality.HIGH));
        assertTrue("Should contain DEFAULT", qualities.contains(ScanAttributes.OutputQuality.DEFAULT));
    }

    /**
     * TEST: All-Options profile — documentFormatsByColorMode is populated for multiple color modes.
     */
    @Test
    public void GivenAllOptionsProfile_WhenGetCapabilitiesCalled_ThenDocumentFormatsByColorModeIsPopulated() throws Exception {
        ScanAttributesCaps caps = getCapabilitiesWithAllOptions(ScanAttributes.Destination.HTTP);

        assertFalse("DocumentFormatsByColorMode should not be empty", caps.getDocumentFormatsByColorMode().isEmpty());
        // MONO should have fewer formats (no JPEG)
        assertTrue("MONO formats should exist", caps.getDocumentFormatsByColorMode().containsKey(ScanAttributes.ColorMode.MONO));
        assertTrue("COLOR formats should exist", caps.getDocumentFormatsByColorMode().containsKey(ScanAttributes.ColorMode.COLOR));
        // COLOR should support more formats than MONO (JPEG is color/gray only)
        int monoCount = caps.getDocumentFormatsByColorMode().get(ScanAttributes.ColorMode.MONO).size();
        int colorCount = caps.getDocumentFormatsByColorMode().get(ScanAttributes.ColorMode.COLOR).size();
        assertTrue("COLOR should have >= MONO formats count", colorCount >= monoCount);
    }

    // ========================================================================================
    // Phase 6c: Full Enum Profile — Unmapped E2 Values Filtering Tests
    // Profile now contains ALL possible E2 enum values (allPossibleValuesMap).
    // Verifies that expanding possibleValues beyond ScanTypeMapping entries does not affect
    // the adapter's output. Only mapped E2 values appear in result capability lists.
    // Note: DEFAULT is always present from Builder initialization, not from unmapped conversion.
    // ========================================================================================

    /**
     * TEST: 9 E2 colorMode values in profile, only 4 have ScanTypeMapping entries.
     * Unmapped: cmAutoColorAndBlack, cmAutoColorAndGray, cmBlackAndWhite,
     *           cmGrayscaleAutoDetect, cmTrueBlack → silently dropped (null from convertEtoW).
     * DEFAULT comes from Builder init, not from unmapped values.
     */
    @Test
    public void GivenFullEnumProfile_ColorMode_ThenUnmappedValuesDropped() throws Exception {
        ScanAttributesCaps caps = getCapabilitiesWithAllOptions(ScanAttributes.Destination.HTTP);
        // 4 mapped values + 1 DEFAULT (Builder init) = 5
        assertEquals("ColorModeList size", 5, caps.getColorModeList().size());
        assertTrue(caps.getColorModeList().contains(ScanAttributes.ColorMode.AUTO));
        assertTrue(caps.getColorModeList().contains(ScanAttributes.ColorMode.MONO));
        assertTrue(caps.getColorModeList().contains(ScanAttributes.ColorMode.GRAY));
        assertTrue(caps.getColorModeList().contains(ScanAttributes.ColorMode.COLOR));
        assertTrue(caps.getColorModeList().contains(ScanAttributes.ColorMode.DEFAULT));
    }

    /**
     * TEST: 68 E2 mediaSource values in profile, only 3 have ScanTypeMapping entries.
     * Unmapped: miAlternate, miBottom, miBypassTray, miCenter, etc. → silently dropped.
     */
    @Test
    public void GivenFullEnumProfile_MediaSource_ThenUnmappedValuesDropped() throws Exception {
        ScanAttributesCaps caps = getCapabilitiesWithAllOptions(ScanAttributes.Destination.HTTP);
        // 3 mapped values + 1 DEFAULT (Builder init) = 4
        assertEquals("MediaSourceList size", 4, caps.getMediaSourceList().size());
    }

    /**
     * TEST: 161 E2 mediaSize values in profile, only 56 have ScanTypeMapping entries.
     * Unmapped: msANSI_C, msANSI_D, msDIN_2A0, msISO_A0, etc. → silently dropped.
     */
    @Test
    public void GivenFullEnumProfile_MediaSize_ThenUnmappedValuesDropped() throws Exception {
        ScanAttributesCaps caps = getCapabilitiesWithAllOptions(ScanAttributes.Destination.HTTP);
        // 57 mapped values + 1 DEFAULT (Builder init) = 58
        assertEquals("ScanSizeList size", 58, caps.getScanSizeList().size());
    }

    /**
     * TEST: 10 E2 resolution values in profile, only 9 have ScanTypeMapping entries.
     * Unmapped: dpi1200 → silently dropped.
     */
    @Test
    public void GivenFullEnumProfile_Resolution_ThenUnmappedDpi1200Dropped() throws Exception {
        ScanAttributesCaps caps = getCapabilitiesWithAllOptions(ScanAttributes.Destination.HTTP);
        // 9 mapped values + 1 DEFAULT (Builder init) = 10
        assertEquals("ResolutionList size", 10, caps.getResolutionList().size());
    }

    /**
     * TEST: 7 E2 contentType values in profile, only 3 have ScanTypeMapping entries.
     * Unmapped: dctAutoDetect, dctGlossy, dctLineDrawing, dctPhoto → silently dropped.
     */
    @Test
    public void GivenFullEnumProfile_ContentType_ThenUnmappedValuesDropped() throws Exception {
        ScanAttributesCaps caps = getCapabilitiesWithAllOptions(ScanAttributes.Destination.HTTP);
        // 3 mapped values + 1 DEFAULT (Builder init) = 4
        assertEquals("TextPhotoOptimizationList size", 4, caps.getTextPhotoOptimizationList().size());
        assertTrue(caps.getTextPhotoOptimizationList().contains(ScanAttributes.TextPhotoOptimization.TEXT));
        assertTrue(caps.getTextPhotoOptimizationList().contains(ScanAttributes.TextPhotoOptimization.MIXED_2));
        assertTrue(caps.getTextPhotoOptimizationList().contains(ScanAttributes.TextPhotoOptimization.GRAPHIC));
        assertTrue(caps.getTextPhotoOptimizationList().contains(ScanAttributes.TextPhotoOptimization.DEFAULT));
    }

    /**
     * TEST: 6 E2 contentOrientation values in profile, only 3 have ScanTypeMapping entries.
     * Unmapped: coReverseLandscape, coReversePortrait, coUndefined → silently dropped.
     */
    @Test
    public void GivenFullEnumProfile_ContentOrientation_ThenUnmappedValuesDropped() throws Exception {
        ScanAttributesCaps caps = getCapabilitiesWithAllOptions(ScanAttributes.Destination.HTTP);
        // 3 mapped values + 1 DEFAULT (Builder init) = 4
        assertEquals("OrientationList size", 4, caps.getOrientationList().size());
    }

    /**
     * TEST: 44 E2 outputFileFormat values in profile, only 12 have ScanTypeMapping entries.
     * Unmapped: ffAuto, ffBmp, ffCals, ffCgrft, ffDoc, ffPcl, ffPng, etc. → silently dropped.
     */
    @Test
    public void GivenFullEnumProfile_OutputFileFormat_ThenUnmappedValuesDropped() throws Exception {
        ScanAttributesCaps caps = getCapabilitiesWithAllOptions(ScanAttributes.Destination.HTTP);
        // 12 mapped values + 1 DEFAULT (Builder init) = 13
        assertEquals("DocumentFormatList size", 13,
                caps.getDocumentFormatList(ScanAttributes.Destination.ME).size());
    }

    /**
     * TEST: 6 E2 outputQualityVsSize values in profile, only 3 have ScanTypeMapping entries.
     * Unmapped: qvsHighest, qvsLowest, qvsNone → silently dropped.
     */
    @Test
    public void GivenFullEnumProfile_OutputQuality_ThenUnmappedValuesDropped() throws Exception {
        ScanAttributesCaps caps = getCapabilitiesWithAllOptions(ScanAttributes.Destination.HTTP);
        // 3 mapped values + 1 DEFAULT (Builder init) = 4
        assertEquals("OutputQualityList size", 4, caps.getOutputQualityList().size());
    }

    /**
     * TEST: 3 E2 blankPageSuppression values in profile, only 2 have ScanTypeMapping entries.
     * Unmapped: bpdDetectOnly → silently dropped.
     */
    @Test
    public void GivenFullEnumProfile_BlankPageSuppression_ThenUnmappedBpdDetectOnlyDropped() throws Exception {
        ScanAttributesCaps caps = getCapabilitiesWithAllOptions(ScanAttributes.Destination.HTTP);
        // 2 mapped values + 1 DEFAULT (Builder init) = 3
        assertEquals("BlankImageRemovalModeList size", 3,
                caps.getBlankImageRemovalModeList().size());
        assertTrue(caps.getBlankImageRemovalModeList().contains(ScanAttributes.BlankImageRemovalMode.OFF));
        assertTrue(caps.getBlankImageRemovalModeList().contains(ScanAttributes.BlankImageRemovalMode.ON));
        assertTrue(caps.getBlankImageRemovalModeList().contains(ScanAttributes.BlankImageRemovalMode.DEFAULT));
    }

    /**
     * TEST: documentFormatsByColorMode should only contain mapped colorMode keys.
     * Unmapped colorMode values (cmAutoColorAndBlack, etc.) must not appear as keys.
     */
    @Test
    public void GivenFullEnumProfile_DocumentFormatsByColorMode_ThenUnmappedColorModesNotInKeys() throws Exception {
        ScanAttributesCaps caps = getCapabilitiesWithAllOptions(ScanAttributes.Destination.HTTP);

        // Only mapped colorMode values should appear as keys
        assertFalse("DocumentFormatsByColorMode should not be empty",
                caps.getDocumentFormatsByColorMode().isEmpty());
        for (ScanAttributes.ColorMode key : caps.getDocumentFormatsByColorMode().keySet()) {
            assertNotNull("DocumentFormatsByColorMode key must not be null", key);
        }
        // Mapped colorMode values: AUTO, MONO, GRAY, COLOR
        assertTrue("AUTO should be a key",
                caps.getDocumentFormatsByColorMode().containsKey(ScanAttributes.ColorMode.AUTO));
        assertTrue("COLOR should be a key",
                caps.getDocumentFormatsByColorMode().containsKey(ScanAttributes.ColorMode.COLOR));
    }
}

