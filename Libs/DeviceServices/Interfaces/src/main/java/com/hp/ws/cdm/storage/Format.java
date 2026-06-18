
package com.hp.ws.cdm.storage;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class Format {

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
     * The state of operation
     * 
     */
    @SerializedName("state")
    @Expose
    private Format.State state;
    /**
     * The last result of the operation
     * 
     */
    @SerializedName("lastResult")
    @Expose
    private Format.LastResult lastResult;
    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    @SerializedName("lastResultTimeStamp")
    @Expose
    private Date lastResultTimeStamp;
    @SerializedName("failureReason")
    @Expose
    private Format.FailureReason failureReason;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("jobStoragePairingEnabled")
    @Expose
    private Property.FeatureEnabled jobStoragePairingEnabled;
    /**
     * The driveId of storage device to be formatted.
     * 
     */
    @SerializedName("driveId")
    @Expose
    private String driveId;

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
     * The state of operation
     * 
     */
    public Format.State getState() {
        return state;
    }

    /**
     * The state of operation
     * 
     */
    public void setState(Format.State state) {
        this.state = state;
    }

    /**
     * The last result of the operation
     * 
     */
    public Format.LastResult getLastResult() {
        return lastResult;
    }

    /**
     * The last result of the operation
     * 
     */
    public void setLastResult(Format.LastResult lastResult) {
        this.lastResult = lastResult;
    }

    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    public Date getLastResultTimeStamp() {
        return lastResultTimeStamp;
    }

    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    public void setLastResultTimeStamp(Date lastResultTimeStamp) {
        this.lastResultTimeStamp = lastResultTimeStamp;
    }

    public Format.FailureReason getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(Format.FailureReason failureReason) {
        this.failureReason = failureReason;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getJobStoragePairingEnabled() {
        return jobStoragePairingEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setJobStoragePairingEnabled(Property.FeatureEnabled jobStoragePairingEnabled) {
        this.jobStoragePairingEnabled = jobStoragePairingEnabled;
    }

    /**
     * The driveId of storage device to be formatted.
     * 
     */
    public String getDriveId() {
        return driveId;
    }

    /**
     * The driveId of storage device to be formatted.
     * 
     */
    public void setDriveId(String driveId) {
        this.driveId = driveId;
    }

    public enum FailureReason {

        @SerializedName("busy")
        BUSY("busy"),
        @SerializedName("unknown")
        UNKNOWN("unknown");
        private final String value;
        private final static Map<String, Format.FailureReason> CONSTANTS = new HashMap<String, Format.FailureReason>();

        static {
            for (Format.FailureReason c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        FailureReason(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Format.FailureReason fromValue(String value) {
            Format.FailureReason constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * The last result of the operation
     * 
     */
    public enum LastResult {

        @SerializedName("success")
        SUCCESS("success"),
        @SerializedName("fail")
        FAIL("fail");
        private final String value;
        private final static Map<String, Format.LastResult> CONSTANTS = new HashMap<String, Format.LastResult>();

        static {
            for (Format.LastResult c: values()) {
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

        public static Format.LastResult fromValue(String value) {
            Format.LastResult constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * The state of operation
     * 
     */
    public enum State {

        @SerializedName("ready")
        READY("ready"),
        @SerializedName("processing")
        PROCESSING("processing");
        private final String value;
        private final static Map<String, Format.State> CONSTANTS = new HashMap<String, Format.State>();

        static {
            for (Format.State c: values()) {
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

        public static Format.State fromValue(String value) {
            Format.State constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
