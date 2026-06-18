
package com.hp.ws.cdm.ioconfig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UsbConfig {

    /**
     * Custom suffix to serial number to allow host based filtering
     * 
     */
    @SerializedName("serialNumberSuffix")
    @Expose
    private String serialNumberSuffix;

    /**
     * Custom suffix to serial number to allow host based filtering
     * 
     */
    public String getSerialNumberSuffix() {
        return serialNumberSuffix;
    }

    /**
     * Custom suffix to serial number to allow host based filtering
     * 
     */
    public void setSerialNumberSuffix(String serialNumberSuffix) {
        this.serialNumberSuffix = serialNumberSuffix;
    }

}
