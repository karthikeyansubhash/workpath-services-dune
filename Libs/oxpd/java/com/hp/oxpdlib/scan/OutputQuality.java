// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.text.TextUtils;

/** Enumeration of output quality values. */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum OutputQuality {
    /** Not applicable. */
    NotApplicable("NotApplicable"),
    /** Highest quality (least loss due to file compression, largest file size). */
    High("High"),
    /** Medium quality (medium loss due to file compression, medium file size). */
    Medium("Medium"),
    /** Lowest quality (most loss due to file compression, smallest file size). */
    Low("Low");

    /**
     * SOAP value associated with enum
     */
    final String mValue;

    /**
     * OutputQuality constructor
     * @param value
     *              SOAP value associated with enum
     */
    OutputQuality(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to OutputQuality
     * @param value
     *              SOAP value string
     * @return
     *              Matching OutputQuality enum or null if no match is found
     */
    static OutputQuality fromAttributeValue(String value) {
        for(OutputQuality enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }    
}
