package com.hp.jetadvantage.link.device.services.standard;

import android.util.Log;

import com.hp.ext.clients.CustomObjectMapper;
import com.hp.ext.clients.jobstatistics.JobStatisticsServiceClient;
import com.hp.ext.clients.jobstatistics.JobStatisticsServiceClientImpl;
import com.hp.ext.service.jobStatistics.Capabilities;
import com.hp.ext.service.jobStatistics.JobStatisticsAgentRegistrationRecord;
import com.hp.ext.service.jobStatistics.Jobs;
import com.hp.ext.service.jobStatistics.Jobs_Modify;
import com.hp.ext.service.jobStatistics.SequenceNumber;
import com.hp.ext.service.jobStatistics.StatisticsCallbackPayload;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceStatisticsService;
import com.hp.jetadvantage.link.device.services.interfaces.IE2PayloadCallback;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.standard.common.DeviceManagementService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardJsonParser;
import com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelCallbackRegistry;
import com.hp.ws.websocket.AppChannelServiceResponse;

import java.util.concurrent.atomic.AtomicReference;

public class StandardDeviceStatisticsService extends StandardDeviceService implements IDeviceStatisticsService {
    public static final String E2SERVICE_STATISTICS_CANONICAL_GUN = "com.hp.ext.service.jobStatistics.version.1";
    private static final String SERVICE_PATH_JOB_STATISTICS = "jobStatistics";

    private static final String TAG = Constants.TAG + "/Statistics";
    private final AtomicReference<IServiceCallback> serviceCallback = new AtomicReference<>();

    public StandardDeviceStatisticsService() {
        super(new JobStatisticsAgentRegistrationRecord().getTypeGUN());
    }

    public StandardDeviceStatisticsService(DeviceManagementService deviceManagementService) {
        super(deviceManagementService);
    }

