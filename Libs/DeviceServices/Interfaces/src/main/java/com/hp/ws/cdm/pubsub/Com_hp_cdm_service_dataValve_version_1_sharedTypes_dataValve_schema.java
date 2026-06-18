
package com.hp.ws.cdm.pubsub;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Adding transactionId to filterInfo
 * 
 */
public class Com_hp_cdm_service_dataValve_version_1_sharedTypes_dataValve_schema {

    @SerializedName("filterInfo")
    @Expose
    private FilterInfo filterInfo;
    /**
     * Filter errors
     * 
     */
    @SerializedName("filterError")
    @Expose
    private Com_hp_cdm_service_dataValve_version_1_sharedTypes_dataValve_schema.FilterError filterError;

    public FilterInfo getFilterInfo() {
        return filterInfo;
    }

    public void setFilterInfo(FilterInfo filterInfo) {
        this.filterInfo = filterInfo;
    }

    /**
     * Filter errors
     * 
     */
    public Com_hp_cdm_service_dataValve_version_1_sharedTypes_dataValve_schema.FilterError getFilterError() {
        return filterError;
    }

    /**
     * Filter errors
     * 
     */
    public void setFilterError(Com_hp_cdm_service_dataValve_version_1_sharedTypes_dataValve_schema.FilterError filterError) {
        this.filterError = filterError;
    }


    /**
     * Filter errors
     * 
     */
    public enum FilterError {

        @SerializedName("notFound")
        NOT_FOUND("notFound"),
        @SerializedName("badFilter")
        BAD_FILTER("badFilter");
        private final String value;
        private final static Map<String, Com_hp_cdm_service_dataValve_version_1_sharedTypes_dataValve_schema.FilterError> CONSTANTS = new HashMap<String, Com_hp_cdm_service_dataValve_version_1_sharedTypes_dataValve_schema.FilterError>();

        static {
            for (Com_hp_cdm_service_dataValve_version_1_sharedTypes_dataValve_schema.FilterError c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        FilterError(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Com_hp_cdm_service_dataValve_version_1_sharedTypes_dataValve_schema.FilterError fromValue(String value) {
            Com_hp_cdm_service_dataValve_version_1_sharedTypes_dataValve_schema.FilterError constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
