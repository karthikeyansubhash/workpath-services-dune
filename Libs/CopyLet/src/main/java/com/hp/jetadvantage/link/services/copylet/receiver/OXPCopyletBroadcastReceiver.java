// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.copylet.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hp.jetadvantage.link.api.copier.Copylet;
import com.hp.jetadvantage.link.api.copier.CopyletAttributes;
import com.hp.jetadvantage.link.api.copier.intent.CopyToRequestIntent;
import com.hp.jetadvantage.link.common.helper.SelectedPrinterHelper;
import com.hp.jetadvantage.link.common.model.ApiType;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;
import com.hp.jetadvantage.link.services.copylet.service.CopyJobIntentService;
import com.hp.jetadvantage.link.services.settingsui.SettingsUIActivity;

public class OXPCopyletBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            SpsPermissionHelper.ensurePermission(context);

            final CopyToRequestIntent.IntentParams params = CopyToRequestIntent.getIntentParams(intent);

            if (SelectedPrinterHelper.get(context.getContentResolver()).getApiType() != ApiType.OXP) {
                return;
            }

            SLog.d(Copylet.TAG, "Received submitted copy params: " + params);

            if (null != params) {
                final CopyletAttributes taskAttributes = params.getTaskAttributes();

                if (null != taskAttributes) {
                    if (taskAttributes.getShowSettingsUI()) {
                        SLog.d(Copylet.TAG, "Start settings activity.");

                        final Intent settingsLaunch = new Intent(context, SettingsUIActivity.class);

                        settingsLaunch.putExtras(intent);
                        settingsLaunch.setAction(intent.getAction());
                        settingsLaunch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(settingsLaunch);
                    } else {
                        CopyJobIntentService.startCopy(context, intent.getExtras());
                    }
                }
            }

            if (isOrderedBroadcast()) {
                abortBroadcast();
            }
        } catch (Exception e) {
            SLog.e(Copylet.TAG, "Failed to parse copy submit request, ignoring" + e.getMessage(), e);
        }
    }
}
