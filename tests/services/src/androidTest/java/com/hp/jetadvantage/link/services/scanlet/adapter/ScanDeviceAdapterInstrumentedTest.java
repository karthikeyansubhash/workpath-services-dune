package com.hp.jetadvantage.link.services.scanlet.adapter;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.net.Uri;
import android.os.Bundle;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.hp.ext.service.scanJob.ScanTicket;
import com.hp.ext.service.scanJob.TransmissionMode;
import com.hp.ext.types.imaging.BindingFormat;
import com.hp.ext.types.imaging.ColorMode;
import com.hp.ext.types.imaging.CompressionType;
import com.hp.ext.types.imaging.FileFormat;
import com.hp.ext.types.imaging.PlexMode;
import com.hp.ext.types.media.MediaInputId;
import com.hp.ext.types.protocol.Unsigned32;
import com.hp.jetadvantage.link.BaseInstrumentedTest;
import com.hp.jetadvantage.link.api.scanner.FileOptionsAttributes;
import com.hp.jetadvantage.link.api.scanner.FileOptionsAttributesCaps;
import com.hp.jetadvantage.link.api.scanner.NetworkCredentialsAttributes;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes;
import com.hp.jetadvantage.link.api.scanner.ScanAttributesCaps;
import com.hp.jetadvantage.link.api.scanner.ScanAttributesReader;
import com.hp.jetadvantage.link.api.scanner.Scanlet;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.utils.JsonParser;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceScanJobService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceManagementService;
import com.hp.jetadvantage.link.services.common.exception.SdkException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ScanDeviceAdapterInstrumentedTest extends BaseInstrumentedTest {

    @Before
    public void SetUp() {
        super.SetUp();
    }

    @Test
    public void GivenScanDeviceAdapter_WhenConstructorCalled_ThenObjectCreated() {
        ScanOptionProfileAdapter scanDeviceAdapter = new ScanOptionProfileAdapter();
        assertNotNull("scanDeviceAdapter", scanDeviceAdapter);
    }

    @Test
    public void GivenScanDeviceAdapter_WhenDeviceManagementServiceInitiated_AndConstructorCalled_ThenObjectCreated() {

        //initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //create scan device adapter object
        ScanOptionProfileAdapter scanDeviceAdapter = new ScanOptionProfileAdapter();
        assertNotNull("scanDeviceAdapter", scanDeviceAdapter);
    }

    @Test
    public void GivenScanDeviceAdapter_WhenGetCapsCalled_AfterDeviceManagementServiceInitiated_ThenScanAttributesCapsShouldBeReturned() {
        // 1. initialize device manager service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        // 2. create scan job service object
        StandardDeviceScanJobService scanJobService = new StandardDeviceScanJobService();

        // 3. get scan attributes from the connected device simulator
        try {
            String resultJson = ScanOptionProfileAdapter.getCapabilities(testPackageName, scanJobService, ScanAttributes.TransmissionMode.JOB, ScanAttributes.Destination.HTTP, Sdk.VERSION.LEVEL);
            ScanAttributesCaps scanAttributeCaps = JsonParser.getInstance().fromJson(resultJson, ScanAttributesCaps.class);

            // 4. validate result scanAttributesCaps
            assertFalse("ColorModeList", scanAttributeCaps.getColorModeList().isEmpty());
            assertEquals("ColorModeList", 5, scanAttributeCaps.getColorModeList().size());
            assertFalse("DuplexList", scanAttributeCaps.getDuplexList().isEmpty());
            assertEquals("DuplexList", 4, scanAttributeCaps.getDuplexList().size());
            assertFalse("OrientationList", scanAttributeCaps.getOrientationList().isEmpty());
            assertEquals("OrientationList", 3, scanAttributeCaps.getOrientationList().size());
            // NOTE: Simulator test get 1 DestinationList (http).
            assertFalse("DestinationList", scanAttributeCaps.getDestinationList().isEmpty());
            assertEquals("DestinationList", 6, scanAttributeCaps.getDestinationList().size());
            assertFalse("DocumentFormatList", scanAttributeCaps.getDocumentFormatList(ScanAttributes.Destination.ME).isEmpty());
            assertEquals("DocumentFormatList", 6, scanAttributeCaps.getDocumentFormatList(ScanAttributes.Destination.ME).size());
            assertFalse("ScanSizeList", scanAttributeCaps.getScanSizeList().isEmpty());
            assertEquals("ScanSizeList", 24, scanAttributeCaps.getScanSizeList().size());
            assertFalse("ScanPreviewList", scanAttributeCaps.getScanPreviewList().isEmpty());
            // ScanPreview(e2 imagePreviewMode) is not available in the test profile
            assertFalse("ResolutionList", scanAttributeCaps.getResolutionList().isEmpty());
            assertEquals("ResolutionList", 7, scanAttributeCaps.getResolutionList().size());
            assertFalse("BackgroundCleanupList", scanAttributeCaps.getBackgroundCleanupList().isEmpty());
            assertEquals("BackgroundCleanupList", 10, scanAttributeCaps.getBackgroundCleanupList().size());
            assertFalse("ContrastAdjustmentList", scanAttributeCaps.getContrastAdjustmentList().isEmpty());
            assertEquals("ContrastAdjustmentList", 10, scanAttributeCaps.getContrastAdjustmentList().size());
            assertFalse("BlankImageRemovalModeList", scanAttributeCaps.getBlankImageRemovalModeList().isEmpty());
            // BlackImageRemoval(e2 blankPageSuppression) is not available in the test profile
            assertFalse("ProgressDialogModeList", scanAttributeCaps.getProgressDialogModeList().isEmpty());
            assertEquals("ProgressDialogModeList", 3, scanAttributeCaps.getProgressDialogModeList().size());
            assertFalse("OutputQualityList", scanAttributeCaps.getOutputQualityList().isEmpty());
            assertEquals("OutputQualityList", 4, scanAttributeCaps.getOutputQualityList().size());
            assertFalse("TransmissionModeList", scanAttributeCaps.getTransmissionModeList().isEmpty());
            assertEquals("TransmissionModeList", 3, scanAttributeCaps.getTransmissionModeList().size());
            assertFalse("SharpnessAdjustmentList", scanAttributeCaps.getSharpnessAdjustmentList().isEmpty());
            // SharpnessAdjustment(e2 sharpness) is not available in the test profile
            assertFalse("TextPhotoOptimizationList", scanAttributeCaps.getTextPhotoOptimizationList().isEmpty());
            assertEquals("TextPhotoOptimizationList", 4, scanAttributeCaps.getTextPhotoOptimizationList().size());
            assertFalse("MediaSourceList", scanAttributeCaps.getMediaSourceList().isEmpty());
            assertEquals("MediaSourceList", 4, scanAttributeCaps.getMediaSourceList().size());
            assertFalse("MisfeedDetectionModeList", scanAttributeCaps.getMisfeedDetectionModeList().isEmpty());
            assertEquals("MisfeedDetectionModeList", 3, scanAttributeCaps.getMisfeedDetectionModeList().size());
            assertFalse("CaptureModeList", scanAttributeCaps.getCaptureModeList().isEmpty());
            // CaptureMode(e2 scanCaptureMode) is not available in the test profile
            assertFalse("AutomaticStraightenModeList", scanAttributeCaps.getAutomaticStraightenModeList().isEmpty());
            assertEquals("AutomaticStraightenModeList", 3, scanAttributeCaps.getAutomaticStraightenModeList().size());

            //TODO : add validation when each capability option is implemented. below is just default cap list
            assertFalse("DarknessAdjustmentList", scanAttributeCaps.getDarknessAdjustmentList().isEmpty());
            assertFalse("ColorDropoutModeList", scanAttributeCaps.getColorDropoutModeList().isEmpty());
            assertFalse("CropModeList", scanAttributeCaps.getCropModeList().isEmpty());
            assertFalse("JobAssemblyModeList", scanAttributeCaps.getJobAssemblyModeList().isEmpty());
            assertFalse("MediaWeightAdjustmentList", scanAttributeCaps.getMediaWeightAdjustmentList().isEmpty());
            assertFalse("SplitAttachmentByPageList", scanAttributeCaps.getSplitAttachmentByPageList().isEmpty());
            assertFalse("EraseMarginUnitList", scanAttributeCaps.getEraseMarginUnitList().isEmpty());
        } catch (SdkException e) {
            fail("Unexpected exception=" + e);
        }
    }

    @Test
    public void GivenScanDeviceAdapter_WhenGetCapsCalledWithFtpDestination_AfterDeviceManagementServiceInitiated_ThenScanAttributesCapsShouldBeReturned() {
        // 1. initialize device manager service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        // 2. create scan job service object
        StandardDeviceScanJobService scanJobService = new StandardDeviceScanJobService();

        // 3. get scan attributes from the connected device simulator
        try {
            String resultJson = ScanOptionProfileAdapter.getCapabilities(testPackageName, scanJobService, ScanAttributes.TransmissionMode.JOB, ScanAttributes.Destination.FTP, Sdk.VERSION.LEVEL);
            ScanAttributesCaps scanAttributeCaps = JsonParser.getInstance().fromJson(resultJson, ScanAttributesCaps.class);

            // 4. validate result scanAttributesCaps - FTP should use NetworkFolder profile
            assertFalse("ColorModeList", scanAttributeCaps.getColorModeList().isEmpty());
            assertEquals("ColorModeList", 5, scanAttributeCaps.getColorModeList().size());
            assertFalse("DuplexList", scanAttributeCaps.getDuplexList().isEmpty());
            assertEquals("DuplexList", 4, scanAttributeCaps.getDuplexList().size());
            assertFalse("OrientationList", scanAttributeCaps.getOrientationList().isEmpty());
            assertEquals("OrientationList", 3, scanAttributeCaps.getOrientationList().size());
            assertFalse("DestinationList", scanAttributeCaps.getDestinationList().isEmpty());
            assertEquals("DestinationList", 6, scanAttributeCaps.getDestinationList().size());
            assertFalse("DocumentFormatList", scanAttributeCaps.getDocumentFormatList(ScanAttributes.Destination.FTP).isEmpty());
            assertEquals("DocumentFormatList", 6, scanAttributeCaps.getDocumentFormatList(ScanAttributes.Destination.FTP).size());
            assertFalse("ScanSizeList", scanAttributeCaps.getScanSizeList().isEmpty());
            assertEquals("ScanSizeList", 24, scanAttributeCaps.getScanSizeList().size());
            assertFalse("ResolutionList", scanAttributeCaps.getResolutionList().isEmpty());
            assertEquals("ResolutionList", 7, scanAttributeCaps.getResolutionList().size());
            assertFalse("BackgroundCleanupList", scanAttributeCaps.getBackgroundCleanupList().isEmpty());
            assertEquals("BackgroundCleanupList", 10, scanAttributeCaps.getBackgroundCleanupList().size());
            assertFalse("ContrastAdjustmentList", scanAttributeCaps.getContrastAdjustmentList().isEmpty());
            assertEquals("ContrastAdjustmentList", 10, scanAttributeCaps.getContrastAdjustmentList().size());
            assertFalse("ProgressDialogModeList", scanAttributeCaps.getProgressDialogModeList().isEmpty());
            assertEquals("ProgressDialogModeList", 3, scanAttributeCaps.getProgressDialogModeList().size());
            assertFalse("OutputQualityList", scanAttributeCaps.getOutputQualityList().isEmpty());
            assertEquals("OutputQualityList", 4, scanAttributeCaps.getOutputQualityList().size());
            assertFalse("TransmissionModeList", scanAttributeCaps.getTransmissionModeList().isEmpty());
            assertEquals("TransmissionModeList", 3, scanAttributeCaps.getTransmissionModeList().size());
            assertFalse("MediaSourceList", scanAttributeCaps.getMediaSourceList().isEmpty());
            assertEquals("MediaSourceList", 4, scanAttributeCaps.getMediaSourceList().size());
            assertFalse("AutomaticStraightenModeList", scanAttributeCaps.getAutomaticStraightenModeList().isEmpty());
            assertEquals("AutomaticStraightenModeList", 3, scanAttributeCaps.getAutomaticStraightenModeList().size());
        } catch (SdkException e) {
            fail("Unexpected exception=" + e);
        }
    }

    @Test
    public void GivenScanDeviceAdapter_WhenGetCapsCalledWithNetworkFolderDestination_AfterDeviceManagementServiceInitiated_ThenScanAttributesCapsShouldBeReturned() {
        // 1. initialize device manager service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        // 2. create scan job service object
        StandardDeviceScanJobService scanJobService = new StandardDeviceScanJobService();

        // 3. get scan attributes from the connected device simulator
        try {
            String resultJson = ScanOptionProfileAdapter.getCapabilities(testPackageName, scanJobService, ScanAttributes.TransmissionMode.JOB, ScanAttributes.Destination.NETWORK_FOLDER, Sdk.VERSION.LEVEL);
            ScanAttributesCaps scanAttributeCaps = JsonParser.getInstance().fromJson(resultJson, ScanAttributesCaps.class);

            // 4. validate result scanAttributesCaps - NETWORK_FOLDER should use NetworkFolder profile
            assertFalse("ColorModeList", scanAttributeCaps.getColorModeList().isEmpty());
            assertEquals("ColorModeList", 5, scanAttributeCaps.getColorModeList().size());
            assertFalse("DuplexList", scanAttributeCaps.getDuplexList().isEmpty());
            assertEquals("DuplexList", 4, scanAttributeCaps.getDuplexList().size());
            assertFalse("OrientationList", scanAttributeCaps.getOrientationList().isEmpty());
            assertEquals("OrientationList", 3, scanAttributeCaps.getOrientationList().size());
            assertFalse("DestinationList", scanAttributeCaps.getDestinationList().isEmpty());
            assertEquals("DestinationList", 6, scanAttributeCaps.getDestinationList().size());
            assertFalse("DocumentFormatList", scanAttributeCaps.getDocumentFormatList(ScanAttributes.Destination.NETWORK_FOLDER).isEmpty());
            assertEquals("DocumentFormatList", 6, scanAttributeCaps.getDocumentFormatList(ScanAttributes.Destination.NETWORK_FOLDER).size());
            assertFalse("ScanSizeList", scanAttributeCaps.getScanSizeList().isEmpty());
            assertEquals("ScanSizeList", 24, scanAttributeCaps.getScanSizeList().size());
            assertFalse("ResolutionList", scanAttributeCaps.getResolutionList().isEmpty());
            assertEquals("ResolutionList", 7, scanAttributeCaps.getResolutionList().size());
            assertFalse("BackgroundCleanupList", scanAttributeCaps.getBackgroundCleanupList().isEmpty());
            assertEquals("BackgroundCleanupList", 10, scanAttributeCaps.getBackgroundCleanupList().size());
            assertFalse("ContrastAdjustmentList", scanAttributeCaps.getContrastAdjustmentList().isEmpty());
            assertEquals("ContrastAdjustmentList", 10, scanAttributeCaps.getContrastAdjustmentList().size());
            assertFalse("ProgressDialogModeList", scanAttributeCaps.getProgressDialogModeList().isEmpty());
            assertEquals("ProgressDialogModeList", 3, scanAttributeCaps.getProgressDialogModeList().size());
            assertFalse("OutputQualityList", scanAttributeCaps.getOutputQualityList().isEmpty());
            assertEquals("OutputQualityList", 4, scanAttributeCaps.getOutputQualityList().size());
            assertFalse("TransmissionModeList", scanAttributeCaps.getTransmissionModeList().isEmpty());
            assertEquals("TransmissionModeList", 3, scanAttributeCaps.getTransmissionModeList().size());
            assertFalse("MediaSourceList", scanAttributeCaps.getMediaSourceList().isEmpty());
            assertEquals("MediaSourceList", 4, scanAttributeCaps.getMediaSourceList().size());
            assertFalse("AutomaticStraightenModeList", scanAttributeCaps.getAutomaticStraightenModeList().isEmpty());
            assertEquals("AutomaticStraightenModeList", 3, scanAttributeCaps.getAutomaticStraightenModeList().size());
        } catch (SdkException e) {
            fail("Unexpected exception=" + e);
        }
    }

    @Test
    public void GivenScanDeviceAdapter_WhenGetCapsCalledWithImageModeAndFtpDestination_ThenFtpShouldNotBeInDestinationList() {
        // 1. initialize device manager service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        // 2. create scan job service object
        StandardDeviceScanJobService scanJobService = new StandardDeviceScanJobService();

        // 3. get scan attributes from the connected device simulator with IMAGE mode and FTP destination
        try {
            String resultJson = ScanOptionProfileAdapter.getCapabilities(testPackageName, scanJobService, ScanAttributes.TransmissionMode.IMAGE, ScanAttributes.Destination.FTP, Sdk.VERSION.LEVEL);
            ScanAttributesCaps scanAttributeCaps = JsonParser.getInstance().fromJson(resultJson, ScanAttributesCaps.class);

            // 4. validate result - IMAGE mode should not support NetworkFolder/FTP destinations
            assertFalse("DestinationList is empty", scanAttributeCaps.getDestinationList().isEmpty());
            assertEquals("DestinationList should not include FTP/NETWORK_FOLDER in IMAGE mode", 4, scanAttributeCaps.getDestinationList().size());
            assertFalse("FTP should not be in destination list for IMAGE mode",
                    scanAttributeCaps.getDestinationList().contains(ScanAttributes.Destination.FTP));
            assertFalse("NETWORK_FOLDER should not be in destination list for IMAGE mode",
                    scanAttributeCaps.getDestinationList().contains(ScanAttributes.Destination.NETWORK_FOLDER));
        } catch (SdkException e) {
            fail("Unexpected exception=" + e);
        }
    }

    @Test
    public void GivenScanDeviceAdapter_WhenGetCapsCalledWithImageModeAndNetworkFolderDestination_ThenNetworkFolderShouldNotBeInDestinationList() {
        // 1. initialize device manager service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        // 2. create scan job service object
        StandardDeviceScanJobService scanJobService = new StandardDeviceScanJobService();

        // 3. get scan attributes from the connected device simulator with IMAGE mode and NETWORK_FOLDER destination
        try {
            String resultJson = ScanOptionProfileAdapter.getCapabilities(testPackageName, scanJobService, ScanAttributes.TransmissionMode.IMAGE, ScanAttributes.Destination.NETWORK_FOLDER, Sdk.VERSION.LEVEL);
            ScanAttributesCaps scanAttributeCaps = JsonParser.getInstance().fromJson(resultJson, ScanAttributesCaps.class);

            // 4. validate result - IMAGE mode should not support NetworkFolder/FTP destinations
            assertFalse("DestinationList is empty", scanAttributeCaps.getDestinationList().isEmpty());
            assertEquals("DestinationList should not include FTP/NETWORK_FOLDER in IMAGE mode", 4, scanAttributeCaps.getDestinationList().size());
            assertFalse("FTP should not be in destination list for IMAGE mode",
                    scanAttributeCaps.getDestinationList().contains(ScanAttributes.Destination.FTP));
            assertFalse("NETWORK_FOLDER should not be in destination list for IMAGE mode",
                    scanAttributeCaps.getDestinationList().contains(ScanAttributes.Destination.NETWORK_FOLDER));
        } catch (SdkException e) {
            fail("Unexpected exception=" + e);
        }
    }

    @Test
    public void GivenScanDeviceAdapter_WhenGetDefaultsCalled_AfterDeviceManagementServiceInitiated_ThenDefaultScanAttributesShouldBeReturned() {
        // 1. initialize device manager service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        // 2. create scan job service object
        StandardDeviceScanJobService scanJobService = new StandardDeviceScanJobService();

        // 3. get scan attributes from the connected device simulator

        try {
            String resultJson = ScanDefaultOptionAdapter.getDefaults(testPackageName, scanJobService, ScanAttributes.Destination.HTTP, Sdk.VERSION.LEVEL);
            ScanAttributes scanAttributes = JsonParser.getInstance().fromJson(resultJson, ScanAttributes.class);

            // 4. validate result scanAttributes
            assertNotNull("scanAttributes", scanAttributes);
            ScanAttributesReader reader = new ScanAttributesReader(scanAttributes);
            assertEquals("ColorMode", ScanAttributes.ColorMode.COLOR, reader.getColorMode());
            assertEquals("Duplex", ScanAttributes.Duplex.NONE, reader.getPlex());
            assertEquals("Orientation", ScanAttributes.Orientation.PORTRAIT, reader.getOrientation());
            assertEquals("Destination", ScanAttributes.Destination.ME, reader.getDestination());
            assertEquals("DocumentFormat", ScanAttributes.DocumentFormat.PDF, reader.getDocumentFormat());
            assertEquals("ScanSize", ScanAttributes.ScanSize.LETTER, reader.getScanSize());
            assertEquals("ScanPreview", ScanAttributes.ScanPreview.DEFAULT, reader.getScanPreview()); //This option is not available on sample test data.
            assertEquals("Resolution", ScanAttributes.Resolution.DPI_300, reader.getResolution());
            assertEquals("BackgroundCleanup", ScanAttributes.BackgroundCleanup.LEVEL_2, reader.getBackgroundCleanup());
            assertEquals("ContrastAdjustment", ScanAttributes.ContrastAdjustment.LEVEL_1, reader.getContrastAdjustment());
            assertEquals("BlankImageRemovalMode", ScanAttributes.BlankImageRemovalMode.OFF, reader.getBlankImageRemovalMode()); //This option is not
            // available on sample test data.
            assertEquals("ProgressDialogMode", ScanAttributes.ProgressDialogMode.ON, reader.getProgressDialogMode());
            assertEquals("OutputQuality", ScanAttributes.OutputQuality.MEDIUM, reader.getOutputQuality());
            assertEquals("TransmissionMode", ScanAttributes.TransmissionMode.JOB, reader.getTransmissionMode());
            assertEquals("SharpnessAdjustment", ScanAttributes.SharpnessAdjustment.LEVEL_1, reader.getSharpnessAdjustment()); //This option is not available
            // on sample test data.
            assertEquals("TextPhotoOptimization", ScanAttributes.TextPhotoOptimization.MIXED_2, reader.getTextPhotoOptimization()); //The E2 option value
            // dctMixed is not matched with workpath type.
            assertEquals("MediaSource", ScanAttributes.MediaSource.ADF, reader.getMediaSource());
            assertEquals("MisfeedDetectionMode", ScanAttributes.MisfeedDetectionMode.OFF, reader.getMisfeedDetectionMode());
            assertEquals("CaptureMode", ScanAttributes.CaptureMode.STANDARD, reader.getCaptureMode()); //This option is not
            // available on sample test data.
            assertEquals("AutomaticStraightenMode", ScanAttributes.AutomaticStraightenMode.DISABLE, reader.getAutomaticStraightenMode());
        } catch (SdkException e) {
            fail("Unexpected exception=" + e);
        }
    }

    @Test
    public void GivenScanDeviceAdapter_WhenGetFileOptionsCalled_AfterDeviceManagementServiceInitiated_ThenFileOptionsAttributesCapsShouldBeReturned() {
        // 1. initialize device manager service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        // 2. create scan job service object
        StandardDeviceScanJobService scanJobService = new StandardDeviceScanJobService();

        // 3. get scan attributes from the connected device simulator

        try {
            String resultJsonStr = ScanFileOptionAdapter.getFileOptions(testPackageName, scanJobService, ScanAttributes.ColorMode.COLOR,
                    ScanAttributes.DocumentFormat.PDF_A);
            FileOptionsAttributesCaps fileOptionsAttributesCaps = JsonParser.getInstance().fromJson(resultJsonStr, FileOptionsAttributesCaps.class);

            //4. Validate result ScanAttributes
            assertFalse("OcrLanguageList", fileOptionsAttributesCaps.getOcrLanguageList().isEmpty());
            assertEquals("OcrLanguageList", 1, fileOptionsAttributesCaps.getOcrLanguageList().size());
            assertFalse("PdfCompressionModeList", fileOptionsAttributesCaps.getPdfCompressionModeList().isEmpty());
            assertEquals("PdfCompressionModeList", 3, fileOptionsAttributesCaps.getPdfCompressionModeList().size());
            assertFalse("TiffCompressionModeList", fileOptionsAttributesCaps.getTiffCompressionModeList().isEmpty());
            assertEquals("TiffCompressionModeList", 1, fileOptionsAttributesCaps.getTiffCompressionModeList().size());
            assertFalse("XpsCompressionModeList", fileOptionsAttributesCaps.getXpsCompressionModeList().isEmpty());
            assertEquals("XpsCompressionModeList", 1, fileOptionsAttributesCaps.getXpsCompressionModeList().size());
            assertFalse("PdfEncryptionPasswordSupported", fileOptionsAttributesCaps.isPdfEncryptionPasswordSupported());

            //3. Call the target device adapter method: getFileOptions
            resultJsonStr = ScanFileOptionAdapter.getFileOptions(testPackageName, scanJobService, ScanAttributes.ColorMode.COLOR,
                    ScanAttributes.DocumentFormat.PDF);
            fileOptionsAttributesCaps = JsonParser.getInstance().fromJson(resultJsonStr, FileOptionsAttributesCaps.class);

            //4. Validate result ScanAttributes
            assertFalse("OcrLanguageList", fileOptionsAttributesCaps.getOcrLanguageList().isEmpty());
            assertEquals("OcrLanguageList", 1, fileOptionsAttributesCaps.getOcrLanguageList().size());
            assertFalse("PdfCompressionModeList", fileOptionsAttributesCaps.getPdfCompressionModeList().isEmpty());
            assertEquals("PdfCompressionModeList", 1, fileOptionsAttributesCaps.getPdfCompressionModeList().size());
            assertFalse("TiffCompressionModeList", fileOptionsAttributesCaps.getTiffCompressionModeList().isEmpty());
            assertEquals("TiffCompressionModeList", 1, fileOptionsAttributesCaps.getTiffCompressionModeList().size());
            assertFalse("XpsCompressionModeList", fileOptionsAttributesCaps.getXpsCompressionModeList().isEmpty());
            assertEquals("XpsCompressionModeList", 1, fileOptionsAttributesCaps.getXpsCompressionModeList().size());
            assertTrue("PdfEncryptionPasswordSupported", fileOptionsAttributesCaps.isPdfEncryptionPasswordSupported());

            //3. Call the target device adapter method: getFileOptions
            resultJsonStr = ScanFileOptionAdapter.getFileOptions(testPackageName, scanJobService, ScanAttributes.ColorMode.COLOR,
                    ScanAttributes.DocumentFormat.TIFF);
            fileOptionsAttributesCaps = JsonParser.getInstance().fromJson(resultJsonStr, FileOptionsAttributesCaps.class);

            //4. Validate result ScanAttributes
            assertFalse("OcrLanguageList", fileOptionsAttributesCaps.getOcrLanguageList().isEmpty());
            assertEquals("OcrLanguageList", 1, fileOptionsAttributesCaps.getOcrLanguageList().size());
            assertFalse("PdfCompressionModeList", fileOptionsAttributesCaps.getPdfCompressionModeList().isEmpty());
            assertEquals("PdfCompressionModeList", 1, fileOptionsAttributesCaps.getPdfCompressionModeList().size());
            assertFalse("TiffCompressionModeList", fileOptionsAttributesCaps.getTiffCompressionModeList().isEmpty());
            assertEquals("TiffCompressionModeList", 3, fileOptionsAttributesCaps.getTiffCompressionModeList().size());
            assertFalse("XpsCompressionModeList", fileOptionsAttributesCaps.getXpsCompressionModeList().isEmpty());
            assertEquals("XpsCompressionModeList", 1, fileOptionsAttributesCaps.getXpsCompressionModeList().size());
            assertFalse("PdfEncryptionPasswordSupported", fileOptionsAttributesCaps.isPdfEncryptionPasswordSupported());

            //3. Call the target device adapter method: getFileOptions
            resultJsonStr = ScanFileOptionAdapter.getFileOptions(testPackageName, scanJobService, ScanAttributes.ColorMode.MONO,
                    ScanAttributes.DocumentFormat.TIFF);
            fileOptionsAttributesCaps = JsonParser.getInstance().fromJson(resultJsonStr, FileOptionsAttributesCaps.class);

            //4. Validate result ScanAttributes
            assertFalse("OcrLanguageList", fileOptionsAttributesCaps.getOcrLanguageList().isEmpty());
            assertEquals("OcrLanguageList", 1, fileOptionsAttributesCaps.getOcrLanguageList().size());
            assertFalse("PdfCompressionModeList", fileOptionsAttributesCaps.getPdfCompressionModeList().isEmpty());
            assertEquals("PdfCompressionModeList", 1, fileOptionsAttributesCaps.getPdfCompressionModeList().size());
            assertFalse("TiffCompressionModeList", fileOptionsAttributesCaps.getTiffCompressionModeList().isEmpty());
            assertEquals("TiffCompressionModeList", 3, fileOptionsAttributesCaps.getTiffCompressionModeList().size());
            assertFalse("XpsCompressionModeList", fileOptionsAttributesCaps.getXpsCompressionModeList().isEmpty());
            assertEquals("XpsCompressionModeList", 1, fileOptionsAttributesCaps.getXpsCompressionModeList().size());
            assertFalse("PdfEncryptionPasswordSupported", fileOptionsAttributesCaps.isPdfEncryptionPasswordSupported());
        } catch (SdkException e) {
            fail("Unexpected exception=" + e);
        }
    }

    @Test
    public void GivenScanDeviceAdapter_WhenGetScanTicketCalled_WithDestinationHttp_AfterDeviceManagementServiceInitiated_ThenValidScanTicketShouldBeReturned() {
        // 1. initialize device manager service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        // 2. create scan job service object
        StandardDeviceScanJobService scanJobService = new StandardDeviceScanJobService();

        // 3. get scan attributes from the connected device simulator
        Bundle bundle = new Bundle();
        bundle.putInt(Scanlet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION_LEVEL.EIGHT);

        try {
            //4. Generate testScanAttributes
            String resultJson = ScanOptionProfileAdapter.getCapabilities(testPackageName, scanJobService, ScanAttributes.TransmissionMode.JOB, ScanAttributes.Destination.HTTP, Sdk.VERSION_LEVEL.EIGHT);
            ScanAttributesCaps scanAttributeCaps = JsonParser.getInstance().fromJson(resultJson, ScanAttributesCaps.class);

            String resultJsonStr = ScanFileOptionAdapter.getFileOptions(testPackageName, scanJobService, ScanAttributes.ColorMode.COLOR,
                    ScanAttributes.DocumentFormat.TIFF);
            FileOptionsAttributesCaps fileOptionsAttributesCaps = JsonParser.getInstance().fromJson(resultJsonStr, FileOptionsAttributesCaps.class);

            ScanAttributes.HttpBuilder scanAttributeHttpBuilder = new ScanAttributes.HttpBuilder(Uri.parse("https://test.com:8080/hp/scan"));
            NetworkCredentialsAttributes.Builder networkCredentialsAttributesBuilder = new NetworkCredentialsAttributes.Builder();
            networkCredentialsAttributesBuilder.setUserName("admin");
            networkCredentialsAttributesBuilder.setPassword("password");
            scanAttributeHttpBuilder.setNetworkCredentials(networkCredentialsAttributesBuilder.build());
            scanAttributeHttpBuilder.setColorMode(ScanAttributes.ColorMode.COLOR);
            scanAttributeHttpBuilder.setDocumentFormat(ScanAttributes.DocumentFormat.TIFF);
            scanAttributeHttpBuilder.setDuplex(ScanAttributes.Duplex.BOOK);
            scanAttributeHttpBuilder.setBackgroundCleanup(ScanAttributes.BackgroundCleanup.LEVEL_1);
            scanAttributeHttpBuilder.setContrastAdjustment(ScanAttributes.ContrastAdjustment.LEVEL_1);
            scanAttributeHttpBuilder.setTransmissionMode(ScanAttributes.TransmissionMode.IMAGE);
            scanAttributeHttpBuilder.setMediaSource(ScanAttributes.MediaSource.ADF);
            scanAttributeHttpBuilder.setFileName("test");
            FileOptionsAttributes.Builder fileOptionsAttributesBuilder = new FileOptionsAttributes.Builder();
            fileOptionsAttributesBuilder.setTiffCompressionMode(FileOptionsAttributes.TiffCompressionMode.JPEG_TIFF_6);
            scanAttributeHttpBuilder.setFileOptionsAttributes(fileOptionsAttributesBuilder.build(fileOptionsAttributesCaps));
            ScanAttributesReader scanAttributesReader = new ScanAttributesReader(scanAttributeHttpBuilder.build(scanAttributeCaps));

            ScanAttributesReader defaultScanAttributes = ScanTicketAdapter.getDefaultScanAttributes(testPackageName, scanJobService,
                    scanAttributesReader.getDestination(), true, Sdk.VERSION_LEVEL.EIGHT);
            //5. Call the target device adapter method: getScanTicket
            ScanTicket scanTicket = ScanTicketAdapter.getScanTicket(scanAttributesReader, defaultScanAttributes);

            //6. Validate result ScanAttributes
            assertTrue("Http", scanTicket.getDestinationOptions().isHttp());
            assertEquals("Username", "admin",
                    scanTicket.getDestinationOptions().getHttp().getDestination().getAuthorization().getBasic().getUsername().getExplicit().getExplicitValue());
            assertEquals("Password", "password",
                    scanTicket.getDestinationOptions().getHttp().getDestination().getAuthorization().getBasic().getPassword().getExplicit().getExplicitValue());
            assertEquals("Host", "test.com:8080",
                    scanTicket.getDestinationOptions().getHttp().getDestination().getHost().getExplicit().getExplicitValue().getValue());
            assertEquals("Path", "/hp/scan",
                    scanTicket.getDestinationOptions().getHttp().getDestination().getPath().getExplicit().getExplicitValue().getValue());
            assertEquals("Scheme", "https", scanTicket.getDestinationOptions().getHttp().getDestination().getScheme());
            assertEquals("MaxRetries", 0,
                    scanTicket.getDestinationOptions().getHttp().getDestination().getRetry().getBasic().getMaxRetries().getExplicit().getExplicitValue().intValue());
            assertEquals("TimeoutSeconds", 60,
                    scanTicket.getDestinationOptions().getHttp().getDestination().getRetry().getBasic().getTimeoutSeconds().getExplicit().getExplicitValue().intValue());
            assertNull("MetadataOptions", scanTicket.getMetadataOptions());
            assertEquals("ColorMode", ColorMode.CmColor, scanTicket.getScanOptions().getColorMode());
            assertEquals("OutputFileFormat", FileFormat.FfTiff, scanTicket.getScanOptions().getOutputFileFormat());
            assertEquals("PlexMode", PlexMode.PmDuplex, scanTicket.getScanOptions().getPlexMode());
            assertEquals("MediaBindingFormat", BindingFormat.BfFlipLeft, scanTicket.getScanOptions().getMediaBindingFormat());
            assertEquals("BackgroundCleanup", new Unsigned32(1L).getValue(), scanTicket.getScanOptions().getBackgroundCleanup().getValue());
            assertEquals("Contrast", new Unsigned32(1L).getValue(), scanTicket.getScanOptions().getContrast().getValue());
            assertEquals("FileTransmissionMode", TransmissionMode.TmImage, scanTicket.getScanOptions().getFileTransmissionMode());
            assertEquals("MediaSource", MediaInputId.MiAdf, scanTicket.getScanOptions().getMediaSource());
            assertEquals("FileName", "test", scanTicket.getScanOptions().getFileName().getExplicit().getExplicitValue());
            assertEquals("ColorAndGrayCompression", CompressionType.CtOJpeg, scanTicket.getScanOptions().getColorAndGrayCompression());
        } catch (Exception e) {
            fail("Unexpected exception=" + e);
        }
    }
}
