
package com.hp.ws.cdm.pubsub;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * resource identifier for the item resource of a collection
 * 
 */
public class ResourceId {

    /**
     * ordinal index value of collection item
     * 
     */
    @SerializedName("ordinalIndex")
    @Expose
    private Integer ordinalIndex;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("value")
    @Expose
    private String value;

    /**
     * ordinal index value of collection item
     * 
     */
    public Integer getOrdinalIndex() {
        return ordinalIndex;
    }

    /**
     * ordinal index value of collection item
     * 
     */
    public void setOrdinalIndex(Integer ordinalIndex) {
        this.ordinalIndex = ordinalIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
