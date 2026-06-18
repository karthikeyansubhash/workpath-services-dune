
package com.hp.ws.cdm.security;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;


/**
 * Contains the Secure Boot feature details
 * 
 */
public class SecureBootSettings {

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("intrusionDetectionSupported")
    @Expose
    private Property.FeatureEnabled intrusionDetectionSupported;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("sureStartSupported")
    @Expose
    private Property.FeatureEnabled sureStartSupported;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("whiteListingSupported")
    @Expose
    private Property.FeatureEnabled whiteListingSupported;

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getIntrusionDetectionSupported() {
        return intrusionDetectionSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setIntrusionDetectionSupported(Property.FeatureEnabled intrusionDetectionSupported) {
        this.intrusionDetectionSupported = intrusionDetectionSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getSureStartSupported() {
        return sureStartSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setSureStartSupported(Property.FeatureEnabled sureStartSupported) {
        this.sureStartSupported = sureStartSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getWhiteListingSupported() {
        return whiteListingSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setWhiteListingSupported(Property.FeatureEnabled whiteListingSupported) {
        this.whiteListingSupported = whiteListingSupported;
    }

}
