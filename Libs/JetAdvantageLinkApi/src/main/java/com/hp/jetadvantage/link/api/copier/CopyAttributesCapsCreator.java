// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.copier;

import static com.hp.jetadvantage.link.api.copier.CopyAttributes.BackgroundCleanup;
import static com.hp.jetadvantage.link.api.copier.CopyAttributes.BookletBordersEachPage;
import static com.hp.jetadvantage.link.api.copier.CopyAttributes.BookletFinishingOption;
import static com.hp.jetadvantage.link.api.copier.CopyAttributes.BookletFormat;
import static com.hp.jetadvantage.link.api.copier.CopyAttributes.CaptureMode;
import static com.hp.jetadvantage.link.api.copier.CopyAttributes.CollateMode;
import static com.hp.jetadvantage.link.api.copier.CopyAttributes.ContrastAdjustment;
import static com.hp.jetadvantage.link.api.copier.CopyAttributes.DarknessAdjustment;
import static com.hp.jetadvantage.link.api.copier.CopyAttributes.EraseMarginUnit;
import static com.hp.jetadvantage.link.api.copier.CopyAttributes.FoldMode;
import static com.hp.jetadvantage.link.api.copier.CopyAttributes.ImageShiftReduceToFit;
import static com.hp.jetadvantage.link.api.copier.CopyAttributes.ImageShiftUnits;
import static com.hp.jetadvantage.link.api.copier.CopyAttributes.JobAssemblyMode;
import static com.hp.jetadvantage.link.api.copier.CopyAttributes.JobExecutionMode;
import static com.hp.jetadvantage.link.api.copier.CopyAttributes.NumberUpDirection;
import static com.hp.jetadvantage.link.api.copier.CopyAttributes.NumberUpMode;
import static com.hp.jetadvantage.link.api.copier.CopyAttributes.OutputBin;
import static com.hp.jetadvantage.link.api.copier.CopyAttributes.PaperSize;
import static com.hp.jetadvantage.link.api.copier.CopyAttributes.PaperSource;
import static com.hp.jetadvantage.link.api.copier.CopyAttributes.PaperType;
import static com.hp.jetadvantage.link.api.copier.CopyAttributes.ProgressDialogMode;
import static com.hp.jetadvantage.link.api.copier.CopyAttributes.PunchMode;
import static com.hp.jetadvantage.link.api.copier.CopyAttributes.ScaleMode;
import static com.hp.jetadvantage.link.api.copier.CopyAttributes.SharpnessAdjustment;
import static com.hp.jetadvantage.link.api.copier.CopyAttributes.StampPosition;
import static com.hp.jetadvantage.link.api.copier.CopyAttributes.StapleOption;
import static com.hp.jetadvantage.link.api.copier.CopyAttributes.TextGraphicsOptimization;
import static com.hp.jetadvantage.link.api.copier.CopyAttributes.WatermarkBackgroundPattern;
import static com.hp.jetadvantage.link.api.copier.CopyAttributes.WatermarkMessageType;
import static com.hp.jetadvantage.link.api.copier.CopyAttributes.WatermarkOnlyFirstPage;
import static com.hp.jetadvantage.link.api.copier.CopyAttributes.WatermarkRotate45;
import static com.hp.jetadvantage.link.api.copier.CopyAttributes.WatermarkType;

import com.hp.jetadvantage.link.api.copier.CopyAttributes.ColorMode;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.CopyPreview;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.Duplex;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.Orientation;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.ScanSize;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.ScanSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hp.jetadvantage.link.api.copier.CopyAttributes.*;

/**
 * Holds the set of attributes provided from the printer when requesting a copy
 *
 * @hide The creation of capabilities is meant for the copier service to expose a read-only list of capabilities.
 * Clients should not be writing capabilities.
 */
public class CopyAttributesCapsCreator {

    final List<ColorMode> mColorModeList;

    final List<Orientation> mOrientationList;

    final List<Duplex> mScanDuplexList;

    final List<ScanSize> mScanSizeList;

    final FloatRange mScanCustomLengthRange;

    final FloatRange mScanCustomWidthRange;

    final List<ScanSource> mScanSourceList;

    final List<CopyPreview> mCopyPreviewList;

    final List<BackgroundCleanup> mBackgroundCleanupList;

    final List<ContrastAdjustment> mContrastAdjustmentList;

    final List<DarknessAdjustment> mDarknessAdjustmentList;

    final List<SharpnessAdjustment> mSharpnessAdjustmentList;

    final List<Duplex> mPrintDuplexList;

    final List<PaperSize> mPrintSizeList;

    final List<PaperType> mPaperTypeList;

    final FloatRange mPrintCustomLengthRange;

    final FloatRange mPrintCustomWidthRange;

    final Range mCopiesRange;

    final List<CollateMode> mCollateModeList;

    final List<PaperSource> mPaperSourceList;

    final List<ScaleMode> mScaleModeList;

    final Map<ScanSource, Range> mScalePercentRangeMap;

    final List<TextGraphicsOptimization> mTextGraphicsOptimizationList;

    final List<JobAssemblyMode> mJobAssemblyModeList;

    final List<JobExecutionMode> mJobExecutionModeList;

