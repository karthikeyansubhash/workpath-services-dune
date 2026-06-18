// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.printer;

import static com.hp.jetadvantage.link.api.Result.RESULT_OK;

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

import java.util.Arrays;
import java.util.List;

/**
 * PrinterStatus provides an interface to retrieve device status details
 *
 * @since API 1
 */
public class PrinterStatus {
    private static final String TAG = Printlet.TAG;

    private PrinterStatus() {
    }

    /**
     * Returns the device status.
     *
     * @param context The Context object for your activity or application.
     * @param result  (optional) Indicates any errors which occurred
     * @return StatusInfo containing status details
     * @throws NullPointerException if context is null
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
            Bundle extras = new Bundle();
            extras.putInt(Printlet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

            final ContentResolver resolver = context.getContentResolver();
            final Bundle bundle = resolver.call(Printlet.getContentUri(resolver),
                    Printlet.Method.GET_STATUS, null, extras);

            if (bundle != null) {
                Result.parse(bundle, result);

                if (result.getCode() == RESULT_OK) {
                    final String jsonStr = bundle.getString(Result.KEY_RESULT);
                    return JsonParser.getInstance().fromJson(jsonStr, StatusInfo.class);
                }
            } else {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SYSTEM_ERROR, "Received empty response");
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
     * Returns the printer trays information.
     *
     * @param context The Context object for your activity or application.
     * @param result  (optional) Indicates any errors which occurred
     * @return List containing tray details
     * @throws NullPointerException if context is null
     *
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    public static synchronized List<TrayInfo> getTrays(@NonNull final Context context, @Nullable Result result) {
        JetAdvantageLink.getInstance().checkPreconditions(context);

        if (result == null) {
            result = new Result();
        }

        try {
            Bundle extras = new Bundle();
            extras.putInt(Printlet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

            final ContentResolver resolver = context.getContentResolver();
            final Bundle bundle = resolver.call(Printlet.getContentUri(resolver),
                    Printlet.Method.GET_TRAY_INFO, null, extras);

            if (bundle != null) {
                Result.parse(bundle, result);

                if (result.getCode() == RESULT_OK) {
                    final String jsonStr = bundle.getString(Result.KEY_RESULT);
                    TrayInfo[] trayArray = JsonParser.getInstance().fromJson(jsonStr, TrayInfo[].class);
                    return Arrays.asList(trayArray);
                }
            } else {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SYSTEM_ERROR, "Received empty response");
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
     * If it's not supported, PrinterStatus operation will be failed.</p>
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
        extras.putInt(Printlet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

        try {
            final ContentResolver resolver = context.getContentResolver();
            final Bundle returnBundle = resolver
                    .call(Printlet.getContentUri(resolver),
                            Printlet.Method.IS_STATUS_SUPPORTED,
                            null,
                            extras);

            return returnBundle != null
                    && Result.parse(returnBundle, new Result()).getCode() == Result.RESULT_OK
                    && returnBundle.containsKey(Printlet.IS_SUPPORTED_EXTRA) && returnBundle.getBoolean(
                    Printlet.IS_SUPPORTED_EXTRA);
        } catch (Exception e) {
            SLog.e(TAG, "Failed to call Link Services: " + e.getMessage());
        }

        return false;
    }
}
