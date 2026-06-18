
package com.hp.ws.cdm.system;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Born-on Date of the device. Both serviceId and installationDate fields represent the same data. It is required to set ONLY one of them, setting both is not allowed, and can be set ONLY ONCE.
 * 
 */
public class InstallationDate {

    /**
     * day
     * 
     */
    @SerializedName("day")
    @Expose
    private Integer day;
    /**
     * month
     * 
     */
    @SerializedName("month")
    @Expose
    private Integer month;
    /**
     * Year
     * 
     */
    @SerializedName("year")
    @Expose
    private Integer year;

    /**
     * day
     * 
     */
    public Integer getDay() {
        return day;
    }

    /**
     * day
     * 
     */
    public void setDay(Integer day) {
        this.day = day;
    }

    /**
     * month
     * 
     */
    public Integer getMonth() {
        return month;
    }

    /**
     * month
     * 
     */
    public void setMonth(Integer month) {
        this.month = month;
    }

    /**
     * Year
     * 
     */
    public Integer getYear() {
        return year;
    }

    /**
     * Year
     * 
     */
    public void setYear(Integer year) {
        this.year = year;
    }

}
