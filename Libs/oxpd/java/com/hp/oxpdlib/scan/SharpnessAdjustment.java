// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.text.TextUtils;

/** Enumeration of sharpness adjustment values. */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum SharpnessAdjustment {
    /** Not applicable. */
    NotApplicable("NotApplicable"),
    /** Least sharp. Sharpness level 0 of 4. */
    Value0("Value0"),
    /** Sharpness level 1 of 4. */
    Value1("Value1"),
    /** Sharpness level 2 of 4. */
    Value2("Value2"),
    /** Sharpness level 3 of 4. */
    Value3("Value3"),
    /** Sharpest. Sharpness level 4 of 4. */
    Value4("Value4");

    /**
     * SOAP value associated with enum
     */
    final String mValue;

    /**
     * SharpnessAdjustment constructor
     * @param value
     *              SOAP value associated with enum
     */
    SharpnessAdjustment(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to SharpnessAdjustment
     * @param value
     *              SOAP value string
     * @return
     *              Matching SharpnessAdjustment enum or null if no match is found
     */
    static SharpnessAdjustment fromAttributeValue(String value) {
        for(SharpnessAdjustment enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
