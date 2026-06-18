/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.standard;

import android.util.Log;

import com.hp.ext.service.usbAccessories.OperationCallbackResponse;
import com.hp.ext.service.usbAccessories.RegistrationRequest;
import com.hp.ext.service.usbAccessories.RegistrationResponse;
import com.hp.ext.service.usbAccessories.UsbAccessoriesAgentRegistrationRecord;
import com.hp.ext.service.usbAccessories.UsbCallback;
import com.hp.ext.service.usbAccessories.UsbCallbackEnvelope;
import com.hp.ext.service.usbAccessories.UsbRegistrationPayload;
import com.hp.jetadvantage.link.common.utils.JsonParser;
import com.hp.jetadvantage.link.common.utils.Pair;
import com.hp.jetadvantage.link.common.utils.StringUtility;
import com.hp.jetadvantage.link.device.services.interfaces.IAppInstallUninstallCallback;
import com.hp.jetadvantage.link.device.services.interfaces.IE2AsyncIoCallback;
import com.hp.jetadvantage.link.device.services.interfaces.IE2ChannelSetupCallback;
import com.hp.jetadvantage.link.device.services.interfaces.IE2PayloadCallback;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.standard.common.DeviceManagementService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceService;
import com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelCallbackRegistry;
import com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelHttpStatus;
import com.hp.workpath.internal.utils.adapter.E2TypeJsonConverter;
import com.hp.workpath.internal.utils.adapter.GsonAdapter;
import com.hp.ws.websocket.AppChannelService;
import com.hp.ws.websocket.AppChannelServiceResponse;

import java.util.Objects;

/**
 * Registry for managing USB accessory callbacks and channel setup.
 * This class handles registration and unregistration of various callbacks related to:
 * - USB accessory registration events
 * - USB HID I/O operations
 * - App channel setup notifications
 * - App install/uninstall events
 */
public class AccessoryCallbackRegistry {

    private static final String TAG = Constants.TAG + "/ACC/CallBack";

    // Service target paths defined in E2 USB Accessories Spec:
    // https://github-partner.azc.ext.hp.com/jedi/dune/blob/24s/green/src/fw/ws/extensibility/Specifications/com.hp.ext.service.usbAccessories/1/0/accessoriesAgentServiceTarget.openapi.json
    private static final String SERVICE_TARGET_PATH_REGISTRATION = "/registration";
    private static final String SERVICE_TARGET_PATH_OPERATION_CALLBACK = "/operationCallback";

    // E2 Type identifiers (GUN = Globally Unique Name)
    private final String registrationRecordTypeGUN;
    private final String serviceTypeGun;
    private final String registrationRequestTypeGUN;
    private final String callbackEnvelopeTypeGUN;

    private final Object lock = new Object();
    private final DeviceManagementService deviceManagementService;

    private volatile StandardDeviceService.ISetupCallback channelSetupCallback;
    private volatile StandardDeviceService.IServiceCallback operationCallback;
    private volatile StandardDeviceService.IServiceCallback usbAccessoryRegistrationCallback;

    /**
     * Constructs a new AccessoryCallbackRegistry.
     *
     * @param deviceManagementService Service for device management operations (must not be null)
     * @param serviceTypeGun          E2 service type identifier (must not be null)
     * @throws NullPointerException if any parameter is null or if type IDs cannot be retrieved
     */
    public AccessoryCallbackRegistry(DeviceManagementService deviceManagementService, String serviceTypeGun) {
        this.deviceManagementService = Objects.requireNonNull(deviceManagementService,
                "DeviceManagementService cannot be null");
        this.serviceTypeGun = Objects.requireNonNull(serviceTypeGun, "serviceTypeGun cannot be null");

        // E2 Type GUN strings
        this.registrationRecordTypeGUN = new UsbAccessoriesAgentRegistrationRecord().getTypeGUN();
        this.registrationRequestTypeGUN = new RegistrationRequest().getTypeGUN();
        this.callbackEnvelopeTypeGUN = new UsbCallbackEnvelope().getTypeGUN();

        // Validate that type IDs were successfully retrieved
        Objects.requireNonNull(this.registrationRecordTypeGUN, "registrationRecordTypeGUN cannot be null");
        Objects.requireNonNull(this.registrationRequestTypeGUN, "registrationPayloadTypeGUN cannot be null");
        Objects.requireNonNull(this.callbackEnvelopeTypeGUN, "callbackEnvelopeTypeGUN cannot be null");
    }

