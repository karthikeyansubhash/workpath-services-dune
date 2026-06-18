
package com.hp.ws.cdm.security;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;
import com.hp.ws.cdm.commonglossary.Property;

public class IdCodeConfig {

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
    @SerializedName("enabled")
    @Expose
    private Property.FeatureEnabled enabled;
    /**
     * It tells the maximum number of ID codes supported
     * 
     */
    @SerializedName("maxIdCodesSupported")
    @Expose
    private Integer maxIdCodesSupported;
    /**
     * It provides the default permission set for new codes
     * 
     */
    @SerializedName("defaultRole")
    @Expose
    private String defaultRole;
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

    /**
     * It tells the maximum number of ID codes supported
     * 
     */
    public Integer getMaxIdCodesSupported() {
        return maxIdCodesSupported;
    }

    /**
     * It tells the maximum number of ID codes supported
     * 
     */
    public void setMaxIdCodesSupported(Integer maxIdCodesSupported) {
        this.maxIdCodesSupported = maxIdCodesSupported;
    }

    /**
     * It provides the default permission set for new codes
     * 
     */
    public String getDefaultRole() {
        return defaultRole;
    }

    /**
     * It provides the default permission set for new codes
     * 
     */
    public void setDefaultRole(String defaultRole) {
        this.defaultRole = defaultRole;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

}
