// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.model;

import android.text.TextUtils;

/**
 * Enumeration of scaling modes.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum ScalingMode {

    /** Automatic */
    Auto("auto"),
    /** Automatic based on document content. */
    AutoFit("auto-fit"),
    /** Fill */
    Fill("fill"),
    /** Fit */
    Fit("fit"),
    /** No scaling */
    None("none");

    /**
     * IPP value associated with enum
     */
    public final String mValue;

    /**
     * ScalingMode constructor
     * @param value
     *              IPP value associated with enum
     */
    ScalingMode(String value) {
        mValue = value;
    }

    /**
     * Convert ipp value string to ScalingMode
     * @param value
     *              ipp value string
     * @return
     *              Matching ScalingMode enum or null if no match is found
     */
    static ScalingMode fromAttributeValue(String value) {
        for(ScalingMode enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
