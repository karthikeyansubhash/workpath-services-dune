package com.hp.oxpdlib.options;

import android.text.TextUtils;

/** Enumeration of ImageShiftReduceToFit. */

@SuppressWarnings({"unused", "WeakerAccess"})
public enum ImageShiftReduceToFit {

    /** true */
    True("true"),

    /** false */
    False("false");

    /**
     * SOAP value associated with enum
     */
    public final String mValue;

    /**
     * ImageShiftReduceToFit constructor
     * @param value
     *              SOAP value associated with enum
     */
    ImageShiftReduceToFit(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to ImageShiftReduceToFit
     * @param value
     *              SOAP value string
     * @return
     *              Matching ImageShiftReduceToFit enum or null if no match is found
     */
    public static ImageShiftReduceToFit fromAttributeValue(String value) {
        for(ImageShiftReduceToFit enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}