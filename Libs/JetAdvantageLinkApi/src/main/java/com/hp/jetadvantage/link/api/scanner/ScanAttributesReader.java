// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.scanner;

import android.net.Uri;

import androidx.annotation.Keep;

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
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.MediaWeightAdjustment;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.MisfeedDetectionMode;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.Orientation;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.OutputQuality;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.ProgressDialogMode;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.Resolution;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.ScanPreview;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.ScanSize;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.SharpnessAdjustment;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.TextPhotoOptimization;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.TransmissionMode;

/**
 * Reads attributes from requested scan job
 *
 * @since API 1
 */
@SuppressWarnings({"unused"})
public class ScanAttributesReader {
    @Keep
    private final ScanAttributes mAttrs;

    /**
     * Constructor
     *
     * @param scanAttrs The scan attributes used to construct this.
     * @since API 1
     */
    @Keep
    public ScanAttributesReader(final ScanAttributes scanAttrs) {
        mAttrs = scanAttrs;
    }

    /**
     * The version in which the attributes were created.
     *
     * @return The version in which the attributes were created.
     * @hide for internal use
     * @since API 1
     */
    public int getVersion() {
        return mAttrs.mVersion;
    }

    /**
     * Gets the color mode of the scan job.
     *
     * @return color mode
     * @since API 1
     */
    @Keep
    public ColorMode getColorMode() {
        return mAttrs.mColorMode;
    }

    /**
     * Gets the duplex mode of the scan job.
     *
     * @return duplex mode
     * @since API 1
     */
    @Keep
    public Duplex getPlex() {
        return mAttrs.mPlex;
    }

    /**
     * Gets the destination of the scan job.
     *
     * @return destination for scanned files
     * @since API 1
     */
    @Keep
    public Destination getDestination() {
        return mAttrs.mDestination;
    }

    /**
     * Gets the original orientation of the scan job.
     *
     * @return original orientation
     * @since API 1
     */
    @Keep
    public Orientation getOrientation() {
        return mAttrs.mOrientation;
    }

    /**
     * Gets the size of the scanned region.
     *
     * @return scan size type.
     * @since API 1
     */
    @Keep
    public ScanSize getScanSize() {
        return mAttrs.mScanSize;
    }

    /**
     * Gets the custom length of the scanned region
     *
     * @return custom length in inches.
     * @since API 1
     */
    @Keep
    public Float getCustomLength() {
        return mAttrs.mCustomLength;
    }

    /**
     * Gets the custom width of the scanned region
     *
     * @return custom width in inches.
     * @since API 1
     */
    @Keep
    public Float getCustomWidth() {
        return mAttrs.mCustomWidth;
    }

    /**
     * Gets the output format of the scanned image(s).
     *
     * @return the output format of the scanned image(s), as a string.
     * @since API 1
     */
    @Keep
    public DocumentFormat getDocumentFormat() {
        return mAttrs.mDocFormat;
    }

    /**
     * Gets the resolution to be used for the scanned image(s).
     *
     * @return the resolution to be used for the scanned image(s).
     * @since API 1
     */
    @Keep
    public Resolution getResolution() {
        return mAttrs.mResolutionType;
    }

    /**
     * Gets the preview mode of system dialog
     * @return selected scan preview option
     * @since API 1
     */
    @Keep
    public ScanPreview getScanPreview() {
        return mAttrs.mScanPreview;
    }

    /**
     * Gets folder name to be stored scanned image(s).
     * @return folder path
     * @since API 1
     */
    @Keep
    public String getFolderName() {
        return mAttrs.mFolderName;
    }

    /**
     * Gets the option of background cleanup
     * @return selected background cleanup option
     * @since API 1
     */
    @Keep
    public BackgroundCleanup getBackgroundCleanup() {
        return mAttrs.mBackgroundCleanup;
    }

    /**
     * Gets contrast adjustment option of the scan job
     * @return selected contrast adjustment option
     * @since API 1
     */
    @Keep
    public ContrastAdjustment getContrastAdjustment() {
        return mAttrs.mContrastAdjustment;
    }

    /**
     * Gets darkness adjustment option of the scan job
     * @return selected darkness adjustment option
     * @since API 1
     */
    @Keep
    public DarknessAdjustment getDarknessAdjustment() {
        return mAttrs.mDarknessAdjustment;
    }

