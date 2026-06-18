// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.scanner;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.api.CapabilitiesExceededException;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.utils.CommonUtility;
import com.hp.jetadvantage.link.common.utils.FileUtility;
import com.hp.workpath.api.PaperSize;

import java.util.Locale;

/**
 * The sets of attributes for requesting a scan.<br/>
 * An instance of this class is created using one of the builders.
 *
 * @since API 1
 */
public final class ScanAttributes implements Parcelable {
    /**
     * A collection of ColorMode.
     *
     * @since API 1
     */
    public enum ColorMode {
        /**
         * Color mode default from the device.
         *
         * @since API 1
         */
        DEFAULT,

        /**
         * If the job consists of a mixture of Black and Color images (side of a sheet), the device will detect the content of each image and generate appropriate output.
         *
         * @since API 1
         */
        AUTO,

        /**
         * Color
         *
         * @since API 1
         */
        COLOR,

        /**
         * Gray
         *
         * @since API 1
         */
        GRAY,

        /**
         * Black and White.
         *
         * @since API 1
         */
        MONO
    }

    /**
     * A collection of duplex mode
     *
     * @since API 1
     */
    public enum Duplex {
        /**
         * Default duplex settings configured on the device.
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
     * A collection of original Orientation options.
     *
     * @since API 1
     */
    public enum Orientation {
        /**
         * Default original orientation settings configured on the device.
         *
         * @since API 1
         */
        DEFAULT,
        /**
         * Portrait orientation of a page
         *
         * @since API 1
         */
        PORTRAIT,
        /**
         * Landscape orientation of a page
         *
         * @since API 1
         */
        LANDSCAPE,
        /**
         * AutoDetect
         *
         * @since API 5
         */
        AUTO_DETECT
    }

    /**
     * A collection of destinations for scanning.
     *
     * @since API 1
     */
    public enum Destination {
        /**
         * Me : Scanned document is stored in local storage.
         *
         * @since API 1
         */
        ME,

        /**
         * Http : Scanned document is sent to server through http.
         *
         * @since API 1
         */
        HTTP,

        /**
         * Ftp : Scanned document is sent to server through ftp.
         *
         * @since API 1
         */
        FTP,

        /**
         * Network folder : Scanned document is stored in network folder.
         *
         * @since API 1
         */
        NETWORK_FOLDER,

        /**
         * Email : Scanned document is sent by email as attachment.
         *
         * @deviceOnly
         * @since API 1
         */
        EMAIL,

        /**
         * USB : Scanned document is stored in USB
         *
         * @deviceOnly
         * @since API 2
         */
        USB
    }

    /**
     * A collection of document formats for scanning.
     *
     * @since API 1
     */
    public enum DocumentFormat {
        /**
         * Document format default from the device.
         *
         * @since API 1
         */
        DEFAULT,

        /**
         * JPEG file type
         *
         * @since API 1
         */
        JPEG,

        /**
         * PDF file type
         *
         * @since API 1
         */
        PDF,

        /**
         * TIFF
         * Each image (side of a sheet) is provided as a separate TIFF file
         *
         * @since API 1
         */
        TIFF,

        /**
         * MTIFF
         * Multiple TIFF images in a single TIFF file
         *
         * @since API 1
         */
        MTIFF,

        /**
         * PDF with OCR text
         *
         * @since API 1
         */
        OCR_PDF_TEXT_UNDER_IMAGE,

        /**
         * PDF/A with OCR text
         *
         * @since API 1
         */
        OCR_PDF_A_TEXT_UNDER_IMAGE,

        /**
         * XPS with OCR text
         *
         * @since API 1
         */
        OCR_XPS_TEXT_UNDER_IMAGE,

        /**
         * CSV with OCR
         *
         * @since API 1
         */
        OCR_CSV,

        /**
         * HTML with OCR
         *
         * @since API 1
         */
        OCR_HTML,

        /**
         * RTF with OCR
         *
         * @since API 1
         */
        OCR_RTF,

        /**
         * Text with OCR
         *
         * @since API 1
         */
        OCR_TEXT,

        /**
         * Unicode text with OCR
         *
         * @since API 1
         */
        OCR_UNICODE_TEXT,

        /**
         * XML with OCR
         *
         * @since API 1
         */
        OCR_XML,

        /**
         * PDF_A
         * ISO-standardized version of the Portable Document Format (PDF)
         *
         * @since API 1
         */
        PDF_A,

        /**
         * XPS
         * Open xml paper specification
         *
         * @since API 1
         */
        XPS
    }

    /**
     * A collection of resolution types for scanning.
     *
     * @since API 1
     */
    public enum Resolution {
        /**
         * Default value from the device.
         *
         * @since API 1
         */
        DEFAULT,

        DPI_75,
        DPI_100,
        DPI_150,
        DPI_200,
        DPI_240,
        DPI_300,
        DPI_400,
        DPI_500,
        DPI_600;

        public static Resolution valueOf(int dpi) {
            if (dpi == 75) {
                return DPI_75;
            } else if (dpi == 100) {
                return DPI_100;
            } else if (dpi == 150) {
                return DPI_150;
            } else if (dpi == 200) {
                return DPI_200;
            } else if (dpi == 240) {
                return DPI_240;
            } else if (dpi == 300) {
                return DPI_300;
            } else if (dpi == 400) {
                return DPI_400;
            } else if (dpi == 500) {
                return DPI_500;
            } else if (dpi == 600) {
                return DPI_600;
            }
            return DEFAULT;
        }
    }

    /**
     * The scan size (i.e., size of the original) for scanning.<br>
     * <p>
     * This attribute is relative to the orientation parameter of scan.
     *
     * @since API 1
     */
    public enum ScanSize {
        /**
         * Default scanning size from printer.
         * If the printer supports auto-detection, it will be used.
         *
         * @since API 1
         */
        DEFAULT("Default"),

        /**
         * ISO A3 (297mm x 420mm)
         *
         * @since API 1
         */
        A3("A3"),

        /**
         * ISO A4 Rotated (297mm x 210mm)
         *
         * @since API 1
         */
        A4_ROTATE("A4R"),

        /**
         * ISO A4 (210mm x 297mm)
         *
         * @since API 1
         */
        A4("A4"),

        /**
         * ISO A5 Rotated (210mm x 148mm)
         *
         * @since API 1
         */
        A5_ROTATE("A5R"),

        /**
         * ISO/JIS A5 (148mm x 210mm)
         *
         * @since API 1
         */
        A5("A5"),

        /**
         * ISO A6 Rotated (148mm x 105mm)
         *
         * @since API 1
         */
        A6_ROTATE("A6R"),

        /**
         * ISO A6 (105mm x 148mm)
         *
         * @since API 1
         */
        A6("A6"),

        /**
         * ISO B4 (250mm x 353mm)
         *
         * @since API 1
         */
        B4("B4"),

        /**
         * ISO B5 Rotated (250mm x 176mm)
         *
         * @since API 1
         */
        B5_ROTATE("B5R"),

        /**
         * ISO B5 (176mm x 250mm)
         *
         * @since API 1
         */
        B5("B5"),

        /**
         * ISO B6 (125mm x 176mm)
         *
         * @since API 1
         */
        B6("B6"),

        /**
         * JBusiness Card (55mm x 91mm)
         *
         * @since API 1
         */
        BUSINESS_CARD("BusinessCard"),

        /**
         * Letter Rotated (11inch x 8.5inch)
         *
         * @since API 1
         */
        LETTER_ROTATE("LetterR"),

        /**
         * Letter (8.5inch x 11inch)
         *
         * @since API 1
         */
        LETTER("Letter"),

        /**
         * JIS B4 (257mm x 364mm)
         *
         * @since API 1
         */
        JB4("JB4"),

        /**
         * JIS B5 Rotated (257mm x 182mm)
         *
         * @since API 1
         */
        JB5_ROTATE("JB5R"),

        /**
         * JIS B5 (182mm x 257mm)
         *
         * @since API 1
         */
        JB5("JB5"),

        /**
         * JIS B6 (128mm x 182mm)
         *
         * @since API 1
         */
        JB6("JB6"),

        /**
         * Legal (8.5inch x 14inch)
         *
         * @since API 1
         */
        LEGAL("Legal"),

        /**
         * Ledger (11inch x 17inch)
         *
         * @since API 1
         */
        LEDGER("Ledger"),

        /**
         * Executive (7.25inch x 10.5inch)
         *
         * @since API 1
         */
        EXECUTIVE("Executive"),

        /**
         * Statement (5.5inch x 8.5inch)
         *
         * @since API 1
         */
        STATEMENT("Statement"),

        /**
         * Statement Rotated (8.5inch x 5.5inch)
         *
         * @since API 1
         */
        STATEMENT_ROTATE("StatementR"),

        /**
         * Custom scan size
         *
         * @since API 1
         */
        CUSTOM("Custom"),

        /**
         * K8 (270mm x 390mm)
         *
         * @since API 1
         */
        K8("K8"),

        /**
         * K16 (195mm x 270mm)
         *
         * @since API 1
         */
        K16("K16"),

        /**
         * PRC 8K (273mm x 394mm)
         *
         * @since API 1
         */
        PK8("PK8"),

        /**
         * PRC 16K (197mm x 273mm)
         *
         * @since API 1
         */
        PK16("PK16"),

        /**
         * INCH8POINT5X13 (8.5inch x 13inch)
         *
         * @since API 1
         */
        INCH8POINT5X13("Inch8Point5x13"),

        /**
         * INCH12X18 (12inch x 18inch)
         *
         * @since API 1
         */
        INCH12X18("Inch12x18"),

        /**
         * Mixed Letter and Legal
         *
         * @since API 1
         */
        MIXEDLETTERLEGAL("MixedLetterLegal"),

        /**
         * Mixed A3 and A4
         *
         * @since API 1
         */
        MIXEDA3A4("MixedA3A4"),

        /**
         * Auto detected size for scan (Any)
         * A meta-type used to indicate auto-select
         *
         * @since API 1
         */
        AUTO("Any"),

