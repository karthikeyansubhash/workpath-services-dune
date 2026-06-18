package com.hp.jetadvantage.link.device.services.standard.common;

import android.text.TextUtils;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hp.ext.clients.OXPdHttpRequestException;
import com.hp.ext.types.common.ServicesDiscoveryImpl;
import com.hp.jetadvantage.link.common.utils.StringUtility;
import com.hp.jetadvantage.link.device.services.clients.CDMClient;
import com.hp.jetadvantage.link.device.services.clients.CDMResponse;
import com.hp.jetadvantage.link.device.services.exceptions.BoundDeviceException;
import com.hp.jetadvantage.link.device.services.exceptions.IllegalSolutionException;
import com.hp.jetadvantage.link.device.services.interfaces.IAbstractDeviceService;
import com.hp.net.http.HttpClient;
import com.hp.ws.websocket.AppChannelService;
import com.hp.ws.websocket.AppChannelServiceResponse;
import com.hp.ws.websocket.AppChannelSetup;
import com.hp.ws.websocket.JsonTypedObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Abstract StandardDeviceService class
 * - parent class for all device service implementation
 */
public abstract class StandardDeviceService implements IAbstractDeviceService {
    protected String TAG = Constants.TAG + "/DeviceService";
    protected DeviceManagementService deviceManagementService;

    protected String e2AgentRegistrationRecordTypeGun = "";

    protected StandardDeviceService() {
        deviceManagementService = StandardDeviceManagementService.getInstance();
    }

    protected StandardDeviceService(String agentRegistrationRecordTypeGun) {
        e2AgentRegistrationRecordTypeGun = agentRegistrationRecordTypeGun;
        deviceManagementService = StandardDeviceManagementService.getInstance();
    }

    protected StandardDeviceService(DeviceManagementService deviceManagementService) {
        this.deviceManagementService = deviceManagementService;
    }

    protected StandardDeviceService(DeviceManagementService deviceManagementService,
                                    String agentRegistrationRecordTypeGun) {
        e2AgentRegistrationRecordTypeGun = agentRegistrationRecordTypeGun;
        this.deviceManagementService = deviceManagementService;
    }

    @Override
    public boolean isUiContextAvailable(String packageName) {
        String uiContextToken = getUiContextToken(packageName);
        return !TextUtils.isEmpty(uiContextToken);
    }

    protected String getAgentId(String packageName) {
        return this.deviceManagementService.getAgentId(packageName, e2AgentRegistrationRecordTypeGun);
    }

    protected String getSolutionId(String packageName) {
        return this.deviceManagementService.getSolutionId(packageName);
    }

    protected String getSolutionToken(String packageName) {
        return this.deviceManagementService.getSolutionToken(packageName);
    }

    protected HttpClient getHttpClient() {
        //This is dummy object to minimize OXPd source code changes
        return new HttpClient();
    }

    protected CDMClient getCDMClient() {
        return deviceManagementService.getCDMClient();
    }

    protected String getDeviceIPAddress() {
        return deviceManagementService.getDeviceIPAddress();
    }

    protected ServicesDiscoveryImpl getDiscoveryTree() {
        return deviceManagementService.getDiscoveryTree();
    }

    protected String getUiContextToken(String packageName) {
        return deviceManagementService.getUiContextToken(packageName);
    }

    protected boolean isDeviceConnected() {
        return deviceManagementService.isDeviceConnected();
    }

    protected <T> T perform(E2call<T> func) {
        if (!isDeviceConnected()) {
            throw new BoundDeviceException("There is no bound device.");
        }

        try {
            return func.request();
        } catch (URISyntaxException exception) {
            Log.e(TAG, "perform: URISyntaxException=" + exception.getMessage(), exception);
            throw new BoundDeviceException("Device network address is not a valid URI.");
        } catch (ExecutionException executionException) {
            if (Objects.requireNonNull(executionException.getCause()).getClass() == OXPdHttpRequestException.class) {
                Log.e(TAG, "perform: OXPdHttpRequestException=" + executionException.getCause().getMessage(),
                        executionException.getCause());
                throw (OXPdHttpRequestException) executionException.getCause();
            }
            Log.e(TAG, "perform: ExecutionException=" + executionException.getMessage(), executionException);
            throw new RuntimeException(executionException);
        } catch (InterruptedException interruptedException) {
            Log.e(TAG, "perform: InterruptedException=" + interruptedException.getMessage(), interruptedException);
            throw new RuntimeException(interruptedException);
        } catch (IOException e) {
            Log.e(TAG, "perform: IOException=" + e.getMessage(), e);
            throw new BoundDeviceException("IOException:" + e.getCause());
        }
    }

    protected <T, P> T perform(E2callUniParam<T, P> func, P param) {
        if (!isDeviceConnected()) {
            throw new BoundDeviceException("There is no bound device.");
        }

        try {
            return (T) func.request(param);
        } catch (URISyntaxException exception) {
            Log.e(TAG, "perform 1: URISyntaxException=" + exception.getMessage(), exception);
            throw new BoundDeviceException("Device network address is not a valid URI.");
        } catch (ExecutionException executionException) {
            if (Objects.requireNonNull(executionException.getCause()).getClass() == OXPdHttpRequestException.class) {
                Log.e(TAG, "perform 1: OXPdHttpRequestException=" + executionException.getCause().getMessage(),
                        executionException.getCause());
                throw (OXPdHttpRequestException) executionException.getCause();
            }
            Log.e(TAG, "perform 1: ExecutionException=" + executionException.getMessage(), executionException);
            throw new RuntimeException(executionException);
        } catch (InterruptedException interruptedException) {
            Log.e(TAG, "perform 1: InterruptedException=" + interruptedException.getMessage(), interruptedException);
            throw new RuntimeException(interruptedException);
        }
    }

