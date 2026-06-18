// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.text.TextUtils;

/** Enumeration of text versus photo optimization settings. */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum EraseMarginUnit {
    /** Inches */
    Inches("Inches"),
    /** Millimeters */
    Millimeters("Millimeters");

    /**
     * SOAP value associated with enum
     */
    public final String mValue;

    /**
     * EraseMarginUnits constructor
     * @param value
     *              SOAP value associated with enum
     */
    EraseMarginUnit(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to EraseMarginUnits
     * @param value
     *              SOAP value string
     * @return
     *              Matching EraseMarginUnits enum or null if no match is found
     */
    public static EraseMarginUnit fromAttributeValue(String value) {
        for(EraseMarginUnit enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
