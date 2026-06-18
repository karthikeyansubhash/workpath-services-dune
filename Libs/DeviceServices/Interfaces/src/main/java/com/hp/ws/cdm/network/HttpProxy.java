
package com.hp.ws.cdm.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;


/**
 * Manual HTTP proxy configuration
 * 
 */
public class HttpProxy {

    /**
     * HTTP proxy server address. This could be IPv4 or IPv6 or FQDN address
     * 
     */
    @SerializedName("address")
    @Expose
    private String address;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("enabled")
    @Expose
    private Property.FeatureEnabled enabled;
    /**
     * Port on which HTTP proxy service is hosted
     * 
     */
    @SerializedName("port")
    @Expose
    private Integer port;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("useHttps")
    @Expose
    private Property.FeatureEnabled useHttps;

    /**
     * HTTP proxy server address. This could be IPv4 or IPv6 or FQDN address
     * 
     */
    public String getAddress() {
        return address;
    }

    /**
     * HTTP proxy server address. This could be IPv4 or IPv6 or FQDN address
     * 
     */
    public void setAddress(String address) {
        this.address = address;
    }

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
     * Port on which HTTP proxy service is hosted
     * 
     */
    public Integer getPort() {
        return port;
    }

    /**
     * Port on which HTTP proxy service is hosted
     * 
     */
    public void setPort(Integer port) {
        this.port = port;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getUseHttps() {
        return useHttps;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setUseHttps(Property.FeatureEnabled useHttps) {
        this.useHttps = useHttps;
    }

}
