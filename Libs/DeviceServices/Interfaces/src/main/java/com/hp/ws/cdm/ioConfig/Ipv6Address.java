
package com.hp.ws.cdm.ioconfig;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * IPv6 address details
 * 
 */
public class Ipv6Address {

    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("address")
    @Expose
    private String address;
    /**
     * ipv6 address types
     * 
     */
    @SerializedName("addressType")
    @Expose
    private Ipv6Address.Ipv6AddressTypes addressType;
    @SerializedName("preferredLifeTime")
    @Expose
    private Integer preferredLifeTime;
    @SerializedName("prefixLength")
    @Expose
    private Integer prefixLength;
    @SerializedName("validLifeTime")
    @Expose
    private Integer validLifeTime;

    /**
     * 
     * (Required)
     * 
     */
    public String getAddress() {
        return address;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * ipv6 address types
     * 
     */
    public Ipv6Address.Ipv6AddressTypes getAddressType() {
        return addressType;
    }

    /**
     * ipv6 address types
     * 
     */
    public void setAddressType(Ipv6Address.Ipv6AddressTypes addressType) {
        this.addressType = addressType;
    }

    public Integer getPreferredLifeTime() {
        return preferredLifeTime;
    }

    public void setPreferredLifeTime(Integer preferredLifeTime) {
        this.preferredLifeTime = preferredLifeTime;
    }

    public Integer getPrefixLength() {
        return prefixLength;
    }

    public void setPrefixLength(Integer prefixLength) {
        this.prefixLength = prefixLength;
    }

    public Integer getValidLifeTime() {
        return validLifeTime;
    }

    public void setValidLifeTime(Integer validLifeTime) {
        this.validLifeTime = validLifeTime;
    }


    /**
     * ipv6 address types
     * 
     */
    public enum Ipv6AddressTypes {

        @SerializedName("dhcpv6")
        DHCPV_6("dhcpv6"),
        @SerializedName("linkLocal")
        LINK_LOCAL("linkLocal"),
        @SerializedName("manual")
        MANUAL("manual"),
        @SerializedName("stateless")
        STATELESS("stateless");
        private final String value;
        private final static Map<String, Ipv6Address.Ipv6AddressTypes> CONSTANTS = new HashMap<String, Ipv6Address.Ipv6AddressTypes>();

        static {
            for (Ipv6Address.Ipv6AddressTypes c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Ipv6AddressTypes(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Ipv6Address.Ipv6AddressTypes fromValue(String value) {
            Ipv6Address.Ipv6AddressTypes constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
