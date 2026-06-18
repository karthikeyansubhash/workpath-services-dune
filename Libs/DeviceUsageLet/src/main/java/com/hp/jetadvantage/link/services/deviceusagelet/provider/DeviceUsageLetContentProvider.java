// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.deviceusagelet.provider;

import androidx.annotation.NonNull;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;

import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.helper.SelectedPrinterHelper;
import com.hp.jetadvantage.link.common.model.ApiType;
import com.hp.jetadvantage.link.common.model.PrinterInfo;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.common.exception.SdkConnectionErrorException;
import com.hp.jetadvantage.link.services.common.exception.SdkException;
import com.hp.jetadvantage.link.services.common.exception.SdkInvalidParamException;
import com.hp.jetadvantage.link.services.common.exception.SdkNotSupportedException;
import com.hp.jetadvantage.link.services.common.exception.SdkUnauthorizedException;
import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;
import com.hp.jetadvantage.link.services.deviceusagelet.adapter.DeviceUsageAdapter;
import com.hp.workpath.api.deviceusage.DeviceUsagelet;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceUsageService;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceUsageService;



/**
 * Main DeviceUsageLet provider. It's responsible for serve base DeviceUsageService operations via
 * {@link #call(String, String, Bundle)} method.
 */
public final class DeviceUsageLetContentProvider extends ContentProvider {

    private static final String TAG = DeviceUsagelet.TAG;

    private static final UriMatcher S_URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.sec.DeviceUsagelet";

    private static final int DEVICE_USAGE_LET_CODE = 1;

    static {
        S_URI_MATCHER.addURI(DeviceUsagelet.AUTHORITY, DeviceUsagelet.DIR_PATH_SEGMENT, DEVICE_USAGE_LET_CODE);
    }

    private IDeviceUsageService mDeviceUsageService;

    /**
     * Default constructor
     */
    public DeviceUsageLetContentProvider() {
    }

    @Override
    public boolean onCreate() {
        mDeviceUsageService = new StandardDeviceUsageService();
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
            case DEVICE_USAGE_LET_CODE:
                SLog.d(DeviceUsagelet.TAG, " in DEVICEUSAGE_LET_CODE ");
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
            int clientVersion = extras != null && extras.containsKey(DeviceUsagelet.Keys.KEY_CLIENT_VERSION) ?
                    extras.getInt(DeviceUsagelet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION_LEVEL.ONE) : Sdk.VERSION_LEVEL.ONE;

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

            if (DeviceUsagelet.Method.IS_SUPPORTED.equals(method)) {
                bundle.putBoolean(DeviceUsagelet.IS_SUPPORTED_EXTRA, serviceSupported);
            } else {
                if (!serviceSupported) {
                    throw new SdkNotSupportedException("DeviceUsageService is not supported");
                }

                String pkgName = extras.getString(DeviceUsagelet.Param.PACKAGE_NAME);
                if (TextUtils.isEmpty(pkgName)) {
                    throw new SdkInvalidParamException("Package name is empty");
                }

                String callingPkg = this.getCallingPackage();
                if (TextUtils.isEmpty(callingPkg) || !pkgName.equalsIgnoreCase(callingPkg)) {
                    throw new SdkUnauthorizedException("Service is not allowed for " + pkgName);
                }

                switch (method) {
                    case DeviceUsagelet.Method.GET_DEVICEUSAGE:
                        return getDeviceUsageInfo(pi, clientVersion, bundle, pkgName);

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

    private Bundle getDeviceUsageInfo(final PrinterInfo pi, final int clientVersion, final Bundle bundle, String packageName) throws Exception {
        if (pi.getApiType() == ApiType.OXP) {
            try {
                String deviceUsageInfo = null;
                try {
                    deviceUsageInfo = DeviceUsageAdapter.convertToWorkpathDeviceUsageAdapter(mDeviceUsageService.getLifetimeCounters(packageName));
                } catch (Throwable e) {
                    SLog.e(DeviceUsagelet.TAG, "Not found device usage information from dune: " + e.getMessage(), e);
                }

                Result.pack(bundle, Result.RESULT_OK);
                bundle.putSerializable(Result.KEY_RESULT, (deviceUsageInfo != null) ? deviceUsageInfo : null);
            } catch (Throwable e) {
                SLog.e(TAG, "Failed to retrieve device usage information:" + e.getMessage());
                Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
            }

            return bundle;
        }

        SLog.e(TAG, "Printer doesn't support device usage service");
        throw new SdkNotSupportedException("Printer doesn't support device usage service");
    }

    private boolean isSupported() {
        return mDeviceUsageService.isSupported();
    }
}
