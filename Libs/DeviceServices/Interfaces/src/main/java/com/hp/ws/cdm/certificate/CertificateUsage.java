
package com.hp.ws.cdm.certificate;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.SerializedName;


/**
 * certificate Usage options.
 * 
 */
public enum CertificateUsage {

    @SerializedName("none")
    NONE("none"),
    @SerializedName("networkIdentity")
    NETWORK_IDENTITY("networkIdentity"),
    @SerializedName("emailSigning")
    EMAIL_SIGNING("emailSigning");
    private final String value;
    private final static Map<String, CertificateUsage> CONSTANTS = new HashMap<String, CertificateUsage>();

    static {
        for (CertificateUsage c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    CertificateUsage(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String value() {
        return this.value;
    }

    public static CertificateUsage fromValue(String value) {
        CertificateUsage constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
