// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.hp.oxpdlib.OXPdDevice;
import com.hp.sdd.jabberwocky.chat.HttpRequestResponseContainer;
import com.hp.sdd.jabberwocky.xml.RestXMLTagHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLTagStack;

/** Device's default BasicOptions */
@SuppressWarnings({"unused", "WeakerAccess"})
public class BasicOptions implements Parcelable {

    /**
     * BackgroundCleanup value.
     */
    public BackgroundCleanup backgroundCleanup;
    /**
     * BlankImageRemovalMode value.
     */
    public BlankImageRemovalMode blankImageRemovalMode;
    /**
     * ColorDropoutMode value.
     */
    public ColorDropoutMode colorDropoutMode;
    /**
     * ColorMode value.
     */
    public ColorMode colorMode;
    /**
     * ContrastAdjustment value.
     */
    public ContrastAdjustment contrastAdjustment;
    /**
     * CropMode value.
     */
    public CropMode cropMode;
    /**
     * customLength value.
     * @see BasicOptionsProfile#customMaxLength
     * @see BasicOptionsProfile#customMinLength
     */
    public float customLength;
    /**
     * customWidth value.
     * @see BasicOptionsProfile#customMaxWidth
     * @see BasicOptionsProfile#customMinWidth
     */
    public float customWidth;
    /**
     * DarknessAdjustment value.
     */
    public DarknessAdjustment darknessAdjustment;
    /**
     * DuplexFormat value.
     */
    public DuplexFormat duplexFormat;
    /**
     * FileType value.
     */
    public FileType fileType;
    /**
     * JobAssemblyMode value.
     */
    public JobAssemblyMode jobAssemblyMode;
    /**
     * MediaOrientation value.
     */
    public MediaOrientation mediaOrientation;
    /**
     * MediaSize value.
     */
    public MediaSize mediaSize;
    /**
     * MediaSource value.
     */
    public MediaSource mediaSource;
    /**
     * MediaWeightAdjustment value.
     */
    public MediaWeightAdjustment mediaWeightAdjustment;
    /**
     * MisfeedDetectionMode value.
     */
    public MisfeedDetectionMode misfeedDetectionMode;
    /**
     * OutputQuality value.
     */
    public OutputQuality outputQuality;
    /**
     * PlexMode value.
     */
    public PlexMode plexMode;
    /**
     * PreviewMode value.
     */
    public PreviewMode previewMode;
    /**
     * ProgressDialogMode value.
     */
    public ProgressDialogMode progressDialogMode;
    /**
     * Resolution value.
     */
    public Resolution resolution;
    /**
     * SharpnessAdjustment value.
     */
    public SharpnessAdjustment sharpnessAdjustment;
    /**
     * TextPhotoOptimization value.
     */
    public TextPhotoOptimization textPhotoOptimization;
    /**
     * SplitAttachmentByPage value.
     */
    public SplitAttachmentByPage splitAttachmentByPage;
    /**
     * maxPagesPerAttachment value.
     */
    public int maxPagesPerAttachment = 0;
    /**
     * EraseMarginUnit value.
     */
    public EraseMarginUnit eraseMarginUnit;
    /**
     * eraseBackBottom value.
     */
    public float eraseBackBottom = 0.0f;
    /**
     * eraseBackLeft value.
     */
    public float eraseBackLeft = 0.0f;
    /**
     * eraseBackRight value.
     */
    public float eraseBackRight = 0.0f;
    /**
     * eraseBackTop value.
     */
    public float eraseBackTop = 0.0f;
    /**
     * eraseFrontBottom value.
     */
    public float eraseFrontBottom = 0.0f;
    /**
     * eraseFrontLeft value.
     */
    public float eraseFrontLeft = 0.0f;
    /**
     * eraseFrontRight value.
     */
    public float eraseFrontRight = 0.0f;
    /**
     * eraseFrontTop value.
     */
    public float eraseFrontTop = 0.0f;
    /**
     * eraseFrontTop value.
     */
    public CaptureMode captureMode;
    /**
     * eraseFrontTop value.
     */
    public AutomaticToneMode automaticToneMode;
    /**
     * eraseFrontTop value.
     */
    public AutomaticStraightenMode automaticStraightenMode;

