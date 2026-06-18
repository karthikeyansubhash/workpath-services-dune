// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.copier;

import androidx.annotation.NonNull;
import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.api.CapabilitiesExceededException;
import com.hp.jetadvantage.link.api.scanner.Margins;
import com.hp.jetadvantage.link.common.Sdk;

import java.util.HashMap;
import java.util.Map;

/**
 * Job attributes for requesting a copy from the printer.
 * An instance of this class is created using {@link CopyBuilder} or {@link StoreCopyBuilder}.
 *
 * @since API 3
 */

public class CopyAttributes implements Parcelable {
    /**
     * The color modes for copying supported by the current printer.
     *
     * @since API 3
     */
    public enum ColorMode {
        /**
         * Color mode default from the printer.
         *
         * @since API 3
         */
        DEFAULT,

        /**
         * Auto mode.
         *
         * @since API 3
         */
        AUTO,

        /**
         * Color
         *
         * @since API 3
         */
        COLOR,

        /**
         * Gray / Gray-scale
         *
         * @since API 3
         */
        GRAY,

        /**
         * Mono
         *
         * @since API 3
         */
        MONO
    }

    /**
     * Original Orientation options supported by the current printer.
     *
     * @since API 3
     */
    public enum Orientation {
        /**
         * Uses the default orientation settings configured on the printer.
         *
         * @since API 3
         */
        DEFAULT,

        /**
         * Portrait orientation of a page
         *
         * @since API 3
         */
        PORTRAIT,

        /**
         * Landscape orientation of a page
         *
         * @since API 3
         */
        LANDSCAPE
    }

    /**
     * Duplex options supported by the current printer.
     *
     * @since API 3
     */
    public enum Duplex {
        /**
         * Use the duplex settings configured on the printer.
         *
         * @since API 3
         */
        DEFAULT,

        /**
         * No duplex
         *
         * @since API 3
         */
        NONE,

        /**
         * Duplex along the long edge of the paper.
         *
         * @since API 3
         */
        BOOK,

        /**
         * Duplex along the short edge of the paper.
         *
         * @since API 3
         */
        FLIP
    }

    /**
     * The scan size (i.e., size of the original) for scanning.<br>
     *
     * <p>This attribute is relative to the orientation parameter of scan.</p>
     *
     * @since API 3
     */
    public enum ScanSize {
        /**
         * Default scanning size from printer.
         * If the printer supports auto-detection it will be used.
         *
         * @since API 3
         */
        DEFAULT,

        /**
         * ISO A3 (297mm x 420mm)
         *
         * @since API 3
         */
        A3,

        /**
         * ISO A4 Rotated (297mm x 210mm)
         *
         * @since API 3
         */
        A4_ROTATE,

        /**
         * ISO A4 (210mm x 297mm)
         *
         * @since API 3
         */
        A4,

        /**
         * ISO A5 Rotated (210mm x 148mm)
         *
         * @since API 3
         */
        A5_ROTATE,

        /**
         * ISO A5 (148mm x 210mm)
         *
         * @since API 3
         */
        A5,

        /**
         * ISO A6 Rotated (148mm x 105mm)
         *
         * @since API 3
         */
        A6_ROTATE,

        /**
         * ISO A6 (105mm x 148mm)
         *
         * @since API 3
         */
        A6,

        /**
         * ISO B4 (250mm x 353mm)
         *
         * @since API 3
         */
        B4,

        /**
         * ISO B5 (176mm x 250mm)
         *
         * @since API 3
         */
        B5,

        /**
         * ISO B5 Rotated (250mm x 176mm)
         *
         * @since API 3
         */
        B5_ROTATE,

        /**
         * ISO B6 (125mm x 176mm)
         *
         * @since API 3
         */
        B6,

        /**
         * JBusinessCard (55mm x 91mm)
         *
         * @since API 3
         */
        BUSINESS_CARD,

        /**
         * Letter Rotated (11inch x 8.5inch)
         *
         * @since API 3
         */
        LETTER_ROTATE,

        /**
         * Letter (8.5inch x 11inch)
         *
         * @since API 3
         */
        LETTER,

        /**
         * JIS B4 (257mm x 364mm)
         *
         * @since API 3
         */
        JB4,

        /**
         * JIS B5 Rotated (257mm x 182mm)
         *
         * @since API 3
         */
        JB5_ROTATE,

        /**
         * JIS B5 (182mm x 257mm)
         *
         * @since API 3
         */
        JB5,

        /**
         * JIS B6 (128mm x 182mm)
         *
         * @since API 3
         */
        JB6,

        /**
         * Legal (8.5inch x 14inch)
         *
         * @since API 3
         */
        LEGAL,

        /**
         * Ledger (11inch x 17inch)
         *
         * @since API 3
         */
        LEDGER,

        /**
         * Executive (7.25inch x 10.5inch)
         *
         * @since API 3
         */
        EXECUTIVE,

        /**
         * Statement (5.5inch x 8.5inch)
         *
         * @since API 3
         */
        STATEMENT,

        /**
         * Statement Rotated (8.5inch x 5.5inch)
         *
         * @since API 3
         */
        STATEMENT_ROTATE,

        /**
         * K8 (270mm x 390mm)
         *
         * @since API 3
         */
        K8,

        /**
         * K16 (195mm x 270mm)
         *
         * @since API 3
         */
        K16,

        /**
         * PRC 8K (273mm x 394mm)
         *
         * @since API 3
         */
        PK8,

        /**
         * PRC 16K (197mm x 273mm)
         *
         * @since API 3
         */
        PK16,

        /**
         * INCH8POINT5X13 (8.5inch x 13inch)
         *
         * @since API 3
         */
        INCH8POINT5X13,

        /**
         * INCH12X18 (12inch x 18inch)
         *
         * @since API 3
         */
        INCH12X18,

        /**
         * Mixed Letter and Legal
         *
         * @since API 3
         */
        MIXED_LETTER_LEGAL,

        /**
         * Mixed Letter and Ledger
         *
         * @since API 3
         */
        MIXED_LETTER_LEDGER,

        /**
         * Mixed A3 and A4
         *
         * @since API 3
         */
        MIXED_A3_A4,

        /**
         * Match original media size
         *
         * @since API 3
         */
        MATCH_ORIGINAL,

        /**
         * Custom size
         *
         * @since API 3
         */
        CUSTOM,

        /**
         * Auto detected size for scan (Any)
         * A meta-type used to indicate auto-select
         *
         * @since API 3
         */
        AUTO,

        /**
         * ANSI C (17inch x 22inch)
         *
         * @since API 5
         */
        ANSI_C_17X22in,

        /**
         * ANSI D (22inch x 34inch)
         *
         * @since API 5
         */
        ANSI_D_22X34in,

        /**
         * ANSI E (34inch x 44inch)
         *
         * @since API 5
         */
        ANSI_E_34X44in,

        /**
         * ANSI F (28inch x 40inch)
         *
         * @since API 5
         */
        ANSI_F_28X40in,

        /**
         * Architectural A (9inch x 12inch)
         *
         * @since API 5
         */
        ARCHITECTURAL_A_9X12in,

        /**
         * Architectural C (18inch x 24inch)
         *
         * @since API 5
         */
        ARCHITECTURAL_C_18X24in,

        /**
         * Architectural D (24inch x 36inch)
         *
         * @since API 5
         */
        ARCHITECTURAL_D_24X36in,

        /**
         * Architectural E (36inch x 48inch)
         *
         * @since API 5
         */
        ARCHITECTURAL_E_36X48in,

        /**
         * Architectural E1 (30inch x 42inch)
         *
         * @since API 5
         */
        ARCHITECTURAL_E1_30X42in,

        /**
         * Architectural E2 (26inch x 38inch)
         *
         * @since API 5
         */
        ARCHITECTURAL_E2_26X38in,

        /**
         * Architectural E3 (27inch x 39inch)
         *
         * @since API 5
         */
        ARCHITECTURAL_E3_27X39in,

        /**
         * DIN 2A0 (1189mm x 1682mm)
         *
         * @since API 5
         */
        DIN_2XA0_1189X1682mm,

        /**
         * DIN 4A0 (1682mm x 2378mm)
         *
         * @since API 5
         */
        DIN_4XA0_1682X2378mm,

        /**
         * DL (99mm x 210mm)
         *
         * @since API 5
         */
        DL_99X210mm,

        /**
         * Envelope A2 (4.375inch x 5.75inch)
         *
         * @since API 5
         */
        ENVELOPE_A2,

        /**
         * Envelope Catalog1 (6inch x 9inch)
         *
         * @since API 5
         */
        ENVELOPE_CATALOG,

        /**
         * Envelope Comm10 (4.125inch x 9.5inch)
         *
         * @since API 5
         */
        ENVELOPE_COMM10,

        /**
         * Envelope Comm6.75 (3.625inch x 6.5inch)
         *
         * @since API 5
         */
        ENVELOPE_COMM6,

        /**
         * Envelope DL (110mm x 220mm)
         *
         * @since API 5
         */
        ENVELOPE_DL,

        /**
         * Envelope Monarch (3.875inch x 7.5inch)
         *
         * @since API 5
         */
        ENVELOPE_MONARCH,

        /**
         * Envelope 9 (3.875inch x 8.875inch)
         *
         * @since API 5
         */
        ENVELOPE_9,

        /**
         * Executive Rotated (10.5inch x 7.25inch)
         *
         * @since API 5
         */
        EXECUTIVE_ROTATE,

        /**
         * GENERAL_10X11in (10inch x 11inch)
         *
         * @since API 5
         */
        GENERAL_10X11in,

        /**
         * GENERAL_10X13in (10inch x 13inch)
         *
         * @since API 5
         */
        GENERAL_10X13in,

        /**
         * GENERAL_10X14in (10inch x 14inch)
         *
         * @since API 5
         */
        GENERAL_10X14in,

        /**
         * GENERAL_10X15in (10inch x 15inch)
         *
         * @since API 5
         */
        GENERAL_10X15in,

        /**
         * GENERAL_11X12in (11inch x 12inch)
         *
         * @since API 5
         */
        GENERAL_11X12in,

        /**
         * GENERAL_11X14in (11inch x 14inch)
         *
         * @since API 5
         */
        GENERAL_11X14in,

        /**
         * GENERAL_11X15in (11inch x 15inch)
         *
         * @since API 5
         */
        GENERAL_11X15in,

        /**
         * GENERAL_11X19in (11inch x 19inch)
         *
         * @since API 5
         */
        GENERAL_11X19in,

        /**
         * GENERAL_12X12in (12inch x 12inch)
         *
         * @since API 5
         */
        GENERAL_12X12in,

        /**
         * GENERAL_12X14in (12inch x 14inch)
         *
         * @since API 5
         */
        GENERAL_12X14in,

        /**
         * GENERAL_12X19in (12inch x 19inch)
         *
         * @since API 5
         */
        GENERAL_12X19in,

        /**
         * GENERAL_3POINT5X5in (3.5inch x 5inch)
         *
         * @since API 5
         */
        GENERAL_3POINT5X5in,

        /**
         * GENERAL_3X5in (3inch x 5inch)
         *
         * @since API 5
         */
        GENERAL_3X5in,

        /**
         * GENERAL_4X12in (4inch x 12inch)
         *
         * @since API 5
         */
        GENERAL_4X12in,

        /**
         * GENERAL_4X6in (4inch x 6inch)
         *
         * @since API 5
         */
        GENERAL_4X6in,

        /**
         * GENERAL_4X8in (4inch x 8inch)
         *
         * @since API 5
         */
        GENERAL_4X8in,

        /**
         * GENERAL_5X7in (5inch x 7inch)
         *
         * @since API 5
         */
        GENERAL_5X7in,

        /**
         * GENERAL_5X8in (5inch x 8inch)
         *
         * @since API 5
         */
        GENERAL_5X8in,

        /**
         * GENERAL_6X8in (6inch x 8inch)
         *
         * @since API 5
         */
        GENERAL_6X8in,

        /**
         * GENERAL_7X9in (7inch x 9inch)
         *
         * @since API 5
         */
        GENERAL_7X9in,

        /**
         * GENERAL_9X11in (9inch x 11inch)
         *
         * @since API 5
         */
        GENERAL_9X11in,

        /**
         * Government Legal (8inch x 13inch)
         *
         * @since API 5
         */
        GOVT_LEGAL,

        /**
         * Government Letter (8inch x 10inch)
         *
         * @since API 5
         */
        GOVT_LETTER,

        /**
         * ISO A0 (841mm x 1189mm)
         *
         * @since API 5
         */
        A0,

        /**
         * ISO A1 (594mm x 841mm)
         *
         * @since API 5
         */
        A1,

        /**
         * ISO A2 (420mm x 594mm)
         *
         * @since API 5
         */
        A2,

        /**
         * ISO A7 (74mm x 105mm)
         *
         * @since API 5
         */
        A7,

        /**
         * ISO A8 (52mm x 74mm)
         *
         * @since API 5
         */
        A8,

        /**
         * ISO A9 (37mm x 52mm)
         *
         * @since API 5
         */
        A9,

        /**
         * ISO A10 (26mm x 37mm)
         *
         * @since API 5
         */
        A10,

        /**
         * ISO B0 (1000mm x 1414mm)
         *
         * @since API 5
         */
        B0,

        /**
         * ISO B1 (707mm x 1000mm)
         *
         * @since API 5
         */
        B1,

        /**
         * ISO B2 (500mm x 707mm)
         *
         * @since API 5
         */
        B2,

        /**
         * ISO B3 (353mm x 500mm)
         *
         * @since API 5
         */
        B3,

        /**
         * ISO B7 (88mm x 125mm)
         *
         * @since API 5
         */
        B7,

        /**
         * ISO B8 (62mm x 88mm)
         *
         * @since API 5
         */
        B8,

        /**
         * ISO B9 (44mm x 62mm)
         *
         * @since API 5
         */
        B9,

        /**
         * ISO B10 (31mm x 44mm)
         *
         * @since API 5
         */
        B10,

        /**
         * ISO C0 (917mm x 1297mm)
         *
         * @since API 5
         */
        C0,

        /**
         * ISO C1 (648mm x 917mm)
         *
         * @since API 5
         */
        C1,

        /**
         * ISO C2 (458mm x 648mm)
         *
         * @since API 5
         */
        C2,

        /**
         * ISO C3 (324mm x 458mm)
         *
         * @since API 5
         */
        C3,

        /**
         * ISO C4 (229mm x 324mm)
         *
         * @since API 5
         */
        C4,

        /**
         * ISO C5 (162mm x 229mm)
         *
         * @since API 5
         */
        C5,

        /**
         * ISO C6 (114mm x 162mm)
         *
         * @since API 5
         */
        C6,

        /**
         * ISO C7 (81mm x 114mm)
         *
         * @since API 5
         */
        C7,

        /**
         * ISO C8 (57mm x 81mm)
         *
         * @since API 5
         */
        C8,

        /**
         * ISO C9 (40mm x 57mm)
         *
         * @since API 5
         */
        C9,

        /**
         * ISO C10 (28mm x 40mm)
         *
         * @since API 5
         */
        C10,

        /**
         * Japanese Double Postcard (148mm x 200mm)
         *
         * @since API 5
         */
        JDOUBLE_POSTCARD,

        /**
         * Japanese Double Postcard Rotated (200mm x 148mm)
         *
         * @since API 5
         */
        JDOUBLE_POSTCARD_ROTATE,

        /**
         * JIS B0 (1030mm x 1456mm)
         *
         * @since API 5
         */
        JB0,

        /**
         * JIS B1 (728mm x 1030mm)
         *
         * @since API 5
         */
        JB1,

        /**
         * JIS B2 (515mm x 728mm)
         *
         * @since API 5
         */
        JB2,

        /**
         * JIS B3 (364mm x 515mm)
         *
         * @since API 5
         */
        JB3,

        /**
         * JIS B7 (91mm x 128mm)
         *
         * @since API 5
         */
        JB7,

        /**
         * JIS B8 (64mm x 91mm)
         *
         * @since API 5
         */
        JB8,

        /**
         * JIS B9 (45mm x 64mm)
         *
         * @since API 5
         */
        JB9,

        /**
         * JIS B10 (32mm x 45mm)
         *
         * @since API 5
         */
        JB10,

        /**
         * JIS Chou3 (120mm x 235mm)
         *
         * @since API 5
         */
        JCHOU3,

        /**
         * JIS Chou4 (90mm x 205mm)
         *
         * @since API 5
         */
        JCHOU4,

        /**
         * JIS Exec (216mm x 330mm)
         *
         * @since API 5
         */
        JEXEC,

        /**
         * JIS Kaku2 (240mm x 332mm)
         *
         * @since API 5
         */
        JKAKU2,

        /**
         * Japanese Postcard (100mm x 148mm)
         *
         * @since API 5
         */
        JPOSTCARD,

        /**
         * K16_184X260mm (184mm x 260mm)
         *
         * @since API 5
         */
        K16_184X260mm,

        /**
         * K8_260X368mm (260mm x 368mm)
         *
         * @since API 5
         */
        K8_260X368mm,

        /**
         * Long Scan (8.5inch x 34inch)
         *
         * @since API 5
         */
        LONG_SCAN,

        /**
         * Mutsugiri (203mm x 254mm)
         *
         * @since API 5
         */
        MUTSUGIRI,

        /**
         * Oficio (216mm x 340mm)
         *
         * @since API 5
         */
        OFICIO,

        /**
         * RA0 (860mm x 1220mm)
         *
         * @since API 5
         */
        RA0,

        /**
         * RA1 (610mm x 860mm)
         *
         * @since API 5
         */
        RA1,

        /**
         * RA2 (430mm x 610mm)
         *
         * @since API 5
         */
        RA2,

        /**
         * RA3 (305mm x 430mm)
         *
         * @since API 5
         */
        RA3,

        /**
         * RA4 (215mm x 305mm)
         *
         * @since API 5
         */
        RA4,

        /**
         * SRA0 (900mm x 1280mm)
         *
         * @since API 5
         */
        SRA0,

        /**
         * SRA1 (640mm x 900mm)
         *
         * @since API 5
         */
        SRA1,

        /**
         * SRA2 (450mm x 640mm)
         *
         * @since API 5
         */
        SRA2,

        /**
         * SRA3 (320mm x 450mm)
         *
         * @since API 5
         */
        SRA3,

        /**
         * SRA4 (225mm x 320mm)
         *
         * @since API 5
         */
        SRA4,

        /**
         * Super B (13inch x 19inch)
         *
         * @since API 5
         */
        SUPER_B,

        /**
         * Index card (100mm x 150mm)
         *
         * @since API 5
         */
        INDEXCARD,

        /**
         * An indeterminable size due to lack of sensors
         *
         * @since API 5
         */
        UNKNOWN,

        /**
         * Any custom size
         *
         * @since API 5
         */
        ANY_CUSTOM,

        /**
         * An indeterminable envelope size due to lack of sensors
         *
         * @since API 5
         */
        UNKNOWN_ENVELOP;

        /**
         * Retrieves the height of paper
         *
         * @return <p>supported height from {@link ScanSize}.
         * <ul>
         * <li>Return the supported height</li>
         * <li>Return can be 0.0 if PaperSize height is not supported</li>
         * </ul>
         * </p>
         *
         * @since API 8
         */
        public double getHeight() {
            for (com.hp.workpath.api.PaperSize ps : com.hp.workpath.api.PaperSize.values()) {
                if (this.name().equals(ps.name())) {
                    return ps.getHeight();
                }
            }
            return 0;
        }

        /**
         * Retrieves the width of paper
         *
         * @return <p>supported width from {@link ScanSize}.
         * <ul>
         * <li>Return the supported width</li>
         * <li>Return can be 0.0 if PaperSize width is not supported</li>
         * </ul>
         * </p>
         *
         * @since API 8
         */
        public double getWidth() {
            for (com.hp.workpath.api.PaperSize ps : com.hp.workpath.api.PaperSize.values()) {
                if (this.name().equals(ps.name())) {
                    return ps.getWidth();
                }
            }
            return 0;
        }

