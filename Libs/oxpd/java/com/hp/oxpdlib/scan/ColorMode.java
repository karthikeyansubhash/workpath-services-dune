// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.text.TextUtils;

/** Enumeration of scan file types. */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum ColorMode {
    /** Black and White. 1 bit per pixel. */
    Black("Black"),
    /** Black and White. 8 bits per pixel. */
    Gray("Gray"),
    /** Color. 24 bits per pixel. */
    Color("Color"),
    /**
     * Automatically select appropriate output.
     * If the job consists of a mixture of B&W and color images (side of a sheet), the device will
     * detect the content of each image and generate appropriate output. */
    Auto("Auto");

    /**
     * SOAP value associated with enum
     */
    final String mValue;

    /**
     * ColorMode constructor
     * @param value
     *              SOAP value associated with enum
     */
    ColorMode(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to ColorMode
     * @param value
     *              SOAP value string
     * @return
     *              Matching ColorMode enum or null if no match is found
     */
    static ColorMode fromAttributeValue(String value) {
        for(ColorMode enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
