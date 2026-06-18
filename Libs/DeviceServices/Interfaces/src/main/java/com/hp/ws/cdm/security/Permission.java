
package com.hp.ws.cdm.security;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class Permission {

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
    @SerializedName("permissionId")
    @Expose
    private String permissionId;
    /**
     * The localized named based on the Accept-Language HTTP Header used.
     * 
     */
    @SerializedName("localizedName")
    @Expose
    private String localizedName;
    /**
     * standard uuid format - according to RFC 4122. In its canonical textual representation, the 16 octets of a UUID are represented as 32 hexadecimal (base-16) digits, displayed in five groups separated by hyphens, in the form 8-4-4-4-12 for a total of 36 characters (32 hexadecimal characters and 4 hyphens).   For example: 123e4567-e89b-12d3-a456-426614174000
     * 
     */
    @SerializedName("parentPermission")
    @Expose
    private String parentPermission;
    /**
     * If present, this permission is a part of a hierarchy; the indicated permissions are this permission's children.
     * 
     */
    @SerializedName("childPermissions")
    @Expose
    private List<String> childPermissions = new ArrayList<String>();
    /**
     * standard uuid format - according to RFC 4122. In its canonical textual representation, the 16 octets of a UUID are represented as 32 hexadecimal (base-16) digits, displayed in five groups separated by hyphens, in the form 8-4-4-4-12 for a total of 36 characters (32 hexadecimal characters and 4 hyphens).   For example: 123e4567-e89b-12d3-a456-426614174000
     * 
     */
    @SerializedName("configuredAuthenticationMethod")
    @Expose
    private String configuredAuthenticationMethod;
    /**
     * standard uuid format - according to RFC 4122. In its canonical textual representation, the 16 octets of a UUID are represented as 32 hexadecimal (base-16) digits, displayed in five groups separated by hyphens, in the form 8-4-4-4-12 for a total of 36 characters (32 hexadecimal characters and 4 hyphens).   For example: 123e4567-e89b-12d3-a456-426614174000
     * 
     */
    @SerializedName("effectiveAuthenticationMethod")
    @Expose
    private String effectiveAuthenticationMethod;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("isAuthenticationMethodEditable")
    @Expose
    private Property.FeatureEnabled isAuthenticationMethodEditable;

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
    public String getPermissionId() {
        return permissionId;
    }

    /**
     * standard uuid format - according to RFC 4122. In its canonical textual representation, the 16 octets of a UUID are represented as 32 hexadecimal (base-16) digits, displayed in five groups separated by hyphens, in the form 8-4-4-4-12 for a total of 36 characters (32 hexadecimal characters and 4 hyphens).   For example: 123e4567-e89b-12d3-a456-426614174000
     * 
     */
    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    /**
     * The localized named based on the Accept-Language HTTP Header used.
     * 
     */
    public String getLocalizedName() {
        return localizedName;
    }

    /**
     * The localized named based on the Accept-Language HTTP Header used.
     * 
     */
    public void setLocalizedName(String localizedName) {
        this.localizedName = localizedName;
    }

    /**
     * standard uuid format - according to RFC 4122. In its canonical textual representation, the 16 octets of a UUID are represented as 32 hexadecimal (base-16) digits, displayed in five groups separated by hyphens, in the form 8-4-4-4-12 for a total of 36 characters (32 hexadecimal characters and 4 hyphens).   For example: 123e4567-e89b-12d3-a456-426614174000
     * 
     */
    public String getParentPermission() {
        return parentPermission;
    }

    /**
     * standard uuid format - according to RFC 4122. In its canonical textual representation, the 16 octets of a UUID are represented as 32 hexadecimal (base-16) digits, displayed in five groups separated by hyphens, in the form 8-4-4-4-12 for a total of 36 characters (32 hexadecimal characters and 4 hyphens).   For example: 123e4567-e89b-12d3-a456-426614174000
     * 
     */
    public void setParentPermission(String parentPermission) {
        this.parentPermission = parentPermission;
    }

    /**
     * If present, this permission is a part of a hierarchy; the indicated permissions are this permission's children.
     * 
     */
    public List<String> getChildPermissions() {
        return childPermissions;
    }

    /**
     * If present, this permission is a part of a hierarchy; the indicated permissions are this permission's children.
     * 
     */
    public void setChildPermissions(List<String> childPermissions) {
        this.childPermissions = childPermissions;
    }

    /**
     * standard uuid format - according to RFC 4122. In its canonical textual representation, the 16 octets of a UUID are represented as 32 hexadecimal (base-16) digits, displayed in five groups separated by hyphens, in the form 8-4-4-4-12 for a total of 36 characters (32 hexadecimal characters and 4 hyphens).   For example: 123e4567-e89b-12d3-a456-426614174000
     * 
     */
    public String getConfiguredAuthenticationMethod() {
        return configuredAuthenticationMethod;
    }

    /**
     * standard uuid format - according to RFC 4122. In its canonical textual representation, the 16 octets of a UUID are represented as 32 hexadecimal (base-16) digits, displayed in five groups separated by hyphens, in the form 8-4-4-4-12 for a total of 36 characters (32 hexadecimal characters and 4 hyphens).   For example: 123e4567-e89b-12d3-a456-426614174000
     * 
     */
    public void setConfiguredAuthenticationMethod(String configuredAuthenticationMethod) {
        this.configuredAuthenticationMethod = configuredAuthenticationMethod;
    }

    /**
     * standard uuid format - according to RFC 4122. In its canonical textual representation, the 16 octets of a UUID are represented as 32 hexadecimal (base-16) digits, displayed in five groups separated by hyphens, in the form 8-4-4-4-12 for a total of 36 characters (32 hexadecimal characters and 4 hyphens).   For example: 123e4567-e89b-12d3-a456-426614174000
     * 
     */
    public String getEffectiveAuthenticationMethod() {
        return effectiveAuthenticationMethod;
    }

    /**
     * standard uuid format - according to RFC 4122. In its canonical textual representation, the 16 octets of a UUID are represented as 32 hexadecimal (base-16) digits, displayed in five groups separated by hyphens, in the form 8-4-4-4-12 for a total of 36 characters (32 hexadecimal characters and 4 hyphens).   For example: 123e4567-e89b-12d3-a456-426614174000
     * 
     */
    public void setEffectiveAuthenticationMethod(String effectiveAuthenticationMethod) {
        this.effectiveAuthenticationMethod = effectiveAuthenticationMethod;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getIsAuthenticationMethodEditable() {
        return isAuthenticationMethodEditable;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setIsAuthenticationMethodEditable(Property.FeatureEnabled isAuthenticationMethodEditable) {
        this.isAuthenticationMethodEditable = isAuthenticationMethodEditable;
    }

}
