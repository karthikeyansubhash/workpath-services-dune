// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.printer;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.api.CapabilitiesExceededException;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.utils.CommonUtility;
import com.hp.jetadvantage.link.common.utils.SLog;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The sets of attributes for requesting a print.</b>
 * An instance of this class is created using one of the builders.
 *
 * @since API 1
 */
public class PrintAttributes implements Parcelable {

    /**
     * A collection of the color mode for printing.
     *
     * @since API 1
     */
    public enum ColorMode {
        /**
         * Sets the color mode setting configured on the device.
         */
        DEFAULT,

        /**
         * Color
         *
         * @since API 1
         */
        COLOR,

        /**
         * Black and White.
         *
         * @since API 1
         */
        MONO,

        /**
         * If the job consists of a mixture of Black and Color images (side of a sheet), the device will detect the content of each image and generate appropriate output.
         *
         * @since API 1
         */
        AUTO
    }

    /**
     * A collection of duplex mode for printing.
     *
     * @since API 1
     */
    public enum Duplex {
        /**
         * Sets the duplex settings configured on the device.
         *
         * @since API 1
         */
        DEFAULT,
        /**
         * No duplex
         *
         * @since API 1
         */
        NONE,
        /**
         * Duplex along the long edge of the paper.
         *
         * @since API 1
         */
        BOOK,
        /**
         * Duplex along the short edge of the paper.
         *
         * @since API 1
         */
        FLIP
    }

    /**
     * A collection of the configuration of auto fit
     *
     * @since API 1
     */
    public enum AutoFit {
        /**
         * Sets device's settings.
         *
         * @since API 1
         */
        DEFAULT,
        /**
         * Enable Auto fit.
         *
         * @since API 1
         */
        TRUE,
        /**
         * Disable Auto Fit.
         *
         * @since API 1
         */
        FALSE
    }

    /**
     * A collection of Staple Mode
     *
     * @since API 1
     */
    public enum StapleMode {
        /**
         * Use printer's settings.
         *
         * @since API 1
         */
        DEFAULT,
        /**
         * No staple.
         *
         * @since API 1
         */
        NONE,
        /**
         * One staple.
         *
         * @since API 1
         */
        STAPLE,
        /**
         * One staple in top left corner
         *
         * @since API 1
         */
        TOP_LEFT,
        /**
         * One staple in bottom left corner
         *
         * @since API 1
         */
        BOTTOM_LEFT,
        /**
         * One staple in top right corner
         *
         * @since API 1
         */
        TOP_RIGHT,
        /**
         * One staple in bottom right corner
         *
         * @since API 1
         */
        BOTTOM_RIGHT,
        /**
         * Two staples on left side
         *
         * @since API 1
         */
        DUAL_LEFT,
        /**
         * Two staples on right side
         *
         * @since API 1
         */
        DUAL_RIGHT,
        /**
         * Two staples on top side
         *
         * @since API 1
         */
        DUAL_TOP,
        /**
         * Two staples on bottom side
         *
         * @since API 1
         */
        DUAL_BOTTOM,
        /**
         * Hole.
         *
         * @since API 1
         */
        PUNCH,
        /**
         * Cover.
         *
         * @since API 1
         */
        COVER,
        /**
         * Binds the set.
         *
         * @since API 1
         */
        BIND,
        /**
         * Binds the set with one or more staples along the middle fold.
         *
         * @since API 1
         */
        SADDLE_STITCH,
        /**
         * Binds the set with one or more staples along one edge.
         *
         * @since API 1
         */
        EDGE_STITCH,
        /**
         * Binds the set with one or more staples along the left edge.
         *
         * @since API 1
         */
        EDGE_STITCH_LEFT,
        /**
         * Binds the set with one or more staples along the right edge.
         *
         * @since API 1
         */
        EDGE_STITCH_RIGHT,
        /**
         * Binds the set with one or more staples along the top edge.
         *
         * @since API 1
         */
        EDGE_STITCH_TOP,
        /**
         * Binds the set with one or more staples along the bottom edge.
         *
         * @since API 1
         */
        EDGE_STITCH_BOTTOM
    }

    /**
     * A collection of Paper Source
     *
     * @since API 1
     */
    public enum PaperSource {
        /**
         * Sets device's settings.
         *
         * @since API 1
         */
        DEFAULT,
        /**
         * Sets auto selected paper source
         *
         * @since API 1
         */
        AUTO,
        /**
         * Prints from tray 1
         *
         * @since API 1
         */
        TRAY_1,
        /**
         * Prints from tray 2
         *
         * @since API 1
         */
        TRAY_2,
        /**
         * Prints from tray 3
         *
         * @since API 1
         */
        TRAY_3,
        /**
         * Prints from tray 4
         *
         * @since API 1
         */
        TRAY_4,
        /**
         * Prints from tray 5
         *
         * @since API 1
         */
        TRAY_5,
        /**
         * Prints from tray 6
         *
         * @since API 1
         */
        TRAY_6,
        /**
         * Prints from tray 7
         *
         * @since API 1
         */
        TRAY_7,
        /**
         * Prints from tray 8
         *
         * @since API 1
         */
        TRAY_8,
        /**
         * Prints from tray 9
         *
         * @since API 1
         */
        TRAY_9,
        /**
         * Prints from tray 10
         *
         * @since API 1
         */
        TRAY_10,
        /**
         * Prints from manual feeder
         *
         * @since API 1
         */
        MANUAL_FEED
    }

