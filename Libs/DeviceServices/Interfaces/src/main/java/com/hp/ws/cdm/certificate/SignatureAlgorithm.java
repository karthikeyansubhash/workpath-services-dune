
package com.hp.ws.cdm.certificate;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.SerializedName;

public enum SignatureAlgorithm {

    @SerializedName("sha1WithRsa")
    SHA_1_WITH_RSA("sha1WithRsa"),
    @SerializedName("sha224WithRsa")
    SHA_224_WITH_RSA("sha224WithRsa"),
    @SerializedName("sha256WithRsa")
    SHA_256_WITH_RSA("sha256WithRsa"),
    @SerializedName("sha384WithRsa")
    SHA_384_WITH_RSA("sha384WithRsa"),
    @SerializedName("sha512WithRsa")
    SHA_512_WITH_RSA("sha512WithRsa"),
    @SerializedName("sha1WithEcdsa")
    SHA_1_WITH_ECDSA("sha1WithEcdsa"),
    @SerializedName("sha224WithEcdsa")
    SHA_224_WITH_ECDSA("sha224WithEcdsa"),
    @SerializedName("sha256WithEcdsa")
    SHA_256_WITH_ECDSA("sha256WithEcdsa"),
    @SerializedName("sha384WithEcdsa")
    SHA_384_WITH_ECDSA("sha384WithEcdsa"),
    @SerializedName("sha512WithEcdsa")
    SHA_512_WITH_ECDSA("sha512WithEcdsa");
    private final String value;
    private final static Map<String, SignatureAlgorithm> CONSTANTS = new HashMap<String, SignatureAlgorithm>();

    static {
        for (SignatureAlgorithm c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    SignatureAlgorithm(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String value() {
        return this.value;
    }

    public static SignatureAlgorithm fromValue(String value) {
        SignatureAlgorithm constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
