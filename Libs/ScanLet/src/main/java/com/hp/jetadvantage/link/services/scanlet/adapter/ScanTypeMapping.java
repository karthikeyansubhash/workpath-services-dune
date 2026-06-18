/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.scanlet.adapter;

import com.hp.ext.service.scanJob.TransmissionMode;
import com.hp.ext.types.imaging.BlankPageDetectionMode;
import com.hp.ext.types.imaging.ColorMode;
import com.hp.ext.types.imaging.CompressionType;
import com.hp.ext.types.imaging.ContentOrientation;
import com.hp.ext.types.imaging.DocumentContentType;
import com.hp.ext.types.imaging.FileFormat;
import com.hp.ext.types.imaging.ImagePreviewMode;
import com.hp.ext.types.imaging.QualityVsSize;
import com.hp.ext.types.imaging.Resolution;
import com.hp.ext.types.imaging.ScanCaptureMode;
import com.hp.ext.types.imaging.ScanProgressMode;
import com.hp.ext.types.job.JobActivityState;
import com.hp.ext.types.media.MediaInputId;
import com.hp.ext.types.media.MediaSizeId;
import com.hp.jetadvantage.link.api.job.ScanJobState;
import com.hp.jetadvantage.link.api.scanner.FileOptionsAttributes;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.ScanSize;
import com.hp.jetadvantage.link.device.services.converter.DefaultTypeConverter;
import com.hp.jetadvantage.link.device.services.converter.ITypeConverter;
import com.hp.jetadvantage.link.device.services.converter.ITypeMapping;
import com.hp.jetadvantage.link.device.services.converter.RangeTypeConverter;

import java.util.Map;
import java.util.function.Supplier;

/**
 * ScanTypeMapping
 * Each entry in the enum contains a mapping between the E2 and Workpath API types.
 * Each entry name is expected to be the same string as E2 Json option name, ordered alphabetically.
 * The mapping can be a 1:1 mapping (Category.DEFAULT), a range mapping (Category.RANGE), or a custom mapping
 * (Category.CUSTOM).
 * <p>
 * How to convert Scan option types between the E2 and Workpath API types :
 * - ScanTypeMapping.colorMode.convertEtoW(E2 value) returns Workpath API value
 * - ScanTypeMapping.colorMode.convertWtoE(Workpath value) returns E2 value
 */
public enum ScanTypeMapping implements ITypeMapping {

