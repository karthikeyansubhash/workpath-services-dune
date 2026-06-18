/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RefreshTokenGrantRequest {
    @JsonProperty("grant_type")
    private String grantType = RefreshTokenGrantConstants.REFRESHTOKENPARAMETER;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("client_id")
    private String clientId = RefreshTokenGrantConstants.CLIENTIDSOLUTION;

    public String getGrantType() {
        return grantType;
    }

    public String getClientId() {
        return clientId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
