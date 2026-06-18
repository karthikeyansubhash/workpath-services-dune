// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.statisticslet.service;

import static com.hp.jetadvantage.link.common.constants.LogConstants.TAG_STATISTICS_SERVICE;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;

import com.hp.ext.service.jobStatistics.StatisticsCallbackPayload;
import com.hp.ext.service.jobStatistics.JobStatisticsAgentRegistrationRecord;
import com.hp.jetadvantage.link.common.Platform;
import com.hp.jetadvantage.link.common.constants.CommonConstants;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceStatisticsService;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceStatisticsService;
import com.hp.jetadvantage.link.device.services.standard.common.PackageManagerHelper;
import com.hp.jetadvantage.link.services.common.notification.ServiceNotification;
import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;
import com.hp.jetadvantage.link.services.common.ssp.SpsConstants;
import com.hp.workpath.api.statistics.Statisticslet;

public class StatisticsNotificationObserverService extends Service {
    private static final String TAG = TAG_STATISTICS_SERVICE + "/OBS";
    private static final String STATISTICS_AGENT_TYPE_GUN = new JobStatisticsAgentRegistrationRecord().getTypeGUN();

    private Handler mHandler;
    private HandlerThread mHandlerThread;
    private IDeviceStatisticsService mDeviceStatisticsService;
    private PackageManagerHelper packageManagerHelper = new PackageManagerHelper();

    // Required public no-arg constructor for Android Service instantiation
    public StatisticsNotificationObserverService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            ensurePermissionSafely();

