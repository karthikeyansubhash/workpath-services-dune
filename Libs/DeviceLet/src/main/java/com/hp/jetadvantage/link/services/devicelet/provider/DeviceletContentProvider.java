// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.devicelet.provider;

import static com.hp.jetadvantage.link.common.constants.ErrorMessages.Connection.DEVICE_NOT_CONNECTED;
import static com.hp.jetadvantage.link.common.constants.ErrorMessages.InvalidParam.EMPTY_ATTRIBUTE_KEY;
import static com.hp.jetadvantage.link.common.constants.ErrorMessages.InvalidParam.INVALID_METHOD;
import static com.hp.jetadvantage.link.common.constants.ErrorMessages.NotSupported.API_TYPE_NOT_SUPPORTED;
import static com.hp.jetadvantage.link.common.constants.ErrorMessages.NotSupported.CLIENT_VERSION_NOT_SUPPORTED;
import static com.hp.jetadvantage.link.common.constants.ErrorMessages.NotSupported.DEVICE_SERVICE_NOT_SUPPORTED;
import static com.hp.jetadvantage.link.common.constants.ErrorMessages.ServiceError.DEVICE_SERVICE_GET_ATTRIBUTE_FAILURE;
import static com.hp.jetadvantage.link.common.constants.LogConstants.TAG_DEVICE_SERVICE;
import static com.hp.jetadvantage.link.services.devicelet.provider.Key.CODE_ATTRIBUTE;
import static com.hp.jetadvantage.link.services.devicelet.provider.Key.CONTENT_TYPE_ATTRIBUTE;
import static com.hp.jetadvantage.link.services.devicelet.provider.Key.RESULT_COLUMN_NAMES;
import static com.hp.jetadvantage.link.services.devicelet.provider.Key.sUriMatcher;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.device.DeviceAttribute;
import com.hp.jetadvantage.link.api.device.Devicelet;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.helper.SelectedPrinterHelper;
import com.hp.jetadvantage.link.common.helper.ThreadPolicyManager;
import com.hp.jetadvantage.link.common.model.ApiType;
import com.hp.jetadvantage.link.common.model.PrinterInfo;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.exceptions.BoundDeviceException;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceInfoService;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceInfoService;
import com.hp.jetadvantage.link.services.common.exception.SdkConnectionErrorException;
import com.hp.jetadvantage.link.services.common.exception.SdkException;
import com.hp.jetadvantage.link.services.common.exception.SdkInvalidParamException;
import com.hp.jetadvantage.link.services.common.exception.SdkNotSupportedException;
import com.hp.jetadvantage.link.services.common.exception.SdkServiceErrorException;
import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;
import com.hp.jetadvantage.link.services.devicelet.adapter.DeviceInfoAdapter;

/**
 * {@link ContentProvider} to handle
 * operations.
 */
public final class DeviceletContentProvider extends ContentProvider {
    private static final String TAG = TAG_DEVICE_SERVICE + "/CProvider";
    // For compatibility < 1.3
    private final String KEY_ATTRIBUTE_NAME = "attributeName";
    private final String KEY_CLIENT_VERSION = "clientVersion";
    private IDeviceInfoService deviceInfoService;

    @Override
    public int delete(@NonNull final Uri uri, final String selection, final String[] selectionArgs) {
        return -1;
    }

    @Override
    public String getType(@NonNull final Uri uri) {
        SLog.d(Devicelet.TAG, "getType uri: " + uri);

        switch (sUriMatcher.match(uri)) {
            case CODE_ATTRIBUTE:
                SLog.d(Devicelet.TAG, " in CODE_ATTRIBUTE");
                return CONTENT_TYPE_ATTRIBUTE;
            default:
                throw new IllegalArgumentException("Unknown or Invalid URI: " + uri);
        }
    }

    @Override
    public boolean onCreate() {
        deviceInfoService = new StandardDeviceInfoService();
        return true;
    }

    @Override
    public Cursor query(@NonNull final Uri uri, final String[] projection,
                        final String selection, final String[] selectionArgs,
                        final String sortOrder) {

        MatrixCursor cursor = new MatrixCursor(RESULT_COLUMN_NAMES);
        String key = "";
        try (ThreadPolicyManager policyManager = new ThreadPolicyManager()) {
            SpsPermissionHelper.ensurePermission(getContext());

            final String path = uri.getPath();
            key = path.substring(1);
            final int type = sUriMatcher.match(uri);

            Log.d(TAG, "query: type = " + type + ", key = " + key);
            Log.i(TAG, "query: Start: " + DeviceAttribute.valueOf(uri.getLastPathSegment()));

            final PrinterInfo pi = ensureValidPrinterInfo();
            if (!isDeviceServiceSupported(pi)) {
                throw new SdkNotSupportedException(DEVICE_SERVICE_NOT_SUPPORTED);
            }

            DeviceAttribute attribute = DeviceAttribute.valueOf(uri.getLastPathSegment());
            String value = DeviceInfoAdapter.getDeviceInfo(deviceInfoService, attribute);
            if (value != null) {
                Result result = new Result();
                return setResult(cursor, key, Result.pack(result, Result.RESULT_OK), value);
            } else {
                throw new SdkServiceErrorException("Device content value is null.");
            }
        } catch (SdkException e) {
            Log.e(TAG, e.getResult().toString());
            return setResult(cursor, key, e.getResult(), null);
        } catch (BoundDeviceException e) {
            Log.e(TAG, e.getMessage());
            return setResult(cursor, key, Result.RESULT_FAIL, Result.ErrorCode.CONNECTION_ERROR.name(), e.getMessage(),
                    null);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return setResult(cursor, key, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR.name(), e.getMessage(),
                    null);
        }
    }

