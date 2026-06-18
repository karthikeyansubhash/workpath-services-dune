
package com.hp.ws.cdm.pubsub;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * meta data for collection item notification
 * 
 */
public class CollectionMetaData {

    /**
     * ordinal index value of collection item
     * 
     */
    @SerializedName("ordinalIndex")
    @Expose
    private Integer ordinalIndex;
    /**
     * zero based offset of collection item in this notification report
     * 
     */
    @SerializedName("offset")
    @Expose
    private Integer offset;
    /**
     * number of elements in this notification report
     * 
     */
    @SerializedName("limit")
    @Expose
    private Integer limit;
    /**
     * total elements in the collection
     * 
     */
    @SerializedName("totalElements")
    @Expose
    private Integer totalElements;

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

    /**
     * zero based offset of collection item in this notification report
     * 
     */
    public Integer getOffset() {
        return offset;
    }

    /**
     * zero based offset of collection item in this notification report
     * 
     */
    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    /**
     * number of elements in this notification report
     * 
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * number of elements in this notification report
     * 
     */
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    /**
     * total elements in the collection
     * 
     */
    public Integer getTotalElements() {
        return totalElements;
    }

    /**
     * total elements in the collection
     * 
     */
    public void setTotalElements(Integer totalElements) {
        this.totalElements = totalElements;
    }

}
