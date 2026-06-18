// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.deviceusage;

import androidx.annotation.Keep;

import com.hp.jetadvantage.link.common.annotation.DeviceApi;

/**
 * Class for Providing methods to get SubUnits information for a job.
 *
 * @since API 5
 */
@DeviceApi
public class SubUnitInfo {

    /**
     * Identifies the fundamental nature of the job.
     *
     * @since API 5
     */
    @Keep
    public enum JobCategory {
        /**
         * Print
         *
         * @since API 5
         */
        PRINT("Print"),
        /**
         * DigitalSend
         *
         * @since API 5
         */
        DIGITALSEND("DigitalSend"),
        /**
         * Copy
         *
         * @since API 5
         */
        COPY("Copy"),
        /**
         * Fax
         *
         * @since API 5
         */
        FAX("Fax"),
        /**
         * Stored
         *
         * @since API 5
         */
        STORED("Stored"),
        /**
         * Service
         *
         * @since API 5
         */
        SERVICE("Service"),
        /**
         * ConfigurationDiagnostic
         *
         * @since API 5
         */
        CONFIGURATIONDIAGNOSTIC("ConfigurationDiagnostic"),
        /**
         * Mixed
         *
         * @since API 5
         */
        MIXED("Mixed"),
        /**
         * Notification
         *
         * @since API 5
         */
        NOTIFICATION("Notification"),
        /**
         * Other
         *
         * @since API 5
         */
        OTHER("Other");

        private final String jobCategory;

        JobCategory(final String jobCategory) {
            this.jobCategory = jobCategory;
        }

        public String getValue() {
            return jobCategory;
        }
    }

