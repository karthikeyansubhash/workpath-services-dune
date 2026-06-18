// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.accessories;

public enum HidReportType {
    Input("Input"),
    Output("Output"),
    Feature("Feature");

    public final String value;

    HidReportType(String v) {
        value = v;
    }

    public static HidReportType fromAttributeValue(String v) {
        for (HidReportType c: HidReportType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        return null;
    }

    /**
     * String representation of HidReportType
     * 
     */
    @Override
    public String toString() {
        return value;
    }

}
