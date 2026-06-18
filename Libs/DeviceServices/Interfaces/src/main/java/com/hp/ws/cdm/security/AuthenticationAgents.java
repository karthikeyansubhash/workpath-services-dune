
package com.hp.ws.cdm.security;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthenticationAgents {

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * (Required)
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    /**
     * Array of the configured auth agents for rendering the user interfaces
     * (Required)
     * 
     */
    @SerializedName("authenticationAgents")
    @Expose
    private List<AuthenticationAgent> authenticationAgents = new ArrayList<AuthenticationAgent>();

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * (Required)
     * 
     */
    public String getVersion() {
        return version;
    }

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * (Required)
     * 
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Array of the configured auth agents for rendering the user interfaces
     * (Required)
     * 
     */
    public List<AuthenticationAgent> getAuthenticationAgents() {
        return authenticationAgents;
    }

    /**
     * Array of the configured auth agents for rendering the user interfaces
     * (Required)
     * 
     */
    public void setAuthenticationAgents(List<AuthenticationAgent> authenticationAgents) {
        this.authenticationAgents = authenticationAgents;
    }

}
