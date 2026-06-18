package com.hp.jetadvantage.link.device.services.standard.common;

import com.hp.jetadvantage.link.common.utils.SLog;

import java.util.concurrent.ConcurrentHashMap;

public class SessionAccessTokenManager {
    private static final String TAG = Constants.TAG + "/SAT";

    // Map to track which package names can access the token
    private static final ConcurrentHashMap<String, String> sessionAccessTokenMap = new ConcurrentHashMap<>();

    /**
     * Store a session access token. This will replace any existing token regardless of its package name.
     *
     * @param packageName        The package name associated with the token
     * @param sessionAccessToken The session access token to store
     */
    public static synchronized void storeSessionAccessToken(String packageName, String sessionAccessToken) {
        sessionAccessTokenMap.clear();
        if (packageName != null && sessionAccessToken != null) {
            // Add the package to the access map
            sessionAccessTokenMap.put(packageName, sessionAccessToken);
            SLog.i(TAG, "Stored session access token for packageName: " + packageName);
        } else {
            SLog.w(TAG, "Cannot store session access token: packageName or token is null");
        }
    }

    /**
     * Retrieve the current session access token
     *
     * @return The current session access token or null if no token is stored
     */
    public static synchronized String getSessionAccessToken(String packageName) {
        if (sessionAccessTokenMap.containsKey(packageName)) {
            return sessionAccessTokenMap.get(packageName);
        } else {
            SLog.d(TAG, "No session access token found");
        }
        return null;
    }

    /**
     * Remove the current session access token
     *
     * @return The removed session access token or null if no token was stored
     */
    public static synchronized void removeSessionAccessToken() {
        sessionAccessTokenMap.clear();
    }
}
