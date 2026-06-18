
package com.hp.ws.cdm.jobticket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class PipelineOptionsCapabilities {

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("scalingSupported")
    @Expose
    private Property.FeatureEnabled scalingSupported;

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getScalingSupported() {
        return scalingSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setScalingSupported(Property.FeatureEnabled scalingSupported) {
        this.scalingSupported = scalingSupported;
    }

}
