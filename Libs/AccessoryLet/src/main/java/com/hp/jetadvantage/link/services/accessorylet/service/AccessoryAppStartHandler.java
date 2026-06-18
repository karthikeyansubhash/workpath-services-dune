/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.accessorylet.service;

import static com.hp.jetadvantage.link.common.constants.LogConstants.TAG_ACCESSORY_SERVICE;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hp.jetadvantage.link.api.accessory.AbstractAccessoryService;
import com.hp.jetadvantage.link.api.accessory.hid.Accessorylet;
import com.hp.jetadvantage.link.services.accessorylet.util.AccessoryRegistrationRecord;

import java.util.concurrent.ConcurrentHashMap;

/**
 * AccessoryAppStartHandler class is responsible for sending the
 * {@link com.hp.jetadvantage.link.api.accessory.AbstractAccessoryService.AccessoryMessage#START} message to apps
 * that have owned accessories when the Workpath Accessory Service is started after boot-up is completed.
 * The Workpath library in the app will receive the message and call the
 * {@link com.hp.jetadvantage.link.api.accessory.AbstractAccessoryService .onStart()} callback.
 * <p>
 * The interaction (binding service and sending the message) with the app was defined by legacy SDK and cannot be
 * changed to maintain backward compatibility with existing apps.
 */
public class AccessoryAppStartHandler extends Handler {
    private static final int SEND_START_MESSAGE = 1;
    private static final String TAG = TAG_ACCESSORY_SERVICE + "/SVC/AppStart";
    private static final String MSG_DATA_INTENT = "dataIntent";
    private final Context mContext;

    /**
     * Map to store the status of the app start handler for each app.
     * Key: appPackageName, Value: AppStartHandlerStatus
     */
    private final ConcurrentHashMap<String, AppStartHandlerStatus> handlerStatusMap =
            new ConcurrentHashMap<>();

    AccessoryAppStartHandler(Context context, Looper looper) {
        super(looper);
        mContext = context;
    }

    /////////////////// Override methods from Handler ///////////////////

