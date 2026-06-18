/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.copylet.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.util.Base64;

import com.hp.jetadvantage.link.api.copier.CopyAttributes;
import com.hp.jetadvantage.link.api.copier.Copylet;
import com.hp.jetadvantage.link.common.utils.SLog;

/**
 * SharedPreferences-based storage for Stored Copy Job attributes.
 * Stores the original CopyAttributes as Base64-encoded Parcel bytes keyed by scanJobId,
 * so that Release can restore the original scan/print options.
 *
 * Thread safety: CopyJobIntentService processes on a single HandlerThread,
 * so no concurrent access occurs.
 */
public class StoredCopyJobPreferenceStorage {
    private static final String TAG = Copylet.TAG + "/SCJPref";
    private static final String PREF_FILE_NAME = "stored_copy_job_map";

    public static void saveCopyAttributes(Context context, String key, CopyAttributes copyAttributes) {
        SLog.d(TAG, "saveCopyAttributes : key=" + key);
        Parcel parcel = Parcel.obtain();
        try {
            copyAttributes.writeToParcel(parcel, 0);
            String encoded = Base64.encodeToString(parcel.marshall(), Base64.NO_WRAP);
            getPrefs(context).edit().putString(key, encoded).apply();
        } finally {
            parcel.recycle();
        }
    }

    public static CopyAttributes getCopyAttributes(Context context, String key) {
        SLog.d(TAG, "getCopyAttributes : key=" + key);
        String encoded = getPrefs(context).getString(key, null);
        if (encoded == null) {
            SLog.d(TAG, "getCopyAttributes : not found for key=" + key);
            return null;
        }
        byte[] bytes = Base64.decode(encoded, Base64.NO_WRAP);
        Parcel parcel = Parcel.obtain();
        try {
            parcel.unmarshall(bytes, 0, bytes.length);
            parcel.setDataPosition(0);
            return CopyAttributes.CREATOR.createFromParcel(parcel);
        } catch (RuntimeException e) {
            SLog.e(TAG, "getCopyAttributes : Failed to deserialize for key=" + key + ", " + e.getMessage(), e);
            return null;
        } finally {
            parcel.recycle();
        }
    }

    public static void remove(Context context, String key) {
        SLog.d(TAG, "remove : key=" + key);
        getPrefs(context).edit().remove(key).apply();
    }

    public static void replaceKey(Context context, String oldKey, String newKey) {
        SLog.d(TAG, "replaceKey : oldKey=" + oldKey + " \u2192 newKey=" + newKey);
        SharedPreferences prefs = getPrefs(context);
        String value = prefs.getString(oldKey, null);
        if (value != null) {
            prefs.edit().remove(oldKey).putString(newKey, value).apply();
        }
    }

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }
}
