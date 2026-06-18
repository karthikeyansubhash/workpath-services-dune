// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.storagelet;

import android.content.Context;
import android.text.TextUtils;

import com.hp.jetadvantage.link.api.massstorage.CustomerDataFile;
import com.hp.jetadvantage.link.api.massstorage.MassStorageInfo;
import com.hp.jetadvantage.link.api.massstorage.MassStoragelet;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.common.exception.SdkServiceErrorException;
import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HDDStorage implements IStorage {
    private static final String TAG = MassStoragelet.TAG + "/HDD";

    public static final String ROOT_MOUNT_LOCATION = "/data/workpath/Customer";
    private static final String SIZE_INFO_FILE_LOCATION = "/data/workpath/Customer/Temp/df_Customer";

    public static final String DEFAULT_PATH_DELIMITER = "/";

    private static final long SIXTY_FOUR_GIGA_BYTES = 64 * 1024 * 1024 * 1024;
    private static final long SIXTY_FOUR_BYTES = 64;
    private static final long DEFAULT_LIMITED_SIZE = SIXTY_FOUR_BYTES;

    private static final String STORAGE_NAME = "HDD";
    private static final float AVAILABLE_SIZE_RATIO = 0.8f;

    private File mMountPath;

    public static List<IStorage> getMountedStorages() {
        List<IStorage> resultList = new ArrayList<>();

        HDDStorage storage = new HDDStorage(new File(ROOT_MOUNT_LOCATION));
        if (storage.isMounted()) {
            resultList.add(storage);
        } else {
            SLog.w(MassStoragelet.TAG, "HDD mount point doesn't exist, HDD storage is not supported");
        }
        return resultList;
    }

    HDDStorage(final File mountPath) {
        mMountPath = mountPath;
    }

    @Override
    public MassStorageInfo.StorageType getStorageType() {
        return MassStorageInfo.StorageType.HDD;
    }

    @Override
    public String getName() {
        return mMountPath != null ? STORAGE_NAME : null;
    }

    @Override
    public MassStorageInfo getInfo() {
        if (mMountPath != null) {
            long[] size = getStorageSize();

            return new MassStorageInfo(getStorageType(), getName() + "(" + STORAGE_NAME + ")",
                    size[0], size[1], isMounted(),
                    DEFAULT_PATH_DELIMITER, MassStorageInfo.Protocol.LOCAL, STORAGE_NAME);

        } else {
            return new MassStorageInfo(getStorageType(), getName(),
                    0, 0, isMounted(),
                    null, MassStorageInfo.Protocol.LOCAL, "unknown");
        }
    }

    @Override
    public File getMappedLocation() {
        return new File(DEFAULT_PATH_DELIMITER);
    }

    private boolean isMounted() {
        return mMountPath != null ? mMountPath.exists() : false;
        /*long[] size = getStorageSize();
        if (size[0] >= DEFAULT_LIMITED_SIZE) {
            return mMountPath != null && mMountPath.exists();
        } else {
            return false;
        }*/
    }

    private long[] getStorageSize() {
        long[] size = new long[]{0L, 0L};
        File sizeInfoFile = new File(SIZE_INFO_FILE_LOCATION);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(sizeInfoFile));
            String strTotalSize = reader.readLine().trim();
            String strFreeSize = reader.readLine().trim();
            size[0] = Long.parseLong(strTotalSize);
            size[1] = Long.parseLong(strFreeSize);

            long remainingSpaceSize = (long) (size[0] * (1 - AVAILABLE_SIZE_RATIO));
            size[1] = (size[1] - remainingSpaceSize > 0) ? size[1] - remainingSpaceSize : 0L;

            SLog.i(MassStoragelet.TAG, "Read HDD size without error");
        } catch (Throwable throwable) {
            SLog.w(MassStoragelet.TAG, "Read HDD info file error : " + throwable.getMessage());
            try {
                SLog.i(MassStoragelet.TAG, "Try to get real size");

                File hddPath = new File(ROOT_MOUNT_LOCATION);
                size = new long[]{hddPath.getTotalSpace(), hddPath.getFreeSpace()};
            } catch (Throwable th) {
                SLog.w(MassStoragelet.TAG, "Failed to read real size");
                size = new long[]{0L, 0L};
            }
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }

        return size;
    }

    @Override
    public List<File> list(Context context, String path) throws Exception {
        checkMountState(context);

        String callingPkg = SpsPermissionHelper.getCallingPackage(context);
        if (TextUtils.isEmpty(callingPkg)) {
            SLog.w(TAG, "list method error(hdd), Invalid access");
            return null;
        }

        File pathLocation = new File(mMountPath + DEFAULT_PATH_DELIMITER + callingPkg, path);
        if (!isValidatePath(callingPkg, pathLocation.getCanonicalPath())) {
            SLog.w(TAG, "Invalid path");
            return null;
        }
        return new ArrayList<>(Arrays.asList(pathLocation.listFiles()));
    }

    @Override
    public boolean exists(Context context, String path, CustomerDataFile customerDataFile) throws Exception {
        if (context == null || TextUtils.isEmpty(path) || customerDataFile == null) {
            SLog.w(TAG, "exists method error(hdd), Invalid parameter");
            return false;
        }

        checkMountState(context);

        String callingPkg = SpsPermissionHelper.getCallingPackage(context);
        if (TextUtils.isEmpty(callingPkg)) {
            SLog.w(TAG, "exists method error(hdd), Invalid access");
            return false;
        }

        if (path.equalsIgnoreCase(customerDataFile.getPath())) {
            File file = new File(mMountPath + DEFAULT_PATH_DELIMITER + callingPkg, path);
            SLog.d(TAG, "exists method (hdd), try to check file exists from path");
            if (!isValidatePath(callingPkg, file.getCanonicalPath())) {
                SLog.w(TAG, "Invalid path");
                return false;
            }
            return file.exists();
        }

        SLog.i(TAG, "exists method error(hdd), Invalid path access");
        return false;
    }

    @Override
    public boolean createNewFile(Context context, CustomerDataFile customerDataFile) throws Exception {
        if (context == null || customerDataFile == null) {
            SLog.w(TAG, "createNewFile method error(hdd), Invalid parameter");
            return false;
        }

        checkMountState(context);

        String callingPkg = SpsPermissionHelper.getCallingPackage(context);
        if (TextUtils.isEmpty(callingPkg)) {
            SLog.w(TAG, "createNewFile method error(hdd), Invalid access");
            return false;
        }

        File file = new File(mMountPath + DEFAULT_PATH_DELIMITER + callingPkg, customerDataFile.getPath());
        if (!isValidatePath(callingPkg, file.getCanonicalPath())) {
            SLog.w(TAG, "Invalid path");
            throw new IOException("Invalid file path");
        }

        if (file.exists()) {
            SLog.d(TAG, file.getName() + " is already exist.");
            return false;
        }

        boolean result = false;
        OutputStream os = null;
        FileOutputStream fio = null;
        try {
            fio = new FileOutputStream(file);
            os = new BufferedOutputStream(fio);
            os.flush();
            result = true;
        } catch (Throwable throwable) {
            SLog.e(TAG, "Failed to create new file(hdd) " + throwable.getMessage());
            result = false;
        } finally {
            if (fio != null) {
                try {
                    fio.close();
                } catch (Exception e) {
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (Exception e) {
                }
            }
        }
        SLog.d(TAG, "Success to create new file(hdd)");
        file.setWritable(true, false);
        return result;
    }

    @Override
    public boolean mkdir(Context context, CustomerDataFile customerDataFile) throws Exception {
        if (context == null || customerDataFile == null) {
            SLog.w(TAG, "mkdir method error(hdd), Invalid parameter");
            return false;
        }

        checkMountState(context);

        String callingPkg = SpsPermissionHelper.getCallingPackage(context);
        if (TextUtils.isEmpty(callingPkg)) {
            SLog.w(TAG, "mkdir method error(hdd), Invalid access");
            return false;
        }

        File file = new File(mMountPath + DEFAULT_PATH_DELIMITER + callingPkg, customerDataFile.getPath());
        SLog.d(TAG, "Try to create directory(hdd)");
        if (!isValidatePath(callingPkg, file.getCanonicalPath())) {
            SLog.w(TAG, "Invalid path");
            return false;
        }
        return file.mkdir();
    }

    @Override
    public boolean isFile(Context context, CustomerDataFile customerDataFile) throws Exception {
        if (context == null || customerDataFile == null) {
            SLog.w(TAG, "isFile method error(hdd), Invalid parameter");
            return false;
        }

        checkMountState(context);

        String callingPkg = SpsPermissionHelper.getCallingPackage(context);
        if (TextUtils.isEmpty(callingPkg)) {
            SLog.w(TAG, "isFile method error(hdd), Invalid access");
            return false;
        }

        File file = new File(mMountPath + DEFAULT_PATH_DELIMITER + callingPkg, customerDataFile.getPath());
        SLog.d(TAG, "Try to check isFile(hdd)");
        if (!isValidatePath(callingPkg, file.getCanonicalPath())) {
            SLog.w(TAG, "Invalid path");
            return false;
        }
        return file.isFile();
    }

    @Override
    public boolean isDirectory(Context context, CustomerDataFile customerDataFile) throws Exception {
        if (context == null || customerDataFile == null) {
            SLog.w(TAG, "isDirectory method error(hdd), Invalid parameter");
            return false;
        }

        checkMountState(context);

        String callingPkg = SpsPermissionHelper.getCallingPackage(context);
        if (TextUtils.isEmpty(callingPkg)) {
            SLog.w(TAG, "isDirectory method error(hdd), Invalid access");
            return false;
        }

        File file = new File(mMountPath + DEFAULT_PATH_DELIMITER + callingPkg, customerDataFile.getPath());
        SLog.d(TAG, "Try to check isDirectory(hdd)");
        if (!isValidatePath(callingPkg, file.getCanonicalPath())) {
            SLog.w(TAG, "Invalid path");
            return false;
        }
        return file.isDirectory();
    }

    @Override
    public boolean move(Context context, CustomerDataFile srcCustomerDataFile, CustomerDataFile destCustomerDataFile) throws Exception {
        if (context == null || srcCustomerDataFile == null || destCustomerDataFile == null) {
            SLog.w(TAG, "move method error(hdd), Invalid parameter");
            return false;
        }

        checkMountState(context);

        String callingPkg = SpsPermissionHelper.getCallingPackage(context);
        if (TextUtils.isEmpty(callingPkg)) {
            SLog.w(TAG, "move method error(hdd), Invalid access");
            return false;
        }

        File srcFile = new File(mMountPath + DEFAULT_PATH_DELIMITER + callingPkg, srcCustomerDataFile.getPath());
        File descFile = new File(mMountPath + DEFAULT_PATH_DELIMITER + callingPkg, destCustomerDataFile.getPath());
        SLog.i(TAG, "move method (hdd)");
        if (!isValidatePath(callingPkg, srcFile.getCanonicalPath()) || !isValidatePath(callingPkg, descFile.getCanonicalPath())) {
            SLog.w(TAG, "Invalid path");
            return false;
        }
        return srcFile.renameTo(descFile);
    }

    @Override
    public boolean delete(Context context, CustomerDataFile customerDataFile) throws Exception {
        if (context == null || customerDataFile == null) {
            SLog.w(TAG, "delete method error(hdd), Invalid parameter");
            return false;
        }

        checkMountState(context);

        String callingPkg = SpsPermissionHelper.getCallingPackage(context);
        if (TextUtils.isEmpty(callingPkg)) {
            SLog.w(TAG, "delete method error(hdd), Invalid access");
            return false;
        }

        File file = new File(mMountPath + DEFAULT_PATH_DELIMITER + callingPkg, customerDataFile.getPath());
        SLog.d(TAG, "Try to delete(hdd)");
        if (!isValidatePath(callingPkg, file.getCanonicalPath())) {
            SLog.w(TAG, "Invalid path");
            return false;
        }
        return file.delete();
    }

    @Override
    public long length(Context context, CustomerDataFile customerDataFile) throws Exception {
        if (context == null || customerDataFile == null) {
            SLog.w(TAG, "length method error(hdd), Invalid parameter");
            return 0L;
        }

        checkMountState(context);

        String callingPkg = SpsPermissionHelper.getCallingPackage(context);
        if (TextUtils.isEmpty(callingPkg)) {
            SLog.w(TAG, "length method error(hdd), Invalid access");
            return 0L;
        }

        File file = new File(mMountPath + DEFAULT_PATH_DELIMITER + callingPkg, customerDataFile.getPath());
        SLog.w(TAG, "length method " + file.exists());
        if (!isValidatePath(callingPkg, file.getCanonicalPath())) {
            SLog.w(TAG, "Invalid path");
            return 0L;
        }
        return file.length();
    }

    @Override
    public long lastModified(Context context, CustomerDataFile customerDataFile) throws Exception {
        if (context == null || customerDataFile == null) {
            SLog.w(TAG, "lastModified method error(hdd), Invalid parameter");
            return 0L;
        }

        checkMountState(context);

        String callingPkg = SpsPermissionHelper.getCallingPackage(context);
        if (TextUtils.isEmpty(callingPkg)) {
            SLog.w(TAG, "lastModified method error(hdd), Invalid access");
            return 0L;
        }

        File file = new File(mMountPath + DEFAULT_PATH_DELIMITER + callingPkg, customerDataFile.getPath());
        SLog.w(TAG, "lastModified method " + file.exists());
        if (!isValidatePath(callingPkg, file.getCanonicalPath())) {
            SLog.w(TAG, "Invalid path");
            return 0L;
        }
        return file.lastModified();
    }

    private void checkMountState(Context context) throws Exception {
        if (context == null) {
            SLog.d(TAG, "Failed to start check HDD folder");
            throw new IllegalStateException("Failed to start check HDD folder");
        }

        String callingPkg = SpsPermissionHelper.getCallingPackage(context);
        if (TextUtils.isEmpty(callingPkg)) {
            SLog.d(TAG, "Failed to check HDD folder");
            throw new IllegalStateException("Failed to check HDD folder");
        }

        File root = new File(ROOT_MOUNT_LOCATION);
        if (!root.exists() || !isMounted()) {
            SLog.d(TAG, "HDD storage is not mounted");
            throw new IllegalStateException("HDD storage is not mounted");
        }

        File workRoot = new File(ROOT_MOUNT_LOCATION + DEFAULT_PATH_DELIMITER + callingPkg);
        //For first try
        if (!workRoot.exists()) {
            SLog.d(TAG, "Tried to init HDD for first use");
            workRoot.mkdirs();
        }
        if (!workRoot.exists()) {
            SLog.d(TAG, "Failed to mount HDD folder");
            throw new IllegalStateException("Failed to mount HDD folder");
        }
    }

    private boolean isValidatePath(final String callingPkg, final String canonicalPath) throws Exception {
        SLog.i(TAG, "isValidatePath");
        if (TextUtils.isEmpty(callingPkg) || TextUtils.isEmpty(canonicalPath)) {
            SLog.e(TAG, "Failed to check HDD path");
            return false;
        } else {
            if (canonicalPath.indexOf(mMountPath + DEFAULT_PATH_DELIMITER + callingPkg + DEFAULT_PATH_DELIMITER) != 0) {
                SLog.e(TAG, "Failed to check HDD path(Bad pathname)");
                return false;
            }

            return true;
        }
    }

    private void copyStream(InputStream input, OutputStream output) throws Exception {
        byte[] buffer = new byte[512 * 1024]; // or other buffer size
        int read;

        int total = 0;
        try {
            while ((read = input.read(buffer)) != -1) {
                output.write(buffer, 0, read);
                total += read;
            }
        } finally {
            try {
                input.close();
            } catch (Exception e) {
                SLog.w(TAG, "Failed to close input file:" + e.getMessage());
            }
            try {
                output.close();
            } catch (Exception e) {
                SLog.w(TAG, "Failed to close output file", e);
            }
        }

        SLog.i(MassStoragelet.TAG, "copyStream: " + total + " bytes copied");
    }

    @Override
    public String getRootPath(Context context) throws Exception {
        String callingPkg = SpsPermissionHelper.getCallingPackage(context);
        if (TextUtils.isEmpty(callingPkg)) {
            SLog.w(TAG, "getRootPath method error(hdd), Invalid access");
            throw new SdkServiceErrorException("getRootPath method error(hdd), Invalid access");
        }
        return ROOT_MOUNT_LOCATION + DEFAULT_PATH_DELIMITER + callingPkg;
    }
}
