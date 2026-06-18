
package com.hp.ws.cdm.controlpanel;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.SerializedName;


/**
 * Image formats
 * 
 */
public enum ImageFormat {

    @SerializedName("lottie")
    LOTTIE("lottie"),
    @SerializedName("png")
    PNG("png"),
    @SerializedName("jpg")
    JPG("jpg");
    private final String value;
    private final static Map<String, ImageFormat> CONSTANTS = new HashMap<String, ImageFormat>();

    static {
        for (ImageFormat c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    ImageFormat(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String value() {
        return this.value;
    }

    public static ImageFormat fromValue(String value) {
        ImageFormat constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
