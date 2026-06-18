
package com.hp.ws.cdm.clock;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeFormatInformation {

    @SerializedName("timeFormat")
    @Expose
    private String timeFormat;

    public String getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
    }

}
