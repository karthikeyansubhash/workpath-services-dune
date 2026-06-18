
package com.hp.ws.cdm.security;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Permissions {

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
    @SerializedName("visibleRootPermission")
    @Expose
    private String visibleRootPermission;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("permissions")
    @Expose
    private List<Permission> permissions = new ArrayList<Permission>();
    /**
     * The set of authentication methods (by ID) that can be assigned to editable permissions
     * 
     */
    @SerializedName("allowedAuthenticationMethods")
    @Expose
    private List<String> allowedAuthenticationMethods = new ArrayList<String>();

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
    public String getVisibleRootPermission() {
        return visibleRootPermission;
    }

    /**
     * standard uuid format - according to RFC 4122. In its canonical textual representation, the 16 octets of a UUID are represented as 32 hexadecimal (base-16) digits, displayed in five groups separated by hyphens, in the form 8-4-4-4-12 for a total of 36 characters (32 hexadecimal characters and 4 hyphens).   For example: 123e4567-e89b-12d3-a456-426614174000
     * 
     */
    public void setVisibleRootPermission(String visibleRootPermission) {
        this.visibleRootPermission = visibleRootPermission;
    }

    /**
     * 
     * (Required)
     * 
     */
    public List<Permission> getPermissions() {
        return permissions;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    /**
     * The set of authentication methods (by ID) that can be assigned to editable permissions
     * 
     */
    public List<String> getAllowedAuthenticationMethods() {
        return allowedAuthenticationMethods;
    }

    /**
     * The set of authentication methods (by ID) that can be assigned to editable permissions
     * 
     */
    public void setAllowedAuthenticationMethods(List<String> allowedAuthenticationMethods) {
        this.allowedAuthenticationMethods = allowedAuthenticationMethods;
    }

}
