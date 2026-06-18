
package com.hp.ws.cdm.jobmanagement;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;


/**
 * print settings (intent) used in the job
 * 
 */
public class PrintSettings {

    @SerializedName("colorMode")
    @Expose
    private PrintSettings.ColorModes colorMode;
    @SerializedName("econoMode")
    @Expose
    private String econoMode;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("inQuietMode")
    @Expose
    private Property.FeatureEnabled inQuietMode;
    /**
     * Type of margin used in the job
     * 
     */
    @SerializedName("marginsType")
    @Expose
    private PrintSettings.MarginsType marginsType;
    /**
     * various media properties that describe the media requested vs. used
     * 
     */
    @SerializedName("mediaRequested")
    @Expose
    private MediaRequested mediaRequested;
    @SerializedName("plexMode")
    @Expose
    private PrintSettings.PlexMode plexMode;
    /**
     * print quality value used in the Job
     * 
     */
    @SerializedName("printQuality")
    @Expose
    private PrintSettings.PrintQuality printQuality;
    @SerializedName("printResolution")
    @Expose
    private PrintSettings.Resolutions printResolution;
    /**
     * Display name for the printmode, i.e. 32p_6c_W_UF110
     * 
     */
    @SerializedName("printModeName")
    @Expose
    private String printModeName;
    /**
     * this is print mode definition for LFP stats, printMode can vary across technologies
     * 
     */
    @SerializedName("mediaModeStats")
    @Expose
    private MediaModeStats mediaModeStats;
    /**
     * number of copies requested in a copy job
     * 
     */
    @SerializedName("requestedCopiesCount")
    @Expose
    private Integer requestedCopiesCount;
    /**
     * number of impressions requested in current job
     * 
     */
    @SerializedName("requestedImpressionCount")
    @Expose
    private Integer requestedImpressionCount;
    @SerializedName("collate")
    @Expose
    private PrintSettings.CollateModes collate;
    /**
     * a unique ID used by this printer to identify this output sub-unit from PWG 5100.2-2001
     * 
     */
    @SerializedName("mediaOutputId")
    @Expose
    private PrintSettings.MediaDestinationId mediaOutputId;

    public PrintSettings.ColorModes getColorMode() {
        return colorMode;
    }

    public void setColorMode(PrintSettings.ColorModes colorMode) {
        this.colorMode = colorMode;
    }

    public String getEconoMode() {
        return econoMode;
    }

