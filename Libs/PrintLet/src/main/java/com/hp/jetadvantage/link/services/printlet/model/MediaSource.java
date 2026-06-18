// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.model;

import android.text.TextUtils;

/**
 * Enumeration of media sources.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum MediaSource {

    /** Automatic */
    Auto("auto"),
    /** Tray 1 */
    Tray1("tray-1"),
    /** Tray 2 */
    Tray2("tray-2"),
    /** Tray 3 */
    Tray3("tray-3"),
    /** Tray 4 */
    Tray4("tray-4"),
    /** Tray 5 */
    Tray5("tray-5"),
    /** Tray 6 */
    Tray6("tray-6"),
    /** Tray 7 */
    Tray7("tray-7"),
    /** Tray 8 */
    Tray8("tray-8"),
    /** Tray 9 */
    Tray9("tray-9"),
    /** Tray 10 */
    Tray10("tray-10"),
    /** Manual feed */
    Manual("manual");

    /**
     * IPP value associated with enum
     */
    public final String mValue;

    /**
     * MediaSource constructor
     * @param value
     *              IPP value associated with enum
     */
    MediaSource(String value) {
        mValue = value;
    }

    /**
     * Convert ipp value string to MediaSource
     * @param value
     *              ipp value string
     * @return
     *              Matching MediaSource enum or null if no match is found
     */
    public static MediaSource fromAttributeValue(String value) {
        for(MediaSource enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
