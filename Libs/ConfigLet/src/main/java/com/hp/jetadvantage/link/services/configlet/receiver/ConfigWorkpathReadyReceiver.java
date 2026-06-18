/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.configlet.receiver;

import static com.hp.jetadvantage.link.common.constants.CommonConstants.BroadcastActions.WORKPATH_SERVICE_READY;
import static com.hp.jetadvantage.link.common.constants.LogConstants.TAG_CONFIG_SERVICE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;
import com.hp.jetadvantage.link.services.configlet.service.ConfigNotificationService;


public class ConfigWorkpathReadyReceiver extends BroadcastReceiver {
    private static final String TAG = TAG_CONFIG_SERVICE + "/BR";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            Log.e(TAG, "onReceive: null intent");
            return;
        }

        String action = intent.getAction();
        switch (action) {
            case WORKPATH_SERVICE_READY:
                processBootCompleted(context);
                break;
            default:
                Log.e(TAG, "onReceive: unknown action: " + intent.getAction());
                break;
        }
    }

    private void processBootCompleted(Context context) {
        Log.d(TAG, "processBootCompleted: ENTER ");
        try {
            SpsPermissionHelper.ensurePermission(context);
        } catch (Throwable e) {
            Log.e(TAG, "processBootCompleted: failed " + e.getMessage(), e);
        }

        ConfigNotificationService.start(context);
        Log.d(TAG, "processBootCompleted: EXIT ");
    }
}
