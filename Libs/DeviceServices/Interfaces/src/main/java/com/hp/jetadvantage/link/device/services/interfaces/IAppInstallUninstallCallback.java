/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.interfaces;

import android.content.Context;
import android.content.Intent;

/**
 * Interface for receiving app install uninstall event notifications.
 */
public interface IAppInstallUninstallCallback {

    /**
     * Callback function to receive app install uninstall event notifications.
     * Cautions: this method will be called in the broadcast receiver thread, please do not do any time-consuming
     * operations in this method.
     *
     * @param context
     * @param intent intent sent by PackageManager :
     *               - intent.getAction():  PackageContract.Intent.ACTION_PACKAGE_INSTALLED,
     *                                      PackageContract.Intent.ACTION_PACKAGE_UNINSTALLED
     *               - Extra data : intent.getStringExtra(PackageContract.Intent.EXTRA_SOLUTION_UUID)
     *                              intent.getStringExtra(PackageContract.Intent.EXTRA_PACKAGE)
     */
    void onReceive(Context context, Intent intent);
}
