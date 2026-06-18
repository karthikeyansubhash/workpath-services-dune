// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.storagelet;

import android.content.Context;

import com.hp.jetadvantage.link.api.massstorage.CustomerDataFile;
import com.hp.jetadvantage.link.api.massstorage.MassStorageInfo;

import java.io.File;
import java.util.List;

public interface IStorage {
    MassStorageInfo.StorageType getStorageType();

    String getName();

    MassStorageInfo getInfo();

    File getMappedLocation();

    List<File> list(Context context, String path) throws Exception;

    boolean exists(Context context, String path, CustomerDataFile customerDataFile) throws Exception;

    boolean createNewFile(Context context, CustomerDataFile customerDataFile) throws Exception;

    boolean mkdir(Context context, CustomerDataFile customerDataFile) throws Exception;

    boolean isFile(Context context, CustomerDataFile customerDataFile) throws Exception;

    boolean isDirectory(Context context, CustomerDataFile customerDataFile) throws Exception;

    boolean delete(Context context, CustomerDataFile customerDataFile) throws Exception;

    long length(Context context, CustomerDataFile customerDataFile) throws Exception;

    long lastModified(Context context, CustomerDataFile customerDataFile) throws Exception;

    boolean move(Context context, CustomerDataFile srcCustomerDataFile, CustomerDataFile destCustomerDataFile) throws Exception;

    String getRootPath(Context context) throws Exception;
}
