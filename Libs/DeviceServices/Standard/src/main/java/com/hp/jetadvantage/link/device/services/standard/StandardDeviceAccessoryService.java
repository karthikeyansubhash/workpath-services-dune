/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.standard;

import android.util.Log;

import com.hp.ext.clients.OXPdHttpRequestException;
import com.hp.ext.clients.usbaccessories.UsbAccessoriesServiceClientImpl;
import com.hp.ext.service.usbAccessories.Accessories;
import com.hp.ext.service.usbAccessories.Accessories_Accessory_Hid_OpenHIDAccessory_ReadReport_Params;
import com.hp.ext.service.usbAccessories.Accessories_Accessory_Hid_OpenHIDAccessory_WriteReport_Params;
import com.hp.ext.service.usbAccessories.Accessories_Accessory_Hid_Open_Params;
import com.hp.ext.service.usbAccessories.Accessories_Accessory_Hid_Open_Params_OperationContext_Binding;
import com.hp.ext.service.usbAccessories.Accessories_Accessory_Hid_Open_Params_OperationContext_Value;
import com.hp.ext.service.usbAccessories.Accessory;
import com.hp.ext.service.usbAccessories.Hid_Open;
import com.hp.ext.service.usbAccessories.OpenHIDAccessory;
import com.hp.ext.service.usbAccessories.OpenHIDAccessory_Modify;
import com.hp.ext.service.usbAccessories.OpenHIDAccessory_ReadReport;
import com.hp.ext.service.usbAccessories.OpenHIDAccessory_WriteReport;
import com.hp.ext.service.usbAccessories.UsbAccessoriesAgentRegistrationRecord;
import com.hp.ext.service.usbAccessories.UsbCallback;
import com.hp.ext.service.usbAccessories.UsbRegistrationPayload;
import com.hp.ext.types.base.DeleteContent;
import com.hp.jetadvantage.link.common.utils.Pair;
import com.hp.jetadvantage.link.common.utils.StringUtility;
import com.hp.jetadvantage.link.device.services.interfaces.IAppInstallUninstallCallback;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceAccessoryService;
import com.hp.jetadvantage.link.device.services.interfaces.IE2AsyncIoCallback;
import com.hp.jetadvantage.link.device.services.interfaces.IE2ChannelSetupCallback;
import com.hp.jetadvantage.link.device.services.interfaces.IE2PayloadCallback;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.standard.common.DeviceManagementService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceService;
import com.hp.ws.cdm.commonglossary.Property;
import com.hp.ws.cdm.storage.RemovableDevice;
import com.hp.ws.cdm.usbhost.Capabilities;
import com.hp.ws.cdm.usbhost.Configuration;
import com.hp.ws.cdm.usbhost.UsbPort;

import java.net.URISyntaxException;
import java.util.UUID;

public class StandardDeviceAccessoryService extends StandardDeviceService implements IDeviceAccessoryService {

    private static final String TAG = Constants.TAG + "/ACC";
    private static final String INCLUDE_MEMBERS = "includeMembers";
    public static final String E2SERVICE_USB_ACCESSORIES_GUN = "com.hp.ext.service.usbAccessories.version.1";
    public static final String E2SERVICE_ACCESSORY_CLIENT_SERVICE_TARGET_GUN =
            "com.hp.ext.service.usbAccessories.version.1.clientService.accessoriesAgentServiceTarget";
    private AccessoryCallbackRegistry callbackRegistry;

    public StandardDeviceAccessoryService() {
        super(new UsbAccessoriesAgentRegistrationRecord().getTypeGUN());
        callbackRegistry = new AccessoryCallbackRegistry(deviceManagementService, E2SERVICE_USB_ACCESSORIES_GUN);
    }

    /**
     * Constructor for testing purpose only
     *
     * @param deviceManagementService
     */
    public StandardDeviceAccessoryService(DeviceManagementService deviceManagementService) {
        super(deviceManagementService, new UsbAccessoriesAgentRegistrationRecord().getTypeGUN());
        callbackRegistry = new AccessoryCallbackRegistry(deviceManagementService, E2SERVICE_USB_ACCESSORIES_GUN);
    }

