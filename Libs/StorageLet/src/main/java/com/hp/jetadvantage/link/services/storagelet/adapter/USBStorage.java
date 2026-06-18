// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.storagelet.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.hp.jetadvantage.link.api.massstorage.CustomerDataFile;
import com.hp.jetadvantage.link.api.massstorage.MassStorageInfo;
import com.hp.jetadvantage.link.api.massstorage.MassStoragelet;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceStorageService;
import com.hp.jetadvantage.link.services.common.exception.SdkServiceErrorException;
import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;
import com.hp.jetadvantage.link.services.storagelet.IStorage;
import com.hp.ws.cdm.storage.RemovableDevice;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

public class USBStorage implements IStorage {
    private static final String TAG = MassStoragelet.TAG + "/USB";

    public static final String ROOT_MOUNT_LOCATION = "/data/jedi";
    private static final String SIZE_INFO_FILE_LOCATION = "/data/workpath/Customer/Temp/df_";

    private static final String USB_ROOT_PREFIX = "part"; //Bug fixed(JALPINF-490): Not used

    private static final int DEFAULT_WAIT_TIMEOUT = 2000; //Bug fixed(JALPINF-371) sec, Best until FS4

    private File mMountPath;
    private RemovableDevice mStorageDevice;

    public static List<IStorage> getMountedStorages() {
        File[] usbRoots = getMountedUsbDirectories();
        List<RemovableDevice> datas = null;
        try {
            datas = USBStorage.getStorages();
        } catch (Throwable e) {
            SLog.i(MassStoragelet.TAG, "Not found USB from oxpd - 1: " + e.getMessage());
        }

        if (datas == null) datas = new ArrayList<>();
        List<IStorage> resultList = new ArrayList<>();

        if (usbRoots != null) {
            if (usbRoots.length == 0) {
                // no mounted USB - add one with unmounted state (null - no mounted folder)
                usbRoots = new File[]{null};
            } else {
                SLog.d(MassStoragelet.TAG, "Found USB roots: " + Arrays.toString(usbRoots));
                RemovableDevice storageDevice = null;
                for (File usbRoot : usbRoots) {
                    if (datas.size() > 0) {
                        for (RemovableDevice value : datas) {
                            String name = value.getDriveId();
                            if (usbRoot.getName().equalsIgnoreCase(name)) {
                                storageDevice = value;
                                resultList.add(new USBStorage(usbRoot, storageDevice));
                                SLog.d(MassStoragelet.TAG, "resultList add : " + usbRoot.getName());
                                break;
                            }
                        }
                    } else {
                        SLog.i(MassStoragelet.TAG, "Mounted USB devices: 0");
                    }
                }
            }
        } else {
            SLog.w(MassStoragelet.TAG, "USB mount point doesn't exist, USB storage are not supported");
        }
        SLog.d(MassStoragelet.TAG, "resultList : " + Arrays.toString(resultList.toArray()));
        return resultList;
    }

    private static class WaitStorageMountCallable implements Callable<List<RemovableDevice>> {
        @Override
        public List<RemovableDevice> call() throws Exception {
            Thread.sleep(DEFAULT_WAIT_TIMEOUT);
            return USBStorage.getStorages();
        }
    }

    USBStorage(final File mountPath, RemovableDevice storageDevice) {
        mMountPath = mountPath;
        mStorageDevice = storageDevice;
    }

    USBStorage(final File mountPath) {
        mMountPath = mountPath;
    }

    @Override
    public MassStorageInfo.StorageType getStorageType() {
        return MassStorageInfo.StorageType.USB;
    }

    @Override
    public String getName() {
        return mMountPath != null ? mMountPath.getName() : null;
    }

    @Override
    public MassStorageInfo getInfo() {
        if (mMountPath != null) {
            long[] size = getStorageSize();
            SLog.w(TAG, "USB getInfo:" + size.length);

            if (mStorageDevice != null) {
                return new MassStorageInfo(getStorageType(), mStorageDevice.getVolumeName() + "(" + mStorageDevice.getVolumeName() + ")",
                        size[0], size[1], isMounted(),
                        mMountPath.getAbsolutePath(), MassStorageInfo.Protocol.LOCAL, mStorageDevice.getVolumeName());
            } else {
                return new MassStorageInfo(getStorageType(), getName(),
                        size[0], size[1], isMounted(),
                        mMountPath.getAbsolutePath(), MassStorageInfo.Protocol.LOCAL);
            }
        } else {
            SLog.w(TAG, "USB getInfo");
            return new MassStorageInfo(getStorageType(), getName(),
                    0, 0, isMounted(),
                    null, MassStorageInfo.Protocol.LOCAL, "unknown");
        }
    }

