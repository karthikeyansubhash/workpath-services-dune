
package com.hp.ws.cdm.ioconfig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Device host, domain names
 * 
 */
public class DeviceIdentity {

    /**
     * DNS host/domain name
     * 
     */
    @SerializedName("domainName")
    @Expose
    private NameConfig domainName;
    /**
     * DNS host/domain name
     * 
     */
    @SerializedName("domainNameV6")
    @Expose
    private NameConfig domainNameV6;
    /**
     * DNS host/domain name
     * 
     */
    @SerializedName("hostname")
    @Expose
    private NameConfig hostname;

    /**
     * DNS host/domain name
     * 
     */
    public NameConfig getDomainName() {
        return domainName;
    }

    /**
     * DNS host/domain name
     * 
     */
    public void setDomainName(NameConfig domainName) {
        this.domainName = domainName;
    }

    /**
     * DNS host/domain name
     * 
     */
    public NameConfig getDomainNameV6() {
        return domainNameV6;
    }

    /**
     * DNS host/domain name
     * 
     */
    public void setDomainNameV6(NameConfig domainNameV6) {
        this.domainNameV6 = domainNameV6;
    }

    /**
     * DNS host/domain name
     * 
     */
    public NameConfig getHostname() {
        return hostname;
    }

    /**
     * DNS host/domain name
     * 
     */
    public void setHostname(NameConfig hostname) {
        this.hostname = hostname;
    }

}
