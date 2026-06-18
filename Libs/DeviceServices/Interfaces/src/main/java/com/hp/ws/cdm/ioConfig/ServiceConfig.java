
package com.hp.ws.cdm.ioconfig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServiceConfig {

    /**
     * The Ethernet MAC address of the device
     * 
     */
    @SerializedName("ethernetMacAddress")
    @Expose
    private String ethernetMacAddress;
    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    /**
     * The Wireless MAC address of the device
     * 
     */
    @SerializedName("wifiMacAddress")
    @Expose
    private String wifiMacAddress;

    /**
     * The Ethernet MAC address of the device
     * 
     */
    public String getEthernetMacAddress() {
        return ethernetMacAddress;
    }

    /**
     * The Ethernet MAC address of the device
     * 
     */
    public void setEthernetMacAddress(String ethernetMacAddress) {
        this.ethernetMacAddress = ethernetMacAddress;
    }

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    public String getVersion() {
        return version;
    }

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * The Wireless MAC address of the device
     * 
     */
    public String getWifiMacAddress() {
        return wifiMacAddress;
    }

    /**
     * The Wireless MAC address of the device
     * 
     */
    public void setWifiMacAddress(String wifiMacAddress) {
        this.wifiMacAddress = wifiMacAddress;
    }

}
