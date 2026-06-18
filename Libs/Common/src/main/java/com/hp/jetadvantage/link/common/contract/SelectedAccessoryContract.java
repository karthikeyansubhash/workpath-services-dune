// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.common.contract;

import android.net.Uri;

/**
 * Specifies information necessary to communicate with the accessory Content provider
 * 
 * @author APS
 * 
 */
public class SelectedAccessoryContract {
    public static final String AUTHORITY = "com.hp.jetadvantage.link.authority.accessorylet.internalcp.accessory";

    public static final String DIR_PATH_SEGMENT = "accessorydata";

    public static final Uri CONTENT_URI = Uri.withAppendedPath(Uri.parse("content://" + AUTHORITY), DIR_PATH_SEGMENT);

    public static final String KEY_ACCESSORY_INFO = "accessory";
    public static final String KEY_ACCESSORY_INFOS = "accessories";
    public static final String KEY_ACCESSORY_INFO_ID = "uuid";
    public static final String KEY_ACCESSORY_INFO_PKG = "packageName";

    /**
     * The methods available for calls to the accessory content provider
     */
    public static final class Method {
        public static final String PUT = "put";
        public static final String GET = "get";
        public static final String CLEAR_SELECTED = "clear_selected";
        public static final String CLEAR_ALL = "clear_all";
    }
}
