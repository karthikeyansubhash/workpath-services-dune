// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.accesslet.receiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.accesslet.service.AuthenticationObserverService;
import com.hp.jetadvantage.link.common.constants.PackageContract;
import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;

public class AppInstalledEventReceiver extends BroadcastReceiver {
    private static final String TAG = "SDK/AUTEv";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (context != null && intent != null && intent.getAction() != null){
            if (PackageContract.Intent.ACTION_PACKAGE_INSTALLED.equals(intent.getAction())
                    || PackageContract.Intent.ACTION_PACKAGE_UPDATED.equals(intent.getAction())) {
                SLog.i(TAG, intent.getAction() + " received");
                try {
                    SpsPermissionHelper.ensurePermission(context);

                    AuthenticationObserverService.start(context);
                } catch (Throwable e) {
                    SLog.i(TAG, "Received " + intent.getAction() + ", but starting failed " + e.getMessage());
                }
            }
        }
    }
}
