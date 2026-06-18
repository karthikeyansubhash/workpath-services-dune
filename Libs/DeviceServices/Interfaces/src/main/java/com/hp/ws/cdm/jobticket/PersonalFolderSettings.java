
package com.hp.ws.cdm.jobticket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class PersonalFolderSettings {

    @SerializedName("attributeName")
    @Expose
    private String attributeName;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("createSubFolderBasedOnUserNameEnabled")
    @Expose
    private Property.FeatureEnabled createSubFolderBasedOnUserNameEnabled;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("restrictSubFolderAccessEnabled")
    @Expose
    private Property.FeatureEnabled restrictSubFolderAccessEnabled;

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getCreateSubFolderBasedOnUserNameEnabled() {
        return createSubFolderBasedOnUserNameEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setCreateSubFolderBasedOnUserNameEnabled(Property.FeatureEnabled createSubFolderBasedOnUserNameEnabled) {
        this.createSubFolderBasedOnUserNameEnabled = createSubFolderBasedOnUserNameEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getRestrictSubFolderAccessEnabled() {
        return restrictSubFolderAccessEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setRestrictSubFolderAccessEnabled(Property.FeatureEnabled restrictSubFolderAccessEnabled) {
        this.restrictSubFolderAccessEnabled = restrictSubFolderAccessEnabled;
    }

}
