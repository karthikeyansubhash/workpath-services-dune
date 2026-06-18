// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.model;

import android.text.TextUtils;

/**
 * Enumeration of color modes.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum StapleMode {
    /** One staple. */
    Staple("staple"),
    /** One staple top left. */
    StapleTopLeft("staple-top-left"),
    /** One staple top left. */
    StapleBottomLeft("staple-bottom-left"),
    /** One staple top right. */
    StapleTopRight("staple-top-right"),
    /** One staple bottom right. */
    StapleBottomRight("staple-bottom-right"),
    /** Two staples left. */
    DualLeft("staple-dual-left"),
    /** One staple top right. */
    DualRight("staple-dual-right"),
    /** Two staples top. */
    DualTop("staple-dual-top"),
    /** Two staples bottom. */
    DualBottom("staple-dual-bottom"),
    /** Hole. */
    Punch("punch"),
    /** Cover. */
    Cover("cover"),
    /** Bind the set. */
    Bind("bind"),
    /** Bind the set with one or more staples along the middle fold. */
    SaddleStitch("saddle-stitch"),
    /** Bind the set with one or more staples along one edge. */
    EdgeStitch("edge-stitch"),
    /** Bind the set with one or more staples along the left edge. */
    EdgeStitchLeft("edge-stitch-left"),
    /** Bind the set with one or more staples along the right edge. */
    EdgeStitchRight("edge-stitch-right"),
    /** Bind the set with one or more staples along the top edge. */
    EdgeStitchTop("edge-stitch-top"),
    /** Bind the set with one or more staples along the bottom edge. */
    EdgeStitchBottom("edge-stitch-bottom"),
    /** One staple. */
    None("none");

    /**
     * IPP value associated with enum
     */
    public final String mValue;

    /**
     * StapleMode constructor
     * @param value
     *          IPP value associated with enum
     */
    StapleMode(String value) {
        mValue = value;
    }

    /**
     * Convert ipp value to StapleMode
     * @param value
     *              IPP value
     * @return
     *              Matching StapleMode enum or null if no match found
     */
    static StapleMode fromAttributeValue(String value) {
        for(StapleMode enumValue : values()) {
            if (TextUtils.equals(value, enumValue.mValue)) {
                return enumValue;
            }
        }
        return null;
    }
}
