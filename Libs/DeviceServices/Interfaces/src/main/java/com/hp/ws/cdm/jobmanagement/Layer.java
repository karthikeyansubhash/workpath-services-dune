
package com.hp.ws.cdm.jobmanagement;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Layer {

    /**
     * unique identifier for the layer
     * 
     */
    @SerializedName("layerId")
    @Expose
    private Layer.LayerId layerId;
    @SerializedName("highResImage")
    @Expose
    private ImageDescriptor highResImage;
    @SerializedName("lowResImage")
    @Expose
    private ImageDescriptor lowResImage;

    /**
     * unique identifier for the layer
     * 
     */
    public Layer.LayerId getLayerId() {
        return layerId;
    }

    /**
     * unique identifier for the layer
     * 
     */
    public void setLayerId(Layer.LayerId layerId) {
        this.layerId = layerId;
    }

    public ImageDescriptor getHighResImage() {
        return highResImage;
    }

    public void setHighResImage(ImageDescriptor highResImage) {
        this.highResImage = highResImage;
    }

    public ImageDescriptor getLowResImage() {
        return lowResImage;
    }

    public void setLowResImage(ImageDescriptor lowResImage) {
        this.lowResImage = lowResImage;
    }


    /**
     * unique identifier for the layer
     * 
     */
    public enum LayerId {

        @SerializedName("cmykFront")
        CMYK_FRONT("cmykFront"),
        @SerializedName("white")
        WHITE("white"),
        @SerializedName("cmykBack")
        CMYK_BACK("cmykBack");
        private final String value;
        private final static Map<String, Layer.LayerId> CONSTANTS = new HashMap<String, Layer.LayerId>();

        static {
            for (Layer.LayerId c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        LayerId(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Layer.LayerId fromValue(String value) {
            Layer.LayerId constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
