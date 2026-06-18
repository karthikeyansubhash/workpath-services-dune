// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.copier.intent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.intents.ISpsIntentWrapper;

/**
 * @hide The client should not know how the internal communication is done
 */
public class BaseCopyRequestIntent<T extends BaseCopyRequestIntent.IntentParams> extends Intent implements ISpsIntentWrapper<T> {
    static final String EXTRA_REQ_ID = "reqIDExtra";
    static final String EXTRA_APP_PACKAGE_NAME = "appPackageName";
    static final String EXTRA_APP_ID = "applicationId";
    static final String EXTRA_USERNAME = "username";
    static final String EXTRA_PASSWORD = "password";
    static final String EXTRA_API_LEVEL = "apiLevel";

    BaseCopyRequestIntent(String action) {
        super(action);
    }

    /**
     * Parameters for CopyToRequestIntent.
     */
    public static class IntentParams {
        private final String mReqId;
        private final String mPackageName;
        private final String mApplicationId;
        private final String mUsername;
        private final String mPassword;
        private final Integer mApiLevel;

        /**
         * Creates IntentParams for CopyToRequestIntent.
         * @param reqId          of this job request
         */
        public IntentParams(final String reqId, final String packageName, final String applicationId,
                final String username, final String password, Integer apiLevel) {
            mReqId = reqId;
            mPackageName = packageName;
            mApplicationId = applicationId;
            mUsername = username;
            mPassword = password;
            mApiLevel = apiLevel;
        }

        public String getReqId() {
            return mReqId;
        }

        public String getPackageName() {
            return mPackageName;
        }

        public String getApplicationId() {
            return mApplicationId;
        }

        public String getUsername() {
            return mUsername;
        }

        public String getPassword() {
            return mPassword;
        }

        public Integer getApiLevel() {
            return mApiLevel;
        }
    }

    /**
     * Retrieves the IntentParams of the ScanToRequestIntent.
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

        String rid = null;
        String packageName = null;
        String applicationId = null;
        String username = null;
        String password = null;
        Integer apiLevel = Sdk.VERSION_LEVEL.ONE;

        bundle.setClassLoader(BaseCopyRequestIntent.class.getClassLoader());

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

        return new IntentParams(rid, packageName, applicationId, username, password, apiLevel);
    }

    /**
     * Retrieves the IntentParams of the ScanToRequestIntent.
     */
    @Override
    public T getIntentParams() {
        return (T) getIntentParams(this);
    }

    /**
     * Puts the IntentParams to this ScanToRequestIntent.
     *
     * @param params intent params to store
     * @return Intent
     */
    @SuppressLint("RestrictedApi")
    @Override
    public Intent putIntentParams(final T params) {
        Preconditions.checkNotNull(params);
        putExtra(EXTRA_REQ_ID, params.getReqId());
        putExtra(EXTRA_APP_PACKAGE_NAME, params.getPackageName());
        putExtra(EXTRA_APP_ID, params.getApplicationId());
        putExtra(EXTRA_USERNAME, params.getUsername());
        putExtra(EXTRA_PASSWORD, params.getPassword());
        if (params.getApiLevel() != null) {
            putExtra(EXTRA_API_LEVEL, params.getApiLevel());
        }
        return this;
    }
}
