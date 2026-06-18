/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */

package com.hp.jetadvantage.link.device.services.standard;

import android.util.Log;

import com.google.gson.JsonElement;
import com.hp.ext.clients.CustomObjectMapper;
import com.hp.ext.clients.device.DeviceServiceClient;
import com.hp.ext.clients.device.DeviceServiceClientImpl;
import com.hp.ext.clients.scanjob.ScanJobServiceClient;
import com.hp.ext.clients.scanjob.ScanJobServiceClientImpl;
import com.hp.ext.service.device.Scanner;
import com.hp.ext.service.scanJob.Capabilities;
import com.hp.ext.service.scanJob.DefaultOptions;
import com.hp.ext.service.scanJob.Profile;
import com.hp.ext.service.scanJob.ScanJob;
import com.hp.ext.service.scanJob.ScanJobAgentRegistrationRecord;
import com.hp.ext.service.scanJob.ScanJobNotificationContent;
import com.hp.ext.service.scanJob.ScanJob_Cancel;
import com.hp.ext.service.scanJob.ScanJob_Create;
import com.hp.ext.service.scanJob.ScanNotification;
import com.hp.ext.types.base.DeleteContent;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceScanJobService;
import com.hp.jetadvantage.link.device.services.interfaces.IE2PayloadCallback;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.standard.common.DeviceManagementService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardJsonParser;
import com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelCallbackRegistry;
import com.hp.ws.cdm.usbhost.Configuration;
import com.hp.ws.websocket.JsonTypedObject;

public class StandardDeviceScanJobService extends StandardDeviceService implements IDeviceScanJobService {
    /**
     * E2 ScanJob Service Specifications : refer to dune/src/fw/ws/extensibility/Specifications/com.hp.ext.service.scanJob/1/0/ums.json
     */
    public static final String E2SERVICE_SCAN_JOB_CANONICAL_GUN = "com.hp.ext.service.scanJob.version.1";

    private static final String TAG = Constants.TAG + "/ScanJob";
    private IPayloadCallback payloadCallback = null;

    public StandardDeviceScanJobService() {
        super(new ScanJobAgentRegistrationRecord().getTypeGUN());
    }

    public StandardDeviceScanJobService(DeviceManagementService deviceManagementService) {
        super(deviceManagementService);
    }

    @Override
    public boolean isSupported() {
        E2call<Capabilities> call = () -> {
            ScanJobServiceClient client = new ScanJobServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
            return client.capabilities().getAsync().get();
        };

        try {
            Capabilities e2ScanCap = perform(call);
            String serviceGun = e2ScanCap.getServiceGun();
            if (serviceGun != null && serviceGun.equalsIgnoreCase(E2SERVICE_SCAN_JOB_CANONICAL_GUN)) {
                return true;
            }
            Log.i(TAG, "isSupported : serviceGun not match :" + serviceGun);
            return false;
        } catch (RuntimeException e) {
            Log.i(TAG, "isSupported : RuntimeException=" + e.getMessage());
            return false;
        }
    }

    @Override
    public Scanner getScannerStatus() {
        E2call<Scanner> call = () -> {
            DeviceServiceClient client = new DeviceServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
            return client.scanner().getAsync().get();
        };

        try {
            return perform(call);
        }
        catch (Exception e) {
            Log.i(TAG, "getScanner : Exception=" + e.getMessage());
            return null;
        }
    }

    @Override
    public DefaultOptions getDefaultOptions(String packageName) {
        E2call<DefaultOptions> call = () -> {
            ScanJobServiceClient client = new ScanJobServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
            return client.defaultOptions().getAsync(getSolutionToken(packageName)).get();
        };
        return perform(call);
    }

    @Override
    public Profile getProfile(String packageName) {
        E2call<Profile> call = () -> {
            ScanJobServiceClient client = new ScanJobServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
            return client.profile().getAsync(getSolutionToken(packageName)).get();
        };
        return perform(call);
    }

    @Override
    public ScanJob createScanJob(String packageName, ScanJob_Create scanJobCreate) {
        E2callBiParam<ScanJob, String, ScanJob_Create> call = (String id, ScanJob_Create job) -> {
            ScanJobServiceClient client = new ScanJobServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
            // Execute the POST operation
            return client.scanJobAgents().getMember(id).scanJobs().executeAsync(getUiContextToken(packageName), job).get();
        };
        return perform(call, getAgentId(packageName), scanJobCreate);
    }

