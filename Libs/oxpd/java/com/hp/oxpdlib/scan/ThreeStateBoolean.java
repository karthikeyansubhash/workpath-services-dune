// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.text.TextUtils;

/** Used in cases where a Boolean value would be expected but where the value may be indeterminable (for example, a sensor is not available or not responding). */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum ThreeStateBoolean {
    /** True. */
    True("True"),
    /** False. */
    False("False"),
    /** The value is indeterminable. */
    Unknown("Unknown");

    /**
     * SOAP value associated with enum
     */
    final String mValue;

    /**
     * ThreeStateBoolean constructor
     * @param value
     *              SOAP value associated with enum
     */
    ThreeStateBoolean(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to ThreeStateBoolean
     * @param value
     *              SOAP value string
     * @return
     *              Matching ThreeStateBoolean enum or Unknown if no match is found
     */
    static ThreeStateBoolean fromAttributeValue(String value) {
        for(ThreeStateBoolean enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return Unknown;
    }
}
