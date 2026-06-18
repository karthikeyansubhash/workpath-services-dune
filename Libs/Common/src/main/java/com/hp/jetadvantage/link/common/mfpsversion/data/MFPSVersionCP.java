// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.common.mfpsversion.data;

import android.net.Uri;

/**
 * SPS Version CP
 *
 */
public class MFPSVersionCP {
    
    /**
     * CP for getting API level information at run time
     */
    public static final String AUTHORITY = "com.hp.jetadvantage.link.authority.spsvcp";

    public static final String DIR_PATH_SEGMENT = "spsvcp";

    public static final Uri CONTENT_URI = Uri.withAppendedPath(Uri.parse("content://" + AUTHORITY), DIR_PATH_SEGMENT);

    /**
     * The columns in the cursor for the content provider
     */
    public static final class Contract {
        public static final String KEY_VERSION = "version";
        
        public static final String KEY_API_LEVEL = "apilevel";

        public static final String KEY_API_NAME_LEVEL = "apiNamelevel";
        
        private Contract() {
        }
    }

    /**
     * The methods available for calls to the content provider
     */
    public static final class Method {
        public static final String GET_VERSION = "getversion";
        /** arg for this call can contain callers api version */
        public static final String GET_API_LEVEL = "getapilevel";
        public static final String GET_API_LEVEL_INTERNAL = "getapilevelinternal";
    }
    
}
