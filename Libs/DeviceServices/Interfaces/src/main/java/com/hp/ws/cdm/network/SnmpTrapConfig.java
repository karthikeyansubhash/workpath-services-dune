
package com.hp.ws.cdm.network;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;
import com.hp.ws.cdm.commonglossary.Property;

public class SnmpTrapConfig {

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("enabled")
    @Expose
    private Property.FeatureEnabled enabled;
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();
    /**
     * Maximum number of trap destinations supported
     * 
     */
    @SerializedName("maxTrapDestinations")
    @Expose
    private Integer maxTrapDestinations;
    /**
     * Applicable only if trapVersion is set to Inform. Units are 10 millisecond intervals. Retries are sent at times corresponding to the retry number multiplied by the trapBaseTimeout
     * 
     */
    @SerializedName("trapBaseTimeout")
    @Expose
    private Integer trapBaseTimeout;
    @SerializedName("trapDestinations")
    @Expose
    private List<SnmpTrapDestination> trapDestinations = new ArrayList<SnmpTrapDestination>();
    /**
     * The maximum number of Inform replies that will be sent to the destination.  Retries are sent if no inform reply is received within the timeout value.
     * 
     */
    @SerializedName("trapRetries")
    @Expose
    private Integer trapRetries;
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
    public Property.FeatureEnabled getEnabled() {
        return enabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setEnabled(Property.FeatureEnabled enabled) {
        this.enabled = enabled;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    /**
     * Maximum number of trap destinations supported
     * 
     */
    public Integer getMaxTrapDestinations() {
        return maxTrapDestinations;
    }

    /**
     * Maximum number of trap destinations supported
     * 
     */
    public void setMaxTrapDestinations(Integer maxTrapDestinations) {
        this.maxTrapDestinations = maxTrapDestinations;
    }

    /**
     * Applicable only if trapVersion is set to Inform. Units are 10 millisecond intervals. Retries are sent at times corresponding to the retry number multiplied by the trapBaseTimeout
     * 
     */
    public Integer getTrapBaseTimeout() {
        return trapBaseTimeout;
    }

    /**
     * Applicable only if trapVersion is set to Inform. Units are 10 millisecond intervals. Retries are sent at times corresponding to the retry number multiplied by the trapBaseTimeout
     * 
     */
    public void setTrapBaseTimeout(Integer trapBaseTimeout) {
        this.trapBaseTimeout = trapBaseTimeout;
    }

    public List<SnmpTrapDestination> getTrapDestinations() {
        return trapDestinations;
    }

    public void setTrapDestinations(List<SnmpTrapDestination> trapDestinations) {
        this.trapDestinations = trapDestinations;
    }

    /**
     * The maximum number of Inform replies that will be sent to the destination.  Retries are sent if no inform reply is received within the timeout value.
     * 
     */
    public Integer getTrapRetries() {
        return trapRetries;
    }

    /**
     * The maximum number of Inform replies that will be sent to the destination.  Retries are sent if no inform reply is received within the timeout value.
     * 
     */
    public void setTrapRetries(Integer trapRetries) {
        this.trapRetries = trapRetries;
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
