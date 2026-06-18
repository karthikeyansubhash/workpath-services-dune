/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.common.intents;

import android.content.Intent;

/**
 * abstract Intent to cancel job.
 * It should be handled only by Xlets and used to start xlets services.
 */
public abstract class BaseJobCancelIntent extends Intent {

    /**
     * Mandatory tags
     */
    private static final String EXTRA_JOB_ID = "JOB_ID";
    private static final String EXTRA_APP_PACKAGE_NAME = "APP_PACKAGE_NAME";

    /**
     * Constructor
     */
    public BaseJobCancelIntent(String action, String jobId, String appPackageName) {
        super(action);
        this.putExtra(EXTRA_JOB_ID, jobId);
        this.putExtra(EXTRA_APP_PACKAGE_NAME, appPackageName);
    }

    public static String getJobId(Intent intent) {
        if (intent == null) {
            return null;
        }
        return intent.getStringExtra(EXTRA_JOB_ID);
    }

    public static String getAppPackageName(Intent intent) {
        if (intent == null) {
            return null;
        }
        return intent.getStringExtra(EXTRA_APP_PACKAGE_NAME);
    }
}