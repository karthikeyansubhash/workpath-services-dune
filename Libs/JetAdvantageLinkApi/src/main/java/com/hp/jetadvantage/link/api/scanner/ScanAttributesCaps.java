// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.scanner;

import android.annotation.SuppressLint;

import androidx.core.util.Preconditions;

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
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.MediaWeightAdjustment;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.Orientation;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.OutputQuality;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.ProgressDialogMode;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.Resolution;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.ScanPreview;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.ScanSize;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.SharpnessAdjustment;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.TextPhotoOptimization;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.TransmissionMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Scan attributes capabilities provided available values from the device.
 *
 * @since API 1
 */
public final class ScanAttributesCaps {
    final ScanAttributesCapsCreator mCapsCreator;

    /**
     * Constructor to create default object
     *
     * @param creator object containing the scan capabilities
     * @hide The creator is hidden
     * @since API 1
     */
    public ScanAttributesCaps(final ScanAttributesCapsCreator creator) {
        mCapsCreator = creator;
    }

    /**
     * Gets the groups of color sets supported by the device.
     *
     * @return the color modes supported.
     * @since API 1
     */
    public List<ColorMode> getColorModeList() {
        return Collections.unmodifiableList(mCapsCreator.mColorModeList);
    }

    /**
     * Gets duplex options supported by the device
     *
     * @return List of duplex options.
     * @since API 1
     */
    public List<Duplex> getDuplexList() {
        return Collections.unmodifiableList(mCapsCreator.mPlexList);
    }

    /**
     * The destinations to which the scanned images can be sent.
     *
     * @return the supported destinations.
     * @since API 1
     */
    public List<Destination> getDestinationList() {
        return Collections.unmodifiableList(mCapsCreator.mDestinationList);
    }

    /**
     * The output formats in which the scanned images can be created.
     * The supported format are destination-specific.
     *
     * @param destination type of destination.
     * @return the output formats supported.
     * @since API 1
     */
    @SuppressLint("RestrictedApi")
    public List<DocumentFormat> getDocumentFormatList(final Destination destination) {
        Preconditions.checkNotNull(destination, "Destination must be provided");

        switch (destination) {
            /*
             * For the extension like scan to email.
             */
            case ME:
            default:
                Collections.sort(mCapsCreator.mMeDocFormatList);
                mCapsCreator.mMeDocFormatList.remove(DocumentFormat.DEFAULT);
                mCapsCreator.mMeDocFormatList.add(0, DocumentFormat.DEFAULT);

                return Collections.unmodifiableList(mCapsCreator.mMeDocFormatList);
        }
    }

    /**
     * A list of supported scan sizes.
     *
     * @return scan sizes list.
     * @since API 1
     */
    public List<ScanSize> getScanSizeList() {
        Collections.sort(mCapsCreator.mScanSizeList);

        mCapsCreator.mScanSizeList.remove(ScanSize.DEFAULT);
        mCapsCreator.mScanSizeList.add(0, ScanSize.DEFAULT);

        mCapsCreator.mScanSizeList.remove(ScanSize.CUSTOM);
        mCapsCreator.mScanSizeList.add(ScanSize.CUSTOM);

        return Collections.unmodifiableList(mCapsCreator.mScanSizeList);
    }

    /**
     * Gets a range of custom length for scan size.
     *
     * @return range for custom length.
     * @since API 1
     */
    public Range getCustomLengthRange() {
        return mCapsCreator.mCustomLengthRange;
    }

    /**
     * Gets a range of custom width for scan size.
     *
     * @return range for custom width.
     * @since API 1
     */
    public Range getCustomWidthRange() {
        return mCapsCreator.mCustomWidthRange;
    }

    /**
     * Gets the supported original orientation options by the device
     *
     * @return supported {@link ScanAttributes.Orientation} list
     * @since API 1
     */
    public List<Orientation> getOrientationList() {
        return Collections.unmodifiableList(mCapsCreator.mOrientationList);
    }

    /**
     * Gets the supported options for scan preview by the device
     *
     * @return supported {@link ScanAttributes.ScanPreview} list
     * @since API 1
     */
    public List<ScanPreview> getScanPreviewList() {
        return Collections.unmodifiableList(mCapsCreator.mScanPreviewList);
    }

    /**
     * Gets the supported scan resolution options by the device
     *
     * @return supported {@link ScanAttributes.Resolution} list
     * @since API 1
     */
    public List<Resolution> getResolutionList() {
        return Collections.unmodifiableList(mCapsCreator.mResolutionList);
    }

    /**
     * Gets the supported scan background cleanup options by the device
     *
     * @return supported {@link ScanAttributes.BackgroundCleanup} list
     * @since API 1
     */
    public List<BackgroundCleanup> getBackgroundCleanupList() {
        return Collections.unmodifiableList(mCapsCreator.mBackgroundCleanupList);
    }

