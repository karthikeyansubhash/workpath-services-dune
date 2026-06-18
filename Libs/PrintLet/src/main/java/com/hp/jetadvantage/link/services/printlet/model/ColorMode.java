// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.model;

import android.text.TextUtils;

/**
 * Enumeration of color modes.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum ColorMode {

    /** Automatic based on document content. */
    Auto("auto"),
    /** Full-color output. */
    Color("color"),
    /** One colorant (typically black) shaded/grayscale output. */
    Monochrome("monochrome");

    /**
     * IPP value associated with enum
     */
    public final String mValue;

    /**
     * ColorMode constructor
     * @param value
     *              IPP value associated with enum
     */
    ColorMode(String value) {
        mValue = value;
    }

    /**
     * Convert ipp value string to ColorMode
     * @param value
     *              ipp value string
     * @return
     *              Matching ColorMde enum or Auto if no match is found
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
