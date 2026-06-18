/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthorizationCodeGrantRequest {
    @JsonProperty("grant_type")
    private String grantType = AuthorizationCodeGrantConstants.AUTHORIZATIONCODEGRANTTYPE;
    @JsonProperty("code")
    private String code;
    @JsonProperty("client_id")
    private String clientId = AuthorizationCodeGrantConstants.CLIENTIDSOLUTION;
    @JsonProperty("refresh_token_requested")
    private Boolean refreshTokenRequested = true;

    public String getGrantType() {
        return grantType;
    }

    public String getCode() {
        return code;
    }

    public Boolean getRefreshTokenRequested() {
        return refreshTokenRequested;
    }

    public String getClientId() {
        return clientId;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setRefreshTokenRequested(Boolean refreshTokenRequested) {
        this.refreshTokenRequested = refreshTokenRequested;
    }
}
