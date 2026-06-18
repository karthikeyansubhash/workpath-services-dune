// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.job.intent;

import android.annotation.SuppressLint;
import android.content.Intent;

import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.api.job.Joblet;
import com.hp.jetadvantage.link.api.job.JobletAttributes;
import com.hp.jetadvantage.link.common.intents.ISpsIntentWrapper;

/**
 * @hide The client should not know how the internal communication is done
 */
@SuppressLint("ParcelCreator")
public final class JobProgressRequestIntent extends Intent implements ISpsIntentWrapper<JobProgressRequestIntent.Params> {
    /**
     * Action for broadcasting the Job Progress monitoring request.
     */
    public static final String ACTION = "com.hp.jetadvantage.link.intent.action.JOBPROGRESS";

    private static final String KEY_TASK_ATTRIBUTES = "taskAttributes";
    /**
     * Caller package name, to check permissions
     */
    private static final String KEY_PACKAGE_NAME = "packageName";

    /**
     * Constructor for JobProgressRequestIntent.
     */
    public JobProgressRequestIntent() {
        super(ACTION);
    }

    /**
     * Parameters for JobProgressRequestIntent.
     */
    public static class Params {
        public final String mJobId;
        public final JobletAttributes mTaskAttributes;
        public final Intent mPendingIntent;
        public final String mPackageName;

        /**
         * Creates IntentParams for JobProgressRequestIntent
         *
         * @param jobId          int
         * @param taskAttributes JobletAttributes
         * @param pendingIntent  Intent
         * @param packageName    of the caller
         */
        @SuppressLint("RestrictedApi")
        public Params(final String jobId, final JobletAttributes taskAttributes,
                      final Intent pendingIntent, final String packageName) {
            Preconditions.checkNotNull(jobId, "Job ID cannot be null");
            Preconditions.checkArgument(jobId.length() > 0, "Invalid job Id");

            mJobId = jobId;
            mTaskAttributes = taskAttributes;
            mPendingIntent = pendingIntent;
            mPackageName = packageName;
        }
    }

    /**
     * Retrieves the IntentParams of the JobProgressRequestIntent.
     *
     * @param intent to retrieve parameters from
     * @return IntentParams
     */
    @SuppressLint("RestrictedApi")
    public static Params getIntentParams(final Intent intent) {
        Preconditions.checkNotNull(intent);

        String jobId = "";
        JobletAttributes taskAttributes = null;
        Intent pendingIntent = null;
        String packageName = null;

        if (intent.hasExtra(Joblet.Keys.KEY_JOBID)) {
            jobId = intent.getStringExtra(Joblet.Keys.KEY_JOBID);
        }

        if (intent.hasExtra(KEY_TASK_ATTRIBUTES)) {
            taskAttributes = intent.getParcelableExtra(KEY_TASK_ATTRIBUTES);
        }

        if (intent.hasExtra(Joblet.Keys.KEY_PENDING_INTENT)) {
            pendingIntent = intent.getParcelableExtra(Joblet.Keys.KEY_PENDING_INTENT);
        }

        if (intent.hasExtra(KEY_PACKAGE_NAME)) {
            packageName = intent.getStringExtra(KEY_PACKAGE_NAME);
        }

        return new Params(jobId, taskAttributes, pendingIntent, packageName);
    }

    /**
     * Retrieves the IntentParams of the JobProgressRequestIntent.
     */
    @Override
    public Params getIntentParams() {
        return getIntentParams(this);
    }

    /**
     * Puts the IntentParams to this JobProgressRequestIntent and retrieves the Intent.
     *
     * @param params to be put to the intent
     * @return Intent
     */
    @SuppressLint("RestrictedApi")
    @Override
    public Intent putIntentParams(final Params params) {
        Preconditions.checkNotNull(params);

        putExtra(Joblet.Keys.KEY_JOBID, params.mJobId);
        putExtra(KEY_TASK_ATTRIBUTES, params.mTaskAttributes);
        if (params.mPendingIntent != null) {
            putExtra(Joblet.Keys.KEY_PENDING_INTENT, params.mPendingIntent);
        }

        if (params.mPackageName != null) {
            putExtra(KEY_PACKAGE_NAME, params.mPackageName);
        }
        return this;
    }
}
