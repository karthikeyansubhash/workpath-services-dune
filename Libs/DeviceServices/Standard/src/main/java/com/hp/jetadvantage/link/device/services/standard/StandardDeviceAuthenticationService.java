package com.hp.jetadvantage.link.device.services.standard;

import android.util.Log;

import com.hp.ext.clients.authentication.AuthenticationServiceClient;
import com.hp.ext.clients.authentication.AuthenticationServiceClientImpl;
import com.hp.ext.service.authentication.AuthenticationAccessPoint_InitiateLogin;
import com.hp.ext.service.authentication.AuthenticationAgentRegistrationRecord;
import com.hp.ext.service.authentication.AuthenticationAgent_Login;
import com.hp.ext.service.authentication.Capabilities;
import com.hp.ext.service.authentication.Session_ForceLogout;
import com.hp.ext.types.authentication.PrePromptResult;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceAuthSessionChangeCallback;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceAuthenticationService;
import com.hp.jetadvantage.link.device.services.interfaces.IE2ServiceCallback;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.standard.common.DeviceManagementService;
import com.hp.jetadvantage.link.device.services.standard.common.SessionAccessTokenManager;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceService;

public class StandardDeviceAuthenticationService extends StandardDeviceService implements IDeviceAuthenticationService {
    public static final String E2SERVICE_AUTHENTICATION_CANONICAL_GUN = "com.hp.ext.service.authentication.version.1";
    private static final String TAG = Constants.TAG + "/Auth";
    private AuthenticationCallbackRegistry callbackRegistry;

    public StandardDeviceAuthenticationService() {
        super(new AuthenticationAgentRegistrationRecord().getTypeGUN());
        callbackRegistry = new AuthenticationCallbackRegistry(E2SERVICE_AUTHENTICATION_CANONICAL_GUN);
    }

    public StandardDeviceAuthenticationService(DeviceManagementService deviceManagementService) {
        super(deviceManagementService);
        callbackRegistry = new AuthenticationCallbackRegistry(E2SERVICE_AUTHENTICATION_CANONICAL_GUN);
    }

    @Override
    public boolean isSupported() {
        E2call<Capabilities> call = () -> {
            AuthenticationServiceClient client = new AuthenticationServiceClientImpl(getHttpClient(),
                    getDeviceIPAddress(), getDiscoveryTree());
            return client.capabilities().getAsync().get();
        };

        try {
            Capabilities e2AuthCap = perform(call);
            String serviceGun = e2AuthCap.getServiceGun();
            if (serviceGun != null && serviceGun.equalsIgnoreCase(E2SERVICE_AUTHENTICATION_CANONICAL_GUN)) {
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
    public AuthenticationAccessPoint_InitiateLogin initiateSignIn(String packageName) {
        E2call<AuthenticationAccessPoint_InitiateLogin> e2call = () -> {
            AuthenticationServiceClientImpl authenticationServiceClient =
                    new AuthenticationServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
            return authenticationServiceClient.authenticationAccessPoints().getMember(getAgentId(packageName)).initiateLogin().executeAsync(
                    getSolutionToken(packageName), null).get();
        };
        return perform(e2call);
    }

    @Override
    public AuthenticationAgent_Login signIn(String sessionAccessToken, PrePromptResult prePromptResult,
                                            String packageName) {
        E2call<AuthenticationAgent_Login> e2call = () -> {
            AuthenticationServiceClientImpl authenticationServiceClient =
                    new AuthenticationServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
            return authenticationServiceClient.authenticationAgents().getMember(getAgentId(packageName)).login().executeAsync(sessionAccessToken, prePromptResult).get();
        };
        return perform(e2call);
    }

    @Override
    public Session_ForceLogout signOut(String packageName) {
        String sessionAccessToken = SessionAccessTokenManager.getSessionAccessToken(packageName);
        E2call<Session_ForceLogout> e2call = () -> {
            AuthenticationServiceClientImpl authenticationServiceClient =
                    new AuthenticationServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
            return authenticationServiceClient.session().forceLogout().executeAsync(sessionAccessToken).get();
        };

        Session_ForceLogout result = perform(e2call);
        SessionAccessTokenManager.removeSessionAccessToken();
        return result;
    }

    @Override
    public void registerAuthSessionChangeCallback(IDeviceAuthSessionChangeCallback callback) {
        if (callback != null) {
            callbackRegistry.registerAuthChangeCallback(callback);
        }
    }

    @Override
    public void registerPrePromptCallback(IE2ServiceCallback callback) {
        if (callback != null) {
            callbackRegistry.registerPrePromptCallback(callback);
        }
    }

    @Override
    public void registerPostPromptCallback(IE2ServiceCallback callback) {
        if (callback != null) {
            callbackRegistry.registerPostPromptCallback(callback);
        }
    }

    @Override
    public void registerSignOutNotificationCallback(IE2ServiceCallback callback) {
        if (callback != null) {
            callbackRegistry.registerSignOutNotificationCallback(callback);
        }
    }

    @Override
    public void unRegisterAuthSessionChangeCallback() {
                callbackRegistry.unRegisterAuthChangeCallback();
    }

    @Override
    public void unRegisterPrePromptCallback() {
        callbackRegistry.unRegisterPrePromptCallback();
    }

    @Override
    public void unRegisterPostPromptCallback() {
        callbackRegistry.unRegisterPostPromptCallback();
    }

    @Override
    public void unRegisterSignOutNotificationCallback() {
        callbackRegistry.unRegisterSignOutNotificationCallback();
    }
}