    /**
     * A collection of Paper Size
     *
     * @since API 1
     */
    public enum PaperSize {
        /**
         * Sets device's settings.
         *
         * @since API 1
         */
        DEFAULT,
        /**
         * ISO A3 (297mm x 420mm)
         *
         * @since API 1
         */
        A3,
        /**
         * ISO A4 (210mm x 297mm)
         *
         * @since API 1
         */
        A4,
        /**
         * ISO A5 (148mm x 210mm)
         *
         * @since API 1
         */
        A5,
        /**
         * ISO A6 (105mm x 148mm)
         *
         * @since API 1
         */
        A6,
        /**
         * ISO B4 (250mm x 353mm)
         *
         * @since API 1
         */
        B4,
        /**
         * ISO B5 (176mm x 250mm)
         *
         * @since API 1
         */
        B5,
        /**
         * ISO B6 (125mm x 176mm)
         *
         * @since API 1
         */
        B6,
        /**
         * Envelope 9 (3.875inch x 8.875inch)
         *
         * @since API 5
         */
        ENVELOPE_9,
        /**
         * Envelope Comm10 (4.125inch x 9.5inch)
         *
         * @since API 5
         */
        ENVELOPE_COMM10,
        /**
         * Envelope Monarch (3.875inch x 7.5inch)
         *
         * @since API 5
         */
        ENVELOPE_MONARCH,
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
         * Envelope DL (110mm x 220mm)
         *
         * @since API 5
         */
        ENVELOPE_DL,
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
         * Executive (7.25inch x 10.5inch)
         *
         * @since API 1
         */
        EXECUTIVE,
        /**
         * INCH8POINT5X13 (8.5inch x 13inch)
         *
         * @since API 1
         */
        INCH8POINT5X13,
        /**
         * INCH12X18 (12inch x 18inch)
         *
         * @since API 1
         */
        INCH12X18,
        /**
         * JIS B4 (257mm x 364mm)
         *
         * @since API 1
         */
        JB4,
        /**
         * JIS B5 (182mm x 257mm)
         *
         * @since API 1
         */
        JB5,
        /**
         * JIS B6 (128mm x 182mm)
         *
         * @since API 1
         */
        JB6,
        /**
         * Japanese Double Postcard (148mm x 200mm)
         *
         * @since API 5
         */
        JDOUBLE_POSTCARD,
        /**
         * Japanese Postcard (100mm x 148mm)
         *
         * @since API 5
         */
        JPOSTCARD,
        /**
         * K8 (270mm x 390mm)
         *
         * @since API 5
         */
        K8,
        /**
         * K8_260x368mm (260mm x 368mm)
         *
         * @since API 5
         */
        K8_260x368mm,
        /**
         * K16 (195mm x 270mm)
         *
         * @since API 5
         */
        K16,
        /**
         * K16_184x260mm (184mm x 260mm)
         *
         * @since API 5
         */
        K16_184x260mm,
        /**
         * Ledger (11inch x 17inch)
         *
         * @since API 1
         */
        LEDGER,
        /**
         * Legal (8.5inch x 14inch)
         *
         * @since API 1
         */
        LEGAL,
        /**
         * Letter (8.5inch x 11inch)
         *
         * @since API 1
         */
        LETTER,
        /**
         * Oficio (216mm x 340mm)
         *
         * @since API 5
         */
        OFICIO,
        /**
         * PRC 8K (273mm x 394mm)
         *
         * @since API 1
         */
        PK8,
        /**
         * PRC 16K (197mm x 273mm)
         *
         * @since API 1
         */
        PK16,
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
         * GENERAL_10X15cm (10cm x 15cm)
         *
         * @since API 5
         */
        GENERAL_10X15cm,
        /**
         * GENERAL_3X5in (3inch x 5inch)
         *
         * @since API 5
         */
        GENERAL_3X5in,
        /**
         * GENERAL_4X6in (4inch x 6inch)
         *
         * @since API 5
         */
        GENERAL_4X6in,
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
         * Statement (5.5inch x 8.5inch)
         *
         * @since API 1
         */
        STATEMENT;

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
     * A collection of Paper Type
     *
     * @since API 1
     */
    public enum PaperType {
        /**
         * Sets device's settings.
         *
         * @since API 1
         */
        DEFAULT,
        /**
         * Bond
         *
         * @since API 1
         */
        BOND,
        /**
         * Card Stock 176-220g
         *
         * @since API 1
         */
        CARD_STOCK,
        /**
         * Card Stock Glossy 176-220g
         *
         * @since API 1
         */
        CARD_STOCK_GLOSSY,
        /**
         * Colored
         *
         * @since API 1
         */
        COLORED,
        /**
         * Envelope
         *
         * @since API 1
         */
        ENVELOPE,
        /**
         * Extra Heavy 131-175g
         *
         * @since API 1
         */
        EXTRA_HEAVY,
        /**
         * Extra Heavy Glossy 131-175g
         *
         * @since API 1
         */
        EXTRA_HEAVY_GLOSSY,
        /**
         * Heavy 111-130g
         *
         * @since API 1
         */
        HEAVY,
        /**
         * Heavy Envelope
         *
         * @since API 1
         */
        HEAVY_ENVELOPE,
        /**
         * Hvy Glossy 111-130g
         *
         * @since API 5
         */
        HEAVY_GLOSSY,
        /**
         * Paperboard 301g+
         *
         * @since API 5
         */
        HEAVY_PAPERBOARD,
        /**
         * Heavy Rough
         *
         * @since API 1
         */
        HEAVY_ROUGH,
        /**
         * HP Advanced Photo
         *
         * @since API 1
         */
        HP_ADVANCED_PHOTO,
        /**
         * HP Brochure Glossy
         *
         * @since API 1
         */
        HP_BROCHURE_GLOSSY,
        /**
         * HP Brochure Matte 180g
         *
         * @since API 1
         */
        HP_BROCHURE_MATTE_180G,
        /**
         * HP EcoFFICIENT
         *
         * @since API 1
         */
        HP_ECOFFICIENT,
        /**
         * HP Glossy 120g
         *
         * @since API 1
         */
        HP_GLOSSY_120G,
        /**
         * HP Glossy 150g
         *
         * @since API 1
         */
        HP_GLOSSY_150G,
        /**
         * HP Glossy 200g
         *
         * @since API 1
         */
        HP_GLOSSY_200G,
        /**
         * HP InkJet Matte 120g (Premium Presentation)
         *
         * @since API 1
         */
        HP_INKJET_MATTE_120G,
        /**
         * HP Matte 90g
         *
         * @since API 1
         */
        HP_MATTE_90G,
        /**
         * HP Matte 105g
         *
         * @since API 1
         */
        HP_MATTE_105G,
        /**
         * HP Matte 120g
         *
         * @since API 1
         */
        HP_MATTE_120G,
        /**
         * HP Matter 150g
         *
         * @since API 1
         */
        HP_MATTE_150G,
        /**
         * HP Matte 200g
         *
         * @since API 1
         */
        HP_MATTE_200G,
        /**
         * HP SoftGloss 120g
         *
         * @since API 1
         */
        HP_SOFT_GLOSS_120G,
        /**
         * Intermediate 85-95g
         *
         * @since API 1
         */
        INTERMEDIATE,
        /**
         * Labels
         *
         * @since API 1
         */
        LABELS,
        /**
         * Letterhead
         *
         * @since API 1
         */
        LETTERHEAD,
        /**
         * Light 60-74g
         *
         * @since API 1
         */
        LIGHT,
        /**
         * Light Bond
         *
         * @since API 5
         */
        LIGHT_BOND,
        /**
         * Light Rough
         *
         * @since API 5
         */
        LIGHT_ROUGH,
        /**
         * Paperboard 221-255g
         *
         * @since API 5
         */
        LIGHT_PAPERBOARD,
        /**
         * Mid-Weight 96-110g
         *
         * @since API 1
         */
        MID_WEIGHT,
        /**
         * Mid-WtGlossy 96-110g
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
         * Paperboard 256-300g
         *
         * @since API 5
         */
        PAPERBOARD,
        /**
         * Plain
         *
         * @since API 1
         */
        PLAIN,
        /**
         * Preprinted
         *
         * @since API 1
         */
        PREPRINTED,
        /**
         * Prepunched
         *
         * @since API 1
         */
        PREPUNCHED,
        /**
         * Recycled
         *
         * @since API 1
         */
        RECYCLED,
        /**
         * Rough
         *
         * @since API 1
         */
        ROUGH,
        /**
         * Tab
         *
         * @since API 5
         */
        TAB,
        /**
         * Transparency
         *
         * @since API 1
         */
        TRANSPARENCY,
        /**
         * Vellum
         *
         * @since API 5
         */
        VELLUM
    }

    /**
     * A collection of Document Format
     *
     * @since API 1
     */
    public enum DocumentFormat {
        /**
         * Sets device's default.
         *
         * @since API 1
         */
        AUTO,
        /**
         * PDF Document.
         *
         * @since API 1
         */
        PDF,
        /**
         * JPEG Image.
         *
         * @since API 1
         */
        JPEG,
        /**
         * TIFF Image.
         *
         * @since API 1
         */
        TIFF,
        /**
         * PCL5 Document.
         *
         * @since API 1
         */
        PCL5,
        /**
         * PCL6 Document.
         *
         * @since API 1
         */
        PCL6,
        /**
         * PostScript Document.
         *
         * @since API 1
         */
        PS,
        /**
         * Text Document.
         *
         * @since API 1
         */
        TEXT
    }