    final List<NumberUpMode> mNumberUpModeList;

    final Map<NumberUpMode, List<NumberUpDirection>> mNumberUpDirectionMap;

    final List<JobCredentialsAttributes.PasswordType> mPasswordTypeList;

    final List<OutputBin> mOutputBinList;

    final List<ProgressDialogMode> mProgressDialogModeList;

    final List<EraseMarginUnit> mEraseMarginUnitList;

    final FloatRange mEraseBackBottomRange;

    final FloatRange mEraseBackLeftRange;

    final FloatRange mEraseBackRightRange;

    final FloatRange mEraseBackTopRange;

    final FloatRange mEraseFrontBottomRange;

    final FloatRange mEraseFrontLeftRange;

    final FloatRange mEraseFrontRightRange;

    final FloatRange mEraseFrontTopRange;

    final List<CaptureMode> mCaptureModeList;

    final List<ImageShiftReduceToFit> mImageShiftReduceToFitList;

    final List<ImageShiftUnits> mImageShiftUnitsList;

    final FloatRange mImageShiftXFrontRange;

    final FloatRange mImageShiftYFrontRange;

    final FloatRange mImageShiftXBackRange;

    final FloatRange mImageShiftYBackRange;

    final List<BookletBordersEachPage> mBookletBordersEachPageList;

    final List<BookletFinishingOption> mBookletFinishingOptionList;

    final List<BookletFormat> mBookletFormatList;

    final List<StapleOption> mStapleOption;

    final List<PunchMode> mPunchMode;

    final List<FoldMode> mFoldMode;

    final Map<StampPosition, StampOption> mStampOptionMap;

    final Range mWatermarkDarknessRange;
    final Range mWatermarkTransparencyRange;

    final List<WatermarkRotate45> mWatermarkRotate45List;
    final List<WatermarkType> mWatermarkTypeList;
    final List<WatermarkOnlyFirstPage> mWatermarkOnlyFirstPageList;
    final List<WatermarkMessageType> mWatermarkMessageTypeList;
    final List<WatermarkBackgroundPattern> mWatermarkBackgroundPatternList;
    final List<Integer> mWatermarkTextSizeList;
    final List<String> mWatermarkFontList;
    final List<String> mWatermarkBackgroundColorList;
    final List<String> mWatermarkTextColorList;

    private CopyAttributesCapsCreator(final Builder builder) {
        mColorModeList = builder.mColorModeList;
        mOrientationList = builder.mOrientationList;
        mScanDuplexList = builder.mScanDuplexList;
        mScanSizeList = builder.mScanSizeList;
        mScanCustomLengthRange = builder.mScanCustomLengthRange;
        mScanCustomWidthRange = builder.mScanCustomWidthRange;
        mScanSourceList = builder.mScanSourceList;
        mCopyPreviewList = builder.mCopyPreviewList;
        mBackgroundCleanupList = builder.mBackgroundCleanupList;
        mContrastAdjustmentList = builder.mContrastAdjustmentList;
        mDarknessAdjustmentList = builder.mDarknessAdjustmentList;
        mSharpnessAdjustmentList = builder.mSharpnessAdjustmentList;
        mPrintDuplexList = builder.mPrintDuplexList;
        mPrintSizeList = builder.mPrintSizeList;
        mPrintCustomLengthRange = builder.mPrintCustomLengthRange;
        mPrintCustomWidthRange = builder.mPrintCustomWidthRange;
        mCopiesRange = builder.mCopiesRange;
        mCollateModeList = builder.mCollateModeList;
        mPaperSourceList = builder.mPaperSourceList;
        mPaperTypeList = builder.mPaperTypeList;
        mScaleModeList = builder.mScaleModeList;
        mScalePercentRangeMap = builder.mScalePercentRangeMap;
        mTextGraphicsOptimizationList = builder.mTextGraphicsOptimizationList;
        mJobAssemblyModeList = builder.mJobAssemblyModeList;
        mJobExecutionModeList = builder.mJobExecutionModeList;
        mNumberUpModeList = builder.mNumberUpModeList;
        mNumberUpDirectionMap = builder.mNumberUpDirectionMap;
        mPasswordTypeList = builder.mPasswordTypeList;
        mOutputBinList = builder.mOutputBinList;
        mProgressDialogModeList = builder.mProgressDialogModeList;

        mEraseMarginUnitList = builder.mEraseMarginUnitList;
        mEraseBackBottomRange = builder.mEraseBackBottomRange;
        mEraseBackLeftRange = builder.mEraseBackLeftRange;
        mEraseBackRightRange = builder.mEraseBackRightRange;
        mEraseBackTopRange = builder.mEraseBackTopRange;
        mEraseFrontBottomRange = builder.mEraseFrontBottomRange;
        mEraseFrontLeftRange = builder.mEraseFrontLeftRange;
        mEraseFrontRightRange = builder.mEraseFrontRightRange;
        mEraseFrontTopRange = builder.mEraseFrontTopRange;
        mCaptureModeList = builder.mCaptureModeList;
        mImageShiftReduceToFitList = builder.mImageShiftReduceToFitList;
        mImageShiftUnitsList = builder.mImageShiftUnitsList;
        mImageShiftXFrontRange = builder.mImageShiftXFrontRange;
        mImageShiftYFrontRange = builder.mImageShiftYFrontRange;
        mImageShiftXBackRange = builder.mImageShiftXBackRange;
        mImageShiftYBackRange = builder.mImageShiftYBackRange;
        mBookletBordersEachPageList = builder.mBookletBordersEachPageList;
        mBookletFinishingOptionList = builder.mBookletFinishingOptionList;
        mBookletFormatList = builder.mBookletFormatList;
        mStapleOption = builder.mStapleOption;
        mPunchMode = builder.mPunchMode;
        mFoldMode = builder.mFoldMode;
        mStampOptionMap = builder.mStampOptionMap;
        mWatermarkDarknessRange = builder.mWatermarkDarknessRange;
        mWatermarkRotate45List = builder.mWatermarkRotate45List;
        mWatermarkTypeList = builder.mWatermarkTypeList;

        mWatermarkTransparencyRange = builder.mWatermarkTransparencyRange;
        mWatermarkOnlyFirstPageList = builder.mWatermarkOnlyFirstPageList;
        mWatermarkMessageTypeList = builder.mWatermarkMessageTypeList;
        mWatermarkBackgroundPatternList = builder.mWatermarkBackgroundPatternList;
        mWatermarkBackgroundColorList = builder.mWatermarkBackgroundColorList;
        mWatermarkFontList = builder.mWatermarkFontList;
        mWatermarkTextColorList = builder.mWatermarkTextColorList;
        mWatermarkTextSizeList = builder.mWatermarkTextSizeList;
    }

