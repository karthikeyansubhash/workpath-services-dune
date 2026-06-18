// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.common.intents;

/**
 * Print job cancel intent
 */
public class PrintJobCancelIntent extends BaseJobCancelIntent {
    public static final String ACTION = "com.hp.jetadvantage.link.intent.action.BASIC_API_CANCEL_PRINT";

    public PrintJobCancelIntent(String jobId, String appPackageName) {
        super(ACTION, jobId, appPackageName);
    }
}