    /**
     * The source for file to print.
     *
     * @since API 1
     */
    public enum Source {
        /**
         * Local storage
         *
         * @since API 1
         */
        STORAGE,

        /**
         * Remote storage from http path
         *
         * @since API 1
         */
        HTTP,

        /**
         * Attached USB
         *
         * @deviceOnly
         * @since API 2
         */
        USB,

        /**
         * Input stream
         *
         * @since API 2
         */
        STREAM
    }

    /**
     * A collection of Collate
     *
     * @since API 1
     */
    public enum CollateMode {
        /**
         * Use printer's settings.
         *
         * @since API 1
         */
        DEFAULT,
        /**
         * Collated
         *
         * @since API 1
         */
        COLLATED,
        /**
         * Uncollated
         *
         * @since API 1
         */
        UNCOLLATED
    }

    /**
     * A collection of Orientation
     *
     * @since API 5
     */
    public enum Orientation {
        /**
         * Use printer's settings.
         *
         * @since API 5
         */
        DEFAULT,
        /**
         * Portrait
         *
         * @since API 5
         */
        PORTRAIT,
        /**
         * Landscape
         *
         * @since API 5
         */
        LANDSCAPE,
        /**
         * Reverse Portrait
         *
         * @since API 5
         */
        REVERSE_PORTRAIT,
        /**
         * Reverse Landscape
         *
         * @since API 5
         */
        REVERSE_LANDSCAPE,
        /**
         * NONE
         *
         * @since API 5
         */
        NONE

    }

    /**
     * A collection of Print-quality
     *
     * @since API 5
     */
    public enum PrintQuality {
        /**
         * Use printer's settings.
         *
         * @since API 5
         */
        DEFAULT,
        /**
         * Draft
         *
         * @since API 5
         */
        DRAFT,
        /**
         * Normal
         *
         * @since API 5
         */
        NORMAL,
        /**
         * High
         *
         * @since API 5
         */
        HIGH

    }

    /**
     * A collection of OutputBin
     *
     * @since API 5
     */
    public enum OutputBin {
        /**
         * Use printer's settings.
         *
         * @since API 5
         */
        DEFAULT,
        /**
         * Auto
         *
         * @since API 5
         */
        AUTO,
        /**
         * Bottom
         *
         * @since API 5
         */
        BOTTOM,
        /**
         * Center
         *
         * @since API 5
         */
        CENTER,
        /**
         * FaceDown
         *
         * @since API 5
         */
        FACE_DOWN,
        /**
         * FaceUp
         *
         * @since API 5
         */
        FACE_UP,
        /**
         * LargeCapacity
         *
         * @since API 5
         */
        LARGE_CAPACITY,
        /**
         * Left
         *
         * @since API 5
         */
        LEFT,
        /**
         * OutputBin1 or Mailbox1
         *
         * @since API 5
         */
        OUTPUT_BIN_1,
        /**
         * OutputBin2 or Mailbox2
         *
         * @since API 5
         */
        OUTPUT_BIN_2,
        /**
         * OutputBin3 or Mailbox3
         *
         * @since API 5
         */
        OUTPUT_BIN_3,
        /**
         * OutputBin4 or Mailbox4
         *
         * @since API 5
         */
        OUTPUT_BIN_4,
        /**
         * OutputBin5 or Mailbox5
         *
         * @since API 5
         */
        OUTPUT_BIN_5,
        /**
         * OutputBin6 or Mailbox6
         *
         * @since API 5
         */
        OUTPUT_BIN_6,
        /**
         * OutputBin7 or Mailbox7
         *
         * @since API 5
         */
        OUTPUT_BIN_7,
        /**
         * OutputBin8 or Mailbox8
         *
         * @since API 5
         */
        OUTPUT_BIN_8,
        /**
         * OutputBin9 or Mailbox9
         *
         * @since API 5
         */
        OUTPUT_BIN_9,
        /**
         * OutputBin10 or Mailbox10
         *
         * @since API 5
         */
        OUTPUT_BIN_10,
        /**
         * Middle
         *
         * @since API 5
         */
        MIDDLE,
        /**
         * MyMailbox
         *
         * @since API 5
         */
        MY_MAILBOX,
        /**
         * Rear
         *
         * @since API 5
         */
        REAR,
        /**
         * Right
         *
         * @since API 5
         */
        RIGHT,
        /**
         * Side
         *
         * @since API 5
         */
        SIDE,
        /**
         * Stacker1
         *
         * @since API 5
         */
        STACKER_1,
        /**
         * Stacker2
         *
         * @since API 5
         */
        STACKER_2,
        /**
         * Stacker3
         *
         * @since API 5
         */
        STACKER_3,
        /**
         * Stacker4
         *
         * @since API 5
         */
        STACKER_4,
        /**
         * Stacker5
         *
         * @since API 5
         */
        STACKER_5,
        /**
         * Stacker6
         *
         * @since API 5
         */
        STACKER_6,
        /**
         * Stacker7
         *
         * @since API 5
         */
        STACKER_7,
        /**
         * Stacker8
         *
         * @since API 5
         */
        STACKER_8,
        /**
         * Stacker9
         *
         * @since API 5
         */
        STACKER_9,
        /**
         * Stacker10
         *
         * @since API 5
         */
        STACKER_10,
        /**
         * Top
         *
         * @since API 5
         */
        TOP,
        /**
         * Tray1
         *
         * @since API 5
         */
        TRAY_1,
        /**
         * Tray2
         *
         * @since API 5
         */
        TRAY_2,
        /**
         * Tray3
         *
         * @since API 5
         */
        TRAY_3,
        /**
         * Tray4
         *
         * @since API 5
         */
        TRAY_4,
        /**
         * Tray5
         *
         * @since API 5
         */
        TRAY_5,
        /**
         * Tray6
         *
         * @since API 5
         */
        TRAY_6,
        /**
         * Tray7
         *
         * @since API 5
         */
        TRAY_7,
        /**
         * Tray8
         *
         * @since API 5
         */
        TRAY_8,
        /**
         * Tray9
         *
         * @since API 5
         */
        TRAY_9,
        /**
         * Tray10
         *
         * @since API 5
         */
        TRAY_10
    }

