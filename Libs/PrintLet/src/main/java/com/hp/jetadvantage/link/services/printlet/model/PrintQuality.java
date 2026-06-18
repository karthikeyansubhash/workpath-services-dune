// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.model;

/**
 * An enumeration specifying the desired print quality .
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum PrintQuality {
    /** '3' is draft quality */
    Draft(3),
    /** '4' is Normal quality */
    Normal(4),
    /** '5' is best quality */
    High(5);


    /**
     * IPP value associated with enum
     */
    public final int mValue;

    /**
     * Orientation constructor
     * @param value
     *              IPP value associated with enum
     */
    PrintQuality(int value) {
        mValue = value;
    }

    /**
     * Convert ipp value string to Print Quality
     * @param attrValue
     *              ipp value
     * @return
     *              Matching Print Quality enum or Unknown if no match is found
     */
    static PrintQuality fromAttributeValue(int attrValue) {
        for(PrintQuality enumValue : values()) {
            if (enumValue.mValue == attrValue) {
                return enumValue;
            }
        }
        return Normal;
    }
}
