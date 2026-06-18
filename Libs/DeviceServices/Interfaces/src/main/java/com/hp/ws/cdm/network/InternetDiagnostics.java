
package com.hp.ws.cdm.network;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InternetDiagnostics {

    @SerializedName("failureReason")
    @Expose
    private InternetDiagnostics.FailureReason failureReason;
    @SerializedName("lastResult")
    @Expose
    private InternetDiagnostics.LastResult lastResult;
    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    @SerializedName("resultTimestamp")
    @Expose
    private Date resultTimestamp;
    @SerializedName("state")
    @Expose
    private InternetDiagnostics.State state;
    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;

    public InternetDiagnostics.FailureReason getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(InternetDiagnostics.FailureReason failureReason) {
        this.failureReason = failureReason;
    }

    public InternetDiagnostics.LastResult getLastResult() {
        return lastResult;
    }

    public void setLastResult(InternetDiagnostics.LastResult lastResult) {
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

    public InternetDiagnostics.State getState() {
        return state;
    }

    public void setState(InternetDiagnostics.State state) {
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

    public enum FailureReason {

        @SerializedName("dnsNotConfigured")
        DNS_NOT_CONFIGURED("dnsNotConfigured"),
        @SerializedName("dnsNotReachable")
        DNS_NOT_REACHABLE("dnsNotReachable"),
        @SerializedName("dnsNotResolved")
        DNS_NOT_RESOLVED("dnsNotResolved"),
        @SerializedName("gatewayNotConfigured")
        GATEWAY_NOT_CONFIGURED("gatewayNotConfigured"),
        @SerializedName("gatewayNotReachable")
        GATEWAY_NOT_REACHABLE("gatewayNotReachable"),
        @SerializedName("ipNotRoutable")
        IP_NOT_ROUTABLE("ipNotRoutable"),
        @SerializedName("networkNotConnected")
        NETWORK_NOT_CONNECTED("networkNotConnected"),
        @SerializedName("proxyConnectionFailed")
        PROXY_CONNECTION_FAILED("proxyConnectionFailed"),
        @SerializedName("proxyCredentialsIncorrect")
        PROXY_CREDENTIALS_INCORRECT("proxyCredentialsIncorrect"),
        @SerializedName("proxyNotConfigured")
        PROXY_NOT_CONFIGURED("proxyNotConfigured"),
        @SerializedName("proxyRequiresAuthentication")
        PROXY_REQUIRES_AUTHENTICATION("proxyRequiresAuthentication"),
        @SerializedName("proxyURLResolutionFailed")
        PROXY_URL_RESOLUTION_FAILED("proxyURLResolutionFailed"),
        @SerializedName("duplicateIp")
        DUPLICATE_IP("duplicateIp"),
        @SerializedName("serverConnectionFailed")
        SERVER_CONNECTION_FAILED("serverConnectionFailed"),
        @SerializedName("httpRedirect")
        HTTP_REDIRECT("httpRedirect"),
        @SerializedName("other")
        OTHER("other");
        private final String value;
        private final static Map<String, InternetDiagnostics.FailureReason> CONSTANTS = new HashMap<String, InternetDiagnostics.FailureReason>();

        static {
            for (InternetDiagnostics.FailureReason c: values()) {
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

        public static InternetDiagnostics.FailureReason fromValue(String value) {
            InternetDiagnostics.FailureReason constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum LastResult {

        @SerializedName("connected")
        CONNECTED("connected"),
        @SerializedName("failed")
        FAILED("failed"),
        @SerializedName("cancelled")
        CANCELLED("cancelled");
        private final String value;
        private final static Map<String, InternetDiagnostics.LastResult> CONSTANTS = new HashMap<String, InternetDiagnostics.LastResult>();

        static {
            for (InternetDiagnostics.LastResult c: values()) {
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

        public static InternetDiagnostics.LastResult fromValue(String value) {
            InternetDiagnostics.LastResult constant = CONSTANTS.get(value);
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
        @SerializedName("cancelProcessing")
        CANCEL_PROCESSING("cancelProcessing"),
        @SerializedName("disabled")
        DISABLED("disabled");
        private final String value;
        private final static Map<String, InternetDiagnostics.State> CONSTANTS = new HashMap<String, InternetDiagnostics.State>();

        static {
            for (InternetDiagnostics.State c: values()) {
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

        public static InternetDiagnostics.State fromValue(String value) {
            InternetDiagnostics.State constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
