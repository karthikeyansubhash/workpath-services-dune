// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.device;

import android.net.Uri;

import com.hp.jetadvantage.link.common.annotation.CommonApi;

/**
 * Holds constants for communication with the Devicelet module.
 *
 * @hide
 * @since API 1
 */
@CommonApi
public class Devicelet {
    private Devicelet() {
    }

    /**
     * @hide
     */
    public static final String TAG = "Devicelet";

    /**
     * The parameters used for transporting data. These are used to provide data to the client.
     *
     * @hide The client should not need to know about the content provider parameters
     */
    public static final class Param {

        private Param() {
        }

        /**
         * Param to pass package name to deviceLet
         *
         * @hide for internal communication
         * @since API 1
         */
        public static final String PACKAGE_NAME = "pkgname";

        /**
         * @hide
         */
        public static final String KEY_CLIENT_VERSION = "clientVersion";

        /**
         * @hide
         */
        public static final String KEY_ATTRIBUTE_NAME = "attributeName";
    }

    /**
     * @hide
     */
    public static final String AUTHORITY = "com.hp.jetadvantage.link.authority.deviceletcp";

    /**
     * @hide
     */
    public static final String DIR_PATH_SEGMENT = "deviceletcp";

    /**
     * @hide
     */
    private static final String CONTENT_SCHEME = "content";

    /**
     * @hide
     */
    public static final String IS_SUPPORTED_EXTRA = "com.hp.jetadvantage.link.api.device.extra.IS_SUPPORTED";

    /**
     * Key type is an attribute
     *
     * @hide internal communication data
     */
    public static final String KEY_TYPE_ATTRIBUTE_NAME = "attribute";

    /**
     * @hide
     */
    public static final Uri CONTENT_URI = new Uri.Builder().scheme(CONTENT_SCHEME).authority(AUTHORITY).build();

    // Column names for result column
    /**
     * Result code column
     *
     * @hide internal use
     */
    public static final int RESULT_CODE_COLUMN = 0;
    /**
     * Result error code column
     *
     * @hide internal use
     */
    public static final int RESULT_ERROR_CODE_COLUMN = 1;
    /**
     * Result cause column
     *
     * @hide internal use
     */
    public static final int RESULT_CAUSE_COLUMN = 2;
    /**
     * Key / uri string column
     *
     * @hide internal use
     */
    public static final int KEY_COLUMN = 3;
    /**
     * Value string column
     *
     * @hide internal use
     */
    public static final int VALUE_COLUMN = 4;

    /**
     * @hide
     */
    public static final class Method {
        public static final String IS_SUPPORTED = "is_supported";
        public static final String GET_ATTRIBUTE = "get_attribute";
    }

}
