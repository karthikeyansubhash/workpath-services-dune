package com.hp.jetadvantage.link.services.statisticslet.adapter;

import com.hp.ext.service.jobStatistics.ImpressionClassification;
import com.hp.ext.types.common.BooleanOrNone;
import com.hp.ext.types.imaging.PlexMode;
import com.hp.ext.types.job.JobCategory;
import com.hp.ext.types.job.JobDataSource;
import com.hp.ext.types.job.JobDestination;
import com.hp.ext.types.job.JobDoneStatus;
import com.hp.ext.types.media.MediaInputId;
import com.hp.ext.types.media.MediaOutputId;
import com.hp.ext.types.media.MediaSizeId;
import com.hp.ext.service.jobStatistics.ProcessedBy;
import com.hp.ext.types.media.MediaTypeId;
import com.hp.ext.types.security.AuthenticationKind;
import com.hp.ext.types.supply.SupplyAgentType;
import com.hp.ext.types.supply.SupplyColorCode;
import com.hp.ext.types.supply.SupplyType;
import com.hp.ext.types.usage.CounterUnit;
import com.hp.workpath.api.statistics.jobinfo.StatisticsAttributes;
import com.hp.jetadvantage.link.device.services.converter.DefaultTypeConverter;
import com.hp.jetadvantage.link.device.services.converter.ITypeConverter;
import com.hp.jetadvantage.link.device.services.converter.ITypeMapping;
import com.hp.workpath.api.statistics.jobinfo.emailinfo.EmailInfo;
import com.hp.workpath.api.statistics.jobinfo.faxinfo.FaxAttributes;
import com.hp.workpath.api.statistics.jobinfo.print.PrintAgentInfo;
import com.hp.workpath.api.statistics.jobinfo.userinfo.ExtendedUserInfo;

import java.util.Map;

/**
 * StatisticsTypeMapping
 * Each entry contains a mapping between E2 and Workpath API Statistics types.
 */
public enum StatisticsTypeMapping implements ITypeMapping {

    // PlexMode 매핑
    plexMode(
            PlexMode.class,
            StatisticsAttributes.Plex.SIMPLEX,  // 기본값
            Map.ofEntries(
                    Map.entry(PlexMode.PmSimplex, StatisticsAttributes.Plex.SIMPLEX),
                    Map.entry(PlexMode.PmDuplex, StatisticsAttributes.Plex.DUPLEX)
            )
    ),

    jobCategory(
            JobCategory.class,
            StatisticsAttributes.JobCategory.OTHER, // 기본값
            Map.ofEntries(
                    Map.entry(JobCategory.JcStored, StatisticsAttributes.JobCategory.STORED),
                    Map.entry(JobCategory.JcPrint, StatisticsAttributes.JobCategory.PRINT),
                    Map.entry(JobCategory.JcFax, StatisticsAttributes.JobCategory.FAX),
                    Map.entry(JobCategory.JcDigitalSend, StatisticsAttributes.JobCategory.DIGITALSEND),
                    Map.entry(JobCategory.JcService, StatisticsAttributes.JobCategory.SERVICE),
                    Map.entry(JobCategory.JcConfigurationDiagnostic, StatisticsAttributes.JobCategory.CONFIGURATIONDIAGNOSTIC),
                    Map.entry(JobCategory.JcMixed, StatisticsAttributes.JobCategory.MIXED),
                    Map.entry(JobCategory.JcNotification, StatisticsAttributes.JobCategory.NOTIFICATION),
                    Map.entry(JobCategory.JcOther, StatisticsAttributes.JobCategory.OTHER)
            )
    ),

    jobDataSource(
            JobDataSource.class,
            StatisticsAttributes.JobDataSource.OTHER, // 기본값
            Map.ofEntries(
                    Map.entry(JobDataSource.JdsNetworkIO, StatisticsAttributes.JobDataSource.NETWORKIO),
                    Map.entry(JobDataSource.JdsDirectIO, StatisticsAttributes.JobDataSource.DIRECTIO),
                    Map.entry(JobDataSource.JdsInternalStorage, StatisticsAttributes.JobDataSource.DIRECTIO),
                    Map.entry(JobDataSource.JdsRemovableStorage, StatisticsAttributes.JobDataSource.REMOVABLESTORAGE),
                    Map.entry(JobDataSource.JdsScanner, StatisticsAttributes.JobDataSource.SCANNER),
                    Map.entry(JobDataSource.JdsWebService, StatisticsAttributes.JobDataSource.WEBSERVICE),
                    Map.entry(JobDataSource.JdsEws, StatisticsAttributes.JobDataSource.EWS),
                    Map.entry(JobDataSource.JdsFaxCard, StatisticsAttributes.JobDataSource.FAXCARD),
                    Map.entry(JobDataSource.JdsInternal, StatisticsAttributes.JobDataSource.INTERNAL),
                    Map.entry(JobDataSource.JdsIPPWalkup, StatisticsAttributes.JobDataSource.IPPWALKUP),
                    Map.entry(JobDataSource.JdsOther, StatisticsAttributes.JobDataSource.OTHER)
            )
    ),

    jobDestination(
            JobDestination.class,
            StatisticsAttributes.JobDestination.OTHER,
            Map.ofEntries(
                    Map.entry(JobDestination.JdInternalStorage, StatisticsAttributes.JobDestination.INTERNALSTORAGE),
                    Map.entry(JobDestination.JdRemovableStorage, StatisticsAttributes.JobDestination.REMOVABLESTORAGE),
                    Map.entry(JobDestination.JdScanner, StatisticsAttributes.JobDestination.SCANNER),
                    Map.entry(JobDestination.JdWebService, StatisticsAttributes.JobDestination.WEBSERVICE),
                    Map.entry(JobDestination.JdPrintEngine, StatisticsAttributes.JobDestination.PRINTENGINE),
                    Map.entry(JobDestination.JdFaxCard, StatisticsAttributes.JobDestination.FAXCARD),
                    Map.entry(JobDestination.JdNetworkFolder, StatisticsAttributes.JobDestination.NETWORKFOLDER),
                    Map.entry(JobDestination.JdEmail, StatisticsAttributes.JobDestination.EMAIL),
                    Map.entry(JobDestination.JdFtp, StatisticsAttributes.JobDestination.FTP),
                    Map.entry(JobDestination.JdHttp, StatisticsAttributes.JobDestination.HTTP),
                    Map.entry(JobDestination.JdOther, StatisticsAttributes.JobDestination.OTHER)
            )
    ),

    jobDoneStatus(
            JobDoneStatus.class,
            StatisticsAttributes.JobState.PARTIALLYSUCCEEDED,
            Map.ofEntries(
                    Map.entry(JobDoneStatus.JdsSucceeded, StatisticsAttributes.JobState.SUCCEEDED),
                    Map.entry(JobDoneStatus.JdsFailed, StatisticsAttributes.JobState.FAILED),
                    Map.entry(JobDoneStatus.JdsCanceled, StatisticsAttributes.JobState.CANCELED),
                    Map.entry(JobDoneStatus.JdsPartiallySucceeded, StatisticsAttributes.JobState.PARTIALLYSUCCEEDED),
                    Map.entry(JobDoneStatus.JdsActive, StatisticsAttributes.JobState.PARTIALLYSUCCEEDED)
            )
    ),

    jobPaused(
            com.hp.ext.types.common.BooleanOrNone.class,
            StatisticsAttributes.JobPausedState.NONE,
            Map.ofEntries(
                    Map.entry(BooleanOrNone.True, StatisticsAttributes.JobPausedState.TRUE),
                    Map.entry(BooleanOrNone.False, StatisticsAttributes.JobPausedState.FALSE),
                    Map.entry(BooleanOrNone.None, StatisticsAttributes.JobPausedState.NONE)
            )
    ),

/*    Result(
            Email
            EmailInfo.Result.PARTIAL,
            Map.ofEntries(

            )
    ),*/

