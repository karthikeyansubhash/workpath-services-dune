
package com.hp.ws.cdm.jobticket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class ScanCapabilities {

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("contentTypeSupported")
    @Expose
    private Property.FeatureEnabled contentTypeSupported;

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getContentTypeSupported() {
        return contentTypeSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setContentTypeSupported(Property.FeatureEnabled contentTypeSupported) {
        this.contentTypeSupported = contentTypeSupported;
    }

}
