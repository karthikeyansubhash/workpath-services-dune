package com.hp.oxpdlib.options;

import android.text.TextUtils;

/** Enumeration of BookletBordersEachPage. */
public enum BookletBordersEachPage {

    /** true */
    True("true"),
    /** false */
    False("false");

    /**
     * SOAP value associated with enum
     */
    public final String mValue;

    /**
     * BookletBordersEachPage constructor
     * @param value
     *              SOAP value associated with enum
     */
    BookletBordersEachPage(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to BookletBordersEachPage
     * @param value
     *              SOAP value string
     * @return
     *              Matching BookletBordersEachPage enum or null if no match is found
     */
    public static BookletBordersEachPage fromAttributeValue(String value) {
        for(BookletBordersEachPage enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}