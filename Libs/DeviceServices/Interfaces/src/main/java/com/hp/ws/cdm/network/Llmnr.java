
package com.hp.ws.cdm.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class Llmnr {

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("enabled")
    @Expose
    private Property.FeatureEnabled enabled;

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getEnabled() {
        return enabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setEnabled(Property.FeatureEnabled enabled) {
        this.enabled = enabled;
    }

}
