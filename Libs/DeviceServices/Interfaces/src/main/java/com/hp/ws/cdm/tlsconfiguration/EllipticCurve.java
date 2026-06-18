
package com.hp.ws.cdm.tlsconfiguration;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.SerializedName;

public enum EllipticCurve {

    @SerializedName("p256")
    P_256("p256"),
    @SerializedName("p384")
    P_384("p384"),
    @SerializedName("p521")
    P_521("p521");
    private final String value;
    private final static Map<String, EllipticCurve> CONSTANTS = new HashMap<String, EllipticCurve>();

    static {
        for (EllipticCurve c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    EllipticCurve(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String value() {
        return this.value;
    }

    public static EllipticCurve fromValue(String value) {
        EllipticCurve constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
