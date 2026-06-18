
package com.hp.ws.cdm.security;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;


/**
 * Secure Print device capabilities
 * 
 */
public class SecurePrint {

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("esepSupported")
    @Expose
    private Property.FeatureEnabled esepSupported;

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getEsepSupported() {
        return esepSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setEsepSupported(Property.FeatureEnabled esepSupported) {
        this.esepSupported = esepSupported;
    }

}
