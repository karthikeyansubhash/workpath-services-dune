// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.text.TextUtils;

/** Enumeration of color dropout modes. */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum ColorDropoutMode {
    /** No dropout. */
    Off("Off"),
    /** Remove the red color plane. */
    RemoveRed("RemoveRed"),
    /** Remove the green color plane. */
    RemoveGreen("RemoveGreen"),
    /** Remove the blue color plane. */
    RemoveBlue("RemoveBlue");

    /**
     * SOAP value associated with enum
     */
    final String mValue;

    /**
     * ColorDropoutMode constructor
     * @param value
     *              SOAP value associated with enum
     */
    ColorDropoutMode(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to ColorDropoutMode
     * @param value
     *              SOAP value string
     * @return
     *              Matching ColorDropoutMode enum or null if no match is found
     */
    static ColorDropoutMode fromAttributeValue(String value) {
        for(ColorDropoutMode enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
