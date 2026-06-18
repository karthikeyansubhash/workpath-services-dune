/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.standard.common;

import android.util.Log;

import com.hp.jetadvantage.link.device.services.clients.CDMClient;
import com.hp.jetadvantage.link.device.services.clients.CDMResponse;
import com.hp.ws.e2workpathInterop.AppToken;

import java.util.concurrent.ConcurrentHashMap;

/**
 * AppTokenManager class
 * <p>
 * The Workpath platform caches an app’s token on behalf of an app.
 * The app’s token is invisible to the app. So it can be kept compatible with existing Workpath APIs.
 * When first needed, the Workpath platform obtains an App’s access token via a WorkpathInterop function.
 * It can cache this for reuse until it expires (1 hour) but there is no need to persist.
 */
public class AppTokenManager {
    private static final long APP_TOKEN_LIFE_TIME_SECS = 60 * 60;
    private static final long APP_TOKEN_LIFE_TIME_THRESHOLD_SECS = APP_TOKEN_LIFE_TIME_SECS - 10 * 60;
    private static final String TAG = Constants.TAG + "/AppT";

    /**
     * App token cache : Map<SolutionUUID, Pair<SolutionToken, TimeStamp>>
     */
    private final ConcurrentHashMap<String, TokenInfo<String, Long>> appTokenCache = new ConcurrentHashMap<>();
    private final CDMClient cdmClient;
    private final Object lock = new Object();

    public AppTokenManager(CDMClient cdmClient) {
        this.cdmClient = cdmClient;
    }

    /**
     * Get app token from cache
     * if a cache miss occurs, request it from the device
     *
     * @param solutionId solution UUID
     * @return app token
     */
    protected String getSolutionToken(String solutionId) {
        String appToken = "";

        if (solutionId == null || solutionId.isEmpty()) {
            Log.e(TAG, "getAppToken : invalid param");
            return appToken;
        }

        if (appTokenCache.containsKey(solutionId)) {
            Log.d(TAG, "get : Hit (" + solutionId + ")");
            long currentTimesSeconds = System.currentTimeMillis() / 1000;
            if (currentTimesSeconds < (appTokenCache.get(solutionId).timeStamps.longValue() + getAppTokenLifeTimeThresholdSecs())) {
                appToken = appTokenCache.get(solutionId).token;
            }
        }

        if (appToken.isEmpty()) {
            Log.d(TAG, "get : Miss (" + solutionId + ")");
            appToken = updateSolutionTokenCache(solutionId);
        }

        return appToken;
    }

    protected String updateSolutionTokenCache(String solutionId) {
        String appToken = "";
        if (solutionId == null || solutionId.isEmpty()) {
            Log.i(TAG, "update : invalid param");
            return appToken;
        }
        synchronized (lock) {
            String token = requestSolutionTokenFromDevice(solutionId);
            if (token != null && !token.isEmpty()) {
                appToken = token;
                long currentTimesSeconds = System.currentTimeMillis() / 1000;
                appTokenCache.put(solutionId, new TokenInfo<>(token, new Long(currentTimesSeconds)));

                Log.d(TAG, "update : Put (" + solutionId + ")");
            }
        }
        return appToken;
    }

    protected void clearSolutionTokenCache(String solutionId) {
        if (solutionId != null && !solutionId.isEmpty()) {
            appTokenCache.remove(solutionId);
        }
    }

    private String requestSolutionTokenFromDevice(String solutionId) {
        String url = InteropUrl.APP_TOKEN + "/" + solutionId;
        AppToken appTokenResponse = null;
        try {
            CDMResponse<String> response = cdmClient.sendGetRequest(url);
            appTokenResponse = (AppToken) StandardJsonParser.INSTANCE.fromJson(response.httpBody, AppToken.class);
        } catch (Exception e) {
            Log.e(TAG, "request : " + e.getMessage());
        }

        if (appTokenResponse == null) {
            Log.i(TAG, "request : failed (" + solutionId + ")");
            return "";
        }

        return appTokenResponse.getSolutionToken();
    }

    protected long getAppTokenLifeTimeThresholdSecs() {
        return APP_TOKEN_LIFE_TIME_THRESHOLD_SECS;
    }

    public static final class InteropUrl {
        public static final String APP_TOKEN = "/cdm/e2WorkpathInterop/v1/appToken";
    }

    protected class TokenInfo<F, S> {
        public final F token;
        public final S timeStamps;

        public TokenInfo(F first, S second) {
            this.token = first;
            this.timeStamps = second;
        }
    }
}
