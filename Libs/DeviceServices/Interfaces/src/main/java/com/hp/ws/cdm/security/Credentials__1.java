
package com.hp.ws.cdm.security;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Credentials__1 {

    /**
     * Currently configured Administrator Password on the device
     * 
     */
    @SerializedName("currentPassword")
    @Expose
    private String currentPassword;
    /**
     * Proposed password to change the configured Administrator Password on the device
     * 
     */
    @SerializedName("proposedPassword")
    @Expose
    private String proposedPassword;

    /**
     * Currently configured Administrator Password on the device
     * 
     */
    public String getCurrentPassword() {
        return currentPassword;
    }

    /**
     * Currently configured Administrator Password on the device
     * 
     */
    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    /**
     * Proposed password to change the configured Administrator Password on the device
     * 
     */
    public String getProposedPassword() {
        return proposedPassword;
    }

    /**
     * Proposed password to change the configured Administrator Password on the device
     * 
     */
    public void setProposedPassword(String proposedPassword) {
        this.proposedPassword = proposedPassword;
    }

}
