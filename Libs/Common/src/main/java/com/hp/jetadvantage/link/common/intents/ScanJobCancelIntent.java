/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.common.intents;

/**
 * Scan job cancel intent
 */
public class ScanJobCancelIntent extends BaseJobCancelIntent {
    public static final String ACTION = "com.hp.jetadvantage.link.intent.action.BASIC_API_CANCEL_SCAN";

    public ScanJobCancelIntent(String jobId, String appPackageName) {
        super(ACTION, jobId, appPackageName);
    }
}
