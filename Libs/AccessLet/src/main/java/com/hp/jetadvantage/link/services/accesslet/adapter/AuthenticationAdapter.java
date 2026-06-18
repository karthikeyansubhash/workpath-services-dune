package com.hp.jetadvantage.link.services.accesslet.adapter;

import com.hp.ext.service.authentication.AuthenticationAccessPoint_InitiateLogin;
import com.hp.ext.service.authentication.AuthenticationAgent_Login;
import com.hp.ext.service.authentication.Session_ForceLogout;
import com.hp.ext.types.authentication.PrePromptResult;
import com.hp.jetadvantage.link.api.access.Accesslet;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceAuthenticationService;

public class AuthenticationAdapter {
    private static final String TAG = Accesslet.TAG + "/AuthAdapter";

    public static boolean isSupported(IDeviceAuthenticationService authenticationService) {
        return authenticationService.isSupported();
    }

    public static AuthenticationAccessPoint_InitiateLogin initiatedSignIn(IDeviceAuthenticationService authenticationService, String packageName) {
        return authenticationService.initiateSignIn(packageName);
    }

    public static AuthenticationAgent_Login signIn(IDeviceAuthenticationService authenticationService, String sessionAccessToken, PrePromptResult prePromptResult, String packageName) {
        return authenticationService.signIn(sessionAccessToken, prePromptResult, packageName);
    }

    public static Session_ForceLogout signOut(IDeviceAuthenticationService authenticationService, String packageName) {
        return authenticationService.signOut(packageName);
    }
}
