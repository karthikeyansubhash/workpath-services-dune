// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.options;

public enum TextGraphicsOptimization {
    Text("Text"),
    Mixed("Mixed"),
    Graphic("Graphic"),
    Photograph("Photograph"),
    AutoDetect("AutoDetect"),
    Other("Other");

    public final String mValue;

    TextGraphicsOptimization(String v) {
        mValue = v;
    }

    public static TextGraphicsOptimization fromAttributeValue(String v) {
        for (TextGraphicsOptimization c: TextGraphicsOptimization.values()) {
            if (c.mValue.equals(v)) {
                return c;
            }
        }
        return null;
    }

    /**
     * String representation of TextGraphicsOptimization
     * 
     */
    @Override
    public String toString() {
        return mValue;
    }

}