        /**
         * Retrieves the unit of paper
         *
         * @return <p>supported unit from {@link ScanSize}.
         * <ul>
         * <li>Return the supported unit (mm or inch or cm) according to paper size.</li>
         * <li>Return can be null if PaperSize unit is not supported</li>
         * </ul>
         * </p>
         *
         * @since API 8
         */
        public String getUnit() {
            for (com.hp.workpath.api.PaperSize ps : com.hp.workpath.api.PaperSize.values()) {
                if (this.name().equals(ps.name())) {
                    return ps.getUnit();
                }
            }
            return null;
        }
    }

    /**
     * <p>Scan Source controls a feed source for scanning</p>
     *
     * @since API 3
     */
    public enum ScanSource {
        /**
         * Default value
         *
         * @since API 3
         */
        DEFAULT,

        /**
         * Directs the device to scan from the automatic document feeder.
         *
         * @since API 3
         */
        ADF,

        /**
         * Directs the device to scan from the flatbed (Glass).
         *
         * @since API 3
         */
        FLATBED,

        /**
         * The device will automatically select the media source.
         *
         * @since API 3
         */
        AUTO
    }

    /**
     * <p>Copy Preview controls if device must show a preview of scanned images and request confirmation to copy final document.</p>
     *
     * @since API 3
     */
    public enum CopyPreview {
        /**
         * Default value
         *
         * @since API 3
         */
        DEFAULT,

        /**
         * Enable Copy Preview,
         * After starting a job the device will pre-scan a document and displays preview to an user,
         * if user confirms the preview the device will do a final scan
         *
         * @since API 3
         */

        TRUE,
        /**
         * Disable Copy Preview
         * No preview will be shown during copying
         *
         * @since API 3
         */
        FALSE,
    }

    /**
     * <p>Background Cleanup controls the level of removing background on a scanned image.</p>
     *
     * @since API 3
     */
    @SuppressWarnings("unused")
    public enum BackgroundCleanup {
        /**
         * Default value
         *
         * @since API 3
         */
        DEFAULT,

        /**
         * @since API 3
         */
        LEVEL_0,

        /**
         * @since API 3
         */
        LEVEL_1,

        /**
         * @since API 3
         */
        LEVEL_2,

        /**
         * @since API 3
         */
        LEVEL_3,

        /**
         * @since API 3
         */
        LEVEL_4,

        /**
         * @since API 3
         */
        LEVEL_5,

        /**
         * @since API 3
         */
        LEVEL_6,

        /**
         * @since API 3
         */
        LEVEL_7,

        /**
         * @since API 3
         */
        LEVEL_8
    }

    /**
     * <p>Contrast Adjustment controls the level of contrast correction on a scanned image.</p>
     *
     * @since API 3
     */
    @SuppressWarnings("unused")
    public enum ContrastAdjustment {
        /**
         * Default value
         *
         * @since API 3
         */
        DEFAULT,

        /**
         * @since API 3
         */
        LEVEL_0,

        /**
         * @since API 3
         */
        LEVEL_1,

        /**
         * @since API 3
         */
        LEVEL_2,

        /**
         * @since API 3
         */
        LEVEL_3,

        /**
         * @since API 3
         */
        LEVEL_4,

        /**
         * @since API 3
         */
        LEVEL_5,

        /**
         * @since API 3
         */
        LEVEL_6,

        /**
         * @since API 3
         */
        LEVEL_7,

        /**
         * @since API 3
         */
        LEVEL_8
    }

    /**
     * <p>Darkness Adjustment controls the level of darkness correction on a scanned image.</p>
     *
     * @since API 3
     */
    @SuppressWarnings("unused")
    public enum DarknessAdjustment {
        /**
         * Default value
         *
         * @since API 3
         */
        DEFAULT,

        /**
         * @since API 3
         */
        LEVEL_0,

        /**
         * @since API 3
         */
        LEVEL_1,

        /**
         * @since API 3
         */
        LEVEL_2,

        /**
         * @since API 3
         */
        LEVEL_3,

        /**
         * @since API 3
         */
        LEVEL_4,

        /**
         * @since API 3
         */
        LEVEL_5,

        /**
         * @since API 3
         */
        LEVEL_6,

        /**
         * @since API 3
         */
        LEVEL_7,

        /**
         * @since API 3
         */
        LEVEL_8
    }

    /**
     * <p>Sharpness Adjustment controls the level of sharpness correction on a scanned image.</p>
     *
     * @since API 3
     */
    @SuppressWarnings("unused")
    public enum SharpnessAdjustment {
        /**
         * Default value
         *
         * @since API 3
         */
        DEFAULT,

        /**
         * @since API 3
         */
        LEVEL_0,

        /**
         * @since API 3
         */
        LEVEL_1,

        /**
         * @since API 3
         */
        LEVEL_2,

        /**
         * @since API 3
         */
        LEVEL_3,

        /**
         * @since API 3
         */
        LEVEL_4,
    }

    /**
     * Collate Mode controls sheet collation.
     *
     * @since API 3
     */
    public enum CollateMode {
        /**
         * Use printer's settings.
         *
         * @since API 3
         */
        DEFAULT,

        /**
         * Sheets will be collated
         *
         * @since API 3
         */
        COLLATED,

        /**
         * Sheets will be uncollated
         *
         * @since API 3
         */
        UNCOLLATED
    }

    /**
     * The paper size for printing.<br>
     *
     * <p>This attribute is relative to the orientation parameter of scan.</p>
     *
     * @since API 3
     */
    public enum PaperSize {
        /**
         * Default paper size.
         *
         * @since API 3
         */
        DEFAULT,

        /**
         * ISO A3 (297mm x 420mm)
         *
         * @since API 3
         */
        A3,

        /**
         * ISO A4 Rotated (297mm x 210mm)
         *
         * @since API 3
         */
        A4_ROTATE,

        /**
         * ISO A4 (210mm x 297mm)
         *
         * @since API 3
         */
        A4,

        /**
         * ISO A5 Rotated (210mm x 148mm)
         *
         * @since API 3
         */
        A5_ROTATE,

        /**
         * ISO A5 (148mm x 210mm)
         *
         * @since API 3
         */
        A5,

        /**
         * ISO A6 Rotated (148mm x 105mm)
         *
         * @since API 3
         */
        A6_ROTATE,

        /**
         * ISO A6 (105mm x 148mm)
         *
         * @since API 3
         */
        A6,

        /**
         * ISO B4 (250mm x 353mm)
         *
         * @since API 3
         */
        B4,

        /**
         * ISO B5 (176mm x 250mm)
         *
         * @since API 3
         */
        B5,

        /**
         * ISO B5 Rotated (250mm x 176mm)
         *
         * @since API 3
         */
        B5_ROTATE,

        /**
         * ISO B6 (125mm x 176mm)
         *
         * @since API 3
         */
        B6,

        /**
         * JBusinessCard (55mm x 91mm)
         *
         * @since API 3
         */
        BUSINESS_CARD,

        /**
         * Letter Rotated (11inch x 8.5inch)
         *
         * @since API 3
         */
        LETTER_ROTATE,

        /**
         * Letter (8.5inch x 11inch)
         *
         * @since API 3
         */
        LETTER,

        /**
         * JIS B4 (257mm x 364mm)
         *
         * @since API 3
         */
        JB4,

        /**
         * JIS B5 Rotated (257mm x 182mm)
         *
         * @since API 3
         */
        JB5_ROTATE,

        /**
         * JIS B5 (182mm x 257mm)
         *
         * @since API 3
         */
        JB5,

        /**
         * JIS B6 (128mm x 182mm)
         *
         * @since API 3
         */
        JB6,

        /**
         * Legal (8.5inch x 14inch)
         *
         * @since API 3
         */
        LEGAL,

        /**
         * Ledger (11inch x 17inch)
         *
         * @since API 3
         */
        LEDGER,

        /**
         * Executive (7.25inch x 10.5inch)
         *
         * @since API 3
         */
        EXECUTIVE,

        /**
         * Statement (5.5inch x 8.5inch)
         *
         * @since API 3
         */
        STATEMENT,

        /**
         * Statement Rotated (8.5inch x 5.5inch)
         *
         * @since API 3
         */
        STATEMENT_ROTATE,

        /**
         * K8 (270mm x 390mm)
         *
         * @since API 3
         */
        K8,

        /**
         * K16 (195mm x 270mm)
         *
         * @since API 3
         */
        K16,

        /**
         * PRC 8K (273mm x 394mm)
         *
         * @since API 3
         */
        PK8,

        /**
         * PRC 16K (197mm x 273mm)
         *
         * @since API 3
         */
        PK16,

        /**
         * INCH8POINT5X13 (8.5inch x 13inch)
         *
         * @since API 3
         */
        INCH8POINT5X13,

        /**
         * INCH12X18 (12inch x 18inch)
         *
         * @since API 3
         */
        INCH12X18,

        /**
         * Mixed Letter and Legal paper size
         *
         * @since API 3
         */
        MIXED_LETTER_LEGAL,

        /**
         * Mixed Letter and Ledger paper size
         *
         * @since API 3
         */
        MIXED_LETTER_LEDGER,

        /**
         * Mixed A3 and A4 paper size
         *
         * @since API 3
         */
        MIXED_A3_A4,

        /**
         * Match original scan size
         *
         * @since API 3
         */
        MATCH_ORIGINAL,

        /**
         * Custom size
         *
         * @since API 3
         */
        CUSTOM,

        /**
         * Auto detected size for print (Any)
         * A meta-type used to indicate auto-select
         *
         * @since API 3
         */
        AUTO,

        /**
         * ANSI C (17inch x 22inch)
         *
         * @since API 5
         */
        ANSI_C_17X22in,

        /**
         * ANSI D (22inch x 34inch)
         *
         * @since API 5
         */
        ANSI_D_22X34in,

        /**
         * ANSI E (34inch x 44inch)
         *
         * @since API 5
         */
        ANSI_E_34X44in,

        /**
         * ANSI F (28inch x 40inch)
         *
         * @since API 5
         */
        ANSI_F_28X40in,

        /**
         * Architectural A (9inch x 12inch)
         *
         * @since API 5
         */
        ARCHITECTURAL_A_9X12in,

        /**
         * Architectural C (18inch x 24inch)
         *
         * @since API 5
         */
        ARCHITECTURAL_C_18X24in,

        /**
         * Architectural D (24inch x 36inch)
         *
         * @since API 5
         */
        ARCHITECTURAL_D_24X36in,

        /**
         * Architectural E (36inch x 48inch)
         *
         * @since API 5
         */
        ARCHITECTURAL_E_36X48in,

        /**
         * Architectural E1 (30inch x 42inch)
         *
         * @since API 5
         */
        ARCHITECTURAL_E1_30X42in,

        /**
         * Architectural E2 (26inch x 38inch)
         *
         * @since API 5
         */
        ARCHITECTURAL_E2_26X38in,

        /**
         * Architectural E3 (27inch x 39inch)
         *
         * @since API 5
         */
        ARCHITECTURAL_E3_27X39in,

        /**
         * DIN 2A0 (1189mm x 1682mm)
         *
         * @since API 5
         */
        DIN_2XA0_1189X1682mm,

        /**
         * DIN 4A0 (1682mm x 2378mm)
         *
         * @since API 5
         */
        DIN_4XA0_1682X2378mm,

        /**
         * DL (99mm x 210mm)
         *
         * @since API 5
         */
        DL_99X210mm,

        /**
         * Envelope A2 (4.375inch x 5.75inch)
         *
         * @since API 5
         */
        ENVELOPE_A2,

        /**
         * Envelope Catalog1 (6inch x 9inch)
         *
         * @since API 5
         */
        ENVELOPE_CATALOG,

        /**
         * Envelope Comm10 (4.125inch x 9.5inch)
         *
         * @since API 5
         */
        ENVELOPE_COMM10,

        /**
         * Envelope Comm6.75 (3.625inch x 6.5inch)
         *
         * @since API 5
         */
        ENVELOPE_COMM6,

        /**
         * Envelope DL (110mm x 220mm)
         *
         * @since API 5
         */
        ENVELOPE_DL,

        /**
         * Envelope Monarch (3.875inch x 7.5inch)
         *
         * @since API 5
         */
        ENVELOPE_MONARCH,

        /**
         * Envelope 9 (3.875inch x 8.875inch)
         *
         * @since API 5
         */
        ENVELOPE_9,

        /**
         * Executive Rotated (10.5inch x 7.25inch)
         *
         * @since API 5
         */
        EXECUTIVE_ROTATE,

        /**
         * GENERAL_10X11in (10inch x 11inch)
         *
         * @since API 5
         */
        GENERAL_10X11in,

        /**
         * GENERAL_10X13in (10inch x 13inch)
         *
         * @since API 5
         */
        GENERAL_10X13in,

        /**
         * GENERAL_10X14in (10inch x 14inch)
         *
         * @since API 5
         */
        GENERAL_10X14in,

        /**
         * GENERAL_10X15in (10inch x 15inch)
         *
         * @since API 5
         */
        GENERAL_10X15in,

        /**
         * GENERAL_11X12in (11inch x 12inch)
         *
         * @since API 5
         */
        GENERAL_11X12in,

        /**
         * GENERAL_11X14in (11inch x 14inch)
         *
         * @since API 5
         */
        GENERAL_11X14in,

        /**
         * GENERAL_11X15in (11inch x 15inch)
         *
         * @since API 5
         */
        GENERAL_11X15in,

        /**
         * GENERAL_11X19in (11inch x 19inch)
         *
         * @since API 5
         */
        GENERAL_11X19in,

        /**
         * GENERAL_12X12in (12inch x 12inch)
         *
         * @since API 5
         */
        GENERAL_12X12in,

        /**
         * GENERAL_12X14in (12inch x 14inch)
         *
         * @since API 5
         */
        GENERAL_12X14in,

        /**
         * GENERAL_12X19in (12inch x 19inch)
         *
         * @since API 5
         */
        GENERAL_12X19in,

        /**
         * GENERAL_3POINT5X5in (3.5inch x 5inch)
         *
         * @since API 5
         */
        GENERAL_3POINT5X5in,

        /**
         * GENERAL_3X5in (3inch x 5inch)
         *
         * @since API 5
         */
        GENERAL_3X5in,

        /**
         * GENERAL_4X12in (4inch x 12inch)
         *
         * @since API 5
         */
        GENERAL_4X12in,

        /**
         * GENERAL_4X6in (4inch x 6inch)
         *
         * @since API 5
         */
        GENERAL_4X6in,

        /**
         * GENERAL_4X8in (4inch x 8inch)
         *
         * @since API 5
         */
        GENERAL_4X8in,

        /**
         * GENERAL_5X7in (5inch x 7inch)
         *
         * @since API 5
         */
        GENERAL_5X7in,

        /**
         * GENERAL_5X8in (5inch x 8inch)
         *
         * @since API 5
         */
        GENERAL_5X8in,

        /**
         * GENERAL_6X8in (6inch x 8inch)
         *
         * @since API 5
         */
        GENERAL_6X8in,

        /**
         * GENERAL_7X9in (7inch x 9inch)
         *
         * @since API 5
         */
        GENERAL_7X9in,

        /**
         * GENERAL_9X11in (9inch x 11inch)
         *
         * @since API 5
         */
        GENERAL_9X11in,

        /**
         * Government Legal (8inch x 13inch)
         *
         * @since API 5
         */
        GOVT_LEGAL,

        /**
         * Government Letter (8inch x 10inch)
         *
         * @since API 5
         */
        GOVT_LETTER,

        /**
         * ISO A0 (841mm x 1189mm)
         *
         * @since API 5
         */
        A0,

        /**
         * ISO A1 (594mm x 841mm)
         *
         * @since API 5
         */
        A1,

        /**
         * ISO A2 (420mm x 594mm)
         *
         * @since API 5
         */
        A2,

        /**
         * ISO A7 (74mm x 105mm)
         *
         * @since API 5
         */
        A7,

        /**
         * ISO A8 (52mm x 74mm)
         *
         * @since API 5
         */
        A8,

        /**
         * ISO A9 (37mm x 52mm)
         *
         * @since API 5
         */
        A9,

        /**
         * ISO A10 (26mm x 37mm)
         *
         * @since API 5
         */
        A10,

        /**
         * ISO B0 (1000mm x 1414mm)
         *
         * @since API 5
         */
        B0,

        /**
         * ISO B1 (707mm x 1000mm)
         *
         * @since API 5
         */
        B1,

        /**
         * ISO B2 (500mm x 707mm)
         *
         * @since API 5
         */
        B2,

        /**
         * ISO B3 (353mm x 500mm)
         *
         * @since API 5
         */
        B3,

        /**
         * ISO B7 (88mm x 125mm)
         *
         * @since API 5
         */
        B7,

        /**
         * ISO B8 (62mm x 88mm)
         *
         * @since API 5
         */
        B8,

        /**
         * ISO B9 (44mm x 62mm)
         *
         * @since API 5
         */
        B9,

        /**
         * ISO B10 (31mm x 44mm)
         *
         * @since API 5
         */
        B10,

        /**
         * ISO C0 (917mm x 1297mm)
         *
         * @since API 5
         */
        C0,

        /**
         * ISO C1 (648mm x 917mm)
         *
         * @since API 5
         */
        C1,

        /**
         * ISO C2 (458mm x 648mm)
         *
         * @since API 5
         */
        C2,

        /**
         * ISO C3 (324mm x 458mm)
         *
         * @since API 5
         */
        C3,

        /**
         * ISO C4 (229mm x 324mm)
         *
         * @since API 5
         */
        C4,

        /**
         * ISO C5 (162mm x 229mm)
         *
         * @since API 5
         */
        C5,

        /**
         * ISO C6 (114mm x 162mm)
         *
         * @since API 5
         */
        C6,

        /**
         * ISO C7 (81mm x 114mm)
         *
         * @since API 5
         */
        C7,

        /**
         * ISO C8 (57mm x 81mm)
         *
         * @since API 5
         */
        C8,

        /**
         * ISO C9 (40mm x 57mm)
         *
         * @since API 5
         */
        C9,

        /**
         * ISO C10 (28mm x 40mm)
         *
         * @since API 5
         */
        C10,

        /**
         * Japanese Double Postcard (148mm x 200mm)
         *
         * @since API 5
         */
        JDOUBLE_POSTCARD,

        /**
         * Japanese Double Postcard Rotated (200mm x 148mm)
         *
         * @since API 5
         */
        JDOUBLE_POSTCARD_ROTATE,

        /**
         * JIS B0 (1030mm x 1456mm)
         *
         * @since API 5
         */
        JB0,

        /**
         * JIS B1 (728mm x 1030mm)
         *
         * @since API 5
         */
        JB1,

        /**
         * JIS B2 (515mm x 728mm)
         *
         * @since API 5
         */
        JB2,

        /**
         * JIS B3 (364mm x 515mm)
         *
         * @since API 5
         */
        JB3,

        /**
         * JIS B7 (91mm x 128mm)
         *
         * @since API 5
         */
        JB7,

        /**
         * JIS B8 (64mm x 91mm)
         *
         * @since API 5
         */
        JB8,

        /**
         * JIS B9 (45mm x 64mm)
         *
         * @since API 5
         */
        JB9,

        /**
         * JIS B10 (32mm x 45mm)
         *
         * @since API 5
         */
        JB10,

        /**
         * JIS Chou3 (120mm x 235mm)
         *
         * @since API 5
         */
        JCHOU3,

        /**
         * JIS Chou4 (90mm x 205mm)
         *
         * @since API 5
         */
        JCHOU4,

        /**
         * JIS Exec (216mm x 330mm)
         *
         * @since API 5
         */
        JEXEC,

        /**
         * JIS Kaku2 (240mm x 332mm)
         *
         * @since API 5
         */
        JKAKU2,

        /**
         * Japanese Postcard (100mm x 148mm)
         *
         * @since API 5
         */
        JPOSTCARD,

