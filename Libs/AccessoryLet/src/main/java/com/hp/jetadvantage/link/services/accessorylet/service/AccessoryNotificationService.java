/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.accessorylet.service;

import static com.hp.jetadvantage.link.common.constants.LogConstants.TAG_ACCESSORY_SERVICE;
import static com.hp.jetadvantage.link.common.constants.PackageContract.Intent.EXTRA_PACKAGE;
import static com.hp.jetadvantage.link.services.accessorylet.service.AccessoryDetectionNotificationWorker.ACCESSORY_EVENT_CODE;
import static com.hp.jetadvantage.link.services.accessorylet.service.AccessoryDetectionNotificationWorker.ACCESSORY_RESOURCE_ID;
import static com.hp.jetadvantage.link.services.accessorylet.service.AccessoryDetectionNotificationWorker.APP_PACKAGE_NAME;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.Process;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.work.BackoffPolicy;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.Operation;
import androidx.work.WorkManager;

import com.hp.ext.service.usbAccessories.UsbCallback;
import com.hp.ext.service.usbAccessories.UsbRegistrationPayload;
import com.hp.jetadvantage.link.api.accessory.hid.Accessorylet;
import com.hp.jetadvantage.link.api.accessory.hid.EventCode;
import com.hp.jetadvantage.link.api.accessory.hid.HIDAccessoryInfo;
import com.hp.jetadvantage.link.api.accessory.hid.HIDReport;
import com.hp.jetadvantage.link.api.accessory.hid.HIDReportEventInfo;
import com.hp.jetadvantage.link.api.accessory.hid.HIDReportType;
import com.hp.jetadvantage.link.common.constants.CommonConstants;
import com.hp.jetadvantage.link.common.constants.PackageContract;
import com.hp.jetadvantage.link.common.helper.WorkManagerHelper;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.interfaces.IAppInstallUninstallCallback;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceAccessoryService;
import com.hp.jetadvantage.link.device.services.interfaces.IE2AsyncIoCallback;
import com.hp.jetadvantage.link.device.services.interfaces.IE2ChannelSetupCallback;
import com.hp.jetadvantage.link.device.services.interfaces.IE2PayloadCallback;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceAccessoryService;
import com.hp.jetadvantage.link.services.accessorylet.adapter.AccessoryDeviceAdapter;
import com.hp.jetadvantage.link.services.accessorylet.util.AccessoryUtility;
import com.hp.jetadvantage.link.services.common.notification.ServiceNotification;
import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class AccessoryNotificationService extends Service {
    private static final String TAG = TAG_ACCESSORY_SERVICE + "/SVC";
    private static final String INTENT_EXTRA_ACCESSORY_NOTIFICATION_REQUEST = "isAccessoryNotificationRequest";
    private static final String INTENT_EXTRA_PACKAGE_NAME = "packageName";
    private static final String INTENT_EXTRA_RESOURCE_ID = "resourceId";
    private static final String INTENT_EXTRA_EVENT_CODE = "eventCode";
    private static final int HP_VENDOR_ID = 1008;
    private static final int HP_PRODUCT_ID = 69;
    private static final long CALLBACK_TIMEOUT_MS = 6000;
    /**
     * Timeout for the AbstractAccessoryService.onStart() callback to be completed on the 3'rd party app
     */
    private static final long APP_START_CALLBACK_TIMEOUT_MSEC = 6000;
    private AccessoryAppStartHandler mAppStartHandler;
    private HandlerThread mHandlerThread;
    private IDeviceAccessoryService mDeviceAccessoryService;
    private WorkManager mWorkManager;

    /// /////////////// static methods //////////////////

    public static void start(final Context context) {
        Log.d(TAG, "start : ENTER");
        try {
            SpsPermissionHelper.ensureSystemPermission(context);

            Log.d(TAG, "start: startForegroundService AccessoryNotificationService");
            final Intent intent = new Intent(context, AccessoryNotificationService.class);
            context.startForegroundService(intent);
        } catch (Throwable e) {
            Log.e(TAG, "start: Failed to start AccessoryNotificationService: " + e.getMessage(), e);
        }
        Log.d(TAG, "start : EXIT");
    }

    public static void sendAccessoryNotificationToApp(final Context context, String appPackageName, String resourceId,
                                                      EventCode eventCode) {
        String suffixLog = "[" + appPackageName + "] [" + resourceId + "] [" + eventCode + "]";
        Log.d(TAG, "sendAccessoryNotificationToApp : ENTER - " + suffixLog);
        if (context == null || appPackageName == null || resourceId == null || eventCode == null) {
            Log.e(TAG, "sendAccessoryNotificationToApp: Invalid input parameters " + suffixLog);
            return;
        }
        try {
            SpsPermissionHelper.ensureSystemPermission(context);

            final Intent intent = new Intent(context, AccessoryNotificationService.class);
            intent.putExtra(INTENT_EXTRA_ACCESSORY_NOTIFICATION_REQUEST, true);
            intent.putExtra(INTENT_EXTRA_PACKAGE_NAME, appPackageName);
            intent.putExtra(INTENT_EXTRA_RESOURCE_ID, resourceId);
            intent.putExtra(INTENT_EXTRA_EVENT_CODE, eventCode.name());
            context.startService(intent);
            Log.d(TAG, "sendAccessoryNotificationToApp: call startService AccessoryNotificationService " + suffixLog);
        } catch (Throwable e) {
            Log.e(TAG, "sendAccessoryNotificationToApp: Failed to start AccessoryNotificationService: " +
                    e.getMessage() + suffixLog, e);
        }
        Log.d(TAG, "sendAccessoryNotificationToApp : EXIT - " + suffixLog);
    }

    /// /////////////// Override methods from Service //////////////////

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate : ENTER - AccessoryNotificationService");
        super.onCreate();

        try {
            SpsPermissionHelper.ensureSystemPermission(getApplicationContext());
        } catch (Throwable throwable) {
            Log.i(TAG, "onCreate: AccessoryNotificationService Permission error", throwable);
        }

        try {
            mWorkManager = WorkManagerHelper.getWorkManager(this);
            createAppStartHandler();

            mDeviceAccessoryService = new StandardDeviceAccessoryService();
            registerNotificationCallbacks(mDeviceAccessoryService);

            AccessoryUtility.setSystemProperty(CommonConstants.SYSTEM_SVC_FLAG, TAG,
                    "AccessoryNotificationService created");
            ServiceNotification.showNotification(this);
        } catch (Throwable e) {
            Log.e(TAG, "onCreate: Failed to create accessory notification service: " + e.getMessage(), e);
        }
        Log.d(TAG, "onCreate : EXIT - AccessoryNotificationService");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy : ENTER - AccessoryNotificationService");
        super.onDestroy();

        if (mHandlerThread != null) {
            mHandlerThread.quitSafely();
            mHandlerThread = null;
            mAppStartHandler = null;
        }

        if (mDeviceAccessoryService != null) {
            mDeviceAccessoryService.unRegisterAppChannelSetupCallback();
            mDeviceAccessoryService.unRegisterNotificationCallback();
            mDeviceAccessoryService.unRegisterAppInstallUninstallCallback();
            mDeviceAccessoryService = null;
        }
        Log.d(TAG, "onDestroy : EXIT - AccessoryNotificationService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand : ENTER - AccessoryNotificationService");
        processAccessoryNotificationRequest(intent);
        Log.d(TAG, "onStartCommand : EXIT - AccessoryNotificationService");
        return START_STICKY;
    }

    ////////////////// private methods  //////////////////

    private void createAppStartHandler() {
        mHandlerThread = new HandlerThread(Accessorylet.TAG + ":" + getClass().getSimpleName(),
                Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
        mAppStartHandler = new AccessoryAppStartHandler(this, mHandlerThread.getLooper());
    }

    private void registerNotificationCallbacks(IDeviceAccessoryService deviceAccessoryService) {
        IE2ChannelSetupCallback channelSetupCallback = appPackageName -> processChannelSetupEvent(appPackageName);

        IE2PayloadCallback<UsbRegistrationPayload> usbRegistrationPayloadCallback =
                (appPackageId, notification) -> processUsbRegistrationPayloadEvent(appPackageId, notification);

        IE2AsyncIoCallback<UsbCallback> asyncIOCallback =
                (appPackageId, accessoryId, notification) ->
                        processAsyncIoEvent(appPackageId, accessoryId, notification);

        IAppInstallUninstallCallback appInstallUninstallCallback = (context, intent) ->
                processAppInstallUninstallEvent(intent);

        if (deviceAccessoryService != null) {
            deviceAccessoryService.registerUsbRegistrationChannelSetupCallback(channelSetupCallback);
            deviceAccessoryService.registerNotificationCallback(usbRegistrationPayloadCallback);
            deviceAccessoryService.registerOperationCallback(asyncIOCallback);
            deviceAccessoryService.registerAppInstallUninstallCallback(appInstallUninstallCallback);
        }
    }

    private void processAccessoryNotificationRequest(Intent intent) {
        if (intent == null || !intent.getBooleanExtra(INTENT_EXTRA_ACCESSORY_NOTIFICATION_REQUEST, false)) {
            Log.d(TAG, "processAccessoryNotificationRequest: Skipping request due to null/false flag");
            return;
        }

        String appPkgName = intent.getStringExtra(INTENT_EXTRA_PACKAGE_NAME);
        String resourceId = intent.getStringExtra(INTENT_EXTRA_RESOURCE_ID);
        String eventCode = intent.getStringExtra(INTENT_EXTRA_EVENT_CODE);

        if (appPkgName == null || resourceId == null || eventCode == null) {
            Log.e(TAG, "processAccessoryNotificationRequest: Missing parameters");
            return;
        }

        try {
            EventCode code = EventCode.valueOf(eventCode);
            enqueueAccessoryNotificationWork(appPkgName, java.util.UUID.fromString(resourceId), code);
            Log.d(TAG, String.format("processAccessoryNotificationRequest: [%s][%s][%s]", appPkgName, resourceId,
                    eventCode));
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "processAccessoryNotificationRequest: Invalid event code: " + eventCode, e);
        }
    }

    private void processAppInstallUninstallEvent(Intent intent) {
        if (intent == null || intent.getAction() == null) {
            Log.e(TAG, "processAppInstallUninstallEvent: [AppInstall] null intent");
            return;
        }
        String packageName = intent.getStringExtra(EXTRA_PACKAGE);
        String action = intent.getAction();
        String logTail = "[" + action + "] [" + packageName + "]";
        Log.d(TAG, "processAppInstallUninstallEvent: [AppInstall] ENTER IAppInstallUninstallCallback " + logTail);

        switch (action) {
            case PackageContract.Intent.ACTION_PACKAGE_INSTALLED:
                sendBroadcastToStartApp(packageName);
                mAppStartHandler.sendAppStartMessage(packageName);
                break;
            case PackageContract.Intent.ACTION_PACKAGE_UNINSTALLED:
                mAppStartHandler.cleanup(packageName);
                break;
            default:
                Log.e(TAG, "processAppInstallUninstallEvent: [AppInstall] unknown action: " + intent.getAction());
        }
        Log.d(TAG, "processAppInstallUninstallEvent: [AppInstall] EXIT IAppInstallUninstallCallback " + logTail);
    }

    private void processChannelSetupEvent(String appPackageName) {
        Log.d(TAG, "processChannelSetupEvent : ENTER - appPackageName[" + appPackageName + "]");
        if (appPackageName == null) {
            Log.e(TAG, "processChannelSetupEvent : appPackageName is null ");
            return;
        }
        try {
            if (mDeviceAccessoryService.isSolutionRegistered(appPackageName)) {
                mAppStartHandler.sendAppStartMessage(appPackageName);
            } else {
                Log.d(TAG, "processChannelSetupEvent : not yet registered appPackageName[" + appPackageName + "]");
            }
        } catch (Exception e) {
            Log.e(TAG, "processChannelSetupEvent : Failed to sendAppStartMessage: " + e.getMessage(), e);
        }
        Log.d(TAG, "processChannelSetupEvent : EXIT - appPackageName[" + appPackageName + "]");
    }

    private void processAsyncIoEvent(String appPackageName, String accessoryId, UsbCallback notification) {
        Log.d(TAG, "processAsyncIoEvent: ENTER [" + appPackageName + "," + accessoryId + "]");
        if (appPackageName == null || accessoryId == null || notification == null) {
            Log.e(TAG, String.format("processAsyncIoEvent: param is null - appPackageName = %s, accessoryId = %s",
                    appPackageName, accessoryId));
            return;
        }
        try {
            if (notification.isUsbRead()) {
                Log.d(TAG, "processAsyncIoEvent: UsbRead");
            } else if (notification.isHidRead()) {
                String base64Data = notification.getHidRead().getData().getValue();
                byte[] data = Base64.getDecoder().decode(base64Data);
                Long sequence = notification.getHidRead().getHidReadSequence();

                Log.d(TAG, String.format("processAsyncIoEvent: HidRead[%s,%d]:%s", accessoryId, sequence, base64Data));
                if (data != null && data.length > 0) {
                    HIDAccessoryInfo hidAccessoryInfo = getHidAccessoryInfoWithCache(appPackageName, accessoryId);
                    sendHidReportEventToApp(getApplicationContext(), appPackageName, hidAccessoryInfo, accessoryId,
                            data, sequence);
                } else {
                    Log.e(TAG, String.format("processAsyncIoEvent: data is empty [%s, %d]", accessoryId, sequence));
                }
            }
        } catch (Exception ignored) {
            Log.e(TAG, "processAsyncIoEvent: " + ignored.getMessage(), ignored);
        }
        Log.d(TAG, "processAsyncIoEvent: EXIT [" + appPackageName + "," + accessoryId + "]");
    }

    private void processUsbRegistrationPayloadEvent(String appPackageName, UsbRegistrationPayload notification) {
        String logTail = "AppPackageName[" + appPackageName + "]";
        Log.d(TAG, "processUsbRegistrationPayloadEvent : ENTER - UsbRegistrationPayload " + logTail);
        if (appPackageName == null || notification == null) {
            Log.e(TAG, "processUsbRegistrationPayloadEvent : appPackageName or notification is null " +
                    "(appPackageName=" + appPackageName + ", notification=" + notification + ")");
            return;
        }
        try {
            if (notification.isUsbAttached()) {
                Log.d(TAG, "processUsbRegistrationPayloadEvent : USB accessory attached, " + logTail);
                enqueueAccessoryNotificationWork(appPackageName, notification.getUsbAttached().getResourceId(),
                        EventCode.CONTEXT_CREATED);
            } else if (notification.isUsbDetached()) {
                Log.d(TAG, "processUsbRegistrationPayloadEvent : USB accessory detached, " + logTail);
                UUID resourceId = notification.getUsbDetached().getResourceId();
                AccessoryCache.getInstance().invalidate(resourceId.toString());
                enqueueAccessoryNotificationWork(appPackageName, resourceId, EventCode.CONTEXT_REVOKED);
            }
        } catch (Exception e) {
            Log.e(TAG, "processUsbRegistrationPayloadEvent : Error: " + e.getMessage(), e);
        }
        Log.d(TAG, "processUsbRegistrationPayloadEvent : EXIT - UsbRegistrationPayload " + logTail);
    }

    private void enqueueAccessoryNotificationWork(String appPackageName, java.util.UUID resourceId,
                                                  EventCode eventCode) {
        String logTail = "appPackageName[" + appPackageName + "] ResourceId[" + resourceId + "]";
        Log.d(TAG, "enqueueAccessoryNotificationWork: ENTER " + logTail);
        if (appPackageName == null || resourceId == null) {
            Log.e(TAG, "enqueueAccessoryNotificationWork: appPackageId or resourceId is null " +
                    "(appPackageId=" + appPackageName + ", resourceId=" + resourceId + ")");
            return;
        }

        // Enqueue AccessoryDetectionNotificationWorker to notify the accessory detection to 3'rd party apps
        try {
            Data inputData = new Data.Builder()
                    .putString(APP_PACKAGE_NAME, appPackageName)
                    .putString(ACCESSORY_RESOURCE_ID, resourceId.toString())
                    .putString(ACCESSORY_EVENT_CODE, eventCode.name())
                    .build();

            OneTimeWorkRequest notificationRequest =
                    new OneTimeWorkRequest.Builder(AccessoryDetectionNotificationWorker.class)
                            .setInputData(inputData)
                            //.setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                            .setInitialDelay(getInitialDelay(appPackageName), TimeUnit.MILLISECONDS)
                            .setBackoffCriteria(BackoffPolicy.LINEAR, OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                                    TimeUnit.MILLISECONDS)
                            .build();

            String workName = TAG + "/" + appPackageName + "/" + resourceId;
            Operation operation = mWorkManager.enqueueUniqueWork(workName, ExistingWorkPolicy.APPEND,
                    notificationRequest);

            Log.d(TAG, "enqueueAccessoryNotificationWork: enqueueUniqueWork: " + workName);
        } catch (Exception e) {
            Log.e(TAG, "enqueueAccessoryNotificationWork: Error: " + e.getMessage(), e);
        }
        Log.d(TAG, "enqueueAccessoryNotificationWork: EXIT " + logTail);
    }

    /**
     * Get the initial delay for the AccessoryDetectionNotificationWorker
     * Initial delay is required for the worker to wait for the app start complete AbstractAccessoryService.onStart()
     * callback before sending the accessory context change notification
     *
     * @param appPackageName
     * @return
     */
    private long getInitialDelay(String appPackageName) {
        long initialDelay = 0;
        AccessoryAppStartHandler.AppStartHandlerStatus progress = mAppStartHandler.getProgress(appPackageName);
        if (progress == AccessoryAppStartHandler.AppStartHandlerStatus.STARTED ||
                progress == AccessoryAppStartHandler.AppStartHandlerStatus.IN_PROGRESS) {
            initialDelay = APP_START_CALLBACK_TIMEOUT_MSEC;
        } else if (!mDeviceAccessoryService.isSolutionRegistered(appPackageName)) {
            initialDelay = APP_START_CALLBACK_TIMEOUT_MSEC;
        }

        Log.d(TAG, "getInitialDelay: [" + progress.name() + "," + appPackageName + "] " + initialDelay);
        return initialDelay;
    }

    /**
     * get HIDAccessoryInfo with accessoryId from cache
     * if not found, get HIDAccessoryInfo from the device and cache it
     */
    private HIDAccessoryInfo getHidAccessoryInfoWithCache(String appPackageName, String accessoryId) {
        HIDAccessoryInfo hidAccessoryInfo = null;
        try {
            hidAccessoryInfo = AccessoryCache.getInstance().getHidAccessoryInfo(accessoryId);
            if (hidAccessoryInfo == null) {
                Log.d(TAG, String.format("getHidAccessoryInfoWithCache: accessoryId not found in cache [%s, %s]",
                        appPackageName, accessoryId));
                hidAccessoryInfo = AccessoryDeviceAdapter.getHidAccessoryInfo(mDeviceAccessoryService,
                        appPackageName, accessoryId);
                if (hidAccessoryInfo != null) {
                    AccessoryCache.getInstance().add(accessoryId, appPackageName, hidAccessoryInfo);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "getHidAccessoryInfoWithCache: " + e.getMessage(), e);
        }
        Log.d(TAG, "getHidAccessoryInfoWithCache: " + hidAccessoryInfo);
        return hidAccessoryInfo;
    }

    @SuppressLint("WrongConstant")
    private void sendBroadcastToStartApp(String packageName) {
        Log.d(TAG, "sendBroadcastToStartApp: ENTER - " + packageName);
        Intent intent = new Intent(CommonConstants.BroadcastActions.ACCESSORY_READY);
        intent.setPackage(packageName);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.addFlags(CommonConstants.BroadcastFlags.FLAG_RECEIVER_INCLUDE_BACKGROUND);
        this.sendBroadcast(intent);

        Log.d(TAG, "sendBroadcastToStartApp: EXIT - sent " + CommonConstants.BroadcastActions.ACCESSORY_READY +
                " to " + packageName);
    }

    private void sendHidReportEventToApp(Context context, String appPackageName, HIDAccessoryInfo hidAccessoryInfo,
                                         String accessoryId, byte[] data, Long sequence) {
        Log.d(TAG, String.format("sendHidReportEventToApp: ENTER [%s, %s, %d]", appPackageName, accessoryId, sequence));
        if (data == null || data.length == 0) {
            Log.e(TAG, String.format("sendHidReportEventToApp: empty data [%s, %d]", accessoryId, sequence));
            return;
        }
        if (hidAccessoryInfo == null) {
            Log.e(TAG, String.format("sendHidReportEventToApp: hidAccessoryInfo not found [%s, %d]",
                    accessoryId, sequence));
            return;
        }
        try {
            String timestamp = Calendar.getInstance().getTime().toString();
            ArrayList<HIDReport> hidReports = new ArrayList<>();
            hidReports.add(new HIDReport(HIDReportType.INPUT, data));
            HIDReportEventInfo hidReportEventInfo = new HIDReportEventInfo(sequence, timestamp, hidReports);

            Log.i(TAG, String.format("sendHidReport:[%s,%d]: %s", accessoryId, sequence, Arrays.toString(data)));
            // Broadcast for the Workpath library (AbstractAccessoryObserver.onReceive()) in an app
            sendHidReportBroadcast(context, appPackageName, Accessorylet.ACCESSORY_CHANGE_ACTION, hidAccessoryInfo,
                    hidReportEventInfo);
            // Broadcast for receiving in an app directly
            sendHidReportBroadcast(context, appPackageName, Accessorylet.ACCESSORY_CHANGE_ACTION_FOR_APP,
                    com.hp.workpath.api.accessory.hid.HIDAccessoryInfo.CREATOR_OBJ.createFromObject(hidAccessoryInfo),
                    com.hp.workpath.api.accessory.hid.HIDReportEventInfo.CREATOR_OBJ.createFromObject(hidReportEventInfo));
            sendLegacyReportBroadcast(context, appPackageName, hidAccessoryInfo, data, sequence);

            Log.d(TAG, String.format("sendHidReportEventToApp: EXIT - sent %s to %s for %s ",
                    Accessorylet.ACCESSORY_CHANGE_ACTION_FOR_APP, appPackageName, accessoryId));
        } catch (Exception e) {
            Log.e(TAG, "sendHidReportEventToApp: Error sending HID report " + e.getMessage(), e);
        }
    }

    private void sendHidReportBroadcast(Context context, String appPackageName, String action,
                                        Parcelable hidAccessoryInfo, Parcelable hidReportEventInfo) {
        Intent intent = new Intent(action);
        intent.putExtra(Accessorylet.Keys.KEY_ACCESSORY_INFO, hidAccessoryInfo);
        intent.putExtra(Accessorylet.Keys.KEY_HID_REPORT_EVENT_INFO, hidReportEventInfo);
        intent.setPackage(appPackageName);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        context.sendBroadcast(intent);
    }

    /**
     * Send the HID report data to the app using the legacy broadcast intent
     * This is deprecated broadcast event, maintained for backward compatibility.
     */
    private void sendLegacyReportBroadcast(Context context, String appPackageName, HIDAccessoryInfo hidAccessoryInfo,
                                           byte[] data, Long sequence) {
        ArrayList<byte[]> reportBinaryDataList = new ArrayList<>();
        StringBuffer cardHexData = new StringBuffer();
        String stringData = null;
        int reportByteSize = data.length;
        reportBinaryDataList.add(data);

        for (byte a : data) {
            if (a == 0x0d && isHPCardReader(hidAccessoryInfo.getVendorId(), hidAccessoryInfo.getProductId())) {
                stringData = cardHexData.toString();
                break;
            }
            cardHexData.append((char) a);
        }

        if (!isHPCardReader(hidAccessoryInfo.getVendorId(), hidAccessoryInfo.getProductId())) {
            stringData = cardHexData.toString();
        }

        if (stringData != null && !stringData.isEmpty()) {
            final Intent tempIntent = new Intent(Accessorylet.ACCESSORY_REPORT_ACTION);
            tempIntent.putExtra(Accessorylet.Keys.ACCESSORY_INFO_VENDER_ID, hidAccessoryInfo.getVendorId());
            tempIntent.putExtra(Accessorylet.Keys.ACCESSORY_INFO_VENDOR_ID, hidAccessoryInfo.getVendorId());
            tempIntent.putExtra(Accessorylet.Keys.ACCESSORY_INFO_PRODUCT_ID, hidAccessoryInfo.getProductId());
            tempIntent.putExtra(Accessorylet.Keys.ACCESSORY_INFO_SERIAL_NUMBER, hidAccessoryInfo.getSerialNumber());
            tempIntent.putExtra(Accessorylet.Keys.ACCESSORY_REPORT_INFO_ORDINAL, sequence);
            tempIntent.putExtra(Accessorylet.Keys.ACCESSORY_REPORT_INFO_TIMESTAMP,
                    Calendar.getInstance().getTime().toString());
            tempIntent.putExtra(Accessorylet.Keys.ACCESSORY_REPORT_INFO_DATA, stringData);
            tempIntent.putExtra(Accessorylet.Keys.ACCESSORY_REPORT_INFO_BYTE_ARRAY_LIST, reportBinaryDataList);
            tempIntent.putExtra(Accessorylet.Keys.ACCESSORY_REPORT_INFO_COUNT, reportBinaryDataList.size());
            tempIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            tempIntent.setPackage(appPackageName);
            context.sendBroadcast(tempIntent);

            SLog.d(TAG, "Send Broadcast data : [" + sequence + "], data size [" + reportByteSize + "]");
            reportBinaryDataList.clear();
        }
    }

    private boolean isHPCardReader(int vendorId, int productId) {
        return vendorId == HP_VENDOR_ID && productId == HP_PRODUCT_ID;
    }
}
