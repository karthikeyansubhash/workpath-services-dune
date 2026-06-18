// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.massstorage;

import android.net.Uri;

import com.hp.jetadvantage.link.common.annotation.DeviceApi;

/**
 * Holds constants for communication with the MassStoragelet module.
 *
 * @hide for internal use
 */
@DeviceApi
public class MassStoragelet {
    /**
     * @hide
     */
    public static final String TAG = "MassStoragelet";

    /**
     * @hide
     */
    public static final String IS_SUPPORTED_EXTRA = "com.hp.jetadvantage.link.api.config.extra.IS_SUPPORTED";

    /**
     * @hide
     */
    public static final String MASSSTORAGE_CHANGE_ACTION = "com.hp.jetadvantage.link.api.massstorage.MASSSTORAGE_CHANGED";

    /**
     * @hide
     */
    public static final class Method {
        public static final String IS_SUPPORTED = "is_supported";
        public static final String GET_STORAGE_LIST = "get_storage_list";
        public static final String GET_STORAGE = "get_storage";

        public static final String CREATE_NEW_FILE = "create_new_file";
        public static final String DELETE_FILE = "delete_file";
        public static final String CREATE_NEW_DIRECTORY = "create_new_directory";
        public static final String GET_FILE_INFO = "get_file_info";
        public static final String FILE_EXISTS = "file_exists";
        public static final String IS_FILE = "is_file";
        public static final String IS_DIRECTORY = "is_directory";
        public static final String FILE_LENGTH = "file_length";
        public static final String FILE_LAST_MODIFIED = "file_last_modified";
        public static final String FILE_RENAME = "file_rename";
        public static final String GET_FILE_URI = "get_file_uri";
        public static final String GET_FILE_SOCKET = "get_file_socket";

        public static final String LIST_FILES = "list_files";
    }

    /**
     * The parameters used for transporting data. These are used to provide data to the client.
     *
     * @hide The client should not need to know about the content provider parameters
     */
    public static final class Keys {

        private Keys() {
        }

        /**
         * Param to pass package name to MassStoragelet
         *
         * @hide for internal communication
         */
        public static final String PACKAGE_NAME = "pkgname";

        /**
         * @hide
         */
        public static final String KEY_CLIENT_VERSION = "clientVersion";

        /**
         * @hide
         */
        public static final String KEY_STORAGE_TYPE = "storageType";
        /**
         * @hide
         */
        public static final String KEY_FILE_TYPE = "fileType";
        /**
         * @hide
         */
        public static final String KEY_DEST_FILE_TYPE = "destFileType";
        /**
         * @hide
         */
        public static final String KEY_FILE_URI = "fileUri";
        /**
         * @hide
         */
        public static final String KEY_AVAILABLE_SIZE = "availableSize";
        /**
         * @hide
         */
        public static final String KEY_FILE_ADDR = "fileAddr";
        /**
         * @hide
         */
        public static final String KEY_FILE_PORT = "filePort";
    }

    /**
     * MassStoragelet providers authority
     *
     * @hide for internal communication
     */
    public static final String AUTHORITY = "com.hp.jetadvantage.link.authority.storageletcp";

    /**
     * Path for general MassStoragelet content provider calls
     *
     * @hide for internal communication
     */
    public static final String DIR_PATH_SEGMENT = "storageletcp";

    /**
     * MassStoragelet providers scheme
     *
     * @hide for internal communication
     */
    public static final String CONTENT_SCHEME = "content";

    /**
     * Content Provider URI
     *
     * @hide for internal communication
     */
    public static final Uri CONTENT_URI = new Uri.Builder().scheme(CONTENT_SCHEME).authority(AUTHORITY)
            .appendPath(DIR_PATH_SEGMENT).build();
}
