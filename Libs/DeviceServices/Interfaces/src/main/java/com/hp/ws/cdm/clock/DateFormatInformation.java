
package com.hp.ws.cdm.clock;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DateFormatInformation {

    @SerializedName("dateFormat")
    @Expose
    private String dateFormat;

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

}
