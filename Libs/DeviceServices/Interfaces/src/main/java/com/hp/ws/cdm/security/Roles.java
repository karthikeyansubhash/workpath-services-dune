
package com.hp.ws.cdm.security;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class Roles {

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
     * The maximum number of roles supported
     * 
     */
    @SerializedName("maxRoles")
    @Expose
    private Integer maxRoles;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("customRolesSupported")
    @Expose
    private Property.FeatureEnabled customRolesSupported;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("roles")
    @Expose
    private List<Role> roles = new ArrayList<Role>();

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
     * The maximum number of roles supported
     * 
     */
    public Integer getMaxRoles() {
        return maxRoles;
    }

    /**
     * The maximum number of roles supported
     * 
     */
    public void setMaxRoles(Integer maxRoles) {
        this.maxRoles = maxRoles;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getCustomRolesSupported() {
        return customRolesSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setCustomRolesSupported(Property.FeatureEnabled customRolesSupported) {
        this.customRolesSupported = customRolesSupported;
    }

    /**
     * 
     * (Required)
     * 
     */
    public List<Role> getRoles() {
        return roles;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

}