    /**
     * Builder for determining the attributes supported for requesting a copy job.
     */
    public static class Builder {
        final List<ColorMode> mColorModeList = new ArrayList<>();

        final List<Orientation> mOrientationList = new ArrayList<>();

        final List<Duplex> mScanDuplexList = new ArrayList<>();

        final List<ScanSize> mScanSizeList = new ArrayList<>();

        final FloatRange mScanCustomLengthRange = new FloatRange(0f, 0f);

        final FloatRange mScanCustomWidthRange = new FloatRange(0f, 0f);

        final List<ScanSource> mScanSourceList = new ArrayList<>();

        final List<CopyPreview> mCopyPreviewList = new ArrayList<>();

        final List<BackgroundCleanup> mBackgroundCleanupList = new ArrayList<>();

        final List<ContrastAdjustment> mContrastAdjustmentList = new ArrayList<>();

        final List<DarknessAdjustment> mDarknessAdjustmentList = new ArrayList<>();

        final List<SharpnessAdjustment> mSharpnessAdjustmentList = new ArrayList<>();

        final List<Duplex> mPrintDuplexList = new ArrayList<>();

        final List<PaperSize> mPrintSizeList = new ArrayList<>();

        final FloatRange mPrintCustomLengthRange = new FloatRange(0f, 0f);

        final FloatRange mPrintCustomWidthRange = new FloatRange(0f, 0f);

        final Range mCopiesRange = new Range(1, 1);

        final List<CollateMode> mCollateModeList = new ArrayList<>();

        final List<PaperSource> mPaperSourceList = new ArrayList<>();

        final List<PaperType> mPaperTypeList = new ArrayList<>();

        final List<ScaleMode> mScaleModeList = new ArrayList<>();

        final Map<ScanSource, Range> mScalePercentRangeMap = new HashMap<>();

        final List<TextGraphicsOptimization> mTextGraphicsOptimizationList = new ArrayList<>();

        final List<JobAssemblyMode> mJobAssemblyModeList = new ArrayList<>();

        final List<JobExecutionMode> mJobExecutionModeList = new ArrayList<>();

        final List<NumberUpMode> mNumberUpModeList = new ArrayList<>();

        final Map<NumberUpMode, List<NumberUpDirection>> mNumberUpDirectionMap = new HashMap<>();

        final List<JobCredentialsAttributes.PasswordType> mPasswordTypeList = new ArrayList<>();

        final List<OutputBin> mOutputBinList = new ArrayList<>();

        final List<ProgressDialogMode> mProgressDialogModeList = new ArrayList<>();

        final Range mWatermarkDarknessRange = new Range(1, 5);
        final Range mWatermarkTransparencyRange = new Range(0, 0);
        final List<WatermarkOnlyFirstPage> mWatermarkOnlyFirstPageList = new ArrayList<>();
        final List<WatermarkRotate45> mWatermarkRotate45List = new ArrayList<>();
        final List<WatermarkType> mWatermarkTypeList = new ArrayList<>();
        final List<WatermarkMessageType> mWatermarkMessageTypeList = new ArrayList<>();
        final List<WatermarkBackgroundPattern> mWatermarkBackgroundPatternList = new ArrayList<>();
        final List<Integer> mWatermarkTextSizeList = new ArrayList<>();
        final List<String> mWatermarkFontList = new ArrayList<>();
        final List<String> mWatermarkBackgroundColorList = new ArrayList<>();
        final List<String> mWatermarkTextColorList = new ArrayList<>();

        final List<EraseMarginUnit> mEraseMarginUnitList = new ArrayList<>();

