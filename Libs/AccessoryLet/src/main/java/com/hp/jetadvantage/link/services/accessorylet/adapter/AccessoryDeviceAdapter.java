/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.accessorylet.adapter;

import static com.hp.jetadvantage.link.common.constants.LogConstants.TAG_ACCESSORY_SERVICE;

import android.util.Log;

import androidx.annotation.NonNull;

import com.hp.ext.service.usbAccessories.Accessories;
import com.hp.ext.service.usbAccessories.Accessories_Accessory_Hid_OpenHIDAccessory_ReadReport_Params;
import com.hp.ext.service.usbAccessories.Accessories_Accessory_Hid_OpenHIDAccessory_WriteReport_Params;
import com.hp.ext.service.usbAccessories.Accessory;
import com.hp.ext.service.usbAccessories.OpenHIDAccessory;
import com.hp.ext.service.usbAccessories.OpenHIDAccessory_ReadReport;
import com.hp.ext.service.usbAccessories.OpenHIDAccessory_WriteReport;
import com.hp.ext.service.usbAccessories.RegistrationKind;
import com.hp.ext.service.usbAccessories.UsbData;
import com.hp.ext.types.protocol.Unsigned16;
import com.hp.ext.types.usb.HidReportType;
import com.hp.jetadvantage.link.api.accessory.AccessoryInfo;
import com.hp.jetadvantage.link.api.accessory.RegistrationType;
import com.hp.jetadvantage.link.api.accessory.hid.HIDAccessoryInfo;
import com.hp.jetadvantage.link.api.accessory.hid.HIDInfo;
import com.hp.jetadvantage.link.api.accessory.hid.HIDReport;
import com.hp.jetadvantage.link.api.accessory.hid.HIDReportType;
import com.hp.jetadvantage.link.common.utils.StringUtility;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceAccessoryService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * Adapter class to convert the data types between the Workpath and E2 USB accessory services.
 */
public class AccessoryDeviceAdapter {
    private static final String TAG = TAG_ACCESSORY_SERVICE + "/DA";
    private static final int READ_REPORT_LENGTH = 1024;

    public static boolean isSupported(IDeviceAccessoryService deviceService) {
        return deviceService.isSupported();
    }

    public static boolean isReady(IDeviceAccessoryService deviceService) {
        return deviceService.isReady();
    }

    /**
     * Get the accessory ID of the owned accessory from the IDeviceAccessoryService.
     * Notes: This method does not check if the accessory is registered for the given app. You can check the
     * registration with AccessoryRegistrationRecord.isOwnedAccessoryRegisteredByApp() if needed.
     *
     * @return the accessory resource ID of the owned accessory, or null if the accessory is not found in the device
     */
    public static String getOwnedAccessoryId(IDeviceAccessoryService deviceService, String packageName,
                                             HIDAccessoryInfo hidAccessoryInfo) {
        return getAccessoryId(deviceService, packageName, hidAccessoryInfo, RegistrationKind.RkOwned);
    }

    /**
     * Get the accessory ID of the shared accessory.
     * Notes: This method does not check if the accessory is registered for the given app. You can check the
     * registration with AccessoryRegistrationRecord.isSharedAccessoryRegisteredByApp() if needed.
     *
     * @return the accessory resource ID of the shared accessory, or null if the accessory is not found in the device
     */
    public static String getSharedAccessoryId(IDeviceAccessoryService deviceService,
                                              String packageName, HIDAccessoryInfo hidAccessoryInfo) {
        return getAccessoryId(deviceService, packageName, hidAccessoryInfo, RegistrationKind.RkShared);
    }

    /**
     * Get HIDAccessoryInfo for the given accessory ID.
     *
     * @return HIDAccessoryInfo for the given accessory ID, or null if the accessory is not found in the device
     */
    public static HIDAccessoryInfo getHidAccessoryInfo(IDeviceAccessoryService deviceService, String packageName,
                                                       String accessoryId) {
        if (deviceService == null) {
            return null;
        }
        Accessory e2UsbAccessory = deviceService.getAccessory(packageName, accessoryId);
        return convertEtoW(e2UsbAccessory);
    }

