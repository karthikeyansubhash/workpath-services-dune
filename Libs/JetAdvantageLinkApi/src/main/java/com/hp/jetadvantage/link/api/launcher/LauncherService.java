// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.launcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.api.JetAdvantageLink;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.utils.SLog;

/**
 * Provides interfaces to launch an app with actions.
 *
 * @since API 2
 */
@SuppressWarnings("unused")
public class LauncherService {
    private LauncherService() {}

    /**
     * Requests to launch an application specified with launcher action
     * @param context The Context in which the application is running.
     * @param action The action name for launching.
     * @param result (optional) Indicates any errors which occurred while operation
     * @throws NullPointerException if context or action is null
     *
     * @since API 2
     */
    @SuppressWarnings("unused")
    public static void launch(@NonNull final Context context, @NonNull final LaunchAction action, @Nullable Result result) {
        JetAdvantageLink.getInstance().checkPreconditions(context);

        Bundle extras = new Bundle();
        String packageName = context.getPackageName();
        extras.putString(Launcherlet.Keys.PACKAGE_NAME, packageName);
        extras.putInt(Launcherlet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);
        extras.putSerializable(Launcherlet.Keys.KEY_LAUNCH_ACTION, action);

        if (result == null) {
            result = new Result();
        }
        try {
            final Bundle bundle =
                    context.getContentResolver().call(Launcherlet.CONTENT_URI, Launcherlet.Method.LAUNCH, null, extras);

            if (null == bundle) {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
            } else {
                Result.parse(bundle, result);
            }
        } catch (Exception e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }
    }

    /**
     * Requests to launch an application specified with uuid
     * @param context The Context in which the application is running.
     * @param uuid main application id or sub application id
     * @param result (optional) Indicates any errors which occurred while operation
     * @throws NullPointerException if context or uuid is null
     *
     * @since API 3
     */
    @SuppressWarnings("unused")
    @SuppressLint("RestrictedApi")
    public static void launch(@NonNull final Context context, @NonNull final String uuid, @Nullable Result result) {
        JetAdvantageLink.getInstance().checkPreconditions(context);
        Preconditions.checkNotNull(uuid, "Uuid must be provided");

        Bundle extras = new Bundle();
        String packageName = context.getPackageName();
        extras.putString(Launcherlet.Keys.PACKAGE_NAME, packageName);
        extras.putInt(Launcherlet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);
        extras.putString(Launcherlet.Keys.KEY_APPLICATION_UUID, uuid);

        if (result == null) {
            result = new Result();
        }
        try {
            final Bundle bundle =
                    context.getContentResolver().call(Launcherlet.CONTENT_URI, Launcherlet.Method.APPLICATION, null, extras);

            if (null == bundle) {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
            } else {
                Result.parse(bundle, result);
            }
        } catch (Exception e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }
    }

    /**
     * <p>This method is needed to determine if the service supported or not.
     * If it's not supported, LauncherService operations will be failed.</p>
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
        extras.putInt(Launcherlet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

        try {
            final Bundle returnBundle = context.getContentResolver()
                    .call(Launcherlet.CONTENT_URI,
                            Launcherlet.Method.IS_SUPPORTED,
                            null,
                            extras);
            return returnBundle != null
                    && Result.parse(returnBundle, new Result()).getCode() == Result.RESULT_OK
                    && returnBundle.containsKey(Launcherlet.IS_SUPPORTED_EXTRA) && returnBundle.getBoolean(Launcherlet.IS_SUPPORTED_EXTRA);
        } catch (Exception e) {
            SLog.e(Launcherlet.TAG, "Failed to call service", e);
        }

        return false;
    }

}
