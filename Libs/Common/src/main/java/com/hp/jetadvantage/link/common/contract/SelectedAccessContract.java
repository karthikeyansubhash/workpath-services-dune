// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.common.contract;

import android.net.Uri;

/**
 * Specifies information necessary to communicate with the access Content provider
 * 
 * @author APS
 * 
 */
public class SelectedAccessContract {
    public static final String AUTHORITY = "com.hp.jetadvantage.link.authority.accesslet.internalcp.access";

    public static final String DIR_PATH_SEGMENT = "accessdata";

    public static final Uri CONTENT_URI = Uri.withAppendedPath(Uri.parse("content://" + AUTHORITY), DIR_PATH_SEGMENT);

    public static final String KEY_ACCESS_INFO = "access";
    public static final String KEY_ACCESS_INFO_ID = "uuid";
    public static final String KEY_ACCESS_INFO_PKG = "packageName";

    /**
     * The methods available for calls to the access content provider
     */
    public static final class Method {
        public static final String PUT = "put";
        public static final String GET = "get";
        public static final String CLEAR_ALL = "clear_all";
    }
}
