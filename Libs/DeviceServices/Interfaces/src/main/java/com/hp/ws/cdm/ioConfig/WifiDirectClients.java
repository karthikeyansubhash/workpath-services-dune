
package com.hp.ws.cdm.ioconfig;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WifiDirectClients {

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("connectedClients")
    @Expose
    private List<WifiDirectClient> connectedClients = new ArrayList<WifiDirectClient>();
    @SerializedName("authenticatedClients")
    @Expose
    private List<WifiDirectClient> authenticatedClients = new ArrayList<WifiDirectClient>();

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    public String getVersion() {
        return version;
    }

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * 
     * (Required)
     * 
     */
    public List<WifiDirectClient> getConnectedClients() {
        return connectedClients;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setConnectedClients(List<WifiDirectClient> connectedClients) {
        this.connectedClients = connectedClients;
    }

    public List<WifiDirectClient> getAuthenticatedClients() {
        return authenticatedClients;
    }

    public void setAuthenticatedClients(List<WifiDirectClient> authenticatedClients) {
        this.authenticatedClients = authenticatedClients;
    }

}
