
package com.hp.ws.cdm.jobmanagement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FaxInInfo {

    /**
     * stationId is deprecated, do not implement
     * 
     */
    @SerializedName("stationId")
    @Expose
    private String stationId;
    /**
     * totalReceivedImages is deprecated, do not implement
     * 
     */
    @SerializedName("totalReceivedImages")
    @Expose
    private Integer totalReceivedImages;
    @SerializedName("faxCall")
    @Expose
    private FaxCall faxCall;

    /**
     * stationId is deprecated, do not implement
     * 
     */
    public String getStationId() {
        return stationId;
    }

    /**
     * stationId is deprecated, do not implement
     * 
     */
    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    /**
     * totalReceivedImages is deprecated, do not implement
     * 
     */
    public Integer getTotalReceivedImages() {
        return totalReceivedImages;
    }

    /**
     * totalReceivedImages is deprecated, do not implement
     * 
     */
    public void setTotalReceivedImages(Integer totalReceivedImages) {
        this.totalReceivedImages = totalReceivedImages;
    }

    public FaxCall getFaxCall() {
        return faxCall;
    }

    public void setFaxCall(FaxCall faxCall) {
        this.faxCall = faxCall;
    }

}
