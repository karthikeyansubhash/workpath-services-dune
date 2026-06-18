// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.options;

import android.text.TextUtils;

/** Enumeration of scan preview modes. */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum WatermarkOnlyFirstPage {
    /** No WatermarkOnlyFirstPage. */
    False("false"),
    /** The device will WatermarkOnlyFirstPage images to the walkup user. */
    True("true");

    /**
     * SOAP value associated with enum
     */
    public final String mValue;

    /**
     * PreviewMode constructor
     * @param value
     *              SOAP value associated with enum
     */
    WatermarkOnlyFirstPage(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to PreviewMode
     * @param value
     *              SOAP value string
     * @return
     *              Matching PreviewMode enum or null if no match is found
     */
    public static WatermarkOnlyFirstPage fromAttributeValue(String value) {
        for(WatermarkOnlyFirstPage enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
