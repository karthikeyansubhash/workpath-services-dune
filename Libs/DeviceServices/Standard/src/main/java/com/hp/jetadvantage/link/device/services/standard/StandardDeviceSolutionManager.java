package com.hp.jetadvantage.link.device.services.standard;

import android.util.Log;

import com.hp.ext.clients.CustomObjectMapper;
import com.hp.ext.clients.solutionmanager.SolutionManagerServiceClient;
import com.hp.ext.clients.solutionmanager.SolutionManagerServiceClientImpl;
import com.hp.ext.service.solutionManager.Configuration;
import com.hp.ext.service.solutionManager.Configuration_Modify;
import com.hp.ext.service.solutionManager.Data;
import com.hp.ext.service.solutionManager.SolutionNotificationAgentRegistrationRecord;
import com.hp.ext.types.solutionManager.SolutionNotification;
import com.hp.jetadvantage.link.device.services.exceptions.IllegalSolutionException;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceSolutionManager;
import com.hp.jetadvantage.link.device.services.interfaces.IE2PayloadCallback;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.standard.common.DeviceManagementService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardJsonParser;
import com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelCallbackRegistry;
import com.hp.ws.websocket.JsonTypedObject;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.AbstractMap;

public class StandardDeviceSolutionManager extends StandardDeviceService implements IDeviceSolutionManager {
    public static final String E2SERVICE_SOLUTION_MANAGER_CANONICAL_GUN = "com.hp.ext.service.solutionManager.version.1";
    private static final String TAG = Constants.TAG + "/SolMgr";
    private static final String MIMETYPE_JSON = "application/json";
    private volatile IPayloadCallback payloadCallback = null;

    public StandardDeviceSolutionManager() {
        super(new SolutionNotificationAgentRegistrationRecord().getTypeGUN());
    }

    /**
     * Constructor for testing purpose only
     *
     * @param deviceManagementService
     */
    public StandardDeviceSolutionManager(DeviceManagementService deviceManagementService) {
        super(deviceManagementService, new SolutionNotificationAgentRegistrationRecord().getTypeGUN());
    }

    @Override
    public Configuration getConfiguration(String appPackageName) throws IllegalSolutionException {
        validateSolutionIdAndToken(appPackageName);
        E2call<Configuration> call = () -> {
            Log.d(TAG, "getConfiguration: E2call");
            SolutionManagerServiceClient client = createSolutionManagerServiceClient();
            return client.solutions().getMember(getSolutionId(appPackageName))
                    .configuration()
                    .getAsync(getSolutionToken(appPackageName)).get();
        };
        return perform(call);
    }

    @Override
    public Configuration modifyConfiguration(String appPackageName, Configuration_Modify configurationModify) throws IllegalSolutionException {
        validateSolutionIdAndToken(appPackageName);
        E2call<Configuration> call = () -> {
            Log.d(TAG, "modifyConfiguration: E2call");
            SolutionManagerServiceClient client = createSolutionManagerServiceClient();
            return client.solutions().getMember(getSolutionId(appPackageName))
                    .configuration()
                    .modifyAsync(getSolutionToken(appPackageName), configurationModify)
                    .get();
        };
        return perform(call);
    }

    @Override
    public String getConfigurationData(String appPackageName) throws IllegalSolutionException {
        validateSolutionIdAndToken(appPackageName);
        E2call<String> call = () -> {
            Log.d(TAG, "getConfigurationData: E2call started");
            SolutionManagerServiceClient client = createSolutionManagerServiceClient();
            AbstractMap.SimpleEntry<Data, byte[]> response =
                    client.solutions().getMember(getSolutionId(appPackageName))
                            .configuration()
                            .dataResource()
                            .getAsync(getSolutionToken(appPackageName))
                            .get();
            Data actualContentPart = response.getKey();
            String jsonString = new String(response.getValue());
            Log.d(TAG, "getConfigurationData: " + jsonString);
            return jsonString;
        };
        return perform(call);
    }

    @Override
    public void replaceConfigurationData(String appPackageName, InputStream data) throws IllegalSolutionException {
        validateSolutionIdAndToken(appPackageName);
        E2call<Data> call = () -> {
            Log.d(TAG, "replaceConfigurationData: E2call started");
            SolutionManagerServiceClient client = createSolutionManagerServiceClient();
            Data response = client.solutions().getMember(getSolutionId(appPackageName))
                    .configuration()
                    .dataResource()
                    .replaceAsync(getSolutionToken(appPackageName), data, MIMETYPE_JSON)
                    .get();
            Log.d(TAG, "replaceConfigurationData: " + response);
            return response;
        };
        perform(call);
    }

    @Override
    public void registerNotificationCallback(final IE2PayloadCallback<SolutionNotification> callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Callback cannot be null");
        }
        synchronized (this) {
            if (this.payloadCallback != null) {
                unRegisterNotificationCallback();
            }

            this.payloadCallback = new IPayloadCallback() {
                @Override
                public void onReceiveNotification(final String appPackageId, final JsonTypedObject notification) {
                    processSolutionNotification(appPackageId, notification, callback);
                }
            };

            AppChannelCallbackRegistry.registerPayloadCallback(E2SERVICE_SOLUTION_MANAGER_CANONICAL_GUN,
                    this.payloadCallback);
            Log.i(TAG, "registerNotificationCallback: registered for " + E2SERVICE_SOLUTION_MANAGER_CANONICAL_GUN);
        }
    }

    /**
     * Unregister callback to clean up
     */
    @Override
    public void unRegisterNotificationCallback() {
        synchronized (this) {
            if (this.payloadCallback != null) {
                AppChannelCallbackRegistry.unregisterPayloadCallback(E2SERVICE_SOLUTION_MANAGER_CANONICAL_GUN,
                        this.payloadCallback);
                this.payloadCallback = null;
                Log.i(TAG, "unRegisterNotificationCallback: unregistered payloadCallback for " +
                        E2SERVICE_SOLUTION_MANAGER_CANONICAL_GUN);
            }
        }
    }

    private SolutionManagerServiceClient createSolutionManagerServiceClient() throws URISyntaxException {
        return new SolutionManagerServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
    }

    private void processSolutionNotification(final String appPackageId, final JsonTypedObject notification,
                                             final IE2PayloadCallback<SolutionNotification> callback) {
        try {
            Log.d(TAG, "onReceiveNotification: appPackageId:" + appPackageId + " notification:" + notification);

            final SolutionNotification solutionNotification = new SolutionNotification();
            if (!notification.getTypeGUN().equals(solutionNotification.getTypeGUN())) {
                Log.i(TAG, "onReceiveNotification: unexpected typeGUN: " + notification.getTypeGUN());
                return;
            }

            final CustomObjectMapper<SolutionNotification> objectMapper =
                    new CustomObjectMapper<>(SolutionNotification.class);
            final String jsonStr = StandardJsonParser.INSTANCE.toJson(notification.getValue());
            final SolutionNotification parsedNotification = objectMapper.readValue(jsonStr);

            Log.d(TAG, "onReceiveNotification: appPackageId:" + appPackageId + " callback");
            callback.onReceiveNotification(appPackageId, parsedNotification);
        } catch (Exception e) {
            Log.e(TAG, "Error processing notification: " + e.getMessage(), e);
        }
    }
}
