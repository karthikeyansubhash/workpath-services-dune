/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PasswordGrantRequest {
    @JsonProperty("grant_type")
    private String grantType = PasswordGrantConstants.PASSWORDGRANTTYPE;
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
    @JsonProperty("scope")
    private String scope;

    public String getGrantType() {
        return grantType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

}
