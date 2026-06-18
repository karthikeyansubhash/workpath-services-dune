// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.attestation;

import android.net.Uri;

import com.hp.jetadvantage.link.common.annotation.DeviceApi;

/**
 * Holds constants for communication with the Attestationlet module.
 *
 * @hide for internal use
 * @since API 3
 */
@DeviceApi
public class Attestationlet {

    private Attestationlet() {
    }

    /**
     * @hide
     */
    public static final String TAG = "Attestationlet";

    /**
     * @hide
     */
    public static final String ATT_SUPPORT_EXTRA = "com.hp.jetadvantage.link.api.attestation.extra.ATTSUPPORT";

    /**
     * @hide
     */
    public static final String APP_TOKEN = "com.hp.jetadvantage.link.api.attestation.extra.APPTOKEN";

    /**
     * @hide
     */
    public static final class Keys {
        public static final String KEY_CLIENT_VERSION = "clientVersion";
        public static final String KEY_ACTION = "action";
        public static final String KEY_APPLICATION_TOKEN = "applicationToken";
        public static final String KEY_APPLICATION_EXPIN = "applicationExpIn";

        public static final String PACKAGE_NAME = "pkgname";
    }

    /**
     * @hide
     */
    public static final class Method {
        public static final String GET_TOKEN = "get_token";
        public static final String IS_SUPPORTED = "is_supported";
    }

    /**
     * Attestationlet providers authority
     *
     * @hide for internal communication
     */
    public static final String AUTHORITY = "com.hp.jetadvantage.link.authority.attestationletcp";

    /**
     * Path for general Attestationlet content provider calls
     *
     * @hide for internal communication
     */
    public static final String DIR_PATH_SEGMENT = "Attestationlet";

    /**
     * Attestationlet providers scheme
     *
     * @hide for internal communication
     */
    private static final String CONTENT_SCHEME = "content";

    /**
     * Attestationlet providers content uri
     *
     * @hide for internal communication
     */
    public static final Uri CONTENT_URI = new Uri.Builder().scheme(CONTENT_SCHEME).authority(Attestationlet.AUTHORITY)
            .appendPath(DIR_PATH_SEGMENT).build();
}
