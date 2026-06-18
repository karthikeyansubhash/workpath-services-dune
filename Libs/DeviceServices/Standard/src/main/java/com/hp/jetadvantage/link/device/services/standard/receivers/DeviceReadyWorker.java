/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */

package com.hp.jetadvantage.link.device.services.standard.receivers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.hp.jetadvantage.link.common.constants.CommonConstants;
import com.hp.jetadvantage.link.device.services.clients.TestConnector;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceManagementService;
import com.hp.jetadvantage.link.device.services.standard.services.StandardWebsocketCallbackService;

public class DeviceReadyWorker extends Worker {
    private static final String TAG = Constants.TAG + "/DRWorker";
    private static final String ACTION_WORKPATH_SERVICE_INIT_COMPLETED = "com.hp.workpath.system.WORKPATH_SERVICE_INIT_COMPLETED";
    private static final String ACTION_EXTRA_PARAM_IS_ADDITIONAL_UPDATE = "isAdditionalUpdate";

    private Context mContext;

    @VisibleForTesting
    int mWaitCount = 1000;
    @VisibleForTesting
    long mSleepIntervalMs = 100;

    public DeviceReadyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
    }

    /**
     * Send WORKPATH_SERVICE_INIT_COMPLETED event to System.apk when the workpath service is initialized
     * (IP, token setup completed, and AIDL is connected)
     *
     * @param context            Context to send broadcast event
     * @param isAdditionalUpdate : false if it is the first time to send the event after boot up, true if it is the additional update
     */
    public static void sendWorkpathInitCompletedEvent(Context context, boolean isAdditionalUpdate) {
        if (context == null) {
            Log.e(TAG, "sendWorkpathInitCompletedEvent : context is null");
            return;
        }
        //send WORKPATH_SERVICE_INIT_COMPLETED event to System.apk
        Intent intent = new Intent();
        intent.setAction(ACTION_WORKPATH_SERVICE_INIT_COMPLETED);
        intent.putExtra(ACTION_EXTRA_PARAM_IS_ADDITIONAL_UPDATE, isAdditionalUpdate);
        intent.setPackage(CommonConstants.SYSTEM_SERVICE_PACKAGE_NAME);
        context.sendBroadcast(intent, CommonConstants.Permissions.SYSTEM_PERMISSION);
        Log.i(TAG, "sendWorkpathInitCompletedEvent : send WORKPATH_SERVICE_INIT_COMPLETED , updates : " + isAdditionalUpdate);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork() ENTER");
        String ip = getInputData().getString(DeviceReadyReceiver.DEVICE_IP);
        String token = getInputData().getString(DeviceReadyReceiver.DEVICE_TOKEN);
        String testMode = getInputData().getString(DeviceReadyReceiver.TEST_MODE);
        boolean isTestModeOn = (testMode != null && testMode.equals("on"));

        if (isTestModeOn && token == null) {
            // Test mode: if token is not given, get test token
            TestConnector testConn = new TestConnector();
            token = testConn.getUdwClient(ip).getTestToken();
            Log.i(TAG, "doWork() : Test mode token :" + token);
        }

        if (ip != null && token != null) {
            try {
                StandardDeviceManagementService.getInstance().initialize(mContext, ip, token);
            } catch (Exception e) {
                Log.e(TAG, "doWork() : Exception=" + e.getMessage());
                Log.i(Constants.BOOT_TAG, "DeviceReadyWorker - FAIL : x");
                return Result.retry();
            }

            if (!isTestModeOn) {
                //Normal mode
                Log.d(TAG, "doWork()  Normal mode");
                StandardWebsocketCallbackService.start(mContext);

                //wait AIDL is connected to the System.apk
                int waitCnt = mWaitCount;
                while (waitCnt > 0 && !StandardWebsocketCallbackService.getServiceConnected()) {
                    try {
                        Thread.sleep(mSleepIntervalMs);
                        waitCnt--;
                    } catch (InterruptedException e) {
                        break;
                    }
                }
                Log.i(TAG, "doWork() waitCnt=" + waitCnt + ",isServiceConnected:" + StandardWebsocketCallbackService.getServiceConnected());

                if (StandardWebsocketCallbackService.getServiceConnected()) {
                    Log.i(Constants.BOOT_TAG, "DeviceReadyWorker -- OK");

                    //send WORKPATH_SERVICE_INIT_COMPLETED event to System.apk
                    sendWorkpathInitCompletedEvent(mContext, false);

                    Log.i(TAG, "doWork() send WORKPATH_SERVICE_INIT_COMPLETED");
                    Log.i(Constants.BOOT_TAG, "WORKPATH_SERVICE_INIT_COMPLETED -- OK");
                } else {
                    Log.i(Constants.BOOT_TAG, "DeviceReadyWorker - FAIL : xx");
                    Log.e(TAG, "doWork() : AIDL service connection timed out after " + (1000 - waitCnt) * 100 + "ms," +
                            "retrying...");
                    return Result.retry();
                }

            } else {
                //Test mode : skip to start service
                Log.d(TAG, "doWork() Test mode : skip to start service");
            }
        } else {
            Log.e(TAG, "doWork() : Invalid input, ip or token is null , ip=" + ip);
            Log.i(Constants.BOOT_TAG, "DeviceReadyWorker - FAIL : xxx");
        }


        Log.i(TAG, "doWork() isDeviceConnected=" + StandardDeviceManagementService.getInstance().isDeviceConnected());
        Log.d(TAG, "doWork() EXIT");
        return Result.success();
    }
}