    public static HIDInfo getOpenHidInfo(IDeviceAccessoryService deviceService, String packageName,
                                         UUID openHidId, String accessoryId) {
        if (deviceService == null || StringUtility.isEmpty(packageName) || openHidId == null ||
                StringUtility.isEmpty(accessoryId)) {
            Log.e(TAG, "getOpenHidInfo: invalid parameters");
            return null;
        }
        OpenHIDAccessory openHidAccessory = deviceService.getOpenHidAccessoryInfo(packageName, openHidId, accessoryId);
        return convertEtoW(openHidAccessory);
    }

    public static ArrayList<AccessoryInfo> getOwnedAccessories(IDeviceAccessoryService deviceService,
                                                               String packageName) {
        Accessories accessories = deviceService.getAccessories(packageName);
        ArrayList<AccessoryInfo> accessoryInfoList = new ArrayList<>();
        for (com.hp.ext.service.usbAccessories.Accessory accessory : accessories.getMembers()) {
            if (accessory.getRegistration().getValue().equals(RegistrationKind.RkOwned.getValue())) {
                AccessoryInfo accessoryInfo = convertEtoW(accessory);
                accessoryInfoList.add(accessoryInfo);
            }
        }
        return accessoryInfoList;
    }

    /**
     * Get a list of all attached shared USB Accessories from E2 and convert them to Workpath data type
     *
     * @return a list of all attached shared USB Accessories on the device with Workpath data type
     */
    public static ArrayList<AccessoryInfo> getSharedAccessories(IDeviceAccessoryService deviceService,
                                                                String packageName) {
        Accessories accessories = deviceService.getAccessories(packageName);
        ArrayList<AccessoryInfo> accessoryInfoList = new ArrayList<>();
        for (com.hp.ext.service.usbAccessories.Accessory accessory : accessories.getMembers()) {
            if (accessory.getRegistration().getValue().equals(RegistrationKind.RkShared.getValue())) {
                AccessoryInfo accessoryInfo = convertEtoW(accessory);
                accessoryInfoList.add(accessoryInfo);
            }
        }
        return accessoryInfoList;
    }

    public static ArrayList<AccessoryInfo> enumerateAccessories(IDeviceAccessoryService deviceService,
                                                                String packageName) {
        Accessories accessories = deviceService.getAccessories(packageName);
        ArrayList<AccessoryInfo> accessoryInfoList = new ArrayList<>();
        for (com.hp.ext.service.usbAccessories.Accessory accessory : accessories.getMembers()) {
            AccessoryInfo accessoryInfo = convertEtoW(accessory);
            accessoryInfoList.add(accessoryInfo);
        }
        return accessoryInfoList;
    }

    /**
     * Read a HID report from the accessory synchronously.
     *
     * @return return null if failed to read the report, otherwise return the HIDReport
     */
    public static HIDReport readSyncReport(@NonNull IDeviceAccessoryService deviceService, String packageName,
                                           @NonNull UUID openHidId, String accessoryId, HIDReportType reportType,
                                           boolean isOwned, byte reportId) {

        if (deviceService == null || StringUtility.isEmpty(packageName) || openHidId == null || StringUtility.isEmpty(accessoryId)) {
            Log.e(TAG, "readSyncReport: invalid parameters");
            return null;
        }
        Accessories_Accessory_Hid_OpenHIDAccessory_ReadReport_Params readParams =
                new Accessories_Accessory_Hid_OpenHIDAccessory_ReadReport_Params();
        readParams.setReportId(new com.hp.ext.types.protocol.Byte(reportId));
        readParams.setReportType(convertWtoE(reportType));
        readParams.setAsync(false);
        readParams.setLength(new Unsigned16(READ_REPORT_LENGTH));

        OpenHIDAccessory_ReadReport readReport = deviceService.readReport(packageName, openHidId, accessoryId, isOwned,
                readParams);

        if (readReport != null) {
            String base64Data = readReport.getData().getValue();
            byte[] data = Base64.getDecoder().decode(base64Data);
            HIDReport returnReport = new HIDReport(reportType, data);

            Log.i(TAG, String.format("readSyncReport:[%s,%s]:%s", accessoryId, reportType, Arrays.toString(data)));
            Log.d(TAG, String.format("readSyncReport:[%s,%s]:%s", accessoryId, reportType, base64Data));
            Log.d(TAG, String.format("readOwnedSyncReport: length=%d, status=%s, accessoryId=%s, operationId=%s",
                    data.length, (readReport.getUsbTransferStatus() != null) ?
                            readReport.getUsbTransferStatus().getValue().toString() : "null",
                    accessoryId, readReport.getOperationId()));
            return returnReport;
        } else {
            Log.e(TAG, String.format("readSyncReport: failed [%s,%s,owned=%s,type=%s]",
                    openHidId, accessoryId, isOwned, reportType));
            return null;
        }
    }

