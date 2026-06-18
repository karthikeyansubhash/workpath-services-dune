
package com.hp.ws.cdm.security;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.SerializedName;


/**
 * Supported clients. Future revisions of this type may add new values.
 * 
 */
public enum SupportedClient {

    @SerializedName("com.hp.cdm.client.hpEws")
    COM_HP_CDM_CLIENT_HP_EWS("com.hp.cdm.client.hpEws"),
    @SerializedName("com.hp.cdm.client.hpFrontPanel")
    COM_HP_CDM_CLIENT_HP_FRONT_PANEL("com.hp.cdm.client.hpFrontPanel"),
    @SerializedName("com.hp.cdm.client.hpSoftware")
    COM_HP_CDM_CLIENT_HP_SOFTWARE("com.hp.cdm.client.hpSoftware");
    private final String value;
    private final static Map<String, SupportedClient> CONSTANTS = new HashMap<String, SupportedClient>();

    static {
        for (SupportedClient c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    SupportedClient(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String value() {
        return this.value;
    }

    public static SupportedClient fromValue(String value) {
        SupportedClient constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
