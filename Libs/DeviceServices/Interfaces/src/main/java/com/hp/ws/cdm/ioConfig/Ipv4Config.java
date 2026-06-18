
package com.hp.ws.cdm.ioconfig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;


/**
 * IPv4 configuration
 * 
 */
public class Ipv4Config {

    /**
     * IPv4 address details
     * 
     */
    @SerializedName("address")
    @Expose
    private Ipv4AddressConfig address;
    /**
     * IPv4 DNS configuration
     * 
     */
    @SerializedName("dnsServer")
    @Expose
    private Ipv4DnsServerType dnsServer;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("enabled")
    @Expose
    private Property.FeatureEnabled enabled;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("multicastEnabled")
    @Expose
    private Property.FeatureEnabled multicastEnabled;

    /**
     * IPv4 address details
     * 
     */
    public Ipv4AddressConfig getAddress() {
        return address;
    }

    /**
     * IPv4 address details
     * 
     */
    public void setAddress(Ipv4AddressConfig address) {
        this.address = address;
    }

    /**
     * IPv4 DNS configuration
     * 
     */
    public Ipv4DnsServerType getDnsServer() {
        return dnsServer;
    }

    /**
     * IPv4 DNS configuration
     * 
     */
    public void setDnsServer(Ipv4DnsServerType dnsServer) {
        this.dnsServer = dnsServer;
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
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getMulticastEnabled() {
        return multicastEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setMulticastEnabled(Property.FeatureEnabled multicastEnabled) {
        this.multicastEnabled = multicastEnabled;
    }

}
