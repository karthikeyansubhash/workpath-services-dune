// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.scanner;

import com.hp.jetadvantage.link.api.scanner.ScanAttributes.BackgroundCleanup;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.BlankImageRemovalMode;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.ColorDropoutMode;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.ColorMode;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.ContrastAdjustment;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.CropMode;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.DarknessAdjustment;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.Destination;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.DocumentFormat;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.Duplex;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.JobAssemblyMode;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.MediaSource;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.MisfeedDetectionMode;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.Orientation;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.OutputQuality;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.ProgressDialogMode;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.Resolution;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.ScanPreview;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.ScanSize;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.SharpnessAdjustment;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.TransmissionMode;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.MediaWeightAdjustment;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.TextPhotoOptimization;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.SplitAttachmentByPage;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.EraseMarginUnit;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.CaptureMode;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.AutomaticToneMode;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.AutomaticStraightenMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Holds the set of attributes provided from the printer when requesting a scan
 *
 * @hide The creation of capabilities is meant for the scanner service to expose a read-only list of capabilities. Clients should not be writing
 * capabilities.
 */
public class ScanAttributesCapsCreator {

    final List<ColorMode> mColorModeList;

    final List<Duplex> mPlexList;

    final List<Destination> mDestinationList;

    final List<DocumentFormat> mMeDocFormatList;

    final List<Orientation> mOrientationList;

    final List<ScanPreview> mScanPreviewList;

    final List<Resolution> mResolutionList;

    final List<ScanSize> mScanSizeList;

    final List<BackgroundCleanup> mBackgroundCleanupList;

    final List<ContrastAdjustment> mContrastAdjustmentList;

    final List<DarknessAdjustment> mDarknessAdjustmentList;

    final List<BlankImageRemovalMode> mBlankImageRemovalModeList;

    final List<ColorDropoutMode> mColorDropoutModeList;

    final List<CropMode> mCropModeList;

    final List<OutputQuality> mOutputQualityList;

    final List<ProgressDialogMode> mProgressDialogModeList;

    final List<TransmissionMode> mTransmissionModeList;

    final Map<ColorMode, List<DocumentFormat>> mDocumentFormatsByColorMode;

    final List<JobAssemblyMode> mJobAssemblyModeList;

    final List<SharpnessAdjustment> mSharpnessAdjustmentList;

    final List<MediaWeightAdjustment> mMediaWeightAdjustmentList;

    final List<TextPhotoOptimization> mTextPhotoOptimizationList;

    final List<MediaSource> mMediaSourceList;

    final List<MisfeedDetectionMode> mMisfeedDetectionModeList;

    final Range mCustomLengthRange;

    final Range mCustomWidthRange;

    final List<SplitAttachmentByPage> mSplitAttachmentByPageList;

    final Range mMaxPagesPerAttachmentRange;

    final List<EraseMarginUnit> mEraseMarginUnitList;

    final Range mEraseBackBottomRange;
    final Range mEraseBackLeftRange;
    final Range mEraseBackRightRange;
    final Range mEraseBackTopRange;
    final Range mEraseFrontBottomRange;
    final Range mEraseFrontLeftRange;
    final Range mEraseFrontRightRange;
    final Range mEraseFrontTopRange;

    final List<CaptureMode> mCaptureModeList;

    final List<AutomaticToneMode> mAutomaticToneModeList;

    final List<AutomaticStraightenMode> mAutomaticStraightenModeList;

