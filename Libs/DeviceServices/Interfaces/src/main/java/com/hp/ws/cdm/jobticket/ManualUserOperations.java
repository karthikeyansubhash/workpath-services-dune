
package com.hp.ws.cdm.jobticket;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class ManualUserOperations {

    /**
     * Job operation like scan, will do the image preview based on enabled, disabled and optional.
     * 
     */
    @SerializedName("imagePreviewConfiguration")
    @Expose
    private ImagePreviewConfiguration imagePreviewConfiguration;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("autoRelease")
    @Expose
    private Property.FeatureEnabled autoRelease;

    /**
     * Job operation like scan, will do the image preview based on enabled, disabled and optional.
     * 
     */
    public ImagePreviewConfiguration getImagePreviewConfiguration() {
        return imagePreviewConfiguration;
    }

    /**
     * Job operation like scan, will do the image preview based on enabled, disabled and optional.
     * 
     */
    public void setImagePreviewConfiguration(ImagePreviewConfiguration imagePreviewConfiguration) {
        this.imagePreviewConfiguration = imagePreviewConfiguration;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getAutoRelease() {
        return autoRelease;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setAutoRelease(Property.FeatureEnabled autoRelease) {
        this.autoRelease = autoRelease;
    }


    /**
     * Job operation like scan, will do the image preview based on enabled, disabled and optional.
     * 
     */
    public enum ImagePreviewConfiguration {

        @SerializedName("disable")
        DISABLE("disable"),
        @SerializedName("enable")
        ENABLE("enable"),
        @SerializedName("optional")
        OPTIONAL("optional");
        private final String value;
        private final static Map<String, ImagePreviewConfiguration> CONSTANTS = new HashMap<String, ImagePreviewConfiguration>();

        static {
            for (ImagePreviewConfiguration c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        ImagePreviewConfiguration(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static ImagePreviewConfiguration fromValue(String value) {
            ImagePreviewConfiguration constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
