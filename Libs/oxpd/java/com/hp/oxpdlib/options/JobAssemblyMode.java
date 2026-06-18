// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.options;

import android.text.TextUtils;

/** Enumeration of job assembly modes. */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum JobAssemblyMode {
    /** Scan only one segment per job. */
    Off("Off"),
    /** Scan multiple segments, prompting the walk-up user for another segment at the end of each segment, and assembling all segments into a single job. This is a very simple job assembly mode. Scan settings cannot be adjusted between segments. This mode is intended for jobs where the number of sheets exceeds the capacity of the ADF. For more complex job assembly operations (different scan settings per segment), the web application is expected to combine multiple scan jobs at the device into a single job at the server. */
    On("On");

    /**
     * SOAP value associated with enum
     */
    public final String mValue;

    /**
     * JobAssemblyMode constructor
     * @param value
     *              SOAP value associated with enum
     */
    JobAssemblyMode(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to JobAssemblyMode
     * @param value
     *              SOAP value string
     * @return
     *              Matching JobAssemblyMode enum or null if no match is found
     */
    public static JobAssemblyMode fromAttributeValue(String value) {
        for(JobAssemblyMode enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