    /**
     * MediaSize
     *
     * @since API 5
     */
    @Keep
    public enum MediaSize {
        /**
         * Letter (8.5inch x 11inch)
         *
         * @since API 5
         */
        LETTER("ANSI_A_8point5x11in"),
        /**
         * Letter Rotated (11inch x 8.5inch)
         *
         * @since API 5
         */
        LETTER_ROTATE("ANSI_A_Rotated_8point5x11in"),
        /**
         * Ledger (11inch x 17inch)
         *
         * @since API 5
         */
        LEDGER("ANSI_B_11x17in"),
        /**
         * ANSI C (17inch x 22inch)
         *
         * @since API 5
         */
        ANSI_C_17X22in("ANSI_C_17x22in"),
        /**
         * ANSI D (22inch x 34inch)
         *
         * @since API 5
         */
        ANSI_D_22X34in("ANSI_D_22x34in"),
        /**
         * ANSI E (34inch x 44inch)
         *
         * @since API 5
         */
        ANSI_E_34X44in("ANSI_E_34x44in"),
        /**
         * ANSI F (28inch x 40inch)
         *
         * @since API 5
         */
        ANSI_F_28X40in("ANSI_F_28x40in"),
        /**
         * Architectural A (9inch x 12inch)
         *
         * @since API 5
         */
        ARCHITECTURAL_A_9X12in("Architectural_A_9x12in"),
        /**
         * INCH12X18 (12inch x 18inch)
         *
         * @since API 5
         */
        INCH12X18("Architectural_B_12x18in"),
        /**
         * Architectural C (18inch x 24inch)
         *
         * @since API 5
         */
        ARCHITECTURAL_C_18X24in("Architectural_C_18x24in"),
        /**
         * Architectural D (24inch x 36inch)
         *
         * @since API 5
         */
        ARCHITECTURAL_D_24X36in("Architectural_D_24x36in"),
        /**
         * Architectural E (36inch x 48inch)
         *
         * @since API 5
         */
        ARCHITECTURAL_E_36X48in("Architectural_E_36x48in"),
        /**
         * Architectural E1 (30inch x 42inch)
         *
         * @since API 5
         */
        ARCHITECTURAL_E1_30X42in("Architectural_E1_30x42in"),
        /**
         * Architectural E2 (26inch x 38inch)
         *
         * @since API 5
         */
        ARCHITECTURAL_E2_26X38in("Architectural_E2_26x38in"),
        /**
         * Architectural E3 (27inch x 39inch)
         *
         * @since API 5
         */
        ARCHITECTURAL_E3_27X39in("Architectural_E3_27x39in"),
        /**
         * DIN 2A0 (1189mm x 1682mm)
         *
         * @since API 5
         */
        DIN_2XA0_1189X1682mm("DIN_2A0_1189x1682mm"),
        /**
         * DIN 4A0 (1682mm x 2378mm)
         *
         * @since API 5
         */
        DIN_4XA0_1682X2378mm("DIN_4A0_1682x2378mm"),
        /**
         * DL (99mm x 210mm)
         *
         * @since API 5
         */
        DL_99X210mm("DL_99x210mm"),
        /**
         * Envelope A2 (4.375inch x 5.75inch)
         *
         * @since API 5
         */
        ENVELOPE_A2("Envelope_A2_4point375x5point75in"),
        /**
         * Envelope Catalog1 (6inch x 9inch)
         *
         * @since API 5
         */
        ENVELOPE_CATALOG("Envelope_Catalog1_6x9in"),
        /**
         * Envelope Comm10 (4.125inch x 9.5inch)
         *
         * @since API 5
         */
        ENVELOPE_COMM10("Envelope_Comm10_4point125x9point5in"),
        /**
         * Envelope Comm6.75 (3.625inch x 6.5inch)
         *
         * @since API 5
         */
        ENVELOPE_COMM6("Envelope_Comm6point75_3point625x6point5in"),
        /**
         * Envelope DL (110mm x 220mm)
         *
         * @since API 5
         */
        ENVELOPE_DL("Envelope_DL_110x220mm"),
        /**
         * Envelope Monarch (3.875inch x 7.5inch)
         *
         * @since API 5
         */
        ENVELOPE_MONARCH("Envelope_Monarch_3point875x7point5in"),
        /**
         * Envelope 9 (3.875inch x 8.875inch)
         *
         * @since API 5
         */
        ENVELOPE_9("Envelope_Windsor_3point875x8point875in"),
        /**
         * Executive (7.25inch x 10.5inch)
         *
         * @since API 5
         */
        EXECUTIVE("Executive_7point25x10point5in"),
        /**
         * Executive Rotated (10.5inch x 7.25inch)
         *
         * @since API 5
         */
        EXECUTIVE_ROTATE("Executive_Rotated_7point25x10point5in"),
        /**
         * INCH8POINT5X13 (8.5inch x 13inch)
         *
         * @since API 5
         */
        INCH8POINT5X13("Foolscap_8point5x13in"),
        /**
         * GENERAL_10X11in (10inch x 11inch)
         *
         * @since API 5
         */
        GENERAL_10X11in("General_10x11in"),
        /**
         * GENERAL_10X13in (10inch x 13inch)
         *
         * @since API 5
         */
        GENERAL_10X13in("General_10x13in"),
        /**
         * GENERAL_10X14in (10inch x 14inch)
         *
         * @since API 5
         */
        GENERAL_10X14in("General_10x14in"),
        /**
         * GENERAL_10X15in (10inch x 15inch)
         *
         * @since API 5
         */
        GENERAL_10X15in("General_10x15in"),
        /**
         * GENERAL_11X12in (11inch x 12inch)
         *
         * @since API 5
         */
        GENERAL_11X12in("General_11x12in"),
        /**
         * GENERAL_11X14in (11inch x 14inch)
         *
         * @since API 5
         */
        GENERAL_11X14in("General_11x14in"),
        /**
         * GENERAL_11X15in (11inch x 15inch)
         *
         * @since API 5
         */
        GENERAL_11X15in("General_11x15in"),
        /**
         * GENERAL_11X19in (11inch x 19inch)
         *
         * @since API 5
         */
        GENERAL_11X19in("General_11x19in"),
        /**
         * GENERAL_12X12in (12inch x 12inch)
         *
         * @since API 5
         */
        GENERAL_12X12in("General_12x12in"),
        /**
         * GENERAL_12X14in (12inch x14inch)
         *
         * @since API 5
         */
        GENERAL_12X14in("General_12x14in"),
        /**
         * GENERAL_12X19in (12inch x 19inch)
         *
         * @since API 5
         */
        GENERAL_12X19in("General_12x19in"),
        /**
         * GENERAL_3POINT5X5in (3.5inch x 5inch)
         *
         * @since API 5
         */
        GENERAL_3POINT5X5in("General_3point5x5in"),
        /**
         * GENERAL_3X5in (3inch x 5inch)
         *
         * @since API 5
         */
        GENERAL_3X5in("General_3x5in"),
        /**
         * GENERAL_4X12in (4inch x 12inch)
         *
         * @since API 5
         */
        GENERAL_4X12in("General_4x12in"),
        /**
         * GENERAL_4X6in (4inch x 6inch)
         *
         * @since API 5
         */
        GENERAL_4X6in("General_4x6in"),
        /**
         * GENERAL_4X8in (4inch x 8inch)
         *
         * @since API 5
         */
        GENERAL_4X8in("General_4x8in"),
        /**
         * GENERAL_5X7in (5inch x 7inch)
         *
         * @since API 5
         */
        GENERAL_5X7in("General_5x7in"),
        /**
         * ENERAL_5X8in (5inch x 8inch)
         *
         * @since API 5
         */
        GENERAL_5X8in("General_5x8in"),
        /**
         * GENERAL_6X8in (6inch x 8inch)
         *
         * @since API 5
         */
        GENERAL_6X8in("General_6x8in"),
        /**
         * GENERAL_7X9in (7inch x 9inch)
         *
         * @since API 5
         */
        GENERAL_7X9in("General_7x9in"),
        /**
         * GENERAL_9X11in (9inch x 11inch)
         *
         * @since API 5
         */
        GENERAL_9X11in("General_9x11in"),
        /**
         * Government Legal (8inch x 13inch)
         *
         * @since API 5
         */
        GOVT_LEGAL("Govt_Legal_8x13in"),
        /**
         * Government Letter (8inch x 10inch)
         *
         * @since API 5
         */
        GOVT_LETTER("Govt_Letter_8x10in"),
        /**
         * Statement (5.5inch x 8.5inch)
         *
         * @since API 5
         */
        STATEMENT("Invoice_5point5x8point5in"),
        /**
         * ISO A0 (841mm x 1189mm)
         *
         * @since API 5
         */
        A0("ISO_A0_841x1189mm"),
        /**
         * ISO A1 (594mm x 841mm)
         *
         * @since API 5
         */
        A1("ISO_A1_594x841mm"),
        /**
         * ISO A2 (420mm x 594mm)
         *
         * @since API 5
         */
        A2("ISO_A2_420x594mm"),
        /**
         * ISO A3 (297mm x 420mm)
         *
         * @since API 5
         */
        A3("ISO_A3_297x420mm"),
        /**
         * ISO A4 (210mm x 297mm)
         *
         * @since API 5
         */
        A4("ISO_A4_210x297mm"),
        /**
         * ISO A4 Rotated (297mm x 210mm)
         *
         * @since API 5
         */
        A4_ROTATE("ISO_A4_Rotated_210x297mm"),
        /**
         * ISO A5 (148mm x 210mm)
         *
         * @since API 5
         */
        A5("ISO_A5_148x210mm"),
        /**
         * ISO A5 Rotated (210mm x 148mm)
         *
         * @since API 5
         */
        A5_ROTATE("ISO_A5_Rotated_148x210mm"),
        /**
         * ISO A6 (105mm x 148mm)
         *
         * @since API 5
         */
        A6("ISO_A6_105x148mm"),
        /**
         * ISO A7 (74mm x 105mm)
         *
         * @since API 5
         */
        A7("ISO_A7_74x105mm"),
        /**
         * ISO A8 (52mm x 74mm)
         *
         * @since API 5
         */
        A8("ISO_A8_52x74mm"),
        /**
         * ISO A9 (37mm x 52mm)
         *
         * @since API 5
         */
        A9("ISO_A9_37x52mm"),
        /**
         * ISO A10 (26mm x 37mm)
         *
         * @since API 5
         */
        A10("ISO_A10_26x37mm"),
        /**
         * ISO B0 (1000mm x 1414mm)
         *
         * @since API 5
         */
        B0("ISO_B0_1000x1414mm"),
        /**
         * ISO B1 (707mm x 1000mm)
         *
         * @since API 5
         */
        B1("ISO_B1_707x1000mm"),
        /**
         * ISO B2 (500mm x 707mm)
         *
         * @since API 5
         */
        B2("ISO_B2_500x707mm"),
        /**
         * ISO B3 (353mm x 500mm)
         *
         * @since API 5
         */
        B3("ISO_B3_353x500mm"),
        /**
         * ISO B4 (250mm x 353mm)
         *
         * @since API 5
         */
        B4("ISO_B4_250x353mm"),
        /**
         * ISO B5 (176mm x 250mm)
         *
         * @since API 5
         */
        B5("ISO_B5_176x250mm"),
        /**
         * ISO_B6 (125mm x 176mm)
         *
         * @since API 5
         */
        B6("ISO_B6_125x176mm"),
        /**
         * ISO B7 (88mm x 125mm)
         *
         * @since API 5
         */
        B7("ISO_B7_88x125mm"),
        /**
         * ISO B8 (62mm x 88mm)
         *
         * @since API 5
         */
        B8("ISO_B8_62x88mm"),
        /**
         * ISO B9 (44mm x 62mm)
         *
         * @since API 5
         */
        B9("ISO_B9_44x62mm"),
        /**
         * ISO B10 (31mm x 44mm)
         *
         * @since API 5
         */
        B10("ISO_B10_31x44mm"),
        /**
         * ISO C0 (917mm x 1297mm)
         *
         * @since API 5
         */
        C0("ISO_C0_917x1297mm"),
        /**
         * ISO C1 (648mm x 917mm)
         *
         * @since API 5
         */
        C1("ISO_C1_648x917mm"),
        /**
         * ISO C2 (458mm x 648mm)
         *
         * @since API 5
         */
        C2("ISO_C2_458x648mm"),
        /**
         * ISO C3 (324mm x 458mm)
         *
         * @since API 5
         */
        C3("ISO_C3_324x458mm"),
        /**
         * ISO C4 (229mm x 324mm)
         *
         * @since API 5
         */
        C4("ISO_C4_229x324mm"),
        /**
         * ISO C5 (162mm x 229mm)
         *
         * @since API 5
         */
        C5("ISO_C5_162x229mm"),
        /**
         * ISO C6 (114mm x 162mm)
         *
         * @since API 5
         */
        C6("ISO_C6_114x162mm"),
        /**
         * ISO C7 (81mm x 114mm)
         *
         * @since API 5
         */
        C7("ISO_C7_81x114mm"),
        /**
         * ISO C8 (57mm x 81mm)
         *
         * @since API 5
         */
        C8("ISO_C8_57x81mm"),
        /**
         * ISO C9 (40mm x 57mm)
         *
         * @since API 5
         */
        C9("ISO_C9_40x57mm"),
        /**
         * ISO C10 (28mm x 40mm)
         *
         * @since API 5
         */
        C10("ISO_C10_28x40mm"),
        /**
         * JBusinessCard (55mm x 91mm)
         *
         * @since API 5
         */
        BUSINESS_CARD("JBusinessCard_55x91mm"),
        /**
         * Japanese Double Postcard (148mm x 200mm)
         *
         * @since API 5
         */
        JDOUBLE_POSTCARD("JDoublePostcard_148x200mm"),
        /**
         * Japanese Double Postcard Rotated (200mm x 148mm)
         *
         * @since API 5
         */
        JDOUBLE_POSTCARD_ROTATE("JDoublePostcard_Rotated_148x200mm"),
        /**
         * JIS B0 (1030mm x 1456mm)
         *
         * @since API 5
         */
        JB0("JIS_B0_1030x1456mm"),
        /**
         * JIS B1 (728mm x 1030mm)
         *
         * @since API 5
         */
        JB1("JIS_B1_728x1030mm"),
        /**
         * JIS B2 (515mm x 728mm)
         *
         * @since API 5
         */
        JB2("JIS_B2_515x728mm"),
        /**
         * JIS B3 (364mm x 515mm)
         *
         * @since API 5
         */
        JB3("JIS_B3_364x515mm"),
        /**
         * JIS B4 (257mm x 364mm)
         *
         * @since API 5
         */
        JB4("JIS_B4_257x364mm"),
        /**
         * JIS B5 (182mm x 257mm)
         *
         * @since API 5
         */
        JB5("JIS_B5_182x257mm"),
        /**
         * JIS B5 Rotated (257mm x 182mm)
         *
         * @since API 5
         */
        JB5_ROTATE("JIS_B5_Rotated_182x257mm"),
        /**
         * JIS B6 (128mm x 182mm)
         *
         * @since API 5
         */
        JB6("JIS_B6_128x182mm"),
        /**
         * JIS B7 (91mm x 128mm)
         *
         * @since API 5
         */
        JB7("JIS_B7_91x128mm"),
        /**
         * JIS B8 (64mm x 91mm)
         *
         * @since API 5
         */
        JB8("JIS_B8_64x91mm"),
        /**
         * JIS B9 (45mm x 64mm)
         *
         * @since API 5
         */
        JB9("JIS_B9_45x64mm"),
        /**
         * JIS B10 (32mm x 45mm)
         *
         * @since API 5
         */
        JB10("JIS_B10_32x45mm"),
        /**
         * JIS Chou3 (120mm x 235mm)
         *
         * @since API 5
         */
        JCHOU3("JIS_Chou3_120x235mm"),
        /**
         * JIS Chou4 (90mm x 205mm)
         *
         * @since API 5
         */
        JCHOU4("JIS_Chou4_90x205mm"),
        /**
         * JIS Exec (216mm x 330mm)
         *
         * @since API 5
         */
        JEXEC("JIS_Exec_216x330mm"),
        /**
         * JIS Kaku2 (240mm x 332mm)
         *
         * @since API 5
         */
        JKAKU2("JIS_Kaku2_240x332mm"),
        /**
         * Japanese Postcard (100mm x 148mm)
         *
         * @since API 5
         */
        JPOSTCARD("JPostcard_100x148mm"),
        /**
         * K16_184X260mm (184mm x 260mm)
         *
         * @since API 5
         */
        K16_184X260mm("K16_184x260mm"),
        /**
         * K16 (195mm x 270mm)
         *
         * @since API 5
         */
        K16("K16_195x270mm"),
        /**
         * PRC 16K (197mm x 273mm)
         *
         * @since API 5
         */
        PK16("K16_197x273mm"),
        /**
         * K8_260X368mm (260mm x 368mm)
         *
         * @since API 5
         */
        K8_260X368mm("K8_260x368mm"),
        /**
         * K8 (270mm x 390mm)
         *
         * @since API 5
         */
        K8("K8_270x390mm"),
        /**
         * PRC 8K (273mm x 394mm)
         *
         * @since API 5
         */
        PK8("K8_273x394mm"),
        /**
         * Legal (8.5inch x 14inch)
         *
         * @since API 5
         */
        LEGAL("Legal_8point5x14in"),
        /**
         * Long Scan (8.5inch x 34inch)
         *
         * @since API 5
         */
        LONG_SCAN("LongScan_8point5x34in"),
        /**
         * Mutsugiri (203mm x 254mm)
         *
         * @since API 5
         */
        MUTSUGIRI("Mutsugiri_203x254mm"),
        /**
         * Oficio (216mm x 340mm)
         *
         * @since API 5
         */
        OFICIO("Oficio_216x340mm"),
        /**
         * RA0 (860mm x 1220mm)
         *
         * @since API 5
         */
        RA0("RA0_860x1220mm"),
        /**
         * RA1 (610mm x 860mm)
         *
         * @since API 5
         */
        RA1("RA1_610x860mm"),
        /**
         * RA2 (430mm x 610mm)
         *
         * @since API 5
         */
        RA2("RA2_430x610mm"),
        /**
         * RA3 (305mm x 430mm)
         *
         * @since API 5
         */
        RA3("RA3_305x430mm"),
        /**
         * RA4 (215mm x 305mm)
         *
         * @since API 5
         */
        RA4("RA4_215x305mm"),
        /**
         * SRA0 (900mm x 1280mm)
         *
         * @since API 5
         */
        SRA0("RA0_900x1280mm"),
        /**
         * SRA1 (640mm x 900mm)
         *
         * @since API 5
         */
        SRA1("SRA1_640x900mm"),
        /**
         * SRA2 (450mm x 640mm)
         *
         * @since API 5
         */
        SRA2("SRA2_450x640mm"),
        /**
         * SRA3 (320mm x 450mm)
         *
         * @since API 5
         */
        SRA3("SRA3_320x450mm"),
        /**
         * SRA4 (225mm x 320mm)
         *
         * @since API 5
         */
        SRA4("SRA4_225x320mm"),
        /**
         * Super B (13inch x 19inch)
         *
         * @since API 5
         */
        SUPER_B("Super_B_13x19in"),
        /**
         * Any
         *
         * @since API 5
         */
        AUTO("Any"),
        /**
         * Match original media size
         *
         * @since API 5
         */
        MATCH_ORIGINAL("MatchOriginal"),
        /**
         * Mixed Letter and Legal
         *
         * @since API 5
         */
        MIXED_LETTER_LEGAL("MixedLetterLegal"),
        /**
         * Mixed Letter and Ledger
         *
         * @since API 5
         */
        MIXED_LETTER_LEDGER("MixedLetterLedger"),
        /**
         * Mixed A3 and A4
         *
         * @since API 5
         */
        MIXED_A3_A4("MixedA4A3"),
        /**
         * Custom size
         *
         * @since API 5
         */
        CUSTOM("Custom"),
        /**
         * Other
         *
         * @since API 5
         */
        OTHER("Other"),
        /**
         * An indeterminable size due to lack of sensors
         *
         * @since API 5
         */
        UNKNOWN("Unknown"),
        /**
         * An indeterminable size due to lack of sensors
         *
         * @since API 5
         */
        UNKNOWN_ENVELOP("UnknownEnvelope"),
        /**
         * Index card (100mm x 150mm)
         *
         * @since API 5
         */
        INDEXCARD("ISO_INDEXCARD_100x150mm"),
        /**
         * Any custom size
         *
         * @since API 5
         */
        ANY_CUSTOM("AnyCustom");

        private final String mediaSize;

        MediaSize(final String mediaSize) {
            this.mediaSize = mediaSize;
        }

        public String getValue() {
            return mediaSize;
        }
    }

    /**
     * ColorMode
     *
     * @since API 5
     */
    public enum ColorMode {
        /**
         * MONO
         *
         * @since API 5
         */
        MONO("Mono"),
        /**
         * COLOR
         *
         * @since API 5
         */
        COLOR("Color");

        private final String colorMode;

        ColorMode(final String colorMode) {
            this.colorMode = colorMode;
        }

        public String getValue() {
            return colorMode;
        }
    }
}
