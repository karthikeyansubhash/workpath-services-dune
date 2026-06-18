
package com.hp.ws.cdm.security;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class ServiceUserConfig {

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
    @SerializedName("passwordConfiguredByUser")
    @Expose
    private Property.FeatureEnabled passwordConfiguredByUser;
    /**
     * If specified on a PATCH, changes the service user's current password or PIN code. The new value must comply with the product's policy for service user passwords. The current password is never returned when getting the configuration).
     * 
     */
    @SerializedName("password")
    @Expose
    private String password;

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
    public Property.FeatureEnabled getPasswordConfiguredByUser() {
        return passwordConfiguredByUser;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setPasswordConfiguredByUser(Property.FeatureEnabled passwordConfiguredByUser) {
        this.passwordConfiguredByUser = passwordConfiguredByUser;
    }

    /**
     * If specified on a PATCH, changes the service user's current password or PIN code. The new value must comply with the product's policy for service user passwords. The current password is never returned when getting the configuration).
     * 
     */
    public String getPassword() {
        return password;
    }

    /**
     * If specified on a PATCH, changes the service user's current password or PIN code. The new value must comply with the product's policy for service user passwords. The current password is never returned when getting the configuration).
     * 
     */
    public void setPassword(String password) {
        this.password = password;
    }

}
