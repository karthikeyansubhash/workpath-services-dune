
package com.hp.ws.cdm.jobmanagement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SheetSet {

    @SerializedName("backImpressionClassification")
    @Expose
    private com.hp.ws.cdm.jobmanagement.PrintSettings.ColorModes backImpressionClassification;
    /**
     * The number of sheets matching this sheet set
     * 
     */
    @SerializedName("count")
    @Expose
    private Long count;
    @SerializedName("frontImpressionClassification")
    @Expose
    private com.hp.ws.cdm.jobmanagement.PrintSettings.ColorModes frontImpressionClassification;
    /**
     * a unique ID used by this printer to identify this input sub-unit from PWG 5100.13
     * 
     */
    @SerializedName("mediaInputId")
    @Expose
    private com.hp.ws.cdm.jobmanagement.MediaInput.MediaSourceId mediaInputId;
    /**
     * a unique ID used by this printer to identify this output sub-unit from PWG 5100.2-2001
     * 
     */
    @SerializedName("mediaOutputId")
    @Expose
    private com.hp.ws.cdm.jobmanagement.PrintSettings.MediaDestinationId mediaOutputId;
    /**
     * Media size, values according to the standard PWG5101.1 or HP extension pattern for exceptions
     * 
     */
    @SerializedName("mediaSizeId")
    @Expose
    private com.hp.ws.cdm.jobmanagement.MediaInput.MediaSize mediaSizeId;
    /**
     * Media type,  values according to the standard PWG5101.1
     * 
     */
    @SerializedName("mediaTypeId")
    @Expose
    private com.hp.ws.cdm.jobmanagement.MediaInput.MediaType mediaTypeId;
    /**
     * LF ID - media family (non-localized) - SBH - is this defined in another schema? - what are the format restrictions?
     * 
     */
    @SerializedName("mediaName")
    @Expose
    private String mediaName;
    /**
     * Media profiles are categorized by families. UI groups media profiles by family
     * 
     */
    @SerializedName("mediaFamily")
    @Expose
    private com.hp.ws.cdm.jobmanagement.MediaRequested.MediaFamily mediaFamily;
    @SerializedName("plexMode")
    @Expose
    private com.hp.ws.cdm.jobmanagement.PrintSettings.PlexMode plexMode;
    /**
     * print quality value used in the Job
     * 
     */
    @SerializedName("printQuality")
    @Expose
    private com.hp.ws.cdm.jobmanagement.PrintSettings.PrintQuality printQuality;
    /**
     * Display name for the printmode, i.e. 32p_6c_W_UF110
     * 
     */
    @SerializedName("printModeName")
    @Expose
    private String printModeName;

    public com.hp.ws.cdm.jobmanagement.PrintSettings.ColorModes getBackImpressionClassification() {
        return backImpressionClassification;
    }

    public void setBackImpressionClassification(com.hp.ws.cdm.jobmanagement.PrintSettings.ColorModes backImpressionClassification) {
        this.backImpressionClassification = backImpressionClassification;
    }

    /**
     * The number of sheets matching this sheet set
     * 
     */
    public Long getCount() {
        return count;
    }

    /**
     * The number of sheets matching this sheet set
     * 
     */
    public void setCount(Long count) {
        this.count = count;
    }

    public com.hp.ws.cdm.jobmanagement.PrintSettings.ColorModes getFrontImpressionClassification() {
        return frontImpressionClassification;
    }

    public void setFrontImpressionClassification(com.hp.ws.cdm.jobmanagement.PrintSettings.ColorModes frontImpressionClassification) {
        this.frontImpressionClassification = frontImpressionClassification;
    }

    /**
     * a unique ID used by this printer to identify this input sub-unit from PWG 5100.13
     * 
     */
    public com.hp.ws.cdm.jobmanagement.MediaInput.MediaSourceId getMediaInputId() {
        return mediaInputId;
    }

    /**
     * a unique ID used by this printer to identify this input sub-unit from PWG 5100.13
     * 
     */
    public void setMediaInputId(com.hp.ws.cdm.jobmanagement.MediaInput.MediaSourceId mediaInputId) {
        this.mediaInputId = mediaInputId;
    }

    /**
     * a unique ID used by this printer to identify this output sub-unit from PWG 5100.2-2001
     * 
     */
    public com.hp.ws.cdm.jobmanagement.PrintSettings.MediaDestinationId getMediaOutputId() {
        return mediaOutputId;
    }

    /**
     * a unique ID used by this printer to identify this output sub-unit from PWG 5100.2-2001
     * 
     */
    public void setMediaOutputId(com.hp.ws.cdm.jobmanagement.PrintSettings.MediaDestinationId mediaOutputId) {
        this.mediaOutputId = mediaOutputId;
    }

    /**
     * Media size, values according to the standard PWG5101.1 or HP extension pattern for exceptions
     * 
     */
    public com.hp.ws.cdm.jobmanagement.MediaInput.MediaSize getMediaSizeId() {
        return mediaSizeId;
    }

    /**
     * Media size, values according to the standard PWG5101.1 or HP extension pattern for exceptions
     * 
     */
    public void setMediaSizeId(com.hp.ws.cdm.jobmanagement.MediaInput.MediaSize mediaSizeId) {
        this.mediaSizeId = mediaSizeId;
    }

    /**
     * Media type,  values according to the standard PWG5101.1
     * 
     */
    public com.hp.ws.cdm.jobmanagement.MediaInput.MediaType getMediaTypeId() {
        return mediaTypeId;
    }

    /**
     * Media type,  values according to the standard PWG5101.1
     * 
     */
    public void setMediaTypeId(com.hp.ws.cdm.jobmanagement.MediaInput.MediaType mediaTypeId) {
        this.mediaTypeId = mediaTypeId;
    }

    /**
     * LF ID - media family (non-localized) - SBH - is this defined in another schema? - what are the format restrictions?
     * 
     */
    public String getMediaName() {
        return mediaName;
    }

    /**
     * LF ID - media family (non-localized) - SBH - is this defined in another schema? - what are the format restrictions?
     * 
     */
    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    /**
     * Media profiles are categorized by families. UI groups media profiles by family
     * 
     */
    public com.hp.ws.cdm.jobmanagement.MediaRequested.MediaFamily getMediaFamily() {
        return mediaFamily;
    }

    /**
     * Media profiles are categorized by families. UI groups media profiles by family
     * 
     */
    public void setMediaFamily(com.hp.ws.cdm.jobmanagement.MediaRequested.MediaFamily mediaFamily) {
        this.mediaFamily = mediaFamily;
    }

    public com.hp.ws.cdm.jobmanagement.PrintSettings.PlexMode getPlexMode() {
        return plexMode;
    }

    public void setPlexMode(com.hp.ws.cdm.jobmanagement.PrintSettings.PlexMode plexMode) {
        this.plexMode = plexMode;
    }

    /**
     * print quality value used in the Job
     * 
     */
    public com.hp.ws.cdm.jobmanagement.PrintSettings.PrintQuality getPrintQuality() {
        return printQuality;
    }

    /**
     * print quality value used in the Job
     * 
     */
    public void setPrintQuality(com.hp.ws.cdm.jobmanagement.PrintSettings.PrintQuality printQuality) {
        this.printQuality = printQuality;
    }

    /**
     * Display name for the printmode, i.e. 32p_6c_W_UF110
     * 
     */
    public String getPrintModeName() {
        return printModeName;
    }

    /**
     * Display name for the printmode, i.e. 32p_6c_W_UF110
     * 
     */
    public void setPrintModeName(String printModeName) {
        this.printModeName = printModeName;
    }

}
