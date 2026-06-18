package com.hp.jetadvantage.link.services.accesslet.sync;

import android.os.Bundle;

import com.hp.ext.types.common.E2Type;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.common.utils.SLog;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class AuthSyncManager {
    private static final String TAG = "AuthSyncManager";
    private static final ConcurrentHashMap<String, CompletableFuture<Bundle>> packageNameToFutureMap = new ConcurrentHashMap<>();
    // Map that associates package names with authentication result objects (E2Type)
    private static final ConcurrentHashMap<String, E2Type> authResultMap = new ConcurrentHashMap<>();

    public static CompletableFuture<Bundle> createSignInFuture(String packageName) {
        CompletableFuture<Bundle> future = new CompletableFuture<>();
        packageNameToFutureMap.put(packageName, future);
        return future;
    }

    public static Result completeSignInFuture(String packageName, Bundle signInData) {
        Result result = new Result();
        CompletableFuture<Bundle> future = packageNameToFutureMap.remove(packageName);
        if (future != null) {
            if (future.isDone()) {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "SignIn for packageName: " + packageName + " was already completed or cancelled.");
            } else {
                SLog.i(TAG, "Completed SignInFuture for packageName: " + packageName);
                future.complete(signInData);
                Result.pack(result, Result.RESULT_OK);
            }
        } else {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "No SignInFuture found to complete for packageName: " + packageName + ". It might have timed out or been completed already.");
        }
        return result;
    }

    public static boolean hasFuture(String packageName) {
        return packageNameToFutureMap.containsKey(packageName);
    }

    /**
     * Store an authentication result object based on package name
     *
     * @param packageName The package name
     * @param authResult  The authentication result object (AuthenticationSuccess, AuthenticationFailed, AuthenticationCanceled, AuthenticationContinued)
     */
    public static void storeAuthResult(String packageName, E2Type authResult) {
        authResultMap.put(packageName, authResult);
    }

    /**
     * Retrieve an authentication result object based on package name
     *
     * @param packageName The package name
     * @return The authentication result object (E2Type)
     */
    public static E2Type getAuthResult(String packageName) {
        return authResultMap.get(packageName);
    }

    /**
     * Remove an authentication result object based on package name
     *
     * @param packageName The package name
     */
    public static void removeAuthResult(String packageName) {
        authResultMap.remove(packageName);
    }
}
