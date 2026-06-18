// Copyright 2025 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.scanlet.provider;

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
import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.api.job.JobService;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes;
import com.hp.jetadvantage.link.api.scanner.Scanlet;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.helper.SelectedPrinterHelper;
import com.hp.jetadvantage.link.common.model.PrinterInfo;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.exceptions.BoundDeviceException;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceScanJobService;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceScanJobService;
import com.hp.jetadvantage.link.services.common.exception.SdkConnectionErrorException;
import com.hp.jetadvantage.link.services.common.exception.SdkException;
import com.hp.jetadvantage.link.services.common.exception.SdkInvalidParamException;
import com.hp.jetadvantage.link.services.common.exception.SdkNotSupportedException;
import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;
import com.hp.jetadvantage.link.services.joblet.model.JobData;
import com.hp.jetadvantage.link.services.joblet.model.JobDataContentProvider;
import com.hp.jetadvantage.link.services.scanlet.ScanConstants;
import com.hp.jetadvantage.link.services.scanlet.adapter.ScanDefaultOptionAdapter;
import com.hp.jetadvantage.link.services.scanlet.adapter.ScanDeviceStatusAdapter;
import com.hp.jetadvantage.link.services.scanlet.adapter.ScanFileOptionAdapter;
import com.hp.jetadvantage.link.services.scanlet.adapter.ScanOptionProfileAdapter;

import java.io.File;
import java.util.ArrayList;

public class ScanletContentProvider extends ContentProvider {
    private static final String TAG = Scanlet.TAG + "/CP";

    private static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.sec.Scanlet";

