// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.access;

import androidx.annotation.Nullable;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.text.TextUtils;

import com.hp.jetadvantage.link.common.annotation.DeviceApi;
import com.hp.jetadvantage.link.common.utils.SLog;

import java.lang.ref.WeakReference;

/**
 * <p>Implementations of this class provide functions to create Link authentication agent for integrating with device authentication.</p>
 * <p>A Link authentication agent implementor can create 3 types of authentication services :
 * <ul>
 *     <li>Prompt</li>
 *     <li>Preprompt</li>
 *     <li>Preprompt and Prompt</li>
 * </ul>
 * </p>
 * <p>
 * Prompt type needs to implement an "Activity" to show the sign in form and has to configure "Activity" name in hpk file using hpktool(SDK tool).
 * Preprompt type needs to receive onPrePrompt() event and has to be checked as preprompt option in hpk file.
 * </p>
 * <p>
 * When authentication service is started, Link platform checks Authentication Agent types which is included in installation option from hpk configuration and decide the next following steps :
 * <ul>
 *     <li>If Authentication Agent has Prompt, "Activity" will be launched.</li>
 *     <li>If Authentication Agent has Preprompt, onPrePrompt() method will be called.</li>
 * </ul>
 * </p>
 * <p>
 * A Link authentication agent which has Preprompt will call {@link AccessService AccessService}.signIn() method with {@link SignInAction SignInAction} to decide authentication process.
 * More details actions can find in {@link SignInAction SignInAction}.
 * </p>
 *
 * @since API 2
 */
@DeviceApi
public abstract class AbstractAuthenticationService extends Service {
    private static final String TAG = "[AbstractAuth]";
    /**
     * @hide for internal use
     */
    public static final class AuthMessage {
        public static final int SIGN_IN = 100;
        public static final int SIGN_OUT = 200;
        public static final int MSG_PRE_PROMPT = 300;
    }

    private Messenger mMessenger;
    private final Object mLock = new Object();
    private boolean mOnCreateCalled = false;

    /**
     * @hide for internal use
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        SLog.d(TAG, "onBind");

        if (!mOnCreateCalled) {
            throw new RuntimeException("super.onCreate() not called");
        }
        if (TextUtils.equals(intent.getAction(), Accesslet.PROVIDER_EVENT_ACTION)) {
            return mMessenger.getBinder();
        }

        throw new UnsupportedOperationException("Unknown action");
    }

    /**
     * @hide for internal use
     */
    @Override
    public boolean onUnbind(Intent intent) {
        SLog.d(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    /**
     * @hide for internal use
     */
    @Override
    public void onCreate() {
        SLog.d(TAG, "onCreate");
        super.onCreate();
        mMessenger = new Messenger(new ServiceHandler(getMainLooper(), this));
        mOnCreateCalled = true;
    }

    /**
     * @hide for internal use
     */
    @Override
    public void onDestroy() {
        SLog.d(TAG, "onDestroy");
        super.onDestroy();
    }

    private static class ServiceHandler extends Handler {
        private final WeakReference<AbstractAuthenticationService> mRef;

        ServiceHandler(Looper looper, AbstractAuthenticationService context) {
            super(looper);
            mRef = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            AbstractAuthenticationService serviceBase = mRef.get();
            if (serviceBase == null) {
                //fail requests
                return;
            }

            Bundle data = msg.getData();

            switch (msg.what) {
                // SignIn
                case AuthMessage.SIGN_IN:
                    synchronized (serviceBase.mLock) {
                        data.setClassLoader(Principal.class.getClassLoader());
                        Principal principal = data.getParcelable(Accesslet.PRINCIPAL_EXTRA);
                        serviceBase.onSignIn(principal);
                    }
                    break;

                // SignOut
                case AuthMessage.SIGN_OUT:
                    synchronized (serviceBase.mLock) {
                        serviceBase.onSignOut();
                    }
                    break;

                // PrePrompt
                case AuthMessage.MSG_PRE_PROMPT:
                    synchronized (serviceBase.mLock) {
                        serviceBase.onPrePrompt();
                    }
                    break;

                default:
                    // return error
            }
        }
    }

    /**
     * <p>Called to notify the client that device has been authenticated successfully with user information.</p>
     *
     * @param principal Authenticated user details.
     * @since API 2
     */
    protected abstract void onSignIn(Principal principal);

    /**
     * <p>Called to notify the client that SignOut operation is finished completely by a user request or authenticated user session is expired by timeout.</p>
     *
     * @since API 2
     */
    protected abstract void onSignOut();

    /**
     * <p>Called to notify the client that prePrompt action is required to proceed the authentication.
     * Link Authentication Agent can decide actions between {@link SignInAction SignInAction} before starting prompt activity.</p>
     *
     * @since API 2
     */
    protected abstract void onPrePrompt();
}
