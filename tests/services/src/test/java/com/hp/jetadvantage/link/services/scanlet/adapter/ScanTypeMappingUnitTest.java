package com.hp.jetadvantage.link.services.scanlet.adapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import androidx.core.util.Pair;

import com.hp.ext.service.scanJob.TransmissionMode;
import com.hp.ext.types.imaging.AutoCropModeType;
import com.hp.ext.types.imaging.BindingFormat;
import com.hp.ext.types.imaging.BlankPageDetectionMode;
import com.hp.ext.types.imaging.ColorMode;
import com.hp.ext.types.imaging.CompressionType;
import com.hp.ext.types.imaging.ContentOrientation;
import com.hp.ext.types.imaging.DocumentContentType;
import com.hp.ext.types.imaging.FileFormat;
import com.hp.ext.types.imaging.ImagePreviewMode;
import com.hp.ext.types.imaging.PlexMode;
import com.hp.ext.types.imaging.QualityVsSize;
import com.hp.ext.types.imaging.Resolution;
import com.hp.ext.types.imaging.ScanCaptureMode;
import com.hp.ext.types.imaging.ScanProgressMode;
import com.hp.ext.types.job.JobActivityState;
import com.hp.ext.types.media.MediaInputId;
import com.hp.ext.types.media.MediaSizeId;
import com.hp.ext.types.protocol.Signed64;
import com.hp.ext.types.protocol.Unsigned32;
import com.hp.jetadvantage.link.api.job.ScanJobState;
import com.hp.jetadvantage.link.api.scanner.FileOptionsAttributes;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.ScanSize;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.device.services.converter.ITypeConverter;
import com.hp.jetadvantage.link.device.services.converter.ITypeMapping;
import com.hp.jetadvantage.link.device.services.converter.RangeTypeConverter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class ScanTypeMappingUnitTest {

    // ========================================================================================
    // Phase 4: Structural Tests
    // ========================================================================================

    @Test
    public void GivenScanTypeMapping_WhenValuesRequested_ThenAllEntriesExist() {
        assertEquals("ScanTypeMapping should have 26 entries", 26, ScanTypeMapping.values().length);
    }

    @Test
    public void GivenScanTypeMapping_WhenGetConverterCalled_ThenNoneReturnsNull() {
        for (ScanTypeMapping mapping : ScanTypeMapping.values()) {
            assertNotNull("Converter should not be null for " + mapping.name(), mapping.getConverter());
        }
    }

    @Test
    public void GivenScanTypeMapping_WhenCategoryChecked_ThenAllMatch() {
        // DEFAULT mappings
        ScanTypeMapping[] defaultMappings = {
                ScanTypeMapping.autoDeskew, ScanTypeMapping.blankPageSuppression,
                ScanTypeMapping.colorAndGrayCompression, ScanTypeMapping.colorMode,
                ScanTypeMapping.contentOrientation, ScanTypeMapping.contentType,
                ScanTypeMapping.fileTransmissionMode, ScanTypeMapping.imagePreviewMode,
                ScanTypeMapping.mediaSize, ScanTypeMapping.mediaSource,
                ScanTypeMapping.misfeedDetection, ScanTypeMapping.monoCompression,
                ScanTypeMapping.outputFileCompression, ScanTypeMapping.outputFileEncryption,
                ScanTypeMapping.outputFileFormat, ScanTypeMapping.outputQualityVsSize,
                ScanTypeMapping.resolution, ScanTypeMapping.scanCaptureMode,
                ScanTypeMapping.scanProgressMode, ScanTypeMapping.jobActivityState
        };
        for (ScanTypeMapping m : defaultMappings) {
            assertEquals(m.name() + " should be DEFAULT", ITypeMapping.Category.DEFAULT, m.getCategory());
        }

        // ENUM_RANGE mappings
        ScanTypeMapping[] rangeMappings = {
                ScanTypeMapping.backgroundCleanup, ScanTypeMapping.contrast,
                ScanTypeMapping.exposureLevel, ScanTypeMapping.sharpness
        };
        for (ScanTypeMapping m : rangeMappings) {
            assertEquals(m.name() + " should be ENUM_RANGE", ITypeMapping.Category.ENUM_RANGE, m.getCategory());
        }

        // CUSTOM mappings
        assertEquals("autoCropMode should be CUSTOM", ITypeMapping.Category.CUSTOM, ScanTypeMapping.autoCropMode.getCategory());
        assertEquals("duplex should be CUSTOM", ITypeMapping.Category.CUSTOM, ScanTypeMapping.duplex.getCategory());
    }

    // ========================================================================================
    // Phase 1: DEFAULT (1:1) Mapping Tests — autoDeskew
    // ========================================================================================

    @Test
    public void GivenScanTypeMapping_WhenAutoDeskewConvertEtoWCalled_ThenReturnConvertedWorkpathEnum() {
        assertEquals(ScanAttributes.AutomaticStraightenMode.ENABLE, ScanTypeMapping.autoDeskew.convertEtoW(Boolean.TRUE));
        assertEquals(ScanAttributes.AutomaticStraightenMode.DISABLE, ScanTypeMapping.autoDeskew.convertEtoW(Boolean.FALSE));
    }

    @Test
    public void GivenScanTypeMapping_WhenAutoDeskewConvertWtoECalled_ThenReturnConvertedE2Type() {
        assertEquals(Boolean.TRUE, ScanTypeMapping.autoDeskew.convertWtoE(ScanAttributes.AutomaticStraightenMode.ENABLE));
        assertEquals(Boolean.FALSE, ScanTypeMapping.autoDeskew.convertWtoE(ScanAttributes.AutomaticStraightenMode.DISABLE));
    }

    @Test
    public void GivenScanTypeMapping_WhenAutoDeskewRoundTrip_ThenPreserveOriginalValue() {
        ScanAttributes.AutomaticStraightenMode w = ScanTypeMapping.autoDeskew.convertEtoW(Boolean.TRUE);
        assertEquals(Boolean.TRUE, ScanTypeMapping.autoDeskew.convertWtoE(w));
        w = ScanTypeMapping.autoDeskew.convertEtoW(Boolean.FALSE);
        assertEquals(Boolean.FALSE, ScanTypeMapping.autoDeskew.convertWtoE(w));
    }

    // ========================================================================================
    // Phase 1: blankPageSuppression
    // ========================================================================================

    @Test
    public void GivenScanTypeMapping_WhenBlankPageSuppressionConvertEtoWCalled_ThenReturnConvertedWorkpathEnum() {
        assertEquals(ScanAttributes.BlankImageRemovalMode.OFF, ScanTypeMapping.blankPageSuppression.convertEtoW(BlankPageDetectionMode.BpdDisable));
        assertEquals(ScanAttributes.BlankImageRemovalMode.ON, ScanTypeMapping.blankPageSuppression.convertEtoW(BlankPageDetectionMode.BpdDetectAndSuppress));
    }

    @Test
    public void GivenScanTypeMapping_WhenBlankPageSuppressionConvertWtoECalled_ThenReturnConvertedE2Type() {
        assertEquals(BlankPageDetectionMode.BpdDisable, ScanTypeMapping.blankPageSuppression.convertWtoE(ScanAttributes.BlankImageRemovalMode.OFF));
        assertEquals(BlankPageDetectionMode.BpdDetectAndSuppress, ScanTypeMapping.blankPageSuppression.convertWtoE(ScanAttributes.BlankImageRemovalMode.ON));
    }

    @Test
    public void GivenScanTypeMapping_WhenBlankPageSuppressionRoundTrip_ThenPreserveOriginalValue() {
        for (BlankPageDetectionMode e2 : new BlankPageDetectionMode[]{BlankPageDetectionMode.BpdDisable, BlankPageDetectionMode.BpdDetectAndSuppress}) {
            assertEquals(e2.getValue(), e2, ScanTypeMapping.blankPageSuppression.convertWtoE(ScanTypeMapping.blankPageSuppression.convertEtoW(e2)));
        }
    }

    // ========================================================================================
    // Phase 1: colorAndGrayCompression
    // ========================================================================================

    @Test
    public void GivenScanTypeMapping_WhenColorAndGrayCompressionConvertEtoWCalled_ThenReturnConvertedWorkpathEnum() {
        assertEquals(FileOptionsAttributes.TiffCompressionMode.JPEG_TIFF_6, ScanTypeMapping.colorAndGrayCompression.convertEtoW(CompressionType.CtOJpeg));
        assertEquals(FileOptionsAttributes.TiffCompressionMode.JPEG_TTN_2, ScanTypeMapping.colorAndGrayCompression.convertEtoW(CompressionType.CtJpeg));
        assertEquals(FileOptionsAttributes.TiffCompressionMode.LZW, ScanTypeMapping.colorAndGrayCompression.convertEtoW(CompressionType.CtLzw));
        assertEquals(FileOptionsAttributes.TiffCompressionMode.HIGH, ScanTypeMapping.colorAndGrayCompression.convertEtoW(CompressionType.CtAuto));
    }

    @Test
    public void GivenScanTypeMapping_WhenColorAndGrayCompressionConvertWtoECalled_ThenReturnConvertedE2Type() {
        assertEquals(CompressionType.CtOJpeg, ScanTypeMapping.colorAndGrayCompression.convertWtoE(FileOptionsAttributes.TiffCompressionMode.JPEG_TIFF_6));
        assertEquals(CompressionType.CtJpeg, ScanTypeMapping.colorAndGrayCompression.convertWtoE(FileOptionsAttributes.TiffCompressionMode.JPEG_TTN_2));
        assertEquals(CompressionType.CtLzw, ScanTypeMapping.colorAndGrayCompression.convertWtoE(FileOptionsAttributes.TiffCompressionMode.LZW));
        assertEquals(CompressionType.CtAuto, ScanTypeMapping.colorAndGrayCompression.convertWtoE(FileOptionsAttributes.TiffCompressionMode.HIGH));
    }

    @Test
    public void GivenScanTypeMapping_WhenColorAndGrayCompressionRoundTrip_ThenPreserveOriginalValue() {
        for (CompressionType e2 : new CompressionType[]{CompressionType.CtOJpeg, CompressionType.CtJpeg, CompressionType.CtLzw, CompressionType.CtAuto}) {
            assertEquals(e2.getValue(), e2, ScanTypeMapping.colorAndGrayCompression.convertWtoE(ScanTypeMapping.colorAndGrayCompression.convertEtoW(e2)));
        }
    }

    // ========================================================================================
    // Phase 1: colorMode
    // ========================================================================================

    @Test
    public void GivenScanTypeMapping_WhenColorModeConvertEtoWCalled_ThenReturnConvertedWorkpathEnum() {
        assertEquals(ScanAttributes.ColorMode.AUTO, ScanTypeMapping.colorMode.convertEtoW(ColorMode.CmAutoDetect));
        assertEquals(ScanAttributes.ColorMode.MONO, ScanTypeMapping.colorMode.convertEtoW(ColorMode.CmMonochrome));
        assertEquals(ScanAttributes.ColorMode.GRAY, ScanTypeMapping.colorMode.convertEtoW(ColorMode.CmGrayscale));
        assertEquals(ScanAttributes.ColorMode.COLOR, ScanTypeMapping.colorMode.convertEtoW(ColorMode.CmColor));
    }

    @Test
    public void GivenScanTypeMapping_WhenColorModeConvertWtoECalled_ThenReturnConvertedE2Type() {
        assertEquals(ColorMode.CmAutoDetect, ScanTypeMapping.colorMode.convertWtoE(ScanAttributes.ColorMode.AUTO));
        assertEquals(ColorMode.CmMonochrome, ScanTypeMapping.colorMode.convertWtoE(ScanAttributes.ColorMode.MONO));
        assertEquals(ColorMode.CmGrayscale, ScanTypeMapping.colorMode.convertWtoE(ScanAttributes.ColorMode.GRAY));
        assertEquals(ColorMode.CmColor, ScanTypeMapping.colorMode.convertWtoE(ScanAttributes.ColorMode.COLOR));
    }

    @Test
    public void GivenScanTypeMapping_WhenColorModeRoundTrip_ThenPreserveOriginalValue() {
        for (ColorMode e2 : new ColorMode[]{ColorMode.CmAutoDetect, ColorMode.CmMonochrome, ColorMode.CmGrayscale, ColorMode.CmColor}) {
            assertEquals(e2.getValue(), e2, ScanTypeMapping.colorMode.convertWtoE(ScanTypeMapping.colorMode.convertEtoW(e2)));
        }
    }

    // ========================================================================================
    // Phase 1: contentOrientation
    // ========================================================================================

    @Test
    public void GivenScanTypeMapping_WhenContentOrientationConvertEtoWCalled_ThenReturnConvertedWorkpathEnum() {
        assertEquals(ScanAttributes.Orientation.PORTRAIT, ScanTypeMapping.contentOrientation.convertEtoW(ContentOrientation.CoPortrait));
        assertEquals(ScanAttributes.Orientation.LANDSCAPE, ScanTypeMapping.contentOrientation.convertEtoW(ContentOrientation.CoLandscape));
        assertEquals(ScanAttributes.Orientation.AUTO_DETECT, ScanTypeMapping.contentOrientation.convertEtoW(ContentOrientation.CoAutoDetect));
    }

    @Test
    public void GivenScanTypeMapping_WhenContentOrientationConvertWtoECalled_ThenReturnConvertedE2Type() {
        assertEquals(ContentOrientation.CoPortrait, ScanTypeMapping.contentOrientation.convertWtoE(ScanAttributes.Orientation.PORTRAIT));
        assertEquals(ContentOrientation.CoLandscape, ScanTypeMapping.contentOrientation.convertWtoE(ScanAttributes.Orientation.LANDSCAPE));
        assertEquals(ContentOrientation.CoAutoDetect, ScanTypeMapping.contentOrientation.convertWtoE(ScanAttributes.Orientation.AUTO_DETECT));
    }

    @Test
    public void GivenScanTypeMapping_WhenContentOrientationRoundTrip_ThenPreserveOriginalValue() {
        for (ContentOrientation e2 : new ContentOrientation[]{ContentOrientation.CoPortrait, ContentOrientation.CoLandscape, ContentOrientation.CoAutoDetect}) {
            assertEquals(e2.getValue(), e2, ScanTypeMapping.contentOrientation.convertWtoE(ScanTypeMapping.contentOrientation.convertEtoW(e2)));
        }
    }

    // ========================================================================================
    // Phase 1: contentType
    // ========================================================================================

    @Test
    public void GivenScanTypeMapping_WhenContentTypeConvertEtoWCalled_ThenReturnConvertedWorkpathEnum() {
        assertEquals(ScanAttributes.TextPhotoOptimization.TEXT, ScanTypeMapping.contentType.convertEtoW(DocumentContentType.DctText));
        assertEquals(ScanAttributes.TextPhotoOptimization.MIXED_2, ScanTypeMapping.contentType.convertEtoW(DocumentContentType.DctMixed));
        assertEquals(ScanAttributes.TextPhotoOptimization.GRAPHIC, ScanTypeMapping.contentType.convertEtoW(DocumentContentType.DctImage));
    }

    @Test
    public void GivenScanTypeMapping_WhenContentTypeConvertWtoECalled_ThenReturnConvertedE2Type() {
        assertEquals(DocumentContentType.DctText, ScanTypeMapping.contentType.convertWtoE(ScanAttributes.TextPhotoOptimization.TEXT));
        assertEquals(DocumentContentType.DctMixed, ScanTypeMapping.contentType.convertWtoE(ScanAttributes.TextPhotoOptimization.MIXED_2));
        assertEquals(DocumentContentType.DctImage, ScanTypeMapping.contentType.convertWtoE(ScanAttributes.TextPhotoOptimization.GRAPHIC));
    }

    @Test
    public void GivenScanTypeMapping_WhenContentTypeRoundTrip_ThenPreserveOriginalValue() {
        for (DocumentContentType e2 : new DocumentContentType[]{DocumentContentType.DctText, DocumentContentType.DctMixed, DocumentContentType.DctImage}) {
            assertEquals(e2.getValue(), e2, ScanTypeMapping.contentType.convertWtoE(ScanTypeMapping.contentType.convertEtoW(e2)));
        }
    }

    // ========================================================================================
    // Phase 1: fileTransmissionMode
    // ========================================================================================

    @Test
    public void GivenScanTypeMapping_WhenFileTransmissionModeConvertEtoWCalled_ThenReturnConvertedWorkpathEnum() {
        assertEquals(ScanAttributes.TransmissionMode.IMAGE, ScanTypeMapping.fileTransmissionMode.convertEtoW(TransmissionMode.TmImage));
        assertEquals(ScanAttributes.TransmissionMode.JOB, ScanTypeMapping.fileTransmissionMode.convertEtoW(TransmissionMode.TmJob));
    }

    @Test
    public void GivenScanTypeMapping_WhenFileTransmissionModeConvertWtoECalled_ThenReturnConvertedE2Type() {
        assertEquals(TransmissionMode.TmImage, ScanTypeMapping.fileTransmissionMode.convertWtoE(ScanAttributes.TransmissionMode.IMAGE));
        assertEquals(TransmissionMode.TmJob, ScanTypeMapping.fileTransmissionMode.convertWtoE(ScanAttributes.TransmissionMode.JOB));
    }

    @Test
    public void GivenScanTypeMapping_WhenFileTransmissionModeRoundTrip_ThenPreserveOriginalValue() {
        for (TransmissionMode e2 : new TransmissionMode[]{TransmissionMode.TmImage, TransmissionMode.TmJob}) {
            assertEquals(e2.getValue(), e2, ScanTypeMapping.fileTransmissionMode.convertWtoE(ScanTypeMapping.fileTransmissionMode.convertEtoW(e2)));
        }
    }

    // ========================================================================================
    // Phase 1: imagePreviewMode
    // ========================================================================================

    @Test
    public void GivenScanTypeMapping_WhenImagePreviewModeConvertEtoWCalled_ThenReturnConvertedWorkpathEnum() {
        assertEquals(ScanAttributes.ScanPreview.TRUE, ScanTypeMapping.imagePreviewMode.convertEtoW(ImagePreviewMode.IpmStandard));
        assertEquals(ScanAttributes.ScanPreview.FALSE, ScanTypeMapping.imagePreviewMode.convertEtoW(ImagePreviewMode.IpmNone));
    }

    @Test
    public void GivenScanTypeMapping_WhenImagePreviewModeConvertWtoECalled_ThenReturnConvertedE2Type() {
        assertEquals(ImagePreviewMode.IpmStandard, ScanTypeMapping.imagePreviewMode.convertWtoE(ScanAttributes.ScanPreview.TRUE));
        assertEquals(ImagePreviewMode.IpmNone, ScanTypeMapping.imagePreviewMode.convertWtoE(ScanAttributes.ScanPreview.FALSE));
    }

    @Test
    public void GivenScanTypeMapping_WhenImagePreviewModeRoundTrip_ThenPreserveOriginalValue() {
        for (ImagePreviewMode e2 : new ImagePreviewMode[]{ImagePreviewMode.IpmStandard, ImagePreviewMode.IpmNone}) {
            assertEquals(e2.getValue(), e2, ScanTypeMapping.imagePreviewMode.convertWtoE(ScanTypeMapping.imagePreviewMode.convertEtoW(e2)));
        }
    }

    // ========================================================================================
    // Phase 1: mediaSize (56 entries — loop-based)
    // ========================================================================================

    @Test
    public void GivenScanTypeMapping_WhenMediaSizeConvertEtoWCalled_ThenAllEntriesMapCorrectly() {
        Map<MediaSizeId, ScanSize> expected = Map.ofEntries(
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
                Map.entry(MediaSizeId.MsInvoice_5point5x8point5in, ScanSize.STATEMENT)
        );
        for (Map.Entry<MediaSizeId, ScanSize> entry : expected.entrySet()) {
            assertEquals(entry.getKey().getValue(), entry.getValue(), ScanTypeMapping.mediaSize.convertEtoW(entry.getKey()));
        }
    }

    @Test
    public void GivenScanTypeMapping_WhenMediaSizeConvertEtoWCalled_ThenExtendedEntriesMapCorrectly() {
        Map<MediaSizeId, ScanSize> expected = Map.ofEntries(
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
                Map.entry(MediaSizeId.MsK8_260x368mm, ScanSize.K8_260X368mm)
        );
        for (Map.Entry<MediaSizeId, ScanSize> entry : expected.entrySet()) {
            assertEquals(entry.getKey().getValue(), entry.getValue(), ScanTypeMapping.mediaSize.convertEtoW(entry.getKey()));
        }
    }

    @Test
    public void GivenScanTypeMapping_WhenMediaSizeConvertEtoWCalled_ThenAdditionalEntriesMapCorrectly() {
        Map<MediaSizeId, ScanSize> expected = Map.ofEntries(
                Map.entry(MediaSizeId.MsK16_184x260mm, ScanSize.K16_184X260mm),
                Map.entry(MediaSizeId.MsMixedLetterLedger, ScanSize.MIXEDLETTERLEDGER),
                Map.entry(MediaSizeId.MsOficio_216x340mm, ScanSize.OFICIO),
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
        );
        for (Map.Entry<MediaSizeId, ScanSize> entry : expected.entrySet()) {
            assertEquals(entry.getKey().getValue(), entry.getValue(), ScanTypeMapping.mediaSize.convertEtoW(entry.getKey()));
        }
    }

    @Test
    public void GivenScanTypeMapping_WhenMediaSizeConvertWtoECalled_ThenSampleEntriesMapCorrectly() {
        assertEquals(MediaSizeId.MsANSI_A_8point5x11in, ScanTypeMapping.mediaSize.convertWtoE(ScanSize.LETTER));
        assertEquals(MediaSizeId.MsISO_A4_210x297mm, ScanTypeMapping.mediaSize.convertWtoE(ScanSize.A4));
        assertEquals(MediaSizeId.MsLegal_8point5x14in, ScanTypeMapping.mediaSize.convertWtoE(ScanSize.LEGAL));
        assertEquals(MediaSizeId.MsAny, ScanTypeMapping.mediaSize.convertWtoE(ScanSize.AUTO));
        assertEquals(MediaSizeId.MsISO_A3_297x420mm, ScanTypeMapping.mediaSize.convertWtoE(ScanSize.A3));
        assertEquals(MediaSizeId.MsANSI_B_11x17in, ScanTypeMapping.mediaSize.convertWtoE(ScanSize.LEDGER));
    }

    @Test
    public void GivenScanTypeMapping_WhenMediaSizeRoundTrip_ThenPreserveOriginalValue() {
        MediaSizeId[] samples = {
                MediaSizeId.MsANSI_A_8point5x11in, MediaSizeId.MsISO_A4_210x297mm,
                MediaSizeId.MsLegal_8point5x14in, MediaSizeId.MsAny,
                MediaSizeId.MsISO_A3_297x420mm, MediaSizeId.MsANSI_B_11x17in,
                MediaSizeId.MsEnvelope_DL_110x220mm, MediaSizeId.MsSRA3_320x450mm
        };
        for (MediaSizeId e2 : samples) {
            assertEquals(e2.getValue(), e2, ScanTypeMapping.mediaSize.convertWtoE(ScanTypeMapping.mediaSize.convertEtoW(e2)));
        }
    }

    // ========================================================================================
    // Phase 1: mediaSource
    // ========================================================================================

    @Test
    public void GivenScanTypeMapping_WhenMediaSourceConvertEtoWCalled_ThenReturnConvertedWorkpathEnum() {
        assertEquals(ScanAttributes.MediaSource.ADF, ScanTypeMapping.mediaSource.convertEtoW(MediaInputId.MiAdf));
        assertEquals(ScanAttributes.MediaSource.FLATBED, ScanTypeMapping.mediaSource.convertEtoW(MediaInputId.MiFlatbed));
        assertEquals(ScanAttributes.MediaSource.AUTO, ScanTypeMapping.mediaSource.convertEtoW(MediaInputId.MiAuto));
    }

    @Test
    public void GivenScanTypeMapping_WhenMediaSourceConvertWtoECalled_ThenReturnConvertedE2Type() {
        assertEquals(MediaInputId.MiAdf, ScanTypeMapping.mediaSource.convertWtoE(ScanAttributes.MediaSource.ADF));
        assertEquals(MediaInputId.MiFlatbed, ScanTypeMapping.mediaSource.convertWtoE(ScanAttributes.MediaSource.FLATBED));
        assertEquals(MediaInputId.MiAuto, ScanTypeMapping.mediaSource.convertWtoE(ScanAttributes.MediaSource.AUTO));
    }

    @Test
    public void GivenScanTypeMapping_WhenMediaSourceRoundTrip_ThenPreserveOriginalValue() {
        for (MediaInputId e2 : new MediaInputId[]{MediaInputId.MiAdf, MediaInputId.MiFlatbed, MediaInputId.MiAuto}) {
            assertEquals(e2.getValue(), e2, ScanTypeMapping.mediaSource.convertWtoE(ScanTypeMapping.mediaSource.convertEtoW(e2)));
        }
    }

    // ========================================================================================
    // Phase 1: misfeedDetection
    // ========================================================================================

    @Test
    public void GivenScanTypeMapping_WhenMisfeedDetectionConvertEtoWCalled_ThenReturnConvertedWorkpathEnum() {
        assertEquals(ScanAttributes.MisfeedDetectionMode.OFF, ScanTypeMapping.misfeedDetection.convertEtoW(Boolean.FALSE));
        assertEquals(ScanAttributes.MisfeedDetectionMode.ON, ScanTypeMapping.misfeedDetection.convertEtoW(Boolean.TRUE));
    }

    @Test
    public void GivenScanTypeMapping_WhenMisfeedDetectionConvertWtoECalled_ThenReturnConvertedE2Type() {
        assertEquals(Boolean.FALSE, ScanTypeMapping.misfeedDetection.convertWtoE(ScanAttributes.MisfeedDetectionMode.OFF));
        assertEquals(Boolean.TRUE, ScanTypeMapping.misfeedDetection.convertWtoE(ScanAttributes.MisfeedDetectionMode.ON));
    }

    @Test
    public void GivenScanTypeMapping_WhenMisfeedDetectionRoundTrip_ThenPreserveOriginalValue() {
        assertEquals(Boolean.FALSE, ScanTypeMapping.misfeedDetection.convertWtoE(ScanTypeMapping.misfeedDetection.convertEtoW(Boolean.FALSE)));
        assertEquals(Boolean.TRUE, ScanTypeMapping.misfeedDetection.convertWtoE(ScanTypeMapping.misfeedDetection.convertEtoW(Boolean.TRUE)));
    }

    // ========================================================================================
    // Phase 1: monoCompression
    // ========================================================================================

    @Test
    public void GivenScanTypeMapping_WhenMonoCompressionConvertEtoWCalled_ThenReturnConvertedWorkpathEnum() {
        assertEquals(FileOptionsAttributes.TiffCompressionMode.HIGH, ScanTypeMapping.monoCompression.convertEtoW(CompressionType.CtAuto));
        assertEquals(FileOptionsAttributes.TiffCompressionMode.G_3, ScanTypeMapping.monoCompression.convertEtoW(CompressionType.CtG3));
        assertEquals(FileOptionsAttributes.TiffCompressionMode.G_4, ScanTypeMapping.monoCompression.convertEtoW(CompressionType.CtG4));
        assertEquals(FileOptionsAttributes.TiffCompressionMode.LZW, ScanTypeMapping.monoCompression.convertEtoW(CompressionType.CtLzw));
    }

    @Test
    public void GivenScanTypeMapping_WhenMonoCompressionConvertWtoECalled_ThenReturnConvertedE2Type() {
        assertEquals(CompressionType.CtAuto, ScanTypeMapping.monoCompression.convertWtoE(FileOptionsAttributes.TiffCompressionMode.HIGH));
        assertEquals(CompressionType.CtG3, ScanTypeMapping.monoCompression.convertWtoE(FileOptionsAttributes.TiffCompressionMode.G_3));
        assertEquals(CompressionType.CtG4, ScanTypeMapping.monoCompression.convertWtoE(FileOptionsAttributes.TiffCompressionMode.G_4));
        assertEquals(CompressionType.CtLzw, ScanTypeMapping.monoCompression.convertWtoE(FileOptionsAttributes.TiffCompressionMode.LZW));
    }

    @Test
    public void GivenScanTypeMapping_WhenMonoCompressionRoundTrip_ThenPreserveOriginalValue() {
        for (CompressionType e2 : new CompressionType[]{CompressionType.CtAuto, CompressionType.CtG3, CompressionType.CtG4, CompressionType.CtLzw}) {
            assertEquals(e2.getValue(), e2, ScanTypeMapping.monoCompression.convertWtoE(ScanTypeMapping.monoCompression.convertEtoW(e2)));
        }
    }

    // ========================================================================================
    // Phase 1: outputFileCompression
    // ========================================================================================

    @Test
    public void GivenScanTypeMapping_WhenOutputFileCompressionConvertEtoWCalled_ThenReturnConvertedWorkpathEnum() {
        assertEquals(FileOptionsAttributes.PdfCompressionMode.NORMAL, ScanTypeMapping.outputFileCompression.convertEtoW(Boolean.FALSE));
        assertEquals(FileOptionsAttributes.PdfCompressionMode.HIGH, ScanTypeMapping.outputFileCompression.convertEtoW(Boolean.TRUE));
    }

    @Test
    public void GivenScanTypeMapping_WhenOutputFileCompressionConvertWtoECalled_ThenReturnConvertedE2Type() {
        assertEquals(Boolean.FALSE, ScanTypeMapping.outputFileCompression.convertWtoE(FileOptionsAttributes.PdfCompressionMode.NORMAL));
        assertEquals(Boolean.TRUE, ScanTypeMapping.outputFileCompression.convertWtoE(FileOptionsAttributes.PdfCompressionMode.HIGH));
    }

    @Test
    public void GivenScanTypeMapping_WhenOutputFileCompressionRoundTrip_ThenPreserveOriginalValue() {
        assertEquals(Boolean.FALSE, ScanTypeMapping.outputFileCompression.convertWtoE(ScanTypeMapping.outputFileCompression.convertEtoW(Boolean.FALSE)));
        assertEquals(Boolean.TRUE, ScanTypeMapping.outputFileCompression.convertWtoE(ScanTypeMapping.outputFileCompression.convertEtoW(Boolean.TRUE)));
    }

    // ========================================================================================
    // Phase 1: outputFileEncryption
    // ========================================================================================

    @Test
    public void GivenScanTypeMapping_WhenOutputFileEncryptionConvertEtoWCalled_ThenReturnConvertedWorkpathEnum() {
        assertEquals(ScanTypeMapping.UndefinedWorkpathApiType.outputFileEncryption.OFF, ScanTypeMapping.outputFileEncryption.convertEtoW(Boolean.FALSE));
        assertEquals(ScanTypeMapping.UndefinedWorkpathApiType.outputFileEncryption.ON, ScanTypeMapping.outputFileEncryption.convertEtoW(Boolean.TRUE));
    }

    @Test
    public void GivenScanTypeMapping_WhenOutputFileEncryptionConvertWtoECalled_ThenReturnConvertedE2Type() {
        assertEquals(Boolean.FALSE, ScanTypeMapping.outputFileEncryption.convertWtoE(ScanTypeMapping.UndefinedWorkpathApiType.outputFileEncryption.OFF));
        assertEquals(Boolean.TRUE, ScanTypeMapping.outputFileEncryption.convertWtoE(ScanTypeMapping.UndefinedWorkpathApiType.outputFileEncryption.ON));
    }

    @Test
    public void GivenScanTypeMapping_WhenOutputFileEncryptionRoundTrip_ThenPreserveOriginalValue() {
        assertEquals(Boolean.FALSE, ScanTypeMapping.outputFileEncryption.convertWtoE(ScanTypeMapping.outputFileEncryption.convertEtoW(Boolean.FALSE)));
        assertEquals(Boolean.TRUE, ScanTypeMapping.outputFileEncryption.convertWtoE(ScanTypeMapping.outputFileEncryption.convertEtoW(Boolean.TRUE)));
    }

    // ========================================================================================
    // Phase 1: outputFileFormat (12 entries)
    // ========================================================================================

    @Test
    public void GivenScanTypeMapping_WhenOutputFileFormatConvertEtoWCalled_ThenReturnConvertedWorkpathEnum() {
        assertEquals(ScanAttributes.DocumentFormat.JPEG, ScanTypeMapping.outputFileFormat.convertEtoW(FileFormat.FfJpeg));
        assertEquals(ScanAttributes.DocumentFormat.PDF, ScanTypeMapping.outputFileFormat.convertEtoW(FileFormat.FfPdf));
        assertEquals(ScanAttributes.DocumentFormat.TIFF, ScanTypeMapping.outputFileFormat.convertEtoW(FileFormat.FfTiff));
        assertEquals(ScanAttributes.DocumentFormat.MTIFF, ScanTypeMapping.outputFileFormat.convertEtoW(FileFormat.FfMtiff));
        assertEquals(ScanAttributes.DocumentFormat.OCR_PDF_TEXT_UNDER_IMAGE, ScanTypeMapping.outputFileFormat.convertEtoW(FileFormat.FfOcrPdf));
        assertEquals(ScanAttributes.DocumentFormat.OCR_PDF_A_TEXT_UNDER_IMAGE, ScanTypeMapping.outputFileFormat.convertEtoW(FileFormat.FfOcrPdfa));
        assertEquals(ScanAttributes.DocumentFormat.OCR_CSV, ScanTypeMapping.outputFileFormat.convertEtoW(FileFormat.FfOcrCsv));
        assertEquals(ScanAttributes.DocumentFormat.OCR_HTML, ScanTypeMapping.outputFileFormat.convertEtoW(FileFormat.FfOcrHtml));
        assertEquals(ScanAttributes.DocumentFormat.OCR_RTF, ScanTypeMapping.outputFileFormat.convertEtoW(FileFormat.FfOcrRtf));
        assertEquals(ScanAttributes.DocumentFormat.OCR_TEXT, ScanTypeMapping.outputFileFormat.convertEtoW(FileFormat.FfOcrText));
        assertEquals(ScanAttributes.DocumentFormat.OCR_UNICODE_TEXT, ScanTypeMapping.outputFileFormat.convertEtoW(FileFormat.FfOcrUnicode));
        assertEquals(ScanAttributes.DocumentFormat.PDF_A, ScanTypeMapping.outputFileFormat.convertEtoW(FileFormat.FfPdfa));
    }

    @Test
    public void GivenScanTypeMapping_WhenOutputFileFormatConvertWtoECalled_ThenReturnConvertedE2Type() {
        assertEquals(FileFormat.FfJpeg, ScanTypeMapping.outputFileFormat.convertWtoE(ScanAttributes.DocumentFormat.JPEG));
        assertEquals(FileFormat.FfPdf, ScanTypeMapping.outputFileFormat.convertWtoE(ScanAttributes.DocumentFormat.PDF));
        assertEquals(FileFormat.FfTiff, ScanTypeMapping.outputFileFormat.convertWtoE(ScanAttributes.DocumentFormat.TIFF));
        assertEquals(FileFormat.FfMtiff, ScanTypeMapping.outputFileFormat.convertWtoE(ScanAttributes.DocumentFormat.MTIFF));
        assertEquals(FileFormat.FfOcrPdf, ScanTypeMapping.outputFileFormat.convertWtoE(ScanAttributes.DocumentFormat.OCR_PDF_TEXT_UNDER_IMAGE));
        assertEquals(FileFormat.FfOcrPdfa, ScanTypeMapping.outputFileFormat.convertWtoE(ScanAttributes.DocumentFormat.OCR_PDF_A_TEXT_UNDER_IMAGE));
        assertEquals(FileFormat.FfOcrCsv, ScanTypeMapping.outputFileFormat.convertWtoE(ScanAttributes.DocumentFormat.OCR_CSV));
        assertEquals(FileFormat.FfOcrHtml, ScanTypeMapping.outputFileFormat.convertWtoE(ScanAttributes.DocumentFormat.OCR_HTML));
        assertEquals(FileFormat.FfOcrRtf, ScanTypeMapping.outputFileFormat.convertWtoE(ScanAttributes.DocumentFormat.OCR_RTF));
        assertEquals(FileFormat.FfOcrText, ScanTypeMapping.outputFileFormat.convertWtoE(ScanAttributes.DocumentFormat.OCR_TEXT));
        assertEquals(FileFormat.FfOcrUnicode, ScanTypeMapping.outputFileFormat.convertWtoE(ScanAttributes.DocumentFormat.OCR_UNICODE_TEXT));
        assertEquals(FileFormat.FfPdfa, ScanTypeMapping.outputFileFormat.convertWtoE(ScanAttributes.DocumentFormat.PDF_A));
    }

    @Test
    public void GivenScanTypeMapping_WhenOutputFileFormatRoundTrip_ThenPreserveOriginalValue() {
        FileFormat[] allFormats = {
                FileFormat.FfJpeg, FileFormat.FfPdf, FileFormat.FfTiff, FileFormat.FfMtiff,
                FileFormat.FfOcrPdf, FileFormat.FfOcrPdfa, FileFormat.FfOcrCsv, FileFormat.FfOcrHtml,
                FileFormat.FfOcrRtf, FileFormat.FfOcrText, FileFormat.FfOcrUnicode, FileFormat.FfPdfa
        };
        for (FileFormat e2 : allFormats) {
            assertEquals(e2.getValue(), e2, ScanTypeMapping.outputFileFormat.convertWtoE(ScanTypeMapping.outputFileFormat.convertEtoW(e2)));
        }
    }

    // ========================================================================================
    // Phase 1: outputQualityVsSize
    // ========================================================================================

    @Test
    public void GivenScanTypeMapping_WhenOutputQualityVsSizeConvertEtoWCalled_ThenReturnConvertedWorkpathEnum() {
        assertEquals(ScanAttributes.OutputQuality.LOW, ScanTypeMapping.outputQualityVsSize.convertEtoW(QualityVsSize.QvsLow));
        assertEquals(ScanAttributes.OutputQuality.MEDIUM, ScanTypeMapping.outputQualityVsSize.convertEtoW(QualityVsSize.QvsMedium));
        assertEquals(ScanAttributes.OutputQuality.HIGH, ScanTypeMapping.outputQualityVsSize.convertEtoW(QualityVsSize.QvsHigh));
    }

    @Test
    public void GivenScanTypeMapping_WhenOutputQualityVsSizeConvertWtoECalled_ThenReturnConvertedE2Type() {
        assertEquals(QualityVsSize.QvsLow, ScanTypeMapping.outputQualityVsSize.convertWtoE(ScanAttributes.OutputQuality.LOW));
        assertEquals(QualityVsSize.QvsMedium, ScanTypeMapping.outputQualityVsSize.convertWtoE(ScanAttributes.OutputQuality.MEDIUM));
        assertEquals(QualityVsSize.QvsHigh, ScanTypeMapping.outputQualityVsSize.convertWtoE(ScanAttributes.OutputQuality.HIGH));
    }

    @Test
    public void GivenScanTypeMapping_WhenOutputQualityVsSizeRoundTrip_ThenPreserveOriginalValue() {
        for (QualityVsSize e2 : new QualityVsSize[]{QualityVsSize.QvsLow, QualityVsSize.QvsMedium, QualityVsSize.QvsHigh}) {
            assertEquals(e2.getValue(), e2, ScanTypeMapping.outputQualityVsSize.convertWtoE(ScanTypeMapping.outputQualityVsSize.convertEtoW(e2)));
        }
    }

    // ========================================================================================
    // Phase 1: resolution (9 entries)
    // ========================================================================================

    @Test
    public void GivenScanTypeMapping_WhenResolutionConvertEtoWCalled_ThenReturnConvertedWorkpathEnum() {
        assertEquals(ScanAttributes.Resolution.DPI_75, ScanTypeMapping.resolution.convertEtoW(Resolution.Dpi75));
        assertEquals(ScanAttributes.Resolution.DPI_100, ScanTypeMapping.resolution.convertEtoW(Resolution.Dpi100));
        assertEquals(ScanAttributes.Resolution.DPI_150, ScanTypeMapping.resolution.convertEtoW(Resolution.Dpi150));
        assertEquals(ScanAttributes.Resolution.DPI_200, ScanTypeMapping.resolution.convertEtoW(Resolution.Dpi200));
        assertEquals(ScanAttributes.Resolution.DPI_240, ScanTypeMapping.resolution.convertEtoW(Resolution.Dpi240));
        assertEquals(ScanAttributes.Resolution.DPI_300, ScanTypeMapping.resolution.convertEtoW(Resolution.Dpi300));
        assertEquals(ScanAttributes.Resolution.DPI_400, ScanTypeMapping.resolution.convertEtoW(Resolution.Dpi400));
        assertEquals(ScanAttributes.Resolution.DPI_500, ScanTypeMapping.resolution.convertEtoW(Resolution.Dpi500));
        assertEquals(ScanAttributes.Resolution.DPI_600, ScanTypeMapping.resolution.convertEtoW(Resolution.Dpi600));
    }

    @Test
    public void GivenScanTypeMapping_WhenResolutionConvertWtoECalled_ThenReturnConvertedE2Type() {
        assertEquals(Resolution.Dpi75, ScanTypeMapping.resolution.convertWtoE(ScanAttributes.Resolution.DPI_75));
        assertEquals(Resolution.Dpi100, ScanTypeMapping.resolution.convertWtoE(ScanAttributes.Resolution.DPI_100));
        assertEquals(Resolution.Dpi150, ScanTypeMapping.resolution.convertWtoE(ScanAttributes.Resolution.DPI_150));
        assertEquals(Resolution.Dpi200, ScanTypeMapping.resolution.convertWtoE(ScanAttributes.Resolution.DPI_200));
        assertEquals(Resolution.Dpi240, ScanTypeMapping.resolution.convertWtoE(ScanAttributes.Resolution.DPI_240));
        assertEquals(Resolution.Dpi300, ScanTypeMapping.resolution.convertWtoE(ScanAttributes.Resolution.DPI_300));
        assertEquals(Resolution.Dpi400, ScanTypeMapping.resolution.convertWtoE(ScanAttributes.Resolution.DPI_400));
        assertEquals(Resolution.Dpi500, ScanTypeMapping.resolution.convertWtoE(ScanAttributes.Resolution.DPI_500));
        assertEquals(Resolution.Dpi600, ScanTypeMapping.resolution.convertWtoE(ScanAttributes.Resolution.DPI_600));
    }

    @Test
    public void GivenScanTypeMapping_WhenResolutionRoundTrip_ThenPreserveOriginalValue() {
        Resolution[] all = {
                Resolution.Dpi75, Resolution.Dpi100, Resolution.Dpi150, Resolution.Dpi200,
                Resolution.Dpi240, Resolution.Dpi300, Resolution.Dpi400, Resolution.Dpi500, Resolution.Dpi600
        };
        for (Resolution e2 : all) {
            assertEquals(e2.getValue(), e2, ScanTypeMapping.resolution.convertWtoE(ScanTypeMapping.resolution.convertEtoW(e2)));
        }
    }

    // ========================================================================================
    // Phase 1: scanCaptureMode
    // ========================================================================================

    @Test
    public void GivenScanTypeMapping_WhenScanCaptureModeConvertEtoWCalled_ThenReturnConvertedWorkpathEnum() {
        assertEquals(ScanAttributes.CaptureMode.STANDARD, ScanTypeMapping.scanCaptureMode.convertEtoW(ScanCaptureMode.ScmStandard));
        assertEquals(ScanAttributes.CaptureMode.STANDARD_ADD_PAGES, ScanTypeMapping.scanCaptureMode.convertEtoW(ScanCaptureMode.ScmJobBuild));
        assertEquals(ScanAttributes.CaptureMode.BOOK_CAPTURE, ScanTypeMapping.scanCaptureMode.convertEtoW(ScanCaptureMode.ScmBook));
        assertEquals(ScanAttributes.CaptureMode.ID_CAPTURE_PROMPT_BOTH_SIDES, ScanTypeMapping.scanCaptureMode.convertEtoW(ScanCaptureMode.ScmIdCard));
    }

    @Test
    public void GivenScanTypeMapping_WhenScanCaptureModeConvertWtoECalled_ThenReturnConvertedE2Type() {
        assertEquals(ScanCaptureMode.ScmStandard, ScanTypeMapping.scanCaptureMode.convertWtoE(ScanAttributes.CaptureMode.STANDARD));
        assertEquals(ScanCaptureMode.ScmJobBuild, ScanTypeMapping.scanCaptureMode.convertWtoE(ScanAttributes.CaptureMode.STANDARD_ADD_PAGES));
        assertEquals(ScanCaptureMode.ScmBook, ScanTypeMapping.scanCaptureMode.convertWtoE(ScanAttributes.CaptureMode.BOOK_CAPTURE));
        assertEquals(ScanCaptureMode.ScmIdCard, ScanTypeMapping.scanCaptureMode.convertWtoE(ScanAttributes.CaptureMode.ID_CAPTURE_PROMPT_BOTH_SIDES));
    }

    @Test
    public void GivenScanTypeMapping_WhenScanCaptureModeRoundTrip_ThenPreserveOriginalValue() {
        for (ScanCaptureMode e2 : new ScanCaptureMode[]{ScanCaptureMode.ScmStandard, ScanCaptureMode.ScmJobBuild, ScanCaptureMode.ScmBook, ScanCaptureMode.ScmIdCard}) {
            assertEquals(e2.getValue(), e2, ScanTypeMapping.scanCaptureMode.convertWtoE(ScanTypeMapping.scanCaptureMode.convertEtoW(e2)));
        }
    }

    // ========================================================================================
    // Phase 1: scanProgressMode
    // ========================================================================================

    @Test
    public void GivenScanTypeMapping_WhenScanProgressModeConvertEtoWCalled_ThenReturnConvertedWorkpathEnum() {
        assertEquals(ScanAttributes.ProgressDialogMode.OFF, ScanTypeMapping.scanProgressMode.convertEtoW(ScanProgressMode.SpmNone));
        assertEquals(ScanAttributes.ProgressDialogMode.ON, ScanTypeMapping.scanProgressMode.convertEtoW(ScanProgressMode.SpmStandard));
    }

    @Test
    public void GivenScanTypeMapping_WhenScanProgressModeConvertWtoECalled_ThenReturnConvertedE2Type() {
        assertEquals(ScanProgressMode.SpmNone, ScanTypeMapping.scanProgressMode.convertWtoE(ScanAttributes.ProgressDialogMode.OFF));
        assertEquals(ScanProgressMode.SpmStandard, ScanTypeMapping.scanProgressMode.convertWtoE(ScanAttributes.ProgressDialogMode.ON));
    }

    @Test
    public void GivenScanTypeMapping_WhenScanProgressModeRoundTrip_ThenPreserveOriginalValue() {
        for (ScanProgressMode e2 : new ScanProgressMode[]{ScanProgressMode.SpmNone, ScanProgressMode.SpmStandard}) {
            assertEquals(e2.getValue(), e2, ScanTypeMapping.scanProgressMode.convertWtoE(ScanTypeMapping.scanProgressMode.convertEtoW(e2)));
        }
    }

    // ========================================================================================
    // Phase 1: jobActivityState
    // ========================================================================================

    @Test
    public void GivenScanTypeMapping_WhenJobActivityStateConvertEtoWCalled_ThenReturnConvertedWorkpathEnum() {
        assertEquals(ScanJobState.ActivityState.NOT_STARTED, ScanTypeMapping.jobActivityState.convertEtoW(JobActivityState.JasNotStarted));
        assertEquals(ScanJobState.ActivityState.STARTED, ScanTypeMapping.jobActivityState.convertEtoW(JobActivityState.JasStarted));
        assertEquals(ScanJobState.ActivityState.COMPLETED, ScanTypeMapping.jobActivityState.convertEtoW(JobActivityState.JasCompleted));
    }

    @Test
    public void GivenScanTypeMapping_WhenJobActivityStateConvertWtoECalled_ThenReturnConvertedE2Type() {
        assertEquals(JobActivityState.JasNotStarted, ScanTypeMapping.jobActivityState.convertWtoE(ScanJobState.ActivityState.NOT_STARTED));
        assertEquals(JobActivityState.JasStarted, ScanTypeMapping.jobActivityState.convertWtoE(ScanJobState.ActivityState.STARTED));
        assertEquals(JobActivityState.JasCompleted, ScanTypeMapping.jobActivityState.convertWtoE(ScanJobState.ActivityState.COMPLETED));
    }

    @Test
    public void GivenScanTypeMapping_WhenJobActivityStateRoundTrip_ThenPreserveOriginalValue() {
        for (JobActivityState e2 : new JobActivityState[]{JobActivityState.JasNotStarted, JobActivityState.JasStarted, JobActivityState.JasCompleted}) {
            assertEquals(e2.getValue(), e2, ScanTypeMapping.jobActivityState.convertWtoE(ScanTypeMapping.jobActivityState.convertEtoW(e2)));
        }
    }

    // ========================================================================================
    // Phase 2: ENUM_RANGE Tests — backgroundCleanup (9 levels, range 0-100)
    // ========================================================================================

    @Test
    public void GivenScanTypeMapping_WhenBackgroundCleanupConvertEtoWCalled_ThenBoundariesMapCorrectly() {
        RangeTypeConverter converter = (RangeTypeConverter) ScanTypeMapping.backgroundCleanup.getConverter();
        converter.setRange(new Signed64(0L), new Signed64(100L), new Unsigned32(1L));
        assertEquals(ScanAttributes.BackgroundCleanup.LEVEL_0, converter.convertEtoW(new Unsigned32(0L)));
        assertEquals(ScanAttributes.BackgroundCleanup.LEVEL_4, converter.convertEtoW(new Unsigned32(50L)));
        assertEquals(ScanAttributes.BackgroundCleanup.LEVEL_8, converter.convertEtoW(new Unsigned32(100L)));
    }

    @Test
    public void GivenScanTypeMapping_WhenBackgroundCleanupConvertWtoECalled_ThenAllLevelsMapCorrectly() {
        RangeTypeConverter converter = (RangeTypeConverter) ScanTypeMapping.backgroundCleanup.getConverter();
        converter.setRange(new Signed64(0L), new Signed64(100L), new Unsigned32(1L));

        assertEquals(new Unsigned32(0L).getValue(), ((Unsigned32) ScanTypeMapping.backgroundCleanup.convertWtoE(ScanAttributes.BackgroundCleanup.LEVEL_0)).getValue());
        assertEquals(new Unsigned32(13L).getValue(), ((Unsigned32) ScanTypeMapping.backgroundCleanup.convertWtoE(ScanAttributes.BackgroundCleanup.LEVEL_1)).getValue());
        assertEquals(new Unsigned32(100L).getValue(), ((Unsigned32) ScanTypeMapping.backgroundCleanup.convertWtoE(ScanAttributes.BackgroundCleanup.LEVEL_8)).getValue());
    }

    @Test(expected = RuntimeException.class)
    public void GivenScanTypeMapping_WhenBackgroundCleanupConvertEtoWCalledWithoutSetRange_ThenThrowException() {
        // Create a new converter without calling setRange
        RangeTypeConverter freshConverter = new RangeTypeConverter<>(9, ScanAttributes.BackgroundCleanup.class,
                ScanAttributes.BackgroundCleanup.DEFAULT, ScanAttributes.BackgroundCleanup.LEVEL_0, ScanAttributes.BackgroundCleanup.LEVEL_8);
        freshConverter.convertEtoW(new Unsigned32(50L));
    }

    // ========================================================================================
    // Phase 2: contrast (9 levels, range 0-8)
    // ========================================================================================

    @Test
    public void GivenScanTypeMapping_WhenContrastConvertEtoWCalled_ThenBoundariesMapCorrectly() {
        RangeTypeConverter converter = (RangeTypeConverter) ScanTypeMapping.contrast.getConverter();
        converter.setRange(new Signed64(0L), new Signed64(8L), new Unsigned32(1L));
        assertEquals(ScanAttributes.ContrastAdjustment.LEVEL_0, converter.convertEtoW(new Unsigned32(0L)));
        assertEquals(ScanAttributes.ContrastAdjustment.LEVEL_4, converter.convertEtoW(new Unsigned32(4L)));
        assertEquals(ScanAttributes.ContrastAdjustment.LEVEL_8, converter.convertEtoW(new Unsigned32(8L)));
    }

    @Test
    public void GivenScanTypeMapping_WhenContrastConvertWtoECalled_ThenAllLevelsMapCorrectly() {
        RangeTypeConverter converter = (RangeTypeConverter) ScanTypeMapping.contrast.getConverter();
        converter.setRange(new Signed64(0L), new Signed64(8L), new Unsigned32(1L));
        assertEquals(new Unsigned32(0L).getValue(), ((Unsigned32) ScanTypeMapping.contrast.convertWtoE(ScanAttributes.ContrastAdjustment.LEVEL_0)).getValue());
        assertEquals(new Unsigned32(4L).getValue(), ((Unsigned32) ScanTypeMapping.contrast.convertWtoE(ScanAttributes.ContrastAdjustment.LEVEL_4)).getValue());
        assertEquals(new Unsigned32(8L).getValue(), ((Unsigned32) ScanTypeMapping.contrast.convertWtoE(ScanAttributes.ContrastAdjustment.LEVEL_8)).getValue());
    }

    @Test
    public void GivenScanTypeMapping_WhenContrastRoundTrip_ThenBoundariesPreserved() {
        RangeTypeConverter converter = (RangeTypeConverter) ScanTypeMapping.contrast.getConverter();
        converter.setRange(new Signed64(0L), new Signed64(8L), new Unsigned32(1L));
        // Min boundary
        Unsigned32 e2Min = new Unsigned32(0L);
        assertEquals(e2Min.getValue(), ((Unsigned32) ScanTypeMapping.contrast.convertWtoE(ScanTypeMapping.contrast.convertEtoW(e2Min))).getValue());
        // Max boundary
        Unsigned32 e2Max = new Unsigned32(8L);
        assertEquals(e2Max.getValue(), ((Unsigned32) ScanTypeMapping.contrast.convertWtoE(ScanTypeMapping.contrast.convertEtoW(e2Max))).getValue());
    }

    // ========================================================================================
    // Phase 2: exposureLevel (9 levels, range 0-8)
    // ========================================================================================

    @Test
    public void GivenScanTypeMapping_WhenExposureLevelConvertEtoWCalled_ThenBoundariesMapCorrectly() {
        RangeTypeConverter converter = (RangeTypeConverter) ScanTypeMapping.exposureLevel.getConverter();
        converter.setRange(new Signed64(0L), new Signed64(8L), new Unsigned32(1L));
        assertEquals(ScanAttributes.DarknessAdjustment.LEVEL_0, converter.convertEtoW(new Unsigned32(0L)));
        assertEquals(ScanAttributes.DarknessAdjustment.LEVEL_4, converter.convertEtoW(new Unsigned32(4L)));
        assertEquals(ScanAttributes.DarknessAdjustment.LEVEL_8, converter.convertEtoW(new Unsigned32(8L)));
    }

    @Test
    public void GivenScanTypeMapping_WhenExposureLevelConvertWtoECalled_ThenAllLevelsMapCorrectly() {
        RangeTypeConverter converter = (RangeTypeConverter) ScanTypeMapping.exposureLevel.getConverter();
        converter.setRange(new Signed64(0L), new Signed64(8L), new Unsigned32(1L));
        assertEquals(new Unsigned32(0L).getValue(), ((Unsigned32) ScanTypeMapping.exposureLevel.convertWtoE(ScanAttributes.DarknessAdjustment.LEVEL_0)).getValue());
        assertEquals(new Unsigned32(4L).getValue(), ((Unsigned32) ScanTypeMapping.exposureLevel.convertWtoE(ScanAttributes.DarknessAdjustment.LEVEL_4)).getValue());
        assertEquals(new Unsigned32(8L).getValue(), ((Unsigned32) ScanTypeMapping.exposureLevel.convertWtoE(ScanAttributes.DarknessAdjustment.LEVEL_8)).getValue());
    }

    @Test
    public void GivenScanTypeMapping_WhenExposureLevelRoundTrip_ThenBoundariesPreserved() {
        RangeTypeConverter converter = (RangeTypeConverter) ScanTypeMapping.exposureLevel.getConverter();
        converter.setRange(new Signed64(0L), new Signed64(8L), new Unsigned32(1L));
        Unsigned32 e2Min = new Unsigned32(0L);
        assertEquals(e2Min.getValue(), ((Unsigned32) ScanTypeMapping.exposureLevel.convertWtoE(ScanTypeMapping.exposureLevel.convertEtoW(e2Min))).getValue());
        Unsigned32 e2Max = new Unsigned32(8L);
        assertEquals(e2Max.getValue(), ((Unsigned32) ScanTypeMapping.exposureLevel.convertWtoE(ScanTypeMapping.exposureLevel.convertEtoW(e2Max))).getValue());
    }

    // ========================================================================================
    // Phase 2: sharpness (5 levels, range 0-8)
    // ========================================================================================

    @Test
    public void GivenScanTypeMapping_WhenSharpnessConvertEtoWCalled_ThenBoundariesMapCorrectly() {
        RangeTypeConverter converter = (RangeTypeConverter) ScanTypeMapping.sharpness.getConverter();
        converter.setRange(new Signed64(0L), new Signed64(8L), new Unsigned32(1L));
        assertEquals(ScanAttributes.SharpnessAdjustment.LEVEL_0, converter.convertEtoW(new Unsigned32(0L)));
        assertEquals(ScanAttributes.SharpnessAdjustment.LEVEL_2, converter.convertEtoW(new Unsigned32(4L)));
        assertEquals(ScanAttributes.SharpnessAdjustment.LEVEL_4, converter.convertEtoW(new Unsigned32(8L)));
    }

    @Test
    public void GivenScanTypeMapping_WhenSharpnessConvertWtoECalled_ThenAllLevelsMapCorrectly() {
        RangeTypeConverter converter = (RangeTypeConverter) ScanTypeMapping.sharpness.getConverter();
        converter.setRange(new Signed64(0L), new Signed64(8L), new Unsigned32(1L));
        assertEquals(new Unsigned32(0L).getValue(), ((Unsigned32) ScanTypeMapping.sharpness.convertWtoE(ScanAttributes.SharpnessAdjustment.LEVEL_0)).getValue());
        assertEquals(new Unsigned32(8L).getValue(), ((Unsigned32) ScanTypeMapping.sharpness.convertWtoE(ScanAttributes.SharpnessAdjustment.LEVEL_4)).getValue());
    }

    @Test
    public void GivenScanTypeMapping_WhenSharpnessRoundTrip_ThenBoundariesPreserved() {
        RangeTypeConverter converter = (RangeTypeConverter) ScanTypeMapping.sharpness.getConverter();
        converter.setRange(new Signed64(0L), new Signed64(8L), new Unsigned32(1L));
        Unsigned32 e2Min = new Unsigned32(0L);
        assertEquals(e2Min.getValue(), ((Unsigned32) ScanTypeMapping.sharpness.convertWtoE(ScanTypeMapping.sharpness.convertEtoW(e2Min))).getValue());
        Unsigned32 e2Max = new Unsigned32(8L);
        assertEquals(e2Max.getValue(), ((Unsigned32) ScanTypeMapping.sharpness.convertWtoE(ScanTypeMapping.sharpness.convertEtoW(e2Max))).getValue());
    }

    // ========================================================================================
    // Phase 3a: CUSTOM — DuplexConverter
    // ========================================================================================

    @Test
    public void GivenScanTypeMapping_WhenDuplexConvertEtoWCalled_ThenReturnConvertedWorkpathEnum() {
        ITypeConverter converter = ScanTypeMapping.duplex.getConverter();
        assertEquals(ScanAttributes.Duplex.NONE, converter.convertEtoW(new Pair<>(PlexMode.PmSimplex, null)));
        assertEquals(ScanAttributes.Duplex.BOOK, converter.convertEtoW(new Pair<>(PlexMode.PmDuplex, BindingFormat.BfFlipLeft)));
        assertEquals(ScanAttributes.Duplex.FLIP, converter.convertEtoW(new Pair<>(PlexMode.PmDuplex, BindingFormat.BfFlipUp)));
    }

    @Test
    public void GivenScanTypeMapping_WhenDuplexSimplexWithBindingFormat_ThenBindingFormatIgnored() {
        ITypeConverter converter = ScanTypeMapping.duplex.getConverter();
        // Simplex should map to NONE regardless of binding format
        assertEquals(ScanAttributes.Duplex.NONE, converter.convertEtoW(new Pair<>(PlexMode.PmSimplex, BindingFormat.BfFlipLeft)));
        assertEquals(ScanAttributes.Duplex.NONE, converter.convertEtoW(new Pair<>(PlexMode.PmSimplex, BindingFormat.BfFlipUp)));
    }

    @Test
    public void GivenScanTypeMapping_WhenDuplexConvertEtoWWithNull_ThenReturnNull() {
        ITypeConverter converter = ScanTypeMapping.duplex.getConverter();
        assertNull(converter.convertEtoW(null));
    }

    @Test
    public void GivenScanTypeMapping_WhenDuplexConvertWtoECalled_ThenReturnConvertedE2Type() {
        ITypeConverter converter = ScanTypeMapping.duplex.getConverter();
        assertEquals(new Pair<>(PlexMode.PmSimplex, null), converter.convertWtoE(ScanAttributes.Duplex.NONE));
        assertEquals(new Pair<>(PlexMode.PmDuplex, BindingFormat.BfFlipLeft), converter.convertWtoE(ScanAttributes.Duplex.BOOK));
        assertEquals(new Pair<>(PlexMode.PmDuplex, BindingFormat.BfFlipUp), converter.convertWtoE(ScanAttributes.Duplex.FLIP));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void GivenScanTypeMapping_WhenDuplexGetE2TypeCalled_ThenThrowUnsupportedOperationException() {
        ScanTypeMapping.duplex.getConverter().getE2Type();
    }

    // ========================================================================================
    // Phase 3b: CUSTOM — CropModeConverter (version-aware)
    // ========================================================================================

    @Test
    public void GivenScanTypeMapping_WhenAutoCropModeConvertEtoW_VersionSixOrAbove_ThenReturnDetailedCropModes() {
        ITypeConverter converter = ScanTypeMapping.autoCropMode.getConverter(Sdk.VERSION_LEVEL.SIX);
        assertEquals(ScanAttributes.CropMode.OFF, converter.convertEtoW(AutoCropModeType.AcmtOff));
        assertEquals(ScanAttributes.CropMode.CROP_TO_CONTENT, converter.convertEtoW(AutoCropModeType.AcmtContentCrop));
        assertEquals(ScanAttributes.CropMode.CROP_TO_PAPER, converter.convertEtoW(AutoCropModeType.AcmtPageCrop));
    }

    @Test
    public void GivenScanTypeMapping_WhenAutoCropModeConvertEtoW_VersionBelowSix_ThenReturnLegacyOnMode() {
        ITypeConverter converter = ScanTypeMapping.autoCropMode.getConverter(Sdk.VERSION_LEVEL.FIVE);
        assertEquals(ScanAttributes.CropMode.OFF, converter.convertEtoW(AutoCropModeType.AcmtOff));
        assertEquals(ScanAttributes.CropMode.ON, converter.convertEtoW(AutoCropModeType.AcmtContentCrop));
        assertEquals(ScanAttributes.CropMode.ON, converter.convertEtoW(AutoCropModeType.AcmtPageCrop));
    }

    @Test
    public void GivenScanTypeMapping_WhenAutoCropModeConvertWtoECalled_ThenReturnConvertedE2Type() {
        ITypeConverter converter = ScanTypeMapping.autoCropMode.getConverter();
        assertEquals(AutoCropModeType.AcmtOff, converter.convertWtoE(ScanAttributes.CropMode.OFF));
        assertEquals(AutoCropModeType.AcmtPageCrop, converter.convertWtoE(ScanAttributes.CropMode.CROP_TO_PAPER));
        assertEquals(AutoCropModeType.AcmtContentCrop, converter.convertWtoE(ScanAttributes.CropMode.CROP_TO_CONTENT));
        // ON is a legacy value not present in mapEtoW, so reverse lookup returns null
        assertNull("CropMode.ON has no reverse mapping", converter.convertWtoE(ScanAttributes.CropMode.ON));
    }

    @Test
    public void GivenScanTypeMapping_WhenAutoCropModeConvertEtoWWithNull_ThenReturnNull() {
        ITypeConverter converter = ScanTypeMapping.autoCropMode.getConverter();
        assertNull(converter.convertEtoW(null));
    }

    @Test
    public void GivenScanTypeMapping_WhenAutoCropModeGetConverterWithCurrentVersion_ThenReturnDetailedModes() {
        // Current VERSION.LEVEL is 9 which is >= SIX (6)
        ITypeConverter converter = ScanTypeMapping.autoCropMode.getConverter(Sdk.VERSION.LEVEL);
        assertEquals(ScanAttributes.CropMode.CROP_TO_CONTENT, converter.convertEtoW(AutoCropModeType.AcmtContentCrop));
        assertEquals(ScanAttributes.CropMode.CROP_TO_PAPER, converter.convertEtoW(AutoCropModeType.AcmtPageCrop));
    }
}
