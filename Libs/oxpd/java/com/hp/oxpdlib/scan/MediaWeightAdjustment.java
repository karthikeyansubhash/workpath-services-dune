// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.text.TextUtils;

/** Enumeration of media weight adjustments. */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum MediaWeightAdjustment {
    /** Not applicable. */
    NotApplicable("NotApplicable"),
    /** Adjust the scanner feed mechanism for typical (normal) paper weights. */
    Normal("Normal"),
    /** Adjust the scanner feed mechanism for paper weighing more than 100 grams per square meter. */
    Heavy("Heavy");

    /**
     * SOAP value associated with enum
     */
    final String mValue;

    /**
     * MediaWeightAdjustment constructor
     * @param value
     *              SOAP value associated with enum
     */
    MediaWeightAdjustment(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to MediaWeightAdjustment
     * @param value
     *              SOAP value string
     * @return
     *              Matching MediaWeightAdjustment enum or null if no match is found
     */
    static MediaWeightAdjustment fromAttributeValue(String value) {
        for(MediaWeightAdjustment enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
