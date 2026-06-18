// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.access;

import android.net.Uri;

import com.hp.jetadvantage.link.common.annotation.DeviceApi;

/**
 * Holds constants for communication with the Accesslet module.
 *
 * @hide for internal use
 * @since API 1
 */
@DeviceApi
public class Accesslet {

    private Accesslet() {
    }

    /**
     * @hide
     */
    public static final String TAG = "Accesslet";

    /**
     * @hide
     */
    public static final String AA_SUPPORT_EXTRA = "com.hp.jetadvantage.link.api.access.extra.AASUPPORT";

    /**
     * @hide
     */
    public static final String PRINCIPAL_EXTRA = "com.hp.jetadvantage.link.api.access.extra.PRINCIPAL";

    /**
     * @hide
     */
    public static final String PROVIDER_EVENT_ACTION = "com.hp.jetadvantage.link.api.action.AUTHENTICATION";

    /**
     * @hide
     */
    public static final class Keys {
        public static final String KEY_CLIENT_VERSION = "clientVersion";
        public static final String KEY_ACTION = "action";
        public static final String KEY_AUTHENTICATION = "authentication";
        public static final String KEY_CLIENT_ID = "clientId";
        public static final String PACKAGE_NAME = "pkgname";
    }

    /**
     * @hide
     */
    public static final class Method {
        public static final String GET_PROPERTIES = "retrieve_properties";
        public static final String SIGN_IN = "sign_in";
        public static final String SIGN_OUT = "sign_out";
        public static final String START_SIGN_IN_PROCESS = "start_sign_in_process";
        public static final String IS_SUPPORTED = "is_supported";
        public static final String GET_TOKEN = "get_token";
    }

    /**
     * @hide
     */
    public static final class DuneMethod {
        public static final String GET_CREDENTIAL = "get_credential";
        public static final String SAVE_SESSION = "save_session";
    }

    /**
     * Accesslet providers authority
     *
     * @hide for internal communication
     */
    public static final String AUTHORITY = "com.hp.jetadvantage.link.authority.accessletcp";

    /**
     * Path for general Accesslet content provider calls
     *
     * @hide for internal communication
     */
    public static final String DIR_PATH_SEGMENT = "Accesslet";

    /**
     * Accesslet providers scheme
     *
     * @hide for internal communication
     */
    private static final String CONTENT_SCHEME = "content";

    /**
     * Accesslet providers content uri
     *
     * @hide for internal communication
     */
    public static final Uri CONTENT_URI = new Uri.Builder().scheme(CONTENT_SCHEME).authority(Accesslet.AUTHORITY)
            .appendPath(DIR_PATH_SEGMENT).build();
}
