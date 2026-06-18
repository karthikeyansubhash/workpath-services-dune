// Copyright 2025 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.authorization;

import android.net.Uri;

public class Authorizationlet {
    private Authorizationlet() {
    }

    /**
     * @hide
     */
    public static final String TAG = "[SDK]AUTHz";

    /**
     * @hide
     */
    public static final String AUTHORITY = "com.hp.workpath.api.authorization.authorizationletcp";

    /**
     * @hide
     */
    private static final String CONTENT_SCHEME = "content";

    /**
     * @hide
     */
    public static final String DIR_PATH_SEGMENT = "Authorizationlet";

    /**
     * @hide
     */
    public static final Uri CONTENT_URI = new Uri.Builder().scheme(CONTENT_SCHEME).authority(AUTHORITY).appendPath(DIR_PATH_SEGMENT).build();


    /**
     * @hide
     */
    public static final String ACTION = "com.hp.workpath.api.action.AUTHORIZATION";

    /**
     * @hide
     */
    public static final String CHANGED_NOTIFICATION_ACTION = "com.hp.workpath.action.AUTHORIZATION_CHANGE_NOTIFICATION";

    /**
     * @hide
     */
    public static final String PROVIDER_EVENT_ACTION = "com.hp.jetadvantage.link.api.action.AUTHORIZATION";

    /**
     * @hide
     */
    public static final String AUTHORIZATION_PROVIDER = "AUTHORIZATION";

    /**
     * @hide
     */
    public static final String ACTION_AUTHORIZATION_REQUEST = "com.hp.workpath.action.AUTHORIZATION_REQUEST";

    /**
     * @hide
     */
    public static final String ACTION_AUTHORIZATION_PERMISSION = "com.hp.workpath.permission.AUTHORIZATION_PERMISSION";


    public static final class Keys {
        private Keys() {

        }

        /**
         * Param to pass package name
         *
         * @hide for internal communication
         * @since API 3
         */
        public static final String PACKAGE_NAME = "pkgname";

        /**
         * @hide
         */
        public static final String KEY_CLIENT_VERSION = "clientVersion";

        /**
         * @hide
         */
        public static final String KEY_PRINTER_INFO = "PrinterInfo";

        /**
         * @hide
         */
        public static final String KEY_IS_SUPPORT = "isSupport";//"com.hp.jetadvantage.link.api.authorization.ISSUPPORT";

        /**
         * @hide
         */
        public static final String KEY_PROXY_CONFIGURATION = "proxyConfiguration";

        /**
         * @hide
         */
        public static final String KEY_SIGN_IN_METHOD = "signInMethod";

        /**
         * @hide
         */
        public static final String KEY_APP_PERMISSION = "permission";

        /**
         * @hide
         */
        public static final String KEY_LANGUAGE_CODE = "languageCode";

        /**
         * @hide
         */
        public static final String KEY_USER_AUTHORIZATION_DATA = "userAuthorizationData";

        /**
         * @hide
         */
        public static final String KEY_NOTIFICATION_DATA = "changeNotificationEventData";

    }

    /**
     * The methods available for calls to the content provider
     *
     * @hide The client should not need to know about the content provider methods
     */
    public static final class Method {
        private Method() {
        }

        public static final String GET_CONFIGURATION = "getConfiguration";
        public static final String SET_CONFIGURATION = "setConfiguration";
        public static final String GET_PERMISSIONS = "getPermissions";
        public static final String GET_SIGNINMETHODS = "getSignInMethod";
        public static final String INIT_CALLBACK = "initCallback";
        public static final String IS_SUPPORTED = "isSupported";
    }
}
