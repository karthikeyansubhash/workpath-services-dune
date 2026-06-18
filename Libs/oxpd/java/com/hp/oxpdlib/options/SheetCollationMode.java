// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.options;

public enum SheetCollationMode {
    Collated("Collated"),
    Uncollated("Uncollated"),
    Other("Other");

    public final String mValue;

    SheetCollationMode(String v) {
        mValue = v;
    }

    public static SheetCollationMode fromAttributeValue(String v) {
        for (SheetCollationMode c: SheetCollationMode.values()) {
            if (c.mValue.equals(v)) {
                return c;
            }
        }
        return null;
    }

    /**
     * String representation of SheetCollationMode
     * 
     */
    @Override
    public String toString() {
        return mValue;
    }

}
