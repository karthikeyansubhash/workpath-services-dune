// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.configlet.provider;

import static com.hp.jetadvantage.link.common.constants.LogConstants.TAG_CONFIG_SERVICE;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.config.ConfigService;
import com.hp.jetadvantage.link.api.config.Configlet;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.common.utils.StringUtility;
import com.hp.jetadvantage.link.device.services.exceptions.IllegalSolutionException;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceSolutionManager;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceSolutionManager;
import com.hp.jetadvantage.link.services.common.exception.SdkException;
import com.hp.jetadvantage.link.services.common.exception.SdkInvalidParamException;
import com.hp.jetadvantage.link.services.common.exception.SdkNotSupportedException;
import com.hp.jetadvantage.link.services.common.exception.SdkServiceErrorException;
import com.hp.jetadvantage.link.services.common.exception.SdkUnauthorizedException;
import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;
import com.hp.jetadvantage.link.services.configlet.adapter.ConfigAdapter;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Main ConfigLet provider. It's responsible for serve base {@link ConfigService} operations via
 * {@link #call(String, String, Bundle)} method.<br>
 */
public final class ConfigLetContentProvider extends ContentProvider {

    public static final String DIR_CONFIG_SEGMENT = "configs";
    protected static final int FREQUENT_MODIFIED_THRESHOLD_COUNT = 10;
    protected static final long FREQUENT_MODIFIED_THRESHOLD_SECONDS = 5;
    private static final String TAG = TAG_CONFIG_SERVICE + "/" + "CP";
    private static final UriMatcher S_URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.sec.Configlet";
    private static final int CONFIG_LET_CODE = 1;
    private static final int CONFIG_CODE = 2;
    private static final int CONFIG_ITEM_CODE = 3;
    // Map to store the last modified time of the config data for each package
    // ConcurrentHashMap<PackageName, last modified time>
    private static ConcurrentHashMap<String, ConcurrentLinkedDeque<LocalDateTime>> mLastModifiedTimeMap =
            new ConcurrentHashMap<>();

    static {
        S_URI_MATCHER.addURI(Configlet.AUTHORITY, Configlet.DIR_PATH_SEGMENT, CONFIG_LET_CODE);
        S_URI_MATCHER.addURI(Configlet.AUTHORITY, DIR_CONFIG_SEGMENT, CONFIG_CODE);
        S_URI_MATCHER.addURI(Configlet.AUTHORITY, DIR_CONFIG_SEGMENT + "/*", CONFIG_ITEM_CODE);
    }

    private IDeviceSolutionManager mSolutionManager;

    /**
     * Default constructor
     */
    public ConfigLetContentProvider() {
        mSolutionManager = new StandardDeviceSolutionManager();
    }

    public ConfigLetContentProvider(IDeviceSolutionManager solutionManager) {
        mSolutionManager = solutionManager;
    }

    /**
     * Check if the package has been modified frequently within the last to prevent sending excessive notification.
     *
     * @return true if at least FREQUENT_MODIFIED_THRESHOLD_COUNT modifications
     * occurred within the last FREQUENT_MODIFIED_THRESHOLD_SECONDS.
     */
    public static boolean isModifiedFrequently(String packageName, boolean cleanup) {
        if (StringUtility.isEmpty(packageName)) {
            return false;
        }
        boolean frequent = false;
        try {
            ConcurrentLinkedDeque<LocalDateTime> deque =
                    mLastModifiedTimeMap.computeIfAbsent(packageName, k -> new ConcurrentLinkedDeque<>());

            // prune old entries
            LocalDateTime cutoff = LocalDateTime.now().minusSeconds(FREQUENT_MODIFIED_THRESHOLD_SECONDS);
            while (true) {
                LocalDateTime ts = deque.peekFirst();
                if (ts == null || !ts.isBefore(cutoff)) {
                    break;
                }
                deque.pollFirst();
            }

            frequent = deque.size() >= FREQUENT_MODIFIED_THRESHOLD_COUNT;
            if (frequent && cleanup) {
                deque.clear();
                Log.d(TAG, "Cleared modification history for " + packageName);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error checking modification frequency for " + packageName + ": " + e.getMessage());
        }
        return frequent;
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
            case CONFIG_LET_CODE:
                SLog.d(Configlet.TAG, " in CONFIG_LET_CODE ");
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
            Log.d(TAG, "call " + method);
            SpsPermissionHelper.ensurePermission(getContext());

            final StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
            StrictMode.setThreadPolicy(policy);

            boolean serviceSupported = isSupported();

            if (Configlet.Method.IS_SUPPORTED.equals(method)) {
                bundle.putBoolean(Configlet.IS_SUPPORTED_EXTRA, serviceSupported);
            } else {
                if (!serviceSupported) {
                    throw new SdkNotSupportedException("ConfigService is not supported");
                }

                String pkgName = extras.getString(Configlet.Param.PACKAGE_NAME);
                if (TextUtils.isEmpty(pkgName)) {
                    throw new SdkInvalidParamException("Package name is empty");
                }

                String callingPkg = this.getCallingPackage();
                if (TextUtils.isEmpty(callingPkg) || !pkgName.equalsIgnoreCase(callingPkg)) {
                    throw new SdkUnauthorizedException("Service is not allowed for " + pkgName);
                }

                switch (method) {
                    case Configlet.Method.GET_CONFIG:
                        return getConfig(bundle, pkgName);

                    case Configlet.Method.SET_CONFIG:
                        String configData = extras.getString(Configlet.Param.CONFIG_DATA);
                        Bundle result = setConfig(bundle, pkgName, configData);
                        updateLastModifiedTime(pkgName);
                        return result;

                    default:
                        throw new SdkInvalidParamException("Method " + method + " is not supported");
                }
            }
        } catch (SdkException e) {
            Log.e(TAG, "call:" + method + ": SdkException: " + e.getMessage());
            Result.pack(bundle, e.getResult());
        } catch (IllegalSolutionException e) {
            Log.e(TAG, "call:" + method + ": IllegalSolutionException: " + e.getMessage());
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.INVALID_PARAM, e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "call:" + method + ": Exception: " + e.getMessage(), e);
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        } finally {
            if (originalPolicy != null) {
                StrictMode.setThreadPolicy(originalPolicy);
            }
        }

        Log.d(TAG, "call:" + method + ": Failed: " + bundle.toString());
        return bundle;
    }

    protected Bundle getConfig(final Bundle bundle, String packageName) throws SdkException {
        return ConfigAdapter.getConfigData(mSolutionManager, bundle, packageName);
    }

    protected Bundle setConfig(final Bundle bundle, final String packageName,
                               final String configData) throws SdkException {
        final int CONFIG_DATA_MAX_SIZE = 66560;
        if (configData != null && configData.length() > CONFIG_DATA_MAX_SIZE) {
            throw new SdkServiceErrorException("Configuration data exceeds max " + CONFIG_DATA_MAX_SIZE + " " +
                    "allowed");
        }
        return ConfigAdapter.setConfigData(mSolutionManager, bundle, packageName, configData);
    }

    /**
     * Record a modification timestamp for the given package.
     */
    protected void updateLastModifiedTime(String packageName) {
        try {
            ConcurrentLinkedDeque<LocalDateTime> deque =
                    mLastModifiedTimeMap.computeIfAbsent(packageName, k -> new ConcurrentLinkedDeque<>());
            deque.addLast(LocalDateTime.now());
            Log.d(TAG, "Recorded modification for " + packageName);
        } catch (Exception e) {
            Log.e(TAG, "Error updating last modified time for " + packageName + ": " + e.getMessage());
        }
    }

    protected boolean isSupported() {
        //ConfigService must be supported always
        return true;
    }
}
