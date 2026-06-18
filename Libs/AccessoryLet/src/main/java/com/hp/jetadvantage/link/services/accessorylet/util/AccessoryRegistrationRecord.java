package com.hp.jetadvantage.link.services.accessorylet.util;

import static com.hp.jetadvantage.link.common.constants.LogConstants.TAG_ACCESSORY_SERVICE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.hp.jetadvantage.link.api.accessory.hid.HIDAccessoryInfo;
import com.hp.jetadvantage.link.common.constants.PackageContract;
import com.hp.jetadvantage.link.common.utils.JsonParser;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.accessorylet.model.AccessoryRegistrationInfo;

import java.util.ArrayList;
import java.util.List;

public class AccessoryRegistrationRecord {
    private static final String TAG = TAG_ACCESSORY_SERVICE + "/UTL/RR";
    private static final String ACCESSORIES_PROVIDER = "com.hp.ext.service.usbAccessories.version.1.type" +
            ".usbAccessoriesAgentRegistrationRecord";
    private static Uri queryUriForTest = null;
    /////////////////////// Public methods ///////////////////////

    /**
     * Get the registered accessories for the given app from the Workpath PackageManager database
     *
     * @param context     Context to access the content resolver
     * @param packageName Application package name
     * @return List of registered accessories for the app
     */
    public static List<AccessoryRegistrationInfo> getRegisteredAccessories(@NonNull final Context context,
                                                                           @NonNull final String packageName) {
        return queryForAccessoryRegistrationByPackageName(context, packageName);
    }

    /**
     * Check if the app has any owned accessories in the registration record
     *
     * @param context     Context to access the content resolver
     * @param packageName Application package name
     * @return true if the app has owned accessories in the registration record
     */
    public static boolean hasAppOwnedAccessories(@NonNull final Context context, @NonNull final String packageName) {
        if (context == null || TextUtils.isEmpty(packageName)) {
            return false;
        }

        List<AccessoryRegistrationInfo> accessories = queryForAccessoryRegistrationByPackageName(context, packageName);
        for (AccessoryRegistrationInfo accessory : accessories) {
            if (accessory.isOwned()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isOwnedAccessoryRegisteredByApp(@NonNull final Context context,
                                                          @NonNull final String packageName,
                                                          @NonNull HIDAccessoryInfo hidAccessoryInfo) {
        List<AccessoryRegistrationInfo> registered = queryForAccessoryRegistrationByPackageName(context, packageName);
        if(registered == null) {
            return false;
        }
        return registered.stream().anyMatch(reg -> reg.equals(hidAccessoryInfo) && reg.isOwned());
    }

    public static boolean isSharedAccessoryRegisteredByApp(@NonNull final Context context,
                                                           @NonNull final String packageName,
                                                           @NonNull HIDAccessoryInfo hidAccessoryInfo) {
        List<AccessoryRegistrationInfo> registered = queryForAccessoryRegistrationByPackageName(context, packageName);
        if(registered == null) {
            return false;
        }
        return registered.stream().anyMatch(reg -> reg.equals(hidAccessoryInfo) && reg.isShared());
    }

    protected static Uri getQueryUri() {
        if (queryUriForTest != null) {
            return queryUriForTest;
        }
        return PackageContract.PROVIDERS_CONTENT_URI;
    }

    /**
     * only for test purpose
     * @param uri
     */
    protected static void setQueryUri(Uri uri) {
        queryUriForTest = uri;
    }
    /////////////////////// Private methods ///////////////////////

    /**
     * Query the Workpath PackageManager database by package name to get accessory registration parameters.
     *
     * @param context     Context to access the content resolver
     * @param packageName Application package name
     * @return List of accessory registration information objects
     */
    @SuppressLint("Range")
    private static List<AccessoryRegistrationInfo> queryForAccessoryRegistrationByPackageName(@NonNull final Context context,
                                                                                              @NonNull final String packageName) {
        Cursor cursor = null;
        List<AccessoryRegistrationInfo> accessories = new ArrayList<>();
        try {
            String selection = PackageContract.PackageProviderEntry.PACKAGE_NAME + " = ?";
            String[] selectionArgs = new String[]{packageName};

            cursor = context.getContentResolver().query(
                    getQueryUri(),
                    null, selection, selectionArgs, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String functionType = cursor.getString(
                            cursor.getColumnIndex(PackageContract.PackageProviderEntry.FUNCTION_TYPE));

                    if (ACCESSORIES_PROVIDER.equals(functionType)) { //must, providers are multi-agents
                        String accessoryJson =
                                cursor.getString(cursor.getColumnIndex(PackageContract.PackageProviderEntry.EXT_DATA1));
                        AccessoryRegistrationInfo accessory = parseAccessoryRegistrationInfo(accessoryJson);
                        if (accessory != null) {
                            accessories.add(accessory);
                        }
                    }
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return accessories;
    }

    private static AccessoryRegistrationInfo parseAccessoryRegistrationInfo(String accessoryJson) {
        try {
            if (!TextUtils.isEmpty(accessoryJson)) {
                return JsonParser.getInstance().fromJson(accessoryJson, AccessoryRegistrationInfo.class);
            }
        } catch (Exception e) {
            SLog.e(TAG, "parseAccessoryRegistrationInfo: " + e.getMessage(), e);
        }
        return null;
    }
}
