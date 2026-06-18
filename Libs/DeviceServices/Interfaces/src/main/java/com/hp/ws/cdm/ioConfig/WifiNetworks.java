
package com.hp.ws.cdm.ioconfig;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WifiNetworks {

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("wifiNetworkList")
    @Expose
    private List<WifiNetwork> wifiNetworkList = new ArrayList<WifiNetwork>();

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

    public List<WifiNetwork> getWifiNetworkList() {
        return wifiNetworkList;
    }

    public void setWifiNetworkList(List<WifiNetwork> wifiNetworkList) {
        this.wifiNetworkList = wifiNetworkList;
    }

}