    protected <T, P, Q> T perform(E2callBiParam<T, P, Q> func, P param1, Q param2) {
        if (!isDeviceConnected()) {
            Log.e(TAG, "perform 2: Device is not connected");
            throw new BoundDeviceException("There is no bound device.");
        }

        try {
            return (T) func.request(param1, param2);
        } catch (URISyntaxException exception) {
            Log.e(TAG, "perform 2: URISyntaxException=" + exception.getMessage(), exception);
            throw new BoundDeviceException("Device network address is not a valid URI.");
        } catch (ExecutionException executionException) {
            if (Objects.requireNonNull(executionException.getCause()).getClass() == OXPdHttpRequestException.class) {
                Log.e(TAG, "perform 2: OXPdHttpRequestException=" + executionException.getCause().getMessage(),
                        executionException.getCause());
                throw (OXPdHttpRequestException) executionException.getCause();
            }
            Log.e(TAG, "perform 2: ExecutionException=" + executionException.getMessage(), executionException);
            throw new RuntimeException(executionException);
        } catch (InterruptedException interruptedException) {
            Log.e(TAG, "perform 2: InterruptedException=" + interruptedException.getMessage(), interruptedException);
            throw new RuntimeException(interruptedException);
        } catch (JsonProcessingException jsonProcessingException) {
            Log.e(TAG, "perform 2: JsonProcessingException=" + jsonProcessingException.getMessage(),
                    jsonProcessingException);
            throw new RuntimeException(jsonProcessingException);
        } catch (IOException ioe) {
            Log.e(TAG, "perform 2: IOException=" + ioe.getMessage(), ioe);
            throw new RuntimeException(ioe);
        }
    }

    protected <T> T perform(CdmCall func, Class<T> tClass) {
        if (!isDeviceConnected()) {
            Log.e(TAG, "perform CdmCall 1: Device is not connected");
            throw new BoundDeviceException("There is no bound device.");
        }

        if (getCDMClient() == null) {
            Log.e(TAG, "perform CdmCall 1: CDMClient is null");
            throw new BoundDeviceException("CDMClient is not created.");
        }

        CDMResponse<String> response;

        try {
            response = func.request();
        } catch (IOException e) {
            Log.e(TAG, "perform CdmCall 1: IOException=" + e.getMessage(), e);
            throw new BoundDeviceException(e.getMessage());
        }

        try {
            T responseJsonData = (T) StandardJsonParser.INSTANCE.fromJson(response.httpBody, tClass);
            return responseJsonData;
        } catch (ClassCastException e) {
            Log.e(TAG, "perform CdmCall 1: Unexpected ClassCastException :" + e);
            return null;
        }
    }

    protected void perform(CdmCall func) {
        if (!isDeviceConnected()) {
            Log.e(TAG, "perform CdmCall: Device is not connected");
            throw new BoundDeviceException("There is no bound device.");
        }

        if (getCDMClient() == null) {
            Log.e(TAG, "perform CdmCall: CDMClient is null");
            throw new BoundDeviceException("CDMClient is not created.");
        }

        try {
            func.request();
        } catch (IOException e) {
            Log.e(TAG, "perform CdmCall: IOException=" + e.getMessage(), e);
            throw new BoundDeviceException(e.getMessage());
        }
    }

    protected void validateSolutionIdAndToken(String appPackageName) {
        String solutionId = getSolutionId(appPackageName);
        if (StringUtility.isEmpty(solutionId)) {
            Log.e(TAG, "Solution ID cannot be found for app: " + appPackageName);
            throw new IllegalSolutionException("Solution ID cannot be found");
        }

        if (StringUtility.isEmpty(getSolutionToken(appPackageName))) {
            Log.e(TAG, "Solution token cannot be found for app: " + appPackageName);
            throw new IllegalSolutionException("Solution token cannot be found");
        }
    }

    @FunctionalInterface
    public interface E2call<T> {
        T request() throws URISyntaxException, ExecutionException, InterruptedException, IOException;
    }

    @FunctionalInterface
    public interface E2callUniParam<T, P> {
        T request(P param1) throws URISyntaxException, ExecutionException, InterruptedException;
    }

    @FunctionalInterface
    public interface E2callBiParam<T, P, Q> {
        T request(P param1, Q param2) throws URISyntaxException, ExecutionException, InterruptedException,
                JsonProcessingException, IOException;
    }

    @FunctionalInterface
    public interface CdmCall {
        CDMResponse<String> request() throws IOException;
    }

    @FunctionalInterface
    public interface IPayloadCallback {
        void onReceiveNotification(String appPackageId, JsonTypedObject notification);
    }

    @FunctionalInterface
    public interface ISetupCallback {
        void onSetupNotification(String appPackageId, AppChannelSetup channelMessage);
    }

    @FunctionalInterface
    public interface IServiceCallback {

        /**
         * callback function for service call
         *
         * @param target         target for the service call
         *                       In case of authentication service call, this is the authenticationTarget in the
         *                       authenticationAgentRegistrationRecord.
         *                       (ex : "com.hp.workpath.sample.authentication/.AuthenticationActivity")
         * @param serviceMessage service message
         * @return
         * @throws InterruptedException
         */
        AppChannelServiceResponse onServiceCall(String target, AppChannelService serviceMessage) throws InterruptedException;
    }
}
