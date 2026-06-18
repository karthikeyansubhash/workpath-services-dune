package com.hp.oxpdlib.options;

import android.text.TextUtils;

/** Enumeration of ImageShiftUnits. */
public enum ImageShiftUnits {
    /** Inches */
    Inches("Inches"),
    /** Millimeters */
    Millimeters("Millimeters");

    /**
     * SOAP value associated with enum
     */
    public final String mValue;

    /**
     * ImageShiftUnits constructor
     * @param value
     *              SOAP value associated with enum
     */
    ImageShiftUnits(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to ImageShiftUnits
     * @param value
     *              SOAP value string
     * @return
     *              Matching ImageShiftUnits enum or null if no match is found
     */
    public static ImageShiftUnits fromAttributeValue(String value) {
        for(ImageShiftUnits enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}