
package com.hp.ws.cdm.ioconfig;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuggestIpAddr {

    /**
     * Suggested IP address
     * 
     */
    @SerializedName("ipAddress")
    @Expose
    private String ipAddress;
    /**
     * Suggested default gateway IP address
     * 
     */
    @SerializedName("ipDefaultGateway")
    @Expose
    private String ipDefaultGateway;
    /**
     * Suggested IP subnet mask
     * 
     */
    @SerializedName("ipSubnetMask")
    @Expose
    private String ipSubnetMask;
    /**
     * Result of last IP address suggestion
     * 
     */
    @SerializedName("lastResult")
    @Expose
    private SuggestIpAddr.LastResult lastResult;
    /**
     * IP address suggestion state
     * 
     */
    @SerializedName("state")
    @Expose
    private SuggestIpAddr.State state;
    /**
     * The time in seconds since the last suggestion
     * 
     */
    @SerializedName("timeSinceLastSuggestion")
    @Expose
    private Integer timeSinceLastSuggestion;
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
     * Suggested IP address
     * 
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * Suggested IP address
     * 
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * Suggested default gateway IP address
     * 
     */
    public String getIpDefaultGateway() {
        return ipDefaultGateway;
    }

    /**
     * Suggested default gateway IP address
     * 
     */
    public void setIpDefaultGateway(String ipDefaultGateway) {
        this.ipDefaultGateway = ipDefaultGateway;
    }

    /**
     * Suggested IP subnet mask
     * 
     */
    public String getIpSubnetMask() {
        return ipSubnetMask;
    }

    /**
     * Suggested IP subnet mask
     * 
     */
    public void setIpSubnetMask(String ipSubnetMask) {
        this.ipSubnetMask = ipSubnetMask;
    }

    /**
     * Result of last IP address suggestion
     * 
     */
    public SuggestIpAddr.LastResult getLastResult() {
        return lastResult;
    }

    /**
     * Result of last IP address suggestion
     * 
     */
    public void setLastResult(SuggestIpAddr.LastResult lastResult) {
        this.lastResult = lastResult;
    }

    /**
     * IP address suggestion state
     * 
     */
    public SuggestIpAddr.State getState() {
        return state;
    }

    /**
     * IP address suggestion state
     * 
     */
    public void setState(SuggestIpAddr.State state) {
        this.state = state;
    }

    /**
     * The time in seconds since the last suggestion
     * 
     */
    public Integer getTimeSinceLastSuggestion() {
        return timeSinceLastSuggestion;
    }

    /**
     * The time in seconds since the last suggestion
     * 
     */
    public void setTimeSinceLastSuggestion(Integer timeSinceLastSuggestion) {
        this.timeSinceLastSuggestion = timeSinceLastSuggestion;
    }

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
     * Result of last IP address suggestion
     * 
     */
    public enum LastResult {

        @SerializedName("success")
        SUCCESS("success"),
        @SerializedName("fail")
        FAIL("fail");
        private final String value;
        private final static Map<String, SuggestIpAddr.LastResult> CONSTANTS = new HashMap<String, SuggestIpAddr.LastResult>();

        static {
            for (SuggestIpAddr.LastResult c: values()) {
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

        public static SuggestIpAddr.LastResult fromValue(String value) {
            SuggestIpAddr.LastResult constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * IP address suggestion state
     * 
     */
    public enum State {

        @SerializedName("ready")
        READY("ready"),
        @SerializedName("processing")
        PROCESSING("processing");
        private final String value;
        private final static Map<String, SuggestIpAddr.State> CONSTANTS = new HashMap<String, SuggestIpAddr.State>();

        static {
            for (SuggestIpAddr.State c: values()) {
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

        public static SuggestIpAddr.State fromValue(String value) {
            SuggestIpAddr.State constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
