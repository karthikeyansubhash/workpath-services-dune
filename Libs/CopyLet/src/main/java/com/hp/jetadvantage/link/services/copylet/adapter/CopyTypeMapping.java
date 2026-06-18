/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.copylet.adapter;

import com.hp.ext.types.imaging.ColorMode;
import com.hp.ext.types.imaging.ContentOrientation;
import com.hp.ext.types.imaging.DocumentContentType;
import com.hp.ext.types.imaging.DuplexBindingFormat;
import com.hp.ext.types.imaging.ImagePreviewMode;
import com.hp.ext.types.imaging.PagesPerSheetType;
import com.hp.ext.types.imaging.ScaleSelectionType;
import com.hp.ext.types.imaging.ScanCaptureMode;
import com.hp.ext.types.imaging.ScanProgressMode;
import com.hp.ext.types.imaging.SheetCollationType;
import com.hp.ext.types.media.MediaInputId;
import com.hp.ext.types.media.MediaOutputId;
import com.hp.ext.types.media.MediaSizeId;
import com.hp.ext.types.media.MediaTypeId;
import com.hp.jetadvantage.link.api.copier.CopyAttributes;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.OutputBin;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.PaperSize;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.PaperSource;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.PaperType;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.ScanSize;
import com.hp.jetadvantage.link.device.services.converter.DefaultTypeConverter;
import com.hp.jetadvantage.link.device.services.converter.ITypeConverter;
import com.hp.jetadvantage.link.device.services.converter.ITypeMapping;
import com.hp.jetadvantage.link.device.services.converter.IntRangeTypeConverter;
import com.hp.jetadvantage.link.device.services.converter.RangeTypeConverter;

import java.util.Map;
import java.util.function.Supplier;

/**
 * CopyTypeMapping
 * Each entry in the enum contains a mapping between the E2 and Workpath API types.
 * Each entry name is expected to be the same string as E2 Json option name, ordered alphabetically.
 * The mapping can be a 1:1 mapping (Category.DEFAULT), a range to enum mapping  (Category.ENUM_RANGE), a numeric
 * range to range mapping
 * (Category.INTEGER_RANGE or Category.FLOAT_RANGE) or a custom mapping (Category.CUSTOM).
 * <p>
 * How to convert Copy option types between the E2 and Workpath API types :
 * - CopyTypeMapping.colorMode.convertEtoW(E2 value) returns Workpath API value
 * - CopyTypeMapping.colorMode.convertWtoE(Workpath value) returns E2 value
 */
