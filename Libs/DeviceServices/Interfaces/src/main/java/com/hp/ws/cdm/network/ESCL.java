
package com.hp.ws.cdm.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class ESCL {

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("eSCL")
    @Expose
    private Property.FeatureEnabled eSCL;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("eSCLSecure")
    @Expose
    private Property.FeatureEnabled eSCLSecure;
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
    @SerializedName("webScan")
    @Expose
    private Property.FeatureEnabled webScan;

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled geteSCL() {
        return eSCL;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void seteSCL(Property.FeatureEnabled eSCL) {
        this.eSCL = eSCL;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled geteSCLSecure() {
        return eSCLSecure;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void seteSCLSecure(Property.FeatureEnabled eSCLSecure) {
        this.eSCLSecure = eSCLSecure;
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
    public Property.FeatureEnabled getWebScan() {
        return webScan;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setWebScan(Property.FeatureEnabled webScan) {
        this.webScan = webScan;
    }

}
