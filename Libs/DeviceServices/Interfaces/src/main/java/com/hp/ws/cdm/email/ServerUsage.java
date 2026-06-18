
package com.hp.ws.cdm.email;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class ServerUsage {

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("email")
    @Expose
    private Property.FeatureEnabled email;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("fax")
    @Expose
    private Property.FeatureEnabled fax;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("automatedEmail")
    @Expose
    private Property.FeatureEnabled automatedEmail;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("autoSend")
    @Expose
    private Property.FeatureEnabled autoSend;

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getEmail() {
        return email;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setEmail(Property.FeatureEnabled email) {
        this.email = email;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getFax() {
        return fax;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setFax(Property.FeatureEnabled fax) {
        this.fax = fax;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getAutomatedEmail() {
        return automatedEmail;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setAutomatedEmail(Property.FeatureEnabled automatedEmail) {
        this.automatedEmail = automatedEmail;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getAutoSend() {
        return autoSend;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setAutoSend(Property.FeatureEnabled autoSend) {
        this.autoSend = autoSend;
    }

}
