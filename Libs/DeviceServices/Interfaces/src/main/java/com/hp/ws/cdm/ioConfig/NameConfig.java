
package com.hp.ws.cdm.ioconfig;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * DNS host/domain name
 * 
 */
public class NameConfig {

    /**
     * Configuration methods for dns, hostname, etc
     * 
     */
    @SerializedName("configMethod")
    @Expose
    private NameConfig.ConfigMethods configMethod;
    @SerializedName("name")
    @Expose
    private String name;

    /**
     * Configuration methods for dns, hostname, etc
     * 
     */
    public NameConfig.ConfigMethods getConfigMethod() {
        return configMethod;
    }

    /**
     * Configuration methods for dns, hostname, etc
     * 
     */
    public void setConfigMethod(NameConfig.ConfigMethods configMethod) {
        this.configMethod = configMethod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    /**
     * Configuration methods for dns, hostname, etc
     * 
     */
    public enum ConfigMethods {

        @SerializedName("notConfigured")
        NOT_CONFIGURED("notConfigured"),
        @SerializedName("automatic")
        AUTOMATIC("automatic"),
        @SerializedName("manual")
        MANUAL("manual");
        private final String value;
        private final static Map<String, NameConfig.ConfigMethods> CONSTANTS = new HashMap<String, NameConfig.ConfigMethods>();

        static {
            for (NameConfig.ConfigMethods c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        ConfigMethods(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static NameConfig.ConfigMethods fromValue(String value) {
            NameConfig.ConfigMethods constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