public enum CopyTypeMapping implements ITypeMapping {
    /////////////////////////// Copy Option Mappings ///////////////////////////
    collationType(
            com.hp.ext.types.imaging.SheetCollationType.class,
            CopyAttributes.CollateMode.DEFAULT,
            Map.of(
                    SheetCollationType.SctCollated, CopyAttributes.CollateMode.COLLATED,
                    SheetCollationType.SctUncollated, CopyAttributes.CollateMode.UNCOLLATED
            )
    ),
    colorMode(
            com.hp.ext.types.imaging.ColorMode.class,
            CopyAttributes.ColorMode.DEFAULT,
            Map.of(
                    ColorMode.CmAutoDetect, CopyAttributes.ColorMode.AUTO,
                    ColorMode.CmMonochrome, CopyAttributes.ColorMode.MONO,
                    ColorMode.CmColor, CopyAttributes.ColorMode.COLOR,
                    ColorMode.CmGrayscale, CopyAttributes.ColorMode.GRAY,
                    ColorMode.CmBlackAndWhite, CopyAttributes.ColorMode.MONO,
                    ColorMode.CmAutoColorAndGray, CopyAttributes.ColorMode.COLOR,
                    ColorMode.CmAutoColorAndBlack, CopyAttributes.ColorMode.COLOR,
                    ColorMode.CmGrayscaleAutoDetect, CopyAttributes.ColorMode.AUTO,
                    ColorMode.CmTrueBlack, CopyAttributes.ColorMode.MONO
            )
    ),
    contentOrientation(
            com.hp.ext.types.imaging.ContentOrientation.class,
            CopyAttributes.Orientation.DEFAULT,
            Map.of(
                    //ContentOrientation.CoAutoDetect, CopyAttributes.Orientation.?
                    ContentOrientation.CoLandscape, CopyAttributes.Orientation.LANDSCAPE,
                    ContentOrientation.CoPortrait, CopyAttributes.Orientation.PORTRAIT
                    //ContentOrientation.CoReversePortrait, CopyAttributes.Orientation.?
                    //ContentOrientation.CoReverseLandscape, CopyAttributes.Orientation.?
                    //ContentOrientation.CoUndefined, CopyAttributes.Orientation.?
            )
    ),
    contentType(
            com.hp.ext.types.imaging.DocumentContentType.class,
            CopyAttributes.TextGraphicsOptimization.DEFAULT,
            Map.of(
                    DocumentContentType.DctAutoDetect, CopyAttributes.TextGraphicsOptimization.AUTODETECT,
                    DocumentContentType.DctText, CopyAttributes.TextGraphicsOptimization.TEXT,
                    //DocumentContentType.DctLineDrawing, CopyAttributes.TextGraphicsOptimization.?
                    DocumentContentType.DctPhoto, CopyAttributes.TextGraphicsOptimization.PHOTOGRAPH,
                    DocumentContentType.DctImage, CopyAttributes.TextGraphicsOptimization.GRAPHIC,
                    //DocumentContentType.DctGlossy, CopyAttributes.TextGraphicsOptimization.?
                    DocumentContentType.DctMixed, CopyAttributes.TextGraphicsOptimization.MIXED
            )
    ),
    copies(
            com.hp.ext.types.protocol.Unsigned32.class,
            Integer.class
    ),
    imagePreviewMode(
            com.hp.ext.types.imaging.ImagePreviewMode.class,
            CopyAttributes.CopyPreview.DEFAULT,
            Map.of(
                    ImagePreviewMode.IpmNone, CopyAttributes.CopyPreview.FALSE,
                    ImagePreviewMode.IpmStandard, CopyAttributes.CopyPreview.TRUE
            )
    ),
    originalMediaSize(
            com.hp.ext.types.media.MediaSizeId.class,
            ScanSize.DEFAULT,
            Map.ofEntries(
                    Map.entry(MediaSizeId.MsANSI_A_8point5x11in, ScanSize.LETTER),
                    Map.entry(MediaSizeId.MsANSI_A_Rotated_8point5x11in, ScanSize.LETTER_ROTATE),
                    Map.entry(MediaSizeId.MsANSI_B_11x17in, ScanSize.LEDGER),
                    Map.entry(MediaSizeId.MsANSI_C_17x22in, ScanSize.ANSI_C_17X22in),
                    Map.entry(MediaSizeId.MsANSI_D_22x34in, ScanSize.ANSI_D_22X34in),
                    Map.entry(MediaSizeId.MsANSI_E_34x44in, ScanSize.ANSI_E_34X44in),
                    Map.entry(MediaSizeId.MsANSI_F_28x40in, ScanSize.ANSI_F_28X40in),
                    Map.entry(MediaSizeId.MsArchitectural_A_9x12in, ScanSize.ARCHITECTURAL_A_9X12in),
                    //                  Map.entry(MediaSizeId.MsArchitectural_B_12x18in, ScanSize.?),
                    Map.entry(MediaSizeId.MsArchitectural_C_18x24in, ScanSize.ARCHITECTURAL_C_18X24in),
                    Map.entry(MediaSizeId.MsArchitectural_D_24x36in, ScanSize.ARCHITECTURAL_D_24X36in),
                    Map.entry(MediaSizeId.MsArchitectural_E_36x48in, ScanSize.ARCHITECTURAL_E_36X48in),
                    Map.entry(MediaSizeId.MsArchitectural_E1_30x42in, ScanSize.ARCHITECTURAL_E1_30X42in),
                    Map.entry(MediaSizeId.MsArchitectural_E2_26x38in, ScanSize.ARCHITECTURAL_E2_26X38in),
                    Map.entry(MediaSizeId.MsArchitectural_E3_27x39in, ScanSize.ARCHITECTURAL_E3_27X39in),
                    Map.entry(MediaSizeId.MsDIN_2A0_1189x1682mm, ScanSize.DIN_2XA0_1189X1682mm),
                    Map.entry(MediaSizeId.MsDIN_4A0_1682x2378mm, ScanSize.DIN_4XA0_1682X2378mm),
                    Map.entry(MediaSizeId.MsDL_99x210mm, ScanSize.DL_99X210mm),
                    Map.entry(MediaSizeId.MsEnvelope_A2_4point375x5point75in, ScanSize.ENVELOPE_A2),
                    Map.entry(MediaSizeId.MsEnvelope_Catalog1_6x9in, ScanSize.ENVELOPE_CATALOG),
                    Map.entry(MediaSizeId.MsEnvelope_Comm10_4point125x9point5in, ScanSize.ENVELOPE_COMM10),
                    Map.entry(MediaSizeId.MsEnvelope_Comm6point75_3point625x6point5in, ScanSize.ENVELOPE_COMM6),
                    Map.entry(MediaSizeId.MsEnvelope_DL_110x220mm, ScanSize.ENVELOPE_DL),
                    Map.entry(MediaSizeId.MsEnvelope_Monarch_3point875x7point5in, ScanSize.ENVELOPE_MONARCH),
                    Map.entry(MediaSizeId.MsEnvelope_Windsor_3point875x8point875in, ScanSize.ENVELOPE_9),
                    Map.entry(MediaSizeId.MsExecutive_7point25x10point5in, ScanSize.EXECUTIVE),
                    Map.entry(MediaSizeId.MsExecutive_Rotated_7point25x10point5in, ScanSize.EXECUTIVE_ROTATE),
                    Map.entry(MediaSizeId.MsFoolscap_8point5x13in, ScanSize.INCH8POINT5X13),
                    Map.entry(MediaSizeId.MsGeneral_10x11in, ScanSize.GENERAL_10X11in),
                    Map.entry(MediaSizeId.MsGeneral_10x13in, ScanSize.GENERAL_10X13in),
                    Map.entry(MediaSizeId.MsGeneral_10x14in, ScanSize.GENERAL_10X14in),
                    Map.entry(MediaSizeId.MsGeneral_10x15in, ScanSize.GENERAL_10X15in),
                    Map.entry(MediaSizeId.MsGeneral_11x12in, ScanSize.GENERAL_11X12in),
                    Map.entry(MediaSizeId.MsGeneral_11x14in, ScanSize.GENERAL_11X14in),
                    Map.entry(MediaSizeId.MsGeneral_11x15in, ScanSize.GENERAL_11X15in),
                    Map.entry(MediaSizeId.MsGeneral_11x19in, ScanSize.GENERAL_11X19in),
                    Map.entry(MediaSizeId.MsGeneral_12x12in, ScanSize.GENERAL_12X12in),
                    Map.entry(MediaSizeId.MsGeneral_12x14in, ScanSize.GENERAL_12X14in),
                    Map.entry(MediaSizeId.MsGeneral_12x19in, ScanSize.GENERAL_12X19in),
                    Map.entry(MediaSizeId.MsGeneral_3point5x5in, ScanSize.GENERAL_3POINT5X5in),
                    Map.entry(MediaSizeId.MsGeneral_3x5in, ScanSize.GENERAL_3X5in),
                    Map.entry(MediaSizeId.MsGeneral_4x12in, ScanSize.GENERAL_4X12in),
                    Map.entry(MediaSizeId.MsGeneral_4x6in, ScanSize.GENERAL_4X6in),
                    Map.entry(MediaSizeId.MsGeneral_4x8in, ScanSize.GENERAL_4X8in),
                    Map.entry(MediaSizeId.MsGeneral_5x7in, ScanSize.GENERAL_5X7in),
                    Map.entry(MediaSizeId.MsGeneral_5x8in, ScanSize.GENERAL_5X8in),
                    Map.entry(MediaSizeId.MsGeneral_6x8in, ScanSize.GENERAL_6X8in),
                    Map.entry(MediaSizeId.MsGeneral_7x9in, ScanSize.GENERAL_7X9in),
                    Map.entry(MediaSizeId.MsGeneral_9x11in, ScanSize.GENERAL_9X11in),
                    Map.entry(MediaSizeId.MsGovt_Legal_8x13in, ScanSize.GOVT_LEGAL),
                    Map.entry(MediaSizeId.MsGovt_Letter_8x10in, ScanSize.GOVT_LETTER),
                    Map.entry(MediaSizeId.MsInvoice_5point5x8point5in, ScanSize.STATEMENT),
                    Map.entry(MediaSizeId.MsISO_A0_841x1189mm, ScanSize.A0),
                    Map.entry(MediaSizeId.MsISO_A1_594x841mm, ScanSize.A1),
                    Map.entry(MediaSizeId.MsISO_A2_420x594mm, ScanSize.A2),
                    Map.entry(MediaSizeId.MsISO_A3_297x420mm, ScanSize.A3),
                    Map.entry(MediaSizeId.MsISO_A4_210x297mm, ScanSize.A4),
                    Map.entry(MediaSizeId.MsISO_A4_Rotated_210x297mm, ScanSize.A4_ROTATE),
                    Map.entry(MediaSizeId.MsISO_A5_148x210mm, ScanSize.A5),
                    Map.entry(MediaSizeId.MsISO_A5_Rotated_148x210mm, ScanSize.A5_ROTATE),
                    Map.entry(MediaSizeId.MsISO_A6_105x148mm, ScanSize.A6),
                    Map.entry(MediaSizeId.MsISO_A7_74x105mm, ScanSize.A7),
                    Map.entry(MediaSizeId.MsISO_A8_52x74mm, ScanSize.A8),
                    Map.entry(MediaSizeId.MsISO_A9_37x52mm, ScanSize.A9),
                    Map.entry(MediaSizeId.MsISO_A10_26x37mm, ScanSize.A10),
                    Map.entry(MediaSizeId.MsISO_B0_1000x1414mm, ScanSize.B0),
                    Map.entry(MediaSizeId.MsISO_B1_707x1000mm, ScanSize.B1),
                    Map.entry(MediaSizeId.MsISO_B2_500x707mm, ScanSize.B2),
                    Map.entry(MediaSizeId.MsISO_B3_353x500mm, ScanSize.B3),
                    Map.entry(MediaSizeId.MsISO_B4_250x353mm, ScanSize.B4),
                    Map.entry(MediaSizeId.MsISO_B5_176x250mm, ScanSize.B5),
                    Map.entry(MediaSizeId.MsISO_B6_125x176mm, ScanSize.B6),
                    Map.entry(MediaSizeId.MsISO_B7_88x125mm, ScanSize.B7),
                    Map.entry(MediaSizeId.MsISO_B8_62x88mm, ScanSize.B8),
                    Map.entry(MediaSizeId.MsISO_B9_44x62mm, ScanSize.B9),
                    Map.entry(MediaSizeId.MsISO_B10_31x44mm, ScanSize.B10),
                    Map.entry(MediaSizeId.MsISO_C0_917x1297mm, ScanSize.C0),
                    Map.entry(MediaSizeId.MsISO_C1_648x917mm, ScanSize.C1),
                    Map.entry(MediaSizeId.MsISO_C2_458x648mm, ScanSize.C2),
                    Map.entry(MediaSizeId.MsISO_C3_324x458mm, ScanSize.C3),
                    Map.entry(MediaSizeId.MsISO_C4_229x324mm, ScanSize.C4),
                    Map.entry(MediaSizeId.MsISO_C5_162x229mm, ScanSize.C5),
                    Map.entry(MediaSizeId.MsISO_C6_114x162mm, ScanSize.C6),
                    Map.entry(MediaSizeId.MsISO_C7_81x114mm, ScanSize.C7),
                    Map.entry(MediaSizeId.MsISO_C8_57x81mm, ScanSize.C8),
                    Map.entry(MediaSizeId.MsISO_C9_40x57mm, ScanSize.C9),
                    Map.entry(MediaSizeId.MsISO_C10_28x40mm, ScanSize.C10),
                    Map.entry(MediaSizeId.MsJBusinessCard_55x91mm, ScanSize.BUSINESS_CARD),
                    Map.entry(MediaSizeId.MsJDoublePostcard_148x200mm, ScanSize.JDOUBLE_POSTCARD),
                    Map.entry(MediaSizeId.MsJDoublePostcard_Rotated_148x200mm, ScanSize.JDOUBLE_POSTCARD_ROTATE),
                    Map.entry(MediaSizeId.MsJIS_B0_1030x1456mm, ScanSize.JB0),
                    Map.entry(MediaSizeId.MsJIS_B1_728x1030mm, ScanSize.JB1),
                    Map.entry(MediaSizeId.MsJIS_B2_515x728mm, ScanSize.JB2),
                    Map.entry(MediaSizeId.MsJIS_B3_364x515mm, ScanSize.JB3),
                    Map.entry(MediaSizeId.MsJIS_B4_257x364mm, ScanSize.JB4),
                    Map.entry(MediaSizeId.MsJIS_B5_182x257mm, ScanSize.JB5),
                    Map.entry(MediaSizeId.MsJIS_B5_Rotated_182x257mm, ScanSize.JB5_ROTATE),
                    Map.entry(MediaSizeId.MsJIS_B6_128x182mm, ScanSize.JB6),
                    Map.entry(MediaSizeId.MsJIS_B7_91x128mm, ScanSize.JB7),
                    Map.entry(MediaSizeId.MsJIS_B8_64x91mm, ScanSize.JB8),
                    Map.entry(MediaSizeId.MsJIS_B9_45x64mm, ScanSize.JB9),
                    Map.entry(MediaSizeId.MsJIS_B10_32x45mm, ScanSize.JB10),
                    Map.entry(MediaSizeId.MsJIS_Chou3_120x235mm, ScanSize.JCHOU3),
                    Map.entry(MediaSizeId.MsJIS_Chou4_90x205mm, ScanSize.JCHOU4),
                    Map.entry(MediaSizeId.MsJIS_Exec_216x330mm, ScanSize.JEXEC),
                    Map.entry(MediaSizeId.MsJIS_Kaku2_240x332mm, ScanSize.JKAKU2),
                    Map.entry(MediaSizeId.MsJPostcard_100x148mm, ScanSize.JPOSTCARD),
                    Map.entry(MediaSizeId.MsK16_184x260mm, ScanSize.K16_184X260mm),
                    Map.entry(MediaSizeId.MsK16_195x270mm, ScanSize.K16),
                    Map.entry(MediaSizeId.MsK16_197x273mm, ScanSize.PK16),
                    Map.entry(MediaSizeId.MsK8_260x368mm, ScanSize.K8_260X368mm),
                    Map.entry(MediaSizeId.MsK8_270x390mm, ScanSize.K8),
                    Map.entry(MediaSizeId.MsK8_273x394mm, ScanSize.PK8),
                    Map.entry(MediaSizeId.MsLegal_8point5x14in, ScanSize.LEGAL),
                    Map.entry(MediaSizeId.MsLongScan_8point5x34in, ScanSize.LONG_SCAN),
                    Map.entry(MediaSizeId.MsMutsugiri_203x254mm, ScanSize.MUTSUGIRI),
                    Map.entry(MediaSizeId.MsOficio_216x340mm, ScanSize.OFICIO),
                    Map.entry(MediaSizeId.MsRA0_860x1220mm, ScanSize.RA0),
                    Map.entry(MediaSizeId.MsRA1_610x860mm, ScanSize.RA1),
                    Map.entry(MediaSizeId.MsRA2_430x610mm, ScanSize.RA2),
                    Map.entry(MediaSizeId.MsRA3_305x430mm, ScanSize.RA3),
                    Map.entry(MediaSizeId.MsRA4_215x305mm, ScanSize.RA4),
                    Map.entry(MediaSizeId.MsSRA0_900x1280mm, ScanSize.SRA0),
                    Map.entry(MediaSizeId.MsSRA1_640x900mm, ScanSize.SRA1),
                    Map.entry(MediaSizeId.MsSRA2_450x640mm, ScanSize.SRA2),
                    Map.entry(MediaSizeId.MsSRA3_320x450mm, ScanSize.SRA3),
                    Map.entry(MediaSizeId.MsSRA4_225x320mm, ScanSize.SRA4),
                    Map.entry(MediaSizeId.MsSuper_B_13x19in, ScanSize.SUPER_B),
                    Map.entry(MediaSizeId.MsAny, ScanSize.AUTO),
                    Map.entry(MediaSizeId.MsMatchOriginal, ScanSize.MATCH_ORIGINAL),
                    Map.entry(MediaSizeId.MsMixedLetterLegal, ScanSize.MIXED_LETTER_LEGAL),
                    Map.entry(MediaSizeId.MsMixedLetterLedger, ScanSize.MIXED_LETTER_LEDGER),
                    Map.entry(MediaSizeId.MsMixedA4A3, ScanSize.MIXED_A3_A4),
                    Map.entry(MediaSizeId.MsCustom, ScanSize.CUSTOM),
                    //          Map.entry(MediaSizeId.MsOther, ScanSize.?),
                    Map.entry(MediaSizeId.MsUnknown, ScanSize.UNKNOWN),
                    Map.entry(MediaSizeId.MsUnknownEnvelope, ScanSize.UNKNOWN_ENVELOP),
                    Map.entry(MediaSizeId.MsISO_INDEXCARD_100x150mm, ScanSize.INDEXCARD),
                    Map.entry(MediaSizeId.MsAnyCustom, ScanSize.ANY_CUSTOM)
            )
    ),
    originalMediaSource(
            com.hp.ext.types.media.MediaInputId.class,
            CopyAttributes.ScanSource.DEFAULT,
            Map.of(
                    MediaInputId.MiAuto, CopyAttributes.ScanSource.AUTO,
                    MediaInputId.MiAdf, CopyAttributes.ScanSource.ADF,
                    MediaInputId.MiFlatbed, CopyAttributes.ScanSource.FLATBED
            )
    ),
    originalPlexMode(
            new CopyCustomConverterSupplier.DuplexConverter()
    ),
    outputDuplexBinding(
            com.hp.ext.types.imaging.DuplexBindingFormat.class,
            CopyAttributes.Duplex.DEFAULT,
            Map.of(
                    DuplexBindingFormat.DbfOneSided, CopyAttributes.Duplex.NONE,
                    DuplexBindingFormat.DbfDuplexLongEdge, CopyAttributes.Duplex.BOOK,
                    DuplexBindingFormat.DbfDuplexShortEdge, CopyAttributes.Duplex.FLIP
            )
    ),
    outputMediaDestination(
            com.hp.ext.types.media.MediaOutputId.class,
            CopyAttributes.OutputBin.DEFAULT,
            Map.ofEntries(
                    Map.entry(MediaOutputId.MoAccessory, OutputBin.ACCESSORY),
                    Map.entry(MediaOutputId.MoAdf, OutputBin.ADF),
                    Map.entry(MediaOutputId.MoAuto, OutputBin.AUTO),
                    Map.entry(MediaOutputId.MoBooklet, OutputBin.BOOKLET),
                    Map.entry(MediaOutputId.MoDefault, OutputBin.DEFAULT),
                    Map.entry(MediaOutputId.MoDocumentFeeder, OutputBin.DOCUMENT_FEEDER),
                    Map.entry(MediaOutputId.MoExternal, OutputBin.EXTERNAL),
                    Map.entry(MediaOutputId.MoFaceDown, OutputBin.FACE_DOWN),
                    Map.entry(MediaOutputId.MoFaceDownCorrectOrder, OutputBin.FACE_DOWN_CORRECT_ORDER),
                    Map.entry(MediaOutputId.MoFaceUp, OutputBin.FACE_UP),
                    Map.entry(MediaOutputId.MoFaceUpStraightestPath, OutputBin.FACE_UP_STRAIGHTEST_PATH),
                    Map.entry(MediaOutputId.MoFax, OutputBin.FAX),
                    Map.entry(MediaOutputId.MoFolded, OutputBin.FOLDED),
                    Map.entry(MediaOutputId.MoLeft, OutputBin.LEFT),
                    Map.entry(MediaOutputId.MoLeftStraightestPath, OutputBin.LEFT_STRAIGHTEST_PATH),
                    Map.entry(MediaOutputId.MoLower, OutputBin.LOWER),
                    Map.entry(MediaOutputId.MoLowerBooklet, OutputBin.LOWER_BOOKLET),
                    Map.entry(MediaOutputId.MoLowerLeft, OutputBin.LOWER_LEFT),
                    Map.entry(MediaOutputId.MoLowerLeftHighestCapacity, OutputBin.LOWER_LEFT_HIGHEST_CAPACITY),
                    Map.entry(MediaOutputId.MoLowerStacker, OutputBin.LOWER_STACKER),
                    Map.entry(MediaOutputId.MoMainCopier, OutputBin.MAIN_COPIER),
                    Map.entry(MediaOutputId.MoMiddle, OutputBin.MIDDLE),
                    Map.entry(MediaOutputId.MoMiddleLeft, OutputBin.MIDDLE_LEFT),
                    Map.entry(MediaOutputId.MoOutputBin1, OutputBin.OUTPUT_BIN_1),
                    Map.entry(MediaOutputId.MoOutputBin2, OutputBin.OUTPUT_BIN_2),
                    Map.entry(MediaOutputId.MoOutputBin3, OutputBin.OUTPUT_BIN_3),
                    Map.entry(MediaOutputId.MoOutputBin4, OutputBin.OUTPUT_BIN_4),
                    Map.entry(MediaOutputId.MoOutputBin5, OutputBin.OUTPUT_BIN_5),
                    Map.entry(MediaOutputId.MoOutputBin6, OutputBin.OUTPUT_BIN_6),
                    Map.entry(MediaOutputId.MoOutputBin7, OutputBin.OUTPUT_BIN_7),
                    Map.entry(MediaOutputId.MoOutputBin8, OutputBin.OUTPUT_BIN_8),
                    Map.entry(MediaOutputId.MoOutputBin9, OutputBin.OUTPUT_BIN_9),
                    Map.entry(MediaOutputId.MoOutputBin10, OutputBin.OUTPUT_BIN_10),
                    Map.entry(MediaOutputId.MoOutputBin11, OutputBin.OUTPUT_BIN_11),
                    Map.entry(MediaOutputId.MoOutputBin12, OutputBin.OUTPUT_BIN_12),
                    Map.entry(MediaOutputId.MoOutputBin13, OutputBin.OUTPUT_BIN_13),
                    Map.entry(MediaOutputId.MoOutputBin14, OutputBin.OUTPUT_BIN_14),
                    Map.entry(MediaOutputId.MoOutputBin15, OutputBin.OUTPUT_BIN_15),
                    Map.entry(MediaOutputId.MoOutputBin16, OutputBin.OUTPUT_BIN_16),
                    Map.entry(MediaOutputId.MoRear, OutputBin.REAR),
                    Map.entry(MediaOutputId.MoRearFaceUp, OutputBin.REAR_FACE_UP),
                    Map.entry(MediaOutputId.MoRearStraightestPath, OutputBin.REAR_STRAIGHTEST_PATH),
                    Map.entry(MediaOutputId.MoStacker, OutputBin.STACKER),
                    Map.entry(MediaOutputId.MoStandard, OutputBin.STANDARD),
                    Map.entry(MediaOutputId.MoStandardCorrectOrder, OutputBin.STANDARD_CORRECT_ORDER),
                    Map.entry(MediaOutputId.MoStandardTop, OutputBin.STANDARD_TOP),
                    Map.entry(MediaOutputId.MoTop, OutputBin.TOP),
                    Map.entry(MediaOutputId.MoUpper, OutputBin.UPPER),
                    Map.entry(MediaOutputId.MoUpperFaceUp, OutputBin.UPPER_FACE_UP),
                    Map.entry(MediaOutputId.MoUpperLeft, OutputBin.UPPER_LEFT),
                    Map.entry(MediaOutputId.MoUpperLeftBins, OutputBin.UPPER_LEFT_BINS),
                    Map.entry(MediaOutputId.MoUpperLeftStraightestPath, OutputBin.UPPER_LEFT_STRAIGHTEST_PATH),
                    Map.entry(MediaOutputId.MoVirtualBins1To3, OutputBin.VIRTUAL_BINS_1TO3),
                    Map.entry(MediaOutputId.MoVirtualBins1To5, OutputBin.VIRTUAL_BINS_1TO5),
                    Map.entry(MediaOutputId.MoVirtualBins1To8, OutputBin.VIRTUAL_BINS_1TO8),
                    Map.entry(MediaOutputId.MoVirtualBins1To10, OutputBin.VIRTUAL_BINS_1TO10),
                    Map.entry(MediaOutputId.MoVirtualBins2To8, OutputBin.VIRTUAL_BINS_2TO8),
                    Map.entry(MediaOutputId.MoVirtualFinisherBins, OutputBin.VIRTUAL_FINISHER_BINS),
                    Map.entry(MediaOutputId.MoVirtualLeftBins, OutputBin.VIRTUAL_LEFT_BINS),
                    //            Map.entry(MediaOutputId.MoOther, OutputBin.?),
                    Map.entry(MediaOutputId.MoAlternate, OutputBin.ALTERNATE),
                    Map.entry(MediaOutputId.MoBottom, OutputBin.BOTTOM),
                    Map.entry(MediaOutputId.MoCenter, OutputBin.CENTER),
                    Map.entry(MediaOutputId.MoCollator, OutputBin.COLLATOR),
                    Map.entry(MediaOutputId.MoDuplexer, OutputBin.DUPLEXER),
                    Map.entry(MediaOutputId.MoEngineOptionalBin1, OutputBin.ENGINE_OPTIONAL_BIN_1),
                    Map.entry(MediaOutputId.MoLargeCapacity, OutputBin.LARGE_CAPACITY),
                    Map.entry(MediaOutputId.MoMyMailbox, OutputBin.MY_MAILBOX),
                    Map.entry(MediaOutputId.MoRight, OutputBin.RIGHT),
                    Map.entry(MediaOutputId.MoSide, OutputBin.SIDE),
                    Map.entry(MediaOutputId.MoStackerFacedown, OutputBin.STACKER_FACEDOWN),
                    Map.entry(MediaOutputId.MoStackerFaceUp, OutputBin.STACKER_FACE_UP),
                    Map.entry(MediaOutputId.MoStackerStaples, OutputBin.STACKER_STAPLES),
                    Map.entry(MediaOutputId.MoUniversalOutputBin, OutputBin.UNIVERSAL_OUTPUT_BIN),
                    Map.entry(MediaOutputId.MoStapler, OutputBin.STAPLER)
            )
    ),
    outputMediaSize(
            com.hp.ext.types.media.MediaSizeId.class,
            CopyAttributes.PaperSize.DEFAULT,
            Map.ofEntries(
                    Map.entry(MediaSizeId.MsANSI_A_8point5x11in, PaperSize.LETTER),
                    Map.entry(MediaSizeId.MsANSI_A_Rotated_8point5x11in, PaperSize.LETTER_ROTATE),
                    Map.entry(MediaSizeId.MsANSI_B_11x17in, PaperSize.LEDGER),
                    Map.entry(MediaSizeId.MsANSI_C_17x22in, PaperSize.ANSI_C_17X22in),
                    Map.entry(MediaSizeId.MsANSI_D_22x34in, PaperSize.ANSI_D_22X34in),
                    Map.entry(MediaSizeId.MsANSI_E_34x44in, PaperSize.ANSI_E_34X44in),
                    Map.entry(MediaSizeId.MsANSI_F_28x40in, PaperSize.ANSI_F_28X40in),
                    Map.entry(MediaSizeId.MsArchitectural_A_9x12in, PaperSize.ARCHITECTURAL_A_9X12in),
                    //                  Map.entry(MediaSizeId.MsArchitectural_B_12x18in, PaperSize.?),
                    Map.entry(MediaSizeId.MsArchitectural_C_18x24in, PaperSize.ARCHITECTURAL_C_18X24in),
                    Map.entry(MediaSizeId.MsArchitectural_D_24x36in, PaperSize.ARCHITECTURAL_D_24X36in),
                    Map.entry(MediaSizeId.MsArchitectural_E_36x48in, PaperSize.ARCHITECTURAL_E_36X48in),
                    Map.entry(MediaSizeId.MsArchitectural_E1_30x42in, PaperSize.ARCHITECTURAL_E1_30X42in),
                    Map.entry(MediaSizeId.MsArchitectural_E2_26x38in, PaperSize.ARCHITECTURAL_E2_26X38in),
                    Map.entry(MediaSizeId.MsArchitectural_E3_27x39in, PaperSize.ARCHITECTURAL_E3_27X39in),
                    Map.entry(MediaSizeId.MsDIN_2A0_1189x1682mm, PaperSize.DIN_2XA0_1189X1682mm),
                    Map.entry(MediaSizeId.MsDIN_4A0_1682x2378mm, PaperSize.DIN_4XA0_1682X2378mm),
                    Map.entry(MediaSizeId.MsDL_99x210mm, PaperSize.DL_99X210mm),
                    Map.entry(MediaSizeId.MsEnvelope_A2_4point375x5point75in, PaperSize.ENVELOPE_A2),
                    Map.entry(MediaSizeId.MsEnvelope_Catalog1_6x9in, PaperSize.ENVELOPE_CATALOG),
                    Map.entry(MediaSizeId.MsEnvelope_Comm10_4point125x9point5in, PaperSize.ENVELOPE_COMM10),
                    Map.entry(MediaSizeId.MsEnvelope_Comm6point75_3point625x6point5in, PaperSize.ENVELOPE_COMM6),
                    Map.entry(MediaSizeId.MsEnvelope_DL_110x220mm, PaperSize.ENVELOPE_DL),
                    Map.entry(MediaSizeId.MsEnvelope_Monarch_3point875x7point5in, PaperSize.ENVELOPE_MONARCH),
                    Map.entry(MediaSizeId.MsEnvelope_Windsor_3point875x8point875in, PaperSize.ENVELOPE_9),
                    Map.entry(MediaSizeId.MsExecutive_7point25x10point5in, PaperSize.EXECUTIVE),
                    Map.entry(MediaSizeId.MsExecutive_Rotated_7point25x10point5in, PaperSize.EXECUTIVE_ROTATE),
                    Map.entry(MediaSizeId.MsFoolscap_8point5x13in, PaperSize.INCH8POINT5X13),
                    Map.entry(MediaSizeId.MsGeneral_10x11in, PaperSize.GENERAL_10X11in),
                    Map.entry(MediaSizeId.MsGeneral_10x13in, PaperSize.GENERAL_10X13in),
                    Map.entry(MediaSizeId.MsGeneral_10x14in, PaperSize.GENERAL_10X14in),
                    Map.entry(MediaSizeId.MsGeneral_10x15in, PaperSize.GENERAL_10X15in),
                    Map.entry(MediaSizeId.MsGeneral_11x12in, PaperSize.GENERAL_11X12in),
                    Map.entry(MediaSizeId.MsGeneral_11x14in, PaperSize.GENERAL_11X14in),
                    Map.entry(MediaSizeId.MsGeneral_11x15in, PaperSize.GENERAL_11X15in),
                    Map.entry(MediaSizeId.MsGeneral_11x19in, PaperSize.GENERAL_11X19in),
                    Map.entry(MediaSizeId.MsGeneral_12x12in, PaperSize.GENERAL_12X12in),
                    Map.entry(MediaSizeId.MsGeneral_12x14in, PaperSize.GENERAL_12X14in),
                    Map.entry(MediaSizeId.MsGeneral_12x19in, PaperSize.GENERAL_12X19in),
                    Map.entry(MediaSizeId.MsGeneral_3point5x5in, PaperSize.GENERAL_3POINT5X5in),
                    Map.entry(MediaSizeId.MsGeneral_3x5in, PaperSize.GENERAL_3X5in),
                    Map.entry(MediaSizeId.MsGeneral_4x12in, PaperSize.GENERAL_4X12in),
                    Map.entry(MediaSizeId.MsGeneral_4x6in, PaperSize.GENERAL_4X6in),
                    Map.entry(MediaSizeId.MsGeneral_4x8in, PaperSize.GENERAL_4X8in),
                    Map.entry(MediaSizeId.MsGeneral_5x7in, PaperSize.GENERAL_5X7in),
                    Map.entry(MediaSizeId.MsGeneral_5x8in, PaperSize.GENERAL_5X8in),
                    Map.entry(MediaSizeId.MsGeneral_6x8in, PaperSize.GENERAL_6X8in),
                    Map.entry(MediaSizeId.MsGeneral_7x9in, PaperSize.GENERAL_7X9in),
                    Map.entry(MediaSizeId.MsGeneral_9x11in, PaperSize.GENERAL_9X11in),
                    Map.entry(MediaSizeId.MsGovt_Legal_8x13in, PaperSize.GOVT_LEGAL),
                    Map.entry(MediaSizeId.MsGovt_Letter_8x10in, PaperSize.GOVT_LETTER),
                    Map.entry(MediaSizeId.MsInvoice_5point5x8point5in, PaperSize.STATEMENT),
                    Map.entry(MediaSizeId.MsISO_A0_841x1189mm, PaperSize.A0),
                    Map.entry(MediaSizeId.MsISO_A1_594x841mm, PaperSize.A1),
                    Map.entry(MediaSizeId.MsISO_A2_420x594mm, PaperSize.A2),
                    Map.entry(MediaSizeId.MsISO_A3_297x420mm, PaperSize.A3),
                    Map.entry(MediaSizeId.MsISO_A4_210x297mm, PaperSize.A4),
                    Map.entry(MediaSizeId.MsISO_A4_Rotated_210x297mm, PaperSize.A4_ROTATE),
                    Map.entry(MediaSizeId.MsISO_A5_148x210mm, PaperSize.A5),
                    Map.entry(MediaSizeId.MsISO_A5_Rotated_148x210mm, PaperSize.A5_ROTATE),
                    Map.entry(MediaSizeId.MsISO_A6_105x148mm, PaperSize.A6),
                    Map.entry(MediaSizeId.MsISO_A7_74x105mm, PaperSize.A7),
                    Map.entry(MediaSizeId.MsISO_A8_52x74mm, PaperSize.A8),
                    Map.entry(MediaSizeId.MsISO_A9_37x52mm, PaperSize.A9),
                    Map.entry(MediaSizeId.MsISO_A10_26x37mm, PaperSize.A10),
                    Map.entry(MediaSizeId.MsISO_B0_1000x1414mm, PaperSize.B0),
                    Map.entry(MediaSizeId.MsISO_B1_707x1000mm, PaperSize.B1),
                    Map.entry(MediaSizeId.MsISO_B2_500x707mm, PaperSize.B2),
                    Map.entry(MediaSizeId.MsISO_B3_353x500mm, PaperSize.B3),
                    Map.entry(MediaSizeId.MsISO_B4_250x353mm, PaperSize.B4),
                    Map.entry(MediaSizeId.MsISO_B5_176x250mm, PaperSize.B5),
                    Map.entry(MediaSizeId.MsISO_B6_125x176mm, PaperSize.B6),
                    Map.entry(MediaSizeId.MsISO_B7_88x125mm, PaperSize.B7),
                    Map.entry(MediaSizeId.MsISO_B8_62x88mm, PaperSize.B8),
                    Map.entry(MediaSizeId.MsISO_B9_44x62mm, PaperSize.B9),
                    Map.entry(MediaSizeId.MsISO_B10_31x44mm, PaperSize.B10),
                    Map.entry(MediaSizeId.MsISO_C0_917x1297mm, PaperSize.C0),
                    Map.entry(MediaSizeId.MsISO_C1_648x917mm, PaperSize.C1),
                    Map.entry(MediaSizeId.MsISO_C2_458x648mm, PaperSize.C2),
                    Map.entry(MediaSizeId.MsISO_C3_324x458mm, PaperSize.C3),
                    Map.entry(MediaSizeId.MsISO_C4_229x324mm, PaperSize.C4),
                    Map.entry(MediaSizeId.MsISO_C5_162x229mm, PaperSize.C5),
                    Map.entry(MediaSizeId.MsISO_C6_114x162mm, PaperSize.C6),
                    Map.entry(MediaSizeId.MsISO_C7_81x114mm, PaperSize.C7),
                    Map.entry(MediaSizeId.MsISO_C8_57x81mm, PaperSize.C8),
                    Map.entry(MediaSizeId.MsISO_C9_40x57mm, PaperSize.C9),
                    Map.entry(MediaSizeId.MsISO_C10_28x40mm, PaperSize.C10),
                    Map.entry(MediaSizeId.MsJBusinessCard_55x91mm, PaperSize.BUSINESS_CARD),
                    Map.entry(MediaSizeId.MsJDoublePostcard_148x200mm, PaperSize.JDOUBLE_POSTCARD),
                    Map.entry(MediaSizeId.MsJDoublePostcard_Rotated_148x200mm, PaperSize.JDOUBLE_POSTCARD_ROTATE),
                    Map.entry(MediaSizeId.MsJIS_B0_1030x1456mm, PaperSize.JB0),
                    Map.entry(MediaSizeId.MsJIS_B1_728x1030mm, PaperSize.JB1),
                    Map.entry(MediaSizeId.MsJIS_B2_515x728mm, PaperSize.JB2),
                    Map.entry(MediaSizeId.MsJIS_B3_364x515mm, PaperSize.JB3),
                    Map.entry(MediaSizeId.MsJIS_B4_257x364mm, PaperSize.JB4),
                    Map.entry(MediaSizeId.MsJIS_B5_182x257mm, PaperSize.JB5),
                    Map.entry(MediaSizeId.MsJIS_B5_Rotated_182x257mm, PaperSize.JB5_ROTATE),
                    Map.entry(MediaSizeId.MsJIS_B6_128x182mm, PaperSize.JB6),
                    Map.entry(MediaSizeId.MsJIS_B7_91x128mm, PaperSize.JB7),
                    Map.entry(MediaSizeId.MsJIS_B8_64x91mm, PaperSize.JB8),
                    Map.entry(MediaSizeId.MsJIS_B9_45x64mm, PaperSize.JB9),
                    Map.entry(MediaSizeId.MsJIS_B10_32x45mm, PaperSize.JB10),
                    Map.entry(MediaSizeId.MsJIS_Chou3_120x235mm, PaperSize.JCHOU3),
                    Map.entry(MediaSizeId.MsJIS_Chou4_90x205mm, PaperSize.JCHOU4),
                    Map.entry(MediaSizeId.MsJIS_Exec_216x330mm, PaperSize.JEXEC),
                    Map.entry(MediaSizeId.MsJIS_Kaku2_240x332mm, PaperSize.JKAKU2),
                    Map.entry(MediaSizeId.MsJPostcard_100x148mm, PaperSize.JPOSTCARD),
                    Map.entry(MediaSizeId.MsK16_184x260mm, PaperSize.K16_184X260mm),
                    Map.entry(MediaSizeId.MsK16_195x270mm, PaperSize.K16),
                    Map.entry(MediaSizeId.MsK16_197x273mm, PaperSize.PK16),
                    Map.entry(MediaSizeId.MsK8_260x368mm, PaperSize.K8_260X368mm),
                    Map.entry(MediaSizeId.MsK8_270x390mm, PaperSize.K8),
                    Map.entry(MediaSizeId.MsK8_273x394mm, PaperSize.PK8),
                    Map.entry(MediaSizeId.MsLegal_8point5x14in, PaperSize.LEGAL),
                    Map.entry(MediaSizeId.MsLongScan_8point5x34in, PaperSize.LONG_SCAN),
                    Map.entry(MediaSizeId.MsMutsugiri_203x254mm, PaperSize.MUTSUGIRI),
                    Map.entry(MediaSizeId.MsOficio_216x340mm, PaperSize.OFICIO),
                    Map.entry(MediaSizeId.MsRA0_860x1220mm, PaperSize.RA0),
                    Map.entry(MediaSizeId.MsRA1_610x860mm, PaperSize.RA1),
                    Map.entry(MediaSizeId.MsRA2_430x610mm, PaperSize.RA2),
                    Map.entry(MediaSizeId.MsRA3_305x430mm, PaperSize.RA3),
                    Map.entry(MediaSizeId.MsRA4_215x305mm, PaperSize.RA4),
                    Map.entry(MediaSizeId.MsSRA0_900x1280mm, PaperSize.SRA0),
                    Map.entry(MediaSizeId.MsSRA1_640x900mm, PaperSize.SRA1),
                    Map.entry(MediaSizeId.MsSRA2_450x640mm, PaperSize.SRA2),
                    Map.entry(MediaSizeId.MsSRA3_320x450mm, PaperSize.SRA3),
                    Map.entry(MediaSizeId.MsSRA4_225x320mm, PaperSize.SRA4),
                    Map.entry(MediaSizeId.MsSuper_B_13x19in, PaperSize.SUPER_B),
                    //                  Map.entry(MediaSizeId.MsAny, PaperSize.?),
                    Map.entry(MediaSizeId.MsMatchOriginal, PaperSize.MATCH_ORIGINAL),
                    Map.entry(MediaSizeId.MsMixedLetterLegal, PaperSize.MIXED_LETTER_LEGAL),
                    Map.entry(MediaSizeId.MsMixedLetterLedger, PaperSize.MIXED_LETTER_LEDGER),
                    Map.entry(MediaSizeId.MsMixedA4A3, PaperSize.MIXED_A3_A4),
                    Map.entry(MediaSizeId.MsCustom, PaperSize.CUSTOM),
                    //                  Map.entry(MediaSizeId.MsOther, PaperSize.?),
                    Map.entry(MediaSizeId.MsUnknown, PaperSize.UNKNOWN),
                    Map.entry(MediaSizeId.MsUnknownEnvelope, PaperSize.UNKNOWN_ENVELOP),
                    Map.entry(MediaSizeId.MsISO_INDEXCARD_100x150mm, PaperSize.INDEXCARD),
                    Map.entry(MediaSizeId.MsAnyCustom, PaperSize.ANY_CUSTOM)
            )
    ),
    outputMediaSource(
            com.hp.ext.types.media.MediaInputId.class,
            CopyAttributes.PaperSource.DEFAULT,
            Map.ofEntries(
                    Map.entry(MediaInputId.MiAdf, PaperSource.ADF),
                    Map.entry(MediaInputId.MiEnvelopeFeed, PaperSource.ENVELOPE_FEED),
                    Map.entry(MediaInputId.MiFlatbed, PaperSource.FLATBED),
                    Map.entry(MediaInputId.MiManualFeedTray, PaperSource.MANUAL_FEED),
                    Map.entry(MediaInputId.MiTray1, PaperSource.TRAY_1),
                    Map.entry(MediaInputId.MiTray2, PaperSource.TRAY_2),
                    Map.entry(MediaInputId.MiTray3, PaperSource.TRAY_3),
                    Map.entry(MediaInputId.MiTray4, PaperSource.TRAY_4),
                    Map.entry(MediaInputId.MiTray5, PaperSource.TRAY_5),
                    Map.entry(MediaInputId.MiTray6, PaperSource.TRAY_6),
                    Map.entry(MediaInputId.MiTray7, PaperSource.TRAY_7),
                    Map.entry(MediaInputId.MiTray8, PaperSource.TRAY_8),
                    Map.entry(MediaInputId.MiTray9, PaperSource.TRAY_9),
                    Map.entry(MediaInputId.MiTray10, PaperSource.TRAY_10),
                    Map.entry(MediaInputId.MiTray11, PaperSource.TRAY_11),
                    Map.entry(MediaInputId.MiTray12, PaperSource.TRAY_12),
                    Map.entry(MediaInputId.MiTray13, PaperSource.TRAY_13),
                    Map.entry(MediaInputId.MiTray14, PaperSource.TRAY_14),
                    Map.entry(MediaInputId.MiTray15, PaperSource.TRAY_15),
                    Map.entry(MediaInputId.MiTray16, PaperSource.TRAY_16),
                    Map.entry(MediaInputId.MiAuto, PaperSource.AUTO),
                    //                  Map.entry(MediaInputId.MiOther, PaperSource.?),
                    Map.entry(MediaInputId.MiDuplexer, PaperSource.DUPLEXER),
                    Map.entry(MediaInputId.MiExternal, PaperSource.EXTERNAL),
                    Map.entry(MediaInputId.MiExternalTray1, PaperSource.EXTERNAL_TRAY_1),
                    Map.entry(MediaInputId.MiExternalTray2, PaperSource.EXTERNAL_TRAY_2),
                    Map.entry(MediaInputId.MiExternalTray3, PaperSource.EXTERNAL_TRAY_3),
                    Map.entry(MediaInputId.MiExternalTray4, PaperSource.EXTERNAL_TRAY_4),
                    Map.entry(MediaInputId.MiExternalTray5, PaperSource.EXTERNAL_TRAY_5),
                    Map.entry(MediaInputId.MiExternalTray6, PaperSource.EXTERNAL_TRAY_6),
                    Map.entry(MediaInputId.MiExternalTray7, PaperSource.EXTERNAL_TRAY_7),
                    Map.entry(MediaInputId.MiExternalTray8, PaperSource.EXTERNAL_TRAY_8),
                    Map.entry(MediaInputId.MiExternalTray9, PaperSource.EXTERNAL_TRAY_9),
                    Map.entry(MediaInputId.MiExternalTray10, PaperSource.EXTERNAL_TRAY_10),
                    Map.entry(MediaInputId.MiMultipurposeTray, PaperSource.MULTIPURPOSE_TRAY),
                    Map.entry(MediaInputId.MiPhotoTray, PaperSource.PHOTO_TRAY),
                    Map.entry(MediaInputId.MiRearManualFeed, PaperSource.REAR_MANUAL_FEED),
                    Map.entry(MediaInputId.MiRoll1, PaperSource.ROLL_1),
                    Map.entry(MediaInputId.MiRoll2, PaperSource.ROLL_2),
                    Map.entry(MediaInputId.MiRoll3, PaperSource.ROLL_3),
                    Map.entry(MediaInputId.MiRoll4, PaperSource.ROLL_4),
                    Map.entry(MediaInputId.MiEnvelopeFeedJobSettings, PaperSource.ENVELOPE_FEED_JOB_SETTINGS),
                    Map.entry(MediaInputId.MiTray1JobSettings, PaperSource.TRAY_1_JOB_SETTINGS)
                    //                  Map.entry(MediaInputId.MiMdf, PaperSource.?)
            )
    ),
    outputMediaType(
            MediaTypeId.class,
            CopyAttributes.PaperType.DEFAULT,
            Map.ofEntries(
                    Map.entry(MediaTypeId.MtBond, PaperType.BOND),
                    Map.entry(MediaTypeId.MtBrochureMatte, PaperType.BROCHURE_MATTE),
                    Map.entry(MediaTypeId.MtCardstock_176to220g, PaperType.CARD_STOCK),
                    Map.entry(MediaTypeId.MtCardstockGloss_176to220g, PaperType.CARD_STOCK_GLOSSY),
                    Map.entry(MediaTypeId.MtColor, PaperType.COLORED),
                    Map.entry(MediaTypeId.MtCoverMatte, PaperType.COVER_MATTE),
                    Map.entry(MediaTypeId.MtEnvelope, PaperType.ENVELOPE),
                    Map.entry(MediaTypeId.MtExtraHeavy_131to175g, PaperType.EXTRA_HEAVY),
                    Map.entry(MediaTypeId.MtExtraHeavyGloss_131to175g, PaperType.EXTRA_HEAVY_GLOSSY),
                    Map.entry(MediaTypeId.MtHeavy_111to130g, PaperType.HEAVY),
                    Map.entry(MediaTypeId.MtHeavyEnvelope, PaperType.HEAVY_ENVELOPE),
                    Map.entry(MediaTypeId.MtHeavyGloss_111to130g, PaperType.HEAVY_GLOSSY),
                    Map.entry(MediaTypeId.MtHeavyRough, PaperType.HEAVY_ROUGH),
                    Map.entry(MediaTypeId.MtHPAdvancedPhoto, PaperType.HP_ADVANCED_PHOTO),
                    Map.entry(MediaTypeId.MtHPBrochureGloss_180g, PaperType.HP_BROCHURE_GLOSSY),
                    Map.entry(MediaTypeId.MtHPBrochureMatte_180g, PaperType.HP_BROCHURE_MATTE_180G),
                    Map.entry(MediaTypeId.MtHPCoverMatte_200g, PaperType.HP_COVER_MATTE_200G),
                    Map.entry(MediaTypeId.MtHPEcoFFICIENT, PaperType.HP_ECOFFICIENT),
                    Map.entry(MediaTypeId.MtHPEverydayPhotoMatte, PaperType.HP_EVERYDAY_PHOTO_MATTE),
                    Map.entry(MediaTypeId.MtHPGloss_130g, PaperType.HP_GLOSSY_120G),
                    Map.entry(MediaTypeId.MtHPGloss_160g, PaperType.HP_GLOSSY_150G),
                    Map.entry(MediaTypeId.MtHPGloss_220g, PaperType.HP_GLOSSY_200G),
                    Map.entry(MediaTypeId.MtHPMatte_90g, PaperType.HP_MATTE_90G),
                    Map.entry(MediaTypeId.MtHPMatte_105g, PaperType.HP_MATTE_105G),
                    Map.entry(MediaTypeId.MtHPMatte_120g, PaperType.HP_MATTE_120G),
                    Map.entry(MediaTypeId.MtHPMatte_160g, PaperType.HP_MATTE_150G),
                    Map.entry(MediaTypeId.MtHPMatteBrochureAndFlyer_180g, PaperType.HP_MATTE_BROCHURE_AND_FLYER_180G),
                    Map.entry(MediaTypeId.MtHPPhoto, PaperType.HP_PHOTO),
                    Map.entry(MediaTypeId.MtHPPhotoPlus, PaperType.HP_PHOTO_PLUS),
                    Map.entry(MediaTypeId.MtHPPremiumInkjetTransparency, PaperType.HP_PREMIUM_INKJET_TRANSPARENCY),
                    Map.entry(MediaTypeId.MtHPPremiumMatte_120g, PaperType.HP_PREMIUM_MATTE_120G),
                    Map.entry(MediaTypeId.MtHPSoftGloss_120g, PaperType.HP_SOFT_GLOSS_120G),
                    Map.entry(MediaTypeId.MtHPTough, PaperType.HP_TOUGH),
                    Map.entry(MediaTypeId.MtHPTrifoldGloss_160g, PaperType.HP_TRIFOLD_GLOSSY_160G),
                    Map.entry(MediaTypeId.MtIntermediate_85to95g, PaperType.INTERMEDIATE),
                    Map.entry(MediaTypeId.MtLabels, PaperType.LABELS),
                    Map.entry(MediaTypeId.MtLetterhead, PaperType.LETTERHEAD),
                    Map.entry(MediaTypeId.MtLight_60to74g, PaperType.LIGHT),
                    Map.entry(MediaTypeId.MtMatte, PaperType.MATTE),
                    Map.entry(MediaTypeId.MtMidweight_96to110g, PaperType.MID_WEIGHT),
                    Map.entry(MediaTypeId.MtMidweightGloss_96to110g, PaperType.MID_WEIGHT_GLOSSY),
                    Map.entry(MediaTypeId.MtOpaqueFilm, PaperType.OPAQUE_FILM),
                    Map.entry(MediaTypeId.MtPhoto, PaperType.PHOTO),
                    Map.entry(MediaTypeId.MtPlain, PaperType.PLAIN),
//                    Map.entry(MediaTypeId.MtPremiumInkjet, PaperType. ?),
                    Map.entry(MediaTypeId.MtPreprinted, PaperType.PREPRINTED),
                    Map.entry(MediaTypeId.MtPrepunched, PaperType.PREPUNCHED),
                    Map.entry(MediaTypeId.MtRecycled, PaperType.RECYCLED),
                    Map.entry(MediaTypeId.MtRough, PaperType.ROUGH),
                    Map.entry(MediaTypeId.MtShelfEdgeLabels, PaperType.SHELF_EDGE_LABELS),
                    Map.entry(MediaTypeId.MtTab, PaperType.TAB),
                    Map.entry(MediaTypeId.MtThickPlain, PaperType.THICK_PLAIN),
                    Map.entry(MediaTypeId.MtTransparency, PaperType.TRANSPARENCY),
                    Map.entry(MediaTypeId.MtVellum, PaperType.VELLUM),
                    Map.entry(MediaTypeId.MtUserDefined1, PaperType.USER_DEFINED_1),
                    Map.entry(MediaTypeId.MtUserDefined2, PaperType.USER_DEFINED_2),
                    Map.entry(MediaTypeId.MtUserDefined3, PaperType.USER_DEFINED_3),
                    Map.entry(MediaTypeId.MtUserDefined4, PaperType.USER_DEFINED_4),
                    Map.entry(MediaTypeId.MtUserDefined5, PaperType.USER_DEFINED_5),
                    Map.entry(MediaTypeId.MtUserDefined6, PaperType.USER_DEFINED_6),
                    Map.entry(MediaTypeId.MtUserDefined7, PaperType.USER_DEFINED_7),
                    Map.entry(MediaTypeId.MtUserDefined8, PaperType.USER_DEFINED_8),
                    Map.entry(MediaTypeId.MtUserDefined9, PaperType.USER_DEFINED_9),
                    Map.entry(MediaTypeId.MtUserDefined10, PaperType.USER_DEFINED_10),
                    Map.entry(MediaTypeId.MtUserDefined11, PaperType.USER_DEFINED_11),
                    Map.entry(MediaTypeId.MtUserDefined12, PaperType.USER_DEFINED_12),
                    Map.entry(MediaTypeId.MtUserDefined13, PaperType.USER_DEFINED_13),
                    Map.entry(MediaTypeId.MtUserDefined14, PaperType.USER_DEFINED_14),
                    Map.entry(MediaTypeId.MtUserDefined15, PaperType.USER_DEFINED_15),
                    Map.entry(MediaTypeId.MtUserDefined16, PaperType.USER_DEFINED_16),
                    Map.entry(MediaTypeId.MtAny, PaperType.ANY),
//                    Map.entry(MediaTypeId.MtOther, PaperType. ?),
                    Map.entry(MediaTypeId.MtHPMatte_200g, PaperType.HP_MATTE_200G),
                    Map.entry(MediaTypeId.MtLightBond, PaperType.LIGHT_BOND),
                    Map.entry(MediaTypeId.MtLightPaperboard, PaperType.LIGHT_PAPERBOARD),
                    Map.entry(MediaTypeId.MtLightRough, PaperType.LIGHT_ROUGH),
                    Map.entry(MediaTypeId.MtPaperboard, PaperType.PAPERBOARD),
                    Map.entry(MediaTypeId.MtAuto, PaperType.AUTO),
                    Map.entry(MediaTypeId.MtHeavyBond, PaperType.HEAVY_BOND),
                    Map.entry(MediaTypeId.MtHeavyPaperboard, PaperType.HEAVY_PAPERBOARD),
                    Map.entry(MediaTypeId.MtHPGlossyEdgeline180G, PaperType.HP_GLOSSY_EDGELINE_180G)
            )
    ),
    pagesPerSheet(
            com.hp.ext.types.imaging.PagesPerSheetType.class,
            CopyAttributes.NumberUpMode.DEFAULT,
            Map.of(
                    PagesPerSheetType.PpsOneUp, CopyAttributes.NumberUpMode.DEFAULT,
                    PagesPerSheetType.PpsTwoUp, CopyAttributes.NumberUpMode.TWO_UP,
                    PagesPerSheetType.PpsFourUp, CopyAttributes.NumberUpMode.FOUR_UP,
                    PagesPerSheetType.PpsEightUp, CopyAttributes.NumberUpMode.EIGHT_UP
            )
    ),
    scaleSelection(
            ScaleSelectionType.class,
            CopyAttributes.ScaleMode.DEFAULT,
            Map.of(
                    ScaleSelectionType.SstNone, CopyAttributes.ScaleMode.AUTO,
                    ScaleSelectionType.SstCustom, CopyAttributes.ScaleMode.MANUAL
//                    ScaleSelectionType.SstFitToPage, CopyAttributes.ScaleMode.?,
//                    ScaleSelectionType.SstFullPage, CopyAttributes.ScaleMode.?,
//                    ScaleSelectionType.SstLegalToLetter, CopyAttributes.ScaleMode.?,
//                    ScaleSelectionType.SstA4ToLetter, CopyAttributes.ScaleMode.?,
//                    ScaleSelectionType.SstLetterToA4, CopyAttributes.ScaleMode.?,
//                    ScaleSelectionType.?, CopyAttributes.ScaleMode.AUTO_MARGINS_INCLUDED,
            )
    ),
    scanCaptureMode(
            ScanCaptureMode.class,
            CopyAttributes.CaptureMode.DEFAULT,
            Map.of(
                    ScanCaptureMode.ScmStandard, CopyAttributes.CaptureMode.STANDARD,
//                    ScanCaptureMode.ScmJobBuild, CopyAttributes.CaptureMode. ?,
                    ScanCaptureMode.ScmBook, CopyAttributes.CaptureMode.BOOK_CAPTURE
//                    ScanCaptureMode.ScmIdCard, CopyAttributes.CaptureMode. ?,
//                    ScanCaptureMode. ?, CopyAttributes.CaptureMode.ID_CAPTURE_PROMPT_BOTH_SIDES,
//                    ScanCaptureMode. ?, CopyAttributes.CaptureMode.ID_CAPTURE_PROMPT_BACK_SIDE_ONLY,
//                    ScanCaptureMode. ?, CopyAttributes.CaptureMode.STANDARD_ADD_PAGES
            )
    ),
    scanProgressMode(
            ScanProgressMode.class,
            CopyAttributes.ProgressDialogMode.DEFAULT,
            Map.of(
                    ScanProgressMode.SpmNone, CopyAttributes.ProgressDialogMode.OFF,
                    ScanProgressMode.SpmStandard, CopyAttributes.ProgressDialogMode.ON
            )
    );

