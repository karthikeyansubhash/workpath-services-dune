
package com.hp.ws.cdm.jobticket;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class Scaling {

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("scaleToFitEnabled")
    @Expose
    private Property.FeatureEnabled scaleToFitEnabled;
    @SerializedName("xScalePercent")
    @Expose
    private Integer xScalePercent;
    @SerializedName("yScalePercent")
    @Expose
    private Integer yScalePercent;
    @SerializedName("scaleSelection")
    @Expose
    private ScaleSelection scaleSelection;
    /**
     * a unique ID used by this printer to identify this input sub-unit from PWG 5100.13
     * 
     */
    @SerializedName("scaleToOutput")
    @Expose
    private com.hp.ws.cdm.jobmanagement.MediaInput.MediaSourceId scaleToOutput;
    /**
     * Media size, values according to the standard PWG5101.1 or HP extension pattern for exceptions
     * 
     */
    @SerializedName("scaleToSize")
    @Expose
    private com.hp.ws.cdm.jobmanagement.MediaInput.MediaSize scaleToSize;

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getScaleToFitEnabled() {
        return scaleToFitEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setScaleToFitEnabled(Property.FeatureEnabled scaleToFitEnabled) {
        this.scaleToFitEnabled = scaleToFitEnabled;
    }

    public Integer getxScalePercent() {
        return xScalePercent;
    }

    public void setxScalePercent(Integer xScalePercent) {
        this.xScalePercent = xScalePercent;
    }

    public Integer getyScalePercent() {
        return yScalePercent;
    }

    public void setyScalePercent(Integer yScalePercent) {
        this.yScalePercent = yScalePercent;
    }

    public ScaleSelection getScaleSelection() {
        return scaleSelection;
    }

    public void setScaleSelection(ScaleSelection scaleSelection) {
        this.scaleSelection = scaleSelection;
    }

    /**
     * a unique ID used by this printer to identify this input sub-unit from PWG 5100.13
     * 
     */
    public com.hp.ws.cdm.jobmanagement.MediaInput.MediaSourceId getScaleToOutput() {
        return scaleToOutput;
    }

    /**
     * a unique ID used by this printer to identify this input sub-unit from PWG 5100.13
     * 
     */
    public void setScaleToOutput(com.hp.ws.cdm.jobmanagement.MediaInput.MediaSourceId scaleToOutput) {
        this.scaleToOutput = scaleToOutput;
    }

    /**
     * Media size, values according to the standard PWG5101.1 or HP extension pattern for exceptions
     * 
     */
    public com.hp.ws.cdm.jobmanagement.MediaInput.MediaSize getScaleToSize() {
        return scaleToSize;
    }

    /**
     * Media size, values according to the standard PWG5101.1 or HP extension pattern for exceptions
     * 
     */
    public void setScaleToSize(com.hp.ws.cdm.jobmanagement.MediaInput.MediaSize scaleToSize) {
        this.scaleToSize = scaleToSize;
    }

    public enum ScaleSelection {

        @SerializedName("none")
        NONE("none"),
        @SerializedName("custom")
        CUSTOM("custom"),
        @SerializedName("standardSizeScaling")
        STANDARD_SIZE_SCALING("standardSizeScaling"),
        @SerializedName("scaleToOutput")
        SCALE_TO_OUTPUT("scaleToOutput"),
        @SerializedName("fitToPage")
        FIT_TO_PAGE("fitToPage"),
        @SerializedName("fullPage")
        FULL_PAGE("fullPage"),
        @SerializedName("legalToLetter")
        LEGAL_TO_LETTER("legalToLetter"),
        @SerializedName("a4ToLetter")
        A_4_TO_LETTER("a4ToLetter"),
        @SerializedName("letterToA4")
        LETTER_TO_A_4("letterToA4");
        private final String value;
        private final static Map<String, ScaleSelection> CONSTANTS = new HashMap<String, ScaleSelection>();

        static {
            for (ScaleSelection c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        ScaleSelection(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static ScaleSelection fromValue(String value) {
            ScaleSelection constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
