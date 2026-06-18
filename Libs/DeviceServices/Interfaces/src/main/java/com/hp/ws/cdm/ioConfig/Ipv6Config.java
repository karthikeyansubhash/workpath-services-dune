
package com.hp.ws.cdm.ioconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;


/**
 * IPv6 configuration
 * 
 */
public class Ipv6Config {

    @SerializedName("addresses")
    @Expose
    private List<Ipv6Address> addresses = new ArrayList<Ipv6Address>();
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("dhcpv6Enabled")
    @Expose
    private Property.FeatureEnabled dhcpv6Enabled;
    @SerializedName("dhcpv6Policy")
    @Expose
    private Ipv6Config.Dhcpv6Policy dhcpv6Policy;
    /**
     * IPv6 DNS configuration
     * 
     */
    @SerializedName("dnsServer")
    @Expose
    private Ipv6DnsServerType dnsServer;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("enabled")
    @Expose
    private Property.FeatureEnabled enabled;
    @SerializedName("manual")
    @Expose
    private Manual manual;
    @SerializedName("prefixList")
    @Expose
    private List<String> prefixList = new ArrayList<String>();
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("statelessEnabled")
    @Expose
    private Property.FeatureEnabled statelessEnabled;

    public List<Ipv6Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Ipv6Address> addresses) {
        this.addresses = addresses;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getDhcpv6Enabled() {
        return dhcpv6Enabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setDhcpv6Enabled(Property.FeatureEnabled dhcpv6Enabled) {
        this.dhcpv6Enabled = dhcpv6Enabled;
    }

    public Ipv6Config.Dhcpv6Policy getDhcpv6Policy() {
        return dhcpv6Policy;
    }

    public void setDhcpv6Policy(Ipv6Config.Dhcpv6Policy dhcpv6Policy) {
        this.dhcpv6Policy = dhcpv6Policy;
    }

    /**
     * IPv6 DNS configuration
     * 
     */
    public Ipv6DnsServerType getDnsServer() {
        return dnsServer;
    }

    /**
     * IPv6 DNS configuration
     * 
     */
    public void setDnsServer(Ipv6DnsServerType dnsServer) {
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

    public Manual getManual() {
        return manual;
    }

    public void setManual(Manual manual) {
        this.manual = manual;
    }

    public List<String> getPrefixList() {
        return prefixList;
    }

    public void setPrefixList(List<String> prefixList) {
        this.prefixList = prefixList;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getStatelessEnabled() {
        return statelessEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setStatelessEnabled(Property.FeatureEnabled statelessEnabled) {
        this.statelessEnabled = statelessEnabled;
    }

    public enum Dhcpv6Policy {

        @SerializedName("always")
        ALWAYS("always"),
        @SerializedName("onRouterRequest")
        ON_ROUTER_REQUEST("onRouterRequest"),
        @SerializedName("onStatelessFailure")
        ON_STATELESS_FAILURE("onStatelessFailure");
        private final String value;
        private final static Map<String, Ipv6Config.Dhcpv6Policy> CONSTANTS = new HashMap<String, Ipv6Config.Dhcpv6Policy>();

        static {
            for (Ipv6Config.Dhcpv6Policy c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Dhcpv6Policy(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Ipv6Config.Dhcpv6Policy fromValue(String value) {
            Ipv6Config.Dhcpv6Policy constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
