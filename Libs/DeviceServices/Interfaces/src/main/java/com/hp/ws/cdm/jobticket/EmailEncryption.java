
package com.hp.ws.cdm.jobticket;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;


/**
 * Encrypt the email using provided algorithm
 * 
 */
public class EmailEncryption {

    /**
     * This is the enum which represent the user selected email encryption algorithm
     * (Required)
     * 
     */
    @SerializedName("algorithm")
    @Expose
    private EncryptionAlgorithm algorithm;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("isEditable")
    @Expose
    private Property.FeatureEnabled isEditable;
    /**
     * To retrieve recipient public key certificate using this attribute
     * 
     */
    @SerializedName("recipientsPublicKey")
    @Expose
    private String recipientsPublicKey;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("verifyRecipientUsingPublicKey")
    @Expose
    private Property.FeatureEnabled verifyRecipientUsingPublicKey;

    /**
     * This is the enum which represent the user selected email encryption algorithm
     * (Required)
     * 
     */
    public EncryptionAlgorithm getAlgorithm() {
        return algorithm;
    }

    /**
     * This is the enum which represent the user selected email encryption algorithm
     * (Required)
     * 
     */
    public void setAlgorithm(EncryptionAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getIsEditable() {
        return isEditable;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setIsEditable(Property.FeatureEnabled isEditable) {
        this.isEditable = isEditable;
    }

    /**
     * To retrieve recipient public key certificate using this attribute
     * 
     */
    public String getRecipientsPublicKey() {
        return recipientsPublicKey;
    }

    /**
     * To retrieve recipient public key certificate using this attribute
     * 
     */
    public void setRecipientsPublicKey(String recipientsPublicKey) {
        this.recipientsPublicKey = recipientsPublicKey;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getVerifyRecipientUsingPublicKey() {
        return verifyRecipientUsingPublicKey;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setVerifyRecipientUsingPublicKey(Property.FeatureEnabled verifyRecipientUsingPublicKey) {
        this.verifyRecipientUsingPublicKey = verifyRecipientUsingPublicKey;
    }


    /**
     * This is the enum which represent the user selected email encryption algorithm
     * 
     */
    public enum EncryptionAlgorithm {

        @SerializedName("aes128")
        AES_128("aes128"),
        @SerializedName("aes256")
        AES_256("aes256"),
        @SerializedName("tripleDes")
        TRIPLE_DES("tripleDes");
        private final String value;
        private final static Map<String, EncryptionAlgorithm> CONSTANTS = new HashMap<String, EncryptionAlgorithm>();

        static {
            for (EncryptionAlgorithm c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        EncryptionAlgorithm(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static EncryptionAlgorithm fromValue(String value) {
            EncryptionAlgorithm constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
