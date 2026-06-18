
package com.hp.ws.cdm.tlsconfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class Cipher {

    /**
     * Cipher name using the standard convention
     * 
     */
    @SerializedName("cipherName")
    @Expose
    private String cipherName;
    /**
     * Cipher OID
     * 
     */
    @SerializedName("cipherId")
    @Expose
    private String cipherId;
    /**
     * Cipher strength as defined on the device
     * 
     */
    @SerializedName("cipherStrength")
    @Expose
    private Cipher.CipherStrength cipherStrength;
    /**
     * The TLS versions in which cipher is supported
     * 
     */
    @SerializedName("protocolVersions")
    @Expose
    private List<ProtocolVersion> protocolVersions = new ArrayList<ProtocolVersion>();
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("fipsCompatibility")
    @Expose
    private Property.FeatureEnabled fipsCompatibility;

    /**
     * Cipher name using the standard convention
     * 
     */
    public String getCipherName() {
        return cipherName;
    }

    /**
     * Cipher name using the standard convention
     * 
     */
    public void setCipherName(String cipherName) {
        this.cipherName = cipherName;
    }

    /**
     * Cipher OID
     * 
     */
    public String getCipherId() {
        return cipherId;
    }

    /**
     * Cipher OID
     * 
     */
    public void setCipherId(String cipherId) {
        this.cipherId = cipherId;
    }

    /**
     * Cipher strength as defined on the device
     * 
     */
    public Cipher.CipherStrength getCipherStrength() {
        return cipherStrength;
    }

    /**
     * Cipher strength as defined on the device
     * 
     */
    public void setCipherStrength(Cipher.CipherStrength cipherStrength) {
        this.cipherStrength = cipherStrength;
    }

    /**
     * The TLS versions in which cipher is supported
     * 
     */
    public List<ProtocolVersion> getProtocolVersions() {
        return protocolVersions;
    }

    /**
     * The TLS versions in which cipher is supported
     * 
     */
    public void setProtocolVersions(List<ProtocolVersion> protocolVersions) {
        this.protocolVersions = protocolVersions;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getFipsCompatibility() {
        return fipsCompatibility;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setFipsCompatibility(Property.FeatureEnabled fipsCompatibility) {
        this.fipsCompatibility = fipsCompatibility;
    }


    /**
     * Cipher strength as defined on the device
     * 
     */
    public enum CipherStrength {

        @SerializedName("low")
        LOW("low"),
        @SerializedName("medium")
        MEDIUM("medium"),
        @SerializedName("high")
        HIGH("high");
        private final String value;
        private final static Map<String, Cipher.CipherStrength> CONSTANTS = new HashMap<String, Cipher.CipherStrength>();

        static {
            for (Cipher.CipherStrength c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        CipherStrength(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Cipher.CipherStrength fromValue(String value) {
            Cipher.CipherStrength constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
