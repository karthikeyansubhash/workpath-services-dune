// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.model;

import android.text.TextUtils;

public enum CollateMode {

    Collated("separate-documents-collated-copies"),
    Uncollated("separate-documents-uncollated-copies");

    /**
     * IPP value associated with enum
     */
    public final String mValue;

    /**
     * CollateMode constructor
     * @param value
     *              IPP value associated with enum
     */
    CollateMode(String value) {
        mValue = value;
    }

    /**
     * Convert ipp value string to CollateMode
     * @param value
     *              ipp value string
     * @return
     *              Matching CollateMode enum or Auto if no match is found
     */
    static CollateMode fromAttributeValue(String value) {
        for(CollateMode enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
