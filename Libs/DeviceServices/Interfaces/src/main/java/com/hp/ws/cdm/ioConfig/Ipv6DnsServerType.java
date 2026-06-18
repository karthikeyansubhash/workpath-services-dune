
package com.hp.ws.cdm.ioconfig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * IPv6 DNS configuration
 * 
 */
public class Ipv6DnsServerType {

    /**
     * DNS Configuration method
     * 
     */
    @SerializedName("preferredDnsConfigMethod")
    @Expose
    private com.hp.ws.cdm.ioconfig.Ipv4DnsServerType.DnsConfigMethod preferredDnsConfigMethod;
    /**
     * IPv6 DNS Address
     * 
     */
    @SerializedName("primary")
    @Expose
    private Ipv6DnsAddressConfig primary;
    /**
     * IPv6 DNS Address
     * 
     */
    @SerializedName("secondary")
    @Expose
    private Ipv6DnsAddressConfig secondary;

    /**
     * DNS Configuration method
     * 
     */
    public com.hp.ws.cdm.ioconfig.Ipv4DnsServerType.DnsConfigMethod getPreferredDnsConfigMethod() {
        return preferredDnsConfigMethod;
    }

    /**
     * DNS Configuration method
     * 
     */
    public void setPreferredDnsConfigMethod(com.hp.ws.cdm.ioconfig.Ipv4DnsServerType.DnsConfigMethod preferredDnsConfigMethod) {
        this.preferredDnsConfigMethod = preferredDnsConfigMethod;
    }

    /**
     * IPv6 DNS Address
     * 
     */
    public Ipv6DnsAddressConfig getPrimary() {
        return primary;
    }

    /**
     * IPv6 DNS Address
     * 
     */
    public void setPrimary(Ipv6DnsAddressConfig primary) {
        this.primary = primary;
    }

    /**
     * IPv6 DNS Address
     * 
     */
    public Ipv6DnsAddressConfig getSecondary() {
        return secondary;
    }

    /**
     * IPv6 DNS Address
     * 
     */
    public void setSecondary(Ipv6DnsAddressConfig secondary) {
        this.secondary = secondary;
    }

}
