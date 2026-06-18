
package com.hp.ws.cdm.ioconfig;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;


/**
 * IPv4 address details
 * 
 */
public class Ipv4AddressConfig {

    /**
     * IPv4 address configuration method
     * 
     */
    @SerializedName("activeConfigMethod")
    @Expose
    private Ipv4AddressConfig.Ipv4ConfigMethods activeConfigMethod;
    @SerializedName("configuredBy")
    @Expose
    private String configuredBy;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("dhcpFqdnRfc4702Compliance")
    @Expose
    private Property.FeatureEnabled dhcpFqdnRfc4702Compliance;
    @SerializedName("dhcpLeaseTime")
    @Expose
    private Integer dhcpLeaseTime;
    @SerializedName("gateway")
    @Expose
    private String gateway;
    @SerializedName("ip")
    @Expose
    private String ip;
    /**
     * IPv4 address configuration method
     * 
     */
    @SerializedName("requestedConfigMethod")
    @Expose
    private Ipv4AddressConfig.Ipv4ConfigMethods requestedConfigMethod;
    @SerializedName("subnet")
    @Expose
    private String subnet;

    /**
     * IPv4 address configuration method
     * 
     */
    public Ipv4AddressConfig.Ipv4ConfigMethods getActiveConfigMethod() {
        return activeConfigMethod;
    }

    /**
     * IPv4 address configuration method
     * 
     */
    public void setActiveConfigMethod(Ipv4AddressConfig.Ipv4ConfigMethods activeConfigMethod) {
        this.activeConfigMethod = activeConfigMethod;
    }

    public String getConfiguredBy() {
        return configuredBy;
    }

    public void setConfiguredBy(String configuredBy) {
        this.configuredBy = configuredBy;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getDhcpFqdnRfc4702Compliance() {
        return dhcpFqdnRfc4702Compliance;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setDhcpFqdnRfc4702Compliance(Property.FeatureEnabled dhcpFqdnRfc4702Compliance) {
        this.dhcpFqdnRfc4702Compliance = dhcpFqdnRfc4702Compliance;
    }

    public Integer getDhcpLeaseTime() {
        return dhcpLeaseTime;
    }

    public void setDhcpLeaseTime(Integer dhcpLeaseTime) {
        this.dhcpLeaseTime = dhcpLeaseTime;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * IPv4 address configuration method
     * 
     */
    public Ipv4AddressConfig.Ipv4ConfigMethods getRequestedConfigMethod() {
        return requestedConfigMethod;
    }

    /**
     * IPv4 address configuration method
     * 
     */
    public void setRequestedConfigMethod(Ipv4AddressConfig.Ipv4ConfigMethods requestedConfigMethod) {
        this.requestedConfigMethod = requestedConfigMethod;
    }

    public String getSubnet() {
        return subnet;
    }

    public void setSubnet(String subnet) {
        this.subnet = subnet;
    }


    /**
     * IPv4 address configuration method
     * 
     */
    public enum Ipv4ConfigMethods {

        @SerializedName("unknown")
        UNKNOWN("unknown"),
        @SerializedName("autoip")
        AUTOIP("autoip"),
        @SerializedName("bootp")
        BOOTP("bootp"),
        @SerializedName("dhcpv4")
        DHCPV_4("dhcpv4"),
        @SerializedName("manual")
        MANUAL("manual");
        private final String value;
        private final static Map<String, Ipv4AddressConfig.Ipv4ConfigMethods> CONSTANTS = new HashMap<String, Ipv4AddressConfig.Ipv4ConfigMethods>();

        static {
            for (Ipv4AddressConfig.Ipv4ConfigMethods c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Ipv4ConfigMethods(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Ipv4AddressConfig.Ipv4ConfigMethods fromValue(String value) {
            Ipv4AddressConfig.Ipv4ConfigMethods constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
