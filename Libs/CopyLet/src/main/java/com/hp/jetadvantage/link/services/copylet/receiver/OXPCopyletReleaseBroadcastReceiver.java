// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.copylet.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hp.jetadvantage.link.api.copier.Copylet;
import com.hp.jetadvantage.link.api.copier.intent.ReleaseRequestIntent;
import com.hp.jetadvantage.link.common.helper.SelectedPrinterHelper;
import com.hp.jetadvantage.link.common.model.ApiType;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;
import com.hp.jetadvantage.link.services.copylet.service.CopyJobIntentService;

public class OXPCopyletReleaseBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            SpsPermissionHelper.ensurePermission(context);

            final ReleaseRequestIntent.IntentParams params = ReleaseRequestIntent.getIntentParams(intent);

            if (SelectedPrinterHelper.get(context.getContentResolver()).getApiType() != ApiType.OXP) {
                return;
            }

            SLog.d(Copylet.TAG, "Received submitted release copy params: " + params);

            if (null != params) {
                CopyJobIntentService.startRelease(context, intent.getExtras());
            }

            if (isOrderedBroadcast()) {
                abortBroadcast();
            }
        } catch (Exception e) {
            SLog.e(Copylet.TAG, "Failed to parse copy release request, ignoring" + e.getMessage());
        }
    }
}