        final FloatRange mEraseBackBottomRange = new FloatRange(0, 0);
        final FloatRange mEraseBackLeftRange = new FloatRange(0, 0);
        final FloatRange mEraseBackRightRange = new FloatRange(0, 0);
        final FloatRange mEraseBackTopRange = new FloatRange(0, 0);
        final FloatRange mEraseFrontBottomRange = new FloatRange(0, 0);
        final FloatRange mEraseFrontLeftRange = new FloatRange(0, 0);
        final FloatRange mEraseFrontRightRange = new FloatRange(0, 0);
        final FloatRange mEraseFrontTopRange = new FloatRange(0, 0);
        final List<CaptureMode> mCaptureModeList = new ArrayList<>();

        final List<ImageShiftReduceToFit> mImageShiftReduceToFitList = new ArrayList<>();

        final List<ImageShiftUnits> mImageShiftUnitsList = new ArrayList<>();

        final FloatRange mImageShiftXFrontRange = new FloatRange(0, 0);
        final FloatRange mImageShiftYFrontRange = new FloatRange(0, 0);
        final FloatRange mImageShiftXBackRange = new FloatRange(0, 0);
        final FloatRange mImageShiftYBackRange = new FloatRange(0, 0);

        final List<BookletBordersEachPage> mBookletBordersEachPageList = new ArrayList<>();

        final List<BookletFinishingOption> mBookletFinishingOptionList = new ArrayList<>();

        final List<BookletFormat> mBookletFormatList = new ArrayList<>();

        final List<StapleOption> mStapleOption = new ArrayList<>();

        final List<PunchMode> mPunchMode= new ArrayList<>();

        final List<FoldMode> mFoldMode = new ArrayList<>();


        final Map<StampPosition, StampOption> mStampOptionMap = new HashMap<>();


        /**
         * Constructs a new builder used for determining the attributes supported for scanned images back to the client.
         */
        public Builder() {
            // Empty constructor
            mColorModeList.add(ColorMode.DEFAULT);
            mOrientationList.add(Orientation.DEFAULT);
            mScanDuplexList.add(Duplex.DEFAULT);
            mScanSizeList.add(ScanSize.DEFAULT);
            mScanSourceList.add(ScanSource.DEFAULT);
            mCopyPreviewList.add(CopyPreview.DEFAULT);
            mBackgroundCleanupList.add(BackgroundCleanup.DEFAULT);
            mContrastAdjustmentList.add(ContrastAdjustment.DEFAULT);
            mDarknessAdjustmentList.add(DarknessAdjustment.DEFAULT);
            mSharpnessAdjustmentList.add(SharpnessAdjustment.DEFAULT);
            mPrintDuplexList.add(Duplex.DEFAULT);
            mPrintSizeList.add(PaperSize.DEFAULT);
            mCollateModeList.add(CollateMode.DEFAULT);
            mPaperSourceList.add(PaperSource.DEFAULT);
            mPaperTypeList.add(PaperType.DEFAULT);
            mScaleModeList.add(ScaleMode.DEFAULT);
            mScalePercentRangeMap.put(ScanSource.DEFAULT, new Range(100, 100));
            mTextGraphicsOptimizationList.add(TextGraphicsOptimization.DEFAULT);
            mJobAssemblyModeList.add(JobAssemblyMode.DEFAULT);
            mNumberUpModeList.add(NumberUpMode.DEFAULT);
            mNumberUpDirectionMap.put(NumberUpMode.DEFAULT, Collections.singletonList(NumberUpDirection.DEFAULT));
            mOutputBinList.add(OutputBin.DEFAULT);
            mProgressDialogModeList.add(ProgressDialogMode.DEFAULT);
            mEraseMarginUnitList.add(CopyAttributes.EraseMarginUnit.DEFAULT);
            mCaptureModeList.add(CaptureMode.DEFAULT);
            mImageShiftReduceToFitList.add(ImageShiftReduceToFit.DEFAULT);
            mImageShiftUnitsList.add(ImageShiftUnits.DEFAULT);
            mBookletBordersEachPageList.add(BookletBordersEachPage.DEFAULT);
            mBookletFinishingOptionList.add(BookletFinishingOption.DEFAULT);
            mBookletFormatList.add(BookletFormat.DEFAULT);
            mStapleOption.add(StapleOption.DEFAULT);
            mPunchMode.add(PunchMode.DEFAULT);
            mFoldMode.add(FoldMode.DEFAULT);
            mWatermarkTypeList.add(WatermarkType.DEFAULT);
            mWatermarkOnlyFirstPageList.add(WatermarkOnlyFirstPage.DEFAULT);
            mWatermarkRotate45List.add(WatermarkRotate45.DEFAULT);
            mWatermarkMessageTypeList.add(WatermarkMessageType.NONE);
            mWatermarkBackgroundPatternList.add(WatermarkBackgroundPattern.DEFAULT);
        }

        /**
         * Adds a color set in which the scanned image(s) will be supplied.
         *
         * @param colorMode The color set.
         * @return this builder for method chaining.
         */
        public Builder addColorMode(final ColorMode colorMode) {
            mColorModeList.add(colorMode);
            return this;
        }

        /**
         * Adds a original orientation set in which the scanned image(s) will be supplied.
         *
         * @param orientation The original orientation set.
         * @return this builder for method chaining.
         */
        public Builder addOrientation(final Orientation orientation) {
            mOrientationList.add(orientation);
            return this;
        }

