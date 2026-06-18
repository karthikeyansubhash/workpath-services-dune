// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.launcher;

import android.net.Uri;

/**
 * Holds constants for communication with the Launcherlet module.
 *
 * @hide for internal use
 */
public class Launcherlet {
    /**
     * @hide
     */
    public static final String TAG = "Launcherlet";

    /**
     * @hide
     */
    public static final String IS_SUPPORTED_EXTRA = "com.hp.jetadvantage.link.api.launcher.extra.IS_SUPPORTED";

    /**
     * @hide
     */
    public static final class Method {
        public static final String IS_SUPPORTED = "is_supported";
        public static final String LAUNCH = "launch";
        public static final String APPLICATION = "application";
    }

    /**
     * The parameters used for transporting data. These are used to provide data to the client.
     *
     * @hide The client should not need to know about the content provider parameters
     */
    public static final class Keys {

        private Keys() {
        }

        /**
         * Param to pass package name to Launcherlet
         *
         * @hide for internal communication
         */
        public static final String PACKAGE_NAME = "pkgname";

        /**
         * @hide
         */
        public static final String KEY_CLIENT_VERSION = "clientVersion";

        /**
         * @hide
         */
        public static final String KEY_LAUNCH_ACTION = "launchAction";
        /**
         * @hide
         */
        public static final String KEY_APPLICATION_UUID = "launchUuid";
    }

    /**
     * Launcherlet providers authority
     *
     * @hide for internal communication
     */
    public static final String AUTHORITY = "com.hp.jetadvantage.link.authority.launcherletcp";

    /**
     * Path for general Launcherlet content provider calls
     *
     * @hide for internal communication
     */
    public static final String DIR_PATH_SEGMENT = "launcherletcp";

    /**
     * Launcherlet providers scheme
     *
     * @hide for internal communication
     */
    public static final String CONTENT_SCHEME = "content";

    /**
     * Content Provider URI
     *
     * @hide for internal communication
     */
    public static final Uri CONTENT_URI = new Uri.Builder().scheme(CONTENT_SCHEME).authority(AUTHORITY)
            .appendPath(DIR_PATH_SEGMENT).build();
}
