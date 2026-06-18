/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */

package com.hp.jetadvantage.link.device.services.standard;

import android.util.Log;

import com.google.gson.JsonElement;
import com.hp.ext.clients.CustomObjectMapper;
import com.hp.ext.clients.copy.CopyServiceClient;
import com.hp.ext.clients.copy.CopyServiceClientImpl;
import com.hp.ext.service.copy.Capabilities;
import com.hp.ext.service.copy.CopyAgentRegistrationRecord;
import com.hp.ext.service.copy.CopyJob;
import com.hp.ext.service.copy.CopyJobNotificationContent;
import com.hp.ext.service.copy.CopyJob_Cancel;
import com.hp.ext.service.copy.CopyJob_Create;
import com.hp.ext.service.copy.CopyNotification;
import com.hp.ext.service.copy.DefaultOptions;
import com.hp.ext.service.copy.Profile;
import com.hp.ext.service.copy.ReleaseStoredJobRequest;
import com.hp.ext.service.copy.StoredJob;
import com.hp.ext.service.copy.StoredJobs;
import com.hp.ext.service.copy.StoredJob_Release;
import com.hp.ext.service.copy.StoredJob_Remove;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceCopyJobService;
import com.hp.jetadvantage.link.device.services.interfaces.IE2PayloadCallback;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.standard.common.DeviceManagementService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardJsonParser;
import com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelCallbackRegistry;

public class StandardDeviceCopyJobService extends StandardDeviceService implements IDeviceCopyJobService {
    /**
     * E2 CopyJob Service Specifications : refer to dune/src/fw/ws/extensibility/Specifications/com.hp.ext.service.copy/1/0/ums.json
     */
    public static final String E2SERVICE_COPY_JOB_CANONICAL_GUN = "com.hp.ext.service.copy.version.1";
    private static final String TAG = Constants.TAG + "/CopyJob";
    private static final String LOG_STORED_JOB_ID = ", storedJobId=";
    private IPayloadCallback payloadCallback = null;

    public StandardDeviceCopyJobService() {
        super(new CopyAgentRegistrationRecord().getTypeGUN());
    }

    public StandardDeviceCopyJobService(DeviceManagementService deviceManagementService) {
        super(deviceManagementService);
    }

    public boolean isSupported() {
        E2call<Capabilities> call = () -> {
            CopyServiceClient client = new CopyServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
            return client.capabilities().getAsync().get();
        };

        try {
            Capabilities e2CopyCap = perform(call);
            String serviceGun = e2CopyCap.getServiceGun();
            if (serviceGun != null && serviceGun.equalsIgnoreCase(E2SERVICE_COPY_JOB_CANONICAL_GUN)) {
                return true;
            }
            Log.i(TAG, "isSupported : serviceGun not match :" + serviceGun);
            return false;
        } catch (Exception e) {
            Log.e(TAG, "isSupported : Exception");
            Log.e(TAG, Log.getStackTraceString(e));
            return false;
        }
    }

    @Override
    public DefaultOptions getDefaultOptions(String packageName) {
        E2call<DefaultOptions> call = () -> {
            CopyServiceClient client = new CopyServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
            return client.defaultOptions().getAsync(getSolutionToken(packageName)).get();
        };
        return perform(call);
    }

    @Override
    public Profile getProfile(String packageName) {
        E2call<Profile> call = () -> {
            CopyServiceClient client = new CopyServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
            return client.profile().getAsync(getSolutionToken(packageName)).get();
        };
        return perform(call);
    }

    @Override
    public CopyJob createCopyJob(String packageName, CopyJob_Create copyJobCreate) {
        E2callBiParam<CopyJob, String, CopyJob_Create> call = (String id, CopyJob_Create job) -> {
            CopyServiceClient client = new CopyServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
            // Execute the POST operation
            return client.copyAgents().getMember(id).copyJobs().executeAsync(getUiContextToken(packageName), job).get();
        };
        return perform(call, getAgentId(packageName), copyJobCreate);
    }

    @Override
    public CopyJob getCopyJob(String packageName, String copyJobId) {
        E2callBiParam<CopyJob, String, String> call = (String p1, String p2) -> {
            CopyServiceClient client = new CopyServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
            // Execute the GET operation
            return client.copyAgents().getMember(p1).copyJobs().getMember(p2).getAsync(getSolutionToken(packageName)).get();
        };
        return perform(call, getAgentId(packageName), copyJobId);
    }

    @Override
    public CopyJob_Cancel cancelCopyJob(String packageName, String copyJobId) {
        E2callBiParam<CopyJob_Cancel, String, String> call = (String p1, String p2) -> {
            CopyServiceClient client = new CopyServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
            /// Execute the POST operation
            return client.copyAgents().getMember(p1).copyJobs().getMember(p2).cancel().executeAsync(getSolutionToken(packageName)).get();
        };
        return perform(call, getAgentId(packageName), copyJobId);
    }

