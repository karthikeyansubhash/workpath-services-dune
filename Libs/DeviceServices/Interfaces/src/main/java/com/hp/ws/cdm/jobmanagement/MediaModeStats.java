
package com.hp.ws.cdm.jobmanagement;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;


/**
 * this is print mode definition for LFP stats, printMode can vary across technologies
 * 
 */
public class MediaModeStats {

    /**
     * user friendly name of the print settings used in the job
     * 
     */
    @SerializedName("mediaModeName")
    @Expose
    private String mediaModeName;
    /**
     * user friendly description of the print settings used in the job
     * 
     */
    @SerializedName("mediaModeDescription")
    @Expose
    private String mediaModeDescription;
    @SerializedName("colorMode")
    @Expose
    private MediaModeStats.MediaColorMode colorMode = MediaModeStats.MediaColorMode.fromValue("unknown");
    @SerializedName("inkDensity")
    @Expose
    private Integer inkDensity;
    @SerializedName("inkDensityTopLayer")
    @Expose
    private Integer inkDensityTopLayer;
    @SerializedName("numberOfPasses")
    @Expose
    private Integer numberOfPasses;
    @SerializedName("whiteInkDensity")
    @Expose
    private Integer whiteInkDensity;
    @SerializedName("whiteMode")
    @Expose
    private MediaModeStats.WhiteMode whiteMode = MediaModeStats.WhiteMode.fromValue("notAvailable");
    /**
     * The ink drying (curing) temperature, (from 7000 to 11500 hundredths of celsius degrees)
     * 
     */
    @SerializedName("curingTemperature")
    @Expose
    private Integer curingTemperature;
    /**
     * Customizable values between 0 - 1550 ms
     * 
     */
    @SerializedName("interPassDelayOffset")
    @Expose
    private Integer interPassDelayOffset;
    /**
     * Customizable values between -10 to +10  mm/m
     * 
     */
    @SerializedName("mediaAdvanceFactor")
    @Expose
    private Integer mediaAdvanceFactor;
    /**
     * Level of overcoat agent to be applied. from 0 to 300 hundredths of drops.
     * 
     */
    @SerializedName("overcoat")
    @Expose
    private Integer overcoat;
    /**
     * units cm/s
     * 
     */
    @SerializedName("printZoneAirflow")
    @Expose
    private Integer printZoneAirflow;
    /**
     * Customizable values between 3700 - 4500 hundredths of degrees celsius
     * 
     */
    @SerializedName("printZoneTemperature")
    @Expose
    private Integer printZoneTemperature;
    /**
     * Vacuum pressure that attracts media to print zone. from 0 to 100 mmH2O
     * 
     */
    @SerializedName("vacuum")
    @Expose
    private Integer vacuum;
    /**
     * horizontal resolution of the printmode in DPI
     * 
     */
    @SerializedName("horizontalResolution")
    @Expose
    private Integer horizontalResolution;
    /**
     * vertical resolution of the printmode in DPI
     * 
     */
    @SerializedName("verticalResolution")
    @Expose
    private Integer verticalResolution;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("bidirectional")
    @Expose
    private Property.FeatureEnabled bidirectional;
    /**
     * carriage speed in 100th of inches per second
     * 
     */
    @SerializedName("carriageSpeed")
    @Expose
    private Integer carriageSpeed;

    /**
     * user friendly name of the print settings used in the job
     * 
     */
    public String getMediaModeName() {
        return mediaModeName;
    }

    /**
     * user friendly name of the print settings used in the job
     * 
     */
    public void setMediaModeName(String mediaModeName) {
        this.mediaModeName = mediaModeName;
    }

    /**
     * user friendly description of the print settings used in the job
     * 
     */
    public String getMediaModeDescription() {
        return mediaModeDescription;
    }

    /**
     * user friendly description of the print settings used in the job
     * 
     */
    public void setMediaModeDescription(String mediaModeDescription) {
        this.mediaModeDescription = mediaModeDescription;
    }

    public MediaModeStats.MediaColorMode getColorMode() {
        return colorMode;
    }

    public void setColorMode(MediaModeStats.MediaColorMode colorMode) {
        this.colorMode = colorMode;
    }

    public Integer getInkDensity() {
        return inkDensity;
    }

    public void setInkDensity(Integer inkDensity) {
        this.inkDensity = inkDensity;
    }

    public Integer getInkDensityTopLayer() {
        return inkDensityTopLayer;
    }

    public void setInkDensityTopLayer(Integer inkDensityTopLayer) {
        this.inkDensityTopLayer = inkDensityTopLayer;
    }

    public Integer getNumberOfPasses() {
        return numberOfPasses;
    }

    public void setNumberOfPasses(Integer numberOfPasses) {
        this.numberOfPasses = numberOfPasses;
    }

    public Integer getWhiteInkDensity() {
        return whiteInkDensity;
    }

    public void setWhiteInkDensity(Integer whiteInkDensity) {
        this.whiteInkDensity = whiteInkDensity;
    }

    public MediaModeStats.WhiteMode getWhiteMode() {
        return whiteMode;
    }

    public void setWhiteMode(MediaModeStats.WhiteMode whiteMode) {
        this.whiteMode = whiteMode;
    }

    /**
     * The ink drying (curing) temperature, (from 7000 to 11500 hundredths of celsius degrees)
     * 
     */
    public Integer getCuringTemperature() {
        return curingTemperature;
    }

