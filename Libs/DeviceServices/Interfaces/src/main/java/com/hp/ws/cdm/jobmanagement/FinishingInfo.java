
package com.hp.ws.cdm.jobmanagement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;


/**
 * provides the finishingInfo job specific details
 * 
 */
public class FinishingInfo {

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("fold")
    @Expose
    private Property.FeatureEnabled fold;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("holePunch")
    @Expose
    private Property.FeatureEnabled holePunch;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("staple")
    @Expose
    private Property.FeatureEnabled staple;

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getFold() {
        return fold;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setFold(Property.FeatureEnabled fold) {
        this.fold = fold;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getHolePunch() {
        return holePunch;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setHolePunch(Property.FeatureEnabled holePunch) {
        this.holePunch = holePunch;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getStaple() {
        return staple;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setStaple(Property.FeatureEnabled staple) {
        this.staple = staple;
    }

}