    /// //////////////////////// Scan Option Mapping ///////////////////////////
    autoDeskew(
            Boolean.class,
            ScanAttributes.AutomaticStraightenMode.DEFAULT,
            Map.of(
                    Boolean.TRUE, ScanAttributes.AutomaticStraightenMode.ENABLE,
                    Boolean.FALSE, ScanAttributes.AutomaticStraightenMode.DISABLE
            )
    ),
    backgroundCleanup(
            9,
            ScanAttributes.BackgroundCleanup.class,
            ScanAttributes.BackgroundCleanup.DEFAULT,
            ScanAttributes.BackgroundCleanup.LEVEL_0,
            ScanAttributes.BackgroundCleanup.LEVEL_8
    ),
    blankPageSuppression(
            BlankPageDetectionMode.class,
            ScanAttributes.BlankImageRemovalMode.DEFAULT,
            Map.of(
                    BlankPageDetectionMode.BpdDisable, ScanAttributes.BlankImageRemovalMode.OFF,
                    BlankPageDetectionMode.BpdDetectAndSuppress, ScanAttributes.BlankImageRemovalMode.ON
            )
    ),
    autoCropMode(new ScanCustomConverterSupplier.CropModeConverter()),
    colorAndGrayCompression(
            CompressionType.class,
            FileOptionsAttributes.TiffCompressionMode.DEFAULT,
            Map.of(
                    CompressionType.CtOJpeg, FileOptionsAttributes.TiffCompressionMode.JPEG_TIFF_6,
                    CompressionType.CtJpeg, FileOptionsAttributes.TiffCompressionMode.JPEG_TTN_2,
                    CompressionType.CtLzw, FileOptionsAttributes.TiffCompressionMode.LZW,
                    CompressionType.CtAuto, FileOptionsAttributes.TiffCompressionMode.HIGH
            )
    ),
    colorMode(
            ColorMode.class,
            ScanAttributes.ColorMode.DEFAULT,
            Map.of(
                    ColorMode.CmAutoDetect, ScanAttributes.ColorMode.AUTO,
                    ColorMode.CmMonochrome, ScanAttributes.ColorMode.MONO,
                    ColorMode.CmGrayscale, ScanAttributes.ColorMode.GRAY,
                    ColorMode.CmColor, ScanAttributes.ColorMode.COLOR
            )
    ),
    contentOrientation(
            ContentOrientation.class,
            ScanAttributes.Orientation.DEFAULT,
            Map.of(
                    ContentOrientation.CoPortrait, ScanAttributes.Orientation.PORTRAIT,
                    ContentOrientation.CoLandscape, ScanAttributes.Orientation.LANDSCAPE,
                    ContentOrientation.CoAutoDetect, ScanAttributes.Orientation.AUTO_DETECT
            )
    ),
    contentType(
            DocumentContentType.class,
            ScanAttributes.TextPhotoOptimization.DEFAULT,
            Map.of(
                    DocumentContentType.DctText, ScanAttributes.TextPhotoOptimization.TEXT,
                    DocumentContentType.DctMixed, ScanAttributes.TextPhotoOptimization.MIXED_2,
                    DocumentContentType.DctImage, ScanAttributes.TextPhotoOptimization.GRAPHIC
            )
    ),
    contrast(
            9,
            ScanAttributes.ContrastAdjustment.class,
            ScanAttributes.ContrastAdjustment.DEFAULT,
            ScanAttributes.ContrastAdjustment.LEVEL_0,
            ScanAttributes.ContrastAdjustment.LEVEL_8
    ),
    exposureLevel(
            9,
            ScanAttributes.DarknessAdjustment.class,
            ScanAttributes.DarknessAdjustment.DEFAULT,
            ScanAttributes.DarknessAdjustment.LEVEL_0,
            ScanAttributes.DarknessAdjustment.LEVEL_8
    ),
    duplex(new ScanCustomConverterSupplier.DuplexConverter()),
    fileTransmissionMode(
            TransmissionMode.class,
            ScanAttributes.TransmissionMode.DEFAULT,
            Map.of(
                    TransmissionMode.TmImage, ScanAttributes.TransmissionMode.IMAGE,
                    TransmissionMode.TmJob, ScanAttributes.TransmissionMode.JOB
            )
    ),
    imagePreviewMode(
            ImagePreviewMode.class,
            ScanAttributes.ScanPreview.DEFAULT,
            Map.of(
                    ImagePreviewMode.IpmStandard, ScanAttributes.ScanPreview.TRUE,
                    ImagePreviewMode.IpmNone, ScanAttributes.ScanPreview.FALSE
            )
    ),
    mediaSize(
            MediaSizeId.class,
            ScanAttributes.ScanSize.DEFAULT,
            Map.ofEntries(
                    Map.entry(MediaSizeId.MsISO_A3_297x420mm, ScanSize.A3),
                    Map.entry(MediaSizeId.MsISO_A4_Rotated_210x297mm, ScanSize.A4_ROTATE),
                    Map.entry(MediaSizeId.MsISO_A4_210x297mm, ScanSize.A4),
                    Map.entry(MediaSizeId.MsISO_A5_Rotated_148x210mm, ScanSize.A5_ROTATE),
                    Map.entry(MediaSizeId.MsISO_A5_148x210mm, ScanSize.A5),
                    Map.entry(MediaSizeId.MsISO_A6_105x148mm, ScanSize.A6),
                    Map.entry(MediaSizeId.MsISO_B4_250x353mm, ScanSize.B4),
                    Map.entry(MediaSizeId.MsISO_B5_176x250mm, ScanSize.B5),
                    Map.entry(MediaSizeId.MsISO_B6_125x176mm, ScanSize.B6),
                    Map.entry(MediaSizeId.MsJBusinessCard_55x91mm, ScanSize.BUSINESS_CARD),
                    Map.entry(MediaSizeId.MsANSI_A_Rotated_8point5x11in, ScanSize.LETTER_ROTATE),
                    Map.entry(MediaSizeId.MsANSI_A_8point5x11in, ScanSize.LETTER),
                    Map.entry(MediaSizeId.MsJIS_B4_257x364mm, ScanSize.JB4),
                    Map.entry(MediaSizeId.MsJIS_B5_Rotated_182x257mm, ScanSize.JB5_ROTATE),
                    Map.entry(MediaSizeId.MsJIS_B5_182x257mm, ScanSize.JB5),
                    Map.entry(MediaSizeId.MsJIS_B6_128x182mm, ScanSize.JB6),
                    Map.entry(MediaSizeId.MsLegal_8point5x14in, ScanSize.LEGAL),
                    Map.entry(MediaSizeId.MsANSI_B_11x17in, ScanSize.LEDGER),
                    Map.entry(MediaSizeId.MsExecutive_7point25x10point5in, ScanSize.EXECUTIVE),
                    Map.entry(MediaSizeId.MsInvoice_5point5x8point5in, ScanSize.STATEMENT),
                    Map.entry(MediaSizeId.MsCustom, ScanSize.CUSTOM),
                    Map.entry(MediaSizeId.MsK8_270x390mm, ScanSize.K8),
                    Map.entry(MediaSizeId.MsK16_195x270mm, ScanSize.K16),
                    Map.entry(MediaSizeId.MsK8_273x394mm, ScanSize.PK8),
                    Map.entry(MediaSizeId.MsK16_197x273mm, ScanSize.PK16),
                    Map.entry(MediaSizeId.MsMixedLetterLegal, ScanSize.MIXEDLETTERLEGAL),
                    Map.entry(MediaSizeId.MsMixedA4A3, ScanSize.MIXEDA3A4),
                    Map.entry(MediaSizeId.MsAny, ScanSize.AUTO),
                    Map.entry(MediaSizeId.MsEnvelope_Windsor_3point875x8point875in, ScanSize.ENVELOPE_9),
                    Map.entry(MediaSizeId.MsEnvelope_Comm10_4point125x9point5in, ScanSize.ENVELOPE_COMM10),
                    Map.entry(MediaSizeId.MsEnvelope_Monarch_3point875x7point5in, ScanSize.ENVELOPE_MONARCH),
                    Map.entry(MediaSizeId.MsISO_C5_162x229mm, ScanSize.C5),
                    Map.entry(MediaSizeId.MsISO_C6_114x162mm, ScanSize.C6),
                    Map.entry(MediaSizeId.MsEnvelope_DL_110x220mm, ScanSize.ENVELOPE_DL),
                    Map.entry(MediaSizeId.MsJIS_Chou3_120x235mm, ScanSize.JCHOU3),
                    Map.entry(MediaSizeId.MsJIS_Chou4_90x205mm, ScanSize.JCHOU4),
                    Map.entry(MediaSizeId.MsUnknownEnvelope, ScanSize.UNKNOWN_ENVELOP),
                    Map.entry(MediaSizeId.MsJDoublePostcard_148x200mm, ScanSize.JDOUBLE_POSTCARD),
                    Map.entry(MediaSizeId.MsJPostcard_100x148mm, ScanSize.JPOSTCARD),
                    Map.entry(MediaSizeId.MsK8_260x368mm, ScanSize.K8_260X368mm),
                    Map.entry(MediaSizeId.MsK16_184x260mm, ScanSize.K16_184X260mm),
                    Map.entry(MediaSizeId.MsMixedLetterLedger, ScanSize.MIXEDLETTERLEDGER),
                    Map.entry(MediaSizeId.MsOficio_216x340mm, ScanSize.OFICIO),
                    Map.entry(MediaSizeId.MsOM_Photo_100x150mm, ScanSize.GENERAL_10X15cm),
                    Map.entry(MediaSizeId.MsRA3_305x430mm, ScanSize.RA3),
                    Map.entry(MediaSizeId.MsRA4_215x305mm, ScanSize.RA4),
                    Map.entry(MediaSizeId.MsGeneral_3x5in, ScanSize.GENERAL_3X5in),
                    Map.entry(MediaSizeId.MsGeneral_4x6in, ScanSize.GENERAL_4X6in),
                    Map.entry(MediaSizeId.MsGeneral_5x7in, ScanSize.GENERAL_5X7in),
                    Map.entry(MediaSizeId.MsGeneral_5x8in, ScanSize.GENERAL_5X8in),
                    Map.entry(MediaSizeId.MsFoolscap_8point5x13in, ScanSize.GENERAL_8POINT5X13in),
                    Map.entry(MediaSizeId.MsLongScan_8point5x34in, ScanSize.GENERAL_8POINT5X34in),
                    Map.entry(MediaSizeId.MsArchitectural_B_12x18in, ScanSize.GENERAL_12X18in),
                    Map.entry(MediaSizeId.MsGeneral_3point5x5in, ScanSize.GENERAL_L_9X13cm),
                    Map.entry(MediaSizeId.MsSRA3_320x450mm, ScanSize.SRA3),
                    Map.entry(MediaSizeId.MsSRA4_225x320mm, ScanSize.SRA4),
                    Map.entry(MediaSizeId.MsUnknown, ScanSize.UNKNOWN)
            )
    ),
    mediaSource(
            MediaInputId.class,
            ScanAttributes.MediaSource.DEFAULT,
            Map.of(
                    MediaInputId.MiAdf, ScanAttributes.MediaSource.ADF,
                    MediaInputId.MiFlatbed, ScanAttributes.MediaSource.FLATBED,
                    MediaInputId.MiAuto, ScanAttributes.MediaSource.AUTO
            )
    ),
    misfeedDetection(
            Boolean.class,
            ScanAttributes.MisfeedDetectionMode.DEFAULT,
            Map.of(
                    Boolean.FALSE, ScanAttributes.MisfeedDetectionMode.OFF,
                    Boolean.TRUE, ScanAttributes.MisfeedDetectionMode.ON
            )
    ),
    monoCompression(
            CompressionType.class,
            FileOptionsAttributes.TiffCompressionMode.DEFAULT,
            Map.of(
                    CompressionType.CtAuto, FileOptionsAttributes.TiffCompressionMode.HIGH,
                    CompressionType.CtG3, FileOptionsAttributes.TiffCompressionMode.G_3,
                    CompressionType.CtG4, FileOptionsAttributes.TiffCompressionMode.G_4,
                    CompressionType.CtLzw, FileOptionsAttributes.TiffCompressionMode.LZW
            )
    ),
    outputFileCompression(
            Boolean.class,
            FileOptionsAttributes.PdfCompressionMode.DEFAULT,
            Map.of(
                    Boolean.FALSE, FileOptionsAttributes.PdfCompressionMode.NORMAL,
                    Boolean.TRUE, FileOptionsAttributes.PdfCompressionMode.HIGH
            )
    ),
    outputFileEncryption(
            Boolean.class,
            UndefinedWorkpathApiType.outputFileEncryption.DEFAULT,
            Map.of(
                    Boolean.FALSE, UndefinedWorkpathApiType.outputFileEncryption.OFF,
                    Boolean.TRUE, UndefinedWorkpathApiType.outputFileEncryption.ON
            )
    ),
    outputFileFormat(
            FileFormat.class,
            ScanAttributes.DocumentFormat.DEFAULT,
            Map.ofEntries(
                    Map.entry(FileFormat.FfJpeg, ScanAttributes.DocumentFormat.JPEG),
                    Map.entry(FileFormat.FfPdf, ScanAttributes.DocumentFormat.PDF),
                    Map.entry(FileFormat.FfTiff, ScanAttributes.DocumentFormat.TIFF),
                    Map.entry(FileFormat.FfMtiff, ScanAttributes.DocumentFormat.MTIFF),
                    Map.entry(FileFormat.FfOcrPdf, ScanAttributes.DocumentFormat.OCR_PDF_TEXT_UNDER_IMAGE),
                    Map.entry(FileFormat.FfOcrPdfa, ScanAttributes.DocumentFormat.OCR_PDF_A_TEXT_UNDER_IMAGE),
                    Map.entry(FileFormat.FfOcrCsv, ScanAttributes.DocumentFormat.OCR_CSV),
                    Map.entry(FileFormat.FfOcrHtml, ScanAttributes.DocumentFormat.OCR_HTML),
                    Map.entry(FileFormat.FfOcrRtf, ScanAttributes.DocumentFormat.OCR_RTF),
                    Map.entry(FileFormat.FfOcrText, ScanAttributes.DocumentFormat.OCR_TEXT),
                    Map.entry(FileFormat.FfOcrUnicode, ScanAttributes.DocumentFormat.OCR_UNICODE_TEXT),
                    Map.entry(FileFormat.FfPdfa, ScanAttributes.DocumentFormat.PDF_A)
            )
    ),
    outputQualityVsSize(
            QualityVsSize.class,
            ScanAttributes.OutputQuality.DEFAULT,
            Map.of(
                    QualityVsSize.QvsLow, ScanAttributes.OutputQuality.LOW,
                    QualityVsSize.QvsMedium, ScanAttributes.OutputQuality.MEDIUM,
                    QualityVsSize.QvsHigh, ScanAttributes.OutputQuality.HIGH
            )
    ),
    resolution(
            Resolution.class,
            ScanAttributes.Resolution.DEFAULT,
            Map.of(
                    Resolution.Dpi75, ScanAttributes.Resolution.DPI_75,
                    Resolution.Dpi100, ScanAttributes.Resolution.DPI_100,
                    Resolution.Dpi150, ScanAttributes.Resolution.DPI_150,
                    Resolution.Dpi200, ScanAttributes.Resolution.DPI_200,
                    Resolution.Dpi240, ScanAttributes.Resolution.DPI_240,
                    Resolution.Dpi300, ScanAttributes.Resolution.DPI_300,
                    Resolution.Dpi400, ScanAttributes.Resolution.DPI_400,
                    Resolution.Dpi500, ScanAttributes.Resolution.DPI_500,
                    Resolution.Dpi600, ScanAttributes.Resolution.DPI_600
            )
    ),
    scanCaptureMode(
            ScanCaptureMode.class,
            ScanAttributes.CaptureMode.DEFAULT,
            Map.of(
                    ScanCaptureMode.ScmStandard, ScanAttributes.CaptureMode.STANDARD,
                    ScanCaptureMode.ScmJobBuild, ScanAttributes.CaptureMode.STANDARD_ADD_PAGES,
                    ScanCaptureMode.ScmBook, ScanAttributes.CaptureMode.BOOK_CAPTURE,
                    ScanCaptureMode.ScmIdCard, ScanAttributes.CaptureMode.ID_CAPTURE_PROMPT_BOTH_SIDES
            )
    ),
    scanProgressMode(
            ScanProgressMode.class,
            ScanAttributes.ProgressDialogMode.DEFAULT,
            Map.of(
                    ScanProgressMode.SpmNone, ScanAttributes.ProgressDialogMode.OFF,
                    ScanProgressMode.SpmStandard, ScanAttributes.ProgressDialogMode.ON
            )
    ),
    sharpness(
            5,
            ScanAttributes.SharpnessAdjustment.class,
            ScanAttributes.SharpnessAdjustment.DEFAULT,
            ScanAttributes.SharpnessAdjustment.LEVEL_0,
            ScanAttributes.SharpnessAdjustment.LEVEL_4
    ),

