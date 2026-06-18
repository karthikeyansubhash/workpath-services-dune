
package com.hp.ws.cdm.ioconfig;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WifiDiagnostics {

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
    private WifiDiagnostics.State state;
    @SerializedName("lastResult")
    @Expose
    private WifiDiagnostics.LastResult lastResult;
    @SerializedName("vsaCodes")
    @Expose
    private List<VsaCodes> vsaCodes = new ArrayList<VsaCodes>();
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

    public WifiDiagnostics.State getState() {
        return state;
    }

    public void setState(WifiDiagnostics.State state) {
        this.state = state;
    }

    public WifiDiagnostics.LastResult getLastResult() {
        return lastResult;
    }

    public void setLastResult(WifiDiagnostics.LastResult lastResult) {
        this.lastResult = lastResult;
    }

    public List<VsaCodes> getVsaCodes() {
        return vsaCodes;
    }

    public void setVsaCodes(List<VsaCodes> vsaCodes) {
        this.vsaCodes = vsaCodes;
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

        @SerializedName("connected")
        CONNECTED("connected"),
        @SerializedName("failed")
        FAILED("failed");
        private final String value;
        private final static Map<String, WifiDiagnostics.LastResult> CONSTANTS = new HashMap<String, WifiDiagnostics.LastResult>();

        static {
            for (WifiDiagnostics.LastResult c: values()) {
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

        public static WifiDiagnostics.LastResult fromValue(String value) {
            WifiDiagnostics.LastResult constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum State {

        @SerializedName("ready")
        READY("ready"),
        @SerializedName("processing")
        PROCESSING("processing"),
        @SerializedName("disabled")
        DISABLED("disabled");
        private final String value;
        private final static Map<String, WifiDiagnostics.State> CONSTANTS = new HashMap<String, WifiDiagnostics.State>();

        static {
            for (WifiDiagnostics.State c: values()) {
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

        public static WifiDiagnostics.State fromValue(String value) {
            WifiDiagnostics.State constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
