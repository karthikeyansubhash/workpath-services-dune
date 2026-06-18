// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.text.TextUtils;

/** Enumeration of duplex format values. */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum DuplexFormat {
    /** Not applicable. */
    NotApplicable("NotApplicable"),
    /** Images are oriented as in a book (binding on the left). */
    Book("Book"),
    /** Images are oriented in a 'flip up' style (binding on the top). */
    Flip("Flip");

    /**
     * SOAP value associated with enum
     */
    final String mValue;

    /**
     * ActivityState constructor
     * @param value
     *              SOAP value associated with enum
     */
    DuplexFormat(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to DuplexFormat
     * @param value
     *              SOAP value string
     * @return
     *              Matching DuplexFormat enum or null if no match is found
     */
    static DuplexFormat fromAttributeValue(String value) {
        for(DuplexFormat enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