    private ScanAttributesCapsCreator(final Builder builder) {
        mColorModeList = builder.mColorModeList;
        mPlexList = builder.mPlexList;
        mDestinationList = builder.mDestinationList;
        mMeDocFormatList = builder.mMeDocFormatList;
        mOrientationList = builder.mOrientationList;
        mScanPreviewList = builder.mScanPreviewList;
        mResolutionList = builder.mResolutionList;
        mScanSizeList = builder.mScanSizeList;
        mBackgroundCleanupList = builder.mBackgroundCleanupList;
        mContrastAdjustmentList = builder.mContrastAdjustmentList;
        mDarknessAdjustmentList = builder.mDarknessAdjustmentList;
        mBlankImageRemovalModeList = builder.mBlankImageRemovalModeList;
        mColorDropoutModeList = builder.mColorDropoutModeList;
        mCropModeList = builder.mCropModeList;
        mOutputQualityList = builder.mOutputQualityList;
        mProgressDialogModeList = builder.mProgressDialogModeList;
        mTransmissionModeList = builder.mTransmissionModeList;
        mDocumentFormatsByColorMode = builder.mDocumentFormatsByColorMode;
        mJobAssemblyModeList = builder.mJobAssemblyModeList;
        mSharpnessAdjustmentList = builder.mSharpnessAdjustmentList;
        mMediaWeightAdjustmentList = builder.mMediaWeightAdjustmentList;
        mTextPhotoOptimizationList = builder.mTextPhotoOptimizationList;
        mMediaSourceList = builder.mMediaSourceList;
        mMisfeedDetectionModeList = builder.mMisfeedDetectionModeList;
        mCustomLengthRange = builder.mCustomLengthRange;
        mCustomWidthRange = builder.mCustomWidthRange;
        // ScanTicket3 Support
        mSplitAttachmentByPageList = builder.mSplitAttachmentByPageList;
        mMaxPagesPerAttachmentRange = builder.mMaxPagesPerAttachmentRange;
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
        mAutomaticToneModeList = builder.mAutomaticToneModeList;
        mAutomaticStraightenModeList = builder.mAutomaticStraightenModeList;
    }

    /**
     * Builder for determining the attributes supported for requesting images to be sent back to the client.
     */
    public static class Builder {
        final List<ColorMode> mColorModeList = new ArrayList<>();

        final List<Duplex> mPlexList = new ArrayList<>();

        final List<Destination> mDestinationList = new ArrayList<>();

        final List<DocumentFormat> mMeDocFormatList = new ArrayList<>();

        final List<Orientation> mOrientationList = new ArrayList<>();

        final List<ScanPreview> mScanPreviewList = new ArrayList<>();

        final List<Resolution> mResolutionList = new ArrayList<>();

        final List<ScanSize> mScanSizeList = new ArrayList<>();

        final List<BackgroundCleanup> mBackgroundCleanupList = new ArrayList<>();

        final List<ContrastAdjustment> mContrastAdjustmentList = new ArrayList<>();

        final List<DarknessAdjustment> mDarknessAdjustmentList = new ArrayList<>();

        final List<BlankImageRemovalMode> mBlankImageRemovalModeList = new ArrayList<>();

        final List<ColorDropoutMode> mColorDropoutModeList = new ArrayList<>();

        final List<CropMode> mCropModeList = new ArrayList<>();

        final List<OutputQuality> mOutputQualityList = new ArrayList<>();

        final List<ProgressDialogMode> mProgressDialogModeList = new ArrayList<>();

        final List<TransmissionMode> mTransmissionModeList = new ArrayList<>();

        final Map<ColorMode, List<DocumentFormat>> mDocumentFormatsByColorMode = new HashMap<>();

        final List<JobAssemblyMode> mJobAssemblyModeList = new ArrayList<>();

        final List<SharpnessAdjustment> mSharpnessAdjustmentList = new ArrayList<>();

        final List<MediaWeightAdjustment> mMediaWeightAdjustmentList = new ArrayList<>();

        final List<TextPhotoOptimization> mTextPhotoOptimizationList = new ArrayList<>();

        final List<MediaSource> mMediaSourceList = new ArrayList<>();

        final List<MisfeedDetectionMode> mMisfeedDetectionModeList = new ArrayList<>();

        final Range mCustomLengthRange = new Range(0, 0);

        final Range mCustomWidthRange = new Range(0, 0);

        final List<SplitAttachmentByPage> mSplitAttachmentByPageList = new ArrayList<>();

        final Range mMaxPagesPerAttachmentRange = new Range(0, 0);

