
package com.hp.ws.cdm.certificate;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.SerializedName;

public enum KeyStrength {

    @SerializedName("bits1024")
    BITS_1024("bits1024"),
    @SerializedName("bits2048")
    BITS_2048("bits2048"),
    @SerializedName("bits3072")
    BITS_3072("bits3072"),
    @SerializedName("bits4096")
    BITS_4096("bits4096"),
    @SerializedName("bits8192")
    BITS_8192("bits8192"),
    @SerializedName("bits256")
    BITS_256("bits256"),
    @SerializedName("bits384")
    BITS_384("bits384"),
    @SerializedName("bits521")
    BITS_521("bits521");
    private final String value;
    private final static Map<String, KeyStrength> CONSTANTS = new HashMap<String, KeyStrength>();

    static {
        for (KeyStrength c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    KeyStrength(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String value() {
        return this.value;
    }

    public static KeyStrength fromValue(String value) {
        KeyStrength constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
