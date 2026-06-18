// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.massstorage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;

import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.api.JetAdvantageLink;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.annotation.DeviceApi;
import com.hp.jetadvantage.link.common.utils.SLog;

import java.util.List;

/**
 * Provides interfaces to access mass storage.
 *
 * @since API 2
 */
@SuppressWarnings("unused")
@DeviceApi
public final class MassStorageService {
    private static final String TAG = "[MassSVC]";

    private MassStorageService() {}

    /**
     * Returns list of supported mass storage
     *
     * @param context The Context in which the application is running.
     * @param result (optional) Indicates any errors which occurred while operation
     * @return List list of {@link MassStorageInfo MassStorageInfo} which is retrieved from a device
     * @throws NullPointerException if context is null
     *
     * @since API 2
     */
    @SuppressWarnings("unused")
    public static List<MassStorageInfo> getStorageList(@NonNull final Context context,
                                                       @Nullable Result result) {
        JetAdvantageLink.getInstance().checkPreconditions(context);

        Bundle extras = new Bundle();
        String packageName = context.getPackageName();
        extras.putString(MassStoragelet.Keys.PACKAGE_NAME, packageName);
        extras.putInt(MassStoragelet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

        if (result == null) {
            result = new Result();
        }
        try {
            final Bundle bundle =
                    context.getContentResolver().call(MassStoragelet.CONTENT_URI, MassStoragelet.Method.GET_STORAGE_LIST, null, extras);

            if (null == bundle) {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
            } else {
                bundle.setClassLoader(MassStorageInfo.class.getClassLoader());
                Result.parse(bundle, result);

                if (result.getCode() == Result.RESULT_OK && bundle.containsKey(Result.KEY_RESULT)) {
                    return bundle.getParcelableArrayList(Result.KEY_RESULT);
                }
            }
        } catch (Exception e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }

        return null;
    }

    /**
     * <p>This method is needed to determine if the service supported or not.
     * If it's not supported, operations will be failed.</p>
     *
     * @param context The Context in which the application is running.
     * @return boolean Returns result of supported. If service is supported, method returns "TRUE".
     * @throws NullPointerException Returns error if context is null.
     * @since API 2
     */
    @SuppressWarnings({"unused"})
    @SuppressLint("RestrictedApi")
    public static boolean isSupported(@NonNull final Context context) {
        Preconditions.checkNotNull(context, "Context must be provided");

        Bundle extras = new Bundle();
        extras.putInt(MassStoragelet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

        final Bundle returnBundle = context.getContentResolver()
                .call(MassStoragelet.CONTENT_URI,
                        MassStoragelet.Method.IS_SUPPORTED,
                        null,
                        extras);
        return returnBundle != null
                && Result.parse(returnBundle, new Result()).getCode() == Result.RESULT_OK
                && returnBundle.containsKey(MassStoragelet.IS_SUPPORTED_EXTRA) && returnBundle.getBoolean(MassStoragelet.IS_SUPPORTED_EXTRA);
    }

    /**
     * <p>Provides a mechanism to monitor the state of the
     * {@link MassStorageService MassStorageService} events. If application extends AbstractMassStorageChangeObserver, it can receive
     * events when massstorage is attached or detached.</p>
     *
     * @since API 3
     */
    @DeviceApi
    public static abstract class AbstractMassStorageChangeObserver extends BroadcastReceiver {
        private final Handler mHandler;

        /**
         * <p>Constructor</p>
         * @param handler The handler to run callbacks on, or null if none.
         * @since API 3
         */
        public AbstractMassStorageChangeObserver(final Handler handler) {
            super();
            mHandler = handler;
        }

        /**
         * <p>Requests to register the observer to monitor events of the change of massstorage status.</p>
         *
         * @param context The Context in which the application is running. If it's null, event will not be triggered.
         * @throws NullPointerException If context is null.
         * @since API 3
         */
        @SuppressWarnings({"unused"})
        @SuppressLint("RestrictedApi")
        public void register(@NonNull final Context context) {
            Preconditions.checkNotNull(context, "Context must be provided");
            SLog.d(TAG, "Registering for massstorage changed event");

            final IntentFilter filter = new IntentFilter(MassStoragelet.MASSSTORAGE_CHANGE_ACTION);
            context.registerReceiver(this, filter);
        }

        /**
         * <p>Requests to unregister the observer to stop monitoring events of the change of massstorage status.</p>
         *
         * @param context The Context in which the application is running.
         * @throws NullPointerException If context is null.
         * @throws IllegalArgumentException if this receiver hasn't been registered.
         * @since API 1
         */
        @SuppressWarnings({"unused"})
        @SuppressLint("RestrictedApi")
        public void unregister(@NonNull final Context context) {
            Preconditions.checkNotNull(context, "Context must be provided");
            SLog.d(TAG, "Un-registering for massstorage changed event");
            context.unregisterReceiver(this);
        }

        /**
         * @hide final
         */
        @Override
        public final void onReceive(final Context context, final Intent intent) {
            SLog.d(TAG, "Received intent for " + intent.getAction());
            final String action = intent.getAction();
            if (!MassStoragelet.MASSSTORAGE_CHANGE_ACTION.equals(action)) {
                SLog.e(TAG, "Received invalid intent");
                return;
            }
            if (mHandler == null) {
                onRecv();
            } else {
                mHandler.post(new MassStorageStateRunnable());
            }
        }

        private void onRecv() {
            onChange();
        }

        private final class MassStorageStateRunnable implements Runnable {
            MassStorageStateRunnable() {
            }

            @Override
            public void run() {
                AbstractMassStorageChangeObserver.this.onRecv();
            }
        }

        /**
         * <p>Called to notify the client that massstorage state is changed.</p>
         *
         * @since API 3
         */
        public abstract void onChange();
    }
}
