
package com.hp.ws.cdm;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * hint
 * <p>
 * 
 * 
 */
public class Hint {

    /**
     * by default, the service API definition explains which HTTP methods are allowed on this resource. This hint is recommended if the behavior doesn't match common patterns.
     * 
     */
    @SerializedName("method")
    @Expose
    private Hint.Method method;

    /**
     * by default, the service API definition explains which HTTP methods are allowed on this resource. This hint is recommended if the behavior doesn't match common patterns.
     * 
     */
    public Hint.Method getMethod() {
        return method;
    }

    /**
     * by default, the service API definition explains which HTTP methods are allowed on this resource. This hint is recommended if the behavior doesn't match common patterns.
     * 
     */
    public void setMethod(Hint.Method method) {
        this.method = method;
    }


    /**
     * by default, the service API definition explains which HTTP methods are allowed on this resource. This hint is recommended if the behavior doesn't match common patterns.
     * 
     */
    public enum Method {

        @SerializedName("get")
        GET("get"),
        @SerializedName("put")
        PUT("put"),
        @SerializedName("patch")
        PATCH("patch"),
        @SerializedName("post")
        POST("post"),
        @SerializedName("delete")
        DELETE("delete");
        private final String value;
        private final static Map<String, Hint.Method> CONSTANTS = new HashMap<String, Hint.Method>();

        static {
            for (Hint.Method c: values()) {
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

        public static Hint.Method fromValue(String value) {
            Hint.Method constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
