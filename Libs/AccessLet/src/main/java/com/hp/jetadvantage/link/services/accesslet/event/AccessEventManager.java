// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.accesslet.event;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hp.jetadvantage.link.common.utils.SLog;

public class AccessEventManager {
    private static final String TAG = "AccessEV";

    public static void sendEventBroadcast(final Context context, final String action, final Bundle extras, final String permission) {
        SLog.d(TAG, "SendEventBroadcast for " + action);
        Intent intent = new Intent(action);
        intent.putExtras(extras);
        context.sendBroadcast(intent, permission);
    }
}
