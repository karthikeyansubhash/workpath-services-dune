// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.accessories;

public enum OwnedAccessoryEventCode {
    ContextCreated("ContextCreated"),
    ContextResent("ContextResent"),
    ContextRevoked("ContextRevoked");

    public final String mValue;

    OwnedAccessoryEventCode(String v) {
        mValue = v;
    }

    public static OwnedAccessoryEventCode fromAttributeValue(String v) {
        for (OwnedAccessoryEventCode c: OwnedAccessoryEventCode.values()) {
            if (c.mValue.equals(v)) {
                return c;
            }
        }
        return null;
    }

    /**
     * String representation of OwnedAccessoryEventCode
     * 
     */
    @Override
    public String toString() {
        return mValue;
    }

}
