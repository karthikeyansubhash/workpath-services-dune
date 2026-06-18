/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.accessorylet.service;

import static com.hp.jetadvantage.link.common.constants.LogConstants.TAG_ACCESSORY_SERVICE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.hp.jetadvantage.link.api.accessory.RegistrationType;
import com.hp.jetadvantage.link.api.accessory.hid.Accessorylet;
import com.hp.jetadvantage.link.api.accessory.hid.EventCode;
import com.hp.jetadvantage.link.api.accessory.hid.HIDAccessoryInfo;
import com.hp.jetadvantage.link.common.constants.CommonConstants;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceAccessoryService;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceAccessoryService;
import com.hp.jetadvantage.link.services.accessorylet.adapter.AccessoryDeviceAdapter;

import java.util.Calendar;

/**
 * Worker class to notify an app of an accessory context change event when an accessory is attached or detached.
 */
public class AccessoryDetectionNotificationWorker extends Worker {
    public static final String APP_PACKAGE_NAME = "APP_PACKAGE_NAME";
    public static final String ACCESSORY_RESOURCE_ID = "ACCESSORY_RESOURCE_ID";
    public static final String ACCESSORY_EVENT_CODE = "ACCESSORY_EVENT_CODE";
    private static final String TAG = TAG_ACCESSORY_SERVICE + "/SVC/DetWorker";

    public AccessoryDetectionNotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork: ENTER - AccessoryDetectionNotificationWorker");
        String packageName = getInputData().getString(APP_PACKAGE_NAME);
        String resourceId = getInputData().getString(ACCESSORY_RESOURCE_ID);
        String eventCodeStr = getInputData().getString(ACCESSORY_EVENT_CODE);

        if (packageName == null || resourceId == null || eventCodeStr == null) {
            Log.e(TAG, "doWork: packageName or resourceId is null");
            return Result.failure();
        }
        EventCode eventCode = EventCode.valueOf(eventCodeStr);

        String suffixLog = "eventCode:" + eventCode.name() + ", packageName:" + packageName + ", resourceId:" + resourceId;
        Log.d(TAG, "doWork: " + suffixLog);

