package com.hp.jetadvantage.link.services.storagelet.service;

import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceStorageService;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceStorageService;
import com.hp.ws.websocket.SystemManagementMessage;

public class StorageServiceManager {
    public static final String TAG = "StorageServiceManager";
    private final IDeviceStorageService mStorageService;
    private final MassStorageCallbackHandler mSystemCallbackHandler;


    StorageServiceManager(MassStorageCallbackHandler callback) {
        mStorageService = new StandardDeviceStorageService();
        mSystemCallbackHandler = callback;
    }

    public void registerAppInstallUninstallCallback(){
        SLog.i(TAG, "registerAppInstallUninstallCallback");
        mStorageService.registerAppInstallUninstallCallback(mSystemCallbackHandler.appInstallUninstallCallback);
    }

    public void unregisterAppInstallUninstallCallback(){
        SLog.i(TAG, "unregisterAppInstallUninstallCallback");
        mStorageService.unregisterAppInstallUninstallCallback(mSystemCallbackHandler.appInstallUninstallCallback);
    }

    public void registerSystemStateCallback() {
        SLog.i(TAG, "registerSystemStateCallback");
        mStorageService.registerSystemStateChangeCallback(SystemManagementMessage.SystemState.SCE_USB_STORAGE_EVENT, mSystemCallbackHandler.systemStateChangeCallback);
    }

    public void unregisterSystemStateCallback() {
        SLog.i(TAG, "unregisterSystemStateCallback");
        mStorageService.unRegisterSystemStateChangeCallback();
    }
}