    public static boolean writeSyncReport(@NonNull IDeviceAccessoryService deviceService, String packageName,
                                          @NonNull UUID openHidId, String accessoryId,
                                          @NonNull HIDReport requestedReport, boolean isOwned, byte reportId) {
        boolean result = false;
        if (deviceService == null || StringUtility.isEmpty(packageName) || openHidId == null || StringUtility.isEmpty(accessoryId)) {
            Log.e(TAG, "writeSyncReport: invalid parameters");
            return false;
        }
        if (requestedReport == null || requestedReport.getData() == null) {
            Log.e(TAG, "writeSyncReport: requestedReport is null or empty");
            return false;
        }
        String base64Data = Base64.getEncoder().encodeToString(requestedReport.getData());
        UsbData e2UsbData = new UsbData(base64Data);

        Accessories_Accessory_Hid_OpenHIDAccessory_WriteReport_Params writeParams =
                new Accessories_Accessory_Hid_OpenHIDAccessory_WriteReport_Params();

        writeParams.setReportId(new com.hp.ext.types.protocol.Byte(reportId));
        writeParams.setReportType(convertWtoE(requestedReport.getType()));
        writeParams.setAsync(false);
        writeParams.setData(e2UsbData);

        OpenHIDAccessory_WriteReport writeReport = deviceService.writeReport(packageName, openHidId, accessoryId,
                isOwned, writeParams);
        if (writeReport != null) {
            int length = (writeReport.getBytesWritten() != null) ? writeReport.getBytesWritten().getValue() : 0;

            Log.i(TAG, String.format("writeSyncReport:[%s,%s]:%s", accessoryId, requestedReport.getType(),
                    Arrays.toString(requestedReport.getData())));
            Log.d(TAG, String.format("writeSyncReport:[%s,%s]:%s", accessoryId, requestedReport.getType(), base64Data));
            Log.d(TAG, String.format("writeSyncReport: length=%d, status=%s, accessoryId=%s, operationId=%s",
                    length, (writeReport.getUsbTransferStatus() != null) ?
                            writeReport.getUsbTransferStatus().getValue().toString() : "null",
                    accessoryId, writeReport.getOperationId()));
            result = true;
        } else {
            Log.e(TAG, String.format("writeSyncReport: failed [%s,%s,%s,owned=%s,type=%s,]",
                    packageName, openHidId, accessoryId, isOwned, requestedReport.getType()));
            result = false;
        }
        return result;
    }

    /// /////////////////////////////// Private Methods /////////////////////////////////
    private static HIDAccessoryInfo convertEtoW(com.hp.ext.service.usbAccessories.Accessory e2Accessory) {
        if (e2Accessory == null) {
            return null;
        }

        int vendorId = Optional.ofNullable(e2Accessory.getVendorId())
                .map(v -> v.getValue().intValue())
                .orElse(0);
        int productId = Optional.ofNullable(e2Accessory.getProductId())
                .map(p -> p.getValue().intValue())
                .orElse(0);
        String serialNumber = Optional.ofNullable(e2Accessory.getSerialNumber())
                .map(s -> s.getValue())
                .orElse(null);
        String productName = Optional.ofNullable(e2Accessory.getProductName())
                .map(p -> p.getValue())
                .orElse(null);
        String manufacturerName = Optional.ofNullable(e2Accessory.getManufacturerName())
                .map(m -> m.getValue())
                .orElse(null);
        RegistrationType registrationType = (e2Accessory.getRegistration() != null &&
                e2Accessory.getRegistration() == RegistrationKind.RkOwned)
                ? RegistrationType.OWNED
                : RegistrationType.SHARED;

        return new HIDAccessoryInfo(vendorId, productId, serialNumber, productName, manufacturerName, registrationType);
    }

