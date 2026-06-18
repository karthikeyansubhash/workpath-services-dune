// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.text.TextUtils;

/** Enumeration of media orientations (content orientations), which determine the orientation of the digital output. */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum MediaOrientation {
    /** The image received at the destination will be in a portrait orientation -- taller than wide. */
    Portrait("Portrait"),
    /** The image received at the destination will be in a landscape orientation -- wider than tall. */
    Landscape("Landscape"),
    /** AutoDetect */
    AutoDetect("AutoDetect");

    /**
     * SOAP value associated with enum
     */
    final String mValue;

    /**
     * MediaOrientation constructor
     * @param value
     *              SOAP value associated with enum
     */
    MediaOrientation(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to MediaOrientation
     * @param value
     *              SOAP value string
     * @return
     *              Matching MediaOrientation enum or null if no match is found
     */
    static MediaOrientation fromAttributeValue(String value) {
        for(MediaOrientation enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
