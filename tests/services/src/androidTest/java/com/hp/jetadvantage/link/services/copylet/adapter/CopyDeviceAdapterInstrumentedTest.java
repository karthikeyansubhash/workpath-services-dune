package com.hp.jetadvantage.link.services.copylet.adapter;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.os.Bundle;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.hp.jetadvantage.link.BaseInstrumentedTest;
import com.hp.jetadvantage.link.api.copier.CopyAttributesCaps;
import com.hp.jetadvantage.link.api.copier.Copylet;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.utils.JsonParser;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceCopyJobService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceManagementService;
import com.hp.jetadvantage.link.services.common.exception.SdkException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CopyDeviceAdapterInstrumentedTest extends BaseInstrumentedTest {
    @Before
    public void SetUp() {
        super.SetUp();
    }

    @Test
    public void GivenCopyDeviceAdapter_WhenConstructorCalled_ThenObjectCreated() {
        CopyDeviceAdapter copyDeviceAdapter = new CopyDeviceAdapter();
        assertNotNull(copyDeviceAdapter);
    }

    @Test
    public void GivenCopyDeviceAdapter_WhenDeviceManagementServiceInitiated_AndConstructorCalled_ThenObjectCreated() {

        //initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //create copy device adapter object
        CopyDeviceAdapter copyDeviceAdapter = new CopyDeviceAdapter();
        assertNotNull(copyDeviceAdapter);
    }

    @Test
    public void GivenCopyDeviceAdapter_WhenGetCapsCalled_AfterDeviceManagementServiceInitiated_ThenCopyAttributesCapsShouldBeReturned() {
        // 1. initialize device manager service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        // 2. create copy job service object
        StandardDeviceCopyJobService copyJobService = new StandardDeviceCopyJobService();

        // 3. get copy attributes from the connected device simulator

        try {
            String resultJson = CopyOptionProfileAdapter.getCaps(testPackageName, copyJobService,
                    Sdk.VERSION_LEVEL.EIGHT);
            CopyAttributesCaps copyAttributeCaps = JsonParser.getInstance().fromJson(resultJson,
                    CopyAttributesCaps.class);

            //validate result copyAttributesCaps
            assertFalse("ColorModeList empty", copyAttributeCaps.getColorModeList().isEmpty());
            assertEquals("ColorModeList size", 4, copyAttributeCaps.getColorModeList().size());
            assertFalse("ScanDuplexList empty", copyAttributeCaps.getScanDuplexList().isEmpty());
            assertEquals("ScanDuplexList size", 4, copyAttributeCaps.getScanDuplexList().size());
            assertFalse("ScanSizeList empty", copyAttributeCaps.getScanSizeList().isEmpty());
            assertEquals("ScanSizeList size", 33, copyAttributeCaps.getScanSizeList().size());
            assertFalse("ScanSourceList empty", copyAttributeCaps.getScanSourceList().isEmpty());
            assertEquals("ScanSourceList size", 4, copyAttributeCaps.getScanSourceList().size());
            assertFalse("PrintDuplexList empty", copyAttributeCaps.getPrintDuplexList().isEmpty());
            assertEquals("PrintDuplexList size", 4, copyAttributeCaps.getPrintDuplexList().size());
            assertFalse("PrintSizeList empty", copyAttributeCaps.getPrintSizeList().isEmpty());
            assertEquals("PrintSizeList size", 42, copyAttributeCaps.getPrintSizeList().size());
            assertEquals("CopiesRange lower bound", 1, copyAttributeCaps.getCopiesRange().getLowerBound());
            assertEquals("CopiesRange upper bound", 9999, copyAttributeCaps.getCopiesRange().getUpperBound());
            assertFalse("CollateModeList empty", copyAttributeCaps.getCollateModeList().isEmpty());
            assertEquals("CollateModeList size", 3, copyAttributeCaps.getCollateModeList().size());
            assertFalse("PaperSourceList empty", copyAttributeCaps.getPaperSourceList().isEmpty());
            assertEquals("PaperSourceList size", 5, copyAttributeCaps.getPaperSourceList().size());
            assertFalse("ScaleModeList empty", copyAttributeCaps.getScaleModeList().isEmpty());
            assertEquals("ScaleModeList size", 3, copyAttributeCaps.getScaleModeList().size());
            assertFalse("TextGraphicsOptimizationList empty",
                    copyAttributeCaps.getTextGraphicsOptimizationList().isEmpty());
            assertEquals("TextGraphicsOptimizationList size", 6,
                    copyAttributeCaps.getTextGraphicsOptimizationList().size());
            assertFalse("NumberUpModeList empty", copyAttributeCaps.getNumberUpModeList().isEmpty());
            assertEquals("NumberUpModeList size", 4, copyAttributeCaps.getNumberUpModeList().size());
            assertFalse("ProgressDialogModeList empty", copyAttributeCaps.getProgressDialogModeList().isEmpty());
            assertEquals("ProgressDialogModeList size", 3, copyAttributeCaps.getProgressDialogModeList().size());
            assertFalse("OrientationList empty", copyAttributeCaps.getOrientationList().isEmpty());
            assertEquals("ScanCustomLengthRange lower bound", 0.0,
                    copyAttributeCaps.getScanCustomLengthRange().getLowerBound(), 0.1);
            assertEquals("ScanCustomLengthRange upper bound", 0.0,
                    copyAttributeCaps.getScanCustomLengthRange().getUpperBound(), 0.1);
            assertEquals("ScanCustomWidthRange lower bound", 0.0,
                    copyAttributeCaps.getScanCustomWidthRange().getLowerBound(), 0.1);
            assertEquals("ScanCustomWidthRange upper bound", 0.0,
                    copyAttributeCaps.getScanCustomWidthRange().getUpperBound(), 0.1);
            assertFalse("CopyPreviewList empty", copyAttributeCaps.getCopyPreviewList().isEmpty());
            assertFalse("BackgroundCleanupList empty", copyAttributeCaps.getBackgroundCleanupList().isEmpty());
            assertFalse("ContrastAdjustmentList empty", copyAttributeCaps.getContrastAdjustmentList().isEmpty());
            assertFalse("DarknessAdjustmentList empty", copyAttributeCaps.getDarknessAdjustmentList().isEmpty());
            assertFalse("SharpnessAdjustmentList empty", copyAttributeCaps.getSharpnessAdjustmentList().isEmpty());
            assertEquals("PrintCustomLengthRange lower bound", 0.0,
                    copyAttributeCaps.getPrintCustomLengthRange().getLowerBound(), 0.1);
            assertEquals("PrintCustomLengthRange upper bound", 0.0,
                    copyAttributeCaps.getPrintCustomLengthRange().getUpperBound(), 0.1);
            assertEquals("PrintCustomWidthRange lower bound", 0.0,
                    copyAttributeCaps.getPrintCustomWidthRange().getLowerBound(), 0.1);
            assertEquals("PrintCustomWidthRange upper bound", 0.0,
                    copyAttributeCaps.getPrintCustomWidthRange().getUpperBound(), 0.1);
            assertFalse("PaperTypeList empty", copyAttributeCaps.getPaperTypeList().isEmpty());
            assertFalse("ScalePercentRangeByScanSource empty",
                    copyAttributeCaps.getScalePercentRangeByScanSource().isEmpty());
            assertFalse("JobAssemblyModeList empty", copyAttributeCaps.getJobAssemblyModeList().isEmpty());
            assertFalse("JobExecutionModeList empty", copyAttributeCaps.getJobExecutionModeList().isEmpty());
            assertFalse("NumberUpDirectionByNumberUpCount empty",
                    copyAttributeCaps.getNumberUpDirectionByNumberUpCount().isEmpty());
            assertFalse("PasswordTypeList empty", copyAttributeCaps.getPasswordTypeList().isEmpty());
            assertEquals("PasswordTypeList size", 3, copyAttributeCaps.getPasswordTypeList().size());
            assertFalse("OutputBinList empty", copyAttributeCaps.getOutputBinList().isEmpty());
            assertFalse("EraseMarginUnitList empty", copyAttributeCaps.getEraseMarginUnitList().isEmpty());
            assertEquals("EraseBackBottomRange lower bound", 0.0,
                    copyAttributeCaps.getEraseBackBottomRange().getLowerBound(), 0.1);
            assertEquals("EraseBackBottomRange upper bound", 0.0,
                    copyAttributeCaps.getEraseBackBottomRange().getUpperBound(), 0.1);
            assertEquals("EraseBackLeftRange lower bound", 0.0,
                    copyAttributeCaps.getEraseBackLeftRange().getLowerBound(), 0.1);
            assertEquals("EraseBackLeftRange upper bound", 0.0,
                    copyAttributeCaps.getEraseBackLeftRange().getUpperBound(), 0.1);
            assertEquals("EraseBackRightRange lower bound", 0.0,
                    copyAttributeCaps.getEraseBackRightRange().getLowerBound(), 0.1);
            assertEquals("EraseBackRightRange upper bound", 0.0,
                    copyAttributeCaps.getEraseBackRightRange().getUpperBound(), 0.1);
            assertEquals("EraseBackTopRange lower bound", 0.0,
                    copyAttributeCaps.getEraseBackTopRange().getLowerBound(), 0.1);
            assertEquals("EraseBackTopRange upper bound", 0.0,
                    copyAttributeCaps.getEraseBackTopRange().getUpperBound(), 0.1);
            assertEquals("EraseFrontBottomRange lower bound", 0.0,
                    copyAttributeCaps.getEraseFrontBottomRange().getLowerBound(), 0.1);
            assertEquals("EraseFrontBottomRange upper bound", 0.0,
                    copyAttributeCaps.getEraseFrontBottomRange().getUpperBound(), 0.1);
            assertEquals("EraseFrontLeftRange lower bound", 0.0,
                    copyAttributeCaps.getEraseFrontLeftRange().getLowerBound(), 0.1);
            assertEquals("EraseFrontLeftRange upper bound", 0.0,
                    copyAttributeCaps.getEraseFrontLeftRange().getUpperBound(), 0.1);
            assertEquals("EraseFrontRightRange lower bound", 0.0,
                    copyAttributeCaps.getEraseFrontRightRange().getLowerBound(), 0.1);
            assertEquals("EraseFrontRightRange upper bound", 0.0,
                    copyAttributeCaps.getEraseFrontRightRange().getUpperBound(), 0.1);
            assertEquals("EraseFrontTopRange lower bound", 0.0,
                    copyAttributeCaps.getEraseFrontTopRange().getLowerBound(), 0.1);
            assertEquals("EraseFrontTopRange upper bound", 0.0,
                    copyAttributeCaps.getEraseFrontTopRange().getUpperBound(), 0.1);
            assertFalse("CaptureModeList empty", copyAttributeCaps.getCaptureModeList().isEmpty());
            assertFalse("ImageShiftReduceToFitList empty", copyAttributeCaps.getImageShiftReduceToFitList().isEmpty());
            assertFalse("ImageShiftUnitsList empty", copyAttributeCaps.getImageShiftUnitsList().isEmpty());
            assertEquals("ImageShiftXFrontRange lower bound", 0.0,
                    copyAttributeCaps.getImageShiftXFrontRange().getLowerBound(), 0.1);
            assertEquals("ImageShiftXFrontRange upper bound", 0.0,
                    copyAttributeCaps.getImageShiftXFrontRange().getUpperBound(), 0.1);
            assertEquals("ImageShiftYFrontRange lower bound", 0.0,
                    copyAttributeCaps.getImageShiftYFrontRange().getLowerBound(), 0.1);
            assertEquals("ImageShiftYFrontRange upper bound", 0.0,
                    copyAttributeCaps.getImageShiftYFrontRange().getUpperBound(), 0.1);
            assertEquals("ImageShiftXBackRange lower bound", 0.0,
                    copyAttributeCaps.getImageShiftXBackRange().getLowerBound(), 0.1);
            assertEquals("ImageShiftXBackRange upper bound", 0.0,
                    copyAttributeCaps.getImageShiftXBackRange().getUpperBound(), 0.1);
            assertEquals("ImageShiftYBackRange lower bound", 0.0,
                    copyAttributeCaps.getImageShiftYBackRange().getLowerBound(), 0.1);
            assertEquals("ImageShiftYBackRange upper bound", 0.0,
                    copyAttributeCaps.getImageShiftYBackRange().getUpperBound(), 0.1);
            assertFalse("BookletBordersEachPageList empty",
                    copyAttributeCaps.getBookletBordersEachPageList().isEmpty());
            assertFalse("BookletFinishingOptionList empty",
                    copyAttributeCaps.getBookletFinishingOptionList().isEmpty());
            assertFalse("BookletFormatList empty", copyAttributeCaps.getBookletFormatList().isEmpty());
            assertFalse("StapleOptionList empty", copyAttributeCaps.getStapleOptionList().isEmpty());
            assertFalse("PunchModeList empty", copyAttributeCaps.getPunchModeList().isEmpty());
            assertFalse("FoldModeList empty", copyAttributeCaps.getFoldModeList().isEmpty());
            assertFalse("StampPositionList empty", copyAttributeCaps.getStampPositionList().isEmpty());
            assertEquals("WatermarkTransparencyRange lower bound", 0,
                    copyAttributeCaps.getWatermarkTransparencyRange().getLowerBound());
            assertEquals("WatermarkTransparencyRange upper bound", 0,
                    copyAttributeCaps.getWatermarkTransparencyRange().getUpperBound());
            assertFalse("WatermarkOnlyFirstPageList empty",
                    copyAttributeCaps.getWatermarkOnlyFirstPageList().isEmpty());
            assertEquals("WatermarkDarknessRange lower bound", 1,
                    copyAttributeCaps.getWatermarkDarknessRange().getLowerBound());
            assertEquals("WatermarkDarknessRange upper bound", 5,
                    copyAttributeCaps.getWatermarkDarknessRange().getUpperBound());
            assertFalse("WatermarkRotate45List empty", copyAttributeCaps.getWatermarkRotate45List().isEmpty());
            assertFalse("WatermarkTypeList empty", copyAttributeCaps.getWatermarkTypeList().isEmpty());
            assertFalse("WatermarkMessageTypeList empty", copyAttributeCaps.getWatermarkMessageTypeList().isEmpty());
            assertFalse("WatermarkBackgroundPatternList empty",
                    copyAttributeCaps.getWatermarkBackgroundPatternList().isEmpty());
            assertTrue("WatermarkBackgroundColorList not empty",
                    copyAttributeCaps.getWatermarkBackgroundColorList().isEmpty());
            assertTrue("WatermarkFontList not empty", copyAttributeCaps.getWatermarkFontList().isEmpty());
            assertTrue("WatermarkTextColorList not empty", copyAttributeCaps.getWatermarkTextColorList().isEmpty());
            assertFalse("WatermarkTextSizeList empty", copyAttributeCaps.getWatermarkTextSizeList().isEmpty());
        } catch (SdkException e) {
            fail("Unexpected exception=" + e);
        }
    }
}
