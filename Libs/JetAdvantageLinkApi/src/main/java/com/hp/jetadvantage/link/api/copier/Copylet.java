// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.copier;

import androidx.annotation.NonNull;
import android.content.ContentResolver;
import android.net.Uri;

import com.hp.jetadvantage.link.common.helper.SelectedPrinterHelper;
import com.hp.jetadvantage.link.common.model.ApiType;
import com.hp.jetadvantage.link.common.model.PrinterInfo;

/**
 * Specifies the information needed to communicate with the Copylet.
 *
 * @hide for internal use
 * @since API 3
 */
@SuppressWarnings({"WeakerAccess"})
public class Copylet {

    private Copylet() {
    }

    /**
     * @hide trivial
     */
    public static final String TAG = "Copylet";

    /**
     * @hide
     */
    public static final String IS_SUPPORTED_EXTRA = "com.hp.jetadvantage.link.api.copier.extra.IS_SUPPORTED";

    /**
     * @hide trivial
     */
    public static final String ACTION = "com.hp.jetadvantage.link.intent.action.copylet";

    /**
     * @hide trivial
     */
    public static final String AUTHORITY = "com.hp.jetadvantage.link.authority.copyletcp";

    /**
     * @hide internal data for communication between internal components
     */
    public static final String AUTHORITY_OXP = "com.hp.jetadvantage.link.authority.oxp.copyletcp";

    /**
     * @hide internal data for communication between internal components
     */
    public static final String AUTHORITY_BASIC = "com.hp.jetadvantage.link.authority.basic.copyletcp";

    /**
     * @hide trivial
     */
    public static final String DIR_PATH_SEGMENT = "copyletcp";

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
     * multiple number of Xlet for CopierService
     *
     * @param contentResolver ContentResolver to get systems information
     * @return Uri of the copy content
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
     * multiple number of Xlet for CopierService
     *
     * @param api {@link ApiType} to retrieve data for
     * @return Uri of the copy content
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
     * @since API 3
     */
    public static final class Keys {
        private Keys() {
            // Utility class
        }

        /**
         * @hide
         */
        public static final String KEY_CLIENT_VERSION = "clientVersion";

        /**
         * @hide
         */
        public static final String KEY_STORED_JOB_ID = "storedJobId";

        /**
         * @hide
         */
        public static final String KEY_DELETE_REQ = "deleteReqId";

    }

    /**
     * The methods available for calls to the MFPServices content provider
     *
     * @hide The client should not need to know about the content provider methods
     */
    public static final class Method {
        private Method() {
        }

        public static final String GET_DEFAULTS = "get_defaults";
        public static final String GET_CAPS = "get_caps";
        public static final String GET_STATUS = "get_status";
        public static final String DELETE_JOB = "delete_job";
        public static final String ENUMERATE_JOBS = "enumerate_jobs";
        public static final String IS_SUPPORTED = "is_supported";
    }
}
