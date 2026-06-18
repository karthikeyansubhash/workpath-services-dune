// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.copier;

import com.hp.jetadvantage.link.api.copier.CopyAttributes.ColorMode;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.Duplex;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.Orientation;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.ScanSize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.hp.jetadvantage.link.api.copier.CopyAttributes.*;

/**
 * Copy attributes capabilities provided from the printer.
 *
 * @since API 3
 */
public class CopyAttributesCaps {
    final CopyAttributesCapsCreator mCapsCreator;

    /**
     * Constructs this object from the CopyAttributesCapsCreator object
     *
     * @param creator object containing the copy capabilities
     * @hide The creator is hidden
     * @since API 3
     */
    public CopyAttributesCaps(final CopyAttributesCapsCreator creator) {
        mCapsCreator = creator;
    }

    /**
     * Indicates the groups of color sets which the scanned output will reside within. For example, a monospace set indicates that the scanned output
     * only includes black, white, and various shades of gray.
     *
     * @return the color modes supported.
     * @since API 3
     */
    public List<ColorMode> getColorModeList() {
        return Collections.unmodifiableList(mCapsCreator.mColorModeList);
    }

    /**
     * Returns the supported original orientation options by the current printer
     *
     * @return supported {@link Orientation} list
     * @since API 3
     */
    public List<Orientation> getOrientationList() {
        return Collections.unmodifiableList(mCapsCreator.mOrientationList);
    }

    /**
     * Returns scan duplex options supported by the current printer
     *
     * @return List of duplex options.
     * @since API 3
     */
    public List<Duplex> getScanDuplexList() {
        return Collections.unmodifiableList(mCapsCreator.mScanDuplexList);
    }

    /**
     * A list of scan sizes supported by the current printer
     *
     * @return scan sizes
     * @since API 3
     */
    public List<ScanSize> getScanSizeList() {
        return Collections.unmodifiableList(mCapsCreator.mScanSizeList);
    }

    /**
     * A range for scan size custom length.
     *
     * @return range for custom length.
     * @since API 3
     */
    public FloatRange getScanCustomLengthRange() {
        return mCapsCreator.mScanCustomLengthRange;
    }

    /**
     * A range for scan size custom width.
     *
     * @return range for custom width.
     * @since API 3
     */
    public FloatRange getScanCustomWidthRange() {
        return mCapsCreator.mScanCustomWidthRange;
    }

    /**
     * A list of scan sources supported by the current printer
     *
     * @return scan sources
     * @since API 3
     */
    public List<ScanSource> getScanSourceList() {
        return Collections.unmodifiableList(mCapsCreator.mScanSourceList);
    }

    /**
     * Returns the supported copy preview options by the current printer
     *
     * @return supported {@link CopyPreview} list
     * @since API 3
     */
    public List<CopyPreview> getCopyPreviewList() {
        return Collections.unmodifiableList(mCapsCreator.mCopyPreviewList);
    }

    /**
     * Returns the supported scan background cleanup options by the current printer
     *
     * @return supported {@link BackgroundCleanup} list
     * @since API 3
     */
    public List<BackgroundCleanup> getBackgroundCleanupList() {
        return Collections.unmodifiableList(mCapsCreator.mBackgroundCleanupList);
    }

    /**
     * Returns the supported scan contrast adjustment options by the current printer
     *
     * @return supported {@link ContrastAdjustment} list
     * @since API 3
     */
    public List<ContrastAdjustment> getContrastAdjustmentList() {
        return Collections.unmodifiableList(mCapsCreator.mContrastAdjustmentList);
    }

    /**
     * Returns the supported scan darkness adjustment options by the current printer
     *
     * @return supported {@link DarknessAdjustment} list
     * @since API 3
     */
    public List<DarknessAdjustment> getDarknessAdjustmentList() {
        return Collections.unmodifiableList(mCapsCreator.mDarknessAdjustmentList);
    }

    /**
     * Returns the supported scan sharpness adjustment options by the current printer
     *
     * @return supported {@link DarknessAdjustment} list
     * @since API 3
     */
    public List<SharpnessAdjustment> getSharpnessAdjustmentList() {
        return Collections.unmodifiableList(mCapsCreator.mSharpnessAdjustmentList);
    }