    private static class VERSION {
        public static final String REVISION_3 = "1.3";
    }

    /**
     * Constructor used by the library to construct BasicOptions objects from the device's default BasicOptions. <b>Apps should never construct a BasicOptions object. Instead, they should call GetDefaultBasicOptions, modify the returned BasicOptions if required, then use that BasicOptions object to construct a ScanTicket.</b>
     * @param tagHandler
     *              XML handler to extract data from
     * @throws Error
     *              If an error occurs.
     */
    @SuppressWarnings("ConstantConditions")
    private BasicOptions(RestXMLTagHandler tagHandler) throws Error {
        OXPdScan.faultExceptionCheck(tagHandler);
        this.fileType = (FileType) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__FILE_TYPE);
        this.colorMode = (ColorMode) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__COLOR_MODE);
        this.backgroundCleanup = (BackgroundCleanup) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__BACKGROUND_CLEANUP);
        this.blankImageRemovalMode = (BlankImageRemovalMode) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__BLANK_IMAGE_REMOVAL_MODE);
        this.colorDropoutMode = (ColorDropoutMode) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__COLOR_DROPOUT_MODE);
        this.contrastAdjustment = (ContrastAdjustment) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__CONTRAST_ADJUSTMENT);
        this.cropMode = (CropMode) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__CROP_MODE);
        this.customLength = (Float) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__CUSTOM_LENGTH);
        this.customWidth = (Float) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__CUSTOM_WIDTH);
        this.darknessAdjustment = (DarknessAdjustment) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__DARKNESS_ADJUSTMENT);
        this.duplexFormat = (DuplexFormat) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__DUPLEX_FORMAT);
        this.jobAssemblyMode = (JobAssemblyMode) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__JOB_ASSEMBLY_MODE);
        this.mediaOrientation = (MediaOrientation) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__MEDIA_ORIENTATION);
        this.mediaSize = (MediaSize) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__MEDIA_SIZE);
        this.mediaSource = (MediaSource) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__MEDIA_SOURCE);
        this.mediaWeightAdjustment = (MediaWeightAdjustment) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__MEDIA_WEIGHT_ADJUSTMENT);
        this.misfeedDetectionMode = (MisfeedDetectionMode) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__MISFEED_DETECTION_MODE);
        this.outputQuality = (OutputQuality) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__OUTPUT_QUALITY);
        this.plexMode = (PlexMode) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__PLEXMODE);
        this.previewMode = (PreviewMode) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__PREVIEW_MODE);
        this.progressDialogMode = (ProgressDialogMode) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__PROGRESS_DIALOG_MODE);
        this.resolution = (Resolution) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__RESOLUTION);
        this.sharpnessAdjustment = (SharpnessAdjustment) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__SHARPNESS_ADJUSTMENT);
        this.textPhotoOptimization = (TextPhotoOptimization) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__TEXT_PHOTO_OPTIMIZATION);
        // ScanTicket3
        if (OXPdScan.isSupportedScanMinorVersion(VERSION.REVISION_3)) {
            this.splitAttachmentByPage = (SplitAttachmentByPage) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__SPLIT_ATTACHMENT_BY_PAGES);
            this.maxPagesPerAttachment = (Integer) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__MAX_PAGES_PER_ATTACHMENT);
            this.eraseMarginUnit = (EraseMarginUnit) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__ERASE_MARGIN_UNITS);
            this.eraseBackBottom = (Float) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__ERASE_BACK_BOTTOM);
            this.eraseBackLeft = (Float) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__ERASE_BACK_LEFT);
            this.eraseBackRight = (Float) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__ERASE_BACK_RIGHT);
            this.eraseBackTop = (Float) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__ERASE_BACK_TOP);
            this.eraseFrontBottom = (Float) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__ERASE_FRONT_BOTTOM);
            this.eraseFrontLeft = (Float) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__ERASE_FRONT_LEFT);
            this.eraseFrontRight = (Float) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__ERASE_FRONT_RIGHT);
            this.eraseFrontTop = (Float) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__ERASE_FRONT_TOP);
            this.captureMode = (CaptureMode) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__CAPTURE_MODE);
            this.automaticToneMode = (AutomaticToneMode) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__AUTOMATIC_TONE_MODE);
            this.automaticStraightenMode = (AutomaticStraightenMode) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__AUTOMATIC_STRAIGHTEN_MODE);
        }
    }

    // For SDK Siumlator
    private BasicOptions(FileType fileType,
                         ColorMode colorMode,
                         BackgroundCleanup backgroundCleanup,
                         BlankImageRemovalMode blankImageRemovalMode,
                         ColorDropoutMode colorDropoutMode,
                         ContrastAdjustment contrastAdjustment,
                         CropMode cropMode,
                         float customLength,
                         float customWidth,
                         DarknessAdjustment darknessAdjustment,
                         DuplexFormat duplexFormat,
                         JobAssemblyMode jobAssemblyMode,
                         MediaOrientation mediaOrientation,
                         MediaSize mediaSize,
                         MediaSource mediaSource,
                         MediaWeightAdjustment mediaWeightAdjustment,
                         MisfeedDetectionMode misfeedDetectionMode,
                         OutputQuality outputQuality,
                         PlexMode plexMode,
                         PreviewMode previewMode,
                         ProgressDialogMode progressDialogMode,
                         Resolution resolution,
                         SharpnessAdjustment sharpnessAdjustment,
                         TextPhotoOptimization textPhotoOptimization,
                         SplitAttachmentByPage splitAttachmentByPage,
                         int maxPagesPerAttachment,
                         EraseMarginUnit eraseMarginUnit,
                         float eraseBackBottom,
                         float eraseBackLeft,
                         float eraseBackRight,
                         float eraseBackTop,
                         float eraseFrontBottom,
                         float eraseFrontLeft,
                         float eraseFrontRight,
                         float eraseFrontTop,
                         CaptureMode captureMode,
                         AutomaticToneMode automaticToneMode,
                         AutomaticStraightenMode automaticStraightenMode
    ) {
        this.fileType = fileType;
        this.colorMode = colorMode;
        this.backgroundCleanup = backgroundCleanup;
        this.blankImageRemovalMode = blankImageRemovalMode;
        this.colorDropoutMode = colorDropoutMode;
        this.contrastAdjustment = contrastAdjustment;
        this.cropMode = cropMode;
        this.customLength = customLength;
        this.customWidth = customWidth;
        this.darknessAdjustment = darknessAdjustment;
        this.duplexFormat = duplexFormat;
        this.jobAssemblyMode = jobAssemblyMode;
        this.mediaOrientation = mediaOrientation;
        this.mediaSize = mediaSize;
        this.mediaSource = mediaSource;
        this.mediaWeightAdjustment = mediaWeightAdjustment;
        this.misfeedDetectionMode = misfeedDetectionMode;
        this.outputQuality = outputQuality;
        this.plexMode = plexMode;
        this.previewMode = previewMode;
        this.progressDialogMode = progressDialogMode;
        this.resolution = resolution;
        this.sharpnessAdjustment = sharpnessAdjustment;
        this.textPhotoOptimization = textPhotoOptimization;
        // ScanTicket3
        if (OXPdScan.isSupportedScanMinorVersion(VERSION.REVISION_3)) {
            this.splitAttachmentByPage = splitAttachmentByPage;
            this.maxPagesPerAttachment = maxPagesPerAttachment;
            this.eraseMarginUnit = eraseMarginUnit;
            this.eraseBackBottom = eraseBackBottom;
            this.eraseBackLeft = eraseBackLeft;
            this.eraseBackRight = eraseBackRight;
            this.eraseBackTop = eraseBackTop;
            this.eraseFrontBottom = eraseFrontBottom;
            this.eraseFrontLeft = eraseFrontLeft;
            this.eraseFrontRight = eraseFrontRight;
            this.eraseFrontTop = eraseFrontTop;
            this.captureMode = captureMode;
            this.automaticToneMode = automaticToneMode;
            this.automaticStraightenMode = automaticStraightenMode;
        }
    }

    /**
     * Create a copy of the provided basic options
     * @param defaultBasicOptions
     *              BasicOptions to copy from
     */
    public BasicOptions(BasicOptions defaultBasicOptions) {
        this.fileType = defaultBasicOptions.fileType;
        this.colorMode = defaultBasicOptions.colorMode;
        this.backgroundCleanup = defaultBasicOptions.backgroundCleanup;
        this.blankImageRemovalMode = defaultBasicOptions.blankImageRemovalMode;
        this.colorDropoutMode = defaultBasicOptions.colorDropoutMode;
        this.contrastAdjustment = defaultBasicOptions.contrastAdjustment;
        this.cropMode = defaultBasicOptions.cropMode;
        this.customLength = defaultBasicOptions.customLength;
        this.customWidth = defaultBasicOptions.customWidth;
        this.darknessAdjustment = defaultBasicOptions.darknessAdjustment;
        this.duplexFormat = defaultBasicOptions.duplexFormat;
        this.jobAssemblyMode = defaultBasicOptions.jobAssemblyMode;
        this.mediaOrientation = defaultBasicOptions.mediaOrientation;
        this.mediaSize = defaultBasicOptions.mediaSize;
        this.mediaSource = defaultBasicOptions.mediaSource;
        this.mediaWeightAdjustment = defaultBasicOptions.mediaWeightAdjustment;
        this.misfeedDetectionMode = defaultBasicOptions.misfeedDetectionMode;
        this.outputQuality = defaultBasicOptions.outputQuality;
        this.plexMode = defaultBasicOptions.plexMode;
        this.previewMode = defaultBasicOptions.previewMode;
        this.progressDialogMode = defaultBasicOptions.progressDialogMode;
        this.resolution = defaultBasicOptions.resolution;
        this.sharpnessAdjustment = defaultBasicOptions.sharpnessAdjustment;
        this.textPhotoOptimization = defaultBasicOptions.textPhotoOptimization;
        // ScanTicket3
        if (OXPdScan.isSupportedScanMinorVersion(VERSION.REVISION_3)) {
            this.splitAttachmentByPage = defaultBasicOptions.splitAttachmentByPage;
            this.maxPagesPerAttachment = defaultBasicOptions.maxPagesPerAttachment;
            this.eraseMarginUnit = defaultBasicOptions.eraseMarginUnit;
            this.eraseBackBottom = defaultBasicOptions.eraseBackBottom;
            this.eraseBackLeft = defaultBasicOptions.eraseBackLeft;
            this.eraseBackRight = defaultBasicOptions.eraseBackRight;
            this.eraseBackTop = defaultBasicOptions.eraseBackTop;
            this.eraseFrontBottom = defaultBasicOptions.eraseFrontBottom;
            this.eraseFrontLeft = defaultBasicOptions.eraseFrontLeft;
            this.eraseFrontRight = defaultBasicOptions.eraseFrontRight;
            this.eraseFrontTop = defaultBasicOptions.eraseFrontTop;
            this.captureMode = defaultBasicOptions.captureMode;
            this.automaticToneMode = defaultBasicOptions.automaticToneMode;
            this.automaticStraightenMode = defaultBasicOptions.automaticStraightenMode;
        }
    }

    //For SDK Simulator
    static BasicOptions parseRequestResult
            (FileType fileType,
             ColorMode colorMode,
             BackgroundCleanup backgroundCleanup,
             BlankImageRemovalMode blankImageRemovalMode,
             ColorDropoutMode colorDropoutMode,
             ContrastAdjustment contrastAdjustment,
             CropMode cropMode,
             float customLength,
             float customWidth,
             DarknessAdjustment darknessAdjustment,
             DuplexFormat duplexFormat,
             JobAssemblyMode jobAssemblyMode,
             MediaOrientation mediaOrientation,
             MediaSize mediaSize,
             MediaSource mediaSource,
             MediaWeightAdjustment mediaWeightAdjustment,
             MisfeedDetectionMode misfeedDetectionMode,
             OutputQuality outputQuality,
             PlexMode plexMode,
             PreviewMode previewMode,
             ProgressDialogMode progressDialogMode,
             Resolution resolution,
             SharpnessAdjustment sharpnessAdjustment,
             TextPhotoOptimization textPhotoOptimization,
             SplitAttachmentByPage splitAttachmentByPage,
             int maxPagesPerAttachment,
             EraseMarginUnit eraseMarginUnit,
             float eraseBackBottom,
             float eraseBackLeft,
             float eraseBackRight,
             float eraseBackTop,
             float eraseFrontBottom,
             float eraseFrontLeft,
             float eraseFrontRight,
             float eraseFrontTop,
             CaptureMode captureMode,
             AutomaticToneMode automaticToneMode,
             AutomaticStraightenMode automaticStraightenMode
            ) {
        return new BasicOptions(fileType,
                colorMode,
                backgroundCleanup,
                blankImageRemovalMode,
                colorDropoutMode,
                contrastAdjustment,
                cropMode,
                customLength,
                customWidth,
                darknessAdjustment,
                duplexFormat,
                jobAssemblyMode,
                mediaOrientation,
                mediaSize,
                mediaSource,
                mediaWeightAdjustment,
                misfeedDetectionMode,
                outputQuality,
                plexMode,
                previewMode,
                progressDialogMode,
                resolution,
                sharpnessAdjustment,
                textPhotoOptimization,
                splitAttachmentByPage,
                maxPagesPerAttachment,
                eraseMarginUnit,
                eraseBackBottom,
                eraseBackLeft,
                eraseBackRight,
                eraseBackTop,
                eraseFrontBottom,
                eraseFrontLeft,
                eraseFrontRight,
                eraseFrontTop,
                captureMode,
                automaticToneMode,
                automaticStraightenMode
        );
    }

    /**
     * Builds a BasicOptions instance from the provided HTTP request/response
     * @param device
     *              OXPd device instance
     * @param requestResponse
     *              HTTP request/response pair
     * @param tagHandler
     *              XML tag handler
     * @return
     *              BasicOptions instance
     * @throws Error
     *              When errors are detected
     */
    static BasicOptions parseRequestResult(final OXPdDevice device, HttpRequestResponseContainer requestResponse, RestXMLTagHandler tagHandler) throws Error {

        RestXMLTagHandler.XMLEndTagHandler infoCollector = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__FILE_TYPE, localName)) {
                    handler.setTagData(localName, FileType.fromAttributeValue(data));
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__COLOR_MODE, localName)) {
                    handler.setTagData(localName, ColorMode.fromAttributeValue(data));
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__BACKGROUND_CLEANUP, localName)) {
                    handler.setTagData(localName, BackgroundCleanup.fromAttributeValue(data));
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__BLANK_IMAGE_REMOVAL_MODE, localName)) {
                    handler.setTagData(localName, BlankImageRemovalMode.fromAttributeValue(data));
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__COLOR_DROPOUT_MODE, localName)) {
                    handler.setTagData(localName, ColorDropoutMode.fromAttributeValue(data));
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__CONTRAST_ADJUSTMENT, localName)) {
                    handler.setTagData(localName, ContrastAdjustment.fromAttributeValue(data));
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__CROP_MODE, localName)) {
                    handler.setTagData(localName, CropMode.fromAttributeValue(data));
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__CUSTOM_LENGTH, localName)) {
                    setFloatTagData(handler, localName, data, 11f);
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__CUSTOM_WIDTH, localName)) {
                    setFloatTagData(handler, localName, data, 8.5f);
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__DARKNESS_ADJUSTMENT, localName)) {
                    handler.setTagData(localName, DarknessAdjustment.fromAttributeValue(data));
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__DUPLEX_FORMAT, localName)) {
                    handler.setTagData(localName, DuplexFormat.fromAttributeValue(data));
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__JOB_ASSEMBLY_MODE, localName)) {
                    handler.setTagData(localName, JobAssemblyMode.fromAttributeValue(data));
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__MEDIA_ORIENTATION, localName)) {
                    handler.setTagData(localName, MediaOrientation.fromAttributeValue(data));
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__MEDIA_SIZE, localName)) {
                    handler.setTagData(localName, MediaSize.fromAttributeValue(data));
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__MEDIA_SOURCE, localName)) {
                    handler.setTagData(localName, MediaSource.fromAttributeValue(data));
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__MEDIA_WEIGHT_ADJUSTMENT, localName)) {
                    handler.setTagData(localName, MediaWeightAdjustment.fromAttributeValue(data));
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__MISFEED_DETECTION_MODE, localName)) {
                    handler.setTagData(localName, MisfeedDetectionMode.fromAttributeValue(data));
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__OUTPUT_QUALITY, localName)) {
                    handler.setTagData(localName, OutputQuality.fromAttributeValue(data));
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__PLEXMODE, localName)) {
                    handler.setTagData(localName, PlexMode.fromAttributeValue(data));
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__PREVIEW_MODE, localName)) {
                    handler.setTagData(localName, PreviewMode.fromAttributeValue(data));
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__PROGRESS_DIALOG_MODE, localName)) {
                    handler.setTagData(localName, ProgressDialogMode.fromAttributeValue(data));
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__RESOLUTION, localName)) {
                    handler.setTagData(localName, Resolution.fromAttributeValue(data));
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__SHARPNESS_ADJUSTMENT, localName)) {
                    handler.setTagData(localName, SharpnessAdjustment.fromAttributeValue(data));
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__TEXT_PHOTO_OPTIMIZATION, localName)) {
                    handler.setTagData(localName, TextPhotoOptimization.fromAttributeValue(data));
                }
                // ScanTicket3
                if (OXPdScan.isSupportedScanMinorVersion(VERSION.REVISION_3)) {
                    if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__SPLIT_ATTACHMENT_BY_PAGES, localName)) {
                        handler.setTagData(localName, SplitAttachmentByPage.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__MAX_PAGES_PER_ATTACHMENT, localName)) {
                        setIntegerTagData(handler, localName, data, 9999);
                    } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__ERASE_MARGIN_UNITS, localName)) {
                        handler.setTagData(localName, EraseMarginUnit.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__ERASE_BACK_BOTTOM, localName)) {
                        setFloatTagData(handler, localName, data, 0.0f);
                    } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__ERASE_BACK_LEFT, localName)) {
                        setFloatTagData(handler, localName, data, 0.0f);
                    } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__ERASE_BACK_RIGHT, localName)) {
                        setFloatTagData(handler, localName, data, 0.0f);
                    } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__ERASE_BACK_TOP, localName)) {
                        setFloatTagData(handler, localName, data, 0.0f);
                    } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__ERASE_FRONT_BOTTOM, localName)) {
                        setFloatTagData(handler, localName, data, 0.0f);
                    } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__ERASE_FRONT_LEFT, localName)) {
                        setFloatTagData(handler, localName, data, 0.0f);
                    } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__ERASE_FRONT_RIGHT, localName)) {
                        setFloatTagData(handler, localName, data, 0.0f);
                    } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__ERASE_FRONT_TOP, localName)) {
                        setFloatTagData(handler, localName, data, 0.0f);
                    } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__CAPTURE_MODE, localName)) {
                        handler.setTagData(localName, CaptureMode.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__AUTOMATIC_TONE_MODE, localName)) {
                        handler.setTagData(localName, AutomaticToneMode.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__AUTOMATIC_STRAIGHTEN_MODE, localName)) {
                        handler.setTagData(localName, AutomaticStraightenMode.fromAttributeValue(data));
                    }
                }
            }

            private void setFloatTagData(RestXMLTagHandler handler, String localName, String data, Float defaultValue) {
                Float custom;
                try {
                    custom = Float.valueOf(data);
                } catch(Exception ignored) {
                    custom = defaultValue;
                }
                handler.setTagData(localName, custom);
            }

            private void setIntegerTagData(RestXMLTagHandler handler, String localName, String data, Integer defaultValue) {
                Integer custom;
                try {
                    custom = Integer.valueOf(data);
                } catch(Exception ignored) {
                    custom = defaultValue;
                }
                handler.setTagData(localName, custom);
            }
        };

        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__FILE_TYPE, null, infoCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__COLOR_MODE, null, infoCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__BACKGROUND_CLEANUP, null, infoCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__BLANK_IMAGE_REMOVAL_MODE, null, infoCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__COLOR_DROPOUT_MODE, null, infoCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__CONTRAST_ADJUSTMENT, null, infoCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__CROP_MODE, null, infoCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__CUSTOM_LENGTH, null, infoCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__CUSTOM_WIDTH, null, infoCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__DARKNESS_ADJUSTMENT, null, infoCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__DUPLEX_FORMAT, null, infoCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__JOB_ASSEMBLY_MODE, null, infoCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__MEDIA_ORIENTATION, null, infoCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__MEDIA_SIZE, null, infoCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__MEDIA_SOURCE, null, infoCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__MEDIA_WEIGHT_ADJUSTMENT, null, infoCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__MISFEED_DETECTION_MODE, null, infoCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__OUTPUT_QUALITY, null, infoCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__PLEXMODE, null, infoCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__PREVIEW_MODE, null, infoCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__PROGRESS_DIALOG_MODE, null, infoCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__RESOLUTION, null, infoCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__SHARPNESS_ADJUSTMENT, null, infoCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__TEXT_PHOTO_OPTIMIZATION, null, infoCollector);
        // ScanTicket3
        if (OXPdScan.isSupportedScanMinorVersion(VERSION.REVISION_3)) {
            tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__SPLIT_ATTACHMENT_BY_PAGES, null, infoCollector);
            tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__MAX_PAGES_PER_ATTACHMENT, null, infoCollector);
            tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__ERASE_MARGIN_UNITS, null, infoCollector);
            tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__ERASE_BACK_BOTTOM, null, infoCollector);
            tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__ERASE_BACK_LEFT, null, infoCollector);
            tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__ERASE_BACK_RIGHT, null, infoCollector);
            tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__ERASE_BACK_TOP, null, infoCollector);
            tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__ERASE_FRONT_BOTTOM, null, infoCollector);
            tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__ERASE_FRONT_LEFT, null, infoCollector);
            tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__ERASE_FRONT_RIGHT, null, infoCollector);
            tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__ERASE_FRONT_TOP, null, infoCollector);
            tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__CAPTURE_MODE, null, infoCollector);
            tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__AUTOMATIC_TONE_MODE, null, infoCollector);
            tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__AUTOMATIC_STRAIGHTEN_MODE, null, infoCollector);
        }
        device.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);

        return new BasicOptions(tagHandler);
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable instance's marshaled representation.
     * @return
     *              0 for no special objects
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     * @param dest The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written. May be 0 or {@link Parcelable#PARCELABLE_WRITE_RETURN_VALUE}
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.fileType.ordinal());
        dest.writeInt(this.colorMode.ordinal());
        dest.writeInt(this.backgroundCleanup.ordinal());
        dest.writeInt(this.blankImageRemovalMode.ordinal());
        dest.writeInt(this.colorDropoutMode.ordinal());
        dest.writeInt(this.contrastAdjustment.ordinal());
        dest.writeInt(this.cropMode.ordinal());
        dest.writeFloat(this.customLength);
        dest.writeFloat(this.customWidth);
        dest.writeInt(this.darknessAdjustment.ordinal());
        dest.writeInt(this.duplexFormat.ordinal());
        dest.writeInt(this.jobAssemblyMode.ordinal());
        dest.writeInt(this.mediaOrientation.ordinal());
        dest.writeInt(this.mediaSize.ordinal());
        dest.writeInt(this.mediaSource.ordinal());
        dest.writeInt(this.mediaWeightAdjustment.ordinal());
        dest.writeInt(this.misfeedDetectionMode.ordinal());
        dest.writeInt(this.outputQuality.ordinal());
        dest.writeInt(this.plexMode.ordinal());
        dest.writeInt(this.previewMode.ordinal());
        dest.writeInt(this.progressDialogMode.ordinal());
        dest.writeInt(this.resolution.ordinal());
        dest.writeInt(this.sharpnessAdjustment.ordinal());
        dest.writeInt(this.textPhotoOptimization.ordinal());
        // ScanTicket3
        if (OXPdScan.isSupportedScanMinorVersion(VERSION.REVISION_3)) {
            dest.writeInt(this.splitAttachmentByPage.ordinal());
            dest.writeInt(this.maxPagesPerAttachment);
            dest.writeInt(this.eraseMarginUnit.ordinal());
            dest.writeFloat(this.eraseBackBottom);
            dest.writeFloat(this.eraseBackLeft);
            dest.writeFloat(this.eraseBackRight);
            dest.writeFloat(this.eraseBackTop);
            dest.writeFloat(this.eraseFrontBottom);
            dest.writeFloat(this.eraseFrontLeft);
            dest.writeFloat(this.eraseFrontRight);
            dest.writeFloat(this.eraseFrontTop);
            dest.writeInt(this.captureMode.ordinal());
            dest.writeInt(this.automaticToneMode.ordinal());
            dest.writeInt(this.automaticStraightenMode.ordinal());
        }
    }

    /**
     * Constructor when rebuilding from {@link Parcel}
     * @param in Parcel from which the object should be reconstructed
     */
    private BasicOptions(Parcel in) {
        this.fileType = FileType.values()[in.readInt()];
        this.colorMode = ColorMode.values()[in.readInt()];
        this.backgroundCleanup = BackgroundCleanup.values()[in.readInt()];
        this.blankImageRemovalMode = BlankImageRemovalMode.values()[in.readInt()];
        this.colorDropoutMode = ColorDropoutMode.values()[in.readInt()];
        this.contrastAdjustment = ContrastAdjustment.values()[in.readInt()];
        this.cropMode = CropMode.values()[in.readInt()];
        this.customLength = in.readFloat();
        this.customWidth = in.readFloat();
        this.darknessAdjustment = DarknessAdjustment.values()[in.readInt()];
        this.duplexFormat = DuplexFormat.values()[in.readInt()];
        this.jobAssemblyMode = JobAssemblyMode.values()[in.readInt()];
        this.mediaOrientation = MediaOrientation.values()[in.readInt()];
        this.mediaSize = MediaSize.values()[in.readInt()];
        this.mediaSource = MediaSource.values()[in.readInt()];
        this.mediaWeightAdjustment = MediaWeightAdjustment.values()[in.readInt()];
        this.misfeedDetectionMode = MisfeedDetectionMode.values()[in.readInt()];
        this.outputQuality = OutputQuality.values()[in.readInt()];
        this.plexMode = PlexMode.values()[in.readInt()];
        this.previewMode = PreviewMode.values()[in.readInt()];
        this.progressDialogMode = ProgressDialogMode.values()[in.readInt()];
        this.resolution = Resolution.values()[in.readInt()];
        this.sharpnessAdjustment = SharpnessAdjustment.values()[in.readInt()];
        this.textPhotoOptimization = TextPhotoOptimization.values()[in.readInt()];

        // ScanTicket3
        if (OXPdScan.isSupportedScanMinorVersion(VERSION.REVISION_3)) {
            this.splitAttachmentByPage = SplitAttachmentByPage.values()[in.readInt()];
            this.maxPagesPerAttachment = in.readInt();
            this.eraseMarginUnit = EraseMarginUnit.values()[in.readInt()];
            this.eraseBackBottom = in.readFloat();
            this.eraseBackLeft = in.readFloat();
            this.eraseBackRight = in.readFloat();
            this.eraseBackTop = in.readFloat();
            this.eraseFrontBottom = in.readFloat();
            this.eraseFrontLeft = in.readFloat();
            this.eraseFrontRight = in.readFloat();
            this.eraseFrontTop = in.readFloat();
            this.captureMode = CaptureMode.values()[in.readInt()];
            this.automaticToneMode = AutomaticToneMode.values()[in.readInt()];
            this.automaticStraightenMode = AutomaticStraightenMode.values()[in.readInt()];
        }
    }

    /**
     * BasicOptions creator
     */
    public static final Parcelable.Creator<BasicOptions> CREATOR =
            new Parcelable.Creator<BasicOptions>() {

                /**
                 * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had previously been written by {@link BasicOptions#writeToParcel(Parcel, int)}
                 * @param in
                 *              The Parcel to read the object's data from.
                 * @return
                 *              Returns a new instance of the Parcelable class.
                 */
                @Override
                public BasicOptions createFromParcel(Parcel in) {
                    return new BasicOptions(in);
                }

                /**
                 * Create a new array of the Parcelable class.
                 * @param size
                 *              Size of the array
                 * @return
                 *              Returns an array of the Parcelable class, with every entry initialized to null.
                 */
                @Override
                public BasicOptions[] newArray(int size) {
                    return new BasicOptions[size];
                }
            };
}
