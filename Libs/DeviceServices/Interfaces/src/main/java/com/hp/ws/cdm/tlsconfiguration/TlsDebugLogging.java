
package com.hp.ws.cdm.tlsconfiguration;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;


/**
 * TLS debug configuration used for analysing field issues
 * 
 */
public class TlsDebugLogging {

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("enabled")
    @Expose
    private Property.FeatureEnabled enabled;
    /**
     * TLS debug type - basic or enhanced
     * 
     */
    @SerializedName("type")
    @Expose
    private TlsDebugLogging.Type type;

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

    /**
     * TLS debug type - basic or enhanced
     * 
     */
    public TlsDebugLogging.Type getType() {
        return type;
    }

    /**
     * TLS debug type - basic or enhanced
     * 
     */
    public void setType(TlsDebugLogging.Type type) {
        this.type = type;
    }


    /**
     * TLS debug type - basic or enhanced
     * 
     */
    public enum Type {

        @SerializedName("basic")
        BASIC("basic"),
        @SerializedName("enhanced")
        ENHANCED("enhanced");
        private final String value;
        private final static Map<String, TlsDebugLogging.Type> CONSTANTS = new HashMap<String, TlsDebugLogging.Type>();

        static {
            for (TlsDebugLogging.Type c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Type(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static TlsDebugLogging.Type fromValue(String value) {
            TlsDebugLogging.Type constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
