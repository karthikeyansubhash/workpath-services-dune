// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.job;

import android.net.Uri;

/**
 * Specifies information necessary to communicate with the Joblet.
 *
 * @hide for internal use
 * @since API 1
 */
public class Joblet {

    private Joblet() {
    }

    /**
     * @hide trivial
     */
    public static final String TAG = "Joblet";

    /**
     * @hide trivial
     * @since API 1
     */
    public static final String ACTION = "com.hp.jetadvantage.link.intent.action.joblet";

    /**
     * Broadcast action to register JobletAbstractObserver
     *
     * @hide for internal use only
     * @since API 1
     */
    public static final String ACTION_REGISTER = "com.hp.jetadvantage.link.intent.action.joblet.REGISTER";

    /**
     * @hide client is not revealed the request id passed if printer is not connected.  Result codes are used for that.
     */
    public static final String UNKNOWN_MFP_ID = "ff:ff:ff:ff:ff:ff/255.255.255.255";
    /**
     * @hide trivial
     */
    public static final String AUTHORITY = "com.hp.jetadvantage.link.authority.joblet";

    /**
     * @hide internal
     */
    public static final String DIR_PATH_SEGMENT = "joblet";

    /**
     * @hide internal
     */
    public static final String CLIENTS_PATH_SEGMENT = "clients";

    /**
     * @hide trivial
     */
    private static final String CONTENT_SCHEME = "content";

    /**
     * @hide trivial
     */
    public static final Uri CONTENT_URI = new Uri.Builder().scheme(CONTENT_SCHEME).authority(AUTHORITY)
            .appendPath(DIR_PATH_SEGMENT).build();


    /**
     * Methods available for calls to the Joblet ContentProvider
     *
     * @hide The client should not need to know about the content provider methods
     */
    public static final class Method {
        private Method() {
            // Utility class
        }

        /**
         * @hide The client should not need to know about the content provider methods
         */
        public static final String CANCEL_JOB = "cancelJob";
        /**
         * @hide The client should not need to know about the content provider methods
         */
        public static final String GET_JOB_INFO = "getJobInfo";
        /**
         * @hide The client should not need to know about the content provider methods
         */
        public static final String GET_PROCESSING_INFO = "getProcessingInfo";
        /**
         * @hide The client should not need to know about the content provider methods
         */
        public static final String GET_JOB_RID = "getJobRid";
    }

    /**
     * Keys used for transporting data.
     *
     * @since API 1
     */
    public static final class Keys {

        private Keys() {
            // Utility class
        }

        /**
         * Key to retrieve the job id.
         *
         * @since API 1
         */
        public static final String KEY_JOBID = "jobId";

        /**
         * Key to retrieve the rid.
         *
         * @since API 1
         * @hide The client should not need to know about these params
         */
        public static final String KEY_RID = "rid";

        /**
         * <br><b>Type:</b> Intent / Parcelable
         *
         * @since API 1
         * @hide The client should not need to know about these params
         */
        public static final String KEY_PENDING_INTENT = "pendingIntent";

        /**
         * Key to retrieve if the job was successful or not
         *
         * @hide The client should not need to know about these params
         * @since API 1
         */
        public static final String KEY_IS_SUCCESS = "isSuccess";
        /**
         * Key to retrieve the scanned image count
         * <p>
         * <b>Type:</b> int
         *
         * @since API 1
         */
        public static final String KEY_SCAN_IMAGE_COUNT = "scanImageCount";
        /**
         * Key to retrieve the destinations count to which data has been sent
         * <p>
         * <b>Type:</b> int
         *
         * @since API 1
         * @hide The client should not need to know about these params
         */
        public static final String KEY_DST_COUNT = "destinationCount";
        /**
         * Key to retrieve the total number of destinations
         * <p>
         * <br><b>Type:</b> int
         *
         * @since API 1
         * @hide The client should not need to know about these params
         */
        public static final String KEY_DST_TOTAL = "destinationTotal";
        /**
         * Key to retrieve the printed image count
         * <p>
         * <br><b>Type:</b> int
         *
         * @since API 1
         */
        public static final String KEY_PRINT_IMAGE_COUNT = "printImageCount";
        /**
         * Key to retrieve the total number of sets printer
         * <p>
         * <br><b>Type:</b> int
         *
         * @since API 1
         * @hide The client should not need to know about these params
         */
        public static final String KEY_SET_COUNT = "setsCompleted";
        /**
         * Key to retrieve the number of sheets printed
         * <p>
         * <br><b>Type:</b> int
         *
         * @since API 1
         * @hide The client should not need to know about these params
         */
        public static final String KEY_SHEET_COUNT = "sheetsCompleted";
        /**
         * Key to retrieve job information. Can be obtained only in all jobs observer.
         * <p>
         * <br><b>Type:</b> {@link JobInfo}
         *
         * @since API 1
         * @hide The client should not need to know about these params
         */
        public static final String KEY_JOB_INFO = "job";

        /**
         * * <br><b>Type:</b> boolean
         *
         * @since API 1
         * @hide The client should not need to know about these params
         */
        public static final String KEY_SINGLESEGMENTSCAN_FLAG = "singleSegmentScanFlag";

        /**
         * <br><b>Type:</b> Parcelable
         *
         * @since API 1
         * @hide The client should not need to know about these params
         */
        public static final String KEY_JOB_TYPE = "jobType";

        /**
         * Client version extra for determining Joblet behaviour
         * <p>
         * <br><b>Type:</b> int
         *
         * @since API 1
         * @hide The client should not need to know about these params
         */
        public static final String KEY_CLIENTS_VERSION = "clientsVersion";

        /**
         * Client package extra for proper callbacks sending
         * <p>
         * <br><b>Type:</b> String
         *
         * @since API 1
         * @hide The client should not need to know about these params
         */
        public static final String KEY_CLIENT_PACKAGE = "clientsPackage";

        /**
         * * <br><b>Type:</b> boolean
         *
         * @since API 1
         * @hide The client should not need to know about these params
         */
        public static final String KEY_SCANNINGCOMPLETED_FLAG = "scanningCompletedFlag";
    }

    /**
     * @hide The client should not need to know about the content provider params
     */
    public static final class Params {
        private Params() {
        }

        /**
         * @hide The client should not need to know about the content provider params
         */
        public static final String JOB_ID_TAG = "jobId";
        /**
         * @hide The client should not need to know about the content provider params
         */
        public static final String PROCESSING_INFO_LIST = "procInfoList";
        /**
         * @hide The client should not need to know about the content provider params
         */
        public static final String JOB_INFO_FILTER_TAG = "jobInfoFilter";
        /**
         * @hide The client should not need to know about the content provider params
         */
        public static final String KEY_CLIENT_VERSION = "clientVersion";
    }

    /**
     * @hide The client does not need to know about the server messages
     */
    public static final class Message {
        public static final int MSG_CANCEL_NPCD = 0;
        public static final int MSG_SHOW_NPCD = 1;
        public static final int MSG_SHOW_JPD = 2;
        public static final int MSG_RE_SHOW_JPD = 3;
        public static final int MSG_CANCEL_JPD = 4;
        public static final int MSG_CANCEL_ALL = 5;
        public static final int MSG_CLIENT_HANDLED_CONFIRMATION = 6;
        // Foreground activity tracker messages
        public static final int MSG_IN_FG = 10;
    }
}

