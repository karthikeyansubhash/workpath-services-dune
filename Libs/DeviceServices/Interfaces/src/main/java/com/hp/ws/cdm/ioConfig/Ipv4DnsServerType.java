
package com.hp.ws.cdm.ioconfig;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * IPv4 DNS configuration
 * 
 */
public class Ipv4DnsServerType {

    /**
     * DNS Configuration method
     * 
     */
    @SerializedName("preferredDnsConfigMethod")
    @Expose
    private Ipv4DnsServerType.DnsConfigMethod preferredDnsConfigMethod;
    /**
     * IPv4 DNS Address
     * 
     */
    @SerializedName("primary")
    @Expose
    private Ipv4DnsAddressConfig primary;
    /**
     * IPv4 DNS Address
     * 
     */
    @SerializedName("secondary")
    @Expose
    private Ipv4DnsAddressConfig secondary;

    /**
     * DNS Configuration method
     * 
     */
    public Ipv4DnsServerType.DnsConfigMethod getPreferredDnsConfigMethod() {
        return preferredDnsConfigMethod;
    }

    /**
     * DNS Configuration method
     * 
     */
    public void setPreferredDnsConfigMethod(Ipv4DnsServerType.DnsConfigMethod preferredDnsConfigMethod) {
        this.preferredDnsConfigMethod = preferredDnsConfigMethod;
    }

    /**
     * IPv4 DNS Address
     * 
     */
    public Ipv4DnsAddressConfig getPrimary() {
        return primary;
    }

    /**
     * IPv4 DNS Address
     * 
     */
    public void setPrimary(Ipv4DnsAddressConfig primary) {
        this.primary = primary;
    }

    /**
     * IPv4 DNS Address
     * 
     */
    public Ipv4DnsAddressConfig getSecondary() {
        return secondary;
    }

    /**
     * IPv4 DNS Address
     * 
     */
    public void setSecondary(Ipv4DnsAddressConfig secondary) {
        this.secondary = secondary;
    }


    /**
     * DNS Configuration method
     * 
     */
    public enum DnsConfigMethod {

        @SerializedName("automatic")
        AUTOMATIC("automatic"),
        @SerializedName("manual")
        MANUAL("manual");
        private final String value;
        private final static Map<String, Ipv4DnsServerType.DnsConfigMethod> CONSTANTS = new HashMap<String, Ipv4DnsServerType.DnsConfigMethod>();

        static {
            for (Ipv4DnsServerType.DnsConfigMethod c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        DnsConfigMethod(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Ipv4DnsServerType.DnsConfigMethod fromValue(String value) {
            Ipv4DnsServerType.DnsConfigMethod constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
