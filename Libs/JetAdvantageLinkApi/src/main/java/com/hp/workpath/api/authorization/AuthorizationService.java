// Copyright 2025 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.authorization;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hp.jetadvantage.link.api.JetAdvantageLink;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.common.Sdk;

import java.util.ArrayList;

/**
 * The AuthorizationService class provides methods to interact with the SDK.
 * This includes checking support, setting configurations, and retrieving permissions and sign-in methods.
 *
 * @since API 9
 */
public class AuthorizationService {
    private final static String TAG = "[sdklib][authz]";

    private AuthorizationService() {
    }

    /**
     * Checks if the authorization service is supported on the device.
     *
     * @param context The application context.
     * @return True if the service is supported, false otherwise.
     * @since API 9
     */
    public static boolean isSupported(@NonNull final Context context) {
        JetAdvantageLink.getInstance().checkPreconditions(context);

        Bundle extras = new Bundle();
        String packageName = context.getPackageName();
        extras.putInt(Authorizationlet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);
        extras.putString(Authorizationlet.Keys.PACKAGE_NAME, packageName);

        final Bundle returnBundle = context.getContentResolver()
                .call(Authorizationlet.CONTENT_URI,
                        Authorizationlet.Method.IS_SUPPORTED,
                        null,
                        extras);
        return returnBundle != null
                && Result.parse(returnBundle, new Result()).getCode() == Result.RESULT_OK
                && returnBundle.containsKey(Authorizationlet.Keys.KEY_IS_SUPPORT) && returnBundle.getBoolean(Authorizationlet.Keys.KEY_IS_SUPPORT);
    }

    /**
     * Sets the proxy configuration for the authorization service.
     * This method configures the authorization service with a new proxy configuration {@link ProxyConfiguration}, which includes settings for guest permissions, sign-in methods, and other parameters.
     *
     * @param context The application context.
     * @param result  (optional) Indicates any errors occurred while registering.
     * @param pc      The proxy configuration to set.
     * @since API 9
     */
    public static void setConfiguration(@NonNull Context context, @Nullable Result result, @Nullable ProxyConfiguration pc) {
        JetAdvantageLink.getInstance().checkPreconditions(context);

        Bundle extras = new Bundle();
        String packageName = context.getPackageName();
        extras.putString(Authorizationlet.Keys.PACKAGE_NAME, packageName);
        extras.putInt(Authorizationlet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);
        extras.putParcelable(Authorizationlet.Keys.KEY_PROXY_CONFIGURATION, pc);

        if (result == null) {
            result = new Result();
        }
        try {
            final Bundle bundle
                    = context.getContentResolver().call(Authorizationlet.CONTENT_URI, Authorizationlet.Method.SET_CONFIGURATION, null, extras);

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
     * Returns a list of all permissions provided to the device.
     *
     * @param context The application context.
     * @param result  (optional) Indicates any errors occurred while registering.
     * @return A list of permissions available on the device.
     * @since API 9
     */
    public static ArrayList<Permission> getPermissions(@NonNull Context context, @Nullable Result result) {
        JetAdvantageLink.getInstance().checkPreconditions(context);
        Bundle extras = new Bundle();
        String packageName = context.getPackageName();
        extras.putString(Authorizationlet.Keys.PACKAGE_NAME, packageName);
        extras.putInt(Authorizationlet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

        if (result == null) {
            result = new Result();
        }
        try {
            final Bundle bundle =
                    context.getContentResolver().call(Authorizationlet.CONTENT_URI, Authorizationlet.Method.GET_PERMISSIONS, null, extras);

            if (null == bundle) {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
            } else {
                bundle.setClassLoader(Permission.class.getClassLoader());
                Result.parse(bundle, result);

                if (bundle.containsKey(Result.KEY_RESULT)) {
                    return bundle.getParcelableArrayList(Result.KEY_RESULT);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }
        return null;
    }

    /**
     * Returns a list of currently existing sign-in methods.
     *
     * @param context      The application context.
     * @param result       (optional) Indicates any errors occurred while registering.
     * @param languageCode The language code for localization.
     * @return A list of sign-in methods available on the device.
     * @since API 9
     */
    public static ArrayList<SignInMethod> getSignInMethod(@NonNull Context context, @Nullable Result result, @NonNull String languageCode) {
        JetAdvantageLink.getInstance().checkPreconditions(context);
        Bundle extras = new Bundle();
        String packageName = context.getPackageName();
        extras.putString(Authorizationlet.Keys.PACKAGE_NAME, packageName);
        extras.putInt(Authorizationlet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);
        extras.putString(Authorizationlet.Keys.KEY_LANGUAGE_CODE, languageCode);

        if (result == null) {
            result = new Result();
        }
        try {
            final Bundle bundle =
                    context.getContentResolver().call(Authorizationlet.CONTENT_URI, Authorizationlet.Method.GET_SIGNINMETHODS, null, extras);

            if (null == bundle) {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
            } else {
                bundle.setClassLoader(SignInMethod.class.getClassLoader());
                Result.parse(bundle, result);
                if (bundle.containsKey(Result.KEY_RESULT)) {
                    return bundle.getParcelableArrayList(Result.KEY_RESULT);
                }
            }
        } catch (Exception e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }
        return null;
    }

    /**
     * Returns the current proxy configuration for the authorization service.
     *
     * @param context The application context.
     * @param result  (optional) Indicates any errors occurred while registering.
     * @return The current proxy configuration.
     * @since API 9
     */
    public static ProxyConfiguration getConfiguration(@NonNull Context context, @Nullable Result result) {
        JetAdvantageLink.getInstance().checkPreconditions(context);
        Bundle extras = new Bundle();
        String packageName = context.getPackageName();
        extras.putString(Authorizationlet.Keys.PACKAGE_NAME, packageName);
        extras.putInt(Authorizationlet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

        if (result == null) {
            result = new Result();
        }
        try {
            final Bundle bundle =
                    context.getContentResolver().call(Authorizationlet.CONTENT_URI, Authorizationlet.Method.GET_CONFIGURATION, null, extras);

            if (null == bundle) {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
            } else {
                bundle.setClassLoader(ProxyConfiguration.class.getClassLoader());
                Result.parse(bundle, result);
                if (bundle.containsKey(Result.KEY_RESULT)) {
                    return bundle.getParcelable(Result.KEY_RESULT);
                }
            }
        } catch (Exception e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }
        return null;
    }

}
