
package com.hp.ws.cdm.security;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class Role {

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
     * standard uuid format - according to RFC 4122. In its canonical textual representation, the 16 octets of a UUID are represented as 32 hexadecimal (base-16) digits, displayed in five groups separated by hyphens, in the form 8-4-4-4-12 for a total of 36 characters (32 hexadecimal characters and 4 hyphens).   For example: 123e4567-e89b-12d3-a456-426614174000
     * 
     */
    @SerializedName("roleId")
    @Expose
    private String roleId;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("isBuiltIn")
    @Expose
    private Property.FeatureEnabled isBuiltIn;
    /**
     * The localized named based on the Accept-Language HTTP Header used. Omitted for user-defined roles.
     * 
     */
    @SerializedName("localizedName")
    @Expose
    private String localizedName;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("isAssignable")
    @Expose
    private Property.FeatureEnabled isAssignable;

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
     * standard uuid format - according to RFC 4122. In its canonical textual representation, the 16 octets of a UUID are represented as 32 hexadecimal (base-16) digits, displayed in five groups separated by hyphens, in the form 8-4-4-4-12 for a total of 36 characters (32 hexadecimal characters and 4 hyphens).   For example: 123e4567-e89b-12d3-a456-426614174000
     * 
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * standard uuid format - according to RFC 4122. In its canonical textual representation, the 16 octets of a UUID are represented as 32 hexadecimal (base-16) digits, displayed in five groups separated by hyphens, in the form 8-4-4-4-12 for a total of 36 characters (32 hexadecimal characters and 4 hyphens).   For example: 123e4567-e89b-12d3-a456-426614174000
     * 
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getIsBuiltIn() {
        return isBuiltIn;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setIsBuiltIn(Property.FeatureEnabled isBuiltIn) {
        this.isBuiltIn = isBuiltIn;
    }

    /**
     * The localized named based on the Accept-Language HTTP Header used. Omitted for user-defined roles.
     * 
     */
    public String getLocalizedName() {
        return localizedName;
    }

    /**
     * The localized named based on the Accept-Language HTTP Header used. Omitted for user-defined roles.
     * 
     */
    public void setLocalizedName(String localizedName) {
        this.localizedName = localizedName;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getIsAssignable() {
        return isAssignable;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setIsAssignable(Property.FeatureEnabled isAssignable) {
        this.isAssignable = isAssignable;
    }

}
