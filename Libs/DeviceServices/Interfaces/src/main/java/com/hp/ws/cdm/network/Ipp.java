
package com.hp.ws.cdm.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class Ipp {

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("enableCertificateValidation")
    @Expose
    private Property.FeatureEnabled enableCertificateValidation;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("enableUserAuthentication")
    @Expose
    private Property.FeatureEnabled enableUserAuthentication;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("ipp")
    @Expose
    private Property.FeatureEnabled ipp;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("ippSecure")
    @Expose
    private Property.FeatureEnabled ippSecure;

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getEnableCertificateValidation() {
        return enableCertificateValidation;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setEnableCertificateValidation(Property.FeatureEnabled enableCertificateValidation) {
        this.enableCertificateValidation = enableCertificateValidation;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getEnableUserAuthentication() {
        return enableUserAuthentication;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setEnableUserAuthentication(Property.FeatureEnabled enableUserAuthentication) {
        this.enableUserAuthentication = enableUserAuthentication;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getIpp() {
        return ipp;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setIpp(Property.FeatureEnabled ipp) {
        this.ipp = ipp;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getIppSecure() {
        return ippSecure;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setIppSecure(Property.FeatureEnabled ippSecure) {
        this.ippSecure = ippSecure;
    }

}
