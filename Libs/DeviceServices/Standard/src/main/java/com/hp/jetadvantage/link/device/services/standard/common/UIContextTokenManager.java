/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.standard.common;

import android.util.Log;

public class UIContextTokenManager {

    private final Object lock = new Object();
    private Boolean available = false;
    private String applicationId = "";
    private String solutionId = "";
    private String token = "";

    public void clearUIConTextToken() {
        synchronized (lock) {
            available = false;
            this.token = "";
            this.solutionId = "";
        }
    }

    public String getUIContextToken(String solutionId) {
        synchronized (lock) {
            if (available && this.solutionId.equalsIgnoreCase(solutionId)) {
                return token;
            }
        }

        Log.i(Constants.TAG, "getUIContextToken : available :" + available + " [" + solutionId + " : " + this.solutionId + "]");
        return "";
    }

    public void setUIConTextToken(String solutionId, String token) {
        synchronized (lock) {
            this.token = token;
            this.solutionId = solutionId;
            available = true;
        }
    }
}
