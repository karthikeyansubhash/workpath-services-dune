
package com.hp.ws.cdm.jobmanagement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class M {

    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    @SerializedName("supplyMarkingAgent")
    @Expose
    private LargeCounter supplyMarkingAgent;

    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    public LargeCounter getSupplyMarkingAgent() {
        return supplyMarkingAgent;
    }

    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    public void setSupplyMarkingAgent(LargeCounter supplyMarkingAgent) {
        this.supplyMarkingAgent = supplyMarkingAgent;
    }

}