    mediaInputId(
            MediaInputId.class,
            StatisticsAttributes.PaperSource.OTHER,
            Map.ofEntries(
                    Map.entry(MediaInputId.MiAdf, StatisticsAttributes.PaperSource.ADF),
                    Map.entry(MediaInputId.MiEnvelopeFeed, StatisticsAttributes.PaperSource.ENVELOPE_FEED),
                    Map.entry(MediaInputId.MiFlatbed, StatisticsAttributes.PaperSource.FLATBED),
                    Map.entry(MediaInputId.MiTray1, StatisticsAttributes.PaperSource.TRAY_1),
                    Map.entry(MediaInputId.MiTray2, StatisticsAttributes.PaperSource.TRAY_2),
                    Map.entry(MediaInputId.MiTray3, StatisticsAttributes.PaperSource.TRAY_3),
                    Map.entry(MediaInputId.MiTray4, StatisticsAttributes.PaperSource.TRAY_4),
                    Map.entry(MediaInputId.MiTray5, StatisticsAttributes.PaperSource.TRAY_5),
                    Map.entry(MediaInputId.MiTray6, StatisticsAttributes.PaperSource.TRAY_6),
                    Map.entry(MediaInputId.MiTray7, StatisticsAttributes.PaperSource.TRAY_7),
                    Map.entry(MediaInputId.MiTray8, StatisticsAttributes.PaperSource.TRAY_8),
                    Map.entry(MediaInputId.MiTray9, StatisticsAttributes.PaperSource.TRAY_9),
                    Map.entry(MediaInputId.MiTray10, StatisticsAttributes.PaperSource.TRAY_10),
                    Map.entry(MediaInputId.MiTray11, StatisticsAttributes.PaperSource.TRAY_11),
                    Map.entry(MediaInputId.MiTray12, StatisticsAttributes.PaperSource.TRAY_12),
                    Map.entry(MediaInputId.MiTray13, StatisticsAttributes.PaperSource.TRAY_13),
                    Map.entry(MediaInputId.MiTray14, StatisticsAttributes.PaperSource.TRAY_14),
                    Map.entry(MediaInputId.MiTray15, StatisticsAttributes.PaperSource.TRAY_15),
                    Map.entry(MediaInputId.MiTray16, StatisticsAttributes.PaperSource.TRAY_16),
                    Map.entry(MediaInputId.MiAuto, StatisticsAttributes.PaperSource.AUTO),
                    //                  Map.entry(MediaInputId.MiOther, PaperSource.?),
                    Map.entry(MediaInputId.MiDuplexer, StatisticsAttributes.PaperSource.DUPLEXER),
                    Map.entry(MediaInputId.MiExternal, StatisticsAttributes.PaperSource.EXTERNAL),
                    Map.entry(MediaInputId.MiExternalTray1, StatisticsAttributes.PaperSource.EXTERNAL_TRAY_1),
                    Map.entry(MediaInputId.MiExternalTray2, StatisticsAttributes.PaperSource.EXTERNAL_TRAY_2),
                    Map.entry(MediaInputId.MiExternalTray3, StatisticsAttributes.PaperSource.EXTERNAL_TRAY_3),
                    Map.entry(MediaInputId.MiExternalTray4, StatisticsAttributes.PaperSource.EXTERNAL_TRAY_4),
                    Map.entry(MediaInputId.MiExternalTray5, StatisticsAttributes.PaperSource.EXTERNAL_TRAY_5),
                    Map.entry(MediaInputId.MiExternalTray6, StatisticsAttributes.PaperSource.EXTERNAL_TRAY_6),
                    Map.entry(MediaInputId.MiExternalTray7, StatisticsAttributes.PaperSource.EXTERNAL_TRAY_7),
                    Map.entry(MediaInputId.MiExternalTray8, StatisticsAttributes.PaperSource.EXTERNAL_TRAY_8),
                    Map.entry(MediaInputId.MiExternalTray9, StatisticsAttributes.PaperSource.EXTERNAL_TRAY_9),
                    Map.entry(MediaInputId.MiExternalTray10, StatisticsAttributes.PaperSource.EXTERNAL_TRAY_10),
                    Map.entry(MediaInputId.MiMultipurposeTray, StatisticsAttributes.PaperSource.MULTIPURPOSE_TRAY),
                    Map.entry(MediaInputId.MiPhotoTray, StatisticsAttributes.PaperSource.PHOTO_TRAY),
                    Map.entry(MediaInputId.MiRearManualFeed, StatisticsAttributes.PaperSource.REAR_MANUAL_FEED),
                    Map.entry(MediaInputId.MiRoll1, StatisticsAttributes.PaperSource.ROLL_1),
                    Map.entry(MediaInputId.MiRoll2, StatisticsAttributes.PaperSource.ROLL_2),
                    Map.entry(MediaInputId.MiRoll3, StatisticsAttributes.PaperSource.ROLL_3),
                    Map.entry(MediaInputId.MiRoll4, StatisticsAttributes.PaperSource.ROLL_4),
                    Map.entry(MediaInputId.MiEnvelopeFeedJobSettings, StatisticsAttributes.PaperSource.ENVELOPE_FEED_JOB_SETTINGS),
                    Map.entry(MediaInputId.MiTray1JobSettings, StatisticsAttributes.PaperSource.TRAY_1_JOB_SETTINGS)
                    //                  Map.entry(MediaInputId.MiMdf, PaperSource.?)
            )
    ),

    impressionClassification(
            ImpressionClassification.class,
            StatisticsAttributes.ImpressionClassification.NONE,
            Map.ofEntries(
                    Map.entry(ImpressionClassification.IcBlank, StatisticsAttributes.ImpressionClassification.BLANK),
                    Map.entry(ImpressionClassification.IcColor, StatisticsAttributes.ImpressionClassification.COLOR),
                    Map.entry(ImpressionClassification.IcMonochrome, StatisticsAttributes.ImpressionClassification.MONOCHROME),
                    Map.entry(ImpressionClassification.IcNone, StatisticsAttributes.ImpressionClassification.NONE)
            )
    ),

