/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.devicelet.adapter;

import static com.hp.jetadvantage.link.common.constants.LogConstants.TAG_DEVICE_SERVICE;

import android.util.Log;

import androidx.annotation.NonNull;

import com.hp.ext.service.device.DeploymentInformation;
import com.hp.ext.service.device.NetworkAdapterInfo;
import com.hp.ext.service.device.OwnerConfiguredInformation;
import com.hp.jetadvantage.link.api.device.DeviceAttribute;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceInfoService;
import com.hp.ws.cdm.controlpanel.Configuration;

import java.util.List;

/**
 * Adapter for retrieving device information from IDeviceInfoService.
 * Provides mapping between DeviceAttribute enum values and actual device data.
 */
public final class DeviceInfoAdapter {
    public static final String DEFAULT_NETWORK_ADAPTER_NAME = "eth0";
    public static final String HP_FUTURE_SMART_6 = "HP FutureSmart 6";
    public static final String HP_VENDOR = "HP";
    private static final String TAG = TAG_DEVICE_SERVICE + "/Adapter";


    private DeviceInfoAdapter() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Return a device attribute value queried from the provided deviceInfoService.
     *
     * @param deviceInfoService the service to query device information from
     * @param type              the type of device attribute to retrieve
     * @return the device attribute value, or null if not available or not implemented
     */
    public static String getDeviceInfo(@NonNull IDeviceInfoService deviceInfoService, DeviceAttribute type) {
        return switch (type) {
            case DA_NETWORK_MACADDRESS, DA_NETWORK_IPADDRESS, DA_NETWORK_HOSTNAME,
                 DA_ASSET_NUMBER, DA_COMPANY_CONTACT, DA_COMPANY_NAME,
                 DA_DEVICE_LOCATION, DA_MACHINE_NAME -> getDeploymentInformation(deviceInfoService, type);

            case DA_SYSTEM_MODELNAME, DA_SYSTEM_SERIALNUMBER, DA_SYSTEM_FIRMWARE_VERSION,
                 DA_SYSTEM_DEVICE_ID, DA_SYSTEM_FORMATTER_SERIAL_NUMBER, DA_SYSTEM_PRODUCT_NUMBER ->
                    getIdentityInformation(deviceInfoService, type);

            case DA_SYSTEM_LANGUAGE -> getLanguage(deviceInfoService);
            case DA_SYSTEM_LANGUAGE_CAPABILITY -> getSupportedLanguages(deviceInfoService);

            case DA_DEVICE_VENDOR -> HP_VENDOR;
            case DA_SYSTEM_HP_FUTURE_SMART_LEVEL -> HP_FUTURE_SMART_6;
        };
    }

    /**
     * Retrieves deployment-related information from the device.
     *
     * @param deviceInfoService the service to query
     * @param type              the specific deployment attribute to retrieve
     * @return the deployment information value, or null if not available
     */
    protected static String getDeploymentInformation(@NonNull IDeviceInfoService deviceInfoService,
                                                     DeviceAttribute type) {
        if (deviceInfoService == null) {
            Log.e(TAG, "getDeploymentInformation : deviceInfoService is null");
            return null;
        }

        DeploymentInformation deploymentInfo = deviceInfoService.getDeploymentInformation();
        if (deploymentInfo == null) {
            Log.e(TAG, "getDeploymentInformation : fail to getDeploymentInformation");
            return null;
        }

        NetworkAdapterInfo defaultNetAdapter = findDefaultNetworkAdapter(deploymentInfo.getNetworkInfo());
        OwnerConfiguredInformation ownerInfo = deploymentInfo.getOwnerInfo();

        return switch (type) {
            case DA_NETWORK_MACADDRESS -> safeGetValue(defaultNetAdapter, NetworkAdapterInfo::getMacAddress);
            case DA_NETWORK_IPADDRESS -> safeGetValue(defaultNetAdapter, NetworkAdapterInfo::getIpV4);
            case DA_NETWORK_HOSTNAME -> safeGetValue(defaultNetAdapter, NetworkAdapterInfo::getHostName);
            case DA_ASSET_NUMBER -> safeGetValue(ownerInfo, OwnerConfiguredInformation::getAssetNumber);
            case DA_COMPANY_CONTACT -> safeGetValue(ownerInfo, OwnerConfiguredInformation::getContactName);
            case DA_COMPANY_NAME -> safeGetValue(ownerInfo, OwnerConfiguredInformation::getCompanyName);
            case DA_DEVICE_LOCATION -> safeGetValue(ownerInfo, OwnerConfiguredInformation::getDeviceLocation);
            case DA_MACHINE_NAME -> safeGetValue(ownerInfo, OwnerConfiguredInformation::getDeviceName);
            default -> null;
        };
    }

