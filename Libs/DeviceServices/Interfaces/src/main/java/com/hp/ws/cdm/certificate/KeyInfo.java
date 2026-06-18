
package com.hp.ws.cdm.certificate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KeyInfo {

    @SerializedName("keyType")
    @Expose
    private com.hp.ws.cdm.certificate.KeyInfoCapabilities.KeyType keyType;
    @SerializedName("keyStrength")
    @Expose
    private KeyStrength keyStrength;
    @SerializedName("modulus")
    @Expose
    private String modulus;
    @SerializedName("exponent")
    @Expose
    private String exponent;

    public com.hp.ws.cdm.certificate.KeyInfoCapabilities.KeyType getKeyType() {
        return keyType;
    }

    public void setKeyType(com.hp.ws.cdm.certificate.KeyInfoCapabilities.KeyType keyType) {
        this.keyType = keyType;
    }

    public KeyStrength getKeyStrength() {
        return keyStrength;
    }

    public void setKeyStrength(KeyStrength keyStrength) {
        this.keyStrength = keyStrength;
    }

    public String getModulus() {
        return modulus;
    }

    public void setModulus(String modulus) {
        this.modulus = modulus;
    }

    public String getExponent() {
        return exponent;
    }

    public void setExponent(String exponent) {
        this.exponent = exponent;
    }

}