    mediaOutputId(
            MediaOutputId.class,
            StatisticsAttributes.OutputMediaType.OTHER,
            Map.ofEntries(
                    Map.entry(MediaOutputId.MoAccessory, StatisticsAttributes.OutputBin.ACCESSORY),
                    Map.entry(MediaOutputId.MoAdf, StatisticsAttributes.OutputBin.ADF),
                    Map.entry(MediaOutputId.MoAuto, StatisticsAttributes.OutputBin.AUTO),
                    Map.entry(MediaOutputId.MoBooklet, StatisticsAttributes.OutputBin.BOOKLET),
                    Map.entry(MediaOutputId.MoDefault, StatisticsAttributes.OutputBin.DEFAULT),
                    Map.entry(MediaOutputId.MoDocumentFeeder, StatisticsAttributes.OutputBin.DOCUMENT_FEEDER),
                    Map.entry(MediaOutputId.MoExternal, StatisticsAttributes.OutputBin.EXTERNAL),
                    Map.entry(MediaOutputId.MoFaceDown, StatisticsAttributes.OutputBin.FACE_DOWN),
                    Map.entry(MediaOutputId.MoFaceDownCorrectOrder, StatisticsAttributes.OutputBin.FACE_DOWN_CORRECT_ORDER),
                    Map.entry(MediaOutputId.MoFaceUp, StatisticsAttributes.OutputBin.FACE_UP),
                    Map.entry(MediaOutputId.MoFaceUpStraightestPath, StatisticsAttributes.OutputBin.FACE_UP_STRAIGHTEST_PATH),
                    Map.entry(MediaOutputId.MoFax, StatisticsAttributes.OutputBin.FAX),
                    Map.entry(MediaOutputId.MoFolded, StatisticsAttributes.OutputBin.FOLDED),
                    Map.entry(MediaOutputId.MoLeft, StatisticsAttributes.OutputBin.LEFT),
                    Map.entry(MediaOutputId.MoLeftStraightestPath, StatisticsAttributes.OutputBin.LEFT_STRAIGHTEST_PATH),
                    Map.entry(MediaOutputId.MoLower, StatisticsAttributes.OutputBin.LOWER),
                    Map.entry(MediaOutputId.MoLowerBooklet, StatisticsAttributes.OutputBin.LOWER_BOOKLET),
                    Map.entry(MediaOutputId.MoLowerLeft, StatisticsAttributes.OutputBin.LOWER_LEFT),
                    Map.entry(MediaOutputId.MoLowerLeftHighestCapacity, StatisticsAttributes.OutputBin.LOWER_LEFT_HIGHEST_CAPACITY),
                    Map.entry(MediaOutputId.MoLowerStacker, StatisticsAttributes.OutputBin.LOWER_STACKER),
                    Map.entry(MediaOutputId.MoMainCopier, StatisticsAttributes.OutputBin.MAIN_COPIER),
                    Map.entry(MediaOutputId.MoMiddle, StatisticsAttributes.OutputBin.MIDDLE),
                    Map.entry(MediaOutputId.MoMiddleLeft, StatisticsAttributes.OutputBin.MIDDLE_LEFT),
                    Map.entry(MediaOutputId.MoOutputBin1, StatisticsAttributes.OutputBin.OUTPUT_BIN_1),
                    Map.entry(MediaOutputId.MoOutputBin2, StatisticsAttributes.OutputBin.OUTPUT_BIN_2),
                    Map.entry(MediaOutputId.MoOutputBin3, StatisticsAttributes.OutputBin.OUTPUT_BIN_3),
                    Map.entry(MediaOutputId.MoOutputBin4, StatisticsAttributes.OutputBin.OUTPUT_BIN_4),
                    Map.entry(MediaOutputId.MoOutputBin5, StatisticsAttributes.OutputBin.OUTPUT_BIN_5),
                    Map.entry(MediaOutputId.MoOutputBin6, StatisticsAttributes.OutputBin.OUTPUT_BIN_6),
                    Map.entry(MediaOutputId.MoOutputBin7, StatisticsAttributes.OutputBin.OUTPUT_BIN_7),
                    Map.entry(MediaOutputId.MoOutputBin8, StatisticsAttributes.OutputBin.OUTPUT_BIN_8),
                    Map.entry(MediaOutputId.MoOutputBin9, StatisticsAttributes.OutputBin.OUTPUT_BIN_9),
                    Map.entry(MediaOutputId.MoOutputBin10, StatisticsAttributes.OutputBin.OUTPUT_BIN_10),
                    Map.entry(MediaOutputId.MoOutputBin11, StatisticsAttributes.OutputBin.OUTPUT_BIN_11),
                    Map.entry(MediaOutputId.MoOutputBin12, StatisticsAttributes.OutputBin.OUTPUT_BIN_12),
                    Map.entry(MediaOutputId.MoOutputBin13, StatisticsAttributes.OutputBin.OUTPUT_BIN_13),
                    Map.entry(MediaOutputId.MoOutputBin14, StatisticsAttributes.OutputBin.OUTPUT_BIN_14),
                    Map.entry(MediaOutputId.MoOutputBin15, StatisticsAttributes.OutputBin.OUTPUT_BIN_15),
                    Map.entry(MediaOutputId.MoOutputBin16, StatisticsAttributes.OutputBin.OUTPUT_BIN_16),
                    Map.entry(MediaOutputId.MoRear, StatisticsAttributes.OutputBin.REAR),
                    Map.entry(MediaOutputId.MoRearFaceUp, StatisticsAttributes.OutputBin.REAR_FACE_UP),
                    Map.entry(MediaOutputId.MoRearStraightestPath, StatisticsAttributes.OutputBin.REAR_STRAIGHTEST_PATH),
                    Map.entry(MediaOutputId.MoStacker, StatisticsAttributes.OutputBin.STACKER),
                    Map.entry(MediaOutputId.MoStandard, StatisticsAttributes.OutputBin.STANDARD),
                    Map.entry(MediaOutputId.MoStandardCorrectOrder, StatisticsAttributes.OutputBin.STANDARD_CORRECT_ORDER),
                    Map.entry(MediaOutputId.MoStandardTop, StatisticsAttributes.OutputBin.STANDARD_TOP),
                    Map.entry(MediaOutputId.MoTop, StatisticsAttributes.OutputBin.TOP),
                    Map.entry(MediaOutputId.MoUpper, StatisticsAttributes.OutputBin.UPPER),
                    Map.entry(MediaOutputId.MoUpperFaceUp, StatisticsAttributes.OutputBin.UPPER_FACE_UP),
                    Map.entry(MediaOutputId.MoUpperLeft, StatisticsAttributes.OutputBin.UPPER_LEFT),
                    Map.entry(MediaOutputId.MoUpperLeftBins, StatisticsAttributes.OutputBin.UPPER_LEFT_BINS),
                    Map.entry(MediaOutputId.MoUpperLeftStraightestPath, StatisticsAttributes.OutputBin.UPPER_LEFT_STRAIGHTEST_PATH),
                    Map.entry(MediaOutputId.MoVirtualBins1To3, StatisticsAttributes.OutputBin.VIRTUAL_BINS_1TO3),
                    Map.entry(MediaOutputId.MoVirtualBins1To5, StatisticsAttributes.OutputBin.VIRTUAL_BINS_1TO5),
                    Map.entry(MediaOutputId.MoVirtualBins1To8, StatisticsAttributes.OutputBin.VIRTUAL_BINS_1TO8),
                    Map.entry(MediaOutputId.MoVirtualBins1To10, StatisticsAttributes.OutputBin.VIRTUAL_BINS_1TO10),
                    Map.entry(MediaOutputId.MoVirtualBins2To8, StatisticsAttributes.OutputBin.VIRTUAL_BINS_2TO8),
                    Map.entry(MediaOutputId.MoVirtualFinisherBins, StatisticsAttributes.OutputBin.VIRTUAL_FINISHER_BINS),
                    Map.entry(MediaOutputId.MoVirtualLeftBins, StatisticsAttributes.OutputBin.VIRTUAL_LEFT_BINS),
                    Map.entry(MediaOutputId.MoOther, StatisticsAttributes.OutputBin.OTHER),
                    Map.entry(MediaOutputId.MoAlternate, StatisticsAttributes.OutputBin.ALTERNATE),
                    Map.entry(MediaOutputId.MoBottom, StatisticsAttributes.OutputBin.BOTTOM),
                    Map.entry(MediaOutputId.MoCenter, StatisticsAttributes.OutputBin.CENTER),
                    Map.entry(MediaOutputId.MoCollator, StatisticsAttributes.OutputBin.COLLATOR),
                    Map.entry(MediaOutputId.MoDuplexer, StatisticsAttributes.OutputBin.DUPLEXER),
                    Map.entry(MediaOutputId.MoEngineOptionalBin1, StatisticsAttributes.OutputBin.ENGINE_OPTIONAL_BIN_1),
                    Map.entry(MediaOutputId.MoLargeCapacity, StatisticsAttributes.OutputBin.LARGE_CAPACITY),
                    Map.entry(MediaOutputId.MoMyMailbox, StatisticsAttributes.OutputBin.MY_MAILBOX),
                    Map.entry(MediaOutputId.MoRight, StatisticsAttributes.OutputBin.RIGHT),
                    Map.entry(MediaOutputId.MoSide, StatisticsAttributes.OutputBin.SIDE),
                    Map.entry(MediaOutputId.MoStackerFacedown, StatisticsAttributes.OutputBin.STACKER_FACEDOWN),
                    Map.entry(MediaOutputId.MoStackerFaceUp, StatisticsAttributes.OutputBin.STACKER_FACE_UP),
                    Map.entry(MediaOutputId.MoStackerStaples, StatisticsAttributes.OutputBin.STACKER_STAPLES),
                    Map.entry(MediaOutputId.MoUniversalOutputBin, StatisticsAttributes.OutputBin.UNIVERSAL_OUTPUT_BIN),
                    Map.entry(MediaOutputId.MoStapler, StatisticsAttributes.OutputBin.STAPLER)
            )
    ),

