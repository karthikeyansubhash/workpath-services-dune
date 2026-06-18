// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services;

import android.content.IntentFilter;
import android.os.Build;
import android.os.StrictMode;
import android.preference.PreferenceManager;

import androidx.multidex.BuildConfig;
import androidx.multidex.MultiDexApplication;

import com.hp.jetadvantage.link.common.Platform;
import com.hp.jetadvantage.link.common.utils.SLog;
// TODO : [DUNE-169977] revisit later
/*
import com.hp.jetadvantage.link.services.accesslet.receiver.AppInstalledEventReceiver;
import com.hp.jetadvantage.link.services.accesslet.receiver.StartAuthObserverReceiver;
import com.hp.jetadvantage.link.services.configlet.receiver.PacManConfigChangedBroadcastReceiver;
import com.hp.jetadvantage.link.services.deviceeventslet.receiver.StartDeviceEventObserverReceiver;
import com.hp.jetadvantage.link.services.joblet.receiver.JobletReceiver;
import com.hp.jetadvantage.link.services.printlet.receiver.OXPPrintletBroadcastReceiver;
import com.hp.jetadvantage.link.services.scanlet.receiver.ScanletBroadcastReceiver;
import com.hp.jetadvantage.link.services.statisticslet.receiver.StartStatisticsObserverReceiver;
import com.hp.jetadvantage.link.services.storagelet.receiver.AppUnInstalledEventReceiver;
import com.hp.jetadvantage.link.services.storagelet.receiver.StartMassStroageEventReceiver;
 */
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceManagementService;

/**
 * Sample application to enable strict mode for execution
 */
public class JetAdvantageLinkServicesApplication extends MultiDexApplication {

    private static final boolean ENABLE_STRICT_MODE = BuildConfig.DEBUG && false;
    private final String TAG = "[SDK]/M";

    @Override
    public void onCreate() {
        SLog.d(TAG, "Application start");

        if (!Platform.isPanel()) {
            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                    .putBoolean(getPackageName() + "#oxpd_legacy_mode", true).apply();
        }

        if (ENABLE_STRICT_MODE) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }

        StandardDeviceManagementService.getInstance().setApplicationContext(getApplicationContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //TODO repeat

// TODO : [DUNE-169977] revisit later
/*
            // Removed for processing only by Auth process
//            filter = new IntentFilter("com.hp.jetadvantage.link.SWITCH");
//            this.registerReceiver(new SwitchEventReceiver(), filter);

            filter = new IntentFilter("com.hp.jetadvantage.link.intent.action.JOBPROGRESS");
            filter.addAction("com.hp.jetadvantage.link.intent.action.ALL_JOBPROGRESS");
            filter.addAction("com.hp.jetadvantage.link.intent.action.ALL_JOBPROGRESS_STOP");
            this.registerReceiver(new JobletReceiver(), filter);

            filter = new IntentFilter("com.hp.jetadvantage.link.intent.action.SCANTO");
            this.registerReceiver(new ScanletBroadcastReceiver(), filter);

            filter = new IntentFilter("android.intent.action.MY_PACKAGE_REPLACED");
            filter.addAction("com.hp.workpath.system.WORKPATH_SERVICE_READY");
            this.registerReceiver(new StartMassStroageEventReceiver(), filter);

            filter = new IntentFilter("com.hp.jetadvantage.link.services.deviceeventslet.START_DEVICE_EVENT_OBSERVER");
            filter.addAction("android.intent.action.MY_PACKAGE_REPLACED");
            this.registerReceiver(new StartDeviceEventObserverReceiver(), filter);
*/
        }

        super.onCreate();
    }

}
