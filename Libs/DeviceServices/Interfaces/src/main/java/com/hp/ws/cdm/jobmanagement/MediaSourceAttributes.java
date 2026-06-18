
package com.hp.ws.cdm.jobmanagement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MediaSourceAttributes {

    /**
     * Identifier of the specific media type
     * 
     */
    @SerializedName("mediaKeyId")
    @Expose
    private String mediaKeyId;
    /**
     * a unique ID used by this printer to identify this input sub-unit from PWG 5100.13
     * 
     */
    @SerializedName("mediaInputId")
    @Expose
    private com.hp.ws.cdm.jobmanagement.MediaInput.MediaSourceId mediaInputId;
    /**
     * Media size, values according to the standard PWG5101.1 or HP extension pattern for exceptions
     * 
     */
    @SerializedName("mediaSize")
    @Expose
    private com.hp.ws.cdm.jobmanagement.MediaInput.MediaSize mediaSize;
    /**
     * the width of the media source - units are mm
     * 
     */
    @SerializedName("width")
    @Expose
    private Integer width;

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
     * Media size, values according to the standard PWG5101.1 or HP extension pattern for exceptions
     * 
     */
    public com.hp.ws.cdm.jobmanagement.MediaInput.MediaSize getMediaSize() {
        return mediaSize;
    }

    /**
     * Media size, values according to the standard PWG5101.1 or HP extension pattern for exceptions
     * 
     */
    public void setMediaSize(com.hp.ws.cdm.jobmanagement.MediaInput.MediaSize mediaSize) {
        this.mediaSize = mediaSize;
    }

    /**
     * the width of the media source - units are mm
     * 
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * the width of the media source - units are mm
     * 
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

}
