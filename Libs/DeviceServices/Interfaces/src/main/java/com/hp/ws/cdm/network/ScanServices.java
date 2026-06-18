
package com.hp.ws.cdm.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class ScanServices {

    @SerializedName("eSCL")
    @Expose
    private ESCL eSCL;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("wsScan")
    @Expose
    private Property.FeatureEnabled wsScan;

    public ESCL geteSCL() {
        return eSCL;
    }

    public void seteSCL(ESCL eSCL) {
        this.eSCL = eSCL;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getWsScan() {
        return wsScan;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setWsScan(Property.FeatureEnabled wsScan) {
        this.wsScan = wsScan;
    }

}
