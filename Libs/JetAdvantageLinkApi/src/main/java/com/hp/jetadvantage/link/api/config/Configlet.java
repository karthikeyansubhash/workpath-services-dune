// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.config;

import android.net.Uri;

import com.hp.jetadvantage.link.common.annotation.DeviceApi;

/**
 * Holds constants for communication with the Configlet module.
 *
 * @hide for internal use
 * @since API 1
 */
@DeviceApi
public class Configlet {

    private Configlet() {
    }

    /**
     * @hide
     */
    public static final String TAG = "Configlet";

    /**
     * @hide
     */
    public static final String IS_SUPPORTED_EXTRA = "com.hp.jetadvantage.link.api.config.extra.IS_SUPPORTED";

    /**
     * @hide
     */
    public static final String CONFIG_CHANGE_ACTION = "com.hp.jetadvantage.link.api.config.CONFIG_CHANGED";

    /**
     * @hide
     */
    public static final class Method {
        public static final String GET_CONFIG = "get_config";
        public static final String SET_CONFIG = "set_config";
        public static final String IS_SUPPORTED = "is_supported";
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
         * Param to pass package name to configLet
         *
         * @hide for internal communication
         * @since API 1
         */
        public static final String PACKAGE_NAME = "pkgname";

        /**
         * Param to pass config data to configLet
         *
         * @hide for internal communication
         * @since API 1
         */
        public static final String CONFIG_DATA = "configdata";

        /**
         * @hide
         */
        public static final String KEY_CLIENT_VERSION = "clientVersion";
    }

    /**
     * Configlet providers authority
     *
     * @hide for internal communication
     * @since API 1
     */
    public static final String AUTHORITY = "com.hp.jetadvantage.link.authority.configletcp";

    /**
     * Path for general Configlet content provider calls
     *
     * @hide for internal communication
     * @since API 1
     */
    public static final String DIR_PATH_SEGMENT = "Configlet";

    /**
     * Configlet providers scheme
     *
     * @hide for internal communication
     * @since API 1
     */
    public static final String CONTENT_SCHEME = "content";

    /**
     * Parameter to store UUID
     *
     * @hide for internal communication
     * @since API 1
     */
    public static final Uri CONTENT_URI = new Uri.Builder().scheme(CONTENT_SCHEME).authority(Configlet.AUTHORITY)
            .appendPath(DIR_PATH_SEGMENT).build();
}
