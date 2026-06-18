
package com.hp.ws.cdm.ioconfig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdapterStats {

    @SerializedName("eth0")
    @Expose
    private IoStatistics eth0;
    @SerializedName("eth1")
    @Expose
    private IoStatistics eth1;
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
    private IoStatistics wifi0;
    @SerializedName("wifi1")
    @Expose
    private IoStatistics wifi1;

    public IoStatistics getEth0() {
        return eth0;
    }

    public void setEth0(IoStatistics eth0) {
        this.eth0 = eth0;
    }

    public IoStatistics getEth1() {
        return eth1;
    }

    public void setEth1(IoStatistics eth1) {
        this.eth1 = eth1;
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

    public IoStatistics getWifi0() {
        return wifi0;
    }

    public void setWifi0(IoStatistics wifi0) {
        this.wifi0 = wifi0;
    }

    public IoStatistics getWifi1() {
        return wifi1;
    }

    public void setWifi1(IoStatistics wifi1) {
        this.wifi1 = wifi1;
    }

}
