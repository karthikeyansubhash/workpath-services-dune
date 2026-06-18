
package com.hp.ws.cdm.jobticket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class FolderCapabilities {

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("personalFolderSettingsSupported")
    @Expose
    private Property.FeatureEnabled personalFolderSettingsSupported;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("folderDestinationFtpSettingsSupported")
    @Expose
    private Property.FeatureEnabled folderDestinationFtpSettingsSupported;

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getPersonalFolderSettingsSupported() {
        return personalFolderSettingsSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setPersonalFolderSettingsSupported(Property.FeatureEnabled personalFolderSettingsSupported) {
        this.personalFolderSettingsSupported = personalFolderSettingsSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getFolderDestinationFtpSettingsSupported() {
        return folderDestinationFtpSettingsSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setFolderDestinationFtpSettingsSupported(Property.FeatureEnabled folderDestinationFtpSettingsSupported) {
        this.folderDestinationFtpSettingsSupported = folderDestinationFtpSettingsSupported;
    }

}
