
package com.hp.ws.cdm.pubsub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FilterInfo {

    /**
     * Each syncrhronization of filters with the end devices or local valve controllers are identified by a unique ID. This ID should be passed to the actual data telemetry for correlation purposes.
     * 
     */
    @SerializedName("transactionId")
    @Expose
    private String transactionId;
    /**
     * Filter identifier
     * 
     */
    @SerializedName("filterId")
    @Expose
    private Integer filterId;
    /**
     * filterType none is set when unknown value is received, it is reported in filter error response while applying the filter
     * 
     */
    @SerializedName("filterType")
    @Expose
    private FilterInfo.FilterType filterType = FilterInfo.FilterType.fromValue("none");
    /**
     * Resource identifier, gun/resource url/ ledm namespace
     * 
     */
    @SerializedName("resourceId")
    @Expose
    private String resourceId;
    /**
     * array of xpaths in case of ledm resourcetype, jsonpaths in case of cdm resourceType of the attributes
     * 
     */
    @SerializedName("attributes")
    @Expose
    private List<String> attributes = new ArrayList<String>();

    /**
     * Each syncrhronization of filters with the end devices or local valve controllers are identified by a unique ID. This ID should be passed to the actual data telemetry for correlation purposes.
     * 
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * Each syncrhronization of filters with the end devices or local valve controllers are identified by a unique ID. This ID should be passed to the actual data telemetry for correlation purposes.
     * 
     */
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * Filter identifier
     * 
     */
    public Integer getFilterId() {
        return filterId;
    }

    /**
     * Filter identifier
     * 
     */
    public void setFilterId(Integer filterId) {
        this.filterId = filterId;
    }

    /**
     * filterType none is set when unknown value is received, it is reported in filter error response while applying the filter
     * 
     */
    public FilterInfo.FilterType getFilterType() {
        return filterType;
    }

    /**
     * filterType none is set when unknown value is received, it is reported in filter error response while applying the filter
     * 
     */
    public void setFilterType(FilterInfo.FilterType filterType) {
        this.filterType = filterType;
    }

    /**
     * Resource identifier, gun/resource url/ ledm namespace
     * 
     */
    public String getResourceId() {
        return resourceId;
    }

    /**
     * Resource identifier, gun/resource url/ ledm namespace
     * 
     */
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * array of xpaths in case of ledm resourcetype, jsonpaths in case of cdm resourceType of the attributes
     * 
     */
    public List<String> getAttributes() {
        return attributes;
    }

    /**
     * array of xpaths in case of ledm resourcetype, jsonpaths in case of cdm resourceType of the attributes
     * 
     */
    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }


    /**
     * filterType none is set when unknown value is received, it is reported in filter error response while applying the filter
     * 
     */
    public enum FilterType {

        @SerializedName("exclusion")
        EXCLUSION("exclusion"),
        @SerializedName("inclusion")
        INCLUSION("inclusion"),
        @SerializedName("none")
        NONE("none");
        private final String value;
        private final static Map<String, FilterInfo.FilterType> CONSTANTS = new HashMap<String, FilterInfo.FilterType>();

        static {
            for (FilterInfo.FilterType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        FilterType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static FilterInfo.FilterType fromValue(String value) {
            FilterInfo.FilterType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
