
package com.hp.ws.cdm.jobticket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class PrintCapabilities {

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("collateSupported")
    @Expose
    private Property.FeatureEnabled collateSupported;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("colorModeSupported")
    @Expose
    private Property.FeatureEnabled colorModeSupported;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("plexModeSupported")
    @Expose
    private Property.FeatureEnabled plexModeSupported;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("printQualitySupported")
    @Expose
    private Property.FeatureEnabled printQualitySupported;

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getCollateSupported() {
        return collateSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setCollateSupported(Property.FeatureEnabled collateSupported) {
        this.collateSupported = collateSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getColorModeSupported() {
        return colorModeSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setColorModeSupported(Property.FeatureEnabled colorModeSupported) {
        this.colorModeSupported = colorModeSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getPlexModeSupported() {
        return plexModeSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setPlexModeSupported(Property.FeatureEnabled plexModeSupported) {
        this.plexModeSupported = plexModeSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getPrintQualitySupported() {
        return printQualitySupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setPrintQualitySupported(Property.FeatureEnabled printQualitySupported) {
        this.printQualitySupported = printQualitySupported;
    }

}
