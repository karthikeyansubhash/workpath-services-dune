
package com.hp.ws.cdm.security;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;


/**
 * When state is completed, this communicates the results of the authentication
 * 
 */
public class AuthResult {

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("success")
    @Expose
    private Property.FeatureEnabled success;
    /**
     * If success is true, this holds an authorization code that can be exchanged for an access token. When exchanging this code, the RFC 7636 code verifier associated with the code challenge provided when creating the authorizer resource must also be provided.
     * 
     */
    @SerializedName("authorizationCode")
    @Expose
    private String authorizationCode;
    /**
     * If success is false, this communicates why authentication failed. Future revisions of this interface may add new values.
     * 
     */
    @SerializedName("failureCode")
    @Expose
    private AuthResult.FailureCode failureCode;
    /**
     * Present if and only if the failureCode has a value of “customMessage”. This is an already-localized message indicating the failure reason.
     * 
     */
    @SerializedName("failureMessage")
    @Expose
    private String failureMessage;

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getSuccess() {
        return success;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setSuccess(Property.FeatureEnabled success) {
        this.success = success;
    }

    /**
     * If success is true, this holds an authorization code that can be exchanged for an access token. When exchanging this code, the RFC 7636 code verifier associated with the code challenge provided when creating the authorizer resource must also be provided.
     * 
     */
    public String getAuthorizationCode() {
        return authorizationCode;
    }

    /**
     * If success is true, this holds an authorization code that can be exchanged for an access token. When exchanging this code, the RFC 7636 code verifier associated with the code challenge provided when creating the authorizer resource must also be provided.
     * 
     */
    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    /**
     * If success is false, this communicates why authentication failed. Future revisions of this interface may add new values.
     * 
     */
    public AuthResult.FailureCode getFailureCode() {
        return failureCode;
    }

    /**
     * If success is false, this communicates why authentication failed. Future revisions of this interface may add new values.
     * 
     */
    public void setFailureCode(AuthResult.FailureCode failureCode) {
        this.failureCode = failureCode;
    }

    /**
     * Present if and only if the failureCode has a value of “customMessage”. This is an already-localized message indicating the failure reason.
     * 
     */
    public String getFailureMessage() {
        return failureMessage;
    }

    /**
     * Present if and only if the failureCode has a value of “customMessage”. This is an already-localized message indicating the failure reason.
     * 
     */
    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }


    /**
     * If success is false, this communicates why authentication failed. Future revisions of this interface may add new values.
     * 
     */
    public enum FailureCode {

        @SerializedName("customMessage")
        CUSTOM_MESSAGE("customMessage"),
        @SerializedName("canceled")
        CANCELED("canceled"),
        @SerializedName("invalidCredentials")
        INVALID_CREDENTIALS("invalidCredentials"),
        @SerializedName("remoteCommunicationFailure")
        REMOTE_COMMUNICATION_FAILURE("remoteCommunicationFailure"),
        @SerializedName("other")
        OTHER("other");
        private final String value;
        private final static Map<String, AuthResult.FailureCode> CONSTANTS = new HashMap<String, AuthResult.FailureCode>();

        static {
            for (AuthResult.FailureCode c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        FailureCode(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static AuthResult.FailureCode fromValue(String value) {
            AuthResult.FailureCode constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
