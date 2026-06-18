package com.hp.oxpdlib.options;

import android.text.TextUtils;

/** Enumeration of BookletFormat. */
public enum BookletFormat {

    /** Off */
    Off("Off"),
    /** LeftEdge */
    LeftEdge("LeftEdge"),
    /** RightEdge */
    RightEdge("RightEdge"),
    /** Other */
    Other("Other");

    /**
     * SOAP value associated with enum
     */
    public final String mValue;

    /**
     * BookletFormat constructor
     * @param value
     *              SOAP value associated with enum
     */
    BookletFormat(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to BookletFormat
     * @param value
     *              SOAP value string
     * @return
     *              Matching BookletFormat enum or null if no match is found
     */
    public static BookletFormat fromAttributeValue(String value) {
        for(BookletFormat enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}