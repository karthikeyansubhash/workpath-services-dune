// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.accesslet.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.hp.jetadvantage.link.api.access.AbstractAuthenticationService;
import com.hp.jetadvantage.link.api.access.Accesslet;
import com.hp.jetadvantage.link.api.access.Principal;
import com.hp.jetadvantage.link.common.Platform;
import com.hp.jetadvantage.link.common.constants.CommonConstants;
import com.hp.jetadvantage.link.common.contract.SelectedAccessContract;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.standard.common.PackageManagerHelper;
import com.hp.jetadvantage.link.services.accesslet.event.AccessEventManager;
import com.hp.jetadvantage.link.services.accesslet.util.AuthenticationUtility;
import com.hp.jetadvantage.link.services.common.notification.ServiceNotification;
import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;

import java.util.ArrayList;
import java.util.Map;

public class AuthenticationObserverService extends Service {
    private static final String TAG = Accesslet.TAG + "/AuthSVC";

    private Handler mHandler;
    private AuthenticationServiceManager mAuthServiceManager;

    private final String ACTION_SIGNIN = "com.hp.jetadvantage.link.intent.action.SIGNIN";
    private final String ACTION_SIGNOUT = "com.hp.jetadvantage.link.intent.action.SIGNOUT";
    private final String EXTRA_PROVIDER_ID = "EXTRA_PROVIDER_ID";
    private final String EXTRA_PRINICPAL = "EXTRA_PRINICPAL";

    public AuthenticationObserverService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            ensurePermissions();

