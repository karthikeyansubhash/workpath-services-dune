// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.text.TextUtils;

/** Enumeration of text versus photo optimization settings. */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum SplitAttachmentByPage {
    /** Disabled */
    Disabled("Disabled"),
    /** Enabled */
    Enabled("Enabled");

    /**
     * SOAP value associated with enum
     */
    final String mValue;

    /**
     * SplitAttachmentByPage constructor
     * @param value
     *              SOAP value associated with enum
     */
    SplitAttachmentByPage(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to SplitAttachmentByPage
     * @param value
     *              SOAP value string
     * @return
     *              Matching SplitAttachmentByPage enum or null if no match is found
     */
    static SplitAttachmentByPage fromAttributeValue(String value) {
        for(SplitAttachmentByPage enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
