// Copyright 2025 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.authorization;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.ResultReceiver;
import android.text.TextUtils;

import com.hp.jetadvantage.link.common.utils.JsonParser;
import com.hp.jetadvantage.link.common.utils.SLog;

import java.lang.ref.WeakReference;

/**
 * AbstractAuthorizationService provides the base implementation for handling authorization features on the Workpath platform.
 * This service handles user authorization data.
 *
 * @since API 9
 */
public abstract class AbstractAuthorizationService extends Service {
    private static final String TAG = Authorizationlet.TAG + "/AAS";

    private Messenger mMessenger;
    private static ResultReceiver resultReceiver;
    private final Object mLock = new Object();

    /**
     * @hide for internal use
     */
    public static final class AuthorizationMessage {
        public static final int UserAuthorizationData = 100;
    }

    /**
     * @hide for internal use
     */
    @Override
    public IBinder onBind(Intent intent) {
        SLog.d(TAG, "onBind");
        if (TextUtils.equals(intent.getAction(), Authorizationlet.ACTION)) {
            this.resultReceiver = intent.getParcelableExtra("resultReceiver");
            return mMessenger.getBinder();
        }
        throw new UnsupportedOperationException("Unknown action");
    }

    /**
     * @hide for internal use
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SLog.d(TAG, "onStartCommand");
        this.resultReceiver = intent.getParcelableExtra("resultReceiver");
        return START_NOT_STICKY;
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
    }

    /**
     * @hide for internal use
     */
    private static class ServiceHandler extends Handler {
        private final WeakReference<AbstractAuthorizationService> mRef;

        ServiceHandler(Looper looper, AbstractAuthorizationService context) {
            super(looper);
            mRef = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            AbstractAuthorizationService serviceBase = mRef.get();
            if (serviceBase == null) {
                //fail requests
                SLog.i(TAG, "serviceBase is null");
                return;
            }
            switch (msg.what) {
                case AuthorizationMessage.UserAuthorizationData:
                    synchronized (serviceBase.mLock) {
                        Bundle data = (Bundle) msg.obj;
                        String userAuthorizationDataStr = data.getString(Authorizationlet.Keys.KEY_USER_AUTHORIZATION_DATA);
                        UserAuthorizationData userAuthorizationData = JsonParser.getInstance().fromJson(userAuthorizationDataStr, UserAuthorizationData.class);

                        //get result from 3rd app
                        UserAuthorizationResult userAuthorizationResult = serviceBase.authorizeUser(userAuthorizationData);
                        if (resultReceiver != null) {
                            Bundle resultData = new Bundle();
                            resultData.putString("response", JsonParser.getInstance().toJson(userAuthorizationResult));
                            resultReceiver.send(0, resultData);
                        }
                    }
                    break;
                default:
                    // return error
                    SLog.i(TAG, "msg (" + msg.what + ") error");
            }
        }
    }

    /**
     * Called to notify when user sign-in that authorization service request user authorization data for grant/denies.
     * <p>
     * This method processes the user authorization data received from the SDK and determines the user's access permissions.
     *
     * @param userAuthorizationData Signs in user authorization data
     * @return UserAuthorizationResult The result of the authorization process, including granted and denied permissions.
     * @since API 9
     */
    public abstract UserAuthorizationResult authorizeUser(UserAuthorizationData userAuthorizationData);
}
