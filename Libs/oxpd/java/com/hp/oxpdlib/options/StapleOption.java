package com.hp.oxpdlib.options;
import android.text.TextUtils;

/** Enumeration of copy file types. */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum StapleOption {
    /** staple location None */
    None("None"),
    /** staple location TopAnyOnePointAny */
    TopAnyOnePointAny("TopAnyOnePointAny"),
    /** staple location TopAnyonePointAngled */
    TopAnyonePointAngled("TopAnyonePointAngled"),
    /** staple location TopLeftOnePointAny */
    TopLeftOnePointAny("TopLeftOnePointAny"),
    /** staple location TopLeftOnePointAngled */
    TopLeftOnePointAngled("TopLeftOnePointAngled"),
    /** staple location TopLeftOnePointHorizontal */
    TopLeftOnePointHorizontal("TopLeftOnePointHorizontal"),
    /** staple location TopLeftOnePointHorizontal */
    TopLeftOnePointVertical("TopLeftOnePointHorizontal"),
    /** staple location TopRightOnePointAny */
    TopRightOnePointAny("TopRightOnePointAny"),
    /** staple location TopRightOnePointAngled */
    TopRightOnePointAngled("TopRightOnePointAngled"),
    /** staple location TopRightOnePointHorizontal */
    TopRightOnePointHorizontal("TopRightOnePointHorizontal"),
    /** staple location TopRightOnePointVertical */
    TopRightOnePointVertical("TopRightOnePointVertical"),
    /** staple location BottomAnyOnePointAny */
    BottomAnyOnePointAny("BottomAnyOnePointAny"),
    /** staple location BottomAnyonePointAngled */
    BottomAnyonePointAngled("BottomAnyonePointAngled"),
    /** staple location BottomLeftOnePointAny */
    BottomLeftOnePointAny("BottomLeftOnePointAny"),
    /** staple location BottomLeftOnePointAngled */
    BottomLeftOnePointAngled("BottomLeftOnePointAngled"),
    /** staple location BottomLeftOnePointHorizontal */
    BottomLeftOnePointHorizontal("BottomLeftOnePointHorizontal"),
    /** staple location BottomLeftOnePointVertical */
    BottomLeftOnePointVertical("BottomLeftOnePointVertical"),
    /** staple location BottomRightOnePointAny */
    BottomRightOnePointAny("BottomRightOnePointAny"),
    /** staple location BottomRightOnePointAngled */
    BottomRightOnePointAngled("BottomRightOnePointAngled"),
    /** staple location BottomRightOnePointHorizontal */
    BottomRightOnePointHorizontal("BottomRightOnePointHorizontal"),
    /** staple location BottomRightOnePointVertical */
    BottomRightOnePointVertical("BottomRightOnePointVertical"),
    /** staple location CenterOnePoint */
    CenterOnePoint("CenterOnePoint"),
    /** staple location LeftTwoPoints */
    LeftTwoPoints("LeftTwoPoints"),
    /** staple location LeftTwoPointsAny */
    LeftTwoPointsAny("LeftTwoPointsAny"),
    /** staple location RightTwoPoints */
    RightTwoPoints("RightTwoPoints"),
    /** staple location TopTwoPoints */
    TopTwoPoints("TopTwoPoints"),
    /** staple location BottomTwoPoints */
    BottomTwoPoints("BottomTwoPoints"),
    /** staple location CenterTwoPoints */
    CenterTwoPoints("CenterTwoPoints"),
    /** staple location LeftThreePoints */
    LeftThreePoints("LeftThreePoints"),
    /** staple location LeftThreePointsAny */
    LeftThreePointsAny("LeftThreePointsAny"),
    /** staple location RightThreePoints */
    RightThreePoints("RightThreePoints"),
    /** staple location TopThreePoints */
    TopThreePoints("TopThreePoints"),
    /** staple location BottomThreePoints */
    BottomThreePoints("BottomThreePoints"),
    /** staple location CenterThreePoints */
    CenterThreePoints("CenterThreePoints"),
    /** staple location LeftSixPoints */
    LeftSixPoints("LeftSixPoints"),
    /** staple location LeftSixPointsAny */
    LeftSixPointsAny("LeftSixPointsAny"),
    /** staple location RightSixPoints */
    RightSixPoints("RightSixPoints"),
    /** staple location TopSixPoints */
    TopSixPoints("TopSixPoints"),
    /** staple location BottomSixPoints */
    BottomSixPoints("BottomSixPoints"),
    /** staple location CenterSixPoints */
    CenterSixPoints("CenterSixPoints"),
    /** staple location CustomLegacy */
    CustomLegacy("CustomLegacy"),
    /** staple location CustomPointsOption1 */
    CustomPointsOption1("CustomPointsOption1"),
    /** staple location CustomPointsOption2 */
    CustomPointsOption2("CustomPointsOption2"),
    /** staple location CustomPointsOption3 */
    CustomPointsOption3("CustomPointsOption3"),
    /** staple location CustomPointsOption4 */
    CustomPointsOption4("CustomPointsOption4"),
    /** staple location CustomPointsOption5 */
    CustomPointsOption5("CustomPointsOption5"),
    /** staple location CustomPointsOption6 */
    CustomPointsOption6("CustomPointsOption6"),
    /** staple location CustomPointsOption7 */
    CustomPointsOption7("CustomPointsOption7"),
    /** staple location CustomPointsOption8 */
    CustomPointsOption8("CustomPointsOption8"),
    /** staple location CustomPointsOption9 */
    CustomPointsOption9("CustomPointsOption9"),
    /** staple location CustomPointsOption10 */
    CustomPointsOption10("CustomPointsOption10"),
    /** staple location ExtraOption1 */
    ExtraOption1("ExtraOption1"),
    /** staple location ExtraOption2 */
    ExtraOption2("ExtraOption2"),
    /** staple location ExtraOption3 */
    ExtraOption3("ExtraOption3"),
    /** staple location ExtraOption4 */
    ExtraOption4("ExtraOption4"),
    /** staple location ExtraOption5 */
    ExtraOption5("ExtraOption5"),
    /** staple location ExtraOption6 */
    ExtraOption6("ExtraOption6"),
    /** staple location ExtraOption7 */
    ExtraOption7("ExtraOption7"),
    /** staple location ExtraOption8 */
    ExtraOption8("ExtraOption8"),
    /** staple location ExtraOption9 */
    ExtraOption9("ExtraOption9"),
    /** staple location ExtraOption10 */
    ExtraOption10("ExtraOption10"),
    /** staple location Other */
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
    StapleOption(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to Staple
     * @param value
     *              SOAP value string
     * @return
     *              Matching StapleOption enum or null if no match is found
     */
    public static StapleOption fromAttributeValue(String value) {
        for(StapleOption enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
