// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.text.TextUtils;

/** AutomaticToneMode */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum AutomaticToneMode {
    /** Disabled AutomaticToneMode. */
    Disabled("Disabled"),
    /** Enabled AutomaticToneMode. */
    Enabled("Enabled");

    /**
     * SOAP value associated with enum
     */
    final String mValue;

    /**
     * AutomaticToneMode constructor
     * @param value
     *              SOAP value associated with enum
     */
    AutomaticToneMode(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to AutomaticToneMode
     * @param value
     *              SOAP value string
     * @return
     *              Matching AutomaticToneMode enum or null if no match is found
     */
    static AutomaticToneMode fromAttributeValue(String value) {
        for(AutomaticToneMode enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
