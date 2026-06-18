
package com.hp.ws.cdm.certificate;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.SerializedName;

public enum CertificateFormat {

    @SerializedName("pem")
    PEM("pem"),
    @SerializedName("pkcs12")
    PKCS_12("pkcs12"),
    @SerializedName("pkcs7")
    PKCS_7("pkcs7"),
    @SerializedName("cer")
    CER("cer"),
    @SerializedName("der")
    DER("der");
    private final String value;
    private final static Map<String, CertificateFormat> CONSTANTS = new HashMap<String, CertificateFormat>();

    static {
        for (CertificateFormat c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    CertificateFormat(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String value() {
        return this.value;
    }

    public static CertificateFormat fromValue(String value) {
        CertificateFormat constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
