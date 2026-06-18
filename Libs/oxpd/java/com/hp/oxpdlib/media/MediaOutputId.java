// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.media;

import android.text.TextUtils;

public enum MediaOutputId {

    Accessory("Accessory"),
    Adf("Adf"),
    Auto("Auto"),
    Booklet("Booklet"),
    Default("Default"),
    DocumentFeeder("DocumentFeeder"),
    External("External"),
    FaceDown("FaceDown"),
    FaceDownCorrectOrder("FaceDownCorrectOrder"),
    FaceUp("FaceUp"),
    FaceUpStraightestPath("FaceUpStraightestPath"),
    Fax("Fax"),
    Folded("Folded"),
    Left("Left"),
    LeftStraightestPath("LeftStraightestPath"),
    Lower("Lower"),
    LowerBooklet("LowerBooklet"),
    LowerLeft("LowerLeft"),
    LowerLeftHighestCapacity("LowerLeftHighestCapacity"),
    LowerStacker("LowerStacker"),
    MainCopier("MainCopier"),
    Middle("Middle"),
    MiddleLeft("MiddleLeft"),
    OutputBin1("OutputBin1"),
    OutputBin2("OutputBin2"),
    OutputBin3("OutputBin3"),
    OutputBin4("OutputBin4"),
    OutputBin5("OutputBin5"),
    OutputBin6("OutputBin6"),
    OutputBin7("OutputBin7"),
    OutputBin8("OutputBin8"),
    OutputBin9("OutputBin9"),
    OutputBin10("OutputBin10"),
    OutputBin11("OutputBin11"),
    OutputBin12("OutputBin12"),
    OutputBin13("OutputBin13"),
    OutputBin14("OutputBin14"),
    OutputBin15("OutputBin15"),
    OutputBin16("OutputBin16"),
    Rear("Rear"),
    RearFaceUp("RearFaceUp"),
    RearStraightestPath("RearStraightestPath"),
    Stacker("Stacker"),
    Standard("Standard"),
    StandardCorrectOrder("StandardCorrectOrder"),
    StandardTop("StandardTop"),
    Top("Top"),
    Upper("Upper"),
    UpperFaceUp("UpperFaceUp"),
    UpperLeft("UpperLeft"),
    UpperLeftBins("UpperLeftBins"),
    UpperLeftStraightestPath("UpperLeftStraightestPath"),
    VirtualBins1To3("VirtualBins1To3"),
    VirtualBins1To5("VirtualBins1To5"),
    VirtualBins1To8("VirtualBins1To8"),
    VirtualBins1To10("VirtualBins1To10"),
    VirtualBins2To8("VirtualBins2To8"),
    VirtualFinisherBins("VirtualFinisherBins"),
    VirtualLeftBins("VirtualLeftBins"),
    Alternate("Alternate"),
    Bottom("Bottom"),
    Center("Center"),
    Collator("Collator"),
    Duplexer("Duplexer"),
    EngineOptionalBin1("EngineOptionalBin1"),
    LargeCapacity("LargeCapacity"),
    MyMailbox("MyMailbox"),
    Right("Right"),
    Side("Side"),
    StackerFacedown("StackerFacedown"),
    StackerFaceUp("StackerFaceUp"),
    StackerStaples("StackerStaples"),
    UniversalOutputBin("UniversalOutputBin"),
    Stapler("Stapler"),
    Other("Other");

    public final String mValue;

    MediaOutputId(String value) {
        mValue = value;
    }

    public static MediaOutputId fromAttributeValue(String value) {
        for (MediaOutputId enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) return enumValue;
        }
        return null;
    }
}
