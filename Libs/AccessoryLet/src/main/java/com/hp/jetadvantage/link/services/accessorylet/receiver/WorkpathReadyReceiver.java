// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.accessorylet.receiver;

import static com.hp.jetadvantage.link.common.constants.CommonConstants.BroadcastActions.WORKPATH_SERVICE_READY;
import static com.hp.jetadvantage.link.common.constants.LogConstants.TAG_ACCESSORY_SERVICE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hp.jetadvantage.link.common.constants.CommonConstants;
import com.hp.jetadvantage.link.services.accessorylet.service.AccessoryNotificationService;
import com.hp.jetadvantage.link.services.accessorylet.util.AccessoryUtility;
import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;

public class WorkpathReadyReceiver extends BroadcastReceiver {
    private static final String TAG = TAG_ACCESSORY_SERVICE + "/BR";

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
            AccessoryUtility.setSystemProperty(CommonConstants.SYSTEM_SVC_FLAG, TAG, "AccessoryService started");
        } catch (Throwable e) {
            Log.e(TAG, "processBootCompleted: failed " + e.getMessage(), e);
        }

        AccessoryNotificationService.start(context);
        CommonConstants.sendBroadCastForBoot(context, CommonConstants.BroadcastActions.READY_ACCESSORYLET);
        Log.d(TAG, "processBootCompleted: EXIT ");
    }
}