        /**
         * K16_184X260mm (184mm x 260mm)
         *
         * @since API 5
         */
        K16_184X260mm,

        /**
         * K8_260X368mm (260mm x 368mm)
         *
         * @since API 5
         */
        K8_260X368mm,

        /**
         * Long Scan (8.5inch x 34inch)
         *
         * @since API 5
         */
        LONG_SCAN,

        /**
         * Mutsugiri (203mm x 254mm)
         *
         * @since API 5
         */
        MUTSUGIRI,

        /**
         * Oficio (216mm x 340mm)
         *
         * @since API 5
         */
        OFICIO,

        /**
         * RA0 (860mm x 1220mm)
         *
         * @since API 5
         */
        RA0,

        /**
         * RA1 (610mm x 860mm)
         *
         * @since API 5
         */
        RA1,

        /**
         * RA2 (430mm x 610mm)
         *
         * @since API 5
         */
        RA2,

        /**
         * RA3 (305mm x 430mm)
         *
         * @since API 5
         */
        RA3,

        /**
         * RA4 (215mm x 305mm)
         *
         * @since API 5
         */
        RA4,

        /**
         * SRA0 (900mm x 1280mm)
         *
         * @since API 5
         */
        SRA0,

        /**
         * SRA1 (640mm x 900mm)
         *
         * @since API 5
         */
        SRA1,

        /**
         * SRA2 (450mm x 640mm)
         *
         * @since API 5
         */
        SRA2,

        /**
         * SRA3 (320mm x 450mm)
         *
         * @since API 5
         */
        SRA3,

        /**
         * SRA4 (225mm x 320mm)
         *
         * @since API 5
         */
        SRA4,

        /**
         * Super B (13inch x 19inch)
         *
         * @since API 5
         */
        SUPER_B,

        /**
         * Index card (100mm x 150mm)
         *
         * @since API 5
         */
        INDEXCARD,

        /**
         * An indeterminable size due to lack of sensors
         *
         * @since API 5
         */
        UNKNOWN,

        /**
         * Any custom size
         *
         * @since API 5
         */
        ANY_CUSTOM,

        /**
         * An indeterminable envelope size due to lack of sensors
         *
         * @since API 5
         */
        UNKNOWN_ENVELOP;

        /**
         * Retrieves the height of paper
         *
         * @return <p>supported height from {@link PaperSize}.
         * <ul>
         * <li>Return the supported height</li>
         * <li>Return can be 0.0 if PaperSize height is not supported</li>
         * </ul>
         * </p>
         *
         * @since API 8
         */
        public double getHeight() {
            for (com.hp.workpath.api.PaperSize ps : com.hp.workpath.api.PaperSize.values()) {
                if (this.name().equals(ps.name())) {
                    return ps.getHeight();
                }
            }
            return 0;
        }

        /**
         * Retrieves the width of paper
         *
         * @return <p>supported width from {@link PaperSize}.
         * <ul>
         * <li>Return the supported width</li>
         * <li>Return can be 0.0 if PaperSize width is not supported</li>
         * </ul>
         * </p>
         *
         * @since API 8
         */
        public double getWidth() {
            for (com.hp.workpath.api.PaperSize ps : com.hp.workpath.api.PaperSize.values()) {
                if (this.name().equals(ps.name())) {
                    return ps.getWidth();
                }
            }
            return 0;
        }

        /**
         * Retrieves the unit of paper
         *
         * @return <p>supported unit from {@link PaperSize}.
         * <ul>
         * <li>Return the supported unit (mm or inch or cm) according to paper size.</li>
         * <li>Return can be null if PaperSize unit is not supported</li>
         * </ul>
         * </p>
         *
         * @since API 8
         */
        public String getUnit() {
            for (com.hp.workpath.api.PaperSize ps : com.hp.workpath.api.PaperSize.values()) {
                if (this.name().equals(ps.name())) {
                    return ps.getUnit();
                }
            }
            return null;
        }
    }

    /**
     * Paper Source defines the tray that feeds the copy output.
     *
     * @since API 3
     */
    public enum PaperSource {
        /**
         * Use printer's settings.
         *
         * @since API 3
         */
        DEFAULT,

        /**
         * Use printer auto selection
         *
         * @since API 3
         */
        AUTO,

        /**
         * Use tray 1
         *
         * @since API 3
         */
        TRAY_1,

        /**
         * Use tray 2
         *
         * @since API 3
         */
        TRAY_2,

        /**
         * Use tray 3
         *
         * @since API 3
         */
        TRAY_3,

        /**
         * Use tray 4
         *
         * @since API 3
         */
        TRAY_4,

        /**
         * Use tray 5
         *
         * @since API 3
         */
        TRAY_5,

        /**
         * Use manual feeder
         *
         * @since API 3
         */
        MANUAL_FEED,

        /**
         * Automatic Document Feeder
         *
         * @since API 5
         */
        ADF,

        /**
         * Envelope Feed
         *
         * @since API 5
         */
        ENVELOPE_FEED,

        /**
         * Flatbed/Glass
         *
         * @since API 5
         */
        FLATBED,

        /**
         * Tray 6
         *
         * @since API 5
         */
        TRAY_6,

        /**
         * Tray 7
         *
         * @since API 5
         */
        TRAY_7,

        /**
         * Tray 8
         *
         * @since API 5
         */
        TRAY_8,

        /**
         * Tray 9
         *
         * @since API 5
         */
        TRAY_9,

        /**
         * Tray 10
         *
         * @since API 5
         */
        TRAY_10,

        /**
         * Tray 11
         *
         * @since API 5
         */
        TRAY_11,

        /**
         * Tray 12
         *
         * @since API 5
         */
        TRAY_12,

        /**
         * Tray 13
         *
         * @since API 5
         */
        TRAY_13,

        /**
         * Tray 14
         *
         * @since API 5
         */
        TRAY_14,

        /**
         * Tray 15
         *
         * @since API 5
         */
        TRAY_15,

        /**
         * Tray 16
         *
         * @since API 5
         */
        TRAY_16,

        /**
         * Duplexer
         *
         * @since API 5
         */
        DUPLEXER,

        /**
         * External
         *
         * @since API 5
         */
        EXTERNAL,

        /**
         * External tray 1
         *
         * @since API 5
         */
        EXTERNAL_TRAY_1,

        /**
         * External tray 2
         *
         * @since API 5
         */
        EXTERNAL_TRAY_2,

        /**
         * External tray 3
         *
         * @since API 5
         */
        EXTERNAL_TRAY_3,

        /**
         * External tray 4
         *
         * @since API 5
         */
        EXTERNAL_TRAY_4,

        /**
         * External tray 5
         *
         * @since API 5
         */
        EXTERNAL_TRAY_5,

        /**
         * External tray 6
         *
         * @since API 5
         */
        EXTERNAL_TRAY_6,

        /**
         * External tray 7
         *
         * @since API 5
         */
        EXTERNAL_TRAY_7,

        /**
         * External tray 8
         *
         * @since API 5
         */
        EXTERNAL_TRAY_8,

        /**
         * External tray 9
         *
         * @since API 5
         */
        EXTERNAL_TRAY_9,

        /**
         * External tray 10
         *
         * @since API 5
         */
        EXTERNAL_TRAY_10,

        /**
         * Multipurpose Tray
         *
         * @since API 5
         */
        MULTIPURPOSE_TRAY,

        /**
         * Photo tray
         *
         * @since API 5
         */
        PHOTO_TRAY,

        /**
         * Rear manual feed
         *
         * @since API 5
         */
        REAR_MANUAL_FEED,

        /**
         * Roll 1
         *
         * @since API 5
         */
        ROLL_1,

        /**
         * Roll 2
         *
         * @since API 5
         */
        ROLL_2,

        /**
         * Roll 3
         *
         * @since API 5
         */
        ROLL_3,

        /**
         * Roll 4
         *
         * @since API 5
         */
        ROLL_4,

        /**
         * Envelope feed job settings
         *
         * @since API 5
         */
        ENVELOPE_FEED_JOB_SETTINGS,

        /**
         * Tray 1 job settings
         *
         * @since API 5
         */
        TRAY_1_JOB_SETTINGS
    }

    /**
     * Scale Mode controls reduce-enlarge mode of a scanned image.
     *
     * @since API 3
     */
    public enum ScaleMode {
        /**
         * Use printer's settings.
         *
         * @since API 3
         */
        DEFAULT,

        /**
         * Automatic based on media size
         *
         * @since API 3
         */
        AUTO,

        /**
         * Include margins when automatic calculation of reduction/enlargement
         *
         * @since API 3
         */
        AUTO_MARGINS_INCLUDED,

        /**
         * Reduce/Enlarge manually adjustable
         *
         * @since API 3
         */
        MANUAL
    }

    /**
     * <p>Job Assembly Mode controls whether to allow multiple scan segments assembled
     * into a single job when completed.</p>
     *
     * @since API 3
     */
    @SuppressWarnings("unused")
    public enum JobAssemblyMode {
        /**
         * Default value
         *
         * @since API 3
         */
        DEFAULT,

        /**
         * Scan only one segment per job
         *
         * @since API 3
         */
        OFF,

        /**
         * Scan multiple segments, prompting the walk-up user for another segment at the end of each segment,
         * and assembling all segments into a single job. Scan settings cannot be adjusted between segments.
         * This mode is intended for jobs where the number of sheets exceeds the capacity of the ADF
         *
         * @since API 3
         */
        ON
    }

    /**
     * <p>Job Execution Mode controls whether to print the scanned images or store as a stored job</p>
     *
     * @since API 3
     */
    @SuppressWarnings("unused")
    public enum JobExecutionMode {
        /**
         * Scan and print
         *
         * @since API 3
         */
        NORMAL,

        /**
         * Scan and store in job storage to release later
         *
         * @since API 3
         */
        STORE
    }

    /**
     * Number Up Mode controls how many scanned images are placed per printer sheet.
     *
     * @since API 3
     */
    public enum NumberUpMode {
        /**
         * Use printer's settings.
         *
         * @since API 3
         */
        DEFAULT,

        /**
         * One image per sheet
         *
         * @since API 3
         */
        ONE_UP,

        /**
         * Two images per sheet
         *
         * @since API 3
         */
        TWO_UP,

        /**
         * Four images per sheet
         *
         * @since API 3
         */
        FOUR_UP,

        /**
         * Eight images per sheet
         *
         * @since API 3
         */
        EIGHT_UP,

        /**
         * Sixteen images per sheet
         *
         * @since API 3
         */
        SIXTEEN_UP,

        /**
         * Thirty two images per sheet
         *
         * @since API 3
         */
        THIRTY_TWO
    }

    /**
     * Number Up Direction controls the order of images placed on a printed sheet.
     *
     * @since API 3
     */
    public enum NumberUpDirection {
        /**
         * Use printer's settings.
         *
         * @since API 3
         */
        DEFAULT,

        /**
         * From top to bottom and from right to left
         *
         * @since API 3
         */
        TO_BOTTOM_TO_LEFT,

        /**
         * from top to bottom and from left to right
         *
         * @since API 3
         */
        TO_BOTTOM_TO_RIGHT,

        /**
         * From right to left and from top to bottom
         *
         * @since API 3
         */
        TO_LEFT_TO_BOTTOM,

        /**
         * From right to left and bottom to top
         *
         * @since API 3
         */
        TO_LEFT_TO_TOP,

        /**
         * From left to right and from top to bottom
         *
         * @since API 3
         */
        TO_RIGHT_TO_BOTTOM,

        /**
         * From left to right and from bottom to top
         *
         * @since API 3
         */
        TO_RIGHT_TO_TOP,

        /**
         * From bottom to top and from right to left
         *
         * @since API 3
         */
        TO_TOP_TO_LEFT,

        /**
         * From bottom to top and from left to right
         *
         * @since API 3
         */
        TO_TOP_TO_RIGHT
    }

    /**
     * Retention mode for stored copy job supported by current printer.
     *
     * @since API 3
     */
    public enum RetentionMode {
        /**
         * Use printer's settings.
         *
         * @since API 3
         */
        DEFAULT,

        /**
         * Don't delete
         *
         * @since API 3
         */
        KEEP,

        /**
         * Delete
         *
         * @since API 3
         */
        DELETE
    }

    /**
     * The paper type for printing.
     *
     * @since API 3
     */
    public enum PaperType {
        /**
         * Sets device's settings.
         *
         * @since API 3
         */
        DEFAULT,
        /**
         * Card Stock 176-220g
         *
         * @since API 3
         */
        CARD_STOCK,
        /**
         * Card Stock Glossy 176-220g
         *
         * @since API 3
         */
        CARD_STOCK_GLOSSY,
        /**
         * HP EcoFFICIENT
         *
         * @since API 3
         */
        HP_ECOFFICIENT,
        /**
         * HP Glossy 120g
         *
         * @since API 3
         */
        HP_GLOSSY_120G,
        /**
         * HP Glossy 150g
         *
         * @since API 3
         */
        HP_GLOSSY_150G,
        /**
         * HP Glossy 200g
         *
         * @since API 3
         */
        HP_GLOSSY_200G,
        /**
         * HP Matte 105g
         *
         * @since API 3
         */
        HP_MATTE_105G,
        /**
         * HP Matte 120g
         *
         * @since API 3
         */
        HP_MATTE_120G,
        /**
         * HP Matter 150g
         *
         * @since API 3
         */
        HP_MATTE_150G,
        /**
         * HP Matte 200g
         *
         * @since API 3
         */
        HP_MATTE_200G,
        /**
         * HP Matte 90g
         *
         * @since API 3
         */
        HP_MATTE_90G,
        /**
         * HP SoftGloss 120g
         *
         * @since API 3
         */
        HP_SOFT_GLOSS_120G,
        /**
         * Extra Heavy 131-175g
         *
         * @since API 3
         */
        EXTRA_HEAVY,
        /**
         * Extra Heavy Glossy 131-175g
         *
         * @since API 3
         */
        EXTRA_HEAVY_GLOSSY,
        /**
         * Heavy 111-130g
         *
         * @since API 3
         */
        HEAVY,
        /**
         * Mid-weight 96-110g
         *
         * @since API 3
         */
        MID_WEIGHT,
        /**
         * Intermediate 85-95g
         *
         * @since API 3
         */
        INTERMEDIATE,
        /**
         * Light 60-74g
         *
         * @since API 3
         */
        LIGHT,
        /**
         * Recycled
         *
         * @since API 3
         */
        RECYCLED,
        /**
         * Rough
         *
         * @since API 3
         */
        ROUGH,
        /**
         * Heavy Rough
         *
         * @since API 3
         */
        HEAVY_ROUGH,
        /**
         * Envelope
         *
         * @since API 3
         */
        ENVELOPE,
        /**
         * Heavy Envelope
         *
         * @since API 3
         */
        HEAVY_ENVELOPE,
        /**
         * Labels
         *
         * @since API 3
         */
        LABELS,
        /**
         * Plain
         *
         * @since API 3
         */
        PLAIN,
        /**
         * Bond
         *
         * @since API 3
         */
        BOND,
        /**
         * Colored
         *
         * @since API 3
         */
        COLORED,
        /**
         * Letterhead
         *
         * @since API 3
         */
        LETTERHEAD,
        /**
         * Preprinted
         *
         * @since API 3
         */
        PREPRINTED,
        /**
         * Prepunched
         *
         * @since API 3
         */
        PREPUNCHED,
        /**
         * Transparency
         *
         * @since API 3
         */
        TRANSPARENCY,
        /**
         * HP Advanced Photo
         *
         * @since API 3
         */
        HP_ADVANCED_PHOTO,
        /**
         * HP Brochure Glossy
         *
         * @since API 3
         */
        HP_BROCHURE_GLOSSY,
        /**
         * HP Brochure Matte 180g
         *
         * @since API 3
         */
        HP_BROCHURE_MATTE_180G,
        /**
         * HP InkJet Matte 120g (Premium Presentation)
         *
         * @since API 3
         */
        HP_INKJET_MATTE_120G,
        /**
         * Any type
         *
         * @since API 5
         */
        ANY,
        /**
         * Brochure Matte
         *
         * @since API 5
         */
        BROCHURE_MATTE,
        /**
         * Cover Matte
         *
         * @since API 5
         */
        COVER_MATTE,
        /**
         * Heavy Glossy (111 to 130g)
         *
         * @since API 5
         */
        HEAVY_GLOSSY,
        /**
         * Heavy paperboard
         *
         * @since API 5
         */
        HEAVY_PAPERBOARD,
        /**
         * HP Cover Matte (200g)
         *
         * @since API 5
         */
        HP_COVER_MATTE_200G,
        /**
         * HP Everyday Photo Matte
         *
         * @since API 5
         */
        HP_EVERYDAY_PHOTO_MATTE,
        /**
         * HP Glossy Edgeline 180G
         *
         * @since API 5
         */
        HP_GLOSSY_EDGELINE_180G,
        /**
         * HP Matte Brochure And Flyer (180g)
         *
         * @since API 5
         */
        HP_MATTE_BROCHURE_AND_FLYER_180G,
        /**
         * HP Photo
         *
         * @since API 5
         */
        HP_PHOTO,
        /**
         * HP Photo Plus
         *
         * @since API 5
         */
        HP_PHOTO_PLUS,
        /**
         * HP Premium Inkjet Transparency
         *
         * @since API 5
         */
        HP_PREMIUM_INKJET_TRANSPARENCY,
        /**
         * HP Premium Matte (120g)
         *
         * @since API 5
         */
        HP_PREMIUM_MATTE_120G,
        /**
         * HP Tough
         *
         * @since API 5
         */
        HP_TOUGH,
        /**
         * HP Trifold Glossy (160g)
         *
         * @since API 5
         */
        HP_TRIFOLD_GLOSSY_160G,
        /**
         * Light bond
         *
         * @since API 5
         */
        LIGHT_BOND,
        /**
         * Light paperboard
         *
         * @since API 5
         */
        LIGHT_PAPERBOARD,
        /**
         * Light rough
         *
         * @since API 5
         */
        LIGHT_ROUGH,
        /**
         * Matte
         *
         * @since API 5
         */
        MATTE,
        /**
         * Midweight Gloss (96 to 110g)
         *
         * @since API 5
         */
        MID_WEIGHT_GLOSSY,
        /**
         * Opaque Film
         *
         * @since API 5
         */
        OPAQUE_FILM,
        /**
         * Paperboard
         *
         * @since API 5
         */
        PAPERBOARD,
        /**
         * Photo
         *
         * @since API 5
         */
        PHOTO,
        /**
         * Shelf Edge Labels
         *
         * @since API 5
         */
        SHELF_EDGE_LABELS,
        /**
         * Tab
         *
         * @since API 5
         */
        TAB,
        /**
         * Thick Plain
         *
         * @since API 5
         */
        THICK_PLAIN,
        /**
         * Vellum
         *
         * @since API 5
         */
        VELLUM,
        /**
         * User defined type 1
         *
         * @since API 5
         */
        USER_DEFINED_1,
        /**
         * User defined type 2
         *
         * @since API 5
         */
        USER_DEFINED_2,
        /**
         * User defined type 3
         *
         * @since API 5
         */
        USER_DEFINED_3,
        /**
         * User defined type 4
         *
         * @since API 5
         */
        USER_DEFINED_4,
        /**
         * User defined type 5
         *
         * @since API 5
         */
        USER_DEFINED_5,
        /**
         * User defined type 6
         *
         * @since API 5
         */
        USER_DEFINED_6,
        /**
         * User defined type 7
         *
         * @since API 5
         */
        USER_DEFINED_7,
        /**
         * User defined type 8
         *
         * @since API 5
         */
        USER_DEFINED_8,
        /**
         * User defined type 9
         *
         * @since API 5
         */
        USER_DEFINED_9,
        /**
         * User defined type 10
         *
         * @since API 5
         */
        USER_DEFINED_10,
        /**
         * User defined type 11
         *
         * @since API 5
         */
        USER_DEFINED_11,
        /**
         * User defined type 12
         *
         * @since API 5
         */
        USER_DEFINED_12,
        /**
         * User defined type 13
         *
         * @since API 5
         */
        USER_DEFINED_13,
        /**
         * User defined type 14
         *
         * @since API 5
         */
        USER_DEFINED_14,
        /**
         * User defined type 15
         *
         * @since API 5
         */
        USER_DEFINED_15,
        /**
         * User defined type 16
         *
         * @since API 5
         */
        USER_DEFINED_16,
        /**
         * Auto
         *
         * @since API 5
         */
        AUTO,
        /**
         * Heavy bond
         *
         * @since API 5
         */
        HEAVY_BOND

    }

