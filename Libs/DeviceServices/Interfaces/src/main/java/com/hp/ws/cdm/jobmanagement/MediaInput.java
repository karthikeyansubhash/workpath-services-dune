
package com.hp.ws.cdm.jobmanagement;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MediaInput {

    /**
     * Units of measure for media capacity - TBD: for Common Glossary.
     * 
     */
    @SerializedName("capacityUnit")
    @Expose
    private MediaInput.CapacityUnit capacityUnit;
    /**
     * The current capacity of the input sub-unit in input sub-unit capacity units
     * 
     */
    @SerializedName("currentLevel")
    @Expose
    private Integer currentLevel;
    /**
     * Media size, values according to the standard PWG5101.1 or HP extension pattern for exceptions
     * 
     */
    @SerializedName("currentMediaSize")
    @Expose
    private MediaInput.MediaSize currentMediaSize;
    /**
     * Media type,  values according to the standard PWG5101.1
     * 
     */
    @SerializedName("currentMediaType")
    @Expose
    private MediaInput.MediaType currentMediaType;
    /**
     * The type of technology employed by the input sub-unit
     * 
     */
    @SerializedName("inputType")
    @Expose
    private MediaInput.InputType inputType;
    /**
     * The maximum capacity of the input sub-unit in input sub-unit capacity units
     * 
     */
    @SerializedName("maxCapacity")
    @Expose
    private Integer maxCapacity;
    /**
     * a unique ID used by this printer to identify this input sub-unit from PWG 5100.13
     * 
     */
    @SerializedName("mediaSourceId")
    @Expose
    private MediaInput.MediaSourceId mediaSourceId;
    /**
     * The current status of this input sub-unit
     * 
     */
    @SerializedName("status")
    @Expose
    private MediaInput.Status status;

    /**
     * Units of measure for media capacity - TBD: for Common Glossary.
     * 
     */
    public MediaInput.CapacityUnit getCapacityUnit() {
        return capacityUnit;
    }

    /**
     * Units of measure for media capacity - TBD: for Common Glossary.
     * 
     */
    public void setCapacityUnit(MediaInput.CapacityUnit capacityUnit) {
        this.capacityUnit = capacityUnit;
    }

    /**
     * The current capacity of the input sub-unit in input sub-unit capacity units
     * 
     */
    public Integer getCurrentLevel() {
        return currentLevel;
    }

    /**
     * The current capacity of the input sub-unit in input sub-unit capacity units
     * 
     */
    public void setCurrentLevel(Integer currentLevel) {
        this.currentLevel = currentLevel;
    }

    /**
     * Media size, values according to the standard PWG5101.1 or HP extension pattern for exceptions
     * 
     */
    public MediaInput.MediaSize getCurrentMediaSize() {
        return currentMediaSize;
    }

    /**
     * Media size, values according to the standard PWG5101.1 or HP extension pattern for exceptions
     * 
     */
    public void setCurrentMediaSize(MediaInput.MediaSize currentMediaSize) {
        this.currentMediaSize = currentMediaSize;
    }

    /**
     * Media type,  values according to the standard PWG5101.1
     * 
     */
    public MediaInput.MediaType getCurrentMediaType() {
        return currentMediaType;
    }

    /**
     * Media type,  values according to the standard PWG5101.1
     * 
     */
    public void setCurrentMediaType(MediaInput.MediaType currentMediaType) {
        this.currentMediaType = currentMediaType;
    }

    /**
     * The type of technology employed by the input sub-unit
     * 
     */
    public MediaInput.InputType getInputType() {
        return inputType;
    }

    /**
     * The type of technology employed by the input sub-unit
     * 
     */
    public void setInputType(MediaInput.InputType inputType) {
        this.inputType = inputType;
    }

    /**
     * The maximum capacity of the input sub-unit in input sub-unit capacity units
     * 
     */
    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    /**
     * The maximum capacity of the input sub-unit in input sub-unit capacity units
     * 
     */
    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    /**
     * a unique ID used by this printer to identify this input sub-unit from PWG 5100.13
     * 
     */
    public MediaInput.MediaSourceId getMediaSourceId() {
        return mediaSourceId;
    }

    /**
     * a unique ID used by this printer to identify this input sub-unit from PWG 5100.13
     * 
     */
    public void setMediaSourceId(MediaInput.MediaSourceId mediaSourceId) {
        this.mediaSourceId = mediaSourceId;
    }

    /**
     * The current status of this input sub-unit
     * 
     */
    public MediaInput.Status getStatus() {
        return status;
    }

    /**
     * The current status of this input sub-unit
     * 
     */
    public void setStatus(MediaInput.Status status) {
        this.status = status;
    }


    /**
     * Units of measure for media capacity - TBD: for Common Glossary.
     * 
     */
    public enum CapacityUnit {

        @SerializedName("micrometers")
        MICROMETERS("micrometers"),
        @SerializedName("sheets")
        SHEETS("sheets"),
        @SerializedName("tenThousandthsOfInches")
        TEN_THOUSANDTHS_OF_INCHES("tenThousandthsOfInches"),
        @SerializedName("feet")
        FEET("feet"),
        @SerializedName("meters")
        METERS("meters");
        private final String value;
        private final static Map<String, MediaInput.CapacityUnit> CONSTANTS = new HashMap<String, MediaInput.CapacityUnit>();

        static {
            for (MediaInput.CapacityUnit c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        CapacityUnit(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static MediaInput.CapacityUnit fromValue(String value) {
            MediaInput.CapacityUnit constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * The type of technology employed by the input sub-unit
     * 
     */
    public enum InputType {

        @SerializedName("other")
        OTHER("other"),
        @SerializedName("unknown")
        UNKNOWN("unknown"),
        @SerializedName("sheetFeedAutoRemovableTray")
        SHEET_FEED_AUTO_REMOVABLE_TRAY("sheetFeedAutoRemovableTray"),
        @SerializedName("sheetFeedAutoNonRemovableTray")
        SHEET_FEED_AUTO_NON_REMOVABLE_TRAY("sheetFeedAutoNonRemovableTray"),
        @SerializedName("sheetFeedManual")
        SHEET_FEED_MANUAL("sheetFeedManual"),
        @SerializedName("continuousRoll")
        CONTINUOUS_ROLL("continuousRoll");
        private final String value;
        private final static Map<String, MediaInput.InputType> CONSTANTS = new HashMap<String, MediaInput.InputType>();

        static {
            for (MediaInput.InputType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        InputType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static MediaInput.InputType fromValue(String value) {
            MediaInput.InputType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Media size, values according to the standard PWG5101.1 or HP extension pattern for exceptions
     * 
     */
    public enum MediaSize {

        @SerializedName("any")
        ANY("any"),
        @SerializedName("anycustom")
        ANYCUSTOM("anycustom"),
        @SerializedName("asme_f_28x40in")
        ASME_F_28_X_40_IN("asme_f_28x40in"),
        @SerializedName("custom")
        CUSTOM("custom"),
        @SerializedName("iso_a0_841x1189mm")
        ISO_A_0_841_X_1189_MM("iso_a0_841x1189mm"),
        @SerializedName("iso_a1_594x841mm")
        ISO_A_1_594_X_841_MM("iso_a1_594x841mm"),
        @SerializedName("iso_a2_420x594mm")
        ISO_A_2_420_X_594_MM("iso_a2_420x594mm"),
        @SerializedName("iso_a3_297x420mm")
        ISO_A_3_297_X_420_MM("iso_a3_297x420mm"),
        @SerializedName("iso_a4_210x297mm")
        ISO_A_4_210_X_297_MM("iso_a4_210x297mm"),
        @SerializedName("iso_a5_148x210mm")
        ISO_A_5_148_X_210_MM("iso_a5_148x210mm"),
        @SerializedName("iso_a6_105x148mm")
        ISO_A_6_105_X_148_MM("iso_a6_105x148mm"),
        @SerializedName("iso_b0_1000x1414mm")
        ISO_B_0_1000_X_1414_MM("iso_b0_1000x1414mm"),
        @SerializedName("iso_b1_707x1000mm")
        ISO_B_1_707_X_1000_MM("iso_b1_707x1000mm"),
        @SerializedName("iso_b2_500x707mm")
        ISO_B_2_500_X_707_MM("iso_b2_500x707mm"),
        @SerializedName("iso_b3_353x500mm")
        ISO_B_3_353_X_500_MM("iso_b3_353x500mm"),
        @SerializedName("iso_b4_250x353mm")
        ISO_B_4_250_X_353_MM("iso_b4_250x353mm"),
        @SerializedName("iso_b5_176x250mm")
        ISO_B_5_176_X_250_MM("iso_b5_176x250mm"),
        @SerializedName("iso_b6_125x176mm")
        ISO_B_6_125_X_176_MM("iso_b6_125x176mm"),
        @SerializedName("iso_c0_917x1297mm")
        ISO_C_0_917_X_1297_MM("iso_c0_917x1297mm"),
        @SerializedName("iso_c1_648x917mm")
        ISO_C_1_648_X_917_MM("iso_c1_648x917mm"),
        @SerializedName("iso_c2_458x648mm")
        ISO_C_2_458_X_648_MM("iso_c2_458x648mm"),
        @SerializedName("iso_c3_324x458mm")
        ISO_C_3_324_X_458_MM("iso_c3_324x458mm"),
        @SerializedName("iso_c4_229x324mm")
        ISO_C_4_229_X_324_MM("iso_c4_229x324mm"),
        @SerializedName("iso_c5_162x229mm")
        ISO_C_5_162_X_229_MM("iso_c5_162x229mm"),
        @SerializedName("iso_c6_114x162mm")
        ISO_C_6_114_X_162_MM("iso_c6_114x162mm"),
        @SerializedName("iso_dl_110x220mm")
        ISO_DL_110_X_220_MM("iso_dl_110x220mm"),
        @SerializedName("iso_ra3_305x430mm")
        ISO_RA_3_305_X_430_MM("iso_ra3_305x430mm"),
        @SerializedName("iso_ra4_215x305mm")
        ISO_RA_4_215_X_305_MM("iso_ra4_215x305mm"),
        @SerializedName("iso_sra3_320x450mm")
        ISO_SRA_3_320_X_450_MM("iso_sra3_320x450mm"),
        @SerializedName("iso_sra4_225x320mm")
        ISO_SRA_4_225_X_320_MM("iso_sra4_225x320mm"),
        @SerializedName("jis_b0_1030x1456mm")
        JIS_B_0_1030_X_1456_MM("jis_b0_1030x1456mm"),
        @SerializedName("jis_b1_728x1030mm")
        JIS_B_1_728_X_1030_MM("jis_b1_728x1030mm"),
        @SerializedName("jis_b2_515x728mm")
        JIS_B_2_515_X_728_MM("jis_b2_515x728mm"),
        @SerializedName("jis_b3_364x515mm")
        JIS_B_3_364_X_515_MM("jis_b3_364x515mm"),
        @SerializedName("jis_b4_257x364mm")
        JIS_B_4_257_X_364_MM("jis_b4_257x364mm"),
        @SerializedName("jis_b5_182x257mm")
        JIS_B_5_182_X_257_MM("jis_b5_182x257mm"),
        @SerializedName("jis_b6_128x182mm")
        JIS_B_6_128_X_182_MM("jis_b6_128x182mm"),
        @SerializedName("jpn_chou3_120x235mm")
        JPN_CHOU_3_120_X_235_MM("jpn_chou3_120x235mm"),
        @SerializedName("jpn_chou4_90x205mm")
        JPN_CHOU_4_90_X_205_MM("jpn_chou4_90x205mm"),
        @SerializedName("jpn_hagaki_100x148mm")
        JPN_HAGAKI_100_X_148_MM("jpn_hagaki_100x148mm"),
        @SerializedName("jpn_photo-2l_127x177_8mm")
        JPN_PHOTO_2_L_127_X_177_8_MM("jpn_photo-2l_127x177_8mm"),
        @SerializedName("jpn_oufuku_148x200mm")
        JPN_OUFUKU_148_X_200_MM("jpn_oufuku_148x200mm"),
        @SerializedName("na_10x15_10x15in")
        NA_10_X_15_10_X_15_IN("na_10x15_10x15in"),
        @SerializedName("na_5x7_5x7in")
        NA_5_X_7_5_X_7_IN("na_5x7_5x7in"),
        @SerializedName("na_a2_4.375x5.75in")
        NA_A_2_4_375_X_5_75_IN("na_a2_4.375x5.75in"),
        @SerializedName("na_arch-a_9x12in")
        NA_ARCH_A_9_X_12_IN("na_arch-a_9x12in"),
        @SerializedName("na_arch-b_12x18in")
        NA_ARCH_B_12_X_18_IN("na_arch-b_12x18in"),
        @SerializedName("na_arch-c_18x24in")
        NA_ARCH_C_18_X_24_IN("na_arch-c_18x24in"),
        @SerializedName("na_arch-d_24x36in")
        NA_ARCH_D_24_X_36_IN("na_arch-d_24x36in"),
        @SerializedName("na_arch-e_36x48in")
        NA_ARCH_E_36_X_48_IN("na_arch-e_36x48in"),
        @SerializedName("na_arch-e2_26x38in")
        NA_ARCH_E_2_26_X_38_IN("na_arch-e2_26x38in"),
        @SerializedName("na_arch-e3_27x39in")
        NA_ARCH_E_3_27_X_39_IN("na_arch-e3_27x39in"),
        @SerializedName("na_c_17x22in")
        NA_C_17_X_22_IN("na_c_17x22in"),
        @SerializedName("na_d_22x34in")
        NA_D_22_X_34_IN("na_d_22x34in"),
        @SerializedName("na_e_34x44in")
        NA_E_34_X_44_IN("na_e_34x44in"),
        @SerializedName("na_edp_11x14in")
        NA_EDP_11_X_14_IN("na_edp_11x14in"),
        @SerializedName("na_executive_7.25x10.5in")
        NA_EXECUTIVE_7_25_X_10_5_IN("na_executive_7.25x10.5in"),
        @SerializedName("na_foolscap_8.5x13in")
        NA_FOOLSCAP_8_5_X_13_IN("na_foolscap_8.5x13in"),
        @SerializedName("na_govt-letter_8x10in")
        NA_GOVT_LETTER_8_X_10_IN("na_govt-letter_8x10in"),
        @SerializedName("na_index-3x5_3x5in")
        NA_INDEX_3_X_5_3_X_5_IN("na_index-3x5_3x5in"),
        @SerializedName("na_index-4x6_4x6in")
        NA_INDEX_4_X_6_4_X_6_IN("na_index-4x6_4x6in"),
        @SerializedName("na_index-5x8_5x8in")
        NA_INDEX_5_X_8_5_X_8_IN("na_index-5x8_5x8in"),
        @SerializedName("na_invoice_5.5x8.5in")
        NA_INVOICE_5_5_X_8_5_IN("na_invoice_5.5x8.5in"),
        @SerializedName("na_ledger_11x17in")
        NA_LEDGER_11_X_17_IN("na_ledger_11x17in"),
        @SerializedName("na_legal_8.5x14in")
        NA_LEGAL_8_5_X_14_IN("na_legal_8.5x14in"),
        @SerializedName("na_letter_8.5x11in")
        NA_LETTER_8_5_X_11_IN("na_letter_8.5x11in"),
        @SerializedName("na_monarch_3.875x7.5in")
        NA_MONARCH_3_875_X_7_5_IN("na_monarch_3.875x7.5in"),
        @SerializedName("na_number-10_4.125x9.5in")
        NA_NUMBER_10_4_125_X_9_5_IN("na_number-10_4.125x9.5in"),
        @SerializedName("na_number-9_3.875x8.875in")
        NA_NUMBER_9_3_875_X_8_875_IN("na_number-9_3.875x8.875in"),
        @SerializedName("na_oficio_8.5x13.4in")
        NA_OFICIO_8_5_X_13_4_IN("na_oficio_8.5x13.4in"),
        @SerializedName("na_personal_3.625x6.5in")
        NA_PERSONAL_3_625_X_6_5_IN("na_personal_3.625x6.5in"),
        @SerializedName("na_super-b_13x19in")
        NA_SUPER_B_13_X_19_IN("na_super-b_13x19in"),
        @SerializedName("na_wide-format_30x42in")
        NA_WIDE_FORMAT_30_X_42_IN("na_wide-format_30x42in"),
        @SerializedName("oe_12x16_12x16in")
        OE_12_X_16_12_X_16_IN("oe_12x16_12x16in"),
        @SerializedName("oe_14x17_14x17in")
        OE_14_X_17_14_X_17_IN("oe_14x17_14x17in"),
        @SerializedName("oe_18x22_18x22in")
        OE_18_X_22_18_X_22_IN("oe_18x22_18x22in"),
        @SerializedName("oe_photo-l_3.5x5in")
        OE_PHOTO_L_3_5_X_5_IN("oe_photo-l_3.5x5in"),
        @SerializedName("oe_photo-4x12_4x12in")
        OE_PHOTO_4_X_12_4_X_12_IN("oe_photo-4x12_4x12in"),
        @SerializedName("oe_photo-4x5_4x5in")
        OE_PHOTO_4_X_5_4_X_5_IN("oe_photo-4x5_4x5in"),
        @SerializedName("oe_photo-10r_10x12in")
        OE_PHOTO_10_R_10_X_12_IN("oe_photo-10r_10x12in"),
        @SerializedName("oe_photo-14x18_14x18in")
        OE_PHOTO_14_X_18_14_X_18_IN("oe_photo-14x18_14x18in"),
        @SerializedName("oe_photo-16r_16x20in")
        OE_PHOTO_16_R_16_X_20_IN("oe_photo-16r_16x20in"),
        @SerializedName("oe_photo-20r_20x24in")
        OE_PHOTO_20_R_20_X_24_IN("oe_photo-20r_20x24in"),
        @SerializedName("oe_photo-22x28_22x28in")
        OE_PHOTO_22_X_28_22_X_28_IN("oe_photo-22x28_22x28in"),
        @SerializedName("oe_photo-24x30_24x30in")
        OE_PHOTO_24_X_30_24_X_30_IN("oe_photo-24x30_24x30in"),
        @SerializedName("oe_square-photo_5x5in")
        OE_SQUARE_PHOTO_5_X_5_IN("oe_square-photo_5x5in"),
        @SerializedName("om_16k_184x260mm")
        OM_16_K_184_X_260_MM("om_16k_184x260mm"),
        @SerializedName("om_16k_195x270mm")
        OM_16_K_195_X_270_MM("om_16k_195x270mm"),
        @SerializedName("om_8k_260x368mm")
        OM_8_K_260_X_368_MM("om_8k_260x368mm"),
        @SerializedName("om_8k_270x390mm")
        OM_8_K_270_X_390_MM("om_8k_270x390mm"),
        @SerializedName("om_photo-30x40_300x400mm")
        OM_PHOTO_30_X_40_300_X_400_MM("om_photo-30x40_300x400mm"),
        @SerializedName("om_photo-30x45_300x450mm")
        OM_PHOTO_30_X_45_300_X_450_MM("om_photo-30x45_300x450mm"),
        @SerializedName("om_photo-35x46_350x460mm")
        OM_PHOTO_35_X_46_350_X_460_MM("om_photo-35x46_350x460mm"),
        @SerializedName("om_photo-40x60_400x600mm")
        OM_PHOTO_40_X_60_400_X_600_MM("om_photo-40x60_400x600mm"),
        @SerializedName("om_photo-50x76_500x760mm")
        OM_PHOTO_50_X_76_500_X_760_MM("om_photo-50x76_500x760mm"),
        @SerializedName("om_photo-60x90_600x900mm")
        OM_PHOTO_60_X_90_600_X_900_MM("om_photo-60x90_600x900mm"),
        @SerializedName("om_small-photo_100x150mm")
        OM_SMALL_PHOTO_100_X_150_MM("om_small-photo_100x150mm"),
        @SerializedName("om_medium-photo_130x180mm")
        OM_MEDIUM_PHOTO_130_X_180_MM("om_medium-photo_130x180mm"),
        @SerializedName("roc_16k_7.75x10.75in")
        ROC_16_K_7_75_X_10_75_IN("roc_16k_7.75x10.75in"),
        @SerializedName("roc_8k_10.75x15.5in")
        ROC_8_K_10_75_X_15_5_IN("roc_8k_10.75x15.5in"),
        @SerializedName("com.hp.ext.mediaSize.iso_a4_210x297mm.rotated")
        COM_HP_EXT_MEDIA_SIZE_ISO_A_4_210_X_297_MM_ROTATED("com.hp.ext.mediaSize.iso_a4_210x297mm.rotated"),
        @SerializedName("com.hp.ext.mediaSize.na_letter_8.5x11in.rotated")
        COM_HP_EXT_MEDIA_SIZE_NA_LETTER_8_5_X_11_IN_ROTATED("com.hp.ext.mediaSize.na_letter_8.5x11in.rotated"),
        @SerializedName("com.hp.ext.mediaSize.iso_a5_148x210mm.rotated")
        COM_HP_EXT_MEDIA_SIZE_ISO_A_5_148_X_210_MM_ROTATED("com.hp.ext.mediaSize.iso_a5_148x210mm.rotated"),
        @SerializedName("com.hp.ext.mediaSize.jis_b5_182x257mm.rotated")
        COM_HP_EXT_MEDIA_SIZE_JIS_B_5_182_X_257_MM_ROTATED("com.hp.ext.mediaSize.jis_b5_182x257mm.rotated"),
        @SerializedName("com.hp.ext.mediaSize.jpn_oufuku_148x200mm.rotated")
        COM_HP_EXT_MEDIA_SIZE_JPN_OUFUKU_148_X_200_MM_ROTATED("com.hp.ext.mediaSize.jpn_oufuku_148x200mm.rotated"),
        @SerializedName("com.hp.ext.mediaSize.mixed-letter-legal")
        COM_HP_EXT_MEDIA_SIZE_MIXED_LETTER_LEGAL("com.hp.ext.mediaSize.mixed-letter-legal"),
        @SerializedName("com.hp.ext.mediaSize.mixed-letter-ledger")
        COM_HP_EXT_MEDIA_SIZE_MIXED_LETTER_LEDGER("com.hp.ext.mediaSize.mixed-letter-ledger"),
        @SerializedName("com.hp.ext.mediaSize.mixed-a4-a3")
        COM_HP_EXT_MEDIA_SIZE_MIXED_A_4_A_3("com.hp.ext.mediaSize.mixed-a4-a3"),
        @SerializedName("com.hp.ext.mediaSize.long-scan")
        COM_HP_EXT_MEDIA_SIZE_LONG_SCAN("com.hp.ext.mediaSize.long-scan");
        private final String value;
        private final static Map<String, MediaInput.MediaSize> CONSTANTS = new HashMap<String, MediaInput.MediaSize>();

        static {
            for (MediaInput.MediaSize c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        MediaSize(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static MediaInput.MediaSize fromValue(String value) {
            MediaInput.MediaSize constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * a unique ID used by this printer to identify this input sub-unit from PWG 5100.13
     * 
     */
    public enum MediaSourceId {

        @SerializedName("alternate")
        ALTERNATE("alternate"),
        @SerializedName("alternate-roll")
        ALTERNATE_ROLL("alternate-roll"),
        @SerializedName("auto")
        AUTO("auto"),
        @SerializedName("bottom")
        BOTTOM("bottom"),
        @SerializedName("by-pass-tray")
        BY_PASS_TRAY("by-pass-tray"),
        @SerializedName("center")
        CENTER("center"),
        @SerializedName("disc")
        DISC("disc"),
        @SerializedName("duplexer")
        DUPLEXER("duplexer"),
        @SerializedName("envelope")
        ENVELOPE("envelope"),
        @SerializedName("flat-bed")
        FLAT_BED("flat-bed"),
        @SerializedName("hagaki")
        HAGAKI("hagaki"),
        @SerializedName("large-capacity")
        LARGE_CAPACITY("large-capacity"),
        @SerializedName("left")
        LEFT("left"),
        @SerializedName("main")
        MAIN("main"),
        @SerializedName("main-roll")
        MAIN_ROLL("main-roll"),
        @SerializedName("manual")
        MANUAL("manual"),
        @SerializedName("middle")
        MIDDLE("middle"),
        @SerializedName("photo")
        PHOTO("photo"),
        @SerializedName("rear")
        REAR("rear"),
        @SerializedName("right")
        RIGHT("right"),
        @SerializedName("roll")
        ROLL("roll"),
        @SerializedName("roll-1")
        ROLL_1("roll-1"),
        @SerializedName("roll-10")
        ROLL_10("roll-10"),
        @SerializedName("roll-11")
        ROLL_11("roll-11"),
        @SerializedName("roll-2")
        ROLL_2("roll-2"),
        @SerializedName("roll-3")
        ROLL_3("roll-3"),
        @SerializedName("roll-4")
        ROLL_4("roll-4"),
        @SerializedName("roll-5")
        ROLL_5("roll-5"),
        @SerializedName("roll-6")
        ROLL_6("roll-6"),
        @SerializedName("roll-7")
        ROLL_7("roll-7"),
        @SerializedName("roll-8")
        ROLL_8("roll-8"),
        @SerializedName("roll-9")
        ROLL_9("roll-9"),
        @SerializedName("side")
        SIDE("side"),
        @SerializedName("textile")
        TEXTILE("textile"),
        @SerializedName("top")
        TOP("top"),
        @SerializedName("tray-1")
        TRAY_1("tray-1"),
        @SerializedName("tray-10")
        TRAY_10("tray-10"),
        @SerializedName("tray-11")
        TRAY_11("tray-11"),
        @SerializedName("tray-12")
        TRAY_12("tray-12"),
        @SerializedName("tray-13")
        TRAY_13("tray-13"),
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
        TRAY_9("tray-9");
        private final String value;
        private final static Map<String, MediaInput.MediaSourceId> CONSTANTS = new HashMap<String, MediaInput.MediaSourceId>();

        static {
            for (MediaInput.MediaSourceId c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        MediaSourceId(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static MediaInput.MediaSourceId fromValue(String value) {
            MediaInput.MediaSourceId constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Media type,  values according to the standard PWG5101.1
     * 
     */
    public enum MediaType {

        @SerializedName("any")
        ANY("any"),
        @SerializedName("cardstock")
        CARDSTOCK("cardstock"),
        @SerializedName("com.hp.advanced-photo")
        COM_HP_ADVANCED_PHOTO("com.hp.advanced-photo"),
        @SerializedName("com.hp.brochure-glossy")
        COM_HP_BROCHURE_GLOSSY("com.hp.brochure-glossy"),
        @SerializedName("com.hp.brochure-matte")
        COM_HP_BROCHURE_MATTE("com.hp.brochure-matte"),
        @SerializedName("com.hp.cardstock-glossy")
        COM_HP_CARDSTOCK_GLOSSY("com.hp.cardstock-glossy"),
        @SerializedName("com.hp.EcoSMARTLite")
        COM_HP_ECO_SMART_LITE("com.hp.EcoSMARTLite"),
        @SerializedName("com.hp.extra-heavy")
        COM_HP_EXTRA_HEAVY("com.hp.extra-heavy"),
        @SerializedName("com.hp.extra-heavy-gloss")
        COM_HP_EXTRA_HEAVY_GLOSS("com.hp.extra-heavy-gloss"),
        @SerializedName("com.hp.film-opaque")
        COM_HP_FILM_OPAQUE("com.hp.film-opaque"),
        @SerializedName("com.hp.glossy-130gsm")
        COM_HP_GLOSSY_130_GSM("com.hp.glossy-130gsm"),
        @SerializedName("com.hp.glossy-160gsm")
        COM_HP_GLOSSY_160_GSM("com.hp.glossy-160gsm"),
        @SerializedName("com.hp.glossy-220gsm")
        COM_HP_GLOSSY_220_GSM("com.hp.glossy-220gsm"),
        @SerializedName("com.hp.heavy-glossy")
        COM_HP_HEAVY_GLOSSY("com.hp.heavy-glossy"),
        @SerializedName("com.hp.heavy-rough")
        COM_HP_HEAVY_ROUGH("com.hp.heavy-rough"),
        @SerializedName("com.hp.heavypaperboard")
        COM_HP_HEAVYPAPERBOARD("com.hp.heavypaperboard"),
        @SerializedName("com.hp.intermediate")
        COM_HP_INTERMEDIATE("com.hp.intermediate"),
        @SerializedName("com.hp.lightpaperboard")
        COM_HP_LIGHTPAPERBOARD("com.hp.lightpaperboard"),
        @SerializedName("com.hp.matte-105gsm")
        COM_HP_MATTE_105_GSM("com.hp.matte-105gsm"),
        @SerializedName("com.hp.matte-120gsm")
        COM_HP_MATTE_120_GSM("com.hp.matte-120gsm"),
        @SerializedName("com.hp.matte-160gsm")
        COM_HP_MATTE_160_GSM("com.hp.matte-160gsm"),
        @SerializedName("com.hp.matte-200gsm")
        COM_HP_MATTE_200_GSM("com.hp.matte-200gsm"),
        @SerializedName("com.hp.matte-90gsm")
        COM_HP_MATTE_90_GSM("com.hp.matte-90gsm"),
        @SerializedName("com.hp.midweight")
        COM_HP_MIDWEIGHT("com.hp.midweight"),
        @SerializedName("com.hp.midweight-glossy")
        COM_HP_MIDWEIGHT_GLOSSY("com.hp.midweight-glossy"),
        @SerializedName("com.hp.paperboard")
        COM_HP_PAPERBOARD("com.hp.paperboard"),
        @SerializedName("com.hp.recycled")
        COM_HP_RECYCLED("com.hp.recycled"),
        @SerializedName("com.hp.soft-gloss-120gsm")
        COM_HP_SOFT_GLOSS_120_GSM("com.hp.soft-gloss-120gsm"),
        @SerializedName("com.hp-stationery-thick")
        COM_HP_STATIONERY_THICK("com.hp-stationery-thick"),
        @SerializedName("com.hp.rough")
        COM_HP_ROUGH("com.hp.rough"),
        @SerializedName("com.hp-bright-white-stationery-inkjet-paper")
        COM_HP_BRIGHT_WHITE_STATIONERY_INKJET_PAPER("com.hp-bright-white-stationery-inkjet-paper"),
        @SerializedName("com.hp-clear-film")
        COM_HP_CLEAR_FILM("com.hp-clear-film"),
        @SerializedName("com.hp-everyday-adhesive-gloss-polypropylene-labels")
        COM_HP_EVERYDAY_ADHESIVE_GLOSS_POLYPROPYLENE_LABELS("com.hp-everyday-adhesive-gloss-polypropylene-labels"),
        @SerializedName("com.hp-everyday-adhesive-polypropylene-labels-matte")
        COM_HP_EVERYDAY_ADHESIVE_POLYPROPYLENE_LABELS_MATTE("com.hp-everyday-adhesive-polypropylene-labels-matte"),
        @SerializedName("com.hp-everyday-matte-polypropylene")
        COM_HP_EVERYDAY_MATTE_POLYPROPYLENE("com.hp-everyday-matte-polypropylene"),
        @SerializedName("com.hp-production-matte-polypropylene")
        COM_HP_PRODUCTION_MATTE_POLYPROPYLENE("com.hp-production-matte-polypropylene"),
        @SerializedName("com.hp-matte-film")
        COM_HP_MATTE_FILM("com.hp-matte-film"),
        @SerializedName("com.hp-matte-inkjet")
        COM_HP_MATTE_INKJET("com.hp-matte-inkjet"),
        @SerializedName("com.hp-matte-photo-duplex")
        COM_HP_MATTE_PHOTO_DUPLEX("com.hp-matte-photo-duplex"),
        @SerializedName("com.hp-matte-presentation")
        COM_HP_MATTE_PRESENTATION("com.hp-matte-presentation"),
        @SerializedName("com.hp-matte-brochure")
        COM_HP_MATTE_BROCHURE("com.hp-matte-brochure"),
        @SerializedName("com.hp-production-matte-poster-paper")
        COM_HP_PRODUCTION_MATTE_POSTER_PAPER("com.hp-production-matte-poster-paper"),
        @SerializedName("com.hp-production-satin-poster-paper")
        COM_HP_PRODUCTION_SATIN_POSTER_PAPER("com.hp-production-satin-poster-paper"),
        @SerializedName("com.hp-natural-tracing-paper-stationery-lightweight")
        COM_HP_NATURAL_TRACING_PAPER_STATIONERY_LIGHTWEIGHT("com.hp-natural-tracing-paper-stationery-lightweight"),
        @SerializedName("com.hp-photographic-glossy")
        COM_HP_PHOTOGRAPHIC_GLOSSY("com.hp-photographic-glossy"),
        @SerializedName("com.hp-photographic-inkjet")
        COM_HP_PHOTOGRAPHIC_INKJET("com.hp-photographic-inkjet"),
        @SerializedName("com.hp-premium-matte-polypropylene")
        COM_HP_PREMIUM_MATTE_POLYPROPYLENE("com.hp-premium-matte-polypropylene"),
        @SerializedName("com.hp-specialty-glossy")
        COM_HP_SPECIALTY_GLOSSY("com.hp-specialty-glossy"),
        @SerializedName("com.hp-specialty-glossy-inkjet")
        COM_HP_SPECIALTY_GLOSSY_INKJET("com.hp-specialty-glossy-inkjet"),
        @SerializedName("com.hp-specialty-hagaki")
        COM_HP_SPECIALTY_HAGAKI("com.hp-specialty-hagaki"),
        @SerializedName("com.hp-stationery-bond-universal-paper")
        COM_HP_STATIONERY_BOND_UNIVERSAL_PAPER("com.hp-stationery-bond-universal-paper"),
        @SerializedName("com.hp-20lb-bond-colorpro-technology")
        COM_HP_20_LB_BOND_COLORPRO_TECHNOLOGY("com.hp-20lb-bond-colorpro-technology"),
        @SerializedName("com.hp-premium-bond-paper")
        COM_HP_PREMIUM_BOND_PAPER("com.hp-premium-bond-paper"),
        @SerializedName("com.hp-bright-white-bond-paper")
        COM_HP_BRIGHT_WHITE_BOND_PAPER("com.hp-bright-white-bond-paper"),
        @SerializedName("com.hp-stationery-coated-paper")
        COM_HP_STATIONERY_COATED_PAPER("com.hp-stationery-coated-paper"),
        @SerializedName("com.hp-stationery-heavyweight-coated")
        COM_HP_STATIONERY_HEAVYWEIGHT_COATED("com.hp-stationery-heavyweight-coated"),
        @SerializedName("com.hp-stationery-light")
        COM_HP_STATIONERY_LIGHT("com.hp-stationery-light"),
        @SerializedName("com.hp-trifold-brochure-glossy-150gsm")
        COM_HP_TRIFOLD_BROCHURE_GLOSSY_150_GSM("com.hp-trifold-brochure-glossy-150gsm"),
        @SerializedName("com.hp-trifold-brochure-glossy-180gsm")
        COM_HP_TRIFOLD_BROCHURE_GLOSSY_180_GSM("com.hp-trifold-brochure-glossy-180gsm"),
        @SerializedName("com.hp-universal-adhesive-vinyl-labels")
        COM_HP_UNIVERSAL_ADHESIVE_VINYL_LABELS("com.hp-universal-adhesive-vinyl-labels"),
        @SerializedName("com.hp-universal-instant-dry-photographic-glossy-paper")
        COM_HP_UNIVERSAL_INSTANT_DRY_PHOTOGRAPHIC_GLOSSY_PAPER("com.hp-universal-instant-dry-photographic-glossy-paper"),
        @SerializedName("com.hp-universal-instant-dry-satin-photographic-semi-gloss-paper")
        COM_HP_UNIVERSAL_INSTANT_DRY_SATIN_PHOTOGRAPHIC_SEMI_GLOSS_PAPER("com.hp-universal-instant-dry-satin-photographic-semi-gloss-paper"),
        @SerializedName("com.hp-universal-photographic-glossy-paper")
        COM_HP_UNIVERSAL_PHOTOGRAPHIC_GLOSSY_PAPER("com.hp-universal-photographic-glossy-paper"),
        @SerializedName("com.hp-universal-photographic-satin-paper")
        COM_HP_UNIVERSAL_PHOTOGRAPHIC_SATIN_PAPER("com.hp-universal-photographic-satin-paper"),
        @SerializedName("com.hp-universal-stationery-coated-paper")
        COM_HP_UNIVERSAL_STATIONERY_COATED_PAPER("com.hp-universal-stationery-coated-paper"),
        @SerializedName("com.hp-universal-stationery-heavyweight-coated-paper")
        COM_HP_UNIVERSAL_STATIONERY_HEAVYWEIGHT_COATED_PAPER("com.hp-universal-stationery-heavyweight-coated-paper"),
        @SerializedName("com.hp-gloss-poster-paper")
        COM_HP_GLOSS_POSTER_PAPER("com.hp-gloss-poster-paper"),
        @SerializedName("com.hp-double-matte-film")
        COM_HP_DOUBLE_MATTE_FILM("com.hp-double-matte-film"),
        @SerializedName("com.hp-polyester-backlit")
        COM_HP_POLYESTER_BACKLIT("com.hp-polyester-backlit"),
        @SerializedName("com.hp-generic-self-adhesive-vinyl-paper")
        COM_HP_GENERIC_SELF_ADHESIVE_VINYL_PAPER("com.hp-generic-self-adhesive-vinyl-paper"),
        @SerializedName("com.hp-foldable-document-material")
        COM_HP_FOLDABLE_DOCUMENT_MATERIAL("com.hp-foldable-document-material"),
        @SerializedName("custom")
        CUSTOM("custom"),
        @SerializedName("custom-adhesive-backlit")
        CUSTOM_ADHESIVE_BACKLIT("custom-adhesive-backlit"),
        @SerializedName("custom-adhesive-polypropylene-labels-glossy")
        CUSTOM_ADHESIVE_POLYPROPYLENE_LABELS_GLOSSY("custom-adhesive-polypropylene-labels-glossy"),
        @SerializedName("custom-adhesive-polypropylene-labels-matte")
        CUSTOM_ADHESIVE_POLYPROPYLENE_LABELS_MATTE("custom-adhesive-polypropylene-labels-matte"),
        @SerializedName("custom-adhesive-polypropylene-labels")
        CUSTOM_ADHESIVE_POLYPROPYLENE_LABELS("custom-adhesive-polypropylene-labels"),
        @SerializedName("custom-blueprint-paper-with-red-stamps-stationery-colored")
        CUSTOM_BLUEPRINT_PAPER_WITH_RED_STAMPS_STATIONERY_COLORED("custom-blueprint-paper-with-red-stamps-stationery-colored"),
        @SerializedName("custom-canvas")
        CUSTOM_CANVAS("custom-canvas"),
        @SerializedName("custom-clear-film-Kod")
        CUSTOM_CLEAR_FILM_KOD("custom-clear-film-Kod"),
        @SerializedName("custom-coated-cad")
        CUSTOM_COATED_CAD("custom-coated-cad"),
        @SerializedName("custom-digital-blueprint-paper-stationery-colored")
        CUSTOM_DIGITAL_BLUEPRINT_PAPER_STATIONERY_COLORED("custom-digital-blueprint-paper-stationery-colored"),
        @SerializedName("custom-generic-natural-tracing-paper-over-65gsm-stationery-lightweight")
        CUSTOM_GENERIC_NATURAL_TRACING_PAPER_OVER_65_GSM_STATIONERY_LIGHTWEIGHT("custom-generic-natural-tracing-paper-over-65gsm-stationery-lightweight"),
        @SerializedName("custom-generic-natural-tracing-paper-under-65gsm-stationery-lightweight")
        CUSTOM_GENERIC_NATURAL_TRACING_PAPER_UNDER_65_GSM_STATIONERY_LIGHTWEIGHT("custom-generic-natural-tracing-paper-under-65gsm-stationery-lightweight"),
        @SerializedName("custom-generic-natural-tracing-paper-stationery-lightweight")
        CUSTOM_GENERIC_NATURAL_TRACING_PAPER_STATIONERY_LIGHTWEIGHT("custom-generic-natural-tracing-paper-stationery-lightweight"),
        @SerializedName("custom-heat-transfer")
        CUSTOM_HEAT_TRANSFER("custom-heat-transfer"),
        @SerializedName("custom-matte-film")
        CUSTOM_MATTE_FILM("custom-matte-film"),
        @SerializedName("custom-natural-tracing-rubber-resistant-stationery-lightweight-paper")
        CUSTOM_NATURAL_TRACING_RUBBER_RESISTANT_STATIONERY_LIGHTWEIGHT_PAPER("custom-natural-tracing-rubber-resistant-stationery-lightweight-paper"),
        @SerializedName("custom-pet-backlit")
        CUSTOM_PET_BACKLIT("custom-pet-backlit"),
        @SerializedName("custom-pet-frontlit")
        CUSTOM_PET_FRONTLIT("custom-pet-frontlit"),
        @SerializedName("custom-pet-frontlit-transparent")
        CUSTOM_PET_FRONTLIT_TRANSPARENT("custom-pet-frontlit-transparent"),
        @SerializedName("custom-plain-paper-for-retail-stationery")
        CUSTOM_PLAIN_PAPER_FOR_RETAIL_STATIONERY("custom-plain-paper-for-retail-stationery"),
        @SerializedName("custom-polypropylene-photographic-satin-paper")
        CUSTOM_POLYPROPYLENE_PHOTOGRAPHIC_SATIN_PAPER("custom-polypropylene-photographic-satin-paper"),
        @SerializedName("custom-pppebanner")
        CUSTOM_PPPEBANNER("custom-pppebanner"),
        @SerializedName("custom-pvc-banner")
        CUSTOM_PVC_BANNER("custom-pvc-banner"),
        @SerializedName("custom-pvc-banner-backlit")
        CUSTOM_PVC_BANNER_BACKLIT("custom-pvc-banner-backlit"),
        @SerializedName("custom-polyester-backlit")
        CUSTOM_POLYESTER_BACKLIT("custom-polyester-backlit"),
        @SerializedName("custom-self-adhesive-cast")
        CUSTOM_SELF_ADHESIVE_CAST("custom-self-adhesive-cast"),
        @SerializedName("custom-self-adhesive-transparent")
        CUSTOM_SELF_ADHESIVE_TRANSPARENT("custom-self-adhesive-transparent"),
        @SerializedName("custom-stationery-backlit")
        CUSTOM_STATIONERY_BACKLIT("custom-stationery-backlit"),
        @SerializedName("custom-stationery-blueback")
        CUSTOM_STATIONERY_BLUEBACK("custom-stationery-blueback"),
        @SerializedName("custom-stationery-graphics")
        CUSTOM_STATIONERY_GRAPHICS("custom-stationery-graphics"),
        @SerializedName("custom-super-heavy-coated")
        CUSTOM_SUPER_HEAVY_COATED("custom-super-heavy-coated"),
        @SerializedName("custom-synthetic")
        CUSTOM_SYNTHETIC("custom-synthetic"),
        @SerializedName("custom-synthetic-backlit")
        CUSTOM_SYNTHETIC_BACKLIT("custom-synthetic-backlit"),
        @SerializedName("custom-textile")
        CUSTOM_TEXTILE("custom-textile"),
        @SerializedName("custom-textile-backlit")
        CUSTOM_TEXTILE_BACKLIT("custom-textile-backlit"),
        @SerializedName("custom-uncoated")
        CUSTOM_UNCOATED("custom-uncoated"),
        @SerializedName("custom-wallpaper-non-woven")
        CUSTOM_WALLPAPER_NON_WOVEN("custom-wallpaper-non-woven"),
        @SerializedName("custom-wallpaper-woven")
        CUSTOM_WALLPAPER_WOVEN("custom-wallpaper-woven"),
        @SerializedName("custom-wallpaper")
        CUSTOM_WALLPAPER("custom-wallpaper"),
        @SerializedName("custom-super-heavyweight-plus-matte")
        CUSTOM_SUPER_HEAVYWEIGHT_PLUS_MATTE("custom-super-heavyweight-plus-matte"),
        @SerializedName("custom-photo-matte-paper")
        CUSTOM_PHOTO_MATTE_PAPER("custom-photo-matte-paper"),
        @SerializedName("custom-polypropylene")
        CUSTOM_POLYPROPYLENE("custom-polypropylene"),
        @SerializedName("custom-yellow-paper")
        CUSTOM_YELLOW_PAPER("custom-yellow-paper"),
        @SerializedName("backlit-material")
        BACKLIT_MATERIAL("backlit-material"),
        @SerializedName("envelope")
        ENVELOPE("envelope"),
        @SerializedName("envelope-heavyweight")
        ENVELOPE_HEAVYWEIGHT("envelope-heavyweight"),
        @SerializedName("labels")
        LABELS("labels"),
        @SerializedName("photographic-glossy")
        PHOTOGRAPHIC_GLOSSY("photographic-glossy"),
        @SerializedName("photographic-semi-gloss")
        PHOTOGRAPHIC_SEMI_GLOSS("photographic-semi-gloss"),
        @SerializedName("plastic-matte")
        PLASTIC_MATTE("plastic-matte"),
        @SerializedName("self-adhesive-labels")
        SELF_ADHESIVE_LABELS("self-adhesive-labels"),
        @SerializedName("stationery")
        STATIONERY("stationery"),
        @SerializedName("stationery-bond")
        STATIONERY_BOND("stationery-bond"),
        @SerializedName("stationery-coated")
        STATIONERY_COATED("stationery-coated"),
        @SerializedName("stationery-colored")
        STATIONERY_COLORED("stationery-colored"),
        @SerializedName("stationery-fine")
        STATIONERY_FINE("stationery-fine"),
        @SerializedName("stationery-heavyweight")
        STATIONERY_HEAVYWEIGHT("stationery-heavyweight"),
        @SerializedName("stationery-heavyweight-coated")
        STATIONERY_HEAVYWEIGHT_COATED("stationery-heavyweight-coated"),
        @SerializedName("stationery-letterhead")
        STATIONERY_LETTERHEAD("stationery-letterhead"),
        @SerializedName("stationery-lightweight")
        STATIONERY_LIGHTWEIGHT("stationery-lightweight"),
        @SerializedName("stationery-preprinted")
        STATIONERY_PREPRINTED("stationery-preprinted"),
        @SerializedName("stationery-prepunched")
        STATIONERY_PREPUNCHED("stationery-prepunched"),
        @SerializedName("transfer")
        TRANSFER("transfer"),
        @SerializedName("transparency")
        TRANSPARENCY("transparency"),
        @SerializedName("com.hp.light-bond")
        COM_HP_LIGHT_BOND("com.hp.light-bond"),
        @SerializedName("com.hp-production-satin-photo-paper")
        COM_HP_PRODUCTION_SATIN_PHOTO_PAPER("com.hp-production-satin-photo-paper"),
        @SerializedName("com.hp-production-gloss-photo-paper")
        COM_HP_PRODUCTION_GLOSS_PHOTO_PAPER("com.hp-production-gloss-photo-paper"),
        @SerializedName("com.hp.usertype-1")
        COM_HP_USERTYPE_1("com.hp.usertype-1"),
        @SerializedName("com.hp.usertype-2")
        COM_HP_USERTYPE_2("com.hp.usertype-2"),
        @SerializedName("com.hp.usertype-3")
        COM_HP_USERTYPE_3("com.hp.usertype-3"),
        @SerializedName("com.hp.usertype-4")
        COM_HP_USERTYPE_4("com.hp.usertype-4"),
        @SerializedName("com.hp.usertype-5")
        COM_HP_USERTYPE_5("com.hp.usertype-5"),
        @SerializedName("com.hp.usertype-6")
        COM_HP_USERTYPE_6("com.hp.usertype-6"),
        @SerializedName("com.hp.usertype-7")
        COM_HP_USERTYPE_7("com.hp.usertype-7"),
        @SerializedName("com.hp.usertype-8")
        COM_HP_USERTYPE_8("com.hp.usertype-8"),
        @SerializedName("com.hp.usertype-9")
        COM_HP_USERTYPE_9("com.hp.usertype-9"),
        @SerializedName("com.hp.usertype-10")
        COM_HP_USERTYPE_10("com.hp.usertype-10"),
        @SerializedName("custom-plain-paper-50pct-ink-density")
        CUSTOM_PLAIN_PAPER_50_PCT_INK_DENSITY("custom-plain-paper-50pct-ink-density"),
        @SerializedName("custom-plain-paper-60pct-ink-density")
        CUSTOM_PLAIN_PAPER_60_PCT_INK_DENSITY("custom-plain-paper-60pct-ink-density"),
        @SerializedName("custom-plain-paper-70pct-ink-density")
        CUSTOM_PLAIN_PAPER_70_PCT_INK_DENSITY("custom-plain-paper-70pct-ink-density"),
        @SerializedName("custom-plain-paper-80pct-ink-density")
        CUSTOM_PLAIN_PAPER_80_PCT_INK_DENSITY("custom-plain-paper-80pct-ink-density"),
        @SerializedName("custom-plain-paper-90pct-ink-density")
        CUSTOM_PLAIN_PAPER_90_PCT_INK_DENSITY("custom-plain-paper-90pct-ink-density"),
        @SerializedName("custom-plain-paper-100pct-ink-density")
        CUSTOM_PLAIN_PAPER_100_PCT_INK_DENSITY("custom-plain-paper-100pct-ink-density");
        private final String value;
        private final static Map<String, MediaInput.MediaType> CONSTANTS = new HashMap<String, MediaInput.MediaType>();

        static {
            for (MediaInput.MediaType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        MediaType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static MediaInput.MediaType fromValue(String value) {
            MediaInput.MediaType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * The current status of this input sub-unit
     * 
     */
    public enum Status {

        @SerializedName("ok")
        OK("ok"),
        @SerializedName("empty")
        EMPTY("empty"),
        @SerializedName("unknown")
        UNKNOWN("unknown"),
        @SerializedName("error")
        ERROR("error");
        private final String value;
        private final static Map<String, MediaInput.Status> CONSTANTS = new HashMap<String, MediaInput.Status>();

        static {
            for (MediaInput.Status c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Status(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static MediaInput.Status fromValue(String value) {
            MediaInput.Status constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
