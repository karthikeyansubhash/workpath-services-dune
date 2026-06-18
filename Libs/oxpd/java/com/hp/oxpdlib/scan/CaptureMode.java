// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.text.TextUtils;

/** Enumeration of text versus photo optimization settings. */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum CaptureMode {
    /** Standard */
    Standard("Standard"),
    /** StandardAddPages */
    StandardAddPages("StandardAddPages"),
    /** BookCapture */
    BookCapture("BookCapture"),
    /** IDCapturePromptBothSides */
    IDCapturePromptBothSides("IDCapturePromptBothSides"),
    /** IDCapturePromptBackSideOnly */
    IDCapturePromptBackSideOnly("IDCapturePromptBackSideOnly");

    /**
     * SOAP value associated with enum
     */
    public final String mValue;

    /**
     * CaptureMode constructor
     * @param value
     *              SOAP value associated with enum
     */
    CaptureMode(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to CaptureMode
     * @param value
     *              SOAP value string
     * @return
     *              Matching CaptureMode enum or null if no match is found
     */
    public static CaptureMode fromAttributeValue(String value) {
        for(CaptureMode enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
