// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api;

import android.content.Context;

import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.common.annotation.CommonApi;

/**
 * <p>Interface ILetObserver is a generic interface to monitor the status of events changes associated with the job</p>
 *
 * @since API 1
 */
@CommonApi
public interface ILetObserver {

    /**
     * Keys used for transporting data. These are used to provide data to the client.
     *
     * @hide The client should not need to know about the keys used for transporting data
     * @since API 1
     */
    @CommonApi
    final class Keys {
        private Keys() {
        }

        /**
         * Key to retrieve the resource id.
         *
         * @hide The client should not need to know this key
         * @since API 1
         */
        public static final String KEY_RID = "rid";

        /**
         * Key to retrieve the state.
         *
         * @hide The client should not need to know this key
         * @since API 1
         */
        public static final String KEY_STATE = "state";

        /**
         * Key to retrieve the job info.
         *
         * @hide The client should not need to know this key
         * @since API 1
         */
        public static final String KEY_JOB_INFO = "jobInfo";

        /**
         * Internal value for credentials dialog
         * @hide
         */
        public static final String KEY_REQUEST_INTENT = "requestIntent";
    }

    /**
     * @hide The state transportation data
     * @since API 1
     */
    @CommonApi
    final class State {
        private State() {
        }

        public static final String CANCEL = "cancel";
        public static final String COMPLETE = "complete";
        public static final String PROGRESS = "progress";
        public static final String CONFIRMATION = "confirmation";
        public static final String CREDENTIALS = "credentials";
        public static final String FAIL = "fail";
        public static final String UNKNOWN = "unknown";
    }

    /**
     * <p>Registers the observer to start monitoring events.</p>
     *
     * @param context The Context in which the application is running.
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    void register(final Context context);

    /**
     * <p>Unregisters the observer to stop monitoring events.</p>
     *
     * @param context The Context in which the application is running.
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    void unregister(final Context context);

    /**
     * <p>Called to notify the client that the job associated with resourceId was canceled by a user or a program.</p>
     *
     * @param rid The triggered resourceId
     * @since API 1
     */
    @SuppressWarnings({"unused", "EmptyMethod"})
    void onCancel(final String rid);

    /**
     * <p>Called to notify the client that the job completed successfully.</p>
     *
     * @param rid     The triggered resourceId
     * @param jobInfo The object of job details when it's completed
     * @since API 1
     */
    @SuppressWarnings({"unused", "EmptyMethod"})
    void onComplete(final String rid, final JobInfo jobInfo);

    /**
     * <p>Called multiple times to notify the client that the job has been in progress depending on the nature of the job status.</p>
     *
     * @param rid     The triggered resourceId
     * @param jobInfo The object of job details while progressing a job
     * @since API 1
     */
    @SuppressWarnings({"unused", "EmptyMethod"})
    void onProgress(final String rid, final JobInfo jobInfo);

    /**
     * <p>Called to notify the client that the job failed to complete successfully.</p>
     *
     * @param rid    The triggered resourceId
     * @param result The details with error code and reason
     * @since API 1
     */
    @SuppressWarnings({"unused", "EmptyMethod"})
    void onFail(final String rid, final Result result);
}
