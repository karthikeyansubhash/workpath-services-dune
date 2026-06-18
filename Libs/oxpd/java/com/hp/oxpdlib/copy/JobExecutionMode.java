// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.copy;

public enum JobExecutionMode {
    Normal("Normal"),
    Store("Store");

    public final String mValue;

    JobExecutionMode(String v) {
        mValue = v;
    }

    public static JobExecutionMode fromAttributeValue(String v) {
        for (JobExecutionMode c: JobExecutionMode.values()) {
            if (c.mValue.equals(v)) {
                return c;
            }
        }
        return null;
    }

    /**
     * String representation of JobExecutionMode
     * 
     */
    @Override
    public String toString() {
        return mValue;
    }

}