    @Override
    public boolean closeHidAccessory(String packageName, String accessoryId, UUID openHidId) {
        E2call<DeleteContent> call = () -> {
            UsbAccessoriesServiceClientImpl usbAccessoriesClient = createUsbAccessoriesClient();
            // Execute the DELETE open hid operation
            return usbAccessoriesClient.accessories()
                    .getMember(accessoryId)
                    .hidResource()
                    .getMember(openHidId.toString())
                    .deleteAsync(getSolutionToken(packageName))
                    .get();
        };
        DeleteContent deleteContent = perform(call);
        return true;
    }

    @Override
    public Accessories getAccessories(String packageName) {
        E2call<Accessories> call = () -> {
            UsbAccessoriesServiceClientImpl usbAccessoriesClient = createUsbAccessoriesClient();
            return usbAccessoriesClient.accessories().getAsync(getSolutionToken(packageName), INCLUDE_MEMBERS).get();
        };
        return perform(call);
    }

    @Override
    public Accessory getAccessory(String packageName, String accessoryId) {
        E2call<Accessory> call = () -> {
            UsbAccessoriesServiceClientImpl usbAccessoriesClient = createUsbAccessoriesClient();
            return usbAccessoriesClient.accessories().getMember(accessoryId).getAsync(getSolutionToken(packageName)).get();
        };
        try {
            return perform(call);
        } catch (OXPdHttpRequestException e) {
            Log.e(TAG, "getAccessory: [" + accessoryId + "] " + e.getMessage());
            return null;
        }
    }

    @Override
    public OpenHIDAccessory getOpenHidAccessoryInfo(String packageName, UUID openHidId, String accessoryId) {
        E2call<OpenHIDAccessory> call = () -> {
            UsbAccessoriesServiceClientImpl usbAccessoriesClient = createUsbAccessoriesClient();
            return usbAccessoriesClient.accessories()
                    .getMember(accessoryId)
                    .hidResource()
                    .getMember(openHidId.toString())
                    .getAsync(getSolutionToken(packageName)).get();
        };
        return perform(call);
    }

    @Override
    public boolean isAgentRegistered(String packageName) {
        if (StringUtility.isEmpty(packageName)) {
            return false;
        }
        return getAgentId(packageName) != null;
    }

    @Override
    public boolean isReady() {
        // Check the status of CDM and E2 accessory.
        CdmCall cdmCall = () -> getCDMClient().sendGetRequest(CDMUrl.CONFIGURATION);
        Configuration configuration = perform(cdmCall, Configuration.class);
        return configuration != null && Property.FeatureEnabled.TRUE.equals(configuration.getPlugAndPlayEnabled());
    }

    @Override
    public boolean isSolutionRegistered(String packageName) {
        if (packageName == null) return false;
        String solutionId = getSolutionId(packageName);
        return (solutionId == null) ? false : true;
    }

    @Override
    public boolean isSupported() {
        return isSupportedCDMAccessory() && isSupportedE2Accessory();
    }

    @Override
    public UUID openOwnedHidAccessory(String packageName, String accessoryId) {
        return openHidAccessoryInternal(packageName, accessoryId, true);
    }

    @Override
    public UUID openSharedHidAccessory(String packageName, String accessoryId) {
        return openHidAccessoryInternal(packageName, accessoryId, false);
    }

    @Override
    public boolean startAsyncReading(String packageName, UUID openHidId, String accessoryId, boolean isOwned) {
        if (StringUtility.isEmpty(packageName) || openHidId == null || StringUtility.isEmpty(accessoryId)) {
            Log.e(TAG, "startAsyncReading: invalid parameters " + packageName + "," + accessoryId);
            return false;
        }
        OpenHIDAccessory modifiedOpenHIDAccessory = modifyAsyncReading(packageName, openHidId, accessoryId, true,
                isOwned);
        if (modifiedOpenHIDAccessory == null) {
            Log.e(TAG, "startAsyncReading: result is null");
            return false;
        }
        return modifiedOpenHIDAccessory.getReportReadingActive() == true;
    }

    @Override
    public boolean stopAsyncReading(String packageName, UUID openHidId, String accessoryId, boolean isOwned) {
        if (StringUtility.isEmpty(packageName) || openHidId == null || StringUtility.isEmpty(accessoryId)) {
            Log.e(TAG, "stopAsyncReading: invalid parameters " + packageName + "," + accessoryId);
            return false;
        }
        OpenHIDAccessory modifiedOpenHIDAccessory = modifyAsyncReading(packageName, openHidId, accessoryId, false,
                isOwned);
        if (modifiedOpenHIDAccessory == null) {
            Log.e(TAG, "stopAsyncReading: result is null");
            return false;
        }
        return modifiedOpenHIDAccessory.getReportReadingActive() == false;
    }

