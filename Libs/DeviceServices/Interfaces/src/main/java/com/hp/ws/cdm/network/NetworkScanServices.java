
package com.hp.ws.cdm.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class NetworkScanServices {

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("networkInitiatedScan")
    @Expose
    private Property.FeatureEnabled networkInitiatedScan;
    @SerializedName("scanServices")
    @Expose
    private ScanServices scanServices;
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
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getNetworkInitiatedScan() {
        return networkInitiatedScan;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setNetworkInitiatedScan(Property.FeatureEnabled networkInitiatedScan) {
        this.networkInitiatedScan = networkInitiatedScan;
    }

    public ScanServices getScanServices() {
        return scanServices;
    }

    public void setScanServices(ScanServices scanServices) {
        this.scanServices = scanServices;
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

}