    /**
     * Registers a callback for USB accessory registration notifications.
     * The callback will be invoked when a valid UsbRegistrationPayload notification is received
     * for owned-accessory registration events.
     *
     * @param callback Called when a USB registration event occurs (must not be null)
     */
    protected void registerUsbRegistrationCallback(final IE2PayloadCallback<UsbRegistrationPayload> callback) {
        if (callback == null) {
            Log.e(TAG, "registerUsbRegistrationCallback: callback is null");
            return;
        }
        synchronized (lock) {
            unregisterUsbRegistrationCallback();
            usbAccessoryRegistrationCallback = (appPackageName, serviceMessage) ->
                    handleUsbRegistrationCallback(callback, appPackageName, serviceMessage);


            Log.d(TAG, "Registering USB registration callback");
            AppChannelCallbackRegistry.registerServiceCallback(serviceTypeGun, SERVICE_TARGET_PATH_REGISTRATION,
                    usbAccessoryRegistrationCallback);
        }
    }

    /**
     * Registers a callback for USB HID asynchronous I/O operations.
     * The callback is invoked when asynchronous USB operations complete.
     *
     * @param callback Called when a USB operation completes (must not be null)
     *
     */
    protected void registerOperationCallback(final IE2AsyncIoCallback<UsbCallback> callback) {
        if (callback == null) {
            Log.e(TAG, "registerOperationCallback: callback is null");
            return;
        }
        synchronized (lock) {
            unregisterOperationCallback();
            operationCallback = (appPackageId, serviceMessage) ->
                    handleOperationCallbackMessage(callback, serviceMessage);

            AppChannelCallbackRegistry.registerServiceCallback(serviceTypeGun, SERVICE_TARGET_PATH_OPERATION_CALLBACK,
                    operationCallback);
        }
    }

