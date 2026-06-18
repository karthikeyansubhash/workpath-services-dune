package com.hp.oxpdlib.options;

import android.text.TextUtils;

/** Enumeration of BookletFinishingOption. */
public enum BookletFinishingOption {

    /** Bale */
    Bale("Bale"),
    /** Bind */
    Bind("Bind"),
    /** BindBottom */
    BindBottom("BindBottom"),
    /** BindLeft */
    BindLeft("BindLeft"),
    /** BindRight */
    BindRight("BindRight"),
    /** BindTop */
    BindTop("BindTop"),
    /** BookletMaker */
    BookletMaker("BookletMaker"),
    /** Cover */
    Cover("Cover"),
    /** EdgeStitch */
    EdgeStitch("EdgeStitch"),
    /** EdgeStitchBottom */
    EdgeStitchBottom("EdgeStitchBottom"),
    /** EdgeStitchLeft */
    EdgeStitchLeft("EdgeStitchLeft"),
    /** EdgeStichRight */
    EdgeStichRight("EdgeStichRight"),
    /** EdgeStitchTop */
    EdgeStitchTop("EdgeStitchTop"),
    /** Fold */
    Fold("Fold"),
    /** JogOffset */
    JogOffset("JogOffset"),
    /** None */
    None("None"),
    /** SaddleStitch */
    SaddleStitch("SaddleStitch"),
    /** Trim */
    Trim("Trim"),
    /** Other */
    Other("Other");

    /**
     * SOAP value associated with enum
     */
    public final String mValue;

    /**
     * BookletFinishingOption constructor
     * @param value
     *              SOAP value associated with enum
     */
    BookletFinishingOption(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to BookletFinishingOption
     * @param value
     *              SOAP value string
     * @return
     *              Matching BookletFinishingOption enum or null if no match is found
     */
    public static BookletFinishingOption fromAttributeValue(String value) {
        for(BookletFinishingOption enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }

}