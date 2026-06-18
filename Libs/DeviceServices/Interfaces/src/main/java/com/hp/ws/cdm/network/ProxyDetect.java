
package com.hp.ws.cdm.network;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProxyDetect {

    /**
     * The failure reason when  lastResult is failed
     * 
     */
    @SerializedName("failureReason")
    @Expose
    private ProxyDetect.FailureReason failureReason;
    /**
     * Array of the detected proxy servers
     * 
     */
    @SerializedName("httpProxyList")
    @Expose
    private List<DetectedHttpProxy> httpProxyList = new ArrayList<DetectedHttpProxy>();
    /**
     * Last result from the web proxy detect
     * 
     */
    @SerializedName("lastResult")
    @Expose
    private ProxyDetect.LastResult lastResult;
    /**
     * Detected URL path to Proxy Auto-Configuration (PAC) file
     * 
     */
    @SerializedName("pacUrl")
    @Expose
    private String pacUrl;
    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    @SerializedName("resultTimestamp")
    @Expose
    private Date resultTimestamp;
    /**
     * Proxy detect state
     * 
     */
    @SerializedName("state")
    @Expose
    private ProxyDetect.State state;
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
     * The failure reason when  lastResult is failed
     * 
     */
    public ProxyDetect.FailureReason getFailureReason() {
        return failureReason;
    }

    /**
     * The failure reason when  lastResult is failed
     * 
     */
    public void setFailureReason(ProxyDetect.FailureReason failureReason) {
        this.failureReason = failureReason;
    }

    /**
     * Array of the detected proxy servers
     * 
     */
    public List<DetectedHttpProxy> getHttpProxyList() {
        return httpProxyList;
    }

    /**
     * Array of the detected proxy servers
     * 
     */
    public void setHttpProxyList(List<DetectedHttpProxy> httpProxyList) {
        this.httpProxyList = httpProxyList;
    }

    /**
     * Last result from the web proxy detect
     * 
     */
    public ProxyDetect.LastResult getLastResult() {
        return lastResult;
    }

    /**
     * Last result from the web proxy detect
     * 
     */
    public void setLastResult(ProxyDetect.LastResult lastResult) {
        this.lastResult = lastResult;
    }

    /**
     * Detected URL path to Proxy Auto-Configuration (PAC) file
     * 
     */
    public String getPacUrl() {
        return pacUrl;
    }

    /**
     * Detected URL path to Proxy Auto-Configuration (PAC) file
     * 
     */
    public void setPacUrl(String pacUrl) {
        this.pacUrl = pacUrl;
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

    /**
     * Proxy detect state
     * 
     */
    public ProxyDetect.State getState() {
        return state;
    }

    /**
     * Proxy detect state
     * 
     */
    public void setState(ProxyDetect.State state) {
        this.state = state;
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
     * The failure reason when  lastResult is failed
     * 
     */
    public enum FailureReason {

        @SerializedName("connectionTimeout")
        CONNECTION_TIMEOUT("connectionTimeout"),
        @SerializedName("connectionError")
        CONNECTION_ERROR("connectionError"),
        @SerializedName("parseError")
        PARSE_ERROR("parseError"),
        @SerializedName("cancelled")
        CANCELLED("cancelled");
        private final String value;
        private final static Map<String, ProxyDetect.FailureReason> CONSTANTS = new HashMap<String, ProxyDetect.FailureReason>();

        static {
            for (ProxyDetect.FailureReason c: values()) {
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

        public static ProxyDetect.FailureReason fromValue(String value) {
            ProxyDetect.FailureReason constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Last result from the web proxy detect
     * 
     */
    public enum LastResult {

        @SerializedName("success")
        SUCCESS("success"),
        @SerializedName("failed")
        FAILED("failed");
        private final String value;
        private final static Map<String, ProxyDetect.LastResult> CONSTANTS = new HashMap<String, ProxyDetect.LastResult>();

        static {
            for (ProxyDetect.LastResult c: values()) {
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

        public static ProxyDetect.LastResult fromValue(String value) {
            ProxyDetect.LastResult constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Proxy detect state
     * 
     */
    public enum State {

        @SerializedName("idle")
        IDLE("idle"),
        @SerializedName("processing")
        PROCESSING("processing"),
        @SerializedName("cancelProcessing")
        CANCEL_PROCESSING("cancelProcessing");
        private final String value;
        private final static Map<String, ProxyDetect.State> CONSTANTS = new HashMap<String, ProxyDetect.State>();

        static {
            for (ProxyDetect.State c: values()) {
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

        public static ProxyDetect.State fromValue(String value) {
            ProxyDetect.State constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
