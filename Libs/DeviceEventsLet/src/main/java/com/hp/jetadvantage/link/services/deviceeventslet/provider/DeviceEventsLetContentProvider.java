// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.deviceeventslet.provider;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.helper.SelectedPrinterHelper;
import com.hp.jetadvantage.link.common.model.PrinterInfo;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceEventService;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceEventService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardJsonParser;
import com.hp.jetadvantage.link.services.common.exception.SdkConnectionErrorException;
import com.hp.jetadvantage.link.services.common.exception.SdkException;
import com.hp.jetadvantage.link.services.common.exception.SdkInvalidParamException;
import com.hp.jetadvantage.link.services.common.exception.SdkNotSupportedException;
import com.hp.jetadvantage.link.services.common.exception.SdkUnauthorizedException;
import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;
import com.hp.jetadvantage.link.services.deviceeventslet.adapter.DeviceEventAdapter;
import com.hp.jetadvantage.link.services.deviceeventslet.handler.DeviceEventHandler;
import com.hp.jetadvantage.link.services.deviceeventslet.model.DeviceEvent;
import com.hp.workpath.api.deviceevents.DeviceEventslet;

import java.util.List;

/**
 * Main DeviceEventsLet provider. It's responsible for serve base DeviceEventsService operations via
 * {@link #call(String, String, Bundle)} method.
 */
public final class DeviceEventsLetContentProvider extends ContentProvider {

    private static final String TAG = DeviceEventslet.TAG;

    private static final UriMatcher S_URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.sec.DeviceEventslet";

    private static final int DEVICE_EVENTS_LET_CODE = 1;

    public static final String INIT_DEVICE_EVENTS = "init_device_events";

    static {
        S_URI_MATCHER.addURI(DeviceEventslet.AUTHORITY, DeviceEventslet.DIR_PATH_SEGMENT, DEVICE_EVENTS_LET_CODE);
    }

    private IDeviceEventService mDeviceEventService;

    /**
     * Default constructor
     */
    public DeviceEventsLetContentProvider() {
    }

    @Override
    public boolean onCreate() {
        SLog.e(TAG, "DeviceEventsLetContentProvider: created");
        mDeviceEventService = new StandardDeviceEventService();
        DeviceEventAdapter.addCallback(mDeviceEventService, new DeviceEventHandler(getContext()));
        return true;
    }

    @Override
    public synchronized Uri insert(final Uri uri, final ContentValues values) {
        return null;
    }

    @Override
    public synchronized Cursor query(@NonNull final Uri uri, final String[] projection, final String selection,
                                     final String[] selectionArgs,
                                     final String sortOrder) {
        return null;
    }

    @Override
    public synchronized int update(@NonNull final Uri uri, final ContentValues values, final String selection,
                                   final String[] selectionArgs) {
        return 0;
    }

    @Override
    public synchronized int delete(@NonNull final Uri uri, final String selection, final String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(@NonNull final Uri uri) {
        switch (S_URI_MATCHER.match(uri)) {
            case DEVICE_EVENTS_LET_CODE:
                SLog.d(TAG, " in DEVICE_EVENTS_LET_CODE ");
                return CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unknown or Invalid URI: " + uri);
        }
    }

    @Override
    public synchronized Bundle call(@NonNull final String method, final String arg, final Bundle extras) {
        final Bundle bundle = new Bundle();
        Result.pack(bundle, Result.RESULT_OK);

        StrictMode.ThreadPolicy originalPolicy = StrictMode.getThreadPolicy();
        try {
            if (getContext() == null) {
                SLog.e(TAG, "Context is null, invalid call");
                throw new SdkInvalidParamException("Context is null");
            }

            // if extras is null - it means it's old API 1
            int clientVersion = extras != null && extras.containsKey(DeviceEventslet.Keys.KEY_CLIENT_VERSION) ?
                    extras.getInt(DeviceEventslet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION_LEVEL.ONE) : Sdk.VERSION_LEVEL.ONE;

            if (TextUtils.isEmpty(method)) {
                SLog.e(TAG, "method is null, invalid call");
                throw new SdkInvalidParamException("Invalid access");
            }
            SLog.v(TAG, "call " + method);
            SpsPermissionHelper.ensurePermission(getContext());

            final PrinterInfo pi = SelectedPrinterHelper.get(getContext().getContentResolver());

            if (PrinterInfo.isEmpty(pi)) {
                SLog.e(TAG, "Device is not connected");
                throw new SdkConnectionErrorException("Device is not connected");
            }

            final StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
            StrictMode.setThreadPolicy(policy);

            boolean serviceSupported = isSupported();

            if (DeviceEventslet.Method.IS_SUPPORTED.equals(method)) {
                bundle.putBoolean(DeviceEventslet.IS_SUPPORTED_EXTRA, serviceSupported);
            } else {
                if (!serviceSupported) {
                    throw new SdkNotSupportedException("DeviceEventsService is not supported");
                }

                String pkgName = extras.getString(DeviceEventslet.Param.PACKAGE_NAME);
                if (TextUtils.isEmpty(pkgName)) {
                    throw new SdkInvalidParamException("Package name is empty");
                }

                String callingPkg = this.getCallingPackage();
                if (TextUtils.isEmpty(callingPkg) || !pkgName.equalsIgnoreCase(callingPkg)) {
                    throw new SdkUnauthorizedException("Service is not allowed for " + pkgName);
                }

                switch (method) {
                    case INIT_DEVICE_EVENTS:
                    case DeviceEventslet.Method.GET_DEVICE_EVENTS:
                        return getDeviceEventsInfo(bundle, pkgName);

                    default:
                        throw new SdkInvalidParamException("Method " + method + " is not supported");
                }
            }
        } catch (SdkException e) {
            Result.pack(bundle, e.getResult());
        } catch (Throwable e) {
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        } finally {
            if (originalPolicy != null) {
                StrictMode.setThreadPolicy(originalPolicy);
            }
        }

        return bundle;
    }

    @SuppressLint("RestrictedApi")
    private Bundle getDeviceEventsInfo(final Bundle bundle, String packageName) {
        Preconditions.checkNotNull(packageName, "Invalid access, pkgname is null");
        try {
            List<DeviceEvent> deviceEventsList = DeviceEventAdapter.getDeviceEvents(mDeviceEventService);
            Result.pack(bundle, Result.RESULT_OK);
            bundle.putSerializable(Result.KEY_RESULT, StandardJsonParser.INSTANCE.toJson(deviceEventsList));
        } catch (Throwable e) {
            SLog.e(TAG, "Failed to retrieve device events information:" + e.getMessage());
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }
        return bundle;
    }

    private boolean isSupported() {
        return DeviceEventAdapter.isSupported(mDeviceEventService);
    }
}
