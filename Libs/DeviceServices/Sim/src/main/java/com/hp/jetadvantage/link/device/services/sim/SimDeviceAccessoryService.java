package com.hp.jetadvantage.link.device.services.sim;

import com.hp.ext.service.usbAccessories.Accessories;
import com.hp.ext.service.usbAccessories.Accessories_Accessory_Hid_OpenHIDAccessory_ReadReport_Params;
import com.hp.ext.service.usbAccessories.Accessories_Accessory_Hid_OpenHIDAccessory_WriteReport_Params;
import com.hp.ext.service.usbAccessories.Accessory;
import com.hp.ext.service.usbAccessories.OpenHIDAccessory;
import com.hp.ext.service.usbAccessories.OpenHIDAccessory_ReadReport;
import com.hp.ext.service.usbAccessories.OpenHIDAccessory_WriteReport;
import com.hp.ext.service.usbAccessories.UsbCallback;
import com.hp.ext.service.usbAccessories.UsbCallbackEnvelope;
import com.hp.ext.service.usbAccessories.UsbRegistrationPayload;
import com.hp.jetadvantage.link.device.services.interfaces.IAppInstallUninstallCallback;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceAccessoryService;
import com.hp.jetadvantage.link.device.services.interfaces.IE2AsyncIoCallback;
import com.hp.jetadvantage.link.device.services.interfaces.IE2ChannelSetupCallback;
import com.hp.jetadvantage.link.device.services.interfaces.IE2PayloadCallback;

import java.util.UUID;

public class SimDeviceAccessoryService implements IDeviceAccessoryService {

    @Override
    public boolean isAgentRegistered(String packageName) {
        return false;
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public boolean isSolutionRegistered(String packageName) {
        return false;
    }

    @Override
    public boolean isSupported() {
        return false;
    }

    @Override
    public UUID openOwnedHidAccessory(String packageName, String accessoryId) {
        return null;
    }

    @Override
    public UUID openSharedHidAccessory(String packageName, String accessoryId) {
        return null;
    }

    @Override
    public boolean startAsyncReading(String packageName, UUID openHidId, String accessoryId, boolean isOwned) {
        return false;
    }

    @Override
    public boolean stopAsyncReading(String packageName, UUID openHidId, String accessoryId, boolean isOwned) {
        return false;
    }

    @Override
    public OpenHIDAccessory_ReadReport readReport(String packageName, UUID openHidId, String accessoryId, boolean isOwned, Accessories_Accessory_Hid_OpenHIDAccessory_ReadReport_Params readReportRequest) {
        return null;
    }

    @Override
    public OpenHIDAccessory_WriteReport writeReport(String packageName, UUID openHidId, String accessoryId, boolean isOwned, Accessories_Accessory_Hid_OpenHIDAccessory_WriteReport_Params writeReportRequest) {
        return null;
    }

    @Override
    public boolean closeHidAccessory(String packageName, String accessoryId, UUID openHidId) {
        return false;
    }

    @Override
    public Accessories getAccessories(String packageName){
        return null;
    }

    @Override
    public Accessory getAccessory(String packageName, String accessoryId) {
        return null;
    }

    @Override
    public OpenHIDAccessory getOpenHidAccessoryInfo(String packageName, UUID openHidId, String accessoryId) {
        return null;
    }

    @Override
    public void registerNotificationCallback(IE2PayloadCallback<UsbRegistrationPayload> callback) {

    }

    @Override
    public void registerOperationCallback(IE2AsyncIoCallback<UsbCallback> callback) {

    }

    @Override
    public void registerUsbRegistrationChannelSetupCallback(IE2ChannelSetupCallback callback) {

    }

    @Override
    public void registerAppInstallUninstallCallback(IAppInstallUninstallCallback callback) {

    }

    @Override
    public void unRegisterNotificationCallback() {

    }

    @Override
    public void unRegisterAppChannelSetupCallback() {

    }

    @Override
    public void unRegisterAppInstallUninstallCallback() {

    }

    @Override
    public void unregisterOperationCallback() {

    }

    @Override
    public boolean isUiContextAvailable(String packageName) {
        return false;
    }
}
