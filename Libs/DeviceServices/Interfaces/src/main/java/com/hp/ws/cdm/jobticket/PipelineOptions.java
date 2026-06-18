
package com.hp.ws.cdm.jobticket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class PipelineOptions {

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("notification")
    @Expose
    private Notification notification;
    @SerializedName("imageModifications")
    @Expose
    private ImageModifications imageModifications;
    @SerializedName("manualUserOperations")
    @Expose
    private ManualUserOperations manualUserOperations;
    @SerializedName("scaling")
    @Expose
    private Scaling scaling;
    @SerializedName("sendFileAttributes")
    @Expose
    private SendFileAttributes sendFileAttributes;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("generatePreview")
    @Expose
    private Property.FeatureEnabled generatePreview;
    @SerializedName("layout")
    @Expose
    private Layout layout;

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

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public ImageModifications getImageModifications() {
        return imageModifications;
    }

    public void setImageModifications(ImageModifications imageModifications) {
        this.imageModifications = imageModifications;
    }

    public ManualUserOperations getManualUserOperations() {
        return manualUserOperations;
    }

    public void setManualUserOperations(ManualUserOperations manualUserOperations) {
        this.manualUserOperations = manualUserOperations;
    }

    public Scaling getScaling() {
        return scaling;
    }

    public void setScaling(Scaling scaling) {
        this.scaling = scaling;
    }

    public SendFileAttributes getSendFileAttributes() {
        return sendFileAttributes;
    }

    public void setSendFileAttributes(SendFileAttributes sendFileAttributes) {
        this.sendFileAttributes = sendFileAttributes;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getGeneratePreview() {
        return generatePreview;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setGeneratePreview(Property.FeatureEnabled generatePreview) {
        this.generatePreview = generatePreview;
    }

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

}
