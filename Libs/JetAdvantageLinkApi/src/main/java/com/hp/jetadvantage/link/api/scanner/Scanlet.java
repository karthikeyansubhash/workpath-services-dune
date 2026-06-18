// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.scanner;

import androidx.annotation.NonNull;
import android.content.ContentResolver;
import android.net.Uri;

import com.hp.jetadvantage.link.common.helper.SelectedPrinterHelper;
import com.hp.jetadvantage.link.common.model.ApiType;
import com.hp.jetadvantage.link.common.model.PrinterInfo;

/**
 * Specifies the information needed to communicate with the Scanlet.
 *
 * @hide for internal use
 * @since API 1
 */
@SuppressWarnings({"WeakerAccess"})
public class Scanlet {

    private Scanlet() {
    }

    /**
     * @hide trivial
     */
    public static final String TAG = "Scanlet";

    /**
     * @hide
     */
    public static final String IS_SUPPORTED_EXTRA = "com.hp.jetadvantage.link.api.printer.extra.IS_SUPPORTED";

    /**
     * @hide trivial
     */
    public static final String ACTION = "com.hp.jetadvantage.link.intent.action.scanlet";

    /**
     * @hide trivial
     */
    public static final String AUTHORITY = "com.hp.jetadvantage.link.authority.scanletcp";

    /**
     * @hide internal data for communication between internal components
     */
    public static final String AUTHORITY_OXP = "com.hp.jetadvantage.link.authority.oxp.scanletcp";

    /**
     * @hide internal data for communication between internal components
     */
    public static final String AUTHORITY_BASIC = "com.hp.jetadvantage.link.authority.basic.scanletcp";

    /**
     * @hide trivial
     */
    public static final String DIR_PATH_SEGMENT = "scanletcp";

    /**
     * @hide trivial
     */
    private static final String CONTENT_SCHEME = "content";

    /**
     * @hide trivial
     */
    public static final Uri CONTENT_URI = new Uri.Builder().scheme(CONTENT_SCHEME).authority(AUTHORITY)
            .appendPath(DIR_PATH_SEGMENT).build();

    /**
     * @hide internal data for communication between internal components
     */
    public static final Uri CONTENT_OXP_URI = new Uri.Builder().scheme(CONTENT_SCHEME).authority(AUTHORITY_OXP)
            .appendPath(DIR_PATH_SEGMENT).build();

    /**
     * @hide internal data for communication between internal components
     */
    public static final Uri CONTENT_BASIC_URI = new Uri.Builder().scheme(CONTENT_SCHEME).authority(AUTHORITY_BASIC)
            .appendPath(DIR_PATH_SEGMENT).build();

    /**
     * Getter for Content Uri in case the SDK supports
     * multiple number of Xlet for ScannerService
     *
     * @param contentResolver ContentResolver to get systems information
     * @return Uri of the scan content
     * @hide for internal use for getting content Uri
     */
    public static Uri getContentUri(final ContentResolver contentResolver) {
        final PrinterInfo selectedPi = SelectedPrinterHelper.get(contentResolver);

        if (!PrinterInfo.isEmpty(selectedPi)) {
            return getContentUri(selectedPi.getApiType());
        }

        return CONTENT_URI;
    }

    /**
     * Getter for Content Uri in case the SDK supports
     * multiple number of Xlet for ScannerService
     *
     * @param api {@link ApiType} to retrieve data for
     * @return Uri of the scan content
     * @hide for internal use for getting content Uri
     */
    public static Uri getContentUri(@NonNull final ApiType api) {
        switch (api) {
            case BASIC:
                return CONTENT_BASIC_URI;
            case OXP:
                return CONTENT_OXP_URI;
            default:
                break;
        }

        return CONTENT_URI;
    }

    /**
     * Keys used for transporting data. These are used to provide data to the client.
     * @hide
     * @since API 1
     */
    public static final class Keys {
        private Keys() {
            // Utility class
        }

        /**
         * Key to retrieve the list of stored file names.<br>
         * This list contains filename with complete paths. For OPE each file name
         * has format of media_storage_name/filename (e.g. download/2015-12-30_17.15.19_0.pdf).
         * <p>
         * <br><b>Type:</b> ArrayList&lt;String&gt;
         *
         * @since API 1
         */
        public static final String KEY_FILENAME_LIST = "filenamelist";

        /**
         * @hide
         */
        public static final String KEY_CLIENT_VERSION = "clientVersion";

        /**
         * @hide
         */
        public static final String KEY_TRANSMISSION_MODE = "transmissionMode";

        /**
         * @hide
         */
        public static final String KEY_COLOR_MODE = "colorMode";

        /**
         * @hide
         */
        public static final String KEY_DOCUMENT_FORMAT = "documentFormat";
    }

    /**
     * The methods available for calls to the content provider
     *
     * @hide The client should not need to know about the content provider methods
     */
    public static final class Method {
        private Method() {
        }

        public static final String GET_DEFAULTS = "get_defaults";
        public static final String GET_CAPS = "get_caps";
        public static final String GET_STATUS = "get_status";
        public static final String GET_FILE_OPTIONS_CAPS = "get_file_options_caps";
        public static final String IS_SUPPORTED = "is_supported";
        public static final String GET_FILE_REQ = "get_file_req";
        public static final String PUT_FILE_REQ = "put_file_req";
    }

    /**
     * The parameters used for transporting data. These are used to provide data to the client.
     *
     * @hide The client should not need to know about the content provider parameters
     */
    public static final class Param {

        private Param() {
        }

        /**
         * Param to pass package name to ScanLet
         *
         * @hide for internal communication
         * @since API 1
         */
        public static final String PACKAGE_NAME = "pkgname";

        /**
         * @hide
         */
        public static final String KEY_JOB_ID = "jobId";

        /**
         * @hide
         */
        public static final String KEY_FILE_URI = "fileUri";

        /**
         * @hide
         */
        public static final String KEY_FILE_URIS = "fileUris";

        /**
         * @hide
         */
        public static final String KEY_CLIENT_VERSION = "clientVersion";
    }
}
