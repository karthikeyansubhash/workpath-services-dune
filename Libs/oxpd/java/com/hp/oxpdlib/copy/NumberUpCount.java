// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.copy;

public enum NumberUpCount {
    OneUp("OneUp"),
    TwoUp("TwoUp"),
    FourUp("FourUp"),
    EightUp("EightUp"),
    SixteenUp("SixteenUp"),
    ThirtyTwoUp("ThirtyTwoUp");

    public final String mValue;

    NumberUpCount(String v) {
        mValue = v;
    }

    public String value() {
        return mValue;
    }

    public static NumberUpCount fromAttributeValue(String v) {
        for (NumberUpCount c: NumberUpCount.values()) {
            if (c.mValue.equals(v)) {
                return c;
            }
        }
        return null;
    }

    /**
     * String representation of NumberUpCount
     * 
     */
    @Override
    public String toString() {
        return mValue;
    }

}
