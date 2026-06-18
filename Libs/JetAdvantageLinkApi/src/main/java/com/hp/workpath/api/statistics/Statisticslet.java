// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.statistics;

import android.net.Uri;

import com.hp.jetadvantage.link.common.annotation.DeviceApi;

/**
 * Holds constants for communication with the Statisticslet module.
 *
 * @hide for internal use
 * @since API 5
 */
@DeviceApi
public class Statisticslet {

    private Statisticslet() {
    }

    /**
     * @hide
     */
    public static final String TAG = "Statisticslet";

    /**
     * @hide
     */
    public static final String IS_SUPPORTED_EXTRA = "com.hp.jetadvantage.link.api.statistics.extra.IS_SUPPORTED";

    /**
     * @hide
     */
    public static final String NOTIFICATION_CHANGE_ACTION = "com.hp.jetadvantage.link.intent.action.NOTIFICATION_CHANGED";

    /**
     * @hide
     */
    public static final String NOTIFICATION_CHANGE_ACTION_SYSTEM = "com.hp.jetadvantage.link.intent.action.system.NOTIFICATION_CHANGED";

    /**
     * @hide
     */
    public static final class Keys {
        public static final String KEY_CLIENT_VERSION = "clientVersion";
        public static final String PACKAGE_NAME = "pkgname";
        public static final String EXTRA_UUID = "UUID";
        public static final String EXTRA_DATA = "EXTRA_DATA";
        public static final String EXTRA_DATA2 = "EXTRA_DATA2";
    }

    /**
     * @hide
     */
    public static final class Method {
        public static final String IS_SUPPORTED = "is_supported";
        public static final String GET_JOBINFO = "get_jobinfo";
        public static final String GET_LASTJOBSEQUENCE = "get_lastjobsequence";
        public static final String COMMIT_LASTJOBSEQUENCE = "commit_lastjobsequence";
        public static final String GET_LASTCOMMITTEDJOBSEQUENCE = "get_lastcommittedjobsequence";
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

        /**
         * @hide
         */
        public static final String KEY_JOBSEQUENCE = "jobSequence";

        /**
         * @hide
         */
        public static final String KEY_JOBLIMIT = "jobLimit";

        /**
         * @hide
         */
        public static final String KEY_OFFSET = "jobOffset";
    }

    /**
     * DeviceUsagelet providers authority
     *
     * @hide for internal communication
     * @since API 5
     */
    public static final String AUTHORITY = "com.hp.jetadvantage.link.authority.statisticsletcp";

    /**
     * Path for general DeviceUsagelet content provider calls
     *
     * @hide for internal communication
     * @since API 5
     */
    public static final String DIR_PATH_SEGMENT = "Statisticslet";

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
    public static final Uri CONTENT_URI = new Uri.Builder().scheme(CONTENT_SCHEME).authority(Statisticslet.AUTHORITY)
            .appendPath(DIR_PATH_SEGMENT).build();
}