        /**
         * Adds a scan duplex set in which the scanned image(s) will be supplied.
         *
         * @param duplex The Duplex set.
         * @return this builder for method chaining.
         */
        public Builder addScanDuplex(final Duplex duplex) {
            mScanDuplexList.add(duplex);
            return this;
        }

        /**
         * Adds a scan size
         *
         * @param scanSize scan size
         * @return this builder for method chaining.
         */
        public Builder addScanSize(final ScanSize scanSize) {
            mScanSizeList.add(scanSize);
            return this;
        }

        /**
         * Sets scan size custom length range
         *
         * @param lowerBound minimum value
         * @param upperBound maximum value
         * @return this builder for method chaining.
         */
        public Builder setScanCustomLengthRange(final float lowerBound, final float upperBound) {
            mScanCustomLengthRange.mLowerBound = lowerBound;
            mScanCustomLengthRange.mUpperBound = upperBound;
            return this;
        }

        /**
         * Sets scan size custom width range
         *
         * @param lowerBound minimum value
         * @param upperBound maximum value
         * @return this builder for method chaining.
         */
        public Builder setScanCustomWidthRange(final float lowerBound, final float upperBound) {
            mScanCustomWidthRange.mLowerBound = lowerBound;
            mScanCustomWidthRange.mUpperBound = upperBound;
            return this;
        }

        /**
         * Adds a scan source
         *
         * @param scanSource source value
         * @return this builder for method chaining.
         */
        public Builder addScanSource(final ScanSource scanSource) {
            mScanSourceList.add(scanSource);
            return this;
        }

        /**
         * Adds a copy preview.
         *
         * @param copyPreview The copy preview value
         * @return this builder for method chaining.
         */
        public Builder addCopyPreview(final CopyPreview copyPreview) {
            mCopyPreviewList.add(copyPreview);
            return this;
        }

        /**
         * Adds background cleanup
         *
         * @param backgroundCleanup value
         * @return this builder for method chaining.
         */
        public Builder addBackgroundCleanup(final BackgroundCleanup backgroundCleanup) {
            mBackgroundCleanupList.add(backgroundCleanup);
            return this;
        }

        /**
         * Adds contrast adjustment
         *
         * @param contrastAdjustment value
         * @return this builder for method chaining.
         */
        public Builder addContrastAdjustment(final ContrastAdjustment contrastAdjustment) {
            mContrastAdjustmentList.add(contrastAdjustment);
            return this;
        }

        /**
         * Adds darkness adjustment
         *
         * @param darknessAdjustment value
         * @return this builder for method chaining.
         */
        public Builder addDarknessAdjustment(final DarknessAdjustment darknessAdjustment) {
            mDarknessAdjustmentList.add(darknessAdjustment);
            return this;
        }

        /**
         * Adds sharpness adjustment
         *
         * @param sharpnessAdjustment value
         * @return this builder for method chaining.
         */
        public Builder addSharpnessAdjustment(final SharpnessAdjustment sharpnessAdjustment) {
            mSharpnessAdjustmentList.add(sharpnessAdjustment);
            return this;
        }

        /**
         * Adds a print duplex set in which the scanned image(s) will be supplied.
         *
         * @param duplex The Duplex set.
         * @return this builder for method chaining.
         */
        public Builder addPrintDuplex(final Duplex duplex) {
            mPrintDuplexList.add(duplex);
            return this;
        }

        /**
         * Adds a print size
         *
         * @param printSize print size
         * @return this builder for method chaining.
         */
        public Builder addPrintSize(final PaperSize printSize) {
            mPrintSizeList.add(printSize);
            return this;
        }

        /**
         * Sets print size custom length range
         *
         * @param lowerBound minimum value
         * @param upperBound maximum value
         * @return this builder for method chaining.
         */
        public Builder setPrintCustomLengthRange(final float lowerBound, final float upperBound) {
            mPrintCustomLengthRange.mLowerBound = lowerBound;
            mPrintCustomLengthRange.mUpperBound = upperBound;
            return this;
        }

        /**
         * Sets print size custom width range
         *
         * @param lowerBound minimum value
         * @param upperBound maximum value
         * @return this builder for method chaining.
         */
        public Builder setPrintCustomWidthRange(final float lowerBound, final float upperBound) {
            mPrintCustomWidthRange.mLowerBound = lowerBound;
            mPrintCustomWidthRange.mUpperBound = upperBound;
            return this;
        }

        /**
         * Sets copies range
         *
         * @param lowerBound minimum value
         * @param upperBound maximum value
         * @return this builder for method chaining.
         */
        public Builder setCopiesRange(final int lowerBound, final int upperBound) {
            mCopiesRange.mLowerBound = lowerBound;
            mCopiesRange.mUpperBound = upperBound;
            return this;
        }

        /**
         * Adds a collate mode
         *
         * @param collateMode value
         * @return this builder for method chaining.
         */
        public Builder addCollateMode(final CollateMode collateMode) {
            mCollateModeList.add(collateMode);
            return this;
        }

