package com.hp.jetadvantage.link.device.services.standard;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hp.jetadvantage.link.common.utils.JsonParser;
import com.hp.jetadvantage.link.common.utils.StringUtility;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceAuthSessionChangeCallback;
import com.hp.jetadvantage.link.device.services.interfaces.IE2ServiceCallback;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.standard.common.SessionAccessTokenManager;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceService;
import com.hp.jetadvantage.link.device.services.standard.services.SystemManagementMessageHandler;
import com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelCallbackRegistry;
import com.hp.ws.websocket.AppChannelService;

public class AuthenticationCallbackRegistry {
    private static final String TAG = Constants.TAG + "/Auth/CR";
    private static final String SERVICE_PATH_PRE_PROMPT_RESULT = "prePromptResult";
    private static final String SERVICE_PATH_POST_PROMPT_RESULT = "postPromptResult";
    private static final String SERVICE_SIGN_OUT_NOTIFICATION = "signoutNotification";

    private final String serviceTypeGun;

    private IDeviceAuthSessionChangeCallback authChangeCallback;
    private StandardDeviceService.IServiceCallback servicePrePromptCallback;
    private StandardDeviceService.IServiceCallback servicePostPromptCallback;
    private StandardDeviceService.IServiceCallback serviceSignOutNotificationCallback;

    public AuthenticationCallbackRegistry(String serviceTypeGun) {
        this.serviceTypeGun = serviceTypeGun;
    }

    protected synchronized void registerAuthChangeCallback(IDeviceAuthSessionChangeCallback callback) {
        Log.d(TAG, "registerAuthChangeCallback: ENTER");
        if (authChangeCallback != null) {
            Log.w(TAG, "registerAuthChangeCallback: Callback already registered, unregistering first.");
            unRegisterAuthChangeCallback();
        }
        authChangeCallback = callback;
        SystemManagementMessageHandler.addAuthSessionChangeListener(callback);
        Log.d(TAG, "registerAuthChangeCallback: EXIT");
    }

    protected void registerPrePromptCallback(IE2ServiceCallback callback) {
        Log.i(TAG, "registerPrePromptCallback");
        if (this.servicePrePromptCallback != null) {
            unRegisterPrePromptCallback();
        }

        this.servicePrePromptCallback = (authnTargetActivity, serviceMessage) -> {
            Log.e(TAG, "servicePrePromptCallback :" + JsonParser.getInstance().toJson(serviceMessage));
            String packageName = getPackageName(authnTargetActivity);
            if (StringUtility.isEmpty(packageName)) {
                Log.e(TAG, "servicePrePromptCallback: invalid authnTargetActivity :" + authnTargetActivity);
                return null;
            }
            storeSessionToken(packageName, serviceMessage);
            return callback.onReceive(packageName, serviceMessage);
        };
        AppChannelCallbackRegistry.registerServiceCallback(serviceTypeGun, SERVICE_PATH_PRE_PROMPT_RESULT,
                servicePrePromptCallback);
    }

    protected void registerPostPromptCallback(IE2ServiceCallback callback) {
        Log.i(TAG, "registerPostPromptCallback");
        if (this.servicePostPromptCallback != null) {
            unRegisterPostPromptCallback();
        }

        this.servicePostPromptCallback = (authnTargetActivity, serviceMessage) -> {
            String packageName = getPackageName(authnTargetActivity);
            if (StringUtility.isEmpty(packageName)) {
                Log.e(TAG, "servicePostPromptCallback: invalid authnTargetActivity :" + authnTargetActivity);
                return null;
            }
            storeSessionToken(packageName, serviceMessage);
            return callback.onReceive(packageName, serviceMessage);
        };
        AppChannelCallbackRegistry.registerServiceCallback(serviceTypeGun, SERVICE_PATH_POST_PROMPT_RESULT,
                servicePostPromptCallback);
    }

    protected void registerSignOutNotificationCallback(IE2ServiceCallback callback) {
        Log.i(TAG, "registerSignOutNotificationCallback");
        if (this.serviceSignOutNotificationCallback != null) {
            unRegisterSignOutNotificationCallback();
        }

        this.serviceSignOutNotificationCallback = (authnTargetActivity, serviceMessage) -> {
            String packageName = getPackageName(authnTargetActivity);
            if (StringUtility.isEmpty(packageName)) {
                Log.e(TAG, "serviceSignOutNotificationCallback: invalid authnTargetActivity :" + authnTargetActivity);
                return null;
            }
            return callback.onReceive(packageName, serviceMessage);
        };
        AppChannelCallbackRegistry.registerServiceCallback(serviceTypeGun, SERVICE_SIGN_OUT_NOTIFICATION,
                serviceSignOutNotificationCallback);
    }

    private String getPackageName(String authnTargetActivity) {
        if (StringUtility.isEmpty(authnTargetActivity)) {
            Log.e(TAG, "getPackageName: Empty authnTargetActivity received");
            return "";
        }
        String packageName = authnTargetActivity.split("/")[0];
        Log.d(TAG, "PackageName received: " + packageName);
        return packageName;
    }

    private void storeSessionToken(String appPackageName, AppChannelService serviceMessage) {
        if (serviceMessage != null && serviceMessage.getRequestBody() != null) {
            JsonElement valueElement = serviceMessage.getRequestBody().getValue();
            if (valueElement != null && valueElement.isJsonObject()) {
                String sessionAccessToken = getSessionAccessToken(valueElement);
                SessionAccessTokenManager.storeSessionAccessToken(appPackageName, sessionAccessToken);
            }
        }
    }

    protected synchronized void unRegisterAuthChangeCallback() {
        Log.d(TAG, "unRegisterAuthChangeCallback: ENTER");
        if (authChangeCallback != null) {
            SystemManagementMessageHandler.removeAuthSessionChangeListener(authChangeCallback);
        }
        Log.d(TAG, "unRegisterAuthChangeCallback: EXIT");
    }

    protected void unRegisterPrePromptCallback() {
        if (this.servicePrePromptCallback != null) {
            AppChannelCallbackRegistry.unregisterServiceCallback(serviceTypeGun, SERVICE_PATH_PRE_PROMPT_RESULT);
            this.servicePrePromptCallback = null;
        }
    }

    protected void unRegisterPostPromptCallback() {
        if (this.servicePostPromptCallback != null) {
            AppChannelCallbackRegistry.unregisterServiceCallback(serviceTypeGun, SERVICE_PATH_POST_PROMPT_RESULT);
            this.servicePostPromptCallback = null;
        }
    }

    protected void unRegisterSignOutNotificationCallback() {
        if (this.serviceSignOutNotificationCallback != null) {
            AppChannelCallbackRegistry.unregisterServiceCallback(serviceTypeGun, SERVICE_SIGN_OUT_NOTIFICATION);
            this.serviceSignOutNotificationCallback = null;
        }
    }

    private String getSessionAccessToken(JsonElement valueElement) {
        if (valueElement != null && valueElement.isJsonObject()) {
            JsonObject valueObject = valueElement.getAsJsonObject();
            JsonElement sessionAccessTokenObject = valueObject.get("sessionAccessToken");
            if (sessionAccessTokenObject != null) {
                return sessionAccessTokenObject.getAsString();
            }
        }
        return null;
    }
}
