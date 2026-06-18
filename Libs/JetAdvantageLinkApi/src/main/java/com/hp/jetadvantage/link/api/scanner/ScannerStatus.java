// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.scanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

import com.hp.jetadvantage.link.api.JetAdvantageLink;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.utils.JsonParser;
import com.hp.jetadvantage.link.common.utils.SLog;

/**
 * ScannerStatus provides an interface to retrieve scanner status details for scanning
 *
 * @since API 1
 */
public class ScannerStatus {

    private ScannerStatus() {
    }

    /**
     * Returns the current status of scanner.
     *
     * @param context The Context object for your activity or application.
     * @param result  (optional) Indicates any errors which occurred while retrieving status.
     * @return StatusInfo containing status
     * @throws NullPointerException if context is null.
     *
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    public static synchronized StatusInfo getStatus(@NonNull final Context context, @Nullable Result result) {
        JetAdvantageLink.getInstance().checkPreconditions(context);

        if (result == null) {
            result = new Result();
        }

        try {
            final ContentResolver resolver = context.getContentResolver();
            Bundle extras = new Bundle();
            extras.putInt(Scanlet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);
            final Bundle bundle = resolver.call(Scanlet.getContentUri(resolver),
                    Scanlet.Method.GET_STATUS, null, extras);

            Result.parse(bundle, result);

            if (result.getCode() == Result.RESULT_OK) {
                final String jsonStr = bundle.getString(Result.KEY_RESULT);
                return JsonParser.getInstance().fromJson(jsonStr, StatusInfo.class);
            }
        } catch (SecurityException se){
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, se.getMessage());
            throw se;
        } catch (Exception e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.UNKNOWN, e.getMessage());
        }
        return null;
    }

    /**
     * <p>This method is needed to determine if the service supported or not.
     * If it's not supported, ScannerStatus operation will be failed.</p>
     *
     * @param context The Context in which the application is running.
     * @return boolean Returns result of supported. If service is supported, method returns "TRUE".
     * @throws NullPointerException Returns error if context is null.
     *
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    public static boolean isSupported(@NonNull final Context context) {
        JetAdvantageLink.getInstance().checkPreconditions(context);

        Bundle extras = new Bundle();
        extras.putInt(Scanlet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

        try {
            final ContentResolver resolver = context.getContentResolver();
            final Bundle returnBundle = resolver
                    .call(Scanlet.getContentUri(resolver),
                            Scanlet.Method.IS_SUPPORTED,
                            null,
                            extras);

            return returnBundle != null
                    && Result.parse(returnBundle, new Result()).getCode() == Result.RESULT_OK
                    && returnBundle.containsKey(Scanlet.IS_SUPPORTED_EXTRA) && returnBundle.getBoolean(Scanlet.IS_SUPPORTED_EXTRA);
        } catch (Exception e) {
            SLog.e(Scanlet.TAG, "Failed to call Link Services: " + e.getMessage());
        }

        return false;
    }
}
