
package com.hp.ws.cdm.tlsconfiguration;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.SerializedName;

public enum ProtocolVersion {

    @SerializedName("tls1_0")
    TLS_1_0("tls1_0"),
    @SerializedName("tls1_1")
    TLS_1_1("tls1_1"),
    @SerializedName("tls1_2")
    TLS_1_2("tls1_2"),
    @SerializedName("tls1_3")
    TLS_1_3("tls1_3");
    private final String value;
    private final static Map<String, ProtocolVersion> CONSTANTS = new HashMap<String, ProtocolVersion>();

    static {
        for (ProtocolVersion c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    ProtocolVersion(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String value() {
        return this.value;
    }

    public static ProtocolVersion fromValue(String value) {
        ProtocolVersion constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
