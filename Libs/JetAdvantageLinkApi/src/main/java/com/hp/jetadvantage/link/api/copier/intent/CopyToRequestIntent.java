// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.copier.intent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.hp.jetadvantage.link.api.copier.CopyAttributes;
import com.hp.jetadvantage.link.api.copier.CopyletAttributes;
import com.hp.jetadvantage.link.common.Sdk;

/**
 * @hide The client should not know how the internal communication is done
 */
@SuppressLint("ParcelCreator")
public class CopyToRequestIntent extends BaseCopyRequestIntent<CopyToRequestIntent.IntentParams> {
    /**
     * Action to launch the CopyTo Activity.
     */
    public static final String ACTION = "com.hp.jetadvantage.link.intent.action.COPY";
    private static final String EXTRA_COPY_ATTRIBUTES = "copyAttrExtra";
    private static final String EXTRA_TASK_ATTRIBUTES = "taskAttrExtra";

    /**
     * Constructor for CopyToRequestIntent.
     */
    public CopyToRequestIntent() {
        super(ACTION);
    }

    /**
     * Parameters for CopyToRequestIntent.
     */
    public static class IntentParams extends BaseCopyRequestIntent.IntentParams {
        private final CopyAttributes mCopyAttributes;
        private final CopyletAttributes mTaskAttributes;

        /**
         * Creates IntentParams for CopyToRequestIntent.
         *  @param attributes     {@link CopyAttributes}
         * @param taskAttributes {@link CopyletAttributes}
         * @param reqId          of this job request
         */
        public IntentParams(final CopyAttributes attributes, final CopyletAttributes taskAttributes,
                            final String reqId, final String packageName, final String applicationId,
                final String username, final String password, Integer apiLevel) {
            super(reqId, packageName, applicationId, username, password, apiLevel);

            mCopyAttributes = attributes;

            if (taskAttributes == null) {
                mTaskAttributes = new CopyletAttributes.Builder().build();
            } else {
                mTaskAttributes = taskAttributes;
            }
        }

        public CopyAttributes getCopyAttributes() {
            return mCopyAttributes;
        }

        public CopyletAttributes getTaskAttributes() {
            return mTaskAttributes;
        }
    }

    /**
     * Retrieves the IntentParams of the CopyToRequestIntent.
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

        CopyAttributes sa = null;
        CopyletAttributes ta = null;
        String rid = null;
        String packageName = null;
        String applicationId = null;
        String username = null;
        String password = null;
        Integer apiLevel = Sdk.VERSION_LEVEL.ONE;

        bundle.setClassLoader(CopyAttributes.class.getClassLoader());

        if (bundle.containsKey(EXTRA_COPY_ATTRIBUTES)) {
            sa = bundle.getParcelable(EXTRA_COPY_ATTRIBUTES);
        }

        if (bundle.containsKey(EXTRA_TASK_ATTRIBUTES)) {
            bundle.setClassLoader(CopyletAttributes.class.getClassLoader());
            ta = bundle.getParcelable(EXTRA_TASK_ATTRIBUTES);
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

        return new IntentParams(sa, ta, rid, packageName, applicationId, username, password, apiLevel);
    }

    /**
     * Retrieves the IntentParams of the CopyToRequestIntent.
     */
    @Override
    public IntentParams getIntentParams() {
        return getIntentParams(this);
    }

    /**
     * Puts the IntentParams to this CopyToRequestIntent.
     *
     * @param params intent params to store
     * @return Intent
     */
    @Override
    public Intent putIntentParams(final IntentParams params) {
        super.putIntentParams(params);
        putExtra(EXTRA_COPY_ATTRIBUTES, params.mCopyAttributes);
        putExtra(EXTRA_TASK_ATTRIBUTES, params.mTaskAttributes);
        return this;
    }
}
