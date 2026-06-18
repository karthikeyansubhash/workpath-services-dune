// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.accesslet.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hp.jetadvantage.link.common.constants.CommonConstants;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.accesslet.service.AuthenticationObserverService;
import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;

import java.util.Objects;

public class BootCompletedReceiver extends BroadcastReceiver {
    private static final String TAG = "SDK/AUTBC";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), CommonConstants.BroadcastActions.WORKPATH_SERVICE_READY)) {
            SLog.i(TAG, "Received " + intent.getAction() + ", starting server");

            try {
                SpsPermissionHelper.ensurePermission(context);
                AuthenticationObserverService.start(context);
            } catch (Throwable e) {
                SLog.i(TAG, "Received " + intent.getAction() + ", but starting failed " + e.getMessage());
            }
        }
    }
}
