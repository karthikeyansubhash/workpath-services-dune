// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.deviceusage;

import android.net.Uri;

import com.hp.jetadvantage.link.common.annotation.DeviceApi;

/**
 * Holds constants for communication with the DeviceUsagelet module.
 *
 * @hide for internal use
 * @since API 5
 */
@DeviceApi
public class DeviceUsagelet {

    private DeviceUsagelet() {
    }

    /**
     * @hide
     */
    public static final String TAG = "DeviceUsagelet";

    /**
     * @hide
     */
    public static final String IS_SUPPORTED_EXTRA = "com.hp.jetadvantage.link.api.deviceusage.extra.IS_SUPPORTED";

    /**
     * @hide
     */
    public static final class Keys {
        public static final String KEY_CLIENT_VERSION = "clientVersion";
        public static final String PACKAGE_NAME = "pkgname";
    }

    /**
     * @hide
     */
    public static final class Method {
        public static final String IS_SUPPORTED = "is_supported";
        public static final String GET_DEVICEUSAGE = "get_deviceusage";
    }

    /**
     * @hide
     */
    public static final class PlexType {
        public static final String SIMPLEX = "Simplex";
        public static final String DUPLEX = "Duplex";
    }

    /**
     * @hide
     */
    public static final class JobCategory {
        public static final String COPY = "Copy";
        public static final String FAX = "Fax";
        public static final String PRINT = "Print";
        public static final String SEND = "Send";
    }

    /**
     * @hide
     */
    public static final class ColorMode {
        public static final String COLOR = "Color";
        public static final String MONO = "Mono";
    }

    /**
     * @hide
     */
    public static final class ScanPlex {
        public static final String ADF_SIMPLEX = "ADFSimplex";
        public static final String ADF_DUPLEX = "ADFDuplex";
        public static final String FLATBED = "Flatbed";
    }

    /**
     * The parameters used for transporting data. These are used to provide data to the client.
     *
     * @hide The client should not need to know about the content provider parameters
     */
    public static final class Param {

        private Param() {
        }

        /**
         * Param to pass package name to DeviceUsagelet
         *
         * @hide for internal communication
         * @since API 5
         */
        public static final String PACKAGE_NAME = "pkgname";

        /**
         * @hide
         */
        public static final String KEY_CLIENT_VERSION = "clientVersion";
    }

    /**
     * DeviceUsagelet providers authority
     *
     * @hide for internal communication
     * @since API 5
     */
    public static final String AUTHORITY = "com.hp.jetadvantage.link.authority.deviceusageletcp";

    /**
     * Path for general DeviceUsagelet content provider calls
     *
     * @hide for internal communication
     * @since API 5
     */
    public static final String DIR_PATH_SEGMENT = "DeviceUsagelet";

    /**
     * DeviceUsagelet providers scheme
     *
     * @hide for internal communication
     * @since API 5
     */
    public static final String CONTENT_SCHEME = "content";

    /**
     * DeviceUsagelet providers content uri
     *
     * @hide for internal communication
     * @since API 5
     */
    public static final Uri CONTENT_URI = new Uri.Builder().scheme(CONTENT_SCHEME).authority(DeviceUsagelet.AUTHORITY)
            .appendPath(DIR_PATH_SEGMENT).build();
}