    private static HIDInfo convertEtoW(com.hp.ext.service.usbAccessories.OpenHIDAccessory openHidAccessory) {
        if (openHidAccessory == null) {
            return null;
        }

        int featureReportLength = Optional.ofNullable(openHidAccessory.getFeatureReportLength())
                .map(v -> v.getValue().intValue())
                .orElse(0);
        int inputReportLength = Optional.ofNullable(openHidAccessory.getInputReportLength())
                .map(p -> p.getValue().intValue())
                .orElse(0);
        int outputReportLength = Optional.ofNullable(openHidAccessory.getOutputReportLength())
                .map(s -> s.getValue().intValue())
                .orElse(0);
        boolean isReading = Optional.ofNullable(openHidAccessory.getReportReadingActive())
                .map(s -> s.booleanValue())
                .orElse(false);

        return new HIDInfo(featureReportLength, inputReportLength, outputReportLength, isReading);
    }

    private static com.hp.ext.types.usb.HidReportType convertWtoE(HIDReportType reportType) {
        com.hp.ext.types.usb.HidReportType e2ReportType = null;
        switch (reportType) {
            case FEATURE:
                e2ReportType = HidReportType.HrtFeature;
                break;
            case INPUT:
                e2ReportType = HidReportType.HrtInput;
                break;
            case OUTPUT:
                e2ReportType = HidReportType.HrtOutput;
                break;
            default:
                Log.e(TAG, "Unknown reportType: " + reportType);
                break;
        }
        return e2ReportType;
    }

    private static String findMatchingAccessoryId(Accessories accessories, HIDAccessoryInfo hidAccessoryInfo,
                                                  RegistrationKind registrationKind,
                                                  Predicate<Accessory> serialMatchPredicate) {
        return accessories.getMembers().stream()
                .filter(a -> a.getVendorId() != null && hidAccessoryInfo.getVendorId() == a.getVendorId().getValue().intValue())
                .filter(a -> a.getProductId() != null && hidAccessoryInfo.getProductId() == a.getProductId().getValue().intValue())
                .filter(serialMatchPredicate)
                .filter(a -> a.getRegistration().getValue().equals(registrationKind.getValue()))
                .findFirst()
                .map(a -> a.getAccessoryID().toString())
                .orElse(null);
    }

    private static String getAccessoryId(IDeviceAccessoryService deviceService, String packageName,
                                         HIDAccessoryInfo hidAccessoryInfo, RegistrationKind registrationKind) {
        Accessories accessories = deviceService.getAccessories(packageName);
        if (accessories == null || accessories.getMembers() == null) {
            return null;
        }

        // First try exact serial number matching
        String accessoryId = findMatchingAccessoryId(accessories, hidAccessoryInfo, registrationKind,
                a -> isSerialNumberExactlyMatching(hidAccessoryInfo, a));

        if (accessoryId == null) {
            // Fallback: consider null or empty serial numbers as matching.
            accessoryId = findMatchingAccessoryId(accessories, hidAccessoryInfo, registrationKind,
                    a -> isSerialNumberMatching(hidAccessoryInfo, a));
        }
        return accessoryId;
    }

    private static boolean isSerialNumberExactlyMatching(HIDAccessoryInfo hidAccessoryInfo,
                                                         com.hp.ext.service.usbAccessories.Accessory accessory) {
        String e2UsbSerial = accessory.getSerialNumber() != null ? accessory.getSerialNumber().getValue() : null;
        String wHidSerial = hidAccessoryInfo.getSerialNumber();

        // If both serial numbers are null or empty, they are considered matching
        if (isNullOrEmpty(e2UsbSerial) && isNullOrEmpty(wHidSerial)) {
            return true;
        }
        // If only one of the serial numbers is null or empty, they are not matching
        if (isNullOrEmpty(e2UsbSerial) || isNullOrEmpty(wHidSerial)) {
            return false;
        }
        return e2UsbSerial.equalsIgnoreCase(wHidSerial);
    }

    private static boolean isSerialNumberMatching(HIDAccessoryInfo hidAccessoryInfo,
                                                  com.hp.ext.service.usbAccessories.Accessory accessory) {
        String e2UsbSerial = accessory.getSerialNumber() != null ? accessory.getSerialNumber().getValue() : null;
        String wHidSerial = hidAccessoryInfo.getSerialNumber();

        // If either serial is null, empty, or only whitespace, treat them as matching.
        if (isNullOrEmpty(e2UsbSerial) || isNullOrEmpty(wHidSerial)) {
            return true;
        }
        return e2UsbSerial.equalsIgnoreCase(wHidSerial);
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