    /**
     * A collection of finishings
     *
     * @since API 5
     */
    public enum Finishings {
        /**
         * Use printer's settings.
         *
         * @since API 5
         */
        DEFAULT,
        /**
         * No staple.
         *
         * @since API 5
         */
        NONE,
        /**
         * One staple.
         *
         * @since API 5
         */
        STAPLE,
        /**
         * Punch.
         *
         * @since API 5
         */
        PUNCH,
        /**
         * Cover.
         *
         * @since API 5
         */
        COVER,
        /**
         * Binds the set.
         *
         * @since API 5
         */
        BIND,
        /**
         * Binds the set with one or more staples along the middle fold.
         *
         * @since API 5
         */
        SADDLE_STITCH,
        /**
         * Binds the set with one or more staples along one edge.
         *
         * @since API 5
         */
        EDGE_STITCH,
        /**
         * Fold (any type)
         *
         * @since API 5
         */
        FOLD,
        /**
         * Trim (any type)
         *
         * @since API 5
         */
        TRIM,
        /**
         * Bale (any type)
         *
         * @since API 5
         */
        BALE,
        /**
         * Fold to make booklet
         *
         * @since API 5
         */
        BOOKLET_MAKER,
        /**
         * Offset for binding (any type)
         *
         * @since API 5
         */
        JOG_OFFSET,
        /**
         * Apply protective liquid or powder coating
         *
         * @since API 5
         */
        COAT,
        /**
         * Apply protective (solid) material
         *
         * @since API 5
         */
        LAMINATE,
        /**
         * One staple in top left corner
         *
         * @since API 5
         */
        STAPLE_TOP_LEFT,
        /**
         * One staple in bottom left corner
         *
         * @since API 5
         */
        STAPLE_BOTTOM_LEFT,
        /**
         * One staple in top right corner
         *
         * @since API 5
         */
        STAPLE_TOP_RIGHT,
        /**
         * One staple in bottom right corner
         *
         * @since API 5
         */
        STAPLE_BOTTOM_RIGHT,
        /**
         * Binds the set with one or more staples along the left edge.
         *
         * @since API 5
         */
        EDGE_STITCH_LEFT,
        /**
         * Binds the set with one or more staples along the top edge.
         *
         * @since API 5
         */
        EDGE_STITCH_TOP,
        /**
         * Binds the set with one or more staples along the right edge.
         *
         * @since API 5
         */
        EDGE_STITCH_RIGHT,
        /**
         * Binds the set with one or more staples along the bottom edge.
         *
         * @since API 5
         */
        EDGE_STITCH_BOTTOM,
        /**
         * Two staples on left side
         *
         * @since API 5
         */
        STAPLE_DUAL_LEFT,
        /**
         * Two staples on top side
         *
         * @since API 5
         */
        STAPLE_DUAL_TOP,
        /**
         * Two staples on right side
         *
         * @since API 5
         */
        STAPLE_DUAL_RIGHT,
        /**
         * Two staples on bottom side
         *
         * @since API 5
         */
        STAPLE_DUAL_BOTTOM,
        /**
         * Three staples on left
         *
         * @since API 5
         */
        STAPLE_TRIPLE_LEFT,
        /**
         * Three staples on top
         *
         * @since API 5
         */
        STAPLE_TRIPLE_TOP,
        /**
         * Three staples on right
         *
         * @since API 5
         */
        STAPLE_TRIPLE_RIGHT,
        /**
         * Three staples on bottom
         *
         * @since API 5
         */
        STAPLE_TRIPLE_BOTTOM,
        /**
         * Bind on left
         *
         * @since API 5
         */
        BIND_LEFT,
        /**
         * Bind on top
         *
         * @since API 5
         */
        BIND_TOP,
        /**
         * Bind on right
         *
         * @since API 5
         */
        BIND_RIGHT,
        /**
         * Bind on bottom
         *
         * @since API 5
         */
        BIND_BOTTOM,
        /**
         * Trim output after each copy
         *
         * @since API 5
         */
        TRIM_AFTER_COPIES,
        /**
         * Trim output after each document
         *
         * @since API 5
         */
        TRIM_AFTER_DOCUMENTS,
        /**
         * Trim output after job
         *
         * @since API 5
         */
        TRIM_AFTER_JOB,
        /**
         * Trim output after each page
         *
         * @since API 5
         */
        TRIM_AFTER_PAGES,
        /**
         * Punch 1 hole top left
         *
         * @since API 5
         */
        PUNCH_TOP_LEFT,
        /**
         * Punch 1 hole bottom left
         *
         * @since API 5
         */
        PUNCH_BOTTOM_LEFT,
        /**
         * Punch 1 hole top right
         *
         * @since API 5
         */
        PUNCH_TOP_RIGHT,
        /**
         * Punch 1 hole bottom right
         *
         * @since API 5
         */
        PUNCH_BOTTOM_RIGHT,
        /**
         * Punch 2 holes left side
         *
         * @since API 5
         */
        PUNCH_DUAL_LEFT,
        /**
         * Punch 2 holes top edge
         *
         * @since API 5
         */
        PUNCH_DUAL_TOP,
        /**
         * Punch 2 holes right side
         *
         * @since API 5
         */
        PUNCH_DUAL_RIGHT,
        /**
         * Punch 2 holes bottom edge
         *
         * @since API 5
         */
        PUNCH_DUAL_BOTTOM,
        /**
         * Punch 3 holes left side
         *
         * @since API 5
         */
        PUNCH_TRIPLE_LEFT,
        /**
         * Punch 3 holes top edge
         *
         * @since API 5
         */
        PUNCH_TRIPLE_TOP,
        /**
         * Punch 3 holes right side
         *
         * @since API 5
         */
        PUNCH_TRIPLE_RIGHT,
        /**
         * Punch 3 holes bottom edge
         *
         * @since API 5
         */
        PUNCH_TRIPLE_BOTTOM,
        /**
         * Punch 4 holes left side
         *
         * @since API 5
         */
        PUNCH_QUAD_LEFT,
        /**
         * Punch 4 holes top edge
         *
         * @since API 5
         */
        PUNCH_QUAD_TOP,
        /**
         * Punch 4 holes right side
         *
         * @since API 5
         */
        PUNCH_QUAD_RIGHT,
        /**
         * Punch 4 holes bottom edge
         *
         * @since API 5
         */
        PUNCH_QUAD_BOTTOM,
        /**
         * Punch multiple holes left side
         *
         * @since API 5
         */
        PUNCH_MULTIPLE_LEFT,
        /**
         * Punch multiple holes top edge
         *
         * @since API 5
         */
        PUNCH_MULTIPLE_TOP,
        /**
         * Punch multiple holes right side
         *
         * @since API 5
         */
        PUNCH_MULTIPLE_RIGHT,
        /**
         * Punch multiple holes bottom edge
         *
         * @since API 5
         */
        PUNCH_MULTIPLE_BOTTOM,
        /**
         * Accordion-fold the paper vertically into four sections
         *
         * @since API 5
         */
        FOLD_ACCORDION,
        /**
         * Fold the top and bottom quarters of the paper towards the midline, then fold in half vertically
         *
         * @since API 5
         */
        FOLD_DOUBLE_GATE,
        /**
         * Fold the top and bottom quarters of the paper towards the midline
         *
         * @since API 5
         */
        FOLD_GATE,
        /**
         * Fold the paper in half vertically
         *
         * @since API 5
         */
        FOLD_HALF,
        /**
         * Fold the paper in half horizontally, then Z-fold the paper vertically
         *
         * @since API 5
         */
        FOLD_HALF_Z,
        /**
         * Fold the top quarter of the paper towards the midline
         *
         * @since API 5
         */
        FOLD_LEFT_GATE,
        /**
         * Fold the paper into three sections vertically; sometimes also known as a C fold
         *
         * @since API 5
         */
        FOLD_LETTER,
        /**
         * Fold the paper in half vertically two times, yielding four sections
         *
         * @since API 5
         */
        FOLD_PARALLEL,
        /**
         * Fold the paper in half horizontally and vertically; sometimes also called a cross fold
         *
         * @since API 5
         */
        FOLD_POSTER,
        /**
         * Fold the bottom quarter of the paper towards the midline
         *
         * @since API 5
         */
        FOLD_RIGHT_GATE,
        /**
         * Fold the paper vertically into three sections, forming a Z
         *
         * @since API 5
         */
        FOLD_Z,
        /**
         * Fold the paper vertically into two small sections and one larger, forming an elongated Z
         *
         * @since API 5
         */
        FOLD_ENGINEERING_Z
    }


    final int mVersion;

    final ColorMode mColorMode;

    final Duplex mPlex;

    final AutoFit mAutoFit;

    final StapleMode mStapleMode;

    final PaperSource mPaperSource;

    final PaperSize mPaperSize;

    final PaperType mPaperType;

    final DocumentFormat mDocumentFormat;

