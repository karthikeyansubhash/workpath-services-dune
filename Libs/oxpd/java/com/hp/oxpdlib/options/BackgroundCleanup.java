// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.options;

import android.text.TextUtils;

/** Enumeration of background cleanup values. */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum BackgroundCleanup {
    /** Not applicable. */
    NotApplicable("NotApplicable"),
    /** No cleanup. Cleanup level 0 of 8. */
    Value0("Value0"),
    /** Cleanup level 1 of 8. */
    Value1("Value1"),
    /** Cleanup level 2 of 8. */
    Value2("Value2"),
    /** Cleanup level 3 of 8. */
    Value3("Value3"),
    /** Cleanup level 4 of 8. */
    Value4("Value4"),
    /** Cleanup level 5 of 8. */
    Value5("Value5"),
    /** Cleanup level 6 of 8. */
    Value6("Value6"),
    /** Cleanup level 7 of 8. */
    Value7("Value7"),
    /** Most aggressive cleanup. Cleanup level 8 of 8. */
    Value8("Value8");

    /**
     * SOAP value associated with enum
     */
    public final String mValue;

    /**
     * BackgroundCleanup constructor
     * @param value
     *              SOAP value associated with enum
     */
    BackgroundCleanup(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to BackgroundCleanup
     * @param value
     *              SOAP value string
     * @return
     *              Matching BackgroundCleanup enum or null if no match is found
     */
    public static BackgroundCleanup fromAttributeValue(String value) {
        for(BackgroundCleanup enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