    /**
     * Returns print duplex options supported by the current printer
     *
     * @return List of duplex options.
     * @since API 3
     */
    public List<Duplex> getPrintDuplexList() {
        return Collections.unmodifiableList(mCapsCreator.mPrintDuplexList);
    }

    /**
     * A list of print sizes supported by the current printer
     *
     * @return print sizes
     * @since API 3
     */
    public List<PaperSize> getPrintSizeList() {
        return Collections.unmodifiableList(mCapsCreator.mPrintSizeList);
    }

    /**
     * A range for print size custom length.
     *
     * @return range for custom length.
     * @since API 3
     */
    public FloatRange getPrintCustomLengthRange() {
        return mCapsCreator.mPrintCustomLengthRange;
    }

    /**
     * A range for print size custom width.
     *
     * @return range for custom width.
     * @since API 3
     */
    public FloatRange getPrintCustomWidthRange() {
        return mCapsCreator.mPrintCustomWidthRange;
    }

    /**
     * Returns copies range supported by the current printer
     *
     * @return List of duplex options.
     * @since API 3
     */
    public Range getCopiesRange() {
        return mCapsCreator.mCopiesRange;
    }

    /**
     * Return collate options supported on the current printer.
     *
     * @return List of Collate options.
     * @remarks An empty list indicates that the option is not supported by the printer.
     * @since API 3
     */
    public List<CollateMode> getCollateModeList() {
        return Collections.unmodifiableList(mCapsCreator.mCollateModeList);
    }

    /**
     * Return paper sources supported on the current printer.
     *
     * @return List of supported paper sources.
     * @remarks An empty list indicates that the option is not supported by the printer.
     * @since API 3
     */
    public List<PaperSource> getPaperSourceList() {
        return Collections.unmodifiableList(mCapsCreator.mPaperSourceList);
    }

    /**
     * Return paper types supported on the current device.
     *
     * @return List of paper types.
     * @remarks An empty list indicates that the option is not supported by the printer.
     * @since API 3
     */
    public List<PaperType> getPaperTypeList() {
        return Collections.unmodifiableList(mCapsCreator.mPaperTypeList);
    }

    /**
     * Return scale modes supported on the current printer.
     *
     * @return List of scale modes.
     * @remarks An empty list indicates that the option is not supported by the printer.
     * @since API 3
     */
    public List<ScaleMode> getScaleModeList() {
        return Collections.unmodifiableList(mCapsCreator.mScaleModeList);
    }

    /**
     * Returns scale percent range supported by the current printer
     *
     * @return List of duplex options.
     * @since API 3
     */
    public Map<ScanSource, Range> getScalePercentRangeByScanSource() {
        return mCapsCreator.mScalePercentRangeMap;
    }

    /**
     * Gets the supported text/photo optimization options by the device
     *
     * @return List of text/graphics optimization options
     * @since API 3
     */
    public List<TextGraphicsOptimization> getTextGraphicsOptimizationList() {
        return Collections.unmodifiableList(mCapsCreator.mTextGraphicsOptimizationList);
    }

    /**
     * Gets the supported job assembly modes by the device
     *
     * @return List of job assembly modes.
     * @since API 3
     */
    public List<JobAssemblyMode> getJobAssemblyModeList() {
        return Collections.unmodifiableList(mCapsCreator.mJobAssemblyModeList);
    }

    /**
     * Returns job execution modes supported on the current printer.
     *
     * @return List of job execution modes.
     * @since API 3
     */
    public List<JobExecutionMode> getJobExecutionModeList() {
        return Collections.unmodifiableList(mCapsCreator.mJobExecutionModeList);
    }

    /**
     * Return number up modes supported on the current printer.
     *
     * @return List of number up modes.
     * @remarks An empty list indicates that the option is not supported by the printer.
     * @since API 3
     */
    public List<NumberUpMode> getNumberUpModeList() {
        return Collections.unmodifiableList(mCapsCreator.mNumberUpModeList);
    }

    /**
     * Return number up directions supported on the current printer.
     *
     * @return List of number up direction.
     * @remarks An empty list indicates that the option is not supported by the printer.
     * @since API 3
     */
    public Map<NumberUpMode, List<NumberUpDirection>> getNumberUpDirectionByNumberUpCount() {
        return Collections.unmodifiableMap(mCapsCreator.mNumberUpDirectionMap);
    }


