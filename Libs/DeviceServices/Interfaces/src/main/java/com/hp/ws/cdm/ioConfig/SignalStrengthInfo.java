
package com.hp.ws.cdm.ioconfig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Wireless signal strength
 * 
 */
public class SignalStrengthInfo {

    /**
     * The current signal strength in decibels
     * 
     */
    @SerializedName("dBm")
    @Expose
    private Integer dBm;
    /**
     * The current signal strength approximated to a value in range 1-5
     * 
     */
    @SerializedName("signalStrength")
    @Expose
    private Integer signalStrength;

    /**
     * The current signal strength in decibels
     * 
     */
    public Integer getdBm() {
        return dBm;
    }

    /**
     * The current signal strength in decibels
     * 
     */
    public void setdBm(Integer dBm) {
        this.dBm = dBm;
    }

    /**
     * The current signal strength approximated to a value in range 1-5
     * 
     */
    public Integer getSignalStrength() {
        return signalStrength;
    }

    /**
     * The current signal strength approximated to a value in range 1-5
     * 
     */
    public void setSignalStrength(Integer signalStrength) {
        this.signalStrength = signalStrength;
    }

}