    @Override
    public void handleMessage(@NonNull Message msg) {
        try {
            switch (msg.what) {
                case SEND_START_MESSAGE:
                    handleStartMessage(msg);
                    break;
                default:
                    Log.e(TAG, "handleMessage : unknown message : " + msg.what);
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to handle message " + msg.what, e);
        }
    }

    /////////////////// Protected methods from Handler ///////////////////

    protected AppStartHandlerStatus getProgress(String appPackageName) {
        AppStartHandlerStatus status = handlerStatusMap.get(appPackageName);
        if (status == null) {
            status = AppStartHandlerStatus.NOT_STARTED;
        }
        return status;
    }

    /**
     * Send a app start message to the handler thread to trigger the app's AbstractAccessoryService.onStart() callback.
     * This method may be called from callback thread or main thread, so it should not block the caller.
     *
     * @param appPackageName app's package name
     */
    protected void sendAppStartMessage(String appPackageName) {
        Log.d(TAG, "sendAppStartMessage: [AppInstall] ENTER " + getSuffixLog(appPackageName));
        try {
            if (appPackageName != null) {
                if (getProgress(appPackageName) != AppStartHandlerStatus.NOT_STARTED) {
                    Log.i(TAG, "sendAppStartMessage: [AppInstall] already started " + getSuffixLog(appPackageName));
                    return;
                }
                Intent intent = new Intent(Accessorylet.PROVIDER_EVENT_ACTION);
                intent.setPackage(appPackageName);

                Bundle bundle = new Bundle(1);
                bundle.putParcelable(MSG_DATA_INTENT, intent);

                Message msg = this.obtainMessage(SEND_START_MESSAGE, AbstractAccessoryService.AccessoryMessage.START,
                        0, bundle);
                this.sendMessage(msg);
                setHandlerStatus(appPackageName, AppStartHandlerStatus.STARTED, "sendAppStartMessage");
                Log.d(TAG, "sendAppStartMessage: [AppInstall] sent message to handler " +
                        getSuffixLog(appPackageName));
            } else {
                Log.e(TAG, "sendAppStartMessage: [AppInstall] agentPackageName is null");
            }
        } catch (Exception e) {
            Log.e(TAG, "sendAppStartMessage: [AppInstall] failed to send message. " + e.getMessage(), e);
        }
        Log.d(TAG, "sendAppStartMessage: [AppInstall] EXIT " + getSuffixLog(appPackageName));
    }

    protected void cleanup(String packageName) {
        if (packageName != null) {
            handlerStatusMap.remove(packageName);
        }
    }

    protected boolean hasAppOwnedAccessories(String packageName) {
        return AccessoryRegistrationRecord.hasAppOwnedAccessories(mContext, packageName);
    }

    /////////////////// Private methods from Handler ///////////////////

    /**
     * bind to App's AbstractAccessoryService
     */
    private void bindToAbstractAccessoryService(Intent intent, int messageCode, String packageName) {
        Log.d(TAG, "bindToAbstractAccessoryService: ENTER");
        if (hasAppOwnedAccessories(packageName)) {
            Log.d(TAG, "bindToAbstractAccessoryService: bindService to " + getSuffixLog(packageName));
            setHandlerStatus(packageName, AppStartHandlerStatus.IN_PROGRESS, "bindToAbstractAccessoryService");
            boolean result = mContext.bindService(intent, new AccessoryServiceConnection(messageCode),
                    Context.BIND_AUTO_CREATE);

            if (result) {
                Log.d(TAG, "bindToAbstractAccessoryService: bindService success " + getSuffixLog(packageName));
            } else {
                Log.e(TAG, "bindToAbstractAccessoryService: bindService failed " + getSuffixLog(packageName));
                setHandlerStatus(packageName, AppStartHandlerStatus.FAILED, "bindToAbstractAccessoryService");
            }
        } else {
            setHandlerStatus(packageName, AppStartHandlerStatus.NOT_OWNED, "bindToAbstractAccessoryService");
        }
        Log.d(TAG, "bindToAbstractAccessoryService: EXIT " + getSuffixLog(packageName));
    }

    private String getSuffixLog(String appPackageName) {
        return "[" + getProgress(appPackageName) + ", " + appPackageName + "]";
    }

    private void handleStartMessage(Message msg) {
        Log.d(TAG, "handleStartMessage: ENTER");
        Bundle data = (Bundle) msg.obj;
        Intent intent = data.getParcelable(MSG_DATA_INTENT);
        if (intent == null) {
            Log.e(TAG, "handleStartMessage: empty intent");
            return;
        }
        String packageName = intent.getPackage();
        if (packageName == null || packageName.isEmpty()) {
            Log.e(TAG, "handleStartMessage: empty packageName");
            return;
        }
        int messageCode = msg.arg1;

        bindToAbstractAccessoryService(intent, messageCode, packageName);
        Log.d(TAG, "handleStartMessage: EXIT");
    }

    private void setHandlerStatus(String appPackageName, AppStartHandlerStatus status, String prefixLog) {
        handlerStatusMap.put(appPackageName, status);
        Log.d(TAG, prefixLog + ": setState: " + getSuffixLog(appPackageName));
    }

    ////////////////// Inner enum and Inner class ///////////////////
    public enum AppStartHandlerStatus {
        NOT_STARTED,    //AppStartHandler is not started for the app
        STARTED,        //AppStartHandler has started for the app
        IN_PROGRESS,    //AppStartHandler is in progress, binding to the app and sending a start message
        COMPLETED,      //AppStartHandler is completed sending the start message to the app
        FAILED,         //AppStartHandler is failed to send start message to the app
        NOT_OWNED       //The app does not have any owned accessories, stopped.
    }

    private class AccessoryServiceConnection implements ServiceConnection {
        private final int messageCode;

        AccessoryServiceConnection(int messageCode) {
            this.messageCode = messageCode;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: ENTER " + name);
            Messenger mMessenger = new Messenger(service);
            Message msg = Message.obtain(null, messageCode, 0, 0);

            // Send message to an App to trigger App's AbstractAccessoryService.onStart() callback
            try {
                mMessenger.send(msg);
                Log.d(TAG, "onServiceConnected : SEND_START_MESSAGE to " + name);
            } catch (RemoteException e) {
                Log.e(TAG, "onServiceConnected: Failed to send message to app " + e.getMessage(), e);
            } catch (Exception e) {
                Log.e(TAG, "onServiceConnected: Failed to send message to app " + e.getMessage(), e);
            }

            unbindService(name);

            if (name != null) {
                setHandlerStatus(name.getPackageName(), AppStartHandlerStatus.COMPLETED, "onServiceConnected");
                Log.d(TAG, "onServiceConnected: unbindService done " + getSuffixLog(name.getPackageName()));
            }
            Log.d(TAG, "onServiceConnected: EXIT " + name);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected: ENTER " + name);
            unbindService(name);
            if (name != null)
                setHandlerStatus(name.getPackageName(), AppStartHandlerStatus.COMPLETED, "onServiceDisconnected");
            Log.i(TAG, "onServiceDisconnected: EXIT " + name);
        }

        @Override
        public void onBindingDied(ComponentName name) {
            Log.i(TAG, "onBindingDied: ENTER " + name);
            unbindService(name);
            if (name != null) setHandlerStatus(name.getPackageName(), AppStartHandlerStatus.FAILED, "onBindingDied");
            Log.i(TAG, "onBindingDied: EXIT " + name);
        }

        @Override
        public void onNullBinding(ComponentName name) {
            Log.i(TAG, "onNullBinding: ENTER " + name);
            unbindService(name);
            if (name != null) setHandlerStatus(name.getPackageName(), AppStartHandlerStatus.FAILED, "onNullBinding");
            Log.i(TAG, "onNullBinding: EXIT " + name);
        }

        private void unbindService(ComponentName name) {
            try {
                mContext.unbindService(this);
            } catch (Exception e) {
                Log.e(TAG, "unbindService: Failed to unbindService from " + name + e.getMessage(), e);
            }
        }
    }
}
