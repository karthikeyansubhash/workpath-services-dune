// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.text.TextUtils;

/** Enumeration of TIFF compression modes. */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum TiffCompressionMode {
    /** Not applicable. */
    NotApplicable("NotApplicable"),
    /** CCITT Group 3 (can only be used with Black color mode). */
    G3("G3"),
    /** CCITT Group 4 (can only be used with Black color mode). */
    G4("G4"),
    /** JPEG TIFF Version 6.0 (cannot be used with Black color mode). */
    JpegTiff6("JpegTiff6"),
    /** JPEG TIFF Technical Note 2 (cannot be used with Black color mode). */
    JpegTTN2("JpegTTN2"),
    /** Lempel Ziv Welch. */
    LZW("LZW"),
    /** The device will use adaptive methods to produce the smallest possible output (may only be used with Black color mode). */
    High("High");

    /**
     * SOAP value associated with enum
     */
    final String mValue;

    /**
     * TiffCompressionMode constructor
     * @param value
     *              SOAP value associated with enum
     */
    TiffCompressionMode(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to TiffCompressionMode
     * @param value
     *              SOAP value string
     * @return
     *              Matching TiffCompressionMode enum or null if no match is found
     */
    static TiffCompressionMode fromAttributeValue(String value) {
        for(TiffCompressionMode enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
