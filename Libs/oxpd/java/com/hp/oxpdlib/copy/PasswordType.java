// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.copy;

public enum PasswordType {
    None("None"),
    Numeric("Numeric"),
    Alphanumeric("Alphanumeric");

    public final String mValue;

    PasswordType(String v) {
        mValue = v;
    }

    public String value() {
        return mValue;
    }

    public static PasswordType fromAttributeValue(String v) {
        for (PasswordType c: PasswordType.values()) {
            if (c.mValue.equals(v)) {
                return c;
            }
        }
        return null;
    }

    /**
     * String representation of PasswordType
     * 
     */
    @Override
    public String toString() {
        return mValue;
    }

}
