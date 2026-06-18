// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.statisticslet.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hp.jetadvantage.link.common.constants.CommonConstants;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;
import com.hp.jetadvantage.link.services.statisticslet.service.StatisticsNotificationObserverService;

import java.util.Objects;

public class BootCompletedReceiver extends BroadcastReceiver {
    private static final String TAG = "SDK/STATBC";
    private static int CNT = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), CommonConstants.BroadcastActions.WORKPATH_SERVICE_READY)) {
            SLog.i(TAG, "Received "+intent.getAction()+", starting server");

            try {
                SpsPermissionHelper.ensurePermission(context);
                StatisticsNotificationObserverService.start(context);
            } catch (Throwable e) {
                SLog.i(TAG, "Received " + intent.getAction() + ", but starting failed " + e.getMessage());
            }
        }
    }
}