    private static final int Scanlet_CODE = 1;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(Scanlet.AUTHORITY_OXP, Scanlet.DIR_PATH_SEGMENT, Scanlet_CODE);
    }

    private IDeviceScanJobService scanDeviceService;

    public ScanletContentProvider() {
        this.scanDeviceService = new StandardDeviceScanJobService();
    }

    /**
     * Constructor just for mocking.
     *
     * @param scanDeviceService Injected device service
     */
    public ScanletContentProvider(IDeviceScanJobService scanDeviceService) {
        this.scanDeviceService = scanDeviceService;
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
            case Scanlet_CODE:
                SLog.d(Scanlet.TAG, " in Scanlet_CODE");
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
        SLog.d(TAG, "call : " + method + " from " + appPackageName);

        StrictMode.ThreadPolicy originalPolicy = StrictMode.getThreadPolicy();
        try {
            SpsPermissionHelper.ensurePermission(getContext());
            final StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
            StrictMode.setThreadPolicy(policy);

            boolean serviceSupported = isSupported();
            if (Scanlet.Method.IS_SUPPORTED.equals(method)) {
                bundle.putBoolean(Scanlet.IS_SUPPORTED_EXTRA, serviceSupported);
            } else {
                if (!serviceSupported) {
                    throw new SdkNotSupportedException("ScannerService is not supported");
                }
                handleCallMethod(method, appPackageName, extras, bundle);
            }
        } catch (SdkException e) {
            SLog.e(TAG, "call : SdkException - " + e.getMessage());
            Result.pack(bundle, e.getResult());
        } catch (BoundDeviceException e) {
            SLog.e(TAG, "call : BoundDeviceException - " + e.getMessage());
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.CONNECTION_ERROR, e.getMessage());
        } catch (Exception e) {
            SLog.e(TAG, "call : Exception - " + e.getMessage());
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        } finally {
            if (originalPolicy != null) {
                StrictMode.setThreadPolicy(originalPolicy);
            }
        }
        return bundle;
    }

    private void handleCallMethod(@NonNull String method, String appPackageName, Bundle extras, Bundle bundle) throws Exception {
        String resultStr = "";
        final String TEMP_FOLDER = ".tmp";

        ScanAttributes.TransmissionMode transmissionMode =
                extras != null && extras.containsKey(Scanlet.Keys.KEY_TRANSMISSION_MODE) ?
                        (ScanAttributes.TransmissionMode) extras.getSerializable(Scanlet.Keys.KEY_TRANSMISSION_MODE) :
                        ScanAttributes.TransmissionMode.JOB;

        int clientVersion = (extras != null && extras.containsKey(Scanlet.Keys.KEY_CLIENT_VERSION))
                ? extras.getInt(Scanlet.Keys.KEY_CLIENT_VERSION)
                : Sdk.VERSION.LEVEL;

        switch (method) {
            case Scanlet.Method.GET_CAPS:
                resultStr = ScanOptionProfileAdapter.getCapabilities(appPackageName, scanDeviceService, transmissionMode, ScanConstants.DEFAULT_SCAN_DESTINATION, clientVersion);
                break;
            case Scanlet.Method.GET_DEFAULTS:
                resultStr = ScanDefaultOptionAdapter.getDefaults(appPackageName, scanDeviceService, ScanConstants.DEFAULT_SCAN_DESTINATION, clientVersion);
                break;
            case Scanlet.Method.GET_FILE_OPTIONS_CAPS:
                resultStr = handleGetFileOptionsCaps(extras, appPackageName);
                break;
            case Scanlet.Method.GET_STATUS:
                resultStr = ScanDeviceStatusAdapter.getStatus(scanDeviceService);
                break;
            case Scanlet.Method.GET_FILE_REQ:
                resultStr = handleGetFileReq(extras, bundle, TEMP_FOLDER);
                break;
            case Scanlet.Method.PUT_FILE_REQ:
                resultStr = handlePutFileReq(extras, bundle, TEMP_FOLDER);
                break;
            default:
                throw new SdkInvalidParamException("Method " + method + " is not supported");
        }
        Result.pack(bundle, Result.RESULT_OK);
        bundle.putString(Result.KEY_RESULT, resultStr);
    }

    private String handleGetFileOptionsCaps(@NonNull Bundle extras, String appPackageName) throws SdkException {
        ScanAttributes.ColorMode colorMode = extras != null && extras.containsKey(Scanlet.Keys.KEY_COLOR_MODE) ?
                (ScanAttributes.ColorMode) extras.getSerializable(Scanlet.Keys.KEY_COLOR_MODE) : null;
        ScanAttributes.DocumentFormat documentFormat = extras != null && extras.containsKey(Scanlet.Keys.KEY_DOCUMENT_FORMAT) ?
                (ScanAttributes.DocumentFormat) extras.getSerializable(Scanlet.Keys.KEY_DOCUMENT_FORMAT) : null;
        return ScanFileOptionAdapter.getFileOptions(appPackageName, scanDeviceService, colorMode, documentFormat);
    }

    private String handleGetFileReq(@NonNull Bundle extras, Bundle bundle, String tempFolder) {
        String jobId = extras.getString(Scanlet.Param.KEY_JOB_ID);
        File baseFile = new File(getContext().getFilesDir().getAbsolutePath() + "/" + tempFolder, jobId);
        if (baseFile.exists()) {
            bundle.putString(Scanlet.Param.KEY_FILE_URI, getContext().getFilesDir().getAbsolutePath());
            return "";
        }
        return "";
    }

    private String handlePutFileReq(@NonNull Bundle extras, Bundle bundle, String tempFolder) {
        String jobId = extras.getString(Scanlet.Param.KEY_JOB_ID);
        String packageName = extras.getString(Scanlet.Param.PACKAGE_NAME);
        File baseFolder = new File(getContext().getFilesDir().getAbsolutePath() + "/" + tempFolder, jobId);

        if (extras.containsKey(Scanlet.Param.KEY_FILE_URIS)) {
            ArrayList<String> fileUris = extras.getStringArrayList(Scanlet.Param.KEY_FILE_URIS);
            final String key = JobService.getRid(getContext(), jobId);
            if (JobDataContentProvider.contains(getContext().getContentResolver(), key)) {
                final JobData jobData = JobDataContentProvider.get(getContext().getContentResolver(), key);
                if (jobData.mDeviceType == JobInfo.JobType.SCAN) {
                    jobData.mFileNames = fileUris;
                    JobDataContentProvider.put(getContext().getContentResolver(), key, jobId, jobData);
                    SLog.d(TAG, "Success to update " + fileUris.size());
                }
            } else {
                SLog.d(TAG, "Fail to retrieve data ");
            }
            JobDataContentProvider.setIsPutData(false);
            JobDataContentProvider.remove(getContext().getContentResolver(), key);
        } else {
            SLog.d(TAG, "Fail to update ");
        }

        if (baseFolder.exists()) {
            try {
                File[] tmpFiles = baseFolder.listFiles();
                for (File tmpFile : tmpFiles) {
                    getContext().revokeUriPermission(Uri.parse(tmpFile.getAbsolutePath()),
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    if (tmpFile.isFile() && tmpFile.delete()) {
                        SLog.d(TAG, "File is removed");
                        bundle.putString(Scanlet.Param.KEY_FILE_URI, getContext().getFilesDir().getAbsolutePath());
                        return "";
                    }
                }
            } catch (Exception e) {
                SLog.e(TAG, "Failed to clean folder", e);
            }
        }
        return "";
    }

    private boolean isSupported() throws SdkConnectionErrorException {
        SLog.d(TAG, "isSupported:  ENTER");
        final PrinterInfo pi = SelectedPrinterHelper.get(getContext().getContentResolver());
        if (PrinterInfo.isEmpty(pi)) {
            throw new SdkConnectionErrorException("Device is not connected, PrinterInfo is empty");
        }

        boolean isSupported = (pi.getCapabilities() & PrinterInfo.Capability.CAPABILITY_SCAN) != 0;
        if (isSupported) {
            if (scanDeviceService == null) {
                SLog.i(TAG, "scanDeviceService not available");
                isSupported = false;
            } else {
                isSupported = scanDeviceService.isSupported();
            }
        }
        SLog.d(TAG, "isSupported: EXIT - ScanService " + (isSupported ? "is supported" : "is not supported"));
        return isSupported;
    }
}
