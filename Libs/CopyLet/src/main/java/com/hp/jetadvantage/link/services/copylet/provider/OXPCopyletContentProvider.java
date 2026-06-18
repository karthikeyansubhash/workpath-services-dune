// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.copylet.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.copier.Copylet;
import com.hp.jetadvantage.link.api.copier.JobCredentialsAttributes;
import com.hp.jetadvantage.link.api.copier.intent.DeleteRequestIntent;
import com.hp.jetadvantage.link.api.copier.StoredJobInfo;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.helper.SelectedPrinterHelper;
import com.hp.jetadvantage.link.common.model.PrinterInfo;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceCopyJobService;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceCopyJobService;
import com.hp.jetadvantage.link.services.common.exception.SdkConnectionErrorException;
import com.hp.jetadvantage.link.services.common.exception.SdkException;
import com.hp.jetadvantage.link.services.common.exception.SdkInvalidParamException;
import com.hp.jetadvantage.link.services.common.exception.SdkNotSupportedException;
import com.hp.jetadvantage.link.services.common.exception.SdkServiceErrorException;
import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;
import com.hp.jetadvantage.link.services.common.util.ErrorCodeResolver;
import com.hp.jetadvantage.link.services.copylet.adapter.CopyDeviceAdapter;
import com.hp.jetadvantage.link.services.copylet.adapter.CopyJobAdapter;
import com.hp.jetadvantage.link.services.copylet.adapter.CopyOptionProfileAdapter;
import java.util.ArrayList;
import java.util.List;

public class OXPCopyletContentProvider extends ContentProvider {
    private static final String TAG = Copylet.TAG + "/OXPCopyletCP";

    private static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.sec.Copylet";

    private static final int Copylet_CODE = 1;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(Copylet.AUTHORITY_OXP, Copylet.DIR_PATH_SEGMENT, Copylet_CODE);
    }

    private IDeviceCopyJobService copyDeviceService;

    public OXPCopyletContentProvider() {
        this.copyDeviceService = new StandardDeviceCopyJobService();
    }

