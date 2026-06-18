// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.text.TextUtils;

/** Enumeration of darkness adjustment values (note that 0 is darkest and 8 is lightest). */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum DarknessAdjustment {
    /** Not applicable. */
    NotApplicable("NotApplicable"),
    /** Darkest. Darkness level 0 of 8. */
    Value0("Value0"),
    /** Darkness level 1 of 8. */
    Value1("Value1"),
    /** Darkness level 2 of 8. */
    Value2("Value2"),
    /** Darkness level 3 of 8. */
    Value3("Value3"),
    /** Darkness level 4 of 8. */
    Value4("Value4"),
    /** Darkness level 5 of 8. */
    Value5("Value5"),
    /** Darkness level 6 of 8. */
    Value6("Value6"),
    /** Darkness level 7 of 8. */
    Value7("Value7"),
    /** Lightest. Contrast level 8 of 8. */
    Value8("Value8");

    /**
     * SOAP value associated with enum
     */
    final String mValue;

    /**
     * DarknessAdjustment constructor
     * @param value
     *              SOAP value associated with enum
     */
    DarknessAdjustment(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to DarknessAdjustment
     * @param value
     *              SOAP value string
     * @return
     *              Matching DarknessAdjustment enum or null if no match is found
     */
    static DarknessAdjustment fromAttributeValue(String value) {
        for(DarknessAdjustment enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
