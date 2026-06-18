
package com.hp.ws.cdm.jobticket;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Layout {

    @SerializedName("fitOption")
    @Expose
    private FitOption fitOption;
    @SerializedName("selection")
    @Expose
    private Selection selection;

    public FitOption getFitOption() {
        return fitOption;
    }

    public void setFitOption(FitOption fitOption) {
        this.fitOption = fitOption;
    }

    public Selection getSelection() {
        return selection;
    }

    public void setSelection(Selection selection) {
        this.selection = selection;
    }

    public enum FitOption {

        @SerializedName("cropToFit")
        CROP_TO_FIT("cropToFit"),
        @SerializedName("scaleToFit")
        SCALE_TO_FIT("scaleToFit");
        private final String value;
        private final static Map<String, FitOption> CONSTANTS = new HashMap<String, FitOption>();

        static {
            for (FitOption c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        FitOption(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static FitOption fromValue(String value) {
            FitOption constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum Selection {

        @SerializedName("layout4X5Inches")
        LAYOUT_4_X_5_INCHES("layout4X5Inches"),
        @SerializedName("layout4X6Inches")
        LAYOUT_4_X_6_INCHES("layout4X6Inches"),
        @SerializedName("layout5X7Inches")
        LAYOUT_5_X_7_INCHES("layout5X7Inches"),
        @SerializedName("layoutLetter8Dot5X11Inches")
        LAYOUT_LETTER_8_DOT_5_X_11_INCHES("layoutLetter8Dot5X11Inches"),
        @SerializedName("layout4X5InchesOn8Dot5X11Inches")
        LAYOUT_4_X_5_INCHES_ON_8_DOT_5_X_11_INCHES("layout4X5InchesOn8Dot5X11Inches"),
        @SerializedName("layout4X6InchesOn8Dot5X11Inches")
        LAYOUT_4_X_6_INCHES_ON_8_DOT_5_X_11_INCHES("layout4X6InchesOn8Dot5X11Inches"),
        @SerializedName("layout5X7InchesOn8Dot5X11Inches")
        LAYOUT_5_X_7_INCHES_ON_8_DOT_5_X_11_INCHES("layout5X7InchesOn8Dot5X11Inches"),
        @SerializedName("layout3Dot5X5InchesOn8Dot5X11Inches")
        LAYOUT_3_DOT_5_X_5_INCHES_ON_8_DOT_5_X_11_INCHES("layout3Dot5X5InchesOn8Dot5X11Inches"),
        @SerializedName("layout8X10InchesOn8Dot5X11Inches")
        LAYOUT_8_X_10_INCHES_ON_8_DOT_5_X_11_INCHES("layout8X10InchesOn8Dot5X11Inches"),
        @SerializedName("layoutIndexOn8Dot5X11Inches")
        LAYOUT_INDEX_ON_8_DOT_5_X_11_INCHES("layoutIndexOn8Dot5X11Inches"),
        @SerializedName("layout11X14Inches")
        LAYOUT_11_X_14_INCHES("layout11X14Inches"),
        @SerializedName("layoutTabloid11X17Inches")
        LAYOUT_TABLOID_11_X_17_INCHES("layoutTabloid11X17Inches"),
        @SerializedName("layout13X19Inches")
        LAYOUT_13_X_19_INCHES("layout13X19Inches"),
        @SerializedName("layout4X6InchesOn13X19Inches")
        LAYOUT_4_X_6_INCHES_ON_13_X_19_INCHES("layout4X6InchesOn13X19Inches"),
        @SerializedName("layout5X7InchesOn13X19Inches")
        LAYOUT_5_X_7_INCHES_ON_13_X_19_INCHES("layout5X7InchesOn13X19Inches"),
        @SerializedName("layout8X10InchesOn13X19Inches")
        LAYOUT_8_X_10_INCHES_ON_13_X_19_INCHES("layout8X10InchesOn13X19Inches"),
        @SerializedName("layoutLandscape13X19InchesStoryboard")
        LAYOUT_LANDSCAPE_13_X_19_INCHES_STORYBOARD("layoutLandscape13X19InchesStoryboard"),
        @SerializedName("layoutPortrait13X19InchesStoryboard")
        LAYOUT_PORTRAIT_13_X_19_INCHES_STORYBOARD("layoutPortrait13X19InchesStoryboard"),
        @SerializedName("layout10X13Cm")
        LAYOUT_10_X_13_CM("layout10X13Cm"),
        @SerializedName("layout10X15Cm")
        LAYOUT_10_X_15_CM("layout10X15Cm"),
        @SerializedName("layout13X18Cm")
        LAYOUT_13_X_18_CM("layout13X18Cm"),
        @SerializedName("layoutA4")
        LAYOUT_A_4("layoutA4"),
        @SerializedName("layout10X13CmOnA4")
        LAYOUT_10_X_13_CM_ON_A_4("layout10X13CmOnA4"),
        @SerializedName("layout10X15CmOnA4")
        LAYOUT_10_X_15_CM_ON_A_4("layout10X15CmOnA4"),
        @SerializedName("layout13X18CmOnA4")
        LAYOUT_13_X_18_CM_ON_A_4("layout13X18CmOnA4"),
        @SerializedName("layout9X13CmOnA4")
        LAYOUT_9_X_13_CM_ON_A_4("layout9X13CmOnA4"),
        @SerializedName("layout20X25CmOnA4")
        LAYOUT_20_X_25_CM_ON_A_4("layout20X25CmOnA4"),
        @SerializedName("layoutIndexOnA4")
        LAYOUT_INDEX_ON_A_4("layoutIndexOnA4"),
        @SerializedName("layoutA3")
        LAYOUT_A_3("layoutA3"),
        @SerializedName("layout10X15CmOnA3")
        LAYOUT_10_X_15_CM_ON_A_3("layout10X15CmOnA3"),
        @SerializedName("layout13X18CmOnA3")
        LAYOUT_13_X_18_CM_ON_A_3("layout13X18CmOnA3"),
        @SerializedName("layout9X13CmOnA3")
        LAYOUT_9_X_13_CM_ON_A_3("layout9X13CmOnA3"),
        @SerializedName("layout20X25CmOnA3")
        LAYOUT_20_X_25_CM_ON_A_3("layout20X25CmOnA3"),
        @SerializedName("layoutA4OnA3")
        LAYOUT_A_4_ON_A_3("layoutA4OnA3"),
        @SerializedName("layoutLandscapeA3Storyboard")
        LAYOUT_LANDSCAPE_A_3_STORYBOARD("layoutLandscapeA3Storyboard"),
        @SerializedName("layoutPortraitA3Storyboard")
        LAYOUT_PORTRAIT_A_3_STORYBOARD("layoutPortraitA3Storyboard"),
        @SerializedName("layoutL89X127Mm")
        LAYOUT_L_89_X_127_MM("layoutL89X127Mm"),
        @SerializedName("layout2L127X178Mm")
        LAYOUT_2_L_127_X_178_MM("layout2L127X178Mm"),
        @SerializedName("layoutHagaki100X148Mm")
        LAYOUT_HAGAKI_100_X_148_MM("layoutHagaki100X148Mm"),
        @SerializedName("layoutLOnA4")
        LAYOUT_L_ON_A_4("layoutLOnA4"),
        @SerializedName("layoutHagakiOnA4")
        LAYOUT_HAGAKI_ON_A_4("layoutHagakiOnA4"),
        @SerializedName("layout2LOnA4")
        LAYOUT_2_L_ON_A_4("layout2LOnA4"),
        @SerializedName("layoutMutsugiriOnA4")
        LAYOUT_MUTSUGIRI_ON_A_4("layoutMutsugiriOnA4"),
        @SerializedName("layoutHagakiOnA3")
        LAYOUT_HAGAKI_ON_A_3("layoutHagakiOnA3"),
        @SerializedName("layout2LOnA3")
        LAYOUT_2_L_ON_A_3("layout2LOnA3"),
        @SerializedName("layoutLOnA3")
        LAYOUT_L_ON_A_3("layoutLOnA3"),
        @SerializedName("layoutMutsugiriOnA3")
        LAYOUT_MUTSUGIRI_ON_A_3("layoutMutsugiriOnA3"),
        @SerializedName("layout4X5InchesOnA4")
        LAYOUT_4_X_5_INCHES_ON_A_4("layout4X5InchesOnA4"),
        @SerializedName("layout4X6InchesOnA4")
        LAYOUT_4_X_6_INCHES_ON_A_4("layout4X6InchesOnA4"),
        @SerializedName("layout5X7InchesOnA4")
        LAYOUT_5_X_7_INCHES_ON_A_4("layout5X7InchesOnA4"),
        @SerializedName("layout3Dot5X5InchesOnA4")
        LAYOUT_3_DOT_5_X_5_INCHES_ON_A_4("layout3Dot5X5InchesOnA4"),
        @SerializedName("layout8X10InchesOnA4")
        LAYOUT_8_X_10_INCHES_ON_A_4("layout8X10InchesOnA4");
        private final String value;
        private final static Map<String, Selection> CONSTANTS = new HashMap<String, Selection>();

        static {
            for (Selection c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Selection(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Selection fromValue(String value) {
            Selection constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
