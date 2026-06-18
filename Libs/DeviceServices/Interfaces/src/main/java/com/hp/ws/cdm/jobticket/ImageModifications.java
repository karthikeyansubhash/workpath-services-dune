
package com.hp.ws.cdm.jobticket;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class ImageModifications {

    /**
     * Scan will use the sharpness factor for scanning accuracy.Operation will use the media sharpness factor for text/picture accuracy for print, scan and digital send. Lower numbers are soft edges and higher numbers are sharper edges.
     * 
     */
    @SerializedName("sharpness")
    @Expose
    private Integer sharpness;
    /**
     * Scan will use the background cleanup factor to show more specks at lower number and reduces to fewer specks at higher number, for scanning accuracy.
     * 
     */
    @SerializedName("backgroundCleanup")
    @Expose
    private Integer backgroundCleanup;
    /**
     * Scan will use the darkness factor to show lighter at lower number, normal at middle and darker at higher number, for scanning darkness.
     * 
     */
    @SerializedName("exposure")
    @Expose
    private Integer exposure;
    /**
     * Scan will use the contrast factor to show the less contrast at lower number, normal at middle and more contrast at higher number, for scanning contrast.
     * 
     */
    @SerializedName("contrast")
    @Expose
    private Integer contrast;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("blankPageSuppressionEnabled")
    @Expose
    private Property.FeatureEnabled blankPageSuppressionEnabled;
    @SerializedName("pagesPerSheet")
    @Expose
    private PagesPerSheet pagesPerSheet;
    /**
     * Media size, values according to the standard PWG5101.1 or HP extension pattern for exceptions
     * 
     */
    @SerializedName("outputCanvasMediaSize")
    @Expose
    private com.hp.ws.cdm.jobmanagement.MediaInput.MediaSize outputCanvasMediaSize;
    /**
     * a unique ID used by this printer to identify this input sub-unit from PWG 5100.13
     * 
     */
    @SerializedName("outputCanvasMediaId")
    @Expose
    private com.hp.ws.cdm.jobmanagement.MediaInput.MediaSourceId outputCanvasMediaId;
    /**
     * Select a custom width value to select the output width size in mm.
     * 
     */
    @SerializedName("outputCanvasCustomWidth")
    @Expose
    private Float outputCanvasCustomWidth;
    /**
     * Select a custom width value to select the output length size in mm.
     * 
     */
    @SerializedName("outputCanvasCustomLength")
    @Expose
    private Float outputCanvasCustomLength;
    @SerializedName("outputCanvasAnchor")
    @Expose
    private OutputCanvasAnchor outputCanvasAnchor;
    /**
     * related to the orientation of the print content rendering on media
     * 
     */
    @SerializedName("outputCanvasOrientation")
    @Expose
    private Scan.ContentOrientation outputCanvasOrientation;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("backgroundNoiseRemoval")
    @Expose
    private Property.FeatureEnabled backgroundNoiseRemoval;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("backgroundColorRemoval")
    @Expose
    private Property.FeatureEnabled backgroundColorRemoval;
    /**
     * Select a fine tuning level value for backgroundColorRemoval operation. High values result in a more aggressive background removal.
     * 
     */
    @SerializedName("backgroundColorRemovalLevel")
    @Expose
    private Integer backgroundColorRemovalLevel;
    /**
     * Scan will use the black enhancement level to darken the image gray into pure black.
     * 
     */
    @SerializedName("blackEnhancementLevel")
    @Expose
    private Integer blackEnhancementLevel;
    /**
     * Nup page location of printing page sequence. next page located to right to left or top to bottom manner
     * 
     */
    @SerializedName("numberUpPresentationDirection")
    @Expose
    private NumberUpPresentationDirection numberUpPresentationDirection;
    /**
     * Type of border to the image on a copy page
     * 
     */
    @SerializedName("imageBorder")
    @Expose
    private ImageBorder imageBorder;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("applySameWidthForAllEdges")
    @Expose
    private Property.FeatureEnabled applySameWidthForAllEdges;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("mirrorFrontSideEnabled")
    @Expose
    private Property.FeatureEnabled mirrorFrontSideEnabled;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("eraseEdgesEnabled")
    @Expose
    private Property.FeatureEnabled eraseEdgesEnabled;
    /**
     * Erase measurement for front top side of the image in ten-thousandth of an inch.
     * 
     */
    @SerializedName("eraseFrontTop")
    @Expose
    private Float eraseFrontTop;
    /**
     * Erase measurement for front bottom side of the image in ten-thousandth of an inch.
     * 
     */
    @SerializedName("eraseFrontBottom")
    @Expose
    private Float eraseFrontBottom;
    /**
     * Erase measurement for front left side of the image in ten-thousandth of an inch.
     * 
     */
    @SerializedName("eraseFrontLeft")
    @Expose
    private Float eraseFrontLeft;
    /**
     * Erase measurement for front right side of the image in ten-thousandth of an inch.
     * 
     */
    @SerializedName("eraseFrontRight")
    @Expose
    private Float eraseFrontRight;
    /**
     * Erase measurement for back top side of the image in ten-thousandth of an inch.
     * 
     */
    @SerializedName("eraseBackTop")
    @Expose
    private Float eraseBackTop;
    /**
     * Erase measurement for back bottom side of the image in ten-thousandth of an inch.
     * 
     */
    @SerializedName("eraseBackBottom")
    @Expose
    private Float eraseBackBottom;
    /**
     * Erase measurement for back left side of the image in ten-thousandth of an inch.
     * 
     */
    @SerializedName("eraseBackLeft")
    @Expose
    private Float eraseBackLeft;
    /**
     * Erase measurement for back right side of the image in ten-thousandth of an inch.
     * 
     */
    @SerializedName("eraseBackRight")
    @Expose
    private Float eraseBackRight;

    /**
     * Scan will use the sharpness factor for scanning accuracy.Operation will use the media sharpness factor for text/picture accuracy for print, scan and digital send. Lower numbers are soft edges and higher numbers are sharper edges.
     * 
     */
    public Integer getSharpness() {
        return sharpness;
    }

    /**
     * Scan will use the sharpness factor for scanning accuracy.Operation will use the media sharpness factor for text/picture accuracy for print, scan and digital send. Lower numbers are soft edges and higher numbers are sharper edges.
     * 
     */
    public void setSharpness(Integer sharpness) {
        this.sharpness = sharpness;
    }

    /**
     * Scan will use the background cleanup factor to show more specks at lower number and reduces to fewer specks at higher number, for scanning accuracy.
     * 
     */
    public Integer getBackgroundCleanup() {
        return backgroundCleanup;
    }

    /**
     * Scan will use the background cleanup factor to show more specks at lower number and reduces to fewer specks at higher number, for scanning accuracy.
     * 
     */
    public void setBackgroundCleanup(Integer backgroundCleanup) {
        this.backgroundCleanup = backgroundCleanup;
    }

    /**
     * Scan will use the darkness factor to show lighter at lower number, normal at middle and darker at higher number, for scanning darkness.
     * 
     */
    public Integer getExposure() {
        return exposure;
    }

    /**
     * Scan will use the darkness factor to show lighter at lower number, normal at middle and darker at higher number, for scanning darkness.
     * 
     */
    public void setExposure(Integer exposure) {
        this.exposure = exposure;
    }

    /**
     * Scan will use the contrast factor to show the less contrast at lower number, normal at middle and more contrast at higher number, for scanning contrast.
     * 
     */
    public Integer getContrast() {
        return contrast;
    }

    /**
     * Scan will use the contrast factor to show the less contrast at lower number, normal at middle and more contrast at higher number, for scanning contrast.
     * 
     */
    public void setContrast(Integer contrast) {
        this.contrast = contrast;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getBlankPageSuppressionEnabled() {
        return blankPageSuppressionEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setBlankPageSuppressionEnabled(Property.FeatureEnabled blankPageSuppressionEnabled) {
        this.blankPageSuppressionEnabled = blankPageSuppressionEnabled;
    }

    public PagesPerSheet getPagesPerSheet() {
        return pagesPerSheet;
    }

    public void setPagesPerSheet(PagesPerSheet pagesPerSheet) {
        this.pagesPerSheet = pagesPerSheet;
    }

    /**
     * Media size, values according to the standard PWG5101.1 or HP extension pattern for exceptions
     * 
     */
    public com.hp.ws.cdm.jobmanagement.MediaInput.MediaSize getOutputCanvasMediaSize() {
        return outputCanvasMediaSize;
    }

    /**
     * Media size, values according to the standard PWG5101.1 or HP extension pattern for exceptions
     * 
     */
    public void setOutputCanvasMediaSize(com.hp.ws.cdm.jobmanagement.MediaInput.MediaSize outputCanvasMediaSize) {
        this.outputCanvasMediaSize = outputCanvasMediaSize;
    }

    /**
     * a unique ID used by this printer to identify this input sub-unit from PWG 5100.13
     * 
     */
    public com.hp.ws.cdm.jobmanagement.MediaInput.MediaSourceId getOutputCanvasMediaId() {
        return outputCanvasMediaId;
    }

    /**
     * a unique ID used by this printer to identify this input sub-unit from PWG 5100.13
     * 
     */
    public void setOutputCanvasMediaId(com.hp.ws.cdm.jobmanagement.MediaInput.MediaSourceId outputCanvasMediaId) {
        this.outputCanvasMediaId = outputCanvasMediaId;
    }

    /**
     * Select a custom width value to select the output width size in mm.
     * 
     */
    public Float getOutputCanvasCustomWidth() {
        return outputCanvasCustomWidth;
    }

    /**
     * Select a custom width value to select the output width size in mm.
     * 
     */
    public void setOutputCanvasCustomWidth(Float outputCanvasCustomWidth) {
        this.outputCanvasCustomWidth = outputCanvasCustomWidth;
    }

    /**
     * Select a custom width value to select the output length size in mm.
     * 
     */
    public Float getOutputCanvasCustomLength() {
        return outputCanvasCustomLength;
    }

    /**
     * Select a custom width value to select the output length size in mm.
     * 
     */
    public void setOutputCanvasCustomLength(Float outputCanvasCustomLength) {
        this.outputCanvasCustomLength = outputCanvasCustomLength;
    }

    public OutputCanvasAnchor getOutputCanvasAnchor() {
        return outputCanvasAnchor;
    }

    public void setOutputCanvasAnchor(OutputCanvasAnchor outputCanvasAnchor) {
        this.outputCanvasAnchor = outputCanvasAnchor;
    }

    /**
     * related to the orientation of the print content rendering on media
     * 
     */
    public Scan.ContentOrientation getOutputCanvasOrientation() {
        return outputCanvasOrientation;
    }

    /**
     * related to the orientation of the print content rendering on media
     * 
     */
    public void setOutputCanvasOrientation(Scan.ContentOrientation outputCanvasOrientation) {
        this.outputCanvasOrientation = outputCanvasOrientation;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getBackgroundNoiseRemoval() {
        return backgroundNoiseRemoval;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setBackgroundNoiseRemoval(Property.FeatureEnabled backgroundNoiseRemoval) {
        this.backgroundNoiseRemoval = backgroundNoiseRemoval;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getBackgroundColorRemoval() {
        return backgroundColorRemoval;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setBackgroundColorRemoval(Property.FeatureEnabled backgroundColorRemoval) {
        this.backgroundColorRemoval = backgroundColorRemoval;
    }

    /**
     * Select a fine tuning level value for backgroundColorRemoval operation. High values result in a more aggressive background removal.
     * 
     */
    public Integer getBackgroundColorRemovalLevel() {
        return backgroundColorRemovalLevel;
    }

    /**
     * Select a fine tuning level value for backgroundColorRemoval operation. High values result in a more aggressive background removal.
     * 
     */
    public void setBackgroundColorRemovalLevel(Integer backgroundColorRemovalLevel) {
        this.backgroundColorRemovalLevel = backgroundColorRemovalLevel;
    }

    /**
     * Scan will use the black enhancement level to darken the image gray into pure black.
     * 
     */
    public Integer getBlackEnhancementLevel() {
        return blackEnhancementLevel;
    }

    /**
     * Scan will use the black enhancement level to darken the image gray into pure black.
     * 
     */
    public void setBlackEnhancementLevel(Integer blackEnhancementLevel) {
        this.blackEnhancementLevel = blackEnhancementLevel;
    }

    /**
     * Nup page location of printing page sequence. next page located to right to left or top to bottom manner
     * 
     */
    public NumberUpPresentationDirection getNumberUpPresentationDirection() {
        return numberUpPresentationDirection;
    }

    /**
     * Nup page location of printing page sequence. next page located to right to left or top to bottom manner
     * 
     */
    public void setNumberUpPresentationDirection(NumberUpPresentationDirection numberUpPresentationDirection) {
        this.numberUpPresentationDirection = numberUpPresentationDirection;
    }

    /**
     * Type of border to the image on a copy page
     * 
     */
    public ImageBorder getImageBorder() {
        return imageBorder;
    }

    /**
     * Type of border to the image on a copy page
     * 
     */
    public void setImageBorder(ImageBorder imageBorder) {
        this.imageBorder = imageBorder;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getApplySameWidthForAllEdges() {
        return applySameWidthForAllEdges;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setApplySameWidthForAllEdges(Property.FeatureEnabled applySameWidthForAllEdges) {
        this.applySameWidthForAllEdges = applySameWidthForAllEdges;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getMirrorFrontSideEnabled() {
        return mirrorFrontSideEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setMirrorFrontSideEnabled(Property.FeatureEnabled mirrorFrontSideEnabled) {
        this.mirrorFrontSideEnabled = mirrorFrontSideEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getEraseEdgesEnabled() {
        return eraseEdgesEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setEraseEdgesEnabled(Property.FeatureEnabled eraseEdgesEnabled) {
        this.eraseEdgesEnabled = eraseEdgesEnabled;
    }

    /**
     * Erase measurement for front top side of the image in ten-thousandth of an inch.
     * 
     */
    public Float getEraseFrontTop() {
        return eraseFrontTop;
    }

    /**
     * Erase measurement for front top side of the image in ten-thousandth of an inch.
     * 
     */
    public void setEraseFrontTop(Float eraseFrontTop) {
        this.eraseFrontTop = eraseFrontTop;
    }

    /**
     * Erase measurement for front bottom side of the image in ten-thousandth of an inch.
     * 
     */
    public Float getEraseFrontBottom() {
        return eraseFrontBottom;
    }

    /**
     * Erase measurement for front bottom side of the image in ten-thousandth of an inch.
     * 
     */
    public void setEraseFrontBottom(Float eraseFrontBottom) {
        this.eraseFrontBottom = eraseFrontBottom;
    }

    /**
     * Erase measurement for front left side of the image in ten-thousandth of an inch.
     * 
     */
    public Float getEraseFrontLeft() {
        return eraseFrontLeft;
    }

    /**
     * Erase measurement for front left side of the image in ten-thousandth of an inch.
     * 
     */
    public void setEraseFrontLeft(Float eraseFrontLeft) {
        this.eraseFrontLeft = eraseFrontLeft;
    }

    /**
     * Erase measurement for front right side of the image in ten-thousandth of an inch.
     * 
     */
    public Float getEraseFrontRight() {
        return eraseFrontRight;
    }

    /**
     * Erase measurement for front right side of the image in ten-thousandth of an inch.
     * 
     */
    public void setEraseFrontRight(Float eraseFrontRight) {
        this.eraseFrontRight = eraseFrontRight;
    }

    /**
     * Erase measurement for back top side of the image in ten-thousandth of an inch.
     * 
     */
    public Float getEraseBackTop() {
        return eraseBackTop;
    }

    /**
     * Erase measurement for back top side of the image in ten-thousandth of an inch.
     * 
     */
    public void setEraseBackTop(Float eraseBackTop) {
        this.eraseBackTop = eraseBackTop;
    }

    /**
     * Erase measurement for back bottom side of the image in ten-thousandth of an inch.
     * 
     */
    public Float getEraseBackBottom() {
        return eraseBackBottom;
    }

    /**
     * Erase measurement for back bottom side of the image in ten-thousandth of an inch.
     * 
     */
    public void setEraseBackBottom(Float eraseBackBottom) {
        this.eraseBackBottom = eraseBackBottom;
    }

    /**
     * Erase measurement for back left side of the image in ten-thousandth of an inch.
     * 
     */
    public Float getEraseBackLeft() {
        return eraseBackLeft;
    }

    /**
     * Erase measurement for back left side of the image in ten-thousandth of an inch.
     * 
     */
    public void setEraseBackLeft(Float eraseBackLeft) {
        this.eraseBackLeft = eraseBackLeft;
    }

    /**
     * Erase measurement for back right side of the image in ten-thousandth of an inch.
     * 
     */
    public Float getEraseBackRight() {
        return eraseBackRight;
    }

    /**
     * Erase measurement for back right side of the image in ten-thousandth of an inch.
     * 
     */
    public void setEraseBackRight(Float eraseBackRight) {
        this.eraseBackRight = eraseBackRight;
    }


    /**
     * Type of border to the image on a copy page
     * 
     */
    public enum ImageBorder {

        @SerializedName("noBorder")
        NO_BORDER("noBorder"),
        @SerializedName("defaultLineBorder")
        DEFAULT_LINE_BORDER("defaultLineBorder");
        private final String value;
        private final static Map<String, ImageBorder> CONSTANTS = new HashMap<String, ImageBorder>();

        static {
            for (ImageBorder c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        ImageBorder(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static ImageBorder fromValue(String value) {
            ImageBorder constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Nup page location of printing page sequence. next page located to right to left or top to bottom manner
     * 
     */
    public enum NumberUpPresentationDirection {

        @SerializedName("toBottomToLeft")
        TO_BOTTOM_TO_LEFT("toBottomToLeft"),
        @SerializedName("toBottomToRight")
        TO_BOTTOM_TO_RIGHT("toBottomToRight"),
        @SerializedName("toLeftToBottom")
        TO_LEFT_TO_BOTTOM("toLeftToBottom"),
        @SerializedName("toLeftToTop")
        TO_LEFT_TO_TOP("toLeftToTop"),
        @SerializedName("toRightToBottom")
        TO_RIGHT_TO_BOTTOM("toRightToBottom"),
        @SerializedName("toRightToTop")
        TO_RIGHT_TO_TOP("toRightToTop"),
        @SerializedName("toTopToLeft")
        TO_TOP_TO_LEFT("toTopToLeft"),
        @SerializedName("toTopToRight")
        TO_TOP_TO_RIGHT("toTopToRight");
        private final String value;
        private final static Map<String, NumberUpPresentationDirection> CONSTANTS = new HashMap<String, NumberUpPresentationDirection>();

        static {
            for (NumberUpPresentationDirection c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        NumberUpPresentationDirection(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static NumberUpPresentationDirection fromValue(String value) {
            NumberUpPresentationDirection constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum OutputCanvasAnchor {

        @SerializedName("topLeft")
        TOP_LEFT("topLeft"),
        @SerializedName("topCenter")
        TOP_CENTER("topCenter"),
        @SerializedName("topRight")
        TOP_RIGHT("topRight"),
        @SerializedName("middleLeft")
        MIDDLE_LEFT("middleLeft"),
        @SerializedName("middleCenter")
        MIDDLE_CENTER("middleCenter"),
        @SerializedName("middleRight")
        MIDDLE_RIGHT("middleRight"),
        @SerializedName("bottomLeft")
        BOTTOM_LEFT("bottomLeft"),
        @SerializedName("bottomCenter")
        BOTTOM_CENTER("bottomCenter"),
        @SerializedName("bottomRight")
        BOTTOM_RIGHT("bottomRight");
        private final String value;
        private final static Map<String, OutputCanvasAnchor> CONSTANTS = new HashMap<String, OutputCanvasAnchor>();

        static {
            for (OutputCanvasAnchor c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        OutputCanvasAnchor(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static OutputCanvasAnchor fromValue(String value) {
            OutputCanvasAnchor constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum PagesPerSheet {

        @SerializedName("oneUp")
        ONE_UP("oneUp"),
        @SerializedName("twoUp")
        TWO_UP("twoUp"),
        @SerializedName("fourUp")
        FOUR_UP("fourUp"),
        @SerializedName("eightUp")
        EIGHT_UP("eightUp");
        private final String value;
        private final static Map<String, PagesPerSheet> CONSTANTS = new HashMap<String, PagesPerSheet>();

        static {
            for (PagesPerSheet c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        PagesPerSheet(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static PagesPerSheet fromValue(String value) {
            PagesPerSheet constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
