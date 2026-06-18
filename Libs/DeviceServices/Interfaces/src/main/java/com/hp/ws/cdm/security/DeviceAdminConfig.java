
package com.hp.ws.cdm.security;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class DeviceAdminConfig {

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
    @SerializedName("passwordByDefaultSupported")
    @Expose
    private Property.FeatureEnabled passwordByDefaultSupported;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("configuredByUser")
    @Expose
    private Property.FeatureEnabled configuredByUser;
    /**
     * element must be present either if 'configured' or 'passwordByDefaultSupported' are 'true'. 0 means password has been set with the empty string. This attribute is deprecated in Dune
     * 
     */
    @SerializedName("currentPasswordLength")
    @Expose
    private Integer currentPasswordLength;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("passwordSet")
    @Expose
    private Property.FeatureEnabled passwordSet;
    @SerializedName("credentials")
    @Expose
    private Credentials__1 credentials;

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
    public Property.FeatureEnabled getPasswordByDefaultSupported() {
        return passwordByDefaultSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setPasswordByDefaultSupported(Property.FeatureEnabled passwordByDefaultSupported) {
        this.passwordByDefaultSupported = passwordByDefaultSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getConfiguredByUser() {
        return configuredByUser;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setConfiguredByUser(Property.FeatureEnabled configuredByUser) {
        this.configuredByUser = configuredByUser;
    }

    /**
     * element must be present either if 'configured' or 'passwordByDefaultSupported' are 'true'. 0 means password has been set with the empty string. This attribute is deprecated in Dune
     * 
     */
    public Integer getCurrentPasswordLength() {
        return currentPasswordLength;
    }

    /**
     * element must be present either if 'configured' or 'passwordByDefaultSupported' are 'true'. 0 means password has been set with the empty string. This attribute is deprecated in Dune
     * 
     */
    public void setCurrentPasswordLength(Integer currentPasswordLength) {
        this.currentPasswordLength = currentPasswordLength;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getPasswordSet() {
        return passwordSet;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setPasswordSet(Property.FeatureEnabled passwordSet) {
        this.passwordSet = passwordSet;
    }

    public Credentials__1 getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials__1 credentials) {
        this.credentials = credentials;
    }

}
