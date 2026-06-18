
package com.hp.ws.cdm.security;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LdapTest {

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("ldapConfig")
    @Expose
    private LdapConfig ldapConfig;
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
     * LDAP authentication state
     * 
     */
    @SerializedName("state")
    @Expose
    private LdapTest.State state;
    /**
     * Authentication Result status
     * 
     */
    @SerializedName("lastResult")
    @Expose
    private LdapTest.LastResult lastResult;
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
    private LdapTest.FailureReason failureReason;

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

    public LdapConfig getLdapConfig() {
        return ldapConfig;
    }

    public void setLdapConfig(LdapConfig ldapConfig) {
        this.ldapConfig = ldapConfig;
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
     * LDAP authentication state
     * 
     */
    public LdapTest.State getState() {
        return state;
    }

    /**
     * LDAP authentication state
     * 
     */
    public void setState(LdapTest.State state) {
        this.state = state;
    }

    /**
     * Authentication Result status
     * 
     */
    public LdapTest.LastResult getLastResult() {
        return lastResult;
    }

    /**
     * Authentication Result status
     * 
     */
    public void setLastResult(LdapTest.LastResult lastResult) {
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
    public LdapTest.FailureReason getFailureReason() {
        return failureReason;
    }

    /**
     * Error code to be used when the status is failed. Future revisions of this type may add new values.
     * 
     */
    public void setFailureReason(LdapTest.FailureReason failureReason) {
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
        private final static Map<String, LdapTest.FailureReason> CONSTANTS = new HashMap<String, LdapTest.FailureReason>();

        static {
            for (LdapTest.FailureReason c: values()) {
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

        public static LdapTest.FailureReason fromValue(String value) {
            LdapTest.FailureReason constant = CONSTANTS.get(value);
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
        private final static Map<String, LdapTest.LastResult> CONSTANTS = new HashMap<String, LdapTest.LastResult>();

        static {
            for (LdapTest.LastResult c: values()) {
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

        public static LdapTest.LastResult fromValue(String value) {
            LdapTest.LastResult constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * LDAP authentication state
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
        private final static Map<String, LdapTest.State> CONSTANTS = new HashMap<String, LdapTest.State>();

        static {
            for (LdapTest.State c: values()) {
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

        public static LdapTest.State fromValue(String value) {
            LdapTest.State constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
