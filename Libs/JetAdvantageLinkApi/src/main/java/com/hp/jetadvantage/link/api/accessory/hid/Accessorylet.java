// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.accessory.hid;

import android.content.ContentResolver;
import android.net.Uri;

/**
 * Specifies the information needed to communicate with the AccessoryLet.
 *
 * @hide for internal use
 * @since API 3
 */
@SuppressWarnings({"WeakerAccess"})
public class Accessorylet {

    private Accessorylet() {
    }

    /**
     * @hide trivial
     */
    public static final String TAG = "Accessorylet";

    /**
     * @hide
     */
    public static final String IS_SUPPORTED_EXTRA = "com.hp.jetadvantage.link.api.accessory.extra.IS_SUPPORTED";

    /**
     * @hide trivial
     */
    public static final String ACTION = "com.hp.jetadvantage.link.intent.action.accessorylet";

    /**
     * @hide trivial
     */
    public static final String ACCESSORY_CONTEXT_CHANGE_ACTION = "com.hp.jetadvantage.link.api.accessory.ACCESSORY_CONTEXT_CHANGE_ACTION";

    /**
     * @hide trivial
     */
    public static final String ACCESSORY_CHANGE_ACTION = "com.hp.jetadvantage.link.api.accessory.ACCESSORY_CHANGE_ACTION";

    /**
     * @hide trivial
     */
    public static final String AUTHORITY = "com.hp.jetadvantage.link.authority.accessoryletcp";

    /**
     * @hide trivial
     */
    public static final String DIR_PATH_SEGMENT = "accessoryletcp";

    /**
     * @hide trivial
     */
    private static final String CONTENT_SCHEME = "content";

    /**
     * @hide
     */
    public static final String PROVIDER_EVENT_ACTION = "com.hp.jetadvantage.link.api.action.ACCESSORY";

    /**
     * @hide
     */
    public static final String ACCESSORY_STATUS_ACTION = "com.hp.workpath.action.ACCESSORY_STATUS_ACTION";

    /**
     * @hide
     */
    public static final String ACCESSORY_REPORT_ACTION = "com.hp.workpath.action.ACCESSORY_REPORT_ACTION";

    /**
     * @hide trivial
     */
    public static final String ACCESSORY_CONTEXT_CHANGE_ACTION_FOR_APP = "com.hp.workpath.api.accessory.ACCESSORY_CONTEXT_CHANGE_ACTION";

    /**
     * @hide trivial
     */
    public static final String ACCESSORY_CHANGE_ACTION_FOR_APP = "com.hp.workpath.api.accessory.ACCESSORY_CHANGE_ACTION";

    /**
     * @hide trivial
     */
    public static final Uri CONTENT_URI = new Uri.Builder().scheme(CONTENT_SCHEME).authority(AUTHORITY)
            .appendPath(DIR_PATH_SEGMENT).build();

