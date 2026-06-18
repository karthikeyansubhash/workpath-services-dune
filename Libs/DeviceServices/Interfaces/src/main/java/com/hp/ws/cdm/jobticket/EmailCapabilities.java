
package com.hp.ws.cdm.jobticket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class EmailCapabilities {

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("ccListSupported")
    @Expose
    private Property.FeatureEnabled ccListSupported;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("bccListSupported")
    @Expose
    private Property.FeatureEnabled bccListSupported;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("emailSigningSupported")
    @Expose
    private Property.FeatureEnabled emailSigningSupported;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("emailEncryptionSupported")
    @Expose
    private Property.FeatureEnabled emailEncryptionSupported;

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getCcListSupported() {
        return ccListSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setCcListSupported(Property.FeatureEnabled ccListSupported) {
        this.ccListSupported = ccListSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getBccListSupported() {
        return bccListSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setBccListSupported(Property.FeatureEnabled bccListSupported) {
        this.bccListSupported = bccListSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getEmailSigningSupported() {
        return emailSigningSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setEmailSigningSupported(Property.FeatureEnabled emailSigningSupported) {
        this.emailSigningSupported = emailSigningSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getEmailEncryptionSupported() {
        return emailEncryptionSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setEmailEncryptionSupported(Property.FeatureEnabled emailEncryptionSupported) {
        this.emailEncryptionSupported = emailEncryptionSupported;
    }

}
