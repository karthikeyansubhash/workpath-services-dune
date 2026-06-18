// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.jabberwocky.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * Debugging utility
 */
@SuppressWarnings("unused")
public class FnDebugUtils {

    /**
     * Global flag to use for debugging checks
     */
    public static boolean mDebugEnabled = false;

    /**
     * Private constructor
     */
    private FnDebugUtils() {}

    /**
     * Initialize debugging mode from application context
     * @param context
     *              Context to use for lookup
     */
    public static void setDebugMode(Context context) {
        if (context != null) {
            mDebugEnabled = ((context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0);
        }
    }

    /**
     * Print the current callstack
     * @param message
     *              Message to include in "Exception" call stack
     */
    public static void printStackTrack(String message) {
        try {
            throw new Exception("]:->" + ((message != null) ? message : "" ) + " 3---");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
