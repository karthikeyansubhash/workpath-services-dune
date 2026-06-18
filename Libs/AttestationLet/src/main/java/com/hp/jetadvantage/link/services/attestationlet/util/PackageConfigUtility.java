// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.attestationlet.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.hp.jetadvantage.link.common.constants.PackageContract;
import com.hp.jetadvantage.link.common.utils.SLog;

public class PackageConfigUtility {
    private static final String TAG = "[SDK][PKGCON]";

    @SuppressLint("Range")
    public static Bundle getPackageAttestationInfoByUUID(@NonNull final Context context, @NonNull final String uuid) {
        Cursor packageCursor = null;
        try {
            SLog.d(TAG, "Get pkg link config info with " + uuid);
            packageCursor = context.getContentResolver().query(
                    Uri.withAppendedPath(PackageContract.PACKAGES_ATTESTATION_CONTENT_URI, uuid),
                    null, null, null, null, null);

            if (packageCursor != null) {
                if (packageCursor.moveToNext()) {
                    SLog.d(TAG, "Found pkg link config info with " + uuid);
                    String auth = packageCursor.getString(
                            packageCursor.getColumnIndex(PackageContract.PackageAttestationEntry.AUTH));
                    String user = packageCursor.getString(
                            packageCursor.getColumnIndex(PackageContract.PackageAttestationEntry.USER));
                    String ldbKey = packageCursor.getString(
                            packageCursor.getColumnIndex(PackageContract.PackageAttestationEntry.KEY));
                    String data = packageCursor.getString(
                            packageCursor.getColumnIndex(PackageContract.PackageAttestationEntry.DATA));
                    Bundle bundle = new Bundle();
                    bundle.putString(PackageContract.PackageAttestationEntry.SOLUTION_ID, uuid);
                    bundle.putString(PackageContract.PackageAttestationEntry.AUTH, auth);
                    bundle.putString(PackageContract.PackageAttestationEntry.USER, user);
                    bundle.putString(PackageContract.PackageAttestationEntry.KEY, ldbKey);
                    bundle.putString(PackageContract.PackageAttestationEntry.DATA, data);
                    return bundle;
                }
            }
        } catch (Exception e) {
            SLog.e(TAG, "Couldn't find pkg link config info with :" + uuid + ",reason: " + e.getMessage());
        } finally {
            if (packageCursor != null) {
                packageCursor.close();
            }
        }
        return null;
    }

    @SuppressLint("Range")
    public static Bundle getPackageInformationByUUID(@NonNull final Context context, @NonNull final String uuid) {
        Cursor packageCursor = null;
        try {
            SLog.d(TAG, "Get pkg link config info with " + uuid);

            Uri solutionUri = Uri.withAppendedPath(
                    Uri.withAppendedPath(PackageContract.PACKAGES_CONTENT_URI, PackageContract.PACKAGES_SOLUTION_PATH_SEGMENT),
                    uuid);
            packageCursor = context.getContentResolver().query(
                    solutionUri,
                    null, null, null, null, null);

            if (packageCursor != null) {
                if (packageCursor.moveToNext()) {
                    SLog.d(TAG, "Found pkg info with " + uuid);

                    String appUUID = packageCursor.getString(
                            packageCursor.getColumnIndex(PackageContract.PackageEntry.APPLICATION_AGENT_ID));
                    String pkgName = packageCursor.getString(
                            packageCursor.getColumnIndex(PackageContract.PackageEntry.PACKAGE_NAME));
                    String version = packageCursor.getString(
                            packageCursor.getColumnIndex(PackageContract.PackageEntry.HPK2_VERSION));

                    Bundle bundle = new Bundle();
                    bundle.putString(PackageContract.PackageEntry.APPLICATION_AGENT_ID, appUUID);
                    bundle.putString(PackageContract.PackageEntry.PACKAGE_NAME, pkgName);
                    bundle.putString(PackageContract.PackageEntry.HPK2_VERSION, version);
                    return bundle;
                }
            }
        } catch (Exception e) {
            SLog.e(TAG, "Couldn't find pkg info with :" + uuid + ",reason: " + e.getMessage());
        } finally {
            if (packageCursor != null) {
                packageCursor.close();
            }
        }

        return null;
    }

    @SuppressLint("Range")
    public static String getSolutionIdByPackageName(@NonNull final Context context, @NonNull final String packageName) {
        Cursor packageCursor = null;
        try {
            String selection = PackageContract.PackageEntry.PACKAGE_NAME + " = ?";
            String[] selectionArgs = new String[]{packageName};

            packageCursor = context.getContentResolver().query(PackageContract.PACKAGES_CONTENT_URI,
                    null, selection, selectionArgs, null, null);

            if (packageCursor != null) {
                if (packageCursor.moveToNext()) {
                    String solutionId = packageCursor.getString(
                            packageCursor.getColumnIndex(PackageContract.PackageEntry.SOLUTION_ID));
                    return solutionId;
                }
            }
        } finally {
            if (packageCursor != null) {
                packageCursor.close();
            }
        }

        return null;
    }
}