    /**
     * Gets the supported scan contrast adjustment options by the device
     *
     * @return supported {@link ScanAttributes.ContrastAdjustment} list
     * @since API 1
     */
    public List<ContrastAdjustment> getContrastAdjustmentList() {
        return Collections.unmodifiableList(mCapsCreator.mContrastAdjustmentList);
    }

    /**
     * Gets the supported scan darkness adjustment options by the device
     *
     * @return supported {@link ScanAttributes.DarknessAdjustment} list
     * @since API 1
     */
    public List<DarknessAdjustment> getDarknessAdjustmentList() {
        return Collections.unmodifiableList(mCapsCreator.mDarknessAdjustmentList);
    }

    /**
     * Gets the supported scan blank image removal modes by the device
     *
     * @return supported {@link ScanAttributes.BlankImageRemovalMode} list
     * @since API 1
     */
    public List<BlankImageRemovalMode> getBlankImageRemovalModeList() {
        return Collections.unmodifiableList(mCapsCreator.mBlankImageRemovalModeList);
    }

    /**
     * Gets the supported scan color dropout modes by the device
     *
     * @return supported {@link ScanAttributes.ColorDropoutMode} list
     * @since API 1
     */
    public List<ColorDropoutMode> getColorDropoutModeList() {
        return Collections.unmodifiableList(mCapsCreator.mColorDropoutModeList);
    }

    /**
     * Gets the supported scan crop modes by the device
     *
     * @return supported {@link ScanAttributes.CropMode} list
     * @since API 1
     */
    public List<CropMode> getCropModeList() {
        return Collections.unmodifiableList(mCapsCreator.mCropModeList);
    }

    /**
     * Gets the supported modes for using scan progress dialog by the device
     *
     * @return supported {@link ScanAttributes.ProgressDialogMode} list
     * @since API 1
     */
    public List<ProgressDialogMode> getProgressDialogModeList() {
        return Collections.unmodifiableList(mCapsCreator.mProgressDialogModeList);
    }

    /**
     * Gets the supported scan output qualities by the device
     *
     * @return supported {@link ScanAttributes.OutputQuality} list
     * @since API 1
     */
    public List<OutputQuality> getOutputQualityList() {
        return Collections.unmodifiableList(mCapsCreator.mOutputQualityList);
    }

    /**
     * Gets the supported scan transmission modes by the device
     *
     * @return supported {@link ScanAttributes.TransmissionMode} list
     * @since API 1
     */
    public List<TransmissionMode> getTransmissionModeList() {
        return Collections.unmodifiableList(mCapsCreator.mTransmissionModeList);
    }

    /**
     * Gets the list of supported document formats for each color mode
     *
     * @return supported {@link ScanAttributes.DocumentFormat} list
     * @since API 1
     */
    public Map<ColorMode, List<DocumentFormat>> getDocumentFormatsByColorMode() {
        return Collections.unmodifiableMap(mCapsCreator.mDocumentFormatsByColorMode);
    }
    /**
     * Gets the supported job assembly modes by the device
     *
     * @return supported {@link ScanAttributes.JobAssemblyMode} list
     * @since API 1
     */
    public List<JobAssemblyMode> getJobAssemblyModeList() {
        return Collections.unmodifiableList(mCapsCreator.mJobAssemblyModeList);
    }

    /**
     * Gets the supported sharpness adjustment options by the device
     *
     * @return supported {@link ScanAttributes.SharpnessAdjustment} list
     * @since API 1
     */
    public List<SharpnessAdjustment> getSharpnessAdjustmentList() {
        return Collections.unmodifiableList(mCapsCreator.mSharpnessAdjustmentList);
    }

    /**
     * Gets the supported media weight adjustment options by the device
     *
     * @return supported {@link ScanAttributes.MediaWeightAdjustment} list
     * @since API 1
     */
    public List<MediaWeightAdjustment> getMediaWeightAdjustmentList() {
        return Collections.unmodifiableList(mCapsCreator.mMediaWeightAdjustmentList);
    }

    /**
     * Gets the supported text/photo optimization options by the device
     *
     * @return supported {@link ScanAttributes.TextPhotoOptimization} list
     * @since API 1
     */
    public List<TextPhotoOptimization> getTextPhotoOptimizationList() {
        return Collections.unmodifiableList(mCapsCreator.mTextPhotoOptimizationList);
    }

	/**
     * Gets the supported media source options by the device
     *
     * @return supported {@link ScanAttributes.MediaSource} list
     * @since API 1
     */
    public List<ScanAttributes.MediaSource> getMediaSourceList() {
        return Collections.unmodifiableList(mCapsCreator.mMediaSourceList);
    }

