// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.accessorylet.util;

import static com.hp.jetadvantage.link.common.constants.LogConstants.TAG_ACCESSORY_SERVICE;

import android.util.Log;

public class AccessoryUtility {
    private static final String TAG = TAG_ACCESSORY_SERVICE + "/UTL";

    public static void setSystemProperty(String key, String value, String description) {
        try {
            System.setProperty(key, value);
            Log.d(value, description);
        } catch (Exception ignored) {
        }
    }
}
