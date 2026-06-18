// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.storagelet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hp.jetadvantage.link.api.massstorage.MassStorageInfo;
import com.hp.jetadvantage.link.services.storagelet.adapter.USBStorage;

import java.util.ArrayList;
import java.util.List;

public class StorageFactory {
    public static final StorageFactory INSTANCE = new StorageFactory();

    private StorageFactory() {
    }

    public IStorage getStorageForPath(@NonNull final MassStorageInfo.StorageType type, @NonNull final String path) {
        if(type == null || path == null) return null;

        IStorage findStorage = null;
        List<IStorage> storageList = getStorageList(type);
        for (IStorage storage : storageList) {
            if (type == MassStorageInfo.StorageType.USB) {
                if (storage.getInfo().isMounted() && path.startsWith(storage.getMappedLocation().getAbsolutePath())) {
                    findStorage = storage;
                }
            } else if (type == MassStorageInfo.StorageType.HDD) {
                if (storage.getInfo().isMounted()) {
                    findStorage = storage;
                }
            }
        }
        return findStorage;

    }

    public List<IStorage> getStorageList(@Nullable final MassStorageInfo.StorageType type) {
        List<IStorage> result = new ArrayList<>();

        // repeat for all storage type
        if (type == null) {
            result.addAll(USBStorage.getMountedStorages());
            result.addAll(HDDStorage.getMountedStorages());
        } else if (type == MassStorageInfo.StorageType.USB) {
            result.addAll(USBStorage.getMountedStorages());
        } else if (type == MassStorageInfo.StorageType.HDD) {
            result.addAll(HDDStorage.getMountedStorages());
        }

        return result;
    }
}
