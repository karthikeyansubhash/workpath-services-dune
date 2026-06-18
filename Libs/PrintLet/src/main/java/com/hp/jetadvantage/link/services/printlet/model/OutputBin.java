// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.model;

import android.text.TextUtils;

/**
 * Enumeration of output-bin modes.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum OutputBin {

    /** Automatically select output-bin */
    Auto("auto"),
    /** Bottom select output-bin. */
    Bottom("bottom"),
    /** Center select output-bin. */
    Center("center"),
    /** Face-Down select output-bin. */
    FaceDown("face-down"),
    /** Face-up select output-bin. */
    FaceUp("face-up"),
    /** LargeCapacity select output-bin. */
    LargeCapacity("large-capacity"),
    /** Left select output-bin. */
    Left("left"),
    /** Mailbox1 select output-bin. */
    Mailbox1("mailbox-1"),
    /** Mailbox2 select output-bin. */
    Mailbox2("mailbox-2"),
    /** Mailbox3 select output-bin. */
    Mailbox3("mailbox-3"),
    /** Mailbox4 select output-bin. */
    Mailbox4("mailbox-4"),
    /** Mailbox5 select output-bin. */
    Mailbox5("mailbox-5"),
    /** Mailbox6 select output-bin. */
    Mailbox6("mailbox-6"),
    /** Mailbox7 select output-bin. */
    Mailbox7("mailbox-7"),
    /** Mailbox8 select output-bin. */
    Mailbox8("mailbox-8"),
    /** Mailbox9 select output-bin. */
    Mailbox9("mailbox-9"),
    /** Mailbox10 select output-bin. */
    Mailbox10("mailbox-10"),
    /** Middle select output-bin. */
    Middle("middle"),
    /** MyMailbox select output-bin. */
    MyMailbox("my-mailbox"),
    /** Rear select output-bin. */
    Rear("rear"),
    /** Right select output-bin. */
    Right("right"),
    /** Side select output-bin. */
    Side("side"),
    /** Stacker1 select output-bin. */
    Stacker1("stacker-1"),
    /** Stacker2 select output-bin. */
    Stacker2("stacker-2"),
    /** Stacker3 select output-bin. */
    Stacker3("stacker-3"),
    /** Stacker4 select output-bin. */
    Stacker4("stacker-4"),
    /** Stacker5 select output-bin. */
    Stacker5("stacker-5"),
    /** Stacker6 select output-bin. */
    Stacker6("stacker-6"),
    /** Stacker7 select output-bin. */
    Stacker7("stacker-7"),
    /** Stacker8 select output-bin. */
    Stacker8("stacker-8"),
    /** Stacker9 select output-bin. */
    Stacker9("stacker-9"),
    /** Stacker10 select output-bin. */
    Stacker10("stacker-10"),
    /** Top select output-bin. */
    Top("top"),
    /** Tray1 select output-bin. */
    Tray1("tray-1"),
    /** Tray2 select output-bin. */
    Tray2("tray-2"),
    /** Tray3 select output-bin. */
    Tray3("tray-3"),
    /** Tray4 select output-bin. */
    Tray4("tray-4"),
    /** Tray5 select output-bin. */
    Tray5("tray-5"),
    /** Tray6 select output-bin. */
    Tray6("tray-6"),
    /** Tray7 select output-bin. */
    Tray7("tray-7"),
    /** Tray8 select output-bin. */
    Tray8("tray-8"),
    /** Tray9 select output-bin. */
    Tray9("tray-9"),
    /** Tray10 select output-bin. */
    Tray10("tray-10");

    /**
     * IPP value associated with enum
     */
    public final String mValue;

    /**
     * ColorMode constructor
     * @param value
     *              IPP value associated with enum
     */
    OutputBin(String value) {
        mValue = value;
    }

    /**
     * Convert ipp value string to OutputBin
     * @param value
     *              ipp value string
     * @return
     *              Matching OutputBin enum or Auto if no match is found
     */
    static OutputBin fromAttributeValue(String value) {
        for(OutputBin enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
