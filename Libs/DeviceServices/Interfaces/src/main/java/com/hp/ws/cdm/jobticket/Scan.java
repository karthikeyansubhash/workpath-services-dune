
package com.hp.ws.cdm.jobticket;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class Scan {

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("colorMode")
    @Expose
    private ColorModes colorMode;
    /**
     * Scan media source values. This values will be use in the jobTicket scan definition
     * 
     */
    @SerializedName("mediaSource")
    @Expose
    private com.hp.ws.cdm.jobmanagement.ScanInfo.ScanMediaSourceId mediaSource;
    /**
     * Media size, values according to the standard PWG5101.1 or HP extension pattern for exceptions
     * 
     */
    @SerializedName("mediaSize")
    @Expose
    private com.hp.ws.cdm.jobmanagement.MediaInput.MediaSize mediaSize;
    /**
     * Shift of image placement in x-direction in mm units
     * 
     */
    @SerializedName("xOffset")
    @Expose
    private Integer xOffset;
    /**
     * Shift of image placement in y-direction in mm units
     * 
     */
    @SerializedName("yOffset")
    @Expose
    private Integer yOffset;
    @SerializedName("plexMode")
    @Expose
    private com.hp.ws.cdm.jobmanagement.PrintSettings.PlexMode plexMode;
    @SerializedName("resolution")
    @Expose
    private Resolutions resolution;
    @SerializedName("pageBinding")
    @Expose
    private com.hp.ws.cdm.jobmanagement.ScanInfo.DuplexBinding pageBinding;
    @SerializedName("contentType")
    @Expose
    private ContentType contentType;
    /**
     * related to the orientation of the print content rendering on media
     * 
     */
    @SerializedName("contentOrientation")
    @Expose
    private ContentOrientation contentOrientation;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("pagesFlipUpEnabled")
    @Expose
    private Property.FeatureEnabled pagesFlipUpEnabled;
    @SerializedName("autoColorDetect")
    @Expose
    private AutoColorDetect autoColorDetect;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("blackBackground")
    @Expose
    private Property.FeatureEnabled blackBackground;
    /**
     * As mediaType is based on PWG, and so Print specific, scanMediaType includes the types supported by Scan
     * 
     */
    @SerializedName("mediaType")
    @Expose
    private ScanMediaType mediaType;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("autoExposure")
    @Expose
    private Property.FeatureEnabled autoExposure;
    @SerializedName("gamma")
    @Expose
    private Integer gamma;
    @SerializedName("highlight")
    @Expose
    private Integer highlight;
    @SerializedName("colorSensitivity")
    @Expose
    private Integer colorSensitivity;
    @SerializedName("colorRange")
    @Expose
    private Integer colorRange;
    @SerializedName("ccdChannel")
    @Expose
    private CcdChannel ccdChannel;
    @SerializedName("binaryRendering")
    @Expose
    private BinaryRendering binaryRendering;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("descreen")
    @Expose
    private Property.FeatureEnabled descreen;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("feederPickStop")
    @Expose
    private Property.FeatureEnabled feederPickStop;
    @SerializedName("shadow")
    @Expose
    private Integer shadow;
    @SerializedName("compressionFactor")
    @Expose
    private Integer compressionFactor;
    @SerializedName("threshold")
    @Expose
    private Integer threshold;
    @SerializedName("scanCaptureMode")
    @Expose
    private ScanCaptureMode scanCaptureMode;
    @SerializedName("scanAcquisitionsSpeed")
    @Expose
    private ScanAcquisitionsSpeed scanAcquisitionsSpeed;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("autoDeskew")
    @Expose
    private Property.FeatureEnabled autoDeskew;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("edgeToEdgeScan")
    @Expose
    private Property.FeatureEnabled edgeToEdgeScan;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("longPlotScan")
    @Expose
    private Property.FeatureEnabled longPlotScan;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("invertColors")
    @Expose
    private Property.FeatureEnabled invertColors;

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    public String getVersion() {
        return version;
    }

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    public void setVersion(String version) {
        this.version = version;
    }

    public ColorModes getColorMode() {
        return colorMode;
    }

    public void setColorMode(ColorModes colorMode) {
        this.colorMode = colorMode;
    }

    /**
     * Scan media source values. This values will be use in the jobTicket scan definition
     * 
     */
    public com.hp.ws.cdm.jobmanagement.ScanInfo.ScanMediaSourceId getMediaSource() {
        return mediaSource;
    }

    /**
     * Scan media source values. This values will be use in the jobTicket scan definition
     * 
     */
    public void setMediaSource(com.hp.ws.cdm.jobmanagement.ScanInfo.ScanMediaSourceId mediaSource) {
        this.mediaSource = mediaSource;
    }

    /**
     * Media size, values according to the standard PWG5101.1 or HP extension pattern for exceptions
     * 
     */
    public com.hp.ws.cdm.jobmanagement.MediaInput.MediaSize getMediaSize() {
        return mediaSize;
    }

    /**
     * Media size, values according to the standard PWG5101.1 or HP extension pattern for exceptions
     * 
     */
    public void setMediaSize(com.hp.ws.cdm.jobmanagement.MediaInput.MediaSize mediaSize) {
        this.mediaSize = mediaSize;
    }

    /**
     * Shift of image placement in x-direction in mm units
     * 
     */
    public Integer getxOffset() {
        return xOffset;
    }

    /**
     * Shift of image placement in x-direction in mm units
     * 
     */
    public void setxOffset(Integer xOffset) {
        this.xOffset = xOffset;
    }

    /**
     * Shift of image placement in y-direction in mm units
     * 
     */
    public Integer getyOffset() {
        return yOffset;
    }

    /**
     * Shift of image placement in y-direction in mm units
     * 
     */
    public void setyOffset(Integer yOffset) {
        this.yOffset = yOffset;
    }

    public com.hp.ws.cdm.jobmanagement.PrintSettings.PlexMode getPlexMode() {
        return plexMode;
    }

    public void setPlexMode(com.hp.ws.cdm.jobmanagement.PrintSettings.PlexMode plexMode) {
        this.plexMode = plexMode;
    }

    public Resolutions getResolution() {
        return resolution;
    }

    public void setResolution(Resolutions resolution) {
        this.resolution = resolution;
    }

    public com.hp.ws.cdm.jobmanagement.ScanInfo.DuplexBinding getPageBinding() {
        return pageBinding;
    }

    public void setPageBinding(com.hp.ws.cdm.jobmanagement.ScanInfo.DuplexBinding pageBinding) {
        this.pageBinding = pageBinding;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    /**
     * related to the orientation of the print content rendering on media
     * 
     */
    public ContentOrientation getContentOrientation() {
        return contentOrientation;
    }

    /**
     * related to the orientation of the print content rendering on media
     * 
     */
    public void setContentOrientation(ContentOrientation contentOrientation) {
        this.contentOrientation = contentOrientation;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getPagesFlipUpEnabled() {
        return pagesFlipUpEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setPagesFlipUpEnabled(Property.FeatureEnabled pagesFlipUpEnabled) {
        this.pagesFlipUpEnabled = pagesFlipUpEnabled;
    }

    public AutoColorDetect getAutoColorDetect() {
        return autoColorDetect;
    }

    public void setAutoColorDetect(AutoColorDetect autoColorDetect) {
        this.autoColorDetect = autoColorDetect;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getBlackBackground() {
        return blackBackground;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setBlackBackground(Property.FeatureEnabled blackBackground) {
        this.blackBackground = blackBackground;
    }

    /**
     * As mediaType is based on PWG, and so Print specific, scanMediaType includes the types supported by Scan
     * 
     */
    public ScanMediaType getMediaType() {
        return mediaType;
    }

    /**
     * As mediaType is based on PWG, and so Print specific, scanMediaType includes the types supported by Scan
     * 
     */
    public void setMediaType(ScanMediaType mediaType) {
        this.mediaType = mediaType;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getAutoExposure() {
        return autoExposure;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setAutoExposure(Property.FeatureEnabled autoExposure) {
        this.autoExposure = autoExposure;
    }

    public Integer getGamma() {
        return gamma;
    }

    public void setGamma(Integer gamma) {
        this.gamma = gamma;
    }

    public Integer getHighlight() {
        return highlight;
    }

    public void setHighlight(Integer highlight) {
        this.highlight = highlight;
    }

    public Integer getColorSensitivity() {
        return colorSensitivity;
    }

    public void setColorSensitivity(Integer colorSensitivity) {
        this.colorSensitivity = colorSensitivity;
    }

    public Integer getColorRange() {
        return colorRange;
    }

    public void setColorRange(Integer colorRange) {
        this.colorRange = colorRange;
    }

    public CcdChannel getCcdChannel() {
        return ccdChannel;
    }

    public void setCcdChannel(CcdChannel ccdChannel) {
        this.ccdChannel = ccdChannel;
    }

    public BinaryRendering getBinaryRendering() {
        return binaryRendering;
    }

    public void setBinaryRendering(BinaryRendering binaryRendering) {
        this.binaryRendering = binaryRendering;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getDescreen() {
        return descreen;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setDescreen(Property.FeatureEnabled descreen) {
        this.descreen = descreen;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getFeederPickStop() {
        return feederPickStop;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setFeederPickStop(Property.FeatureEnabled feederPickStop) {
        this.feederPickStop = feederPickStop;
    }

    public Integer getShadow() {
        return shadow;
    }

    public void setShadow(Integer shadow) {
        this.shadow = shadow;
    }

    public Integer getCompressionFactor() {
        return compressionFactor;
    }

    public void setCompressionFactor(Integer compressionFactor) {
        this.compressionFactor = compressionFactor;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    public ScanCaptureMode getScanCaptureMode() {
        return scanCaptureMode;
    }

    public void setScanCaptureMode(ScanCaptureMode scanCaptureMode) {
        this.scanCaptureMode = scanCaptureMode;
    }

    public ScanAcquisitionsSpeed getScanAcquisitionsSpeed() {
        return scanAcquisitionsSpeed;
    }

    public void setScanAcquisitionsSpeed(ScanAcquisitionsSpeed scanAcquisitionsSpeed) {
        this.scanAcquisitionsSpeed = scanAcquisitionsSpeed;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getAutoDeskew() {
        return autoDeskew;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setAutoDeskew(Property.FeatureEnabled autoDeskew) {
        this.autoDeskew = autoDeskew;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getEdgeToEdgeScan() {
        return edgeToEdgeScan;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setEdgeToEdgeScan(Property.FeatureEnabled edgeToEdgeScan) {
        this.edgeToEdgeScan = edgeToEdgeScan;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getLongPlotScan() {
        return longPlotScan;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setLongPlotScan(Property.FeatureEnabled longPlotScan) {
        this.longPlotScan = longPlotScan;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getInvertColors() {
        return invertColors;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setInvertColors(Property.FeatureEnabled invertColors) {
        this.invertColors = invertColors;
    }

    public enum AutoColorDetect {

        @SerializedName("detectOnly")
        DETECT_ONLY("detectOnly"),
        @SerializedName("treatNonColorAsBlackAndWhite1")
        TREAT_NON_COLOR_AS_BLACK_AND_WHITE_1("treatNonColorAsBlackAndWhite1"),
        @SerializedName("treatNonColorAsGrayscale8")
        TREAT_NON_COLOR_AS_GRAYSCALE_8("treatNonColorAsGrayscale8");
        private final String value;
        private final static Map<String, AutoColorDetect> CONSTANTS = new HashMap<String, AutoColorDetect>();

        static {
            for (AutoColorDetect c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        AutoColorDetect(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static AutoColorDetect fromValue(String value) {
            AutoColorDetect constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum BinaryRendering {

        @SerializedName("halftone")
        HALFTONE("halftone"),
        @SerializedName("threshold")
        THRESHOLD("threshold"),
        @SerializedName("errorDiffusion")
        ERROR_DIFFUSION("errorDiffusion");
        private final String value;
        private final static Map<String, BinaryRendering> CONSTANTS = new HashMap<String, BinaryRendering>();

        static {
            for (BinaryRendering c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        BinaryRendering(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static BinaryRendering fromValue(String value) {
            BinaryRendering constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum CcdChannel {

        @SerializedName("ntsc")
        NTSC("ntsc"),
        @SerializedName("grayCcd")
        GRAY_CCD("grayCcd"),
        @SerializedName("grayCcdEmulated")
        GRAY_CCD_EMULATED("grayCcdEmulated"),
        @SerializedName("red")
        RED("red"),
        @SerializedName("green")
        GREEN("green"),
        @SerializedName("blue")
        BLUE("blue");
        private final String value;
        private final static Map<String, CcdChannel> CONSTANTS = new HashMap<String, CcdChannel>();

        static {
            for (CcdChannel c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        CcdChannel(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static CcdChannel fromValue(String value) {
            CcdChannel constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum ColorModes {

        @SerializedName("color")
        COLOR("color"),
        @SerializedName("monochrome")
        MONOCHROME("monochrome"),
        @SerializedName("grayscale")
        GRAYSCALE("grayscale"),
        @SerializedName("autoDetect")
        AUTO_DETECT("autoDetect"),
        @SerializedName("blackAndWhite")
        BLACK_AND_WHITE("blackAndWhite"),
        @SerializedName("trueBlack")
        TRUE_BLACK("trueBlack"),
        @SerializedName("autoDetectColorOrBlack")
        AUTO_DETECT_COLOR_OR_BLACK("autoDetectColorOrBlack"),
        @SerializedName("autoDetectColorOrGrayscale")
        AUTO_DETECT_COLOR_OR_GRAYSCALE("autoDetectColorOrGrayscale"),
        @SerializedName("autoDetectGrayscaleOnly")
        AUTO_DETECT_GRAYSCALE_ONLY("autoDetectGrayscaleOnly");
        private final String value;
        private final static Map<String, ColorModes> CONSTANTS = new HashMap<String, ColorModes>();

        static {
            for (ColorModes c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        ColorModes(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static ColorModes fromValue(String value) {
            ColorModes constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * related to the orientation of the print content rendering on media
     * 
     */
    public enum ContentOrientation {

        @SerializedName("landscape")
        LANDSCAPE("landscape"),
        @SerializedName("portrait")
        PORTRAIT("portrait");
        private final String value;
        private final static Map<String, ContentOrientation> CONSTANTS = new HashMap<String, ContentOrientation>();

        static {
            for (ContentOrientation c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        ContentOrientation(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static ContentOrientation fromValue(String value) {
            ContentOrientation constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum ContentType {

        @SerializedName("glossy")
        GLOSSY("glossy"),
        @SerializedName("mixed")
        MIXED("mixed"),
        @SerializedName("photo")
        PHOTO("photo"),
        @SerializedName("text")
        TEXT("text"),
        @SerializedName("image")
        IMAGE("image"),
        @SerializedName("lineDrawing")
        LINE_DRAWING("lineDrawing"),
        @SerializedName("autoDetect")
        AUTO_DETECT("autoDetect");
        private final String value;
        private final static Map<String, ContentType> CONSTANTS = new HashMap<String, ContentType>();

        static {
            for (ContentType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        ContentType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static ContentType fromValue(String value) {
            ContentType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum Resolutions {

        @SerializedName("e75Dpi")
        E_75_DPI("e75Dpi"),
        @SerializedName("e100Dpi")
        E_100_DPI("e100Dpi"),
        @SerializedName("e150Dpi")
        E_150_DPI("e150Dpi"),
        @SerializedName("e200Dpi")
        E_200_DPI("e200Dpi"),
        @SerializedName("e240Dpi")
        E_240_DPI("e240Dpi"),
        @SerializedName("e300Dpi")
        E_300_DPI("e300Dpi"),
        @SerializedName("e400Dpi")
        E_400_DPI("e400Dpi"),
        @SerializedName("e500Dpi")
        E_500_DPI("e500Dpi"),
        @SerializedName("e600Dpi")
        E_600_DPI("e600Dpi"),
        @SerializedName("e1200Dpi")
        E_1200_DPI("e1200Dpi");
        private final String value;
        private final static Map<String, Resolutions> CONSTANTS = new HashMap<String, Resolutions>();

        static {
            for (Resolutions c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Resolutions(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Resolutions fromValue(String value) {
            Resolutions constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum ScanAcquisitionsSpeed {

        @SerializedName("slow")
        SLOW("slow"),
        @SerializedName("auto")
        AUTO("auto");
        private final String value;
        private final static Map<String, ScanAcquisitionsSpeed> CONSTANTS = new HashMap<String, ScanAcquisitionsSpeed>();

        static {
            for (ScanAcquisitionsSpeed c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        ScanAcquisitionsSpeed(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static ScanAcquisitionsSpeed fromValue(String value) {
            ScanAcquisitionsSpeed constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum ScanCaptureMode {

        @SerializedName("standard")
        STANDARD("standard"),
        @SerializedName("idCard")
        ID_CARD("idCard"),
        @SerializedName("bookMode")
        BOOK_MODE("bookMode"),
        @SerializedName("jobBuild")
        JOB_BUILD("jobBuild");
        private final String value;
        private final static Map<String, ScanCaptureMode> CONSTANTS = new HashMap<String, ScanCaptureMode>();

        static {
            for (ScanCaptureMode c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        ScanCaptureMode(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static ScanCaptureMode fromValue(String value) {
            ScanCaptureMode constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * As mediaType is based on PWG, and so Print specific, scanMediaType includes the types supported by Scan
     * 
     */
    public enum ScanMediaType {

        @SerializedName("blueprints")
        BLUEPRINTS("blueprints"),
        @SerializedName("darkBlueprints")
        DARK_BLUEPRINTS("darkBlueprints"),
        @SerializedName("oldRecycledPaper")
        OLD_RECYCLED_PAPER("oldRecycledPaper"),
        @SerializedName("object3d")
        OBJECT_3_D("object3d"),
        @SerializedName("photoPaper")
        PHOTO_PAPER("photoPaper"),
        @SerializedName("translucentPaper")
        TRANSLUCENT_PAPER("translucentPaper"),
        @SerializedName("whitePaper")
        WHITE_PAPER("whitePaper"),
        @SerializedName("whitePaperEnhanced")
        WHITE_PAPER_ENHANCED("whitePaperEnhanced"),
        @SerializedName("brochureMatte")
        BROCHURE_MATTE("brochureMatte"),
        @SerializedName("brochureGlossy")
        BROCHURE_GLOSSY("brochureGlossy");
        private final String value;
        private final static Map<String, ScanMediaType> CONSTANTS = new HashMap<String, ScanMediaType>();

        static {
            for (ScanMediaType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        ScanMediaType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static ScanMediaType fromValue(String value) {
            ScanMediaType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