    /**
     * Gets the value which blank image is removed or not
     * @return selected blank image removal mode option
     * @since API 1
     */
    @Keep
    public BlankImageRemovalMode getBlankImageRemovalMode() {
        return mAttrs.mBlankImageRemovalMode;
    }

    /**
     * Gets color dropout option of the scan job
     * @return selected color dropout mode option
     * @since API 1
     */
    @Keep
    public ColorDropoutMode getColorDropoutMode() {
        return mAttrs.mColorDropoutMode;
    }

    /**
     * Gets the crop mode of the scan job
     * @return selected crop mode option
     * @since API 1
     */
    @Keep
    public CropMode getCropMode() {
        return mAttrs.mCropMode;
    }

    /**
     * Gets the progress dialog option
     * @return selected progress dialog mode option
     * @since API 1
     */
    @Keep
    public ProgressDialogMode getProgressDialogMode() {
        return mAttrs.mProgressDialogMode;
    }

    /**
     * Gets the output quality option of the scan job
     * @return selected output quality option
     * @since API 1
     */
    @Keep
    public OutputQuality getOutputQuality() {
        return mAttrs.mOutputQuality;
    }

    /**
     * Gets the transmission option of the scan job
     * @return selected transmission mode option
     * @since API 1
     */
    @Keep
    public TransmissionMode getTransmissionMode() {
        return mAttrs.mTransmissionMode;
    }

    /**
     * Gets the assembly mode of the scan job
     * @return selected job assembly mode option
     * @since API 1
     */
    @Keep
    public JobAssemblyMode getJobAssemblyMode() {
        return mAttrs.mJobAssemblyMode;
    }

    /**
     * Gets the sharpness adjustment option of the scan job
     * @return selected sharpness adjustment option
     * @since API 1
     */
    @Keep
    public SharpnessAdjustment getSharpnessAdjustment() {
        return mAttrs.mSharpnessAdjustment;
    }

    /**
     * Gets the media weight adjustment option of the scan job
     * @return selected media weight adjustment option
     * @since API 1
     */
    @Keep
    public MediaWeightAdjustment getMediaWeightAdjustment() {
        return mAttrs.mMediaWeightAdjustment;
    }

    /**
     * Gets the text/photo optimization option of the scan job
     * @return selected text/photo optimization option
     * @since API 1
     */
    @Keep
    public TextPhotoOptimization getTextPhotoOptimization() {
        return mAttrs.mTextPhotoOptimization;
    }

    /**
     * Gets the media source of the scan job
     * @return selected media source option
     * @since API 1
     */
    @Keep
    public MediaSource getMediaSource() { return mAttrs.mMediaSource; }

    /**
     * Gets the misfeed detection mode of the scan job
     * @return selected misfeed detection mode option
     * @since API 1
     */
    @Keep
    public MisfeedDetectionMode getMisfeedDetectionMode() { return mAttrs.mMisfeedDetectionMode; }

    /**
     * SplitAttachmentByPage
     * @return SplitAttachmentByPage
     * @since API 5
     */
    @Keep
    public ScanAttributes.SplitAttachmentByPage getSplitAttachmentByPage() { return mAttrs.mSplitAttachmentByPage; }

    /**
     * MaxPagesPerAttachment
     * @return MaxPagesPerAttachment
     * @since API 5
     */
    @Keep
    public Integer getMaxPagesPerAttachment() { return mAttrs.mMaxPagesPerAttachment; }

    /**
     * EraseMarginUnit
     * @return EraseMarginUnit
     * @since API 5
     */
    @Keep
    public ScanAttributes.EraseMarginUnit getEraseMarginUnit() { return mAttrs.mEraseMarginUnit; }

    /**
     * EraseBackBottomMargin
     * @return EraseBackBottomMargin
     * @since API 5
     */
    @Keep
    public Float getEraseBackBottomMargin() { return mAttrs.mEraseBackBottom; }

    /**
     * EraseBackLeftMargin
     * @return EraseBackLeftMargin
     * @since API 5
     */
    @Keep
    public Float getEraseBackLeftMargin() { return mAttrs.mEraseBackLeft; }

    /**
     * EraseBackRightMargin
     * @return EraseBackRightMargin
     * @since API 5
     */
    @Keep
    public Float getEraseBackRightMargin() { return mAttrs.mEraseBackRight; }

