package com.hp.oxpdlib.options;

public enum StampPosition {

    TOP_LEFT("stampTopLeft"),
    TOP_CENTER("stampTopCenter"),
    TOP_RIGHT("stampTopRight"),
    BOTTOM_LEFT("stampBottomLeft"),
    BOTTOM_CENTER("stampBottomCenter"),
    BOTTOM_RIGHT("stampBottomRight");

    public final String mValue;

    StampPosition(String v) {
        mValue = v;
    }

    public static StampPosition fromAttributeValue(String v) {
        for (StampPosition c: StampPosition.values()) {
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