    public void setEconoMode(String econoMode) {
        this.econoMode = econoMode;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getInQuietMode() {
        return inQuietMode;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setInQuietMode(Property.FeatureEnabled inQuietMode) {
        this.inQuietMode = inQuietMode;
    }

    /**
     * Type of margin used in the job
     * 
     */
    public PrintSettings.MarginsType getMarginsType() {
        return marginsType;
    }

    /**
     * Type of margin used in the job
     * 
     */
    public void setMarginsType(PrintSettings.MarginsType marginsType) {
        this.marginsType = marginsType;
    }

    /**
     * various media properties that describe the media requested vs. used
     * 
     */
    public MediaRequested getMediaRequested() {
        return mediaRequested;
    }

    /**
     * various media properties that describe the media requested vs. used
     * 
     */
    public void setMediaRequested(MediaRequested mediaRequested) {
        this.mediaRequested = mediaRequested;
    }

    public PrintSettings.PlexMode getPlexMode() {
        return plexMode;
    }

    public void setPlexMode(PrintSettings.PlexMode plexMode) {
        this.plexMode = plexMode;
    }

    /**
     * print quality value used in the Job
     * 
     */
    public PrintSettings.PrintQuality getPrintQuality() {
        return printQuality;
    }

    /**
     * print quality value used in the Job
     * 
     */
    public void setPrintQuality(PrintSettings.PrintQuality printQuality) {
        this.printQuality = printQuality;
    }

    public PrintSettings.Resolutions getPrintResolution() {
        return printResolution;
    }

    public void setPrintResolution(PrintSettings.Resolutions printResolution) {
        this.printResolution = printResolution;
    }

    /**
     * Display name for the printmode, i.e. 32p_6c_W_UF110
     * 
     */
    public String getPrintModeName() {
        return printModeName;
    }

    /**
     * Display name for the printmode, i.e. 32p_6c_W_UF110
     * 
     */
    public void setPrintModeName(String printModeName) {
        this.printModeName = printModeName;
    }

    /**
     * this is print mode definition for LFP stats, printMode can vary across technologies
     * 
     */
    public MediaModeStats getMediaModeStats() {
        return mediaModeStats;
    }

    /**
     * this is print mode definition for LFP stats, printMode can vary across technologies
     * 
     */
    public void setMediaModeStats(MediaModeStats mediaModeStats) {
        this.mediaModeStats = mediaModeStats;
    }

    /**
     * number of copies requested in a copy job
     * 
     */
    public Integer getRequestedCopiesCount() {
        return requestedCopiesCount;
    }

    /**
     * number of copies requested in a copy job
     * 
     */
    public void setRequestedCopiesCount(Integer requestedCopiesCount) {
        this.requestedCopiesCount = requestedCopiesCount;
    }

    /**
     * number of impressions requested in current job
     * 
     */
    public Integer getRequestedImpressionCount() {
        return requestedImpressionCount;
    }

    /**
     * number of impressions requested in current job
     * 
     */
    public void setRequestedImpressionCount(Integer requestedImpressionCount) {
        this.requestedImpressionCount = requestedImpressionCount;
    }

    public PrintSettings.CollateModes getCollate() {
        return collate;
    }

    public void setCollate(PrintSettings.CollateModes collate) {
        this.collate = collate;
    }

    /**
     * a unique ID used by this printer to identify this output sub-unit from PWG 5100.2-2001
     * 
     */
    public PrintSettings.MediaDestinationId getMediaOutputId() {
        return mediaOutputId;
    }

    /**
     * a unique ID used by this printer to identify this output sub-unit from PWG 5100.2-2001
     * 
     */
    public void setMediaOutputId(PrintSettings.MediaDestinationId mediaOutputId) {
        this.mediaOutputId = mediaOutputId;
    }

    public enum CollateModes {

        @SerializedName("collated")
        COLLATED("collated"),
        @SerializedName("uncollated")
        UNCOLLATED("uncollated");
        private final String value;
        private final static Map<String, PrintSettings.CollateModes> CONSTANTS = new HashMap<String, PrintSettings.CollateModes>();

        static {
            for (PrintSettings.CollateModes c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        CollateModes(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static PrintSettings.CollateModes fromValue(String value) {
            PrintSettings.CollateModes constant = CONSTANTS.get(value);
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
        private final static Map<String, PrintSettings.ColorModes> CONSTANTS = new HashMap<String, PrintSettings.ColorModes>();

        static {
            for (PrintSettings.ColorModes c: values()) {
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

        public static PrintSettings.ColorModes fromValue(String value) {
            PrintSettings.ColorModes constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Type of margin used in the job
     * 
     */
    public enum MarginsType {

        @SerializedName("normal")
        NORMAL("normal"),
        @SerializedName("fullBleed")
        FULL_BLEED("fullBleed");
        private final String value;
        private final static Map<String, PrintSettings.MarginsType> CONSTANTS = new HashMap<String, PrintSettings.MarginsType>();

        static {
            for (PrintSettings.MarginsType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        MarginsType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static PrintSettings.MarginsType fromValue(String value) {
            PrintSettings.MarginsType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * a unique ID used by this printer to identify this output sub-unit from PWG 5100.2-2001
     * 
     */
    public enum MediaDestinationId {

        @SerializedName("auto")
        AUTO("auto"),
        @SerializedName("bin")
        BIN("bin"),
        @SerializedName("default")
        DEFAULT("default"),
        @SerializedName("folder-1")
        FOLDER_1("folder-1"),
        @SerializedName("folder-2")
        FOLDER_2("folder-2"),
        @SerializedName("folder-3")
        FOLDER_3("folder-3"),
        @SerializedName("folder-4")
        FOLDER_4("folder-4"),
        @SerializedName("generic-accessory")
        GENERIC_ACCESSORY("generic-accessory"),
        @SerializedName("horizontal-cutter")
        HORIZONTAL_CUTTER("horizontal-cutter"),
        @SerializedName("stacker-1")
        STACKER_1("stacker-1"),
        @SerializedName("stacker-2")
        STACKER_2("stacker-2"),
        @SerializedName("stacker-3")
        STACKER_3("stacker-3"),
        @SerializedName("stacker-4")
        STACKER_4("stacker-4"),
        @SerializedName("stacker-accessory")
        STACKER_ACCESSORY("stacker-accessory"),
        @SerializedName("standard-bin")
        STANDARD_BIN("standard-bin"),
        @SerializedName("take-up-reel")
        TAKE_UP_REEL("take-up-reel"),
        @SerializedName("tray-1")
        TRAY_1("tray-1"),
        @SerializedName("tray-2")
        TRAY_2("tray-2"),
        @SerializedName("tray-3")
        TRAY_3("tray-3"),
        @SerializedName("tray-4")
        TRAY_4("tray-4"),
        @SerializedName("tray-5")
        TRAY_5("tray-5"),
        @SerializedName("tray-6")
        TRAY_6("tray-6"),
        @SerializedName("tray-7")
        TRAY_7("tray-7"),
        @SerializedName("tray-8")
        TRAY_8("tray-8"),
        @SerializedName("tray-9")
        TRAY_9("tray-9"),
        @SerializedName("tray-10")
        TRAY_10("tray-10"),
        @SerializedName("vertical-cutter")
        VERTICAL_CUTTER("vertical-cutter"),
        @SerializedName("alternate")
        ALTERNATE("alternate");
        private final String value;
        private final static Map<String, PrintSettings.MediaDestinationId> CONSTANTS = new HashMap<String, PrintSettings.MediaDestinationId>();

        static {
            for (PrintSettings.MediaDestinationId c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        MediaDestinationId(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static PrintSettings.MediaDestinationId fromValue(String value) {
            PrintSettings.MediaDestinationId constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum PlexMode {

        @SerializedName("simplex")
        SIMPLEX("simplex"),
        @SerializedName("duplex")
        DUPLEX("duplex");
        private final String value;
        private final static Map<String, PrintSettings.PlexMode> CONSTANTS = new HashMap<String, PrintSettings.PlexMode>();

        static {
            for (PrintSettings.PlexMode c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        PlexMode(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static PrintSettings.PlexMode fromValue(String value) {
            PrintSettings.PlexMode constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * print quality value used in the Job
     * 
     */
    public enum PrintQuality {

        @SerializedName("draft")
        DRAFT("draft"),
        @SerializedName("normal")
        NORMAL("normal"),
        @SerializedName("best")
        BEST("best");
        private final String value;
        private final static Map<String, PrintSettings.PrintQuality> CONSTANTS = new HashMap<String, PrintSettings.PrintQuality>();

        static {
            for (PrintSettings.PrintQuality c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        PrintQuality(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static PrintSettings.PrintQuality fromValue(String value) {
            PrintSettings.PrintQuality constant = CONSTANTS.get(value);
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
        private final static Map<String, PrintSettings.Resolutions> CONSTANTS = new HashMap<String, PrintSettings.Resolutions>();

        static {
            for (PrintSettings.Resolutions c: values()) {
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

        public static PrintSettings.Resolutions fromValue(String value) {
            PrintSettings.Resolutions constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
