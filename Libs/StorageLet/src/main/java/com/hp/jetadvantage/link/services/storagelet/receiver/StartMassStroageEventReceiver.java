// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.storagelet.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.common.constants.CommonConstants;
import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;
import com.hp.jetadvantage.link.services.storagelet.service.MassstorageObserverService;

public class StartMassStroageEventReceiver extends BroadcastReceiver {
    private static final String TAG = "SDK/MASSBC";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null
                && CommonConstants.BroadcastActions.WORKPATH_SERVICE_READY.equals(intent.getAction())) {
            Log.i(TAG, "Received "+intent.getAction()+", starting server");
            try {
                SpsPermissionHelper.ensurePermission(context);
                MassstorageObserverService.start(context);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    try {
                        System.setProperty(CommonConstants.SYSTEM_SVC_FLAG, TAG);
                        SLog.d(TAG, "Created mass receiver.");
                    } catch (Exception e) {}
                }
            } catch (Exception e) {
                Log.i(TAG, "Received " + intent.getAction() + ", but starting failed " + e.getMessage());
            }
        }
    }
}