    mediaTypeId(
            MediaTypeId.class,
            StatisticsAttributes.OutputMediaType.OTHER,
            Map.ofEntries(
                    Map.entry(MediaTypeId.MtBond, StatisticsAttributes.OutputMediaType.BOND),
                    Map.entry(MediaTypeId.MtBrochureMatte, StatisticsAttributes.OutputMediaType.BROCHURE_MATTE),
                    Map.entry(MediaTypeId.MtCardstock_176to220g, StatisticsAttributes.OutputMediaType.CARD_STOCK),
                    Map.entry(MediaTypeId.MtCardstockGloss_176to220g, StatisticsAttributes.OutputMediaType.CARD_STOCK_GLOSSY),
                    Map.entry(MediaTypeId.MtColor, StatisticsAttributes.OutputMediaType.COLORED),
                    Map.entry(MediaTypeId.MtCoverMatte, StatisticsAttributes.OutputMediaType.COVER_MATTE),
                    Map.entry(MediaTypeId.MtEnvelope, StatisticsAttributes.OutputMediaType.ENVELOPE),
                    Map.entry(MediaTypeId.MtExtraHeavy_131to175g, StatisticsAttributes.OutputMediaType.EXTRA_HEAVY),
                    Map.entry(MediaTypeId.MtExtraHeavyGloss_131to175g, StatisticsAttributes.OutputMediaType.EXTRA_HEAVY_GLOSSY),
                    Map.entry(MediaTypeId.MtHeavy_111to130g, StatisticsAttributes.OutputMediaType.HEAVY),
                    Map.entry(MediaTypeId.MtHeavyEnvelope, StatisticsAttributes.OutputMediaType.HEAVY_ENVELOPE),
                    Map.entry(MediaTypeId.MtHeavyGloss_111to130g, StatisticsAttributes.OutputMediaType.HEAVY_GLOSSY),
                    Map.entry(MediaTypeId.MtHeavyRough, StatisticsAttributes.OutputMediaType.HEAVY_ROUGH),
                    Map.entry(MediaTypeId.MtHPAdvancedPhoto, StatisticsAttributes.OutputMediaType.HP_ADVANCED_PHOTO),
                    Map.entry(MediaTypeId.MtBrochureGlossy, StatisticsAttributes.OutputMediaType.HP_BROCHURE_GLOSSY),
                    Map.entry(MediaTypeId.MtHPBrochureMatte_180g, StatisticsAttributes.OutputMediaType.HP_BROCHURE_MATTE_180G),
                    Map.entry(MediaTypeId.MtHPCoverMatte_200g, StatisticsAttributes.OutputMediaType.HP_COVER_MATTE_200G),
                    Map.entry(MediaTypeId.MtHPEcoFFICIENT, StatisticsAttributes.OutputMediaType.HP_ECOFFICIENT),
                    Map.entry(MediaTypeId.MtHPEverydayPhotoMatte, StatisticsAttributes.OutputMediaType.HP_EVERYDAY_PHOTO_MATTE),
                    Map.entry(MediaTypeId.MtHPGloss_130g, StatisticsAttributes.OutputMediaType.HP_GLOSSY_120G),
                    Map.entry(MediaTypeId.MtHPGloss_160g, StatisticsAttributes.OutputMediaType.HP_GLOSSY_150G),
                    Map.entry(MediaTypeId.MtHPGloss_220g, StatisticsAttributes.OutputMediaType.HP_GLOSSY_200G),
                    Map.entry(MediaTypeId.MtHPMatte_90g, StatisticsAttributes.OutputMediaType.HP_MATTE_90G),
                    Map.entry(MediaTypeId.MtHPMatte_105g, StatisticsAttributes.OutputMediaType.HP_MATTE_105G),
                    Map.entry(MediaTypeId.MtHPMatte_120g, StatisticsAttributes.OutputMediaType.HP_MATTE_120G),
                    Map.entry(MediaTypeId.MtHPMatte_160g, StatisticsAttributes.OutputMediaType.HP_MATTE_150G),
                    Map.entry(MediaTypeId.MtHPMatte_200g, StatisticsAttributes.OutputMediaType.HP_MATTE_200G),
                    Map.entry(MediaTypeId.MtHPMatteBrochureAndFlyer_180g, StatisticsAttributes.OutputMediaType.HP_MATTE_BROCHURE_AND_FLYER_180G),
                    Map.entry(MediaTypeId.MtHPPhoto, StatisticsAttributes.OutputMediaType.HP_PHOTO),
                    Map.entry(MediaTypeId.MtHPPhotoPlus, StatisticsAttributes.OutputMediaType.HP_PHOTO_PLUS),
                    Map.entry(MediaTypeId.MtHPPremiumInkjetTransparency, StatisticsAttributes.OutputMediaType.HP_PREMIUM_INKJET_TRANSPARENCY),
                    Map.entry(MediaTypeId.MtHPPremiumMatte_120g, StatisticsAttributes.OutputMediaType.HP_PREMIUM_MATTE_120G),
                    Map.entry(MediaTypeId.MtHPSoftGloss_120g, StatisticsAttributes.OutputMediaType.HP_SOFT_GLOSS_120G),
                    Map.entry(MediaTypeId.MtHPTough, StatisticsAttributes.OutputMediaType.HP_TOUGH),
                    Map.entry(MediaTypeId.MtHPTrifoldGloss_160g, StatisticsAttributes.OutputMediaType.HP_TRIFOLD_GLOSSY_160G),
                    Map.entry(MediaTypeId.MtIntermediate_85to95g, StatisticsAttributes.OutputMediaType.INTERMEDIATE),
                    Map.entry(MediaTypeId.MtLabels, StatisticsAttributes.OutputMediaType.LABELS),
                    Map.entry(MediaTypeId.MtLetterhead, StatisticsAttributes.OutputMediaType.LETTERHEAD),
                    Map.entry(MediaTypeId.MtLight_60to74g, StatisticsAttributes.OutputMediaType.LIGHT),
                    Map.entry(MediaTypeId.MtMatte, StatisticsAttributes.OutputMediaType.MATTE),
                    Map.entry(MediaTypeId.MtMidweight_96to110g, StatisticsAttributes.OutputMediaType.MID_WEIGHT),
                    Map.entry(MediaTypeId.MtMidweightGloss_96to110g, StatisticsAttributes.OutputMediaType.MID_WEIGHT_GLOSSY),
                    Map.entry(MediaTypeId.MtOpaqueFilm, StatisticsAttributes.OutputMediaType.OPAQUE_FILM),
                    Map.entry(MediaTypeId.MtPhoto, StatisticsAttributes.OutputMediaType.PHOTO),
                    Map.entry(MediaTypeId.MtPlain, StatisticsAttributes.OutputMediaType.PLAIN),
                    Map.entry(MediaTypeId.MtPremiumInkjet, StatisticsAttributes.OutputMediaType.HP_INKJET_MATTE_120G),
                    Map.entry(MediaTypeId.MtPreprinted, StatisticsAttributes.OutputMediaType.PREPRINTED),
                    Map.entry(MediaTypeId.MtPrepunched, StatisticsAttributes.OutputMediaType.PREPUNCHED),
                    Map.entry(MediaTypeId.MtRecycled, StatisticsAttributes.OutputMediaType.RECYCLED),
                    Map.entry(MediaTypeId.MtRough, StatisticsAttributes.OutputMediaType.ROUGH),
                    Map.entry(MediaTypeId.MtShelfEdgeLabels, StatisticsAttributes.OutputMediaType.SHELF_EDGE_LABELS),
                    Map.entry(MediaTypeId.MtTab, StatisticsAttributes.OutputMediaType.TAB),
                    Map.entry(MediaTypeId.MtThickPlain, StatisticsAttributes.OutputMediaType.THICK_PLAIN),
                    Map.entry(MediaTypeId.MtTransparency, StatisticsAttributes.OutputMediaType.TRANSPARENCY),
                    Map.entry(MediaTypeId.MtVellum, StatisticsAttributes.OutputMediaType.VELLUM),
                    Map.entry(MediaTypeId.MtUserDefined1, StatisticsAttributes.OutputMediaType.USER_DEFINED_1),
                    Map.entry(MediaTypeId.MtUserDefined2, StatisticsAttributes.OutputMediaType.USER_DEFINED_2),
                    Map.entry(MediaTypeId.MtUserDefined3, StatisticsAttributes.OutputMediaType.USER_DEFINED_3),
                    Map.entry(MediaTypeId.MtUserDefined4, StatisticsAttributes.OutputMediaType.USER_DEFINED_4),
                    Map.entry(MediaTypeId.MtUserDefined5, StatisticsAttributes.OutputMediaType.USER_DEFINED_5),
                    Map.entry(MediaTypeId.MtUserDefined6, StatisticsAttributes.OutputMediaType.USER_DEFINED_6),
                    Map.entry(MediaTypeId.MtUserDefined7, StatisticsAttributes.OutputMediaType.USER_DEFINED_7),
                    Map.entry(MediaTypeId.MtUserDefined8, StatisticsAttributes.OutputMediaType.USER_DEFINED_8),
                    Map.entry(MediaTypeId.MtUserDefined9, StatisticsAttributes.OutputMediaType.USER_DEFINED_9),
                    Map.entry(MediaTypeId.MtUserDefined10, StatisticsAttributes.OutputMediaType.USER_DEFINED_10),
                    Map.entry(MediaTypeId.MtUserDefined11, StatisticsAttributes.OutputMediaType.USER_DEFINED_11),
                    Map.entry(MediaTypeId.MtUserDefined12, StatisticsAttributes.OutputMediaType.USER_DEFINED_12),
                    Map.entry(MediaTypeId.MtUserDefined13, StatisticsAttributes.OutputMediaType.USER_DEFINED_13),
                    Map.entry(MediaTypeId.MtUserDefined14, StatisticsAttributes.OutputMediaType.USER_DEFINED_14),
                    Map.entry(MediaTypeId.MtUserDefined15, StatisticsAttributes.OutputMediaType.USER_DEFINED_15),
                    Map.entry(MediaTypeId.MtUserDefined16, StatisticsAttributes.OutputMediaType.USER_DEFINED_16),
                    Map.entry(MediaTypeId.MtAny, StatisticsAttributes.OutputMediaType.ANY),
                    Map.entry(MediaTypeId.MtOther, StatisticsAttributes.OutputMediaType.OTHER),
                    Map.entry(MediaTypeId.MtLightBond, StatisticsAttributes.OutputMediaType.LIGHT_BOND),
                    Map.entry(MediaTypeId.MtLightPaperboard, StatisticsAttributes.OutputMediaType.LIGHT_PAPERBOARD),
                    Map.entry(MediaTypeId.MtLightRough, StatisticsAttributes.OutputMediaType.LIGHT_ROUGH),
                    Map.entry(MediaTypeId.MtPaperboard, StatisticsAttributes.OutputMediaType.PAPERBOARD),
                    Map.entry(MediaTypeId.MtAuto, StatisticsAttributes.OutputMediaType.AUTO),
                    Map.entry(MediaTypeId.MtHeavyBond, StatisticsAttributes.OutputMediaType.HEAVY_BOND),
                    Map.entry(MediaTypeId.MtHeavyPaperboard, StatisticsAttributes.OutputMediaType.HEAVY_PAPERBOARD),
                    Map.entry(MediaTypeId.MtHPGlossyEdgeline180G, StatisticsAttributes.OutputMediaType.HP_GLOSSY_EDGELINE_180G)
            )
    ),

