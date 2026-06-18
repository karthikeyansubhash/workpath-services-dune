// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.common.services;

import androidx.annotation.Nullable;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Base64;

import com.hp.jetadvantage.link.common.utils.SLog;

import java.security.Signature;

public class ServicesToLibraryService extends Service {
    private static String TAG = ServicesToLibraryService.class.getCanonicalName();

    public static ScanEncryptCallback scanEncryptCallback;

    public interface ScanEncryptCallback {
        byte[] sign(Signature signature, byte[] data);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        SLog.d(TAG, "onBind called");
        return binder;
    }

    private final IServicesToLibraryInterface.Stub binder = new IServicesToLibraryInterface.Stub() {
        public String encrypt(String algorithm, String data) {
            //SLog.d(TAG, "Encrypting with algorithm " + algorithm + " and data " + data);
            SLog.d(TAG, "Requested to get data");
            if (scanEncryptCallback != null) {
                try {
                    Signature signature = Signature.getInstance(algorithm);
                    byte[] encrypted = scanEncryptCallback.sign(signature, Base64.decode(data, Base64.DEFAULT));
                    return Base64.encodeToString(encrypted, Base64.NO_WRAP);
                } catch (Exception e) {
                    SLog.e(TAG, "Failed to encrypt data" + e.getMessage());
                }
            }
            return null;
        }
    };

}
