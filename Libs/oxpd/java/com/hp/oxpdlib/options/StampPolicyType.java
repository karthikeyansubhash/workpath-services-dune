package com.hp.oxpdlib.options;

public enum StampPolicyType {
    NONE("None"),
    ENFORCED("Enforced"),
    GUIDED("Guided"),
    OTHER("Other");

    public final String mValue;

    StampPolicyType(String v) {
        mValue = v;
    }

    public static StampPolicyType fromAttributeValue(String v) {
        for (StampPolicyType c: StampPolicyType.values()) {
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
