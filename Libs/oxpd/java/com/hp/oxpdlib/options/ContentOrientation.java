// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.options;

public enum ContentOrientation {
    Portrait("Portrait"),
    Landscape("Landscape"),
    Other("Other");

    public final String mValue;

    ContentOrientation(String v) {
        mValue = v;
    }

    public static ContentOrientation fromAttributeValue(String v) {
        for (ContentOrientation c: ContentOrientation.values()) {
            if (c.mValue.equals(v)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return mValue;
    }

}
