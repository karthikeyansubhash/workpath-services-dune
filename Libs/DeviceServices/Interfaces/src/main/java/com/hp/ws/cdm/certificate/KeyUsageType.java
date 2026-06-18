
package com.hp.ws.cdm.certificate;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.SerializedName;


/**
 * Known flags for a Certificate's Key Usage.
 * 
 */
public enum KeyUsageType {

    @SerializedName("digitalSignatureKeyUsage")
    DIGITAL_SIGNATURE_KEY_USAGE("digitalSignatureKeyUsage"),
    @SerializedName("nonRepudiationKeyUsage")
    NON_REPUDIATION_KEY_USAGE("nonRepudiationKeyUsage"),
    @SerializedName("keyEnciphermentKeyUsage")
    KEY_ENCIPHERMENT_KEY_USAGE("keyEnciphermentKeyUsage"),
    @SerializedName("dataEnciphermentKeyUsage")
    DATA_ENCIPHERMENT_KEY_USAGE("dataEnciphermentKeyUsage"),
    @SerializedName("keyAgreementKeyUsage")
    KEY_AGREEMENT_KEY_USAGE("keyAgreementKeyUsage"),
    @SerializedName("keyCertSignKeyUsage")
    KEY_CERT_SIGN_KEY_USAGE("keyCertSignKeyUsage"),
    @SerializedName("certOfflineCrlSignKeyUsage")
    CERT_OFFLINE_CRL_SIGN_KEY_USAGE("certOfflineCrlSignKeyUsage"),
    @SerializedName("encipherOnlyKeyUsage")
    ENCIPHER_ONLY_KEY_USAGE("encipherOnlyKeyUsage"),
    @SerializedName("keyEnciphermentaoKeyUsage")
    KEY_ENCIPHERMENTAO_KEY_USAGE("keyEnciphermentaoKeyUsage"),
    @SerializedName("crlSign86KeyUsage")
    CRL_SIGN_86_KEY_USAGE("crlSign86KeyUsage"),
    @SerializedName("nonRepudiationC0KeyUsage")
    NON_REPUDIATION_C_0_KEY_USAGE("nonRepudiationC0KeyUsage"),
    @SerializedName("dataEnciphermentB0KeyUsage")
    DATA_ENCIPHERMENT_B_0_KEY_USAGE("dataEnciphermentB0KeyUsage"),
    @SerializedName("decipherOnlyKeyUsage")
    DECIPHER_ONLY_KEY_USAGE("decipherOnlyKeyUsage");
    private final String value;
    private final static Map<String, KeyUsageType> CONSTANTS = new HashMap<String, KeyUsageType>();

    static {
        for (KeyUsageType c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    KeyUsageType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String value() {
        return this.value;
    }

    public static KeyUsageType fromValue(String value) {
        KeyUsageType constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
