// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.copy;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import com.hp.oxpdlib.OXPdDevice;
import com.hp.oxpdlib.media.MediaInputId;
import com.hp.oxpdlib.media.MediaOutputId;
import com.hp.oxpdlib.media.MediaSizeId;
import com.hp.oxpdlib.media.MediaTypeId;
import com.hp.oxpdlib.options.BackgroundCleanup;
import com.hp.oxpdlib.options.BookletBordersEachPage;
import com.hp.oxpdlib.options.BookletFinishingOption;
import com.hp.oxpdlib.options.BookletFormat;
import com.hp.oxpdlib.options.ColorMode;
import com.hp.oxpdlib.options.ContentOrientation;
import com.hp.oxpdlib.options.ContrastAdjustment;
import com.hp.oxpdlib.options.DarknessAdjustment;
import com.hp.oxpdlib.options.DuplexFormat;
import com.hp.oxpdlib.options.ImageShiftReduceToFit;
import com.hp.oxpdlib.options.ImageShiftUnits;
import com.hp.oxpdlib.options.FoldMode;
import com.hp.oxpdlib.options.JobAssemblyMode;
import com.hp.oxpdlib.options.PlexMode;
import com.hp.oxpdlib.options.PreviewMode;
import com.hp.oxpdlib.options.ProgressDialogMode;
import com.hp.oxpdlib.options.PunchMode;
import com.hp.oxpdlib.options.ReduceEnlargeMode;
import com.hp.oxpdlib.options.ScanSourceId;
import com.hp.oxpdlib.options.SharpnessAdjustment;
import com.hp.oxpdlib.options.SheetCollationMode;
import com.hp.oxpdlib.options.StapleOption;
import com.hp.oxpdlib.options.StampFormat;
import com.hp.oxpdlib.options.StampOption;
import com.hp.oxpdlib.options.StampPolicyType;
import com.hp.oxpdlib.options.StampPosition;
import com.hp.oxpdlib.options.StampType;
import com.hp.oxpdlib.options.StampType;
import com.hp.oxpdlib.options.TextGraphicsOptimization;
import com.hp.oxpdlib.options.WatermarkMessageType;
import com.hp.oxpdlib.scan.EraseMarginUnit;
import com.hp.oxpdlib.scan.CaptureMode;
import com.hp.oxpdlib.options.WatermarkOnlyFirstPage;
import com.hp.oxpdlib.options.WatermarkPattern;
import com.hp.oxpdlib.options.WatermarkRotate45;
import com.hp.oxpdlib.options.WatermarkType;
import com.hp.sdd.jabberwocky.chat.HttpRequestResponseContainer;
import com.hp.sdd.jabberwocky.xml.RestXMLTagHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLTagStack;

import java.util.HashMap;
import java.util.Map;

public class CopyOptions implements Parcelable {
    public long copies;
    public PlexMode originalSides;
    public DuplexFormat originalDuplexFormat;
    public PlexMode outputSides;
    public DuplexFormat outputDuplexFormat;
    public ColorMode colorMode;
    public MediaSizeId originalMediaSize;
    public float originalCustomSizeX;
    public float originalCustomSizeY;
    public MediaSizeId outputMediaSize;
    public float outputCustomSizeX;
    public float outputCustomSizeY;
    public MediaTypeId outputMediaType;
    public MediaInputId outputMediaTray;
    public ContentOrientation contentOrientation;
    public SharpnessAdjustment sharpnessAdjustment;
    public DarknessAdjustment darknessAdjustment;
    public ContrastAdjustment contrastAdjustment;
    public BackgroundCleanup backgroundCleanup;
    public TextGraphicsOptimization textGraphicsOptimization;
    public SheetCollationMode outputCollation;
    public MediaOutputId outputBin;
    public JobAssemblyMode jobAssembly;
    public PreviewMode jobPreview;
    public ReduceEnlargeMode reduceEnlarge;
    public boolean reduceEnlargeMarginsIncluded;
    public long reduceEnlargePercent;
    public ScanSourceId scanSource;
    public JobExecutionMode jobExecutionMode;
    public NumberUpCount numberUpCount;
    public NumberUpDirection numberUpDirection;
    public String storeJobFolderName;
    public String storeJobName;
    public PasswordType storeJobPasswordType;
    public String storeJobPassword;
    public boolean storeJobDeleteOnPowerCycle;
    public boolean storeJobDeleteOnRelease;
    public ProgressDialogMode progressDialogMode;
    public EraseMarginUnit eraseMarginUnit;
    public float eraseBackBottom;
    public float eraseBackLeft;
    public float eraseBackRight;
    public float eraseBackTop;
    public float eraseFrontBottom;
    public float eraseFrontLeft;
    public float eraseFrontRight;
    public float eraseFrontTop;
    public CaptureMode captureMode;
    public ImageShiftReduceToFit imageShiftReduceToFit;
    public ImageShiftUnits imageShiftUnits;
    public float imageShiftXFront;
    public float imageShiftYFront;
    public float imageShiftXBack;
    public float imageShiftYBack;
    public BookletBordersEachPage bookletBordersEachPage;
    public BookletFinishingOption bookletFinishingOption;
    public BookletFormat bookletFormat;
    public StapleOption stapleOption;
    public PunchMode punchMode;
    public FoldMode foldMode;
    public Map<StampPosition, StampOption> stampOptionMap;

