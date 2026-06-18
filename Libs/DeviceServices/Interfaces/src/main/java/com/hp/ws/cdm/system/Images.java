
package com.hp.ws.cdm.system;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * resource to retrieve the device's images
 * 
 */
public class Images {

    /**
     * A list of printer images
     * (Required)
     * 
     */
    @SerializedName("images")
    @Expose
    private List<Image> images = new ArrayList<Image>();
    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * (Required)
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;

    /**
     * A list of printer images
     * (Required)
     * 
     */
    public List<Image> getImages() {
        return images;
    }

    /**
     * A list of printer images
     * (Required)
     * 
     */
    public void setImages(List<Image> images) {
        this.images = images;
    }

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * (Required)
     * 
     */
    public String getVersion() {
        return version;
    }

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * (Required)
     * 
     */
    public void setVersion(String version) {
        this.version = version;
    }

}
