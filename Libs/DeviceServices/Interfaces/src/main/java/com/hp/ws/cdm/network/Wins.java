
package com.hp.ws.cdm.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class Wins {

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("enabled")
    @Expose
    private Property.FeatureEnabled enabled;
    /**
     * Primary WINS server for name registration
     * 
     */
    @SerializedName("primaryServer")
    @Expose
    private String primaryServer;
    /**
     * Secondary WINS server for name registration
     * 
     */
    @SerializedName("secondaryServer")
    @Expose
    private String secondaryServer;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("winsPort")
    @Expose
    private Property.FeatureEnabled winsPort;

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getEnabled() {
        return enabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setEnabled(Property.FeatureEnabled enabled) {
        this.enabled = enabled;
    }

    /**
     * Primary WINS server for name registration
     * 
     */
    public String getPrimaryServer() {
        return primaryServer;
    }

    /**
     * Primary WINS server for name registration
     * 
     */
    public void setPrimaryServer(String primaryServer) {
        this.primaryServer = primaryServer;
    }

    /**
     * Secondary WINS server for name registration
     * 
     */
    public String getSecondaryServer() {
        return secondaryServer;
    }

    /**
     * Secondary WINS server for name registration
     * 
     */
    public void setSecondaryServer(String secondaryServer) {
        this.secondaryServer = secondaryServer;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getWinsPort() {
        return winsPort;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setWinsPort(Property.FeatureEnabled winsPort) {
        this.winsPort = winsPort;
    }

}
