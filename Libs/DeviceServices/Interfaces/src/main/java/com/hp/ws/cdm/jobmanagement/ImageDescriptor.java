
package com.hp.ws.cdm.jobmanagement;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;

public class ImageDescriptor {

    /**
     * Height of the image (in pixels)
     * (Required)
     * 
     */
    @SerializedName("height")
    @Expose
    private Integer height;
    /**
     * Width of the image (in pixels)
     * (Required)
     * 
     */
    @SerializedName("width")
    @Expose
    private Integer width;
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();

    /**
     * Height of the image (in pixels)
     * (Required)
     * 
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * Height of the image (in pixels)
     * (Required)
     * 
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
     * Width of the image (in pixels)
     * (Required)
     * 
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * Width of the image (in pixels)
     * (Required)
     * 
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

}
