
package com.hp.ws.cdm.jobmanagement;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * counter object with count and unit string enum
 * 
 */
public class Counter {

    @SerializedName("count")
    @Expose
    private Integer count;
    /**
     * Enum of possible unit types
     * 
     */
    @SerializedName("unit")
    @Expose
    private Counter.CounterUnit unit;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * Enum of possible unit types
     * 
     */
    public Counter.CounterUnit getUnit() {
        return unit;
    }

    /**
     * Enum of possible unit types
     * 
     */
    public void setUnit(Counter.CounterUnit unit) {
        this.unit = unit;
    }


    /**
     * Enum of possible unit types
     * 
     */
    public enum CounterUnit {

        @SerializedName("centimeters")
        CENTIMETERS("centimeters"),
        @SerializedName("cycles")
        CYCLES("cycles"),
        @SerializedName("dots")
        DOTS("dots"),
        @SerializedName("decimicrograms")
        DECIMICROGRAMS("decimicrograms"),
        @SerializedName("hundredImpressions")
        HUNDRED_IMPRESSIONS("hundredImpressions"),
        @SerializedName("hundredThousandsOfPixels")
        HUNDRED_THOUSANDS_OF_PIXELS("hundredThousandsOfPixels"),
        @SerializedName("impressions")
        IMPRESSIONS("impressions"),
        @SerializedName("microliters")
        MICROLITERS("microliters"),
        @SerializedName("microns")
        MICRONS("microns"),
        @SerializedName("milligrams")
        MILLIGRAMS("milligrams"),
        @SerializedName("milliliters")
        MILLILITERS("milliliters"),
        @SerializedName("millimeters")
        MILLIMETERS("millimeters"),
        @SerializedName("nanoliters")
        NANOLITERS("nanoliters"),
        @SerializedName("picoliters")
        PICOLITERS("picoliters"),
        @SerializedName("pixels")
        PIXELS("pixels"),
        @SerializedName("pages")
        PAGES("pages"),
        @SerializedName("percent")
        PERCENT("percent"),
        @SerializedName("seconds")
        SECONDS("seconds"),
        @SerializedName("sqcm")
        SQCM("sqcm"),
        @SerializedName("thousandsOfDots")
        THOUSANDS_OF_DOTS("thousandsOfDots"),
        @SerializedName("micrograms")
        MICROGRAMS("micrograms");
        private final String value;
        private final static Map<String, Counter.CounterUnit> CONSTANTS = new HashMap<String, Counter.CounterUnit>();

        static {
            for (Counter.CounterUnit c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        CounterUnit(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Counter.CounterUnit fromValue(String value) {
            Counter.CounterUnit constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
