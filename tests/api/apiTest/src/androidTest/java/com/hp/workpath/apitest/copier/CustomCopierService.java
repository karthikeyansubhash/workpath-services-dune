package com.hp.workpath.apitest.copier;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.hp.workpath.api.Result;
import com.hp.workpath.api.Workpath;
import com.hp.workpath.api.copier.CopyAttributes;
import com.hp.workpath.api.copier.CopyAttributesCaps;
import com.hp.workpath.api.copier.Copylet;
import com.hp.workpath.apitest.util.Utils;

public class CustomCopierService {
    private static final String TAG = "CustomCopierService";

    public static synchronized CopyAttributesCaps getCapabilities(@NonNull Context var0, @Nullable Result var1) {
        Workpath.getInstance().checkPreconditions(var0);
        if (var1 == null) {
            var1 = new Result();
        }

        try {
            ContentResolver var2 = var0.getContentResolver();
            Bundle var3 = new Bundle();
            var3.putInt("clientVersion", 8);
            Bundle var4 = var2.call(Copylet.getContentUri(var2), "get_caps", (String) null, var3);
            Result.parse(var4, var1);
            if (var1.getCode() == -1) {
                String var5 = var4.getString("result");
                Utils.logLongMessage(TAG, "getCapabilities: " + var5);
                return new Gson().fromJson(var5, CopyAttributesCaps.class);
            }
        } catch (SecurityException var6) {
            Result.pack(var1, 0, Result.ErrorCode.SERVICE_ERROR, var6.getMessage());
            throw var6;
        } catch (Exception var7) {
            Result.pack(var1, 0, Result.ErrorCode.UNKNOWN, var7.getMessage());
            Log.e("Copylet", "Failed to deserialize: " + var7.getMessage());
        }

        return null;
    }

    public static synchronized CopyAttributes getDefaults(@NonNull Context var0, @Nullable Result var1) {
        Workpath.getInstance().checkPreconditions(var0);
        if (var1 == null) {
            var1 = new Result();
        }

        try {
            ContentResolver var2 = var0.getContentResolver();
            Bundle var3 = new Bundle();
            var3.putInt("clientVersion", 8);
            Bundle var4 = var2.call(Copylet.getContentUri(var2), "get_defaults", (String)null, var3);
            Result.parse(var4, var1);
            if (var1.getCode() == -1) {
                String var5 = var4.getString("result");
                Utils.logLongMessage(TAG, "getDefaults: " + var5);
                return new Gson().fromJson(var5, CopyAttributes.class);
            }
        } catch (SecurityException var6) {
            Result.pack(var1, 0, Result.ErrorCode.SERVICE_ERROR, var6.getMessage());
            throw var6;
        } catch (Exception var7) {
            Result.pack(var1, 0, Result.ErrorCode.UNKNOWN, var7.getMessage());
            Log.e("Copylet", "Failed to deserialize: " + var7.getMessage());
        }

        return null;
    }
}
