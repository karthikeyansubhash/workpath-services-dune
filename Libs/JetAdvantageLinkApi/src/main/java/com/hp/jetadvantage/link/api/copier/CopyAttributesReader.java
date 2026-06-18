// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.copier;

import androidx.annotation.Keep;

import com.hp.jetadvantage.link.api.copier.CopyAttributes.BackgroundCleanup;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.CollateMode;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.ColorMode;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.ContrastAdjustment;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.CopyPreview;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.DarknessAdjustment;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.Duplex;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.JobAssemblyMode;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.JobExecutionMode;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.NumberUpDirection;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.NumberUpMode;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.Orientation;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.PaperSize;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.PaperSource;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.PaperType;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.RetentionMode;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.ScaleMode;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.ScanSize;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.ScanSource;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.SharpnessAdjustment;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.TextGraphicsOptimization;

import java.util.HashMap;
import java.util.Map;

/**
 * Reads attributes which are for requesting a copy from the printer
 *
 * @since API 3
 */
@SuppressWarnings({"unused"})
public class CopyAttributesReader {
    @Keep
    private final CopyAttributes mAttrs;

    /**
     * Constructs this from the copy attributes provided.
     *
     * @param copyAttrs The copy attributes used to construct this.
     * @since API 3
     */
    @Keep
    public CopyAttributesReader(final CopyAttributes copyAttrs) {
        mAttrs = copyAttrs;
    }

    /**
     * The version in which the attributes were created.
     *
     * @return The version in which the attributes were created.
     * @hide for internal use
     * @since API 3
     */
    @Keep
    public int getVersion() {
        return mAttrs.mVersion;
    }

    /**
     * The color mode for the copy.
     *
     * @return color mode
     * @since API 3
     */
    @Keep
    public ColorMode getColorMode() {
        return mAttrs.mColorMode;
    }

    /**
     * The original orientation for the copy.
     *
     * @return original orientation
     * @since API 3
     */
    @Keep
    public Orientation getOrientation() {
        return mAttrs.mOrientation;
    }

    /**
     * The duplex mode for the copy.
     *
     * @return duplex mode
     * @since API 3
     */
    @Keep
    public Duplex getScanDuplex() {
        return mAttrs.mScanDuplex;
    }

    /**
     * The size of the scanned region.
     *
     * @return scan size type.
     * @since API 3
     */
    @Keep
    public ScanSize getScanSize() {
        return mAttrs.mScanSize;
    }

    /**
     * The custom length of the paper to scan.
     *
     * @return scan size type.
     * @since API 3
     */
    @Keep
    public Float getScanCustomLength() {
        return mAttrs.mScanCustomLength;
    }

    /**
     * The custom width of the paper to scan.
     *
     * @return scan size type.
     * @since API 3
     */
    @Keep
    public Float getScanCustomWidth() {
        return mAttrs.mScanCustomWidth;
    }

    /**
     * The source for the scanning.
     *
     * @return scan source.
     * @since API 3
     */
    @Keep
    public ScanSource getScanSource() {
        return mAttrs.mScanSource;
    }

    /**
     * Show a preview of the copied images
     *
     * @return selected scan preview option
     * @since API 3
     */
    @Keep
    public CopyPreview getCopyPreview() {
        return mAttrs.mCopyPreview;
    }

    /**
     * The background clean up for the scan
     *
     * @return selected background cleanup option
     * @since API 3
     */
    @Keep
    public BackgroundCleanup getBackgroundCleanup() {
        return mAttrs.mBackgroundCleanup;
    }

    /**
     * The contrast adjustment for the scan
     *
     * @return selected contrast adjustment option
     * @since API 3
     */
    @Keep
    public ContrastAdjustment getContrastAdjustment() {
        return mAttrs.mContrastAdjustment;
    }

    /**
     * The darkness adjustment for the scan
     *
     * @return selected darkness adjustment option
     * @since API 3
     */
    @Keep
    public DarknessAdjustment getDarknessAdjustment() {
        return mAttrs.mDarknessAdjustment;
    }

    /**
     * The sharpness adjustment for the scan
     *
     * @return selected sharpness adjustment option
     * @since API 3
     */
    @Keep
    public SharpnessAdjustment getSharpnessAdjustment() {
        return mAttrs.mSharpnessAdjustment;
    }

