// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.device.settings;

import android.net.Uri;

/**
 * Holds constants for communication with the DeviceSettingslet module.
 *
 * @hide for internal use
 */
public class DeviceSettingslet {
    /**
     * @hide
     */
    public static final String TAG = "DeviceSettingslet";

    /**
     * @hide
     */
    public static final String IS_SUPPORTED_EXTRA = "com.hp.jetadvantage.link.api.devicesettings.extra.IS_SUPPORTED";

    /**
     * @hide
     */
    public static final class Method {
        public static final String IS_SUPPORTED = "is_supported";
        public static final String ENABLE_PRINTING = "enablePrinting";
        public static final String DISABLE_PRINTING = "disablePrinting";
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
         * Param to pass package name to DeviceSettingslet
         *
         * @hide for internal communication
         */
        public static final String PACKAGE_NAME = "pkgname";

        /**
         * @hide
         */
        public static final String KEY_CLIENT_VERSION = "clientVersion";
    }

    /**
     * DeviceSettingslet providers authority
     *
     * @hide for internal communication
     */
    public static final String AUTHORITY = "com.hp.jetadvantage.link.authority.devicesettingstcp";

    /**
     * Path for general DeviceSettingslet content provider calls
     *
     * @hide for internal communication
     */
    public static final String DIR_PATH_SEGMENT = "devicesettingstcp";

    /**
     * DeviceSettingslet providers scheme
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