        /**
         * Adds a paper source
         *
         * @param paperSource value
         * @return this builder for method chaining.
         */
        public Builder addPaperSource(final PaperSource paperSource) {
            mPaperSourceList.add(paperSource);
            return this;
        }

        /**
         * Adds a paper type
         *
         * @param paperType value
         * @return this builder for method chaining.
         */
        public Builder addPaperType(final PaperType paperType) {
            mPaperTypeList.add(paperType);
            return this;
        }

        /**
         * Adds a scale mode
         *
         * @param scaleMode value
         * @return this builder for method chaining.
         */
        public Builder addScaleMode(final ScaleMode scaleMode) {
            mScaleModeList.add(scaleMode);
            return this;
        }

        /**
         * Sets scale percent range
         *
         * @param lowerBound minimum value
         * @param upperBound maximum value
         * @return this builder for method chaining.
         */
        public Builder setScalePercentRange(final ScanSource scanSource, final int lowerBound, final int upperBound) {
            mScalePercentRangeMap.put(scanSource, new Range(lowerBound, upperBound));
            return this;
        }


        /**
         * Adds text graphics optimization
         *
         * @param textGraphicsOptimization value
         * @return this builder for method chaining.
         */
        public Builder addTextGraphicsOptimization(final TextGraphicsOptimization textGraphicsOptimization) {
            mTextGraphicsOptimizationList.add(textGraphicsOptimization);
            return this;
        }

        /**
         * Adds a job assembly mode
         *
         * @param jobAssemblyMode value
         * @return this builder for method chaining.
         */
        public Builder addJobAssemblyMode(final JobAssemblyMode jobAssemblyMode) {
            mJobAssemblyModeList.add(jobAssemblyMode);
            return this;
        }

        /**
         * Adds a job execution mode
         *
         * @param jobExecutionMode value
         * @return this builder for method chaining.
         */
        public Builder addJobExecutionMode(final JobExecutionMode jobExecutionMode) {
            mJobExecutionModeList.add(jobExecutionMode);
            return this;
        }

        /**
         * Adds a number up mode
         *
         * @param numberUpMode value
         * @return this builder for method chaining.
         */
        public Builder addNumberUpMode(final NumberUpMode numberUpMode) {
            mNumberUpModeList.add(numberUpMode);
            return this;
        }

        /**
         * Adds a number up direction
         *
         * @param numberUpMode number up mode
         * @param numberUpDirections value
         * @return this builder for method chaining.
         */
        public Builder addNumberUpDirection(final NumberUpMode numberUpMode, final List<NumberUpDirection> numberUpDirections) {
            mNumberUpDirectionMap.put(numberUpMode, numberUpDirections);
            return this;
        }

        /**
         * Adds a password type
         *
         * @param passwordType value
         * @return this builder for method chaining.
         */
        public Builder addPasswordType(final JobCredentialsAttributes.PasswordType passwordType) {
            mPasswordTypeList.add(passwordType);
            return this;
        }

        /**
         * Adds Output-bin option
         *
         * @param outputBin value
         * @return this builder for method chaining.
         */
        public Builder addOutputBin(final OutputBin outputBin) {
            mOutputBinList.add(outputBin);
            return this;
        }


        /**
         * Sets progressDialogMode
         *
         * @param progressDialogMode value
         * @return this builder for method chaining.
         */
        public Builder addProgressDialogMode(final ProgressDialogMode progressDialogMode) {
            mProgressDialogModeList.add(progressDialogMode);
            return this;
        }

        /**
         * Sets stampPosition
         *
         * @param stampPosition value
         * @return this builder for method chaining.
         */
        public Builder addStampOption(final StampPosition stampPosition, final StampOption stampOption) {
            mStampOptionMap.put(stampPosition, stampOption);
            return this;
        }

        /**
         * Sets stapleOption
         *
         * @param stapleOption value
         * @return this builder for method chaining.
         */
        public Builder addStapleOption(final StapleOption stapleOption) {
            mStapleOption.add(stapleOption);
            return this;
        }

        /**
         * Sets punchMode
         *
         * @param punchMode value
         * @return this builder for method chaining.
         */
        public Builder addPunchMode(final PunchMode punchMode) {
            mPunchMode.add(punchMode);
            return this;
        }

        /**
         * Sets foldMode
         *
         * @param foldMode value
         * @return this builder for method chaining.
         */
        public Builder addFoldMode(final FoldMode foldMode) {
            mFoldMode.add(foldMode);
            return this;
        }

        /**
         * EraseMarginUnit mode
         *
         * @param eraseMarginUnit value
         * @return this builder for method chaining.
         */
        public Builder addEraseMarginUnits (final CopyAttributes.EraseMarginUnit eraseMarginUnit) {
            mEraseMarginUnitList.add(eraseMarginUnit);
            return this;
        }
        /** Adds capture mode options
         *
         * @param captureMode value
         * @return this builder for method chaining.
         */
        public Builder addCaptureMode(final CaptureMode captureMode) {
            mCaptureModeList.add(captureMode);
            return this;
        }

        /**
         * EraseBackBottom range
         *
         * @param lowerBound minimum value
         * @param highBound maximum value
         * @return this builder for method chaining.
         */
        public Builder setEraseBackBottomRange(final float lowerBound, final float highBound) {
            mEraseBackBottomRange.mLowerBound = lowerBound;
            mEraseBackBottomRange.mUpperBound = highBound;
            return this;
        }

