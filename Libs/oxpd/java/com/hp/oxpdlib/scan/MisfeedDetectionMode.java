// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.text.TextUtils;

/** Enumeration of misfeed detection modes. */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum MisfeedDetectionMode {
    /** No misfeed detection. */
    Off("Off"),
    /** Detect misfeeds. */
    On("On");

    /**
     * SOAP value associated with enum
     */
    final String mValue;

    /**
     * MisfeedDetectionMode constructor
     * @param value
     *              SOAP value associated with enum
     */
    MisfeedDetectionMode(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to MisfeedDetectionMode
     * @param value
     *              SOAP value string
     * @return
     *              Matching MisfeedDetectionMode enum or null if no match is found
     */
    static MisfeedDetectionMode fromAttributeValue(String value) {
        for(MisfeedDetectionMode enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
