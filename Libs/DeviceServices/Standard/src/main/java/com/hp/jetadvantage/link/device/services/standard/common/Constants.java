/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.standard.common;

import static com.hp.jetadvantage.link.common.constants.LogConstants.PREFIX_WORKPATH_SERVICE;

public class Constants {

    /**
     * Define Final String Constants for logging tags
     */
    public static final String TAG = PREFIX_WORKPATH_SERVICE + "DSS";
    public static final String BOOT_TAG = PREFIX_WORKPATH_SERVICE + "[BOOT]DSS";  //booting logs
    public static final String PERF_TAG = PREFIX_WORKPATH_SERVICE + "[PERF]DSS";  //performance logs


    /**
     * Define Final String Constants for http status codes
     */
    public static final int HTTP_STATUS_CODE_UNAUTHORIZED = 401;
    public static final int HTTP_STATUS_CODE_FORBIDDEN = 403;
    public static final int HTTP_STATUS_CODE_NOT_FOUND = 404;
    public static final int HTTP_STATUS_CODE_METHOD_NOT_ALLOWED = 405;

    /**
     * Define Final String Constants for device information
     */
    public static final String DEVICE_INTERNAL_HOSTNAME = "fwprinter2";

    /**
     * Define Final String Constants for tests
     */
    public static final String TEST_PACKAGE_NAME = "8a734ba3-0d22-4f8d-9c8b-b3c3a7a4d1a6";

    /**
     *  Define Final String Constants for xxx
     */
}
