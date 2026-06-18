/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */

package com.hp.jetadvantage.link.device.services.standard.common;

import android.content.Context;

import androidx.annotation.VisibleForTesting;

import com.hp.ext.types.common.ServicesDiscoveryImpl;
import com.hp.jetadvantage.link.device.services.clients.CDMClient;
import com.hp.jetadvantage.link.device.services.interfaces.IAppInstallUninstallCallback;

import java.util.List;

public interface DeviceManagementService {
    void initialize(String ip, String token);

    /**
     * Initialize DeviceManagementService
     * This method is used to initialize device info (ip, token) when DEVICE_READY BR is received from System App
     * Context is needed to cache the device info in the app's secure storage
     *
     * @param context application context of the SDK services
     * @param ip      device IP address
     * @param token   device access token
     */
    void initialize(Context context, String ip, String token);

    void updateDeviceInfo(String ip, String token);

    /**
     * Update device info
     * This method is used when token is updated from the System App
     *
     * @param context application context of the SDK services
     * @param ip      device IP address
     * @param token   device access token
     */
    void updateDeviceInfo(Context context, String ip, String token);

    /**
     * Re-initialize DeviceManagementService with cached device info
     * This method would be called in case that the SDK service process is stopped by the Android unexpectedly and
     * restarted
     *
     * @param context application context of the SDK services
     */
    void reInitialize(Context context);

    void clearSolutionTokenCache(String solutionId);

    /**
     * Get the agent ID of the the app's package from the PACMAN
     *
     * @param packageName application's package name
     * @param agentType   E2 Agent registration Record Type GUN
     * @return agent ID
     */
    String getAgentId(String packageName, String agentType);

    /**
     * Get the callback list for app install events if the app is relevant to the registered callbacks with
     * the app's registration record
     *
     * @param packageName the package name of the app
     * @return callback list that is relevant to the app
     */
    List<IAppInstallUninstallCallback> getAppInstallCallbacks(String packageName);

    /**
     * Get the callback list for app uninstall events.
     * Returns all registered callbacks as we cannot get any registration record info for the already uninstalled app.
     *
     * @param packageName the package name of the app
     * @return the list of all registered callbacks as we cannot get any registration record info for the already
     * uninstalled app
     */
    List<IAppInstallUninstallCallback> getAppUnInstallCallbacks(String packageName);

    CDMClient getCDMClient();

    String getDeviceIPAddress();

    ServicesDiscoveryImpl getDiscoveryTree();

    String getSolutionId(String packageName);

    String getSolutionToken(String packageName);

    String getUiContextToken(String packageName);

    UIContextTokenManager getUIContextTokenManager();

    @VisibleForTesting
    void setUIContextTokenManager(UIContextTokenManager uiContextTokenMgr);

    boolean hasUnauthorizedToken();

    boolean isDeviceConnected();

    /**
     * Register the app install/uninstall callback receiver.
     * The registered callback will be triggered when an app is installed if the app has the
     * agent registration record TypeGun in its registration record.
     * The callback will also be triggered when the app is uninstalled but for uninstallation we don't know the
     * registration record TypeGun of the uninstalled app so every uninstallation will trigger all the callbacks
     *
     * @param e2AgentRegistrationRecordTypeGun the E2 agent registration record TypeGun that you want to monitor for
     *                                         this callback
     * @param callback                         the callback to be triggered when the app is installed or uninstalled
     */
    void registerAppInstallUninstallReceiver(String e2AgentRegistrationRecordTypeGun,
                                             IAppInstallUninstallCallback callback);

    void unRegisterAppInstallUninstallReceiver(String e2AgentRegistrationRecordTypeGun);

    /**
     * Register the app install/uninstall callback receiver.
     * The registered callback will be triggered when any app is installed/uninstalled, regardless of its agent
     * registration record.
     * <p>
     * the callback registered with this method should be unregistered by using
     * {@link #unregisterAppInstallUninstallCallback(IAppInstallUninstallCallback callback)}
     *
     * @param callback
     */
    void registerAppInstallUninstallCallback(IAppInstallUninstallCallback callback);

    void unregisterAppInstallUninstallCallback(IAppInstallUninstallCallback callback);

    void setApplicationContext(Context applicationContext);

    @VisibleForTesting
    void setPackageManagerHelper(PackageManagerHelper mockPmHelper);
}
