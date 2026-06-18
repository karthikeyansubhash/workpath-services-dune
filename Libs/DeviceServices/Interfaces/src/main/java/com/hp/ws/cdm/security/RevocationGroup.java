
package com.hp.ws.cdm.security;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RevocationGroup {

    /**
     * The category of tokens to be revoked. Future revisions of this type may add new values. Clients must not supply a value not supported by the device.
     * (Required)
     * 
     */
    @SerializedName("category")
    @Expose
    private RevocationGroup.RevocationCategory category;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("state")
    @Expose
    private RevocationGroup.RevocationState state;

    /**
     * The category of tokens to be revoked. Future revisions of this type may add new values. Clients must not supply a value not supported by the device.
     * (Required)
     * 
     */
    public RevocationGroup.RevocationCategory getCategory() {
        return category;
    }

    /**
     * The category of tokens to be revoked. Future revisions of this type may add new values. Clients must not supply a value not supported by the device.
     * (Required)
     * 
     */
    public void setCategory(RevocationGroup.RevocationCategory category) {
        this.category = category;
    }

    /**
     * 
     * (Required)
     * 
     */
    public RevocationGroup.RevocationState getState() {
        return state;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setState(RevocationGroup.RevocationState state) {
        this.state = state;
    }


    /**
     * The category of tokens to be revoked. Future revisions of this type may add new values. Clients must not supply a value not supported by the device.
     * 
     */
    public enum RevocationCategory {

        @SerializedName("allPublicClientTokens")
        ALL_PUBLIC_CLIENT_TOKENS("allPublicClientTokens");
        private final String value;
        private final static Map<String, RevocationGroup.RevocationCategory> CONSTANTS = new HashMap<String, RevocationGroup.RevocationCategory>();

        static {
            for (RevocationGroup.RevocationCategory c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        RevocationCategory(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static RevocationGroup.RevocationCategory fromValue(String value) {
            RevocationGroup.RevocationCategory constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum RevocationState {

        @SerializedName("enabled")
        ENABLED("enabled"),
        @SerializedName("revoked")
        REVOKED("revoked");
        private final String value;
        private final static Map<String, RevocationGroup.RevocationState> CONSTANTS = new HashMap<String, RevocationGroup.RevocationState>();

        static {
            for (RevocationGroup.RevocationState c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        RevocationState(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static RevocationGroup.RevocationState fromValue(String value) {
            RevocationGroup.RevocationState constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
