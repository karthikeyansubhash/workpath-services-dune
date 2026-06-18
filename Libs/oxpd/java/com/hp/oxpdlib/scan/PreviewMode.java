// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.text.TextUtils;

/** Enumeration of scan preview modes. */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum PreviewMode {
    /** No scan preview. */
    Off("Off"),
    /** The device will display preview images to the walkup user. */
    On("On");

    /**
     * SOAP value associated with enum
     */
    final String mValue;

    /**
     * PreviewMode constructor
     * @param value
     *              SOAP value associated with enum
     */
    PreviewMode(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to PreviewMode
     * @param value
     *              SOAP value string
     * @return
     *              Matching PreviewMode enum or null if no match is found
     */
    static PreviewMode fromAttributeValue(String value) {
        for(PreviewMode enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