    mediaSizeId(
            MediaSizeId.class,
            StatisticsAttributes.ScanSize.OTHER,
            Map.ofEntries(
                    Map.entry(MediaSizeId.MsANSI_A_8point5x11in, StatisticsAttributes.ScanSize.LETTER),
                    Map.entry(MediaSizeId.MsANSI_A_Rotated_8point5x11in, StatisticsAttributes.ScanSize.LETTER_ROTATE),
                    Map.entry(MediaSizeId.MsANSI_B_11x17in, StatisticsAttributes.ScanSize.LEDGER),
                    Map.entry(MediaSizeId.MsANSI_C_17x22in, StatisticsAttributes.ScanSize.ANSI_C_17X22in),
                    Map.entry(MediaSizeId.MsANSI_D_22x34in, StatisticsAttributes.ScanSize.ANSI_D_22X34in),
                    Map.entry(MediaSizeId.MsANSI_E_34x44in, StatisticsAttributes.ScanSize.ANSI_E_34X44in),
                    Map.entry(MediaSizeId.MsANSI_F_28x40in, StatisticsAttributes.ScanSize.ANSI_F_28X40in),
                    Map.entry(MediaSizeId.MsArchitectural_A_9x12in, StatisticsAttributes.ScanSize.ARCHITECTURAL_A_9X12in),
                    //                  Map.entry(MediaSizeId.MsArchitectural_B_12x18in, ScanSize.?),
                    Map.entry(MediaSizeId.MsArchitectural_C_18x24in, StatisticsAttributes.ScanSize.ARCHITECTURAL_C_18X24in),
                    Map.entry(MediaSizeId.MsArchitectural_D_24x36in, StatisticsAttributes.ScanSize.ARCHITECTURAL_D_24X36in),
                    Map.entry(MediaSizeId.MsArchitectural_E_36x48in, StatisticsAttributes.ScanSize.ARCHITECTURAL_E_36X48in),
                    Map.entry(MediaSizeId.MsArchitectural_E1_30x42in, StatisticsAttributes.ScanSize.ARCHITECTURAL_E1_30X42in),
                    Map.entry(MediaSizeId.MsArchitectural_E2_26x38in, StatisticsAttributes.ScanSize.ARCHITECTURAL_E2_26X38in),
                    Map.entry(MediaSizeId.MsArchitectural_E3_27x39in, StatisticsAttributes.ScanSize.ARCHITECTURAL_E3_27X39in),
                    Map.entry(MediaSizeId.MsDIN_2A0_1189x1682mm, StatisticsAttributes.ScanSize.DIN_2XA0_1189X1682mm),
                    Map.entry(MediaSizeId.MsDIN_4A0_1682x2378mm, StatisticsAttributes.ScanSize.DIN_4XA0_1682X2378mm),
                    Map.entry(MediaSizeId.MsDL_99x210mm, StatisticsAttributes.ScanSize.DL_99X210mm),
                    Map.entry(MediaSizeId.MsEnvelope_A2_4point375x5point75in, StatisticsAttributes.ScanSize.ENVELOPE_A2),
                    Map.entry(MediaSizeId.MsEnvelope_Catalog1_6x9in, StatisticsAttributes.ScanSize.ENVELOPE_CATALOG),
                    Map.entry(MediaSizeId.MsEnvelope_Comm10_4point125x9point5in, StatisticsAttributes.ScanSize.ENVELOPE_COMM10),
                    Map.entry(MediaSizeId.MsEnvelope_Comm6point75_3point625x6point5in, StatisticsAttributes.ScanSize.ENVELOPE_COMM6),
                    Map.entry(MediaSizeId.MsEnvelope_DL_110x220mm, StatisticsAttributes.ScanSize.ENVELOPE_DL),
                    Map.entry(MediaSizeId.MsEnvelope_Monarch_3point875x7point5in, StatisticsAttributes.ScanSize.ENVELOPE_MONARCH),
                    Map.entry(MediaSizeId.MsEnvelope_Windsor_3point875x8point875in, StatisticsAttributes.ScanSize.ENVELOPE_9),
                    Map.entry(MediaSizeId.MsExecutive_7point25x10point5in, StatisticsAttributes.ScanSize.EXECUTIVE),
                    Map.entry(MediaSizeId.MsExecutive_Rotated_7point25x10point5in, StatisticsAttributes.ScanSize.EXECUTIVE_ROTATE),
                    Map.entry(MediaSizeId.MsFoolscap_8point5x13in, StatisticsAttributes.ScanSize.INCH8POINT5X13),
                    Map.entry(MediaSizeId.MsGeneral_10x11in, StatisticsAttributes.ScanSize.GENERAL_10X11in),
                    Map.entry(MediaSizeId.MsGeneral_10x13in, StatisticsAttributes.ScanSize.GENERAL_10X13in),
                    Map.entry(MediaSizeId.MsGeneral_10x14in, StatisticsAttributes.ScanSize.GENERAL_10X14in),
                    Map.entry(MediaSizeId.MsGeneral_10x15in, StatisticsAttributes.ScanSize.GENERAL_10X15in),
                    Map.entry(MediaSizeId.MsGeneral_11x12in, StatisticsAttributes.ScanSize.GENERAL_11X12in),
                    Map.entry(MediaSizeId.MsGeneral_11x14in, StatisticsAttributes.ScanSize.GENERAL_11X14in),
                    Map.entry(MediaSizeId.MsGeneral_11x15in, StatisticsAttributes.ScanSize.GENERAL_11X15in),
                    Map.entry(MediaSizeId.MsGeneral_11x19in, StatisticsAttributes.ScanSize.GENERAL_11X19in),
                    Map.entry(MediaSizeId.MsGeneral_12x12in, StatisticsAttributes.ScanSize.GENERAL_12X12in),
                    Map.entry(MediaSizeId.MsGeneral_12x14in, StatisticsAttributes.ScanSize.GENERAL_12X14in),
                    Map.entry(MediaSizeId.MsGeneral_12x19in, StatisticsAttributes.ScanSize.GENERAL_12X19in),
                    Map.entry(MediaSizeId.MsGeneral_3point5x5in, StatisticsAttributes.ScanSize.GENERAL_3POINT5X5in),
                    Map.entry(MediaSizeId.MsGeneral_3x5in, StatisticsAttributes.ScanSize.GENERAL_3X5in),
                    Map.entry(MediaSizeId.MsGeneral_4x12in, StatisticsAttributes.ScanSize.GENERAL_4X12in),
                    Map.entry(MediaSizeId.MsGeneral_4x6in, StatisticsAttributes.ScanSize.GENERAL_4X6in),
                    Map.entry(MediaSizeId.MsGeneral_4x8in, StatisticsAttributes.ScanSize.GENERAL_4X8in),
                    Map.entry(MediaSizeId.MsGeneral_5x7in, StatisticsAttributes.ScanSize.GENERAL_5X7in),
                    Map.entry(MediaSizeId.MsGeneral_5x8in, StatisticsAttributes.ScanSize.GENERAL_5X8in),
                    Map.entry(MediaSizeId.MsGeneral_6x8in, StatisticsAttributes.ScanSize.GENERAL_6X8in),
                    Map.entry(MediaSizeId.MsGeneral_7x9in, StatisticsAttributes.ScanSize.GENERAL_7X9in),
                    Map.entry(MediaSizeId.MsGeneral_9x11in, StatisticsAttributes.ScanSize.GENERAL_9X11in),
                    Map.entry(MediaSizeId.MsGovt_Legal_8x13in, StatisticsAttributes.ScanSize.GOVT_LEGAL),
                    Map.entry(MediaSizeId.MsGovt_Letter_8x10in, StatisticsAttributes.ScanSize.GOVT_LETTER),
                    Map.entry(MediaSizeId.MsInvoice_5point5x8point5in, StatisticsAttributes.ScanSize.STATEMENT),
                    Map.entry(MediaSizeId.MsISO_A0_841x1189mm, StatisticsAttributes.ScanSize.A0),
                    Map.entry(MediaSizeId.MsISO_A1_594x841mm, StatisticsAttributes.ScanSize.A1),
                    Map.entry(MediaSizeId.MsISO_A2_420x594mm, StatisticsAttributes.ScanSize.A2),
                    Map.entry(MediaSizeId.MsISO_A3_297x420mm, StatisticsAttributes.ScanSize.A3),
                    Map.entry(MediaSizeId.MsISO_A4_210x297mm, StatisticsAttributes.ScanSize.A4),
                    Map.entry(MediaSizeId.MsISO_A4_Rotated_210x297mm, StatisticsAttributes.ScanSize.A4_ROTATE),
                    Map.entry(MediaSizeId.MsISO_A5_148x210mm, StatisticsAttributes.ScanSize.A5),
                    Map.entry(MediaSizeId.MsISO_A5_Rotated_148x210mm, StatisticsAttributes.ScanSize.A5_ROTATE),
                    Map.entry(MediaSizeId.MsISO_A6_105x148mm, StatisticsAttributes.ScanSize.A6),
                    Map.entry(MediaSizeId.MsISO_A7_74x105mm, StatisticsAttributes.ScanSize.A7),
                    Map.entry(MediaSizeId.MsISO_A8_52x74mm, StatisticsAttributes.ScanSize.A8),
                    Map.entry(MediaSizeId.MsISO_A9_37x52mm, StatisticsAttributes.ScanSize.A9),
                    Map.entry(MediaSizeId.MsISO_A10_26x37mm, StatisticsAttributes.ScanSize.A10),
                    Map.entry(MediaSizeId.MsISO_B0_1000x1414mm, StatisticsAttributes.ScanSize.B0),
                    Map.entry(MediaSizeId.MsISO_B1_707x1000mm, StatisticsAttributes.ScanSize.B1),
                    Map.entry(MediaSizeId.MsISO_B2_500x707mm, StatisticsAttributes.ScanSize.B2),
                    Map.entry(MediaSizeId.MsISO_B3_353x500mm, StatisticsAttributes.ScanSize.B3),
                    Map.entry(MediaSizeId.MsISO_B4_250x353mm, StatisticsAttributes.ScanSize.B4),
                    Map.entry(MediaSizeId.MsISO_B5_176x250mm, StatisticsAttributes.ScanSize.B5),
                    Map.entry(MediaSizeId.MsISO_B6_125x176mm, StatisticsAttributes.ScanSize.B6),
                    Map.entry(MediaSizeId.MsISO_B7_88x125mm, StatisticsAttributes.ScanSize.B7),
                    Map.entry(MediaSizeId.MsISO_B8_62x88mm, StatisticsAttributes.ScanSize.B8),
                    Map.entry(MediaSizeId.MsISO_B9_44x62mm, StatisticsAttributes.ScanSize.B9),
                    Map.entry(MediaSizeId.MsISO_B10_31x44mm, StatisticsAttributes.ScanSize.B10),
                    Map.entry(MediaSizeId.MsISO_C0_917x1297mm, StatisticsAttributes.ScanSize.C0),
                    Map.entry(MediaSizeId.MsISO_C1_648x917mm, StatisticsAttributes.ScanSize.C1),
                    Map.entry(MediaSizeId.MsISO_C2_458x648mm, StatisticsAttributes.ScanSize.C2),
                    Map.entry(MediaSizeId.MsISO_C3_324x458mm, StatisticsAttributes.ScanSize.C3),
                    Map.entry(MediaSizeId.MsISO_C4_229x324mm, StatisticsAttributes.ScanSize.C4),
                    Map.entry(MediaSizeId.MsISO_C5_162x229mm, StatisticsAttributes.ScanSize.C5),
                    Map.entry(MediaSizeId.MsISO_C6_114x162mm, StatisticsAttributes.ScanSize.C6),
                    Map.entry(MediaSizeId.MsISO_C7_81x114mm, StatisticsAttributes.ScanSize.C7),
                    Map.entry(MediaSizeId.MsISO_C8_57x81mm, StatisticsAttributes.ScanSize.C8),
                    Map.entry(MediaSizeId.MsISO_C9_40x57mm, StatisticsAttributes.ScanSize.C9),
                    Map.entry(MediaSizeId.MsISO_C10_28x40mm, StatisticsAttributes.ScanSize.C10),
                    Map.entry(MediaSizeId.MsJBusinessCard_55x91mm, StatisticsAttributes.ScanSize.BUSINESS_CARD),
                    Map.entry(MediaSizeId.MsJDoublePostcard_148x200mm, StatisticsAttributes.ScanSize.JDOUBLE_POSTCARD),
                    Map.entry(MediaSizeId.MsJDoublePostcard_Rotated_148x200mm, StatisticsAttributes.ScanSize.JDOUBLE_POSTCARD_ROTATE),
                    Map.entry(MediaSizeId.MsJIS_B0_1030x1456mm, StatisticsAttributes.ScanSize.JB0),
                    Map.entry(MediaSizeId.MsJIS_B1_728x1030mm, StatisticsAttributes.ScanSize.JB1),
                    Map.entry(MediaSizeId.MsJIS_B2_515x728mm, StatisticsAttributes.ScanSize.JB2),
                    Map.entry(MediaSizeId.MsJIS_B3_364x515mm, StatisticsAttributes.ScanSize.JB3),
                    Map.entry(MediaSizeId.MsJIS_B4_257x364mm, StatisticsAttributes.ScanSize.JB4),
                    Map.entry(MediaSizeId.MsJIS_B5_182x257mm, StatisticsAttributes.ScanSize.JB5),
                    Map.entry(MediaSizeId.MsJIS_B5_Rotated_182x257mm, StatisticsAttributes.ScanSize.JB5_ROTATE),
                    Map.entry(MediaSizeId.MsJIS_B6_128x182mm, StatisticsAttributes.ScanSize.JB6),
                    Map.entry(MediaSizeId.MsJIS_B7_91x128mm, StatisticsAttributes.ScanSize.JB7),
                    Map.entry(MediaSizeId.MsJIS_B8_64x91mm, StatisticsAttributes.ScanSize.JB8),
                    Map.entry(MediaSizeId.MsJIS_B9_45x64mm, StatisticsAttributes.ScanSize.JB9),
                    Map.entry(MediaSizeId.MsJIS_B10_32x45mm, StatisticsAttributes.ScanSize.JB10),
                    Map.entry(MediaSizeId.MsJIS_Chou3_120x235mm, StatisticsAttributes.ScanSize.JCHOU3),
                    Map.entry(MediaSizeId.MsJIS_Chou4_90x205mm, StatisticsAttributes.ScanSize.JCHOU4),
                    Map.entry(MediaSizeId.MsJIS_Exec_216x330mm, StatisticsAttributes.ScanSize.JEXEC),
                    Map.entry(MediaSizeId.MsJIS_Kaku2_240x332mm, StatisticsAttributes.ScanSize.JKAKU2),
                    Map.entry(MediaSizeId.MsJPostcard_100x148mm, StatisticsAttributes.ScanSize.JPOSTCARD),
                    Map.entry(MediaSizeId.MsK16_184x260mm, StatisticsAttributes.ScanSize.K16_184X260mm),
                    Map.entry(MediaSizeId.MsK16_195x270mm, StatisticsAttributes.ScanSize.K16),
                    Map.entry(MediaSizeId.MsK16_197x273mm, StatisticsAttributes.ScanSize.PK16),
                    Map.entry(MediaSizeId.MsK8_260x368mm, StatisticsAttributes.ScanSize.K8_260X368mm),
                    Map.entry(MediaSizeId.MsK8_270x390mm, StatisticsAttributes.ScanSize.K8),
                    Map.entry(MediaSizeId.MsK8_273x394mm, StatisticsAttributes.ScanSize.PK8),
                    Map.entry(MediaSizeId.MsLegal_8point5x14in, StatisticsAttributes.ScanSize.LEGAL),
                    Map.entry(MediaSizeId.MsLongScan_8point5x34in, StatisticsAttributes.ScanSize.LONG_SCAN),
                    Map.entry(MediaSizeId.MsMutsugiri_203x254mm, StatisticsAttributes.ScanSize.MUTSUGIRI),
                    Map.entry(MediaSizeId.MsOficio_216x340mm, StatisticsAttributes.ScanSize.OFICIO),
                    Map.entry(MediaSizeId.MsRA0_860x1220mm, StatisticsAttributes.ScanSize.RA0),
                    Map.entry(MediaSizeId.MsRA1_610x860mm, StatisticsAttributes.ScanSize.RA1),
                    Map.entry(MediaSizeId.MsRA2_430x610mm, StatisticsAttributes.ScanSize.RA2),
                    Map.entry(MediaSizeId.MsRA3_305x430mm, StatisticsAttributes.ScanSize.RA3),
                    Map.entry(MediaSizeId.MsRA4_215x305mm, StatisticsAttributes.ScanSize.RA4),
                    Map.entry(MediaSizeId.MsSRA0_900x1280mm, StatisticsAttributes.ScanSize.SRA0),
                    Map.entry(MediaSizeId.MsSRA1_640x900mm, StatisticsAttributes.ScanSize.SRA1),
                    Map.entry(MediaSizeId.MsSRA2_450x640mm, StatisticsAttributes.ScanSize.SRA2),
                    Map.entry(MediaSizeId.MsSRA3_320x450mm, StatisticsAttributes.ScanSize.SRA3),
                    Map.entry(MediaSizeId.MsSRA4_225x320mm, StatisticsAttributes.ScanSize.SRA4),
                    Map.entry(MediaSizeId.MsSuper_B_13x19in, StatisticsAttributes.ScanSize.SUPER_B),
                    Map.entry(MediaSizeId.MsAny, StatisticsAttributes.ScanSize.AUTO),
                    Map.entry(MediaSizeId.MsMatchOriginal, StatisticsAttributes.ScanSize.MATCH_ORIGINAL),
                    Map.entry(MediaSizeId.MsMixedLetterLegal, StatisticsAttributes.ScanSize.MIXED_LETTER_LEGAL),
                    Map.entry(MediaSizeId.MsMixedLetterLedger, StatisticsAttributes.ScanSize.MIXED_LETTER_LEDGER),
                    Map.entry(MediaSizeId.MsMixedA4A3, StatisticsAttributes.ScanSize.MIXED_A3_A4),
                    Map.entry(MediaSizeId.MsCustom, StatisticsAttributes.ScanSize.CUSTOM),
                    Map.entry(MediaSizeId.MsUnknown, StatisticsAttributes.ScanSize.UNKNOWN),
                    Map.entry(MediaSizeId.MsUnknownEnvelope, StatisticsAttributes.ScanSize.UNKNOWN_ENVELOP),
                    Map.entry(MediaSizeId.MsISO_INDEXCARD_100x150mm, StatisticsAttributes.ScanSize.INDEXCARD),
                    Map.entry(MediaSizeId.MsAnyCustom, StatisticsAttributes.ScanSize.ANY_CUSTOM)

            )
    ),

