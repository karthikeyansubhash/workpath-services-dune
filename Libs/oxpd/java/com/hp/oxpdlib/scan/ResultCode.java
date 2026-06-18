// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.text.TextUtils;

/** Enumeration of scan result codes. */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum ResultCode {
    /** Job is still active (not yet complete). */
    Pending("Pending"),
    /** Job was scanned and delivered successfully. */
    Succeeded("Succeeded"),
    /** Job was canceled by the user or the CancelJob method. */
    Canceled("Canceled"),
    /** Job failed prior to completion (includes jobs specifying a JobAssemblyMode of On, and whose UIContext is programatically released during the scan phase). */
    Failed("Failed");

    /**
     * SOAP value associated with enum
     */
    final String mValue;

    /**
     * ResultCode constructor
     * @param value
     *              SOAP value associated with enum
     */
    ResultCode(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to ResultCode
     * @param value
     *              SOAP value string
     * @return
     *              Matching ResultCode enum or null if no match is found
     */
    static ResultCode fromAttributeValue(String value) {
        for(ResultCode enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
