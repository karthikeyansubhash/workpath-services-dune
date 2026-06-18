// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.model;

/**
 * Enumeration of orientation.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum Orientation {
    /** Portrait orientation */
    Portrait(3),
    /** Landscape orientation */
    Landscape(4),
    /** Reverse portrait orientation */
    ReverseLandscape(5),
    /** Reverse landscape orientation */
    ReversePortrait(6),
    /** None */
    None(7);

    /**
     * IPP value associated with enum
     */
    public final int mValue;

    /**
     * Orientation constructor
     * @param value
     *              IPP value associated with enum
     */
    Orientation(int value) {
        mValue = value;
    }

    /**
     * Convert ipp value string to Orientation
     * @param attrValue
     *              ipp value string
     * @return
     *              Matching Orientation enum or Unknown if no match is found
     */
    static Orientation fromAttributeValue(int attrValue) {
        for(Orientation enumValue : values()) {
            if (enumValue.mValue == attrValue) {
                return enumValue;
            }
        }
        return null;
    }
}
