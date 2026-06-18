// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.supplies;

import android.net.Uri;

import com.hp.jetadvantage.link.common.annotation.DeviceApi;

/**
 * Holds constants for communication with the Supplieslet module.
 *
 * @hide for internal use
 * @since API 5
 */
@DeviceApi
public class Supplieslet {

    private Supplieslet() {
    }

    /**
     * @hide
     */
    public static final String TAG = "Supplieslet";

    /**
     * @hide
     */
    public static final String IS_SUPPORTED_EXTRA = "com.hp.jetadvantage.link.api.supplies.extra.IS_SUPPORTED";

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
        public static final String GET_SUPPLIES = "get_supplies";
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
         * Param to pass package name to Supplieslet
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
     * Supplieslet providers authority
     *
     * @hide for internal communication
     * @since API 5
     */
    public static final String AUTHORITY = "com.hp.jetadvantage.link.authority.suppliesletcp";

    /**
     * Path for general Supplieslet content provider calls
     *
     * @hide for internal communication
     * @since API 5
     */
    public static final String DIR_PATH_SEGMENT = "Supplieslet";

    /**
     * Supplieslet providers scheme
     *
     * @hide for internal communication
     * @since API 5
     */
    public static final String CONTENT_SCHEME = "content";

    /**
     * Supplieslet providers content uri
     *
     * @hide for internal communication
     * @since API 5
     */
    public static final Uri CONTENT_URI = new Uri.Builder().scheme(CONTENT_SCHEME).authority(Supplieslet.AUTHORITY)
            .appendPath(DIR_PATH_SEGMENT).build();
}
