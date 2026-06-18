
package com.hp.ws.cdm.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInfo {

    /**
     * The authentication type used. Future revisions of this type may add new values.
     * (Required)
     * 
     */
    @SerializedName("authenticationType")
    @Expose
    private UserInfo.AuthenticationType authenticationType;
    /**
     * The ID of the authentication agent used
     * (Required)
     * 
     */
    @SerializedName("authenticationAgent")
    @Expose
    private String authenticationAgent;
    /**
     * The user's fully qualified name
     * (Required)
     * 
     */
    @SerializedName("fullyQualifiedUserName")
    @Expose
    private String fullyQualifiedUserName;
    /**
     * The user's simple name
     * 
     */
    @SerializedName("simpleName")
    @Expose
    private String simpleName;
    /**
     * The user's display name
     * 
     */
    @SerializedName("displayName")
    @Expose
    private String displayName;
    /**
     * The user's e-mail address
     * 
     */
    @SerializedName("emailAddress")
    @Expose
    private String emailAddress;
    /**
     * Array of user attributes provided by the authentication agent, including those explicitly provided in other fields.
     * 
     */
    @SerializedName("userAttributes")
    @Expose
    private List<UserAttribute> userAttributes = new ArrayList<UserAttribute>();

    /**
     * The authentication type used. Future revisions of this type may add new values.
     * (Required)
     * 
     */
    public UserInfo.AuthenticationType getAuthenticationType() {
        return authenticationType;
    }

    /**
     * The authentication type used. Future revisions of this type may add new values.
     * (Required)
     * 
     */
    public void setAuthenticationType(UserInfo.AuthenticationType authenticationType) {
        this.authenticationType = authenticationType;
    }

    /**
     * The ID of the authentication agent used
     * (Required)
     * 
     */
    public String getAuthenticationAgent() {
        return authenticationAgent;
    }

    /**
     * The ID of the authentication agent used
     * (Required)
     * 
     */
    public void setAuthenticationAgent(String authenticationAgent) {
        this.authenticationAgent = authenticationAgent;
    }

    /**
     * The user's fully qualified name
     * (Required)
     * 
     */
    public String getFullyQualifiedUserName() {
        return fullyQualifiedUserName;
    }

    /**
     * The user's fully qualified name
     * (Required)
     * 
     */
    public void setFullyQualifiedUserName(String fullyQualifiedUserName) {
        this.fullyQualifiedUserName = fullyQualifiedUserName;
    }

    /**
     * The user's simple name
     * 
     */
    public String getSimpleName() {
        return simpleName;
    }

    /**
     * The user's simple name
     * 
     */
    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    /**
     * The user's display name
     * 
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * The user's display name
     * 
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * The user's e-mail address
     * 
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * The user's e-mail address
     * 
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Array of user attributes provided by the authentication agent, including those explicitly provided in other fields.
     * 
     */
    public List<UserAttribute> getUserAttributes() {
        return userAttributes;
    }

    /**
     * Array of user attributes provided by the authentication agent, including those explicitly provided in other fields.
     * 
     */
    public void setUserAttributes(List<UserAttribute> userAttributes) {
        this.userAttributes = userAttributes;
    }


    /**
     * The authentication type used. Future revisions of this type may add new values.
     * 
     */
    public enum AuthenticationType {

        @SerializedName("deviceAdmin")
        DEVICE_ADMIN("deviceAdmin"),
        @SerializedName("devicePin")
        DEVICE_PIN("devicePin"),
        @SerializedName("deviceUser")
        DEVICE_USER("deviceUser"),
        @SerializedName("idCode")
        ID_CODE("idCode"),
        @SerializedName("deviceService")
        DEVICE_SERVICE("deviceService"),
        @SerializedName("pushButton")
        PUSH_BUTTON("pushButton"),
        @SerializedName("ldap")
        LDAP("ldap"),
        @SerializedName("windows")
        WINDOWS("windows"),
        @SerializedName("smartcard")
        SMARTCARD("smartcard"),
        @SerializedName("abacus")
        ABACUS("abacus"),
        @SerializedName("custom")
        CUSTOM("custom"),
        @SerializedName("guest")
        GUEST("guest"),
        @SerializedName("argos")
        ARGOS("argos"),
        @SerializedName("other")
        OTHER("other");
        private final String value;
        private final static Map<String, UserInfo.AuthenticationType> CONSTANTS = new HashMap<String, UserInfo.AuthenticationType>();

        static {
            for (UserInfo.AuthenticationType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        AuthenticationType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static UserInfo.AuthenticationType fromValue(String value) {
            UserInfo.AuthenticationType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
