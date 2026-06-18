// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.copy;

public enum NumberUpDirection {
    ToBottomToLeft("ToBottomToLeft"),
    ToBottomToRight("ToBottomToRight"),
    ToLeftToBottom("ToLeftToBottom"),
    ToLeftToTop("ToLeftToTop"),
    ToRightToBottom("ToRightToBottom"),
    ToRightToTop("ToRightToTop"),
    ToTopToLeft("ToTopToLeft"),
    ToTopToRight("ToTopToRight");

    public final String mValue;

    NumberUpDirection(String v) {
        mValue = v;
    }

    public String value() {
        return mValue;
    }

    public static NumberUpDirection fromAttributeValue(String v) {
        for (NumberUpDirection c: NumberUpDirection.values()) {
            if (c.mValue.equals(v)) {
                return c;
            }
        }
        return null;
    }

    /**
     * String representation of NumberUpDirection
     * 
     */
    @Override
    public String toString() {
        return mValue;
    }

}