    /**
     * <p>Enumeration of text versus graphics optimization settings</p>
     *
     * @since API 3
     */
    @SuppressWarnings("unused")
    public enum TextGraphicsOptimization {
        /**
         * Default value
         *
         * @since API
         */
        DEFAULT,

        /**
         * Optimize for all text.
         *
         * @since API 3
         */
        TEXT,

        /**
         * Optimize for mixed text with graphics.
         *
         * @since API 3
         */
        MIXED,

        /**
         * Optimize for all graphics.
         *
         * @since API 3
         */
        GRAPHIC,

        /**
         * Optimize for photograph.
         *
         * @since API 3
         */
        PHOTOGRAPH,

        /**
         * Automatically detect.
         *
         * @since API 3
         */
        AUTODETECT
    }

    /**
     * <p>Output bin options suppprted by the current printer</p>
     *
     * @since API 5
     */
    @SuppressWarnings("unused")
    public enum OutputBin {
        /**
         * Default value
         *
         * @since API 5
         */
        DEFAULT,

        /**
         * Accessory bin
         *
         * @since API 5
         */
        ACCESSORY,

        /**
         * ADF output bin
         *
         * @since API 5
         */
        ADF,

        /**
         * Printer selected output bin
         *
         * @since API 5
         */
        AUTO,

        /**
         * Booklet bin
         *
         * @since API 5
         */
        BOOKLET,

        /**
         * Document feeder output bin
         *
         * @since API 5
         */
        DOCUMENT_FEEDER,

        /**
         * External bin
         *
         * @since API 5
         */
        EXTERNAL,

        /**
         * Face down bin
         *
         * @since API 5
         */
        FACE_DOWN,

        /**
         * Face down bin correct order
         *
         * @since API 5
         */
        FACE_DOWN_CORRECT_ORDER,

        /**
         * Face up bin
         *
         * @since API 5
         */
        FACE_UP,

        /**
         * Face up bin with the straightest path
         *
         * @since API 5
         */
        FACE_UP_STRAIGHTEST_PATH,

        /**
         * Fax output bin
         *
         * @since API 5
         */
        FAX,

        /**
         * Folded output bin
         *
         * @since API 5
         */
        FOLDED,

        /**
         * Left bin
         *
         * @since API 5
         */
        LEFT,

        /**
         * Left bin with the straightest path
         *
         * @since API 5
         */
        LEFT_STRAIGHTEST_PATH,

        /**
         * Lower bin
         *
         * @since API 5
         */
        LOWER,

        /**
         * Lower booklet bin
         *
         * @since API 5
         */
        LOWER_BOOKLET,

        /**
         * Lower left bin
         *
         * @since API 5
         */
        LOWER_LEFT,

        /**
         * Lower left highest capacity bin
         *
         * @since API 5
         */
        LOWER_LEFT_HIGHEST_CAPACITY,

        /**
         * Lower stacker bin
         *
         * @since API 5
         */
        LOWER_STACKER,

        /**
         * Main copier bin
         *
         * @since API 5
         */
        MAIN_COPIER,

        /**
         * Middle bin
         *
         * @since API 5
         */
        MIDDLE,

        /**
         * Middle left bin
         *
         * @since API 5
         */
        MIDDLE_LEFT,

        /**
         * Output bin 1
         *
         * @since API 5
         */
        OUTPUT_BIN_1,

        /**
         * Output bin 2
         *
         * @since API 5
         */
        OUTPUT_BIN_2,

        /**
         * Output bin 3
         *
         * @since API 5
         */
        OUTPUT_BIN_3,

        /**
         * Output bin 4
         *
         * @since API 5
         */
        OUTPUT_BIN_4,

        /**
         * Output bin 5
         *
         * @since API 5
         */
        OUTPUT_BIN_5,

        /**
         * Output bin 6
         *
         * @since API 5
         */
        OUTPUT_BIN_6,

        /**
         * Output bin 7
         *
         * @since API 5
         */
        OUTPUT_BIN_7,

        /**
         * Output bin 8
         *
         * @since API 5
         */
        OUTPUT_BIN_8,

        /**
         * Output bin 9
         *
         * @since API 5
         */
        OUTPUT_BIN_9,

        /**
         * Output bin 10
         *
         * @since API 5
         */
        OUTPUT_BIN_10,

        /**
         * Output bin 11
         *
         * @since API 5
         */
        OUTPUT_BIN_11,

        /**
         * Output bin 12
         *
         * @since API 5
         */
        OUTPUT_BIN_12,

        /**
         * Output bin 13
         *
         * @since API 5
         */
        OUTPUT_BIN_13,

        /**
         * Output bin 14
         *
         * @since API 5
         */
        OUTPUT_BIN_14,

        /**
         * Output bin 15
         *
         * @since API 5
         */
        OUTPUT_BIN_15,

        /**
         * Output bin 16
         *
         * @since API 5
         */
        OUTPUT_BIN_16,

        /**
         * Rear bin
         *
         * @since API 5
         */
        REAR,

        /**
         * Rear face up bin
         *
         * @since API 5
         */
        REAR_FACE_UP,

        /**
         * Rear bin with the straightest path
         *
         * @since API 5
         */
        REAR_STRAIGHTEST_PATH,

        /**
         * Stacker
         *
         * @since API 5
         */
        STACKER,

        /**
         * Standard bin
         *
         * @since API 5
         */
        STANDARD,

        /**
         * Standard bin correct order
         *
         * @since API 5
         */
        STANDARD_CORRECT_ORDER,

        /**
         * Standard top bin
         *
         * @since API 5
         */
        STANDARD_TOP,

        /**
         * Top bin
         *
         * @since API 5
         */
        TOP,

        /**
         * Upper bin
         *
         * @since API 5
         */
        UPPER,

        /**
         * Upper face up bin
         *
         * @since API 5
         */
        UPPER_FACE_UP,

        /**
         * Upper left bin
         *
         * @since API 5
         */
        UPPER_LEFT,

        /**
         * Upper left bins
         *
         * @since API 5
         */
        UPPER_LEFT_BINS,

        /**
         * Upper left bin with the straightest path
         *
         * @since API 5
         */
        UPPER_LEFT_STRAIGHTEST_PATH,

        /**
         * Bins 1 to 3 (virtual bin)
         *
         * @since API 5
         */
        VIRTUAL_BINS_1TO3,

        /**
         * Bins 1 to 5 (virtual bin)
         *
         * @since API 5
         */
        VIRTUAL_BINS_1TO5,

        /**
         * Bins 1 to 8 (virtual bin)
         *
         * @since API 5
         */
        VIRTUAL_BINS_1TO8,

        /**
         * Bins 1 to 10 (virtual bin)
         *
         * @since API 5
         */
        VIRTUAL_BINS_1TO10,

        /**
         * Bins 2 to 8 (virtual bin)
         *
         * @since API 5
         */
        VIRTUAL_BINS_2TO8,

        /**
         * Finisher Bins (virtual bin)
         *
         * @since API 5
         */
        VIRTUAL_FINISHER_BINS,

        /**
         * Left Bins (virtual bin)
         *
         * @since API 5
         */
        VIRTUAL_LEFT_BINS,

        /**
         * An alternate output bin
         *
         * @since API 5
         */
        ALTERNATE,

        /**
         * The bottom output bin
         *
         * @since API 5
         */
        BOTTOM,

        /**
         * The center output bin
         *
         * @since API 5
         */
        CENTER,

        /**
         * A collator output bin
         *
         * @since API 5
         */
        COLLATOR,

        /**
         * A duplexer output bin
         *
         * @since API 5
         */
        DUPLEXER,

        /**
         * Engine optional bin 1
         *
         * @since API 5
         */
        ENGINE_OPTIONAL_BIN_1,

        /**
         * Large capacity bin
         *
         * @since API 5
         */
        LARGE_CAPACITY,

        /**
         * A custom mailbox bin
         *
         * @since API 5
         */
        MY_MAILBOX,

        /**
         * The righ output bin
         *
         * @since API 5
         */
        RIGHT,

        /**
         * The side output bin
         *
         * @since API 5
         */
        SIDE,

        /**
         * Stacker face down
         *
         * @since API 5
         */
        STACKER_FACEDOWN,

        /**
         * Stacker face up
         *
         * @since API 5
         */
        STACKER_FACE_UP,

        /**
         * Stacker staples
         *
         * @since API 5
         */
        STACKER_STAPLES,

        /**
         * Universal output bin
         *
         * @since API 5
         */
        UNIVERSAL_OUTPUT_BIN,

        /**
         * Stapler
         *
         * @since API 5
         */
        STAPLER
    }

    /**
     * <p>Progress dialog mode options supported by current printer</p>
     *
     * @since API 5
     */
    @SuppressWarnings("unused")
    public enum ProgressDialogMode {
        /**
         * Default value
         *
         * @since API 5
         */
        DEFAULT,

        /**
         * Off
         *
         * @since API 5
         */
        OFF,

        /**
         * On
         *
         * @since API 5
         */
        ON
    }

    /**
     * <p>Margin unit used for erase edge</p>
     *
     * @since API 6
     */
    @SuppressWarnings("unused")
    public enum EraseMarginUnit {
        /**
         * Default value
         *
         * @since API 6
         */
        DEFAULT,

        /**
         * Feature should use inches
         *
         * @since API 6
         */
        INCHES,

        /**
         * Feature should use millimeters
         *
         * @since API 6
         */
        MILLIMETERS
    }

    /**
     * <p>Capture Mode for Copy</p>
     *
     * @since API 6
     */
    public enum CaptureMode {
        /**
         * Default value
         *
         * @since API 6
         */
        DEFAULT,

        /**
         * Standard
         *
         * @since API 6
         */
        STANDARD,

        /**
         * Standard add pages
         *
         * @since API 6
         */
        STANDARD_ADD_PAGES,

        /**
         * Book capture
         *
         * @since API 6
         */
        BOOK_CAPTURE,

        /**
         * ID capture prompt both sides
         *
         * @since API 6
         */
        ID_CAPTURE_PROMPT_BOTH_SIDES,

        /**
         * ID capture prompt back side only
         *
         * @since API 6
         */
        ID_CAPTURE_PROMPT_BACK_SIDE_ONLY,
    }

    /**
     * <p>ImageShift Reduce To Fit</p>
     *
     * @since API 5
     */
    public enum ImageShiftReduceToFit {
        /**
         * Default value
         *
         * @since API 3
         */
        DEFAULT,
        /**
         * true
         *
         * @since API 5
         */
        TRUE,
        /**
         * false
         *
         * @since API 5
         */
        FALSE,
    }

    /**
     * <p>Image Shift Units</p>
     *
     * @since API 5
     */
    public enum ImageShiftUnits {
        /**
         * Default value
         *
         * @since API 5
         */
        DEFAULT,

        /**
         * Feature should use inches
         *
         * @since API 5
         */
        INCHES,

        /**
         * Feature should use millimeters
         *
         * @since API 5
         */
        MILLIMETERS
    }

    /**
     * <p>Booklet Borders Each Page</p>
     *
     * @since API 5
     */
    public enum BookletBordersEachPage {
        /**
         * Default value
         *
         * @since API 3
         */
        DEFAULT,
        /**
         * true
         *
         * @since API 5
         */
        TRUE,
        /**
         * false
         *
         * @since API 5
         */
        FALSE
    }

    /**
     * <p>Booklet Finishing Option</p>
     *
     * @since API 5
     */
    public enum BookletFinishingOption {
        /**
         * Default value
         *
         * @since API 5
         */
        DEFAULT,
        /**
         * Bale
         *
         * @since API 5
         */
        BALE,
        /**
         * Bind
         *
         * @since API 5
         */
        BIND,
        /**
         * Bind bottom
         *
         * @since API 5
         */
        BINDBOTTOM,
        /**
         * Bind left
         *
         * @since API 5
         */
        BINDLEFT,
        /**
         * Bind right
         *
         * @since API 5
         */
        BINDRIGHT,
        /**
         * Bind top
         *
         * @since API 5
         */
        BINDTOP,
        /**
         * Booklet maker
         *
         * @since API 5
         */
        BOOKLETMAKER,
        /**
         * Cover
         *
         * @since API 5
         */
        COVER,
        /**
         * Edge stitch
         *
         * @since API 5
         */
        EDGESTITCH,
        /**
         * Edge stitch bottom
         *
         * @since API 5
         */
        EDGESTITCHBOTTOM,
        /**
         * Edge stitch left
         *
         * @since API 5
         */
        EDGESTITCHLEFT,
        /**
         * Edge stich right
         *
         * @since API 5
         */
        EDGESTICHRIGHT,
        /**
         * Edge stitch top
         *
         * @since API 5
         */
        EDGESTITCHTOP,
        /**
         * Fold
         *
         * @since API 5
         */
        FOLD,
        /**
         * Jog offset
         *
         * @since API 5
         */
        JOGOFFSET,
        /**
         * None
         *
         * @since API 5
         */
        NONE,
        /**
         * Saddle stitch
         *
         * @since API 5
         */
        SADDLESTITCH,
        /**
         * Trim
         *
         * @since API 5
         */
        TRIM,
        /**
         * Other
         *
         * @since API 5
         */
        OTHER
    }

    /**
     * <p>Booklet Format</p>
     *
     * @since API 5
     */
    public enum BookletFormat {
        /**
         * Default value
         *
         * @since API 5
         */
        DEFAULT,
        /**
         * Off
         *
         * @since API 5
         */
        OFF,
        /**
         * Left edge
         *
         * @since API 5
         */
        LEFTEDGE,
        /**
         * Right edge
         *
         * @since API 5
         */
        RIGHTEDGE,
        /**
         * Other
         *
         * @since API 5
         */
        OTHER
    }

    /**
     * <p>Staple mode</p>
     *
     * @since API 6
     */
    @SuppressWarnings("unused")
    public enum StapleOption {
        /**
         * None
         *
         * @since API 6
         */
        NONE,

        /**
         * Default
         *
         * @since API 6
         */
        DEFAULT,

        /**
         * TopAnyOnePointAny
         *
         * @since API 6
         */
        TOP_ANY_ONE_POINT_ANY,

        /**
         * TopAnyonePointAngled
         *
         * @since API 6
         */
        TOP_ANY_ONE_POINT_ANGLED,

        /**
         * TopLeftOnePointAny
         *
         * @since API 6
         */
        TOP_LEFT_ONE_POINT_ANY,

        /**
         * TopLeftOnePointAngled
         *
         * @since API 6
         */
        TOP_LEFT_ONE_POINT_ANGLED,

        /**
         * TopLeftOnePointHorizontal
         *
         * @since API 6
         */
        TOP_LEFT_ONE_POINT_HORIZONTAL,

        /**
         * TopLeftOnePointVertical
         *
         * @since API 6
         */
        TOP_LEFT_ONE_POINT_VERTICAL,

        /**
         * TopRightOnePointAny
         *
         * @since API 6
         */
        TOP_RIGHT_ONE_POINT_ANY,

        /**
         * TopRightOnePointAngled
         *
         * @since API 6
         */
        TOP_RIGHT_ONE_POINT_ANGLED,

        /**
         * TopRightOnePointHorizontal
         *
         * @since API 6
         */
        TOP_RIGHT_ONE_POINT_HORIZONTAL,

        /**
         * TopRightOnePointVertical
         *
         * @since API 6
         */
        TOP_RIGHT_ONE_POINT_VERTICAL,

        /**
         * BottomAnyOnePointAny
         *
         * @since API 6
         */
        BOTTOM_ANY_ONE_POINT_ANY,

        /**
         * BottomAnyonePointAngled
         *
         * @since API 6
         */
        BOTTOM_ANY_ONE_POINT_ANGLED,

        /**
         * BottomLeftOnePointAny
         *
         * @since API 6
         */
        BOTTOM_LEFT_ONE_POINT_ANY,

        /**
         * BottomLeftOnePointAngled
         *
         * @since API 6
         */
        BOTTOM_LEFT_ONE_POINT_ANGLED,

        /**
         * BottomLeftOnePointHorizontal
         *
         * @since API 6
         */
        BOTTOM_LEFT_ONE_POINT_HORIZONTAL,

        /**
         * BottomLeftOnePointVertical
         *
         * @since API 6
         */
        BOTTOM_LEFT_ONE_POINT_VERTICAL,

        /**
         * BottomRightOnePointAny
         *
         * @since API 6
         */
        BOTTOM_RIGHT_ONE_POINT_ANY,

        /**
         * BottomRightOnePointAngled
         *
         * @since API 6
         */
        BOTTOM_RIGHT_ONE_POINT_ANGLED,

        /**
         * BottomRightOnePointHorizontal
         *
         * @since API 6
         */
        BOTTOM_RIGHT_ONE_POINT_HORIZONTAL,

        /**
         * BottomRightOnePointVertical
         *
         * @since API 6
         */
        BOTTOM_RIGHT_ONE_POINT_VERTICAL,

        /**
         * CenterOnePoint
         *
         * @since API 6
         */
        CENTER_ONE_POINT,

        /**
         * LeftTwoPoints
         *
         * @since API 6
         */
        LEFT_TWO_POINTS,

        /**
         * LeftTwoPointsAny
         *
         * @since API 6
         */
        LEFT_TWO_POINTS_ANY,

        /**
         * RightTwoPoints
         *
         * @since API 6
         */
        RIGHT_TWO_POINTS,

        /**
         * TopTwoPoints
         *
         * @since API 6
         */
        TOP_TWO_POINTS,

        /**
         * BottomTwoPoints
         *
         * @since API 6
         */
        BOTTOM_TWO_POINTS,

        /**
         * CenterTwoPoints
         *
         * @since API 6
         */
        CENTER_TWO_POINTS,

        /**
         * LeftThreePoints
         *
         * @since API 6
         */
        LEFT_THREE_POINTS,

        /**
         * LeftThreePointsAny
         *
         * @since API 6
         */
        LEFT_THREE_POINTS_ANY,

        /**
         * RightThreePoints
         *
         * @since API 6
         */
        RIGHT_THREE_POINTS,

        /**
         * TopThreePoints
         *
         * @since API 6
         */
        TOP_THREE_POINTS,

        /**
         * BottomThreePoints
         *
         * @since API 6
         */
        BOTTOM_THREE_POINTS,

        /**
         * CenterThreePoints
         *
         * @since API 6
         */
        CENTER_THREE_POINTS,

        /**
         * LeftSixPoints
         *
         * @since API 6
         */
        LEFT_SIX_POINTS,

        /**
         * LeftSixPointsAny
         *
         * @since API 6
         */
        LEFT_SIX_POINTS_ANY,

        /**
         * RightSixPoints
         *
         * @since API 6
         */
        RIGHT_SIX_POINTS,

        /**
         * TopSixPoints
         *
         * @since API 6
         */
        TOP_SIX_POINTS,

        /**
         * BottomSixPoints
         *
         * @since API 6
         */
        BOTTOM_SIX_POINTS,

        /**
         * CenterSixPoints
         *
         * @since API 6
         */
        CENTER_SIX_POINTS,

        /**
         * CustomLegacy
         *
         * @since API 6
         */
        CUSTOM_LEGACY,

        /**
         * CustomPointsOption1
         *
         * @since API 6
         */
        CUSTOM_POINTS_OPTION_1,

        /**
         * CustomPointsOption2
         *
         * @since API 6
         */
        CUSTOM_POINTS_OPTION_2,

