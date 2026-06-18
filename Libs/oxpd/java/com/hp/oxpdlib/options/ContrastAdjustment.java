// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.options;

import android.text.TextUtils;

/** Enumeration of contrast adjustment values. */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum ContrastAdjustment {
    /** Not applicable. */
    NotApplicable("NotApplicable"),
    /** Least contrast. Contrast level 0 of 8. */
    Value0("Value0"),
    /** Contrast level 1 of 8. */
    Value1("Value1"),
    /** Contrast level 2 of 8. */
    Value2("Value2"),
    /** Contrast level 3 of 8. */
    Value3("Value3"),
    /** Contrast level 4 of 8. */
    Value4("Value4"),
    /** Contrast level 5 of 8. */
    Value5("Value5"),
    /** Contrast level 6 of 8. */
    Value6("Value6"),
    /** Contrast level 7 of 8. */
    Value7("Value7"),
    /** Most contrast. Contrast level 8 of 8. */
    Value8("Value8");

    /**
     * SOAP value associated with enum
     */
    public final String mValue;

    /**
     * ContrastAdjustment constructor
     * @param value
     *              SOAP value associated with enum
     */
    ContrastAdjustment(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to ColorMode
     * @param value
     *              SOAP value string
     * @return
     *              Matching ColorMode enum or null if no match is found
     */
    public static ContrastAdjustment fromAttributeValue(String value) {
        for(ContrastAdjustment enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