            if (Platform.isPanel()) {
                initializeDependencies();
                initializeSystemProperties();

                ServiceNotification.showNotification(this);
                CommonConstants.sendBroadCastForBoot(getApplicationContext(),
                        CommonConstants.BroadcastActions.READY_ACCESSLET);

                mHandler.sendMessage(mHandler.obtainMessage(ServiceHandler.MSG_START));
                SLog.d(TAG, "Authentication service successfully created");
            } else {
                SLog.d(TAG, "Not created auth svc - not on panel device");
            }
        } catch (Throwable e) {
            SLog.e(TAG, "Failed to create authentication service: " + e.getMessage(), e);
        }
    }

    private void ensurePermissions() {
        try {
            SpsPermissionHelper.ensurePermission(getApplicationContext());
        } catch (Throwable throwable) {
            SLog.i(TAG, "Permission error in authentication service (ignored): " + throwable.getMessage());
        }
    }

    private void initializeHandlerThread() {
        HandlerThread handlerThread = new HandlerThread(Accesslet.TAG + ":" + getClass().getSimpleName(),
                Process.THREAD_PRIORITY_BACKGROUND);
        handlerThread.start();
        mHandler = new ServiceHandler(handlerThread.getLooper(), this, mAuthServiceManager);
    }

    private void initializeDependencies() {
        CallbackHandler callbackHandler = new CallbackHandler(getApplicationContext(), this);
        mAuthServiceManager = new AuthenticationServiceManager(callbackHandler);
        initializeHandlerThread();
    }

    private void initializeSystemProperties() {
        try {
            System.setProperty(CommonConstants.SYSTEM_SVC_FLAG, TAG);
            SLog.d(TAG, "Created auth svc.");
        } catch (Exception ignored) {
            SLog.w(TAG, "Failed to set system property (non-critical)");
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            ensurePermissions();
            SLog.d(TAG, "Authentication service starting operations");
        } catch (Throwable e) {
            SLog.e(TAG, "Failed to start authentication service: " + e.getMessage(), e);
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAuthServiceManager != null) {
            mAuthServiceManager.unregisterNotificationCallbacks();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void start(final Context context) {
        try {
            SpsPermissionHelper.ensurePermission(context);

            if (Platform.isPanel()) {
                SLog.d(TAG, "Start auth svc.");
                final Intent intent = new Intent(context, AuthenticationObserverService.class);
                context.startForegroundService(intent);
            } else {
                SLog.d(TAG, "Not started auth svc.");
            }
        } catch (Throwable e) {
            SLog.d(TAG, "Not created auth svc because of permission(start)." + e.getMessage());
        }
    }

    public void sendMessageToClient(Context context, String agentPackageName, Principal principal) {
        try {
            SLog.d(TAG, "Authentication session is changed, start sending an event: " + agentPackageName);

            PackageManagerHelper pmHelper = new PackageManagerHelper();
            String agentId = pmHelper.getAgentId(context, agentPackageName, AuthenticationUtility.AUTH_PROVIDER);

            int messageCode = principal.isAuthenticated() ?
                    AbstractAuthenticationService.AuthMessage.SIGN_IN :
                    AbstractAuthenticationService.AuthMessage.SIGN_OUT;

            Bundle bundleForAuth = new Bundle(1);

            if (agentPackageName != null) {
                Intent intent = new Intent(Accesslet.PROVIDER_EVENT_ACTION);
                intent.setPackage(agentPackageName);

                Bundle bundle = new Bundle(1);
                bundle.putParcelable(ServiceHandler.MSG_DATA_INTENT, intent);
                bundle.putParcelable(ServiceHandler.MSG_DATA_PRINCIPAL, principal);

                Message msg = mHandler.obtainMessage(ServiceHandler.MSG_SEND_SIGNIN, messageCode, 0, bundle);
                mHandler.sendMessage(msg);

                Bundle extras = new Bundle();
                extras.putString(SelectedAccessContract.KEY_ACCESS_INFO_ID, agentId);
                extras.putString(SelectedAccessContract.KEY_ACCESS_INFO_PKG, agentPackageName);
                context.getContentResolver()
                        .call(SelectedAccessContract.CONTENT_URI,
                                SelectedAccessContract.Method.PUT,
                                null,
                                extras);

                SLog.d(TAG, "Authentication session is changed, start sending signin BroadCastEvent");

                bundleForAuth.putString(EXTRA_PROVIDER_ID, agentId);
                bundleForAuth.putParcelable(EXTRA_PRINICPAL, principal);
                AccessEventManager.sendEventBroadcast(context, ACTION_SIGNIN, bundleForAuth, null);
            } else if (!principal.isAuthenticated()) {
                try {
                    final Bundle returnBundle = context.getContentResolver()
                            .call(SelectedAccessContract.CONTENT_URI,
                                    SelectedAccessContract.Method.GET,
                                    null,
                                    null);
                    agentId = returnBundle.getString(SelectedAccessContract.KEY_ACCESS_INFO_ID);
                    agentPackageName = returnBundle.getString(SelectedAccessContract.KEY_ACCESS_INFO_PKG);

                    Intent intent = new Intent(Accesslet.PROVIDER_EVENT_ACTION);
                    intent.setPackage(agentPackageName);

                    Bundle bundle = new Bundle(1);
                    bundle.putParcelable(ServiceHandler.MSG_DATA_INTENT, intent);

                    try {
                        if (TextUtils.isEmpty(agentPackageName)) {
                            Map<String, String> names = AuthenticationUtility.getAuthAgentPackageNameList(context);
                            bundle.putStringArrayList(ServiceHandler.MSG_DATA_AUTHPKG, new ArrayList<>(names.values()));
                        } else {
                            ArrayList<String> pkgName = new ArrayList<>();
                            pkgName.add(agentPackageName);
                            bundle.putStringArrayList(ServiceHandler.MSG_DATA_AUTHPKG, pkgName);
                        }
                    } catch (Exception e) {
                        SLog.e(TAG, "Authentication session is changed, failed to set target: " + e.getMessage());
                    }

                    SLog.d(TAG, "Authentication session is changed, start calling signout to agentId: " + agentId);

                    Message msg = mHandler.obtainMessage(ServiceHandler.MSG_SEND_SIGNOUT, messageCode, 0, bundle);
                    mHandler.sendMessage(msg);

                    context.getContentResolver()
                            .call(SelectedAccessContract.CONTENT_URI,
                                    SelectedAccessContract.Method.CLEAR_ALL,
                                    null,
                                    null);
                } catch (Exception e) {
                    SLog.e(TAG, "Failed to send message for signout.");
                }

                SLog.d(TAG, "Authentication session is changed, start sending signout BroadCastEvent");
                AccessEventManager.sendEventBroadcast(context, ACTION_SIGNOUT, bundleForAuth, null);
            }

        } catch (Exception e) {
            SLog.e(TAG, "Failed to send message to the client with " + e.getMessage());
        }
    }

    public void sendMessagePrePromptToClient(String agentPackageName) {
        try {
            if (agentPackageName != null) {
                Intent intent = new Intent(Accesslet.PROVIDER_EVENT_ACTION);
                intent.setPackage(agentPackageName);

                int messageCode = AbstractAuthenticationService.AuthMessage.MSG_PRE_PROMPT;

                Bundle bundle = new Bundle();
                bundle.putParcelable(ServiceHandler.MSG_DATA_INTENT, intent);
                Message msg = mHandler.obtainMessage(ServiceHandler.MSG_PRE_PROMPT, messageCode, 0, bundle);
                mHandler.sendMessage(msg);
                SLog.w(TAG, "Sent MSG_PRE_PROMPT to the client for : " + agentPackageName);
            } else {
                SLog.w(TAG, "Could not find package name. Cannot send MSG_PRE_PROMPT.");
            }
        } catch (Exception e) {
            SLog.e(TAG, "Failed to send MSG_PRE_PROMPT to the client for : " + agentPackageName, e);
        }
    }
}