    /**
     * The duplex mode for the print.
     *
     * @return duplex mode
     * @since API 3
     */
    @Keep
    public Duplex getPrintDuplex() {
        return mAttrs.mPrintDuplex;
    }

    /**
     * The size of the paper to print.
     *
     * @return print size type.
     * @since API 3
     */
    @Keep
    public PaperSize getPrintSize() {
        return mAttrs.mPrintSize;
    }


    /**
     * The custom length of the paper to print.
     *
     * @return print size type.
     * @since API 3
     */
    @Keep
    public Float getPrintCustomLength() {
        return mAttrs.mPrintCustomLength;
    }

    /**
     * The custom width of the paper to print.
     *
     * @return print size type.
     * @since API 3
     */
    @Keep
    public Float getPrintCustomWidth() {
        return mAttrs.mPrintCustomWidth;
    }

    /**
     * @return copies values
     * @since API 3
     */
    @Keep
    public int getCopies() {
        return mAttrs.mCopies;
    }

    /**
     * The collate mode for the print.
     *
     * @return collate mode
     * @since API 3
     */
    @Keep
    public CollateMode getCollateMode() {
        return mAttrs.mCollateMode;
    }

    /**
     * The source of paper (tray) for the print.
     *
     * @return collate mode
     * @since API 3
     */
    @Keep
    public PaperSource getPaperSource() {
        return mAttrs.mPaperSource;
    }

    /**
     * The paper type for the print.
     *
     * @return paper type.
     * @since API 3
     */
    @Keep
    public PaperType getPaperType() {
        return mAttrs.mPaperType;
    }

    /**
     * Reduce or enlarge during copying
     *
     * @return scale mode
     * @since API 3
     */
    @Keep
    public ScaleMode getScaleMode() {
        return mAttrs.mScaleMode;
    }

    /**
     * Scale percent (for manual scale mode)
     *
     * @return scale percent
     * @since API 3
     */
    @Keep
    public int getScalePercent() {
        return mAttrs.mScalePercent;
    }

    /**
     * Text/graphics optimization option
     *
     * @return text/graphics optimization option
     * @since API 3
     */
    @Keep
    public TextGraphicsOptimization getTextGraphicsOptimization() {
        return mAttrs.mTextGraphicsOptimization;
    }

    /**
     * The job assembly mode for the scan
     *
     * @return job assembly mode option
     * @since API 3
     */
    @Keep
    public JobAssemblyMode getJobAssemblyMode() {
        return mAttrs.mJobAssemblyMode;
    }

    /**
     * The job execution mode for copy
     *
     * @return job execution mode option
     * @since API 3
     */
    @Keep
    public JobExecutionMode getJobExecutionMode() {
        return mAttrs.mJobExecutionMode;
    }

    /**
     * Number up mode for the copy
     *
     * @return number up mode
     * @since API 3
     */
    @Keep
    public NumberUpMode getNumberUpMode() {
        return mAttrs.mNumberUpMode;
    }

    /**
     * Number up direction for the copy
     *
     * @return number up direction
     * @since API 3
     */
    @Keep
    public NumberUpDirection getNumberUpDirection() {
        return mAttrs.mNumberUpDirection;
    }

    /**
     * The folder name for the storing job
     *
     * @return store job folder name
     * @since API 3
     */
    public String getStoreJobFolderName() {
        return mAttrs.mStoreJobFolderName;
    }

    /**
     * The name for the storing job
     *
     * @return store job name
     * @since API 3
     */
    public String getStoreJobName() {
        return mAttrs.mStoreJobName;
    }

    /**
     * The retention mode for power cycle
     *
     * @return store job retention mode on power cycle
     * @since API 3
     */
    public RetentionMode getStoredJobRetentionModeOnPowerCycle() {
        return mAttrs.mStoredJobRetentionModeOnPowerCycle;
    }

    /**
     * The retention mode for release job
     *
     * @return store job retention mode on release
     * @since API 3
     */
    public RetentionMode getStoredJobRetentionModeOnRelease() {
        return mAttrs.mStoredJobRetentionModeOnRelease;
    }

    /**
     * The password type for stored job
     *
     * @return store job credentials
     * @since API 3
     */
    public JobCredentialsAttributes getStoredJobCredentialsAttributes() {
        return mAttrs.mStoredJobCredentialsAttributes;
    }

