// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.text.TextUtils;

/** Enumeration of activity states for processing, scanning, transmitting, etc. */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum ActivityState {
    /** The activity has not started. */
    NotStarted("NotStarted"),
    /** The activity has started. */
    Started("Started"),
    /** The activity has completed. */
    Completed("Completed");

    /**
     * SOAP value associated with enum
     */
    final String mValue;

    /**
     * ActivityState constructor
     * @param value
     *              SOAP value associated with enum
     */
    ActivityState(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to ActivityState
     * @param value
     *              SOAP value string
     * @return
     *              Matching ActivityState enum or null if no match is found
     */
    static ActivityState fromAttributeValue(String value) {
        for(ActivityState enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
