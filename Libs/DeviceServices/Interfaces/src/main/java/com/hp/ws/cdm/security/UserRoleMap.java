
package com.hp.ws.cdm.security;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class UserRoleMap {

    /**
     * username
     * 
     */
    @SerializedName("user")
    @Expose
    private String user;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("isGroup")
    @Expose
    private Property.FeatureEnabled isGroup;
    /**
     * role
     * 
     */
    @SerializedName("role")
    @Expose
    private String role;

    /**
     * username
     * 
     */
    public String getUser() {
        return user;
    }

    /**
     * username
     * 
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getIsGroup() {
        return isGroup;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setIsGroup(Property.FeatureEnabled isGroup) {
        this.isGroup = isGroup;
    }

    /**
     * role
     * 
     */
    public String getRole() {
        return role;
    }

    /**
     * role
     * 
     */
    public void setRole(String role) {
        this.role = role;
    }

}