    /**
     * The output bin for copy
     *
     * @return output bin
     * @since API 5
     */
    public CopyAttributes.OutputBin getOutputBin() {
        return mAttrs.mOutputBin;
    }

    /**
     * The progress dialog mode for copy
     *
     * @return progress dialog mode
     * @since API 5
     */
    public CopyAttributes.ProgressDialogMode getProgressDialogMode() {
        return mAttrs.mProgressDialogMode;
    }
    /**
     * @return Transparency values
     * @since API 6
     */
    @Keep
    public int getWatermarkTransparency() {
        return mAttrs.mWatermarkTransparency;
    }

    /**
     * @return TextSize values
     * @since API 6
     */
    @Keep
    public int getWatermarkTextSize() {
        return mAttrs.mWatermarkTextSize;
    }

    /**
     * @return BackgroundColor values
     * @since API 6
     */
    @Keep
    public String getWatermarkBackgroundColor() {
        return mAttrs.mWatermarkBackgroundColor;
    }

    /**
     * @return Font values
     * @since API 6
     */
    @Keep
    public String getWatermarkFont() {
        return mAttrs.mWatermarkFont;
    }
    /**
     * @return TextColor values
     * @since API 6
     */
    @Keep
    public String getWatermarkTextColor() {
        return mAttrs.mWatermarkTextColor;
    }

    /**
     * Watermark OnlyFirstPage value
     *
     * @return selected watermark OnlyFirstPage option
     * @since API 6
     */
    @Keep
    public CopyAttributes.WatermarkOnlyFirstPage getWatermarkOnlyFirstPage() {
        return mAttrs.mWatermarkOnlyFirstPage;
    }

    /**
     * @return darkness values
     * @since API 6
     */
    @Keep
    public int getWatermarkDarkness() {
        return mAttrs.mWatermarkDarkness;
    }

    /**
     * @return darkness values
     * @since API 6
     */
    @Keep
    public String getWatermarkText() {
        return mAttrs.mWatermarkText;
    }

    /**
     * Watermark Rotation value
     *
     * @return selected watermark rotation option
     * @since API 6
     */
    @Keep
    public CopyAttributes.WatermarkRotate45 getWatermarkRotation45() {
        return mAttrs.mWatermarkRotate45;
    }
    /**
     * Watermark Type value
     *
     * @return selected watermark Type option
     * @since API 6
     */
    @Keep
    public CopyAttributes.WatermarkType getWatermarkType() {
        return mAttrs.mWatermarkType;
    }

    /**
     * EraseMarginUnit
     * @return EraseMarginUnit
     * @since API 6
     */
    @Keep
    public CopyAttributes.EraseMarginUnit getEraseMarginUnit() { return mAttrs.mEraseMarginUnit; }

    /**
     * EraseBackBottomMargin
     * @return EraseBackBottomMargin
     * @since API 6
     */
    @Keep
    public Float getEraseBackBottomMargin() { return mAttrs.mEraseBackBottom; }

    /**
     * EraseBackLeftMargin
     * @return EraseBackLeftMargin
     * @since API 6
     */
    @Keep
    public Float getEraseBackLeftMargin() { return mAttrs.mEraseBackLeft; }

    /**
     * EraseBackRightMargin
     * @return EraseBackRightMargin
     * @since API 6
     */
    @Keep
    public Float getEraseBackRightMargin() { return mAttrs.mEraseBackRight; }

    /**
     * EraseBackTopMargin
     * @return EraseBackTopMargin
     * @since API 6
     */
    @Keep
    public Float getEraseBackTopMargin() { return mAttrs.mEraseBackTop; }

    /**
     * EraseFrontBottomMargin
     * @return EraseFrontBottomMargin
     * @since API 6
     */
    @Keep
    public Float getEraseFrontBottomMargin() { return mAttrs.mEraseFrontBottom; }

    /**
     * EraseFrontLeftMargin
     * @return EraseFrontLeftMargin
     * @since API 6
     */
    @Keep
    public Float getEraseFrontLeftMargin() { return mAttrs.mEraseFrontLeft; }

    /**
     * EraseFrontRightMargin
     * @return EraseFrontRightMargin
     * @since API 6
     */
    @Keep
    public Float getEraseFrontRightMargin() { return mAttrs.mEraseFrontRight; }

