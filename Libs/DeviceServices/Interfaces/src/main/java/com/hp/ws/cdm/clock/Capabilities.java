
package com.hp.ws.cdm.clock;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Capabilities {

    @SerializedName("timeFormatInformation")
    @Expose
    private List<TimeFormatInformation> timeFormatInformation = new ArrayList<TimeFormatInformation>();
    @SerializedName("dateFormatInformation")
    @Expose
    private List<DateFormatInformation> dateFormatInformation = new ArrayList<DateFormatInformation>();
    @SerializedName("systemTimeSyncInformation")
    @Expose
    private List<SystemTimeSyncInformation> systemTimeSyncInformation = new ArrayList<SystemTimeSyncInformation>();

    public List<TimeFormatInformation> getTimeFormatInformation() {
        return timeFormatInformation;
    }

    public void setTimeFormatInformation(List<TimeFormatInformation> timeFormatInformation) {
        this.timeFormatInformation = timeFormatInformation;
    }

    public List<DateFormatInformation> getDateFormatInformation() {
        return dateFormatInformation;
    }

    public void setDateFormatInformation(List<DateFormatInformation> dateFormatInformation) {
        this.dateFormatInformation = dateFormatInformation;
    }

    public List<SystemTimeSyncInformation> getSystemTimeSyncInformation() {
        return systemTimeSyncInformation;
    }

    public void setSystemTimeSyncInformation(List<SystemTimeSyncInformation> systemTimeSyncInformation) {
        this.systemTimeSyncInformation = systemTimeSyncInformation;
    }

}
