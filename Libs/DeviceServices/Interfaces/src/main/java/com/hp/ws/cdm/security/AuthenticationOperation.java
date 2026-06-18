
package com.hp.ws.cdm.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;

public class AuthenticationOperation {

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();
    /**
     * The ID of this resource
     * 
     */
    @SerializedName("authOperationId")
    @Expose
    private String authOperationId;
    /**
     * The agent ID of the agent driving this authentication sequence
     * 
     */
    @SerializedName("authAgentId")
    @Expose
    private String authAgentId;
    /**
     * The language code to be used for any localized user messages, consisting of a two-letter language code from ISO 639 part 1, and  a two-letter country code from ISO 3166 part 1, alpha-2.
     * 
     */
    @SerializedName("languageCode")
    @Expose
    private String languageCode;
    /**
     * The current state of the execution of the sequence
     * 
     */
    @SerializedName("state")
    @Expose
    private AuthenticationOperation.State state;
    /**
     * Indicates if the sequence has been invalidated (logged out)
     * 
     */
    @SerializedName("validity")
    @Expose
    private AuthenticationOperation.Validity validity;
    /**
     * When state is completed, this communicates the results of the authentication
     * 
     */
    @SerializedName("authResult")
    @Expose
    private AuthResult authResult;
    /**
     * This describes what credentials are required
     * 
     */
    @SerializedName("credentialInfo")
    @Expose
    private CredentialInfo credentialInfo;
    /**
     * An RFC 7636 code challenge that will be used in a subsequent authorization code exchange to obtain an access token for a successful authentication. The challenge method is implicitly S256.
     * 
     */
    @SerializedName("codeChallenge")
    @Expose
    private String codeChallenge;
    /**
     * The credentials newly acquired. The field of this object depends on the type of agent and/or credentialInfo.
     * 
     */
    @SerializedName("credentials")
    @Expose
    private Credentials credentials;

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

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    /**
     * The ID of this resource
     * 
     */
    public String getAuthOperationId() {
        return authOperationId;
    }

    /**
     * The ID of this resource
     * 
     */
    public void setAuthOperationId(String authOperationId) {
        this.authOperationId = authOperationId;
    }

    /**
     * The agent ID of the agent driving this authentication sequence
     * 
     */
    public String getAuthAgentId() {
        return authAgentId;
    }

    /**
     * The agent ID of the agent driving this authentication sequence
     * 
     */
    public void setAuthAgentId(String authAgentId) {
        this.authAgentId = authAgentId;
    }

    /**
     * The language code to be used for any localized user messages, consisting of a two-letter language code from ISO 639 part 1, and  a two-letter country code from ISO 3166 part 1, alpha-2.
     * 
     */
    public String getLanguageCode() {
        return languageCode;
    }

    /**
     * The language code to be used for any localized user messages, consisting of a two-letter language code from ISO 639 part 1, and  a two-letter country code from ISO 3166 part 1, alpha-2.
     * 
     */
    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    /**
     * The current state of the execution of the sequence
     * 
     */
    public AuthenticationOperation.State getState() {
        return state;
    }

    /**
     * The current state of the execution of the sequence
     * 
     */
    public void setState(AuthenticationOperation.State state) {
        this.state = state;
    }

    /**
     * Indicates if the sequence has been invalidated (logged out)
     * 
     */
    public AuthenticationOperation.Validity getValidity() {
        return validity;
    }

    /**
     * Indicates if the sequence has been invalidated (logged out)
     * 
     */
    public void setValidity(AuthenticationOperation.Validity validity) {
        this.validity = validity;
    }

    /**
     * When state is completed, this communicates the results of the authentication
     * 
     */
    public AuthResult getAuthResult() {
        return authResult;
    }

    /**
     * When state is completed, this communicates the results of the authentication
     * 
     */
    public void setAuthResult(AuthResult authResult) {
        this.authResult = authResult;
    }

    /**
     * This describes what credentials are required
     * 
     */
    public CredentialInfo getCredentialInfo() {
        return credentialInfo;
    }

    /**
     * This describes what credentials are required
     * 
     */
    public void setCredentialInfo(CredentialInfo credentialInfo) {
        this.credentialInfo = credentialInfo;
    }

    /**
     * An RFC 7636 code challenge that will be used in a subsequent authorization code exchange to obtain an access token for a successful authentication. The challenge method is implicitly S256.
     * 
     */
    public String getCodeChallenge() {
        return codeChallenge;
    }

    /**
     * An RFC 7636 code challenge that will be used in a subsequent authorization code exchange to obtain an access token for a successful authentication. The challenge method is implicitly S256.
     * 
     */
    public void setCodeChallenge(String codeChallenge) {
        this.codeChallenge = codeChallenge;
    }

    /**
     * The credentials newly acquired. The field of this object depends on the type of agent and/or credentialInfo.
     * 
     */
    public Credentials getCredentials() {
        return credentials;
    }

    /**
     * The credentials newly acquired. The field of this object depends on the type of agent and/or credentialInfo.
     * 
     */
    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }


    /**
     * The current state of the execution of the sequence
     * 
     */
    public enum State {

        @SerializedName("authenticating")
        AUTHENTICATING("authenticating"),
        @SerializedName("credentialsRequired")
        CREDENTIALS_REQUIRED("credentialsRequired"),
        @SerializedName("cancelProcessing")
        CANCEL_PROCESSING("cancelProcessing"),
        @SerializedName("completed")
        COMPLETED("completed");
        private final String value;
        private final static Map<String, AuthenticationOperation.State> CONSTANTS = new HashMap<String, AuthenticationOperation.State>();

        static {
            for (AuthenticationOperation.State c: values()) {
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

        public static AuthenticationOperation.State fromValue(String value) {
            AuthenticationOperation.State constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Indicates if the sequence has been invalidated (logged out)
     * 
     */
    public enum Validity {

        @SerializedName("valid")
        VALID("valid"),
        @SerializedName("loggedOut")
        LOGGED_OUT("loggedOut");
        private final String value;
        private final static Map<String, AuthenticationOperation.Validity> CONSTANTS = new HashMap<String, AuthenticationOperation.Validity>();

        static {
            for (AuthenticationOperation.Validity c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Validity(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static AuthenticationOperation.Validity fromValue(String value) {
            AuthenticationOperation.Validity constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
