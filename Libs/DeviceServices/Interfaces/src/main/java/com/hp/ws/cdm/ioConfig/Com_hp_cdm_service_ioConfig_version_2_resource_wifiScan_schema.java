
package com.hp.ws.cdm.ioconfig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Wireless networks definition
 * 
 */
public class Com_hp_cdm_service_ioConfig_version_2_resource_wifiScan_schema {

    @SerializedName("wifiScan")
    @Expose
    private WifiScan wifiScan;
    @SerializedName("wifiNetworks")
    @Expose
    private WifiNetworks wifiNetworks;

    public WifiScan getWifiScan() {
        return wifiScan;
    }

    public void setWifiScan(WifiScan wifiScan) {
        this.wifiScan = wifiScan;
    }

    public WifiNetworks getWifiNetworks() {
        return wifiNetworks;
    }

    public void setWifiNetworks(WifiNetworks wifiNetworks) {
        this.wifiNetworks = wifiNetworks;
    }

}