    @Override
    public int update(@NonNull final Uri uri, final ContentValues values, final String selection,
                      final String[] selectionArgs) {
        return -1;
    }

    @Override
    public Uri insert(@NonNull final Uri uri, final ContentValues values) {
        return null;
    }

    @Override
    public synchronized Bundle call(@NonNull final String method, final String arg, final Bundle extras) {
        final Bundle bundle = new Bundle();
        Result.pack(bundle, Result.RESULT_OK);
        Log.v(TAG, "call " + method);

        try (ThreadPolicyManager policyManager = new ThreadPolicyManager()) {
            if (getContext() == null) {
                throw new SdkInvalidParamException("Context is null");
            }

            SpsPermissionHelper.ensurePermission(getContext());
            ensureClientVersionSupported(method, extras);

            final PrinterInfo pi = ensureValidPrinterInfo();
            boolean serviceSupported = isDeviceServiceSupported(pi);
            if (Devicelet.Method.IS_SUPPORTED.equals(method)) {
                bundle.putBoolean(Devicelet.IS_SUPPORTED_EXTRA, serviceSupported);
                return bundle;
            }
            if (!serviceSupported) {
                throw new SdkNotSupportedException(DEVICE_SERVICE_NOT_SUPPORTED);
            }

            switch (method) {
                case Devicelet.Method.GET_ATTRIBUTE:
                    String key = extras.getString(KEY_ATTRIBUTE_NAME);
                    if (TextUtils.isEmpty(key)) {
                        throw new SdkInvalidParamException(EMPTY_ATTRIBUTE_KEY);
                    }
                    return getDeviceAttribute(bundle, key);
                default:
                    throw new SdkInvalidParamException(INVALID_METHOD + " (" + method + ")");
            }
        } catch (SdkException e) {
            Result.pack(bundle, e.getResult());
        } catch (BoundDeviceException e) {
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.CONNECTION_ERROR, e.getMessage());
        } catch (Exception e) {
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }
        return bundle;
    }

    // =========================== private helper methods ===========================
    private PrinterInfo ensureValidPrinterInfo() throws SdkException {
        final PrinterInfo pi = SelectedPrinterHelper.get(getContext().getContentResolver());
        if (!PrinterInfo.isConnected(pi)) {
            Log.e(TAG, DEVICE_NOT_CONNECTED);
            throw new SdkConnectionErrorException(DEVICE_NOT_CONNECTED);
        }

        if (pi.getApiType() != ApiType.OXP) {
            throw new SdkNotSupportedException(API_TYPE_NOT_SUPPORTED);
        }
        return pi;
    }

    private void ensureClientVersionSupported(final String method, final Bundle extras) throws SdkNotSupportedException {
        // if extras is null - it means it's old API 1
        int currentClientVersion = extras != null && extras.containsKey(KEY_CLIENT_VERSION) ?
                extras.getInt(KEY_CLIENT_VERSION, Sdk.VERSION_LEVEL.ONE) : Sdk.VERSION_LEVEL.ONE;

        int minClientVersion = Sdk.VERSION_LEVEL.ONE;
        switch (method) {
            // Can not use because of compatibility < 1.3
            case Devicelet.Method.GET_ATTRIBUTE:
                minClientVersion = Sdk.VERSION_LEVEL.THREE;
                break;
        }
        if (minClientVersion > currentClientVersion) throw new SdkNotSupportedException(CLIENT_VERSION_NOT_SUPPORTED);
    }

    private Bundle getDeviceAttribute(final Bundle bundle, String attributeKey) throws SdkServiceErrorException {
        DeviceAttribute attribute = DeviceAttribute.valueOf(attributeKey);
        String value = DeviceInfoAdapter.getDeviceInfo(deviceInfoService, attribute);
        Log.d(TAG, "Value for key " + attributeKey + ": " + value);

        if (value == null) {
            throw new SdkServiceErrorException(DEVICE_SERVICE_GET_ATTRIBUTE_FAILURE);
        }
        Result.pack(bundle, Result.RESULT_OK, null);
        bundle.putString(Result.KEY_RESULT, value);
        return bundle;
    }

    private boolean isDeviceServiceSupported(final PrinterInfo pi) {
        boolean isSupported = true;
        return isSupported;
    }

    private Cursor setResult(final MatrixCursor cursor, final String key, final int code,
                             final String errorCode, final String cause, final String value) {
        final String[] columnValues = new String[cursor.getColumnCount()];

        Bundle bundle = new Bundle();

        bundle.putInt(Result.KEY_CODE, code);
        bundle.putString(Result.KEY_CAUSE, cause);

        columnValues[Devicelet.RESULT_CODE_COLUMN] = Integer.toString(code);
        columnValues[Devicelet.RESULT_ERROR_CODE_COLUMN] = errorCode;
        columnValues[Devicelet.RESULT_CAUSE_COLUMN] = cause;
        columnValues[Devicelet.KEY_COLUMN] = key;
        columnValues[Devicelet.VALUE_COLUMN] = value;

        Log.d(Devicelet.TAG, " Result.pack: bundle = " + columnValues[0] + ", key = " + key + ", value = " + value);

        cursor.addRow(columnValues);

        return cursor;
    }

    private Cursor setResult(final MatrixCursor cursor, final String key, Result result, String value) {
        int code = result.getCode();
        String errorCode = result.getErrorCode() != null ? result.getErrorCode().name() : null;
        String cause = result.getCause();

        return setResult(cursor, key, code, errorCode, cause, value);
    }
}
