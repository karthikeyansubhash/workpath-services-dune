
package com.hp.ws.cdm.jobmanagement;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Page {

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    /**
     * unique identifier for the page
     * 
     */
    @SerializedName("pageId")
    @Expose
    private String pageId;
    /**
     * Position of the page in a multi-page job
     * 
     */
    @SerializedName("pageNumber")
    @Expose
    private Integer pageNumber;
    /**
     * number of copies requested for the page
     * 
     */
    @SerializedName("requestedCopiesCount")
    @Expose
    private Integer requestedCopiesCount;
    /**
     * number of copies completed for the page
     * 
     */
    @SerializedName("completedCopiesCount")
    @Expose
    private Integer completedCopiesCount;
    /**
     * Dimensions of the page in the original document.
     * 
     */
    @SerializedName("originalSize")
    @Expose
    private OriginalSize originalSize;
    /**
     * Dimensions of the page in the output.
     * 
     */
    @SerializedName("outputSize")
    @Expose
    private OutputSize outputSize;
    /**
     * Margins of the page in the output in mm.
     * 
     */
    @SerializedName("pageMargins")
    @Expose
    private PageMargins pageMargins;
    /**
     * Percentage [0-100] indicating how much of the preview and thumbnail image has been generated.
     * 
     */
    @SerializedName("previewProgress")
    @Expose
    private Integer previewProgress;
    @SerializedName("highResImage")
    @Expose
    private ImageDescriptor highResImage;
    @SerializedName("lowResImage")
    @Expose
    private ImageDescriptor lowResImage;
    /**
     * Field will be present for products that support layers.  This property will not be present for products that do not support layers.
     * 
     */
    @SerializedName("layers")
    @Expose
    private List<Layer> layers = new ArrayList<Layer>();

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

    /**
     * unique identifier for the page
     * 
     */
    public String getPageId() {
        return pageId;
    }

    /**
     * unique identifier for the page
     * 
     */
    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    /**
     * Position of the page in a multi-page job
     * 
     */
    public Integer getPageNumber() {
        return pageNumber;
    }

    /**
     * Position of the page in a multi-page job
     * 
     */
    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    /**
     * number of copies requested for the page
     * 
     */
    public Integer getRequestedCopiesCount() {
        return requestedCopiesCount;
    }

    /**
     * number of copies requested for the page
     * 
     */
    public void setRequestedCopiesCount(Integer requestedCopiesCount) {
        this.requestedCopiesCount = requestedCopiesCount;
    }

    /**
     * number of copies completed for the page
     * 
     */
    public Integer getCompletedCopiesCount() {
        return completedCopiesCount;
    }

    /**
     * number of copies completed for the page
     * 
     */
    public void setCompletedCopiesCount(Integer completedCopiesCount) {
        this.completedCopiesCount = completedCopiesCount;
    }

    /**
     * Dimensions of the page in the original document.
     * 
     */
    public OriginalSize getOriginalSize() {
        return originalSize;
    }

    /**
     * Dimensions of the page in the original document.
     * 
     */
    public void setOriginalSize(OriginalSize originalSize) {
        this.originalSize = originalSize;
    }

    /**
     * Dimensions of the page in the output.
     * 
     */
    public OutputSize getOutputSize() {
        return outputSize;
    }

    /**
     * Dimensions of the page in the output.
     * 
     */
    public void setOutputSize(OutputSize outputSize) {
        this.outputSize = outputSize;
    }

    /**
     * Margins of the page in the output in mm.
     * 
     */
    public PageMargins getPageMargins() {
        return pageMargins;
    }

    /**
     * Margins of the page in the output in mm.
     * 
     */
    public void setPageMargins(PageMargins pageMargins) {
        this.pageMargins = pageMargins;
    }

    /**
     * Percentage [0-100] indicating how much of the preview and thumbnail image has been generated.
     * 
     */
    public Integer getPreviewProgress() {
        return previewProgress;
    }

    /**
     * Percentage [0-100] indicating how much of the preview and thumbnail image has been generated.
     * 
     */
    public void setPreviewProgress(Integer previewProgress) {
        this.previewProgress = previewProgress;
    }

    public ImageDescriptor getHighResImage() {
        return highResImage;
    }

    public void setHighResImage(ImageDescriptor highResImage) {
        this.highResImage = highResImage;
    }

    public ImageDescriptor getLowResImage() {
        return lowResImage;
    }

    public void setLowResImage(ImageDescriptor lowResImage) {
        this.lowResImage = lowResImage;
    }

    /**
     * Field will be present for products that support layers.  This property will not be present for products that do not support layers.
     * 
     */
    public List<Layer> getLayers() {
        return layers;
    }

    /**
     * Field will be present for products that support layers.  This property will not be present for products that do not support layers.
     * 
     */
    public void setLayers(List<Layer> layers) {
        this.layers = layers;
    }

}