        HIDAccessoryInfo hidAccessoryInfo = handleAccessoryInfo(packageName, resourceId, eventCode);
        if (hidAccessoryInfo != null) {
            String timestamp = Calendar.getInstance().getTime().toString();

            sendContextChangeToSdkLib(packageName, hidAccessoryInfo, eventCode, resourceId, timestamp);
            sendContextChangeToAppReceiver(packageName, hidAccessoryInfo, eventCode, resourceId, timestamp);
            sendLegacyBroadcast(packageName, hidAccessoryInfo, eventCode, resourceId, timestamp);

            Log.d(TAG, "doWork: " + hidAccessoryInfo);
        } else {
            Log.e(TAG, "doWork: HIDAccessoryInfo is null, " + suffixLog);
        }
        Log.d(TAG, "doWork: EXIT - AccessoryDetectionNotificationWorker");
        return Result.success();
    }

    protected HIDAccessoryInfo getHidAccessoryInfoFromDevice(String packageName, String resourceId) {
        IDeviceAccessoryService deviceAccessoryService = new StandardDeviceAccessoryService();
        return AccessoryDeviceAdapter.getHidAccessoryInfo(deviceAccessoryService, packageName,
                resourceId);
    }

    private HIDAccessoryInfo handleAccessoryInfo(String packageName, String resourceId, EventCode eventCode) {
        HIDAccessoryInfo hidAccessoryInfo;
        if (eventCode == EventCode.CONTEXT_REVOKED) {
            hidAccessoryInfo = AccessoryCache.getInstance().getHidAccessoryInfo(resourceId);
            AccessoryCache.getInstance().remove(resourceId);
        } else {
            hidAccessoryInfo = getHidAccessoryInfoFromDevice(packageName, resourceId);
            if(hidAccessoryInfo != null) {
                AccessoryCache.getInstance().add(resourceId, packageName, hidAccessoryInfo);
            } else {
                Log.e(TAG, "handleAccessoryInfo: failed to getHidAccessoryInfoFromDevice ");
            }
        }
        return hidAccessoryInfo;
    }

    /**
     * Sends the context change broadcast event to the Workpath library in the app.
     * Workpath library {com.hp.workpath.api.accessory.hid.AccessoryService.AbstractAccessoryStartObserver} in
     * the app receives this event and calls {AbstractAccessoryStartObserver.onReady()} callback in the app.
     * <p>
     * Note: Some old apps receives this broadcast event directly in their static broadcast receiver.
     */
    @SuppressLint("WrongConstant")
    private void sendContextChangeToSdkLib(String packageName, HIDAccessoryInfo hidAccessoryInfo, EventCode eventCode
            , String resourceId, String timestamp) {
        final Intent contextChangeIntent = new Intent(Accessorylet.ACCESSORY_CONTEXT_CHANGE_ACTION);
        contextChangeIntent.putExtra(Accessorylet.Keys.KEY_ACCESSORY_INFO, hidAccessoryInfo);
        contextChangeIntent.putExtra(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_EVENT_CODE, eventCode);
        contextChangeIntent.putExtra(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID, resourceId);
        contextChangeIntent.putExtra(Accessorylet.Keys.KEY_TIMESTAMP, timestamp);
        contextChangeIntent.setPackage(packageName);
        contextChangeIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        contextChangeIntent.addFlags(CommonConstants.BroadcastFlags.FLAG_RECEIVER_INCLUDE_BACKGROUND);
        getApplicationContext().sendBroadcast(contextChangeIntent);

        Log.i(TAG, "sendContextChangeToSdkLib : sent ACCESSORY_CONTEXT_CHANGE_ACTION broadcast" +
                "[packageName:" + packageName + ", resourceId:" + resourceId + ", eventCode:" + eventCode + "]");
    }

    /**
     * Sends the context change broadcast event to the app's static broadcast receiver.
     * This broadcast event has been revised to deliver the accessory context change event to the app directly,
     * waking it up if it has been stopped or crashed.
     */
    @SuppressLint("WrongConstant")
    private void sendContextChangeToAppReceiver(String packageName, HIDAccessoryInfo hidAccessoryInfo,
                                                EventCode eventCode, String resourceId, String timestamp) {
        final Intent workpathIntent = new Intent(Accessorylet.ACCESSORY_CONTEXT_CHANGE_ACTION_FOR_APP);
        workpathIntent.putExtra(Accessorylet.Keys.KEY_ACCESSORY_INFO,
                com.hp.workpath.api.accessory.hid.HIDAccessoryInfo.CREATOR_OBJ.createFromObject(hidAccessoryInfo));
        workpathIntent.putExtra(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_EVENT_CODE,
                com.hp.workpath.api.accessory.hid.EventCode.valueOf(eventCode.name()));
        workpathIntent.putExtra(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID, resourceId);
        workpathIntent.putExtra(Accessorylet.Keys.KEY_TIMESTAMP, timestamp);
        workpathIntent.setPackage(packageName);
        workpathIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        workpathIntent.addFlags(CommonConstants.BroadcastFlags.FLAG_RECEIVER_INCLUDE_BACKGROUND);
        getApplicationContext().sendBroadcast(workpathIntent);

        Log.i(TAG, "sendContextChangeToAppReceiver : sent ACCESSORY_CONTEXT_CHANGE_ACTION_FOR_APP broadcast" +
                "[packageName:" + packageName + ", resourceId:" + resourceId + ", eventCode:" + eventCode + "]");
    }

    /**
     * Send the accessory status action broadcast event to the legacy app receiver.
     * This is deprecated broadcast event, maintained for backward compatibility.
     */
    @SuppressLint("WrongConstant")
    private void sendLegacyBroadcast(String packageName, HIDAccessoryInfo hidAccessoryInfo, EventCode eventCode,
                                     String resourceId, String timestamp) {
        final Intent tempIntent = new Intent(Accessorylet.ACCESSORY_STATUS_ACTION);
        tempIntent.putExtra(Accessorylet.Keys.ACCESSORY_INFO_VENDER_ID, hidAccessoryInfo.getVendorId());
        tempIntent.putExtra(Accessorylet.Keys.ACCESSORY_INFO_VENDOR_ID, hidAccessoryInfo.getVendorId());
        tempIntent.putExtra(Accessorylet.Keys.ACCESSORY_INFO_PRODUCT_ID, hidAccessoryInfo.getProductId());
        tempIntent.putExtra(Accessorylet.Keys.ACCESSORY_INFO_SERIAL_NUMBER, hidAccessoryInfo.getSerialNumber());
        tempIntent.putExtra(Accessorylet.Keys.ACCESSORY_INFO_REGISTRATION_TYPE, RegistrationType.OWNED.ordinal());
        tempIntent.putExtra(Accessorylet.Keys.ACCESSORY_INFO_EVENT_CODE, eventCode.ordinal());
        tempIntent.putExtra(Accessorylet.Keys.ACCESSORY_INFO_CONTEXT_ID, resourceId);
        tempIntent.putExtra(Accessorylet.Keys.ACCESSORY_INFO_TIMESTAMP, timestamp);
        tempIntent.setPackage(packageName);
        tempIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        tempIntent.addFlags(CommonConstants.BroadcastFlags.FLAG_RECEIVER_INCLUDE_BACKGROUND);
        getApplicationContext().sendBroadcast(tempIntent);

        Log.i(TAG, "sendLegacyBroadcast : sent ACCESSORY_STATUS_ACTION broadcast" +
                "[packageName:" + packageName + ", resourceId:" + resourceId + ", eventCode:" + eventCode + "]");
    }
}
