// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.helper.email;

import android.net.Uri;

import com.hp.jetadvantage.link.common.annotation.DeviceApi;

/**
 * Holds constants for communication with the EmailLet module.
 *
 * @hide for internal use
 * @since API 1
 */
@DeviceApi
public class Emaillet {
    private Emaillet() {
    }

    /**
     * @hide
     */
    public static final String TAG = "Emaillet";

    /**
     * @hide
     */
    public static final String IS_SUPPORTED_EXTRA = "com.hp.jetadvantage.link.api.config.extra.IS_SUPPORTED";

    /**
     * @hide
     */
    public static final class Method {
        public static final String SEND = "send";
        public static final String GET_DEFAULTS = "get_defaults";
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
         * Param to pass package name to Emaillet
         *
         * @hide for internal communication
         */
        public static final String PACKAGE_NAME = "pkgname";

        /**
         * Param to pass Email data to Emaillet
         *
         * @hide for internal communication
         */
        public static final String EMAIL_ATTRIBUTES = "emailAttributes";

        /**
         * Param to pass Attachment list to Emaillet
         *
         * @hide for internal communication
         */
        public static final String EMAIL_FILENAMES = "emailFileNames";

        /**
         * Param to pass SMTP data to Emaillet
         *
         * @hide for internal communication
         */
        public static final String SMTP_ATTRIBUTES = "smtpAttributes";

        /**
         * Param to pass Proxy data to Emaillet
         *
         * @hide for internal communication
         */
        public static final String PROXY_ATTRIBUTES = "proxyAttributes";

        /**
         * @hide
         */
        public static final String KEY_CLIENT_VERSION = "clientVersion";
    }

    /**
     * Emaillet providers authority
     *
     * @hide for internal communication
     */
    public static final String AUTHORITY = "com.hp.jetadvantage.link.authority.emailletcp";

    /**
     * Path for general Emaillet content provider calls
     *
     * @hide for internal communication
     */
    public static final String DIR_PATH_SEGMENT = "emailletcp";

    /**
     * Emaillet providers scheme
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
