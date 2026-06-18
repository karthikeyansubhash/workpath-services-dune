package com.hp.jetadvantage.link.services.accesslet.service;

import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceAuthenticationService;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceAuthenticationService;

public class AuthenticationServiceManager {
    private static final String TAG = "AuthServiceManager";
    private final IDeviceAuthenticationService mAuthenticationService;
    private final CallbackHandler mCallbackHandler;

    public AuthenticationServiceManager(CallbackHandler callbackHandler) {
        mAuthenticationService = new StandardDeviceAuthenticationService();
        mCallbackHandler = callbackHandler;
    }

    public void registerNotificationCallback() {
        SLog.i(TAG, "Registering notification callbacks");
        mAuthenticationService.registerAuthSessionChangeCallback(mCallbackHandler.authSessionChangeCallback);
        mAuthenticationService.registerPrePromptCallback(mCallbackHandler.prePromptCallback);
        mAuthenticationService.registerPostPromptCallback(mCallbackHandler.postPromptCallback);
        mAuthenticationService.registerSignOutNotificationCallback(mCallbackHandler.signOutNotificationCallback);
    }

    public void unregisterNotificationCallbacks() {
        SLog.i(TAG, "Unregistering notification callbacks");
        mAuthenticationService.unRegisterAuthSessionChangeCallback();
        mAuthenticationService.unRegisterPrePromptCallback();
        mAuthenticationService.unRegisterPostPromptCallback();
        mAuthenticationService.unRegisterSignOutNotificationCallback();
    }
}