        /**
         * CustomPointsOption3
         *
         * @since API 6
         */
        CUSTOM_POINTS_OPTION_3,

        /**
         * CustomPointsOption4
         *
         * @since API 6
         */
        CUSTOM_POINTS_OPTION_4,

        /**
         * CustomPointsOption5
         *
         * @since API 6
         */
        CUSTOM_POINTS_OPTION_5,

        /**
         * CustomPointsOption6
         *
         * @since API 6
         */
        CUSTOM_POINTS_OPTION_6,

        /**
         * CustomPointsOption7
         *
         * @since API 6
         */
        CUSTOM_POINTS_OPTION_7,

        /**
         * CustomPointsOption8
         *
         * @since API 6
         */
        CUSTOM_POINTS_OPTION_8,

        /**
         * CustomPointsOption9
         *
         * @since API 6
         */
        CUSTOM_POINTS_OPTION_9,

        /**
         * CustomPointsOption10
         *
         * @since API 6
         */
        CUSTOM_POINTS_OPTION_10,

        /**
         * ExtraOption1
         *
         * @since API 6
         */
        EXTRA_OPTION_1,

        /**
         * ExtraOption2
         *
         * @since API 6
         */
        EXTRA_OPTION_2,

        /**
         * ExtraOption3
         *
         * @since API 6
         */
        EXTRA_OPTION_3,

        /**
         * ExtraOption4
         *
         * @since API 6
         */
        EXTRA_OPTION_4,

        /**
         * ExtraOption5
         *
         * @since API 6
         */
        EXTRA_OPTION_5,

        /**
         * ExtraOption6
         *
         * @since API 6
         */
        EXTRA_OPTION_6,

        /**
         * ExtraOption7
         *
         * @since API 6
         */
        EXTRA_OPTION_7,

        /**
         * ExtraOption8
         *
         * @since API 6
         */
        EXTRA_OPTION_8,

        /**
         * ExtraOption9
         *
         * @since API 6
         */
        EXTRA_OPTION_9,

        /**
         * ExtraOption10
         *
         * @since API 6
         */
        EXTRA_OPTION_10,

        /**
         * Other
         *
         * @since API 6
         */
        OTHER
    }

    /**
     * <p>Punch mode</p>
     *
     * @since API 6
     */
    @SuppressWarnings("unused")
    public enum PunchMode {
        /**
         * None
         *
         * @since API 6
         */
        NONE,

        /**
         * Default
         *
         * @since API 6
         */
        DEFAULT,

        /**
         * TwoPointAny
         *
         * @since API 6
         */
        TWO_POINT_ANY,

        /**
         * LeftTwoPointDin
         *
         * @since API 6
         */
        LEFT_TWO_POINT_DIN,

        /**
         * RightTwoPointDin
         *
         * @since API 6
         */
        RIGHT_TWO_POINT_DIN,

        /**
         * TopTwoPointDin
         *
         * @since API 6
         */
        TOP_TWO_POINT_DIN,

        /**
         * BottomTwoPointDin
         *
         * @since API 6
         */
        BOTTOM_TWO_POINT_DIN,

        /**
         * TwoPointDin
         *
         * @since API 6
         */
        TWO_POINT_DIN,

        /**
         * LeftTwoPointUs
         *
         * @since API 6
         */
        LEFT_TWO_POINT_US,

        /**
         * RightTwoPointUs
         *
         * @since API 6
         */
        RIGHT_TWO_POINT_US,

        /**
         * TopTwoPointUs
         *
         * @since API 6
         */
        TOP_TWO_POINT_US,

        /**
         * BottomTwoPointUs
         *
         * @since API 6
         */
        BOTTOM_TWO_POINT_US,

        /**
         * TwoPointUs
         *
         * @since API 6
         */
        TWO_POINT_US,

        /**
         * LeftThreePointUs
         *
         * @since API 6
         */
        LEFT_THREE_POINT_US,

        /**
         * RightThreePointUs
         *
         * @since API 6
         */
        RIGHT_THREE_POINT_US,

        /**
         * TopThreePointUs
         *
         * @since API 6
         */
        TOP_THREE_POINT_US,

        /**
         * BottomThreePointUs
         *
         * @since API 6
         */
        BOTTOM_THREE_POINT_US,

        /**
         * ThreePointUs
         *
         * @since API 6
         */
        THREE_POINT_US,

        /**
         * ThreePointAny
         *
         * @since API 6
         */
        THREE_POINT_ANY,

        /**
         * LeftFourPointDin
         *
         * @since API 6
         */
        LEFT_FOUR_POINT_DIN,

        /**
         * RightFourPointDin
         *
         * @since API 6
         */
        RIGHT_FOUR_POINT_DIN,

        /**
         * BottomFourPointDin
         *
         * @since API 6
         */
        BOTTOM_FOUR_POINT_DIN,

        /**
         * FourPointDin
         *
         * @since API 6
         */
        FOUR_POINT_DIN,

        /**
         * LeftFourPointSwd
         *
         * @since API 6
         */
        LEFT_FOUR_POINT_SWD,

        /**
         * RightFourPointSwd
         *
         * @since API 6
         */
        RIGHT_FOUR_POINT_SWD,

        /**
         * TopFourPointSwd
         *
         * @since API 6
         */
        TOP_FOUR_POINT_SWD,

        /**
         * BottomFourPointSwd
         *
         * @since API 6
         */
        BOTTOM_FOUR_POINT_SWD,

        /**
         * FourPointSwd
         *
         * @since API 6
         */
        FOUR_POINT_SWD,

        /**
         * FourPointAny
         *
         * @since API 6
         */
        FOUR_POINT_ANY,

        /**
         * LeftTwoPoint
         *
         * @since API 6
         */
        LEFT_TWO_POINT,

        /**
         * RightTwoPoint
         *
         * @since API 6
         */
        RIGHT_TWO_POINT,

        /**
         * TopTwoPoint
         *
         * @since API 6
         */
        TOP_TWO_POINT,

        /**
         * BottomTwoPoint
         *
         * @since API 6
         */
        BOTTOM_TWO_POINT,

        /**
         * Other
         *
         * @since API 6
         */
        OTHER
    }

    /**
     * <p>Fold mode</p>
     *
     * @since API 6
     */
    @SuppressWarnings("unused")
    public enum FoldMode {
        /**
         * None
         *
         * @since API 6
         */
        NONE,

        /**
         * Default
         *
         * @since API 6
         */
        DEFAULT,

        /**
         * CInwardTop
         *
         * @since API 6
         */
        C_INWARD_TOP,

        /**
         * CInwardBottom
         *
         * @since API 6
         */
        C_INWARD_BOTTOM,

        /**
         * COutwardTop
         *
         * @since API 6
         */
        C_OUTWARD_TOP,

        /**
         * COutwardBottom
         *
         * @since API 6
         */
        C_OUTWARD_BOTTOM,

        /**
         * VInwardTop
         *
         * @since API 6
         */
        V_INWARD_TOP,

        /**
         * VInwardBottom
         *
         * @since API 6
         */
        V_INWARD_BOTTOM,

        /**
         * VOutwardTop
         *
         * @since API 6
         */
        V_OUTWARD_TOP,

        /**
         * VOutwardBottom
         *
         * @since API 6
         */
        V_OUTWARD_BOTTOM,

        /**
         * Other
         *
         * @since API 6
         */
        OTHER
    }

    /**
     * <p>Stamp Position</p>
     *
     * @since API 6
     */
    @SuppressWarnings("unused")
    public enum StampPosition {
        /**
         * Top Left
         *
         * @since API 6
         */
        TOP_LEFT,

        /**
         * Top Center
         *
         * @since API 6
         */
        TOP_CENTER,

        /**
         * Top Right
         *
         * @since API 6
         */
        TOP_RIGHT,

        /**
         * Bottom Left
         *
         * @since API 6
         */
        BOTTOM_LEFT,

        /**
         * Bottom Center
         *
         * @since API 6
         */
        BOTTOM_CENTER,

        /**
         * Bottom Right
         *
         * @since API 6
         */
        BOTTOM_RIGHT


    }

    /**
     * <p>Indicates if watermark should be OnlyFirstPage</p>
     *
     * @since API 6
     */
    public enum WatermarkType {
        /**
         * Default value
         *
         * @since API 6
         */
        DEFAULT,
        /**
         * Default value
         *
         * @since API 6
         */
        NONE,

        /**
         * Enable Watermark Text,
         *
         * @since API 6
         */

        TEXT,
        /**
         * Enable Watermark Text Secure
         *
         * @since API 6
         */
        SECURE,
    }

    /**
     * <p>Indicates if watermark should be OnlyFirstPage</p>
     *
     * @since API 6
     */
    public enum WatermarkOnlyFirstPage {
        /**
         * Default value
         *
         * @since API 6
         */
        DEFAULT,

        /**
         * Enable Watermark OnlyFirstPage,
         *
         * @since API 6
         */

        TRUE,
        /**
         * Disable Watermark OnlyFirstPage
         *
         * @since API 6
         */
        FALSE,
    }

    /**
     * <p>Indicates if watermark should be rotated 45 degrees.</p>
     *
     * @since API 6
     */
    public enum WatermarkRotate45 {
        /**
         * Default value
         *
         * @since API 6
         */
        DEFAULT,

        /**
         * Enable Watermark Rotation,
         *
         * @since API 6
         */

        TRUE,
        /**
         * Disable Watermark Rotation
         *
         * @since API 6
         */
        FALSE,
    }

    public enum WatermarkMessageType {
        /**
         * Default value
         *
         * @since API 6
         */
        NONE,

        CUSTOM,

        CONFIDENTIAL,

        CUSTOMSTRING1,

        CUSTOMSTRING2,

        CUSTOMSTRING3,

        CUSTOMSTRING4,

        CUSTOMSTRING5,

        CUSTOMSTRING6,

        CUSTOMSTRING7,

        CUSTOMSTRING8,

        CUSTOMSTRING9,

        CUSTOMSTRING10,

        CUSTOMSTRING11,

        CUSTOMSTRING12,

        CUSTOMSTRING13,

        CUSTOMSTRING14,

        CUSTOMSTRING15,

        CUSTOMSTRING16,

        DATEANDTIME,

        DRAFT,

        IPADDRESS,

        PAGENUMBER,

        PRODUCTINFORMATION,

        SECRET,

        TOPSECRET,

        URGENT,

        USERNAME,

        OTHER,
    }

    public enum WatermarkBackgroundPattern {
        /**
         * Default value
         *
         * @since API 6
         */
        DEFAULT,

        SCROLL,

        LEAF,

        FLAT,

        OTHER,
    }

    final int mVersion;

    final ColorMode mColorMode;

    final Orientation mOrientation;

    final Duplex mScanDuplex;

    final ScanSize mScanSize;

    final Float mScanCustomLength;

    final Float mScanCustomWidth;

    final ScanSource mScanSource;

    final CopyPreview mCopyPreview;

    final BackgroundCleanup mBackgroundCleanup;

    final ContrastAdjustment mContrastAdjustment;

    final DarknessAdjustment mDarknessAdjustment;

    final SharpnessAdjustment mSharpnessAdjustment;

    final Duplex mPrintDuplex;

    final PaperSize mPrintSize;

    final Float mPrintCustomLength;

    final Float mPrintCustomWidth;

    final int mCopies;

    final CollateMode mCollateMode;

    final PaperSource mPaperSource;

    final PaperType mPaperType;

    final ScaleMode mScaleMode;

    final int mScalePercent;

    final TextGraphicsOptimization mTextGraphicsOptimization;

    final JobAssemblyMode mJobAssemblyMode;

    final JobExecutionMode mJobExecutionMode;

    final NumberUpMode mNumberUpMode;

    final NumberUpDirection mNumberUpDirection;

    final String mStoreJobFolderName;

    final String mStoreJobName;

    final RetentionMode mStoredJobRetentionModeOnPowerCycle;

    final RetentionMode mStoredJobRetentionModeOnRelease;

    final JobCredentialsAttributes mStoredJobCredentialsAttributes;

    final OutputBin mOutputBin;

    final ProgressDialogMode mProgressDialogMode;

    final EraseMarginUnit mEraseMarginUnit;

    final Float mEraseBackBottom;
    final Float mEraseBackLeft;
    final Float mEraseBackRight;
    final Float mEraseBackTop;
    final Float mEraseFrontBottom;
    final Float mEraseFrontLeft;
    final Float mEraseFrontRight;
    final Float mEraseFrontTop;

    final CaptureMode mCaptureMode;

    final ImageShiftReduceToFit mImageShiftReduceToFit;

    final ImageShiftUnits mImageShiftUnits;

    final Float mImageShiftXFront;
    final Float mImageShiftYFront;
    final Float mImageShiftXBack;
    final Float mImageShiftYBack;

    final BookletBordersEachPage mBookletBordersEachPage;

    final BookletFinishingOption mBookletFinishingOption;

    final BookletFormat mBookletFormat;

    final StapleOption mStapleOption;

    final PunchMode mPunchMode;

    final FoldMode mFoldMode;

    final Map<String, StampOption> mStampOptionMap;

    final int mWatermarkTextSize;

    final int mWatermarkTransparency;

    final String mWatermarkBackgroundColor;

    final String mWatermarkFont;

    final String mWatermarkTextColor;

    final WatermarkOnlyFirstPage mWatermarkOnlyFirstPage;

    final int mWatermarkDarkness;

    final String mWatermarkText;

    final WatermarkRotate45 mWatermarkRotate45;

    final WatermarkType mWatermarkType;

    final WatermarkMessageType mWatermarkMessageType;

    final WatermarkBackgroundPattern mWatermarkBackgroundPattern;

    private CopyAttributes(final CopyAttributesBuilder builder) {
        mVersion = Sdk.VERSION.LEVEL;
        mColorMode = builder.mColorMode;
        mOrientation = builder.mOrientation;
        mScanDuplex = builder.mScanDuplex;
        mScanSize = builder.mScanSize;
        mScanCustomLength = builder.mScanCustomLength;
        mScanCustomWidth = builder.mScanCustomWidth;
        mScanSource = builder.mScanSource;
        mCopyPreview = builder.mCopyPreview;
        mBackgroundCleanup = builder.mBackgroundCleanup;
        mContrastAdjustment = builder.mContrastAdjustment;
        mDarknessAdjustment = builder.mDarknessAdjustment;
        mSharpnessAdjustment = builder.mSharpnessAdjustment;
        mPrintDuplex = builder.mPrintDuplex;
        mPrintSize = builder.mPrintSize;
        mPrintCustomLength = builder.mPrintCustomLength;
        mPrintCustomWidth = builder.mPrintCustomWidth;
        mCopies = builder.mCopies;
        mCollateMode = builder.mCollateMode;
        mPaperSource = builder.mPaperSource;
        mPaperType = builder.mPaperType;
        mScaleMode = builder.mScaleMode;
        mScalePercent = builder.mScalePercent;
        mTextGraphicsOptimization = builder.mTextGraphicsOptimization;
        mJobAssemblyMode = builder.mJobAssemblyMode;
        mJobExecutionMode = builder.mJobExecutionMode;
        mNumberUpMode = builder.mNumberUpMode;
        mNumberUpDirection = builder.mNumberUpDirection;

        if (builder instanceof StoreCopyBuilder) {
            StoreCopyBuilder storeCopyBuilder = (StoreCopyBuilder) builder;
            mStoreJobFolderName = storeCopyBuilder.mStoreJobFolderName;
            mStoreJobName = storeCopyBuilder.mStoreJobName;
            mStoredJobRetentionModeOnPowerCycle = storeCopyBuilder.mStoredJobRetentionModeOnPowerCycle;
            mStoredJobRetentionModeOnRelease = storeCopyBuilder.mStoredJobRetentionModeOnRelease;
            mStoredJobCredentialsAttributes = storeCopyBuilder.mJobCredentialsAttributes;
        } else {
            mStoreJobFolderName = null;
            mStoreJobName = null;
            mStoredJobRetentionModeOnPowerCycle = null;
            mStoredJobRetentionModeOnRelease = null;
            mStoredJobCredentialsAttributes = null;
        }

        //From SDK 1.5
        mOutputBin = builder.mOutputBin;
        mProgressDialogMode = builder.mProgressDialogMode;

        //From SDK 1.6
        mEraseMarginUnit = builder.mEraseMarginUnit;

        mEraseBackBottom = builder.mEraseBackBottom;
        mEraseBackLeft = builder.mEraseBackLeft;
        mEraseBackRight = builder.mEraseBackRight;
        mEraseBackTop = builder.mEraseBackTop;
        mEraseFrontBottom = builder.mEraseFrontBottom;
        mEraseFrontLeft = builder.mEraseFrontLeft;
        mEraseFrontRight = builder.mEraseFrontRight;
        mEraseFrontTop = builder.mEraseFrontTop;
        mCaptureMode = builder.mCaptureMode;
        mImageShiftReduceToFit = builder.mImageShiftReduceToFit;
        mImageShiftUnits = builder.mImageShiftUnits;
        mImageShiftXFront = builder.mImageShiftXFront;
        mImageShiftYFront = builder.mImageShiftYFront;
        mImageShiftXBack = builder.mImageShiftXBack;
        mImageShiftYBack = builder.mImageShiftYBack;
        mBookletBordersEachPage = builder.mBookletBordersEachPage;
        mBookletFinishingOption = builder.mBookletFinishingOption;
        mBookletFormat = builder.mBookletFormat;

        //From SDK 1.6
        mStapleOption = builder.mStapleOption;
        mPunchMode = builder.mPunchMode;
        mFoldMode = builder.mFoldMode;

        //From SDK 1.6
        mStampOptionMap = builder.mStampOptionMap;


        mWatermarkDarkness = builder.mWatermarkDarkness;
        mWatermarkText = builder.mWatermarkText;
        mWatermarkRotate45 = builder.mWatermarkRotate45;
        mWatermarkType = builder.mWatermarkType;
        mWatermarkTextSize = builder.mWatermarkTextSize;
        mWatermarkTransparency = builder.mWatermarkTransparency;
        mWatermarkBackgroundColor = builder.mWatermarkBackgroundColor;
        mWatermarkFont = builder.mWatermarkFont;
        mWatermarkTextColor = builder.mWatermarkTextColor;
        mWatermarkOnlyFirstPage = builder.mWatermarkOnlyFirstPage;
        mWatermarkMessageType = builder.mWatermarkMessageType;
        mWatermarkBackgroundPattern = builder.mWatermarkBackgroundPattern;
    }

