// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.common.contract;

import android.net.Uri;

/**
 * Specifies information necessary to communicate with the LSM Content provider
 * 
 * @author APS
 * 
 */
public class SelectedPrinterContract {
    public static final String AUTHORITY = "com.hp.jetadvantage.link.authority.lsmcp.oxp";

    public static final String DIR_PATH_SEGMENT = "lsmcp";

    public static final Uri CONTENT_URI = Uri.withAppendedPath(Uri.parse("content://" + AUTHORITY), DIR_PATH_SEGMENT);

    public static final String KEY_PRINTER_INFO = "pi";

    public static final String KEY_UPDATE = "update";

    public static final String KEY_CLIENT_VERSION = "clientVersion";

    /**
     * The methods available for calls to the LSM content provider
     * 
     * @author APS
     * 
     */
    public static final class Method {
        public static final String ADD = "add";
        public static final String CLEAR = "clear";
        public static final String GET = "get";
    }
}
