// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.common.providers;

import androidx.annotation.Nullable;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.common.contract.SelectedAccessoryContract;
import com.hp.jetadvantage.link.common.utils.SLog;

import java.util.ArrayList;
import java.util.Map;

/**
 * Content provider which keeps the registered accessory information
 * information.
 */
public class SelectedAccessoryContentProvider extends ContentProvider {
    private static final String TAG = "Accessorylet";

    private static final String DELIM = ";";

    // segments for uri building
    private static final String CONTENT_SCHEME = "content";

    public static final Uri CONTENT_URI = new Uri.Builder().scheme(CONTENT_SCHEME).authority(SelectedAccessoryContract.AUTHORITY)
            .appendPath(SelectedAccessoryContract.DIR_PATH_SEGMENT).build();

    private static final int ACCESSORY_DATA_CODE = 1;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(SelectedAccessoryContract.AUTHORITY, SelectedAccessoryContract.DIR_PATH_SEGMENT, ACCESSORY_DATA_CODE);
    }

    // SharedPreferences files for accessory data
    private static final String ACCESSORY_DATA_FILE = "accessoryDataFile";
    // Shared preferences objects for accessory data storing / restoring
    private SharedPreferences mAccessoryDataSP;

    @Override
    public boolean onCreate() {
        if (getContext() == null) {
            return false;
        }

        mAccessoryDataSP = getContext().getSharedPreferences(getContext().getPackageName() + ACCESSORY_DATA_FILE,
                Context.MODE_PRIVATE);

        SpsPermissionHelper.grantPermission(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(final Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(final Uri uri, final ContentValues values) {
        return null;
    }

    @Override
    public int delete(final Uri uri, final String selection, final String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
        return 0;
    }

    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public Bundle call(final String method, final String arg, final Bundle extras) {
        Bundle bundle = new Bundle();

        if (getContext() == null) {
            return null;
        }

        switch (method) {
            case SelectedAccessoryContract.Method.GET: {
                Map<String, String> result = getPersistedAccessData(getContext());
                ArrayList<String> accessDatas = new ArrayList<>();
                if(result != null && result.size() > 0) {
                    SLog.d(TAG, "Selected AccessoryCP has been loaded " + result.size());
                    for(String key: result.keySet()) {
                        if(result.get(key) != null) {
                            String accessData = result.get(key);
                            if (!TextUtils.isEmpty(accessData)) {
                                accessDatas.add(accessData);
                            }
                        }
                    }
                    bundle.putSerializable(SelectedAccessoryContract.KEY_ACCESSORY_INFOS, accessDatas);
                }
                break;
            }
            case SelectedAccessoryContract.Method.PUT: {
                Preconditions.checkNotNull(extras, "Bundle is required for insertion");
                String uuid = extras.getString(SelectedAccessoryContract.KEY_ACCESSORY_INFO_ID);
                String packageName = extras.getString(SelectedAccessoryContract.KEY_ACCESSORY_INFO_PKG);

                Preconditions.checkNotNull(uuid, "Accessory information is required for insertion");
                Preconditions.checkNotNull(packageName, "Accessory information is required for insertion");

                Map<String, String> result = getPersistedAccessData(getContext());
                if(result != null) {
                    saveToSharedPrefs(SelectedAccessoryContract.KEY_ACCESSORY_INFO + ":" + uuid, uuid + DELIM + packageName);
                }
                break;
            }
            case SelectedAccessoryContract.Method.CLEAR_SELECTED: {
                Preconditions.checkNotNull(extras, "Bundle is required for selected delete");
                String uuid = extras.getString(SelectedAccessoryContract.KEY_ACCESSORY_INFO_ID);
                removeFromSharedPrefs(SelectedAccessoryContract.KEY_ACCESSORY_INFO + ":" + uuid);
                break;
            }
            case SelectedAccessoryContract.Method.CLEAR_ALL: {
                removeAllFromSharedPrefs();
                break;
            }
            default:
                // A call was made which is not understood
                throw new IllegalArgumentException("The method {" + method + " is not supported by the content provider");
        }

        SLog.d(TAG, "Selected AccessoryCP has been requested " + method);

        return bundle;
    }

    private void saveToSharedPrefs(final String key, final String accessData) {
        if (null == mAccessoryDataSP) {
            SLog.e(TAG, "SharedPrefs object is null.  Cannot save");
            return;
        }
        final SharedPreferences.Editor accessDataEditor = mAccessoryDataSP.edit();
        accessDataEditor.putString(key, accessData);
        accessDataEditor.commit();
        SLog.v(TAG, "Saved accessoryData as stringSet in sharedPreferences");
    }


    @SuppressLint("RestrictedApi")
    private static Map<String, String> getPersistedAccessData(final Context context) {
        Preconditions.checkNotNull(context, "Context cannot be null");
        final SharedPreferences sharedPrefs = context.getSharedPreferences(context.getPackageName() + ACCESSORY_DATA_FILE, Context.MODE_PRIVATE);
        if (null == sharedPrefs) {
            SLog.w(TAG, "SharedPreferences for the specified context is null. Returning null");
            return null;
        }
        Map<String, String> persistedAccessData = (Map<String, String>)sharedPrefs.getAll();
        return persistedAccessData;
    }

    private void removeAllFromSharedPrefs() {
        removeFromSharedPrefs(null);
    }

    private void removeFromSharedPrefs(final String key) {
        if (null == mAccessoryDataSP) {
            SLog.e(TAG, "SharedPrefs object is null.  Cannot remove");
            return;
        }
        final SharedPreferences.Editor accessDataEditor = mAccessoryDataSP.edit();

        if(key == null) {
            accessDataEditor.clear();
        } else {
            accessDataEditor.remove(key);
        }
        accessDataEditor.commit();

        if(key!=null) SLog.v(TAG, "Removed key " + key + " from sharedPreferences");
        else SLog.v(TAG, "Removed data from sharedPreferences");
    }
}
