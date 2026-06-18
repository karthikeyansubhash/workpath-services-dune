// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.text.TextUtils;

/** Enumeration of scan resolution values (width x height) in pixels per inch. */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum Resolution {
    /** 75x75 pixels. */
    Res75x75("Res75x75"),
    /** 100x100 pixels. */
    Res100x100("Res100x100"),
    /** 150x150 pixels. */
    Res150x150("Res150x150"),
    /** 200x200 pixels. */
    Res200x200("Res200x200"),
    /** 240x240 pixels. */
    Res240x240("Res240x240"),
    /** 300x300 pixels. */
    Res300x300("Res300x300"),
    /** 400x400 pixels. */
    Res400x400("Res400x400"),
    /** 500x500 pixels. */
    Res500x500("Res500x500"),
    /** 600x600 pixels. */
    Res600x600("Res600x600");

    /**
     * SOAP value associated with enum
     */
    final String mValue;

    /**
     * Resolution constructor
     * @param value
     *              SOAP value associated with enum
     */
    Resolution(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to Resolution
     * @param value
     *              SOAP value string
     * @return
     *              Matching Resolution enum or null if no match is found
     */
    static Resolution fromAttributeValue(String value) {
        for(Resolution enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }    
}
