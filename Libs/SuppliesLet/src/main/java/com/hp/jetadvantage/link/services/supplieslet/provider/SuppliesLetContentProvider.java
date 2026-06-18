// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.supplieslet.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.hp.ext.service.device.Identity;
import com.hp.ext.service.supplies.SuppliesInfo;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.common.helper.SelectedPrinterHelper;
import com.hp.jetadvantage.link.common.model.PrinterInfo;
import com.hp.jetadvantage.link.common.utils.Preconditions;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceSuppliesService;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceSuppliesService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardJsonParser;
import com.hp.jetadvantage.link.services.common.exception.SdkConnectionErrorException;
import com.hp.jetadvantage.link.services.common.exception.SdkException;
import com.hp.jetadvantage.link.services.common.exception.SdkInvalidParamException;
import com.hp.jetadvantage.link.services.common.exception.SdkNotSupportedException;
import com.hp.jetadvantage.link.services.common.exception.SdkUnauthorizedException;
import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;
import com.hp.jetadvantage.link.services.supplieslet.adapter.SuppliesAdapter;
import com.hp.jetadvantage.link.services.supplieslet.model.SuppliesData;
import com.hp.workpath.api.supplies.Supplieslet;

/**
 * Main SuppliesLet provider. It's responsible for serve base SuppliesService operations via
 * {@link #call(String, String, Bundle)} method.
 */
public final class SuppliesLetContentProvider extends ContentProvider {

    private static final String TAG = Supplieslet.TAG;

    private static final String MAKE_AND_MODEL = "MAKE_AND_MODEL";

    private static final UriMatcher S_URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.sec.Supplieslet";

    private static final int SUPPLIES_LET_CODE = 1;

    static {
        S_URI_MATCHER.addURI(Supplieslet.AUTHORITY, Supplieslet.DIR_PATH_SEGMENT, SUPPLIES_LET_CODE);
    }

    private IDeviceSuppliesService mSuppliesService;

    /**
     * Default constructor
     */
    public SuppliesLetContentProvider() {
    }

    @Override
    public boolean onCreate() {
        mSuppliesService = new StandardDeviceSuppliesService();
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
            case SUPPLIES_LET_CODE:
                SLog.d(Supplieslet.TAG, " in SUPPLIES_LET_CODE ");
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

            if (Supplieslet.Method.IS_SUPPORTED.equals(method)) {
                bundle.putBoolean(Supplieslet.IS_SUPPORTED_EXTRA, serviceSupported);
            } else {
                if (!serviceSupported) {
                    throw new SdkNotSupportedException("SuppliesService is not supported");
                }

                String pkgName = extras.getString(Supplieslet.Param.PACKAGE_NAME);
                if (TextUtils.isEmpty(pkgName)) {
                    throw new SdkInvalidParamException("Package name is empty");
                }

                String callingPkg = this.getCallingPackage();
                if (TextUtils.isEmpty(callingPkg) || !pkgName.equalsIgnoreCase(callingPkg)) {
                    throw new SdkUnauthorizedException("Service is not allowed for " + pkgName);
                }

                switch (method) {
                    case Supplieslet.Method.GET_SUPPLIES:
                        return getSupplies(bundle, callingPkg);

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

    private Bundle getSupplies(final Bundle bundle, String packageName) {
        try {
            Preconditions.checkNotNull(packageName, "Invalid access, pkgname is null");
            String makeAndModel = getMakeAndModel();
            SuppliesInfo suppliesInfo = SuppliesAdapter.getSuppliesInfo(mSuppliesService, packageName);
            SuppliesData suppliesData = new SuppliesData(suppliesInfo, makeAndModel);
            Result.pack(bundle, Result.RESULT_OK);
            bundle.putSerializable(Result.KEY_RESULT, StandardJsonParser.INSTANCE.toJson(suppliesData));
        } catch (Exception e) {
            SLog.e(TAG, "Failed to retrieve supplies:" + e.getMessage());
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }
        return bundle;
    }

    private boolean isSupported() {
        return SuppliesAdapter.isSupported(mSuppliesService);
    }

    public void setMakeAndModel(String value) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(MAKE_AND_MODEL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MAKE_AND_MODEL, value);
        editor.apply();
    }

    public String getMakeAndModel() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(MAKE_AND_MODEL, Context.MODE_PRIVATE);
        String makeAndModel = sharedPreferences.getString(MAKE_AND_MODEL, null);
        if (TextUtils.isEmpty(makeAndModel)) {
            Identity identity = SuppliesAdapter.getIdentity(mSuppliesService);
            makeAndModel = identity.getMakeAndModelInfo().getFamily().getValue();
            setMakeAndModel(makeAndModel);
        }
        return makeAndModel;
    }
}
