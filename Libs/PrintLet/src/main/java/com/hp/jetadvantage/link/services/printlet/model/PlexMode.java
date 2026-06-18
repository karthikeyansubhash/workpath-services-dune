// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.model;

import android.text.TextUtils;

/**
 * Enumeration of duplex modes.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum PlexMode {
    /** Print on one side. */
    Simplex("one-sided"),
    /** Print on both sides, with binding along the long edge (Book). */
    DuplexLongEdge("two-sided-long-edge"),
    /** Print on both sides, with binding along the short edge (Flip). */
    DuplexShortEdge("two-sided-short-edge");

    /**
     * IPP value associated with enum
     */
    public final String mValue;

    /**
     * PlexMode constructor
     * @param value
     *          IPP value associated with enum
     */
    PlexMode(String value) {
        mValue = value;
    }

    /**
     * Convert ipp value to PlexMode
     * @param value
     *              IPP value
     * @return
     *              Matching PlexMode enum or null if no match found
     */
    static PlexMode fromAttributeValue(String value) {
        for(PlexMode enumValue : values()) {
            if (TextUtils.equals(value, enumValue.mValue)) {
                return enumValue;
            }
        }
        return null;
    }
}