    @Override
    public ScanJob getScanJob(String packageName, String scanJobId) {
        E2callBiParam<ScanJob, String, String> call = (String p1, String p2) -> {
            ScanJobServiceClient client = new ScanJobServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
            // Execute the GET operation
            return client.scanJobAgents().getMember(p1).scanJobs().getMember(p2).getAsync(getSolutionToken(packageName)).get();
        };
        return perform(call, getAgentId(packageName), scanJobId);
    }

    @Override
    public DeleteContent deleteLocalScanJob(String packageName, String scanJobId) {
        E2callBiParam<DeleteContent, String, String> call = (String p1, String p2) -> {
            ScanJobServiceClient client = new ScanJobServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
            return client.scanJobAgents().getMember(p1).localScans().getMember(p2).deleteAsync(getSolutionToken(packageName)).get();
        };
        return perform(call, getAgentId(packageName), scanJobId);
    }

    @Override
    public ScanJob_Cancel cancelScanJob(String packageName, String scanJobId) {
        E2callBiParam<ScanJob_Cancel, String, String> call = (String p1, String p2) -> {
            ScanJobServiceClient client = new ScanJobServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
            // Execute the POST operation
            // Log.d(TAG, "cancelScanJob : p1=" + p1 + ", p2=" + p2);
            return client.scanJobAgents().getMember(p1).scanJobs().getMember(p2).cancel().executeAsync(getSolutionToken(packageName)).get();
        };
        return perform(call, getAgentId(packageName), scanJobId);
    }

    /**
     * Register callback to receive scan notifications for all scan events indicating a change from E2 service
     * Only one callback can be registered on a instance of IDeviceScanJobService.
     * If multiple callbacks are registered, the last callback will be available.
     * Cautions : The registered callback should be unregistered by calling unRegisterNotificationCallback()
     * when the IDeviceScanJobService object is not used anymore. Otherwise, it will make memory leakage.
     *
     * @param callback callback object to receive scan notification by overriding onReceiveNotification method
     */
    @Override
    public void registerNotificationCallback(IE2PayloadCallback<ScanNotification> callback) {
        if (this.payloadCallback != null) {
            unRegisterNotificationCallback();
        }

        // create a wrapper IPayloadCallback callback to parse json from the websocket
        this.payloadCallback = new IPayloadCallback() {
            @Override
            public void onReceiveNotification(String appPackageId, JsonTypedObject notification) {
                ScanNotification scanNotification = new ScanNotification();
                if (notification.getTypeGUN().equals(scanNotification.getTypeGUN())) {
                    JsonElement jobNotificationContent = notification.getValue().get("jobNotification");
                    if (jobNotificationContent == null) {
                        Log.i(TAG, "onReceiveNotification(): Not a jobNotification, drop the message :" + StandardJsonParser.INSTANCE.toJson(notification.getValue()));
                        return;
                    }
                    //convert the received websocket message to E2Type object
                    CustomObjectMapper<ScanJobNotificationContent> objectMapper = new CustomObjectMapper<>(ScanJobNotificationContent.class);
                    String jsonStr = StandardJsonParser.INSTANCE.toJson(jobNotificationContent);
                    scanNotification.setJobNotification(objectMapper.readValue(jsonStr));

                    callback.onReceiveNotification(appPackageId, scanNotification);
                } else {
                    Log.i(TAG, "onReceiveNotification() unexpected typeGUN:" + notification.getTypeGUN());
                }
            }
        };
        AppChannelCallbackRegistry.registerPayloadCallback(E2SERVICE_SCAN_JOB_CANONICAL_GUN, this.payloadCallback);
    }

    /**
     * Unregister callback to clean up
     */
    @Override
    public void unRegisterNotificationCallback() {
        if (this.payloadCallback != null) {
            AppChannelCallbackRegistry.unregisterPayloadCallback(E2SERVICE_SCAN_JOB_CANONICAL_GUN, this.payloadCallback);
            this.payloadCallback = null;
        }
    }
}
