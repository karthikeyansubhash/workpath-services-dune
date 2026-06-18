// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.access;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.annotation.DeviceApi;
import com.hp.jetadvantage.link.common.utils.JsonParser;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.JetAdvantageLink;

/**
 * <p>AccessService provides interfaces for getting user information on a device.</p>
 *
 * @since API 1
 */
@DeviceApi
public final class AccessService {

    private AccessService() {
    }

    /**
     * <p>Returns current authenticated user details.</p>
     * <p>User details include authority such as admin or not.
     * If device is not authenticated, guest credentials will be returned.</p>
     *
     * @param context The Context in which the application is running. If it's null, a client can not retrieve user details.
     * @param result  (optional) Indicates reason when errors occurred.
     * @return {@link Principal Principal} Return current user information
     * @throws NullPointerException Returns error if required parameters such as context are empty or null.
     *
     * @since API 1
     */
    @Nullable
    @SuppressWarnings({"unused"})
    public static Principal getCurrentPrincipal(@NonNull final Context context,
                                                @Nullable Result result) {
        JetAdvantageLink.getInstance().checkPreconditions(context);

        if (result == null) {
            result = new Result();
        }

        try {
            Bundle extras = new Bundle();
            extras.putInt(Accesslet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

            final Bundle returnBundle = context.getContentResolver()
                    .call(Accesslet.CONTENT_URI,
                            Accesslet.Method.GET_PROPERTIES,
                            null,
                            extras);

            Result.parse(returnBundle, result);

            if (result.getCode() == Result.RESULT_OK) {
                String principalInString = returnBundle.getString(Accesslet.PRINCIPAL_EXTRA);
                return JsonParser.getInstance().fromJson(principalInString, Principal.class);
            }
        } catch (Exception e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }
        return null;
    }

    /**
     * <p>Requests sign in with {@link SignInAction SignInAction} options. Each option will be:
     * <ul>
     *     <li>"success" will request sign in with authenticationAttributes. In this case, authenticationAttributes should be provided.</li>
     *     <li>"fail" will finish sign in with failure message.</li>
     *     <li>"cancel" will cancel sign in.</li>
     *     <li>"continue" will request to launch an activity which is defined as Prompt.
     *     This option can be called in {@link AbstractAuthenticationService AbstractAuthenticationService} onPrePrompt() method.</li>
     * </ul>
     * </p>
     *
     * @param context The Context in which the application is running. If it's null, sign in will be failed.
     * @param signInAction Sign-in option
     * @param authenticationAttributes (optional) The user details for requesting sign in with success.
     * @param result  (optional) Indicates reason when errors occurred.
     * @throws NullPointerException Returns error if context is null.
     *
     * @since API 2
     */
    @SuppressWarnings({"unused"})
    public static void signIn(@NonNull final Context context, @NonNull SignInAction signInAction,
            @Nullable AuthenticationAttributes authenticationAttributes, @Nullable Result result) {
        JetAdvantageLink.getInstance().checkPreconditions(context);

        if (result == null) {
            result = new Result();
        }

        try {
            Bundle extras = new Bundle();
            extras.putInt(Accesslet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);
            extras.putParcelable(Accesslet.Keys.KEY_ACTION, signInAction);
            extras.setClassLoader(AuthenticationAttributes.class.getClassLoader());
            extras.putParcelable(Accesslet.Keys.KEY_AUTHENTICATION, authenticationAttributes);

            final Bundle returnBundle = context.getContentResolver()
                    .call(Accesslet.CONTENT_URI,
                            Accesslet.Method.SIGN_IN,
                            null,
                            extras);

            Result.parse(returnBundle, result);
        } catch (Exception e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }
    }

    /**
     * <p>Requests sign out on a device. If sign out is finished completely, user credentials will be removed.</p>
     *
     * @param context The Context in which the application is running. If it's null, sign out will be failed.
     * @param result  (optional) Indicates reason when errors occurred.
     * @throws NullPointerException Returns error if context is null.
     *
     * @since API 2
     */
    @SuppressWarnings({"unused"})
    public static void signOut(@NonNull final Context context, @Nullable Result result) {
        JetAdvantageLink.getInstance().checkPreconditions(context);

        if (result == null) {
            result = new Result();
        }

        try {
            Bundle extras = new Bundle();
            extras.putInt(Accesslet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

            final Bundle returnBundle = context.getContentResolver()
                    .call(Accesslet.CONTENT_URI,
                            Accesslet.Method.SIGN_OUT,
                            null,
                            extras);
            Result.parse(returnBundle, result);
        } catch (Exception e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }
    }

    /**
     * <p>Starts authentication process without a selection of Link Authentication agent on panel.</p>
     *
     * @param context The Context in which the application is running. If it's null, authentication process will not be started.
     * @param result  (optional) Indicates reason when errors occurred.
     * @throws NullPointerException Returns error if context is null.
     *
     * @since API 2
     */
    @SuppressWarnings({"unused"})
    public static void initiateSignIn(@NonNull final Context context, @Nullable Result result) {
        JetAdvantageLink.getInstance().checkPreconditions(context);

        if (result == null) {
            result = new Result();
        }

        try {
            Bundle extras = new Bundle();
            extras.putInt(Accesslet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

            final Bundle returnBundle = context.getContentResolver()
                    .call(Accesslet.CONTENT_URI,
                            Accesslet.Method.START_SIGN_IN_PROCESS,
                            null,
                            extras);
            Result.parse(returnBundle, result);
        } catch (Exception e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }
    }

    /**
     * <p>This method is needed to determine if the service supported or not.
     * If it's not supported, AccessService operation will be failed.</p>
     *
     * @param context The Context in which the application is running.
     * @return boolean Returns result of supported. If service is supported, method returns "TRUE".
     * @throws NullPointerException Returns error if context is null.
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    public static boolean isSupported(@NonNull final Context context) {
        JetAdvantageLink.getInstance().checkPreconditions(context);

        Bundle extras = new Bundle();
        extras.putInt(Accesslet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

        final Bundle returnBundle = context.getContentResolver()
                .call(Accesslet.CONTENT_URI,
                        Accesslet.Method.IS_SUPPORTED,
                        null,
                        extras);
        return returnBundle != null
                && Result.parse(returnBundle, new Result()).getCode() == Result.RESULT_OK
                && returnBundle.containsKey(Accesslet.AA_SUPPORT_EXTRA) && returnBundle.getBoolean(Accesslet.AA_SUPPORT_EXTRA);
    }

    /**
     * <p>Returns device token for SIO when hp user is signed in.</p>
     * <p>If device is not authenticated, device token will be not valid value.</p>
     *
     * @param context The Context in which the application is running. If it's null, api doesn't return valid token.
     * @param clientId clientId for SIO
     * @param result  (optional) Indicates reason when errors occurred.
     * @return {@link Principal Principal} Return device token for SIO
     * @throws NullPointerException Returns error if required parameters such as context are empty or null.
     *
     * @since API 3
     */
    @Nullable
    @SuppressWarnings({"unused"})
    public static DeviceToken getDeviceAccessToken(@NonNull final Context context, @Nullable String clientId, @Nullable Result result) {
        JetAdvantageLink.getInstance().checkPreconditions(context);

        if (result == null) {
            result = new Result();
        }

        try {
            if(TextUtils.isEmpty(clientId)) {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.INVALID_PARAM, "Invalid client id");
            } else {
                Bundle extras = new Bundle();
                String packageName = context.getPackageName();
                extras.putString(Accesslet.Keys.PACKAGE_NAME, packageName);
                extras.putInt(Accesslet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);
                extras.putString(Accesslet.Keys.KEY_CLIENT_ID, clientId);

                final Bundle returnBundle = context.getContentResolver()
                        .call(Accesslet.CONTENT_URI,
                                Accesslet.Method.GET_TOKEN,
                                null,
                                extras);

                if (null == returnBundle) {
                    Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
                } else {
                    //returnBundle.setClassLoader(DeviceToken.class.getClassLoader()); //TODO 190315 Needs to decide the management of the version policy for DeviceToken
                    Result.parse(returnBundle, result);

                    if (result.getCode() == Result.RESULT_OK && returnBundle.containsKey(Result.KEY_RESULT)) {
                        return JsonParser.getInstance().fromJson(returnBundle.getString(Result.KEY_RESULT), DeviceToken.class);
                    } else {
                        Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "Failed to get device token: " + result.getCause());
                    }
                }
            }
        } catch (Exception e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }
        return null;
    }

}