        /**
         * Envelope B5 (176mm x 250mm)
         *
         * @since API 5
         */
        ENVELOPE_B5("EnvelopeB5"),

        /**
         * Envelope 9 (3.875inch x 8.875inch)
         *
         * @since API 5
         */
        ENVELOPE_9("Envelope9"),

        /**
         * Envelope Comm10 (4.125inch x 9.5inch)
         *
         * @since API 5
         */
        ENVELOPE_COMM10("EnvelopeComm10"),

        /**
         * Envelope Monarch (3.875inch x 7.5inch)
         *
         * @since API 5
         */
        ENVELOPE_MONARCH("EnvelopeMonarch"),

        /**
         * ISO C5 (162mm x 229mm)
         *
         * @since API 5
         */
        C5("C5"),

        /**
         * ISO C6 (114mm x 162mm)
         *
         * @since API 5
         */
        C6("C6"),

        /**
         * Envelope DL (110mm x 220mm)
         *
         * @since API 5
         */
        ENVELOPE_DL("EnvelopeDL"),

        /**
         * JIS Chou3 (120mm x 235mm)
         *
         * @since API 5
         */
        JCHOU3("JChou3"),

        /**
         * JIS Chou4 (90mm x 205mm)
         *
         * @since API 5
         */
        JCHOU4("JChou4"),

        /**
         * Unknown Envelope
         *
         * @since API 5
         */
        UNKNOWN_ENVELOP("UnknownEnvelope"),

        /**
         * Japanese Double Postcard (148mm x 200mm)
         *
         * @since API 5
         */
        JDOUBLE_POSTCARD("JDoublePostcard"),

        /**
         * Japanese Postcard (100mm x 148mm)
         *
         * @since API 5
         */
        JPOSTCARD("JPostcard"),

        /**
         * K8_260X368mm (260mm x 368mm)
         *
         * @since API 5
         */
        K8_260X368mm("K8_260x368mm"),

        /**
         * K16_184X260mm (184mm x 260 mm)
         *
         * @since API 5
         */
        K16_184X260mm("K16_184x260mm"),

        /**
         * Mixed Letter and Ledger
         *
         * @since API 5
         */
        MIXEDLETTERLEDGER("MixedLetterLedger"),

        /**
         * Mixed Original
         *
         * @since API 5
         */
        MIXEDORIGINAL("MixedOriginal"),

        /**
         * Oficio (216mm x 340mm)
         *
         * @since API 5
         */
        OFICIO("Oficio"),

        /**
         * RA3 (305mm x 430mm)
         *
         * @since API 5
         */
        RA3("RA3"),

        /**
         * RA4 (215mm x 305mm)
         *
         * @since API 5
         */
        RA4("RA4"),

        /**
         * GENERAL_10X15cm (10cm x 15cm)
         *
         * @since API 5
         */
        GENERAL_10X15cm("General10x15cm"),

        /**
         * GENERAL_12X18in (12inch x 18 inch)
         *
         * @since API 5
         */
        GENERAL_12X18in("General12x18in"),

        /**
         * GENERAL_3X5in (3inch x 5inch)
         *
         * @since API 5
         */
        GENERAL_3X5in("General3x5in"),

        /**
         * GENERAL_4X6in (4inch x 6inch)
         *
         * @since API 5
         */
        GENERAL_4X6in("General4x6in"),

        /**
         * GENERAL_5X7in (5inch x 7inch)
         *
         * @since API 5
         */
        GENERAL_5X7in("General5x7in"),

        /**
         * GENERAL_5X8in (5inch x 8inch)
         *
         * @since API 5
         */
        GENERAL_5X8in("General5x8in"),

        /**
         * GENERAL_8POINT5X13in (8.5inch x 13inch)
         *
         * @since API 5
         */
        GENERAL_8POINT5X13in("General8Point5x13in"),

        /**
         * GENERAL_8POINT5X34in (8.5inch x 34inch)
         *
         * @since API 5
         */
        GENERAL_8POINT5X34in("General8point5x34in"),

        /**
         * GENERAL L (9cm x 13cm)
         *
         * @since API 5
         */
        GENERAL_L_9X13cm("GeneralL9x13cm"),

        /**
         * SRA3 (320mm x 450mm)
         *
         * @since API 5
         */
        SRA3("Sra3"),

        /**
         * SRA4 (225mm x 320mm)
         *
         * @since API 5
         */
        SRA4("Sra4"),

        /**
         * Unknown
         *
         * @since API 5
         */
        UNKNOWN("Unknown");

        private final String scanSize;

        ScanSize(final String scanSize) {
            this.scanSize = scanSize;
        }

        /**
         * @hide trivial
         */
        @Override
        public String toString() {
            return scanSize;
        }

