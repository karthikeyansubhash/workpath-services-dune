/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.standard.common;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.security.GeneralSecurityException;

/**
 * This class is used to store data in shared preferences in an encrypted form.
 * It helps prevent data inconsistency between DUNE firmware and the Workpath services app when the app is restarted
 * by Android OS or reinstalled by developers.
 * The stored data will be cleared every cold boot.
 */
public class StandardSecureAppStorage {
    private static final String TAG = Constants.TAG + "/AppStorage";
    private static final String ENCRYPTED_SHARED_PREFS_FILE_NAME = "prefs";

    public static void clearSharedPreference(Context context) {
        try {
            // Create a MasterKey.Builder
            MasterKey masterKey = new MasterKey.Builder(context.getApplicationContext())
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            // Create EncryptedSharedPreferences with the master key
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    context.getApplicationContext(),
                    ENCRYPTED_SHARED_PREFS_FILE_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            SharedPreferences.Editor spfEditor = sharedPreferences.edit();
            spfEditor.clear();
            spfEditor.apply();
            Log.d(TAG, "clearSharedPreference : cleared");
        } catch (GeneralSecurityException e) {
            Log.i(TAG, "clearSharedPreference : GeneralSecurityException=" + e.getMessage());
        } catch (Exception e) {
            Log.i(TAG, "clearSharedPreference : Exception=" + e.getMessage());
        }
    }

    public static void setSharedPreference(Context context, String key, String data) {
        try {
            // Create a MasterKey.Builder
            MasterKey masterKey = new MasterKey.Builder(context.getApplicationContext())
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            // Create EncryptedSharedPreferences with the master key
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    context.getApplicationContext(),
                    ENCRYPTED_SHARED_PREFS_FILE_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            SharedPreferences.Editor spfEditor = sharedPreferences.edit();
            spfEditor.putString(key, data);
            spfEditor.apply();
            Log.d(TAG, "setSharedPreference : Set data");
        } catch (GeneralSecurityException e) {
            Log.i(TAG, "setSharedPreference : GeneralSecurityException=" + e.getMessage());
        } catch (Exception e) {
            Log.i(TAG, "setSharedPreference : Exception=" + e.getMessage());
        }
    }

    public static String getSharedPreference(Context context, String key) {
        try {
            // Create a MasterKey.Builder
            MasterKey masterKey = new MasterKey.Builder(context.getApplicationContext())
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            // Create EncryptedSharedPreferences with the master key
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    context.getApplicationContext(),
                    ENCRYPTED_SHARED_PREFS_FILE_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            return sharedPreferences.getString(key, "");
        } catch (GeneralSecurityException e) {
            Log.i(TAG, "getSharedPreference : GeneralSecurityException=" + e.getMessage());
        } catch (Exception e) {
            Log.i(TAG, "getSharedPreference : Exception=" + e.getMessage());
        }
        return null;
    }
}
