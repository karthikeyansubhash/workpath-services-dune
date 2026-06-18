// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.printer;

import androidx.annotation.NonNull;
import android.content.ContentResolver;
import android.net.Uri;

import com.hp.jetadvantage.link.common.helper.SelectedPrinterHelper;
import com.hp.jetadvantage.link.common.model.ApiType;
import com.hp.jetadvantage.link.common.model.PrinterInfo;

/**
 * Specifies information necessary to communicate with the Printlet.
 *
 * @hide for internal use
 * @since API 1
 */
@SuppressWarnings({"WeakerAccess"})
public class Printlet {
    private Printlet() {
    }

    /**
     * @hide trivial
     */
    public static final String TAG = "Printlet";

    /**
     * @hide trivial
     * @since API 1
     */
    public static final String ACTION = "com.hp.jetadvantage.link.intent.action.printlet";

    /**
     * @hide
     */
    public static final String IS_SUPPORTED_EXTRA = "com.hp.jetadvantage.link.api.printer.extra.IS_SUPPORTED";

    /**
     * @hide
     */
    public static final String GET_JOB_COUNT_EXTRA = "com.hp.jetadvantage.link.api.printer.extra.GET_JOB_COUNT_EXTRA";

    /**
     * @hide
     */
    public static final String GET_AVAILABLE_JOB_COUNT_EXTRA = "com.hp.jetadvantage.link.api.printer.extra.GET_AVAILABLE_JOB_COUNT_EXTRA";

    /**
     * @hide trivial
     */
    public static final String AUTHORITY = "com.hp.jetadvantage.link.authority.printletcp";

    /**
     * @hide internal data for communication between internal components
     */
    public static final String AUTHORITY_OXP = "com.hp.jetadvantage.link.authority.oxp.printletcp";

    /**
     * @hide internal data for communication between internal components
     */
    public static final String AUTHORITY_BASIC = "com.hp.jetadvantage.link.authority.basic.printletcp";

    /**
     * @hide trivial
     */
    public static final String DIR_PATH_SEGMENT = "printletcp";

    /**
     * @hide trivial
     */
    private static final String CONTENT_SCHEME = "content";

    /**
     * @hide trivial
     */
    public static final Uri CONTENT_URI = new Uri.Builder()
            .scheme(CONTENT_SCHEME)
            .authority(AUTHORITY)
            .appendPath(DIR_PATH_SEGMENT)
            .build();

    /**
     * @hide internal data for communication between internal components
     */
    public static final Uri CONTENT_OXP_URI = new Uri.Builder()
            .scheme(CONTENT_SCHEME)
            .authority(AUTHORITY_OXP)
            .appendPath(DIR_PATH_SEGMENT)
            .build();

    /**
     * @hide internal data for communication between internal components
     */
    public static final Uri CONTENT_BASIC_URI = new Uri.Builder()
            .scheme(CONTENT_SCHEME)
            .authority(AUTHORITY_BASIC)
            .appendPath(DIR_PATH_SEGMENT)
            .build();

    /**
     * Getter for Content Uri in case the SDK supports
     * multiple number of X-let for ScannerService
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
     * multiple number of X-let for ScannerService
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
     * @hide
     */
    public static final class Keys {
        public static final String KEY_CLIENT_VERSION = "clientVersion";
    }

    /**
     * The methods available for calls to the Printlet
     *
     * @hide used internally by PrintService
     */
    public static final class Method {
        private Method() {
        }

        public static final String GET_CAPS = "get_caps";
        public static final String GET_DEFAULTS = "get_defaults";
        public static final String GET_STATUS = "get_status";
        public static final String GET_TRAY_INFO = "get_tray_info";
        public static final String IS_SUPPORTED = "is_supported";
        public static final String IS_STATUS_SUPPORTED = "is_status_supported";
        public static final String GET_JOB_COUNT = "get_job_count";
        public static final String GET_AVAILABLE_JOB_COUNT = "get_available_job_count";
    }
}
