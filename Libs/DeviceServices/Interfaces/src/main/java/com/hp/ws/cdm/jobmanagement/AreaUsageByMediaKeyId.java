
package com.hp.ws.cdm.jobmanagement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AreaUsageByMediaKeyId {

    /**
     * Identifier of the specific media type
     * 
     */
    @SerializedName("mediaKeyId")
    @Expose
    private String mediaKeyId;
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
    /**
     * Media profiles are categorized among these classes for PPU (pay per use) kind of solutions
     * 
     */
    @SerializedName("mediaCategoryClass")
    @Expose
    private com.hp.ws.cdm.jobmanagement.MediaRequested.MediaCategoryClass mediaCategoryClass;
    /**
     * identifier of the predefined media type used to create the media type (for custom media types)
     * 
     */
    @SerializedName("parentMediaId")
    @Expose
    private String parentMediaId;
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
     * counter object with count and unit string enum
     * 
     */
    @SerializedName("thickness")
    @Expose
    private Counter thickness;
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

    /**
     * Identifier of the specific media type
     * 
     */
    public String getMediaKeyId() {
        return mediaKeyId;
    }

    /**
     * Identifier of the specific media type
     * 
     */
    public void setMediaKeyId(String mediaKeyId) {
        this.mediaKeyId = mediaKeyId;
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

    /**
     * Media profiles are categorized among these classes for PPU (pay per use) kind of solutions
     * 
     */
    public com.hp.ws.cdm.jobmanagement.MediaRequested.MediaCategoryClass getMediaCategoryClass() {
        return mediaCategoryClass;
    }

    /**
     * Media profiles are categorized among these classes for PPU (pay per use) kind of solutions
     * 
     */
    public void setMediaCategoryClass(com.hp.ws.cdm.jobmanagement.MediaRequested.MediaCategoryClass mediaCategoryClass) {
        this.mediaCategoryClass = mediaCategoryClass;
    }

    /**
     * identifier of the predefined media type used to create the media type (for custom media types)
     * 
     */
    public String getParentMediaId() {
        return parentMediaId;
    }

    /**
     * identifier of the predefined media type used to create the media type (for custom media types)
     * 
     */
    public void setParentMediaId(String parentMediaId) {
        this.parentMediaId = parentMediaId;
    }

    public String getParentMediaName() {
        return parentMediaName;
    }

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