    /**
     * Registers a callback for USB Accessory service channel setup events.
     * The callback is invoked when a valid channel setup notification is received.
     *
     * @param callback Called when channel setup occurs (must not be null)
     */
    protected void registerChannelSetupCallback(final IE2ChannelSetupCallback callback) {
        if (callback == null) {
            Log.e(TAG, "registerChannelSetupCallback: callback is null");
            return;
        }
        synchronized (lock) {
            unregisterChannelSetupCallback();
            channelSetupCallback = (appPackageName, channelSetup) -> {
                try {
                    if (channelSetup != null) {
                        Log.i(TAG, "Channel setup received: " + JsonParser.getInstance().toJson(channelSetup));
                        callback.onSetup(appPackageName);
                    } else {
                        Log.e(TAG, "Channel setup callback received null channelMessage");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Exception in channel setup callback: " + e.getMessage(), e);
                }
            };

            AppChannelCallbackRegistry.registerSetupCallback(serviceTypeGun, channelSetupCallback);
        }
    }

    /**
     * Registers a callback for app install/uninstall events.
     *
     * @param callback Called when an app is installed or uninstalled (must not be null)
     */
    protected void registerAppInstallUninstallCallback(IAppInstallUninstallCallback callback) {
        if (callback == null) {
            Log.e(TAG, "registerAppInstallUninstallCallback: callback is null");
            return;
        }
        deviceManagementService.registerAppInstallUninstallReceiver(registrationRecordTypeGUN, callback);
    }

    // --- Unregistration Methods ---

    /**
     * Unregisters the USB operation callback.
     */
    protected void unregisterOperationCallback() {
        synchronized (lock) {
            if (operationCallback != null) {
                AppChannelCallbackRegistry.unregisterServiceCallback(
                        serviceTypeGun, SERVICE_TARGET_PATH_OPERATION_CALLBACK);
                operationCallback = null;
            }
        }
    }

    /**
     * Unregisters the USB registration callback.
     */
    protected void unregisterUsbRegistrationCallback() {
        synchronized (lock) {
            if (usbAccessoryRegistrationCallback != null) {
                AppChannelCallbackRegistry.unregisterServiceCallback(
                        serviceTypeGun, SERVICE_TARGET_PATH_REGISTRATION);
                usbAccessoryRegistrationCallback = null;
            }
        }
    }

    /**
     * Unregisters the app channel setup callback, if registered.
     */
    protected void unregisterChannelSetupCallback() {
        synchronized (lock) {
            if (channelSetupCallback != null) {
                AppChannelCallbackRegistry.unregisterSetupCallback(serviceTypeGun, channelSetupCallback);
                channelSetupCallback = null;
            }
        }
    }

    /**
     * Unregisters the app install/uninstall callback.
     */
    protected void unregisterAppInstallUninstallCallback() {
        if (!StringUtility.isEmpty(registrationRecordTypeGUN)) {
            deviceManagementService.unRegisterAppInstallUninstallReceiver(registrationRecordTypeGUN);
        }
    }

    // --- Private Methods ---

    /**
     * Handles USB registration callback messages.
     *
     * Example UsbRegistrationPayload message:
     * "channelMessage": {
     *     "channelId": "aba754e6-e1df-4f9f-aa07-4a269b9eca82",
     *     "message": {
     *         "service": {
     *             "attachments": [],
     *             "httpMethod": "POST",
     *             "path": "/registration",
     *             "requestBody": {
     *                 "typeGUN": "com.hp.ext.service.usbAccessories.version.1.type.usbRegistrationPayload",
     *                 "value": {
     *                     "usbAttached": {
     *                         "resourceId": "5add062b-2f22-48f7-84c4-6359f422dda1",
     *                         "resourceLink": "/ext/usbAccessories/v1/accessories/5add062b-2f22-48f7-84c4-6359f422dda1"
     *                     }
     *                 }
     *             },
     *             "serviceCallId": "6b0a9e6b-240b-4065-bfa6-40fdb1c4cdde"
     *         }
     *     }
     * }
     */
    AppChannelServiceResponse handleUsbRegistrationCallback(
            IE2PayloadCallback<UsbRegistrationPayload> callback, String appPackageName,
            AppChannelService serviceMessage) {
        try {
            Log.d(TAG, "usbAccessoryRegistrationCallback: " + GsonAdapter.INSTANCE.toJson(serviceMessage));

            if (!isValidServiceMessage(serviceMessage, registrationRequestTypeGUN)) {
                Log.d(TAG, "Skipped USB registration callback: requestBody typeGUN did not match "
                        + registrationRequestTypeGUN);
                return createRegistrationResponse(serviceMessage);
            }

            RegistrationRequest registrationRequest =
                    E2TypeJsonConverter.fromJsonTypedObject(serviceMessage.getRequestBody(),
                            RegistrationRequest.class);
            if (registrationRequest != null) {
                Log.d(TAG, "Invoking USB registration callback");
                callback.onReceiveNotification(appPackageName, registrationRequest.getRegistrationPayload());
                Log.d(TAG, "USB registration callback completed");
            } else {
                Log.e(TAG, "Failed to parse UsbRegistrationPayload: " +
                        GsonAdapter.INSTANCE.toJson(serviceMessage));
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception in USB registration callback: " + e.getMessage(), e);
        }
        return createRegistrationResponse(serviceMessage);
    }

    /**
     * Handles operation callback messages.
     *
     * Example UsbCallbackEnvelope message:
     * "channelMessage": {
     *     "channelId": "9abba4d0-0a82-4616-bbe7-d40cb436fcc5",
     *     "message": {
     *         "service": {
     *             "attachments": [],
     *             "httpMethod": "POST",
     *             "path": "/operationCallback",
     *             "requestBody": {
     *                 "typeGUN": "com.hp.ext.service.usbAccessories.version.1.type.usbCallbackEnvelope",
     *                 "value": {
     *                     "operationContext": "com.hp.workpath.sample.accessorysample/56a88191-b64b-4cff-9bf2-b7037a8d52df",
     *                     "usbCallback": {
     *                         "hidRead": {
     *                             "data": "ODA0NDE2MzM=",
     *                             "hidReadSequence": 1
     *                         }
     *                     }
     *                 }
     *             },
     *             "serviceCallId": "de081437-e2b7-40ea-8107-f99866022f5e"
     *         }
     *     }
     * }
     */
    AppChannelServiceResponse handleOperationCallbackMessage(IE2AsyncIoCallback<UsbCallback> callback,
                                                                     AppChannelService serviceMessage) {
        try {
            if (!isValidServiceMessage(serviceMessage, callbackEnvelopeTypeGUN)) {
                Log.d(TAG, "Skipped USB operation callback: requestBody typeGUN did not match "
                        + callbackEnvelopeTypeGUN);
                return createOperationCallbackResponse(serviceMessage);
            }
            Log.d(TAG, "Operation callback received: " + GsonAdapter.INSTANCE.toJson(serviceMessage));
            UsbCallbackEnvelope envelope = E2TypeJsonConverter.fromJsonTypedObject(
                    serviceMessage.getRequestBody(), UsbCallbackEnvelope.class);
            if (hasValidEnvelope(envelope)) {
                Pair<String, String> context =
                        StandardDeviceAccessoryService.OperationContextHelper.parse(envelope.getOperationContext());
                if (context == null) {
                    Log.e(TAG, "Failed to parse operationContext: " + envelope.getOperationContext());
                } else {
                    String appPackageName = context.first;
                    String accessoryId = context.second;

                    Log.d(TAG, "Invoking operation callback for package: " + appPackageName + ", accessory: " + accessoryId);
                    callback.onReceiveNotification(appPackageName, accessoryId, envelope.getUsbCallback());
                    Log.d(TAG, "Operation callback completed");
                }
            } else {
                Log.e(TAG, "Failed to parse UsbCallbackEnvelope: " +
                        GsonAdapter.INSTANCE.toJson(serviceMessage));
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception in operation callback: " + e.getMessage(), e);
        }
        return createOperationCallbackResponse(serviceMessage);
    }

    private boolean hasValidEnvelope(UsbCallbackEnvelope envelope) {
        return envelope != null
                && envelope.getUsbCallback() != null
                && envelope.getOperationContext() != null;
    }

    private boolean isValidServiceMessage(AppChannelService serviceMessage, String expectedTypeGUN) {
        return serviceMessage != null
                && serviceMessage.getRequestBody() != null
                && expectedTypeGUN.equalsIgnoreCase(serviceMessage.getRequestBody().getTypeGUN());
    }

    AppChannelServiceResponse createRegistrationResponse(AppChannelService serviceMessage) {
        if (serviceMessage == null) {
            Log.e(TAG, "createRegistrationResponse: serviceMessage is null");
            return null;
        }

        RegistrationResponse registrationResponse = new RegistrationResponse();
        registrationResponse.setPrompt(false);

        AppChannelServiceResponse serviceResponse =
                new AppChannelServiceResponse(serviceMessage.getServiceCallId(), AppChannelHttpStatus.OK.code());
        serviceResponse.setResponseBody(registrationResponse);
        return serviceResponse;
    }

    AppChannelServiceResponse createOperationCallbackResponse(AppChannelService serviceMessage) {
        if (serviceMessage == null) {
            Log.e(TAG, "createOperationCallbackResponse: serviceMessage is null");
            return null;
        }

        com.hp.ext.service.usbAccessories.ResponseAction action =
                new com.hp.ext.service.usbAccessories.ResponseAction();
        action.setNone(new com.hp.ext.types.actions.NonAction());

        OperationCallbackResponse operationCallbackResponse = new OperationCallbackResponse();
        operationCallbackResponse.setAction(action);

        AppChannelServiceResponse serviceResponse =
                new AppChannelServiceResponse(serviceMessage.getServiceCallId(), AppChannelHttpStatus.OK.code());
        serviceResponse.setResponseBody(operationCallbackResponse);
        return serviceResponse;
    }
}
