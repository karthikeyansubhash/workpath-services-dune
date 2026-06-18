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

import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.common.contract.SelectedAccessContract;
import com.hp.jetadvantage.link.common.utils.SLog;

import java.util.Map;

/**
 * Content provider which keeps the access information along with the authentication state
 * information.
 */
public class SelectedAccessContentProvider extends ContentProvider {
    private static final String TAG = "Accesslet";

    private static final String DELIM = ";";

    // segments for uri building
    private static final String CONTENT_SCHEME = "content";

    public static final Uri CONTENT_URI = new Uri.Builder().scheme(CONTENT_SCHEME).authority(SelectedAccessContract.AUTHORITY)
            .appendPath(SelectedAccessContract.DIR_PATH_SEGMENT).build();

    private static final int AUTH_DATA_CODE = 1;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(SelectedAccessContract.AUTHORITY, SelectedAccessContract.DIR_PATH_SEGMENT, AUTH_DATA_CODE);
    }

    // SharedPreferences files for access data
    private static final String ACCESS_DATA_FILE = "accessDataFile";
    // Shared preferences objects for access data storing / restoring
    private SharedPreferences mAccessDataSP;

    @Override
    public boolean onCreate() {
        if (getContext() == null) {
            return false;
        }

        mAccessDataSP = getContext().getSharedPreferences(getContext().getPackageName() + ACCESS_DATA_FILE,
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
            case SelectedAccessContract.Method.GET: {
                Map<String, String> result = getPersistedAccessData(getContext());
                if(result != null && result.size() == 1) {
                    String[] accessData = result.get(SelectedAccessContract.KEY_ACCESS_INFO).split(DELIM);
                    if(accessData != null && accessData.length == 2) {
                        bundle.putString(SelectedAccessContract.KEY_ACCESS_INFO_ID, accessData[0]);
                        bundle.putString(SelectedAccessContract.KEY_ACCESS_INFO_PKG, accessData[1]);
                    }
                    SLog.d(TAG, "Selected AccessCP has been loaded " + result.size());
                }
                break;
            }
            case SelectedAccessContract.Method.PUT: {
                Preconditions.checkNotNull(extras, "Bundle is required for insertion");
                String uuid = extras.getString(SelectedAccessContract.KEY_ACCESS_INFO_ID);
                String packageName = extras.getString(SelectedAccessContract.KEY_ACCESS_INFO_PKG);

                Preconditions.checkNotNull(uuid, "Access information is required for insertion");
                Preconditions.checkNotNull(packageName, "Access information is required for insertion");

                saveToSharedPrefs(SelectedAccessContract.KEY_ACCESS_INFO, uuid + DELIM +packageName);
                break;
            }
            case SelectedAccessContract.Method.CLEAR_ALL: {
                removeAllFromSharedPrefs();
                break;
            }
            default:
                // A call was made which is not understood
                throw new IllegalArgumentException("The method {" + method + " is not supported by the content provider");
        }

        SLog.d(TAG, "Selected AccessCP has been requested " + method);

        return bundle;
    }

    private void saveToSharedPrefs(final String key, final String accessData) {
        if (null == mAccessDataSP) {
            SLog.e(TAG, "SharedPrefs object is null.  Cannot save");
            return;
        }
        final SharedPreferences.Editor accessDataEditor = mAccessDataSP.edit();
        accessDataEditor.putString(key, accessData);
        accessDataEditor.commit();
        SLog.v(TAG, "Saved accessData as stringSet in sharedPreferences");
    }


    @SuppressLint("RestrictedApi")
    private static Map<String, String> getPersistedAccessData(final Context context) {
        Preconditions.checkNotNull(context, "Context cannot be null");
        final SharedPreferences sharedPrefs = context.getSharedPreferences(context.getPackageName() + ACCESS_DATA_FILE, Context.MODE_PRIVATE);
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
        if (null == mAccessDataSP) {
            SLog.e(TAG, "SharedPrefs object is null.  Cannot remove");
            return;
        }
        final SharedPreferences.Editor accessDataEditor = mAccessDataSP.edit();

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
