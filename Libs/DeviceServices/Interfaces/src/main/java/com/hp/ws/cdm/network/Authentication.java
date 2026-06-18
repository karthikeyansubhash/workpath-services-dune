
package com.hp.ws.cdm.network;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;


/**
 * Proxy Server authentication configuration
 * 
 */
public class Authentication {

    /**
     * User password
     * 
     */
    @SerializedName("password")
    @Expose
    private String password;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("passwordSet")
    @Expose
    private Property.FeatureEnabled passwordSet;
    /**
     * When auto type is selected, client could use any of the supported authentication methods or auto detect
     * 
     */
    @SerializedName("type")
    @Expose
    private Authentication.Type type;
    /**
     * User authentication identity
     * 
     */
    @SerializedName("userId")
    @Expose
    private String userId;

    /**
     * User password
     * 
     */
    public String getPassword() {
        return password;
    }

    /**
     * User password
     * 
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getPasswordSet() {
        return passwordSet;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setPasswordSet(Property.FeatureEnabled passwordSet) {
        this.passwordSet = passwordSet;
    }

    /**
     * When auto type is selected, client could use any of the supported authentication methods or auto detect
     * 
     */
    public Authentication.Type getType() {
        return type;
    }

    /**
     * When auto type is selected, client could use any of the supported authentication methods or auto detect
     * 
     */
    public void setType(Authentication.Type type) {
        this.type = type;
    }

    /**
     * User authentication identity
     * 
     */
    public String getUserId() {
        return userId;
    }

    /**
     * User authentication identity
     * 
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }


    /**
     * When auto type is selected, client could use any of the supported authentication methods or auto detect
     * 
     */
    public enum Type {

        @SerializedName("noAuth")
        NO_AUTH("noAuth"),
        @SerializedName("auto")
        AUTO("auto"),
        @SerializedName("basic")
        BASIC("basic"),
        @SerializedName("digest")
        DIGEST("digest"),
        @SerializedName("ntlm")
        NTLM("ntlm");
        private final String value;
        private final static Map<String, Authentication.Type> CONSTANTS = new HashMap<String, Authentication.Type>();

        static {
            for (Authentication.Type c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Type(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Authentication.Type fromValue(String value) {
            Authentication.Type constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
