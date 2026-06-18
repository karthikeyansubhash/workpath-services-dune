
package com.hp.ws.cdm.security;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PermissionGranted {

    /**
     * standard uuid format - according to RFC 4122. In its canonical textual representation, the 16 octets of a UUID are represented as 32 hexadecimal (base-16) digits, displayed in five groups separated by hyphens, in the form 8-4-4-4-12 for a total of 36 characters (32 hexadecimal characters and 4 hyphens).   For example: 123e4567-e89b-12d3-a456-426614174000
     * (Required)
     * 
     */
    @SerializedName("permissionId")
    @Expose
    private String permissionId;
    /**
     * Internal permission number
     * (Required)
     * 
     */
    @SerializedName("permissionNumber")
    @Expose
    private Integer permissionNumber;

    /**
     * standard uuid format - according to RFC 4122. In its canonical textual representation, the 16 octets of a UUID are represented as 32 hexadecimal (base-16) digits, displayed in five groups separated by hyphens, in the form 8-4-4-4-12 for a total of 36 characters (32 hexadecimal characters and 4 hyphens).   For example: 123e4567-e89b-12d3-a456-426614174000
     * (Required)
     * 
     */
    public String getPermissionId() {
        return permissionId;
    }

    /**
     * standard uuid format - according to RFC 4122. In its canonical textual representation, the 16 octets of a UUID are represented as 32 hexadecimal (base-16) digits, displayed in five groups separated by hyphens, in the form 8-4-4-4-12 for a total of 36 characters (32 hexadecimal characters and 4 hyphens).   For example: 123e4567-e89b-12d3-a456-426614174000
     * (Required)
     * 
     */
    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    /**
     * Internal permission number
     * (Required)
     * 
     */
    public Integer getPermissionNumber() {
        return permissionNumber;
    }

    /**
     * Internal permission number
     * (Required)
     * 
     */
    public void setPermissionNumber(Integer permissionNumber) {
        this.permissionNumber = permissionNumber;
    }

}
