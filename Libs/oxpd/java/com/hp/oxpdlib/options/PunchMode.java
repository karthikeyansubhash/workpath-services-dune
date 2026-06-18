package com.hp.oxpdlib.options;
import android.text.TextUtils;

/** Enumeration of copy file types. */
public enum PunchMode {
    /** Punch location None */
    None("None"),
    /** Punch location TwoPointAny */
    TwoPointAny("TwoPointAny"),
    /** Punch location LeftTwoPointDin */
    LeftTwoPointDin("LeftTwoPointDin"),
    /** Punch location RightTwoPointDin */
    RightTwoPointDin("RightTwoPointDin"),
    /** Punch location TopTwoPointDin */
    TopTwoPointDin("TopTwoPointDin"),
    /** Punch location BottomTwoPointDin */
    BottomTwoPointDin("BottomTwoPointDin"),
    /** Punch location TwoPointDin */
    TwoPointDin("TwoPointDin"),
    /** Punch location LeftTwoPointUs */
    LeftTwoPointUs("LeftTwoPointUs"),
    /** Punch location RightTwoPointUs */
    RightTwoPointUs("RightTwoPointUs"),
    /** Punch location TopTwoPointUs */
    TopTwoPointUs("TopTwoPointUs"),
    /** Punch location BottomTwoPointUs */
    BottomTwoPointUs("BottomTwoPointUs"),
    /** Punch location TwoPointUs */
    TwoPointUs("TwoPointUs"),
    /** Punch location LeftThreePointUs */
    LeftThreePointUs("LeftThreePointUs"),
    /** Punch location RightThreePointUs */
    RightThreePointUs("RightThreePointUs"),
    /** Punch location TopThreePointUs */
    TopThreePointUs("TopThreePointUs"),
    /** Punch location BottomThreePointUs */
    BottomThreePointUs("BottomThreePointUs"),
    /** Punch location ThreePointUs */
    ThreePointUs("ThreePointUs"),
    /** Punch location ThreePointAny */
    ThreePointAny("ThreePointAny"),
    /** Punch location LeftFourPointDin */
    LeftFourPointDin("LeftFourPointDin"),
    /** Punch location None */
    RightFourPointDin("RightFourPointDin"),
    /** Punch location RightFourPointDin */
    BottomFourPointDin("BottomFourPointDin"),
    /** Punch location FourPointDin */
    FourPointDin("FourPointDin"),
    /** Punch location LeftFourPointSwd */
    LeftFourPointSwd("LeftFourPointSwd"),
    /** Punch location RightFourPointSwd */
    RightFourPointSwd("RightFourPointSwd"),
    /** Punch location TopFourPointSwd */
    TopFourPointSwd("TopFourPointSwd"),
    /** Punch location BottomFourPointSwd */
    BottomFourPointSwd("BottomFourPointSwd"),
    /** Punch location FourPointSwd */
    FourPointSwd("FourPointSwd"),
    /** Punch location FourPointAny */
    FourPointAny("FourPointAny"),
    /** Punch location LeftTwoPoint */
    LeftTwoPoint("LeftTwoPoint"),
    /** Punch location RightTwoPoint */
    RightTwoPoint("RightTwoPoint"),
    /** Punch location TopTwoPoint */
    TopTwoPoint("TopTwoPoint"),
    /** Punch location BottomTwoPoint */
    BottomTwoPoint("BottomTwoPoint"),
    /** Punch location Other */
    Other("Other");

    /**
     * SOAP value associated with enum
     */
    public final String mValue;

    /**
     * Staple constructor
     * @param value
     *              SOAP value associated with enum
     */
    PunchMode(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to Staple
     * @param value
     *              SOAP value string
     * @return
     *              Matching PunchMode enum or null if no match is found
     */
    public static PunchMode fromAttributeValue(String value) {
        for(PunchMode enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
