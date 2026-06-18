
package com.hp.ws.cdm.jobmanagement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Margins of the page in the output in mm.
 * 
 */
public class PageMargins {

    /**
     * Top margin.
     * 
     */
    @SerializedName("topMargin")
    @Expose
    private Integer topMargin;
    /**
     * Bottom margin.
     * 
     */
    @SerializedName("bottomMargin")
    @Expose
    private Integer bottomMargin;
    /**
     * Left margin.
     * 
     */
    @SerializedName("leftMargin")
    @Expose
    private Integer leftMargin;
    /**
     * Right margin.
     * 
     */
    @SerializedName("rightMargin")
    @Expose
    private Integer rightMargin;

    /**
     * Top margin.
     * 
     */
    public Integer getTopMargin() {
        return topMargin;
    }

    /**
     * Top margin.
     * 
     */
    public void setTopMargin(Integer topMargin) {
        this.topMargin = topMargin;
    }

    /**
     * Bottom margin.
     * 
     */
    public Integer getBottomMargin() {
        return bottomMargin;
    }

    /**
     * Bottom margin.
     * 
     */
    public void setBottomMargin(Integer bottomMargin) {
        this.bottomMargin = bottomMargin;
    }

    /**
     * Left margin.
     * 
     */
    public Integer getLeftMargin() {
        return leftMargin;
    }

    /**
     * Left margin.
     * 
     */
    public void setLeftMargin(Integer leftMargin) {
        this.leftMargin = leftMargin;
    }

    /**
     * Right margin.
     * 
     */
    public Integer getRightMargin() {
        return rightMargin;
    }

    /**
     * Right margin.
     * 
     */
    public void setRightMargin(Integer rightMargin) {
        this.rightMargin = rightMargin;
    }

}