    /**
     * Return password types supported for protecting stored job on the current printer.
     *
     * @return List of password types
     * @remarks An empty list indicates that the option is not supported by the printer.
     * @since API 3
     */
    public List<JobCredentialsAttributes.PasswordType> getPasswordTypeList() {
        return Collections.unmodifiableList(mCapsCreator.mPasswordTypeList);
    }

    /**
     * Return output bins supported on the current printer.
     *
     * @return List of output bin types
     * @remarks An empty list indicates that the option is not supported by the printer.
     * @since API 5
     */
    public List<OutputBin> getOutputBinList() {
        return Collections.unmodifiableList(mCapsCreator.mOutputBinList);
    }

    /**
     * Return progress dialog modes supported on the current printer.
     *
     * @return List of progress dialog mode types
     * @remarks An empty list indicates that the option is not supported by the printer.
     * @since API 5
     */
    public List<ProgressDialogMode> getProgressDialogModeList() {
        return Collections.unmodifiableList(mCapsCreator.mProgressDialogModeList);
    }

    /**
     * Gets the supported scan EraseMarginUnits by the device
     *
     * @return supported {@link EraseMarginUnit} list
     * @since API 5
     */
    public List<EraseMarginUnit> getEraseMarginUnitList() {
        if (mCapsCreator.mEraseMarginUnitList == null) {
            return new ArrayList<>();
        }
        return Collections.unmodifiableList(mCapsCreator.mEraseMarginUnitList);
    }

    /**
     * Gets a range of EraseBackBottomRange.
     *
     * @return range for EraseBackBottomRange
     * @since API 5
     */
    public FloatRange getEraseBackBottomRange() {
        return mCapsCreator.mEraseBackBottomRange;
    }

    /**
     * Gets a range of EraseBackLeftRange.
     *
     * @return range for EraseBackLeftRange
     * @since API 5
     */
    public FloatRange getEraseBackLeftRange() {
        return mCapsCreator.mEraseBackLeftRange;
    }

    /**
     * Gets a range of EraseBackRightRange
     *
     * @return range for EraseBackRightRange
     * @since API 5
     */
    public FloatRange getEraseBackRightRange() {
        return mCapsCreator.mEraseBackRightRange;
    }

    /**
     * Gets a range of EraseBackTopRange
     *
     * @return range for EraseBackTopRange
     * @since API 5
     */
    public FloatRange getEraseBackTopRange() {
        return mCapsCreator.mEraseBackTopRange;
    }

    /**
     * Gets a range of EraseFrontBottomRange
     *
     * @return range for EraseFrontBottomRange
     * @since API 5
     */
    public FloatRange getEraseFrontBottomRange() {
        return mCapsCreator.mEraseFrontBottomRange;
    }

    /**
     * Gets a range of EraseFrontLeftRange
     *
     * @return range for EraseFrontLeftRange
     * @since API 5
     */
    public FloatRange getEraseFrontLeftRange() {
        return mCapsCreator.mEraseFrontLeftRange;
    }

    /**
     * Gets a range of EraseFrontRightRange
     *
     * @return range for EraseFrontRightRange
     * @since API 5
     */
    public FloatRange getEraseFrontRightRange() {
        return mCapsCreator.mEraseFrontRightRange;
    }

    /**
     * Gets a range of EraseFrontTopRange
     *
     * @return range for EraseFrontTopRange
     * @since API 5
     */
    public FloatRange getEraseFrontTopRange() {
        return mCapsCreator.mEraseFrontTopRange;
    }

    /** Returns the supported capture mode options by the current printer
     *
     * @return supported levels of capture mode settings
     * @since API 6
     */
    public List<CaptureMode> getCaptureModeList() {
        if (mCapsCreator.mCaptureModeList == null) {
            return new ArrayList<>();
        }
        return Collections.unmodifiableList(mCapsCreator.mCaptureModeList);
    }

    /**
     * Return ImageShift Reduce To Fit supported on the current printer.
     *
     * @return List of ImageShiftReduceToFit type
     * @remarks An empty list indicates that the option is not supported by the printer.
     * @since API 5
     */
    public List<ImageShiftReduceToFit> getImageShiftReduceToFitList() {
        return Collections.unmodifiableList(mCapsCreator.mImageShiftReduceToFitList);
    }

