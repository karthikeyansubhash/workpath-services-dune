// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.common.ssp;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Internal SPS constants class. It should contain only internally used system wide SPS-specific constants.
 */
public final class SpsConstants {
    /** Internal signature level permission to be used for broadcasts etc. only inside Support apk */
    public static final String SPS_INTERNAL_PERMISSION = "com.hp.jetadvantage.link.permission.SERVICES_PERMISSION_INTERNAL";

    public static final String SDK_ACCESS_DEVICE_EVENTS_PERMISSION = "com.hp.workpath.permission.sdk.ACCESS_DEVICE_EVENTS_PERMISSION";

    public static final String SDK_ACCESS_DEVICE_USAGE_PERMISSION = "com.hp.workpath.permission.sdk.ACCESS_DEVICE_USAGE_PERMISSION";

    public static final String SDK_ACCESS_STATISTICS_PERMISSION = "com.hp.workpath.permission.sdk.ACCESS_STATISTICS_PERMISSION";

    public static final String SDK_ACCESS_SUPPLIES_PERMISSION = "com.hp.workpath.permission.sdk.ACCESS_SUPPLIES_PERMISSION";

    public static final String DISABLE_PRINTING_PORTS_PERMISSION = "com.hp.workpath.permission.admin.DISABLE_PRINTING_PORTS";
    private SpsConstants() {
        // Nothing to do here
    }

    public static boolean hasPermissionForPackage(Context context, String packageName, String permission) {
        PackageManager packageManager = context.getPackageManager();
        int permissionStatus = packageManager.checkPermission(permission, packageName);
        return permissionStatus == PackageManager.PERMISSION_GRANTED;
    }
}
