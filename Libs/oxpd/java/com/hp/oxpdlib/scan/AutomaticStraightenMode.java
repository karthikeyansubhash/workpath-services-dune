// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.text.TextUtils;

/** AutomaticStraightenMode. */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum AutomaticStraightenMode {
    /** Disabled AutomaticStraightenMode. */
    Disabled("Disabled"),
    /** Enabled AutomaticStraightenMode. */
    Enabled("Enabled");

    /**
     * SOAP value associated with enum
     */
    final String mValue;

    /**
     * AutomaticStraightenMode constructor
     * @param value
     *              SOAP value associated with enum
     */
    AutomaticStraightenMode(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to AutomaticStraightenMode
     * @param value
     *              SOAP value string
     * @return
     *              Matching AutomaticStraightenMode enum or null if no match is found
     */
    static AutomaticStraightenMode fromAttributeValue(String value) {
        for(AutomaticStraightenMode enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