    final CollateMode mCollateMode;

    final int mCopies;

    final transient Uri mFileUri;

    final Source mSource;

    final NetworkCredentialsAttributes mNetworkCredentialsAttributes;

    final String mJobName;

    final Orientation mOrientation;

    final PrintQuality mPrintQuality;

    final OutputBin mOutputBin;

    final int mStartPageRanges;
    final int mEndPageRanges;

    final List<Finishings> mFinishings;

    // not parcelable
    transient InputStream mPrintInputStream;

    /**
     * @hide trivial
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * @hide trivial
     */
    @Override
    public void writeToParcel(final Parcel out, final int flags) {
        out.writeInt(mVersion);
        out.writeInt(mCopies);
        out.writeParcelable(mFileUri, 0);
        out.writeSerializable(mColorMode);
        out.writeSerializable(mPlex);
        out.writeSerializable(mAutoFit);
        out.writeSerializable(mStapleMode);
        out.writeSerializable(mPaperSource);
        out.writeSerializable(mPaperSize);
        out.writeSerializable(mPaperType);
        out.writeSerializable(mDocumentFormat);

        out.writeSerializable(mSource);

        out.writeParcelable(mNetworkCredentialsAttributes, 0);

        out.writeSerializable(mCollateMode);

        out.writeString(mJobName);

        if (mVersion >= Sdk.VERSION_LEVEL.SIX) {
            out.writeSerializable(mOrientation);
            out.writeSerializable(mPrintQuality);
            out.writeSerializable(mOutputBin);
            out.writeInt(mStartPageRanges);
            out.writeInt(mEndPageRanges);
            ArrayList<String> finishingsList = new ArrayList<>();
            for(Finishings finishings: mFinishings) {
                finishingsList.add(finishings.name());
            }
            out.writeStringList(finishingsList);
        }
    }

    /**
     * @hide trivial
     */
    public static final Parcelable.Creator<PrintAttributes> CREATOR = new Parcelable.Creator<PrintAttributes>() {
        public PrintAttributes createFromParcel(final Parcel in) {
            return new PrintAttributes(in);
        }

        public PrintAttributes[] newArray(final int size) {
            return new PrintAttributes[size];
        }
    };

    @SuppressLint("RestrictedApi")
    private PrintAttributes(final Parcel in) {
        mFinishings = new ArrayList<>();
        mVersion = in.readInt();
        Preconditions.checkArgument(mVersion >= Sdk.VERSION_LEVEL.ONE);

        mCopies = in.readInt();
        mFileUri = in.readParcelable(Uri.class.getClassLoader());
        mColorMode = (ColorMode) in.readSerializable();
        mPlex = (Duplex) in.readSerializable();
        mAutoFit = (AutoFit) in.readSerializable();
        mStapleMode = (StapleMode) in.readSerializable();
        mPaperSource = (PaperSource) in.readSerializable();
        mPaperSize = (PaperSize) in.readSerializable();
        mPaperType = (PaperType) in.readSerializable();
        mDocumentFormat = (DocumentFormat) in.readSerializable();

        mSource = (Source) in.readSerializable();
        mNetworkCredentialsAttributes = in.readParcelable(NetworkCredentialsAttributes.class.getClassLoader());

        mCollateMode = (CollateMode) in.readSerializable();

        if (mVersion >= Sdk.VERSION_LEVEL.THREE) {
            mJobName = in.readString();
        } else {
            mJobName = null;
        }

        if (mVersion >= Sdk.VERSION_LEVEL.SIX) {
            mOrientation = (Orientation) in.readSerializable();
            mPrintQuality = (PrintQuality) in.readSerializable();
            mOutputBin = (OutputBin) in.readSerializable();

            mStartPageRanges = in.readInt();
            mEndPageRanges = in.readInt();

            ArrayList<String> finishingsList = new ArrayList<>();
            in.readStringList(finishingsList);
            for (String finishingsStr: finishingsList) {
                mFinishings.add(Finishings.valueOf(finishingsStr));
            }

        } else {
            mOrientation = Orientation.DEFAULT;
            mPrintQuality = PrintQuality.DEFAULT;
            mOutputBin = OutputBin.DEFAULT;
            mStartPageRanges = 0;
            mEndPageRanges = 0;
            mFinishings.add(Finishings.DEFAULT);
        }
    }

    private PrintAttributes(final PrintCommonAttributesBuilder builder) {
        mVersion = Sdk.VERSION.LEVEL;
        mColorMode = builder.mColorMode;
        mPlex = builder.mPlex;
        mAutoFit = builder.mAutoFit;
        mCopies = builder.mCopies;
        mFileUri = builder.mFileUri;
        mStapleMode = builder.mStapleMode;
        mPaperSource = builder.mPaperSource;
        mPaperSize = builder.mPaperSize;
        mPaperType = builder.mPaperType;
        mDocumentFormat = builder.mDocumentFormat;
        mCollateMode = builder.mCollateMode;

        mJobName = builder.mJobName;

        mOrientation = builder.mOrientation;
        mPrintQuality = builder.mPrintQuality;
        mOutputBin = builder.mOutputBin;

        mStartPageRanges = builder.mStartPageRanges;
        mEndPageRanges = builder.mEndPageRanges;
        mFinishings = builder.mFinishings;

        if (builder instanceof PrintFromHttpBuilder) {
            mNetworkCredentialsAttributes = ((PrintFromHttpBuilder) builder).mNetworkCredentialsAttributes;
            mSource = Source.HTTP;
        } else {
            mNetworkCredentialsAttributes = null;

            if (builder instanceof PrintFromUsbBuilder) {
                mSource = Source.USB;
            } else if (builder instanceof PrintFromStreamBuilder) {
                mSource = Source.STREAM;
                mPrintInputStream = ((PrintFromStreamBuilder) builder).mPrintStream;
            } else {
                mSource = Source.STORAGE;
            }
        }
    }

    /**
     * Base builder for common print attributes
     *
     * @since API 1
     */
    public static abstract class PrintCommonAttributesBuilder<T extends PrintCommonAttributesBuilder<T>> {

        ColorMode mColorMode = ColorMode.DEFAULT;

        Duplex mPlex = Duplex.DEFAULT;

        AutoFit mAutoFit = AutoFit.DEFAULT;

        int mCopies = 1;

        Uri mFileUri = Uri.EMPTY;

        StapleMode mStapleMode = StapleMode.DEFAULT;

        PaperSource mPaperSource = PaperSource.DEFAULT;

        PaperSize mPaperSize = PaperSize.DEFAULT;

        PaperType mPaperType = PaperType.DEFAULT;

        DocumentFormat mDocumentFormat = DocumentFormat.AUTO;

        CollateMode mCollateMode = CollateMode.DEFAULT;

        String mJobName = null;

        Orientation mOrientation = Orientation.DEFAULT;

        PrintQuality mPrintQuality = PrintQuality.DEFAULT;

        OutputBin mOutputBin = OutputBin.DEFAULT;

        int mStartPageRanges = 0;
        int mEndPageRanges = 0;

        List<Finishings> mFinishings = new ArrayList<>();

        PrintCommonAttributesBuilder() {
        }

