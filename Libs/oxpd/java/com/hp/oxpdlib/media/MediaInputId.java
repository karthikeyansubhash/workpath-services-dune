// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.media;

import android.text.TextUtils;

public enum MediaInputId {

    Adf("Adf"),
    EnvelopeFeed("EnvelopeFeed"),
    Flatbed("Flatbed"),
    ManualFeedTray("ManualFeedTray"),
    Tray1("Tray1"),
    Tray2("Tray2"),
    Tray3("Tray3"),
    Tray4("Tray4"),
    Tray5("Tray5"),
    Tray6("Tray6"),
    Tray7("Tray7"),
    Tray8("Tray8"),
    Tray9("Tray9"),
    Tray10("Tray10"),
    Tray11("Tray11"),
    Tray12("Tray12"),
    Tray13("Tray13"),
    Tray14("Tray14"),
    Tray15("Tray15"),
    Tray16("Tray16"),
    Auto("Auto"),
    Duplexer("Duplexer"),
    External("External"),
    ExternalTray1("ExternalTray1"),
    ExternalTray2("ExternalTray2"),
    ExternalTray3("ExternalTray3"),
    ExternalTray4("ExternalTray4"),
    ExternalTray5("ExternalTray5"),
    ExternalTray6("ExternalTray6"),
    ExternalTray7("ExternalTray7"),
    ExternalTray8("ExternalTray8"),
    ExternalTray9("ExternalTray9"),
    ExternalTray10("ExternalTray10"),
    MultipurposeTray("MultipurposeTray"),
    PhotoTray("PhotoTray"),
    RearManualFeed("RearManualFeed"),
    Roll1("Roll1"),
    Roll2("Roll2"),
    Roll3("Roll3"),
    Roll4("Roll4"),
    EnvelopeFeedJobSettings("EnvelopeFeedJobSettings"),
    Tray1JobSettings("Tray1JobSettings"),
    Other("Other");

    public final String mValue;

    MediaInputId(String value) {
        mValue = value;
    }

    public static MediaInputId fromAttributeValue(String value) {
        for(MediaInputId enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) return enumValue;
        }
        return null;
    }
}