    /// //////////////////////// ScanJobStatus Mapping ///////////////////////////
    jobActivityState(
            JobActivityState.class,
            ScanJobState.ActivityState.NOT_STARTED,
            Map.of(
                    JobActivityState.JasNotStarted, ScanJobState.ActivityState.NOT_STARTED,
                    JobActivityState.JasStarted, ScanJobState.ActivityState.STARTED,
                    JobActivityState.JasCompleted, ScanJobState.ActivityState.COMPLETED
            )
    );

    private final Category category;
    private final ITypeConverter<?, ?> converter;
    private final Supplier<ITypeConverter> customSupplier;

    /**
     * Enum constructor for creating a default (1:1 mapping) type converter
     *
     * @param e2Type   E2 Class type
     * @param wDefault Workpath API Default value
     * @param mapping  1:1 mapping between E2 and Workpath API types
     * @param <E>      E2 type
     * @param <W>      Workpath API type
     */
    <E, W> ScanTypeMapping(Class<E> e2Type, W wDefault, Map<E, W> mapping) {
        category = Category.DEFAULT;
        customSupplier = null;
        converter = new DefaultTypeConverter<E, W>(e2Type, wDefault) {{
            mapEtoW.putAll(mapping);
        }};
    }

    /**
     * Enum constructor for creating a range type converter
     *
     * @param wRangeSize Workpath API enum size (The number of entries in the Workpath API Enum except DEFAULT enum)
     * @param wEnumType  Workpath API Enum type
     * @param wDefault   Workpath API default value
     * @param wMin       Workpath API minimum value in the enum
     * @param wMax       Workpath API maximum value in the enum
     * @param <W>        Workpath API Enum type
     */
    <W> ScanTypeMapping(int wRangeSize, Class<W> wEnumType, W wDefault, W wMin, W wMax) {
        category = Category.ENUM_RANGE;
        customSupplier = null;
        converter = new RangeTypeConverter<W>(wRangeSize, wEnumType, wDefault, wMin, wMax);
    }

