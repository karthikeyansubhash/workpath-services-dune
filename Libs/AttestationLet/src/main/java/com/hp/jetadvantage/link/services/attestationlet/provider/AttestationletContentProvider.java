// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.attestationlet.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.reflect.TypeToken;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.services.attestationlet.model.AppPlatformConfiguration;
import com.hp.jetadvantage.link.services.attestationlet.model.AttestationAttributes;
import com.hp.jetadvantage.link.api.attestation.Attestationlet;
import com.hp.jetadvantage.link.services.attestationlet.ClientInfoInternal;
import com.hp.jetadvantage.link.common.Platform;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.helper.SelectedPrinterHelper;
import com.hp.jetadvantage.link.common.model.PrinterInfo;
import com.hp.jetadvantage.link.common.utils.JsonParser;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.attestationlet.model.AttestationInfo;
import com.hp.jetadvantage.link.services.attestationlet.model.ExecutionMode;
import com.hp.jetadvantage.link.services.attestationlet.request.AttestationRequest;
import com.hp.jetadvantage.link.services.attestationlet.util.PackageConfigUtility;
import com.hp.jetadvantage.link.common.constants.PackageContract;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceAttestationService;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceAttestationService;
import com.hp.jetadvantage.link.services.attestationlet.adapter.AttestationAdapter;
import com.hp.jetadvantage.link.services.attestationlet.model.AppTokenResponse;
import com.hp.jetadvantage.link.services.common.exception.SdkException;
import com.hp.jetadvantage.link.services.common.exception.SdkInvalidParamException;
import com.hp.jetadvantage.link.services.common.exception.SdkNotSupportedException;
import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.ProxyInfo;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Main AttestationLet provider. It's responsible for serve base {@link com.hp.jetadvantage.link.api.attestation.AttestationService} operations via
 * {@link #call(String, String, Bundle)} method.<br>
 */
public final class AttestationletContentProvider extends ContentProvider {

    private static final String TAG = Attestationlet.TAG;

    private static final UriMatcher S_URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int ATTESTATIONLET_CODE = 1;

    private static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.sec.Attestationlet";

    static {
        S_URI_MATCHER.addURI(Attestationlet.AUTHORITY, Attestationlet.DIR_PATH_SEGMENT, ATTESTATIONLET_CODE);
    }

    private final IDeviceAttestationService attestationDeviceService;

    private static final String X_MODE_DEBUG = "debug";
    private static final String X_MODE_PROD = "production";
    private static final String X_HOST_PROD = "core.api.hp.com";
    private static final String X_HOST_STAGE = "stage.api.hp.com";
    private static final String X_HOST_TEST = "test.api.hp.com";
    private static final String X_TOKEN_CLIENT_ID_PROD = "BNOvLb8fJm4aGmGP2gpxGD7HGCzzCUyO";
    private static final String X_TOKEN_CLIENT_ID_STAGE = "7pwBwaRtxMd54ZVmBtmIe6U4S2A8DuEM";
    private static final String X_TOKEN_CLIENT_ID_TEST = "GcYi4lHjreElWu6eAQ0A5GxFdGKFRQ4s";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String DEVICE_INFO_PATH = "/cdm/e2WorkpathInterop/v1/deviceInfo";

    /**
     * Default constructor
     */
    public AttestationletContentProvider() {
        this.attestationDeviceService = createAttestationDeviceService();
    }

    /**
     * Constructor for testing
     */
    public AttestationletContentProvider(IDeviceAttestationService attestationService) {
        this.attestationDeviceService = attestationService;
    }

    private static IDeviceAttestationService createAttestationDeviceService() {
        // Keep DeviceServices abstraction: in simulation environments we can skip network.
        if (Platform.isEmulator()) {
            try {
                Class<?> clazz = Class.forName("com.hp.jetadvantage.link.device.services.sim.SimDeviceAttestationService");
                Object instance = clazz.newInstance();
                if (instance instanceof IDeviceAttestationService) {
                    return (IDeviceAttestationService) instance;
                }
            } catch (Throwable ignored) {
                // Fall back to standard implementation.
            }
        }

        return new StandardDeviceAttestationService();
    }

    @Override
    public boolean onCreate() {
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
            case ATTESTATIONLET_CODE:
                SLog.d(TAG, "in ATTESTATIONLET_CODE");
                return CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unknown or Invalid URI: " + uri);
        }
    }

    @Override
    public Uri insert(@NonNull final Uri uri, final ContentValues values) {
        return null;
    }

    @Override
    public Cursor query(@NonNull final Uri uri, final String[] projection, final String selection,
                                     final String[] selectionArgs, final String sortOrder) {
        return null;
    }

    @Override
    public int update(@NonNull final Uri uri, final ContentValues values, final String selection,
                      final String[] selectionArgs) {
        return 0;
    }

    @Override
    public Bundle call(@NonNull final String method, final String arg, final Bundle extras) {
        Bundle bundle = new Bundle();
        Result.pack(bundle, Result.RESULT_OK);

        StrictMode.ThreadPolicy originalPolicy = StrictMode.getThreadPolicy();

        try {
            if (getContext() == null) {
                throw new SdkInvalidParamException("Context is null");
            }

            SpsPermissionHelper.ensurePermission(getContext());
            final PrinterInfo pi = SelectedPrinterHelper.get(getContext().getContentResolver());
            // TEST TEMP: Original check commented out to avoid throwing exception when not connected
            // ORIGINAL (revert):
            // if (PrinterInfo.isEmpty(pi)) {
            //     SLog.e(TAG, "Device is not connected");
            //     throw new SdkConnectionErrorException("Device is not connected");
            // }

            final StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
            StrictMode.setThreadPolicy(policy);

            if (Attestationlet.Method.IS_SUPPORTED.equals(method)) {
                bundle.putBoolean(Attestationlet.ATT_SUPPORT_EXTRA, true);
            } else {
                extras.setClassLoader(AttestationAttributes.class.getClassLoader());

                String pkgName = extras.getString(Attestationlet.Keys.PACKAGE_NAME);
                if (TextUtils.isEmpty(pkgName)) {
                    throw new SdkInvalidParamException("Package name is empty");
                }
                SLog.v(TAG, "method: " + method + " pkgName: " + pkgName);

                // if extras is null - it means it's old API 1
                int clientVersion = extras != null && extras.containsKey(Attestationlet.Keys.KEY_CLIENT_VERSION) ?
                        extras.getInt(Attestationlet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION_LEVEL.ONE) : Sdk.VERSION_LEVEL.ONE;

                if (!supportClientVersionMethod(method, clientVersion)) {
                    throw new SdkNotSupportedException("AttestationService is not supported");
                }

                switch (method) {
                    case Attestationlet.Method.GET_TOKEN:
                        // get solution ID
                        String solutionId = PackageConfigUtility.getSolutionIdByPackageName(getContext(), pkgName);
                        if(TextUtils.isEmpty(solutionId) || !(UUID.fromString(solutionId) instanceof UUID)) {
                            throw new SdkInvalidParamException("Solution id is null");
                        }
                        SLog.d(TAG, "call getAppToken for " + solutionId);
                        bundle = getAppToken(pi, bundle, pkgName, solutionId);
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

    private boolean supportClientVersionMethod(String method, int currentClientVersion) {
        int supportClientVersion = Sdk.VERSION_LEVEL.ONE;
        switch (method) {
            case Attestationlet.Method.GET_TOKEN:
                supportClientVersion = Sdk.VERSION_LEVEL.THREE;
                break;
        }
        if(supportClientVersion > currentClientVersion) return false;
        else return true;
    }

    /**
     * Gets application token<br>
     *
     * @param pi     {@link PrinterInfo} connected
     * @param bundle {@link Bundle} with requests data
     * @param solutionId solution id
     * @return bundle with properties and {@link Result}
     */
    private Bundle getAppToken(final PrinterInfo pi, final Bundle bundle, String packageName, String solutionId) throws Exception{
        SLog.d(TAG, "Is running in " + Platform.isPanel());
        if(!(Platform.isPanel() || Platform.isEmulator())){
            SLog.e(TAG, "Not supported feature");
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Feature is not supported.");
            return bundle;
        }

        if (pi == null) {
            SLog.e(TAG, "Connection is failed");
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.CONNECTION_ERROR, "Request is failed. Please retry it later.");
            return bundle;
        }

        AppPlatformConfiguration config = getAppPlatformConfiguration();
//        config.setAndroidDebugBridgeEnabled(true);
        SLog.d(TAG, "App config: ldb=" + config.isAndroidDebugBridgeEnabled() + ", executionMode=" + config.getExecutionMode() + ", host=" + config.getHost());

        if(TextUtils.isEmpty(solutionId)) {
            SLog.e(TAG, "Failed to get solution id");
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.INVALID_PARAM, "solution id is not existed");
            return bundle;
        }

        try {
            UUID appUuid = UUID.fromString(solutionId);
            SLog.d(TAG, "Valid solution id: " + appUuid);
        } catch (IllegalArgumentException iae) {
            SLog.e(TAG, "Failed to get valid solution id: " + solutionId);
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.INVALID_PARAM, "solution id is not valid format");
            return bundle;
        }
        Bundle packageAttestationInfo = PackageConfigUtility.getPackageAttestationInfoByUUID(getContext(), solutionId);
        if(packageAttestationInfo == null) {
            SLog.e(TAG, "Failed to get valid application link config for solutionId: " + solutionId);
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "package is not installed correctly");
            return bundle;
        }

        try {
            // LDB on/off determines which attestation flow to use
            boolean isLdbEnabled = (config != null && config.isAndroidDebugBridgeEnabled());
            SLog.d(TAG, "Attestation mode: " + (isLdbEnabled ? "LDB" : "Non-LDB"));

            String host = config.getHost();
            String clientId = config.getClientId();

            AppTokenResponse tokenResponse;

            if (isLdbEnabled) {
                // LDB flow: read auth config and call cloud with debug credentials
                String auth = packageAttestationInfo.getString(PackageContract.PackageAttestationEntry.AUTH);
                String userName = packageAttestationInfo.getString(PackageContract.PackageAttestationEntry.USER);
                String ldbKey = packageAttestationInfo.getString(PackageContract.PackageAttestationEntry.KEY);
                SLog.d(TAG, "success to get config");

                if (TextUtils.isEmpty(auth)) {
                    Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.INVALID_PARAM, "auth config is empty");
                    return bundle;
                }

                Type clientsType = new TypeToken<ArrayList<ClientInfoInternal>>() {
                }.getType();

                List<ClientInfoInternal> clients;
                if (auth.startsWith("[")) {
                    clients = JsonParser.getInstance().fromJson(auth, clientsType);
                } else {
                    ClientInfoInternal client = JsonParser.getInstance().fromJson(auth, ClientInfoInternal.class);
                    clients = new ArrayList<ClientInfoInternal>();
                    clients.add(client);
                }

                if (clients == null || clients.isEmpty()) {
                    Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.INVALID_PARAM, "clients config is empty");
                    return bundle;
                }

                AttestationAttributes attestationAttributes = new AttestationAttributes.DebugBuilder()
                        .setAppId(solutionId)
                        .addClients(clients)
                        .setDebugUserName(TextUtils.isEmpty(userName) ? "" : userName)
                        .setLDBKey(TextUtils.isEmpty(ldbKey) ? "" : ldbKey)
                        .build();

                AttestationInfo attestationInfo = new AttestationInfo();
                attestationInfo.setAppId(attestationAttributes.getAppId());
                attestationInfo.setClients(attestationAttributes.getClients());

                tokenResponse = requestAppTokenFromCloud(
                        host,
                        X_MODE_DEBUG,
                        getBase64EncodedString(attestationAttributes.getDebugUserName(), attestationAttributes.getLDBKey()),
                        JsonParser.getInstance().toJson(attestationInfo)
                );
            } else {
                // Non-LDB flow:
                // 1) Get package info for app version and signature
                // 2) Call device internal API to obtain deviceResponse (contains "code=xxx&...")
                // 3) Parse code from deviceResponse
                // 4) Exchange code with cloud to obtain appToken
                Bundle pkgInfo = PackageConfigUtility.getPackageInformationByUUID(getContext(), solutionId);
                if (pkgInfo == null) {
                    throw new RuntimeException("Failed to get package info for solutionId: " + solutionId);
                }

                String appVersion = pkgInfo.getString(PackageContract.PackageEntry.HPK2_VERSION);
                String appSignature = packageAttestationInfo.getString(PackageContract.PackageAttestationEntry.DATA);

                if (TextUtils.isEmpty(appVersion) || TextUtils.isEmpty(appSignature)) {
                    throw new RuntimeException("App version or signature is empty. version=" + appVersion);
                }

                // Generate state and nonce on the caller side per API spec:
                // state: opaque value to maintain state between request and callback
                // nonce: mitigate replay attacks
                String state = String.valueOf((int) (Math.random() * 100000));
                String nonce = UUID.randomUUID().toString().replace("-", "");

                String deviceResponse = AttestationAdapter.getDeviceAttestationResponse(
                        attestationDeviceService,
                        packageName,
                        solutionId,
                        appVersion,
                        appSignature,
                        clientId,
                        state,
                        nonce
                );

                tokenResponse = requestAppTokenFromCloudWithDeviceCode(host, deviceResponse);
            }

            Result.pack(bundle, Result.RESULT_OK);
            bundle.putString(Result.KEY_RESULT, tokenResponse.toString());
        } catch (Exception e) {
            SLog.e(TAG, "failed to get application token: " + e.getMessage(), e);
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }
        return bundle;
    }

    private AppTokenResponse requestAppTokenFromCloud(String host,
                                                      String mode,
                                                      String token,
                                                      String attestationInfoJson) throws IOException {
        String response = callAttestationAPI(
                AttestationRequest.POST,
                host,
                AttestationRequest.GET_APP_TOKEN,
                CONTENT_TYPE_JSON,
                token,
                mode,
                attestationInfoJson
        );

        if (TextUtils.isEmpty(response)) {
            throw new IOException("Attestation API returned empty response body");
        }

        com.google.gson.JsonObject jsonResponse;
        try {
            jsonResponse = com.google.gson.JsonParser.parseString(response).getAsJsonObject();
        } catch (Exception parseException) {
            throw new IOException("Attestation API returned non-JSON response: " + response, parseException);
        }

        String appToken = getJsonString(jsonResponse, "app_token");
        String expiresIn = getJsonString(jsonResponse, "expires_in");
        if (TextUtils.isEmpty(appToken) || TextUtils.isEmpty(expiresIn)) {
            String message = getJsonString(jsonResponse, "message");
            if (TextUtils.isEmpty(message) && jsonResponse.has("errors")) {
                message = jsonResponse.get("errors").toString();
            }
            throw new IOException(
                    "Attestation API response missing required fields. " +
                            "appToken=" + appToken + ", expiresIn=" + expiresIn + ", " +
                            "message=" + message + ", body=" + response
            );
        }

        return new AppTokenResponse(appToken, expiresIn);
    }

    /**
     * Non-LDB flow: Exchange device attestation code with cloud to obtain appToken.
     * Device response format: "code=xxx&state=yyy&..."
     * Cloud API: x-mode="P", x-token="", body=code
     */
    private AppTokenResponse requestAppTokenFromCloudWithDeviceCode(String host,
                                                                     String deviceResponse) throws IOException {
        if (TextUtils.isEmpty(deviceResponse)) {
            throw new IOException("Device attestation response is empty");
        }

        // Parse code from deviceResponse (format: "code=xxx&state=yyy&...")
        String code = null;
        String[] responses = deviceResponse.split("&");
        for (String subResponse : responses) {
            if (subResponse.startsWith("code=")) {
                code = subResponse.substring("code=".length());
                break;
            }
        }

        if (TextUtils.isEmpty(code)) {
            throw new IOException("Failed to parse code from device response: " + deviceResponse);
        }

        // Non-LDB: x-mode="production", x-token=code, body="" (empty)
        String response = callAttestationAPI(
                AttestationRequest.POST,
                host,
                AttestationRequest.GET_APP_TOKEN,
                CONTENT_TYPE_JSON,
                code,  // x-token contains the code from device response
                X_MODE_PROD,
                ""     // body is empty for Non-LDB
        );

        if (TextUtils.isEmpty(response)) {
            throw new IOException("Attestation API returned empty response body");
        }

        com.google.gson.JsonObject jsonResponse;
        try {
            jsonResponse = com.google.gson.JsonParser.parseString(response).getAsJsonObject();
        } catch (Exception parseException) {
            throw new IOException("Attestation API returned non-JSON response: " + response, parseException);
        }

        String appToken = getJsonString(jsonResponse, "app_token");
        String expiresIn = getJsonString(jsonResponse, "expires_in");
        if (TextUtils.isEmpty(appToken) || TextUtils.isEmpty(expiresIn)) {
            String message = getJsonString(jsonResponse, "message");
            if (TextUtils.isEmpty(message) && jsonResponse.has("errors")) {
                message = jsonResponse.get("errors").toString();
            }
            throw new IOException(
                    "Attestation API response missing required fields. " +
                            "appToken=" + appToken + ", expiresIn=" + expiresIn + ", " +
                            "message=" + message + ", body=" + response
            );
        }

        return new AppTokenResponse(appToken, expiresIn);
    }

    private static String getJsonString(com.google.gson.JsonObject jsonObject, String key) {
        if (jsonObject == null || TextUtils.isEmpty(key) || !jsonObject.has(key)) {
            return null;
        }

        if (jsonObject.get(key) == null || jsonObject.get(key).isJsonNull()) {
            return null;
        }

        try {
            return jsonObject.get(key).getAsString();
        } catch (Exception ignored) {
            try {
                return jsonObject.get(key).toString();
            } catch (Exception ignoredAgain) {
                return null;
            }
        }
    }

    private String callAttestationAPI(String method, String host, String url, String mediaType,
                                     String token, String mode, String requestBody) throws IOException {
        if (TextUtils.isEmpty(mediaType)) {
            mediaType = CONTENT_TYPE_JSON;
        }

        syncProxySettings();

        SLog.d(TAG, "App Token URL : https://" + host + url);

        Request request = new Request.Builder()
                .method(method, requestBody != null ?
                        RequestBody.create(MediaType.parse(mediaType), requestBody) : null)
                .url("https://" + host + url)
                .header("Connection", "close")
                .header("Content-Type", mediaType)
                .header("x-token", token)
                .header("x-mode", mode)
                .build();

        OkHttpClient httpsClient = createHttpsClient();
        Response response = httpsClient.newCall(request).execute();
        ResponseBody body = response.body();
        String bodyString = (body != null) ? body.string() : null;

        if (!response.isSuccessful()) {
            throw new IOException("REST call failed with HTTP " + response.code() + ", " + bodyString);
        }

        return bodyString;
    }

    private OkHttpClient createHttpsClient() {
        // Uses OkHttp defaults: TLS 1.2/1.3, modern cipher suites, proper certificate validation
        return new OkHttpClient.Builder().build();
    }

    /**
     * Synchronizes proxy settings from ConnectivityManager to Java System properties.
     *
     * OkHttpClient reads proxy from System properties at the time the client is built.
     * Since this process runs in the background without restarting, stale proxy settings
     * would be used after a network proxy change. This method refreshes them before each
     * outbound HTTP call to ensure the latest proxy is always applied.
     */
    private void syncProxySettings() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm == null) {
                SLog.w(TAG, "syncProxySettings: ConnectivityManager is null, skip");
                return;
            }

            ProxyInfo proxyInfo = cm.getDefaultProxy();
            if (proxyInfo != null && !TextUtils.isEmpty(proxyInfo.getHost())) {
                SLog.d(TAG, "syncProxySettings: proxy=" + proxyInfo.getHost() + ":" + proxyInfo.getPort());
                System.setProperty("http.proxyHost", proxyInfo.getHost());
                System.setProperty("http.proxyPort", String.valueOf(proxyInfo.getPort()));
                System.setProperty("https.proxyHost", proxyInfo.getHost());
                System.setProperty("https.proxyPort", String.valueOf(proxyInfo.getPort()));

                String[] exclusionList = proxyInfo.getExclusionList();
                if (exclusionList != null && exclusionList.length > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (String excludedHost : exclusionList) {
                        if (sb.length() > 0) sb.append("|");
                        sb.append(excludedHost);
                    }
                    System.setProperty("http.nonProxyHosts", sb.toString());
                    System.setProperty("https.nonProxyHosts", sb.toString());
                }
            } else {
                // No proxy configured: clear previous settings to avoid stale proxy usage
                SLog.v(TAG, "syncProxySettings: no proxy, clearing stale settings");
                System.clearProperty("http.proxyHost");
                System.clearProperty("http.proxyPort");
                System.clearProperty("https.proxyHost");
                System.clearProperty("https.proxyPort");
                System.clearProperty("http.nonProxyHosts");
                System.clearProperty("https.nonProxyHosts");
            }
        } catch (Exception e) {
            SLog.e(TAG, "syncProxySettings: failed", e);
        }
    }

    private String getBase64EncodedString(String key, String value) {
        if (key == null) {
            key = "";
        }
        if (value == null) {
            value = "";
        }
        return Base64.encodeToString((key + ":" + value).getBytes(), Base64.NO_WRAP | Base64.URL_SAFE);
    }

    private boolean isSupported(PrinterInfo pi) {
        return true;
    }

    private static class DeviceInfo {
        final boolean isSealed;
        final boolean isSecure;
        final String cloudStack;

        DeviceInfo(boolean isSealed, boolean isSecure, String cloudStack) {
            this.isSealed = isSealed;
            this.isSecure = isSecure;
            this.cloudStack = cloudStack;
        }

        boolean isProduction() {
            return isSealed && isSecure;
        }
    }

    private DeviceInfo fetchDeviceInfo() {
        try {
            IDeviceAttestationService.DeviceInfoResult result = attestationDeviceService.getDeviceInfo();
            if (result == null) {
                SLog.w(TAG, "fetchDeviceInfo: DeviceInfoResult is null");
                return null;
            }
            return new DeviceInfo(result.isSealed, result.isSecure, result.cloudStack);
        } catch (Exception e) {
            SLog.e(TAG, "fetchDeviceInfo: failed", e);
            return null;
        }
    }

    private AppPlatformConfiguration getAppPlatformConfiguration() {
        AppPlatformConfiguration appPlatformConfiguration = new AppPlatformConfiguration();

        // Default to false (Non-LDB); overridden below if ldb_state is set
        appPlatformConfiguration.setAndroidDebugBridgeEnabled(Boolean.FALSE);

        boolean mode = false;

        // Read LDB state from secure settings
        String ldbState = android.provider.Settings.Secure.getString(getContext().getContentResolver(), "ldb_state");
        if (!TextUtils.isEmpty(ldbState)) {
            mode = Boolean.parseBoolean(ldbState);
            SLog.d(TAG, "ldb_state=" + ldbState + " -> ldb=" + mode);
        } else {
            SLog.d(TAG, "ldb_state not found, using default ldb=" + mode);
        }

        appPlatformConfiguration.setAndroidDebugBridgeEnabled(mode);
        SLog.d(TAG, "system find the value");

        // Fetch device info to determine execution mode (bootMode) and cloud stack (host/clientId)
        DeviceInfo deviceInfo = fetchDeviceInfo();
        if (deviceInfo != null) {
            if (deviceInfo.isProduction()) {
                // Production mode (isSealed && isSecure): always use PROD regardless of cloudStack
                appPlatformConfiguration.setExecutionMode(ExecutionMode.Production.name());
                appPlatformConfiguration.setHost(X_HOST_PROD);
                appPlatformConfiguration.setClientId(X_TOKEN_CLIENT_ID_PROD);
            } else {
                // Development mode: use cloudStack to determine target environment
                appPlatformConfiguration.setExecutionMode(ExecutionMode.Development.name());
                String cloudStack = deviceInfo.cloudStack;
                if ("pie".equalsIgnoreCase(cloudStack)) {
                    appPlatformConfiguration.setHost(X_HOST_TEST);
                    appPlatformConfiguration.setClientId(X_TOKEN_CLIENT_ID_TEST);
                } else if ("staging".equalsIgnoreCase(cloudStack)) {
                    appPlatformConfiguration.setHost(X_HOST_STAGE);
                    appPlatformConfiguration.setClientId(X_TOKEN_CLIENT_ID_STAGE);
                } else {
                    // "production" cloudStack in development mode -> PROD host
                    appPlatformConfiguration.setHost(X_HOST_PROD);
                    appPlatformConfiguration.setClientId(X_TOKEN_CLIENT_ID_PROD);
                }
            }
            SLog.d(TAG, "deviceInfo: execMode=" + appPlatformConfiguration.getExecutionMode()
                    + ", cloudStack=" + deviceInfo.cloudStack + " -> host=" + appPlatformConfiguration.getHost());
        } else {
            // Fallback defaults
            appPlatformConfiguration.setExecutionMode(ExecutionMode.Production.name());
            appPlatformConfiguration.setHost(X_HOST_PROD);
            appPlatformConfiguration.setClientId(X_TOKEN_CLIENT_ID_PROD);
            SLog.w(TAG, "deviceInfo unavailable, fallback: host=" + X_HOST_PROD);
        }

        return appPlatformConfiguration;
    }
}
