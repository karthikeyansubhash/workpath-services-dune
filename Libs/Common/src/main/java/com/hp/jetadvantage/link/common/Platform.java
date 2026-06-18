// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.common;

import android.os.Build;

/**
 * Platform
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Platform {

    public static final int BUFFER_SIZE = 65536 << 3; //512KB
    private static final String PRINTER_PANEL = "printer";
    private static final String EMULATOR_STR = "emulator";
    private static boolean isTestMode = false;

    /**
     * Method determines the context this code is executing in: printer panel or mobile
     *
     * @return Returns true if the context is printer panel
     */
    public static boolean isPanel() {
        try {
            return Build.MODEL.toLowerCase().contains(PRINTER_PANEL) || isEmulator();
        } catch (NullPointerException e) {
            // it's ok for unit testing
            return false;
        }
    }

    /**
     * @return Returns true if the context is mobile
     */
    public static boolean isMobile() {
        return !isPanel();
    }

    /**
     * @return Returns true if the context is Emulator
     */
    public static boolean isEmulator() {
        return Build.FINGERPRINT.contains(EMULATOR_STR);
    }

    public static boolean isTestMode() {
        return isTestMode;
    }

    protected static void setTestMode(boolean testMode) {
        isTestMode = testMode;
    }
}
