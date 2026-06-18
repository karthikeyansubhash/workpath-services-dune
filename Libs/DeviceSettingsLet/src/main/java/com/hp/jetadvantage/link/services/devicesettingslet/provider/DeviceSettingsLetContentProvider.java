// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.devicesettingslet.provider;

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

import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.device.settings.DeviceSettingsService;
import com.hp.jetadvantage.link.api.device.settings.DeviceSettingslet;
import com.hp.jetadvantage.link.common.Platform;
import com.hp.jetadvantage.link.common.helper.SelectedPrinterHelper;
import com.hp.jetadvantage.link.common.model.PrinterInfo;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceSettingsService;
import com.hp.jetadvantage.link.services.common.exception.SdkConnectionErrorException;
import com.hp.jetadvantage.link.services.common.exception.SdkException;
import com.hp.jetadvantage.link.services.common.exception.SdkInvalidParamException;
import com.hp.jetadvantage.link.services.common.exception.SdkNotSupportedException;
import com.hp.jetadvantage.link.services.common.exception.SdkServiceErrorException;
import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;
import com.hp.jetadvantage.link.services.common.ssp.SpsConstants;

/**
 * Main ConfigLet provider. It's responsible for serve base {@link DeviceSettingsService} operations via
 * {@link #call(String, String, Bundle)} method.<br>
 */
public final class DeviceSettingsLetContentProvider extends ContentProvider {

    private static final String TAG = DeviceSettingslet.TAG;

    private static final UriMatcher S_URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.sec.DeviceSettingslet";

    private static final int DEVICE_SETTINGS_LET_CODE = 1;

    static {
        S_URI_MATCHER.addURI(DeviceSettingslet.AUTHORITY, DeviceSettingslet.DIR_PATH_SEGMENT, DEVICE_SETTINGS_LET_CODE);
    }

    /**
     * Default constructor
     */
    public DeviceSettingsLetContentProvider() {
    }

    @Override
    public boolean onCreate() {
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
            case DEVICE_SETTINGS_LET_CODE:
                SLog.d(DeviceSettingslet.TAG, " in DEVICE_SETTINGS_LET_CODE ");
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
                throw new SdkInvalidParamException("Context is null");
            }
            SLog.v(DeviceSettingslet.TAG, "call " + method);
            SpsPermissionHelper.ensurePermission(getContext());

            final PrinterInfo pi = SelectedPrinterHelper.get(getContext().getContentResolver());

            if (PrinterInfo.isEmpty(pi)) {
                SLog.e(TAG, "Device is not connected");
                throw new SdkConnectionErrorException("Device is not connected");
            }

            final StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
            StrictMode.setThreadPolicy(policy);

            boolean serviceSupported = isSupported(pi);

            if (DeviceSettingslet.Method.IS_SUPPORTED.equals(method)) {
                bundle.putBoolean(DeviceSettingslet.IS_SUPPORTED_EXTRA, serviceSupported);
            } else {
                if (!serviceSupported) {
                    throw new SdkNotSupportedException("DeviceSettingsService is not supported");
                }

                String pkgName = extras.getString(DeviceSettingslet.Keys.PACKAGE_NAME);
                if (TextUtils.isEmpty(pkgName)) {
                    throw new SdkInvalidParamException("Package name is empty");
                }
                SLog.v(TAG, "method: " + method + " pkgName: " + pkgName);

                switch (method) {
                    case DeviceSettingslet.Method.ENABLE_PRINTING:
                        return enablePrinting(bundle);
                    case DeviceSettingslet.Method.DISABLE_PRINTING:
                        if (!SpsConstants.hasPermissionForPackage(getContext(), pkgName, SpsConstants.DISABLE_PRINTING_PORTS_PERMISSION)) {
                            throw new SdkServiceErrorException("Package " + pkgName + " does not have permission: " + SpsConstants.DISABLE_PRINTING_PORTS_PERMISSION);
                        }
                        return disablePrinting(bundle);
                    default:
                        throw new SdkInvalidParamException("Method " + method + " is not supported");
                }
            }
        } catch (SdkException e) {
            Result.pack(bundle, e.getResult());
        } catch (Exception e) {
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        } finally {
            if (originalPolicy != null) {
                StrictMode.setThreadPolicy(originalPolicy);
            }
        }

        return bundle;
    }

    private Bundle enablePrinting(Bundle bundle) {
        try {
            /** Do nothing. JEDI-72132
             *  It is not allowed to call snmp
             *  So when enable port request comes in, it doesn't do anything and just returns RESULT_OK.
             */
            Result.pack(bundle, Result.RESULT_OK);
        } catch (Exception e) {
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }

        return bundle;
    }

    private Bundle disablePrinting(Bundle bundle) {
        try {
            StandardDeviceSettingsService deviceSettingsService = new StandardDeviceSettingsService();
            boolean success = deviceSettingsService.disableNetworkPrintServices();

            if (success) {
                Result.pack(bundle, Result.RESULT_OK);
            } else {
                Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "Failed to disable network print services");
            }
        } catch (Exception e) {
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }

        return bundle;
    }

    private boolean isSupported(final PrinterInfo pi) {
        // TODO Implement more complex check
        return true;
    }
}
