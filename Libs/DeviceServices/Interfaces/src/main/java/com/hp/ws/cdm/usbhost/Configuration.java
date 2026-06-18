
package com.hp.ws.cdm.usbhost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;


/**
 * USB host configuration
 * 
 */
public class Configuration {

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
    @SerializedName("plugAndPlayEnabled")
    @Expose
    private Property.FeatureEnabled plugAndPlayEnabled;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("printFromUsbEnabled")
    @Expose
    private Property.FeatureEnabled printFromUsbEnabled;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("scanToUsbEnabled")
    @Expose
    private Property.FeatureEnabled scanToUsbEnabled;

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
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getPlugAndPlayEnabled() {
        return plugAndPlayEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setPlugAndPlayEnabled(Property.FeatureEnabled plugAndPlayEnabled) {
        this.plugAndPlayEnabled = plugAndPlayEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getPrintFromUsbEnabled() {
        return printFromUsbEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setPrintFromUsbEnabled(Property.FeatureEnabled printFromUsbEnabled) {
        this.printFromUsbEnabled = printFromUsbEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getScanToUsbEnabled() {
        return scanToUsbEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setScanToUsbEnabled(Property.FeatureEnabled scanToUsbEnabled) {
        this.scanToUsbEnabled = scanToUsbEnabled;
    }

}
