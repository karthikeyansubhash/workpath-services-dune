package com.hp.jetadvantage.link.services.storagelet.service;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.hp.jetadvantage.link.api.massstorage.MassStorageInfo;
import com.hp.jetadvantage.link.api.massstorage.MassStoragelet;
import com.hp.jetadvantage.link.common.constants.PackageContract;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.interfaces.IAppInstallUninstallCallback;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceSystemStateChangeCallback;
import com.hp.jetadvantage.link.services.storagelet.HDDStorage;
import com.hp.jetadvantage.link.services.storagelet.IStorage;
import com.hp.jetadvantage.link.services.storagelet.StorageFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MassStorageCallbackHandler {
    private static final String TAG = "[MASSSVC]CallbackHandler";
    private final Context mContext;
    private static ArrayList<String> changedDevices;

    MassStorageCallbackHandler(final Context context) {
        mContext = context;
        changedDevices = new ArrayList<>();
    }

    public final IAppInstallUninstallCallback appInstallUninstallCallback = new IAppInstallUninstallCallback() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (context == null || intent == null || intent.getAction() == null) {
                return;
            }
            final String action = intent.getAction();
            SLog.i(TAG, "Received " + action);

            try {
                if (PackageContract.Intent.ACTION_PACKAGE_UNINSTALLED.equals(action)) {
                    handlePackageUninstalled(intent);
                }
            } catch (Throwable throwable) {
                SLog.e(TAG, action + " call is failed with " + throwable.getMessage());
            }
        }
    };

    private void handlePackageUninstalled(Intent intent) {
        String pkgName = intent.getStringExtra(PackageContract.Intent.EXTRA_PACKAGE);

        if (!TextUtils.isEmpty(pkgName)) {
            removeHdd(pkgName);
        } else {
            SLog.e(TAG, intent.getAction() + " call is failed : pkg is null");
        }
    }

    private static void removeHdd(final String packageName) {
        if (!TextUtils.isEmpty(packageName)) {
            SLog.i(TAG, "Try to remove removeHdd : " + packageName);
            File hdd = new File(HDDStorage.ROOT_MOUNT_LOCATION + HDDStorage.DEFAULT_PATH_DELIMITER, packageName);
            if (hdd.exists()) {
                cleanTemporaryFolder(hdd);
            } else {
                SLog.e(TAG, "Hdd is not existed: " + packageName);
            }
        }
    }

    public static void cleanTemporaryFolder(File folder) {
        try {
            if (folder != null && folder.exists() && folder.isDirectory()) {
                final File[] files = folder.listFiles();
                if (files != null) {
                    for (final File file : files) {
                        // remove folder contents first
                        if (file.isDirectory()) {
                            cleanTemporaryFolder(file);
                        }
                        if (!file.delete()) {
                            SLog.w(TAG, "Failed to delete " + file.getName());
                        } else {
                            SLog.i(TAG, "File is deleted");
                        }
                    }
                }
                if (!folder.delete()) {
                    SLog.w(TAG, "Failed to delete folder " + folder.getName());
                } else {
                    SLog.i(TAG, "Package Folder is deleted");
                }
            }
        } catch (Exception e) {
            SLog.e(TAG, "Failed to clean folder:" + e.getMessage());
        }
    }

    public final IDeviceSystemStateChangeCallback systemStateChangeCallback = new IDeviceSystemStateChangeCallback() {
        @Override
        public void onChange() {
            synchronized (MassStorageCallbackHandler.class) {
                List<IStorage> storageList = StorageFactory.INSTANCE.getStorageList(MassStorageInfo.StorageType.USB);

                if (storageList != null) {
                    ArrayList<String> currentList = new ArrayList<>();
                    for (IStorage storage : storageList) {
                        currentList.add(storage.getName());
                    }

                    if (!changedDevices.equals(currentList)) {
                        changedDevices.clear();
                        changedDevices.addAll(currentList);

                        final Intent massstorageChangeIntent = new Intent(MassStoragelet.MASSSTORAGE_CHANGE_ACTION);
                        mContext.sendBroadcast(massstorageChangeIntent);
                    }
                }
            }
        }
    };

}
