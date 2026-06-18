// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.copy;

public enum CopyJobActivityType {
    SCANNING_STARTED("ScanningStarted"),
    SCANNING_COMPLETED("ScanningCompleted"),
    PROCESSING_STARTED("ProcessingStarted"),
    PROCESSING_COMPLETED("ProcessingCompleted"),
    PRINTING_STARTED("PrintingStarted"),
    PRINTING_COMPLETED("PrintingCompleted"),
    CANCELING_STARTED("CancelingStarted"),
    CANCELING_COMPLETED("CancelingCompleted"),
    JOB_CANCELED("JobCanceled"),
    JOB_FAILED("JobFailed"),
    JOB_SUCCEEDED("JobSucceeded"),
    OTHER("Other");

    private final String value;

    CopyJobActivityType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CopyJobActivityType fromAttributeValue(String v) {
        for (CopyJobActivityType c: CopyJobActivityType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        return null;
    }

    /**
     * String representation of CopyJobActivityType
     *
     */
    @Override
    public String toString() {
        return value;
    }

}
