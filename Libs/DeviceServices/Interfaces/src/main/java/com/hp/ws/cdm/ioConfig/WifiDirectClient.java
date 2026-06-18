
package com.hp.ws.cdm.ioconfig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WifiDirectClient {

    /**
     * WFD Client name
     * 
     */
    @SerializedName("clientName")
    @Expose
    private String clientName;
    /**
     * WFD Client MAC address
     * 
     */
    @SerializedName("clientMacAddress")
    @Expose
    private String clientMacAddress;
    /**
     * WFD Client IP address
     * 
     */
    @SerializedName("clientIpAddress")
    @Expose
    private String clientIpAddress;

    /**
     * WFD Client name
     * 
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * WFD Client name
     * 
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * WFD Client MAC address
     * 
     */
    public String getClientMacAddress() {
        return clientMacAddress;
    }

    /**
     * WFD Client MAC address
     * 
     */
    public void setClientMacAddress(String clientMacAddress) {
        this.clientMacAddress = clientMacAddress;
    }

    /**
     * WFD Client IP address
     * 
     */
    public String getClientIpAddress() {
        return clientIpAddress;
    }

    /**
     * WFD Client IP address
     * 
     */
    public void setClientIpAddress(String clientIpAddress) {
        this.clientIpAddress = clientIpAddress;
    }

}
