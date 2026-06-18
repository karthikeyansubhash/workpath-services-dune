// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.model;

/**
 * Enumeration of print job states.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum PrinterState {

    /** Indicates that new jobs can start processing without waiting. */
    Idle(3),
    /** Indicates that jobs are processing and new jobs will wait before processing. */
    Processing(4),
    /** Indicates that no jobs can be processed and intervention is required. */
    Stopped(5),
    /** The printer state is unknown. */
    Unknown(0);
    // Purposefully omitting the other job states defined by IPP

    /**
     * IPP value associated with enum
     */
    final int mValue;

    /**
     * PrinterState constructor
     * @param value
     *              IPP value associated with enum
     */
    PrinterState(int value) {
        mValue = value;
    }

    /**
     * Convert IPP value to PrinterState enum
     * @param attrValue
     *              IPP value
     * @return
     *              Matching PrinterState enum or Unknown if no match is found
     */
    static PrinterState fromAttributeValue(int attrValue) {
        for(PrinterState enumValue : values()) {
            if (enumValue.mValue == attrValue) {
                return enumValue;
            }
        }
        return Unknown;
    }
}
