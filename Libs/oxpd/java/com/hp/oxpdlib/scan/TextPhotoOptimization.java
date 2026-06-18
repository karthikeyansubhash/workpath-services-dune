// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.text.TextUtils;

/** Enumeration of text versus photo optimization settings. */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum TextPhotoOptimization {
    /** Not applicable. */
    NotApplicable("NotApplicable"),
    /** Optimize for all text. */
    Text("Text"),
    /** Optimize for mostly text with less graphics. */
    Mixed0("Mixed0"),
    /** Optimize for mostly text with more graphics. */
    Mixed1("Mixed1"),
    /** Optimize for an equal mix of text and graphics. */
    Mixed2("Mixed2"),
    /** Optimize for mostly graphics with more text. */
    Mixed3("Mixed3"),
    /** Optimize for mostly graphics with some text. */
    Mixed4("Mixed4"),
    /** Optimize for all graphics/pictures. */
    Graphic("Graphic");

    /**
     * SOAP value associated with enum
     */
    final String mValue;

    /**
     * TextPhotoOptimization constructor
     * @param value
     *              SOAP value associated with enum
     */
    TextPhotoOptimization(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to TextPhotoOptimization
     * @param value
     *              SOAP value string
     * @return
     *              Matching TextPhotoOptimization enum or null if no match is found
     */
    static TextPhotoOptimization fromAttributeValue(String value) {
        for(TextPhotoOptimization enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
