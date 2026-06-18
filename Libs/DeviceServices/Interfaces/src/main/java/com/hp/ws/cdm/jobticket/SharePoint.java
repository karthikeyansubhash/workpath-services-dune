
package com.hp.ws.cdm.jobticket;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class SharePoint {

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("destinationList")
    @Expose
    private List<SharePointDestination> destinationList = new ArrayList<SharePointDestination>();
    @SerializedName("pin")
    @Expose
    private String pin;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("isPinSet")
    @Expose
    private Property.FeatureEnabled isPinSet;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("verifyAccessPriorToJobStart")
    @Expose
    private Property.FeatureEnabled verifyAccessPriorToJobStart;

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    public String getVersion() {
        return version;
    }

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    public void setVersion(String version) {
        this.version = version;
    }

    public List<SharePointDestination> getDestinationList() {
        return destinationList;
    }

    public void setDestinationList(List<SharePointDestination> destinationList) {
        this.destinationList = destinationList;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getIsPinSet() {
        return isPinSet;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setIsPinSet(Property.FeatureEnabled isPinSet) {
        this.isPinSet = isPinSet;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getVerifyAccessPriorToJobStart() {
        return verifyAccessPriorToJobStart;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setVerifyAccessPriorToJobStart(Property.FeatureEnabled verifyAccessPriorToJobStart) {
        this.verifyAccessPriorToJobStart = verifyAccessPriorToJobStart;
    }

}
