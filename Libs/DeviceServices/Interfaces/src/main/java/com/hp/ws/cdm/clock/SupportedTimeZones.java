
package com.hp.ws.cdm.clock;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SupportedTimeZones {

    @SerializedName("timeZoneInformation")
    @Expose
    private List<TimeZoneInformation> timeZoneInformation = new ArrayList<TimeZoneInformation>();

    public List<TimeZoneInformation> getTimeZoneInformation() {
        return timeZoneInformation;
    }

    public void setTimeZoneInformation(List<TimeZoneInformation> timeZoneInformation) {
        this.timeZoneInformation = timeZoneInformation;
    }

}
