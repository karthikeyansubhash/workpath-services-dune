// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.storagelet.provider;

import androidx.annotation.NonNull;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.os.StrictMode;
import android.text.TextUtils;

import androidx.annotation.GuardedBy;

import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.massstorage.CustomerDataFile;
import com.hp.jetadvantage.link.api.massstorage.MassStorageInfo;
import com.hp.jetadvantage.link.api.massstorage.MassStorageService;
import com.hp.jetadvantage.link.api.massstorage.MassStoragelet;
import com.hp.jetadvantage.link.common.Platform;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.helper.SelectedPrinterHelper;
import com.hp.jetadvantage.link.common.model.ApiType;
import com.hp.jetadvantage.link.common.model.PrinterInfo;
import com.hp.jetadvantage.link.common.utils.FileSocketUtility;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.common.exception.SdkConnectionErrorException;
import com.hp.jetadvantage.link.services.common.exception.SdkException;
import com.hp.jetadvantage.link.services.common.exception.SdkInvalidParamException;
import com.hp.jetadvantage.link.services.common.exception.SdkNotSupportedException;
import com.hp.jetadvantage.link.services.common.exception.SdkServiceErrorException;
import com.hp.jetadvantage.link.services.common.exception.SdkUnauthorizedException;
import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;
import com.hp.jetadvantage.link.services.storagelet.HDDStorage;
import com.hp.jetadvantage.link.services.storagelet.IStorage;
import com.hp.jetadvantage.link.services.storagelet.StorageFactory;
import com.hp.jetadvantage.link.services.storagelet.adapter.USBStorage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main ConfigLet provider. It's responsible for serve base {@link MassStorageService} operations via
 * {@link #call(String, String, Bundle)} method.<br>
 */
public final class StorageLetContentProvider extends ContentProvider {
    private static String model = null;
    private static final String TNT = "nanotesla";
    private static final String TRON = "tron";

    private static final String TAG = MassStoragelet.TAG;

    private static final UriMatcher S_URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.sec.MassStoragelet";

    private static final int STORAGE_LET_CODE = 1;

    private static final String DEFAULT_PATH_DELIMITER = "/";

    static {
        S_URI_MATCHER.addURI(MassStoragelet.AUTHORITY, MassStoragelet.DIR_PATH_SEGMENT, STORAGE_LET_CODE);
    }

    private PathStrategy mStrategy;

    @GuardedBy("sCache")
    private static HashMap<String, PathStrategy> sCache = new HashMap<String, PathStrategy>();

    public static Uri getUriForFile(Context context, String authority, File file) {
        final PathStrategy strategy = getPathStrategy(context, authority);
        return strategy.getUriForFile(file);
    }

    private static PathStrategy getPathStrategy(Context context, String authority) {
        PathStrategy strat;
        synchronized (sCache) {
            strat = sCache.get(authority);
            if (strat == null) {
                try {
                    strat = parsePathStrategy(context, authority);
                } catch (Exception e) {
                    SLog.e(TAG, e.getMessage());
                }
                sCache.put(authority, strat);
            }
        }
        return strat;
    }

    private static PathStrategy parsePathStrategy(Context context, String authority)
            throws Exception {
        final SimplePathStrategy strat = new SimplePathStrategy(authority);

        File hddTarget = new File(HDDStorage.ROOT_MOUNT_LOCATION);
        File usbTarget = new File(USBStorage.ROOT_MOUNT_LOCATION);
        strat.addRoot(MassStorageInfo.StorageType.HDD.toString().toLowerCase(), hddTarget);
        strat.addRoot(MassStorageInfo.StorageType.USB.toString().toLowerCase(), usbTarget);

        return strat;
    }

    /**
     * Default constructor
     */
    public StorageLetContentProvider() {
    }

    private static int modeToMode(String mode) {
        int modeBits;
        if ("r".equals(mode)) {
            modeBits = ParcelFileDescriptor.MODE_READ_ONLY;
        } else if ("w".equals(mode) || "wt".equals(mode)) {
            modeBits = ParcelFileDescriptor.MODE_WRITE_ONLY
                    | ParcelFileDescriptor.MODE_CREATE
                    | ParcelFileDescriptor.MODE_TRUNCATE;
        } else if ("wa".equals(mode)) {
            modeBits = ParcelFileDescriptor.MODE_WRITE_ONLY
                    | ParcelFileDescriptor.MODE_CREATE
                    | ParcelFileDescriptor.MODE_APPEND;
        } else if ("rw".equals(mode)) {
            modeBits = ParcelFileDescriptor.MODE_READ_WRITE
                    | ParcelFileDescriptor.MODE_CREATE;
        } else if ("rwt".equals(mode)) {
            modeBits = ParcelFileDescriptor.MODE_READ_WRITE
                    | ParcelFileDescriptor.MODE_CREATE
                    | ParcelFileDescriptor.MODE_TRUNCATE;
        } else {
            throw new IllegalArgumentException("Invalid mode: " + mode);
        }
        return modeBits;
    }

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        // ContentProvider has already checked granted permissions
        final File file = mStrategy.getFileForUri(uri);
        SLog.w(TAG, "file mode : " + mode);
        final int fileMode = modeToMode(mode);
        return ParcelFileDescriptor.open(file, fileMode);
    }

    @Override
    public void attachInfo(Context context, ProviderInfo info) {
        super.attachInfo(context, info);

        // Sanity check our security
//        if (info.exported) {
//            throw new SecurityException("Provider must not be exported");
//        }
        if (!info.grantUriPermissions) {
            throw new SecurityException("Provider must grant uri permissions");
        }
        mStrategy = getPathStrategy(context, info.authority);
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
            case STORAGE_LET_CODE:
                SLog.d(MassStoragelet.TAG, " in STORAGE_LET_CODE ");
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
            SLog.v(MassStoragelet.TAG, "call " + method);
            SpsPermissionHelper.ensurePermission(getContext());

            final PrinterInfo pi = SelectedPrinterHelper.get(getContext().getContentResolver());

            if (PrinterInfo.isEmpty(pi)) {
                SLog.e(TAG, "Device is not connected");
                throw new SdkConnectionErrorException("Device is not connected");
            }

            final StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
            StrictMode.setThreadPolicy(policy);

            extras.setClassLoader(MassStorageInfo.class.getClassLoader());

            int clientVersion = extras != null && extras.containsKey(MassStoragelet.Keys.KEY_CLIENT_VERSION) ?
                    extras.getInt(MassStoragelet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION_LEVEL.ONE) : Sdk.VERSION_LEVEL.ONE;
            // it is always true on the sdk-dune repo.
            boolean serviceSupported = true; //isSupported(pi, clientVersion);


            if (MassStoragelet.Method.IS_SUPPORTED.equals(method)) {
                bundle.putBoolean(MassStoragelet.IS_SUPPORTED_EXTRA, serviceSupported);
            } else {
                if (!serviceSupported) {
                    throw new SdkNotSupportedException("MassStorageService helper is not supported");
                }

                String pkgName = extras.getString(MassStoragelet.Keys.PACKAGE_NAME);
                if (TextUtils.isEmpty(pkgName)) {
                    throw new SdkInvalidParamException("Package name is empty");
                }
                SLog.v(TAG, "method: " + method + " pkgName: " + pkgName);

                //[[Permission check
                String callingPkg = this.getCallingPackage();
                if (TextUtils.isEmpty(callingPkg)
                        || (!pkgName.equalsIgnoreCase(callingPkg) && !Sdk.SERVICES_PACKAGE.equalsIgnoreCase(callingPkg))) {
                    throw new SdkUnauthorizedException("Service is not allowed for " + pkgName);
                }
                //]]
                SLog.v(TAG, "calling pkg : " + callingPkg);

                CustomerDataFile customerDataFile = null;
                switch (method) {
                    case MassStoragelet.Method.GET_STORAGE_LIST:
                        return getStorageList(bundle, clientVersion);
                    case MassStoragelet.Method.GET_STORAGE:
                        MassStorageInfo.StorageType storageType =
                                (MassStorageInfo.StorageType) extras.getSerializable(MassStoragelet.Keys.KEY_STORAGE_TYPE);
                        return getStorage(storageType, bundle);
                    case MassStoragelet.Method.CREATE_NEW_FILE:
                        customerDataFile = extras.getParcelable(MassStoragelet.Keys.KEY_FILE_TYPE);
                        return createNewFile(getContext(), customerDataFile, bundle);
                    case MassStoragelet.Method.CREATE_NEW_DIRECTORY:
                        customerDataFile = extras.getParcelable(MassStoragelet.Keys.KEY_FILE_TYPE);
                        return mkdir(getContext(), customerDataFile, bundle);
                    case MassStoragelet.Method.DELETE_FILE:
                        customerDataFile = extras.getParcelable(MassStoragelet.Keys.KEY_FILE_TYPE);
                        return delete(getContext(), customerDataFile, bundle);
                    case MassStoragelet.Method.LIST_FILES:
                        customerDataFile = extras.getParcelable(MassStoragelet.Keys.KEY_FILE_TYPE);
                        return listFiles(getContext(), customerDataFile, bundle);
                    case MassStoragelet.Method.IS_FILE:
                        customerDataFile = extras.getParcelable(MassStoragelet.Keys.KEY_FILE_TYPE);
                        return isFile(getContext(), customerDataFile, bundle);
                    case MassStoragelet.Method.IS_DIRECTORY:
                        customerDataFile = extras.getParcelable(MassStoragelet.Keys.KEY_FILE_TYPE);
                        return isDirectory(getContext(), customerDataFile, bundle);
                    case MassStoragelet.Method.FILE_EXISTS:
                        customerDataFile = extras.getParcelable(MassStoragelet.Keys.KEY_FILE_TYPE);
                        return exists(getContext(), customerDataFile, bundle);
                    case MassStoragelet.Method.FILE_LAST_MODIFIED:
                        customerDataFile = extras.getParcelable(MassStoragelet.Keys.KEY_FILE_TYPE);
                        return getFileLastModified(getContext(), customerDataFile, bundle);
                    case MassStoragelet.Method.FILE_LENGTH:
                        customerDataFile = extras.getParcelable(MassStoragelet.Keys.KEY_FILE_TYPE);
                        return getFileLength(getContext(), customerDataFile, bundle);
                    case MassStoragelet.Method.FILE_RENAME:
                        customerDataFile = extras.getParcelable(MassStoragelet.Keys.KEY_FILE_TYPE);
                        CustomerDataFile destCustomerDataFile = extras.getParcelable(MassStoragelet.Keys.KEY_DEST_FILE_TYPE);
                        return moveFile(getContext(), customerDataFile, destCustomerDataFile, bundle);

                    case MassStoragelet.Method.GET_FILE_URI:
                        customerDataFile = extras.getParcelable(MassStoragelet.Keys.KEY_FILE_TYPE);
                        return getUriForFile(getContext(), customerDataFile, bundle);
                    case MassStoragelet.Method.GET_FILE_SOCKET:
                        customerDataFile = extras.getParcelable(MassStoragelet.Keys.KEY_FILE_TYPE);
                        return getSocketForFile(getContext(), customerDataFile, bundle);
                    default:
                        throw new SdkInvalidParamException("Method " + method + " is not supported");
                }
            }
        } catch (SdkException e) {
            Result.pack(bundle, e.getResult());
        } catch (Exception e) {
            SLog.e(TAG, e.getMessage(), e);
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        } finally {
            if (originalPolicy != null) {
                StrictMode.setThreadPolicy(originalPolicy);
            }
        }

        return bundle;
    }

    private Bundle getStorageList(final Bundle bundle, int clientVersion) {
        try {
            ArrayList<MassStorageInfo> storageList = new ArrayList<>();
            MassStorageInfo.StorageType storageType = null;
            if (clientVersion < Sdk.VERSION_LEVEL.SIX ) {
                storageType = MassStorageInfo.StorageType.USB;
            }

            for (IStorage storage : StorageFactory.INSTANCE.getStorageList(storageType)) {
                if (storage.getInfo().isMounted()) {
                    storageList.add(storage.getInfo());
                }
            }


            bundle.putParcelableArrayList(Result.KEY_RESULT, storageList);
            Result.pack(bundle, Result.RESULT_OK);

        } catch (Throwable t) {
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, t.getMessage());
        }

        return bundle;
    }

    private Bundle getStorage(final MassStorageInfo.StorageType storageType, final Bundle bundle) {
        try {
            List<IStorage> storageList = StorageFactory.INSTANCE.getStorageList(storageType);
            if (!storageList.isEmpty()) {
                bundle.putParcelable(Result.KEY_RESULT, storageList.get(0).getInfo());
                Result.pack(bundle, Result.RESULT_OK);
            } else {
                Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "MassStorageService of type " + storageType + " not found");
            }
        } catch (Throwable t) {
            SLog.e(TAG, "getStorage() error: " + t.getMessage());
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, t.getMessage());
        }

        return bundle;
    }

    private IStorage getStorage(final CustomerDataFile customerDataFile) throws Exception {
        if (customerDataFile != null && !TextUtils.isEmpty(customerDataFile.getPath())) {
            return StorageFactory.INSTANCE.getStorageForPath(customerDataFile.getStorageInfo().getType(), customerDataFile.getStorageInfo().getExternalFileDirectory() + DEFAULT_PATH_DELIMITER + customerDataFile.getPath());
        }
        throw new SdkServiceErrorException("Failed to retrieve storage");
    }

    private boolean isSupported(final PrinterInfo pi, int clientVersion) {
        boolean isSupported = false;

        if (Platform.isPanel()) {
            if (pi.getApiType() == ApiType.OXP) {
                isSupported = true;
            }
        }

        return isSupported;
    }

    private Bundle exists(final Context context, final CustomerDataFile customerDataFile, final Bundle bundle) {
        String callingPkg = this.getCallingPackage();
        if (TextUtils.isEmpty(callingPkg)) {
            SLog.w(TAG, "exists method error, Invalid access");
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "exists method error, Invalid access");
            return bundle;
        }

        try {
            IStorage storage = getStorage(customerDataFile);
            boolean result = storage.exists(context, customerDataFile.getPath(), customerDataFile);
            bundle.putBoolean(Result.KEY_RESULT, result);
            Result.pack(bundle, Result.RESULT_OK);
            return bundle;
        } catch (Throwable t) {
            SLog.e(TAG, "error in exists: " + t.getMessage());
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, t.getMessage());
        }

        return bundle;
    }

    private Bundle createNewFile(final Context context, final CustomerDataFile customerDataFile, final Bundle bundle) {
        String callingPkg = this.getCallingPackage();
        if (TextUtils.isEmpty(callingPkg)) {
            SLog.w(TAG, "createNewFile method error, Invalid access");
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "createNewFile method error, Invalid access");
            return bundle;
        }

        try {
            IStorage storage = getStorage(customerDataFile);
            boolean result = storage.createNewFile(context, customerDataFile);
            bundle.putBoolean(Result.KEY_RESULT, result);
            Result.pack(bundle, Result.RESULT_OK);
            return bundle;
        } catch (Throwable t) {
            SLog.e(TAG, "error in createNewFile: " + t.getMessage());
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, t.getMessage());
        }

        return bundle;
    }

    private Bundle mkdir(final Context context, final CustomerDataFile customerDataFile, final Bundle bundle) {
        String callingPkg = this.getCallingPackage();
        if (TextUtils.isEmpty(callingPkg)) {
            SLog.w(TAG, "mkdir method error, Invalid access");
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "mkdir method error, Invalid access");
            return bundle;
        }

        try {
            IStorage storage = getStorage(customerDataFile);
            boolean result = storage.mkdir(context, customerDataFile);
            bundle.putBoolean(Result.KEY_RESULT, result);
            Result.pack(bundle, Result.RESULT_OK);
            return bundle;
        } catch (Throwable t) {
            SLog.e(TAG, "error in mkdir: " + t.getMessage());
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, t.getMessage());
        }

        return bundle;
    }

    private Bundle isFile(final Context context, final CustomerDataFile customerDataFile, final Bundle bundle) {
        String callingPkg = this.getCallingPackage();
        if (TextUtils.isEmpty(callingPkg)) {
            SLog.w(TAG, "isFile method error, Invalid access");
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "isFile method error, Invalid access");
            return bundle;
        }

        try {
            IStorage storage = getStorage(customerDataFile);
            boolean result = storage.isFile(context, customerDataFile);
            bundle.putBoolean(Result.KEY_RESULT, result);
            Result.pack(bundle, Result.RESULT_OK);
            return bundle;
        } catch (Throwable t) {
            SLog.e(TAG, "error in isFile: " + t.getMessage());
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, t.getMessage());
        }

        return bundle;
    }

    private Bundle isDirectory(final Context context, final CustomerDataFile customerDataFile, final Bundle bundle) {
        String callingPkg = this.getCallingPackage();
        if (TextUtils.isEmpty(callingPkg)) {
            SLog.w(TAG, "isDirectory method error, Invalid access");
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "isDirectory method error, Invalid access");
            return bundle;
        }

        try {
            IStorage storage = getStorage(customerDataFile);
            boolean result = storage.isDirectory(context, customerDataFile);
            bundle.putBoolean(Result.KEY_RESULT, result);
            Result.pack(bundle, Result.RESULT_OK);
            return bundle;
        } catch (Throwable t) {
            SLog.e(TAG, "error in isDirectory: " + t.getMessage());
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, t.getMessage());
        }

        return bundle;
    }

    private Bundle delete(final Context context, final CustomerDataFile customerDataFile, final Bundle bundle) {
        String callingPkg = this.getCallingPackage();
        if (TextUtils.isEmpty(callingPkg)) {
            SLog.w(TAG, "deleteFile method error, Invalid access");
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "deleteFile method error, Invalid access");
            return bundle;
        }

        try {
            IStorage storage = getStorage(customerDataFile);
            boolean result = storage.delete(context, customerDataFile);
            bundle.putBoolean(Result.KEY_RESULT, result);
            Result.pack(bundle, Result.RESULT_OK);
            return bundle;
        } catch (Throwable t) {
            SLog.e(TAG, "error in delete: " + t.getMessage());
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, t.getMessage());
        }

        return bundle;
    }

    private Bundle getFileLength(final Context context, final CustomerDataFile customerDataFile, final Bundle bundle) {
        String callingPkg = this.getCallingPackage();
        if (TextUtils.isEmpty(callingPkg)) {
            SLog.w(TAG, "getFileLength method error, Invalid access");
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "getFileLength method error, Invalid access");
            return bundle;
        }

        try {
            IStorage storage = getStorage(customerDataFile);
            long result = storage.length(context, customerDataFile);
            bundle.putLong(Result.KEY_RESULT, result);
            Result.pack(bundle, Result.RESULT_OK);
            return bundle;
        } catch (Throwable t) {
            SLog.e(TAG, "error in getFileLength: " + t.getMessage());
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, t.getMessage());
        }

        return bundle;
    }

    private Bundle getFileLastModified(final Context context, final CustomerDataFile customerDataFile, final Bundle bundle) {
        String callingPkg = this.getCallingPackage();
        if (TextUtils.isEmpty(callingPkg)) {
            SLog.w(TAG, "getFileLastModified method error, Invalid access");
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "getFileLastModified method error, Invalid access");
            return bundle;
        }

        try {
            IStorage storage = getStorage(customerDataFile);
            long result = storage.lastModified(context, customerDataFile);
            bundle.putLong(Result.KEY_RESULT, result);
            Result.pack(bundle, Result.RESULT_OK);
            return bundle;
        } catch (Throwable t) {
            SLog.e(TAG, "error in getFileLastModified: " + t.getMessage());
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, t.getMessage());
        }

        return bundle;
    }

    private Bundle listFiles(final Context context, final CustomerDataFile customerDataFile, final Bundle bundle) {
        String callingPkg = this.getCallingPackage();
        if (TextUtils.isEmpty(callingPkg)) {
            SLog.w(TAG, "listFiles method error, Invalid access");
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "listFiles method error, Invalid access");
            return bundle;
        }

        try {
            File file = null;
            String rootPath = "";
            if (customerDataFile.getStorageInfo().getType() == MassStorageInfo.StorageType.HDD) {
                rootPath = HDDStorage.ROOT_MOUNT_LOCATION + DEFAULT_PATH_DELIMITER + callingPkg;

                File rootFilePath = new File(rootPath);
                if (!rootFilePath.exists()) rootFilePath.mkdirs();

                file = new File(rootPath + DEFAULT_PATH_DELIMITER + customerDataFile.getPath());
            } else if (customerDataFile.getStorageInfo().getType() == MassStorageInfo.StorageType.USB) {
                rootPath = customerDataFile.getStorageInfo().getExternalFileDirectory();
                file = new File(rootPath + DEFAULT_PATH_DELIMITER + customerDataFile.getPath());
            }

            if (file == null) {
                SLog.w(TAG, "listFiles method error, failed to retrieve list");
                Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "listFiles method error, failed to retrieve list");
                return bundle;
            }

            File[] listFiles = file.listFiles();
            SLog.d(TAG, "Retrieved file list from original:" + listFiles.length);

            // JEDI-70740 : More time to load the USB contents in JEDI Printers with FW 5.6.0.2 for Workpath solution
            // TNT model uses FTP protocol for getting file attribute(CurlFtpFS).
            // Framework has changed cache policy to no-cache after 25.5.0.3 F/W because refreshing file list issue.
            // isDirectory or isFile operation calls FTP command in the CurlFtpFS at that time because no-cache policy.
            // That operation spend time about 0.2 sec.
            // If there are many file, performance will be slow down.
            // Therefore TNT model just only sort by file name.
            if (TNT.equals(getModelName())) {
                Arrays.sort(listFiles, new Comparator<File>() {
                    public int compare(File f1, File f2) {
                        return f1.compareTo(f2);
                    }
                });
            } else {
                Arrays.sort(listFiles, new Comparator<File>() {
                    public int compare(File f1, File f2) {
                        if (f1.isDirectory() && !f2.isDirectory()) {
                            return -1;
                        } else if (!f1.isDirectory() && f2.isDirectory()) {
                            return 1;
                        } else {
                            return f1.compareTo(f2);
                        }
                    }
                });
            }

            final ArrayList<String> listInfo = new ArrayList<>();
            for (File temp : listFiles) {
                listInfo.add(temp.getAbsolutePath().substring(rootPath.length()));
            }
            SLog.d(TAG, "Retrieved file list:" + listInfo.size());
            Result.pack(bundle, Result.RESULT_OK);
            bundle.putStringArrayList(Result.KEY_RESULT, listInfo);
        } catch (Throwable throwable) {
            SLog.e(TAG, "Failed to retrieve file list:" + throwable.getMessage());
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, throwable.getMessage());
        }

        return bundle;
    }

    private Bundle moveFile(final Context context, final CustomerDataFile customerDataFile, final CustomerDataFile destCustomerDataFile, final Bundle bundle) {
        String callingPkg = this.getCallingPackage();
        if (TextUtils.isEmpty(callingPkg)) {
            SLog.w(TAG, "moveFile method error, Invalid access");
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "moveFile method error, Invalid access");
            return bundle;
        }

        try {
            IStorage storage = getStorage(customerDataFile);
            boolean result = storage.move(context, customerDataFile, destCustomerDataFile);
            bundle.putBoolean(Result.KEY_RESULT, result);
            Result.pack(bundle, Result.RESULT_OK);
            return bundle;
        } catch (Throwable throwable) {
            SLog.e(TAG, "Failed to move :" + throwable.getMessage());
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, throwable.getMessage());
        }

        return bundle;
    }

    private Bundle getUriForFile(final Context context, final CustomerDataFile customerDataFile, final Bundle bundle) {
        String callingPkg = this.getCallingPackage();
        if (TextUtils.isEmpty(callingPkg)) {
            SLog.w(TAG, "getUriForFile method error, Invalid access");
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "getUriForFile method error, Invalid access");
            return bundle;
        }

        try {
            List<IStorage> storageList = StorageFactory.INSTANCE.getStorageList(customerDataFile.getStorageInfo().getType());
            if (!storageList.isEmpty() && storageList.size() > 0) {
                File file = null;
                long availableSize = Long.MAX_VALUE;
                if (customerDataFile.getStorageInfo().getType() == MassStorageInfo.StorageType.HDD) {
                    file = new File(new File(HDDStorage.ROOT_MOUNT_LOCATION + DEFAULT_PATH_DELIMITER + callingPkg), customerDataFile.getPath());
                    availableSize = storageList.get(0).getInfo().getFreeSpace();
                } else {
                    file = new File(new File(customerDataFile.getStorageInfo().getExternalFileDirectory()), customerDataFile.getPath());
                }
                SLog.w(TAG, "getUriForFile : " + file.getName());
                file.setWritable(true, false);

                Uri fileUri = getUriForFile(getContext(), MassStoragelet.AUTHORITY, file);
                context.grantUriPermission(callingPkg, fileUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                SLog.w(TAG, "granted permission");

                bundle.putString(MassStoragelet.Keys.KEY_FILE_URI, fileUri.toString());
                bundle.putLong(MassStoragelet.Keys.KEY_AVAILABLE_SIZE, availableSize);
                Result.pack(bundle, Result.RESULT_OK);
            } else {
                Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "MassStorageService of type " + customerDataFile.getStorageInfo().getType() + " not found");
            }
        } catch (Throwable t) {
            SLog.e(TAG, t.getMessage(), t);
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, t.getMessage());
        }

        return bundle;
    }

    private Bundle getSocketForFile(final Context context, final CustomerDataFile customerDataFile, final Bundle bundle) {
        String callingPkg = this.getCallingPackage();
        if (TextUtils.isEmpty(callingPkg)) {
            SLog.w(TAG, "getUriForFile method error, Invalid access");
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "getUriForFile method error, Invalid access");
            return bundle;
        }

        try {
            List<IStorage> storageList = StorageFactory.INSTANCE.getStorageList(customerDataFile.getStorageInfo().getType());
            if (!storageList.isEmpty() && storageList.size() > 0) {
                File file = null;
                long availableSize = Long.MAX_VALUE;
                if (customerDataFile.getStorageInfo().getType() == MassStorageInfo.StorageType.HDD) {
                    file = new File(new File(HDDStorage.ROOT_MOUNT_LOCATION + DEFAULT_PATH_DELIMITER + callingPkg), customerDataFile.getPath());
                    availableSize = storageList.get(0).getInfo().getFreeSpace();
                } else {
                    file = new File(new File(customerDataFile.getStorageInfo().getExternalFileDirectory()), customerDataFile.getPath());
                }
                SLog.w(TAG, "getSocketForFile : " + file.getName());
                file.setWritable(true, false);

                FileSocketUtility fsu = new FileSocketUtility();
                fsu.createServerSocket(file.getCanonicalPath());


                Uri fileUri = getUriForFile(getContext(), MassStoragelet.AUTHORITY, file);
                context.grantUriPermission(callingPkg, fileUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                SLog.w(TAG, "granted permission");

                //bundle.putString(MassStoragelet.Keys.KEY_FILE_ADDR, fileUri.toString());
                bundle.putLong(MassStoragelet.Keys.KEY_AVAILABLE_SIZE, availableSize);
                Result.pack(bundle, Result.RESULT_OK);
                for (int cnt = 10; cnt > 0; cnt--) {
                    if (fsu.getPort() == 0) {
                        Thread.sleep(50);
                        continue;
                    } else {
                        break;
                    }
                }
                bundle.putString(MassStoragelet.Keys.KEY_FILE_ADDR, fsu.getIpAddress());
                bundle.putInt(MassStoragelet.Keys.KEY_FILE_PORT, fsu.getPort());
            } else {
                Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "MassStorageService of type " + customerDataFile.getStorageInfo().getType() + " not found");
            }
        } catch (Throwable t) {
            SLog.e(TAG, t.getMessage(), t);
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, t.getMessage());
        }

        return bundle;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        return null;
    }

    interface PathStrategy {
        /**
         * Return a {@link Uri} that represents the given {@link File}.
         */
        public Uri getUriForFile(File file);

        /**
         * Return a {@link File} that represents the given {@link Uri}.
         */
        public File getFileForUri(Uri uri);
    }

    /**
     * Strategy that provides access to files living under a narrow whitelist of
     * filesystem roots. It will throw {@link SecurityException} if callers try
     * accessing files outside the configured roots.
     * <p>
     * For example, if configured with
     * {@code addRoot("myfiles", context.getFilesDir())}, then
     * {@code context.getFileStreamPath("foo.txt")} would map to
     * {@code content://myauthority/myfiles/foo.txt}.
     */
    static class SimplePathStrategy implements PathStrategy {
        private final String mAuthority;
        private final HashMap<String, File> mRoots = new HashMap<String, File>();

        public SimplePathStrategy(String authority) {
            mAuthority = authority;
        }

        /**
         * Add a mapping from a name to a filesystem root. The provider only offers
         * access to files that live under configured roots.
         */
        public void addRoot(String name, File root) {
            if (TextUtils.isEmpty(name)) {
                throw new IllegalArgumentException("Name must not be empty");
            }

            try {
                // Resolve to canonical path to keep path checking fast
                root = root.getCanonicalFile();
            } catch (IOException e) {
                throw new IllegalArgumentException(
                        "Failed to resolve canonical path for " + root, e);
            }

            mRoots.put(name, root);
        }

        @Override
        public Uri getUriForFile(File file) {
            String path;
            try {
                path = file.getCanonicalPath();
            } catch (IOException e) {
                throw new IllegalArgumentException("Failed to resolve canonical path for " + file);
            }
            // Find the most-specific root path
            Map.Entry<String, File> mostSpecific = null;
            for (Map.Entry<String, File> root : mRoots.entrySet()) {
                final String rootPath = root.getValue().getPath();

                SLog.w(TAG, "path : " + path.length());
                if (path.startsWith(rootPath) && (mostSpecific == null
                        || rootPath.length() > mostSpecific.getValue().getPath().length())) {
                    mostSpecific = root;
                }
            }

            if (mostSpecific == null) {
                throw new IllegalArgumentException(
                        "Failed to find configured root that contains " + path);
            }

            // Start at first char of path under root
            final String rootPath = mostSpecific.getValue().getPath();
            if (rootPath.endsWith("/")) {
                path = path.substring(rootPath.length());
            } else {
                path = path.substring(rootPath.length() + 1);
            }

            // Encode the tag and path separately
            path = Uri.encode(mostSpecific.getKey()) + '/' + Uri.encode(path, "/");
            return new Uri.Builder().scheme("content")
                    .authority(mAuthority).encodedPath(path).build();
        }

        @Override
        public File getFileForUri(Uri uri) {
            String path = uri.getEncodedPath();

            final int splitIndex = path.indexOf('/', 1);
            final String tag = Uri.decode(path.substring(1, splitIndex));
            path = Uri.decode(path.substring(splitIndex + 1));

            final File root = mRoots.get(tag);
            if (root == null) {
                throw new IllegalArgumentException("Unable to find configured root for " + uri);
            }

            File file = new File(root, path);
            try {
                file = file.getCanonicalFile();
            } catch (IOException e) {
                throw new IllegalArgumentException("Failed to resolve canonical path for " + file);
            }

            if (!file.getPath().startsWith(root.getPath())) {
                throw new SecurityException("Resolved path jumped beyond configured root");
            }
            file.setWritable(true, false);

            return file;
        }
    }

    //Temp method by SH :P
    private String getModelName() {
        if (model != null) {
            return model;
        }
        Process process = null;
        String cmd[] = {"/system/bin/sh", "-c", "getprop | grep ro.arch"};
        BufferedReader reader = null;

        try {
            process = Runtime.getRuntime().exec(cmd);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String readline = reader.readLine();
            SLog.i(TAG, "model = " + readline);
            if (readline != null) {
                if (readline.contains(TRON)) {
                    model = TRON;
                } else if (readline.contains(TNT)) {
                    model = TNT;
                }
            } else {
                model = TNT;
            }

            reader.close();
            reader = null;
        } catch (Exception e) {
            SLog.e(TAG, "failed while reading a model name.");
        } finally {
            if (reader != null) {
                try { reader.close(); } catch (IOException ioe) {}
            }
        }
        return model;
    }
}
