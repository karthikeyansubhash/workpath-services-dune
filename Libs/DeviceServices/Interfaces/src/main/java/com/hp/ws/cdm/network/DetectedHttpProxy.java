
package com.hp.ws.cdm.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class DetectedHttpProxy {

    /**
     * HTTP Proxy server address
     * 
     */
    @SerializedName("address")
    @Expose
    private String address;
    /**
     * HTTP proxy server port
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
     * HTTP Proxy server address
     * 
     */
    public String getAddress() {
        return address;
    }

    /**
     * HTTP Proxy server address
     * 
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * HTTP proxy server port
     * 
     */
    public Integer getPort() {
        return port;
    }

    /**
     * HTTP proxy server port
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
