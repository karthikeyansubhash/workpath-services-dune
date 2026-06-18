// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.launcherlet.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.launcher.LaunchAction;
import com.hp.jetadvantage.link.api.launcher.LauncherService;
import com.hp.jetadvantage.link.api.launcher.Launcherlet;
import com.hp.jetadvantage.link.common.Platform;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceUISwitchService;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceUISwitchService;
import com.hp.jetadvantage.link.services.common.exception.SdkException;
import com.hp.jetadvantage.link.services.common.exception.SdkInvalidParamException;
import com.hp.jetadvantage.link.services.common.exception.SdkNotSupportedException;
import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;
import com.hp.jetadvantage.link.services.launcherlet.provider.util.LauncherUtility;

/**
 * Main LauncherLet provider. It's responsible for serve base {@link LauncherService} operations.<br>
 */
public final class LauncherLetContentProvider extends ContentProvider {

    private static final String TAG = Launcherlet.TAG;

    private static final UriMatcher S_URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.sec.Launcherlet";

    private static final int LAUNCHER_LET_CODE = 1;

    static {
        S_URI_MATCHER.addURI(Launcherlet.AUTHORITY, Launcherlet.DIR_PATH_SEGMENT, LAUNCHER_LET_CODE);
    }


    private static final String CUSTOM_METHOD_LAUNCH_FROM_HOME = "launchFromDeviceHome";
    private static final String CUSTOM_METHOD_EXIT_FROM_HOME = "exitFromDeviceHome";

    /**
     * Default constructor
     */
    public LauncherLetContentProvider() {
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
            case LAUNCHER_LET_CODE:
                SLog.d(Launcherlet.TAG, " in LAUNCHER_LET_CODE ");
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
            SLog.v(Launcherlet.TAG, "call " + method);

            SpsPermissionHelper.ensurePermission(getContext());

            final StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
            StrictMode.setThreadPolicy(policy);

            boolean serviceSupported = isSupported();

            if (Launcherlet.Method.IS_SUPPORTED.equals(method)) {
                bundle.putBoolean(Launcherlet.IS_SUPPORTED_EXTRA, serviceSupported);
            } else {
                if (!serviceSupported) {
                    throw new SdkNotSupportedException("LauncherService is not supported");
                }

                extras.setClassLoader(Launcherlet.class.getClassLoader());

                // if extras is null - it means it's old API 1
                int clientVersion = extras != null && extras.containsKey(Launcherlet.Keys.KEY_CLIENT_VERSION) ?
                        extras.getInt(Launcherlet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION_LEVEL.ONE) : Sdk.VERSION_LEVEL.ONE;

                if (!supportClientVersionMethod(method, clientVersion)) {
                    throw new SdkNotSupportedException("LauncherService is not supported");
                }

                String pkgName = extras.getString(Launcherlet.Keys.PACKAGE_NAME);
                if (TextUtils.isEmpty(pkgName)) {
                    throw new SdkInvalidParamException("Package name is empty");
                }
                SLog.v(TAG, "method: " + method + " pkgName: " + pkgName);

                switch (method) {
                    case Launcherlet.Method.LAUNCH:
                        LaunchAction action = (LaunchAction) extras.getSerializable(Launcherlet.Keys.KEY_LAUNCH_ACTION);
                        return launch(action, bundle);
                    case Launcherlet.Method.APPLICATION:
                        String uuid = extras.getString(Launcherlet.Keys.KEY_APPLICATION_UUID);
                        return startApplication(bundle, uuid);
                    case CUSTOM_METHOD_LAUNCH_FROM_HOME:
                        return launchFromHome(bundle, pkgName);
                    case CUSTOM_METHOD_EXIT_FROM_HOME:
                        return exitFromHome(bundle, pkgName);
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

    private Bundle launch(final LaunchAction action, final Bundle bundle) {
        try {
            switch (action) {
                case HOME:
                    goHome(bundle);
                    break;
            }
        } catch (Exception e) {
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }

        return bundle;
    }

    private void goHome(Bundle bundle) {
        if (Platform.isPanel()) {
            IDeviceUISwitchService uiSwitchService = new StandardDeviceUISwitchService();
            try {
                Thread.sleep(300);
            } catch (Exception e) {
            }
            SLog.v(TAG, "Delay Launch Dune Home Screen");
            uiSwitchService.switchToDevice();
            Result.pack(bundle, Result.RESULT_OK);
        } else {
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Feature is not supported");
        }
    }

    private Bundle startApplication(Bundle bundle, String uuid) throws Exception {
        Context context = getContext();
        String intentUri = LauncherUtility.getButtonInfo(context, uuid);
        if (TextUtils.isEmpty(intentUri)) {
            throw new SdkInvalidParamException("Link application is not installed");
        }
        Intent launchIntent = Intent.parseUri(intentUri, Intent.URI_INTENT_SCHEME);
//        launchIntent.setFlags(launchIntent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        getContext().startActivity(launchIntent);
        Result.pack(bundle, Result.RESULT_OK);
        return bundle;
    }

    private Bundle launchFromHome(Bundle bundle, String pkgName) {
        IDeviceUISwitchService uiSwitchService = new StandardDeviceUISwitchService();
        boolean result = uiSwitchService.launchAppFromDeviceHome(pkgName);
        if (!result) {
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "Failed to launch app from the device home screen");
        }
        return bundle;
    }

    private Bundle exitFromHome(Bundle bundle, String pkgName) {
        IDeviceUISwitchService uiSwitchService = new StandardDeviceUISwitchService();
        boolean result = uiSwitchService.closeAppFromDeviceHome(pkgName);
        if (!result) {
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "Failed to launch app from the device home screen");
        }
        return bundle;
    }

    private boolean isSupported() {
        return Platform.isPanel();
    }

    private boolean supportClientVersionMethod(String method, int currentClientVersion) {
        int supportClientVersion = Sdk.VERSION_LEVEL.ONE;
        switch (method) {
            case Launcherlet.Method.LAUNCH:
                supportClientVersion = Sdk.VERSION_LEVEL.TWO;
                break;
            case Launcherlet.Method.APPLICATION:
                supportClientVersion = Sdk.VERSION_LEVEL.THREE;
                break;
        }
        if (supportClientVersion > currentClientVersion) return false;
        else return true;
    }
}
