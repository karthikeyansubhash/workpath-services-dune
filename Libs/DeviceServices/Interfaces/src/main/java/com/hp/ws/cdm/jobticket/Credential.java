
package com.hp.ws.cdm.jobticket;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class Credential {

    @SerializedName("credentialType")
    @Expose
    private CredentialType credentialType;
    @SerializedName("domainName")
    @Expose
    private String domainName;
    @SerializedName("userName")
    @Expose
    private String userName;
    /**
     * Write only property, sets the password field
     * 
     */
    @SerializedName("password")
    @Expose
    private String password;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("isPasswordSet")
    @Expose
    private Property.FeatureEnabled isPasswordSet;

    public CredentialType getCredentialType() {
        return credentialType;
    }

    public void setCredentialType(CredentialType credentialType) {
        this.credentialType = credentialType;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Write only property, sets the password field
     * 
     */
    public String getPassword() {
        return password;
    }

    /**
     * Write only property, sets the password field
     * 
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getIsPasswordSet() {
        return isPasswordSet;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setIsPasswordSet(Property.FeatureEnabled isPasswordSet) {
        this.isPasswordSet = isPasswordSet;
    }

    public enum CredentialType {

        @SerializedName("signIn")
        SIGN_IN("signIn"),
        @SerializedName("alwaysUse")
        ALWAYS_USE("alwaysUse");
        private final String value;
        private final static Map<String, CredentialType> CONSTANTS = new HashMap<String, CredentialType>();

        static {
            for (CredentialType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        CredentialType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static CredentialType fromValue(String value) {
            CredentialType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
