// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.storagelet.service;

import androidx.annotation.Nullable;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;

import com.hp.jetadvantage.link.api.massstorage.MassStoragelet;
import com.hp.jetadvantage.link.common.Platform;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.common.constants.CommonConstants;
import com.hp.jetadvantage.link.device.services.interfaces.IAppInstallUninstallCallback;
import com.hp.jetadvantage.link.services.common.notification.ServiceNotification;
import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;

public class MassstorageObserverService extends Service {
    private static final String TAG = "MASSSVC";

    private Handler mHandler;
    private HandlerThread mHandlerThread;

    private StorageServiceManager mStorageServiceManager;
    private IAppInstallUninstallCallback callback;

    public MassstorageObserverService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            try {
                SpsPermissionHelper.ensurePermission(getApplicationContext());
            } catch (Throwable throwable) {
                SLog.i(TAG, "Permission error mass svc. ignored.");
            }

            if (Platform.isPanel()) {
                MassStorageCallbackHandler callback = new MassStorageCallbackHandler(getApplicationContext());
                mStorageServiceManager = new StorageServiceManager(callback);

                mHandlerThread = new HandlerThread(MassStoragelet.TAG + ":" + getClass().getSimpleName(),
                        Process.THREAD_PRIORITY_BACKGROUND);
                mHandlerThread.start();
                mHandler = new SubscribeHandler(mHandlerThread.getLooper());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    try {
                        System.setProperty(CommonConstants.SYSTEM_SVC_FLAG, TAG);
                        SLog.d(TAG, "Created massstorage svc.");
                    } catch (Exception e) {
                    }
                    ServiceNotification.showNotification(this);
                }

                CommonConstants.sendBroadCastForBoot(getApplicationContext(),
                        CommonConstants.BroadcastActions.READY_STORAGELET);
            } else {
                SLog.d(TAG, "Not created massstorage svc.");
            }
        } catch (Throwable e) {
            SLog.d(TAG, "Not created massstorage svc because of permission." + e.getMessage());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            SpsPermissionHelper.ensurePermission(getApplicationContext());

            if (mHandler == null) {
                SLog.e(TAG, "Massstorage start failed.(ignored)");
                return START_STICKY;
            }

            mHandler.sendMessage(mHandler.obtainMessage(SubscribeHandler.MSG_START));
        } catch (Throwable e) {
            SLog.d(TAG, "Not started mass because of permission(start cmd)." + e.getMessage());
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mStorageServiceManager != null) {
            mStorageServiceManager.unregisterAppInstallUninstallCallback();
            mStorageServiceManager.unregisterSystemStateCallback();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void start(final Context context) {
        try {
            SpsPermissionHelper.ensurePermission(context);
            if (Platform.isPanel()) {
                SLog.d(TAG, "Start massstorage svc.");
                final Intent intent = new Intent(context, MassstorageObserverService.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intent);
                } else {
                    context.startService(intent);
                }
            } else {
                SLog.d(TAG, "Not started massstorage svc.");
            }
        } catch (Throwable e) {
            SLog.d(TAG, "Not created massstorage svc because of permission(start)." + e.getMessage());
        }
    }

    private class SubscribeHandler extends Handler {
        private static final int MSG_START = 1;

        SubscribeHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (Platform.isPanel()) {
                try {
                    switch (msg.what) {
                        case MSG_START:
                            SLog.d(TAG, "Start mass svc for event monitoring.");
                            mStorageServiceManager.registerAppInstallUninstallCallback();
                            mStorageServiceManager.registerSystemStateCallback();
                            break;
                        default:
                    }
                } catch (Exception e) {
                    SLog.e(TAG, "Failed to handle message " + msg.what, e);
                }
            }
        }
    }
}
