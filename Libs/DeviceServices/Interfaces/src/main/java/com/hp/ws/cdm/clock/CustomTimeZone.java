
package com.hp.ws.cdm.clock;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Time localization settings (timezone & dst behavior)
 * 
 */
public class CustomTimeZone {

    @SerializedName("timeZoneOffsetHours")
    @Expose
    private Integer timeZoneOffsetHours;
    @SerializedName("timeZoneOffsetMinutes")
    @Expose
    private Integer timeZoneOffsetMinutes;
    @SerializedName("dstOffsetInMinutes")
    @Expose
    private Integer dstOffsetInMinutes;
    @SerializedName("dstStartTime")
    @Expose
    private DstTime dstStartTime;
    @SerializedName("dstEndTime")
    @Expose
    private DstTime dstEndTime;

    public Integer getTimeZoneOffsetHours() {
        return timeZoneOffsetHours;
    }

    public void setTimeZoneOffsetHours(Integer timeZoneOffsetHours) {
        this.timeZoneOffsetHours = timeZoneOffsetHours;
    }

    public Integer getTimeZoneOffsetMinutes() {
        return timeZoneOffsetMinutes;
    }

    public void setTimeZoneOffsetMinutes(Integer timeZoneOffsetMinutes) {
        this.timeZoneOffsetMinutes = timeZoneOffsetMinutes;
    }

    public Integer getDstOffsetInMinutes() {
        return dstOffsetInMinutes;
    }

    public void setDstOffsetInMinutes(Integer dstOffsetInMinutes) {
        this.dstOffsetInMinutes = dstOffsetInMinutes;
    }

    public DstTime getDstStartTime() {
        return dstStartTime;
    }

    public void setDstStartTime(DstTime dstStartTime) {
        this.dstStartTime = dstStartTime;
    }

    public DstTime getDstEndTime() {
        return dstEndTime;
    }

    public void setDstEndTime(DstTime dstEndTime) {
        this.dstEndTime = dstEndTime;
    }

}
