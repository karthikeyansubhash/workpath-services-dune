// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.common;

import android.text.TextUtils;

@SuppressWarnings({"WeakerAccess", "unused"})
public enum Binding {
    /** No special binding will be used to communicate to this URI. */
    Plain("Plain"),
    /** A SOAP 1.2 binding will be used to communicate to this URI. */
    Soap12("Soap12");

    /**
     * SOAP value associated with enum
     */
    public final String mValue;

    /**
     * ActivityState constructor
     * @param value
     *              SOAP value associated with enum
     */
    Binding(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to Binding
     * @param value
     *              SOAP value string
     * @return
     *              Matching Binding enum or null if no match is found
     */
    public static Binding fromAttributeValue(String value) {
        for(Binding enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