    /**
     * Finds the default network adapter from the list of available adapters.
     * Prefers the adapter with DEFAULT_NETWORK_ADAPTER_NAME, falls back to the first available adapter.
     *
     * @param networkAdapters list of network adapters
     * @return the default network adapter, or null if none available
     */
    private static NetworkAdapterInfo findDefaultNetworkAdapter(List<NetworkAdapterInfo> networkAdapters) {
        if (networkAdapters == null || networkAdapters.isEmpty()) {
            return null;
        }

        // First, look for the preferred adapter name
        for (NetworkAdapterInfo adapter : networkAdapters) {
            if (adapter != null &&
                    adapter.getAdapterName() != null &&
                    adapter.getAdapterName().equalsIgnoreCase(DEFAULT_NETWORK_ADAPTER_NAME)) {
                return adapter;
            }
        }

        // Fall back to the first non-null adapter
        for (NetworkAdapterInfo adapter : networkAdapters) {
            if (adapter != null) {
                return adapter;
            }
        }

        return null;
    }

    /**
     * Retrieves identity-related information from the device.
     *
     * @param deviceInfoService the service to query
     * @param type              the specific identity attribute to retrieve
     * @return the identity information value, or null if not available
     */
    protected static String getIdentityInformation(@NonNull IDeviceInfoService deviceInfoService,
                                                   DeviceAttribute type) {
        if (deviceInfoService == null) {
            Log.e(TAG, "getIdentityInformation : deviceInfoService is null");
            return null;
        }

        com.hp.ext.service.device.Identity identity = deviceInfoService.getIdentity();
        if (identity == null) {
            return null;
        }

        return switch (type) {
            case DA_SYSTEM_MODELNAME -> {
                com.hp.ext.service.device.MakeAndModelInfo makeAndModel = identity.getMakeAndModelInfo();
                yield safeToString(makeAndModel == null ? null : makeAndModel.getModel());
            }
            case DA_SYSTEM_SERIALNUMBER -> safeToString(identity.getSerialNumber());
            case DA_SYSTEM_FIRMWARE_VERSION -> safeToString(identity.getFirmwareVersion());
            case DA_SYSTEM_DEVICE_ID -> safeToString(identity.getDeviceUuid());
            case DA_SYSTEM_FORMATTER_SERIAL_NUMBER -> safeToString(identity.getFormatterSerialNumber());
            case DA_SYSTEM_PRODUCT_NUMBER -> safeToString(identity.getProductNumber());
            default -> null;
        };
    }

    protected static String getLanguage(@NonNull IDeviceInfoService deviceInfoService) {
        if (deviceInfoService == null) {
            Log.e(TAG, "getLanguage : deviceInfoService is null");
            return null;
        }
        Configuration.Language language = deviceInfoService.getDeviceLanguage();

        return LocaleCodeMap.toWorkpathString(language);
    }

    protected static String getSupportedLanguages(@NonNull IDeviceInfoService deviceInfoService) {
        if (deviceInfoService == null) {
            Log.e(TAG, "getSupportedLanguages : deviceInfoService is null");
            return null;
        }
        List<Configuration.Language> langs = deviceInfoService.getAvailableDeviceLanguages();
        if (langs == null || langs.isEmpty()) {
            Log.e(TAG, "getSupportedLanguages : langs is null or empty");
            return null;
        }
        String joined = langs.stream()
                .map(LocaleCodeMap::toWorkpathString)
                .map(code -> "\"" + code + "\"")
                .reduce((a, b) -> a + "," + b)
                .orElse("");
        return "[" + joined + "]";
    }

    /**
     * Safely extracts a value from an object using a function, handling null objects gracefully.
     *
     * @param <T>       the type of the source object
     * @param <R>       the type of the result
     * @param source    the source object (may be null)
     * @param extractor function to extract the value from the source
     * @return the extracted value as a string, or empty string if source is null
     */
    private static <T, R> String safeGetValue(T source, java.util.function.Function<T, R> extractor) {
        return source == null ? "" : safeToString(extractor.apply(source));
    }

    /**
     * Safely converts an object to string, handling null values.
     *
     * @param obj the object to convert
     * @return string representation of the object, or empty string if the object is null
     */
    private static String safeToString(Object obj) {
        return obj != null ? obj.toString() : "";
    }
}
