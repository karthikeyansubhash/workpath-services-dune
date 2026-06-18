
package com.hp.ws.cdm.jobmanagement;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FaxOutInfo {

    @SerializedName("faxCalls")
    @Expose
    private List<FaxCall> faxCalls = new ArrayList<FaxCall>();
    /**
     * number of total pages to be sent
     * 
     */
    @SerializedName("requestedImageCount")
    @Expose
    private Integer requestedImageCount;

    public List<FaxCall> getFaxCalls() {
        return faxCalls;
    }

    public void setFaxCalls(List<FaxCall> faxCalls) {
        this.faxCalls = faxCalls;
    }

    /**
     * number of total pages to be sent
     * 
     */
    public Integer getRequestedImageCount() {
        return requestedImageCount;
    }

    /**
     * number of total pages to be sent
     * 
     */
    public void setRequestedImageCount(Integer requestedImageCount) {
        this.requestedImageCount = requestedImageCount;
    }

}
