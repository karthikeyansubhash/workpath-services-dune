
package com.hp.ws.cdm.certificate;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.SerializedName;

public enum ExtendedKeyUsageType {

    @SerializedName("serverAuthentication")
    SERVER_AUTHENTICATION("serverAuthentication"),
    @SerializedName("clientAuthentication")
    CLIENT_AUTHENTICATION("clientAuthentication"),
    @SerializedName("codeSigning")
    CODE_SIGNING("codeSigning"),
    @SerializedName("secureEmail")
    SECURE_EMAIL("secureEmail"),
    @SerializedName("timeStamping")
    TIME_STAMPING("timeStamping"),
    @SerializedName("ocspSigning")
    OCSP_SIGNING("ocspSigning"),
    @SerializedName("ipsecEndToEnd")
    IPSEC_END_TO_END("ipsecEndToEnd"),
    @SerializedName("ipsecTunnel")
    IPSEC_TUNNEL("ipsecTunnel"),
    @SerializedName("ipsecUser")
    IPSEC_USER("ipsecUser"),
    @SerializedName("msComCodeSign")
    MS_COM_CODE_SIGN("msComCodeSign"),
    @SerializedName("msEncFilesys")
    MS_ENC_FILESYS("msEncFilesys"),
    @SerializedName("msIndCodeSign")
    MS_IND_CODE_SIGN("msIndCodeSign"),
    @SerializedName("msTrustSign")
    MS_TRUST_SIGN("msTrustSign"),
    @SerializedName("ipsecIk2")
    IPSEC_IK_2("ipsecIk2");
    private final String value;
    private final static Map<String, ExtendedKeyUsageType> CONSTANTS = new HashMap<String, ExtendedKeyUsageType>();

    static {
        for (ExtendedKeyUsageType c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    ExtendedKeyUsageType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String value() {
        return this.value;
    }

    public static ExtendedKeyUsageType fromValue(String value) {
        ExtendedKeyUsageType constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