    @Override
    public OpenHIDAccessory_ReadReport readReport(String packageName, UUID openHidId, String accessoryId,
                                                  boolean isOwned,
                                                  Accessories_Accessory_Hid_OpenHIDAccessory_ReadReport_Params readReportRequest) {
        E2call<OpenHIDAccessory_ReadReport> call = () -> {
            String accessToken = isOwned ? getSolutionToken(packageName) : getUiContextToken(packageName);

            UsbAccessoriesServiceClientImpl usbAccessoriesClient = createUsbAccessoriesClient();
            OpenHIDAccessory_ReadReport readReport =
                    usbAccessoriesClient.accessories()
                            .getMember(accessoryId)
                            .hidResource()
                            .getMember(openHidId.toString())
                            .readReport()
                            .executeAsync(accessToken, readReportRequest)
                            .get();
            return readReport;
        };
        OpenHIDAccessory_ReadReport readReport = perform(call);
        return readReport;
    }

    @Override
    public OpenHIDAccessory_WriteReport writeReport(String packageName, UUID openHidId, String accessoryId,
                                                    boolean isOwned,
                                                    Accessories_Accessory_Hid_OpenHIDAccessory_WriteReport_Params writeReportRequest) {
        E2call<OpenHIDAccessory_WriteReport> call = () -> {
            String accessToken = isOwned ? getSolutionToken(packageName) : getUiContextToken(packageName);

            UsbAccessoriesServiceClientImpl usbAccessoriesClient = createUsbAccessoriesClient();
            OpenHIDAccessory_WriteReport writeReport =
                    usbAccessoriesClient.accessories()
                            .getMember(accessoryId)
                            .hidResource()
                            .getMember(openHidId.toString())
                            .writeReport()
                            .executeAsync(accessToken, writeReportRequest)
                            .get();
            return writeReport;
        };
        OpenHIDAccessory_WriteReport writeReport = perform(call);
        return writeReport;
    }

    @Override
    public void registerNotificationCallback(IE2PayloadCallback<UsbRegistrationPayload> callback) {
        if (callback != null) {
            callbackRegistry.registerUsbRegistrationCallback(callback);
        }
    }

    @Override
    public void registerOperationCallback(IE2AsyncIoCallback<UsbCallback> callback) {
        if (callback != null) {
            callbackRegistry.registerOperationCallback(callback);
        }
    }

    @Override
    public void registerUsbRegistrationChannelSetupCallback(IE2ChannelSetupCallback callback) {
        if (callback != null) {
            callbackRegistry.registerChannelSetupCallback(callback);
        }
    }

    @Override
    public void registerAppInstallUninstallCallback(IAppInstallUninstallCallback callback) {
        if (callback != null) {
            callbackRegistry.registerAppInstallUninstallCallback(callback);
        }
    }

    @Override
    public void unRegisterNotificationCallback() {
        callbackRegistry.unregisterUsbRegistrationCallback();
    }

    @Override
    public void unRegisterAppChannelSetupCallback() {
        callbackRegistry.unregisterChannelSetupCallback();
    }

    @Override
    public void unRegisterAppInstallUninstallCallback() {
        callbackRegistry.unregisterAppInstallUninstallCallback();
    }

    @Override
    public void unregisterOperationCallback() {
        callbackRegistry.unregisterOperationCallback();
    }

    private Accessories_Accessory_Hid_Open_Params createHidOpenParams(String packageName, String accessoryId) {
        Accessories_Accessory_Hid_Open_Params paramsData = new Accessories_Accessory_Hid_Open_Params();
        Accessories_Accessory_Hid_Open_Params_OperationContext_Binding operationContext = new Accessories_Accessory_Hid_Open_Params_OperationContext_Binding();
        Accessories_Accessory_Hid_Open_Params_OperationContext_Value explicit = new Accessories_Accessory_Hid_Open_Params_OperationContext_Value();
        explicit.setExplicitValue(OperationContextHelper.create(packageName, accessoryId));
        operationContext.setExplicit(explicit);
        paramsData.setOperationContext(operationContext);
        paramsData.setReportReadingActive(false);
        return paramsData;
    }

