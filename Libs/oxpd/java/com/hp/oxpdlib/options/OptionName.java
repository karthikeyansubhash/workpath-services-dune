// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.options;

import android.text.TextUtils;
import android.util.Log;

public enum OptionName {
    COPIES("copies"),
    COLOR_MODE("colorMode"),
    ORIGINAL_SIDES("originalSides"),
    ORIGINAL_DUPLEX_FORMAT("originalDuplexFormat"),
    ORIGINAL_CUSTOM_SIZE_X("originalCustomSizeX"),
    ORIGINAL_CUSTOM_SIZE_Y("originalCustomSizeY"),
    ORIGINAL_MEDIA_SIZE("originalMediaSize"),
    OUTPUT_MEDIA_TYPE("outputMediaType"),
    OUTPUT_MEDIA_SIZE("outputMediaSize"),
    OUTPUT_CUSTOM_SIZE_X("outputCustomSizeX"),
    OUTPUT_CUSTOM_SIZE_Y("outputCustomSizeY"),
    OUTPUT_MEDIA_TRAY("outputMediaTray"),
    OUTPUT_SIDES("outputSides"),
    OUTPUT_DUPLEX_FORMAT("outputDuplexFormat"),
    OUTPUT_BIN("outputBin"),
    OUTPUT_COLLATION("outputCollation"),
    CONTENT_ORIENTATION("contentOrientation"),
    SHARPNESS_ADJUSTMENT("sharpnessAdjustment"),
    DARKNESS_ADJUSTMENT("darknessAdjustment"),
    CONTRAST_ADJUSTMENT("contrastAdjustment"),
    BACKGROUND_CLEANUP("backgroundCleanup"),
    TEXT_GRAPHICS_OPTIMIZATION("textGraphicsOptimization"),
    JOB_ASSEMBLY("jobAssembly"),
    JOB_PREVIEW("jobPreview"),
    REDUCE_ENLARGE("reduceEnlarge"),
    REDUCE_ENLARGE_MARGINS_INCLUDED("reduceEnlargeMarginsIncluded"),
    REDUCE_ENLARGE_PERCENT("reduceEnlargePercent"),
    SCAN_SOURCE("scanSource"),
    JOB_EXECUTION_MODE("jobExecutionMode"),
    NUMBER_UP_COUNT("numberUpCount"),
    NUMBER_UP_DIRECTION("numberUpDirection"),
    STORE_JOB_FOLDER_NAME("storeJobFolderName"),
    STORE_JOB_NAME("storeJobName"),
    STORE_JOB_PASSWORD_TYPE("storeJobPasswordType"),
    STORE_JOB_PASSWORD("storeJobPassword"),
    STORE_JOB_DELETE_ON_POWER_CYCLE("storeJobDeleteOnPowerCycle"),
    STORE_JOB_DELETE_ON_RELEASE("storeJobDeleteOnRelease"),
    PROGRESS_DIALOG_MODE("progressDialogMode"),
    ERASE_MARGIN_UNIT("eraseMarginUnits"),
    ERASE_BACK_BOTTOM("eraseBackBottom"),
    ERASE_BACK_LEFT("eraseBackLeft"),
    ERASE_BACK_RIGHT("eraseBackRight"),
    ERASE_BACK_TOP("eraseBackTop"),
    ERASE_FRONT_BOTTOM("eraseFrontBottom"),
    ERASE_FRONT_LEFT("eraseFrontLeft"),
    ERASE_FRONT_RIGHT("eraseFrontRight"),
    ERASE_FRONT_TOP("eraseFrontTop"),
    CAPTURE_MODE("captureMode"),
    IMAGE_SHIFT_REDUCE_TO_FIT("imageShiftReduceToFit"),
    IMAGE_SHIFT_UNITS("imageShiftUnits"),
    IMAGE_SHIFT_X_FRONT("imageShiftXFront"),
    IMAGE_SHIFT_Y_FRONT("imageShiftYFront"),
    IMAGE_SHIFT_X_BACK("imageShiftXBack"),
    IMAGE_SHIFT_Y_BACK("imageShiftYBack"),
    BOOKLET_BORDERS_EACH_PAGE("bookletBordersEachPage"),
    BOOKLET_FINISHING_OPTION("bookletFinishingOption"),
    BOOKLET_FORMAT("bookletFormat"),
    STAMP_TOP_LEFT_TYPE("stampTopLeft.type"),
    STAMP_TOP_LEFT_TEXT("stampTopLeft.text"),
    STAMP_TOP_LEFT_POLICY_TYPE("stampTopLeft.policyType"),
    STAMP_TOP_LEFT_FORMAT_FONT("stampTopLeft.format.font"),
    STAMP_TOP_LEFT_FORMAT_TEXT_SIZE("stampTopLeft.format.textSize"),
    STAMP_TOP_LEFT_FORMAT_TEXT_COLOR("stampTopLeft.format.textColor"),
    STAMP_TOP_LEFT_FORMAT_WHITE_BACKGROUND("stampTopLeft.format.whiteBackground"),
    STAMP_TOP_LEFT_FORMAT_STARTING_PAGE("stampTopLeft.format.startingPage"),
    STAMP_TOP_CENTER_TYPE("stampTopCenter.type"),
    STAMP_TOP_CENTER_TEXT("stampTopCenter.text"),
    STAMP_TOP_CENTER_POLICY_TYPE("stampTopCenter.policyType"),
    STAMP_TOP_CENTER_FORMAT_FONT("stampTopCenter.format.font"),
    STAMP_TOP_CENTER_FORMAT_TEXT_SIZE("stampTopCenter.format.textSize"),
    STAMP_TOP_CENTER_FORMAT_TEXT_COLOR("stampTopCenter.format.textColor"),
    STAMP_TOP_CENTER_FORMAT_WHITE_BACKGROUND("stampTopCenter.format.whiteBackground"),
    STAMP_TOP_CENTER_FORMAT_STARTING_PAGE("stampTopCenter.format.startingPage"),
    STAMP_TOP_RIGHT_TYPE("stampTopRight.type"),
    STAMP_TOP_RIGHT_TEXT("stampTopRight.text"),
    STAMP_TOP_RIGHT_POLICY_TYPE("stampTopRight.policyType"),
    STAMP_TOP_RIGHT_FORMAT_FONT("stampTopRight.format.font"),
    STAMP_TOP_RIGHT_FORMAT_TEXT_SIZE("stampTopRight.format.textSize"),
    STAMP_TOP_RIGHT_FORMAT_TEXT_COLOR("stampTopRight.format.textColor"),
    STAMP_TOP_RIGHT_FORMAT_WHITE_BACKGROUND("stampTopRight.format.whiteBackground"),
    STAMP_TOP_RIGHT_FORMAT_STARTING_PAGE("stampTopRight.format.startingPage"),
    STAMP_BOTTOM_LEFT_TYPE("stampBottomLeft.type"),
    STAMP_BOTTOM_LEFT_TEXT("stampBottomLeft.text"),
    STAMP_BOTTOM_LEFT_POLICY_TYPE("stampBottomLeft.policyType"),
    STAMP_BOTTOM_LEFT_FORMAT_FONT("stampBottomLeft.format.font"),
    STAMP_BOTTOM_LEFT_FORMAT_TEXT_SIZE("stampBottomLeft.format.textSize"),
    STAMP_BOTTOM_LEFT_FORMAT_TEXT_COLOR("stampBottomLeft.format.textColor"),
    STAMP_BOTTOM_LEFT_FORMAT_WHITE_BACKGROUND("stampBottomLeft.format.whiteBackground"),
    STAMP_BOTTOM_LEFT_FORMAT_STARTING_PAGE("stampBottomLeft.format.startingPage"),
    STAMP_BOTTOM_CENTER_TYPE("stampBottomCenter.type"),
    STAMP_BOTTOM_CENTER_TEXT("stampBottomCenter.text"),
    STAMP_BOTTOM_CENTER_POLICY_TYPE("stampBottomCenter.policyType"),
    STAMP_BOTTOM_CENTER_FORMAT_FONT("stampBottomCenter.format.font"),
    STAMP_BOTTOM_CENTER_FORMAT_TEXT_SIZE("stampBottomCenter.format.textSize"),
    STAMP_BOTTOM_CENTER_FORMAT_TEXT_COLOR("stampBottomCenter.format.textColor"),
    STAMP_BOTTOM_CENTER_FORMAT_WHITE_BACKGROUND("stampBottomCenter.format.whiteBackground"),
    STAMP_BOTTOM_CENTER_FORMAT_STARTING_PAGE("stampBottomCenter.format.startingPage"),
    STAMP_BOTTOM_RIGHT_TYPE("stampBottomRight.type"),
    STAMP_BOTTOM_RIGHT_TEXT("stampBottomRight.text"),
    STAMP_BOTTOM_RIGHT_POLICY_TYPE("stampBottomRight.policyType"),
    STAMP_BOTTOM_RIGHT_FORMAT_FONT("stampBottomRight.format.font"),
    STAMP_BOTTOM_RIGHT_FORMAT_TEXT_SIZE("stampBottomRight.format.textSize"),
    STAMP_BOTTOM_RIGHT_FORMAT_TEXT_COLOR("stampBottomRight.format.textColor"),
    STAMP_BOTTOM_RIGHT_FORMAT_WHITE_BACKGROUND("stampBottomRight.format.whiteBackground"),
    STAMP_BOTTOM_RIGHT_FORMAT_STARTING_PAGE("stampBottomRight.format.startingPage"),
    WATERMARK_DARKNESS("watermarkDarkness"),
    WATERMARK_TEXT("watermarkText"),
    WATERMARK_ROTATE45("watermarkRotate45"),
    WATERMARK_TYPE("watermarkType"),
    WATERMARK_TEXT_SIZE("watermarkTextSize"),
    WATERMARK_TRANSPARENCY("watermarkTransparency"),
    WATERMARK_BACKGROUND_COLOR("watermarkBackgroundColor"),
    WATERMARK_FONT("watermarkFont"),
    WATERMARK_TEXT_COLOR("watermarkTextColor"),
    WATERMARK_ONLY_FIRST_PAGE("watermarkOnlyFirstPage"),
    WATERMARK_MESSAGETYPE("watermarkMessageType"),
    WATERMARK_BACKGROUND_PATTERN("watermarkBackgroundPattern"),

    STAPLE_OPTION("stapleLocation"),
    PUNCH_MODE("punchOption"),
    FOLD_MODE("foldOption");
    public String mValue;

    OptionName(String optionName) {
        this.mValue = optionName;
    }

    /**
     * Convert SOAP value string to OptionName
     * @param value
     *              SOAP value string
     * @return
     *              Matching OptionName enum or null if no match is found
     */
    public static OptionName fromAttributeValue(String value) {
        for(OptionName enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        Log.d("OXPdCopy", "Unknown " + value);
        return null;
    }
}
