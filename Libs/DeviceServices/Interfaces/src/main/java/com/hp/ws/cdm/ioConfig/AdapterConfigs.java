
package com.hp.ws.cdm.ioconfig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdapterConfigs {

    @SerializedName("eth0")
    @Expose
    private AdapterConfig eth0;
    @SerializedName("eth1")
    @Expose
    private AdapterConfig eth1;
    @SerializedName("usb")
    @Expose
    private AdapterConfig usb;
    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("wifi0")
    @Expose
    private AdapterConfig wifi0;
    @SerializedName("wifi1")
    @Expose
    private AdapterConfig wifi1;

    public AdapterConfig getEth0() {
        return eth0;
    }

    public void setEth0(AdapterConfig eth0) {
        this.eth0 = eth0;
    }

    public AdapterConfig getEth1() {
        return eth1;
    }

    public void setEth1(AdapterConfig eth1) {
        this.eth1 = eth1;
    }

    public AdapterConfig getUsb() {
        return usb;
    }

    public void setUsb(AdapterConfig usb) {
        this.usb = usb;
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

    public AdapterConfig getWifi0() {
        return wifi0;
    }

    public void setWifi0(AdapterConfig wifi0) {
        this.wifi0 = wifi0;
    }

    public AdapterConfig getWifi1() {
        return wifi1;
    }

    public void setWifi1(AdapterConfig wifi1) {
        this.wifi1 = wifi1;
    }

}
