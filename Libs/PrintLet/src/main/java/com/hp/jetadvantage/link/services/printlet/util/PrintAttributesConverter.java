package com.hp.jetadvantage.link.services.printlet.util;

import com.hp.jetadvantage.link.api.printer.PrintAttributes;
import com.hp.jetadvantage.link.api.printer.PrintAttributesReader;
import com.hp.jetadvantage.link.services.printlet.model.CollateMode;
import com.hp.jetadvantage.link.services.printlet.model.ColorMode;
import com.hp.jetadvantage.link.services.printlet.model.FileType;
import com.hp.jetadvantage.link.services.printlet.model.Finishings;
import com.hp.jetadvantage.link.services.printlet.model.MediaSize;
import com.hp.jetadvantage.link.services.printlet.model.MediaSource;
import com.hp.jetadvantage.link.services.printlet.model.MediaType;
import com.hp.jetadvantage.link.services.printlet.model.Orientation;
import com.hp.jetadvantage.link.services.printlet.model.OutputBin;
import com.hp.jetadvantage.link.services.printlet.model.PlexMode;
import com.hp.jetadvantage.link.services.printlet.model.PrintQuality;
import com.hp.jetadvantage.link.services.printlet.model.ScalingMode;
import com.hp.jetadvantage.link.services.printlet.model.StapleMode;
import com.hp.jetadvantage.link.services.printlet.service.PrintJobIntentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class PrintAttributesConverter {
    static public PrintAttributes.Duplex getDuplex(PlexMode plexMode) {
        if (plexMode != null) {
            switch (plexMode) {
                case Simplex:
                    return PrintAttributes.Duplex.NONE;
                case DuplexLongEdge:
                    return PrintAttributes.Duplex.BOOK;
                case DuplexShortEdge:
                    return PrintAttributes.Duplex.FLIP;
            }
        }
        return null;
    }

    static public PlexMode getPlexMode(PrintAttributesReader attrsReader) {
        if (attrsReader.getPlex() != null) {
            switch (attrsReader.getPlex()) {
                case NONE:
                    return PlexMode.Simplex;
                case BOOK:
                    return PlexMode.DuplexLongEdge;
                case FLIP:
                    return PlexMode.DuplexShortEdge;
            }
        }
        return null;
    }

    static public StapleMode getStapleMode(PrintAttributesReader attrsReader) {
        if (attrsReader.getStapleMode() != null) {
            switch (attrsReader.getStapleMode()) {
                case STAPLE:
                    return StapleMode.Staple;
                case TOP_LEFT:
                    return StapleMode.StapleTopLeft;
                case BOTTOM_LEFT:
                    return StapleMode.StapleBottomLeft;
                case TOP_RIGHT:
                    return StapleMode.StapleTopRight;
                case BOTTOM_RIGHT:
                    return StapleMode.StapleBottomRight;
                case DUAL_LEFT:
                    return StapleMode.DualLeft;
                case DUAL_RIGHT:
                    return StapleMode.DualRight;
                case DUAL_TOP:
                    return StapleMode.DualTop;
                case DUAL_BOTTOM:
                    return StapleMode.DualBottom;
                case PUNCH:
                    return StapleMode.Punch;
                case COVER:
                    return StapleMode.Cover;
                case BIND:
                    return StapleMode.Bind;
                case SADDLE_STITCH:
                    return StapleMode.SaddleStitch;
                case EDGE_STITCH:
                    return StapleMode.EdgeStitch;
                case EDGE_STITCH_LEFT:
                    return StapleMode.EdgeStitchLeft;
                case EDGE_STITCH_RIGHT:
                    return StapleMode.EdgeStitchRight;
                case EDGE_STITCH_TOP:
                    return StapleMode.EdgeStitchTop;
                case EDGE_STITCH_BOTTOM:
                    return StapleMode.EdgeStitchBottom;
                case NONE:
                    return StapleMode.None;
            }
        }
        return null;
    }

    static public ScalingMode getScalingMode(PrintAttributesReader attrsReader) {
        if (attrsReader.getAutoFit() != null) {
            switch (attrsReader.getAutoFit()) {
                case TRUE:
                    return ScalingMode.Fit;
                case FALSE:
                    return ScalingMode.None;
            }
        }
        return null;
    }

    static public ColorMode getColorMode(PrintAttributesReader attrsReader) {
        if (attrsReader.getColorMode() != null) {
            switch (attrsReader.getColorMode()) {
                case AUTO:
                    return ColorMode.Auto;
                case COLOR:
                    return ColorMode.Color;
                case MONO:
                    return ColorMode.Monochrome;
            }
        }
        // should not be reachable.
        return null;
    }

    static public CollateMode getCollateMode(PrintAttributesReader attrsReader) {
        if (attrsReader.getCollateMode() != null) {
            switch (attrsReader.getCollateMode()) {
                case COLLATED:
                    return CollateMode.Collated;
                case UNCOLLATED:
                    return CollateMode.Uncollated;
            }
        }
        return null;
    }

    static public OutputBin getOutputBin(PrintAttributesReader attrsReader) {
        if (attrsReader.getOutputBin() != null) {
            switch (attrsReader.getOutputBin()) {
                case AUTO:
                    return OutputBin.Auto;
                case BOTTOM:
                    return OutputBin.Bottom;
                case CENTER:
                    return OutputBin.Center;
                case FACE_DOWN:
                    return OutputBin.FaceDown;
                case FACE_UP:
                    return OutputBin.FaceUp;
                case LARGE_CAPACITY:
                    return OutputBin.LargeCapacity;
                case LEFT:
                    return OutputBin.Left;
                case OUTPUT_BIN_1:
                    return OutputBin.Mailbox1;
                case OUTPUT_BIN_2:
                    return OutputBin.Mailbox2;
                case OUTPUT_BIN_3:
                    return OutputBin.Mailbox3;
                case OUTPUT_BIN_4:
                    return OutputBin.Mailbox4;
                case OUTPUT_BIN_5:
                    return OutputBin.Mailbox5;
                case OUTPUT_BIN_6:
                    return OutputBin.Mailbox6;
                case OUTPUT_BIN_7:
                    return OutputBin.Mailbox7;
                case OUTPUT_BIN_8:
                    return OutputBin.Mailbox8;
                case OUTPUT_BIN_9:
                    return OutputBin.Mailbox9;
                case OUTPUT_BIN_10:
                    return OutputBin.Mailbox10;
                case MIDDLE:
                    return OutputBin.Middle;
                case MY_MAILBOX:
                    return OutputBin.MyMailbox;
                case REAR:
                    return OutputBin.Rear;
                case RIGHT:
                    return OutputBin.Right;
                case SIDE:
                    return OutputBin.Side;
                case STACKER_1:
                    return OutputBin.Stacker1;
                case STACKER_2:
                    return OutputBin.Stacker2;
                case STACKER_3:
                    return OutputBin.Stacker3;
                case STACKER_4:
                    return OutputBin.Stacker4;
                case STACKER_5:
                    return OutputBin.Stacker5;
                case STACKER_6:
                    return OutputBin.Stacker6;
                case STACKER_7:
                    return OutputBin.Stacker7;
                case STACKER_8:
                    return OutputBin.Stacker8;
                case STACKER_9:
                    return OutputBin.Stacker9;
                case STACKER_10:
                    return OutputBin.Stacker10;
                case TOP:
                    return OutputBin.Top;
                case TRAY_1:
                    return OutputBin.Tray1;
                case TRAY_2:
                    return OutputBin.Tray2;
                case TRAY_3:
                    return OutputBin.Tray3;
                case TRAY_4:
                    return OutputBin.Tray4;
                case TRAY_5:
                    return OutputBin.Tray5;
                case TRAY_6:
                    return OutputBin.Tray6;
                case TRAY_7:
                    return OutputBin.Tray7;
                case TRAY_8:
                    return OutputBin.Tray8;
                case TRAY_9:
                    return OutputBin.Tray9;
                case TRAY_10:
                    return OutputBin.Tray10;
            }
        }
        return null;
    }

    static public MediaSource getMediaSource (PrintAttributesReader attrsReader) {
        if (attrsReader.getPaperSource() != null) {
            switch (attrsReader.getPaperSource()) {
                case AUTO:
                    return MediaSource.Auto;
                case TRAY_1:
                    return MediaSource.Tray1;
                case TRAY_2:
                    return MediaSource.Tray2;
                case TRAY_3:
                    return MediaSource.Tray3;
                case TRAY_4:
                    return MediaSource.Tray4;
                case TRAY_5:
                    return MediaSource.Tray5;
                case TRAY_6:
                    return MediaSource.Tray6;
                case TRAY_7:
                    return MediaSource.Tray7;
                case TRAY_8:
                    return MediaSource.Tray8;
                case TRAY_9:
                    return MediaSource.Tray9;
                case TRAY_10:
                    return MediaSource.Tray10;
                case MANUAL_FEED:
                    return MediaSource.Manual;
            }
        }
        return null;
    }

    static public MediaSize getMediaSize(PrintAttributesReader attrsReader) {
        if (attrsReader.getPaperSize() != null) {
            switch (attrsReader.getPaperSize()) {
                case A3:
                    return MediaSize.A3;
                case A4:
                    return MediaSize.A4;
                case A5:
                    return MediaSize.A5;
                case A6:
                    return MediaSize.A6;
                case B4:
                    return MediaSize.B4;
                case B5:
                    return MediaSize.B5;
                case B6:
                    return MediaSize.B6;
                case JB4:
                    return MediaSize.JB4;
                case JB5:
                    return MediaSize.JB5;
                case JB6:
                    return MediaSize.JB6;
                case LEDGER:
                    return MediaSize.Ledger;
                case LEGAL:
                    return MediaSize.Legal;
                case LETTER:
                    return MediaSize.Letter;
                case PK8:
                    return MediaSize.PK8;
                case PK16:
                    return MediaSize.PK16;
                case INCH12X18:
                    return MediaSize.ArchitecturalB;
                case EXECUTIVE:
                    return MediaSize.Exec;
                case STATEMENT:
                    return MediaSize.Statement;
                case INCH8POINT5X13:
                    return MediaSize.Foolscap;

                // SDK 1.5
                case ENVELOPE_9:
                    return MediaSize.Envelope9;
                case ENVELOPE_COMM10:
                    return MediaSize.EnvelopeComm10;
                case ENVELOPE_MONARCH:
                    return MediaSize.EnvelopeMonarch;
                case C5:
                    return MediaSize.EnvelopeC5;
                case C6:
                    return MediaSize.EnvelopeC6;
                case ENVELOPE_DL:
                    return MediaSize.EnvelopeDL;
                case JCHOU3:
                    return MediaSize.EnvelopeJChou3;
                case JCHOU4:
                    return MediaSize.EnvelopeJChou4;
                case JDOUBLE_POSTCARD:
                    return MediaSize.JDoublePostcard;
                case JPOSTCARD:
                    return MediaSize.Jpostcard;
                case K8:
                    return MediaSize.K8;
                case K8_260x368mm:
                    return MediaSize.K8_260x368mm;
                case K16:
                    return MediaSize.K16;
                case K16_184x260mm:
                    return MediaSize.K16_184x260mm;
                case OFICIO:
                    return MediaSize.Oficio_216x340mm;
                case RA3:
                    return MediaSize.RA3;
                case RA4:
                    return MediaSize.RA4;
                case GENERAL_10X15cm:
                    return MediaSize.Size10x15cm;
                case GENERAL_3X5in:
                    return MediaSize.Size3x5in;
                case GENERAL_4X6in:
                    return MediaSize.Size4x6in;
                case GENERAL_5X7in:
                    return MediaSize.Size5x7in;
                case GENERAL_5X8in:
                    return MediaSize.Size5x8in;
                case SRA3:
                    return MediaSize.Sra3;
                case SRA4:
                    return MediaSize.Sra4;
            }
        }
        return null;
    }

    static public MediaType getMediaType(PrintAttributesReader attrsReader) {
        if (attrsReader.getPaperType() != null) {
            switch (attrsReader.getPaperType()) {
                case BOND:
                    return MediaType.Bond;
                case CARD_STOCK:
                    return MediaType.Cardstock_176to220g;
                case CARD_STOCK_GLOSSY:
                    return MediaType.CardstockGloss_176to220g;
                case COLORED:
                    return MediaType.Color;
                case ENVELOPE:
                    return MediaType.Envelope;
                case EXTRA_HEAVY:
                    return MediaType.ExtraHeavy_131to175g;
                case EXTRA_HEAVY_GLOSSY:
                    return MediaType.ExtraHeavyGloss_131to175g;
                case HEAVY:
                    return MediaType.Heavy_111to130g;
                case HEAVY_ENVELOPE:
                    return MediaType.HeavyEnvelope;
                case HEAVY_GLOSSY:
                    return MediaType.HeavyGloss_111to130g;
                case HEAVY_PAPERBOARD:
                    return MediaType.HeavyPaperboard;
                case HEAVY_ROUGH:
                    return MediaType.HeavyRough;
                case HP_ADVANCED_PHOTO:
                    return MediaType.HPAdvancedPhoto;
                case HP_BROCHURE_GLOSSY:
                    return MediaType.HPBrochureGloss_180g;
                case HP_BROCHURE_MATTE_180G:
                    return MediaType.HPBrochureMatte_180g;
                case HP_ECOFFICIENT:
                    return MediaType.HPEcoFFICIENT;
                case HP_GLOSSY_120G:
                    return MediaType.HPGloss_130g;
                case HP_GLOSSY_150G:
                    return MediaType.HPGloss_160g;
                case HP_GLOSSY_200G:
                    return MediaType.HPGloss_220g;
                case HP_INKJET_MATTE_120G:
                    return MediaType.HPPremiumMatte_120g;
                case HP_MATTE_90G:
                    return MediaType.HPMatte_90g;
                case HP_MATTE_105G:
                    return MediaType.HPMatte_105g;
                case HP_MATTE_120G:
                    return MediaType.HPMatte_120g;
                case HP_MATTE_150G:
                    return MediaType.HPMatte_160g;
                case HP_MATTE_200G:
                    return MediaType.HPMatte_200g;
                case HP_SOFT_GLOSS_120G:
                    return MediaType.HPSoftGloss_120g;
                case INTERMEDIATE:
                    return MediaType.Intermediate_85to95g;
                case LABELS:
                    return MediaType.Labels;
                case LETTERHEAD:
                    return MediaType.Letterhead;
                case LIGHT:
                    return MediaType.Light_60to74g;
                case LIGHT_BOND:
                    return MediaType.LightBond;
                case LIGHT_ROUGH:
                    return MediaType.LightRough;
                case LIGHT_PAPERBOARD:
                    return MediaType.LightPaperboard;
                case MID_WEIGHT:
                    return MediaType.Midweight_96to110g;
                case MID_WEIGHT_GLOSSY:
                    return MediaType.MidweightGloss_96to110g;
                case OPAQUE_FILM:
                    return MediaType.OpaqueFilm;
                case PAPERBOARD:
                    return MediaType.Paperboard;
                case PLAIN:
                    return MediaType.Plain;
                case PREPRINTED:
                    return MediaType.Preprinted;
                case PREPUNCHED:
                    return MediaType.Prepunched;
                case RECYCLED:
                    return MediaType.Recycled;
                case ROUGH:
                    return MediaType.Rough;
                case TAB:
                    return MediaType.Tab;
                case TRANSPARENCY:
                    return MediaType.Transparency;
                case VELLUM:
                    return MediaType.Vellum;
            }
        }
        return null;
    }

    static public FileType getFileType(PrintAttributesReader attrsReader, String docUri) {
        if (attrsReader.getDocumentFormat() != null) {
            switch (attrsReader.getDocumentFormat()) {
                case PDF:
                    return FileType.PDF;
                case JPEG:
                    return FileType.Jpeg;
                case TIFF:
                    return FileType.Tiff;
                case PCL5:
                    return FileType.PCL5;
                case PCL6:
                    return FileType.PCL6;
                case PS:
                    return FileType.PS;
                case TEXT:
                    return FileType.Text;
                case AUTO:
                    if (attrsReader.getSource() == PrintAttributes.Source.STORAGE
                            || attrsReader.getSource() == PrintAttributes.Source.USB) {
                        // check extension for local file
                        String extension = docUri.substring(docUri.lastIndexOf(".") + 1);
                        for (Map.Entry<FileType, List<String>> entry : PrintJobIntentService.EXTENSIONS.entrySet()) {
                            if (entry.getValue().contains(extension.toLowerCase())) {
                                return entry.getKey();
                            }
                        }
                    }
                    break;
            }
        }
        return null;
    }

    static public Orientation getOrientation(PrintAttributesReader attrsReader) {
        if (attrsReader.getOrientation() != null) {
            switch (attrsReader.getOrientation()) {
                case PORTRAIT:
                    return Orientation.Portrait;
                case LANDSCAPE:
                    return Orientation.Landscape;
                case REVERSE_PORTRAIT:
                    return Orientation.ReversePortrait;
                case REVERSE_LANDSCAPE:
                    return Orientation.ReverseLandscape;
                case NONE:
                    return Orientation.None;
            }
        }
        return null;
    }

    static public PrintQuality getPrintQuality(PrintAttributesReader attrsReader) {
        if (attrsReader.getPrintQuality() != null) {
            switch (attrsReader.getPrintQuality()) {
                case DRAFT:
                    return PrintQuality.Draft;
                case NORMAL:
                    return PrintQuality.Normal;
                case HIGH:
                    return PrintQuality.High;
            }
        }
        return null;
    }

    static public List<Finishings> getFinishingsList(PrintAttributesReader attrsReader) {
        if (attrsReader.getFinishingsList() != null && !attrsReader.getFinishingsList().isEmpty() // should not be empty
                && !(attrsReader.getFinishingsList().size() == 1 && "DEFAULT".equals(attrsReader.getFinishingsList().get(0).name()))) {
            // finishings list
            List<Finishings> finishingsList = new ArrayList<>();
            for (PrintAttributes.Finishings finishings : attrsReader.getFinishingsList()) {
                if(PrintAttributesConverter.getFinishings(attrsReader, finishings) != null){
                    finishingsList.add(PrintAttributesConverter.getFinishings(attrsReader, finishings));
                }
            }
            return finishingsList;
        }
        return null;
    }

    static private Finishings getFinishings(PrintAttributesReader attrsReader, PrintAttributes.Finishings finishings) {
        if (attrsReader.getFinishingsList() != null) {
            switch (finishings) {
                case NONE:
                    return Finishings.None;
                case STAPLE:
                    return Finishings.Staple;
                case PUNCH:
                    return Finishings.Punch;
                case COVER:
                    return Finishings.Cover;
                case BIND:
                    return Finishings.Bind;
                case SADDLE_STITCH:
                    return Finishings.SaddleStitch;
                case EDGE_STITCH:
                    return Finishings.EdgeStitch;
                case FOLD:
                    return Finishings.Fold;
                case TRIM:
                    return Finishings.Trim;
                case BALE:
                    return Finishings.Bale;
                case BOOKLET_MAKER:
                    return Finishings.BookletMaker;
                case JOG_OFFSET:
                    return Finishings.JogOffset;
                case COAT:
                    return Finishings.Coat;
                case LAMINATE:
                    return Finishings.Laminate;
                case STAPLE_TOP_LEFT:
                    return Finishings.StapleTopLeft;
                case STAPLE_BOTTOM_LEFT:
                    return Finishings.StapleBottomLeft;
                case STAPLE_TOP_RIGHT:
                    return Finishings.StapleTopRight;
                case STAPLE_BOTTOM_RIGHT:
                    return Finishings.StapleBottomRight;
                case EDGE_STITCH_LEFT:
                    return Finishings.EdgeStitchLeft;
                case EDGE_STITCH_TOP:
                    return Finishings.EdgeStitchTop;
                case EDGE_STITCH_RIGHT:
                    return Finishings.EdgeStitchRight;
                case EDGE_STITCH_BOTTOM:
                    return Finishings.EdgeStitchBottom;
                case STAPLE_DUAL_LEFT:
                    return Finishings.StapleDualLeft;
                case STAPLE_DUAL_TOP:
                    return Finishings.StapleDualTop;
                case STAPLE_DUAL_RIGHT:
                    return Finishings.StapleDualRight;
                case STAPLE_DUAL_BOTTOM:
                    return Finishings.StapleDualBottom;
                case STAPLE_TRIPLE_LEFT:
                    return Finishings.StapleTripleLeft;
                case STAPLE_TRIPLE_TOP:
                    return Finishings.StapleTripleTop;
                case STAPLE_TRIPLE_RIGHT:
                    return Finishings.StapleTripleRight;
                case STAPLE_TRIPLE_BOTTOM:
                    return Finishings.StapleTripleBottom;
                case BIND_LEFT:
                    return Finishings.BindLeft;
                case BIND_TOP:
                    return Finishings.BindTop;
                case BIND_RIGHT:
                    return Finishings.BindRight;
                case BIND_BOTTOM:
                    return Finishings.BindBottom;
                case TRIM_AFTER_COPIES:
                    return Finishings.TrimAfterCopies;
                case TRIM_AFTER_DOCUMENTS:
                    return Finishings.TrimAfterDocuments;
                case TRIM_AFTER_JOB:
                    return Finishings.TrimAfterJob;
                case TRIM_AFTER_PAGES:
                    return Finishings.TrimAfterPages;
                case PUNCH_TOP_LEFT:
                    return Finishings.PunchTopLeft;
                case PUNCH_BOTTOM_LEFT:
                    return Finishings.PunchBottomLeft;
                case PUNCH_TOP_RIGHT:
                    return Finishings.PunchTopRight;
                case PUNCH_BOTTOM_RIGHT:
                    return Finishings.PunchBottomRight;
                case PUNCH_DUAL_LEFT:
                    return Finishings.PunchDualLeft;
                case PUNCH_DUAL_TOP:
                    return Finishings.PunchDualTop;
                case PUNCH_DUAL_RIGHT:
                    return Finishings.PunchDualRight;
                case PUNCH_DUAL_BOTTOM:
                    return Finishings.PunchDualBottom;
                case PUNCH_TRIPLE_LEFT:
                    return Finishings.PunchTripleLeft;
                case PUNCH_TRIPLE_TOP:
                    return Finishings.PunchTripleTop;
                case PUNCH_TRIPLE_RIGHT:
                    return Finishings.PunchTripleRight;
                case PUNCH_TRIPLE_BOTTOM:
                    return Finishings.PunchTripleBottom;
                case PUNCH_QUAD_LEFT:
                    return Finishings.PunchQuadLeft;
                case PUNCH_QUAD_TOP:
                    return Finishings.PunchQuadTop;
                case PUNCH_QUAD_RIGHT:
                    return Finishings.PunchQuadRight;
                case PUNCH_QUAD_BOTTOM:
                    return Finishings.PunchQuadBottom;
                case PUNCH_MULTIPLE_LEFT:
                    return Finishings.PunchMultipleLeft;
                case PUNCH_MULTIPLE_TOP:
                    return Finishings.PunchMultipleTop;
                case PUNCH_MULTIPLE_RIGHT:
                    return Finishings.PunchMultipleRight;
                case PUNCH_MULTIPLE_BOTTOM:
                    return Finishings.PunchMultipleBottom;
                case FOLD_ACCORDION:
                    return Finishings.FoldAccordion;
                case FOLD_DOUBLE_GATE:
                    return Finishings.FoldDoubleGate;
                case FOLD_GATE:
                    return Finishings.FoldGate;
                case FOLD_HALF:
                    return Finishings.FoldHalf;
                case FOLD_HALF_Z:
                    return Finishings.FoldHalfZ;
                case FOLD_LEFT_GATE:
                    return Finishings.FoldLeftGate;
                case FOLD_LETTER:
                    return Finishings.FoldLetter;
                case FOLD_PARALLEL:
                    return Finishings.FoldParallel;
                case FOLD_POSTER:
                    return Finishings.FoldPoster;
                case FOLD_RIGHT_GATE:
                    return Finishings.FoldRightGate;
                case FOLD_Z:
                    return Finishings.FoldZ;
                case FOLD_ENGINEERING_Z:
                    return Finishings.FoldEngineeringZ;
            }
        }

        return null;
    }
}
