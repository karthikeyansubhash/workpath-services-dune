// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.attestation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.content.Context;
import android.os.Bundle;

import com.hp.jetadvantage.link.api.JetAdvantageLink;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.annotation.DeviceApi;
import com.hp.jetadvantage.link.common.utils.JsonParser;

/**
 * <p>AttestationService provides interfaces for communicating with HP attestation server.</p>
 *
 * @since API 3
 */
@DeviceApi
public final class AttestationService {

    private AttestationService() {
    }

    /**
     * <p>Returns application token.</p>
     *
     * @param context The Context in which the application is running. If it's null, a client can not get application token.
     * @param result  (optional) Indicates reason when errors occurred.
     * @return {@link AppToken AppToken} Return application token.
     * @throws NullPointerException Returns error if required parameters such as context are empty or null.
     *
     * @since API 3
     */
    @Nullable
    public static AppToken getAppToken(@NonNull final Context context, @Nullable Result result) {
        JetAdvantageLink.getInstance().checkPreconditions(context);

        if (result == null) {
            result = new Result();
        }

        try {
            Bundle extras = new Bundle();
            String packageName = context.getPackageName();
            extras.putString(Attestationlet.Keys.PACKAGE_NAME, packageName);
            extras.putInt(Attestationlet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

            final Bundle returnBundle = context.getContentResolver()
                    .call(Attestationlet.CONTENT_URI,
                            Attestationlet.Method.GET_TOKEN,
                            null,
                            extras);

            if (null == returnBundle) {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
            } else {
                // returnBundle.setClassLoader(AppToken.class.getClassLoader()); //TODO 190315 Needs to decide the management of the version policy for AppToken
                Result.parse(returnBundle, result);

                if (result.getCode() == Result.RESULT_OK && returnBundle.containsKey(Result.KEY_RESULT)) {
                    return JsonParser.getInstance().fromJson(returnBundle.getString(Result.KEY_RESULT), AppToken.class);
                } else {
                    Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "Failed to get application token: " + result.getCause());
                }
            }
        } catch (Exception e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }
        return null;
    }

    /**
     * <p>This method is needed to determine if the service supported or not.
     * If it's not supported, AttestationService operation will be failed.</p>
     *
     * @param context The Context in which the application is running.
     * @return boolean Returns result of supported. If service is supported, method returns "TRUE".
     * @throws NullPointerException Returns error if context is null.
     * @since API 3
     */
    @SuppressWarnings({"unused"})
    public static boolean isSupported(@NonNull final Context context) {
        JetAdvantageLink.getInstance().checkPreconditions(context);

        Bundle extras = new Bundle();
        extras.putInt(Attestationlet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

        final Bundle returnBundle = context.getContentResolver()
                .call(Attestationlet.CONTENT_URI,
                        Attestationlet.Method.IS_SUPPORTED,
                        null,
                        extras);
        return returnBundle != null
                && Result.parse(returnBundle, new Result()).getCode() == Result.RESULT_OK
                && returnBundle.containsKey(Attestationlet.ATT_SUPPORT_EXTRA) && returnBundle.getBoolean(Attestationlet.ATT_SUPPORT_EXTRA);
    }

}
