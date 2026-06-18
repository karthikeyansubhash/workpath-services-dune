
package com.hp.ws.cdm.ioconfig;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NetworkReset {

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
     * Restore network defaults operation state
     * 
     */
    @SerializedName("state")
    @Expose
    private NetworkReset.State state;
    /**
     * Type of restore to be performed
     * 
     */
    @SerializedName("resetType")
    @Expose
    private NetworkReset.ResetType resetType;
    /**
     * Result of last restore network defaults operation
     * 
     */
    @SerializedName("lastResult")
    @Expose
    private NetworkReset.LastResult lastResult;
    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    @SerializedName("resultTimeStamp")
    @Expose
    private Date resultTimeStamp;

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
     * Restore network defaults operation state
     * 
     */
    public NetworkReset.State getState() {
        return state;
    }

    /**
     * Restore network defaults operation state
     * 
     */
    public void setState(NetworkReset.State state) {
        this.state = state;
    }

    /**
     * Type of restore to be performed
     * 
     */
    public NetworkReset.ResetType getResetType() {
        return resetType;
    }

    /**
     * Type of restore to be performed
     * 
     */
    public void setResetType(NetworkReset.ResetType resetType) {
        this.resetType = resetType;
    }

    /**
     * Result of last restore network defaults operation
     * 
     */
    public NetworkReset.LastResult getLastResult() {
        return lastResult;
    }

    /**
     * Result of last restore network defaults operation
     * 
     */
    public void setLastResult(NetworkReset.LastResult lastResult) {
        this.lastResult = lastResult;
    }

    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    public Date getResultTimeStamp() {
        return resultTimeStamp;
    }

    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    public void setResultTimeStamp(Date resultTimeStamp) {
        this.resultTimeStamp = resultTimeStamp;
    }


    /**
     * Result of last restore network defaults operation
     * 
     */
    public enum LastResult {

        @SerializedName("success")
        SUCCESS("success"),
        @SerializedName("failed")
        FAILED("failed");
        private final String value;
        private final static Map<String, NetworkReset.LastResult> CONSTANTS = new HashMap<String, NetworkReset.LastResult>();

        static {
            for (NetworkReset.LastResult c: values()) {
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

        public static NetworkReset.LastResult fromValue(String value) {
            NetworkReset.LastResult constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Type of restore to be performed
     * 
     */
    public enum ResetType {

        @SerializedName("full")
        FULL("full"),
        @SerializedName("security")
        SECURITY("security");
        private final String value;
        private final static Map<String, NetworkReset.ResetType> CONSTANTS = new HashMap<String, NetworkReset.ResetType>();

        static {
            for (NetworkReset.ResetType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        ResetType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static NetworkReset.ResetType fromValue(String value) {
            NetworkReset.ResetType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Restore network defaults operation state
     * 
     */
    public enum State {

        @SerializedName("ready")
        READY("ready"),
        @SerializedName("processing")
        PROCESSING("processing");
        private final String value;
        private final static Map<String, NetworkReset.State> CONSTANTS = new HashMap<String, NetworkReset.State>();

        static {
            for (NetworkReset.State c: values()) {
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

        public static NetworkReset.State fromValue(String value) {
            NetworkReset.State constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
