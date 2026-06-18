
package com.hp.ws.cdm.ioconfig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class Capabilities {

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("bootpSupported")
    @Expose
    private Property.FeatureEnabled bootpSupported;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("dot1xEthernetSupported")
    @Expose
    private Property.FeatureEnabled dot1xEthernetSupported;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("interfaceIdentityCfgSupported")
    @Expose
    private Property.FeatureEnabled interfaceIdentityCfgSupported;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("interfaceIpCtrlSupported")
    @Expose
    private Property.FeatureEnabled interfaceIpCtrlSupported;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("networkSwitchEnabled")
    @Expose
    private Property.FeatureEnabled networkSwitchEnabled;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("preferredDnsConfigMethodSupported")
    @Expose
    private Property.FeatureEnabled preferredDnsConfigMethodSupported;
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
    public Property.FeatureEnabled getBootpSupported() {
        return bootpSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setBootpSupported(Property.FeatureEnabled bootpSupported) {
        this.bootpSupported = bootpSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getDot1xEthernetSupported() {
        return dot1xEthernetSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setDot1xEthernetSupported(Property.FeatureEnabled dot1xEthernetSupported) {
        this.dot1xEthernetSupported = dot1xEthernetSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getInterfaceIdentityCfgSupported() {
        return interfaceIdentityCfgSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setInterfaceIdentityCfgSupported(Property.FeatureEnabled interfaceIdentityCfgSupported) {
        this.interfaceIdentityCfgSupported = interfaceIdentityCfgSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getInterfaceIpCtrlSupported() {
        return interfaceIpCtrlSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setInterfaceIpCtrlSupported(Property.FeatureEnabled interfaceIpCtrlSupported) {
        this.interfaceIpCtrlSupported = interfaceIpCtrlSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getNetworkSwitchEnabled() {
        return networkSwitchEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setNetworkSwitchEnabled(Property.FeatureEnabled networkSwitchEnabled) {
        this.networkSwitchEnabled = networkSwitchEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getPreferredDnsConfigMethodSupported() {
        return preferredDnsConfigMethodSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setPreferredDnsConfigMethodSupported(Property.FeatureEnabled preferredDnsConfigMethodSupported) {
        this.preferredDnsConfigMethodSupported = preferredDnsConfigMethodSupported;
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
