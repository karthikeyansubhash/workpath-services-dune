
package com.hp.ws.cdm.security;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.SerializedName;

public enum SupportedClaimAliases {

    @SerializedName("com.hp.cdm.auth.alias.deviceRole.public")
    COM_HP_CDM_AUTH_ALIAS_DEVICE_ROLE_PUBLIC("com.hp.cdm.auth.alias.deviceRole.public"),
    @SerializedName("com.hp.cdm.auth.alias.deviceRole.deviceAdmin")
    COM_HP_CDM_AUTH_ALIAS_DEVICE_ROLE_DEVICE_ADMIN("com.hp.cdm.auth.alias.deviceRole.deviceAdmin"),
    @SerializedName("com.hp.cdm.auth.alias.deviceRole.walkUpApp")
    COM_HP_CDM_AUTH_ALIAS_DEVICE_ROLE_WALK_UP_APP("com.hp.cdm.auth.alias.deviceRole.walkUpApp"),
    @SerializedName("com.hp.cdm.auth.alias.deviceRole.hpCloudService")
    COM_HP_CDM_AUTH_ALIAS_DEVICE_ROLE_HP_CLOUD_SERVICE("com.hp.cdm.auth.alias.deviceRole.hpCloudService"),
    @SerializedName("com.hp.cdm.auth.alias.deviceRole.solutionProvider")
    COM_HP_CDM_AUTH_ALIAS_DEVICE_ROLE_SOLUTION_PROVIDER("com.hp.cdm.auth.alias.deviceRole.solutionProvider");
    private final String value;
    private final static Map<String, SupportedClaimAliases> CONSTANTS = new HashMap<String, SupportedClaimAliases>();

    static {
        for (SupportedClaimAliases c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    SupportedClaimAliases(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String value() {
        return this.value;
    }

    public static SupportedClaimAliases fromValue(String value) {
        SupportedClaimAliases constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