    /**
     * Enum constructor for creating a custom type converter
     *
     * @param customConverterSupplier Supplier for the custom type converter
     */
    ScanTypeMapping(Supplier<ITypeConverter> customConverterSupplier) {
        this.category = Category.CUSTOM;
        this.customSupplier = customConverterSupplier;
        converter = customConverterSupplier.get();
    }

    @Override
    public Category getCategory() {
        return category;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ITypeConverter getConverter() {
        return converter;
    }

    /**
     * Returns a version-aware converter for CUSTOM mappings that support client versioning
     * (e.g. {@link ScanCustomConverterSupplier.CropModeConverter}), otherwise returns the
     * default converter.
     *
     * @param clientVersion the client API level from {@link com.hp.jetadvantage.link.common.Sdk.VERSION_LEVEL}
     */
    @SuppressWarnings("unchecked")
    public ITypeConverter getConverter(int clientVersion) {
        if (category == Category.CUSTOM
                && customSupplier instanceof ScanCustomConverterSupplier.CropModeConverter) {
            return new ScanCustomConverterSupplier.CropModeConverter(clientVersion).get();
        }
        return converter;
    }


    /**
     * Undefined Workpath API types
     * Workpath does not have a corresponding type that maps to E2 types
     */
    public static class UndefinedWorkpathApiType {
        public enum outputFileEncryption {
            DEFAULT, OFF, ON
        }
    }
}
