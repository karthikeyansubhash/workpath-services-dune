/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */

package com.hp.jetadvantage.link.device.services.interfaces;

/**
 * Provides the abstract device service interface that is common for all device services.
 */
public interface IAbstractDeviceService {
    /**
     * check if the UI context is available for the given package
     * When the app is launched from the device home screen, the UI context is available.
     *
     * @param packageName
     * @return
     */
    boolean isUiContextAvailable(String packageName);
}
