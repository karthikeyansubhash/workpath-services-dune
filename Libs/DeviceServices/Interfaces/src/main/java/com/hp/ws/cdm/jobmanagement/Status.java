
package com.hp.ws.cdm.jobmanagement;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Status {

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
     * State indicating if the device is idle/processing or paused. Clients can pause the device by setting this state to pauseProcessing. The device can then be resumed by setting this state to resumeProcessing.
     * 
     */
    @SerializedName("state")
    @Expose
    private Status.State state;

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
     * State indicating if the device is idle/processing or paused. Clients can pause the device by setting this state to pauseProcessing. The device can then be resumed by setting this state to resumeProcessing.
     * 
     */
    public Status.State getState() {
        return state;
    }

    /**
     * State indicating if the device is idle/processing or paused. Clients can pause the device by setting this state to pauseProcessing. The device can then be resumed by setting this state to resumeProcessing.
     * 
     */
    public void setState(Status.State state) {
        this.state = state;
    }


    /**
     * State indicating if the device is idle/processing or paused. Clients can pause the device by setting this state to pauseProcessing. The device can then be resumed by setting this state to resumeProcessing.
     * 
     */
    public enum State {

        @SerializedName("idle")
        IDLE("idle"),
        @SerializedName("processing")
        PROCESSING("processing"),
        @SerializedName("pauseProcessing")
        PAUSE_PROCESSING("pauseProcessing"),
        @SerializedName("paused")
        PAUSED("paused"),
        @SerializedName("resumeProcessing")
        RESUME_PROCESSING("resumeProcessing");
        private final String value;
        private final static Map<String, Status.State> CONSTANTS = new HashMap<String, Status.State>();

        static {
            for (Status.State c: values()) {
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

        public static Status.State fromValue(String value) {
            Status.State constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
