// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.text.TextUtils;

/** Enumeration of PDF compression modes. */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum PdfCompressionMode {
    /** Not applicable. */
    NotApplicable("NotApplicable"),
    /** Normal compression. */
    Normal("Normal"),
    /** The device will attempt to use adaptive/selective methods to produce the smallest possible output. The output file will be a composite of multiple image layers at various resolutions (i.e. a supported resolution specified on the scan ticket will be ignored). Note that the result may not always be smaller than with Normal compression. */
    High("High");

    /**
     * SOAP value associated with enum
     */
    final String mValue;

    /**
     * PdfCompressionMode constructor
     * @param value
     *              SOAP value associated with enum
     */
    PdfCompressionMode(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to PdfCompressionMode
     * @param value
     *              SOAP value string
     * @return
     *              Matching PdfCompressionMode enum or null if no match is found
     */
    static PdfCompressionMode fromAttributeValue(String value) {
        for(PdfCompressionMode enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
