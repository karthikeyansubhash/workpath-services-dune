package com.hp.jetadvantage.link.device.services.interfaces;

import com.hp.ext.service.authentication.AuthenticationAccessPoint_InitiateLogin;
import com.hp.ext.service.authentication.AuthenticationAgent_Login;
import com.hp.ext.service.authentication.Session_ForceLogout;
import com.hp.ext.types.authentication.PrePromptResult;

public interface IDeviceAuthenticationService {
    boolean isSupported();

    AuthenticationAccessPoint_InitiateLogin initiateSignIn(String packageName);

    AuthenticationAgent_Login signIn(String sessionAccessToken, PrePromptResult prePromptResult, String packageName);

    Session_ForceLogout signOut(String sessionAccessToken);

    /**
     * Register a callback to be notified of changes in the authentication session.
     * This callback is invoked whenever the user signs in or out on the front panel,
     * irrespective of the authentication agent (Workpath or otherwise).
     * Only one callback instance can be registered at a time for a instance of this service.
     * This means that if a new callback is registered, it will replace any previously registered callback.
     *
     * @param callback The callback to register.
     */
    void registerAuthSessionChangeCallback(IDeviceAuthSessionChangeCallback callback);

    void registerPrePromptCallback(IE2ServiceCallback callback);

    void registerPostPromptCallback(IE2ServiceCallback callback);
    void registerSignOutNotificationCallback(IE2ServiceCallback callback);

    /**
     * Unregisters the callback that was registered to be notified of changes in the authentication session.
     */
    void unRegisterAuthSessionChangeCallback();

    void unRegisterPrePromptCallback();

    void unRegisterPostPromptCallback();

    void unRegisterSignOutNotificationCallback();
}
