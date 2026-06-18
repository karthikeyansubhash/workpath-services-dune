// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.statisticslet.provider;

import androidx.annotation.NonNull;

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

import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.helper.SelectedPrinterHelper;
import com.hp.jetadvantage.link.common.model.PrinterInfo;
import com.hp.jetadvantage.link.common.utils.JsonParser;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceStatisticsService;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceStatisticsService;
import com.hp.jetadvantage.link.services.common.exception.SdkConnectionErrorException;
import com.hp.jetadvantage.link.services.common.exception.SdkException;
import com.hp.jetadvantage.link.services.common.exception.SdkInvalidParamException;
import com.hp.jetadvantage.link.services.common.exception.SdkNotSupportedException;
import com.hp.jetadvantage.link.services.common.exception.SdkUnauthorizedException;
import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;
import com.hp.jetadvantage.link.services.statisticslet.adapter.StatisticsAdapter;
import com.hp.jetadvantage.link.services.statisticslet.response.Agent;
import com.hp.workpath.api.statistics.Statisticslet;

/**
 * Main StatisticsLet provider. It's responsible for serve base StatisticsService operations via
 * {@link #call(String, String, Bundle)} method.
 */
public final class StatisticsLetContentProvider extends ContentProvider {

    private static final String TAG = Statisticslet.TAG;

    private static final UriMatcher S_URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.sec.Statisticslet";

    private static final int STATISTICS_LET_CODE = 1;

    private IDeviceStatisticsService statisticsService;

    static {
        S_URI_MATCHER.addURI(Statisticslet.AUTHORITY, Statisticslet.DIR_PATH_SEGMENT, STATISTICS_LET_CODE);
    }

