// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.copy;

public enum JobResultCode {
    Active("Active"),
    Succeeded("Succeeded"),
    Canceled("Canceled"),
    Failed("Failed"),
    Other("Other");

    public final String value;

    JobResultCode(String v) {
        value = v;
    }

    public static JobResultCode fromAttributeValue(String v) {
        for (JobResultCode c: JobResultCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        return null;
    }

    /**
     * String representation of JobResultCode
     * 
     */
    @Override
    public String toString() {
        return value;
    }

}
