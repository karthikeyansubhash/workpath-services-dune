
package com.hp.ws.cdm.ioconfig;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Configuration methods supported for config Precedence
 * 
 */
public class ConfigPrecedenceMethod {

    @SerializedName("precedence")
    @Expose
    private Integer precedence;
    @SerializedName("method")
    @Expose
    private ConfigPrecedenceMethod.Method method;

    public Integer getPrecedence() {
        return precedence;
    }

    public void setPrecedence(Integer precedence) {
        this.precedence = precedence;
    }

    public ConfigPrecedenceMethod.Method getMethod() {
        return method;
    }

    public void setMethod(ConfigPrecedenceMethod.Method method) {
        this.method = method;
    }

    public enum Method {

        @SerializedName("manual")
        MANUAL("manual"),
        @SerializedName("tftp")
        TFTP("tftp"),
        @SerializedName("dhcpv4")
        DHCPV_4("dhcpv4"),
        @SerializedName("dhcpv6")
        DHCPV_6("dhcpv6"),
        @SerializedName("default")
        DEFAULT("default");
        private final String value;
        private final static Map<String, ConfigPrecedenceMethod.Method> CONSTANTS = new HashMap<String, ConfigPrecedenceMethod.Method>();

        static {
            for (ConfigPrecedenceMethod.Method c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Method(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static ConfigPrecedenceMethod.Method fromValue(String value) {
            ConfigPrecedenceMethod.Method constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