    @SuppressLint("RestrictedApi")
    private CopyAttributes(Parcel in) {
        // The version is used to support compatibility. It must be the first in the parcel. If a new attribute is added, then logic needs to be added
        // to the end of this constructor. The constructor will compare the version passed with the version supported and handle the compatibility. This
        // means that if the version passed is less than the version of the reader, then the reader will read all values up to the version passed.

        // Note: The order of these values is CRITICAL. When new values are needed, please add to the end.
        mVersion = in.readInt();
        Preconditions.checkArgument(mVersion >= Sdk.VERSION_LEVEL.ONE);

        mColorMode = (ColorMode) in.readSerializable();
        mOrientation = (Orientation) in.readSerializable();
        mScanDuplex = (Duplex) in.readSerializable();
        mScanSize = (ScanSize) in.readSerializable();
        mScanCustomLength = (Float) in.readSerializable();
        mScanCustomWidth = (Float) in.readSerializable();
        mScanSource = (ScanSource) in.readSerializable();
        mCopyPreview = (CopyPreview) in.readSerializable();
        mBackgroundCleanup = (BackgroundCleanup) in.readSerializable();
        mContrastAdjustment = (ContrastAdjustment) in.readSerializable();
        mDarknessAdjustment = (DarknessAdjustment) in.readSerializable();
        mSharpnessAdjustment = (SharpnessAdjustment) in.readSerializable();
        mPrintDuplex = (Duplex) in.readSerializable();
        mPrintSize = (PaperSize) in.readSerializable();
        mPrintCustomLength = (Float) in.readSerializable();
        mPrintCustomWidth = (Float) in.readSerializable();
        mCopies = in.readInt();
        mCollateMode = (CollateMode) in.readSerializable();
        mPaperSource = (PaperSource) in.readSerializable();
        mPaperType = (PaperType) in.readSerializable();
        mScaleMode = (ScaleMode) in.readSerializable();
        mScalePercent = in.readInt();
        mTextGraphicsOptimization = (TextGraphicsOptimization) in.readSerializable();
        mJobAssemblyMode = (JobAssemblyMode) in.readSerializable();
        mJobExecutionMode = (JobExecutionMode) in.readSerializable();
        mNumberUpMode = (NumberUpMode) in.readSerializable();
        mNumberUpDirection = (NumberUpDirection) in.readSerializable();
        mStoreJobFolderName = in.readString();
        mStoreJobName = in.readString();
        mStoredJobRetentionModeOnPowerCycle = (RetentionMode) in.readSerializable();
        mStoredJobRetentionModeOnRelease = (RetentionMode) in.readSerializable();
        mStoredJobCredentialsAttributes = in.readParcelable(JobCredentialsAttributes.class.getClassLoader());

        if (mVersion >= Sdk.VERSION_LEVEL.SIX) {
            mOutputBin = (OutputBin) in.readSerializable();
            mProgressDialogMode = (ProgressDialogMode) in.readSerializable();

        } else {
            mOutputBin = null;
            mProgressDialogMode = ProgressDialogMode.OFF;
        }

        if (mVersion >= Sdk.VERSION_LEVEL.SEVEN) {
            mEraseMarginUnit = (EraseMarginUnit) in.readSerializable();

            mEraseBackBottom = in.readFloat();
            mEraseBackLeft = in.readFloat();
            mEraseBackRight = in.readFloat();
            mEraseBackTop = in.readFloat();
            mEraseFrontBottom = in.readFloat();
            mEraseFrontLeft = in.readFloat();
            mEraseFrontRight = in.readFloat();
            mEraseFrontTop = in.readFloat();
            mCaptureMode = (CaptureMode) in.readSerializable();
            mImageShiftReduceToFit = (ImageShiftReduceToFit) in.readSerializable();
            mImageShiftUnits = (ImageShiftUnits) in.readSerializable();
            mImageShiftXFront = in.readFloat();
            mImageShiftYFront = in.readFloat();
            mImageShiftXBack = in.readFloat();
            mImageShiftYBack = in.readFloat();
            mBookletBordersEachPage = (BookletBordersEachPage) in.readSerializable();
            mBookletFinishingOption = (BookletFinishingOption) in.readSerializable();
            mBookletFormat = (BookletFormat) in.readSerializable();

            mStapleOption = (StapleOption) in.readSerializable();
            mPunchMode = (PunchMode) in.readSerializable();
            mFoldMode = (FoldMode) in.readSerializable();
            mStampOptionMap = in.readHashMap(StampOption.class.getClassLoader());
            mWatermarkDarkness = in.readInt();
            mWatermarkText = in.readString();
            mWatermarkRotate45 = (WatermarkRotate45) in.readSerializable();
            mWatermarkType = (WatermarkType) in.readSerializable();
            mWatermarkTextSize = in.readInt();
            mWatermarkTransparency = in.readInt();
            mWatermarkBackgroundColor = in.readString();
            mWatermarkFont = in.readString();
            mWatermarkTextColor = in.readString();
            mWatermarkOnlyFirstPage = (WatermarkOnlyFirstPage) in.readSerializable();
            mWatermarkMessageType = (WatermarkMessageType) in.readSerializable();
            mWatermarkBackgroundPattern = (WatermarkBackgroundPattern) in.readSerializable();

        } else {
            mEraseMarginUnit = EraseMarginUnit.DEFAULT;

            mEraseBackBottom = 0.0f;
            mEraseBackLeft = 0.0f;
            mEraseBackRight = 0.0f;
            mEraseBackTop = 0.0f;
            mEraseFrontBottom = 0.0f;
            mEraseFrontLeft = 0.0f;
            mEraseFrontRight = 0.0f;
            mEraseFrontTop = 0.0f;

            mCaptureMode = CaptureMode.DEFAULT;

            mImageShiftReduceToFit = ImageShiftReduceToFit.DEFAULT;
            mImageShiftUnits = ImageShiftUnits.DEFAULT;

            mImageShiftXFront = 0.0f;
            mImageShiftYFront = 0.0f;
            mImageShiftXBack = 0.0f;
            mImageShiftYBack = 0.0f;

            mBookletBordersEachPage = BookletBordersEachPage.DEFAULT;
            mBookletFinishingOption = BookletFinishingOption.DEFAULT;
            mBookletFormat = BookletFormat.DEFAULT;

            mStapleOption = null;
            mPunchMode = null;
            mFoldMode = null;
            mStampOptionMap = new HashMap<>();
            for (StampPosition stampPosition : StampPosition.values()) {
                mStampOptionMap.put(stampPosition.name(), new StampOption());
            }
            mWatermarkDarkness = 0;
            mWatermarkText = null;
            mWatermarkRotate45 = null;
            mWatermarkType = null;
            mWatermarkTextSize = 0;
            mWatermarkTransparency = 0;
            mWatermarkBackgroundColor = null;
            mWatermarkFont = null;
            mWatermarkTextColor = null;
            mWatermarkOnlyFirstPage = null;
            mWatermarkMessageType = null;
            mWatermarkBackgroundPattern = null;
        }

    }

    /**
     * @hide parcelable implementation
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        // The Sdk version level is used to because changes to this would constitute API level changes. Additionally, this reduces management of
        // <xyz>Attributes versions.

        // Note: The order of these values is CRITICAL. When new values are needed, please add to the end.
        out.writeInt(mVersion);
        out.writeSerializable(mColorMode);
        out.writeSerializable(mOrientation);
        out.writeSerializable(mScanDuplex);
        out.writeSerializable(mScanSize);
        out.writeSerializable(mScanCustomLength);
        out.writeSerializable(mScanCustomWidth);
        out.writeSerializable(mScanSource);
        out.writeSerializable(mCopyPreview);
        out.writeSerializable(mBackgroundCleanup);
        out.writeSerializable(mContrastAdjustment);
        out.writeSerializable(mDarknessAdjustment);
        out.writeSerializable(mSharpnessAdjustment);
        out.writeSerializable(mPrintDuplex);
        out.writeSerializable(mPrintSize);
        out.writeSerializable(mPrintCustomLength);
        out.writeSerializable(mPrintCustomWidth);
        out.writeInt(mCopies);
        out.writeSerializable(mCollateMode);
        out.writeSerializable(mPaperSource);
        out.writeSerializable(mPaperType);
        out.writeSerializable(mScaleMode);
        out.writeInt(mScalePercent);
        out.writeSerializable(mTextGraphicsOptimization);
        out.writeSerializable(mJobAssemblyMode);
        out.writeSerializable(mJobExecutionMode);
        out.writeSerializable(mNumberUpMode);
        out.writeSerializable(mNumberUpDirection);
        out.writeString(mStoreJobFolderName);
        out.writeString(mStoreJobName);
        out.writeSerializable(mStoredJobRetentionModeOnPowerCycle);
        out.writeSerializable(mStoredJobRetentionModeOnRelease);
        out.writeParcelable(mStoredJobCredentialsAttributes, 0);
        out.writeSerializable(mOutputBin);
        out.writeSerializable(mProgressDialogMode);
        out.writeSerializable(mEraseMarginUnit);
        out.writeFloat(mEraseBackBottom);
        out.writeFloat(mEraseBackLeft);
        out.writeFloat(mEraseBackRight);
        out.writeFloat(mEraseBackTop);
        out.writeFloat(mEraseFrontBottom);
        out.writeFloat(mEraseFrontLeft);
        out.writeFloat(mEraseFrontRight);
        out.writeFloat(mEraseFrontTop);
        out.writeSerializable(mCaptureMode);
        out.writeSerializable(mImageShiftReduceToFit);
        out.writeSerializable(mImageShiftUnits);
        out.writeFloat(mImageShiftXFront);
        out.writeFloat(mImageShiftYFront);
        out.writeFloat(mImageShiftXBack);
        out.writeFloat(mImageShiftYBack);
        out.writeSerializable(mBookletBordersEachPage);
        out.writeSerializable(mBookletFinishingOption);
        out.writeSerializable(mBookletFormat);
        out.writeSerializable(mStapleOption);
        out.writeSerializable(mPunchMode);
        out.writeSerializable(mFoldMode);
        out.writeMap(mStampOptionMap);

        out.writeInt(mWatermarkDarkness);
        out.writeString(mWatermarkText);
        out.writeSerializable(mWatermarkRotate45);
        out.writeSerializable(mWatermarkType);
        out.writeInt(mWatermarkTextSize);
        out.writeInt(mWatermarkTransparency);
        out.writeString(mWatermarkBackgroundColor);
        out.writeString(mWatermarkFont);
        out.writeString(mWatermarkTextColor);
        out.writeSerializable(mWatermarkOnlyFirstPage);
        out.writeSerializable(mWatermarkMessageType);
        out.writeSerializable(mWatermarkBackgroundPattern);
    }

    /**
     * @hide parcelable implementation
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * @hide parcelable implementation
     */
    public static final Creator<CopyAttributes> CREATOR = new Creator<CopyAttributes>() {
        @Override
        public CopyAttributes createFromParcel(Parcel in) {
            return new CopyAttributes(in);
        }

        @Override
        public CopyAttributes[] newArray(int size) {
            return new CopyAttributes[size];
        }
    };

    /**
     * Builder for creating {@link CopyAttributes} configured to copying.
     *
     * @since API 3
     */
    public static abstract class CopyAttributesBuilder<T extends CopyAttributesBuilder<T>> {
        ColorMode mColorMode = ColorMode.DEFAULT;

        Orientation mOrientation = Orientation.DEFAULT;

        Duplex mScanDuplex = Duplex.DEFAULT;

        ScanSize mScanSize = ScanSize.DEFAULT;

        float mScanCustomLength = 0f;

        float mScanCustomWidth = 0f;

        ScanSource mScanSource = ScanSource.DEFAULT;

        CopyPreview mCopyPreview = CopyPreview.DEFAULT;

        BackgroundCleanup mBackgroundCleanup = BackgroundCleanup.DEFAULT;

        ContrastAdjustment mContrastAdjustment = ContrastAdjustment.DEFAULT;

        DarknessAdjustment mDarknessAdjustment = DarknessAdjustment.DEFAULT;

        SharpnessAdjustment mSharpnessAdjustment = SharpnessAdjustment.DEFAULT;

        Duplex mPrintDuplex = Duplex.DEFAULT;

        PaperSize mPrintSize = PaperSize.DEFAULT;

        float mPrintCustomLength = 0f;

        float mPrintCustomWidth = 0f;

        int mCopies = 1;

        CollateMode mCollateMode = CollateMode.DEFAULT;

        PaperSource mPaperSource = PaperSource.DEFAULT;

        PaperType mPaperType = PaperType.DEFAULT;

        ScaleMode mScaleMode = ScaleMode.DEFAULT;

        int mScalePercent = 100;

        TextGraphicsOptimization mTextGraphicsOptimization = TextGraphicsOptimization.DEFAULT;

        JobAssemblyMode mJobAssemblyMode = JobAssemblyMode.DEFAULT;

        JobExecutionMode mJobExecutionMode = JobExecutionMode.NORMAL;

        NumberUpMode mNumberUpMode = NumberUpMode.DEFAULT;

        NumberUpDirection mNumberUpDirection = NumberUpDirection.DEFAULT;

        OutputBin mOutputBin = OutputBin.DEFAULT;

        ProgressDialogMode mProgressDialogMode = ProgressDialogMode.DEFAULT;

        EraseMarginUnit mEraseMarginUnit = EraseMarginUnit.DEFAULT;

        float mEraseBackBottom = 0.0f;
        float mEraseBackLeft = 0.0f;
        float mEraseBackRight = 0.0f;
        float mEraseBackTop = 0.0f;
        float mEraseFrontBottom = 0.0f;
        float mEraseFrontLeft = 0.0f;
        float mEraseFrontRight = 0.0f;
        float mEraseFrontTop = 0.0f;
        CaptureMode mCaptureMode = CaptureMode.DEFAULT;

        ImageShiftReduceToFit mImageShiftReduceToFit = ImageShiftReduceToFit.DEFAULT;

        ImageShiftUnits mImageShiftUnits = ImageShiftUnits.DEFAULT;

        float mImageShiftXFront = 0.0f;
        float mImageShiftYFront = 0.0f;
        float mImageShiftXBack = 0.0f;
        float mImageShiftYBack = 0.0f;

        BookletBordersEachPage mBookletBordersEachPage = BookletBordersEachPage.DEFAULT;

        BookletFinishingOption mBookletFinishingOption = BookletFinishingOption.DEFAULT;

        BookletFormat mBookletFormat = BookletFormat.DEFAULT;

        StapleOption mStapleOption = StapleOption.NONE;

        PunchMode mPunchMode = PunchMode.NONE;

        FoldMode mFoldMode = FoldMode.NONE;

        Map<String, StampOption> mStampOptionMap = new HashMap<>();


        int mWatermarkDarkness = 0;
        String mWatermarkText = null;
        WatermarkRotate45 mWatermarkRotate45 = WatermarkRotate45.DEFAULT;
        int mWatermarkTextSize = 0;
        int mWatermarkTransparency = 0;
        String mWatermarkFont = null;
        String mWatermarkBackgroundColor = null;
        String mWatermarkTextColor = null;
        WatermarkOnlyFirstPage mWatermarkOnlyFirstPage = WatermarkOnlyFirstPage.DEFAULT;
        WatermarkType mWatermarkType = WatermarkType.DEFAULT;
        WatermarkMessageType mWatermarkMessageType = WatermarkMessageType.NONE;
        WatermarkBackgroundPattern mWatermarkBackgroundPattern = WatermarkBackgroundPattern.DEFAULT;

        CopyAttributesBuilder() {
        }

        /**
         * Specifies the set of colors which the scanned output will reside
         * within. For example, monospace indicates that the scanned output only
         * includes black, white, and various shades of gray.
         *
         * @param colorMode The set of colors which the scanned output will reside
         *                  within
         * @return this builder for method chaining.
         * @throws NullPointerException if colorMode is null
         * @since API 3
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setColorMode(@NonNull final ColorMode colorMode) {
            mColorMode = Preconditions.checkNotNull(colorMode);
            return (T) this;
        }

        /**
         * Specifies the set of original orientation which the scanned output will reside within.
         *
         * @param orientation The original orientation of document for scanning
         * @return this builder for method chaining.
         * @throws NullPointerException if orientation is null.
         * @since API 3
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setOrientation(@NonNull final Orientation orientation) {
            mOrientation = Preconditions.checkNotNull(orientation);
            return (T) this;
        }

        /**
         * Sets Scan Duplex for scanning
         *
         * @param duplex Book or Flip
         * @return this builder for method chaining.
         * @throws NullPointerException if duplex is null
         * @since API 3
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setScanDuplex(@NonNull final Duplex duplex) {
            mScanDuplex = Preconditions.checkNotNull(duplex);
            return (T) this;
        }

        /**
         * Specifies the size of the scanned image to be copied.
         *
         * @param scanSize The size of the image to be produced from the scanned
         *                 document / image.
         * @return this builder for method chaining.
         * @throws NullPointerException if scanSize is null.
         * @since API 3
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setScanSize(@NonNull final ScanSize scanSize) {
            mScanSize = Preconditions.checkNotNull(scanSize);
            return (T) this;
        }

        /**
         * Sets the scan custom length.
         *
         * @param customLength in inches
         * @return this builder for method chaining.
         * @since API 3
         */
        @SuppressWarnings({"unused", "unchecked"})
        @NonNull
        public T setScanCustomLength(final float customLength) {
            mScanCustomLength = customLength;
            return (T) this;
        }

        /**
         * Sets the scan custom width.
         *
         * @param customWidth in inches
         * @return this builder for method chaining.
         * @since API 3
         */
        @SuppressWarnings({"unused", "unchecked"})
        @NonNull
        public T setScanCustomWidth(final float customWidth) {
            mScanCustomWidth = customWidth;
            return (T) this;
        }

        /**
         * Sets the scan feeding source
         *
         * @param scanSource Scan source value
         * @return this builder for method chaining
         * @throws NullPointerException is scanSource is null;
         * @since API 3
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setScanSource(@NonNull final ScanSource scanSource) {
            mScanSource = Preconditions.checkNotNull(scanSource);
            return (T) this;
        }

        /**
         * Sets the copy preview param. This parameter forces showing a preview of scanned image
         *
         * @param copyPreview Scan preview flag value
         * @return this builder for method chaining
         * @throws NullPointerException is copyPreview is null;
         * @since API 3
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setCopyPreview(@NonNull final CopyPreview copyPreview) {
            mCopyPreview = Preconditions.checkNotNull(copyPreview);
            return (T) this;
        }

        /**
         * Sets the background cleanup level.
         *
         * @param backgroundCleanup level
         * @return this builder for method chaining.
         * @throws NullPointerException if backgroundCleanup is null.
         * @since API 3
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setBackgroundCleanup(@NonNull final BackgroundCleanup backgroundCleanup) {
            mBackgroundCleanup = Preconditions.checkNotNull(backgroundCleanup);
            return (T) this;
        }

        /**
         * Sets the contrast adjustment level.
         *
         * @param contrastAdjustment level
         * @return this builder for method chaining.
         * @throws NullPointerException if contrastAdjustment is null.
         * @since API 3
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setContrastAdjustment(@NonNull final ContrastAdjustment contrastAdjustment) {
            mContrastAdjustment = Preconditions.checkNotNull(contrastAdjustment);
            return (T) this;
        }

        /**
         * Sets the darkness adjustment level.
         *
         * @param darknessAdjustment level
         * @return this builder for method chaining.
         * @throws NullPointerException if darknessAdjustment is null.
         * @since API 3
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setDarknessAdjustment(@NonNull final DarknessAdjustment darknessAdjustment) {
            mDarknessAdjustment = Preconditions.checkNotNull(darknessAdjustment);
            return (T) this;
        }

        /**
         * Sets the sharpness adjustment level.
         *
         * @param sharpnessAdjustment level
         * @return this builder for method chaining.
         * @throws NullPointerException if darknessAdjustment is null.
         * @since API 3
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setSharpnessAdjustment(@NonNull final SharpnessAdjustment sharpnessAdjustment) {
            mSharpnessAdjustment = Preconditions.checkNotNull(sharpnessAdjustment);
            return (T) this;
        }

        /**
         * Set Print Duplex for printing
         *
         * @param duplex Book or Flip
         * @return this builder for method chaining.
         * @throws NullPointerException if duplex is null
         * @since API 3
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setPrintDuplex(@NonNull final Duplex duplex) {
            mPrintDuplex = Preconditions.checkNotNull(duplex);
            return (T) this;
        }

        /**
         * Specifies the size of the printed image.
         *
         * @param printSize The size of the image to be printed
         * @return this builder for method chaining.
         * @throws NullPointerException if printSize is null.
         * @since API 3
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setPrintSize(@NonNull final PaperSize printSize) {
            mPrintSize = Preconditions.checkNotNull(printSize);
            return (T) this;
        }

        /**
         * Sets the print custom length.
         *
         * @param customLength in inches
         * @return this builder for method chaining.
         * @since API 3
         */
        @SuppressWarnings({"unused", "unchecked"})
        @NonNull
        public T setPrintCustomLength(final float customLength) {
            mPrintCustomLength = customLength;
            return (T) this;
        }

        /**
         * Sets the print custom width.
         *
         * @param customWidth in inches
         * @return this builder for method chaining.
         * @since API 3
         */
        @SuppressWarnings({"unused", "unchecked"})
        @NonNull
        public T setPrintCustomWidth(final float customWidth) {
            mPrintCustomWidth = customWidth;
            return (T) this;
        }

