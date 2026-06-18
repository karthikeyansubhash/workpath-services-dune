
package com.hp.ws.cdm.jobmanagement;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JobOperation {

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    /**
     * State of the operation.
     * 
     */
    @SerializedName("state")
    @Expose
    private JobOperation.State state;
    /**
     * The result of the last operation.
     * 
     */
    @SerializedName("lastResult")
    @Expose
    private JobOperation.LastResult lastResult;
    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    @SerializedName("lastResultTimestamp")
    @Expose
    private Date lastResultTimestamp;
    /**
     * Specifies the number of jobs that were included when the last operation completed.
     * 
     */
    @SerializedName("lastResultJobCount")
    @Expose
    private Integer lastResultJobCount;

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    public String getVersion() {
        return version;
    }

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * State of the operation.
     * 
     */
    public JobOperation.State getState() {
        return state;
    }

    /**
     * State of the operation.
     * 
     */
    public void setState(JobOperation.State state) {
        this.state = state;
    }

    /**
     * The result of the last operation.
     * 
     */
    public JobOperation.LastResult getLastResult() {
        return lastResult;
    }

    /**
     * The result of the last operation.
     * 
     */
    public void setLastResult(JobOperation.LastResult lastResult) {
        this.lastResult = lastResult;
    }

    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    public Date getLastResultTimestamp() {
        return lastResultTimestamp;
    }

    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    public void setLastResultTimestamp(Date lastResultTimestamp) {
        this.lastResultTimestamp = lastResultTimestamp;
    }

    /**
     * Specifies the number of jobs that were included when the last operation completed.
     * 
     */
    public Integer getLastResultJobCount() {
        return lastResultJobCount;
    }

    /**
     * Specifies the number of jobs that were included when the last operation completed.
     * 
     */
    public void setLastResultJobCount(Integer lastResultJobCount) {
        this.lastResultJobCount = lastResultJobCount;
    }


    /**
     * The result of the last operation.
     * 
     */
    public enum LastResult {

        @SerializedName("unknown")
        UNKNOWN("unknown"),
        @SerializedName("success")
        SUCCESS("success"),
        @SerializedName("failed")
        FAILED("failed");
        private final String value;
        private final static Map<String, JobOperation.LastResult> CONSTANTS = new HashMap<String, JobOperation.LastResult>();

        static {
            for (JobOperation.LastResult c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        LastResult(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static JobOperation.LastResult fromValue(String value) {
            JobOperation.LastResult constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * State of the operation.
     * 
     */
    public enum State {

        @SerializedName("idle")
        IDLE("idle"),
        @SerializedName("processing")
        PROCESSING("processing");
        private final String value;
        private final static Map<String, JobOperation.State> CONSTANTS = new HashMap<String, JobOperation.State>();

        static {
            for (JobOperation.State c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        State(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static JobOperation.State fromValue(String value) {
            JobOperation.State constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
