// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.common;

/**
 * Sdk
 */
public class Sdk {
    /** Package name of the service app */
    public static final String SERVICES_PACKAGE = "com.hp.jetadvantage.link.services";

    public static class VERSION {

        // Similar to android:versionCode, monotonically increasing
        public static final int LEVEL = VERSION_LEVEL.NINE;

        public static final int PLATFORM_LEVEL = VERSION_LEVEL.NINE;

        public static int TARGET_LEVEL = getTarget();

        public static final String NO_VERSION = "0.0.0";

        /**
         * The version as a string
         */
        public static final String VERSION_NAME = "1.6.3";
    }

    public static class VERSION_LEVEL {
        public static final int UNDEFINED = -1;
        public static final int ONE = 1;   //LinkForSDK 1.0 (24.6.X)
        public static final int TWO = 2;   //LinkForSDK 1.1 (24.7)
        public static final int THREE = 3; //LinkForSDK 1.2 (24.7.1), LinkForSDK 1.3 (24.8)
        public static final int FOUR = 4; //LinkForSDK 1.3.1
        public static final int FIVE = 5; //WorkpathSDK 1.5.0
        public static final int SIX = 6; //WorkpathSDK 1.6.0
        public static final int SEVEN = 7; //WorkpathSDK 1.6.1
        public static final int EIGHT = 8; //WorkpathSDK 1.6.2 (25.7)
        public static final int NINE = 9; //WorkpathSDK 1.6.3 (25.9.2.2)
    }

    public static class VERSION_NAME_LEVEL {
        public static final String UNDEFINED = null;
        public static final String THREE = "1.3.1";
    }

    private static int _TARGET = VERSION.LEVEL;
    public void setTarget(int target) { _TARGET = target; }
    private static int getTarget() {
        return _TARGET;
    }
}
