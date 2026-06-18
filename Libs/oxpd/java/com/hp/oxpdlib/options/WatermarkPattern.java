// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.options;

import android.text.TextUtils;

/** Enumeration of scan preview modes. */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum WatermarkPattern {
    /** The device will rotate watermark preview images to the walkup user. */
    /** Watermark type is None. */
    Scroll("Scroll"),
    /** Watermark type is Text. */
    Leaf("Leaf"),
    /** Watermark type is secure. */
    Flat("Flat"),

    Other("Other");

    /**
     * SOAP value associated with enum
     */
    public final String mValue;

    /**
     * PreviewMode constructor
     * @param value
     *              SOAP value associated with enum
     */
    WatermarkPattern(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to PreviewMode
     * @param value
     *              SOAP value string
     * @return
     *              Matching PreviewMode enum or null if no match is found
     */
    public static WatermarkPattern fromAttributeValue(String value) {
        for(WatermarkPattern enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