    /**
     * Getter for Content Uri in case the SDK supports
     * multiple number of Xlet for AccessoryService
     *
     * @param contentResolver ContentResolver to get systems information
     * @return Uri of the accessory content
     * @hide for internal use for getting content Uri
     */
    public static Uri getContentUri(final ContentResolver contentResolver) {
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
         * Param to pass package name
         *
         * @hide for internal communication
         * @since API 3
         */
        public static final String PACKAGE_NAME = "pkgname";

        /**
         * @hide
         */
        public static final String KEY_ACCESSORY_INFO = "accessoryInfo";

        /**
         * @hide
         */
        public static final String KEY_ACCESSORY_CONTEXT_ID = "accessoryContextId";

        /**
         * @hide
         */
        public static final String KEY_ACCESSORY_CONTEXT_EVENT_CODE = "accessoryContextEventCode";

        /**
         * @hide
         */
        public static final String KEY_HID_REPORT_TYPE = "hidReportType";

        /**
         * @hide
         */
        public static final String KEY_HID_REPORT = "hidReport";

        /**
         * @hide
         */
        public static final String KEY_HID_REPORT_EVENT_INFO = "hidReportEventInfo";

        /**
         * @hide
         */
        public static final String KEY_TIMESTAMP = "timestamp";

        /**
         * @hide
         */
        public static final String KEY_VENDOR_ID = "vendorId";

        /**
         * @hide
         */
        public static final String KEY_PRODUCT_ID = "productId";

        /**
         * @hide
         */
        public static final String KEY_SERIAL_NUMBER = "serialNumber";

        /**
         * @hide
         */
        public static final String KEY_ACCESSORY_REGISTRATION = "accessoryRegistration";

        /**
         * @hide
         */
        public static final String KEY_CALLBACKURI = "accessoryCallBackUri";


        /**
         * @hide
         */
        public static final String ACCESSORY_INFO_VENDER_ID = "ACCESSORY_INFO_VENDER_ID";

        /**
         * @hide
         */
        public static final String ACCESSORY_INFO_VENDOR_ID = "ACCESSORY_INFO_VENDOR_ID";
        /**
         * @hide
         */
        public static final String ACCESSORY_INFO_PRODUCT_ID = "ACCESSORY_INFO_PRODUCT_ID";

        /**
         * @hide
         */
        public static final String ACCESSORY_INFO_SERIAL_NUMBER = "ACCESSORY_INFO_SERIAL_NUMBER";

        /**
         * @hide
         */
        public static final String ACCESSORY_INFO_REGISTRATION_TYPE = "ACCESSORY_INFO_REGISTRATION_TYPE";

        /**
         * @hide
         */
        public static final String ACCESSORY_INFO_CONTEXT_ID = "ACCESSORY_INFO_CONTEXT_ID";

        /**
         * @hide
         */
        public static final String ACCESSORY_INFO_TIMESTAMP = "ACCESSORY_INFO_TIMESTAMP";

        /**
         * @hide
         */
        public static final String ACCESSORY_INFO_EVENT_CODE = "ACCESSORY_INFO_EVENT_CODE";

        /**
         * @hide
         */
        public static final String ACCESSORY_REPORT_INFO_ORDINAL = "ACCESSORY_REPORT_INFO_ORDINAL";

        /**
         * @hide
         */
        public static final String ACCESSORY_REPORT_INFO_TIMESTAMP = "ACCESSORY_REPORT_INFO_TIMESTAMP";

        /**
         * @hide
         */
        public static final String ACCESSORY_REPORT_INFO_DATA = "ACCESSORY_REPORT_INFO_DATA";
        /**
         * @hide
         */
        public static final String ACCESSORY_REPORT_INFO_BYTE_ARRAY_LIST = "ACCESSORY_REPORT_INFO_BYTE_ARRAY_LIST";
        /**
         * @hide
         */
        public static final String ACCESSORY_REPORT_INFO_COUNT = "ACCESSORY_REPORT_INFO_COUNT";
    }

    /**
     * The methods available for calls to the content provider
     *
     * @hide The client should not need to know about the content provider methods
     */
    public static final class Method {
        private Method() {
        }

        public static final String REGISTER = "register";
        public static final String UNREGISTER = "unregister";
        public static final String UNREGISTER_OWNED_SHARED = "unregisterOxpd";
        public static final String ENUMERATE = "enumerate";
        public static final String GET_SHARED = "getShared";
        public static final String GET_OWNED = "getOwned";
        public static final String REGISTER_OWNED = "registerOwned";
        public static final String REGISTER_SHARED = "registerShared";
        public static final String RESEND_OWNED = "resendOwned";
        public static final String RESERVE = "reserve";
        public static final String RELEASE = "release";
        public static final String OPEN = "open";
        public static final String CLOSE = "close";
        public static final String START_READING = "startReading";
        public static final String STOP_READING = "stopReading";
        public static final String GET_HID_INFO = "getHidInfo";
        public static final String READ_REPORT = "readReport";
        public static final String WRITE_REPORT = "writeReport";
        public static final String IS_SUPPORTED = "is_supported";
        public static final String IS_READY = "is_ready";
        public static final String INIT_CALLBACK = "initCallback";
    }
}
