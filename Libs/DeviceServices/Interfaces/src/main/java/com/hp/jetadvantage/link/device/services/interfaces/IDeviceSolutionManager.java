package com.hp.jetadvantage.link.device.services.interfaces;

import com.hp.ext.service.solutionManager.Configuration;
import com.hp.ext.service.solutionManager.Configuration_Modify;
import com.hp.ext.types.solutionManager.SolutionNotification;
import com.hp.jetadvantage.link.device.services.exceptions.IllegalSolutionException;

import java.io.InputStream;

public interface IDeviceSolutionManager {
    /**
     * Get the solution's configuration on the device.
     *
     * @param appPackageName The package name of the solution
     * @return The solution's configuration
     * @throws IllegalSolutionException if the solution is not installed or access token is invalid
     */
    Configuration getConfiguration(String appPackageName) throws IllegalSolutionException;

    /**
     * Modify the solution's configuration on the device.
     *
     * @param appPackageName The package name of the solution
     * @param configuration  The configuration to be modified
     * @return The modified solution's configuration
     * @throws IllegalSolutionException if the solution is not installed or access token is invalid
     */
    Configuration modifyConfiguration(String appPackageName, Configuration_Modify configuration) throws IllegalSolutionException;

    /**
     * Get the solution's configuration data on the device.
     *
     * @param appPackageName The package name of the solution
     * @return The solution's configuration data (JSON string)
     * @throws IllegalSolutionException if the solution is not installed or access token is invalid
     */
    String getConfigurationData(String appPackageName) throws IllegalSolutionException;

    /**
     * Replace the solution's configuration data on the device.
     *
     * @param appPackageName The package name of the solution
     * @param data           The configuration data to be replaced (JSON string)
     * @throws IllegalSolutionException if the solution is not installed or access token is invalid
     */
    void replaceConfigurationData(String appPackageName, InputStream data) throws IllegalSolutionException;

    /**
     * Register a callback to receive solution notifications.
     *
     * @param callback The callback to be registered
     */
    void registerNotificationCallback(IE2PayloadCallback<SolutionNotification> callback);

    /**
     * Unregister the callback to stop receiving solution notifications.
     */
    void unRegisterNotificationCallback();
}