    /**
     * Return Image Shift Units supported on the current printer.
     *
     * @return List of Image Shift Units types
     * @remarks An empty list indicates that the option is not supported by the printer.
     * @since API 5
     */
    public List<ImageShiftUnits> getImageShiftUnitsList() {
        return Collections.unmodifiableList(mCapsCreator.mImageShiftUnitsList);
    }

    /**
     * Gets a range of ImageShiftXFrontRange
     *
     * @return range for ImageShiftXFrontRange
     * @since API 5
     */
    public FloatRange getImageShiftXFrontRange() { return mCapsCreator.mImageShiftXFrontRange; }

    /**
     * Gets a range of ImageShiftYFrontRange
     *
     * @return range for ImageShiftYFrontRange
     * @since API 5
     */
    public FloatRange getImageShiftYFrontRange() { return mCapsCreator.mImageShiftYFrontRange; }

    /**
     * Gets a range of ImageShiftXBackRange
     *
     * @return range for ImageShiftXBackRange
     * @since API 5
     */
    public FloatRange getImageShiftXBackRange() { return mCapsCreator.mImageShiftXBackRange; }

    /**
     * Gets a range of ImageShiftYBackRange
     *
     * @return range for ImageShiftYBackRange
     * @since API 5
     */
    public FloatRange getImageShiftYBackRange() { return mCapsCreator.mImageShiftYBackRange; }

    /**
     * Return Booklet Borders Each Page supported on the current printer.
     *
     * @return List of Booklet Borders Each Page types
     * @remarks An empty list indicates that the option is not supported by the printer.
     * @since API 5
     */
    public List<BookletBordersEachPage> getBookletBordersEachPageList() {
        return Collections.unmodifiableList(mCapsCreator.mBookletBordersEachPageList);
    }

    /**
     * Return Booklet Finishing Option supported on the current printer.
     *
     * @return List of Booklet Finishing Option types
     * @remarks An empty list indicates that the option is not supported by the printer.
     * @since API 5
     */
    public List<BookletFinishingOption> getBookletFinishingOptionList() {
        return Collections.unmodifiableList(mCapsCreator.mBookletFinishingOptionList);
    }

    /**
     * Return Booklet Format supported on the current printer.
     *
     * @return List of Booklet Format types
     * @remarks An empty list indicates that the option is not supported by the printer.
     * @since API 5
     */
    public List<BookletFormat> getBookletFormatList() {
        return Collections.unmodifiableList(mCapsCreator.mBookletFormatList);
    }

    /**
     * Return staple modes supported on the current printer.
     *
     * @return List of staple mode types
     * @remarks An empty list indicates that the option is not supported by the printer.
     * @since API 5
     */
    public List<StapleOption> getStapleOptionList() {
        return Collections.unmodifiableList(mCapsCreator.mStapleOption);
    }

    /**
     * Return punch modes supported on the current printer.
     *
     * @return List of punch mode types
     * @remarks An empty list indicates that the option is not supported by the printer.
     * @since API 5
     */
    public List<PunchMode> getPunchModeList() {
        return Collections.unmodifiableList(mCapsCreator.mPunchMode);
    }

    /**
     * Return fold modes supported on the current printer.
     *
     * @return List of fold mode types
     * @remarks An empty list indicates that the option is not supported by the printer.
     * @since API 6
     */
    public List<FoldMode> getFoldModeList() {
        return Collections.unmodifiableList(mCapsCreator.mFoldMode);
    }

    /**
     * Return stampPosition supported on the current printer.
     *
     * @return List of stampPosition
     * @remarks An empty list indicates that the option is not supported by the printer.
     * @since API 6
     */
    public List<StampPosition> getStampPositionList() {
        return new ArrayList<>(mCapsCreator.mStampOptionMap.keySet());
    }

    /**
     * Return stampOption supported on the current printer.
     *
     * @return stampOption
     * @remarks An empty list indicates that the option is not supported by the printer.
     * @since API 6
     */
    public StampOption getStampOption(StampPosition stampPosition) {
        return mCapsCreator.mStampOptionMap.get(stampPosition);
    }

    public List<StampType> getStampTypeList(StampPosition stampPosition){
        return mCapsCreator.mStampOptionMap.get(stampPosition).getStampTypeList();
    }

    public List<StampPolicyType> getStampPolicyTypeList(StampPosition stampPosition){
        return mCapsCreator.mStampOptionMap.get(stampPosition).getStampPolicyTypeList();
    }