        @SuppressLint("RestrictedApi")
        PrintCommonAttributesBuilder(final PrintAttributes attrs, final Uri uri) {
            Preconditions.checkNotNull(attrs);
            mColorMode = attrs.mColorMode;
            mPlex = attrs.mPlex;
            mAutoFit = attrs.mAutoFit;
            mCopies = attrs.mCopies;
            mFileUri = uri;
            mStapleMode = attrs.mStapleMode;
            mPaperSource = attrs.mPaperSource;
            mPaperSize = attrs.mPaperSize;
            mPaperType = attrs.mPaperType;
            mDocumentFormat = attrs.mDocumentFormat;
            mCollateMode = attrs.mCollateMode;
            mJobName = attrs.mJobName;
            mOrientation = attrs.mOrientation;
            mPrintQuality = attrs.mPrintQuality;
            mOutputBin = attrs.mOutputBin;
            mStartPageRanges = attrs.mStartPageRanges;
            mEndPageRanges = attrs.mEndPageRanges;
            mFinishings = attrs.mFinishings;
        }

        /**
         * Sets color mode
         *
         * @param colorMode The set of colors which the printed output will reside within.
         * @return this builder for method chaining.
         * @throws NullPointerException if colorMode is null
         * @since API 1
         */
        @SuppressLint("RestrictedApi")
        public T setColorMode(final ColorMode colorMode) {
            Preconditions.checkNotNull(colorMode);
            mColorMode = colorMode;
            return (T) this;
        }

        /**
         * Sets number of copies.
         *
         * @param copies The number of copies to be produced.
         * @return this builder for method chaining.
         * @throws IllegalArgumentException if number of copies is less than or equal to zero
         * @since API 1
         */
        @SuppressLint("RestrictedApi")
        public T setCopies(final int copies) {
            Preconditions.checkArgument(copies > 0);
            mCopies = copies;
            return (T) this;
        }

        /**
         * Sets Duplex
         *
         * @param duplex 1- or 2- sided printout.
         * @return this builder for method chaining.
         * @throws NullPointerException if duplex is null
         * @since API 1
         */
        @SuppressLint("RestrictedApi")
        public T setDuplex(final Duplex duplex) {
            Preconditions.checkNotNull(duplex);
            mPlex = duplex;
            return (T) this;
        }

        /**
         * Sets AutoFit
         *
         * @param autoFit Auto-fit for the printout.
         * @return this builder for method chaining.
         * @throws NullPointerException if autoFit is null
         * @since API 1
         */
        @SuppressLint("RestrictedApi")
        public T setAutoFit(final AutoFit autoFit) {
            Preconditions.checkNotNull(autoFit);
            mAutoFit = autoFit;
            return (T) this;
        }

        /**
         * Sets StapleMode
         *
         * @param stapleMode StapleMode for the printout.
         * @return this builder for method chaining.
         * @throws NullPointerException if stapleMode is null
         * @since API 1
         */
        @SuppressLint("RestrictedApi")
        public T setStapleMode(final StapleMode stapleMode) {
            Preconditions.checkNotNull(stapleMode);
            mStapleMode = stapleMode;
            return (T) this;
        }

        /**
         * Sets PaperSource
         *
         * @param paperSource PaperSource for the printout.
         * @return this builder for method chaining.
         * @throws NullPointerException if paperSource is null
         * @since API 1
         */
        @SuppressLint("RestrictedApi")
        public T setPaperSource(final PaperSource paperSource) {
            Preconditions.checkNotNull(paperSource);
            mPaperSource = paperSource;
            return (T) this;
        }

        /**
         * Sets PaperSize
         *
         * @param paperSize PaperSize for the printout.
         * @return this builder for method chaining.
         * @throws NullPointerException if paperSize is null
         * @since API 1
         */
        @SuppressLint("RestrictedApi")
        public T setPaperSize(final PaperSize paperSize) {
            Preconditions.checkNotNull(paperSize);
            mPaperSize = paperSize;
            return (T) this;
        }

        /**
         * Sets PaperType
         *
         * @param paperType PaperType for the printout.
         * @return this builder for method chaining.
         * @throws NullPointerException if ps is null
         * @since API 1
         */
        @SuppressLint("RestrictedApi")
        public T setPaperType(final PaperType paperType) {
            Preconditions.checkNotNull(paperType);
            mPaperType = paperType;
            return (T) this;
        }

        /**
         * Sets DocumentFormat
         *
         * @param documentFormat DocumentFormat for the printout.
         * @return this builder for method chaining.
         * @throws NullPointerException if documentFormat is null
         * @since API 1
         */
        @SuppressLint("RestrictedApi")
        public T setDocumentFormat(final DocumentFormat documentFormat) {
            Preconditions.checkNotNull(documentFormat);
            mDocumentFormat = documentFormat;
            return (T) this;
        }

        /**
         * Sets CollateMode
         *
         * @param collateMode CollateMode for the printout.
         * @return this builder for method chaining.
         * @throws NullPointerException if collateMode is null
         * @since API 1
         */
        @SuppressLint("RestrictedApi")
        public T setCollateMode(final CollateMode collateMode) {
            Preconditions.checkNotNull(collateMode);
            mCollateMode = collateMode;
            return (T) this;
        }

        /**
         * Sets job name
         *
         * @param jobName JobName
         * @return this builder for method chaining.
         * @since API 3
         */
        public T setJobName(final String jobName) {
            if(jobName != null && jobName.trim().length() < 1) {
                mJobName = null;
            } else {
                mJobName = jobName;
            }
            return (T) this;
        }

        /**
         * Sets Orientation
         *
         * @param orientation Orientation for the printout.
         * @return this builder for method chaining.
         * @throws NullPointerException if orientation is null
         * @since API 5
         */
        @SuppressLint("RestrictedApi")
        public T setOrientation(final Orientation orientation) {
            Preconditions.checkNotNull(orientation);
            mOrientation = orientation;
            return (T) this;
        }

        /**
         * Sets Sets Print quality
         *
         * @param printQuality Print quality for the printout.
         * @return this builder for method chaining.
         * @throws NullPointerException if printQuality is null
         * @since API 5
         */
        @SuppressLint("RestrictedApi")
        public T setPrintQuality(final PrintQuality printQuality) {
            Preconditions.checkNotNull(printQuality);
            mPrintQuality = printQuality;
            return (T) this;
        }

        /**
         * Sets OutputBin
         *
         * @param outputBin OutputBin for the printout.
         * @return this builder for method chaining.
         * @throws NullPointerException if outputBin is null
         * @since API 5
         */
        @SuppressLint("RestrictedApi")
        public T setOutputBin(final OutputBin outputBin) {
            Preconditions.checkNotNull(outputBin);
            mOutputBin = outputBin;
            return (T) this;
        }

        /**
         * Sets start page of page-ranges.
         *
         * @param startPageRanges The start page of page-ranges to be produced.
         * @return this builder for method chaining.
         * @throws IllegalArgumentException if startPageRanges is less than or equal to zero
         * @since API 5
         */
        @SuppressLint("RestrictedApi")
        public T setStartPageRanges(final int startPageRanges) {
            if(startPageRanges > 0){
                mStartPageRanges = startPageRanges;
            } else {
                mStartPageRanges = 0;
            }

            return (T) this;
        }

        /**
         * Sets end page of page-ranges.
         *
         * @param endPageRanges The end page of page-ranges to be produced.
         * @return this builder for method chaining.
         * @throws IllegalArgumentException if endPageRanges is less than or equal to zero
         * @since API 5
         */
        @SuppressLint("RestrictedApi")
        public T setEndPageRanges(final int endPageRanges) {
            if(endPageRanges > 0) {
                mEndPageRanges = endPageRanges;
            } else {
                mEndPageRanges = 0;
            }
            return (T) this;
        }