    @Override
    public StoredJobs enumerateStoredJobs(String packageName) {
        Log.i(TAG, "enumerateStoredJobs : packageName=" + packageName);
        E2call<StoredJobs> call = () -> {
            CopyServiceClient client = new CopyServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
            return client.copyAgents().getMember(getAgentId(packageName)).storedJobs()
                    .getAsync(getSolutionToken(packageName), "includeMembers").get();
        };
        return perform(call);
    }

    @Override
    public StoredJob getStoredJob(String packageName, String storedJobId) {
        Log.i(TAG, "getStoredJob : packageName=" + packageName + LOG_STORED_JOB_ID + storedJobId);
        E2callBiParam<StoredJob, String, String> call = (String agentId, String jobId) -> {
            CopyServiceClient client = new CopyServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
            return client.copyAgents().getMember(agentId).storedJobs().getMember(jobId)
                    .getAsync(getSolutionToken(packageName)).get();
        };
        return perform(call, getAgentId(packageName), storedJobId);
    }

    @Override
    public StoredJob_Release releaseStoredJob(String packageName, String storedJobId, ReleaseStoredJobRequest request) {
        Log.i(TAG, "releaseStoredJob(withRequest) : packageName=" + packageName + LOG_STORED_JOB_ID + storedJobId
                + ", hasRequest=" + (request != null));
        E2callBiParam<StoredJob_Release, String, String> call = (String agentId, String jobId) -> {
            CopyServiceClient client = new CopyServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
            return client.copyAgents().getMember(agentId).storedJobs().getMember(jobId)
                    .release().executeAsync(getUiContextToken(packageName), request).get();
        };
        return perform(call, getAgentId(packageName), storedJobId);
    }

    @Override
    public StoredJob_Remove deleteStoredJob(String packageName, String storedJobId, com.hp.ext.service.copy.RemoveStoredJobRequest request) {
        Log.i(TAG, "deleteStoredJob(withRequest) : packageName=" + packageName + LOG_STORED_JOB_ID + storedJobId);
        E2callBiParam<StoredJob_Remove, String, String> call = (String agentId, String jobId) -> {
            CopyServiceClient client = new CopyServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
            return client.copyAgents().getMember(agentId).storedJobs().getMember(jobId)
                    .remove().executeAsync(getSolutionToken(packageName), request).get();
        };
        return perform(call, getAgentId(packageName), storedJobId);
    }

    /**
     * Register callback to receive copy notifications for all copy events indicating a change from E2 service
     * Only one callback can be registered on a instance of IDeviceCopyJobService.
     * If multiple callbacks are registered, the last callback will be available.
     * Cautions : The registered callback should be unregistered by calling unRegisterNotificationCallback()
     * when the IDeviceCopyJobService object is not used anymore. Otherwise, it will make memory leakage.
     *
     * @param callback callback object to receive copy notification by overriding onReceiveNotification method
     */
    @Override
    public void registerNotificationCallback(IE2PayloadCallback<CopyNotification> callback) {
        if (this.payloadCallback != null) {
            unRegisterNotificationCallback();
        }

        // create a wrapper IPayloadCallback callback to parse json from the websocket
        this.payloadCallback = (appPackageId, notification) -> {
            CopyNotification copyNotification = new CopyNotification();
            String innputTypeGun = notification.getTypeGUN();
            String copyNotiTypeGun = copyNotification.getTypeGUN();
            Log.i(TAG, "input notification typeGUN : " + innputTypeGun);
            Log.i(TAG, "copyNotification typeGUN : " + copyNotiTypeGun);
            if (notification.getTypeGUN().equals(copyNotification.getTypeGUN())) {
                JsonElement jobNotificationContent = notification.getValue().get("jobNotification");
                if (jobNotificationContent == null) {
                    Log.i(TAG, "onReceiveNotification(): Not a jobNotification, drop the message :" + StandardJsonParser.INSTANCE.toJson(notification.getValue()));
                    return;
                }
                //convert the received websocket message to E2Type object
                CustomObjectMapper<CopyJobNotificationContent> objectMapper = new CustomObjectMapper<>(CopyJobNotificationContent.class);
                String jsonStr = StandardJsonParser.INSTANCE.toJson(jobNotificationContent);
                copyNotification.setJobNotification(objectMapper.readValue(jsonStr));

                callback.onReceiveNotification(appPackageId, copyNotification);
            } else {
                Log.i(TAG, "onReceiveNotification() unexpected typeGUN:" + notification.getTypeGUN());
            }
        };
        AppChannelCallbackRegistry.registerPayloadCallback(E2SERVICE_COPY_JOB_CANONICAL_GUN, this.payloadCallback);
    }

    /**
     * Unregister callback to clean up
     */
    @Override
    public void unRegisterNotificationCallback() {
        if (this.payloadCallback != null) {
            AppChannelCallbackRegistry.unregisterPayloadCallback(E2SERVICE_COPY_JOB_CANONICAL_GUN, this.payloadCallback);
            this.payloadCallback = null;
        }
    }
}
