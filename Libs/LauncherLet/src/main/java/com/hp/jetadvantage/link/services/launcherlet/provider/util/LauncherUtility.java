// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.launcherlet.provider.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.hp.jetadvantage.link.api.launcher.Launcherlet;
import com.hp.jetadvantage.link.common.utils.SLog;

public class LauncherUtility {
    private static final String LAUNCH_INTENT = "launchIntent";
    private static final Uri PACKAGES_CONTENT_URI = new Uri.Builder().scheme("content").authority("packages").build();

    public static String getButtonInfo(Context context, String uuid) {
        try {
            return getLaunchIntent(context, uuid);
        } catch (Exception e) {
            // java.io.IOException: REST call failed with HTTP 404:
            SLog.e(Launcherlet.TAG, "Failed to retrieve button info:" + e.getMessage());
        }
        return null;
    }

    @SuppressLint("Range")
    public static String getLaunchIntent(Context context, String uuid) {
        String querySelection = "(uuid = ?) OR (solutionUuid = ? AND isMainActivity = ?)";
        String[] querySelectionArgs = new String[]{uuid, uuid, "1"};

        Cursor packageCursor = null;
        String launchIntent = "";

        try {
            packageCursor = context.getContentResolver().query(
                    PACKAGES_CONTENT_URI, null, querySelection, querySelectionArgs, null, null);

            if (packageCursor != null) {
                while (packageCursor.moveToNext()) {
                    launchIntent = packageCursor.getString(packageCursor.getColumnIndex(LAUNCH_INTENT));
                }
            }else{
                SLog.e(Launcherlet.TAG, "PackageCursor is null");
            }
        } finally {
            if (packageCursor != null) {
                packageCursor.close();
            }
        }
        return launchIntent;
    }
}
