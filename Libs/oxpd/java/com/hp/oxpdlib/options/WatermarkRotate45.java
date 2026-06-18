// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.options;

import android.text.TextUtils;

/** Enumeration of scan preview modes. */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum WatermarkRotate45 {
    /** The device will rotate watermark preview images to the walkup user. */
    True("true"),
    /** No watermark rotation. */
    False("false");


    /**
     * SOAP value associated with enum
     */
    public final String mValue;

    /**
     * PreviewMode constructor
     * @param value
     *              SOAP value associated with enum
     */
    WatermarkRotate45(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to PreviewMode
     * @param value
     *              SOAP value string
     * @return
     *              Matching PreviewMode enum or null if no match is found
     */
    public static WatermarkRotate45 fromAttributeValue(String value) {
        for(WatermarkRotate45 enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
