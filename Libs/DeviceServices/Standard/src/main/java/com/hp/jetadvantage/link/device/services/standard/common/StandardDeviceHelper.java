/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */

package com.hp.jetadvantage.link.device.services.standard.common;

import android.content.Context;

import androidx.work.Configuration;
import androidx.work.WorkManager;

public class StandardDeviceHelper {
    public static WorkManager getWorkManager(Context context) {
        WorkManager workManager;
        try {
            workManager = WorkManager.getInstance(context);
        } catch (Throwable t) {
            workManager = null;
        }
        if (workManager == null) {
            try {
                WorkManager.initialize(context, new Configuration.Builder().build());
                workManager = WorkManager.getInstance(context);
            } catch (Throwable ignored) {
            }
        }
        return workManager;
    }
}