    public long watermarkTextSize;
    public long watermarkTransparency;
    public String watermarkBackgroundColor;
    public String watermarkFont;
    public String watermarkTextColor;
    public WatermarkOnlyFirstPage watermarkOnlyFirstPage;
    public long watermarkDarkness;
    public String watermarkText;
    public WatermarkRotate45 watermarkRotate45;
    public WatermarkType watermarkType;
    public WatermarkPattern watermarkBackgroundPattern;
    public WatermarkMessageType watermarkMessageType;
    /**
     * Constructor used by the library to construct CopyOptions objects.
     *
     * @param tagHandler XML handler to extract data from
     */
    @SuppressWarnings("unchecked")
    private CopyOptions(RestXMLTagHandler tagHandler) throws Error {
        OXPdCopy.faultExceptionCheck(tagHandler);

        copies = (Long) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__COPIES);
        originalSides = (PlexMode) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__ORIGINAL_SIDES);
        originalDuplexFormat = (DuplexFormat) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__ORIGINAL_DUPLEX_FORMAT);
        outputSides = (PlexMode) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OUTPUT_SIDES);
        outputDuplexFormat = (DuplexFormat) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OUTPUT_DUPLEX_FORMAT);
        colorMode = (ColorMode) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__COLOR_MODE);
        originalMediaSize = (MediaSizeId) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__ORIGINAL_MEDIA_SIZE);
        originalCustomSizeX = (Float) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__ORIGINAL_CUSTOM_SIZE_X);
        originalCustomSizeY = (Float) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__ORIGINAL_CUSTOM_SIZE_Y);
        outputMediaSize = (MediaSizeId) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OUTPUT_MEDIA_SIZE);
        outputCustomSizeX = (Float) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OUTPUT_CUSTOM_SIZE_X);
        outputCustomSizeY = (Float) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OUTPUT_CUSTOM_SIZE_Y);
        outputMediaType = (MediaTypeId) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OUTPUT_MEDIA_TYPE);
        outputMediaTray = (MediaInputId) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OUTPUT_MEDIA_TRAY);
        contentOrientation = (ContentOrientation) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__CONTENT_ORIENTATION);
        sharpnessAdjustment = (SharpnessAdjustment) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__SHARPNESS_ADJUSTMENT);
        darknessAdjustment = (DarknessAdjustment) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__DARKNESS_ADJUSTMENT);
        contrastAdjustment = (ContrastAdjustment) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__CONTRAST_ADJUSTMENT);
        backgroundCleanup = (BackgroundCleanup) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__BACKGROUND_CLEANUP);
        textGraphicsOptimization = (TextGraphicsOptimization) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__TEXT_GRAPHIC_OPTIMIZATION);
        outputCollation = (SheetCollationMode) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OUTPUT_COLLATION);
        outputBin = (MediaOutputId) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OUTPUT_BIN);
        jobAssembly = (JobAssemblyMode) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__JOB_ASSEMBLY);
        jobPreview = (PreviewMode) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__JOB_PREVIEW);
        reduceEnlarge = (ReduceEnlargeMode) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__REDUCE_ENLARGE);
        reduceEnlargeMarginsIncluded = (Boolean) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__REDUCE_ENLARGE_MARGINS_INCLUDED);
        reduceEnlargePercent = (Long) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__REDUCE_ENLARGE_PERCENT);
        scanSource = (ScanSourceId) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__SCAN_SOURCE);
        jobExecutionMode = (JobExecutionMode) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__JOB_EXECUTION_MODE);
        numberUpCount = (NumberUpCount) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__NUMBER_UP_COUNT);
        numberUpDirection = (NumberUpDirection) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__NUMBER_UP_DIRECTION);
        storeJobFolderName = (String) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__STORE_JOB_FOLDER_NAME);
        storeJobName = (String) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__STORE_JOB_NAME);
        storeJobPasswordType = (PasswordType) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__STORE_JOB_PASSWORD_TYPE);
        storeJobPassword = (String) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__STORE_JOB_PASSWORD);
        storeJobDeleteOnPowerCycle = tagHandler.containsTagData(OXPdCopy.Constants.XML_TAG__COPY__STORE_DELETE_ON_POWER_CYCLE) ?
                (Boolean) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__STORE_DELETE_ON_POWER_CYCLE) : false;
        storeJobDeleteOnRelease = tagHandler.containsTagData(OXPdCopy.Constants.XML_TAG__COPY__STORE_DELETE_ON_RELEASE) ?
                (Boolean) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__STORE_DELETE_ON_RELEASE) : false;
        progressDialogMode = (ProgressDialogMode) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__PROGRESS_DIALOG_MODE);
        eraseMarginUnit = (EraseMarginUnit) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__ERASE_MARGIN_UNIT);
        eraseBackBottom = (Float) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__ERASE_BACK_BOTTOM);
        eraseBackLeft = (Float) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__ERASE_BACK_LEFT);
        eraseBackRight = (Float) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__ERASE_BACK_RIGHT);
        eraseBackTop = (Float) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__ERASE_BACK_TOP);
        eraseFrontBottom = (Float) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__ERASE_FRONT_BOTTOM);
        eraseFrontLeft = (Float) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__ERASE_FRONT_LEFT);
        eraseFrontRight = (Float) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__ERASE_FRONT_RIGHT);
        eraseFrontTop = (Float) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__ERASE_FRONT_TOP);
        captureMode = (CaptureMode) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__CAPTURE_MODE);
        imageShiftReduceToFit = (ImageShiftReduceToFit) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__IMAGE_SHIFT_REDUCE_TO_FIT);
        imageShiftUnits = (ImageShiftUnits) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__IMAGE_SHIFT_UNITS);
        imageShiftXFront = (Float) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__IMAGE_SHIFT_X_FRONT);
        imageShiftYFront = (Float) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__IMAGE_SHIFT_Y_FRONT);
        imageShiftXBack = (Float) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__IMAGE_SHIFT_X_BACK);
        imageShiftYBack = (Float) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__IMAGE_SHIFT_Y_BACK);
        bookletBordersEachPage = (BookletBordersEachPage) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__BOOKLET_BORDERS_EACH_PAGE);
        bookletFinishingOption = (BookletFinishingOption) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__BOOKLET_FINISHING_OPTION);
        bookletFormat = (BookletFormat) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__BOOKLET_FORMAT);
        stapleOption = (StapleOption) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__STAPLE_OPTION);
        punchMode = (PunchMode) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__PUNCH_MODE);
        foldMode = (FoldMode) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__FOLD_MODE);

        stampOptionMap = new HashMap<>();
        StampOption stampTopLeft = (StampOption) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__STAMP_TOP_LEFT);
        StampOption stampTopCenter = (StampOption) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__STAMP_TOP_CENTER);
        StampOption stampTopRight = (StampOption) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__STAMP_TOP_RIGHT);
        StampOption stampBottomLeft = (StampOption) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__STAMP_BOTTOM_LEFT);
        StampOption stampBottomCenter = (StampOption) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__STAMP_BOTTOM_CENTER);
        StampOption stampBottomRight = (StampOption) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__STAMP_BOTTOM_RIGHT);

        if(stampTopLeft != null) {
            stampOptionMap.put(StampPosition.TOP_LEFT, stampTopLeft);
        }
        if(stampTopCenter != null) {
            stampOptionMap.put(StampPosition.TOP_CENTER, stampTopCenter);
        }
        if(stampTopRight != null) {
            stampOptionMap.put(StampPosition.TOP_RIGHT, stampTopRight);
        }
        if(stampBottomLeft != null) {
            stampOptionMap.put(StampPosition.BOTTOM_LEFT, stampBottomLeft);
        }
        if(stampBottomCenter != null) {
            stampOptionMap.put(StampPosition.BOTTOM_CENTER, stampBottomCenter);
        }
        if(stampBottomRight != null) {
            stampOptionMap.put(StampPosition.BOTTOM_RIGHT, stampBottomRight);
        }

        watermarkDarkness = (Long) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__WATERMARK_DARKNESS);
        watermarkText = (String) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__WATERMARK_TEXT);
        watermarkRotate45 = (WatermarkRotate45) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__WATERMARK_ROTATE45);
        watermarkType = (WatermarkType) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__WATERMARK_TYPE);
        watermarkTextSize = (Long) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__WATERMARK_TEXT_SIZE);
        watermarkTransparency = (Long) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__WATERMARK_TRANSPARENCY);
        watermarkBackgroundColor = (String) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__WATERMARK_BACKGROUND_COLOR);
        watermarkFont = (String) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__WATERMARK_FONT);
        watermarkTextColor = (String) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__WATERMARK_TEXT_COLOR);
        watermarkOnlyFirstPage = (WatermarkOnlyFirstPage) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__WATERMARK_ONLY_FIRST_PAGE);
        watermarkMessageType = (WatermarkMessageType) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__WATERMARK_MESSAGETYPE);
        watermarkBackgroundPattern = (WatermarkPattern) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__WATERMARK_BACKGROUND_PATTERN);

    }

    /**
     * Create a copy of the provided copy options
     * @param defaultCopyOptions
     *              CopyOptions to copy from
     */
    public CopyOptions(CopyOptions defaultCopyOptions) {
        copies = defaultCopyOptions.copies;
        originalSides = defaultCopyOptions.originalSides;
        originalDuplexFormat = defaultCopyOptions.originalDuplexFormat;
        outputSides = defaultCopyOptions.outputSides;
        outputDuplexFormat = defaultCopyOptions.outputDuplexFormat;
        colorMode = defaultCopyOptions.colorMode;
        originalMediaSize = defaultCopyOptions.originalMediaSize;
        originalCustomSizeX = defaultCopyOptions.originalCustomSizeX;
        originalCustomSizeY = defaultCopyOptions.originalCustomSizeY;
        outputMediaSize = defaultCopyOptions.outputMediaSize;
        outputCustomSizeX = defaultCopyOptions.outputCustomSizeX;
        outputCustomSizeY = defaultCopyOptions.outputCustomSizeY;
        outputMediaType = defaultCopyOptions.outputMediaType;
        outputMediaTray = defaultCopyOptions.outputMediaTray;
        contentOrientation = defaultCopyOptions.contentOrientation;
        sharpnessAdjustment = defaultCopyOptions.sharpnessAdjustment;
        darknessAdjustment = defaultCopyOptions.darknessAdjustment;
        contrastAdjustment = defaultCopyOptions.contrastAdjustment;
        backgroundCleanup = defaultCopyOptions.backgroundCleanup;
        textGraphicsOptimization = defaultCopyOptions.textGraphicsOptimization;
        outputCollation = defaultCopyOptions.outputCollation;
        outputBin = defaultCopyOptions.outputBin;
        jobAssembly = defaultCopyOptions.jobAssembly;
        jobPreview = defaultCopyOptions.jobPreview;
        reduceEnlarge = defaultCopyOptions.reduceEnlarge;
        reduceEnlargeMarginsIncluded = defaultCopyOptions.reduceEnlargeMarginsIncluded;
        reduceEnlargePercent = defaultCopyOptions.reduceEnlargePercent;
        scanSource = defaultCopyOptions.scanSource;
        jobExecutionMode = defaultCopyOptions.jobExecutionMode;
        numberUpCount = defaultCopyOptions.numberUpCount;
        numberUpDirection = defaultCopyOptions.numberUpDirection;
        storeJobFolderName = defaultCopyOptions.storeJobFolderName;
        storeJobName = defaultCopyOptions.storeJobName;
        storeJobPasswordType = defaultCopyOptions.storeJobPasswordType;
        storeJobPassword = defaultCopyOptions.storeJobPassword;
        storeJobDeleteOnPowerCycle = defaultCopyOptions.storeJobDeleteOnPowerCycle;
        storeJobDeleteOnRelease = defaultCopyOptions.storeJobDeleteOnRelease;
        progressDialogMode = defaultCopyOptions.progressDialogMode;
        eraseMarginUnit = defaultCopyOptions.eraseMarginUnit;
        eraseBackBottom = defaultCopyOptions.eraseBackBottom;
        eraseBackLeft = defaultCopyOptions.eraseBackLeft;
        eraseBackRight = defaultCopyOptions.eraseBackRight;
        eraseBackTop = defaultCopyOptions.eraseBackTop;
        eraseFrontBottom = defaultCopyOptions.eraseFrontBottom;
        eraseFrontLeft = defaultCopyOptions.eraseFrontLeft;
        eraseFrontRight = defaultCopyOptions.eraseFrontRight;
        eraseFrontTop = defaultCopyOptions.eraseFrontTop;
        captureMode = defaultCopyOptions.captureMode;
        imageShiftReduceToFit = defaultCopyOptions.imageShiftReduceToFit;
        imageShiftUnits = defaultCopyOptions.imageShiftUnits;
        imageShiftXFront = defaultCopyOptions.imageShiftXFront;
        imageShiftYFront = defaultCopyOptions.imageShiftYFront;
        imageShiftXBack = defaultCopyOptions.imageShiftXBack;
        imageShiftYBack = defaultCopyOptions.imageShiftYBack;
        bookletBordersEachPage = defaultCopyOptions.bookletBordersEachPage;
        bookletFinishingOption = defaultCopyOptions.bookletFinishingOption;
        bookletFormat = defaultCopyOptions.bookletFormat;
        stapleOption = defaultCopyOptions.stapleOption;
        punchMode = defaultCopyOptions.punchMode;
        foldMode = defaultCopyOptions.foldMode;
        stampOptionMap = defaultCopyOptions.stampOptionMap;
        watermarkDarkness = defaultCopyOptions.watermarkDarkness;
        watermarkText = defaultCopyOptions.watermarkText;
        watermarkRotate45 = defaultCopyOptions.watermarkRotate45;
        watermarkType = defaultCopyOptions.watermarkType;
        watermarkTextSize = defaultCopyOptions.watermarkTextSize;
        watermarkTransparency = defaultCopyOptions.watermarkTransparency;
        watermarkBackgroundColor = defaultCopyOptions.watermarkBackgroundColor;
        watermarkFont = defaultCopyOptions.watermarkFont;
        watermarkTextColor = defaultCopyOptions.watermarkTextColor;
        watermarkOnlyFirstPage = defaultCopyOptions.watermarkOnlyFirstPage;
        watermarkBackgroundPattern = defaultCopyOptions.watermarkBackgroundPattern;
        watermarkMessageType = defaultCopyOptions.watermarkMessageType;
    }

    /**
     * Builds a CopyOptions instance from the provided HTTP request/response
     * @param device
     *              OXPd device instance
     * @param requestResponse
     *              HTTP request/response pair
     * @param tagHandler
     *              XML tag handler
     * @return
     *              CopyOptions instance
     * @throws Error
     *              When errors are detected
     */
    @SuppressWarnings("unchecked")
    static CopyOptions parseRequestResult(OXPdDevice device, HttpRequestResponseContainer requestResponse, RestXMLTagHandler tagHandler) throws Error {

        RestXMLTagHandler.XMLEndTagHandler infoCollector = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
//                Log.i("[SIM]","infoCollector process..");
                try {


                    if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__COPIES, localName)) {
                        Long copies;
                        try {
                            copies = Long.valueOf(data);
                        } catch (Exception ignored) {
                            copies = 1L;
                        }
                        handler.setTagData(localName, copies);
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__ORIGINAL_SIDES, localName)) {
                        handler.setTagData(localName, PlexMode.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__ORIGINAL_DUPLEX_FORMAT, localName)) {
                        handler.setTagData(localName, DuplexFormat.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__OUTPUT_SIDES, localName)) {
                        handler.setTagData(localName, PlexMode.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__OUTPUT_DUPLEX_FORMAT, localName)) {
                        handler.setTagData(localName, DuplexFormat.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__COLOR_MODE, localName)) {
                        handler.setTagData(localName, ColorMode.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__BACKGROUND_CLEANUP, localName)) {
                        handler.setTagData(localName, BackgroundCleanup.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__ORIGINAL_MEDIA_SIZE, localName)) {
                        handler.setTagData(localName, MediaSizeId.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__ORIGINAL_CUSTOM_SIZE_X, localName)) {
                        Float custom;
                        try {
                            custom = Float.valueOf(data);
                        } catch (Exception ignored) {
                            custom = 8.5f;
                        }
                        handler.setTagData(localName, custom);
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__ORIGINAL_CUSTOM_SIZE_Y, localName)) {
                        Float custom;
                        try {
                            custom = Float.valueOf(data);
                        } catch (Exception ignored) {
                            custom = 11f;
                        }
                        handler.setTagData(localName, custom);
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__OUTPUT_MEDIA_SIZE, localName)) {
                        handler.setTagData(localName, MediaSizeId.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__OUTPUT_CUSTOM_SIZE_X, localName)) {
                        Float custom;
                        try {
                            custom = Float.valueOf(data);
                        } catch (Exception ignored) {
                            custom = 8.5f;
                        }
                        handler.setTagData(localName, custom);
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__OUTPUT_CUSTOM_SIZE_Y, localName)) {
                        Float custom;
                        try {
                            custom = Float.valueOf(data);
                        } catch (Exception ignored) {
                            custom = 11f;
                        }
                        handler.setTagData(localName, custom);
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__OUTPUT_MEDIA_TYPE, localName)) {
                        handler.setTagData(localName, MediaTypeId.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__OUTPUT_MEDIA_TRAY, localName)) {
                        handler.setTagData(localName, MediaInputId.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__CONTENT_ORIENTATION, localName)) {
                        handler.setTagData(localName, ContentOrientation.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__SHARPNESS_ADJUSTMENT, localName)) {
                        handler.setTagData(localName, SharpnessAdjustment.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__DARKNESS_ADJUSTMENT, localName)) {
                        handler.setTagData(localName, DarknessAdjustment.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__CONTRAST_ADJUSTMENT, localName)) {
                        handler.setTagData(localName, ContrastAdjustment.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__BACKGROUND_CLEANUP, localName)) {
                        handler.setTagData(localName, BackgroundCleanup.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__TEXT_GRAPHIC_OPTIMIZATION, localName)) {
                        handler.setTagData(localName, TextGraphicsOptimization.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__OUTPUT_COLLATION, localName)) {
                        handler.setTagData(localName, SheetCollationMode.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__OUTPUT_BIN, localName)) {
                        handler.setTagData(localName, MediaOutputId.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__JOB_ASSEMBLY, localName)) {
                        handler.setTagData(localName, JobAssemblyMode.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__JOB_PREVIEW, localName)) {
                        handler.setTagData(localName, PreviewMode.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__REDUCE_ENLARGE, localName)) {
                        handler.setTagData(localName, ReduceEnlargeMode.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__REDUCE_ENLARGE_MARGINS_INCLUDED, localName)) {
                        handler.setTagData(localName, Boolean.valueOf(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__REDUCE_ENLARGE_PERCENT, localName)) {
                        handler.setTagData(localName, Long.valueOf(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__SCAN_SOURCE, localName)) {
                        handler.setTagData(localName, ScanSourceId.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__JOB_EXECUTION_MODE, localName)) {
                        handler.setTagData(localName, JobExecutionMode.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__NUMBER_UP_COUNT, localName)) {
                        handler.setTagData(localName, NumberUpCount.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__NUMBER_UP_DIRECTION, localName)) {
                        handler.setTagData(localName, NumberUpDirection.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__STORE_JOB_FOLDER_NAME, localName)) {
                        handler.setTagData(localName, data);
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__STORE_JOB_NAME, localName)) {
                        handler.setTagData(localName, data);
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__STORE_JOB_PASSWORD_TYPE, localName)) {
                        handler.setTagData(localName, PasswordType.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__STORE_JOB_PASSWORD, localName)) {
                        handler.setTagData(localName, data);
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__STORE_DELETE_ON_POWER_CYCLE, localName)) {
                        handler.setTagData(localName, Boolean.valueOf(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__STORE_DELETE_ON_RELEASE, localName)) {
                        handler.setTagData(localName, Boolean.valueOf(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__PROGRESS_DIALOG_MODE, localName)) {
                        handler.setTagData(localName, ProgressDialogMode.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__ERASE_MARGIN_UNIT, localName)) {
                        handler.setTagData(localName, EraseMarginUnit.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__ERASE_BACK_BOTTOM, localName)) {
                        setFloatTagData(handler, localName, data, 0.0f);
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__ERASE_BACK_LEFT, localName)) {
                        setFloatTagData(handler, localName, data, 0.0f);
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__ERASE_BACK_RIGHT, localName)) {
                        setFloatTagData(handler, localName, data, 0.0f);
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__ERASE_BACK_TOP, localName)) {
                        setFloatTagData(handler, localName, data, 0.0f);
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__ERASE_FRONT_BOTTOM, localName)) {
                        setFloatTagData(handler, localName, data, 0.0f);
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__ERASE_FRONT_LEFT, localName)) {
                        setFloatTagData(handler, localName, data, 0.0f);
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__ERASE_FRONT_RIGHT, localName)) {
                        setFloatTagData(handler, localName, data, 0.0f);
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__ERASE_FRONT_TOP, localName)) {
                        setFloatTagData(handler, localName, data, 0.0f);
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__CAPTURE_MODE, localName)) {
                        handler.setTagData(localName, CaptureMode.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__IMAGE_SHIFT_REDUCE_TO_FIT, localName)) {
                        handler.setTagData(localName, ImageShiftReduceToFit.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__IMAGE_SHIFT_UNITS, localName)) {
                        handler.setTagData(localName, ImageShiftUnits.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__IMAGE_SHIFT_X_FRONT, localName)) {
                        setFloatTagData(handler, localName, data, 0.0f);
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__IMAGE_SHIFT_Y_FRONT, localName)) {
                        setFloatTagData(handler, localName, data, 0.0f);
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__IMAGE_SHIFT_X_BACK, localName)) {
                        setFloatTagData(handler, localName, data, 0.0f);
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__IMAGE_SHIFT_Y_BACK, localName)) {
                        setFloatTagData(handler, localName, data, 0.0f);
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__BOOKLET_BORDERS_EACH_PAGE, localName)) {
                        handler.setTagData(localName, BookletBordersEachPage.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__BOOKLET_FINISHING_OPTION, localName)) {
                        handler.setTagData(localName, BookletFinishingOption.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__BOOKLET_FORMAT, localName)) {
                        handler.setTagData(localName, BookletFormat.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__STAPLE_OPTION, localName)) {
                        handler.setTagData(localName, StapleOption.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__PUNCH_MODE, localName)) {
                        handler.setTagData(localName, PunchMode.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__FOLD_MODE, localName)) {
                        handler.setTagData(localName, FoldMode.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__STAMP_TYPE, localName)) {
                        String stampPosition = getStampTagFromXmlStack(xmlTagStack);
                        StampOption stampOption = (StampOption) handler.getTagData(stampPosition);
                        if (stampOption == null) {
                            stampOption = new StampOption();
                            handler.setTagData(stampPosition, stampOption);
                        }
                        stampOption.type = StampType.valueOf(data.toUpperCase());
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__STAMP_TEXT, localName)) {
                        String stampPosition = getStampTagFromXmlStack(xmlTagStack);
                        StampOption stampOption = (StampOption) handler.getTagData(stampPosition);
                        if (stampOption == null) {
                            stampOption = new StampOption();
                            handler.setTagData(stampPosition, stampOption);
                        }
                        stampOption.text = data;
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__STAMP_FORMAT_FONT, localName)) {
                        String stampPosition = getStampTagFromXmlStack(xmlTagStack);
                        StampOption stampOption = (StampOption) handler.getTagData(stampPosition);
                        if (stampOption == null) {
                            stampOption = new StampOption();
                            handler.setTagData(stampPosition, stampOption);
                        }
                        if (stampOption.format == null) {
                            stampOption.format = new StampFormat();
                        }
                        stampOption.format.font = data;
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__STAMP_FORMAT_STARTING_PAGE, localName)) {
                        String stampPosition = getStampTagFromXmlStack(xmlTagStack);
                        StampOption stampOption = (StampOption) handler.getTagData(stampPosition);
                        if (stampOption == null) {
                            stampOption = new StampOption();
                            handler.setTagData(stampPosition, stampOption);
                        }
                        if (stampOption.format == null) {
                            stampOption.format = new StampFormat();
                        }
                        stampOption.format.startingPage = Integer.parseInt(data);
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__STAMP_FORMAT_TEXT_COLOR, localName)) {
                        String stampPosition = getStampTagFromXmlStack(xmlTagStack);
                        StampOption stampOption = (StampOption) handler.getTagData(stampPosition);
                        if (stampOption == null) {
                            stampOption = new StampOption();
                            handler.setTagData(stampPosition, stampOption);
                        }
                        if (stampOption.format == null) {
                            stampOption.format = new StampFormat();
                        }
                        stampOption.format.textColor = data;
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__STAMP_FORMAT_TEXT_SIZE, localName)) {
                        String stampPosition = getStampTagFromXmlStack(xmlTagStack);
                        StampOption stampOption = (StampOption) handler.getTagData(stampPosition);
                        if (stampOption == null) {
                            stampOption = new StampOption();
                            handler.setTagData(stampPosition, stampOption);
                        }
                        if (stampOption.format == null) {
                            stampOption.format = new StampFormat();
                        }
                        stampOption.format.textSize = Integer.parseInt(data);
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__STAMP_FORMAT_WHITE_BACKGROUND, localName)) {
                        String stampPosition = getStampTagFromXmlStack(xmlTagStack);
                        StampOption stampOption = (StampOption) handler.getTagData(stampPosition);
                        if (stampOption == null) {
                            stampOption = new StampOption();
                            handler.setTagData(stampPosition, stampOption);
                        }
                        if (stampOption.format == null) {
                            stampOption.format = new StampFormat();
                        }
                        stampOption.format.whiteBackground = Boolean.parseBoolean(data);
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__STAMP_POLICY_TYPE, localName)) {
                        String stampPosition = getStampTagFromXmlStack(xmlTagStack);
                        StampOption stampOption = (StampOption) handler.getTagData(stampPosition);
                        if (stampOption == null) {
                            stampOption = new StampOption();
                            handler.setTagData(stampPosition, stampOption);
                        }
                        stampOption.policyType = StampPolicyType.valueOf(data.toUpperCase());

                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__WATERMARK_DARKNESS, localName)) {
                        Long watermarkdarkness;
                        try {
                            watermarkdarkness = Long.valueOf(data);
                        } catch (Exception ignored) {
                            watermarkdarkness = 1L;
                        }
                        handler.setTagData(localName, watermarkdarkness);
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__WATERMARK_TEXT, localName)) {
                        handler.setTagData(localName, data);
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__WATERMARK_TEXT_SIZE, localName)) {
                        Long watermarktextsize;
                        try {
                            watermarktextsize = Long.valueOf(data);
                        } catch (Exception ignored) {
                            watermarktextsize = 1L;
                        }
                        handler.setTagData(localName, watermarktextsize);
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__WATERMARK_TRANSPARENCY, localName)) {
                        Long watermarktransparency;
                        try {
                            watermarktransparency = Long.valueOf(data);
                        } catch (Exception ignored) {
                            watermarktransparency = 1L;
                        }
                        handler.setTagData(localName, watermarktransparency);
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__WATERMARK_ROTATE45, localName)) {
                        handler.setTagData(localName, WatermarkRotate45.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__WATERMARK_TYPE, localName)) {
                        handler.setTagData(localName, WatermarkType.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__WATERMARK_BACKGROUND_PATTERN, localName)) {
                        handler.setTagData(localName, WatermarkPattern.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__WATERMARK_MESSAGETYPE, localName)) {
                        handler.setTagData(localName, WatermarkMessageType.fromAttributeValue(data));
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__WATERMARK_FONT, localName)) {
                        handler.setTagData(localName, data);
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__WATERMARK_BACKGROUND_COLOR, localName)) {
                        handler.setTagData(localName, data);
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__WATERMARK_TEXT_COLOR, localName)) {
                        handler.setTagData(localName, data);
                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__WATERMARK_ONLY_FIRST_PAGE, localName)) {
                        handler.setTagData(localName, WatermarkOnlyFirstPage.fromAttributeValue(data));
                    }
                }catch (Exception e){
                    Log.e("[SIM]",e.getMessage(),e);
                }
            }
            private String getStampTagFromXmlStack(RestXMLTagStack xmlTagStack) {
                if(xmlTagStack.isTagInStack("",OXPdCopy.Constants.XML_TAG__COPY__STAMP_TOP_LEFT)) {
                    return OXPdCopy.Constants.XML_TAG__COPY__STAMP_TOP_LEFT;
                } else if(xmlTagStack.isTagInStack("",OXPdCopy.Constants.XML_TAG__COPY__STAMP_TOP_CENTER)) {
                    return OXPdCopy.Constants.XML_TAG__COPY__STAMP_TOP_CENTER;
                } else if(xmlTagStack.isTagInStack("",OXPdCopy.Constants.XML_TAG__COPY__STAMP_TOP_RIGHT)) {
                    return OXPdCopy.Constants.XML_TAG__COPY__STAMP_TOP_RIGHT;
                } else if(xmlTagStack.isTagInStack("",OXPdCopy.Constants.XML_TAG__COPY__STAMP_BOTTOM_LEFT)) {
                    return OXPdCopy.Constants.XML_TAG__COPY__STAMP_BOTTOM_LEFT;
                } else if(xmlTagStack.isTagInStack("",OXPdCopy.Constants.XML_TAG__COPY__STAMP_BOTTOM_CENTER)) {
                    return OXPdCopy.Constants.XML_TAG__COPY__STAMP_BOTTOM_CENTER;
                } else if(xmlTagStack.isTagInStack("",OXPdCopy.Constants.XML_TAG__COPY__STAMP_BOTTOM_RIGHT)) {
                    return OXPdCopy.Constants.XML_TAG__COPY__STAMP_BOTTOM_RIGHT;
                }
                return null;
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
        };

        try{
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__COPIES, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__ORIGINAL_SIDES, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__ORIGINAL_DUPLEX_FORMAT, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__OUTPUT_SIDES, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__OUTPUT_DUPLEX_FORMAT, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__COLOR_MODE, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__ORIGINAL_MEDIA_SIZE, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__ORIGINAL_CUSTOM_SIZE_X, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__ORIGINAL_CUSTOM_SIZE_Y, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__OUTPUT_MEDIA_SIZE, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__OUTPUT_CUSTOM_SIZE_X, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__OUTPUT_CUSTOM_SIZE_Y, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__OUTPUT_MEDIA_TYPE, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__OUTPUT_MEDIA_TRAY, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__CONTENT_ORIENTATION, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__SHARPNESS_ADJUSTMENT, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__DARKNESS_ADJUSTMENT, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__CONTRAST_ADJUSTMENT, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__BACKGROUND_CLEANUP, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__TEXT_GRAPHIC_OPTIMIZATION, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__OUTPUT_COLLATION, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__OUTPUT_BIN, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__JOB_ASSEMBLY, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__JOB_PREVIEW, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__REDUCE_ENLARGE, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__REDUCE_ENLARGE_MARGINS_INCLUDED, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__REDUCE_ENLARGE_PERCENT, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__SCAN_SOURCE, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__JOB_EXECUTION_MODE, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__NUMBER_UP_COUNT, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__NUMBER_UP_DIRECTION, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__STORE_JOB_FOLDER_NAME, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__STORE_JOB_NAME, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__STORE_JOB_PASSWORD_TYPE, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__STORE_JOB_PASSWORD, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__STORE_DELETE_ON_POWER_CYCLE, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__STORE_DELETE_ON_RELEASE, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__PROGRESS_DIALOG_MODE, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__ERASE_MARGIN_UNIT,null,infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__ERASE_BACK_BOTTOM, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__ERASE_BACK_LEFT, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__ERASE_BACK_RIGHT, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__ERASE_BACK_TOP, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__ERASE_FRONT_BOTTOM, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__ERASE_FRONT_LEFT, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__ERASE_FRONT_RIGHT, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__ERASE_FRONT_TOP, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__CAPTURE_MODE,null,infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__IMAGE_SHIFT_REDUCE_TO_FIT, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__IMAGE_SHIFT_UNITS, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__IMAGE_SHIFT_X_FRONT, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__IMAGE_SHIFT_Y_FRONT, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__IMAGE_SHIFT_X_BACK, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__IMAGE_SHIFT_Y_BACK, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__BOOKLET_BORDERS_EACH_PAGE, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__BOOKLET_FINISHING_OPTION, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__BOOKLET_FORMAT, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__STAPLE_OPTION, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__PUNCH_MODE, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__FOLD_MODE,null,infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__STAMP_POLICY_TYPE, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__STAMP_TYPE, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__STAMP_FORMAT_FONT, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__STAMP_FORMAT_STARTING_PAGE, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__STAMP_FORMAT_TEXT_COLOR, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__STAMP_FORMAT_TEXT_SIZE, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__STAMP_FORMAT_WHITE_BACKGROUND, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__STAMP_TEXT, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__WATERMARK_DARKNESS, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__WATERMARK_TEXT, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__WATERMARK_ROTATE45, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__WATERMARK_TEXT_SIZE, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__WATERMARK_TRANSPARENCY, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__WATERMARK_FONT, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__WATERMARK_BACKGROUND_COLOR, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__WATERMARK_TEXT_COLOR, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__WATERMARK_ONLY_FIRST_PAGE, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__WATERMARK_TYPE, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__WATERMARK_BACKGROUND_PATTERN, null, infoCollector);
            tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__WATERMARK_MESSAGETYPE, null, infoCollector);

        }catch(Exception e){
            Log.e("[SIM]",e.getMessage(),e);
        }

        device.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);

        return new CopyOptions(tagHandler);
    }



    CopyOptions(Parcel in) {
        copies = in.readLong();
        originalSides = PlexMode.values()[in.readInt()];
        originalDuplexFormat = DuplexFormat.values()[in.readInt()];
        outputSides = PlexMode.values()[in.readInt()];
        outputDuplexFormat = DuplexFormat.values()[in.readInt()];
        colorMode = ColorMode.values()[in.readInt()];
        originalMediaSize = MediaSizeId.values()[in.readInt()];
        originalCustomSizeX = in.readFloat();
        originalCustomSizeY = in.readFloat();
        outputMediaSize = MediaSizeId.values()[in.readInt()];
        outputCustomSizeX = in.readFloat();
        outputCustomSizeY = in.readFloat();
        outputMediaType = MediaTypeId.values()[in.readInt()];
        outputMediaTray = MediaInputId.values()[in.readInt()];
        contentOrientation = ContentOrientation.values()[in.readInt()];
        sharpnessAdjustment = SharpnessAdjustment.values()[in.readInt()];
        darknessAdjustment = DarknessAdjustment.values()[in.readInt()];
        contrastAdjustment = ContrastAdjustment.values()[in.readInt()];
        backgroundCleanup = BackgroundCleanup.values()[in.readInt()];
        textGraphicsOptimization = TextGraphicsOptimization.values()[in.readInt()];
        outputCollation = SheetCollationMode.values()[in.readInt()];
        outputBin = MediaOutputId.values()[in.readInt()];
        jobAssembly = JobAssemblyMode.values()[in.readInt()];
        jobPreview = PreviewMode.values()[in.readInt()];
        reduceEnlarge = ReduceEnlargeMode.values()[in.readInt()];
        reduceEnlargeMarginsIncluded = in.readByte() != 0;
        reduceEnlargePercent = in.readLong();
        scanSource = ScanSourceId.values()[in.readInt()];
        jobExecutionMode = JobExecutionMode.values()[in.readInt()];
        numberUpCount = NumberUpCount.values()[in.readInt()];
        numberUpDirection = NumberUpDirection.values()[in.readInt()];
        storeJobFolderName = in.readString();
        storeJobName = in.readString();
        storeJobPasswordType = PasswordType.values()[in.readInt()];
        storeJobPassword = in.readString();
        storeJobDeleteOnPowerCycle = in.readByte() != 0;
        storeJobDeleteOnRelease = in.readByte() != 0;
        progressDialogMode = ProgressDialogMode.values()[in.readInt()];
        watermarkDarkness = in.readLong();
        watermarkText = in.readString();
        watermarkRotate45 = WatermarkRotate45.values()[in.readInt()];
        watermarkType = WatermarkType.values()[in.readInt()];
        watermarkTextSize = in.readLong();
        watermarkTransparency = in.readLong();
        watermarkBackgroundColor = in.readString();
        watermarkFont = in.readString();
        watermarkTextColor = in.readString();
        watermarkOnlyFirstPage = WatermarkOnlyFirstPage.values()[in.readInt()];
        watermarkMessageType = WatermarkMessageType.values()[in.readInt()];
        watermarkBackgroundPattern = WatermarkPattern.values()[in.readInt()];
        eraseMarginUnit = EraseMarginUnit.values()[in.readInt()];
        this.eraseBackBottom = in.readFloat();
        this.eraseBackLeft = in.readFloat();
        this.eraseBackRight = in.readFloat();
        this.eraseBackTop = in.readFloat();
        this.eraseFrontBottom = in.readFloat();
        this.eraseFrontLeft = in.readFloat();
        this.eraseFrontRight = in.readFloat();
        this.eraseFrontTop = in.readFloat();
        captureMode = CaptureMode.values()[in.readInt()];
        imageShiftReduceToFit = ImageShiftReduceToFit.values()[in.readInt()];
        imageShiftUnits = ImageShiftUnits.values()[in.readInt()];
        imageShiftXFront = in.readFloat();
        imageShiftYFront = in.readFloat();
        imageShiftXBack = in.readFloat();
        imageShiftYBack = in.readFloat();
        bookletBordersEachPage = BookletBordersEachPage.values()[in.readInt()];
        bookletFinishingOption = BookletFinishingOption.values()[in.readInt()];
        bookletFormat = BookletFormat.values()[in.readInt()];
        stapleOption = StapleOption.values()[in.readInt()];
        punchMode = PunchMode.values()[in.readInt()];
        foldMode = FoldMode.values()[in.readInt()];
        stampOptionMap = in.readHashMap(StampOption.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(copies);
        dest.writeInt(this.originalSides.ordinal());
        dest.writeInt(this.originalDuplexFormat.ordinal());
        dest.writeInt(this.outputSides.ordinal());
        dest.writeInt(this.outputDuplexFormat.ordinal());
        dest.writeInt(this.colorMode.ordinal());
        dest.writeInt(this.originalMediaSize.ordinal());
        dest.writeFloat(originalCustomSizeX);
        dest.writeFloat(originalCustomSizeY);
        dest.writeInt(this.outputMediaSize.ordinal());
        dest.writeFloat(outputCustomSizeX);
        dest.writeFloat(outputCustomSizeY);
        dest.writeInt(this.outputMediaType.ordinal());
        dest.writeInt(this.outputMediaTray.ordinal());
        dest.writeInt(this.contentOrientation.ordinal());
        dest.writeInt(this.sharpnessAdjustment.ordinal());
        dest.writeInt(this.darknessAdjustment.ordinal());
        dest.writeInt(this.contrastAdjustment.ordinal());
        dest.writeInt(this.backgroundCleanup.ordinal());
        dest.writeInt(this.textGraphicsOptimization.ordinal());
        dest.writeInt(this.outputCollation.ordinal());
        dest.writeInt(this.outputBin.ordinal());
        dest.writeInt(this.jobAssembly.ordinal());
        dest.writeInt(this.jobPreview.ordinal());
        dest.writeInt(this.reduceEnlarge.ordinal());
        dest.writeByte((byte) (reduceEnlargeMarginsIncluded ? 1 : 0));
        dest.writeLong(reduceEnlargePercent);
        dest.writeInt(this.scanSource.ordinal());
        dest.writeInt(this.jobExecutionMode.ordinal());
        dest.writeInt(this.numberUpCount.ordinal());
        dest.writeInt(this.numberUpDirection.ordinal());
        dest.writeString(this.storeJobFolderName);
        dest.writeString(this.storeJobName);
        dest.writeInt(this.storeJobPasswordType.ordinal());
        dest.writeString(this.storeJobPassword);
        dest.writeByte((byte) (storeJobDeleteOnPowerCycle ? 1 : 0));
        dest.writeByte((byte) (storeJobDeleteOnRelease ? 1 : 0));
        dest.writeInt(this.progressDialogMode.ordinal());
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
        dest.writeInt(this.imageShiftReduceToFit.ordinal());
        dest.writeInt(this.imageShiftUnits.ordinal());
        dest.writeFloat(this.imageShiftXFront);
        dest.writeFloat(this.imageShiftYFront);
        dest.writeFloat(this.imageShiftXBack);
        dest.writeFloat(this.imageShiftYBack);
        dest.writeInt(this.bookletBordersEachPage.ordinal());
        dest.writeInt(this.bookletFinishingOption.ordinal());
        dest.writeInt(this.bookletFormat.ordinal());
        dest.writeInt(this.stapleOption.ordinal());
        dest.writeInt(this.punchMode.ordinal());
        dest.writeInt(this.foldMode.ordinal());
        dest.writeMap(this.stampOptionMap);
        dest.writeLong(watermarkDarkness);
        dest.writeString(this.watermarkText);
        dest.writeInt(this.watermarkRotate45.ordinal());
        dest.writeInt(this.watermarkType.ordinal());
        dest.writeLong(watermarkTextSize);
        dest.writeLong(watermarkTransparency);
        dest.writeString(this.watermarkBackgroundColor);
        dest.writeString(this.watermarkFont);
        dest.writeString(this.watermarkTextColor);
        dest.writeInt(this.watermarkOnlyFirstPage.ordinal());
        dest.writeInt(this.watermarkBackgroundPattern.ordinal());
        dest.writeInt(this.watermarkMessageType.ordinal());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CopyOptions> CREATOR = new Creator<CopyOptions>() {
        @Override
        public CopyOptions createFromParcel(Parcel in) {
            return new CopyOptions(in);
        }

        @Override
        public CopyOptions[] newArray(int size) {
            return new CopyOptions[size];
        }
    };
}
