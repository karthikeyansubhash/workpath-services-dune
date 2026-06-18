/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.standard;

import com.hp.jetadvantage.link.device.services.interfaces.IAppInstallUninstallCallback;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceStorageService;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceSystemStateChangeCallback;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.standard.common.DeviceManagementService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceService;
import com.hp.ws.cdm.storage.RemovableDevice;
import com.hp.ws.cdm.storage.RemovableDevices;
import com.hp.ws.websocket.SystemManagementMessage;

import java.util.List;

public class StandardDeviceStorageService extends StandardDeviceService implements IDeviceStorageService {
    private static final String TAG = Constants.TAG + "/Storage";

    private SystemStateCallbackRegistry callbackRegistry;

    public StandardDeviceStorageService() {
        super();
        callbackRegistry = new SystemStateCallbackRegistry();
    }

    public StandardDeviceStorageService(DeviceManagementService deviceManagementService) {
        super(deviceManagementService);
    }

    @Override
    public List<RemovableDevice> getStorages() {
        CdmCall call = () -> getCDMClient().sendGetRequest(CDMUrl.STORAGE_DEVICES_DUNE);
        RemovableDevices response = perform(call, RemovableDevices.class);
        return response.getDevices();
    }

    public static final class CDMUrl {
        public static final String STORAGE_DEVICES_DUNE = "/cdm/storageDevices/v1/removableDevices";
    }

    @Override
    public void registerAppInstallUninstallCallback(IAppInstallUninstallCallback callback) {
        if (callback != null) {
            deviceManagementService.registerAppInstallUninstallCallback(callback);
        }
    }

    @Override
    public void unregisterAppInstallUninstallCallback(IAppInstallUninstallCallback callback) {
        if (callback != null) {
            deviceManagementService.unregisterAppInstallUninstallCallback(callback);
        }
    }

    @Override
    public void registerSystemStateChangeCallback(SystemManagementMessage.SystemState type, IDeviceSystemStateChangeCallback listener) {
        callbackRegistry.registerSystemStateChangeCallback(type, listener);
    }

    @Override
    public void unRegisterSystemStateChangeCallback() {
        callbackRegistry.unRegisterSystemStateChangeCallback();
    }
}