        /**
         * @hide This is for advanced
         */
        public static ScanSize getScanSize(final String jsonStr) {
            if (jsonStr != null) {
                for (ScanSize ss : ScanSize.values()) {
                    if (jsonStr.toLowerCase().equalsIgnoreCase(ss.scanSize.toLowerCase())) {
                        return ss;
                    }
                }
            }

            return ScanSize.DEFAULT;
        }

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
            for (PaperSize ps : PaperSize.values()) {
                if (getScanSize(this.scanSize).name().equals(ps.name())) {
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
            for (PaperSize ps : PaperSize.values()) {
                if (getScanSize(this.scanSize).name().equals(ps.name())) {
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
            for (PaperSize ps : PaperSize.values()) {
                if (getScanSize(this.scanSize).name().equals(ps.name())) {
                    return ps.getUnit();
                }
            }
            return null;
        }
    }

    /**
     * <p>Scan Preview controls for showing a preview of scanned images and requesting confirmation before completing the scan.</p>
     *
     * @deviceOnly
     * @since API 1
     */
    public enum ScanPreview {
        /**
         * Default value
         *
         * @since API 1
         */
        DEFAULT,
        /**
         * Enable Scan Preview.
         * After starting a scan, the device will show the pre-scanned document to display preview
         * to confirm the document from user.
         *
         * @since API 1
         */
        TRUE,
        /**
         * Disable Scan Preview.
         * No preview will be while scanning
         *
         * @since API 1
         */
        FALSE
    }

    /**
     * <p>Background Cleanup controls the level of removing background on a scanned image.</p>
     *
     * @since API 1
     */
    @SuppressWarnings("unused")
    public enum BackgroundCleanup {
        /**
         * Default value
         *
         * @since API 1
         */
        DEFAULT,

        /**
         * No cleanup.
         *
         * @since API 1
         */
        LEVEL_0,

        /**
         * Cleanup level 1 of 8
         *
         * @since API 1
         */
        LEVEL_1,

        /**
         * Cleanup level 2 of 8
         *
         * @since API 1
         */
        LEVEL_2,

        /**
         * Cleanup level 3 of 8
         *
         * @since API 1
         */
        LEVEL_3,

        /**
         * Cleanup level 4 of 8
         *
         * @since API 1
         */
        LEVEL_4,

        /**
         * Cleanup level 5 of 8
         *
         * @since API 1
         */
        LEVEL_5,

        /**
         * Cleanup level 6 of 8
         *
         * @since API 1
         */
        LEVEL_6,

        /**
         * Cleanup level 7 of 8
         *
         * @since API 1
         */
        LEVEL_7,

        /**
         * Most aggressive cleanup.
         *
         * @since API 1
         */
        LEVEL_8
    }

    /**
     * <p>Contrast Adjustment controls the level of contrast correction on a scanned image.</p>
     *
     * @since API 1
     */
    @SuppressWarnings("unused")
    public enum ContrastAdjustment {
        /**
         * Default value
         *
         * @since API 1
         */
        DEFAULT,

        /**
         * Least contrast.
         *
         * @since API 1
         */
        LEVEL_0,

        /**
         * Contrast level 1 of 8
         *
         * @since API 1
         */
        LEVEL_1,

        /**
         * Contrast level 2 of 8
         *
         * @since API 1
         */
        LEVEL_2,

        /**
         * Contrast level 3 of 8
         *
         * @since API 1
         */
        LEVEL_3,

        /**
         * Contrast level 4 of 8
         *
         * @since API 1
         */
        LEVEL_4,

        /**
         * Contrast level 5 of 8
         *
         * @since API 1
         */
        LEVEL_5,

        /**
         * Contrast level 6 of 8
         *
         * @since API 1
         */
        LEVEL_6,

        /**
         * Contrast level 7 of 8
         *
         * @since API 1
         */
        LEVEL_7,

        /**
         * Most contrast.
         *
         * @since API 1
         */
        LEVEL_8
    }

    /**
     * <p>Darkness Adjustment controls the level of darkness correction on a scanned image.</p>
     *
     * @since API 1
     */
    @SuppressWarnings("unused")
    public enum DarknessAdjustment {
        /**
         * Default value
         *
         * @since API 1
         */
        DEFAULT,

        /**
         * Darkest.
         *
         * @since API 1
         */
        LEVEL_0,

        /**
         * Darkness level 1 of 8
         *
         * @since API 1
         */
        LEVEL_1,

        /**
         * Darkness level 2 of 8
         *
         * @since API 1
         */
        LEVEL_2,

        /**
         * Darkness level 3 of 8
         *
         * @since API 1
         */
        LEVEL_3,

        /**
         * Darkness level 4 of 8
         *
         * @since API 1
         */
        LEVEL_4,

        /**
         * Darkness level 5 of 8
         *
         * @since API 1
         */
        LEVEL_5,

        /**
         * Darkness level 6 of 8
         *
         * @since API 1
         */
        LEVEL_6,

        /**
         * Darkness level 7 of 8
         *
         * @since API 1
         */
        LEVEL_7,

        /**
         * Lightest.
         *
         * @since API 1
         */
        LEVEL_8
    }

    /**
     * <p>Blank Image Removal Mode controls the whether to skip blank pages or not.</p>
     *
     * @since API 1
     */
    public enum BlankImageRemovalMode {
        /**
         * Default value
         *
         * @since API 1
         */
        DEFAULT,

        /**
         * Do not remove blank images.
         *
         * @since API 1
         */
        OFF,

        /**
         * Remove blank images.<br/>
         * If all images in the job are removed, the job will fail.<br/>
         * Some older devices will always include the first image in the job, even if it is blank.
         *
         * @since API 1
         */
        ON
    }

    /**
     * <p>Color dropout mode for scanning</p>
     *
     * @since API 1
     */
    @SuppressWarnings("unused")
    public enum ColorDropoutMode {
        /**
         * Default value
         *
         * @since API 1
         */
        DEFAULT,

        /**
         * No dropout.
         *
         * @since API 1
         */
        OFF,

        /**
         * Remove the red color plane.
         *
         * @since API 1
         */
        REMOVE_RED,

        /**
         * Remove the green color plane.
         *
         * @since API 1
         */
        REMOVE_GREEN,

        /**
         * Remove the blue color plane.
         *
         * @since API 1
         */
        REMOVE_BLUE
    }

    /**
     * <p>Crop Mode controls whether the scanned image be cropped to the contents or not .</p>
     *
     * @since API 1
     */
    @SuppressWarnings("unused")
    public enum CropMode {
        /**
         * Default value
         *
         * @since API 1
         */
        DEFAULT,

        /**
         * Do not crop
         *
         * @since API 1
         */
        OFF,

        /**
         * Same as "CROP_TO_CONTENT". Remove white space from top, left, right, and bottom of scanned images.
         * Note that the resulting image will likely not conform to a standard media size.
         * Blank images will not be cropped.
         *
         * @since API 1
         */
        ON,

        /**
         * Crop the image/paper to media size.
         * Note that the resulting image will likely not conform to a standard media size.
         * Blank images will not be cropped.
         *
         * @since API 5
         */
        CROP_TO_PAPER,

        /**
         * Remove white space from top, left, right, and bottom of scanned images.
         * Note that the resulting image will likely not conform to a standard media size.
         * Blank images will not be cropped.
         *
         * @since API 5
         */
        CROP_TO_CONTENT
    }

    /**
     * <p>Progress Dialog Mode controls whether to show device built-in progress dialog or not.</p>
     *
     * @deviceOnly
     * @since API 1
     */
    @SuppressWarnings("unused")
    public enum ProgressDialogMode {
        /**
         * Default value
         *
         * @since API 1
         */
        DEFAULT,

        /**
         * <p>The device will not display built-in scan progress dialogs.<br/>
         * The embedded browser will not be obscured while the device is scanning, allowing the web application to update and display browser content to the walk-up user.</p>
         *
         * @since API 1
         */
        OFF,

        /**
         * <p>The device will display built-in scan progress dialogs.<br/>
         * The embedded browser will be obscured by a built-in scan progress dialog while the device is scanning.</p>
         *
         * @since API 1
         */
        ON
    }

    /**
     * <p>Output Quality controls the result quality of a scanned image.</p>
     *
     * @since API 1
     */
    @SuppressWarnings("unused")
    public enum OutputQuality {
        /**
         * Default value
         *
         * @since API 1
         */
        DEFAULT,

        /**
         * Lowest quality (most loss due to file compression, smallest file size).
         *
         * @since API 1
         */
        LOW,

        /**
         * Medium quality (medium loss due to file compression, medium file size).
         *
         * @since API 1
         */
        MEDIUM,

        /**
         * Highest quality (least loss due to file compression, largest file size).
         *
         * @since API 1
         */
        HIGH
    }

    /**
     * <p>Transmission Mode controls whether a image file will be sent after each page scanned
     * or all files sent when job completed.</p>
     *
     * @since API 1
     */
    @SuppressWarnings("unused")
    public enum TransmissionMode {
        /**
         * Default value
         *
         * @since API 1
         */
        DEFAULT,

        /**
         * Transmit images as they become available (may only be used with HTTP and HTTPS schemes).
         *
         * @since API 1
         */
        IMAGE,

        /**
         * Transmit all images after the job has completed processing.
         *
         * @since API 1
         */
        JOB
    }

    /**
     * <p>Job Assembly Mode controls whether to allow multiple scan segments assembled
     * into a single job when completed.</p>
     *
     * @deviceOnly
     * @since API 1
     */
    @SuppressWarnings("unused")
    public enum JobAssemblyMode {
        /**
         * Default value
         *
         * @since API 1
         */
        DEFAULT,

        /**
         * Scan only one segment per job
         *
         * @since API 1
         */
        OFF,

        /**
         * <p>Scan multiple segments, prompting the walk-up user for another segment at the end of each segment,
         * and assembling all segments into a single job. Scan settings cannot be adjusted between segments.
         * This mode is intended for jobs where the number of sheets exceeds the capacity of the ADF</p>
         *
         * @since API 1
         */
        ON
    }

    /**
     * <p>Sharpness Adjustment controls the level of sharpness correction on a scanned image.</p>
     *
     * @since API 1
     */
    @SuppressWarnings("unused")
    public enum SharpnessAdjustment {
        /**
         * Default value
         *
         * @since API 1
         */
        DEFAULT,

        /**
         * Least sharp.
         *
         * @since API 1
         */
        LEVEL_0,

        /**
         * Sharpness level 1 of 4
         *
         * @since API 1
         */
        LEVEL_1,

        /**
         * Sharpness level 2 of 4
         *
         * @since API 1
         */
        LEVEL_2,

        /**
         * Sharpness level 3 of 4
         *
         * @since API 1
         */
        LEVEL_3,

        /**
         * Sharpest.
         *
         * @since API 1
         */
        LEVEL_4
    }

    /**
     * <p>Enumeration of media weight adjustments.</p>
     *
     * @since API 1
     */
    @SuppressWarnings("unused")
    public enum MediaWeightAdjustment {
        /**
         * Default value
         *
         * @since API 1
         */
        DEFAULT,

        /**
         * Adjust the scanner feed mechanism for typical (normal) paper weights.
         *
         * @since API 1
         */
        NORMAL,

        /**
         * Adjust the scanner feed mechanism for paper weighing more than 100 grams per square meter.
         *
         * @since API 1
         */
        HEAVY
    }

    /**
     * <p>Enumeration of text versus photo optimization settings</p>
     *
     * @since API 1
     */
    @SuppressWarnings("unused")
    public enum TextPhotoOptimization {
        /**
         * Default value
         *
         * @since API 1
         */
        DEFAULT,

        /**
         * Optimize for all text.
         *
         * @since API 1
         */
        TEXT,

        /**
         * Optimize for mostly text with less graphics.
         *
         * @since API 1
         */
        MIXED_0,

        /**
         * Optimize for mostly text with more graphics.
         *
         * @since API 1
         */
        MIXED_1,

        /**
         * Optimize for an equal mix of text and graphics.
         *
         * @since API 1
         */
        MIXED_2,

        /**
         * Optimize for mostly graphics with more text.
         *
         * @since API 1
         */
        MIXED_3,

        /**
         * Optimize for mostly graphics with some text.
         *
         * @since API 1
         */
        MIXED_4,

        /**
         * Optimize for all graphics/pictures.
         *
         * @since API 1
         */
        GRAPHIC
    }

    /**
     * <p>Media Source controls whether device to scan from the ADF or flatbed.</p>
     *
     * @since API 1
     */
    @SuppressWarnings("unused")
    public enum MediaSource {
        /**
         * Default value
         *
         * @since API 1
         */
        DEFAULT,

        /**
         * Directs the device to scan from the automatic document feeder
         *
         * @since API 1
         */
        ADF,

        /**
         * Directs the device to scan from the flatbed (glass)
         *
         * @since API 1
         */
        FLATBED,

        /**
         * The device will automatically select the media source
         *
         * @since API 1
         */
        AUTO
    }

    /**
     * <p>Misfeed Detection Mode controls whether to detect mis feed or not.</p>
     *
     * @since API 1
     */
    @SuppressWarnings("unused")
    public enum MisfeedDetectionMode {
        /**
         * Default value
         *
         * @since API 1
         */
        DEFAULT,

        /**
         * No misfeed detection
         *
         * @since API 1
         */
        OFF,

        /**
         * Detect misfeeds
         *
         * @since API 1
         */
        ON
    }

    /**
     * <p>Enable this feature to scan pages into separate files based on a specified page limit.
     * A page is one side of an original document. JPEG and TIFF have a limit of one page per file.</p>
     *
     * @since API 5
     */
    @SuppressWarnings("unused")
    public enum SplitAttachmentByPage {
        /**
         * Default value
         *
         * @since API 5
         */
        DEFAULT,

        /**
         * Disable split attachment by pages
         *
         * @since API 5
         */
        DISABLED,

        /**
         * Enable split attachment by pages
         *
         * @since API 5
         */
        ENABLED
    }

    /**
     * <p>Margin unit used for erase edge.</p>
     *
     * @since API 5
     */
    @SuppressWarnings("unused")
    public enum EraseMarginUnit {
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
     * <p>Scan capture Mode.</p>
     *
     * @since API 5
     */
    @SuppressWarnings("unused")
    public enum CaptureMode {
        /**
         * Default value
         *
         * @since API 5
         */
        DEFAULT,

        /**
         * Standard
         *
         * @since API 5
         */
        STANDARD,

        /**
         * Standard add pages
         *
         * @since API 5
         */
        STANDARD_ADD_PAGES,

        /**
         * Book capture (A4, Letter Only)
         *
         * @since API 5
         */
        BOOK_CAPTURE,

        /**
         * ID capture prompt both sides (A4, Letter Only)
         *
         * @since API 5
         */
        ID_CAPTURE_PROMPT_BOTH_SIDES,

        /**
         * ID capture prompt back side only (A4, Letter Only)
         *
         * @since API 5
         */
        ID_CAPTURE_PROMPT_BACK_SIDE_ONLY
    }

    /**
     * <p>Automatic tone mode.</p>
     *
     * @since API 5
     */
    @SuppressWarnings("unused")
    public enum AutomaticToneMode {
        /**
         * Default value
         *
         * @since API 5
         */
        DEFAULT,

        /**
         * Enable automatic tone mode
         *
         * @since API 5
         */
        ENABLE,

        /**
         * Disable automatic tone mode
         *
         * @since API 5
         */
        DISABLE
    }

    /**
     * <p>To straighten content that is skewed relative to the page dimensions in the source document.
     * It works in automatic document feeder (ADF) mode.</p>
     *
     * @since API 5
     */
    @SuppressWarnings("unused")
    public enum AutomaticStraightenMode {
        /**
         * Default value
         *
         * @since API 5
         */
        DEFAULT,

        /**
         * Enable automatic straighten mode
         *
         * @since API 5
         */
        ENABLE,

        /**
         * Disable automatic straighten mode
         *
         * @since API 5
         */
        DISABLE
    }

    final int mVersion;

    final ColorMode mColorMode;

    final Duplex mPlex;

    final Destination mDestination;

    final DocumentFormat mDocFormat;

    final Resolution mResolutionType;

    final ScanSize mScanSize;

    final ScanPreview mScanPreview;

    final Orientation mOrientation;

    final String mFileName;

    final String mFolderName;

    final String mCredentialsUsername;

    final String mCredentialsPassword;

    final Uri mUri;

    final int mConnectTimeout;

    final int mReadTimeout;

    final int mMaxRetries;

    final int mRetryInterval;

    final NetworkCredentialsAttributes mNetworkCredentialsAttributes;

    final EmailAttributes mEmailAttributes;

    final SmtpAttributes mSmtpAttributes;

    final BackgroundCleanup mBackgroundCleanup;

    final ContrastAdjustment mContrastAdjustment;

    final DarknessAdjustment mDarknessAdjustment;

    final BlankImageRemovalMode mBlankImageRemovalMode;

    final ColorDropoutMode mColorDropoutMode;

    final CropMode mCropMode;

    final ProgressDialogMode mProgressDialogMode;

    final OutputQuality mOutputQuality;

    final TransmissionMode mTransmissionMode;

    final FileOptionsAttributes mFileOptionsAttributes;

    final JobAssemblyMode mJobAssemblyMode;

    final SharpnessAdjustment mSharpnessAdjustment;

    final MediaWeightAdjustment mMediaWeightAdjustment;

    final TextPhotoOptimization mTextPhotoOptimization;

    final MediaSource mMediaSource;

    final MisfeedDetectionMode mMisfeedDetectionMode;

    final Float mCustomLength;

    final Float mCustomWidth;

    final String mUsbLocation;

    final SplitAttachmentByPage mSplitAttachmentByPage;

    final int mMaxPagesPerAttachment;

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

    final AutomaticToneMode mAutomaticToneMode;

    final AutomaticStraightenMode mAutomaticStraightenMode;

    /**
     * @hide The client should not need to know about the scan parcelable
     * methods
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * @hide The client should not need to know about the scan parcelable
     * methods
     */
    @Override
    public void writeToParcel(final Parcel out, final int flags) {
        // The Sdk version level is used to because changes to this would constitute API level changes. Additionally, this reduces management of
        // <xyz>Attributes versions.

        // Note: The order of these values is CRITICAL. When new values are needed, please add to the end.
        out.writeInt(mVersion);
        out.writeSerializable(mColorMode);
        out.writeSerializable(mDestination);
        out.writeSerializable(mDocFormat);
        out.writeSerializable(mResolutionType);
        out.writeSerializable(mScanSize);
        out.writeSerializable(mPlex);
        out.writeSerializable(mOrientation);
        out.writeSerializable(mScanPreview);
        out.writeString(mFileName);
        out.writeString(mFolderName);
        out.writeString(mCredentialsUsername);
        out.writeString(mCredentialsPassword);

        out.writeParcelable(mUri, 0);
        out.writeInt(mConnectTimeout);
        out.writeInt(mReadTimeout);
        out.writeInt(mMaxRetries);
        out.writeInt(mRetryInterval);

        out.writeParcelable(mNetworkCredentialsAttributes, 0);
        out.writeParcelable(mEmailAttributes, 0);
        out.writeParcelable(mSmtpAttributes, 0);

        out.writeSerializable(mBackgroundCleanup);
        out.writeSerializable(mContrastAdjustment);
        out.writeSerializable(mDarknessAdjustment);
        out.writeSerializable(mBlankImageRemovalMode);
        out.writeSerializable(mColorDropoutMode);
        out.writeSerializable(mCropMode);
        out.writeSerializable(mProgressDialogMode);
        out.writeSerializable(mOutputQuality);
        out.writeSerializable(mTransmissionMode);
        out.writeParcelable(mFileOptionsAttributes, 0);
        out.writeSerializable(mJobAssemblyMode);
        out.writeSerializable(mSharpnessAdjustment);

        out.writeSerializable(mMediaWeightAdjustment);
        out.writeSerializable(mTextPhotoOptimization);
        out.writeSerializable(mMediaSource);
        out.writeSerializable(mMisfeedDetectionMode);
        out.writeSerializable(mCustomLength);
        out.writeSerializable(mCustomWidth);

        out.writeString(mUsbLocation);

        out.writeSerializable(mSplitAttachmentByPage);
        out.writeInt(mMaxPagesPerAttachment);
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
        out.writeSerializable(mAutomaticToneMode);
        out.writeSerializable(mAutomaticStraightenMode);
        // Add new values here
    }

    /**
     * @hide The client should not need to know about the scan parcelable
     * methods
     */
    public static final Parcelable.Creator<ScanAttributes> CREATOR = new Parcelable.Creator<ScanAttributes>() {
        public ScanAttributes createFromParcel(final Parcel in) {
            return new ScanAttributes(in);
        }

        public ScanAttributes[] newArray(final int size) {
            return new ScanAttributes[size];
        }
    };

    @SuppressLint("RestrictedApi")
    private ScanAttributes(final Parcel in) {
        // The version is used to support compatibility. It must be the first in the parcel. If a new attribute is added, then logic needs to be added
        // to the end of this constructor. The constructor will compare the version passed with the version supported and handle the compatibility. This
        // means that if the version passed is less than the version of the reader, then the reader will read all values up to the version passed.

        // Note: The order of these values is CRITICAL. When new values are needed, please add to the end.
        mVersion = in.readInt();
        Preconditions.checkArgument(mVersion >= Sdk.VERSION_LEVEL.ONE);

        mColorMode = (ColorMode) in.readSerializable();
        mDestination = (Destination) in.readSerializable();
        mDocFormat = (DocumentFormat) in.readSerializable();
        mResolutionType = (Resolution) in.readSerializable();
        mScanSize = (ScanSize) in.readSerializable();
        mPlex = (Duplex) in.readSerializable();
        mOrientation = (Orientation) in.readSerializable();
        mScanPreview = (ScanPreview) in.readSerializable();
        mFileName = in.readString();
        mFolderName = in.readString();
        mCredentialsUsername = in.readString();
        mCredentialsPassword = in.readString();

        mUri = in.readParcelable(Uri.class.getClassLoader());
        mConnectTimeout = in.readInt();
        mReadTimeout = in.readInt();
        mMaxRetries = in.readInt();
        mRetryInterval = in.readInt();

        mNetworkCredentialsAttributes = in.readParcelable(NetworkCredentialsAttributes.class.getClassLoader());
        mEmailAttributes = in.readParcelable(EmailAttributes.class.getClassLoader());
        mSmtpAttributes = in.readParcelable(SmtpAttributes.class.getClassLoader());

        mBackgroundCleanup = (BackgroundCleanup) in.readSerializable();
        mContrastAdjustment = (ContrastAdjustment) in.readSerializable();
        mDarknessAdjustment = (DarknessAdjustment) in.readSerializable();
        mBlankImageRemovalMode = (BlankImageRemovalMode) in.readSerializable();
        mColorDropoutMode = (ColorDropoutMode) in.readSerializable();
        mCropMode = (CropMode) in.readSerializable();
        mProgressDialogMode = (ProgressDialogMode) in.readSerializable();
        mOutputQuality = (OutputQuality) in.readSerializable();
        mTransmissionMode = (TransmissionMode) in.readSerializable();
        mFileOptionsAttributes = in.readParcelable(FileOptionsAttributes.class.getClassLoader());
        mJobAssemblyMode = (JobAssemblyMode) in.readSerializable();
        mSharpnessAdjustment = (SharpnessAdjustment) in.readSerializable();
        mMediaWeightAdjustment = (MediaWeightAdjustment) in.readSerializable();
        mTextPhotoOptimization = (TextPhotoOptimization) in.readSerializable();
        mMediaSource = (MediaSource) in.readSerializable();
        mMisfeedDetectionMode = (MisfeedDetectionMode) in.readSerializable();

        mCustomLength = (Float) in.readSerializable();
        mCustomWidth = (Float) in.readSerializable();

        if (mVersion >= Sdk.VERSION_LEVEL.TWO) {
            mUsbLocation = in.readString();
        } else {
            mUsbLocation = null;
        }

        if (mVersion >= Sdk.VERSION_LEVEL.SIX) {
            mSplitAttachmentByPage = (SplitAttachmentByPage) in.readSerializable();
            mMaxPagesPerAttachment = in.readInt();
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
            mAutomaticToneMode = (AutomaticToneMode) in.readSerializable();
            mAutomaticStraightenMode = (AutomaticStraightenMode) in.readSerializable();
        } else {
            mSplitAttachmentByPage = SplitAttachmentByPage.DEFAULT;
            mMaxPagesPerAttachment = 1;
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
            mAutomaticToneMode = AutomaticToneMode.DEFAULT;
            mAutomaticStraightenMode = AutomaticStraightenMode.DEFAULT;
        }
        // Add new values here
    }

    private ScanAttributes(final ScanAttributesBuilder builder) {
        mVersion = Sdk.VERSION.LEVEL;
        mColorMode = builder.mColorMode;
        mPlex = builder.mPlex;
        mDestination = builder.mDestination;
        mDocFormat = builder.mDocFormat;
        mResolutionType = builder.mResolutionType;
        mScanSize = builder.mScanSize;
        mOrientation = builder.mOrientation;
        mScanPreview = builder.mScanPreview;
        mFileName = builder.mFileName;
        mCredentialsUsername = builder.mCredentialsUsername;
        mCredentialsPassword = builder.mCredentialsPassword;
        mBackgroundCleanup = builder.mBackgroundCleanup;
        mContrastAdjustment = builder.mContrastAdjustment;
        mDarknessAdjustment = builder.mDarknessAdjustment;
        mBlankImageRemovalMode = builder.mBlankImageRemovalMode;
        mColorDropoutMode  = builder.mColorDropoutMode;
        mCropMode = builder.mCropMode;
        mProgressDialogMode = builder.mProgressDialogMode;
        mOutputQuality = builder.mOutputQuality;
        mTransmissionMode = builder.mTransmissionMode;
        mFileOptionsAttributes = builder.mFileOptionsAttributes;
        mJobAssemblyMode = builder.mJobAssemblyMode;
        mSharpnessAdjustment = builder.mSharpnessAdjustment;
        mMediaWeightAdjustment = builder.mMediaWeightAdjustment;
        mTextPhotoOptimization = builder.mTextPhotoOptimization;
        mMediaSource = builder.mMediaSource;
        mMisfeedDetectionMode = builder.mMisfeedDetectionMode;
        mCustomLength = builder.mCustomLength;
        mCustomWidth = builder.mCustomWidth;

        // ScanTicket3
        mSplitAttachmentByPage = builder.mSplitAttachmentByPage;
        mMaxPagesPerAttachment = builder.mMaxPagesPerAttachment;
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
        mAutomaticToneMode = builder.mAutomaticToneMode;
        mAutomaticStraightenMode = builder.mAutomaticStraightenMode;

        if (builder instanceof MeBuilder) {
            MeBuilder meBuilder = (MeBuilder) builder;
            mFolderName = meBuilder.mFolderName;
        } else {
            mFolderName = null;
        }

        if (builder instanceof ScanToUriBuilder) {
            ScanToUriBuilder uriBuilder = (ScanToUriBuilder) builder;
            mUri = uriBuilder.mDestinationUri;
            mNetworkCredentialsAttributes = uriBuilder.mNetworkCredentialsAttributes;
            mConnectTimeout = uriBuilder.mConnectTimeout;
            mReadTimeout = uriBuilder.mReadTimeout;
            mMaxRetries = uriBuilder.mMaxRetries;
            mRetryInterval = uriBuilder.mRetryInterval;
        } else {
            mUri = null;
            mNetworkCredentialsAttributes = null;
            mConnectTimeout = 0;
            mReadTimeout = 0;
            mMaxRetries = 0;
            mRetryInterval = 0;
        }

        if (builder instanceof EmailBuilder) {
            EmailBuilder emailBuilder = (EmailBuilder) builder;
            mEmailAttributes = emailBuilder.mEmailAttributes;
            mSmtpAttributes = emailBuilder.mSmtpAttributes;
        } else {
            mEmailAttributes = null;
            mSmtpAttributes = null;
        }

        if (builder instanceof UsbBuilder) {
            mUsbLocation = ((UsbBuilder) builder).mUsbLocation;
        } else {
            mUsbLocation = null;
        }
    }

    /**
     * Base builder for common scan attributes
     *
     * @since API 1
     */
    public static abstract class ScanAttributesBuilder<T extends ScanAttributesBuilder<T>> {

        ColorMode mColorMode = ColorMode.DEFAULT;

        Duplex mPlex = Duplex.DEFAULT;

        @SuppressWarnings("CanBeFinal")
        Destination mDestination;

        DocumentFormat mDocFormat = DocumentFormat.DEFAULT;

        ScanSize mScanSize = ScanSize.DEFAULT;

        Resolution mResolutionType = Resolution.DEFAULT;

        Orientation mOrientation = Orientation.DEFAULT;

        ScanPreview mScanPreview = ScanPreview.DEFAULT;

        String mFileName = null;

        String mCredentialsUsername = null;

        String mCredentialsPassword = null;

        BackgroundCleanup mBackgroundCleanup = BackgroundCleanup.DEFAULT;

        ContrastAdjustment mContrastAdjustment = ContrastAdjustment.DEFAULT;

        DarknessAdjustment mDarknessAdjustment = DarknessAdjustment.DEFAULT;

        BlankImageRemovalMode mBlankImageRemovalMode = BlankImageRemovalMode.DEFAULT;

        ColorDropoutMode mColorDropoutMode = ColorDropoutMode.DEFAULT;

        CropMode mCropMode = CropMode.DEFAULT;

        ProgressDialogMode mProgressDialogMode = ProgressDialogMode.DEFAULT;

        OutputQuality mOutputQuality = OutputQuality.DEFAULT;

        TransmissionMode mTransmissionMode = TransmissionMode.DEFAULT;

        FileOptionsAttributes mFileOptionsAttributes = null;

        JobAssemblyMode mJobAssemblyMode = JobAssemblyMode.DEFAULT;

        SharpnessAdjustment mSharpnessAdjustment = SharpnessAdjustment.DEFAULT;

        MediaWeightAdjustment mMediaWeightAdjustment = MediaWeightAdjustment.DEFAULT;

        TextPhotoOptimization mTextPhotoOptimization = TextPhotoOptimization.DEFAULT;

        MediaSource mMediaSource = MediaSource.DEFAULT;

        MisfeedDetectionMode mMisfeedDetectionMode = MisfeedDetectionMode.DEFAULT;

        float mCustomLength = 0f;

        float mCustomWidth = 0f;

        SplitAttachmentByPage mSplitAttachmentByPage = SplitAttachmentByPage.DEFAULT;

        int mMaxPagesPerAttachment = 0;

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

        AutomaticToneMode mAutomaticToneMode = AutomaticToneMode.DEFAULT;

        AutomaticStraightenMode mAutomaticStraightenMode = AutomaticStraightenMode.DEFAULT;

        ScanAttributesBuilder() {

        }

        /**
         * Specifies color mode.
         *
         * @param colorMode The set of colors
         * @return this builder for method chaining.
         * @throws NullPointerException if colorMode is null
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setColorMode(@NonNull final ColorMode colorMode) {
            mColorMode = Preconditions.checkNotNull(colorMode);
            return (T) this;
        }

        /**
         * Sets Duplex
         *
         * @param duplex Book or Flip
         * @return this builder for method chaining.
         * @throws NullPointerException if duplex is null
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setDuplex(@NonNull final Duplex duplex) {
            mPlex = Preconditions.checkNotNull(duplex);
            return (T) this;
        }

        /**
         * Sets document format
         * @param docFormat The output format of the scanned image(s).
         * @return this builder for method chaining.
         * @throws NullPointerException if docFormat is null
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setDocumentFormat(@NonNull final DocumentFormat docFormat) {
            mDocFormat = Preconditions.checkNotNull(docFormat);
            return (T) this;
        }

        /**
         * Specifies the size of the scanned image.
         *
         * @param scanSize The size of the image to be produced from the scanned
         *                 document / image.
         * @return this builder for method chaining.
         * @throws NullPointerException if scanSize is null.
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setScanSize(@NonNull final ScanSize scanSize) {
            mScanSize = Preconditions.checkNotNull(scanSize);
            return (T) this;
        }

        /**
         * Indicates the resolution for the scanned image(s).
         *
         * @param res The resolution for the scanned image(s)
         * @return this builder for method chaining.
         * @throws NullPointerException if res is null.
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setResolution(@NonNull final Resolution res) {
            mResolutionType = Preconditions.checkNotNull(res);
            return (T) this;
        }

        /**
         * Specifies the set of original orientation
         *
         * @param orientation The original orientation of document for scanning
         * @return this builder for method chaining.
         * @throws NullPointerException if orientation is null.
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setOrientation(@NonNull final Orientation orientation) {
            mOrientation = Preconditions.checkNotNull(orientation);
            return (T) this;
        }

        /**
         * Sets the scan preview param. This parameter forces showing a preview of scanned image
         *
         * @param scanPreview Scan preview flag value
         * @return this builder for method chaining
         * @throws NullPointerException is scanPreview is null;
         * @since API 1
         * @deviceOnly
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setScanPreview(@NonNull final ScanPreview scanPreview) {
            mScanPreview = Preconditions.checkNotNull(scanPreview);
            return (T) this;
        }

        /**
         * Sets the scan base file name
         *
         * @param fileName base filename for scanned images
         * @return this builder for method chaining
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        @NonNull
        public T setFileName(@Nullable final String fileName){
            mFileName = fileName;
            return (T) this;
        }

        /**
         * Sets the credentials for admin user name and password which configured on device to be allowed to run scanning
         *
         * @param userName privileged user name
         * @param password privileged user password
         * @return this builder for method chaining
         * @throws NullPointerException if username or password are null.
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setCredentials(@NonNull final String userName, @NonNull final String password) {
            mCredentialsUsername = Preconditions.checkNotNull(userName);
            mCredentialsPassword = Preconditions.checkNotNull(password);
            return (T) this;
        }

        /**
         * Sets the background cleanup level.
         *
         * @param backgroundCleanup level
         * @return this builder for method chaining.
         * @throws NullPointerException if backgroundCleanup is null.
         * @since API 1
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
         * @since API 1
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
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setDarknessAdjustment(@NonNull final DarknessAdjustment darknessAdjustment) {
            mDarknessAdjustment = Preconditions.checkNotNull(darknessAdjustment);
            return (T) this;
        }

        /**
         * Sets the blank image removal mode.
         *
         * @param blankImageRemovalMode mode
         * @return this builder for method chaining.
         * @throws NullPointerException if blankImageRemovalMode is null.
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setBlankImageRemovalMode(@NonNull final BlankImageRemovalMode blankImageRemovalMode) {
            mBlankImageRemovalMode = Preconditions.checkNotNull(blankImageRemovalMode);
            return (T) this;
        }

        /**
         * Sets the color dropout mode.
         *
         * @param colorDropoutMode mode
         * @return this builder for method chaining.
         * @throws NullPointerException if colorDropoutMode is null.
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setColorDropoutMode(@NonNull final ColorDropoutMode colorDropoutMode) {
            mColorDropoutMode = Preconditions.checkNotNull(colorDropoutMode);
            return (T) this;
        }

        /**
         * Sets the crop mode.
         *
         * @param cropMode mode
         * @return this builder for method chaining.
         * @throws NullPointerException if cropMode is null.
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setCropMode(@NonNull final CropMode cropMode) {
            mCropMode = Preconditions.checkNotNull(cropMode);
            return (T) this;
        }

        /**
         * Sets the progress dialog mode. If TRUE, system job dialog will show while scanning.
         *
         * @param progressDialogMode mode
         * @return this builder for method chaining.
         * @throws NullPointerException if progressDialogMode is null.
         * @since API 1
         * @deviceOnly
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setProgressDialogMode(@NonNull final ProgressDialogMode progressDialogMode) {
            mProgressDialogMode = Preconditions.checkNotNull(progressDialogMode);
            return (T) this;
        }

        /**
         * Sets the output quality.
         *
         * @param outputQuality quality
         * @return this builder for method chaining.
         * @throws NullPointerException if outputQuality is null.
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setOutputQuality(@NonNull final OutputQuality outputQuality) {
            mOutputQuality = Preconditions.checkNotNull(outputQuality);
            return (T) this;
        }

        /**
         * Sets the transmission mode.
         *
         * @param transmissionMode mode
         * @return this builder for method chaining.
         * @throws NullPointerException if transmissionMode is null.
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setTransmissionMode(@NonNull final TransmissionMode transmissionMode) {
            mTransmissionMode = Preconditions.checkNotNull(transmissionMode);
            return (T) this;
        }

        /**
         * Sets the file options attributes.
         *
         * @param fileOptionsAttributes attributes
         * @return this builder for method chaining.
         * @throws NullPointerException if fileOptionsAttributes is null.
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setFileOptionsAttributes(@NonNull final FileOptionsAttributes fileOptionsAttributes) {
            mFileOptionsAttributes = Preconditions.checkNotNull(fileOptionsAttributes);
            return (T) this;
        }

        /**
         * Sets the job assembly mode.
         *
         * @param jobAssemblyMode mode
         * @return this builder for method chaining.
         * @throws NullPointerException if jobAssemblyMode is null.
         * @since API 1
         * @deviceOnly
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setJobAssemblyMode(@NonNull final JobAssemblyMode jobAssemblyMode) {
            mJobAssemblyMode = Preconditions.checkNotNull(jobAssemblyMode);
            return (T) this;
        }

        /**
         * Sets the sharpness adjustment.
         *
         * @param sharpnessAdjustment mode
         * @return this builder for method chaining.
         * @throws NullPointerException if sharpnessAdjustment is null.
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setSharpnessAdjustment(@NonNull final SharpnessAdjustment sharpnessAdjustment) {
            mSharpnessAdjustment = Preconditions.checkNotNull(sharpnessAdjustment);
            return (T) this;
        }

        /**
         * Sets the media weight adjustment.
         *
         * @param mediaWeightAdjustment mode
         * @return this builder for method chaining.
         * @throws NullPointerException if mediaWeightAdjustment is null.
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setMediaWeightAdjustment(@NonNull final MediaWeightAdjustment mediaWeightAdjustment) {
            mMediaWeightAdjustment = Preconditions.checkNotNull(mediaWeightAdjustment);
            return (T) this;
        }

        /**
         * Sets the text photo optimization.
         *
         * @param textPhotoOptimization mode
         * @return this builder for method chaining.
         * @throws NullPointerException if textPhotoOptimization is null.
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setTextPhotoOptimization(@NonNull final TextPhotoOptimization textPhotoOptimization) {
            mTextPhotoOptimization = Preconditions.checkNotNull(textPhotoOptimization);
            return (T) this;
        }

	/**
         * Sets the media source.
         *
         * @param mediaSource scan source
         * @return this builder for method chaining.
         * @throws NullPointerException if mediaSource is null.
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setMediaSource(@NonNull final MediaSource mediaSource) {
            mMediaSource = Preconditions.checkNotNull(mediaSource);
            return (T) this;
        }

        /**
         * Sets the misfeed detection mode.
         *
         * @param misfeedDetectionMode option for misfeed detection
         * @return this builder for method chaining.
         * @throws NullPointerException if misfeedDetectionMode is null.
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public T setMisfeedDetectionMode(@NonNull final MisfeedDetectionMode misfeedDetectionMode) {
            mMisfeedDetectionMode = Preconditions.checkNotNull(misfeedDetectionMode);
            return (T) this;
        }

        /**
         * Sets the custom length.
         *
         * @param customLength in inches
         * @return this builder for method chaining.
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        @NonNull
        public T setCustomLength(final float customLength) {
            mCustomLength = customLength;
            return (T) this;
        }

        /**
         * Sets the custom width.
         *
         * @param customWidth in inches
         * @return this builder for method chaining.
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        @NonNull
        public T setCustomWidth(final float customWidth) {
            mCustomWidth = customWidth;
            return (T) this;
        }

        /**
         * SplitAttachmentByPages
         *
         * @param splitAttachmentByPage mode
         * @return this builder for method chaining.
         * @since API 5
         */
        @SuppressWarnings({"unused", "unchecked"})
        @NonNull
        public T setSplitAttachmentByPage(final SplitAttachmentByPage splitAttachmentByPage) {
            mSplitAttachmentByPage = splitAttachmentByPage;
            return (T) this;
        }

        /**
         * MaxPagesPerAttachment
         *
         * @param maxPagesPerAttachment mode
         * @return this builder for method chaining.
         * @since API 5
         */
        @SuppressWarnings({"unused", "unchecked"})
        @NonNull
        public T setMaxPagesPerAttachment(final int maxPagesPerAttachment) {
            mMaxPagesPerAttachment = maxPagesPerAttachment;
            return (T) this;
        }

        /**
         * EraseMarginUnits
         *
         * @param eraseMarginUnit mode
         * @return this builder for method chaining.
         * @since API 5
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
         * @since API 5
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
         * @since API 5
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

        /**
         * CaptureMode mode
         *
         * @param captureMode mode
         * @return this builder for method chaining.
         * @since API 5
         */
        @SuppressWarnings({"unused", "unchecked"})
        @NonNull
        public T setCaptureMode(final CaptureMode captureMode) {
            mCaptureMode = captureMode;
            return (T) this;
        }

        /**
         * AutomaticToneMode mode
         *
         * @param automaticToneMode mode
         * @return this builder for method chaining.
         * @since API 5
         */
        @SuppressWarnings({"unused", "unchecked"})
        @NonNull
        public T setAutomaticToneMode(final AutomaticToneMode automaticToneMode) {
            mAutomaticToneMode = automaticToneMode;
            return (T) this;
        }

        /**
         * AutomaticStraightenMode mode
         *
         * @param automaticStraightenMode mode
         * @return this builder for method chaining.
         * @since API 5
         */
        @SuppressWarnings({"unused", "unchecked"})
        @NonNull
        public T setAutomaticStraightenMode(final AutomaticStraightenMode automaticStraightenMode) {
            mAutomaticStraightenMode = automaticStraightenMode;
            return (T) this;
        }

        public abstract ScanAttributes build(final ScanAttributesCaps caps) throws CapabilitiesExceededException;

        @SuppressWarnings({"ResultOfMethodCallIgnored"})
        @SuppressLint("RestrictedApi")
        void validate(final ScanAttributesCaps caps) throws CapabilitiesExceededException {
            if (caps == null) {
                throw new CapabilitiesExceededException("ScanAttributesCapabilities is required");
            }

            Preconditions.checkNotNull(caps.mCapsCreator);

            // Validating versus caps and throw exception if can't resolve
            if (!caps.getScanSizeList().contains(mScanSize)) {
                throw new CapabilitiesExceededException("Supplied scan size not supported");
            }

            if (mScanSize.equals(ScanSize.CUSTOM)) {
                if (!caps.getCustomLengthRange().validate(mCustomLength)) {
                    throw new CapabilitiesExceededException("Supplied custom length value is not in range " + caps.getCustomLengthRange());
                }
            }

            if (mScanSize.equals(ScanSize.CUSTOM)) {
                if (!caps.getCustomWidthRange().validate(mCustomWidth)) {
                    throw new CapabilitiesExceededException("Supplied custom width value is not in range " + caps.getCustomWidthRange());
                }
            }

            if (!caps.getColorModeList().contains(mColorMode)) {
                throw new CapabilitiesExceededException("Supplied color mode not supported");
            }

            if (!caps.getOrientationList().contains(mOrientation)) {
                throw new CapabilitiesExceededException("Supplied original orientation not supported");
            }

            if (!caps.getDuplexList().contains(mPlex)) {
                throw new CapabilitiesExceededException("Supplied Duplex not supported");
            }

            if (!caps.getDocumentFormatList(mDestination).contains(mDocFormat)) {
                throw new CapabilitiesExceededException("Supplied document format not supported");
            }

            if (!caps.getResolutionList().contains(mResolutionType)) {
                throw new CapabilitiesExceededException("Supplied resolution not supported");
            }

            if (!caps.getScanPreviewList().contains(mScanPreview)) {
                throw new CapabilitiesExceededException("Supplied scan preview not supported");
            }

            if (!TextUtils.isEmpty(mFileName) && !isFilenameValid(mFileName)) {
                throw new CapabilitiesExceededException("File name is not valid.");
            }

            if (!caps.getBackgroundCleanupList().contains(mBackgroundCleanup)) {
                throw new CapabilitiesExceededException("Supplied background cleanup not supported");
            }

            if (!caps.getContrastAdjustmentList().contains(mContrastAdjustment)) {
                throw new CapabilitiesExceededException("Supplied contrast adjustment not supported");
            }

            if (!caps.getDarknessAdjustmentList().contains(mDarknessAdjustment)) {
                throw new CapabilitiesExceededException("Supplied darkness adjustment not supported");
            }

            if (!caps.getBlankImageRemovalModeList().contains(mBlankImageRemovalMode)) {
                throw new CapabilitiesExceededException("Supplied blank image removal mode not supported");
            }

            if (!caps.getColorDropoutModeList().contains(mColorDropoutMode)) {
                throw new CapabilitiesExceededException("Supplied color dropout mode not supported");
            }

            if (!caps.getCropModeList().contains(mCropMode)) {
                throw new CapabilitiesExceededException("Supplied crop mode not supported");
            }

            if (!caps.getProgressDialogModeList().contains(mProgressDialogMode)) {
                throw new CapabilitiesExceededException("Supplied progress dialog mode not supported");
            }

            if (!caps.getOutputQualityList().contains(mOutputQuality)) {
                throw new CapabilitiesExceededException("Supplied output quality not supported");
            }

            if (!caps.getTransmissionModeList().contains(mTransmissionMode)) {
                throw new CapabilitiesExceededException("Supplied transmission mode not supported");
            }

            if (!caps.getJobAssemblyModeList().contains(mJobAssemblyMode)) {
                throw new CapabilitiesExceededException("Supplied job assembly mode not supported");
            }

            if (!caps.getSharpnessAdjustmentList().contains(mSharpnessAdjustment)) {
                throw new CapabilitiesExceededException("Supplied sharpness adjustment not supported");
            }

            if (!caps.getMediaWeightAdjustmentList().contains(mMediaWeightAdjustment)) {
                throw new CapabilitiesExceededException("Supplied media weight adjustment not supported");
            }

            if (!caps.getTextPhotoOptimizationList().contains(mTextPhotoOptimization)) {
                throw new CapabilitiesExceededException("Supplied text/photo optimization not supported");
            }

            if (!caps.getMediaSourceList().contains(mMediaSource)) {
                throw new CapabilitiesExceededException("Supplied media source not supported");
            }

            if (!caps.getMisfeedDetectionModeList().contains(mMisfeedDetectionMode)) {
                throw new CapabilitiesExceededException("Supplied misfeed detection mode not supported");
            }


            if (!caps.getSplitAttachmentByPageList().contains(mSplitAttachmentByPage)) {
                throw new CapabilitiesExceededException("Supplied split attachment by page mode not supported");
            }

            if (mSplitAttachmentByPage.equals(SplitAttachmentByPage.ENABLED)) {
                if (!caps.getMaxPagesPerAttachmentRange().validate(mMaxPagesPerAttachment)) {
                    throw new CapabilitiesExceededException("Supplied max pages per attachment value is not in range " + caps.getCustomWidthRange());
                }
            }

            if (!caps.getEraseMarginUnitList().contains(mEraseMarginUnit)) {
                throw new CapabilitiesExceededException("Supplied erase margin unit not supported");
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
                throw new CapabilitiesExceededException("Supplied capture mode not supported");
            }

            if (!caps.getAutomaticToneModeList().contains(mAutomaticToneMode)) {
                throw new CapabilitiesExceededException("Supplied automatic tone mode not supported");
            }

            if (!caps.getAutomaticStraightenModeList().contains(mAutomaticStraightenMode)) {
                throw new CapabilitiesExceededException("Supplied automatic straighten mode not supported");
            }

            // need to validate that a pair ColorMode-DocumentFormat is supported
            if (mColorMode != ColorMode.DEFAULT
                    && !caps.getDocumentFormatsByColorMode().get(mColorMode).contains(mDocFormat)) {
                throw new CapabilitiesExceededException("Document format " + mDocFormat
                        + " is not supported for Color mode " + mColorMode);
            }

            // TODO add exception
        }
    }

    /**
     * Builder for creating {@link ScanAttributes} configured to send scanned images back to the client.
     *
     * @since API 1
     */
    public static class MeBuilder extends ScanAttributesBuilder<MeBuilder> {

        String mFolderName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath()
                                + "/Scan/";
        /**
         * Constructor to create a new ScanToMe Builder for sending scanned images back to the
         * application with default attributes.<br>
         * <br>
         * Color Mode = DEFAULT <br>
         * Document Format = DEFAULT <br>
         * Scan Size = DEFAULT <br>
         * Resolution = DEFAULT <br>
         * <br>
         *
         * @since API 1
         */
        @SuppressWarnings({"unused"})
        public MeBuilder() {
            this.mDestination = Destination.ME;
        }

        /**
         * Sets the folder name to be stored after scanning
         *
         * @param folderName base folder for scanned images
         * @return this builder for method chaining
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        @NonNull
        public MeBuilder setFolderName(@Nullable final String folderName){
            if(!TextUtils.isEmpty(folderName)) {
                mFolderName = folderName;
            }
            return this;
        }

        /**
         * Combines all of the attributes in this into a ScanAttributes object.
         *
         * @param caps The scan capabilities.
         * @return ScanAttributes object containing all of the attributes.
         * @throws CapabilitiesExceededException if caps does not contain the set attribute value.
         * @since API 1
         */
        @Override
        @SuppressWarnings({"unused"})
        @NonNull
        public ScanAttributes build(@NonNull final ScanAttributesCaps caps) throws CapabilitiesExceededException {
            validate(caps);

            String externalStorage = Environment.getExternalStorageDirectory().getPath();
            String dataDirectory = Environment.getDataDirectory().getPath();
            if (!TextUtils.isEmpty(mFolderName)
                    && (!mFolderName.startsWith(externalStorage) && !mFolderName.startsWith(dataDirectory))) {
                throw new CapabilitiesExceededException("Folder path is invalid.");
            }
            return new ScanAttributes(this);
        }
    }

    /**
     * Builder for creating {@link ScanAttributes} configured to send scanned images to the external HTTP(S) server.
     *
     * @since API 1
     */
    public static abstract class ScanToUriBuilder<T extends ScanToUriBuilder<T>>
            extends ScanAttributesBuilder<T> {
        private static final int MINIMUM_TIMEOUT = 1;
        private static final int MAXIMUM_TIMEOUT = 300;
        private static final int MINIMUM_RETRIES = 0;
        private static final int MAXIMUM_RETRIES = 5;

        Uri mDestinationUri;
        NetworkCredentialsAttributes mNetworkCredentialsAttributes;
        int mConnectTimeout = 60;
        int mReadTimeout = 60;
        int mMaxRetries = 0;
        int mRetryInterval = 60;

        @SuppressWarnings({"unused"})
        ScanToUriBuilder(final Uri uri) {
            this.mDestinationUri = uri;
        }

        /**
         * Sets the network credentials to access destination URI
         * @param networkCredentialsAttributes network credentials attributes containing authentication parameters
         * @return builder
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        @NonNull
        public T setNetworkCredentials(@Nullable final NetworkCredentialsAttributes networkCredentialsAttributes) {
            mNetworkCredentialsAttributes = networkCredentialsAttributes;
            return (T) this;
        }

        /**
         * Sets the socket connect timeout - how much time to wait before socket will be opened when sending scanned file<br>
         * Default value is 60 seconds.<br>
         * Range: 1 ~ 300 seconds.<br>
         * @param connectTimeout value in seconds
         * @return builder
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        @NonNull
        public T setConnectTimeout(@IntRange(from=MINIMUM_TIMEOUT, to=MAXIMUM_TIMEOUT) final int connectTimeout) {
            mConnectTimeout = connectTimeout;
            return (T) this;
        }

        /**
         * Sets the socket read timeout - how much time to wait
         *      before data becomes available to read from socket when sending scanned file
         * Default value is 60 seconds.<br>
         * Range: 1 ~ 300 seconds.<br>
         * @param readTimeout value in seconds
         * @return builder
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        @NonNull
        public T setReadTimeout(@IntRange(from=MINIMUM_TIMEOUT, to=MAXIMUM_TIMEOUT) final int readTimeout) {
            mReadTimeout = readTimeout;
            return (T) this;
        }

        /**
         * Sets the maximum number of times to retry an attempt to access the uri. A value of 0 indicates no retry
         * if the first attempt fails.<br>
         * Default value is 0 (no retries).<br>
         * Range: 0 ~ 5 times.<br>
         * @param maxRetries how many retries the device must take when sending the scanned file to URI before failing
         * @return builder
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        @NonNull
        public T setMaxConsecutiveRetries(@IntRange(from=MINIMUM_RETRIES, to=MAXIMUM_RETRIES) final int maxRetries) {
            mMaxRetries = maxRetries;
            return (T) this;
        }

        /**
         * Sets the minimum number of seconds to wait before attempting a retry.<br>
         * Default value is 60 seconds.<br>
         * Range: 1 ~ 300 seconds.<br>
         * @param retryInterval seconds to wait between retries
         * @return builder
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        @NonNull
        public T setRetryInterval(@IntRange(from=MINIMUM_TIMEOUT, to=MAXIMUM_TIMEOUT) final int retryInterval) {
            mRetryInterval = retryInterval;
            return (T) this;
        }

        @Override
        void validate(final ScanAttributesCaps caps) throws CapabilitiesExceededException {
            super.validate(caps);

            if (mConnectTimeout < MINIMUM_TIMEOUT || mConnectTimeout > MAXIMUM_TIMEOUT) {
                throw new CapabilitiesExceededException(
                        String.format(Locale.US, "Connect Timeout value must be in range [%d, %d] seconds",
                                MINIMUM_TIMEOUT, MAXIMUM_TIMEOUT));
            }

            if (mReadTimeout < MINIMUM_TIMEOUT || mReadTimeout > MAXIMUM_TIMEOUT) {
                throw new CapabilitiesExceededException(
                        String.format(Locale.US, "Read Timeout value must be in range [%d, %d] seconds",
                                MINIMUM_TIMEOUT, MAXIMUM_TIMEOUT));
            }

            if (mMaxRetries < MINIMUM_RETRIES || mMaxRetries > MAXIMUM_RETRIES) {
                throw new CapabilitiesExceededException(
                        String.format(Locale.US, "Maximum Retries value must be in range [%d, %d] seconds",
                                MINIMUM_RETRIES, MAXIMUM_RETRIES));
            }

            if (mRetryInterval < MINIMUM_TIMEOUT || mRetryInterval > MAXIMUM_TIMEOUT) {
                throw new CapabilitiesExceededException(
                        String.format(Locale.US, "Retry Interval value must be in range [%d, %d] seconds",
                                MINIMUM_TIMEOUT, MAXIMUM_TIMEOUT));
            }
        }
    }

    /**
     * Builder for creating {@link ScanAttributes} configured to send scanned images to the external HTTP(S) server.
     *
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    public static class HttpBuilder extends ScanToUriBuilder<HttpBuilder> {
        /**
         * Construct a new Builder for sending scanned images to the specified external HTTP(s) server URL
         * with default scan attributes.<br>
         * <p>
         * Color Mode = DEFAULT <br>
         * Document Format = DEFAULT <br>
         * Scan Size = DEFAULT <br>
         * Resolution = DEFAULT <br>
         * </p>
         *
         * <p>
         *     The uri must match one of the following formats:
         *     <ul>
         *         <li><b>http</b>://MyWebServer[:port]/MyWebApp/MyScanReceiver</li>
         *         <li><b>https</b>://MyWebServer[:port]/MyWebApp/MyScanReceiver</li>
         *     </ul>
         * </p>
         *
         * @param uri HTTP(S) url
         * @since API 1
         */
        @SuppressWarnings({"unused"})
        public HttpBuilder(final Uri uri) {
            super(uri);
            this.mDestination = Destination.HTTP;
        }

        /**
         * Combines all of the attributes in this into a ScanAttributes object.
         *
         * @param caps The scan capabilities.
         * @return a ScanAttributes object containing all of the attributes.
         * @throws CapabilitiesExceededException if caps does not contain the set attribute value.
         * @since API 1
         */
        @Override
        @SuppressWarnings({"unused"})
        @NonNull
        public ScanAttributes build(@NonNull final ScanAttributesCaps caps) throws CapabilitiesExceededException {
            validate(caps);

            return new ScanAttributes(this);
        }
    }

    /**
     * Builder for creating {@link ScanAttributes} configured to send scanned images to the external FTP server.
     *
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    public static class FtpBuilder extends ScanToUriBuilder<FtpBuilder> {
        /**
         * Constructor to create a new ScanToFtp Builder for sending scanned images to the specified external FTP server URL
         * with default scan attributes.<br>
         * <p>
         * Color Mode = DEFAULT <br>
         * Document Format = DEFAULT <br>
         * Scan Size = DEFAULT <br>
         * Resolution = DEFAULT <br>
         * </p>
         *
         * <p>
         *     The uri must match the following format:
         *     <ul>
         *         <li><b>ftp</b>://MyFtpServer[/MyFolder[/MySubFolder]]</li>
         *     </ul>

         *     The solution must ensure that the sub-folders exist before starting the scan job.
         * </p>
         *
         * @param uri FTP url
         * @since API 1
         */
        @SuppressWarnings({"unused"})
        public FtpBuilder(final Uri uri) {
            super(uri);
            this.mDestination = Destination.FTP;
        }

        /**
         * Combines all of the attributes in this into a ScanAttributes object.
         *
         * @param caps The scan capabilities.
         * @return ScanAttributes object containing all of the attributes.
         * @throws CapabilitiesExceededException if caps does not contain the set attribute value.
         * @since API 1
         */
        @Override
        @SuppressWarnings({"unused"})
        @NonNull
        public ScanAttributes build(@NonNull final ScanAttributesCaps caps) throws CapabilitiesExceededException {
            validate(caps);

            return new ScanAttributes(this);
        }
    }

    /**
     * Builder for creating {@link ScanAttributes} configured to send scanned images to the external network folder server.
     *
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    public static class NetworkFolderBuilder extends ScanToUriBuilder<NetworkFolderBuilder> {
        /**
         * Constructor to create a new Builder for sending scanned images to the specified external network folder URL
         * with default scan attributes.<br>
         * <p>
         * Color Mode = DEFAULT <br>
         * Document Format = DEFAULT <br>
         * Scan Size = DEFAULT <br>
         * Resolution = DEFAULT <br>
         * </p>
         *
         * <p>
         *     The uri must match the following format:
         *     <ul>
         *         <li><b>file</b>:////MyFileServer/MyShare[/MySubFolder]</li>
         *         <li>\\MyFileServer\MyShare[\MySubFolder]</li>
         *     </ul>
         *
         *     The solution must ensure that the sub-folders exist before starting the scan job.
         * </p>
         *
         * @param uri network folder url
         * @since API 1
         */
        @SuppressWarnings({"unused"})
        public NetworkFolderBuilder(final Uri uri) {
            super(uri);
            this.mDestination = Destination.NETWORK_FOLDER;
        }

        /**
         * Combines all of the attributes in this into a ScanAttributes object.
         *
         * @param caps The scan capabilities.
         * @return ScanAttributes object containing all of the attributes.
         * @throws CapabilitiesExceededException if caps does not contain the set attribute value.
         * @since API 1
         */
        @Override
        @SuppressWarnings({"unused"})
        @NonNull
        public ScanAttributes build(@NonNull final ScanAttributesCaps caps) throws CapabilitiesExceededException {
            validate(caps);

            return new ScanAttributes(this);
        }
    }

    /**
     * Builder for creating {@link ScanAttributes} configured to send scanned images to the specified E-mail recipients.
     *
     * @since API 1
     *
     * @deviceOnly
     */
    @SuppressWarnings({"unused"})
    public static class EmailBuilder extends ScanAttributesBuilder<EmailBuilder> {
        EmailAttributes mEmailAttributes;
        SmtpAttributes mSmtpAttributes;

        /**
         * Constructor to create a new Builder for sending scanned images to the specified E-mail recipients
         * with default scan attributes.<br>
         * <p>
         * Color Mode = DEFAULT <br>
         * Document Format = DEFAULT <br>
         * Scan Size = DEFAULT <br>
         * Resolution = DEFAULT <br>
         * </p>
         *
         * @param emailAttributes details of an email message
         * @since API 1
         */
        @SuppressWarnings({"unused"})
        public EmailBuilder(final EmailAttributes emailAttributes) {
            mDestination = Destination.EMAIL;
            mEmailAttributes = emailAttributes;
        }

        @Override
        void validate(ScanAttributesCaps caps) throws CapabilitiesExceededException {
            super.validate(caps);

            if (mEmailAttributes == null) {
                throw new CapabilitiesExceededException("Email attributes must be not provided");
            }

            if (mSmtpAttributes != null && mEmailAttributes.mFrom == null) {
                throw new CapabilitiesExceededException("Email FROM value must be not provided");
            }

            if (mEmailAttributes.mToList.isEmpty()
                    && mEmailAttributes.mCcList.isEmpty()
                    && mEmailAttributes.mBccList.isEmpty()) {
                throw new CapabilitiesExceededException("At least one recipient must be provided");
            }
        }

        /**
         * Sets the connection parameters for establishing a connection to SMTP server
         * @param smtpAttributes SMTP connection parameters
         * @return builder
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        @NonNull
        public EmailBuilder setSmtpAttributes(@Nullable final SmtpAttributes smtpAttributes) {
            mSmtpAttributes = smtpAttributes;
            return this;
        }

        /**
         * Combines all of the attributes in this into a ScanAttributes object.
         *
         * @param caps The scan capabilities.
         * @return ScanAttributes object containing all of the attributes.
         * @throws CapabilitiesExceededException if caps does not contain the set attribute value.
         * @since API 1
         */
        @Override
        @SuppressWarnings({"unused"})
        @NonNull
        public ScanAttributes build(@NonNull final ScanAttributesCaps caps) throws CapabilitiesExceededException {
            validate(caps);

            return new ScanAttributes(this);
        }
    }

    /**
     * Builder for creating {@link ScanAttributes} configured to send scanned images to attached USB storage.
     *
     * @since API 2
     *
     * @deviceOnly
     */
    public static class UsbBuilder extends ScanAttributesBuilder<UsbBuilder> {
        String mUsbLocation;

        /**
         * Construct a new Builder for scanning to the specified USB location with default attributes.<br>
         * <br>
         * Color Mode = DEFAULT <br>
         * Document Format = DEFAULT <br>
         * Scan Size = DEFAULT <br>
         * Resolution = DEFAULT <br>
         * <br>
         *
         * @since API 1
         */
        @SuppressWarnings({"unused"})
        public UsbBuilder(String folder) {
            this.mDestination = Destination.USB;
            this.mUsbLocation = folder;
        }

        /**
         * Combines all of the attributes in this into a ScanAttributes object.
         *
         * @param caps The scan capabilities.
         * @return a ScanAttributes object containing all of the attributes.
         * @throws CapabilitiesExceededException if caps does not contain the set attribute value.
         * @since API 1
         */
        @Override
        @SuppressWarnings({"unused"})
        @NonNull
        public ScanAttributes build(@NonNull final ScanAttributesCaps caps) throws CapabilitiesExceededException {
            validate(caps);

            return new ScanAttributes(this);
        }
    }

    private static boolean isFilenameValid(String fileName){
        return FileUtility.isValidExtFilename(fileName) && FileUtility.isValidFatFilename(fileName);
    }

    /**
     * Validates files uri
     *
     * @param uri original file uri
     * @hide Only for internal use
     */
    public static void validateUri(Uri uri) {
        // if scheme is present - validate it as URL
        if (uri != null) {
            if (uri.getScheme() != null) {
                if (CommonUtility.isValidURL(uri)) {
                    return;
                }
            } else if (CommonUtility.isValidUNC(uri)) {
                return;
            }
        }

        throw new IllegalArgumentException("Destination URL is not valid");
    }

    /**
     * Expresses the {@link ScanAttributes} in a string.
     *
     * @hide trivial
     */
    @Override
    public String toString() {
        return new StringBuilder().append("[").append("Destination=").append(((mDestination != null)?mDestination.name().toString():"null")).append("]").toString();
    }
}
