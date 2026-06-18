
package com.hp.ws.cdm.jobmanagement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UsageByMediaSourceAttribute {

    @SerializedName("mediaSourceAttributes")
    @Expose
    private MediaSourceAttributes mediaSourceAttributes;
    /**
     * a unique ID used by this printer to identify this output sub-unit from PWG 5100.2-2001
     * 
     */
    @SerializedName("mediaOutputId")
    @Expose
    private com.hp.ws.cdm.jobmanagement.PrintSettings.MediaDestinationId mediaOutputId;
    /**
     * Media type,  values according to the standard PWG5101.1
     * 
     */
    @SerializedName("mediaTypeId")
    @Expose
    private com.hp.ws.cdm.jobmanagement.MediaInput.MediaType mediaTypeId;
    /**
     * Media Name used in this source. Normally one.
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
    /**
     * parent media id of the last media used in the source
     * 
     */
    @SerializedName("parentMediaId")
    @Expose
    private String parentMediaId;
    /**
     * Parent Media Name of the last media used in the source
     * 
     */
    @SerializedName("parentMediaName")
    @Expose
    private String parentMediaName;
    /**
     * Media profiles are categorized by families. UI groups media profiles by family
     * 
     */
    @SerializedName("parentMediaFamily")
    @Expose
    private com.hp.ws.cdm.jobmanagement.MediaRequested.MediaFamily parentMediaFamily;
    /**
     * print quality value used in the Job
     * 
     */
    @SerializedName("printQuality")
    @Expose
    private com.hp.ws.cdm.jobmanagement.PrintSettings.PrintQuality printQuality;
    /**
     * Last printModeName used in this source (i.e. 32p_6c_W_UF110)
     * 
     */
    @SerializedName("printModeName")
    @Expose
    private String printModeName;
    /**
     * counter object with count and unit string enum
     * 
     */
    @SerializedName("thickness")
    @Expose
    private Counter thickness;
    /**
     * total of aggregated impressions on this type of source during the job
     * 
     */
    @SerializedName("impressionCount")
    @Expose
    private Integer impressionCount;
    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    @SerializedName("usedArea")
    @Expose
    private LargeCounter usedArea;
    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    @SerializedName("imagedArea")
    @Expose
    private LargeCounter imagedArea;
    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    @SerializedName("printedArea")
    @Expose
    private LargeCounter printedArea;
    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    @SerializedName("usedLength")
    @Expose
    private LargeCounter usedLength;

    public MediaSourceAttributes getMediaSourceAttributes() {
        return mediaSourceAttributes;
    }

    public void setMediaSourceAttributes(MediaSourceAttributes mediaSourceAttributes) {
        this.mediaSourceAttributes = mediaSourceAttributes;
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
     * Media Name used in this source. Normally one.
     * 
     */
    public String getMediaName() {
        return mediaName;
    }

    /**
     * Media Name used in this source. Normally one.
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

    /**
     * parent media id of the last media used in the source
     * 
     */
    public String getParentMediaId() {
        return parentMediaId;
    }

    /**
     * parent media id of the last media used in the source
     * 
     */
    public void setParentMediaId(String parentMediaId) {
        this.parentMediaId = parentMediaId;
    }

    /**
     * Parent Media Name of the last media used in the source
     * 
     */
    public String getParentMediaName() {
        return parentMediaName;
    }

    /**
     * Parent Media Name of the last media used in the source
     * 
     */
    public void setParentMediaName(String parentMediaName) {
        this.parentMediaName = parentMediaName;
    }

    /**
     * Media profiles are categorized by families. UI groups media profiles by family
     * 
     */
    public com.hp.ws.cdm.jobmanagement.MediaRequested.MediaFamily getParentMediaFamily() {
        return parentMediaFamily;
    }

    /**
     * Media profiles are categorized by families. UI groups media profiles by family
     * 
     */
    public void setParentMediaFamily(com.hp.ws.cdm.jobmanagement.MediaRequested.MediaFamily parentMediaFamily) {
        this.parentMediaFamily = parentMediaFamily;
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
     * Last printModeName used in this source (i.e. 32p_6c_W_UF110)
     * 
     */
    public String getPrintModeName() {
        return printModeName;
    }

    /**
     * Last printModeName used in this source (i.e. 32p_6c_W_UF110)
     * 
     */
    public void setPrintModeName(String printModeName) {
        this.printModeName = printModeName;
    }

    /**
     * counter object with count and unit string enum
     * 
     */
    public Counter getThickness() {
        return thickness;
    }

    /**
     * counter object with count and unit string enum
     * 
     */
    public void setThickness(Counter thickness) {
        this.thickness = thickness;
    }

    /**
     * total of aggregated impressions on this type of source during the job
     * 
     */
    public Integer getImpressionCount() {
        return impressionCount;
    }

    /**
     * total of aggregated impressions on this type of source during the job
     * 
     */
    public void setImpressionCount(Integer impressionCount) {
        this.impressionCount = impressionCount;
    }

    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    public LargeCounter getUsedArea() {
        return usedArea;
    }

    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    public void setUsedArea(LargeCounter usedArea) {
        this.usedArea = usedArea;
    }

    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    public LargeCounter getImagedArea() {
        return imagedArea;
    }

    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    public void setImagedArea(LargeCounter imagedArea) {
        this.imagedArea = imagedArea;
    }

    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    public LargeCounter getPrintedArea() {
        return printedArea;
    }

    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    public void setPrintedArea(LargeCounter printedArea) {
        this.printedArea = printedArea;
    }

    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    public LargeCounter getUsedLength() {
        return usedLength;
    }

    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    public void setUsedLength(LargeCounter usedLength) {
        this.usedLength = usedLength;
    }

}