    /**
     * Gets the supported misfeed detection modes by the device
     *
     * @return supported {@link ScanAttributes.MisfeedDetectionMode} list
     * @since API 1
     */
    public List<ScanAttributes.MisfeedDetectionMode> getMisfeedDetectionModeList() {
        return Collections.unmodifiableList(mCapsCreator.mMisfeedDetectionModeList);
    }

    /**
     * Gets the supported scan Split Attachment By Pages by the device
     *
     * @return supported {@link ScanAttributes.SplitAttachmentByPage} list
     * @since API 5
     */
    public List<ScanAttributes.SplitAttachmentByPage> getSplitAttachmentByPageList() {
        if (mCapsCreator.mSplitAttachmentByPageList == null) {
            return new ArrayList<>();
        }
        return Collections.unmodifiableList(mCapsCreator.mSplitAttachmentByPageList);
    }

    /**
     * Gets a range of max pages per attachment range.
     *
     * @return range for max pages per attachment range
     * @since API 5
     */
    public Range getMaxPagesPerAttachmentRange() {
        return mCapsCreator.mMaxPagesPerAttachmentRange;
    }

    /**
     * Gets the supported scan EraseMarginUnits by the device
     *
     * @return supported {@link ScanAttributes.EraseMarginUnit} list
     * @since API 5
     */
    public List<ScanAttributes.EraseMarginUnit> getEraseMarginUnitList() {
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
    public Range getEraseBackBottomRange() {
        return mCapsCreator.mEraseBackBottomRange;
    }

    /**
     * Gets a range of EraseBackLeftRange.
     *
     * @return range for EraseBackLeftRange
     * @since API 5
     */
    public Range getEraseBackLeftRange() {
        return mCapsCreator.mEraseBackLeftRange;
    }

    /**
     * Gets a range of EraseBackRightRange
     *
     * @return range for EraseBackRightRange
     * @since API 5
     */
    public Range getEraseBackRightRange() {
        return mCapsCreator.mEraseBackRightRange;
    }

    /**
     * Gets a range of EraseBackTopRange
     *
     * @return range for EraseBackTopRange
     * @since API 5
     */
    public Range getEraseBackTopRange() {
        return mCapsCreator.mEraseBackTopRange;
    }

    /**
     * Gets a range of EraseFrontBottomRange
     *
     * @return range for EraseFrontBottomRange
     * @since API 5
     */
    public Range getEraseFrontBottomRange() {
        return mCapsCreator.mEraseFrontBottomRange;
    }

    /**
     * Gets a range of EraseFrontLeftRange
     *
     * @return range for EraseFrontLeftRange
     * @since API 5
     */
    public Range getEraseFrontLeftRange() {
        return mCapsCreator.mEraseFrontLeftRange;
    }

    /**
     * Gets a range of EraseFrontRightRange
     *
     * @return range for EraseFrontRightRange
     * @since API 5
     */
    public Range getEraseFrontRightRange() {
        return mCapsCreator.mEraseFrontRightRange;
    }

    /**
     * Gets a range of EraseFrontTopRange
     *
     * @return range for EraseFrontTopRange
     * @since API 5
     */
    public Range getEraseFrontTopRange() {
        return mCapsCreator.mEraseFrontTopRange;
    }

    /**
     * Gets the supported scan CaptureMode by the device
     *
     * @return supported {@link ScanAttributes.CaptureMode} list
     * @since API 5
     */
    public List<ScanAttributes.CaptureMode> getCaptureModeList() {
        if (mCapsCreator.mCaptureModeList == null) {
            return new ArrayList<>();
        }
        return Collections.unmodifiableList(mCapsCreator.mCaptureModeList);
    }

    /**
     * Gets the supported scan AutomaticToneMode by the device
     *
     * @return supported {@link ScanAttributes.AutomaticToneMode} list
     * @since API 5
     */
    public List<ScanAttributes.AutomaticToneMode> getAutomaticToneModeList() {
        if (mCapsCreator.mAutomaticToneModeList == null) {
            return new ArrayList<>();
        }
        return Collections.unmodifiableList(mCapsCreator.mAutomaticToneModeList);
    }

    /**
     * Gets the supported scan AutomaticStraightenMode by the device
     *
     * @return supported {@link ScanAttributes.AutomaticStraightenMode} list
     * @since API 5
     */
    public List<ScanAttributes.AutomaticStraightenMode> getAutomaticStraightenModeList() {
        if (mCapsCreator.mAutomaticStraightenModeList == null) {
            return new ArrayList<>();
        }
        return Collections.unmodifiableList(mCapsCreator.mAutomaticStraightenModeList);
    }
}