        /**
         * EraseBackLeft range
         *
         * @param lowerBound minimum value
         * @param highBound maximum value
         * @return this builder for method chaining.
         */
        public Builder setEraseBackLeftRange(final float lowerBound, final float highBound) {
            mEraseBackLeftRange.mLowerBound = lowerBound;
            mEraseBackLeftRange.mUpperBound = highBound;
            return this;
        }

        /**
         * EraseBackRight range
         *
         * @param lowerBound minimum value
         * @param highBound maximum value
         * @return this builder for method chaining.
         */
        public Builder setEraseBackRightRange(final float lowerBound, final float highBound) {
            mEraseBackRightRange.mLowerBound = lowerBound;
            mEraseBackRightRange.mUpperBound = highBound;
            return this;
        }

        /**
         * EraseBackTop range
         *
         * @param lowerBound minimum value
         * @param highBound maximum value
         * @return this builder for method chaining.
         */
        public Builder setEraseBackTopRange(final float lowerBound, final float highBound) {
            mEraseBackTopRange.mLowerBound = lowerBound;
            mEraseBackTopRange.mUpperBound = highBound;
            return this;
        }

        /**
         * EraseFrontBottom range
         *
         * @param lowerBound minimum value
         * @param highBound maximum value
         * @return this builder for method chaining.
         */
        public Builder setEraseFrontBottomRange(final float lowerBound, final float highBound) {
            mEraseFrontBottomRange.mLowerBound = lowerBound;
            mEraseFrontBottomRange.mUpperBound = highBound;
            return this;
        }

        /**
         * EraseFrontLeft range
         *
         * @param lowerBound minimum value
         * @param highBound maximum value
         * @return this builder for method chaining.
         */
        public Builder setEraseFrontLeftRange(final float lowerBound, final float highBound) {
            mEraseFrontLeftRange.mLowerBound = lowerBound;
            mEraseFrontLeftRange.mUpperBound = highBound;
            return this;
        }

        /**
         * EraseFrontRight range
         *
         * @param lowerBound minimum value
         * @param highBound maximum value
         * @return this builder for method chaining.
         */
        public Builder setEraseFrontRightRange(final float lowerBound, final float highBound) {
            mEraseFrontRightRange.mLowerBound = lowerBound;
            mEraseFrontRightRange.mUpperBound = highBound;
            return this;
        }

        /**
         * EraseFrontTop range
         *
         * @param lowerBound minimum value
         * @param highBound maximum value
         * @return this builder for method chaining.
         */
        public Builder setEraseFrontTopRange(final float lowerBound, final float highBound) {
            mEraseFrontTopRange.mLowerBound = lowerBound;
            mEraseFrontTopRange.mUpperBound = highBound;
            return this;
        }

        /**
         * Sets imageShiftReduceToFit
         *
         * @param imageShiftReduceToFit value
         * @return this builder for method chaining.
         */
        public Builder addImageShiftReduceToFit(final ImageShiftReduceToFit imageShiftReduceToFit) {
            mImageShiftReduceToFitList.add(imageShiftReduceToFit);
            return this;
        }

        /**
         * Sets imageShiftUnits
         *
         * @param imageShiftUnits value
         * @return this builder for method chaining.
         */
        public Builder addImageShiftUnits(final ImageShiftUnits imageShiftUnits) {
            mImageShiftUnitsList.add(imageShiftUnits);
            return this;
        }

        /**
         * ImageShiftXFront range
         *
         * @param lowerBound minimum value
         * @param highBound maximum value
         * @return this builder for method chaining.
         */
        public CopyAttributesCapsCreator.Builder setImageShiftXFrontRange(final float lowerBound, final float highBound) {
            mImageShiftXFrontRange.mLowerBound = lowerBound;
            mImageShiftXFrontRange.mUpperBound = highBound;
            return this;
        }

        /**
         * ImageShiftYFront range
         *
         * @param lowerBound minimum value
         * @param highBound maximum value
         * @return this builder for method chaining.
         */
        public CopyAttributesCapsCreator.Builder setImageShiftYFrontRange(final float lowerBound, final float highBound) {
            mImageShiftYFrontRange.mLowerBound = lowerBound;
            mImageShiftYFrontRange.mUpperBound = highBound;
            return this;
        }

        /**
         * ImageShiftXBack range
         *
         * @param lowerBound minimum value
         * @param highBound maximum value
         * @return this builder for method chaining.
         */
        public CopyAttributesCapsCreator.Builder setImageShiftXBackRange(final float lowerBound, final float highBound) {
            mImageShiftXBackRange.mLowerBound = lowerBound;
            mImageShiftXBackRange.mUpperBound = highBound;
            return this;
        }

        /**
         * ImageShiftYBack range
         *
         * @param lowerBound minimum value
         * @param highBound maximum value
         * @return this builder for method chaining.
         */
        public CopyAttributesCapsCreator.Builder setImageShiftYBackRange(final float lowerBound, final float highBound) {
            mImageShiftYBackRange.mLowerBound = lowerBound;
            mImageShiftYBackRange.mUpperBound = highBound;
            return this;
        }

