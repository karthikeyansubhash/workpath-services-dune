
package com.hp.ws.cdm.clock;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NtpServerDiscovery {

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("state")
    @Expose
    private NtpServerDiscovery.State state;
    @SerializedName("ntpServers")
    @Expose
    private List<String> ntpServers = new ArrayList<String>();
    @SerializedName("lastResult")
    @Expose
    private NtpServerDiscovery.LastResult lastResult;
    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    @SerializedName("resultTimestamp")
    @Expose
    private Date resultTimestamp;

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

    public NtpServerDiscovery.State getState() {
        return state;
    }

    public void setState(NtpServerDiscovery.State state) {
        this.state = state;
    }

    public List<String> getNtpServers() {
        return ntpServers;
    }

    public void setNtpServers(List<String> ntpServers) {
        this.ntpServers = ntpServers;
    }

    public NtpServerDiscovery.LastResult getLastResult() {
        return lastResult;
    }

    public void setLastResult(NtpServerDiscovery.LastResult lastResult) {
        this.lastResult = lastResult;
    }

    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    public Date getResultTimestamp() {
        return resultTimestamp;
    }

    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    public void setResultTimestamp(Date resultTimestamp) {
        this.resultTimestamp = resultTimestamp;
    }

    public enum LastResult {

        @SerializedName("success")
        SUCCESS("success"),
        @SerializedName("failure")
        FAILURE("failure");
        private final String value;
        private final static Map<String, NtpServerDiscovery.LastResult> CONSTANTS = new HashMap<String, NtpServerDiscovery.LastResult>();

        static {
            for (NtpServerDiscovery.LastResult c: values()) {
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

        public static NtpServerDiscovery.LastResult fromValue(String value) {
            NtpServerDiscovery.LastResult constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum State {

        @SerializedName("idle")
        IDLE("idle"),
        @SerializedName("processing")
        PROCESSING("processing");
        private final String value;
        private final static Map<String, NtpServerDiscovery.State> CONSTANTS = new HashMap<String, NtpServerDiscovery.State>();

        static {
            for (NtpServerDiscovery.State c: values()) {
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

        public static NtpServerDiscovery.State fromValue(String value) {
            NtpServerDiscovery.State constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
