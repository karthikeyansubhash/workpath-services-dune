// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.scanlet.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hp.jetadvantage.link.api.scanner.Scanlet;
import com.hp.jetadvantage.link.api.scanner.ScanletAttributes;
import com.hp.jetadvantage.link.api.scanner.intent.ScanToRequestIntent;
import com.hp.jetadvantage.link.common.helper.SelectedPrinterHelper;
import com.hp.jetadvantage.link.common.model.ApiType;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;
import com.hp.jetadvantage.link.services.scanlet.service.ScanJobIntentService;
import com.hp.jetadvantage.link.services.settingsui.SettingsUIActivity;

public class ScanletBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            SpsPermissionHelper.ensurePermission(context);

            final ScanToRequestIntent.IntentParams params = ScanToRequestIntent.getIntentParams(intent);

            if (params == null || params.getForcedApi() != ApiType.OXP) {
                if (SelectedPrinterHelper.get(context.getContentResolver()).getApiType() != ApiType.OXP) {
                    return;
                }
            }

            SLog.d(Scanlet.TAG, "Received submitted scan params: " + params);

            if (null != params) {
                final ScanletAttributes taskAttributes = params.getTaskAttributes();

                if (null != taskAttributes) {
                    if (taskAttributes.getShowSettingsUI()) {
                        SLog.d(Scanlet.TAG, "Start settings activity.");

                        final Intent settingsLaunch = new Intent(context, SettingsUIActivity.class);

                        settingsLaunch.putExtras(intent);
                        settingsLaunch.setAction(intent.getAction());
                        settingsLaunch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(settingsLaunch);
                    } else {
                        ScanJobIntentService.start(context, intent.getExtras(), ScanJobIntentService.class);
                    }
                }
            }

            if (isOrderedBroadcast()) {
                abortBroadcast();
            }
        } catch (Exception e) {
            // catch any exception (parcel exception, NPE etc)
            SLog.e(Scanlet.TAG, "Failed to parse scan request, ignoring" + e.getMessage());
        }
    }
}
