
package com.hp.ws.cdm.jobmanagement;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class Pages {

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
     * 
     * (Required)
     * 
     */
    @SerializedName("pages")
    @Expose
    private List<Page> pages = new ArrayList<Page>();
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("severalOriginalSizes")
    @Expose
    private Property.FeatureEnabled severalOriginalSizes;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("severalOutputSizes")
    @Expose
    private Property.FeatureEnabled severalOutputSizes;

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

    /**
     * 
     * (Required)
     * 
     */
    public List<Page> getPages() {
        return pages;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getSeveralOriginalSizes() {
        return severalOriginalSizes;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setSeveralOriginalSizes(Property.FeatureEnabled severalOriginalSizes) {
        this.severalOriginalSizes = severalOriginalSizes;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getSeveralOutputSizes() {
        return severalOutputSizes;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setSeveralOutputSizes(Property.FeatureEnabled severalOutputSizes) {
        this.severalOutputSizes = severalOutputSizes;
    }

}
