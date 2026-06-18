/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.configlet.service;

import static com.hp.jetadvantage.link.common.constants.LogConstants.TAG_CONFIG_SERVICE;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.hp.ext.types.solutionManager.NotificationType;
import com.hp.ext.types.solutionManager.SolutionNotification;
import com.hp.jetadvantage.link.api.config.Configlet;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceSolutionManager;
import com.hp.jetadvantage.link.device.services.interfaces.IE2PayloadCallback;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceSolutionManager;
import com.hp.jetadvantage.link.services.common.notification.ServiceNotification;
import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;
import com.hp.jetadvantage.link.services.configlet.provider.ConfigLetContentProvider;

public class ConfigNotificationService extends Service {
    private static final String TAG = TAG_CONFIG_SERVICE + "/SVC";
    private IDeviceSolutionManager mDeviceSolutionManager;

    public static void start(final Context context) {
        try {
            SpsPermissionHelper.ensureSystemPermission(context);

            Log.i(TAG, "start: Starting ConfigNotificationService");
            final Intent intent = new Intent(context.getApplicationContext(), ConfigNotificationService.class);
            context.startForegroundService(intent);

        } catch (Throwable e) {
            Log.e(TAG, "Failed to start ConfigNotificationService: " + e.getMessage(), e);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ENTER - ConfigNotificationService");
        super.onCreate();

        try {
            SpsPermissionHelper.ensureSystemPermission(getApplicationContext());
        } catch (Throwable throwable) {
            Log.i(TAG, "onCreate: ConfigNotificationService Permission error", throwable);
        }

        try {
            mDeviceSolutionManager = new StandardDeviceSolutionManager();
            registerNotificationCallbacks(mDeviceSolutionManager);

            ServiceNotification.showNotification(this);
        } catch (Throwable e) {
            Log.e(TAG, "onCreate: Failed to initialize ConfigNotificationService: " + e.getMessage(), e);
        }
        Log.d(TAG, "onCreate: EXIT - ConfigNotificationService");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ConfigNotificationService stopped");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ConfigNotificationService processing start command");
        return START_STICKY;
    }

    private void registerNotificationCallbacks(IDeviceSolutionManager deviceSolutionManager) {
        IE2PayloadCallback<SolutionNotification> callback =
                (appPackageId, notification) -> processSolutionNotification(appPackageId, notification);
        deviceSolutionManager.registerNotificationCallback(callback);
    }

    private void processSolutionNotification(String appPackageId, SolutionNotification notification) {
        try {
            if (appPackageId == null || notification == null) {
                Log.e(TAG, "processSolutionNotification: Invalid notification or appPackageId");
                return;
            }

            Log.d(TAG, "processSolutionNotification: Received notification for appPackageId: " + appPackageId);
            if (NotificationType.NtConfigurationModified.getValue().equals(notification.getNotificationType().getValue())) {
                if (ConfigLetContentProvider.isModifiedFrequently(appPackageId, true)) {
                    Log.d(TAG, "processSolutionNotification: Changed by App, skipping broadcast");
                } else {
                    Intent configChangeIntent = new Intent(Configlet.CONFIG_CHANGE_ACTION);
                    configChangeIntent.setPackage(appPackageId);
                    sendBroadcast(configChangeIntent);
                    Log.d(TAG, "processSolutionNotification: sendBroadcast CONFIG_CHANGE_ACTION to " + appPackageId);
                }
            } else {
                Log.d(TAG,
                        "processSolutionNotification: Skipped notification of type: " + notification.getNotificationType());
            }
        } catch (Exception e) {
            Log.e(TAG, "processSolutionNotification: Error processing notification: " + e.getMessage(), e);
        }
    }
}
