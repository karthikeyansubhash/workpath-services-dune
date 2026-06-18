
package com.hp.ws.cdm.controlpanel;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.SerializedName;


/**
 * Hard keys
 * 
 */
public enum HardKey {

    @SerializedName("home")
    HOME("home"),
    @SerializedName("back")
    BACK("back"),
    @SerializedName("help")
    HELP("help"),
    @SerializedName("start")
    START("start");
    private final String value;
    private final static Map<String, HardKey> CONSTANTS = new HashMap<String, HardKey>();

    static {
        for (HardKey c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    HardKey(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String value() {
        return this.value;
    }

    public static HardKey fromValue(String value) {
        HardKey constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
