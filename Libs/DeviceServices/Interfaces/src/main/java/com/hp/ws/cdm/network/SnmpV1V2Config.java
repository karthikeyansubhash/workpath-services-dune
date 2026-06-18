
package com.hp.ws.cdm.network;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class SnmpV1V2Config {

    @SerializedName("accessOption")
    @Expose
    private SnmpV1V2Config.AccessOption accessOption;
    @SerializedName("contextName")
    @Expose
    private String contextName;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("enabled")
    @Expose
    private Property.FeatureEnabled enabled;
    @SerializedName("readOnlyCommunityName")
    @Expose
    private String readOnlyCommunityName;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("readOnlyCommunityNameSet")
    @Expose
    private Property.FeatureEnabled readOnlyCommunityNameSet;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("readOnlyPublicAllowed")
    @Expose
    private Property.FeatureEnabled readOnlyPublicAllowed;
    @SerializedName("writeOnlyCommunityName")
    @Expose
    private String writeOnlyCommunityName;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("writeOnlyCommunityNameSet")
    @Expose
    private Property.FeatureEnabled writeOnlyCommunityNameSet;

    public SnmpV1V2Config.AccessOption getAccessOption() {
        return accessOption;
    }

    public void setAccessOption(SnmpV1V2Config.AccessOption accessOption) {
        this.accessOption = accessOption;
    }

    public String getContextName() {
        return contextName;
    }

    public void setContextName(String contextName) {
        this.contextName = contextName;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getEnabled() {
        return enabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setEnabled(Property.FeatureEnabled enabled) {
        this.enabled = enabled;
    }

    public String getReadOnlyCommunityName() {
        return readOnlyCommunityName;
    }

    public void setReadOnlyCommunityName(String readOnlyCommunityName) {
        this.readOnlyCommunityName = readOnlyCommunityName;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getReadOnlyCommunityNameSet() {
        return readOnlyCommunityNameSet;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setReadOnlyCommunityNameSet(Property.FeatureEnabled readOnlyCommunityNameSet) {
        this.readOnlyCommunityNameSet = readOnlyCommunityNameSet;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getReadOnlyPublicAllowed() {
        return readOnlyPublicAllowed;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setReadOnlyPublicAllowed(Property.FeatureEnabled readOnlyPublicAllowed) {
        this.readOnlyPublicAllowed = readOnlyPublicAllowed;
    }

    public String getWriteOnlyCommunityName() {
        return writeOnlyCommunityName;
    }

    public void setWriteOnlyCommunityName(String writeOnlyCommunityName) {
        this.writeOnlyCommunityName = writeOnlyCommunityName;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getWriteOnlyCommunityNameSet() {
        return writeOnlyCommunityNameSet;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setWriteOnlyCommunityNameSet(Property.FeatureEnabled writeOnlyCommunityNameSet) {
        this.writeOnlyCommunityNameSet = writeOnlyCommunityNameSet;
    }

    public enum AccessOption {

        @SerializedName("readOnly")
        READ_ONLY("readOnly"),
        @SerializedName("readWrite")
        READ_WRITE("readWrite");
        private final String value;
        private final static Map<String, SnmpV1V2Config.AccessOption> CONSTANTS = new HashMap<String, SnmpV1V2Config.AccessOption>();

        static {
            for (SnmpV1V2Config.AccessOption c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        AccessOption(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static SnmpV1V2Config.AccessOption fromValue(String value) {
            SnmpV1V2Config.AccessOption constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