        /**
         * Set number of copies.
         *
         * @param copies The number of copies to be produced.
         * @return this builder for method chaining.
         * @throws IllegalArgumentException if number of copies is less than or equal to zero
         * @since API 3
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        public T setCopies(final int copies) {
            Preconditions.checkArgument(copies > 0);
            mCopies = copies;
            return (T) this;
        }

        /**
         * Set collate mode
         *
         * @param collateMode CollateMode for the printout.
         * @return this builder for method chaining.
         * @throws NullPointerException if collateMode is null
         * @since API 3
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        public T setCollateMode(@NonNull final CollateMode collateMode) {
            mCollateMode = Preconditions.checkNotNull(collateMode);
            return (T) this;
        }

        /**
         * Set paper source for printing out
         *
         * @param paperSource PaperSource for the printout.
         * @return this builder for method chaining.
         * @throws NullPointerException if paperSource is null
         * @since API 3
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        public T setPaperSource(@NonNull final PaperSource paperSource) {
            mPaperSource = Preconditions.checkNotNull(paperSource);
            return (T) this;
        }

        /**
         * Set paper type for printing out
         *
         * @param paperType PaperType for the printout.
         * @return this builder for method chaining.
         * @throws NullPointerException if paperType is null
         * @since API 3
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        public T setPaperType(@NonNull final PaperType paperType) {
            mPaperType = Preconditions.checkNotNull(paperType);
            return (T) this;
        }

        /**
         * Set scale mode
         *
         * @param scaleMode scale mode value
         * @return this builder for method chaining.
         * @throws NullPointerException if scaleMode is null
         * @since API 3
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        public T setScaleMode(@NonNull final ScaleMode scaleMode) {
            mScaleMode = Preconditions.checkNotNull(scaleMode);
            return (T) this;
        }

        /**
         * Set scale percentage for {@link ScaleMode#MANUAL} mode
         *
         * @param scalePercent scale percent value
         * @return this builder for method chaining.
         * @throws NullPointerException if scaleMode is null
         * @since API 3
         */
        @SuppressWarnings({"unused", "unchecked"})
        public T setScalePercent(final int scalePercent) {
            mScalePercent = scalePercent;
            return (T) this;
        }

        /**
         * Sets the text graphics optimization.
         *
         * @param textGraphicsOptimization mode
         * @return this builder for method chaining.
         * @throws NullPointerException if textGraphicsOptimization is null.
         * @since API 3
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setTextGraphicsOptimization(@NonNull final TextGraphicsOptimization textGraphicsOptimization) {
            mTextGraphicsOptimization = Preconditions.checkNotNull(textGraphicsOptimization);
            return (T) this;
        }

        /**
         * Sets job assembly mode
         *
         * @param jobAssemblyMode value
         * @return this builder for method chaining.
         * @throws NullPointerException if jobAssemblyMode is null
         * @since API 3
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setJobAssemblyMode(@NonNull final JobAssemblyMode jobAssemblyMode) {
            mJobAssemblyMode = Preconditions.checkNotNull(jobAssemblyMode);
            return (T) this;
        }

        /**
         * Sets number up mode
         *
         * @param numberUpMode value
         * @return this builder for method chaining.
         * @throws NullPointerException if numberUpMode is null
         * @since API 3
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setNumberUpMode(@NonNull final NumberUpMode numberUpMode) {
            mNumberUpMode = Preconditions.checkNotNull(numberUpMode);
            return (T) this;
        }

        /**
         * Sets number up direction
         *
         * @param numberUpDirection value
         * @return this builder for method chaining.
         * @throws NullPointerException if numberUpDirection is null
         * @since API 3
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setNumberUpDirection(@NonNull final NumberUpDirection numberUpDirection) {
            mNumberUpDirection = Preconditions.checkNotNull(numberUpDirection);
            return (T) this;
        }

        /**
         * Sets output bin
         *
         * @param outputBin value
         * @return this builder for method chaining.
         * @throws NullPointerException if OutputBin is null
         * @since API 5
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setOutputBin(@NonNull final OutputBin outputBin) {
            mOutputBin = Preconditions.checkNotNull(outputBin);
            return (T) this;
        }

        /**
         * Sets progress dialog mode
         *
         * @param progressDialogMode value
         * @return this builder for method chaining.
         * @throws NullPointerException if ProgressDialogMode is null
         * @since API 5
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setProgressDialogMode(@NonNull final ProgressDialogMode progressDialogMode) {
            mProgressDialogMode = Preconditions.checkNotNull(progressDialogMode);
            return (T) this;
        }

        /**
         * EraseMarginUnits
         *
         * @param eraseMarginUnit mode
         * @return this builder for method chaining.
         * @since API 6
         */
        @SuppressWarnings({"unused", "unchecked"})
        @NonNull
        public T setEraseMarginUnit(final EraseMarginUnit eraseMarginUnit) {
            mEraseMarginUnit = eraseMarginUnit;
            return (T) this;
        }

        /**
         * EraseBack margin
         *
         * @param margins erase back margin
         * @return this builder for method chaining.
         * @since API 6
         */
        @SuppressWarnings({"unused", "unchecked"})
        @NonNull
        public T setEraseBackMargin(final Margins margins) {
            mEraseBackBottom = margins.getBottomMargin();
            mEraseBackLeft = margins.getLeftMargin();
            mEraseBackRight = margins.getRightMargin();
            mEraseBackTop = margins.getTopMargin();
            return (T) this;
        }

        /**
         * EraseFront margin
         *
         * @param margins erase front margin
         * @return this builder for method chaining.
         * @since API 6
         */
        @SuppressWarnings({"unused", "unchecked"})
        @NonNull
        public T setEraseFrontMargin(final Margins margins) {
            mEraseFrontBottom = margins.getBottomMargin();
            mEraseFrontLeft = margins.getLeftMargin();
            mEraseFrontRight = margins.getRightMargin();
            mEraseFrontTop = margins.getTopMargin();
            return (T) this;
        }

        /* Sets the capture mode option.
         *
         * @param captureMode level
         * @return this builder for method chaining.
         * @throws NullPointerException if captureMode is null.
         * @since API 6
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setCaptureMode(@NonNull final CaptureMode captureMode) {
            mCaptureMode = Preconditions.checkNotNull(captureMode);
            return (T) this;
        }

        /**
         * Sets image Shift Reduce To Fit
         *
         * @param imageShiftReduceToFit value
         * @return this builder for method chaining.
         * @throws NullPointerException if ImageShiftReduceToFit is null
         * @since API 5
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setImageShiftReduceToFit(@NonNull final ImageShiftReduceToFit imageShiftReduceToFit) {
            mImageShiftReduceToFit = Preconditions.checkNotNull(imageShiftReduceToFit);
            return (T) this;
        }

        /**
         * Sets image Shift Units
         *
         * @param imageShiftUnits value
         * @return this builder for method chaining.
         * @throws NullPointerException if ImageShiftUnits is null
         * @since API 5
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setImageShiftUnits(@NonNull final ImageShiftUnits imageShiftUnits) {
            mImageShiftUnits = Preconditions.checkNotNull(imageShiftUnits);
            return (T) this;
        }

        /**
         * Sets Image Shift Front
         *
         * @param shifts front shift
         * @return this builder for method chaining.
         * @since API 5
         */
        @SuppressWarnings({"unused", "unchecked"})
        @NonNull
        public T setImageShiftFront(final Shifts shifts) {
            mImageShiftXFront = shifts.getXShift();
            mImageShiftYFront = shifts.getYShift();
            return (T) this;
        }

        /**
         * Sets Image Shift Back
         *
         * @param shifts back shift
         * @return this builder for method chaining.
         * @since API 5
         */
        @SuppressWarnings({"unused", "unchecked"})
        @NonNull
        public T setImageShiftBack(final Shifts shifts) {
            mImageShiftXBack = shifts.getXShift();
            mImageShiftYBack = shifts.getYShift();
            return (T) this;
        }

        /**
         * Sets Booklet Borders Each Page
         *
         * @param bookletBordersEachPage value
         * @return this builder for method chaining.
         * @throws NullPointerException if BookletBordersEachPage is null
         * @since API 5
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setBookletBordersEachPage(@NonNull final BookletBordersEachPage bookletBordersEachPage) {
            mBookletBordersEachPage = Preconditions.checkNotNull(bookletBordersEachPage);
            return (T) this;
        }

        /**
         * Sets Booklet Finishing Option
         *
         * @param bookletFinishingOption value
         * @return this builder for method chaining.
         * @throws NullPointerException if BookletFinishingOption is null
         * @since API 5
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setBookletFinishingOption(@NonNull final BookletFinishingOption bookletFinishingOption) {
            mBookletFinishingOption = Preconditions.checkNotNull(bookletFinishingOption);
            return (T) this;
        }

        /**
         * Sets Booklet Format
         *
         * @param bookletFormat value
         * @return this builder for method chaining.
         * @throws NullPointerException if BookletFormat is null
         * @since API 5
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setBookletFormat(@NonNull final BookletFormat bookletFormat) {
            mBookletFormat = Preconditions.checkNotNull(bookletFormat);
            return (T) this;
        }

        /**
         * Sets staple mode
         *
         * @param stapleMode value
         * @return this builder for method chaining.
         * @throws NullPointerException if ProgressDialogMode is null
         * @since API 5
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setStapleOption(@NonNull final StapleOption stapleMode) {
            mStapleOption = Preconditions.checkNotNull(stapleMode);
            return (T) this;
        }

        /**
         * Sets punch mode
         *
         * @param punchMode value
         * @return this builder for method chaining.
         * @throws NullPointerException if ProgressDialogMode is null
         * @since API 5
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setPunchMode(@NonNull final PunchMode punchMode) {
            mPunchMode = Preconditions.checkNotNull(punchMode);
            return (T) this;
        }

        /**
         * Sets fold mode
         *
         * @param foldMode value
         * @return this builder for method chaining.
         * @throws NullPointerException if foldmode is null
         * @since API 6
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setFoldMode(@NonNull final FoldMode foldMode) {
            mFoldMode = Preconditions.checkNotNull(foldMode);
            return (T) this;
        }


        /**
         * Sets Stamp Option
         *
         * @param stampOption value
         * @return this builder for method chaining.
         * @throws NullPointerException if stampOption is null
         * @since API 6
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setStampOption(StampPosition stampPosition, @NonNull final StampOption stampOption) {
            mStampOptionMap.put(stampPosition.name(), stampOption);
            return (T) this;
        }


        /**
         * Set transparency for watermark.
         *
         * @param transparency The amount of Transparency to be applied.
         * @return this builder for method chaining.
         * @throws IllegalArgumentException if amount of transparency is less than or equal to zero
         * @since API 6
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        public T setWatermarkTransparency(final int transparency) {
//            Preconditions.checkArgument(transparency > 0);
            mWatermarkTransparency = transparency;
            return (T) this;
        }

        /**
         * Set textsize for watermark.
         *
         * @param textSize The textSize for watermark text.
         * @return this builder for method chaining.
         * @throws IllegalArgumentException if amount of textSize is less than or equal to zero
         * @since API 6
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        public T setWatermarktextSize(final int textSize) {
            Preconditions.checkArgument(textSize > 0);
            mWatermarkTextSize = textSize;
            return (T) this;
        }

        /**
         * Sets watermark  BackgroundColor
         *
         * @param backgroundColor for watermark
         * @return this builder for method chaining.
         * @since API 6
         */
        public T setWatermarkBackgroundColor(final String backgroundColor) {
            if (backgroundColor != null && backgroundColor.trim().length() < 1) {
                mWatermarkBackgroundColor = null;
            } else {
                mWatermarkBackgroundColor = backgroundColor;
            }
            return (T) this;
        }

        /**
         * Sets watermarkFont
         *
         * @param watermarkFont watermark text
         * @return this builder for method chaining.
         * @since API 6
         */

        public T setWatermarkFont(final String watermarkFont) {
            if (watermarkFont != null && watermarkFont.trim().length() < 1) {
                mWatermarkFont = null;
            } else {
                mWatermarkFont = watermarkFont;
            }
            return (T) this;
        }

        /**
         * Sets WatermarkTextColor
         *
         * @param watermarkTextColor for watermark text
         * @return this builder for method chaining.
         * @since API 6
         */
        public T setWatermarkTextColor(final String watermarkTextColor) {
            if (watermarkTextColor != null && watermarkTextColor.trim().length() < 1) {
                mWatermarkTextColor = null;
            } else {
                mWatermarkTextColor = watermarkTextColor;
            }
            return (T) this;
        }

        /**
         * Sets the watermark Only FirstPage param.
         *
         * @param watermarkOnlyFirstPage Scan preview flag value
         * @return this builder for method chaining
         * @throws NullPointerException is watermarkOnlyFirstPage is null;
         * @since API 3
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setWatermarkOnlyFirstPage(@NonNull final WatermarkOnlyFirstPage watermarkOnlyFirstPage) {
            mWatermarkOnlyFirstPage = Preconditions.checkNotNull(watermarkOnlyFirstPage);
            return (T) this;
        }

        /**
         * Set Darkness for watermark.
         *
         * @param darkness The amount of darkness to be produced.
         * @return this builder for method chaining.
         * @throws IllegalArgumentException if amount of darkness is less than or equal to zero
         * @since API 6
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        public T setWatermarkDarkness(final int darkness) {
            Preconditions.checkArgument(darkness > 0);
            mWatermarkDarkness = darkness;
            return (T) this;
        }

        /**
         * Sets watermark text
         *
         * @param watermarktext watermark text
         * @return this builder for method chaining.
         * @since API 6
         */
        public T setWatermarkText(final String watermarktext) {
            if (watermarktext != null && watermarktext.trim().length() < 1) {
                mWatermarkText = null;
            } else {
                mWatermarkText = watermarktext;
            }
            return (T) this;
        }

        /**
         * Sets the watermark rotation param.
         *
         * @param watermarkType Scan preview flag value
         * @return this builder for method chaining
         * @throws NullPointerException is watermarkRotate45 is null;
         * @since API 3
         */

        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setWatermarkType(@NonNull final WatermarkType watermarkType) {
            mWatermarkType = Preconditions.checkNotNull(watermarkType);
            return (T) this;
        }

        /**
         * Sets the watermark message param.
         *
         * @param watermarkMessageType
         * @return this builder for method chaining
         * @throws NullPointerException is watermarkRotate45 is null;
         * @since API 3
         */

        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setWatermarkMessageType(@NonNull final WatermarkMessageType watermarkMessageType) {
            mWatermarkMessageType = Preconditions.checkNotNull(watermarkMessageType);
            return (T) this;
        }

        /**
         * Sets the watermark background pattern param.
         *
         * @param watermarkBackgroundPattern
         * @return this builder for method chaining
         * @throws NullPointerException is watermarkRotate45 is null;
         * @since API 3
         */

        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setWatermarkBackgroundPattern(@NonNull final WatermarkBackgroundPattern watermarkBackgroundPattern) {
            mWatermarkBackgroundPattern = Preconditions.checkNotNull(watermarkBackgroundPattern);
            return (T) this;
        }

