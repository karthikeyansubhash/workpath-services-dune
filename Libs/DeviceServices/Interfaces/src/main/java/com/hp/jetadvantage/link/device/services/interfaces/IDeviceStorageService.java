/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */

package com.hp.jetadvantage.link.device.services.interfaces;

import com.hp.ws.cdm.storage.RemovableDevice;
import com.hp.ws.websocket.SystemManagementMessage;

import java.util.List;

/**
 * Device Service Interface : Storage Service
 */
public interface IDeviceStorageService {
    public List<RemovableDevice> getStorages();

    /**
     * Register a callback to receive app installation or uninstallation events.
     * @param callback
     */
    void registerAppInstallUninstallCallback(IAppInstallUninstallCallback callback);

    /**
     * Unregister a callback that is receiving app installation or uninstallation events.
     * @param callback
     */
    void unregisterAppInstallUninstallCallback(IAppInstallUninstallCallback callback);

    void registerSystemStateChangeCallback(SystemManagementMessage.SystemState type, IDeviceSystemStateChangeCallback callback);
    void unRegisterSystemStateChangeCallback();
}