//    private CopyOptionsProfile mCopyOptionsProfile;

    /**
     * Constructor just for mocking.
     *
     * @param copyDeviceService Injected device service
     */
    public OXPCopyletContentProvider(IDeviceCopyJobService copyDeviceService) {
        this.copyDeviceService = copyDeviceService;
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] strings, String s, String[] strings1, String s1) {
        return null;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case Copylet_CODE:
                SLog.d(Copylet.TAG, " in Copylet_CODE");
                return CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unknown or Invalid URI " + uri);
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    @Nullable
    @Override
    public Bundle call(@NonNull String method, String arg, Bundle extras) {
        final Bundle bundle = new Bundle();
        Result.pack(bundle, Result.RESULT_OK);

        String appPackageName = this.getCallingPackage();
        if (extras != null) {
            extras.setClassLoader(DeleteRequestIntent.class.getClassLoader());
        }

        // if extras is null - it means it's old API 1
        int clientVersion = extras != null && extras.containsKey(Copylet.Keys.KEY_CLIENT_VERSION) ?
                extras.getInt(Copylet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION_LEVEL.ONE) : Sdk.VERSION_LEVEL.ONE;

        StrictMode.ThreadPolicy originalPolicy = StrictMode.getThreadPolicy();
        try {
            if (getContext() == null) {
                throw new SdkInvalidParamException("Context is null");
            }

            SpsPermissionHelper.ensurePermission(getContext());

            final PrinterInfo pi = SelectedPrinterHelper.get(getContext().getContentResolver());
            if (PrinterInfo.isEmpty(pi)) {
                throw new SdkConnectionErrorException("Device is not connected");
            }

            final StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
            StrictMode.setThreadPolicy(policy);

            boolean serviceSupported = isSupported(pi);

            if (Copylet.Method.IS_SUPPORTED.equals(method)) {
                bundle.putBoolean(Copylet.IS_SUPPORTED_EXTRA, serviceSupported);
            } else {
                if (!serviceSupported) {
                    throw new SdkNotSupportedException("CopierService is not supported");
                }

                String resultStr;
                switch (method) {
                    case Copylet.Method.GET_CAPS:
                        resultStr = CopyOptionProfileAdapter.getCaps(appPackageName, copyDeviceService, clientVersion);
                        break;
                    case Copylet.Method.GET_DEFAULTS:
                        resultStr = CopyDeviceAdapter.getDefaults(appPackageName, copyDeviceService, clientVersion);
                        break;
                    case Copylet.Method.GET_STATUS:
                        resultStr = getStatus(pi, clientVersion, bundle);
                        break;
                    case Copylet.Method.DELETE_JOB:
                        SLog.d(TAG, "call : DELETE_JOB requested");
                        if (extras == null || !extras.containsKey(Copylet.Keys.KEY_STORED_JOB_ID)) {
                            throw new SdkInvalidParamException("Job id is missing");
                        }

                        Intent intent = extras.getParcelable(Copylet.Keys.KEY_DELETE_REQ);
                        if (intent == null || intent.getExtras() == null) {
                            throw new SdkInvalidParamException("Invalid parameters");
                        }

                        String jobId = extras.getString(Copylet.Keys.KEY_STORED_JOB_ID);
                        SLog.d(TAG, "call : DELETE_JOB jobId=" + jobId);
                        resultStr = deleteJob(pi, clientVersion, jobId, intent.getExtras(), bundle, appPackageName);
                        break;
                    case Copylet.Method.ENUMERATE_JOBS: {
                        SLog.d(TAG, "call : ENUMERATE_JOBS requested for package=" + appPackageName);
                        List<StoredJobInfo> storedJobInfoList =
                                CopyJobAdapter.enumerateAndConvertStoredJobs(getContext(), copyDeviceService, appPackageName);
                        bundle.putParcelableArrayList(Result.KEY_RESULT, new ArrayList<>(storedJobInfoList));
                        SLog.d(TAG, "call : ENUMERATE_JOBS returned " + storedJobInfoList.size() + " jobs");
                        resultStr = null;
                        break;
                    }
                    default:
                        throw new SdkInvalidParamException("Method " + method + " is not supported");
                }
                if (resultStr != null) {
                    bundle.putString(Result.KEY_RESULT, resultStr);
                }
            }
        } catch (SdkException e) {
            Result.pack(bundle, e.getResult());
        } catch (Exception e) {
            Result.pack(bundle, Result.RESULT_FAIL, ErrorCodeResolver.resolve(e, Result.ErrorCode.SERVICE_ERROR),
                    e.getMessage());
            SLog.e(TAG, "Failed to perform operation", e);
        } finally {
            if (originalPolicy != null) {
                StrictMode.setThreadPolicy(originalPolicy);
            }
        }
        return bundle;
    }

    private String deleteJob(final PrinterInfo pi, final int clientVersion, final String jobId, Bundle reqBundle,
                             final Bundle bundle, final String appPackageName) throws Exception {
        SLog.d(TAG, "deleteJob : START, jobId=" + jobId);

        reqBundle.setClassLoader(JobCredentialsAttributes.class.getClassLoader());
        DeleteRequestIntent.IntentParams reqParams = DeleteRequestIntent.getIntentParams(reqBundle);
        JobCredentialsAttributes credentials = reqParams != null ? reqParams.getJobCredentialsAttributes() : null;

        CopyJobAdapter.deleteStoredJob(getContext(), copyDeviceService, appPackageName, jobId, credentials);
        Result.pack(bundle, Result.RESULT_OK);
        SLog.d(TAG, "deleteJob : END, success");

        return null;
    }

    private boolean isSupported(final PrinterInfo pi) {
        boolean isSupported =
                pi.getCapabilities() == 0 || (pi.getCapabilities() & PrinterInfo.Capability.CAPABILITY_COPY) != 0;

        if (isSupported) {
            if (copyDeviceService == null) {
                SLog.i(TAG, "Copy device service is null");
                isSupported = false;
            } else {
                isSupported = copyDeviceService.isSupported();
            }
        }
        SLog.d(TAG, "Device supports COPY feature: " + isSupported);

        return isSupported;
    }

    //여기는 원래 주석처리되어 있었음.. 안쓰나..?
    private String getStatus(PrinterInfo pi, int clientVersion, Bundle bundle) throws Exception {
//        final OXPdCopy copyDevice = ErrorCodeResolver.getClient(getContext(), pi).getCopy();
//
//        CopyStatus scannerStatus = new OXPdAsyncCallFuture<ScannerStatus>() {
//            public void execute() throws Exception {
//                scanDevice.GetScannerStatus(0, this);
//            }
//        }.getResult();
//
//        if (scannerStatus != null) {
//            Result.pack(bundle, Result.RESULT_OK);
//
//            com.hp.jetadvantage.link.api.scanner.ScannerStatus status = new com.hp.jetadvantage.link.api.scanner.ScannerStatus(
//                    scannerStatus.isOnline, scannerStatus.isBusy,
//                    getStatusCondition(scannerStatus.isAdfOutputBinFull),
//                    getStatusCondition(scannerStatus.isPaperInAdf),
//                    getStatusCondition(scannerStatus.isPaperInFlatbed)
//            );
//
//            return JsonParser.getInstance().toJson(status);
//        }

        throw new SdkServiceErrorException("Failed to retrieve scanner status from the device");
    }
}
