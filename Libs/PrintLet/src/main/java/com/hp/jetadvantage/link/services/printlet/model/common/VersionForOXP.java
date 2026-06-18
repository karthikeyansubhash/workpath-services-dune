// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.model.common;

/**
 * VersionForOXP
 */
public class VersionForOXP {

    public static class VERSION {

        // Similar to android:versionCode, monotonically increasing
        public static final int LEVEL = VERSION_LEVEL.THREE;

        public static final String NO_VERSION = "0.0.0";

        /**
         * The version as a string
         */
        public static final String VERSION_NAME = "1.2.0";
    }

    public static class VERSION_LEVEL {
        public static final int UNDEFINED = -1;
        public static final int ONE = 1;
        public static final int TWO = 2;
        public static final int THREE = 3;
    }
}