    counterUnitToAgentUnit(
            CounterUnit.class,
            PrintAgentInfo.AgentUnit.OTHER,
            Map.ofEntries(
                    Map.entry(CounterUnit.CuPixels, PrintAgentInfo.AgentUnit.PIXELS),
                    Map.entry(CounterUnit.CuImpressions, PrintAgentInfo.AgentUnit.IMPRESSIONS),
                    Map.entry(CounterUnit.CuThousandsOfDots, PrintAgentInfo.AgentUnit.DROPS)
            )
    ),

    supplyTypeToConsumableType(
            SupplyType.class,
            PrintAgentInfo.AgentConsumableType.OTHER,
            Map.ofEntries(
                    Map.entry(SupplyType.StInkCartridge, PrintAgentInfo.AgentConsumableType.INKCARTRIDGE),
                    Map.entry(SupplyType.StTonerCartridge, PrintAgentInfo.AgentConsumableType.INKCARTRIDGE),
                    Map.entry(SupplyType.StOther, PrintAgentInfo.AgentConsumableType.OTHER)
            )
    ),

    supplyAgentTypeToContentType(
            SupplyAgentType.class,
            PrintAgentInfo.AgentConsumableContentType.OTHER,
            Map.ofEntries(
                    Map.entry(SupplyAgentType.SatFixing, PrintAgentInfo.AgentConsumableContentType.FIXINGAGENT),
                    Map.entry(SupplyAgentType.SatGlossing, PrintAgentInfo.AgentConsumableContentType.GLOSSINGAGENT),
                    Map.entry(SupplyAgentType.SatMarking, PrintAgentInfo.AgentConsumableContentType.MARKINGAGENT),
                    Map.entry(SupplyAgentType.SatOther, PrintAgentInfo.AgentConsumableContentType.OTHER)
            )
    ),