    /**
     * EraseBackTopMargin
     * @return EraseBackTopMargin
     * @since API 5
     */
    @Keep
    public Float getEraseBackTopMargin() { return mAttrs.mEraseBackTop; }

    /**
     * EraseFrontBottomMargin
     * @return EraseFrontBottomMargin
     * @since API 5
     */
    @Keep
    public Float getEraseFrontBottomMargin() { return mAttrs.mEraseFrontBottom; }

    /**
     * EraseFrontLeftMargin
     * @return EraseFrontLeftMargin
     * @since API 5
     */
    @Keep
    public Float getEraseFrontLeftMargin() { return mAttrs.mEraseFrontLeft; }

    /**
     * EraseFrontRightMargin
     * @return EraseFrontRightMargin
     * @since API 5
     */
    @Keep
    public Float getEraseFrontRightMargin() { return mAttrs.mEraseFrontRight; }

    /**
     * EraseFrontTopMargin
     * @return EraseFrontTopMargin
     * @since API 5
     */
    @Keep
    public Float getEraseFrontTopMargin() { return mAttrs.mEraseFrontTop; }

    /**
     * CaptureMode
     * @return CaptureMode
     * @since API 5
     */
    @Keep
    public ScanAttributes.CaptureMode getCaptureMode() { return mAttrs.mCaptureMode; }

    /**
     * AutomaticToneMode
     * @return AutomaticToneMode
     * @since API 5
     */
    @Keep
    public ScanAttributes.AutomaticToneMode getAutomaticToneMode() { return mAttrs.mAutomaticToneMode; }

    /**
     * AutomaticStraightenMode
     * @return AutomaticStraightenMode
     * @since API 5
     */
    @Keep
    public ScanAttributes.AutomaticStraightenMode getAutomaticStraightenMode() { return mAttrs.mAutomaticStraightenMode; }

    /**
     * @return get file name about scanned file
     * @hide for internal use
     * @since API 1
     */
    public String getFileName() {
        return mAttrs.mFileName;
    }

    /**
     * @return selected file options attributes
     * @hide for internal use
     * @since API 1
     */
    public FileOptionsAttributes getFileOptionsAttributes() {
        return mAttrs.mFileOptionsAttributes;
    }

    /**
     * @return get destination Uri for scanned images
     * @hide for internal use
     * @since API 1
     */
    public Uri getUri() {
        return mAttrs.mUri;
    }

    /**
     * Network credentials for authentication when accessing Uri
     *
     * @return uri authentication credentials
     * @hide for internal use
     * @since API 1
     */
    public NetworkCredentialsAttributes getNetworkCredentialsAttributes() {
        return mAttrs.mNetworkCredentialsAttributes;
    }

    /**
     * Connect timeout for accessing Uri
     *
     * @return timeout in seconds
     * @hide for internal use
     * @since API 1
     */
    public int getConnectTimeout() {
        return mAttrs.mConnectTimeout;
    }

    /**
     * Read timeout for accessing Uri
     *
     * @return timeout in seconds
     * @hide for internal use
     * @since API 1
     */
    public int getReadTimeout() {
        return mAttrs.mReadTimeout;
    }

    /**
     * Maximum retry count for accessing Uri
     *
     * @return maximum retry count
     * @hide for internal use
     * @since API 1
     */
    public int getMaxConsecutiveRetries() {
        return mAttrs.mMaxRetries;
    }

    /**
     * Interval between retries
     *
     * @return interval in seconds
     * @hide for internal use
     * @since API 1
     */
    public int getRetryInterval() {
        return mAttrs.mRetryInterval;
    }

    /**
     * Email attributes for email destination
     *
     * @return email attributes
     * @hide for internal use
     * @since API 1
     */
    public EmailAttributes getEmailAttributes() {
        return mAttrs.mEmailAttributes;
    }

    /**
     * SMTP attributes for SMTP server connection
     *
     * @return smtp attributes
     * @hide for internal use
     * @since API 1
     */
    public SmtpAttributes getSmtpAttributes() {
        return mAttrs.mSmtpAttributes;
    }

    /**
     * USB location
     *
     * @return usb location
     * @hide for internal use
     * @since API 2
     */
    public String getUsbLocation() {
        return mAttrs.mUsbLocation;
    }
}
