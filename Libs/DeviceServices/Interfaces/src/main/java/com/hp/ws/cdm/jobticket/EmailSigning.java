
package com.hp.ws.cdm.jobticket;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;


/**
 * Email signing using the provided algorithm
 * 
 */
public class EmailSigning {

    /**
     * This is the enum which represent the user selected email signing algorithm
     * (Required)
     * 
     */
    @SerializedName("algorithm")
    @Expose
    private SigningAlgorithm algorithm;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("isEditable")
    @Expose
    private Property.FeatureEnabled isEditable;

    /**
     * This is the enum which represent the user selected email signing algorithm
     * (Required)
     * 
     */
    public SigningAlgorithm getAlgorithm() {
        return algorithm;
    }

    /**
     * This is the enum which represent the user selected email signing algorithm
     * (Required)
     * 
     */
    public void setAlgorithm(SigningAlgorithm algorithm) {
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
     * This is the enum which represent the user selected email signing algorithm
     * 
     */
    public enum SigningAlgorithm {

        @SerializedName("sha1")
        SHA_1("sha1"),
        @SerializedName("sha256")
        SHA_256("sha256"),
        @SerializedName("sha384")
        SHA_384("sha384"),
        @SerializedName("sha512")
        SHA_512("sha512");
        private final String value;
        private final static Map<String, SigningAlgorithm> CONSTANTS = new HashMap<String, SigningAlgorithm>();

        static {
            for (SigningAlgorithm c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        SigningAlgorithm(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static SigningAlgorithm fromValue(String value) {
            SigningAlgorithm constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