            if(Platform.isPanel()) {
                mHandlerThread = new HandlerThread(TAG + ":" + getClass().getSimpleName(),
                        Process.THREAD_PRIORITY_BACKGROUND);
                mHandlerThread.start();
                mHandler = new SubscribeHandler(mHandlerThread.getLooper());

                // Register callback before announcing readiness to avoid missing
                // notifications that arrive during boot before onStartCommand runs
                subscribeStatisticsNotificationEvents();

                setSystemServiceFlag();
                ServiceNotification.showNotification(this);

                CommonConstants.sendBroadCastForBoot(getApplicationContext(),
                        CommonConstants.BroadcastActions.READY_STATISTICSLET);
            } else {
                Log.d(TAG, "Not created Statistics svc.");
            }
        } catch (Exception e) {
            Log.d(TAG, "Not created Statistics svc because of permission." + e.getMessage());
        }
    }

    private void ensurePermissionSafely() {
        try {
            SpsPermissionHelper.ensurePermission(getApplicationContext());
        } catch (Exception e) {
            Log.i(TAG, "Permission error statistics svc. ignored.");
        }
    }

    private void setSystemServiceFlag() {
        try {
            System.setProperty(CommonConstants.SYSTEM_SVC_FLAG, TAG);
            Log.d(TAG, "Created Statistics svc.");
        } catch (Exception e) {
            // SecurityException may occur if system property access is restricted
            Log.d(TAG, "Failed to set system service flag: " + e.getMessage());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            SpsPermissionHelper.ensurePermission(getApplicationContext());

            if (mHandler == null) {
                Log.e(TAG, "Statistics start failed.(ignored)");
            } else {
                // Re-subscribe only if not already subscribed (e.g., service restarted by system)
                if (mDeviceStatisticsService == null) {
                    mHandler.sendMessage(mHandler.obtainMessage(SubscribeHandler.MSG_START));
                } else {
                    Log.i(TAG, "Already subscribed to statistics notifications.");
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Not started Statistics because of permission(start cmd)." + e.getMessage());
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy : ENTER");
        super.onDestroy();

        if (mDeviceStatisticsService != null) {
            mDeviceStatisticsService.unRegisterNotificationCallback();
            mDeviceStatisticsService = null;
        }

        if (mHandlerThread != null) {
            mHandlerThread.quitSafely();
            mHandlerThread = null;
            mHandler = null;
        }
        Log.d(TAG, "onDestroy : EXIT");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void start(final Context context) {
        try {
            SpsPermissionHelper.ensurePermission(context);

            if(Platform.isPanel()) {
                Log.d(TAG, "Start Statistics svc.");
                final Intent intent = new Intent(context, StatisticsNotificationObserverService.class);
                context.startForegroundService(intent);
            } else {
                Log.d(TAG, "Not started Statistics svc.");
            }
        } catch (Exception e) {
            Log.d(TAG, "Not created Statistics svc because of permission(start)." + e.getMessage());
        }
    }

    /**
     * Send broadcast to app to notify job completion.
     * The broadcast carries the agent ID, last sequence number processed, and the highest completed
     * job sequence number so the app can call onComplete(completedJobSequence).
     */
    @VisibleForTesting
    void sendBroadcastToApp(Context context, String agentId, long lastSequenceNumber,
                                     long highestSequenceNumber, String pkgName) {
        try {
            Intent intent = new Intent(Statisticslet.NOTIFICATION_CHANGE_ACTION);
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            intent.putExtra(Statisticslet.Keys.EXTRA_UUID, agentId);
            intent.putExtra(Statisticslet.Keys.EXTRA_DATA, lastSequenceNumber);
            int highestSeqInt = clampHighestSeqToInt(highestSequenceNumber);
            if (highestSequenceNumber > Integer.MAX_VALUE) {
                Log.w(TAG, "sendBroadcastToApp: highestSequenceNumber exceeds int range, clamped: "
                        + highestSequenceNumber);
            }
            intent.putExtra(Statisticslet.Keys.EXTRA_DATA2, highestSeqInt);
            intent.setPackage(pkgName);
            context.sendBroadcast(intent, SpsConstants.SDK_ACCESS_STATISTICS_PERMISSION);

            Log.d(TAG, "sendBroadcastToApp: agentId=" + agentId
                    + ", pkg=" + pkgName
                    + ", lastSeq=" + lastSequenceNumber
                    + ", highestSeq=" + highestSequenceNumber);
        } catch (Exception e) {
            Log.e(TAG, "Failed to send agent broadcast: " + e.getMessage());
        }
    }

    /**
     * Subscribe to statistics notification events from the E2 Statistics Agent via Device Service.
     * Registers a notification callback with the Device Statistics Service.
     * When E2 sends a job completion notification via WebSocket, the registered callback is invoked
     * which triggers broadcasts to the app and package manager.
     */
    @VisibleForTesting
    void subscribeStatisticsNotificationEvents() {
        Log.d(TAG, "subscribeStatisticsNotificationEvents: ENTER");
        try {
            mDeviceStatisticsService = createDeviceStatisticsService();

            mDeviceStatisticsService.registerNotificationCallback(this::processStatisticsNotification);
            Log.d(TAG, "subscribeStatisticsNotificationEvents: registered notification callback");
        } catch (Exception e) {
            Log.e(TAG, "subscribeStatisticsNotificationEvents: Failed to register: " + e.getMessage());
        }
        Log.d(TAG, "subscribeStatisticsNotificationEvents: EXIT");
    }

    /**
     * Process the statistics notification received from the E2 Statistics Agent.
     * Extracts sequence numbers from the service message and sends broadcasts to the app and package manager.
     */
    @VisibleForTesting
    void processStatisticsNotification(String appPackageId, StatisticsCallbackPayload notification) {
        Log.d(TAG, "processStatisticsNotification: ENTER appPackageId=" + appPackageId);
        if (notification == null) {
            Log.e(TAG, "processStatisticsNotification: notification is null");
            return;
        }

        try {
            Context context = getApplicationContext();

            long lastSequenceNumberProcessed = 0;
            if (notification.getLastSequenceNumberProcessed() != null
                    && notification.getLastSequenceNumberProcessed().getValue() != null) {
                lastSequenceNumberProcessed = notification.getLastSequenceNumberProcessed().getValue();
            }

            long lastSequenceNumberNotified = 0;
            if (notification.getLastSequenceNumberNotified() != null
                    && notification.getLastSequenceNumberNotified().getValue() != null) {
                lastSequenceNumberNotified = notification.getLastSequenceNumberNotified().getValue();
            }

            String agentId = packageManagerHelper.getAgentId(context, appPackageId, STATISTICS_AGENT_TYPE_GUN);

            if (agentId == null) {
                Log.e(TAG, "processStatisticsNotification: agentId is null for pkg=" + appPackageId);
                return;
            }

            sendBroadcastToApp(context, agentId, lastSequenceNumberProcessed,
                    lastSequenceNumberNotified, appPackageId);
        } catch (Exception e) {
            Log.e(TAG, "processStatisticsNotification: Error: " + e.getMessage());
        }
        Log.d(TAG, "processStatisticsNotification: EXIT");
    }

    @VisibleForTesting
    IDeviceStatisticsService createDeviceStatisticsService() {
        return new StandardDeviceStatisticsService();
    }

    @VisibleForTesting
    IDeviceStatisticsService getDeviceStatisticsService() {
        return mDeviceStatisticsService;
    }

    @VisibleForTesting
    void setDeviceStatisticsService(IDeviceStatisticsService deviceStatisticsService) {
        mDeviceStatisticsService = deviceStatisticsService;
    }

    @VisibleForTesting
    void setPackageManagerHelper(PackageManagerHelper helper) {
        this.packageManagerHelper = helper;
    }

    @VisibleForTesting
    static int clampHighestSeqToInt(long value) {
        return value > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) value;
    }

    private class SubscribeHandler extends Handler {
        private static final int MSG_START = 1;

        SubscribeHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if(Platform.isPanel()) {
                try {
                    if (msg.what == MSG_START) {
                        Log.d(TAG, "Start Statistics svc for event monitoring.");
                        subscribeStatisticsNotificationEvents();
                    } else {
                        Log.e(TAG, "Unknown message: " + msg.what);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Failed to handle message " + msg.what, e);
                }
            }
        }
    }
}
