// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.devicelet.provider;

import android.content.ContentResolver;
import android.content.UriMatcher;

import com.hp.jetadvantage.link.api.device.Devicelet;

public class Key {
    public static final String CONTENT_TYPE_ATTRIBUTE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.sec." + Devicelet.DIR_PATH_SEGMENT +
                    "." + Devicelet.KEY_TYPE_ATTRIBUTE_NAME;

    public static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static final int CODE_ATTRIBUTE = 100;

    public static final String[] RESULT_COLUMN_NAMES = {"RESULT_CODE", "RESULT_ERROR_CODE", "RESULT_CAUSE", "KEY", "VALUE"};

    static {
        sUriMatcher.addURI(Devicelet.AUTHORITY, Devicelet.KEY_TYPE_ATTRIBUTE_NAME + "/*", CODE_ATTRIBUTE);
    }
}
