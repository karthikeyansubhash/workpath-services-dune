// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.copier.intent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.hp.jetadvantage.link.api.copier.StoredJobAttributes;
import com.hp.jetadvantage.link.common.Sdk;

/**
 * @hide The client should not know how the internal communication is done
 */
@SuppressLint("ParcelCreator")
public class ReleaseRequestIntent extends BaseCopyRequestIntent<ReleaseRequestIntent.IntentParams> {
    /**
     * Action to launch the CopyTo Activity.
     */
    private static final String ACTION = "com.hp.jetadvantage.link.intent.action.RELEASE_COPY";
    private static final String EXTRA_JOB_ATTRIBUTES = "jobAttrExtra";

    /**
     * Constructor for ReleaseRequestIntent.
     */
    public ReleaseRequestIntent() {
        super(ACTION);
    }

    /**
     * Parameters for ReleaseRequestIntent.
     */
    public static class IntentParams extends BaseCopyRequestIntent.IntentParams {
        private final StoredJobAttributes mStoredJobAttributes;

        /**
         * Creates IntentParams for ReleaseRequestIntent.
         *
         * @param attributes {@link StoredJobAttributes}
         * @param reqId      of this job request
         */
        public IntentParams(final StoredJobAttributes attributes, final String reqId, final String packageName,
                            final String applicationId, final String username, final String password, Integer apiLevel) {
            super(reqId, packageName, applicationId, username, password, apiLevel);

            mStoredJobAttributes = attributes;
        }

        public StoredJobAttributes getStoredJobAttributes() {
            return mStoredJobAttributes;
        }
    }

    /**
     * Retrieves the IntentParams of the ReleaseRequestIntent.
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

        StoredJobAttributes storedJobAttributes = null;
        String rid = null;
        String packageName = null;
        String applicationId = null;
        String username = null;
        String password = null;
        Integer apiLevel = Sdk.VERSION_LEVEL.ONE;

        bundle.setClassLoader(StoredJobAttributes.class.getClassLoader());

        if (bundle.containsKey(EXTRA_JOB_ATTRIBUTES)) {
            storedJobAttributes = bundle.getParcelable(EXTRA_JOB_ATTRIBUTES);
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

        return new IntentParams(storedJobAttributes, rid, packageName, applicationId, username, password, apiLevel);
    }

    /**
     * Retrieves the IntentParams of the ReleaseRequestIntent.
     */
    @Override
    public IntentParams getIntentParams() {
        return getIntentParams(this);
    }

    /**
     * Puts the IntentParams to this ReleaseRequestIntent.
     *
     * @param params intent params to store
     * @return Intent
     */
    @Override
    public Intent putIntentParams(final IntentParams params) {
        super.putIntentParams(params);
        putExtra(EXTRA_JOB_ATTRIBUTES, params.mStoredJobAttributes);
        return this;
    }
}
