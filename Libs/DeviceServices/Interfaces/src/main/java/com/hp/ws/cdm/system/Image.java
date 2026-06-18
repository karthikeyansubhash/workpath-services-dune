
package com.hp.ws.cdm.system;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image {

    /**
     * system image type
     * (Required)
     * 
     */
    @SerializedName("imageType")
    @Expose
    private Image.ImageType imageType;
    /**
     * URL of the image
     * (Required)
     * 
     */
    @SerializedName("uri")
    @Expose
    private URI uri;

    /**
     * system image type
     * (Required)
     * 
     */
    public Image.ImageType getImageType() {
        return imageType;
    }

    /**
     * system image type
     * (Required)
     * 
     */
    public void setImageType(Image.ImageType imageType) {
        this.imageType = imageType;
    }

    /**
     * URL of the image
     * (Required)
     * 
     */
    public URI getUri() {
        return uri;
    }

    /**
     * URL of the image
     * (Required)
     * 
     */
    public void setUri(URI uri) {
        this.uri = uri;
    }


    /**
     * system image type
     * 
     */
    public enum ImageType {

        @SerializedName("printerIconSmall")
        PRINTER_ICON_SMALL("printerIconSmall"),
        @SerializedName("printerIconMedium")
        PRINTER_ICON_MEDIUM("printerIconMedium"),
        @SerializedName("printerIconLarge")
        PRINTER_ICON_LARGE("printerIconLarge");
        private final String value;
        private final static Map<String, Image.ImageType> CONSTANTS = new HashMap<String, Image.ImageType>();

        static {
            for (Image.ImageType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        ImageType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Image.ImageType fromValue(String value) {
            Image.ImageType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