    private boolean isSupportedCDMAccessory() {
        CdmCall call = () -> getCDMClient().sendGetRequest(CDMUrl.CAPABILITIES);
        Capabilities capabilities = perform(call, Capabilities.class);
        try {
            if (capabilities != null && capabilities.getUsbPorts() != null
                    && !capabilities.getUsbPorts().isEmpty()) {
                UsbPort usbPort = capabilities.getUsbPorts().get(0);
                if (RemovableDevice.UsbPortLocation.FRONT_USB.value().equals(usbPort.getPortLocation().value())
                        || RemovableDevice.UsbPortLocation.REAR_USB.value().equals(usbPort.getPortLocation().value())) {
                    return true;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Accessory isSupported: " + e.getMessage());
        }
        return false;
    }

    private boolean isSupportedE2Accessory() {
        E2call<com.hp.ext.service.usbAccessories.Capabilities> e2call = () -> {
            UsbAccessoriesServiceClientImpl usbAccessoriesClient = createUsbAccessoriesClient();
            return usbAccessoriesClient.capabilities().getAsync().get();
        };
        com.hp.ext.service.usbAccessories.Capabilities capabilities = perform(e2call);
        return capabilities != null && capabilities.getLinks() != null
                && !capabilities.getLinks().isEmpty();
    }

    private OpenHIDAccessory modifyAsyncReading(String packageName, UUID openHidId, String accessoryId,
                                                boolean isActive, boolean isOwned) {
        E2call<OpenHIDAccessory> call = () -> {
            String token = isOwned ? getSolutionToken(packageName) : getUiContextToken(packageName);
            OpenHIDAccessory_Modify modificationRequest = new OpenHIDAccessory_Modify();
            modificationRequest.setReportReadingActive(isActive);
            UsbAccessoriesServiceClientImpl usbAccessoriesClient = createUsbAccessoriesClient();
            return usbAccessoriesClient.accessories()
                    .getMember(accessoryId)
                    .hidResource().getMember(openHidId.toString())
                    .modifyAsync(token, modificationRequest)
                    .get();
        };

        return perform(call);
    }

    private UsbAccessoriesServiceClientImpl createUsbAccessoriesClient() throws URISyntaxException {
        return new UsbAccessoriesServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
    }

    private UUID openHidAccessoryInternal(String packageName, String accessoryId, boolean isOwned) {
        E2call<Hid_Open> call = () -> {
            Accessories_Accessory_Hid_Open_Params hidOpenParams = createHidOpenParams(packageName, accessoryId);
            UsbAccessoriesServiceClientImpl usbAccessoriesClient = createUsbAccessoriesClient();
            String token = isOwned ? getSolutionToken(packageName) : getUiContextToken(packageName);
            return usbAccessoriesClient.accessories()
                    .getMember(accessoryId)
                    .hidResource()
                    .openHidOperation()
                    .executeAsync(token, hidOpenParams)
                    .get();
        };

        Hid_Open hidOpen = perform(call);
        if (hidOpen != null && hidOpen.getOpenHIDAccessory() != null) {
            return hidOpen.getOpenHIDAccessory().getOpenHIDAccessoryID();
        } else {
            return null;
        }
    }

    public static final class CDMUrl {
        public static final String CONFIGURATION = "/cdm/usbHost/v1/configuration";
        public static final String CAPABILITIES = "/cdm/usbHost/v1/capabilities";
    }

    protected static final class OperationContextHelper {
        private static final String DELIMITER = "/";
        private static final int EXPECTED_PARTS = 2;

        private OperationContextHelper() {
            // Private constructor to prevent instantiation
        }

        /**
         * Creates the operation context string from a package name and accessory ID.
         */
        public static String create(String packageName, String accessoryId) {
            return packageName + DELIMITER + accessoryId;
        }

        /**
         * Parses the operation context string into a package name and accessory ID.
         *
         * @return A Pair containing the package name (first) and accessory ID (second), or null if parsing fails.
         */
        public static Pair<String, String> parse(String operationContext) {
            if (operationContext == null) {
                return null;
            }
            String[] parts = operationContext.split(DELIMITER);
            if (parts.length != EXPECTED_PARTS) {
                return null;
            }
            return new Pair<>(parts[0], parts[1]);
        }
    }
}
