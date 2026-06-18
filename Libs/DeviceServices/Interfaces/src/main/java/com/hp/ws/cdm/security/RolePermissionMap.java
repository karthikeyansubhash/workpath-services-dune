
package com.hp.ws.cdm.security;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class RolePermissionMap {

    /**
     * standard uuid format - according to RFC 4122. In its canonical textual representation, the 16 octets of a UUID are represented as 32 hexadecimal (base-16) digits, displayed in five groups separated by hyphens, in the form 8-4-4-4-12 for a total of 36 characters (32 hexadecimal characters and 4 hyphens).   For example: 123e4567-e89b-12d3-a456-426614174000
     * (Required)
     * 
     */
    @SerializedName("role")
    @Expose
    private String role;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("permissionsAreEditable")
    @Expose
    private Property.FeatureEnabled permissionsAreEditable;
    /**
     * The permissions associated with the role
     * (Required)
     * 
     */
    @SerializedName("permissions")
    @Expose
    private List<String> permissions = new ArrayList<String>();

    /**
     * standard uuid format - according to RFC 4122. In its canonical textual representation, the 16 octets of a UUID are represented as 32 hexadecimal (base-16) digits, displayed in five groups separated by hyphens, in the form 8-4-4-4-12 for a total of 36 characters (32 hexadecimal characters and 4 hyphens).   For example: 123e4567-e89b-12d3-a456-426614174000
     * (Required)
     * 
     */
    public String getRole() {
        return role;
    }

    /**
     * standard uuid format - according to RFC 4122. In its canonical textual representation, the 16 octets of a UUID are represented as 32 hexadecimal (base-16) digits, displayed in five groups separated by hyphens, in the form 8-4-4-4-12 for a total of 36 characters (32 hexadecimal characters and 4 hyphens).   For example: 123e4567-e89b-12d3-a456-426614174000
     * (Required)
     * 
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getPermissionsAreEditable() {
        return permissionsAreEditable;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setPermissionsAreEditable(Property.FeatureEnabled permissionsAreEditable) {
        this.permissionsAreEditable = permissionsAreEditable;
    }

    /**
     * The permissions associated with the role
     * (Required)
     * 
     */
    public List<String> getPermissions() {
        return permissions;
    }

    /**
     * The permissions associated with the role
     * (Required)
     * 
     */
    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

}