    @Override
    public boolean isSupported() {
        E2call<Capabilities> call = () -> {
            JobStatisticsServiceClient client = new JobStatisticsServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
            return client.capabilities().getAsync().get();
        };

        try{
            Capabilities e2StatisticsCap = perform(call);
            String serviceGun = e2StatisticsCap.getServiceGun();
            if(serviceGun != null && serviceGun.equalsIgnoreCase(E2SERVICE_STATISTICS_CANONICAL_GUN)){
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
    public Jobs getAllJobsList(String packageName, int offset, int limit) {
        E2call<Jobs> call = () -> {
            JobStatisticsServiceClient client = new JobStatisticsServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
            String queryParam = "includeMembers=true&offset=" + offset + "&limit=" + limit;
            // Execute the GET operation
            return client.jobStatisticsAgents().getMember(getAgentId(packageName)).jobs().getAsync(getSolutionToken(packageName), queryParam).get();
        };
        return perform(call);
    }

    @Override
    public Jobs getJobsList(String packageName) {
        E2call<Jobs> call = () -> {
            JobStatisticsServiceClient client = new JobStatisticsServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
            String queryParam = "includeMembers=true";
            // Execute the GET operation
            return client.jobStatisticsAgents().getMember(getAgentId(packageName)).jobs().getAsync(getSolutionToken(packageName), queryParam).get();
        };
        return perform(call);
    }

    @Override
    public Jobs getJobWithLastSequenceNumberProcessed(String packageName) {
        E2call<Jobs> call = () -> {
            JobStatisticsServiceClient client = new JobStatisticsServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
            // Execute the GET operation
            return client.jobStatisticsAgents().getMember(getAgentId(packageName)).jobs().getAsync(getSolutionToken(packageName)).get();
        };
        return perform(call);
    }


    @Override
    public Jobs commitLastJobSequence(String packageName, int lastSequenceNumberProcessed) {
        E2call<Jobs> call = () -> {
            JobStatisticsServiceClient client = new JobStatisticsServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
            Jobs_Modify modifyRequest = new Jobs_Modify();
            modifyRequest.setLastSequenceNumberProcessed(new SequenceNumber((long) lastSequenceNumberProcessed));
            Log.i(TAG, "commitLastJobSequence : lastSequenceNumberProcessed=" + lastSequenceNumberProcessed);
            // Execute the GET operation
            return client.jobStatisticsAgents().getMember(getAgentId(packageName)).jobs().modifyAsync(getSolutionToken(packageName), modifyRequest).get();
        };
        return perform(call);
    }

    /**
     * Register callback to receive statistics notifications for all job completion events from E2 service.
     * Only one callback can be registered on an instance of IDeviceStatisticsService.
     * If multiple callbacks are registered, the last callback will be available.
     * The registered callback should be unregistered by calling unRegisterNotificationCallback()
     * when the IDeviceStatisticsService object is no longer used. Otherwise, it will cause memory leakage.
     *
     * @param callback callback object to receive statistics notification by overriding onReceiveNotification method
     */
    @Override
    public void registerNotificationCallback(IE2PayloadCallback<StatisticsCallbackPayload> callback) {
        synchronized (this) {
            if (this.serviceCallback.get() != null) {
                unRegisterNotificationCallback();
            }

            IServiceCallback newCallback = (appPackageId, serviceMessage) ->
                processStatisticsServiceCall(appPackageId, serviceMessage, callback);
            this.serviceCallback.set(newCallback);

            AppChannelCallbackRegistry.registerServiceCallback(
                    E2SERVICE_STATISTICS_CANONICAL_GUN, SERVICE_PATH_JOB_STATISTICS, newCallback);
            Log.i(TAG, "registerNotificationCallback: registered for "
                    + E2SERVICE_STATISTICS_CANONICAL_GUN + ", path=" + SERVICE_PATH_JOB_STATISTICS);
        }
    }

    /**
     * Unregister callback to clean up
     */
    @Override
    public void unRegisterNotificationCallback() {
        synchronized (this) {
            IServiceCallback currentCallback = this.serviceCallback.get();
            if (currentCallback != null) {
                AppChannelCallbackRegistry.unregisterServiceCallback(
                        E2SERVICE_STATISTICS_CANONICAL_GUN, SERVICE_PATH_JOB_STATISTICS);
                this.serviceCallback.set(null);
                Log.i(TAG, "unRegisterNotificationCallback: unregistered for "
                        + E2SERVICE_STATISTICS_CANONICAL_GUN + ", path=" + SERVICE_PATH_JOB_STATISTICS);
            }
        }
    }

    private AppChannelServiceResponse processStatisticsServiceCall(
            String appPackageId,
            com.hp.ws.websocket.AppChannelService serviceMessage,
            IE2PayloadCallback<StatisticsCallbackPayload> callback) {
        try {
            com.hp.ws.websocket.JsonTypedObject requestBody = serviceMessage.getRequestBody();
            if (requestBody == null) {
                Log.e(TAG, "processStatisticsServiceCall: requestBody is null");
                return new AppChannelServiceResponse(serviceMessage.getServiceCallId(), 400);
            }

            StatisticsCallbackPayload statisticsPayload = new StatisticsCallbackPayload();
            if (!requestBody.getTypeGUN().equals(statisticsPayload.getTypeGUN())) {
                Log.i(TAG, "processStatisticsServiceCall: unexpected typeGUN: " + requestBody.getTypeGUN());
                return new AppChannelServiceResponse(serviceMessage.getServiceCallId(), 400);
            }

            CustomObjectMapper<StatisticsCallbackPayload> objectMapper =
                    new CustomObjectMapper<>(StatisticsCallbackPayload.class);
            String jsonStr = StandardJsonParser.INSTANCE.toJson(requestBody.getValue());
            StatisticsCallbackPayload parsedPayload = objectMapper.readValue(jsonStr);

            Log.d(TAG, "processStatisticsServiceCall: appPackageId:" + appPackageId);
            callback.onReceiveNotification(appPackageId, parsedPayload);

            return new AppChannelServiceResponse(serviceMessage.getServiceCallId(), 200);
        } catch (Exception e) {
            Log.e(TAG, "processStatisticsServiceCall: Error: " + e.getMessage(), e);
            return new AppChannelServiceResponse(serviceMessage.getServiceCallId(), 500);
        }
    }
}
