package com.hp.jetadvantage.link.device.services.sim;

import com.hp.jetadvantage.link.device.services.interfaces.IAppInstallUninstallCallback;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceStorageService;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceSystemStateChangeCallback;
import com.hp.ws.cdm.storage.RemovableDevice;
import com.hp.ws.websocket.SystemManagementMessage;

import java.util.List;

public class SimDeviceStorageService implements IDeviceStorageService {
    @Override
    public List<RemovableDevice> getStorages() {
        return null;
    }

    @Override
    public void registerAppInstallUninstallCallback(IAppInstallUninstallCallback callback) {

    }

    @Override
    public void unregisterAppInstallUninstallCallback(IAppInstallUninstallCallback callback) {

    }

    @Override
    public void registerSystemStateChangeCallback(SystemManagementMessage.SystemState type, IDeviceSystemStateChangeCallback callback) {

    }

    @Override
    public void unRegisterSystemStateChangeCallback() {

    }

}
