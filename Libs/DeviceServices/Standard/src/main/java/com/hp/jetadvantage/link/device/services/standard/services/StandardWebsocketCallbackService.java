/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.standard.services;

import static com.hp.jetadvantage.link.device.services.standard.common.Constants.BOOT_TAG;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.hp.jetadvantage.link.device.services.clients.services.BaseWebsocketCallbackService;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceManagementService;
import com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelMessageHandler;
import com.hp.jetadvantage.link.system.IWebsocketCallbackService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class StandardWebsocketCallbackService extends BaseWebsocketCallbackService {

    private static final String TAG = Constants.TAG + "/S/WebsocketCB";
    private static final AtomicBoolean serviceConnected = new AtomicBoolean(false);
    private static final AtomicBoolean bindingInProgress = new AtomicBoolean(false);
    private static boolean testMode = false;
    private CdmPubMessageHandler cdmPubMessageHandler;
    private AppChannelMessageHandler appChannelMessageHandler;
    private GatewayMessageHandler gatewayMessageHandler;
    private SystemManagementMessageHandler systemMessageHandler;

    public StandardWebsocketCallbackService() {
        super("");
    }

    public static synchronized void start(final Context context) {
        Log.i(TAG, "start : ENTER");
        if (serviceConnected.get()) {
            Log.i(TAG, "start : already serviceConnected");
            return;
        }
        try {
            //TODO [DUNE-180785]
            //SpsPermissionHelper.ensurePermission(context);

            final Intent intent = new Intent(context, StandardWebsocketCallbackService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !testMode) {
                Log.i(TAG, "start : startForegroundService");
                context.startForegroundService(intent);
            } else {
                Log.i(TAG, "start : startService");
                context.startService(intent);
            }
        } catch (Throwable e) {
            Log.e(TAG, "start : Failed to start, exception=" + e.getMessage());
        }

        Log.i(TAG, "start : EXIT");
    }

    public static synchronized void start(final Context context, Boolean testOnly) {
        testMode = testOnly;
        start(context);
    }

    public static boolean getServiceConnected() {
        return serviceConnected.get();
    }

    @Override
    public void onReceived(int what, String data) {
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate : ENTER");
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !testMode) {
            ServiceNotification.showNotification(this);
        }
        cdmPubMessageHandler = new CdmPubMessageHandler();
        appChannelMessageHandler = new AppChannelMessageHandler(this);
        gatewayMessageHandler = new GatewayMessageHandler();
        systemMessageHandler = new SystemManagementMessageHandler();
        Log.d(TAG, "onCreate : EXIT");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand : ENTER");
        if (serviceConnected.get() || bindingInProgress.getAndSet(true)) {
            Log.i(TAG, "onStartCommand : already connected(" + serviceConnected.get() + ") or binding in progress");
            return START_STICKY;
        }

        this.connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.i(TAG, "onServiceConnected .. : ");
                try {
                    callbackService = IWebsocketCallbackService.Stub.asInterface(service);
                    callbackService.addCallback(cdmPubMessageHandler.callback, cdmPubMessageHandler.MESSAGE_TYPE);
                    callbackService.addCallback(appChannelMessageHandler.callback,
                            appChannelMessageHandler.MESSAGE_TYPE);
                    callbackService.addCallback(gatewayMessageHandler.callback, gatewayMessageHandler.MESSAGE_TYPE);
                    callbackService.addCallback(systemMessageHandler.callback, systemMessageHandler.MESSAGE_TYPE);
                    serviceConnected.set(true);
                    Log.i(BOOT_TAG, "WebsocketCallbackService -- OK");
                } catch (RemoteException e) {
                    Log.e(TAG, "onServiceConnected() RemoteException: " + e.getMessage());
                } finally {
                    bindingInProgress.set(false);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.i(TAG, "onServiceDisconnected .. : ");
                callbackService = null;
                serviceConnected.set(false);
                bindingInProgress.set(false);
            }
        };

        boolean bindServiceResult = bindWebSocketCallbackService();
        if(!bindServiceResult) {
            bindingInProgress.set(false);
            Log.e(TAG, "onServiceConnected: bindService failed");
        }

        /* When the process is stopped, the sticky service will be restarted soon by Android with empty device info.
           In that case, re-initialize StandardDeviceManagementService with cached device info */
        if (StandardDeviceManagementService.getInstance().getDeviceIPAddress() == null) {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(() -> {
                StandardDeviceManagementService.getInstance().reInitialize(getApplicationContext());
            });
        }

        Log.i(TAG, "onStartCommand : EXIT :" + bindServiceResult);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy() ENTER");
        try {
            if (callbackService != null) {
                callbackService.removeCallback(cdmPubMessageHandler.callback, cdmPubMessageHandler.MESSAGE_TYPE);
                callbackService.removeCallback(appChannelMessageHandler.callback,
                        appChannelMessageHandler.MESSAGE_TYPE);
                callbackService.removeCallback(gatewayMessageHandler.callback, gatewayMessageHandler.MESSAGE_TYPE);
                callbackService.removeCallback(systemMessageHandler.callback, systemMessageHandler.MESSAGE_TYPE);
            }
            super.onDestroy();
            serviceConnected.set(false);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        Log.i(TAG, "onDestroy() EXIT");
    }
}
