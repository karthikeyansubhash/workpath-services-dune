
package com.hp.ws.cdm.usbhost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * USB host microservice - promoting 1.1.0-beta.1 to 1.1.0
 * 
 */
public class Com_hp_cdm_service_usbHost_version_1_schema {

    /**
     * USB host capabilities
     * 
     */
    @SerializedName("capabilities")
    @Expose
    private Capabilities capabilities;
    /**
     * USB host configuration
     * 
     */
    @SerializedName("configuration")
    @Expose
    private Configuration configuration;

    /**
     * USB host capabilities
     * 
     */
    public Capabilities getCapabilities() {
        return capabilities;
    }

    /**
     * USB host capabilities
     * 
     */
    public void setCapabilities(Capabilities capabilities) {
        this.capabilities = capabilities;
    }

    /**
     * USB host configuration
     * 
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * USB host configuration
     * 
     */
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

}
