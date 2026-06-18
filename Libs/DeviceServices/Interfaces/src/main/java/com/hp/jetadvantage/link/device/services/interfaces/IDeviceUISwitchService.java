/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.interfaces;

/**
 * Device Service Interface : UI Switching service
 */
public interface IDeviceUISwitchService {
    public void switchToDevice();

    /**
     * Request to launch an app from the device home screen (SPICE in DUNE)
     * The main purpose is to run API tests by launching the API test app from the device home screen to acquire UIContext of the app.
     *
     * @param packageName package name of the app to be launched
     */
    public boolean launchAppFromDeviceHome(String packageName);

    /**
     * Request to close an app from the device home screen (SPICE in DUNE)
     * The main purpose is to release UIContext of the app after API tests are done.
     * @param packageName package name of the app to be closed
     */
    public boolean closeAppFromDeviceHome(String packageName);
}
