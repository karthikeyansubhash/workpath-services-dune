package com.hp.workpath.apitest.scanner;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Preconditions;

import com.google.gson.Gson;
import com.hp.workpath.api.Result;
import com.hp.workpath.api.Workpath;
import com.hp.workpath.api.scanner.FileOptionsAttributesCaps;
import com.hp.workpath.api.scanner.ScanAttributes;
import com.hp.workpath.api.scanner.ScanAttributesCaps;
import com.hp.workpath.api.scanner.Scanlet;
import com.hp.workpath.apitest.util.Utils;

/**
 * CustomScannerService : to log and see the actual json results from Workpath services.
 * ScanAttributes_default.json, ScanAttributesCaps_default.json are retrieved from the CustomScannerService
 */
public class CustomScannerService {
    private static final String TAG = "CustomScannerService";
    public static synchronized ScanAttributesCaps getCapabilities(@NonNull Context var0, @Nullable Result var1) {
        Workpath.getInstance().checkPreconditions(var0);
        if (var1 == null) {
            var1 = new Result();
        }

        try {
            ContentResolver var2 = var0.getContentResolver();
            Bundle var3 = new Bundle();
            var3.putInt("clientVersion", 8);
            Bundle var4 = var2.call(Scanlet.getContentUri(var2), "get_caps", (String)null, var3);
            Result.parse(var4, var1);
            if (var1.getCode() == -1) {
                String var5 = var4.getString("result");
                Utils.logLongMessage(TAG, "getCapabilities: " + var5);
                return new Gson().fromJson(var5, ScanAttributesCaps.class);
            }
        } catch (SecurityException var6) {
            Result.pack(var1, 0, Result.ErrorCode.SERVICE_ERROR, var6.getMessage());
            throw var6;
        } catch (Exception var7) {
            Result.pack(var1, 0, Result.ErrorCode.UNKNOWN, var7.getMessage());
        }
        return null;
    }

    public static synchronized ScanAttributes getDefaults(@NonNull Context var0, @Nullable Result var1) {
        Workpath.getInstance().checkPreconditions(var0);
        if (var1 == null) {
            var1 = new Result();
        }

        try {
            ContentResolver var2 = var0.getContentResolver();
            Bundle var3 = new Bundle();
            var3.putInt("clientVersion", 8);
            Bundle var4 = var2.call(Scanlet.getContentUri(var2), "get_defaults", (String)null, var3);
            Result.parse(var4, var1);
            if (var1.getCode() == -1) {
                String var5 = var4.getString("result");
                Utils.logLongMessage(TAG, "getDefaults: " + var5);
                return new Gson().fromJson(var5, ScanAttributes.class);
            }
        } catch (SecurityException var6) {
            Result.pack(var1, 0, Result.ErrorCode.SERVICE_ERROR, var6.getMessage());
            throw var6;
        } catch (Exception var7) {
            Result.pack(var1, 0, Result.ErrorCode.UNKNOWN, var7.getMessage());
            Log.e("Scanlet", "Failed to deserialize", var7);
        }

        return null;
    }

    public static synchronized FileOptionsAttributesCaps getFileOptionsCapabilities(@NonNull Context var0, @NonNull ScanAttributes.ColorMode var1, @NonNull ScanAttributes.DocumentFormat var2, @Nullable Result var3) {
        Workpath.getInstance().checkPreconditions(var0);
        Preconditions.checkNotNull(var1, "ColorMode parameter must be provided.");
        Preconditions.checkNotNull(var2, "DocumentFormat parameter must be provided.");
        if (var3 == null) {
            var3 = new Result();
        }

        try {
            ContentResolver var4 = var0.getContentResolver();
            Bundle var5 = new Bundle();
            var5.putInt("clientVersion", 8);
            var5.putSerializable("colorMode", com.hp.jetadvantage.link.api.scanner.ScanAttributes.ColorMode.valueOf(var1.name()));
            var5.putSerializable("documentFormat", com.hp.jetadvantage.link.api.scanner.ScanAttributes.DocumentFormat.valueOf(var2.name()));
            Bundle var6 = var4.call(Scanlet.getContentUri(var4), "get_file_options_caps", (String)null, var5);
            Result.parse(var6, var3);
            if (var3.getCode() == -1) {
                String var7 = var6.getString("result");
                Utils.logLongMessage(TAG, "getFileOptionsCapabilities: " + var7);
                return new Gson().fromJson(var7, FileOptionsAttributesCaps.class);
            }
        } catch (SecurityException var8) {
            Result.pack(var3, 0, Result.ErrorCode.SERVICE_ERROR, var8.getMessage());
            throw var8;
        } catch (Exception var9) {
            Result.pack(var3, 0, Result.ErrorCode.UNKNOWN, var9.getMessage());
        }

        return null;
    }
}
