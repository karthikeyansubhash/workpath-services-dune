
package com.hp.ws.cdm.jobmanagement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Dimensions of the page in the original document.
 * 
 */
public class OriginalSize {

    /**
     * Media size, values according to the standard PWG5101.1 or HP extension pattern for exceptions
     * 
     */
    @SerializedName("mediaSize")
    @Expose
    private com.hp.ws.cdm.jobmanagement.MediaInput.MediaSize mediaSize;
    /**
     * Width of the page of the original document in mm.
     * 
     */
    @SerializedName("width")
    @Expose
    private Integer width;
    /**
     * Length of the page of the original document in mm.
     * 
     */
    @SerializedName("length")
    @Expose
    private Integer length;

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
     * Width of the page of the original document in mm.
     * 
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * Width of the page of the original document in mm.
     * 
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     * Length of the page of the original document in mm.
     * 
     */
    public Integer getLength() {
        return length;
    }

    /**
     * Length of the page of the original document in mm.
     * 
     */
    public void setLength(Integer length) {
        this.length = length;
    }

}