    @Override
    public File getMappedLocation() {
        return mMountPath;
    }

    private boolean isMounted() {
        return mMountPath != null && mMountPath.exists();
    }

    private long[] getStorageSize() {
        long[] size = new long[]{0L, 0L};
        File sizeInfoFile = new File(SIZE_INFO_FILE_LOCATION + getName());
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(sizeInfoFile));
            String strTotalSize = reader.readLine().trim();
            String strFreeSize = reader.readLine().trim();
            size[0] = Long.parseLong(strTotalSize);
            size[1] = Long.parseLong(strFreeSize);

        } catch (Throwable throwable) {
            if (mMountPath != null && mMountPath.getTotalSpace() >= 0) {
                size[0] = mMountPath.getTotalSpace();
                size[1] = mMountPath.getFreeSpace();
            } else {
                size = new long[]{0L, 0L};
                SLog.w(MassStoragelet.TAG, "Size is not available");
                SLog.w(MassStoragelet.TAG, "Read USB info file error : " + throwable.getMessage());
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

        File pathLocation = new File(mMountPath, path);

        return new ArrayList<>(Arrays.asList(pathLocation.listFiles()));
    }

    @Override
    public boolean exists(Context context, String path, CustomerDataFile customerDataFile) throws Exception {
        if(context == null || TextUtils.isEmpty(path) || customerDataFile == null) {
            SLog.w(TAG, "exists method error(usb), Invalid parameter");
            return false;
        }

        checkMountState(context);

        String callingPkg = SpsPermissionHelper.getCallingPackage(context);
        if(TextUtils.isEmpty(callingPkg)) {
            SLog.w(TAG, "exists method error(usb), Invalid access");
            return false;
        }

        if (path.equalsIgnoreCase(customerDataFile.getPath())) {
            File file = new File(mMountPath, customerDataFile.getPath());
            SLog.d(TAG, "exists method (usb), try to check file exists from path");
            return file.exists();
        }

        SLog.i(TAG, "exists method error(usb), Invalid path access");
        return false;
    }

    @Override
    public boolean createNewFile(Context context, CustomerDataFile customerDataFile) throws Exception {
        if(context == null || customerDataFile == null) {
            SLog.w(TAG, "createNewFile method error(usb), Invalid parameter");
            return false;
        }

        checkMountState(context);

        String callingPkg = SpsPermissionHelper.getCallingPackage(context);
        if(TextUtils.isEmpty(callingPkg)) {
            SLog.w(TAG, "createNewFile method error(usb), Invalid access");
            return false;
        }

        File file = new File(mMountPath, customerDataFile.getPath());
        SLog.d(TAG, "Create new USB file : " + file.getName());

        boolean result = false;
        OutputStream os = null;
        FileOutputStream fio = null;
        try {
            fio = new FileOutputStream(file);
            os = new BufferedOutputStream(fio);
            os.flush();
            result = true;
        } catch (Throwable throwable) {
            SLog.e(TAG, "Failed to create new file(USB) " + throwable.getMessage());
            result = false;
        } finally {
            if (fio != null) {
                try { fio.close();
                } catch (Exception e) {}
            }
            if (os != null) {
                try { os.close();
                } catch (Exception e) {
                }
            }
        }
        SLog.d(TAG, "Success to create new file(USB)");
        file.setWritable(true, false);
        return result;
    }

    @Override
    public boolean mkdir(Context context, CustomerDataFile customerDataFile) throws Exception {
        if(context == null || customerDataFile == null) {
            SLog.w(TAG, "mkdir method error(usb), Invalid parameter");
            return false;
        }

        checkMountState(context);

        String callingPkg = SpsPermissionHelper.getCallingPackage(context);
        if(TextUtils.isEmpty(callingPkg)) {
            SLog.w(TAG, "mkdir method error(usb), Invalid access");
            return false;
        }

        File file = new File(mMountPath, customerDataFile.getPath());
        SLog.d(TAG, "Try to create directory(usb)");
        return file.mkdir();
    }

    @Override
    public boolean isFile(Context context, CustomerDataFile customerDataFile) throws Exception {
        if(context == null || customerDataFile == null) {
            SLog.w(TAG, "isFile method error(usb), Invalid parameter");
            return false;
        }

        checkMountState(context);

        String callingPkg = SpsPermissionHelper.getCallingPackage(context);
        if(TextUtils.isEmpty(callingPkg)) {
            SLog.w(TAG, "isFile method error(usb), Invalid access");
            return false;
        }

        File file = new File(mMountPath, customerDataFile.getPath());
        SLog.d(TAG, "Try to check isFile(usb)");
        return file.isFile();
    }

    @Override
    public boolean isDirectory(Context context, CustomerDataFile customerDataFile) throws Exception {
        if(context == null || customerDataFile == null) {
            SLog.w(TAG, "isDirectory method error(usb), Invalid parameter");
            return false;
        }

        checkMountState(context);

        String callingPkg = SpsPermissionHelper.getCallingPackage(context);
        if(TextUtils.isEmpty(callingPkg)) {
            SLog.w(TAG, "isDirectory method error(usb), Invalid access");
            return false;
        }

        File file = new File(mMountPath, customerDataFile.getPath());
        SLog.d(TAG, "Try to check isDirectory(usb)");
        return file.isDirectory();
    }

    @Override
    public boolean delete(Context context, CustomerDataFile customerDataFile) throws Exception {
        if(context == null || customerDataFile == null) {
            SLog.w(TAG, "delete method error(usb), Invalid parameter");
            return false;
        }

        checkMountState(context);

        String callingPkg = SpsPermissionHelper.getCallingPackage(context);
        if(TextUtils.isEmpty(callingPkg)) {
            SLog.w(TAG, "delete method error(usb), Invalid access");
            return false;
        }

        File file = new File(mMountPath, customerDataFile.getPath());
        SLog.d(TAG, "Try to delete(usb)");
        return file.delete();
    }

    @Override
    public long length(Context context, CustomerDataFile customerDataFile) throws Exception {
        if(context == null || customerDataFile == null) {
            SLog.w(TAG, "length method error(usb), Invalid parameter");
            return 0L;
        }

        checkMountState(context);

        String callingPkg = SpsPermissionHelper.getCallingPackage(context);
        if(TextUtils.isEmpty(callingPkg)) {
            SLog.w(TAG, "length method error(usb), Invalid access");
            return 0L;
        }

        File file = new File(mMountPath, customerDataFile.getPath());
        SLog.w(TAG, "length method " + file.exists());
        return file.length();
    }

    @Override
    public long lastModified(Context context, CustomerDataFile customerDataFile) throws Exception {
        if(context == null || customerDataFile == null) {
            SLog.w(TAG, "lastModified method error(usb), Invalid parameter");
            return 0L;
        }

        checkMountState(context);

        String callingPkg = SpsPermissionHelper.getCallingPackage(context);
        if(TextUtils.isEmpty(callingPkg)) {
            SLog.w(TAG, "lastModified method error(usb), Invalid access");
            return 0L;
        }

        File file = new File(mMountPath, customerDataFile.getPath());
        SLog.w(TAG, "lastModified method " + file.exists());
        return file.lastModified();
    }

    @Override
    public boolean move(Context context, CustomerDataFile srcCustomerDataFile, CustomerDataFile destCustomerDataFile) throws Exception {
        if(context == null || srcCustomerDataFile == null || destCustomerDataFile == null) {
            SLog.w(TAG, "move method error(usb), Invalid parameter");
            return false;
        }

        checkMountState(context);

        String callingPkg = SpsPermissionHelper.getCallingPackage(context);
        if(TextUtils.isEmpty(callingPkg)) {
            SLog.w(TAG, "move method error(usb), Invalid access");
            return false;
        }

        File srcFile = new File(mMountPath, srcCustomerDataFile.getPath());
        File destFile = new File(mMountPath, destCustomerDataFile.getPath());
        SLog.i(TAG, "move method (usb)");
        return srcFile.renameTo(destFile);
    }

    private static File[] getMountedUsbDirectories() {
        File[] usbRoots = new File(ROOT_MOUNT_LOCATION).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return dir.isDirectory();
            }
        });
        return usbRoots;
    }

    private static List<RemovableDevice> getStorages() throws Exception {
        StandardDeviceStorageService storageService = new StandardDeviceStorageService();
        return storageService.getStorages();
    }

    private void checkMountState(Context context) throws Exception {
        File root = new File(ROOT_MOUNT_LOCATION);
        if (!root.exists() || root.listFiles().length == 0) {
            SLog.d(TAG, "Failed to mount USB folder");
            throw new IllegalStateException("Failed to mount USB storage");
        }

        if (!isMounted()) {
            SLog.d(MassStoragelet.TAG, "USB storage is not mounted");
            throw new IllegalStateException("USB storage is not mounted");
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

        SLog.i(TAG, "copyStream: " + total + " bytes copied");
    }

    @Override
    public String getRootPath(Context context) throws Exception {
        String callingPkg = SpsPermissionHelper.getCallingPackage(context);
        if(TextUtils.isEmpty(callingPkg)) {
            SLog.w(TAG, "getRootPath method error(usb), Invalid access");
            throw new SdkServiceErrorException("getRootPath method error(usb), Invalid access");
        }
        return ROOT_MOUNT_LOCATION;
    }
}
