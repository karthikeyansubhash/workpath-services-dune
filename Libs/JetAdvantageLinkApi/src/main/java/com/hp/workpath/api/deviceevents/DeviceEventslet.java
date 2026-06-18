// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.deviceevents;

import android.net.Uri;

import com.hp.jetadvantage.link.common.annotation.DeviceApi;

/**
 * Holds constants for communication with the DeviceEventslet module.
 *
 * @hide for internal use
 * @since API 5
 */
@DeviceApi
public class DeviceEventslet {

    private DeviceEventslet() {
    }

    /**
     * @hide
     */
    public static final String TAG = "DeviceEventslet";

    /**
     * @hide
     */
    public static final String IS_SUPPORTED_EXTRA = "com.hp.jetadvantage.link.api.deviceevents.extra.IS_SUPPORTED";

    /**
     * @hide
     */
    public static final String EVENT_EXTRA = "com.hp.jetadvantage.link.api.deviceevents.extra.DEVICE_EVENTS";

    /**
     * @hide
     */
    public static final String PROVIDER_EVENT_ACTION = "com.hp.jetadvantage.link.api.action.DEVICEEVENTS";

    /**
     * @hide
     */
    public static final String DEVICE_EVENTS_CHANGED_ACTION = "com.hp.jetadvantage.link.intent.action.DEVICE_EVENTS_CHANGED";

    /**
     * @hide
     */
    public static final class Keys {
        public static final String KEY_CLIENT_VERSION = "clientVersion";
        public static final String PACKAGE_NAME = "pkgname";
        public static final String KEY_DEVICE_EVENTS = "deviceEvents";
    }

    /**
     * @hide
     */
    public static final class Method {
        public static final String IS_SUPPORTED = "is_supported";
        public static final String GET_DEVICE_EVENTS = "get_deviceevents";
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
    public static final String AUTHORITY = "com.hp.jetadvantage.link.authority.deviceeventsletcp";

    /**
     * Path for general DeviceEventslet content provider calls
     *
     * @hide for internal communication
     * @since API 5
     */
    public static final String DIR_PATH_SEGMENT = "DeviceEventslet";

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
    public static final Uri CONTENT_URI = new Uri.Builder().scheme(CONTENT_SCHEME).authority(DeviceEventslet.AUTHORITY)
            .appendPath(DIR_PATH_SEGMENT).build();
}
