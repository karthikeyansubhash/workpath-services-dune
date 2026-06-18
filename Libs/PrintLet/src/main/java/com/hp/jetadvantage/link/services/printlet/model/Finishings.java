// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.model;


import java.util.ArrayList;
import java.util.List;

/**
 * Enumeration of color modes.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum Finishings {
    /** No finishing */
    None(3),
    /** One staple. */
    Staple(4),
    /** Punch (any location/count) */
    Punch(5),
    /** Cover. */
    Cover(6),
    /** Bind the set. */
    Bind(7),
    /** Bind the set with one or more staples along the middle fold. */
    SaddleStitch(8),
    /** Bind the set with one or more staples along one edge. */
    EdgeStitch(9),
    /** Fold (any type) */
    Fold(10),
    /** Trim (any type) */
    Trim(11),
    /** Bale (any type) */
    Bale(12),
    /** Fold to make booklet */
    BookletMaker(13),
    /** Offset for binding (any type) */
    JogOffset(14),
    /** Apply protective liquid or powder coating */
    Coat(15),
    /** Apply protective (solid) material */
    Laminate(16),
    /** One staple top left. */
    StapleTopLeft(20),
    /** One staple bottom left. */
    StapleBottomLeft(21),
    /** One staple top right. */
    StapleTopRight(22),
    /** One staple bottom right. */
    StapleBottomRight(23),
    /** Bind the set with one or more staples along the left edge. */
    EdgeStitchLeft(24),
    /** Bind the set with one or more staples along the top edge. */
    EdgeStitchTop(25),
    /** Bind the set with one or more staples along the right edge. */
    EdgeStitchRight(26),
    /** Bind the set with one or more staples along the bottom edge. */
    EdgeStitchBottom(27),
    /** Two staples left. */
    StapleDualLeft(28),
    /** Two staples top. */
    StapleDualTop(29),
    /** One staple top right. */
    StapleDualRight(30),
    /** Two staples bottom. */
    StapleDualBottom(31),
    /** Three staples on left */
    StapleTripleLeft(32),
    /** Three staples on top */
    StapleTripleTop(33),
    /** Three staples on right */
    StapleTripleRight(34),
    /** Three staples on bottom */
    StapleTripleBottom(35),
    /** Bind on left */
    BindLeft(50),
    /** Bind on top */
    BindTop(51),
    /** Bind on right */
    BindRight(52),
    /** Bind on bottom */
    BindBottom(53),
    /** Trim output after each page */
    TrimAfterPages(60),
    /** Trim output after each document */
    TrimAfterDocuments(61),
    /** Trim output after each copy */
    TrimAfterCopies(62),
    /** Trim output after job */
    TrimAfterJob(63),
    /** Punch 1 hole top left */
    PunchTopLeft(70),
    /** Punch 1 hole bottom left */
    PunchBottomLeft(71),
    /** Punch 1 hole top right */
    PunchTopRight(72),
    /** Punch 1 hole bottom right */
    PunchBottomRight(73),
    /** Punch 2 holes left side */
    PunchDualLeft(74),
    /** Punch 2 holes top edge */
    PunchDualTop(75),
    /** Punch 2 holes right side */
    PunchDualRight(76),
    /** Punch 2 holes bottom edge */
    PunchDualBottom(77),
    /** Punch 3 holes left side */
    PunchTripleLeft(78),
    /** Punch 3 holes top edge */
    PunchTripleTop(79),
    /** Punch 3 holes right side */
    PunchTripleRight(80),
    /** Punch 3 holes bottom edge */
    PunchTripleBottom(81),
    /** Punch 4 holes left side */
    PunchQuadLeft(82),
    /** Punch 4 holes top edge */
    PunchQuadTop(83),
    /** Punch 4 holes right side */
    PunchQuadRight(84),
    /** Punch 4 holes bottom edge */
    PunchQuadBottom(85),
    /** Punch multiple holes left side */
    PunchMultipleLeft(86),
    /** Punch multiple holes top edge */
    PunchMultipleTop(87),
    /** Punch multiple holes right side */
    PunchMultipleRight(88),
    /** Punch multiple holes bottom edge */
    PunchMultipleBottom(89),
    /** Accordion-fold the paper vertically into four sections */
    FoldAccordion(90),
    /** Fold the top and bottom quarters of the paper towards the midline, then fold in half vertically */
    FoldDoubleGate(91),
    /** Fold the top and bottom quarters of the paper towards the midline */
    FoldGate(92),
    /** Fold the paper in half vertically */
    FoldHalf(93),
    /** Fold the paper in half horizontally, then Z-fold the paper vertically */
    FoldHalfZ(94),
    /** Fold the top quarter of the paper towards the midline */
    FoldLeftGate(95),
    /** Fold the paper into three sections vertically; sometimes also known as a C fold */
    FoldLetter(96),
    /** Fold the paper in half vertically two times, yielding four sections */
    FoldParallel(97),
    /** Fold the paper in half horizontally and vertically; sometimes also called a cross fold */
    FoldPoster(98),
    /** Fold the bottom quarter of the paper towards the midline */
    FoldRightGate(99),
    /** Fold the paper vertically into three sections, forming a Z */
    FoldZ(100),
    /** Fold the paper vertically into two small sections and one larger, forming an elongated Z */
    FoldEngineeringZ(101);



    /**
     * IPP value associated with enum
     */
    public final int mValue;

    /**
     * StapleMode constructor
     * @param value
     *          IPP value associated with enum
     */
    Finishings(int value) {
        mValue = value;
    }

    /**
     * Convert ipp value to Finishings
     * @param attrValue
     *              IPP value
     * @return
     *              Matching Finishings enum or null if no match found
     */
    static Finishings fromAttributeValue(int attrValue) {
        for(Finishings enumValue : values()) {
            if (enumValue.mValue == attrValue) {
                return enumValue;
            }
        }
        return null;
    }

    /**
     * Convert ipp value to Finishings
     * @param attrValues
     *              IPP value list
     * @return
     *              Matching Finishings enum list or null if no match found
     */
    static List<Finishings> fromAttributeValue(List<Integer> attrValues) {
        ArrayList<Finishings> finishings = new ArrayList<>();
        for(int attrValue : attrValues) {
            for (Finishings enumValue : values()) {
                if (enumValue.mValue == attrValue) {
                    finishings.add(enumValue);
                }
            }
        }
        if (finishings.size() == 0) return null;
        else return finishings;
    }
}
