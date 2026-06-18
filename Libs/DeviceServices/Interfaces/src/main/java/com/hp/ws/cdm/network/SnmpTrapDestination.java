
package com.hp.ws.cdm.network;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SnmpTrapDestination {

    /**
     * The IP address or hostname of the trap destination
     * 
     */
    @SerializedName("trapAddress")
    @Expose
    private String trapAddress;
    /**
     * The community name to use in the trap request.
     * 
     */
    @SerializedName("trapCommunity")
    @Expose
    private String trapCommunity;
    /**
     * The destination port to send the trap to.
     * 
     */
    @SerializedName("trapPort")
    @Expose
    private Integer trapPort;
    /**
     * The trap version
     * 
     */
    @SerializedName("trapVersion")
    @Expose
    private SnmpTrapDestination.TrapVersion trapVersion;

    /**
     * The IP address or hostname of the trap destination
     * 
     */
    public String getTrapAddress() {
        return trapAddress;
    }

    /**
     * The IP address or hostname of the trap destination
     * 
     */
    public void setTrapAddress(String trapAddress) {
        this.trapAddress = trapAddress;
    }

    /**
     * The community name to use in the trap request.
     * 
     */
    public String getTrapCommunity() {
        return trapCommunity;
    }

    /**
     * The community name to use in the trap request.
     * 
     */
    public void setTrapCommunity(String trapCommunity) {
        this.trapCommunity = trapCommunity;
    }

    /**
     * The destination port to send the trap to.
     * 
     */
    public Integer getTrapPort() {
        return trapPort;
    }

    /**
     * The destination port to send the trap to.
     * 
     */
    public void setTrapPort(Integer trapPort) {
        this.trapPort = trapPort;
    }

    /**
     * The trap version
     * 
     */
    public SnmpTrapDestination.TrapVersion getTrapVersion() {
        return trapVersion;
    }

    /**
     * The trap version
     * 
     */
    public void setTrapVersion(SnmpTrapDestination.TrapVersion trapVersion) {
        this.trapVersion = trapVersion;
    }


    /**
     * The trap version
     * 
     */
    public enum TrapVersion {

        @SerializedName("snmpV1")
        SNMP_V_1("snmpV1"),
        @SerializedName("snmpV2")
        SNMP_V_2("snmpV2"),
        @SerializedName("inform")
        INFORM("inform");
        private final String value;
        private final static Map<String, SnmpTrapDestination.TrapVersion> CONSTANTS = new HashMap<String, SnmpTrapDestination.TrapVersion>();

        static {
            for (SnmpTrapDestination.TrapVersion c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        TrapVersion(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static SnmpTrapDestination.TrapVersion fromValue(String value) {
            SnmpTrapDestination.TrapVersion constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
