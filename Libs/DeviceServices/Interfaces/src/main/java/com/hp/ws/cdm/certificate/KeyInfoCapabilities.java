
package com.hp.ws.cdm.certificate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KeyInfoCapabilities {

    @SerializedName("keyType")
    @Expose
    private KeyInfoCapabilities.KeyType keyType;
    @SerializedName("keyStrengths")
    @Expose
    private List<KeyStrength> keyStrengths = new ArrayList<KeyStrength>();
    @SerializedName("signatureAlgorithms")
    @Expose
    private List<SignatureAlgorithm> signatureAlgorithms = new ArrayList<SignatureAlgorithm>();

    public KeyInfoCapabilities.KeyType getKeyType() {
        return keyType;
    }

    public void setKeyType(KeyInfoCapabilities.KeyType keyType) {
        this.keyType = keyType;
    }

    public List<KeyStrength> getKeyStrengths() {
        return keyStrengths;
    }

    public void setKeyStrengths(List<KeyStrength> keyStrengths) {
        this.keyStrengths = keyStrengths;
    }

    public List<SignatureAlgorithm> getSignatureAlgorithms() {
        return signatureAlgorithms;
    }

    public void setSignatureAlgorithms(List<SignatureAlgorithm> signatureAlgorithms) {
        this.signatureAlgorithms = signatureAlgorithms;
    }

    public enum KeyType {

        @SerializedName("rsa")
        RSA("rsa"),
        @SerializedName("ecc")
        ECC("ecc");
        private final String value;
        private final static Map<String, KeyInfoCapabilities.KeyType> CONSTANTS = new HashMap<String, KeyInfoCapabilities.KeyType>();

        static {
            for (KeyInfoCapabilities.KeyType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        KeyType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static KeyInfoCapabilities.KeyType fromValue(String value) {
            KeyInfoCapabilities.KeyType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