        final List<EraseMarginUnit> mEraseMarginUnitList = new ArrayList<>();

        final Range mEraseBackBottomRange = new Range(0, 0);
        final Range mEraseBackLeftRange = new Range(0, 0);
        final Range mEraseBackRightRange = new Range(0, 0);
        final Range mEraseBackTopRange = new Range(0, 0);
        final Range mEraseFrontBottomRange = new Range(0, 0);
        final Range mEraseFrontLeftRange = new Range(0, 0);
        final Range mEraseFrontRightRange = new Range(0, 0);
        final Range mEraseFrontTopRange = new Range(0, 0);

        final List<CaptureMode> mCaptureModeList = new ArrayList<>();

        final List<AutomaticToneMode> mAutomaticToneModeList = new ArrayList<>();

        final List<AutomaticStraightenMode> mAutomaticStraightenModeList = new ArrayList<>();
        /**
         * Constructs a new builder used for determining the attributes supported for scanned images back to the client.
         */
        public Builder() {
            // Empty constructor
            mColorModeList.add(ColorMode.DEFAULT);
            mPlexList.add(Duplex.DEFAULT);
            mMeDocFormatList.add(DocumentFormat.DEFAULT);
            mOrientationList.add(Orientation.DEFAULT);
            mResolutionList.add(Resolution.DEFAULT);
            mScanPreviewList.add(ScanPreview.DEFAULT);
            mScanSizeList.add(ScanSize.DEFAULT);
            mBackgroundCleanupList.add(BackgroundCleanup.DEFAULT);
            mContrastAdjustmentList.add(ContrastAdjustment.DEFAULT);
            mDarknessAdjustmentList.add(DarknessAdjustment.DEFAULT);
            mBlankImageRemovalModeList.add(BlankImageRemovalMode.DEFAULT);
            mColorDropoutModeList.add(ColorDropoutMode.DEFAULT);
            mCropModeList.add(CropMode.DEFAULT);
            mOutputQualityList.add(OutputQuality.DEFAULT);
            mProgressDialogModeList.add(ProgressDialogMode.DEFAULT);
            mTransmissionModeList.add(TransmissionMode.DEFAULT);
            mDocumentFormatsByColorMode.put(ColorMode.DEFAULT, Collections.singletonList(DocumentFormat.DEFAULT));
            mJobAssemblyModeList.add(JobAssemblyMode.DEFAULT);
            mSharpnessAdjustmentList.add(SharpnessAdjustment.DEFAULT);
            mMediaWeightAdjustmentList.add(MediaWeightAdjustment.DEFAULT);
            mTextPhotoOptimizationList.add(TextPhotoOptimization.DEFAULT);
            mMediaSourceList.add(MediaSource.DEFAULT);
            mMisfeedDetectionModeList.add(MisfeedDetectionMode.DEFAULT);

            mSplitAttachmentByPageList.add(SplitAttachmentByPage.DEFAULT);
            mEraseMarginUnitList.add(EraseMarginUnit.DEFAULT);
            mCaptureModeList.add(CaptureMode.DEFAULT);
            mAutomaticToneModeList.add(AutomaticToneMode.DEFAULT);
            mAutomaticStraightenModeList.add(AutomaticStraightenMode.DEFAULT);
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
         * Adds a duplex set in which the scanned image(s) will be supplied.
         *
         * @param plex The Duplex set.
         * @return this builder for method chaining.
         */
        public Builder addPlex(final Duplex plex) {
            mPlexList.add(plex);
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
         * Adds a destination that the printer can scan to.
         *
         * @param destination The destination that the printer can scan to.
         * @return this builder for method chaining.
         */
        public Builder addDestination(final Destination destination) {
            mDestinationList.add(destination);
            return this;
        }

        /**
         * Adds an output format for me destinations in which the scanned image(s) can be supplied.
         *
         * @param documentFormat The output format
         * @return this builder for method chaining.
         */
        public Builder addMeDocumentFormat(final DocumentFormat documentFormat) {
            mMeDocFormatList.add(documentFormat);
            return this;
        }

        /**
         * Adds scanPreview
         *
         * @param scanPreview The scan preview value
         * @return this builder for method chaining.
         */
        public Builder addScanPreview(final ScanPreview scanPreview) {
            mScanPreviewList.add(scanPreview);
            return this;
        }

        /**
         * Adds Resolution
         *
         * @param resolution Resolution value
         * @return this builder for method chaining.
         */
        public Builder addResolution(final Resolution resolution) {
            mResolutionList.add(resolution);
            return this;
        }

        /**
         * Adds ScanSize
         *
         * @param scanSize Resolution value
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
        public Builder setCustomLengthRange(final float lowerBound, final float upperBound) {
            mCustomLengthRange.mLowerBound = lowerBound;
            mCustomLengthRange.mUpperBound = upperBound;
            return this;
        }

        /**
         * Sets scan size custom width range
         *
         * @param lowerBound minimum value
         * @param upperBound maximum value
         * @return this builder for method chaining.
         */
        public Builder setCustomWidthRange(final float lowerBound, final float upperBound) {
            mCustomWidthRange.mLowerBound = lowerBound;
            mCustomWidthRange.mUpperBound = upperBound;
            return this;
        }

        /**
         * Adds background cleanup
         *
         * @param backgroundCleanup BackgroundCleanup value
         * @return this builder for method chaining.
         */
        public Builder addBackgroundCleanup(final BackgroundCleanup backgroundCleanup) {
            mBackgroundCleanupList.add(backgroundCleanup);
            return this;
        }

        /**
         * Adds contrast adjustment
         *
         * @param contrastAdjustment BackgroundCleanup value
         * @return this builder for method chaining.
         */
        public Builder addContrastAdjustment(final ContrastAdjustment contrastAdjustment) {
            mContrastAdjustmentList.add(contrastAdjustment);
            return this;
        }

        /**
         * Adds darkness adjustment
         *
         * @param darknessAdjustment BackgroundCleanup value
         * @return this builder for method chaining.
         */
        public Builder addDarknessAdjustment(final DarknessAdjustment darknessAdjustment) {
            mDarknessAdjustmentList.add(darknessAdjustment);
            return this;
        }

        /**
         * Adds blank image removal mode
         *
         * @param blankImageRemovalMode value
         * @return this builder for method chaining.
         */
        public Builder addBlankImageRemovalMode(final BlankImageRemovalMode blankImageRemovalMode) {
            mBlankImageRemovalModeList.add(blankImageRemovalMode);
            return this;
        }

        /**
         * Adds color dropout mode
         *
         * @param colorDropoutMode value
         * @return this builder for method chaining.
         */
        public Builder addColorDropoutMode(final ColorDropoutMode colorDropoutMode) {
            mColorDropoutModeList.add(colorDropoutMode);
            return this;
        }

        /**
         * Adds crop mode
         *
         * @param cropMode value
         * @return this builder for method chaining.
         */
        public Builder addCropMode(final CropMode cropMode) {
            mCropModeList.add(cropMode);
            return this;
        }

        /**
         * Adds output quality
         *
         * @param outputQuality value
         * @return this builder for method chaining.
         */
        public Builder addOutputQuality(final OutputQuality outputQuality) {
            mOutputQualityList.add(outputQuality);
            return this;
        }

        /**
         * Adds progress dialog mode
         *
         * @param progressDialogMode value
         * @return this builder for method chaining.
         */
        public Builder addProgressDialogMode(final ProgressDialogMode progressDialogMode) {
            mProgressDialogModeList.add(progressDialogMode);
            return this;
        }

        /**
         * Adds transmission mode
         *
         * @param transmissionMode value
         * @return this builder for method chaining.
         */
        public Builder addTransmissionMode(final TransmissionMode transmissionMode) {
            mTransmissionModeList.add(transmissionMode);
            return this;
        }

        /**
         * Adds document formats list for color mode
         *
         * @param colorMode color mode
         * @param documentFormats list of supported document format for that color mode
         * @return this builder for method chaining.
         */
        public Builder addDocumentFormatsByColorMode(final ColorMode colorMode,
                                                     final List<DocumentFormat> documentFormats) {
            mDocumentFormatsByColorMode.put(colorMode, documentFormats);
            return this;
        }

        /**
         * Adds job assembly mode
         *
         * @param jobAssemblyMode value
         * @return this builder for method chaining.
         */
        public Builder addJobAssemblyMode(final JobAssemblyMode jobAssemblyMode) {
            mJobAssemblyModeList.add(jobAssemblyMode);
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
         * Adds media weight adjustment
         *
         * @param mediaWeightAdjustment value
         * @return this builder for method chaining.
         */
        public Builder addMediaWeightAdjustment(final MediaWeightAdjustment mediaWeightAdjustment) {
            mMediaWeightAdjustmentList.add(mediaWeightAdjustment);
            return this;
        }

        /**
         * Adds text photo optimization
         *
         * @param textPhotoOptimization value
         * @return this builder for method chaining.
         */
        public Builder addTextPhotoOptimization(final TextPhotoOptimization textPhotoOptimization) {
            mTextPhotoOptimizationList.add(textPhotoOptimization);
            return this;
        }

        /**
         * Adds media source
         *
         * @param mediaSource value
         * @return this builder for method chaining.
         */
        public Builder addMediaSource (final MediaSource mediaSource) {
            mMediaSourceList.add(mediaSource);
            return this;
        }

        /**
         * Adds misfeed detection mode
         *
         * @param misfeedDetectionMode value
         * @return this builder for method chaining.
         */
        public Builder addMisfeedDetectionMode (final MisfeedDetectionMode misfeedDetectionMode) {
            mMisfeedDetectionModeList.add(misfeedDetectionMode);
            return this;
        }

        /**
         * SplitAttachmentByPage mode
         *
         * @param splitAttachmentByPage value
         * @return this builder for method chaining.
         */
        public Builder addSplitAttachmentByPage (final SplitAttachmentByPage splitAttachmentByPage) {
            mSplitAttachmentByPageList.add(splitAttachmentByPage);
            return this;
        }

        /**
         * MaxPagesPerAttachmentRange range
         *
         * @param lowerBound minimum value
         * @param highBound maximum value
         * @return this builder for method chaining.
         */
        public Builder setMaxPagesPerAttachmentRange(final int lowerBound, final int highBound) {
            mMaxPagesPerAttachmentRange.mLowerBound = lowerBound;
            mMaxPagesPerAttachmentRange.mUpperBound = highBound;
            return this;
        }

        /**
         * EraseMarginUnit mode
         *
         * @param eraseMarginUnit value
         * @return this builder for method chaining.
         */
        public Builder addEraseMarginUnits(final EraseMarginUnit eraseMarginUnit) {
            mEraseMarginUnitList.add(eraseMarginUnit);
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
         * CaptureMode mode
         *
         * @param captureMode value
         * @return this builder for method chaining.
         */
        public Builder addCaptureMode (final CaptureMode captureMode) {
            mCaptureModeList.add(captureMode);
            return this;
        }

        /**
         * AutomaticToneMode mode
         *
         * @param automaticToneMode value
         * @return this builder for method chaining.
         */
        public Builder addAutomaticToneMode (final AutomaticToneMode automaticToneMode) {
            mAutomaticToneModeList.add(automaticToneMode);
            return this;
        }

        /**
         * AutomaticStraightenMode mode
         *
         * @param automaticStraightenMode value
         * @return this builder for method chaining.
         */
        public Builder addAutomaticStraightenMode (final AutomaticStraightenMode automaticStraightenMode) {
            mAutomaticStraightenModeList.add(automaticStraightenMode);
            return this;
        }

        /**
         * Combine all of the capabilities in this into a ScanAttributesCapsCreator object.
         *
         * @return a ScanAttributesCapsCreator object containing all of the attributes.
         */
        public ScanAttributesCapsCreator build() {
            return new ScanAttributesCapsCreator(this);
        }
    }
}
