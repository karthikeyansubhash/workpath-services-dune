// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.text.TextUtils;

/** Enumeration of crop mode values. */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum CropMode {
    /** Do not crop. */
    Off("Off"),
    /** Remove white space from top, left, right, and bottom of scanned images. Note that the
     * resulting image will likely not conform to a standard media size. Blank images will
     * not be cropped. */
    On("On"),
    // Supported on after version 1.1
    /** Do not crop */
    DoNotCrop("DoNotCrop"),
    /** Crop the image/paper to media size. Note that the resulting image will likely not conform
     * to a standard media size. Blank images will not be cropped. */
    CropToPaper("CropToPaper"),
    /** Remove white space from top, left, right, and bottom of scanned images. Note that the
     * resulting image will likely not conform to a standard media size. Blank images will
     * not be cropped. */
    CropToContent("CropToContent"),
    ;

    /**
     * SOAP value associated with enum
     */
    final String mValue;

    /**
     * CropMode constructor
     * @param value
     *              SOAP value associated with enum
     */
    CropMode(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to CropMode
     * @param value
     *              SOAP value string
     * @return
     *              Matching CropMode enum or null if no match is found
     */
    static CropMode fromAttributeValue(String value) {
        for(CropMode enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }

    /**
     * Convert the crop mode to the version appropriate value
     * @param mode Crop mode to check
     * @param supportsVersion2 Version2 modes are supported
     * @return Return version specific crop mode for specified mode
     */
    static CropMode getVersionAppropriate(CropMode mode, boolean supportsVersion2) {
        if (mode == null) {
            return (supportsVersion2 ? DoNotCrop : Off);
        }
        if (supportsVersion2) {
            switch(mode) {
                case Off:
                    mode = DoNotCrop;
                    break;
                case On:
                    mode = CropToContent;
                    break;
                default:
                    break;
            }
        } else {
            switch(mode) {
                case DoNotCrop:
                    mode = Off;
                    break;
                case CropToPaper:
                case CropToContent:
                    mode = On;
                default:
                    break;
            }
        }
        return mode;
    }
}
