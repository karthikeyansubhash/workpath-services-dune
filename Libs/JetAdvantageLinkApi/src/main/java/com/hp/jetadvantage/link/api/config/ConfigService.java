// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.config;

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

import org.json.JSONObject;

/**
 * <p>ConfigService provides interfaces to access an application configuration for retrieving or updating detail.
 * If an application is installed as hpk file on a Link device, client can access the application configuration data without launching it.</p>
 *
 * @since API 1
 */
@DeviceApi
public final class ConfigService {
    private static final String TAG = Configlet.TAG;

    private ConfigService() {
    }

    /**
     * <p>This method is needed to determine if the service supported or not.
     * If it's not supported, ConfigService operation will be failed.</p>
     *
     * @param context The Context in which the application is running.
     * @return boolean Returns result of supported. If service is supported, method returns "TRUE".
     * @throws NullPointerException Returns error if context is null.
     *
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    @SuppressLint("RestrictedApi")
    public static boolean isSupported(@NonNull final Context context) {
        Preconditions.checkNotNull(context, "Context must be provided");

        Bundle extras = new Bundle();
        extras.putInt(Configlet.Param.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

        final Bundle returnBundle = context.getContentResolver()
                .call(Configlet.CONTENT_URI,
                      Configlet.Method.IS_SUPPORTED,
                     null,
                     extras);
        return returnBundle != null
                && Result.parse(returnBundle, new Result()).getCode() == Result.RESULT_OK
                && returnBundle.containsKey(Configlet.IS_SUPPORTED_EXTRA) && returnBundle.getBoolean(Configlet.IS_SUPPORTED_EXTRA);
    }

    /**
     * <p>Returns configuration data that an application owns.</p>
     *
     * @param context The Context in which the application is running. If it's null, configuration will not be retrieved.
     * @param result  (optional) Indicates any errors which occurred while
     *                retrieving configuration.
     * @return JSONObject Configuration data of an application
     * @throws NullPointerException if context is null.
     *
     * @since API 1
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    @SuppressLint("RestrictedApi")
    @Nullable
    public static synchronized JSONObject getDefaultConfig(@NonNull final Context context,
                                                           @Nullable Result result) {
        Preconditions.checkNotNull(context, "Context must be provided");

        Bundle extras = new Bundle();
        String packageName = context.getPackageName();
        extras.putString(Configlet.Param.PACKAGE_NAME, packageName);
        extras.putInt(Configlet.Param.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

        if (result == null) {
            result = new Result();
        }

        try {

            final Bundle bundle =
                    context.getContentResolver().call(Configlet.CONTENT_URI, Configlet.Method.GET_CONFIG, null, extras);

            if (null == bundle) {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
            } else {
                parseResult(bundle, result);

                if (bundle.containsKey(Result.KEY_RESULT)) {
                    final String configJsonStr = bundle.getString(Result.KEY_RESULT);
                    SLog.d(TAG, "Config is ok.");
                    return new JSONObject(configJsonStr);
                }
            }
        } catch (Exception e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }

        return null;
    }

    /**
     * <p>Updates configuration data that an application owns.</p>
     *
     * @param context The Context in which the application is running. If it's null, configuration will not be updated.
     * @param config  The data for updating configuration of an application
     * @return Result The result of the update
     * @throws NullPointerException If context is null or configuration is not provided.
     *
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    @SuppressLint("RestrictedApi")
    public static synchronized Result setDefaultConfig(@NonNull final Context context, @NonNull final JSONObject config) {
        JetAdvantageLink.getInstance().checkPreconditions(context);
        Preconditions.checkNotNull(config, "Config data must be provided");
        Result result = new Result();
        Bundle extras = new Bundle();
        String packageName = context.getPackageName();
        extras.putString(Configlet.Param.PACKAGE_NAME, packageName);
        extras.putString(Configlet.Param.CONFIG_DATA, config.toString());

        final Bundle bundle =
                context.getContentResolver().call(Configlet.CONTENT_URI, Configlet.Method.SET_CONFIG, null, extras);

        if (null == bundle) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
        } else {
            parseResult(bundle, result);
        }
        return result;
    }

    /**
     * @hide internal used
     */
    private static void parseResult(Bundle bundle, Result result){
        int code = Result.RESULT_OK;
        Result.ErrorCode errorCode = null;
        String cause = null;
        // Error
        if (bundle.containsKey(Result.KEY_CODE)) {
            code = bundle.getInt(Result.KEY_CODE);
        }

        // ErrorCode
        if (bundle.containsKey(Result.KEY_ERROR_CODE)) {
            errorCode = (Result.ErrorCode) bundle.get(Result.KEY_ERROR_CODE);
        }

        // optional
        if (bundle.containsKey(Result.KEY_CAUSE)) {
            cause = bundle.getString(Result.KEY_CAUSE);
        }
        Result.pack(result, code, errorCode, cause);
    }

    /**
     * <p>Provides a mechanism to monitor the state of the
     * {@link ConfigService ConfigService} operations. If application extends AbstractConfigChangeObserver, it can receive
     * events when there have been changes the state of the config.</p>
     *
     * @since API 1
     */
    @DeviceApi
    public static abstract class AbstractConfigChangeObserver extends BroadcastReceiver {
        private final Handler mHandler;

        /**
         * <p>Constructor</p>
         * @param handler The handler to run callbacks on, or null if none.
         * @since API 1
         */
        public AbstractConfigChangeObserver(final Handler handler) {
            super();
            mHandler = handler;
        }

        /**
         * <p>Requests to register the observer to monitor events of the change of configuration.</p>
         *
         * @param context The Context in which the application is running. If it's null, event will not be triggered.
         * @throws NullPointerException If context is null.
         * @since API 1
         */
        @SuppressWarnings({"unused"})
        @SuppressLint("RestrictedApi")
        public void register(@NonNull final Context context) {
            Preconditions.checkNotNull(context, "Context must be provided");
            SLog.d(TAG, "Registering for config changed event");

            final IntentFilter filter = new IntentFilter(Configlet.CONFIG_CHANGE_ACTION);
            context.registerReceiver(this, filter);
        }

        /**
         * <p>Requests to unregister the observer to stop monitoring events of the change of configuration.</p>
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
            SLog.d(TAG, "Un-registering for config changed event");
            context.unregisterReceiver(this);
        }

        /**
         * @hide final
         */
        @Override
        public final void onReceive(final Context context, final Intent intent) {
            SLog.d(TAG, "Received intent for " + intent.getAction());
            final String action = intent.getAction();
            if (!Configlet.CONFIG_CHANGE_ACTION.equals(action)) {
                SLog.e(TAG, "Received invalid intent");
                return;
            }

            Result result = new Result();
            JSONObject jsonObject = getDefaultConfig(context, result);

            if (result.getCode() == Result.RESULT_OK) {
                if (mHandler == null) {
                    onRecv(jsonObject);
                } else {
                    mHandler.post(new ConfigStateRunnable(jsonObject));
                }
            }
        }

        private void onRecv(JSONObject jsonObject) {
            onChange(jsonObject);
        }

        private final class ConfigStateRunnable implements Runnable {
            private JSONObject jsonObject;

            ConfigStateRunnable(JSONObject jsonObject) {
                this.jsonObject = jsonObject;
            }

            @Override
            public void run() {
                AbstractConfigChangeObserver.this.onRecv(jsonObject);
            }
        }

        /**
         * <p>Called to notify the client that the configuration data associated with application is changed.</p>
         *
         * @param updatedData Updated configuration with JSON type
         * @since API 1
         */
        public abstract void onChange(JSONObject updatedData);
    }
}
