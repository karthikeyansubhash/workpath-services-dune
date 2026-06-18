
package com.hp.ws.cdm.commonglossary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Progress state for a flow / step, percent time remaining
 * 
 */
public class ProgressState {

    @SerializedName("percentRemaining")
    @Expose
    private Integer percentRemaining = -1;
    @SerializedName("timeRemaining")
    @Expose
    private Integer timeRemaining;

    public Integer getPercentRemaining() {
        return percentRemaining;
    }

    public void setPercentRemaining(Integer percentRemaining) {
        this.percentRemaining = percentRemaining;
    }

    public Integer getTimeRemaining() {
        return timeRemaining;
    }

    public void setTimeRemaining(Integer timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

}