    supplyColorToWorkpathColor(
            SupplyColorCode.class,
            StatisticsAttributes.Color.OTHER,
            Map.ofEntries(
                    Map.entry(com.hp.ext.types.supply.SupplyColorCode.SccK, StatisticsAttributes.Color.BLACK),
                    Map.entry(com.hp.ext.types.supply.SupplyColorCode.SccMK, StatisticsAttributes.Color.BLACK),
                    Map.entry(com.hp.ext.types.supply.SupplyColorCode.SccPK, StatisticsAttributes.Color.BLACK),
                    Map.entry(com.hp.ext.types.supply.SupplyColorCode.SccC, StatisticsAttributes.Color.CYAN),
                    Map.entry(com.hp.ext.types.supply.SupplyColorCode.SccLC, StatisticsAttributes.Color.LIGHT_CYAN),
                    Map.entry(com.hp.ext.types.supply.SupplyColorCode.SccM, StatisticsAttributes.Color.MAGENTA),
                    Map.entry(com.hp.ext.types.supply.SupplyColorCode.SccLM, StatisticsAttributes.Color.LIGHT_MAGENTA),
                    Map.entry(com.hp.ext.types.supply.SupplyColorCode.SccY, StatisticsAttributes.Color.YELLOW),
                    Map.entry(com.hp.ext.types.supply.SupplyColorCode.SccR, StatisticsAttributes.Color.RED),
                    Map.entry(com.hp.ext.types.supply.SupplyColorCode.SccG, StatisticsAttributes.Color.GREEN),
                    Map.entry(com.hp.ext.types.supply.SupplyColorCode.SccGN, StatisticsAttributes.Color.GREEN),
                    Map.entry(com.hp.ext.types.supply.SupplyColorCode.SccB, StatisticsAttributes.Color.BLUE),
                    Map.entry(com.hp.ext.types.supply.SupplyColorCode.SccW, StatisticsAttributes.Color.WHITE),
                    Map.entry(com.hp.ext.types.supply.SupplyColorCode.SccO, StatisticsAttributes.Color.ORANGE),
                    Map.entry(com.hp.ext.types.supply.SupplyColorCode.SccP, StatisticsAttributes.Color.PINK),
                    Map.entry(com.hp.ext.types.supply.SupplyColorCode.SccV, StatisticsAttributes.Color.VIOLET),
                    Map.entry(com.hp.ext.types.supply.SupplyColorCode.SccCMY, StatisticsAttributes.Color.CYAN_MAGENTA_YELLOW),
                    Map.entry(com.hp.ext.types.supply.SupplyColorCode.SccCMYK, StatisticsAttributes.Color.BLACK_CYAN_MAGENTA_YELLOW),
                    Map.entry(com.hp.ext.types.supply.SupplyColorCode.SccMultiColor, StatisticsAttributes.Color.BLACK_CYAN_MAGENTA_YELLOW),
                    Map.entry(com.hp.ext.types.supply.SupplyColorCode.SccS, StatisticsAttributes.Color.SILVER),
                    Map.entry(com.hp.ext.types.supply.SupplyColorCode.SccF, StatisticsAttributes.Color.FLOURESCENT_PINK),
                    Map.entry(com.hp.ext.types.supply.SupplyColorCode.SccE, StatisticsAttributes.Color.SEPIA),
                    Map.entry(com.hp.ext.types.supply.SupplyColorCode.SccDG, StatisticsAttributes.Color.DARK_GRAY),
                    Map.entry(com.hp.ext.types.supply.SupplyColorCode.SccLG, StatisticsAttributes.Color.LIGHT_GRAY)
            )
    ),

