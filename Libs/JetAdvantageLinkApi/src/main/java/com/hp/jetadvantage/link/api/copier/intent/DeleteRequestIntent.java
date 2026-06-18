// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.copier.intent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.hp.jetadvantage.link.api.copier.JobCredentialsAttributes;
import com.hp.jetadvantage.link.common.Sdk;

/**
 * @hide The client should not know how the internal communication is done
 */
@SuppressLint("ParcelCreator")
public class DeleteRequestIntent extends BaseCopyRequestIntent<DeleteRequestIntent.IntentParams> {
    /**
     * Action to launch the Delete Activity.
     */
    public static final String ACTION = "com.hp.jetadvantage.link.intent.action.DELETE";
    private static final String EXTRA_JOB_CREDENTIALS_ATTRIBUTES = "jobCredentialsAttrExtra";

    /**
     * Constructor for DeleteRequestIntent.
     */
    public DeleteRequestIntent() {
        super(ACTION);
    }

    /**
     * Parameters for DeleteRequestIntent.
     */
    public static class IntentParams extends BaseCopyRequestIntent.IntentParams {
        private final JobCredentialsAttributes mJobCredentialsAttributes;
        /**
         * Creates IntentParams for DeleteRequestIntent.
         * @param reqId          of this job request
         */
        public IntentParams(final JobCredentialsAttributes jobCredentialsAttributes, final String reqId,
                final String packageName, final String applicationId,
                final String username, final String password, Integer apiLevel) {
            super(reqId, packageName, applicationId, username, password, apiLevel);

            mJobCredentialsAttributes = jobCredentialsAttributes;
        }

        public JobCredentialsAttributes getJobCredentialsAttributes() {
            return mJobCredentialsAttributes;
        }
    }

    /**
     * Retrieves the IntentParams of the DeleteRequestIntent.
     *
     * @param intent source intents
     * @return IntentParams
     */
    public static IntentParams getIntentParams(final Intent intent) {
        if (intent == null) {
            return null;
        }
        return getIntentParams(intent.getExtras());
    }

    public static IntentParams getIntentParams(final Bundle bundle) {
        if (bundle == null) {
            return null;
        }

        JobCredentialsAttributes jobCredentialsAttributes = null;
        String rid = null;
        String packageName = null;
        String applicationId = null;
        String username = null;
        String password = null;
        Integer apiLevel = Sdk.VERSION_LEVEL.ONE;

        if (bundle.containsKey(EXTRA_JOB_CREDENTIALS_ATTRIBUTES)) {
            jobCredentialsAttributes = bundle.getParcelable(EXTRA_JOB_CREDENTIALS_ATTRIBUTES);
        }

        if (bundle.containsKey(EXTRA_REQ_ID)) {
            rid = bundle.getString(EXTRA_REQ_ID);
        }

        if (bundle.containsKey(EXTRA_APP_PACKAGE_NAME)) {
            packageName = bundle.getString(EXTRA_APP_PACKAGE_NAME);
        }

        if (bundle.containsKey(EXTRA_APP_ID)) {
            applicationId = bundle.getString(EXTRA_APP_ID);
        }

        if (bundle.containsKey(EXTRA_USERNAME)) {
            username = bundle.getString(EXTRA_USERNAME);
        }

        if (bundle.containsKey(EXTRA_PASSWORD)) {
            password = bundle.getString(EXTRA_PASSWORD);
        }

        if (bundle.containsKey(EXTRA_API_LEVEL)) {
            apiLevel = bundle.getInt(EXTRA_API_LEVEL);
        }

        return new IntentParams(jobCredentialsAttributes, rid, packageName, applicationId, username, password, apiLevel);
    }

    /**
     * Retrieves the IntentParams of the DeleteRequestIntent.
     */
    @Override
    public IntentParams getIntentParams() {
        return getIntentParams(this);
    }


    /**
     * Puts the IntentParams to this DeleteRequestIntent.
     *
     * @param params intent params to store
     * @return Intent
     */
    @Override
    public Intent putIntentParams(final IntentParams params) {
        super.putIntentParams(params);
        putExtra(EXTRA_JOB_CREDENTIALS_ATTRIBUTES, params.mJobCredentialsAttributes);
        return this;
    }
}
