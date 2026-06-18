// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.options;

public enum ScanSourceId {
    Adf("Adf"),
    Flatbed("Flatbed"),
    Auto("Auto"),
    Other("Other");

    public final String mValue;

    ScanSourceId(String v) {
        mValue = v;
    }

    public static ScanSourceId fromAttributeValue(String v) {
        for (ScanSourceId c: ScanSourceId.values()) {
            if (c.mValue.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

    /**
     * String representation of ScanSourceId
     * 
     */
    @Override
    public String toString() {
        return mValue;
    }

}