        /**
         * Sets Finishings
         *
         * @param finishingsList Finishings for the printout.
         * @return this builder for method chaining.
         * @throws NullPointerException if finishingsList is null
         * @since API 5
         */
        @SuppressLint("RestrictedApi")
        public T setFinishingsList(final List<Finishings> finishingsList) {
            Preconditions.checkNotNull(finishingsList);
            mFinishings = finishingsList;
            return (T) this;
        }

        abstract PrintAttributes build(final PrintAttributesCaps caps) throws CapabilitiesExceededException;

        void validate(final PrintAttributesCaps caps) throws CapabilitiesExceededException {
            if (caps == null) {
                throw new CapabilitiesExceededException("PrintAttributesCapabilities is required");
            }

            if (!caps.getColorModeList().contains(mColorMode)) {
                throw new CapabilitiesExceededException("Supplied colormode is not supported");
            }

            if (mCopies > caps.getMaxCopies()) {
                throw new CapabilitiesExceededException("The number of copies specified is greater than the max");
            }

            if (!caps.getDuplexList().contains(mPlex)) {
                throw new CapabilitiesExceededException("Supplied Duplex is not supported");
            }

            if (!caps.getStapleModeList().contains(mStapleMode)) {
                throw new CapabilitiesExceededException("Supplied StapleMode is not supported");
            }

            if (!caps.getPaperSourceList().contains(mPaperSource)) {
                throw new CapabilitiesExceededException("Supplied PaperSource is not supported");
            }

            if (!caps.getPaperSizeList().contains(mPaperSize)) {
                throw new CapabilitiesExceededException("Supplied PaperSize is not supported");
            }

            if (!caps.getPaperTypeList().contains(mPaperType)) {
                throw new CapabilitiesExceededException("Supplied PaperType is not supported");
            }

            if (!caps.getDocumentFormatList().contains(mDocumentFormat)) {
                throw new CapabilitiesExceededException("Supplied DocumentFormat is not supported");
            }

            if (!caps.getCollateModeList().contains(mCollateMode)) {
                throw new CapabilitiesExceededException("Supplied CollateMode is not supported");
            }

            // Boolean type attribute may not be supported (list is empty)
            if (mAutoFit.equals(AutoFit.TRUE) && caps.getAutoFitList().isEmpty()) {
                throw new CapabilitiesExceededException("AutoFit is not supported");
            }

            if (!caps.getOrientationList().contains(mOrientation)) {
                throw new CapabilitiesExceededException("Supplied Orientation is not supported");
            }

            if (!caps.getPrintQualityList().contains(mPrintQuality)) {
                throw new CapabilitiesExceededException("Supplied PrintQuality is not supported");
            }

            if (!caps.getOutputBinList().contains(mOutputBin)) {
                throw new CapabilitiesExceededException("Supplied OutputBin is not supported");
            }

            if (mStartPageRanges > 0) {
                if (mEndPageRanges < 1 || mStartPageRanges > mEndPageRanges) {
                    throw new CapabilitiesExceededException("The start page ranges specified is greater than the end page ranges");
                }
            }

            for (Finishings finishings: mFinishings) {
                if (!caps.getFinishingsList().contains(finishings)) {
                    throw new CapabilitiesExceededException("Supplied Finishings is not supported");

                }
            }
        }
    }

    /**
     * Builder for creating {@link PrintAttributes} to print the files from source.<br/>
     *
     * The file Uri should point to any publicly accessible file (e.g. from Downloads folder) and be one of supported types:
     * examples -"jpeg", "jpg" and "pdf"
     *
     * @since API 1
     */
    public static class PrintFromStorageBuilder extends PrintCommonAttributesBuilder<PrintFromStorageBuilder> {
        /**
         * Prints a file stored on the local storage with default attributes.<br>
         * <br>
         * Color Mode = DEFAULT <br>
         * Duplex = DEFAULT <br>
         * Copies = 1 <br>
         * <br>
         *
         * @param fileUri File URL of the form 'file://',
         *                Supported file types are {"jpeg", "jpg", "pdf"}
         * @remarks The file must reside on publicly accessible folder.
         * @since API 1
         */
        public PrintFromStorageBuilder(final Uri fileUri) {
            mFileUri = fileUri;

            if (mFileUri == null || Uri.EMPTY.equals(mFileUri) || "/".equals(mFileUri.getEncodedPath())) {
                // This will be entered in Setting UI after submit
                mFileUri = Uri.EMPTY;
                return;
            }

            // Checks for Uri validity will be done in submit or even later, in the service
            // validateUri here is just for proper Uri encoding
            try {
                mFileUri = validateUri(Source.STORAGE, mFileUri);
            } catch (IllegalArgumentException ignored) {
            }
        }

        /**
         * Creates builder from existing attributes.
         *
         * @param attrs attributes to copy data from
         * @param uri   Uri to file to use for attrs creation
         * @throws NullPointerException if attrs is null
         * @hide it's needed only for internal usage
         * @since API 1
         */
        public PrintFromStorageBuilder(final PrintAttributes attrs, final Uri uri) {
            super(attrs, uri);
        }

        /**
         * Combines all of the attributes in this into a PrintAttributes object.
         *
         * @param caps The print capabilities.
         * @return PrintAttributes object containing all of the attributes.
         * @throws CapabilitiesExceededException if caps does not contain the set attribute value
         * @throws IllegalArgumentException      if the fileUri cannot be found or points to wrong location
         * @since API 1
         */
        @Override
        public PrintAttributes build(final PrintAttributesCaps caps) throws CapabilitiesExceededException {
            validate(caps);

            return new PrintAttributes(this);
        }
    }

    /**
     * Builder for creating {@link PrintAttributes} to request using an image residing on the external server and available by HTTP(S) URL.<br/>
     *
     * The file Uri should be of http:// or https:// scheme
     *
     * @since API 1
     */
    public static class PrintFromHttpBuilder extends PrintCommonAttributesBuilder<PrintFromHttpBuilder> {
        NetworkCredentialsAttributes mNetworkCredentialsAttributes;

        /**
         * Creates builder with default attributes.<br>
         * <br>
         * Color Mode = DEFAULT <br>
         * Duplex = DEFAULT <br>
         * Copies = 1 <br>
         * <br>
         *
         * @param httpUri File URL of the form 'http://' or 'https://'
         * @since API 1
         */
        public PrintFromHttpBuilder(final Uri httpUri) {
            mFileUri = httpUri;
        }

        /**
         * Creates builder from existing attributes.
         *
         * @param attrs attributes to copy data from
         * @param uri   Uri to file to use for attrs creation
         * @throws NullPointerException if attrs is null
         * @hide it's needed only for internal usage
         * @since API 1
         */
        public PrintFromHttpBuilder(final PrintAttributes attrs, final Uri uri) {
            super(attrs, uri);
            mNetworkCredentialsAttributes = attrs.mNetworkCredentialsAttributes;
        }

        /**
         * Sets the network credentials to access source URI
         * @param networkCredentialsAttributes network credentials attributes containing authentication parameters
         * @return builder
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        public PrintFromHttpBuilder setNetworkCredentials(final NetworkCredentialsAttributes networkCredentialsAttributes) {
            mNetworkCredentialsAttributes = networkCredentialsAttributes;
            return this;
        }

        /**
         * Combines all of the attributes in this into a PrintAttributes object.
         *
         * @param caps The print capabilities.
         * @return a PrintAttributes object containing all of the attributes.
         * @throws CapabilitiesExceededException if caps does not contain the set attribute value
         * @throws IllegalArgumentException      if the fileUri cannot be found or points to wrong location
         * @since API 1
         */
        @Override
        public PrintAttributes build(final PrintAttributesCaps caps) throws CapabilitiesExceededException {
            validate(caps);

            return new PrintAttributes(this);
        }
    }

