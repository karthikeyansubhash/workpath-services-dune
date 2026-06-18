// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hp.jetadvantage.link.api.printer.Printlet;
import com.hp.jetadvantage.link.api.printer.PrintletAttributes;
import com.hp.jetadvantage.link.api.printer.intent.PrintRequestIntent;
import com.hp.jetadvantage.link.common.helper.SelectedPrinterHelper;
import com.hp.jetadvantage.link.common.model.ApiType;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;
import com.hp.jetadvantage.link.services.printlet.service.OXPCreatePrintSpoolerIntentService;
import com.hp.jetadvantage.link.services.settingsui.SettingsUIActivity;

public class OXPPrintletBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
            try {
            SpsPermissionHelper.ensurePermission(context);

            final PrintRequestIntent.IntentParams params = PrintRequestIntent.getIntentParams(intent);

            if (params == null || params.getForcedApi() != ApiType.OXP) {
                if (SelectedPrinterHelper.get(context.getContentResolver()).getApiType() != ApiType.OXP) {
                    return;
                }
            }

            SLog.d(Printlet.TAG, "Received submitted print params: " + params);

            if (params != null) {
                final PrintletAttributes taskAttributes = params.getTaskAttributes();

                if (null != taskAttributes) {
                    // Check if caller package got proper permissions
                    if (params.getTaskAttributes().getShowSettingsUI()) {
                        SLog.d(Printlet.TAG, "Start settings activity.");

                        final Intent settingsLaunch = new Intent(context, SettingsUIActivity.class);

                        settingsLaunch.putExtras(intent);
                        settingsLaunch.setAction(intent.getAction());
                        settingsLaunch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(settingsLaunch);
                    } else {
                        OXPCreatePrintSpoolerIntentService.start(context, intent.getExtras());
                    }
                }
            }

            if (isOrderedBroadcast()) {
                abortBroadcast();
            }
        } catch (Exception e) {
            SLog.e(Printlet.TAG, "Failed to parse print release request, ignoring" + e.getMessage());
        }
    }
}
