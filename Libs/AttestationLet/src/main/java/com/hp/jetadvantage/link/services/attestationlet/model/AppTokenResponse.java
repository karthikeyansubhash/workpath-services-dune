package com.hp.jetadvantage.link.services.attestationlet.model;

import java.util.Objects;
import com.google.gson.Gson;

/**
 * Response model for app token
 */
public class AppTokenResponse {
    private String appToken;
    private String expiresIn;

    public AppTokenResponse() {
    }

    public AppTokenResponse(String appToken, String expiresIn) {
        this.appToken = appToken;
        this.expiresIn = expiresIn;
    }

    public String getAppToken() {
        return appToken;
    }

    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

    @Override
    public String toString() {
        // Use Gson for safe JSON serialization (handles nulls, escaping, etc.)
        return new Gson().toJson(this);
    }
}