        /**
         * Sets the watermark rotation param.
         *
         * @param watermarkRotate45 Scan preview flag value
         * @return this builder for method chaining
         * @throws NullPointerException is watermarkRotate45 is null;
         * @since API 3
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setWatermarkRotate45(@NonNull final WatermarkRotate45 watermarkRotate45) {
            mWatermarkRotate45 = Preconditions.checkNotNull(watermarkRotate45);
            return (T) this;
        }

        public abstract CopyAttributes build(final CopyAttributesCaps caps) throws CapabilitiesExceededException;

        @SuppressWarnings({"ResultOfMethodCallIgnored"})
        @SuppressLint("RestrictedApi")
        void validate(final CopyAttributesCaps caps) throws CapabilitiesExceededException {
            if (caps == null) {
                throw new CapabilitiesExceededException("CopyAttributesCapabilities is required");
            }

            Preconditions.checkNotNull(caps.mCapsCreator);

            if (!caps.getColorModeList().contains(mColorMode)) {
                throw new CapabilitiesExceededException("Supplied color mode not supported : List("
                        + caps.getColorModeList().toString() + ") Target Value(" + mColorMode + ")");
            }

            if (!caps.getOrientationList().contains(mOrientation)) {
                throw new CapabilitiesExceededException("Supplied original orientation not supported : List("
                        + caps.getOrientationList().toString() + ") Target Value(" + mOrientation + ")");
            }

            if (!caps.getScanDuplexList().contains(mScanDuplex)) {
                throw new CapabilitiesExceededException("Supplied scan duplex not supported : List("
                        + caps.getScanDuplexList().toString() + ") Target Value(" + mScanDuplex + ")");
            }

            if (!caps.getScanSizeList().contains(mScanSize)) {
                throw new CapabilitiesExceededException("Supplied scan size not supported : List("
                        + caps.getScanSizeList().toString() + ") Target Value(" + mScanSize + ")");
            }

            if (mScanSize == ScanSize.CUSTOM) {
                if (!caps.getScanCustomLengthRange().validate(mScanCustomLength)) {
                    throw new CapabilitiesExceededException("Supplied scan custom length value is not in range " + caps.getScanCustomLengthRange());
                }

                if (!caps.getScanCustomWidthRange().validate(mScanCustomWidth)) {
                    throw new CapabilitiesExceededException("Supplied scan custom width value is not in range " + caps.getScanCustomWidthRange());
                }
            }

            if (!caps.getScanSourceList().contains(mScanSource)) {
                throw new CapabilitiesExceededException("Supplied scan source not supported : List("
                        + caps.getScanSourceList().toString() + ") Target Value(" + mScanSource + ")");
            }

            if (!caps.getCopyPreviewList().contains(mCopyPreview)) {
                throw new CapabilitiesExceededException("Supplied copy preview not supported : List("
                        + caps.getCopyPreviewList().toString() + ") Value(" + mCopyPreview + ")");
            }

            if (!caps.getBackgroundCleanupList().contains(mBackgroundCleanup)) {
                throw new CapabilitiesExceededException("Supplied background сleanup not supported : List("
                        + caps.getBackgroundCleanupList().toString() + ") Target Value(" + mBackgroundCleanup + ")");
            }

            if (!caps.getContrastAdjustmentList().contains(mContrastAdjustment)) {
                throw new CapabilitiesExceededException("Supplied contrast adjustment not supported : List("
                        + caps.getContrastAdjustmentList().toString() + ") Target Value(" + mContrastAdjustment + ")");
            }

            if (!caps.getDarknessAdjustmentList().contains(mDarknessAdjustment)) {
                throw new CapabilitiesExceededException("Supplied darkness adjustment not supported : List("
                        + caps.getDarknessAdjustmentList().toString() + ") Target Value(" + mDarknessAdjustment + ")");
            }

            if (!caps.getSharpnessAdjustmentList().contains(mSharpnessAdjustment)) {
                throw new CapabilitiesExceededException("Supplied sharpness adjustment not supported : List("
                        + caps.getSharpnessAdjustmentList().toString() + ") Target Value(" + mSharpnessAdjustment + ")");
            }

            if (!caps.getPrintDuplexList().contains(mPrintDuplex)) {
                throw new CapabilitiesExceededException("Supplied print duplex not supported : List("
                        + caps.getPrintDuplexList().toString() + ") Target Value(" + mPrintDuplex + ")");
            }

            if (!caps.getPrintSizeList().contains(mPrintSize)) {
                throw new CapabilitiesExceededException("Supplied print size not supported : List("
                        + caps.getPrintSizeList().toString() + ") Target Value(" + mPrintSize + ")");
            }

            if (mPrintSize == PaperSize.CUSTOM) {
                if (!caps.getPrintCustomLengthRange().validate(mPrintCustomLength)) {
                    throw new CapabilitiesExceededException("Supplied print custom length value is not in range " + caps.getPrintCustomLengthRange());
                }

                if (!caps.getPrintCustomWidthRange().validate(mPrintCustomWidth)) {
                    throw new CapabilitiesExceededException("Supplied print custom width value is not in range " + caps.getPrintCustomWidthRange());
                }
            }

            if (!caps.getCopiesRange().validate(mCopies)) {
                throw new CapabilitiesExceededException("Supplied copies value is not in range " + caps.getCopiesRange());
            }

            if (!caps.getCollateModeList().contains(mCollateMode)) {
                throw new CapabilitiesExceededException("Supplied collate mode is not supported : List("
                        + caps.getCollateModeList().toString() + ") Target Value(" + mCollateMode + ")");
            }

            if (!caps.getPaperSourceList().contains(mPaperSource)) {
                throw new CapabilitiesExceededException("Supplied paper source is not supported : List("
                        + caps.getPaperSourceList().toString() + ") Target Value(" + mPaperSource + ")");
            }

            if (!caps.getPaperTypeList().contains(mPaperType)) {
                throw new CapabilitiesExceededException("Supplied paper type is not supported : List("
                        + caps.getPaperTypeList().toString() + ") Target Value(" + mPaperType + ")");
            }

            if (!caps.getScaleModeList().contains(mScaleMode)) {
                throw new CapabilitiesExceededException("Supplied scale mode is not supported : List("
                        + caps.getScaleModeList().toString() + ") Target Value(" + mScaleMode + ")");
            }

            if (mScaleMode == ScaleMode.MANUAL) {
                if (!caps.getScalePercentRangeByScanSource().get(mScanSource).validate(mScalePercent)) {
                    throw new CapabilitiesExceededException(
                            "Supplied scale percent value is not in range " + caps.getScalePercentRangeByScanSource().get(
                                    mScanSource));
                }
            }

            if (!caps.getTextGraphicsOptimizationList().contains(mTextGraphicsOptimization)) {
                throw new CapabilitiesExceededException("Supplied text/graphics optimization not supported : List("
                        + caps.getTextGraphicsOptimizationList().toString() + ") Target Value(" + mTextGraphicsOptimization + ")");
            }

            if (!caps.getJobAssemblyModeList().contains(mJobAssemblyMode)) {
                throw new CapabilitiesExceededException("Supplied job assembly mode not supported : List("
                        + caps.getJobAssemblyModeList().toString() + ") Target Value(" + mJobAssemblyMode + ")");
            }

            if (!caps.getJobExecutionModeList().contains(mJobExecutionMode)) {
                throw new CapabilitiesExceededException("Supplied job execution mode not supported : List("
                        + caps.getJobExecutionModeList().toString() + ") Target Value(" + mJobExecutionMode + ")");
            }

            if (!caps.getNumberUpModeList().contains(mNumberUpMode)) {
                throw new CapabilitiesExceededException("Supplied number up mode not supported : List("
                        + caps.getNumberUpModeList().toString() + ") Target Value(" + mNumberUpMode + ")");
            }

            if (!(mNumberUpMode == NumberUpMode.DEFAULT || mNumberUpMode == NumberUpMode.ONE_UP)) {
                if (!caps.getNumberUpDirectionByNumberUpCount().get(mNumberUpMode).contains(mNumberUpDirection)) {
                    throw new CapabilitiesExceededException("Supplied number up direction not supported for selected number up mode");
                }
            }

            if (!caps.getOutputBinList().contains(mOutputBin)) {
                throw new CapabilitiesExceededException("Supplied output bin is not supported : List("
                        + caps.getOutputBinList().toString() + ") Target Value(" + mOutputBin + ")");
            }

            if (!caps.getProgressDialogModeList().contains(mProgressDialogMode)) {
                throw new CapabilitiesExceededException("Supplied progress dialog mode is not supported : List("
                        + caps.getProgressDialogModeList().toString() + ") Target Value(" + mProgressDialogMode + ")");
            }

            if (!caps.getWatermarkTypeList().contains(mWatermarkType)) {
                throw new CapabilitiesExceededException("Supplied watermark type value not supported : List("
                        + caps.getWatermarkTypeList().toString() + ") Target Value(" + mWatermarkType + ")");
            }
            if (!(mWatermarkType == WatermarkType.DEFAULT || mWatermarkType == WatermarkType.NONE)) {
                if (mWatermarkType == WatermarkType.TEXT) {
                    if (mWatermarkTextColor == null) {
                        /** Temporary handled in library JALPINF-2016 */
                        setWatermarkTextColor("Black");
                    }
                    if (!caps.getWatermarkTextColorList().contains(mWatermarkTextColor)) {
                        throw new CapabilitiesExceededException("Supplied watermark text color value not supported : List("
                                + caps.getWatermarkTextColorList().toString() + ") Target Value(" + mWatermarkTextColor + ")");
                    }
                } else if (mWatermarkType == WatermarkType.SECURE) {
                    if (!caps.getWatermarkBackgroundPatternList().contains(mWatermarkBackgroundPattern)) {
                        throw new CapabilitiesExceededException("Supplied watermark background pattern value not supported : List("
                                + caps.getWatermarkBackgroundPatternList().toString() + ") Target Value(" + mWatermarkBackgroundPattern + ")");
                    }
                    if (mWatermarkBackgroundColor == null) {
                        /** Temporary handled in library JALPINF-2016 */
                        setWatermarkBackgroundColor("Gray");
                    }
                    if (!caps.getWatermarkBackgroundColorList().contains(mWatermarkBackgroundColor)) {
                        throw new CapabilitiesExceededException("Supplied watermark background color value not supported : List("
                                + caps.getWatermarkBackgroundColorList().toString() + ") Target Value(" + mWatermarkBackgroundColor + ")");
                    }
                    if (!caps.getWatermarkRotate45List().contains(mWatermarkRotate45)) {
                        throw new CapabilitiesExceededException("Supplied watermark rotation value not supported : List("
                                + caps.getWatermarkRotate45List().toString() + ") Target Value(" + mWatermarkRotate45 + ")");
                    }
                }
                if (!caps.getWatermarkTextSizeList().contains(mWatermarkTextSize)) {
                    throw new CapabilitiesExceededException("Supplied watermark TextSize value is not supported : List("
                            + caps.getWatermarkTextSizeList().toString() + ") Target Value(" + mWatermarkTextSize + ")");
                }

                if (!caps.getWatermarkTransparencyRange().validate(mWatermarkTransparency)) {
                    throw new CapabilitiesExceededException("Supplied watermark Transparency value is not in range " + caps.getWatermarkTransparencyRange());
                }

                if (!caps.getWatermarkOnlyFirstPageList().contains(mWatermarkOnlyFirstPage)) {
                    throw new CapabilitiesExceededException("Supplied watermark OnlyFirstPage value not supported : List("
                            + caps.getWatermarkOnlyFirstPageList().toString() + ") Target Value(" + mWatermarkOnlyFirstPage + ")");
                }

                if (!caps.getWatermarkDarknessRange().validate(mWatermarkDarkness)) {
                    throw new CapabilitiesExceededException("Supplied watermark darkness value is not in range " + caps.getWatermarkDarknessRange());
                }

                if (!caps.getWatermarkMessageTypeList().contains(mWatermarkMessageType)) {
                    throw new CapabilitiesExceededException("Supplied watermark message type value not supported : List("
                            + caps.getWatermarkMessageTypeList().toString() + ") Target Value(" + mWatermarkMessageType + ")");
                }
                if (mWatermarkFont == null) {
                    /** Temporary handled in library JALPINF-2016 */
                    setWatermarkFont("LetterGothic");
                }
                if (!caps.getWatermarkFontList().contains(mWatermarkFont)) {
                    throw new CapabilitiesExceededException("Supplied watermark font value not supported : List("
                            + caps.getWatermarkFontList().toString() + ") Target Value(" + mWatermarkFont + ")");
                }
            }

            if (!caps.getEraseMarginUnitList().contains(mEraseMarginUnit)) {
                throw new CapabilitiesExceededException("Supplied erase margin unit not supported : List("
                        + caps.getEraseMarginUnitList().toString() + ") Target Value(" + mEraseMarginUnit + ")");
            }

            if (mEraseBackBottom > 0) {
                if (!caps.getEraseBackBottomRange().validate(mEraseBackBottom)) {
                    throw new CapabilitiesExceededException("Supplied erase back bottom value is not in range " + caps.getEraseBackBottomRange());
                }
            }

            if (mEraseBackLeft > 0) {
                if (!caps.getEraseBackLeftRange().validate(mEraseBackLeft)) {
                    throw new CapabilitiesExceededException("Supplied erase back left value is not in range " + caps.getEraseBackLeftRange());
                }
            }

            if (mEraseBackRight > 0) {
                if (!caps.getEraseBackRightRange().validate(mEraseBackRight)) {
                    throw new CapabilitiesExceededException("Supplied erase back right value is not in range " + caps.getEraseBackRightRange());
                }
            }

            if (mEraseBackTop > 0) {
                if (!caps.getEraseBackTopRange().validate(mEraseBackTop)) {
                    throw new CapabilitiesExceededException("Supplied erase back top value is not in range " + caps.getEraseBackTopRange());
                }
            }

            if (mEraseFrontBottom > 0) {
                if (!caps.getEraseFrontBottomRange().validate(mEraseFrontBottom)) {
                    throw new CapabilitiesExceededException("Supplied erase front bottom value is not in range " + caps.getEraseFrontBottomRange());
                }
            }

            if (mEraseFrontLeft > 0) {
                if (!caps.getEraseFrontLeftRange().validate(mEraseFrontLeft)) {
                    throw new CapabilitiesExceededException("Supplied erase front left value is not in range " + caps.getEraseFrontLeftRange());
                }
            }

            if (mEraseFrontRight > 0) {
                if (!caps.getEraseFrontRightRange().validate(mEraseFrontRight)) {
                    throw new CapabilitiesExceededException("Supplied erase front right value is not in range " + caps.getEraseFrontRightRange());
                }
            }

            if (mEraseFrontTop > 0) {
                if (!caps.getEraseFrontTopRange().validate(mEraseFrontTop)) {
                    throw new CapabilitiesExceededException("Supplied erase front top value is not in range " + caps.getEraseFrontTopRange());
                }
            }

            if (!caps.getCaptureModeList().contains(mCaptureMode)) {
                throw new CapabilitiesExceededException("Supplied capture mode not supported : List("
                        + caps.getCaptureModeList().toString() + ") Target Value(" + mCaptureMode + ")");
            }
            if (!caps.getImageShiftReduceToFitList().contains(mImageShiftReduceToFit)) {
                throw new CapabilitiesExceededException("Supplied ImageShiftReduceToFit is not supported : List("
                        + caps.getImageShiftReduceToFitList().toString() + ") Target Value(" + mImageShiftReduceToFit + ")");
            }

            if (!caps.getImageShiftUnitsList().contains(mImageShiftUnits)) {
                throw new CapabilitiesExceededException("Supplied ImageShiftUnits is not supported : List("
                        + caps.getImageShiftUnitsList().toString() + ") Target Value(" + mImageShiftUnits + ")");
            }

            if (mImageShiftXFront != 0) {
                if (!caps.getImageShiftXFrontRange().validate(mImageShiftXFront)) {
                    throw new CapabilitiesExceededException("Supplied mImageShiftXFront value is not in range " + caps.getImageShiftXFrontRange());
                }
            }

            if (mImageShiftYFront != 0) {
                if (!caps.getImageShiftYFrontRange().validate(mImageShiftYFront)) {
                    throw new CapabilitiesExceededException("Supplied mImageShiftYFront value is not in range " + caps.getImageShiftYFrontRange());
                }
            }

            if (mImageShiftXBack != 0) {
                if (!caps.getImageShiftXBackRange().validate(mImageShiftXBack)) {
                    throw new CapabilitiesExceededException("Supplied mImageShiftXBack value is not in range " + caps.getImageShiftXBackRange());
                }
            }

            if (mImageShiftYBack != 0) {
                if (!caps.getImageShiftYBackRange().validate(mImageShiftYBack)) {
                    throw new CapabilitiesExceededException("Supplied mImageShiftYBack value is not in range " + caps.getImageShiftYBackRange());
                }
            }

            if (!caps.getBookletBordersEachPageList().contains(mBookletBordersEachPage)) {
                throw new CapabilitiesExceededException("Supplied BookletBordersEachPage is not supported : List("
                        + caps.getBookletBordersEachPageList().toString() + ") Target Value(" + mBookletBordersEachPage + ")");
            }

            if (!caps.getBookletFinishingOptionList().contains(mBookletFinishingOption)) {
                throw new CapabilitiesExceededException("Supplied BookletFinishingOption is not supported : List("
                        + caps.getBookletFinishingOptionList().toString() + ") Target Value(" + mBookletFinishingOption + ")");
            }

            if (!caps.getBookletFormatList().contains(mBookletFormat)) {
                throw new CapabilitiesExceededException("Supplied BookletFormat is not supported : List("
                        + caps.getBookletFormatList().toString() + ") Target Value(" + mBookletFormat + ")");
            }

            if (!caps.getStapleOptionList().contains(mStapleOption)) {
                throw new CapabilitiesExceededException("Supplied staple option is not supported : List("
                        + caps.getStapleOptionList().toString() + ") Target Value(" + mStapleOption + ")");
            }

            if (!caps.getPunchModeList().contains(mPunchMode)) {
                throw new CapabilitiesExceededException("Supplied punch mode is not supported : List("
                        + caps.getPunchModeList().toString() + ") Target Value(" + mPunchMode + ")");
            }

            if (!caps.getFoldModeList().contains(mFoldMode)) {
                throw new CapabilitiesExceededException("Supplied fold mode is not supported : List("
                        + caps.getFoldModeList().toString() + ") Target Value(" + mFoldMode + ")");
            }
            boolean isStampOptionSet = false;
            for (String stampPositionName : mStampOptionMap.keySet()) {
                if (!caps.getStampPositionList().contains(StampPosition.valueOf(stampPositionName))) {
                    throw new CapabilitiesExceededException("Supplied stamp option is not supported : List("
                            + caps.getStampPositionList().toString() + ") Target Value(" + StampPosition.valueOf(stampPositionName) + ")");
                }
                if (mStampOptionMap.get(stampPositionName).getType() != StampType.NONE) {
                    isStampOptionSet = true;
                }
            }

            if (isStampOptionSet && mBookletFormat != BookletFormat.DEFAULT && mBookletFormat != BookletFormat.OFF) {
                throw new CapabilitiesExceededException("The stamp option and the booklet option cannot be set at the same time");
            }

        }
    }

    /**
     * Builder for creating {@link CopyAttributes} configured for regular copy including scanning and printing.
     *
     * @since API 3
     */
    public static class CopyBuilder extends CopyAttributesBuilder<CopyBuilder> {
        /**
         * Construct a new Builder for regular copy job.<br>
         *
         * @since API 3
         */
        @SuppressWarnings({"unused"})
        public CopyBuilder() {
            this.mJobExecutionMode = JobExecutionMode.NORMAL;
        }

        /**
         * Combine all of the attributes in this into a ScanAttributes object.
         *
         * @param caps The scan capabilities.
         * @return a ScanAttributes object containing all of the attributes.
         * @throws CapabilitiesExceededException if caps does not contain the set attribute value.
         * @since API 3
         */
        @Override
        @SuppressWarnings({"unused"})
        @NonNull
        public CopyAttributes build(@NonNull final CopyAttributesCaps caps) throws CapabilitiesExceededException {
            validate(caps);

            return new CopyAttributes(this);
        }
    }

    /**
     * Builder for creating {@link CopyAttributes} configured to save copy job to the job storage to be released later.
     *
     * @since API 3
     */
    public static class StoreCopyBuilder extends CopyAttributesBuilder<StoreCopyBuilder> {
        String mStoreJobFolderName;

        String mStoreJobName;

        JobCredentialsAttributes mJobCredentialsAttributes;

        RetentionMode mStoredJobRetentionModeOnPowerCycle = RetentionMode.DEFAULT;

        RetentionMode mStoredJobRetentionModeOnRelease = RetentionMode.DEFAULT;

        /**
         * Construct a new Builder for stored copy job.<br>
         *
         * @since API 3
         */
        @SuppressWarnings({"unused"})
        public StoreCopyBuilder() {
            this.mJobExecutionMode = JobExecutionMode.STORE;
        }

        /**
         * Sets job name for store job
         *
         * @param jobName job name
         * @return this builder for method chaining.
         * @since API 3
         */
        @SuppressWarnings({"unused", "unchecked"})
        @NonNull
        public StoreCopyBuilder setStoreJobName(@NonNull final String jobName) {
            this.mStoreJobName = jobName;
            return this;
        }

        /**
         * Sets folder name for store job
         *
         * @param folderName folder name
         * @return this builder for method chaining.
         * @since API 3
         */
        @SuppressWarnings({"unused", "unchecked"})
        @NonNull
        public StoreCopyBuilder setStoreJobFolderName(@NonNull final String folderName) {
            mStoreJobFolderName = folderName;
            return this;
        }

        /**
         * Sets job credentials to protect stored job
         *
         * @param jobCredentialsAttributes credentials of job
         * @return this builder for method chaining.
         * @throws NullPointerException if jobCredentialsAttributes is null.
         * @since API 3
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public StoreCopyBuilder setStoreJobCredentials(@NonNull final JobCredentialsAttributes jobCredentialsAttributes) {
            mJobCredentialsAttributes = Preconditions.checkNotNull(jobCredentialsAttributes);
            return this;
        }

        /**
         * Sets on power cycle retention mode for stored job
         *
         * @param retentionMode power cycle retention mode
         * @return this builder for method chaining.
         * @throws NullPointerException if passwordType is null.
         * @since API 3
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public StoreCopyBuilder setRetentionModeOnPowerCycle(@NonNull final RetentionMode retentionMode) {
            mStoredJobRetentionModeOnPowerCycle = Preconditions.checkNotNull(retentionMode);
            return this;
        }

        /**
         * Sets on release retention mode for stored job
         *
         * @param retentionMode release retention mode
         * @return this builder for method chaining.
         * @throws NullPointerException if passwordType is null.
         * @since API 3
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public StoreCopyBuilder setRetentionModeOnRelease(@NonNull final RetentionMode retentionMode) {
            mStoredJobRetentionModeOnRelease = Preconditions.checkNotNull(retentionMode);
            return this;
        }

        @Override
        void validate(CopyAttributesCaps caps) throws CapabilitiesExceededException {
            super.validate(caps);

            if (mJobCredentialsAttributes != null) {
                if (!caps.getPasswordTypeList().contains(mJobCredentialsAttributes.mStoreJobPasswordType)) {
                    throw new CapabilitiesExceededException("Supplied stored job password type not supported");
                }
            }
        }

        /**
         * Combine all of the attributes in this into a CopyAttributes object.
         *
         * @param caps The scan capabilities.
         * @return a ScanAttributes object containing all of the attributes.
         * @throws CapabilitiesExceededException if caps does not contain the set attribute value.
         * @throws IllegalArgumentException      if stored job folder name is null or missing fields.
         * @since API 3
         */
        @Override
        @SuppressWarnings({"unused"})
        @NonNull
        public CopyAttributes build(@NonNull final CopyAttributesCaps caps) throws CapabilitiesExceededException {
            validate(caps);

            if (!TextUtils.isEmpty(mStoreJobFolderName) && mStoreJobFolderName.length() > 32) {
                throw new IllegalArgumentException("StoreJobFolderName exceeds maximum size");
            }

            if (!TextUtils.isEmpty(mStoreJobName) && mStoreJobName.length() > 32) {
                throw new IllegalArgumentException("StoreJobName exceeds maximum size");
            }

            return new CopyAttributes(this);
        }
    }

    /**
     * Expresses the {@link CopyAttributes} in a string.
     *
     * @hide trivial
     */
    @Override
    public String toString() {
        return new StringBuilder().append("[").append("ColorMode=").append(((mColorMode != null) ? mColorMode.name().toString() : "null")).append("]").toString();
    }
}
