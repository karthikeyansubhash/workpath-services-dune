/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */

package com.hp.jetadvantage.link.device.services.standard.receivers;

import static com.hp.jetadvantage.link.device.services.standard.common.Constants.BOOT_TAG;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.work.BackoffPolicy;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.OutOfQuotaPolicy;

import com.hp.jetadvantage.link.common.constants.CommonConstants;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceHelper;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceManagementService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardSecureAppStorage;
import com.hp.jetadvantage.link.device.services.standard.services.StandardWebsocketCallbackService;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * BroadcastReceiver for DEVICE_READY, WORKPATH_SERVICE_READY
 * DEVICE_READY : (from System app to SDK service app) Entry point to trigger starting the workpath SDK service APK
 * to initialize connection to the device
 * WORKPATH_SERVICE_READY : (from System app to SDK service app) Entry point to trigger starting each SDK service Let
 */
public class DeviceReadyReceiver extends BroadcastReceiver {
    public static final String DEVICE_IP = "device_ip";
    public static final String DEVICE_TOKEN = "device_token";
    public static final String TEST_MODE = "test_mode";
    public static final String SENT_COUNT = "sent_count";
    private static final String TAG = Constants.TAG + "/B/DevReady";
    private static final String DEVICE_READY_ACTION = "com.hp.workpath.system.DEVICE_READY";
    private static AtomicInteger recvCount = new AtomicInteger(1);

    /**
     * The System app will send a DEVICE_READY broadcast, including the device's IP and token, upon successfully
     * connecting to the device and obtaining the access token after boot-up is completed.
     * Upon receiving the DEVICE_READY broadcast, DeviceReadyReceiver will create a OneTimeWorkRequest to initialize
     * StandardDeviceManagementService and to start StandardWebsocketCallbackService.
     * If StandardDeviceManagementService is already initialized and connected to the device, it will simply update
     * the device information without queueing the WorkRequest.
     *
     * @param context The Context in which the receiver is running.
     * @param intent  The Intent being received.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }

        if (CommonConstants.BroadcastActions.WORKPATH_SERVICE_READY.equals(intent.getAction())) {
            Log.i(BOOT_TAG, "WORKPATH_SERVICE_READY -- OK");
            return;
        }
        int count = recvCount.getAndIncrement();
        Log.i(TAG, "onReceive : ENTER :" + count);
        if (context == null || !DEVICE_READY_ACTION.equals(intent.getAction())) {
            Log.i(TAG, "onReceive : Not an expected event");
            return;
        }

        String ip = intent.getStringExtra(DEVICE_IP);
        String token = intent.getStringExtra(DEVICE_TOKEN);
        int sentCount = intent.getIntExtra(SENT_COUNT, 0);
        String testFlag = intent.getStringExtra(TEST_MODE);
        boolean isTestModeOn = (testFlag != null && testFlag.equals("on"));

        Log.i(BOOT_TAG, "DEVICE_READY -- Count: " + count + "/" + sentCount);
        if (ip != null && (token != null || isTestModeOn)) {
            Log.i(BOOT_TAG, "DEVICE_READY -- OK");

            if (StandardDeviceManagementService.getInstance().isDeviceConnected()) {
                Log.i(TAG, "onReceive : updateDeviceInfo");

                if (isTestModeOn) {
                    StandardDeviceManagementService.getInstance().updateDeviceInfo(null, ip, token);
                    Log.i(BOOT_TAG, "DeviceReadyReceiver update -- OK (test mode)");
                } else {
                    StandardDeviceManagementService.getInstance().updateDeviceInfo(context, ip, token);
                    Log.i(BOOT_TAG, "DeviceReadyReceiver update -- OK");
                    StandardWebsocketCallbackService.start(context);
                    DeviceReadyWorker.sendWorkpathInitCompletedEvent(context, true);
                    Log.i(BOOT_TAG, "DeviceReadyReceiver update -- send WORKPATH_SERVICE_INIT_COMPLETED");
                }
            } else {
                if (count == 1 && sentCount == 1) {
                    //clear shared preferences on every boot cycle.
                    StandardSecureAppStorage.clearSharedPreference(context);
                    Log.i(BOOT_TAG, "DeviceReadyReceiver clear AppStorage -- OK");
                }
                Data inputData = new Data.Builder()
                        .putString(DEVICE_IP, ip)
                        .putString(DEVICE_TOKEN, token)
                        .putString(TEST_MODE, testFlag)
                        .build();

                OneTimeWorkRequest initWorkRequest = new OneTimeWorkRequest.Builder(DeviceReadyWorker.class)
                        .setInputData(inputData)
                        .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                        .setInitialDelay(0, TimeUnit.MILLISECONDS)
                        .setBackoffCriteria(BackoffPolicy.LINEAR, OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                                TimeUnit.MILLISECONDS)
                        .build();

                StandardDeviceHelper.getWorkManager(context).enqueueUniqueWork("DeviceReadyWorker",
                        ExistingWorkPolicy.REPLACE, initWorkRequest);
                Log.i(TAG, "onReceive : enqueue DeviceReadyWorker");
                Log.i(BOOT_TAG, "DeviceReadyReceiver -- OK");
            }
        } else {
            Log.e(TAG, "onReceive : IP or Token is null, IP=" + ip);
        }
        Log.i(TAG, "onReceive : EXIT :" + count);
    }
}
