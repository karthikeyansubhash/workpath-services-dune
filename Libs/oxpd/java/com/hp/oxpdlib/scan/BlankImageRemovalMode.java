// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.text.TextUtils;

/** Enumeration of blank image removal modes. */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum BlankImageRemovalMode {
    /** Do not remove blank images. */
    Off("Off"),
    /**
     * Remove blank images.
     * If all images in the job are removed, the job will fail. Some older
     * devices may always include the first image in the job, even if it is blank. */
    On("On");

    /**
     * SOAP value associated with enum
     */
    final String mValue;

    /**
     * BlankImageRemovalMode constructor
     * @param value
     *              SOAP value associated with enum
     */
    BlankImageRemovalMode(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to BlankImageRemovalMode
     * @param value
     *              SOAP value string
     * @return
     *              Matching BlankImageRemovalMode enum or null if no match is found
     */
    static BlankImageRemovalMode fromAttributeValue(String value) {
        for(BlankImageRemovalMode enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