    /**
     * Default constructor
     */
    public StatisticsLetContentProvider() {
        statisticsService = new StandardDeviceStatisticsService();
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
            case STATISTICS_LET_CODE:
                SLog.d(Statisticslet.TAG, " in STATISTICS_LET_CODE ");
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
            int clientVersion = extras != null && extras.containsKey(Statisticslet.Keys.KEY_CLIENT_VERSION) ?
                    extras.getInt(Statisticslet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION_LEVEL.ONE) : Sdk.VERSION_LEVEL.ONE;

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

            boolean serviceSupported = StatisticsAdapter.isSupported(statisticsService);

            if (Statisticslet.Method.IS_SUPPORTED.equals(method)) {
                bundle.putBoolean(Statisticslet.IS_SUPPORTED_EXTRA, serviceSupported);
            } else {
                if (!serviceSupported) {
                    throw new SdkNotSupportedException("StatisticsService is not supported");
                }

                String pkgName = extras.getString(Statisticslet.Param.PACKAGE_NAME);
                if (TextUtils.isEmpty(pkgName)) {
                    throw new SdkInvalidParamException("Package name is empty");
                }

                String callingPkg = this.getCallingPackage();
                if (TextUtils.isEmpty(callingPkg) || !pkgName.equalsIgnoreCase(callingPkg)) {
                    throw new SdkUnauthorizedException("Service is not allowed for " + pkgName);
                }

                switch (method) {
                    case Statisticslet.Method.GET_JOBINFO:
                        int limit = (extras.containsKey(Statisticslet.Param.KEY_JOBLIMIT))?extras.getInt(Statisticslet.Param.KEY_JOBLIMIT):256;
                        int offset = (extras.containsKey(Statisticslet.Param.KEY_OFFSET))?extras.getInt(Statisticslet.Param.KEY_OFFSET):0;
                        if (TextUtils.isEmpty(pkgName)) {
                            throw new SdkInvalidParamException("Package name is empty");
                        }
                        return getJobInfo(pi, clientVersion, bundle, pkgName, offset, limit);

                    case Statisticslet.Method.GET_LASTJOBSEQUENCE:
                        return getLastJobSequence(pi, clientVersion, bundle, pkgName);

                    case Statisticslet.Method.COMMIT_LASTJOBSEQUENCE:
                        int jobSequence = 0;
                        try {
                            jobSequence = extras.getInt(Statisticslet.Param.KEY_JOBSEQUENCE);
                            return commitLastJobSequence(pi, clientVersion, bundle, pkgName, jobSequence);
                        } catch (Throwable throwable) {
                            throw new SdkInvalidParamException("JobSequence is empty");
                        }

                    case Statisticslet.Method.GET_LASTCOMMITTEDJOBSEQUENCE:
                        return getLastCommittedJobSequence(pi, clientVersion, bundle, pkgName);

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

    @SuppressLint("RestrictedApi")
    private Bundle getJobInfo(final PrinterInfo pi, final int clientVersion, final Bundle bundle, String packageName, int offset, int limit) throws Exception {
        Preconditions.checkNotNull(packageName, "Invalid access, pkgname is null");

        try {
            String jobInfo = null;
            int lastSequenceNumberProcessed = 0;

            try {

                try {
                    Bundle tmpBundle = new Bundle();
                    tmpBundle = getLastJobSequence(pi, clientVersion, tmpBundle, packageName);
                    if (tmpBundle != null
                            && Result.parse(tmpBundle, new Result()).getCode() == Result.RESULT_OK
                            && tmpBundle.containsKey(Result.KEY_RESULT)
                            && !TextUtils.isEmpty(tmpBundle.getString(Result.KEY_RESULT))) {

                        Agent agent = JsonParser.getInstance().fromJson(tmpBundle.getString(Result.KEY_RESULT), Agent.class);
                        lastSequenceNumberProcessed = (int)agent.getLastSequenceNumberProcessed();
                        SLog.i(TAG, "agent lastSequenceNumberProcessed: " + lastSequenceNumberProcessed);
                    }
                } catch (Throwable e) {
                    SLog.i(Statisticslet.TAG, "Not found statistics agent: " + e.getMessage());
                    Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.INVALID_PARAM, "No registered statistics agent");
                    return bundle;
                }

                jobInfo = StatisticsAdapter.getAllJobsList(statisticsService, packageName, offset, limit);
                SLog.i(TAG, "Job Info  : " + jobInfo);
                Result.pack(bundle, Result.RESULT_OK);
                bundle.putSerializable(Result.KEY_RESULT, (jobInfo != null) ? jobInfo : null);
            } catch (Throwable e) {
                SLog.e(Statisticslet.TAG, "Not found statistics information from Dune: " + e.getMessage());
                Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
            }
        } catch (Throwable e) {
            SLog.e(TAG, "Failed to retrieve statistics information:" + e.getMessage());
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }

        return bundle;
    }

    @SuppressLint("RestrictedApi")
    private Bundle getLastCommittedJobSequence(final PrinterInfo pi, final int clientVersion, final Bundle bundle, String packageName) throws Exception {
        Preconditions.checkNotNull(packageName, "Invalid access, pkgname is null");
        try {
            String statisticsInfo = null;

            try {
                statisticsInfo = StatisticsAdapter.getJobsList(statisticsService, packageName);
                SLog.d(TAG, "statisticsInfo: " + statisticsInfo);
                Result.pack(bundle, Result.RESULT_OK);
                bundle.putSerializable(Result.KEY_RESULT, (statisticsInfo != null) ? statisticsInfo : null);
            } catch (Throwable e) {
                SLog.e(TAG, "Not found getLastCommittedJobSequence information from Dune: " + e.getMessage());
                Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
            }
        } catch (Throwable e) {
            SLog.e(TAG, "Failed to retrieve statistics agent information:" + e.getMessage());
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }

        return bundle;
    }

    @SuppressLint("RestrictedApi")
    private Bundle getLastJobSequence(final PrinterInfo pi, final int clientVersion, final Bundle bundle, String packageName) throws Exception {
        Preconditions.checkNotNull(packageName, "Invalid access, pkgname is null");
        try {
            String statisticsInfo = null;
            try {
                statisticsInfo = StatisticsAdapter.getJobWithLastSequenceNumberProcessed(statisticsService, packageName);
                Result.pack(bundle, Result.RESULT_OK);
                bundle.putSerializable(Result.KEY_RESULT, (statisticsInfo != null) ? statisticsInfo : null);
            } catch (Throwable e) {
                SLog.e(TAG, "Not found getLastJobSequence information from Dune: " + e.getMessage());
                Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
            }
        } catch (Throwable e) {
            SLog.e(TAG, "Failed to retrieve statistics agent information:" + e.getMessage());
            e.printStackTrace();
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }

        return bundle;
    }

    @SuppressLint("RestrictedApi")
    private Bundle commitLastJobSequence(final PrinterInfo pi, final int clientVersion, final Bundle bundle, String packageName, int lastJobSequence) throws Exception {
        Preconditions.checkNotNull(packageName, "Invalid access, pkgname is null");
        Preconditions.checkNotNull(lastJobSequence, "LastJobSequence cannot be null");

        try {
            String commitResult = null;
            try {
                commitResult = StatisticsAdapter.commitLastJobSequence(statisticsService, packageName, lastJobSequence);
            } catch (Throwable throwable) {
            }

            if (!TextUtils.isEmpty(commitResult)) {
                Result.pack(bundle, Result.RESULT_OK);
                bundle.putSerializable(Result.KEY_RESULT, true);
            } else {
                Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "LastJobSequence is not committed");
            }
        } catch (Throwable e) {
            SLog.e(TAG, "Failed to commit LastJobSequence: " + e.getMessage());
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }

        return bundle;
    }
}
