
package com.hp.ws.cdm.security;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Com_hp_cdm_service_security_version_1_resource_authenticationAgents_schema {

    @SerializedName("authenticationAgents")
    @Expose
    private AuthenticationAgents authenticationAgents;
    @SerializedName("authenticationAgent")
    @Expose
    private AuthenticationAgent authenticationAgent;

    public AuthenticationAgents getAuthenticationAgents() {
        return authenticationAgents;
    }

    public void setAuthenticationAgents(AuthenticationAgents authenticationAgents) {
        this.authenticationAgents = authenticationAgents;
    }

    public AuthenticationAgent getAuthenticationAgent() {
        return authenticationAgent;
    }

    public void setAuthenticationAgent(AuthenticationAgent authenticationAgent) {
        this.authenticationAgent = authenticationAgent;
    }

}
