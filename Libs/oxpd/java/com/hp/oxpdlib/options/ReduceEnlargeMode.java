// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.options;

public enum ReduceEnlargeMode {
    Automatic("Automatic"),
    Manual("Manual"),
    Other("Other");

    public final String mValue;

    ReduceEnlargeMode(String v) {
        mValue = v;
    }

    public static ReduceEnlargeMode fromAttributeValue(String v) {
        for (ReduceEnlargeMode c: ReduceEnlargeMode.values()) {
            if (c.mValue.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

    /**
     * String representation of ReduceEnlargeMode
     * 
     */
    @Override
    public String toString() {
        return mValue;
    }

}
