// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.model;

import android.text.TextUtils;

/**
 * Enumeration of file types.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum FileType {
    /** Jpeg files (typically *.jpg). */
    Jpeg("image/jpeg"),
    /** PCL5 files (typically *.prn). */
    PCL5("application/vnd.hp-PCL"),
    /** PCL6 files (a.k.a. PCL XL, typically *.prn). */
    PCL6("application/vnd.hp-PCLXL"),
    /** PostScript files (typically *.ps). */
    PS("application/postscript"),
    /** PDF files (typically *.pdf). */
    PDF("application/pdf"),
    /** Plain text files (typically .txt). */
    Text("text/plain"),
    /** Tiff files (typically *.tif). */
    Tiff("image/tiff");
    // Purposefully omitting the other documentFormats defined by IPP

    /**
     * IPP value associated with enum
     */
    public final String mValue;

    /**
     * FileType constructor
     * @param value
     *              IPP value associated with enum
     */
    FileType(String value) {
        mValue = value;
    }

    /**
     * Convert IPP value to FileType
     * @param value
     *              IPP value string
     * @return
     *              Matching FileType enum or null if no match is found
     */
    static FileType fromAttributeValue(String value) {
        for(FileType type : values()) {
            if (TextUtils.equals(value, type.mValue))
                return type;
        }
        return null;
    }
}
