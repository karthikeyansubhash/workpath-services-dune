// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.common.providers;

import static com.hp.jetadvantage.link.common.Platform.isTestMode;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.text.TextUtils;

import androidx.core.content.ContextCompat;

import com.hp.jetadvantage.link.common.Platform;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.constants.CommonConstants;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.common.exception.SdkUnauthorizedException;
import com.hp.jetadvantage.link.services.common.ui.SpsPermissionActivity;

/**
 * Helper to work with Sps Permission ContentProvider
 */
public class SpsPermissionHelper {
    private static final String TAG = "[SDK][SPS]/s";

    private SpsPermissionHelper() {
        // No constructor is needed
    }

    /**
     * Ensures caller permission.
     *
     * @param context {@link Context}
     */
    public static void ensurePermission(final Context context) {
        if (!Platform.isPanel()) {
            return;
        }

        if (context == null) {
            throw new SecurityException("The input context is null, and SDK permissions have not been granted.");
        }

        final String callingPackage = getCallingPackage(context);

        if (TextUtils.isEmpty(callingPackage)) {
            throw new SecurityException("SDK permissions are not granted to empty packages");
        }

        try {
            ensurePermission(context, callingPackage);
        } catch (SdkUnauthorizedException sue) {
            SLog.e(TAG, sue.getMessage());
            throw new SecurityException("SDK permissions are not granted to empty packages");
        }
    }

    /**
     * Ensures caller permission.
     *
     * @param context       {@link Context}
     * @param componentName {@link ComponentName} which should be checked
     * @param allowEmpty    to allow empty component
     */
    public static void ensurePermission(final Context context, final ComponentName componentName,
                                        final boolean allowEmpty) throws SdkUnauthorizedException {
        if (!allowEmpty) {
            if (componentName == null) {
                throw new IllegalArgumentException("Empty component is not granted permissions");
            }

            if (TextUtils.isEmpty(componentName.getPackageName())) {
                throw new IllegalArgumentException("Empty component is not granted permissions");
            }
        }

        ensurePermission(context,
                componentName != null ? componentName.getPackageName() : Sdk.SERVICES_PACKAGE);
    }

    /**
     * Ensures caller permission.
     *
     * @param context            {@link Context}
     * @param callingPackageName to check permission of callingPackageName
     */
    public static void ensurePermission(final Context context, final String callingPackageName) throws SdkUnauthorizedException {
        ensurePermission(context, callingPackageName, false);
    }

    /**
     * Ensures caller permission for system application.
     *
     * @param context {@link Context}
     */
    public static void ensureSystemPermission(final Context context) {
        if (isTestMode()) return;

        final String callingPackage = getCallingPackage(context);

        if (TextUtils.isEmpty(callingPackage)) {
            throw new SecurityException("SDK permissions are not granted to empty packages");
        }

        try {
            ensureSystemPermission(context, callingPackage);
        } catch (SdkUnauthorizedException sue) {
            SLog.e(TAG, sue.getMessage());
            throw new SecurityException("SDK permissions are not granted to empty packages");
        }
    }

    /**
     * Ensures caller permission for system application.
     *
     * @param context            {@link Context}
     * @param callingPackageName to check permission of callingPackageName
     */
    public static void ensureSystemPermission(final Context context, final String callingPackageName) throws SdkUnauthorizedException {
        ensurePermission(context, callingPackageName, true);
    }


    /**
     * Ensures caller permission.
     *
     * @param context            {@link Context}
     * @param callingPackageName to check permission of callingPackageName
     * @param isForSystems       to check for link systems or not
     */
    private static void ensurePermission(final Context context, final String callingPackageName,
                                         final boolean isForSystems) throws SdkUnauthorizedException {
        if (!Platform.isPanel()) {
            // for unit test only
            return;
        }

        if (TextUtils.isEmpty(context.getPackageName())) {
            throw new SdkUnauthorizedException("Service is not allowed");
        } else {
            String[] callingPackages = context.getPackageManager().getPackagesForUid(Binder.getCallingUid());
            if (callingPackages != null) {
                for (String pkg : callingPackages) {
                    if (Sdk.SERVICES_PACKAGE.equalsIgnoreCase(pkg) || Sdk.SERVICES_PACKAGE.equalsIgnoreCase(context.getPackageName())) {
                        return;
                    }

                    if (isForSystems) {
                        if (CommonConstants.SYSTEM_PACMAN_PACKAGE_NAME.equalsIgnoreCase(pkg) ||
                                CommonConstants.SYSTEM_DOR_PACKAGE_NAME.equalsIgnoreCase(pkg) ||
                                CommonConstants.SYSTEM_SERVICE_PACKAGE_NAME.equalsIgnoreCase(pkg)) {
                            return;
                        }
                    }
                }
                throw new SdkUnauthorizedException("Service is not allowed");
            }
            SLog.w(TAG, "Caller is unknown, access is restricted");
            throw new SdkUnauthorizedException("Service is not allowed for unknown callers");

            /*[[ For checking callingPackageName with permission for mobile
            if (!context.getPackageName().equalsIgnoreCase(callingPackageName)) {
            }
            ]]*/
        }

        //TODO packageName for later
        //DON'T REMOVE THE COMMENT. DON'T OPEN packageName
        //SLog.d(TAG, context.getPackageName()+"::::"+packageName);
    }

    /**
     * Checks if there's permission was granted previously or granted once.
     * If there's no permission or granted once it'll show request dialog for
     *
     * @param context {@link Context}
     * @return true if permission was granted,
     * false - otherwise (dialog is shown)
     * @deprecated since API 1 due to permissions APIs redesign
     */
    public static boolean grantPermission(final Context context) {
        // Automatically grant permission on printer device
        if (Platform.isPanel() && !Platform.isEmulator()) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                return true;
            }
        }

        final String callingPackage = getCallingPackage(context);
        final boolean hasWritePermission = (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        final boolean hasReadPermission = (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

        if (TextUtils.isEmpty(callingPackage)) {
            throw new SecurityException("SDK permissions are not granted to empty packages");
        }

        SLog.d(TAG, "Called by Workpath SDK, permission granted");

        if (!hasWritePermission || !hasReadPermission) {
            // Start permission request activity
            final Intent intent = new Intent(context, SpsPermissionActivity.class);

            if (Platform.isPanel() && !Platform.isEmulator() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                intent.putExtra(SpsPermissionActivity.EXTRA_CALLER_ENV, "TRUE"); //Oreo
            } else {
                intent.putExtra(SpsPermissionActivity.EXTRA_CALLER_ENV, "FALSE");
            }
            intent.putExtra(SpsPermissionActivity.EXTRA_CALLER_PACKAGE, Sdk.SERVICES_PACKAGE);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        }
        return true;
    }

    /**
     * Gets calling package (first package of Uid)
     *
     * @param context {@link Context}
     * @return String package name or empty string
     */
// Caution: Avoid using this deprecated method in a content provider due to limitations when multiple apps share a UID.
// Instead, use the Android ContentProvider.getCallingPackage() API.
    @Deprecated
    public static String getCallingPackage(final Context context) {
        final int caller = Binder.getCallingUid();

        if (caller == 0) {
            return "";
        }

        final String[] packages = context.getPackageManager().getPackagesForUid(caller);

        return ((packages != null && packages.length > 0) ? packages[0] : "");
    }
}
