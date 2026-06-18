// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.deviceeventslet.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.constants.CommonConstants;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.deviceeventslet.provider.DeviceEventsLetContentProvider;
import com.hp.workpath.api.Result;
import com.hp.workpath.api.deviceevents.DeviceEventslet;

public class DeviceEventReceiver extends BroadcastReceiver {
    private static final String TAG = "SDK/DEVEvOb";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();
            if (CommonConstants.BroadcastActions.WORKPATH_SERVICE_READY.equals(action)) {
                SLog.i(TAG, "Received " + intent.getAction() + ", starting service");

                Bundle extras = new Bundle();
                String packageName = context.getPackageName();
                extras.putString(DeviceEventslet.Keys.PACKAGE_NAME, packageName);
                extras.putInt(DeviceEventslet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

                Bundle returnBundle = context.getContentResolver()
                        .call(DeviceEventslet.CONTENT_URI, DeviceEventsLetContentProvider.INIT_DEVICE_EVENTS, null, extras);

                Result result = new Result();
                Result.parse(returnBundle, result);
            }
        }
    }
}
