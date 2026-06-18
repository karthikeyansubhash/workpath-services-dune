// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.accesslet.provider;

import android.content.ActivityNotFoundException;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hp.ext.service.authentication.AuthenticationAccessPoint_InitiateLogin;
import com.hp.ext.types.common.E2Type;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.access.AccessService;
import com.hp.jetadvantage.link.api.access.Accesslet;
import com.hp.jetadvantage.link.api.access.AuthenticationAttributes;
import com.hp.jetadvantage.link.api.access.Principal;
import com.hp.jetadvantage.link.api.access.SignInAction;
import com.hp.jetadvantage.link.common.Platform;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.constants.CommonConstants;
import com.hp.jetadvantage.link.common.helper.SelectedPrinterHelper;
import com.hp.jetadvantage.link.common.model.ApiType;
import com.hp.jetadvantage.link.common.model.PrinterInfo;
import com.hp.jetadvantage.link.common.utils.JsonParser;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceAccessService;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceAuthenticationService;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceAccessService;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceAuthenticationService;
import com.hp.jetadvantage.link.device.services.standard.common.PackageManagerHelper;
import com.hp.jetadvantage.link.services.accesslet.OXPAccess;
import com.hp.jetadvantage.link.services.accesslet.PrincipalInternal;
import com.hp.jetadvantage.link.services.accesslet.adapter.AccessAdapter;
import com.hp.jetadvantage.link.services.accesslet.adapter.AuthenticationAdapter;
import com.hp.jetadvantage.link.services.accesslet.sync.AuthSyncManager;
import com.hp.jetadvantage.link.services.accesslet.util.AuthenticationUtility;
import com.hp.jetadvantage.link.services.common.exception.SdkConnectionErrorException;
import com.hp.jetadvantage.link.services.common.exception.SdkException;
import com.hp.jetadvantage.link.services.common.exception.SdkInvalidParamException;
import com.hp.jetadvantage.link.services.common.exception.SdkNotSupportedException;
import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;
import com.hp.ws.cdm.security.AuthenticationAgent;
import com.hp.ws.cdm.security.AuthenticationAgents;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Main AccessLet provider. It's responsible for serve base {@link AccessService} operations via
 * {@link #call(String, String, Bundle)} method.
 */
public final class AccessletContentProvider extends ContentProvider {

    private static final String TAG = Accesslet.TAG;

    private static final UriMatcher S_URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int ACCESSLET_CODE = 1;

    private static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.sec.Accesslet";

    private final IDeviceAccessService accessDeviceService;

    private final IDeviceAuthenticationService authenticationService;

    static {
        S_URI_MATCHER.addURI(Accesslet.AUTHORITY, Accesslet.DIR_PATH_SEGMENT, ACCESSLET_CODE);
    }

    private static final ConcurrentHashMap<String, CompletableFuture<Bundle>> responseFutures = new ConcurrentHashMap<>();

    //////// Dune begin ////////

    private String mCurrentSessionId;

    /**
     * For the Dune session info.
     * session_uuid(key) : solution_uuid;package;token;
     */
    private HashMap<String, Bundle> mSessionHash = new HashMap<>();

    /**
     * For the Dune credential info.
     * session_uuid(key) : credential info.
     */
    private HashMap<String, JSONObject> mCredentialHash = new HashMap<>();
    //////// Dune end ////////

    // Constants for SharedPreferences (ensure these match AuthenticationObserverService)
    private static final String AUTH_PREFS_NAME = "AuthCachePrefs";
    private static final String KEY_PACKAGE_NAME = "packageName";
    private static final String KEY_SESSION_ACCESS_TOKEN = "sessionAccessToken";
    private static final String KEY_SESSION_ID = "sessionId";

    // Define method name for getting cached data (ideally in AccessletContract.java)
    private static final String METHOD_GET_CACHED_AUTH_DATA = "getCachedAuthData";
    // Define keys for the bundle (ideally in AccessletContract.java)
    public static final String CACHE_KEY_PACKAGE_NAME = "cachedPackageName";
    public static final String CACHE_KEY_SESSION_ACCESS_TOKEN = "cachedSessionAccessToken";
    public static final String CACHE_KEY_SESSION_ID = "cachedSessionId";

    /**
     * Default constructor
     */
    public AccessletContentProvider() {
        accessDeviceService = new StandardDeviceAccessService();
        authenticationService = new StandardDeviceAuthenticationService();
    }

    @Override
    public boolean onCreate() {
        Log.i(TAG, "Accesslet contentprovider");
        return true;
    }

    @Override
    public int delete(@NonNull final Uri uri, final String selection, final String[] selectionArgs) {

        return 0;
    }

    @Override
    public String getType(@NonNull final Uri uri) {
        SpsPermissionHelper.ensurePermission(getContext());

        switch (S_URI_MATCHER.match(uri)) {
            case ACCESSLET_CODE:
                SLog.d(TAG, "in ACCESSLET_CODE");
                return CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unknown or Invalid URI: " + uri);
        }
    }

    @Override
    public synchronized Uri insert(@NonNull final Uri uri, final ContentValues values) {

        return null;
    }

    @Override
    public synchronized Cursor query(@NonNull final Uri uri, final String[] projection, final String selection,
                                     final String[] selectionArgs, final String sortOrder) {
        return null;
    }

    @Override
    public int update(@NonNull final Uri uri, final ContentValues values, final String selection,
                      final String[] selectionArgs) {
        return 0;
    }

    @Override
    public synchronized Bundle call(@NonNull final String method, final String arg, final Bundle extras) {
        Bundle bundle = new Bundle();
        Result.pack(bundle, Result.RESULT_OK);

        StrictMode.ThreadPolicy originalPolicy = StrictMode.getThreadPolicy();
        String appPackageName = this.getCallingPackage();

        try {
            if (getContext() == null) {
                throw new SdkInvalidParamException("Context is null");
            }

            // Handle GET_CACHED_AUTH_DATA method
            if (METHOD_GET_CACHED_AUTH_DATA.equals(method)) {
                SharedPreferences prefs = getContext().getSharedPreferences(AUTH_PREFS_NAME, Context.MODE_PRIVATE);
                bundle.putString(CACHE_KEY_PACKAGE_NAME, prefs.getString(KEY_PACKAGE_NAME, null));
                bundle.putString(CACHE_KEY_SESSION_ACCESS_TOKEN, prefs.getString(KEY_SESSION_ACCESS_TOKEN, null));
                bundle.putString(CACHE_KEY_SESSION_ID, prefs.getString(KEY_SESSION_ID, null));
                SLog.i(TAG, "Returning cached auth data: " + bundle.toString());
                return bundle; // Return early for this specific method
            }

            SpsPermissionHelper.ensurePermission(getContext());
            final PrinterInfo pi = SelectedPrinterHelper.get(getContext().getContentResolver());
            if (PrinterInfo.isEmpty(pi)) {
                SLog.e(TAG, "Device is not connected");
                throw new SdkConnectionErrorException("Device is not connected");
            }

            final StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
            StrictMode.setThreadPolicy(policy);

            // if extras is null - it means it's old API 1
            extras.setClassLoader(AuthenticationAttributes.class.getClassLoader());
            int clientVersion = extras != null && extras.containsKey(Accesslet.Keys.KEY_CLIENT_VERSION) ?
                    extras.getInt(Accesslet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION_LEVEL.ONE) : Sdk.VERSION_LEVEL.ONE;
            boolean serviceSupported = isSupported();

            if (Accesslet.Method.IS_SUPPORTED.equals(method)) {
                bundle.putBoolean(Accesslet.AA_SUPPORT_EXTRA, serviceSupported);
            } else {
                if (!serviceSupported) {
                    throw new SdkNotSupportedException("AccessService is not supported");
                }

                if (!supportClientVersionMethod(method, clientVersion)) {
                    throw new SdkNotSupportedException("AccessService is not supported");
                }

                switch (method) {
                    case Accesslet.Method.GET_PROPERTIES:
                        String principalInString = JsonParser.getInstance().toJson(AccessAdapter.getPrincipalProperties(accessDeviceService, appPackageName));
                        bundle.putString(Accesslet.PRINCIPAL_EXTRA, principalInString);
                        break;
                    case Accesslet.Method.SIGN_IN:
                        AuthenticationAttributes authenticationAttributes = extras.getParcelable(Accesslet.Keys.KEY_AUTHENTICATION);
                        extras.setClassLoader(SignInAction.class.getClassLoader());
                        SignInAction signInAction = extras.getParcelable(Accesslet.Keys.KEY_ACTION);

                        // Get the calling package name - this is the key we'll use to find the syncKey
                        String callingPkg = this.getCallingPackage();
                        SLog.i(TAG, "Method.SIGN_IN: " + callingPkg);

                        if (AuthSyncManager.hasFuture(callingPkg)) {
                            SLog.i(TAG, "prePrompt Sign-in case");
                            SLog.i(TAG, "Found syncKey for package: " + callingPkg + ". Completing future.");

                            // We need to pass the relevant sign-in information to the waiting service.
                            Bundle signInDataForService = new Bundle();
                            signInDataForService.putParcelable(Accesslet.Keys.KEY_AUTHENTICATION, authenticationAttributes);
                            signInDataForService.putParcelable(Accesslet.Keys.KEY_ACTION, signInAction);

                            // Complete the future with the sign-in data
                            Result.pack(bundle, AuthSyncManager.completeSignInFuture(callingPkg, signInDataForService));
                        } else {
                            SLog.i(TAG, "postPrompt Sign-in case");
                            Result.pack(bundle, signIn(authenticationAttributes, signInAction));
                        }
                        break;

                    case Accesslet.Method.SIGN_OUT:
                        SLog.i(TAG, "Method.SIGN_OUT");
                        Result.pack(bundle, signOut());
                        break;
                    case Accesslet.Method.START_SIGN_IN_PROCESS:
                        SLog.i(TAG, "Method.START_SIGN_IN_PROCESS: initiatedSignIn()");
                        String callerPackageName = this.getCallingPackage();
                        Result.pack(bundle, initiatedSignInProcess(callerPackageName));
                        break;

                    case Accesslet.Method.GET_TOKEN:
                        if (!extras.containsKey(Accesslet.Keys.KEY_CLIENT_ID)) {
                            throw new SdkInvalidParamException("Client id is null");
                        }

                        String pkgName = extras.getString(Accesslet.Keys.PACKAGE_NAME);
                        if (TextUtils.isEmpty(pkgName)) {
                            throw new SdkInvalidParamException("Package name is empty");
                        }

                        PackageManagerHelper pmHelper = new PackageManagerHelper();
                        String uuid = pmHelper.getApplicationId(getContext(), pkgName);
                        String clientId = extras.getString(Accesslet.Keys.KEY_CLIENT_ID);
                        if (TextUtils.isEmpty(uuid) || !(UUID.fromString(uuid) instanceof UUID)) {
                            throw new SdkInvalidParamException("Application id is null");
                        }
                        if (TextUtils.isEmpty(clientId)) {
                            throw new SdkInvalidParamException("Client id is null");
                        }
                        bundle = getDeviceToken(pi, bundle, uuid, clientId);
                        break;
                    default:
                        throw new SdkInvalidParamException("Method " + method + " is not supported");
                }
            }
        } catch (SdkException e) {
            Result.pack(bundle, e.getResult());
        } catch (Exception e) {
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        } finally {
            if (originalPolicy != null)
                StrictMode.setThreadPolicy(originalPolicy);
        }

        return bundle;
    }

    private Result signIn(final AuthenticationAttributes authenticationAttributes, SignInAction signInAction) {
        Result result = new Result();

        try {
            String packageName = this.getCallingPackage();
            if (packageName != null) {
                E2Type authResult = AuthenticationUtility.getAuthenticationResultFromAuthenticationAttributes(authenticationAttributes, signInAction);
                AuthSyncManager.storeAuthResult(packageName, authResult);
                SLog.i(TAG, "Stored authResult for packageName in signIn: " + packageName);
            } else {
                SLog.w(TAG, "Cannot store to sign-in information: packageName is null");
            }
            backToHomeScreen(getContext());
            Result.pack(result, Result.RESULT_OK);
            return result;
        } catch (Exception e) {
            SLog.e(TAG, "Sign in result exception", e);
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }

        return result;
    }

    private Result signOut() {
        Result result = new Result();
        try {
            String packageName = this.getCallingPackage();
            AuthenticationAdapter.signOut(authenticationService, packageName);
            Result.pack(result, Result.RESULT_OK);
        } catch (Exception e) {
            SLog.e(TAG, "Failed sign out from device with " + e.getMessage());
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }
        return result;
    }

    private boolean isSupported() {
        boolean access = AccessAdapter.isSupported(accessDeviceService);
        boolean auth = AuthenticationAdapter.isSupported(authenticationService);
        Log.i(TAG, "Access is supported: " + access);
        Log.i(TAG, "Authentication is supported: " + auth);
        return access && auth;
    }

    private Result initiatedSignInProcess(String callerPackageName) {
        Result result = new Result();
        Log.i(TAG, "initiatedSignIn: " + callerPackageName);
        try {
            AuthenticationAdapter.initiatedSignIn(authenticationService, callerPackageName);
            Result.pack(result, Result.RESULT_OK);
        } catch (Exception e) {
            // 404 notFound errors and 409 uiNotIdle errors are covered here.
            SLog.e(TAG, "initiatedSignIn exception: " + e.getMessage());
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }
        return result;
    }

    private void backToHomeScreen(Context context) {
        Intent intent = new Intent(CommonConstants.REQUEST_SWITCH_EVENT);
        try {
            intent.setPackage(CommonConstants.SYSTEM_SERVICE_PACKAGE_NAME);
            context.sendBroadcast(intent);
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, "backToHomeScreen: " + e.getMessage(), e);
        }
    }

    /**
     * Gets device token<br>
     *
     * @param pi       {@link PrinterInfo} connected
     * @param bundle   {@link Bundle} with requests data
     * @param uuid     application id
     * @param clientId client id
     * @return bundle with properties and {@link Result}
     */
    private Bundle getDeviceToken(final PrinterInfo pi, final Bundle bundle, String uuid, String clientId) throws Exception {
        SLog.d(TAG, "Is running in " + Platform.isPanel());
        if (!(Platform.isPanel() || Platform.isEmulator())) {
            SLog.e(TAG, "Not supported feature");
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Feature is not supported.");
            return bundle;
        }

        if (pi == null) {
            SLog.e(TAG, "Connection is failed");
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.CONNECTION_ERROR, "Request is failed. Please retry it later.");
            return bundle;
        }

        UUID appUuid = null;
        try {
            appUuid = UUID.fromString(uuid);
            SLog.d(TAG, "Valid uuid " + appUuid);
        } catch (IllegalArgumentException iae) {
            SLog.e(TAG, "Failed to get valid application uuid in mode");
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.INVALID_PARAM, "application uuid is not valid format");
            return bundle;
        }

        if (TextUtils.isEmpty(clientId)) {
            SLog.e(TAG, "Failed to get client id");
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.INVALID_PARAM, "client is not valid");
            return bundle;
        }

        try {
            if (pi.getApiType() == ApiType.OXP) {
                //ClientInfoInternal client = clients.get(0);
                //SLog.d(TAG, "start getting device token:" + client.getClientId());
                String token = OXPAccess.getDeviceToken(clientId);
                Result.pack(bundle, Result.RESULT_OK);
                bundle.putString(Result.KEY_RESULT, "{\"code\":\"" + token + "\"}");
            } else {
                Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "request is not valid");
            }
        } catch (Exception e) {
            SLog.e(TAG, "failed to get device token:" + e.getMessage());
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }

        return bundle;
    }

    private boolean supportClientVersionMethod(String method, int currentClientVersion) {
        int supportClientVersion = Sdk.VERSION_LEVEL.ONE;
        switch (method) {
            case Accesslet.Method.GET_PROPERTIES:
                supportClientVersion = Sdk.VERSION_LEVEL.ONE;
                break;
            case Accesslet.Method.SIGN_IN:
                supportClientVersion = Sdk.VERSION_LEVEL.TWO;
                break;
            case Accesslet.Method.SIGN_OUT:
                supportClientVersion = Sdk.VERSION_LEVEL.TWO;
                break;
            case Accesslet.Method.START_SIGN_IN_PROCESS:
                supportClientVersion = Sdk.VERSION_LEVEL.TWO;
                break;
            case Accesslet.Method.GET_TOKEN:
                supportClientVersion = Sdk.VERSION_LEVEL.THREE;
                break;
        }
        if (supportClientVersion > currentClientVersion) return false;
        else return true;
    }
}
