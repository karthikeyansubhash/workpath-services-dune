
package com.hp.ws.cdm.clock;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DstTime {

    @SerializedName("dayCount")
    @Expose
    private Integer dayCount;
    @SerializedName("dayOfWeek")
    @Expose
    private DstTime.DayOfWeek dayOfWeek;
    @SerializedName("month")
    @Expose
    private Integer month;
    @SerializedName("hour")
    @Expose
    private Integer hour;

    public Integer getDayCount() {
        return dayCount;
    }

    public void setDayCount(Integer dayCount) {
        this.dayCount = dayCount;
    }

    public DstTime.DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DstTime.DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public enum DayOfWeek {

        @SerializedName("sunday")
        SUNDAY("sunday"),
        @SerializedName("monday")
        MONDAY("monday"),
        @SerializedName("tuesday")
        TUESDAY("tuesday"),
        @SerializedName("wednesday")
        WEDNESDAY("wednesday"),
        @SerializedName("thursday")
        THURSDAY("thursday"),
        @SerializedName("friday")
        FRIDAY("friday"),
        @SerializedName("saturday")
        SATURDAY("saturday");
        private final String value;
        private final static Map<String, DstTime.DayOfWeek> CONSTANTS = new HashMap<String, DstTime.DayOfWeek>();

        static {
            for (DstTime.DayOfWeek c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        DayOfWeek(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static DstTime.DayOfWeek fromValue(String value) {
            DstTime.DayOfWeek constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
