
package com.hp.ws.cdm.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;


/**
 * Web Proxy Auto Detection WPAD configuration
 * 
 */
public class Wpad {

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("enabled")
    @Expose
    private Property.FeatureEnabled enabled;
    /**
     * URL path to Proxy Auto-Configuration (PAC) file
     * 
     */
    @SerializedName("pacUrl")
    @Expose
    private String pacUrl;

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
     * URL path to Proxy Auto-Configuration (PAC) file
     * 
     */
    public String getPacUrl() {
        return pacUrl;
    }

    /**
     * URL path to Proxy Auto-Configuration (PAC) file
     * 
     */
    public void setPacUrl(String pacUrl) {
        this.pacUrl = pacUrl;
    }

}
