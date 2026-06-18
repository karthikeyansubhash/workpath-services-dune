
package com.hp.ws.cdm.jobmanagement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * counter object with int64 maximum and unit string enum
 * 
 */
public class LargeCounter {

    @SerializedName("count")
    @Expose
    private Integer count;
    /**
     * Enum of possible unit types
     * 
     */
    @SerializedName("unit")
    @Expose
    private com.hp.ws.cdm.jobmanagement.Counter.CounterUnit unit;

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
    public com.hp.ws.cdm.jobmanagement.Counter.CounterUnit getUnit() {
        return unit;
    }

    /**
     * Enum of possible unit types
     * 
     */
    public void setUnit(com.hp.ws.cdm.jobmanagement.Counter.CounterUnit unit) {
        this.unit = unit;
    }

}
