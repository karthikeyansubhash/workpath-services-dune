
package com.hp.ws.cdm.jobticket;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class Print {

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("collate")
    @Expose
    private CollateModes collate;
    @SerializedName("colorMode")
    @Expose
    private Scan.ColorModes colorMode;
    @SerializedName("rgbSourceProfile")
    @Expose
    private RgbSourceProfiles rgbSourceProfile;
    @SerializedName("cmykSourceProfile")
    @Expose
    private CmykSourceProfiles cmykSourceProfile;
    @SerializedName("renderIntent")
    @Expose
    private RenderIntents renderIntent;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("blackPointCompensation")
    @Expose
    private Property.FeatureEnabled blackPointCompensation;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("pantoneEmulation")
    @Expose
    private Property.FeatureEnabled pantoneEmulation;
    @SerializedName("copies")
    @Expose
    private Integer copies;
    /**
     * a unique ID used by this printer to identify this input sub-unit from PWG 5100.13
     * 
     */
    @SerializedName("mediaSource")
    @Expose
    private com.hp.ws.cdm.jobmanagement.MediaInput.MediaSourceId mediaSource;
    /**
     * a unique ID used by this printer to identify this output sub-unit from PWG 5100.2-2001
     * 
     */
    @SerializedName("mediaDestination")
    @Expose
    private com.hp.ws.cdm.jobmanagement.PrintSettings.MediaDestinationId mediaDestination;
    /**
     * Media size, values according to the standard PWG5101.1 or HP extension pattern for exceptions
     * 
     */
    @SerializedName("mediaSize")
    @Expose
    private com.hp.ws.cdm.jobmanagement.MediaInput.MediaSize mediaSize;
    /**
     * The X feed dimension of custom media size in ten-thousandth of an inch.
     * 
     */
    @SerializedName("customMediaXFeedDimension")
    @Expose
    private Float customMediaXFeedDimension;
    /**
     * The Y feed dimension of custom media size in ten-thousandth of an inch.
     * 
     */
    @SerializedName("customMediaYFeedDimension")
    @Expose
    private Float customMediaYFeedDimension;
    /**
     * Media type,  values according to the standard PWG5101.1
     * 
     */
    @SerializedName("mediaType")
    @Expose
    private com.hp.ws.cdm.jobmanagement.MediaInput.MediaType mediaType;
    /**
     * The folding style specified to be used for the media destination id.
     * 
     */
    @SerializedName("foldingStyleId")
    @Expose
    private Integer foldingStyleId;
    @SerializedName("plexMode")
    @Expose
    private com.hp.ws.cdm.jobmanagement.PrintSettings.PlexMode plexMode;
    @SerializedName("duplexBinding")
    @Expose
    private com.hp.ws.cdm.jobmanagement.ScanInfo.DuplexBinding duplexBinding;
    /**
     * print quality value used in the Job
     * 
     */
    @SerializedName("printQuality")
    @Expose
    private com.hp.ws.cdm.jobmanagement.PrintSettings.PrintQuality printQuality;
    @SerializedName("resolution")
    @Expose
    private Scan.Resolutions resolution;
    @SerializedName("printMargins")
    @Expose
    private PrintMargins printMargins;
    @SerializedName("logicalMargins")
    @Expose
    private LogicalMargins logicalMargins;
    @SerializedName("scaling")
    @Expose
    private Scaling scaling;
    @SerializedName("printingOrder")
    @Expose
    private PrintingOrder printingOrder;
    /**
     * Media profiles are categorized by families. UI groups media profiles by family
     * 
     */
    @SerializedName("mediaFamily")
    @Expose
    private MediaFamily mediaFamily;
    /**
     * Value for the page image rotate(auto, 0, 90, 180, 270)
     * 
     */
    @SerializedName("rotate")
    @Expose
    private Rotate rotate;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("econoMode")
    @Expose
    private Property.FeatureEnabled econoMode;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("maxDetail")
    @Expose
    private Property.FeatureEnabled maxDetail;
    /**
     * Remove Blank Areas option, fullSize: do not eliminate unnecessary blank spaces, InkedArea: remove blank area
     * 
     */
    @SerializedName("removeBlankAreas")
    @Expose
    private RemoveBlankAreas removeBlankAreas;
    /**
     * When rendering and printing of the same job are not coupled, determines the moment of rendering when the pages are started to print
     * 
     */
    @SerializedName("whenToStartPrinting")
    @Expose
    private WhenToStartPrinting whenToStartPrinting;
    /**
     * Staple options
     * 
     */
    @SerializedName("stapleOption")
    @Expose
    private StapleOptions stapleOption;
    /**
     * Punch options
     * 
     */
    @SerializedName("punchOption")
    @Expose
    private PunchOptions punchOption;
    /**
     * Fold options
     * 
     */
    @SerializedName("foldOption")
    @Expose
    private FoldOptions foldOption;
    /**
     * Booklet maker options
     * 
     */
    @SerializedName("bookletMakerOption")
    @Expose
    private BookletMakerOptions bookletMakerOption;
    /**
     * Media feed will allow user to select the mode of paper feed in default print job ticket, manual or automatic feed selection modes.
     * 
     */
    @SerializedName("mediaFeed")
    @Expose
    private MediaFeed mediaFeed;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("jobOffset")
    @Expose
    private Property.FeatureEnabled jobOffset;

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

    public CollateModes getCollate() {
        return collate;
    }

    public void setCollate(CollateModes collate) {
        this.collate = collate;
    }

    public Scan.ColorModes getColorMode() {
        return colorMode;
    }

    public void setColorMode(Scan.ColorModes colorMode) {
        this.colorMode = colorMode;
    }

    public RgbSourceProfiles getRgbSourceProfile() {
        return rgbSourceProfile;
    }

    public void setRgbSourceProfile(RgbSourceProfiles rgbSourceProfile) {
        this.rgbSourceProfile = rgbSourceProfile;
    }

    public CmykSourceProfiles getCmykSourceProfile() {
        return cmykSourceProfile;
    }

    public void setCmykSourceProfile(CmykSourceProfiles cmykSourceProfile) {
        this.cmykSourceProfile = cmykSourceProfile;
    }

    public RenderIntents getRenderIntent() {
        return renderIntent;
    }

    public void setRenderIntent(RenderIntents renderIntent) {
        this.renderIntent = renderIntent;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getBlackPointCompensation() {
        return blackPointCompensation;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setBlackPointCompensation(Property.FeatureEnabled blackPointCompensation) {
        this.blackPointCompensation = blackPointCompensation;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getPantoneEmulation() {
        return pantoneEmulation;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setPantoneEmulation(Property.FeatureEnabled pantoneEmulation) {
        this.pantoneEmulation = pantoneEmulation;
    }

    public Integer getCopies() {
        return copies;
    }

    public void setCopies(Integer copies) {
        this.copies = copies;
    }

    /**
     * a unique ID used by this printer to identify this input sub-unit from PWG 5100.13
     * 
     */
    public com.hp.ws.cdm.jobmanagement.MediaInput.MediaSourceId getMediaSource() {
        return mediaSource;
    }

    /**
     * a unique ID used by this printer to identify this input sub-unit from PWG 5100.13
     * 
     */
    public void setMediaSource(com.hp.ws.cdm.jobmanagement.MediaInput.MediaSourceId mediaSource) {
        this.mediaSource = mediaSource;
    }

    /**
     * a unique ID used by this printer to identify this output sub-unit from PWG 5100.2-2001
     * 
     */
    public com.hp.ws.cdm.jobmanagement.PrintSettings.MediaDestinationId getMediaDestination() {
        return mediaDestination;
    }

    /**
     * a unique ID used by this printer to identify this output sub-unit from PWG 5100.2-2001
     * 
     */
    public void setMediaDestination(com.hp.ws.cdm.jobmanagement.PrintSettings.MediaDestinationId mediaDestination) {
        this.mediaDestination = mediaDestination;
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
     * The X feed dimension of custom media size in ten-thousandth of an inch.
     * 
     */
    public Float getCustomMediaXFeedDimension() {
        return customMediaXFeedDimension;
    }

    /**
     * The X feed dimension of custom media size in ten-thousandth of an inch.
     * 
     */
    public void setCustomMediaXFeedDimension(Float customMediaXFeedDimension) {
        this.customMediaXFeedDimension = customMediaXFeedDimension;
    }

    /**
     * The Y feed dimension of custom media size in ten-thousandth of an inch.
     * 
     */
    public Float getCustomMediaYFeedDimension() {
        return customMediaYFeedDimension;
    }

    /**
     * The Y feed dimension of custom media size in ten-thousandth of an inch.
     * 
     */
    public void setCustomMediaYFeedDimension(Float customMediaYFeedDimension) {
        this.customMediaYFeedDimension = customMediaYFeedDimension;
    }

    /**
     * Media type,  values according to the standard PWG5101.1
     * 
     */
    public com.hp.ws.cdm.jobmanagement.MediaInput.MediaType getMediaType() {
        return mediaType;
    }

    /**
     * Media type,  values according to the standard PWG5101.1
     * 
     */
    public void setMediaType(com.hp.ws.cdm.jobmanagement.MediaInput.MediaType mediaType) {
        this.mediaType = mediaType;
    }

    /**
     * The folding style specified to be used for the media destination id.
     * 
     */
    public Integer getFoldingStyleId() {
        return foldingStyleId;
    }

    /**
     * The folding style specified to be used for the media destination id.
     * 
     */
    public void setFoldingStyleId(Integer foldingStyleId) {
        this.foldingStyleId = foldingStyleId;
    }

    public com.hp.ws.cdm.jobmanagement.PrintSettings.PlexMode getPlexMode() {
        return plexMode;
    }

    public void setPlexMode(com.hp.ws.cdm.jobmanagement.PrintSettings.PlexMode plexMode) {
        this.plexMode = plexMode;
    }

    public com.hp.ws.cdm.jobmanagement.ScanInfo.DuplexBinding getDuplexBinding() {
        return duplexBinding;
    }

    public void setDuplexBinding(com.hp.ws.cdm.jobmanagement.ScanInfo.DuplexBinding duplexBinding) {
        this.duplexBinding = duplexBinding;
    }

    /**
     * print quality value used in the Job
     * 
     */
    public com.hp.ws.cdm.jobmanagement.PrintSettings.PrintQuality getPrintQuality() {
        return printQuality;
    }

    /**
     * print quality value used in the Job
     * 
     */
    public void setPrintQuality(com.hp.ws.cdm.jobmanagement.PrintSettings.PrintQuality printQuality) {
        this.printQuality = printQuality;
    }

    public Scan.Resolutions getResolution() {
        return resolution;
    }

    public void setResolution(Scan.Resolutions resolution) {
        this.resolution = resolution;
    }

    public PrintMargins getPrintMargins() {
        return printMargins;
    }

    public void setPrintMargins(PrintMargins printMargins) {
        this.printMargins = printMargins;
    }

    public LogicalMargins getLogicalMargins() {
        return logicalMargins;
    }

    public void setLogicalMargins(LogicalMargins logicalMargins) {
        this.logicalMargins = logicalMargins;
    }

    public Scaling getScaling() {
        return scaling;
    }

    public void setScaling(Scaling scaling) {
        this.scaling = scaling;
    }

    public PrintingOrder getPrintingOrder() {
        return printingOrder;
    }

    public void setPrintingOrder(PrintingOrder printingOrder) {
        this.printingOrder = printingOrder;
    }

    /**
     * Media profiles are categorized by families. UI groups media profiles by family
     * 
     */
    public MediaFamily getMediaFamily() {
        return mediaFamily;
    }

    /**
     * Media profiles are categorized by families. UI groups media profiles by family
     * 
     */
    public void setMediaFamily(MediaFamily mediaFamily) {
        this.mediaFamily = mediaFamily;
    }

    /**
     * Value for the page image rotate(auto, 0, 90, 180, 270)
     * 
     */
    public Rotate getRotate() {
        return rotate;
    }

    /**
     * Value for the page image rotate(auto, 0, 90, 180, 270)
     * 
     */
    public void setRotate(Rotate rotate) {
        this.rotate = rotate;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getEconoMode() {
        return econoMode;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setEconoMode(Property.FeatureEnabled econoMode) {
        this.econoMode = econoMode;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getMaxDetail() {
        return maxDetail;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setMaxDetail(Property.FeatureEnabled maxDetail) {
        this.maxDetail = maxDetail;
    }

    /**
     * Remove Blank Areas option, fullSize: do not eliminate unnecessary blank spaces, InkedArea: remove blank area
     * 
     */
    public RemoveBlankAreas getRemoveBlankAreas() {
        return removeBlankAreas;
    }

    /**
     * Remove Blank Areas option, fullSize: do not eliminate unnecessary blank spaces, InkedArea: remove blank area
     * 
     */
    public void setRemoveBlankAreas(RemoveBlankAreas removeBlankAreas) {
        this.removeBlankAreas = removeBlankAreas;
    }

    /**
     * When rendering and printing of the same job are not coupled, determines the moment of rendering when the pages are started to print
     * 
     */
    public WhenToStartPrinting getWhenToStartPrinting() {
        return whenToStartPrinting;
    }

    /**
     * When rendering and printing of the same job are not coupled, determines the moment of rendering when the pages are started to print
     * 
     */
    public void setWhenToStartPrinting(WhenToStartPrinting whenToStartPrinting) {
        this.whenToStartPrinting = whenToStartPrinting;
    }

    /**
     * Staple options
     * 
     */
    public StapleOptions getStapleOption() {
        return stapleOption;
    }

    /**
     * Staple options
     * 
     */
    public void setStapleOption(StapleOptions stapleOption) {
        this.stapleOption = stapleOption;
    }

    /**
     * Punch options
     * 
     */
    public PunchOptions getPunchOption() {
        return punchOption;
    }

    /**
     * Punch options
     * 
     */
    public void setPunchOption(PunchOptions punchOption) {
        this.punchOption = punchOption;
    }

    /**
     * Fold options
     * 
     */
    public FoldOptions getFoldOption() {
        return foldOption;
    }

    /**
     * Fold options
     * 
     */
    public void setFoldOption(FoldOptions foldOption) {
        this.foldOption = foldOption;
    }

    /**
     * Booklet maker options
     * 
     */
    public BookletMakerOptions getBookletMakerOption() {
        return bookletMakerOption;
    }

    /**
     * Booklet maker options
     * 
     */
    public void setBookletMakerOption(BookletMakerOptions bookletMakerOption) {
        this.bookletMakerOption = bookletMakerOption;
    }

    /**
     * Media feed will allow user to select the mode of paper feed in default print job ticket, manual or automatic feed selection modes.
     * 
     */
    public MediaFeed getMediaFeed() {
        return mediaFeed;
    }

    /**
     * Media feed will allow user to select the mode of paper feed in default print job ticket, manual or automatic feed selection modes.
     * 
     */
    public void setMediaFeed(MediaFeed mediaFeed) {
        this.mediaFeed = mediaFeed;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getJobOffset() {
        return jobOffset;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setJobOffset(Property.FeatureEnabled jobOffset) {
        this.jobOffset = jobOffset;
    }


    /**
     * Booklet maker options
     * 
     */
    public enum BookletMakerOptions {

        @SerializedName("none")
        NONE("none"),
        @SerializedName("default")
        DEFAULT("default"),
        @SerializedName("bookletMaker")
        BOOKLET_MAKER("bookletMaker"),
        @SerializedName("saddleStitch")
        SADDLE_STITCH("saddleStitch");
        private final String value;
        private final static Map<String, BookletMakerOptions> CONSTANTS = new HashMap<String, BookletMakerOptions>();

        static {
            for (BookletMakerOptions c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        BookletMakerOptions(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static BookletMakerOptions fromValue(String value) {
            BookletMakerOptions constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum CmykSourceProfiles {

        @SerializedName("swopCoated")
        SWOP_COATED("swopCoated"),
        @SerializedName("toyoCoated")
        TOYO_COATED("toyoCoated"),
        @SerializedName("euroCoated")
        EURO_COATED("euroCoated"),
        @SerializedName("dicCoated")
        DIC_COATED("dicCoated"),
        @SerializedName("jmpa")
        JMPA("jmpa"),
        @SerializedName("cmykPlus")
        CMYK_PLUS("cmykPlus"),
        @SerializedName("usWebUncoated")
        US_WEB_UNCOATED("usWebUncoated"),
        @SerializedName("usSheetfedCoated")
        US_SHEETFED_COATED("usSheetfedCoated"),
        @SerializedName("usSheetfedUncoated")
        US_SHEETFED_UNCOATED("usSheetfedUncoated"),
        @SerializedName("euroScaleUncoated")
        EURO_SCALE_UNCOATED("euroScaleUncoated"),
        @SerializedName("europeIsoCoatedFogra27")
        EUROPE_ISO_COATED_FOGRA_27("europeIsoCoatedFogra27"),
        @SerializedName("coatedFogra39")
        COATED_FOGRA_39("coatedFogra39"),
        @SerializedName("isoCoatedV2Eci")
        ISO_COATED_V_2_ECI("isoCoatedV2Eci"),
        @SerializedName("coatedGracol2006")
        COATED_GRACOL_2006("coatedGracol2006"),
        @SerializedName("gracol2013Crpc6")
        GRACOL_2013_CRPC_6("gracol2013Crpc6"),
        @SerializedName("gracol2013UnCrpc3")
        GRACOL_2013_UN_CRPC_3("gracol2013UnCrpc3"),
        @SerializedName("webCoatedSwop2006Grade3Paper")
        WEB_COATED_SWOP_2006_GRADE_3_PAPER("webCoatedSwop2006Grade3Paper"),
        @SerializedName("webCoatedSwop2006Grade5Paper")
        WEB_COATED_SWOP_2006_GRADE_5_PAPER("webCoatedSwop2006Grade5Paper"),
        @SerializedName("psoUncoated")
        PSO_UNCOATED("psoUncoated"),
        @SerializedName("psoLwcImprovedEci")
        PSO_LWC_IMPROVED_ECI("psoLwcImprovedEci"),
        @SerializedName("psoCoatedV3")
        PSO_COATED_V_3("psoCoatedV3"),
        @SerializedName("psoUncoatedV3Fogra52")
        PSO_UNCOATED_V_3_FOGRA_52("psoUncoatedV3Fogra52"),
        @SerializedName("japanWebCoated")
        JAPAN_WEB_COATED("japanWebCoated"),
        @SerializedName("japan2001Coated")
        JAPAN_2001_COATED("japan2001Coated"),
        @SerializedName("japan2001Uncoated")
        JAPAN_2001_UNCOATED("japan2001Uncoated"),
        @SerializedName("japanColor2002Newspaper")
        JAPAN_COLOR_2002_NEWSPAPER("japanColor2002Newspaper"),
        @SerializedName("japanColor2003WebCoated")
        JAPAN_COLOR_2003_WEB_COATED("japanColor2003WebCoated"),
        @SerializedName("japanColor2011Coated")
        JAPAN_COLOR_2011_COATED("japanColor2011Coated"),
        @SerializedName("photoshop4")
        PHOTOSHOP_4("photoshop4"),
        @SerializedName("photoshop5")
        PHOTOSHOP_5("photoshop5"),
        @SerializedName("HpDesignjet1055CmHeavyCoatedPaper")
        HP_DESIGNJET_1055_CM_HEAVY_COATED_PAPER("HpDesignjet1055CmHeavyCoatedPaper"),
        @SerializedName("Hp5000PhotoImageGlossMaxQ")
        HP_5000_PHOTO_IMAGE_GLOSS_MAX_Q("Hp5000PhotoImageGlossMaxQ"),
        @SerializedName("Hp5500HwCoatedPaperMq")
        HP_5500_HW_COATED_PAPER_MQ("Hp5500HwCoatedPaperMq"),
        @SerializedName("Hp5000HwCoatedPaperMaxQ")
        HP_5000_HW_COATED_PAPER_MAX_Q("Hp5000HwCoatedPaperMaxQ"),
        @SerializedName("HpDesignjet1055CmHighGlossPhotoPaper")
        HP_DESIGNJET_1055_CM_HIGH_GLOSS_PHOTO_PAPER("HpDesignjet1055CmHighGlossPhotoPaper"),
        @SerializedName("Hp5500PhImgGlossMq")
        HP_5500_PH_IMG_GLOSS_MQ("Hp5500PhImgGlossMq");
        private final String value;
        private final static Map<String, CmykSourceProfiles> CONSTANTS = new HashMap<String, CmykSourceProfiles>();

        static {
            for (CmykSourceProfiles c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        CmykSourceProfiles(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static CmykSourceProfiles fromValue(String value) {
            CmykSourceProfiles constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum CollateModes {

        @SerializedName("collated")
        COLLATED("collated"),
        @SerializedName("uncollated")
        UNCOLLATED("uncollated");
        private final String value;
        private final static Map<String, CollateModes> CONSTANTS = new HashMap<String, CollateModes>();

        static {
            for (CollateModes c: values()) {
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

        public static CollateModes fromValue(String value) {
            CollateModes constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Fold options
     * 
     */
    public enum FoldOptions {

        @SerializedName("none")
        NONE("none"),
        @SerializedName("default")
        DEFAULT("default"),
        @SerializedName("cInwardTop")
        C_INWARD_TOP("cInwardTop"),
        @SerializedName("cInwardBottom")
        C_INWARD_BOTTOM("cInwardBottom"),
        @SerializedName("cOutwardTop")
        C_OUTWARD_TOP("cOutwardTop"),
        @SerializedName("cOutwardBottom")
        C_OUTWARD_BOTTOM("cOutwardBottom"),
        @SerializedName("vInwardTop")
        V_INWARD_TOP("vInwardTop"),
        @SerializedName("vInwardBottom")
        V_INWARD_BOTTOM("vInwardBottom"),
        @SerializedName("vOutwardTop")
        V_OUTWARD_TOP("vOutwardTop"),
        @SerializedName("vOutwardBottom")
        V_OUTWARD_BOTTOM("vOutwardBottom");
        private final String value;
        private final static Map<String, FoldOptions> CONSTANTS = new HashMap<String, FoldOptions>();

        static {
            for (FoldOptions c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        FoldOptions(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static FoldOptions fromValue(String value) {
            FoldOptions constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum LogicalMargins {

        @SerializedName("zeroMm")
        ZERO_MM("zeroMm"),
        @SerializedName("threeMm")
        THREE_MM("threeMm"),
        @SerializedName("fiveMm")
        FIVE_MM("fiveMm");
        private final String value;
        private final static Map<String, LogicalMargins> CONSTANTS = new HashMap<String, LogicalMargins>();

        static {
            for (LogicalMargins c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        LogicalMargins(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static LogicalMargins fromValue(String value) {
            LogicalMargins constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Media profiles are categorized by families. UI groups media profiles by family
     * 
     */
    public enum MediaFamily {

        @SerializedName("adhesive")
        ADHESIVE("adhesive"),
        @SerializedName("backlit")
        BACKLIT("backlit"),
        @SerializedName("bannerAndSign")
        BANNER_AND_SIGN("bannerAndSign"),
        @SerializedName("bondAndCoated")
        BOND_AND_COATED("bondAndCoated"),
        @SerializedName("blueprint")
        BLUEPRINT("blueprint"),
        @SerializedName("canvas")
        CANVAS("canvas"),
        @SerializedName("custom")
        CUSTOM("custom"),
        @SerializedName("film")
        FILM("film"),
        @SerializedName("heatTransfer")
        HEAT_TRANSFER("heatTransfer"),
        @SerializedName("plain")
        PLAIN("plain"),
        @SerializedName("photo")
        PHOTO("photo"),
        @SerializedName("technical")
        TECHNICAL("technical"),
        @SerializedName("textile")
        TEXTILE("textile"),
        @SerializedName("wallcovering")
        WALLCOVERING("wallcovering"),
        @SerializedName("unknown")
        UNKNOWN("unknown"),
        @SerializedName("poster")
        POSTER("poster"),
        @SerializedName("advancedFoldingPaper")
        ADVANCED_FOLDING_PAPER("advancedFoldingPaper");
        private final String value;
        private final static Map<String, MediaFamily> CONSTANTS = new HashMap<String, MediaFamily>();

        static {
            for (MediaFamily c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        MediaFamily(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static MediaFamily fromValue(String value) {
            MediaFamily constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Media feed will allow user to select the mode of paper feed in default print job ticket, manual or automatic feed selection modes.
     * 
     */
    public enum MediaFeed {

        @SerializedName("manual")
        MANUAL("manual"),
        @SerializedName("automatic")
        AUTOMATIC("automatic");
        private final String value;
        private final static Map<String, MediaFeed> CONSTANTS = new HashMap<String, MediaFeed>();

        static {
            for (MediaFeed c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        MediaFeed(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static MediaFeed fromValue(String value) {
            MediaFeed constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum PrintMargins {

        @SerializedName("oversize")
        OVERSIZE("oversize"),
        @SerializedName("clipContents")
        CLIP_CONTENTS("clipContents"),
        @SerializedName("addToContents")
        ADD_TO_CONTENTS("addToContents");
        private final String value;
        private final static Map<String, PrintMargins> CONSTANTS = new HashMap<String, PrintMargins>();

        static {
            for (PrintMargins c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        PrintMargins(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static PrintMargins fromValue(String value) {
            PrintMargins constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum PrintingOrder {

        @SerializedName("firstPageOnTop")
        FIRST_PAGE_ON_TOP("firstPageOnTop"),
        @SerializedName("lastPageOnTop")
        LAST_PAGE_ON_TOP("lastPageOnTop");
        private final String value;
        private final static Map<String, PrintingOrder> CONSTANTS = new HashMap<String, PrintingOrder>();

        static {
            for (PrintingOrder c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        PrintingOrder(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static PrintingOrder fromValue(String value) {
            PrintingOrder constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Punch options
     * 
     */
    public enum PunchOptions {

        @SerializedName("none")
        NONE("none"),
        @SerializedName("default")
        DEFAULT("default"),
        @SerializedName("twoPointAny")
        TWO_POINT_ANY("twoPointAny"),
        @SerializedName("leftTwoPointDin")
        LEFT_TWO_POINT_DIN("leftTwoPointDin"),
        @SerializedName("rightTwoPointDin")
        RIGHT_TWO_POINT_DIN("rightTwoPointDin"),
        @SerializedName("topTwoPointDin")
        TOP_TWO_POINT_DIN("topTwoPointDin"),
        @SerializedName("bottomTwoPointDin")
        BOTTOM_TWO_POINT_DIN("bottomTwoPointDin"),
        @SerializedName("twoPointDin")
        TWO_POINT_DIN("twoPointDin"),
        @SerializedName("leftTwoPointUs")
        LEFT_TWO_POINT_US("leftTwoPointUs"),
        @SerializedName("rightTwoPointUs")
        RIGHT_TWO_POINT_US("rightTwoPointUs"),
        @SerializedName("topTwoPointUs")
        TOP_TWO_POINT_US("topTwoPointUs"),
        @SerializedName("bottomTwoPointUs")
        BOTTOM_TWO_POINT_US("bottomTwoPointUs"),
        @SerializedName("twoPointUs")
        TWO_POINT_US("twoPointUs"),
        @SerializedName("leftThreePointUs")
        LEFT_THREE_POINT_US("leftThreePointUs"),
        @SerializedName("rightThreePointUs")
        RIGHT_THREE_POINT_US("rightThreePointUs"),
        @SerializedName("topThreePointUs")
        TOP_THREE_POINT_US("topThreePointUs"),
        @SerializedName("bottomThreePointUs")
        BOTTOM_THREE_POINT_US("bottomThreePointUs"),
        @SerializedName("threePointUs")
        THREE_POINT_US("threePointUs"),
        @SerializedName("threePointAny")
        THREE_POINT_ANY("threePointAny"),
        @SerializedName("leftFourPointDin")
        LEFT_FOUR_POINT_DIN("leftFourPointDin"),
        @SerializedName("rightFourPointDin")
        RIGHT_FOUR_POINT_DIN("rightFourPointDin"),
        @SerializedName("topFourPointDin")
        TOP_FOUR_POINT_DIN("topFourPointDin"),
        @SerializedName("bottomFourPointDin")
        BOTTOM_FOUR_POINT_DIN("bottomFourPointDin"),
        @SerializedName("fourPointDin")
        FOUR_POINT_DIN("fourPointDin"),
        @SerializedName("leftFourPointSwd")
        LEFT_FOUR_POINT_SWD("leftFourPointSwd"),
        @SerializedName("rightFourPointSwd")
        RIGHT_FOUR_POINT_SWD("rightFourPointSwd"),
        @SerializedName("topFourPointSwd")
        TOP_FOUR_POINT_SWD("topFourPointSwd"),
        @SerializedName("bottomFourPointSwd")
        BOTTOM_FOUR_POINT_SWD("bottomFourPointSwd"),
        @SerializedName("fourPointSwd")
        FOUR_POINT_SWD("fourPointSwd"),
        @SerializedName("fourPointAny")
        FOUR_POINT_ANY("fourPointAny"),
        @SerializedName("leftTwoPoint")
        LEFT_TWO_POINT("leftTwoPoint"),
        @SerializedName("rightTwoPoint")
        RIGHT_TWO_POINT("rightTwoPoint"),
        @SerializedName("topTwoPoint")
        TOP_TWO_POINT("topTwoPoint"),
        @SerializedName("bottomTwoPoint")
        BOTTOM_TWO_POINT("bottomTwoPoint");
        private final String value;
        private final static Map<String, PunchOptions> CONSTANTS = new HashMap<String, PunchOptions>();

        static {
            for (PunchOptions c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        PunchOptions(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static PunchOptions fromValue(String value) {
            PunchOptions constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Remove Blank Areas option, fullSize: do not eliminate unnecessary blank spaces, InkedArea: remove blank area
     * 
     */
    public enum RemoveBlankAreas {

        @SerializedName("fullSize")
        FULL_SIZE("fullSize"),
        @SerializedName("inkedArea")
        INKED_AREA("inkedArea");
        private final String value;
        private final static Map<String, RemoveBlankAreas> CONSTANTS = new HashMap<String, RemoveBlankAreas>();

        static {
            for (RemoveBlankAreas c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        RemoveBlankAreas(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static RemoveBlankAreas fromValue(String value) {
            RemoveBlankAreas constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum RenderIntents {

        @SerializedName("perceptual")
        PERCEPTUAL("perceptual"),
        @SerializedName("saturation")
        SATURATION("saturation"),
        @SerializedName("relativeColorimetric")
        RELATIVE_COLORIMETRIC("relativeColorimetric"),
        @SerializedName("absoluteColorimetric")
        ABSOLUTE_COLORIMETRIC("absoluteColorimetric");
        private final String value;
        private final static Map<String, RenderIntents> CONSTANTS = new HashMap<String, RenderIntents>();

        static {
            for (RenderIntents c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        RenderIntents(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static RenderIntents fromValue(String value) {
            RenderIntents constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum RgbSourceProfiles {

        @SerializedName("srgb")
        SRGB("srgb"),
        @SerializedName("appleRgb")
        APPLE_RGB("appleRgb"),
        @SerializedName("adobeRgb")
        ADOBE_RGB("adobeRgb"),
        @SerializedName("colorMatchRgb")
        COLOR_MATCH_RGB("colorMatchRgb"),
        @SerializedName("native")
        NATIVE("native");
        private final String value;
        private final static Map<String, RgbSourceProfiles> CONSTANTS = new HashMap<String, RgbSourceProfiles>();

        static {
            for (RgbSourceProfiles c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        RgbSourceProfiles(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static RgbSourceProfiles fromValue(String value) {
            RgbSourceProfiles constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Value for the page image rotate(auto, 0, 90, 180, 270)
     * 
     */
    public enum Rotate {

        @SerializedName("auto")
        AUTO("auto"),
        @SerializedName("rotate0")
        ROTATE_0("rotate0"),
        @SerializedName("rotate90")
        ROTATE_90("rotate90"),
        @SerializedName("rotate180")
        ROTATE_180("rotate180"),
        @SerializedName("rotate270")
        ROTATE_270("rotate270");
        private final String value;
        private final static Map<String, Rotate> CONSTANTS = new HashMap<String, Rotate>();

        static {
            for (Rotate c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Rotate(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Rotate fromValue(String value) {
            Rotate constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Staple options
     * 
     */
    public enum StapleOptions {

        @SerializedName("none")
        NONE("none"),
        @SerializedName("default")
        DEFAULT("default"),
        @SerializedName("topAnyOnePointAny")
        TOP_ANY_ONE_POINT_ANY("topAnyOnePointAny"),
        @SerializedName("topAnyOnePointAngled")
        TOP_ANY_ONE_POINT_ANGLED("topAnyOnePointAngled"),
        @SerializedName("topLeftOnePointAny")
        TOP_LEFT_ONE_POINT_ANY("topLeftOnePointAny"),
        @SerializedName("topLeftOnePointAngled")
        TOP_LEFT_ONE_POINT_ANGLED("topLeftOnePointAngled"),
        @SerializedName("topLeftOnePointHorizontal")
        TOP_LEFT_ONE_POINT_HORIZONTAL("topLeftOnePointHorizontal"),
        @SerializedName("topLeftOnePointVertical")
        TOP_LEFT_ONE_POINT_VERTICAL("topLeftOnePointVertical"),
        @SerializedName("topRightOnePointAny")
        TOP_RIGHT_ONE_POINT_ANY("topRightOnePointAny"),
        @SerializedName("topRightOnePointAngled")
        TOP_RIGHT_ONE_POINT_ANGLED("topRightOnePointAngled"),
        @SerializedName("topRightOnePointHorizontal")
        TOP_RIGHT_ONE_POINT_HORIZONTAL("topRightOnePointHorizontal"),
        @SerializedName("topRightOnePointVertical")
        TOP_RIGHT_ONE_POINT_VERTICAL("topRightOnePointVertical"),
        @SerializedName("bottomLeftOnePointAny")
        BOTTOM_LEFT_ONE_POINT_ANY("bottomLeftOnePointAny"),
        @SerializedName("bottomLeftOnePointAngled")
        BOTTOM_LEFT_ONE_POINT_ANGLED("bottomLeftOnePointAngled"),
        @SerializedName("bottomLeftOnePointHorizontal")
        BOTTOM_LEFT_ONE_POINT_HORIZONTAL("bottomLeftOnePointHorizontal"),
        @SerializedName("bottomLeftOnePointVertical")
        BOTTOM_LEFT_ONE_POINT_VERTICAL("bottomLeftOnePointVertical"),
        @SerializedName("bottomRightOnePointAny")
        BOTTOM_RIGHT_ONE_POINT_ANY("bottomRightOnePointAny"),
        @SerializedName("bottomRightOnePointAngled")
        BOTTOM_RIGHT_ONE_POINT_ANGLED("bottomRightOnePointAngled"),
        @SerializedName("bottomRightOnePointHorizontal")
        BOTTOM_RIGHT_ONE_POINT_HORIZONTAL("bottomRightOnePointHorizontal"),
        @SerializedName("bottomRightOnePointVertical")
        BOTTOM_RIGHT_ONE_POINT_VERTICAL("bottomRightOnePointVertical"),
        @SerializedName("centerOnePoint")
        CENTER_ONE_POINT("centerOnePoint"),
        @SerializedName("leftTwoPoints")
        LEFT_TWO_POINTS("leftTwoPoints"),
        @SerializedName("leftTwoPointsAny")
        LEFT_TWO_POINTS_ANY("leftTwoPointsAny"),
        @SerializedName("rightTwoPoints")
        RIGHT_TWO_POINTS("rightTwoPoints"),
        @SerializedName("topTwoPoints")
        TOP_TWO_POINTS("topTwoPoints"),
        @SerializedName("bottomTwoPoints")
        BOTTOM_TWO_POINTS("bottomTwoPoints"),
        @SerializedName("centerTwoPoints")
        CENTER_TWO_POINTS("centerTwoPoints"),
        @SerializedName("leftThreePoints")
        LEFT_THREE_POINTS("leftThreePoints"),
        @SerializedName("leftThreePointsAny")
        LEFT_THREE_POINTS_ANY("leftThreePointsAny"),
        @SerializedName("rightThreePoints")
        RIGHT_THREE_POINTS("rightThreePoints"),
        @SerializedName("topThreePoints")
        TOP_THREE_POINTS("topThreePoints"),
        @SerializedName("bottomThreePoints")
        BOTTOM_THREE_POINTS("bottomThreePoints"),
        @SerializedName("centerThreePoints")
        CENTER_THREE_POINTS("centerThreePoints"),
        @SerializedName("leftSixPoints")
        LEFT_SIX_POINTS("leftSixPoints"),
        @SerializedName("leftSixPointsAny")
        LEFT_SIX_POINTS_ANY("leftSixPointsAny"),
        @SerializedName("rightSixPoints")
        RIGHT_SIX_POINTS("rightSixPoints"),
        @SerializedName("topSixPoints")
        TOP_SIX_POINTS("topSixPoints"),
        @SerializedName("bottomSixPoints")
        BOTTOM_SIX_POINTS("bottomSixPoints"),
        @SerializedName("centerSixPoints")
        CENTER_SIX_POINTS("centerSixPoints");
        private final String value;
        private final static Map<String, StapleOptions> CONSTANTS = new HashMap<String, StapleOptions>();

        static {
            for (StapleOptions c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        StapleOptions(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static StapleOptions fromValue(String value) {
            StapleOptions constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * When rendering and printing of the same job are not coupled, determines the moment of rendering when the pages are started to print
     * 
     */
    public enum WhenToStartPrinting {

        @SerializedName("immediately")
        IMMEDIATELY("immediately"),
        @SerializedName("afterProcessing")
        AFTER_PROCESSING("afterProcessing"),
        @SerializedName("optimized")
        OPTIMIZED("optimized");
        private final String value;
        private final static Map<String, WhenToStartPrinting> CONSTANTS = new HashMap<String, WhenToStartPrinting>();

        static {
            for (WhenToStartPrinting c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        WhenToStartPrinting(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static WhenToStartPrinting fromValue(String value) {
            WhenToStartPrinting constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
