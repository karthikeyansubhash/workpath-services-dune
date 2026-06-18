// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api;

import androidx.annotation.NonNull;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.api.printer.intent.PrintRequestIntent;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.annotation.CommonApi;
import com.hp.jetadvantage.link.common.mfpsversion.helper.ApiLevel;

/**
 * <p>This class includes all initialization API that uses the JetAdvantage Link SDK service.</p>
 * <p>All clients which access to Link SDK has to call <b>initialize()</b> method, then can catch the result to notify the success or error to a user.</p>
 *
 * @since API 1
 */
@CommonApi
public class JetAdvantageLink {
    private static final String TAG = "JetAdvantageLink";

    private static final JetAdvantageLink INSTANCE = new JetAdvantageLink();

    private static final String SERVICES_PERMISSION = "com.hp.jetadvantage.link.permission.SERVICES_PERMISSION";

    private boolean isInitialized = false;

    private JetAdvantageLink() {
    }

    /**
     * <p>Returns an instance of JetAdvantageLink SDK service when service is ready to use.</p>
     *
     * @return JetAdvantageLink The object of JetAdvantageLink SDK service
     */
    @SuppressWarnings("unused")
    public static JetAdvantageLink getInstance() {
        return INSTANCE;
    }

    /**
     * <p>Provides the version code of JetAdvantageLink SDK API which is running in an application.</p>
     *
     * @return versionCode The version code of JetAdvantageLink SDK API
     * @since API 1
     */
    @SuppressWarnings({"unused", "SameReturnValue"})
    public int getVersionCode() {
        return Sdk.VERSION.PLATFORM_LEVEL;
    }

    /**
     * </p>Provides the version name of JetAdvantageLink SDK API which is running in an application.</p>
     *
     * @return versionName The version name of JetAdvantageLink SDK API
     * @since API 1
     */
    @SuppressWarnings({"unused", "SameReturnValue"})
    public String getVersionName() {
        return Sdk.VERSION.VERSION_NAME;
    }

    /**
     * <p>Initializes Link SDK service.
     * If it fails, method throws an exception that includes the information of the reasons. Reasons are :
     * <ul>
     *     <li>SDK service is not installed on a printer</li>
     *     <li>SDK service needs to update</li>
     *     <li>SDK service needs a permission</li>
     * </ul>
     * </p>
     *
     * @param context The Context in which the application is running. If it's null, initialization will be failed.
     * @throws NullPointerException If context is null
     * @throws SsdkUnsupportedException If SDK needs to update or SDK is not compatible version or JetAdvantage Link SDK Services is not installed
     * @throws SecurityException        If SDK doesn't have the permission to access an application
     * @since API 1
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    @SuppressLint("RestrictedApi")
    public void initialize(@NonNull final Context context) throws SsdkUnsupportedException, SecurityException {
        Preconditions.checkNotNull(context, "Context is required to obtain the initialize");

        // Check if JetAdvantageLinkServices is present in the system
        if (!isInstalled(context.getPackageManager())) {
            throw new SsdkUnsupportedException("Workpath SDK is not installed",
                    SsdkUnsupportedException.LIBRARY_NOT_INSTALLED);
        }

        SecurityException securityException = null;
        VersionState state = VersionState.COMPATIBLE;

        try {
            // JetAdvantage Link Services application is installed, check the version
            state = getVersionState(context.getContentResolver());
        } catch (SecurityException e) {
            securityException = e;
        } catch (Exception e) {
            throw new SsdkUnsupportedException("Workpath SDK is not installed",
                    SsdkUnsupportedException.LIBRARY_NOT_INSTALLED);
        }

        if (securityException != null) {
            if (securityException.getMessage().contains(SERVICES_PERMISSION)) {
                state = VersionState.MFP_SERVICES_NEEDS_UPDATE;
            } else {
                throw securityException;
            }
        }

        if (VersionState.MFP_SERVICES_NEEDS_UPDATE == state) {
            throw new SsdkUnsupportedException("Workpath SDK requires update",
                    SsdkUnsupportedException.LIBRARY_UPDATE_IS_REQUIRED);
        }

        isInitialized = true;
    }

    /**
     * @hide internal use
     */
    @SuppressLint("RestrictedApi")
    public void checkPreconditions(Context context){
        Preconditions.checkNotNull(context, "Context must be provided.");
        Preconditions.checkNotNull(context.getContentResolver(), "Content resolver is required for device connection checks");
        Preconditions.checkState(isInitialized, "JetAdvantageLink class instance is not initialized.");
    }

    /**
     * Compares between the client version and the Support Library versions
     */
    @CommonApi
    private enum VersionState {
        /**
         * The Dependent Package version matches the client's version.
         */
        COMPATIBLE,
        /**
         * The Dependent Package version has a version which is older compared
         * to the client's version.
         */
        MFP_SERVICES_NEEDS_UPDATE,
    }

    /**
     * <p>Determines whether the Dependent Package or HP Workpath SDK is
     * installed.</p>
     *
     * @param packageMgr PackageManager used in looking for the Dependent
     *                   Package APK.
     * @return True, if Dependent Package or HP Workpath SDK is installed.
     * Otherwise, false.
     */
    @SuppressLint("RestrictedApi")
    private static boolean isInstalled(final PackageManager packageMgr) {
        Preconditions.checkNotNull(packageMgr, "PackageManager is necessary to verify that Dependent Packages are installed");

        final Intent intent = new Intent();
        intent.setAction(PrintRequestIntent.ACTION);
        return !packageMgr.queryBroadcastReceivers(intent, PackageManager.GET_RESOLVED_FILTER).isEmpty();
    }

    /**
     * Determines how the client version and the Dependent Package version
     * compare to each other.
     *
     * @return The version difference between Dependent Package and this library
     */
    private static VersionState getVersionState(final ContentResolver cr) {
        final int mfpsSdkLevel = ApiLevel.get(cr);
        if (Sdk.VERSION.LEVEL > mfpsSdkLevel) {
            return VersionState.MFP_SERVICES_NEEDS_UPDATE;
        }

        return VersionState.COMPATIBLE;
    }
}
