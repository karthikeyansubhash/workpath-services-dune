
package com.hp.ws.cdm.security;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WindowsTest {

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("windowsSignInConfig")
    @Expose
    private WindowsSignInConfig windowsSignInConfig;
    /**
     * Windows Domain to authenticate
     * 
     */
    @SerializedName("windowsDomain")
    @Expose
    private String windowsDomain;
    /**
     * Username to authenticate
     * 
     */
    @SerializedName("userId")
    @Expose
    private String userId;
    /**
     * Password to authenticate
     * 
     */
    @SerializedName("password")
    @Expose
    private String password;
    /**
     * Windows authentication state
     * 
     */
    @SerializedName("state")
    @Expose
    private WindowsTest.State state;
    /**
     * Authentication Result status
     * 
     */
    @SerializedName("lastResult")
    @Expose
    private WindowsTest.LastResult lastResult;
    /**
     * Time stamp of the last completion result
     * 
     */
    @SerializedName("resultTimeStamp")
    @Expose
    private String resultTimeStamp;
    /**
     * Error code to be used when the status is failed. Future revisions of this type may add new values.
     * 
     */
    @SerializedName("failureReason")
    @Expose
    private WindowsTest.FailureReason failureReason;

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

    public WindowsSignInConfig getWindowsSignInConfig() {
        return windowsSignInConfig;
    }

    public void setWindowsSignInConfig(WindowsSignInConfig windowsSignInConfig) {
        this.windowsSignInConfig = windowsSignInConfig;
    }

    /**
     * Windows Domain to authenticate
     * 
     */
    public String getWindowsDomain() {
        return windowsDomain;
    }

    /**
     * Windows Domain to authenticate
     * 
     */
    public void setWindowsDomain(String windowsDomain) {
        this.windowsDomain = windowsDomain;
    }

    /**
     * Username to authenticate
     * 
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Username to authenticate
     * 
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Password to authenticate
     * 
     */
    public String getPassword() {
        return password;
    }

    /**
     * Password to authenticate
     * 
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Windows authentication state
     * 
     */
    public WindowsTest.State getState() {
        return state;
    }

    /**
     * Windows authentication state
     * 
     */
    public void setState(WindowsTest.State state) {
        this.state = state;
    }

    /**
     * Authentication Result status
     * 
     */
    public WindowsTest.LastResult getLastResult() {
        return lastResult;
    }

    /**
     * Authentication Result status
     * 
     */
    public void setLastResult(WindowsTest.LastResult lastResult) {
        this.lastResult = lastResult;
    }

    /**
     * Time stamp of the last completion result
     * 
     */
    public String getResultTimeStamp() {
        return resultTimeStamp;
    }

    /**
     * Time stamp of the last completion result
     * 
     */
    public void setResultTimeStamp(String resultTimeStamp) {
        this.resultTimeStamp = resultTimeStamp;
    }

    /**
     * Error code to be used when the status is failed. Future revisions of this type may add new values.
     * 
     */
    public WindowsTest.FailureReason getFailureReason() {
        return failureReason;
    }

    /**
     * Error code to be used when the status is failed. Future revisions of this type may add new values.
     * 
     */
    public void setFailureReason(WindowsTest.FailureReason failureReason) {
        this.failureReason = failureReason;
    }


    /**
     * Error code to be used when the status is failed. Future revisions of this type may add new values.
     * 
     */
    public enum FailureReason {

        @SerializedName("timedout")
        TIMEDOUT("timedout"),
        @SerializedName("invalidAddress")
        INVALID_ADDRESS("invalidAddress"),
        @SerializedName("connectionError")
        CONNECTION_ERROR("connectionError"),
        @SerializedName("serverError")
        SERVER_ERROR("serverError"),
        @SerializedName("authenticationFailed")
        AUTHENTICATION_FAILED("authenticationFailed");
        private final String value;
        private final static Map<String, WindowsTest.FailureReason> CONSTANTS = new HashMap<String, WindowsTest.FailureReason>();

        static {
            for (WindowsTest.FailureReason c: values()) {
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

        public static WindowsTest.FailureReason fromValue(String value) {
            WindowsTest.FailureReason constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Authentication Result status
     * 
     */
    public enum LastResult {

        @SerializedName("success")
        SUCCESS("success"),
        @SerializedName("failed")
        FAILED("failed"),
        @SerializedName("canceled")
        CANCELED("canceled");
        private final String value;
        private final static Map<String, WindowsTest.LastResult> CONSTANTS = new HashMap<String, WindowsTest.LastResult>();

        static {
            for (WindowsTest.LastResult c: values()) {
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

        public static WindowsTest.LastResult fromValue(String value) {
            WindowsTest.LastResult constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Windows authentication state
     * 
     */
    public enum State {

        @SerializedName("processing")
        PROCESSING("processing"),
        @SerializedName("canceling")
        CANCELING("canceling"),
        @SerializedName("idle")
        IDLE("idle");
        private final String value;
        private final static Map<String, WindowsTest.State> CONSTANTS = new HashMap<String, WindowsTest.State>();

        static {
            for (WindowsTest.State c: values()) {
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

        public static WindowsTest.State fromValue(String value) {
            WindowsTest.State constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
