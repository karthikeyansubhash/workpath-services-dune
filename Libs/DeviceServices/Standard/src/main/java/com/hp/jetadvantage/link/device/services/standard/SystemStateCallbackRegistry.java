package com.hp.jetadvantage.link.device.services.standard;

import android.util.Log;

import com.hp.jetadvantage.link.device.services.interfaces.IDeviceSystemStateChangeCallback;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.standard.services.SystemManagementMessageHandler;
import com.hp.ws.websocket.SystemManagementMessage;

public class SystemStateCallbackRegistry {
    private static final String TAG = Constants.TAG + "/State/CR";
    private IDeviceSystemStateChangeCallback storageChangeCallback;

    protected synchronized void registerSystemStateChangeCallback(SystemManagementMessage.SystemState type, IDeviceSystemStateChangeCallback callback) {
        Log.d(TAG, "registerSystemStateChangeCallback: ENTER");
        if (storageChangeCallback != null) {
            Log.w(TAG, "registerSystemStateChangeCallback: Callback already registered, unregistering first.");
            unRegisterSystemStateChangeCallback();
        }
        storageChangeCallback = callback;
        SystemManagementMessageHandler.addSystemStateChangeListener(type, callback);
        Log.d(TAG, "registerSystemStateChangeCallback: EXIT");
    }

    protected synchronized void unRegisterSystemStateChangeCallback() {
        Log.d(TAG, "unRegisterSystemStateChangeCallback: ENTER");
        if (storageChangeCallback != null) {
            SystemManagementMessageHandler.removeSystemStateChangeListener(storageChangeCallback);
        }
        Log.d(TAG, "unRegisterSystemStateChangeCallback: EXIT");
    }


}
