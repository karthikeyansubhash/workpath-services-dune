/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */

package com.hp.jetadvantage.link.device.services.standard.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hp.jetadvantage.link.common.constants.PackageContract;
import com.hp.jetadvantage.link.device.services.interfaces.IAppInstallUninstallCallback;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceManagementService;

import java.util.List;

/**
 * BroadcastReceiver for app install/uninstall events.
 */
public class AppInstallEventReceiver extends BroadcastReceiver {
    private static final String TAG = Constants.TAG + "/B/AppInstall";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        String suffix = intent.getAction() + ", " +
                intent.getStringExtra(PackageContract.Intent.EXTRA_SOLUTION_ID) + ", " +
                intent.getStringExtra(PackageContract.Intent.EXTRA_PACKAGE);
        Log.d(TAG, "onReceive :[AppInstall] ENTER - " + suffix);
        if (PackageContract.Intent.ACTION_PACKAGE_UNINSTALLED.equals(intent.getAction()) ||
                PackageContract.Intent.ACTION_PACKAGE_UPDATED.equals(intent.getAction())) {
            Log.d(TAG, "onReceive : clear cache for uninstalled solution token ");
            StandardDeviceManagementService.getInstance().clearSolutionTokenCache(intent.getStringExtra(PackageContract.Intent.EXTRA_SOLUTION_ID));
        }

        notifyToAppInstallUninstallCallbacks(context, intent);
        Log.d(TAG, "onReceive :[AppInstall] EXIT - " + suffix);
    }

    private void notifyToAppInstallUninstallCallbacks(Context context, Intent intent) {
        List<IAppInstallUninstallCallback> callbacks = null;
        String packageName = intent.getStringExtra(PackageContract.Intent.EXTRA_PACKAGE);

        if (packageName == null) return;

        switch (intent.getAction()) {
            case PackageContract.Intent.ACTION_PACKAGE_INSTALLED:
                callbacks = StandardDeviceManagementService.getInstance().getAppInstallCallbacks(packageName);
                break;
            case PackageContract.Intent.ACTION_PACKAGE_UNINSTALLED:
                callbacks = StandardDeviceManagementService.getInstance().getAppUnInstallCallbacks(packageName);
                break;
        }

        if (callbacks == null) return;

        for (IAppInstallUninstallCallback callback : callbacks) {
            if (callback != null) {
                Log.d(TAG, "onReceive :[AppInstall] callback.onReceive ENTER");
                callback.onReceive(context, intent);
                Log.d(TAG, "onReceive :[AppInstall] callback.onReceive EXIT");
            }
        }
    }
}
