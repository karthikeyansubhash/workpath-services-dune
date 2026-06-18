/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */

package com.hp.jetadvantage.link.device.services.interfaces;

import com.hp.ext.service.usbAccessories.Accessories;
import com.hp.ext.service.usbAccessories.Accessories_Accessory_Hid_OpenHIDAccessory_ReadReport_Params;
import com.hp.ext.service.usbAccessories.Accessories_Accessory_Hid_OpenHIDAccessory_WriteReport_Params;
import com.hp.ext.service.usbAccessories.Accessory;
import com.hp.ext.service.usbAccessories.OpenHIDAccessory;
import com.hp.ext.service.usbAccessories.OpenHIDAccessory_ReadReport;
import com.hp.ext.service.usbAccessories.OpenHIDAccessory_WriteReport;
import com.hp.ext.service.usbAccessories.UsbCallback;
import com.hp.ext.service.usbAccessories.UsbRegistrationPayload;

import java.util.UUID;

public interface IDeviceAccessoryService extends IAbstractDeviceService {

    /**
     * Close(Delete) a specified opened USB HID accessory
     *
     * @param packageName app's packageName
     * @param openHidId   openHidId
     * @return true if the accessory was successfully closed
     */
    boolean closeHidAccessory(String packageName, String accessoryId, UUID openHidId);

    /**
     * Get a list of all attached USB Accessories that are accessible via the E2 API
     *
     * @param packageName app's packageName
     * @return USB accessories
     */
    Accessories getAccessories(String packageName);

    /**
     * Get the USB accessory information by accessoryId from a connected E2 device
     *
     * @param packageName app's packageName
     * @param accessoryId resourceId for the accessory
     * @return Hid accessory information
     */
    Accessory getAccessory(String packageName, String accessoryId);

    /**
     * Get a specified USB HID accessory that has been opened.
     *
     * @param packageName app's packageName
     * @param openHidId   HID accessory ID that has been opened
     * @param accessoryId Accessory resource ID
     * @return
     */
    OpenHIDAccessory getOpenHidAccessoryInfo(String packageName, UUID openHidId, String accessoryId);

    /**
     * Check if the agent is registered on the Workpath pacman
     *
     * @param packageName
     * @return
     */
    boolean isAgentRegistered(String packageName);

    boolean isReady();

    /**
     * Check if the solution is registered on the Workpath pacman
     *
     * @return
     */
    boolean isSolutionRegistered(String packageName);

    boolean isSupported();

    /**
     * Open a specified attached USB HID accessory for an accessory that was registered as owned
     * This will return a openhid-id that can be used to communicate with the opened hid accessory
     *
     * @param packageName
     * @param accessoryId
     * @return openhid-id
     */
    java.util.UUID openOwnedHidAccessory(String packageName, String accessoryId);

    /**
     * Open a specified attached USB HID accessory for an accessory that was registered as shared
     * This will return a openhid-id that can be used to communicate with the opened hid accessory
     *
     * @param packageName
     * @param accessoryId
     * @return openhid-id
     */
    java.util.UUID openSharedHidAccessory(String packageName, String accessoryId);

    boolean startAsyncReading(String packageName, UUID openHidId, String accessoryId, boolean isOwned);

    boolean stopAsyncReading(String packageName, UUID openHidId, String accessoryId, boolean isOwned);

    /**
     * Read a report from a specified opened USB HID accessory's control pipe
     *
     * @param packageName app's package name
     * @param openHidId
     * @param accessoryId
     * @return
     */
    OpenHIDAccessory_ReadReport readReport(String packageName, UUID openHidId, String accessoryId, boolean isOwned,
                                           Accessories_Accessory_Hid_OpenHIDAccessory_ReadReport_Params readReportRequest);

    OpenHIDAccessory_WriteReport writeReport(String packageName, UUID openHidId, String accessoryId, boolean isOwned,
                                             Accessories_Accessory_Hid_OpenHIDAccessory_WriteReport_Params writeReportRequest);

    /**
     * Register callback to receive notification for USB accessory App channel setup events from E2 websocket callback
     *
     * @param callback
     */
    void registerUsbRegistrationChannelSetupCallback(IE2ChannelSetupCallback callback);

    /**
     * Register callback to receive notification for USB accessory App install/uninstall events
     * For install event, the callback will be triggered only for USB accessory apps
     * For uninstall events, we cannot filter only for USB accessory apps, so the callback will be triggered for all app
     * uninstall events
     *
     * @param callback
     */
    void registerAppInstallUninstallCallback(IAppInstallUninstallCallback callback);

    /**
     * Register callback to receive notification for USB accessory attached/detach events from E2 websocket callback
     *
     * @param callback
     */
    void registerNotificationCallback(IE2PayloadCallback<UsbRegistrationPayload> callback);

    void registerOperationCallback(IE2AsyncIoCallback<UsbCallback> callback);

    void unRegisterAppChannelSetupCallback();

    void unRegisterAppInstallUninstallCallback();

    void unregisterOperationCallback();

    void unRegisterNotificationCallback();
}