    /**
     * The ink drying (curing) temperature, (from 7000 to 11500 hundredths of celsius degrees)
     * 
     */
    public void setCuringTemperature(Integer curingTemperature) {
        this.curingTemperature = curingTemperature;
    }

    /**
     * Customizable values between 0 - 1550 ms
     * 
     */
    public Integer getInterPassDelayOffset() {
        return interPassDelayOffset;
    }

    /**
     * Customizable values between 0 - 1550 ms
     * 
     */
    public void setInterPassDelayOffset(Integer interPassDelayOffset) {
        this.interPassDelayOffset = interPassDelayOffset;
    }

    /**
     * Customizable values between -10 to +10  mm/m
     * 
     */
    public Integer getMediaAdvanceFactor() {
        return mediaAdvanceFactor;
    }

    /**
     * Customizable values between -10 to +10  mm/m
     * 
     */
    public void setMediaAdvanceFactor(Integer mediaAdvanceFactor) {
        this.mediaAdvanceFactor = mediaAdvanceFactor;
    }

    /**
     * Level of overcoat agent to be applied. from 0 to 300 hundredths of drops.
     * 
     */
    public Integer getOvercoat() {
        return overcoat;
    }

    /**
     * Level of overcoat agent to be applied. from 0 to 300 hundredths of drops.
     * 
     */
    public void setOvercoat(Integer overcoat) {
        this.overcoat = overcoat;
    }

    /**
     * units cm/s
     * 
     */
    public Integer getPrintZoneAirflow() {
        return printZoneAirflow;
    }

    /**
     * units cm/s
     * 
     */
    public void setPrintZoneAirflow(Integer printZoneAirflow) {
        this.printZoneAirflow = printZoneAirflow;
    }

    /**
     * Customizable values between 3700 - 4500 hundredths of degrees celsius
     * 
     */
    public Integer getPrintZoneTemperature() {
        return printZoneTemperature;
    }

    /**
     * Customizable values between 3700 - 4500 hundredths of degrees celsius
     * 
     */
    public void setPrintZoneTemperature(Integer printZoneTemperature) {
        this.printZoneTemperature = printZoneTemperature;
    }

    /**
     * Vacuum pressure that attracts media to print zone. from 0 to 100 mmH2O
     * 
     */
    public Integer getVacuum() {
        return vacuum;
    }

    /**
     * Vacuum pressure that attracts media to print zone. from 0 to 100 mmH2O
     * 
     */
    public void setVacuum(Integer vacuum) {
        this.vacuum = vacuum;
    }

    /**
     * horizontal resolution of the printmode in DPI
     * 
     */
    public Integer getHorizontalResolution() {
        return horizontalResolution;
    }

    /**
     * horizontal resolution of the printmode in DPI
     * 
     */
    public void setHorizontalResolution(Integer horizontalResolution) {
        this.horizontalResolution = horizontalResolution;
    }

    /**
     * vertical resolution of the printmode in DPI
     * 
     */
    public Integer getVerticalResolution() {
        return verticalResolution;
    }

    /**
     * vertical resolution of the printmode in DPI
     * 
     */
    public void setVerticalResolution(Integer verticalResolution) {
        this.verticalResolution = verticalResolution;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getBidirectional() {
        return bidirectional;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setBidirectional(Property.FeatureEnabled bidirectional) {
        this.bidirectional = bidirectional;
    }

    /**
     * carriage speed in 100th of inches per second
     * 
     */
    public Integer getCarriageSpeed() {
        return carriageSpeed;
    }

    /**
     * carriage speed in 100th of inches per second
     * 
     */
    public void setCarriageSpeed(Integer carriageSpeed) {
        this.carriageSpeed = carriageSpeed;
    }

    public enum MediaColorMode {

        @SerializedName("unknown")
        UNKNOWN("unknown"),
        @SerializedName("cmyk")
        CMYK("cmyk"),
        @SerializedName("cmykLiteS")
        CMYK_LITE_S("cmykLiteS"),
        @SerializedName("cmykLiteSw")
        CMYK_LITE_SW("cmykLiteSw"),
        @SerializedName("sandwich")
        SANDWICH("sandwich"),
        @SerializedName("clc")
        CLC("clc");
        private final String value;
        private final static Map<String, MediaModeStats.MediaColorMode> CONSTANTS = new HashMap<String, MediaModeStats.MediaColorMode>();

        static {
            for (MediaModeStats.MediaColorMode c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        MediaColorMode(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static MediaModeStats.MediaColorMode fromValue(String value) {
            MediaModeStats.MediaColorMode constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum WhiteMode {

        @SerializedName("notAvailable")
        NOT_AVAILABLE("notAvailable"),
        @SerializedName("underflood")
        UNDERFLOOD("underflood"),
        @SerializedName("overflood")
        OVERFLOOD("overflood"),
        @SerializedName("sandwich3Layers")
        SANDWICH_3_LAYERS("sandwich3Layers"),
        @SerializedName("sandwich4Layers")
        SANDWICH_4_LAYERS("sandwich4Layers"),
        @SerializedName("sandwich5Layers")
        SANDWICH_5_LAYERS("sandwich5Layers"),
        @SerializedName("spot")
        SPOT("spot");
        private final String value;
        private final static Map<String, MediaModeStats.WhiteMode> CONSTANTS = new HashMap<String, MediaModeStats.WhiteMode>();

        static {
            for (MediaModeStats.WhiteMode c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        WhiteMode(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static MediaModeStats.WhiteMode fromValue(String value) {
            MediaModeStats.WhiteMode constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
