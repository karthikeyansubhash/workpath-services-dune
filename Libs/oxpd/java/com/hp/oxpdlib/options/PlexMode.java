// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.options;

import android.text.TextUtils;

/** Enumeration of plex modes. */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum PlexMode {
    /** Scan a single (front) side of the original. */
    Simplex("Simplex"),
    /** Scan both sides of the original. */
    Duplex("Duplex");

    /**
     * SOAP value associated with enum
     */
    public final String mValue;

    /**
     * PlexMode constructor
     * @param value
     *              SOAP value associated with enum
     */
    PlexMode(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to PlexMode
     * @param value
     *              SOAP value string
     * @return Matching PlexMode enum or null if no match is found
     */
    public static PlexMode fromAttributeValue(String value) {
        for(PlexMode enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return mValue;
    }
}
