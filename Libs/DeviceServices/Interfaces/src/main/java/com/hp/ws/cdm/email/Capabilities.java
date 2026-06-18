
package com.hp.ws.cdm.email;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class Capabilities {

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
    @SerializedName("isProfileSupported")
    @Expose
    private Property.FeatureEnabled isProfileSupported;
    /**
     * Maximum number of default smtp profiles allowed
     * 
     */
    @SerializedName("maxDefaultProfiles")
    @Expose
    private Integer maxDefaultProfiles;
    /**
     * Maximum number of custom smtp profiles allowed
     * 
     */
    @SerializedName("maxCustomProfiles")
    @Expose
    private Integer maxCustomProfiles;

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
    public Property.FeatureEnabled getIsProfileSupported() {
        return isProfileSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setIsProfileSupported(Property.FeatureEnabled isProfileSupported) {
        this.isProfileSupported = isProfileSupported;
    }

    /**
     * Maximum number of default smtp profiles allowed
     * 
     */
    public Integer getMaxDefaultProfiles() {
        return maxDefaultProfiles;
    }

    /**
     * Maximum number of default smtp profiles allowed
     * 
     */
    public void setMaxDefaultProfiles(Integer maxDefaultProfiles) {
        this.maxDefaultProfiles = maxDefaultProfiles;
    }

    /**
     * Maximum number of custom smtp profiles allowed
     * 
     */
    public Integer getMaxCustomProfiles() {
        return maxCustomProfiles;
    }

    /**
     * Maximum number of custom smtp profiles allowed
     * 
     */
    public void setMaxCustomProfiles(Integer maxCustomProfiles) {
        this.maxCustomProfiles = maxCustomProfiles;
    }

}
