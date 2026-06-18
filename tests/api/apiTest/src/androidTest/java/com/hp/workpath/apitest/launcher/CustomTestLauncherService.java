package com.hp.workpath.apitest.launcher;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hp.workpath.api.Result;
import com.hp.workpath.api.Workpath;
import com.hp.workpath.api.launcher.Launcherlet;

public class CustomTestLauncherService {
    static final String LAUNCH_FROM_HOME = "launchFromDeviceHome";
    static final String EXIT_FROM_HOME = "exitFromDeviceHome";
    /**
     * Custom test launcher service to launch the app from the device home for automated testing
     * This is used to launch an API test app from the device home to get a UIContext Token from the device
     *
     * @param var0 context
     * @param var2 result
     */
    public static void launchMeFromDeviceHome(@NonNull Context var0, @Nullable Result var2) {
        Workpath.getInstance().checkPreconditions(var0);
        Bundle var3 = new Bundle();
        String var4 = var0.getPackageName();
        var3.putString("pkgname", var4);
        var3.putInt("clientVersion", 8);
        if (var2 == null) {
            var2 = new Result();
        }

        try {
            Bundle var5 = var0.getContentResolver().call(Launcherlet.CONTENT_URI, LAUNCH_FROM_HOME, (String) null, var3);
            if (null == var5) {
                Result.pack(var2, 0, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
            } else {
                Result.parse(var5, var2);
            }
        } catch (Exception var6) {
            Result.pack(var2, 0, Result.ErrorCode.SERVICE_ERROR, var6.getMessage());
        }
    }

    public static void exitFromDeviceHome(@NonNull Context var0, @Nullable Result var2) {
        Workpath.getInstance().checkPreconditions(var0);
        Bundle var3 = new Bundle();
        String var4 = var0.getPackageName();
        var3.putString("pkgname", var4);
        var3.putInt("clientVersion", 8);
        if (var2 == null) {
            var2 = new Result();
        }

        try {
            Bundle var5 = var0.getContentResolver().call(Launcherlet.CONTENT_URI, EXIT_FROM_HOME, (String) null, var3);
            if (null == var5) {
                Result.pack(var2, 0, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
            } else {
                Result.parse(var5, var2);
            }
        } catch (Exception var6) {
            Result.pack(var2, 0, Result.ErrorCode.SERVICE_ERROR, var6.getMessage());
        }
    }
}