    private final Category category;
    private final ITypeConverter<?, ?> converter;

    /**
     * Enum constructor for creating a default (1:1 mapping) type converter
     *
     * @param e2Type   E2 Class type
     * @param wDefault Workpath API Default value
     * @param mapping  1:1 mapping between E2 and Workpath API types
     * @param <E>      E2 type
     * @param <W>      Workpath API type
     */
    <E, W> CopyTypeMapping(Class<E> e2Type, W wDefault, Map<E, W> mapping) {
        category = Category.DEFAULT;
        converter = new DefaultTypeConverter<E, W>(e2Type, wDefault) {{
            mapEtoW.putAll(mapping);
        }};
    }

    /**
     * Enum constructor for creating a range type converter
     *
     * @param wRangeSize Workpath API Enum size (The number of entries in the Workpath API Enum except DEFAULT enum)
     * @param wEnumType  Workpath API Enum type
     * @param wDefault   Workpath API default value
     * @param wMin       Workpath API minimum value in the enum
     * @param wMax       Workpath API maximum value in the enum
     * @param <W>        Workpath API Enum types
     */
    <W> CopyTypeMapping(int wRangeSize, Class<W> wEnumType, W wDefault, W wMin, W wMax) {
        category = Category.ENUM_RANGE;
        converter = new RangeTypeConverter<W>(wRangeSize, wEnumType, wDefault, wMin, wMax);
    }

    /**
     * Enum constructor for creating a custom type converter
     *
     * @param customConverterSupplier Supplier for the custom type converter
     */
    CopyTypeMapping(Supplier<ITypeConverter> customConverterSupplier) {
        this.category = Category.CUSTOM;
        converter = customConverterSupplier.get();
    }

    /**
     * Enum constructor for creating a numeric range type converter
     *
     * @param e2Type E2 Class type
     * @param wType  Workpath API type
     * @param <E>    E2 type
     * @param <W>    Workpath API type
     */
    <E, W> CopyTypeMapping(Class<E> e2Type, Class<W> wType) {
        if (wType.equals(Integer.class)) {
            category = Category.INTEGER_RANGE;
            converter = new IntRangeTypeConverter<E>(e2Type);
        } else {
            throw new IllegalArgumentException("CopyTypeMapping: Unsupported Workpath API type for numeric range " +
                    "converter: " + wType);
        }
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
     * Undefined Workpath API types
     * Workpath does not have a corresponding type that maps to E2 types
     */
    public static class UndefinedWorkpathApiType {
    }
}
