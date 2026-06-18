
package com.hp.ws.cdm.network;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class SnmpV3Config {

    /**
     * Account Lockout Configuration
     * 
     */
    @SerializedName("accountLockout")
    @Expose
    private AccountLockout accountLockout;
    @SerializedName("authenticationAlgorithm")
    @Expose
    private SnmpV3Config.SnmpAuthenticationAlgorithm authenticationAlgorithm;
    @SerializedName("authenticationPassphrase")
    @Expose
    private String authenticationPassphrase;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("authenticationPassphraseSet")
    @Expose
    private Property.FeatureEnabled authenticationPassphraseSet;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("enabled")
    @Expose
    private Property.FeatureEnabled enabled;
    @SerializedName("keyType")
    @Expose
    private SnmpV3Config.SnmpKeyType keyType;
    /**
     * Minimum length of the SNMPv3 account passphrase to be enforced
     * 
     */
    @SerializedName("minPasswordLength")
    @Expose
    private Integer minPasswordLength;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("minPasswordLengthEnabled")
    @Expose
    private Property.FeatureEnabled minPasswordLengthEnabled;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("passwordComplexityEnabled")
    @Expose
    private Property.FeatureEnabled passwordComplexityEnabled;
    @SerializedName("privacyAlgorithm")
    @Expose
    private SnmpV3Config.SnmpPrivacyAlgorithm privacyAlgorithm;
    @SerializedName("privacyPassphrase")
    @Expose
    private String privacyPassphrase;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("privacyPassphraseSet")
    @Expose
    private Property.FeatureEnabled privacyPassphraseSet;
    @SerializedName("securityName")
    @Expose
    private String securityName;

    /**
     * Account Lockout Configuration
     * 
     */
    public AccountLockout getAccountLockout() {
        return accountLockout;
    }

    /**
     * Account Lockout Configuration
     * 
     */
    public void setAccountLockout(AccountLockout accountLockout) {
        this.accountLockout = accountLockout;
    }

    public SnmpV3Config.SnmpAuthenticationAlgorithm getAuthenticationAlgorithm() {
        return authenticationAlgorithm;
    }

    public void setAuthenticationAlgorithm(SnmpV3Config.SnmpAuthenticationAlgorithm authenticationAlgorithm) {
        this.authenticationAlgorithm = authenticationAlgorithm;
    }

    public String getAuthenticationPassphrase() {
        return authenticationPassphrase;
    }

    public void setAuthenticationPassphrase(String authenticationPassphrase) {
        this.authenticationPassphrase = authenticationPassphrase;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getAuthenticationPassphraseSet() {
        return authenticationPassphraseSet;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setAuthenticationPassphraseSet(Property.FeatureEnabled authenticationPassphraseSet) {
        this.authenticationPassphraseSet = authenticationPassphraseSet;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getEnabled() {
        return enabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setEnabled(Property.FeatureEnabled enabled) {
        this.enabled = enabled;
    }

    public SnmpV3Config.SnmpKeyType getKeyType() {
        return keyType;
    }

    public void setKeyType(SnmpV3Config.SnmpKeyType keyType) {
        this.keyType = keyType;
    }

    /**
     * Minimum length of the SNMPv3 account passphrase to be enforced
     * 
     */
    public Integer getMinPasswordLength() {
        return minPasswordLength;
    }

    /**
     * Minimum length of the SNMPv3 account passphrase to be enforced
     * 
     */
    public void setMinPasswordLength(Integer minPasswordLength) {
        this.minPasswordLength = minPasswordLength;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getMinPasswordLengthEnabled() {
        return minPasswordLengthEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setMinPasswordLengthEnabled(Property.FeatureEnabled minPasswordLengthEnabled) {
        this.minPasswordLengthEnabled = minPasswordLengthEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getPasswordComplexityEnabled() {
        return passwordComplexityEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setPasswordComplexityEnabled(Property.FeatureEnabled passwordComplexityEnabled) {
        this.passwordComplexityEnabled = passwordComplexityEnabled;
    }

    public SnmpV3Config.SnmpPrivacyAlgorithm getPrivacyAlgorithm() {
        return privacyAlgorithm;
    }

    public void setPrivacyAlgorithm(SnmpV3Config.SnmpPrivacyAlgorithm privacyAlgorithm) {
        this.privacyAlgorithm = privacyAlgorithm;
    }

    public String getPrivacyPassphrase() {
        return privacyPassphrase;
    }

    public void setPrivacyPassphrase(String privacyPassphrase) {
        this.privacyPassphrase = privacyPassphrase;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getPrivacyPassphraseSet() {
        return privacyPassphraseSet;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setPrivacyPassphraseSet(Property.FeatureEnabled privacyPassphraseSet) {
        this.privacyPassphraseSet = privacyPassphraseSet;
    }

    public String getSecurityName() {
        return securityName;
    }

    public void setSecurityName(String securityName) {
        this.securityName = securityName;
    }

    public enum SnmpAuthenticationAlgorithm {

        @SerializedName("md5")
        MD_5("md5"),
        @SerializedName("sha1")
        SHA_1("sha1"),
        @SerializedName("sha2")
        SHA_2("sha2");
        private final String value;
        private final static Map<String, SnmpV3Config.SnmpAuthenticationAlgorithm> CONSTANTS = new HashMap<String, SnmpV3Config.SnmpAuthenticationAlgorithm>();

        static {
            for (SnmpV3Config.SnmpAuthenticationAlgorithm c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        SnmpAuthenticationAlgorithm(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static SnmpV3Config.SnmpAuthenticationAlgorithm fromValue(String value) {
            SnmpV3Config.SnmpAuthenticationAlgorithm constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum SnmpKeyType {

        @SerializedName("hexKey")
        HEX_KEY("hexKey"),
        @SerializedName("passPhrase")
        PASS_PHRASE("passPhrase");
        private final String value;
        private final static Map<String, SnmpV3Config.SnmpKeyType> CONSTANTS = new HashMap<String, SnmpV3Config.SnmpKeyType>();

        static {
            for (SnmpV3Config.SnmpKeyType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        SnmpKeyType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static SnmpV3Config.SnmpKeyType fromValue(String value) {
            SnmpV3Config.SnmpKeyType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum SnmpPrivacyAlgorithm {

        @SerializedName("des")
        DES("des"),
        @SerializedName("aes128")
        AES_128("aes128"),
        @SerializedName("aes256")
        AES_256("aes256");
        private final String value;
        private final static Map<String, SnmpV3Config.SnmpPrivacyAlgorithm> CONSTANTS = new HashMap<String, SnmpV3Config.SnmpPrivacyAlgorithm>();

        static {
            for (SnmpV3Config.SnmpPrivacyAlgorithm c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        SnmpPrivacyAlgorithm(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static SnmpV3Config.SnmpPrivacyAlgorithm fromValue(String value) {
            SnmpV3Config.SnmpPrivacyAlgorithm constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
