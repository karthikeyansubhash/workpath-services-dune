// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.text.TextUtils;

/** Enumeration of XPS compression modes */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum XpsCompressionMode {
    /** Not applicable. */
    NotApplicable("NotApplicable"),
    /** Normal compression. */
    Normal("Normal"),
    /** The device will attempt to use adaptive/selective methods to produce the smallest possible output. The output file will be a composite of multiple image layers at various resolutions (a supported resolution specified on the scan ticket will be ignored). Note that the result may not always be smaller than with Normal compression. */
    High("High");
    
    /**
     * SOAP value associated with enum
     */
    final String mValue;

    /**
     * XpsCompressionMode constructor
     * @param value
     *              SOAP value associated with enum
     */
    XpsCompressionMode(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to XpsCompressionMode
     * @param value
     *              SOAP value string
     * @return
     *              Matching XpsCompressionMode enum or null if no match is found
     */
    static XpsCompressionMode fromAttributeValue(String value) {
        for(XpsCompressionMode enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
