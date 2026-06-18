
package com.hp.ws.cdm.ioconfig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * IPv4 DNS Address
 * 
 */
public class Ipv4DnsAddressConfig {

    @SerializedName("address")
    @Expose
    private String address;
    /**
     * Configuration methods for dns, hostname, etc
     * 
     */
    @SerializedName("configMethod")
    @Expose
    private com.hp.ws.cdm.ioconfig.NameConfig.ConfigMethods configMethod;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Configuration methods for dns, hostname, etc
     * 
     */
    public com.hp.ws.cdm.ioconfig.NameConfig.ConfigMethods getConfigMethod() {
        return configMethod;
    }

    /**
     * Configuration methods for dns, hostname, etc
     * 
     */
    public void setConfigMethod(com.hp.ws.cdm.ioconfig.NameConfig.ConfigMethods configMethod) {
        this.configMethod = configMethod;
    }

}
