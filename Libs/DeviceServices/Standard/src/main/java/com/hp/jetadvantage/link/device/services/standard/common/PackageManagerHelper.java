package com.hp.jetadvantage.link.device.services.standard.common;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.hp.ext.service.application.ApplicationAgentRegistrationRecord;
import com.hp.jetadvantage.link.common.constants.PackageContract;

public class PackageManagerHelper {
    private static final String TAG = "PacMHelper";
    private static final String PACMAN_DB_AGENT_UUID = "agentId";
    private static final String PACMAN_DB_PACKAGE_NAME = "packageName";
    private static final String PACMAN_DB_SOLUTION_UUID = "solutionId";
    private static final String PACMAN_DB_APPLICATION_UUID = "applicationAgentId";

    /**
     * Get the agent ID of the app's package from the PACMAN
     *
     * @param context      application context of the SDK services
     * @param packageName  application's package name
     * @param agentTypeGun E2 Agent registration Record Type GUN
     * @return
     */
    public String getAgentId(Context context, String packageName, String agentTypeGun) {

        if (context == null || packageName == null || agentTypeGun == null) {
            Log.e(TAG, "Invalid param, context:" + context + " packageName:" + packageName + " agentTypeGun:" + agentTypeGun);
            return null;
        }

        if (agentTypeGun.equalsIgnoreCase(new ApplicationAgentRegistrationRecord().getTypeGUN())) {
           return getApplicationId(context, packageName);
        }

        Cursor packageCursor = null;
        String agentId = null;
        try {
            Log.d(TAG, "getAgentId : packageName: " + packageName + ", agentTypeGun:" + agentTypeGun);
            String querySelection = PACMAN_DB_PACKAGE_NAME + " = ? and "
                    + PackageContract.PackageProviderEntry.FUNCTION_TYPE + " = ?";
            String[] querySelectionArgs = new String[]{packageName, agentTypeGun};

            packageCursor = context.getContentResolver().query(
                    PackageContract.PROVIDERS_CONTENT_URI, null, querySelection, querySelectionArgs, null, null);
            if (packageCursor != null) {
                while (packageCursor.moveToNext()) {
                    int index = packageCursor.getColumnIndex(PACMAN_DB_AGENT_UUID);
                    if(index >= 0) {
                        agentId = packageCursor.getString(index);
                        Log.d(TAG, "getAgentId: agentId : " + agentId);
                        break;
                    }
                }
            } else {
                Log.d(TAG, "getAgentId : packageName: " + packageName + ", packageCursor is null");
            }
        } finally {
            if (packageCursor != null) {
                packageCursor.close();
            }
        }
        return agentId;
    }

    public String getSolutionId(Context context, String packageName) {
        String solutionId = queryUuidByPackageName(context, packageName, PACMAN_DB_SOLUTION_UUID);
        Log.d(TAG, "getSolutionId : " + solutionId);
        return solutionId;
    }

    public String getApplicationId(Context context, String packageName) {
        String applicationId = queryUuidByPackageName(context, packageName, PACMAN_DB_APPLICATION_UUID);
        Log.d(TAG, "getApplicationId : " + applicationId);
        return applicationId;
    }

    private String queryUuidByPackageName(Context context, String packageName, String columnIndexName) {
        if (context == null || packageName == null) {
            Log.e(TAG, "queryPacManPackagesTable : Invalid param, context:" + context + " packageName:" + packageName);
            return null;
        }

        Cursor packageCursor = null;
        String uuid = null;
        try {
            Log.d(TAG, "queryPacManPackagesTable : packageName: " + packageName);

            String querySelection = PACMAN_DB_PACKAGE_NAME + " = ?";
            String[] querySelectionArgs = new String[]{packageName};

            packageCursor = context.getContentResolver().query(
                    PackageContract.PACKAGES_CONTENT_URI, null, querySelection, querySelectionArgs, null, null);
            if (packageCursor != null) {
                while (packageCursor.moveToNext()) {
                    int index = packageCursor.getColumnIndex(columnIndexName);
                    Log.d(TAG, "queryPacManPackagesTable : index: " + index);
                    if(index >= 0) {
                        uuid = packageCursor.getString(index);
                        Log.d(TAG, "queryPacManPackagesTable : uuid: " + uuid);
                    }
                }
            }
        } finally {
            if (packageCursor != null) {
                packageCursor.close();
            }
        }
        Log.d(TAG, "queryPacManPackagesTable : return uuid:" + uuid + " for packageName: " + packageName);
        return uuid;
    }
}