    authenticationKind(
            AuthenticationKind.class,
            ExtendedUserInfo.AuthenticationType.OTHER,
            Map.ofEntries(
                    Map.entry(AuthenticationKind.AkWindows, ExtendedUserInfo.AuthenticationType.WINDOWS),
                    Map.entry(AuthenticationKind.AkWindowsSmartCard, ExtendedUserInfo.AuthenticationType.WINDOWS_SMART_CARD),
                    Map.entry(AuthenticationKind.AkLDAP, ExtendedUserInfo.AuthenticationType.LDAP),
                    Map.entry(AuthenticationKind.AkPIN, ExtendedUserInfo.AuthenticationType.PIN)
            )
    ),

    processedBy(
            ProcessedBy.class,
            StatisticsAttributes.JobProcessedBy.OTHER,
            Map.ofEntries(
                    Map.entry(ProcessedBy.PbDevice, StatisticsAttributes.JobProcessedBy.DEVICE),
                    Map.entry(ProcessedBy.PbDss, StatisticsAttributes.JobProcessedBy.DSS),
                    Map.entry(ProcessedBy.PbOther, StatisticsAttributes.JobProcessedBy.OTHER)
            )
    ),

    sipTransport(
            com.hp.ext.types.fax.SipTransport.class,
            FaxAttributes.IpTransport.TCP,
            Map.ofEntries(
                    Map.entry(com.hp.ext.types.fax.SipTransport.StTCP, FaxAttributes.IpTransport.TCP),
                    Map.entry(com.hp.ext.types.fax.SipTransport.StUDP, FaxAttributes.IpTransport.UDP),
                    Map.entry(com.hp.ext.types.fax.SipTransport.StTLS, FaxAttributes.IpTransport.TLS)
            )
    ),

    t38Transport(
            com.hp.ext.types.fax.T38Transport.class,
            FaxAttributes.T38Transport.t38tTCP,  // 기본값
            Map.ofEntries(
                    Map.entry(com.hp.ext.types.fax.T38Transport.T38tTCP, FaxAttributes.T38Transport.t38tTCP),
                    Map.entry(com.hp.ext.types.fax.T38Transport.T38tUDP, FaxAttributes.T38Transport.t38tUDP)
            )
    );

    private final Category category;
    private final ITypeConverter<?, ?> converter;

    /**
     * Enum constructor for creating a default (1:1 mapping) type converter
     */
    <E, W> StatisticsTypeMapping(Class<E> e2Type, W wDefault, Map<E, W> mapping) {
        category = Category.DEFAULT;
        converter = new DefaultTypeConverter<E, W>(e2Type, wDefault) {{
            mapEtoW.putAll(mapping);
        }};
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
}