    public List<String> getStampFormatFontList(StampPosition stampPosition){
        return mCapsCreator.mStampOptionMap.get(stampPosition).getStampFormatFontList();
    }

    public List<Integer> getStampFormatTextSizeList(StampPosition stampPosition){
        return mCapsCreator.mStampOptionMap.get(stampPosition).getStampFormatTextSizeList();
    }

    public List<String> getStampFormatTextColorList(StampPosition stampPosition){
        return mCapsCreator.mStampOptionMap.get(stampPosition).getStampFormatTextColorList();
    }

    public Range getStampFormatStartingPage(StampPosition stampPosition) {
        return mCapsCreator.mStampOptionMap.get(stampPosition).getStampFormatTextStartingPage();
    }

    public List<Boolean> getStampFormatWhiteBackgroundList(StampPosition stampPosition){
        return mCapsCreator.mStampOptionMap.get(stampPosition).getStampFormatWhiteBackgroundList();
    }

    /**
     * Returns Transparency range supported by the current printer
     *
     * @return List of Transparency ranges.
     * @since API 6
     */
    public Range getWatermarkTransparencyRange() {
        return mCapsCreator.mWatermarkTransparencyRange;
    }

    /**
     * Returns the supported watermark OnlyFirstPage options by the current printer
     *
     * @return WatermarkOnlyFirstPage options (true/false)
     * @since API 6
     */
    public List<WatermarkOnlyFirstPage> getWatermarkOnlyFirstPageList() {
        return Collections.unmodifiableList(mCapsCreator.mWatermarkOnlyFirstPageList);
    }

    /**
     * Returns darkness range supported by the current printer
     *
     * @return List of darkness ranges.
     * @since API 6
     */
    public Range getWatermarkDarknessRange() {
        return mCapsCreator.mWatermarkDarknessRange;
    }

    /**
     * Returns the supported watermark rotation options by the current printer
     *
     * @return WatermarkRotation options (true/false)
     * @since API 6
     */
    public List<WatermarkRotate45> getWatermarkRotate45List() {
        return Collections.unmodifiableList(mCapsCreator.mWatermarkRotate45List);
    }
    /**
     * Returns the supported watermark rotation options by the current printer
     *
     * @return WatermarkRotation options (true/false)
     * @since API 6
     */
    public List<WatermarkType> getWatermarkTypeList() {
        return Collections.unmodifiableList(mCapsCreator.mWatermarkTypeList);
    }

    /**
     * Returns the supported watermark message type by the current printer
     *
     * @return WatermarkMessageType options
     * @since API 6
     */
    public List<WatermarkMessageType> getWatermarkMessageTypeList() {
        return Collections.unmodifiableList(mCapsCreator.mWatermarkMessageTypeList);
    }

    /**
     * Returns the supported watermark background patterns by the current printer
     *
     * @return WatermarkBackgroundPattern options
     * @since API 6
     */
    public List<WatermarkBackgroundPattern> getWatermarkBackgroundPatternList() {
        return Collections.unmodifiableList(mCapsCreator.mWatermarkBackgroundPatternList);
    }

    /**
     * Returns the supported watermark background color options by the current printer
     *
     * @return WatermarkBackgroundColor options
     * @since API 6
     */
    public List<String> getWatermarkBackgroundColorList() {
        return Collections.unmodifiableList(mCapsCreator.mWatermarkBackgroundColorList);
    }

    /**
     * Returns the supported watermark font options by the current printer
     *
     * @return WatermarkFont options
     * @since API 6
     */
    public List<String> getWatermarkFontList() {
        return Collections.unmodifiableList(mCapsCreator.mWatermarkFontList);
    }

    /**
     * Returns the supported watermark text color options by the current printer
     *
     * @return WatermarkTextColor options
     * @since API 6
     */
    public List<String> getWatermarkTextColorList() {
        return Collections.unmodifiableList(mCapsCreator.mWatermarkTextColorList);
    }

    /**
     * Returns the supported watermark text size options by the current printer
     *
     * @return WatermarkTextSize options
     * @since API 6
     */
    public List<Integer> getWatermarkTextSizeList() {
        return Collections.unmodifiableList(mCapsCreator.mWatermarkTextSizeList);
    }
}