        /**
         * Sets bookletBordersEachPage
         *
         * @param bookletBordersEachPage value
         * @return this builder for method chaining.
         */
        public Builder addBookletBordersEachPage(final BookletBordersEachPage bookletBordersEachPage) {
            mBookletBordersEachPageList.add(bookletBordersEachPage);
            return this;
        }

        /**
         * Sets bookletFinishingOption
         *
         * @param bookletFinishingOption value
         * @return this builder for method chaining.
         */
        public Builder addBookletFinishingOption(final BookletFinishingOption bookletFinishingOption) {
            mBookletFinishingOptionList.add(bookletFinishingOption);
            return this;
        }

        /**
         * Sets bookletFormat
         *
         * @param bookletFormat value
         * @return this builder for method chaining.
         */
        public Builder addBookletFormat(final BookletFormat bookletFormat) {
            mBookletFormatList.add(bookletFormat);
            return this;
        }

        /**
         * Combines all of the capabilities in this into a ScanAttributesCapsCreator object.
         * Sets Transparency range
         *
         * @param lowerBound minimum value
         * @param highBound  maximum value
         * @return this builder for method chaining.
         */
        public Builder setWatermarkTransparencyRange(final int lowerBound, final int highBound) {
            mWatermarkTransparencyRange.mLowerBound = lowerBound;
            mWatermarkTransparencyRange.mUpperBound = highBound;
            return this;
        }

        /**
         * Adds a Watermark OnlyFirstPageList.
         *
         * @param watermarkOnlyFirstPage The watermark rotation value
         * @return this builder for method chaining.
         */
        public Builder addWatermarkOnlyFirstPageList(final CopyAttributes.WatermarkOnlyFirstPage watermarkOnlyFirstPage) {
            mWatermarkOnlyFirstPageList.add(watermarkOnlyFirstPage);
            return this;
        }

        /**
         * Sets darkness range
         *
         * @param lowerBound minimum value
         * @param highBound  maximum value
         * @return this builder for method chaining.
         */
        public Builder setWatermarkDarknessRange(final int lowerBound, final int highBound) {
            mWatermarkDarknessRange.mLowerBound = lowerBound;
            mWatermarkDarknessRange.mUpperBound = highBound;
            return this;
        }
        /**
         * Adds a watermark type.
         *
         * @param watermarkType The watermark type value
         * @return this builder for method chaining.
         */
        public Builder addWatermarkType(final CopyAttributes.WatermarkType watermarkType) {
            mWatermarkTypeList.add(watermarkType);
            return this;
        }

        /**
         * Adds a watermark rotation.
         *
         * @param watermarkRotate45 The watermark rotation value
         * @return this builder for method chaining.
         */
        public Builder addWatermarkRotation45(final CopyAttributes.WatermarkRotate45 watermarkRotate45) {
            mWatermarkRotate45List.add(watermarkRotate45);
            return this;
        }

        /**
         * Adds a watermark messageType.
         *
         * @param watermarkMessageType The watermark message type value
         * @return this builder for method chaining.
         */
        public Builder addWatermarkMessageType(final CopyAttributes.WatermarkMessageType watermarkMessageType) {
            mWatermarkMessageTypeList.add(watermarkMessageType);
            return this;
        }

        /**
         * Adds a watermark background pattern.
         *
         * @param watermarkBackgroundPattern The watermark background pattern value
         * @return this builder for method chaining.
         */
        public Builder addWatermarkBackgroundPattern(final CopyAttributes.WatermarkBackgroundPattern watermarkBackgroundPattern) {
            mWatermarkBackgroundPatternList.add(watermarkBackgroundPattern);
            return this;
        }

        /**
         * Adds a watermark background color.
         *
         * @param watermarkBackgroundColor The watermark background color value
         * @return this builder for method chaining.
         */
        public Builder addWatermarkBackgroundColor(final String watermarkBackgroundColor) {
            mWatermarkBackgroundColorList.add(watermarkBackgroundColor);
            return this;
        }

        /**
         * Adds a watermark font.
         *
         * @param watermarkFont The watermark font value
         * @return this builder for method chaining.
         */
        public Builder addWatermarkFont(final String watermarkFont) {
            mWatermarkFontList.add(watermarkFont);
            return this;
        }

        /**
         * Adds a watermark text color.
         *
         * @param watermarkTextColor The watermark background text color value
         * @return this builder for method chaining.
         */
        public Builder addWatermarkTextColor(final String watermarkTextColor) {
            mWatermarkTextColorList.add(watermarkTextColor);
            return this;
        }

        /**
         * Adds a watermark text size.
         *
         * @param watermarkTextSize The watermark background text size value
         * @return this builder for method chaining.
         */
        public Builder addWatermarkTextSize(final int watermarkTextSize) {
            mWatermarkTextSizeList.add(watermarkTextSize);
            return this;
        }

        /**
         * Combines all of the capabilities in this into a ScanAttributesCapsCreator object.
         *
         * @return a ScanAttributesCapsCreator object containing all of the attributes.
         */
        public CopyAttributesCapsCreator build() {
            return new CopyAttributesCapsCreator(this);
        }
    }
}