    /**
     * EraseFrontTopMargin
     * @return EraseFrontTopMargin
     * @since API 6
     */
    @Keep
    public Float getEraseFrontTopMargin() { return mAttrs.mEraseFrontTop; }

    /** The capture mode for the copy
     *
     * @return selected capture mode option
     * @since API 6
     */
    @Keep
    public CopyAttributes.CaptureMode getCaptureMode() {
        return mAttrs.mCaptureMode;
    }

    /**
     * The Image Shift Reduce To Fit for copy
     *
     * @return Image Shift Reduce To Fit
     * @since API 5
     */
    public CopyAttributes.ImageShiftReduceToFit getImageShiftReduceToFit() {
        return mAttrs.mImageShiftReduceToFit;
    }

    /**
     * The Image Shift Units for copy
     *
     * @return Image Shift Units
     * @since API 5
     */
    public CopyAttributes.ImageShiftUnits getImageShiftUnits() {
        return mAttrs.mImageShiftUnits;
    }

    /**
     * ImageShiftXFront
     * @return ImageShiftXFront
     * @since API 5
     */
    @Keep
    public Float getImageShiftXFront() { return mAttrs.mImageShiftXFront; }

    /**
     * ImageShiftYFront
     * @return ImageShiftYFront
     * @since API 5
     */
    @Keep
    public Float getImageShiftYFront() { return mAttrs.mImageShiftYFront; }

    /**
     * ImageShiftXBack
     * @return ImageShiftXBack
     * @since API 5
     */
    @Keep
    public Float getImageShiftXBack() { return mAttrs.mImageShiftXBack; }

    /**
     * ImageShiftYBack
     * @return ImageShiftYBack
     * @since API 5
     */
    @Keep
    public Float getImageShiftYBack() { return mAttrs.mImageShiftYBack; }

    /**
     * The Booklet Borders Each Page for copy
     *
     * @return Booklet Borders Each Page
     * @since API 5
     */
    public CopyAttributes.BookletBordersEachPage getBookletBordersEachPage() {
        return mAttrs.mBookletBordersEachPage;
    }

    /**
     * The Booklet Finishing Option for copy
     *
     * @return Booklet Finishing Option
     * @since API 5
     */
    public CopyAttributes.BookletFinishingOption getBookletFinishingOption() {
        return mAttrs.mBookletFinishingOption;
    }

    /**
     * The Booklet Format for copy
     *
     * @return Booklet Format
     * @since API 5
     */
    public CopyAttributes.BookletFormat getBookletFormat() {
        return mAttrs.mBookletFormat;
    }
    /**
     * The staple mode for copy
     *
     * @return staple mode
     * @since API 5
     */
    public CopyAttributes.StapleOption getStapleOption() {
        return mAttrs.mStapleOption;
    }

    /**
     * The punch mode for copy
     *
     * @return punch mode
     * @since API 5
     */
    public CopyAttributes.PunchMode getPunchMode() {
        return mAttrs.mPunchMode;
    }

    /**
     * The fold mode for copy
     *
     * @return fold mode
     * @since API 6
     */
    public CopyAttributes.FoldMode getFoldMode() {
        return mAttrs.mFoldMode;
    }

    /**
     * The StampOption for copy
     *
     * @return StampOption Map
     * @since API 6
     */
    public Map<CopyAttributes.StampPosition, StampOption> getStampOptionMap() {
        Map<CopyAttributes.StampPosition, StampOption> stampOptionMap = new HashMap<>();
        for(String stampPositionName : mAttrs.mStampOptionMap.keySet()){
            stampOptionMap.put(CopyAttributes.StampPosition.valueOf(stampPositionName), mAttrs.mStampOptionMap.get(stampPositionName));
        }
        return stampOptionMap;
    }

    /**
     * Watermark message type value
     *
     * @return selected watermark message type option
     * @since API 6
     */
    @Keep
    public CopyAttributes.WatermarkMessageType getWatermarkMessageType() {
        return mAttrs.mWatermarkMessageType;
    }

    /**
     * Watermark background pattern value
     *
     * @return selected watermark background pattern option
     * @since API 6
     */
    @Keep
    public CopyAttributes.WatermarkBackgroundPattern getWatermarkBackgroundPattern() {
        return mAttrs.mWatermarkBackgroundPattern;
    }
}