    /**
     * Builder for creating {@link PrintAttributes} to request using an file residing on the external USB storage.<br/>
     *
     * @since API 2
     *
     * @deviceOnly
     */
    @SuppressWarnings("unused")
    public static class PrintFromUsbBuilder extends PrintCommonAttributesBuilder<PrintFromUsbBuilder> {
        /**
         * Creates builder with default attributes.<br>
         * <br>
         * Color Mode = DEFAULT <br>
         * Duplex = DEFAULT <br>
         * Copies = 1 <br>
         * <br>
         *
         * @param usbPath path on USB storage
         * @since API 2
         */
        public PrintFromUsbBuilder(final String usbPath) {
            mFileUri = Uri.parse(usbPath);
        }

        /**
         * Combines all of the attributes in this into a PrintAttributes object.
         *
         * @param caps The print capabilities.
         * @return a PrintAttributes object containing all of the attributes.
         * @throws CapabilitiesExceededException if caps does not contain the set attribute value
         * @throws IllegalArgumentException      if the fileUri cannot be found or points to wrong location
         *
         * @since API 2
         */
        @Override
        public PrintAttributes build(final PrintAttributesCaps caps) throws CapabilitiesExceededException {
            validate(caps);

            return new PrintAttributes(this);
        }
    }

    /**
     * Builder for creating {@link PrintAttributes} to request using an data from provided input stream.<br/>
     *
     * @since API 2
     */
    @SuppressWarnings("unused")
    public static class PrintFromStreamBuilder extends PrintCommonAttributesBuilder<PrintFromStreamBuilder> {
        InputStream mPrintStream;

        /**
         * Creates builder with default attributes.<br>
         * <br>
         * Color Mode = DEFAULT <br>
         * Duplex = DEFAULT <br>
         * Copies = 1 <br>
         * <br>
         *
         * @since API 2
         */
        public PrintFromStreamBuilder(final InputStream inputStream) {
            mPrintStream = inputStream;
        }

        /**
         * Combines all of the attributes in this into a PrintAttributes object.
         *
         * @param caps The print capabilities.
         * @return a PrintAttributes object containing all of the attributes.
         * @throws CapabilitiesExceededException if caps does not contain the set attribute value
         * @throws IllegalArgumentException      if the fileUri cannot be found or points to wrong location
         *
         * @since API 2
         */
        @Override
        public PrintAttributes build(final PrintAttributesCaps caps) throws CapabilitiesExceededException {
            validate(caps);

            if (mDocumentFormat != null &&
                    (mDocumentFormat.equals(DocumentFormat.JPEG))) {
                throw new CapabilitiesExceededException("Supplied DocumentFormat is not supported");
            }

            return new PrintAttributes(this);
        }
    }

    /**
     * @hide trivial
     */
    public String toString() {
        return new StringBuilder().append("[").append("DocumentFormat=").append(((mDocumentFormat != null)?mDocumentFormat.name().toString():"null")).append("]").toString();
    }

    /**
     * Validates files uri (file existence, etc).
     *
     * @param fileUri original file uri
     * @return validate file uri
     * @hide Only for internal use
     */
    public static Uri validateUri(final Source source, final Uri fileUri) {
        return validateUri(source, fileUri, DocumentFormat.AUTO);
    }

    /**
     * Validates files uri (file existence, etc).
     *
     * @param source location for print
     * @param fileUri original file uri
     * @return validate file uri
     * @hide Only for internal use
     */
    public static Uri validateUri(final Source source, final Uri fileUri, final DocumentFormat documentFormat) {
        if (source == Source.STORAGE || source == Source.USB) {
            Uri validatedFileUri;
            String path = fileUri.getPath();

            if (fileUri.getScheme() == null || !fileUri.getScheme().equalsIgnoreCase("file")) {
                SLog.w(Printlet.TAG,
                        "Scheme wasn't provided, but we still could try to find a file");
                path = fileUri.toString();
            }

            if (path == null) {
                SLog.e(Printlet.TAG, "Empty path provided");
                return fileUri;
            }

            validatedFileUri = Uri.parse(path);
            File file = new File(path);
            if (source == Source.STORAGE) {
                // Validate files path / uri
                final File downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                final String sdCardPath = Environment.getExternalStorageDirectory().getPath();

                // If it's not first validation, than it might don't have Download folder prefix
                if (!(file.exists() && file.canRead())) {
                    if(!path.startsWith(sdCardPath)) {
                        if(!path.startsWith(Environment.getDataDirectory().getPath())) {
                            path = downloadFolder + path;
                        }
                        validatedFileUri = Uri.parse(path);
                    }
                }
            }
            file = new File(path);

            if (!file.exists()) {
                throw new IllegalArgumentException("There is no file \"" + file + "\" in the storage");
            }

            if (!file.isFile()) {
                throw new IllegalArgumentException("There is no file \"" + file + "\" in the storage (it's a directory)");
            }

            // Validate supported file format for print
            if (!isSupportedPrintFileFormat(Uri.parse(Uri.encode(path)), documentFormat)) {
                if (documentFormat == DocumentFormat.AUTO) {
                    throw new IllegalArgumentException("File " + file + " is not supported for print.");
                } else {
                    throw new IllegalArgumentException("DocumentFormat doesn't correspond the file type.");
                }
            }

            return validatedFileUri;
        } else if (source == Source.HTTP) {
            // if scheme is present - validate it as URL
            if (!CommonUtility.isValidURL(fileUri)) {
                throw new IllegalArgumentException("File Url is not valid");
            }
        }
        return fileUri;
    }

    private static boolean isSupportedPrintFileFormat(final Uri fileUri, final DocumentFormat documentFormat) {
        boolean isSupported = false;

        if (fileUri != null && documentFormat != null) {
            // get supported file format, prn is proprietary format
            final Map<DocumentFormat, List<String>> extensionCriterion = new HashMap<>();
            extensionCriterion.put(DocumentFormat.AUTO, Arrays.asList("jpeg", "jpg", "jpe", "jfif", "pdf", "tif", "tiff", "txt", "ps", "prn", "pcl"));
            extensionCriterion.put(DocumentFormat.JPEG, Arrays.asList("jpeg", "jpg", "jpe", "jfif"));
            //extensionCriterion.put(DocumentFormat.PDF, Collections.singletonList("pdf", "tif", "tiff", "txt"));
            extensionCriterion.put(DocumentFormat.PDF, Arrays.asList("pdf", "tif", "tiff", "txt"));
            extensionCriterion.put(DocumentFormat.TIFF, Arrays.asList("tif", "tiff"));
            extensionCriterion.put(DocumentFormat.PS, Collections.singletonList("ps"));
            extensionCriterion.put(DocumentFormat.TEXT, Collections.singletonList("txt"));
            extensionCriterion.put(DocumentFormat.PCL5, Arrays.asList("prn", "pcl"));
            extensionCriterion.put(DocumentFormat.PCL6, Arrays.asList("prn", "pcl"));

            String fileName = fileUri.getLastPathSegment();
            if (fileName != null && fileName.length() > 1 &&  fileName.lastIndexOf(".") >= 0) {
                String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

                if (extensionCriterion.get(documentFormat).contains(extension.toLowerCase())) {
                    isSupported = true;
                }
            }
        }
        return isSupported;
    }

}
