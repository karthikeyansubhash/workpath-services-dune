// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.text.TextUtils;

/** Enumeration of media sources. */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum MediaSource {
    /** Directs the device to scan from the automatic document feeder. */
    Adf("Adf"),
    /** Directs the device to scan from the flatbed (glass). */
    Flatbed("Flatbed"),
    /** The device will automatically select the media source. */
    Auto("Auto");

    /**
     * SOAP value associated with enum
     */
    final String mValue;

    /**
     * MediaSource constructor
     * @param value
     *              SOAP value associated with enum
     */
    MediaSource(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to MediaSource
     * @param value
     *              SOAP value string
     * @return
     *              Matching MediaSource enum or null if no match is found
     */
    static MediaSource fromAttributeValue(String value) {
        for(MediaSource enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
