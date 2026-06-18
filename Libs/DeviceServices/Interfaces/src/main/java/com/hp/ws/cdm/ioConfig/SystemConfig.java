
package com.hp.ws.cdm.ioconfig;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;
import com.hp.ws.cdm.commonglossary.Property;


/**
 * system configurations across IO adapter interfaces
 * 
 */
public class SystemConfig {

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
     * Device host, domain names
     * 
     */
    @SerializedName("identity")
    @Expose
    private DeviceIdentity identity;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("ipv4Enabled")
    @Expose
    private Property.FeatureEnabled ipv4Enabled;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("ipv6Enabled")
    @Expose
    private Property.FeatureEnabled ipv6Enabled;
    @SerializedName("ioTimeout")
    @Expose
    private Integer ioTimeout;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("connectivityAlertsEnabled")
    @Expose
    private Property.FeatureEnabled connectivityAlertsEnabled;
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();

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
     * Device host, domain names
     * 
     */
    public DeviceIdentity getIdentity() {
        return identity;
    }

    /**
     * Device host, domain names
     * 
     */
    public void setIdentity(DeviceIdentity identity) {
        this.identity = identity;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getIpv4Enabled() {
        return ipv4Enabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setIpv4Enabled(Property.FeatureEnabled ipv4Enabled) {
        this.ipv4Enabled = ipv4Enabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getIpv6Enabled() {
        return ipv6Enabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setIpv6Enabled(Property.FeatureEnabled ipv6Enabled) {
        this.ipv6Enabled = ipv6Enabled;
    }

    public Integer getIoTimeout() {
        return ioTimeout;
    }

    public void setIoTimeout(Integer ioTimeout) {
        this.ioTimeout = ioTimeout;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getConnectivityAlertsEnabled() {
        return connectivityAlertsEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setConnectivityAlertsEnabled(Property.FeatureEnabled connectivityAlertsEnabled) {
        this.connectivityAlertsEnabled = connectivityAlertsEnabled;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

}
