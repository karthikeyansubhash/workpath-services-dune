// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.statistics.jobinfo;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;
import com.hp.workpath.api.statistics.jobinfo.emailinfo.EmailInfo;
import com.hp.jetadvantage.link.common.annotation.DeviceApi;

/**
 * Provides StatisticsAttributes
 *
 * @since API 5
 */
@DeviceApi
public class StatisticsAttributes {

    /**
     * Enums of JobCategory
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
        @SerializedName("Print")
        PRINT,

        /**
         * DigitalSend
         *
         * @since API 5
         */
        @SerializedName("DigitalSend")
        DIGITALSEND,

        /**
         * Fax
         *
         * @since API 5
         */
        @SerializedName("Fax")
        FAX,

        /**
         * Stored
         *
         * @since API 5
         */
        @SerializedName("Stored")
        STORED,

        /**
         * Service
         *
         * @since API 5
         */
        @SerializedName("Service")
        SERVICE,

        /**
         * ConfigurationDiagnostic
         *
         * @since API 5
         */
        @SerializedName("ConfigurationDiagnostic")
        CONFIGURATIONDIAGNOSTIC,

        /**
         * Mixed
         *
         * @since API 5
         */
        @SerializedName("Mixed")
        MIXED,

        /**
         * Notification
         *
         * @since API 5
         */
        @SerializedName("Notification")
        NOTIFICATION,

        /**
         * Other
         *
         * @since API 5
         */
        @SerializedName("Other")
        OTHER
    }

    /**
     * Enums of JobDataSource
     *
     * @since API 5
     */
    @Keep
    public enum JobDataSource {
        /**
         * NetworkIO
         *
         * @since API 5
         */
        @SerializedName("NetworkIO")
        NETWORKIO,

        /**
         * DirectIO
         *
         * @since API 5
         */
        @SerializedName("DirectIO")
        DIRECTIO,

        /**
         * InternalStorage
         *
         * @since API 5
         */
        @SerializedName("InternalStorage")
        INTERNALSTORAGE,

        /**
         * RemovableStorage
         *
         * @since API 5
         */
        @SerializedName("RemovableStorage")
        REMOVABLESTORAGE,

        /**
         * Scanner
         *
         * @since API 5
         */
        @SerializedName("Scanner")
        SCANNER,

        /**
         * WebService
         *
         * @since API 5
         */
        @SerializedName("WebService")
        WEBSERVICE,

        /**
         * Ews
         *
         * @since API 5
         */
        @SerializedName("Ews")
        EWS,

        /**
         * FaxCard
         *
         * @since API 5
         */
        @SerializedName("FaxCard")
        FAXCARD,

        /**
         * Internal
         *
         * @since API 5
         */
        @SerializedName("Internal")
        INTERNAL,

        /**
         * IPPWalkup
         *
         * @since API 5
         */
        @SerializedName("IPPWalkup")
        IPPWALKUP,

        /**
         * Other
         *
         * @since API 5
         */
        @SerializedName("Other")
        OTHER
    }

    /**
     * Enums of JobDestination
     *
     * @since API 5
     */
    @Keep
    public enum JobDestination {
        /**
         * InternalStorage
         *
         * @since API 5
         */
        @SerializedName("InternalStorage")
        INTERNALSTORAGE,

        /**
         * RemovableStorage
         *
         * @since API 5
         */
        @SerializedName("RemovableStorage")
        REMOVABLESTORAGE,

        /**
         * PrintEngine
         *
         * @since API 5
         */
        @SerializedName("PrintEngine")
        PRINTENGINE,

        /**
         * Email
         *
         * @since API 5
         */
        @SerializedName("Email")
        EMAIL,

        /**
         * NetworkFolder
         *
         * @since API 5
         */
        @SerializedName("NetworkFolder")
        NETWORKFOLDER,

        /**
         * Ftp
         *
         * @since API 5
         */
        @SerializedName("Ftp")
        FTP,

        /**
         * Http
         *
         * @since API 5
         */
        @SerializedName("Http")
        HTTP,

        /**
         * FaxCard
         *
         * @since API 5
         */
        @SerializedName("FaxCard")
        FAXCARD,

        /**
         * Scanner
         *
         * @since API 5
         */
        @SerializedName("Scanner")
        SCANNER,

        /**
         * WebService
         *
         * @since API 5
         */
        @SerializedName("WebService")
        WEBSERVICE,

        /**
         * Other
         *
         * @since API 5
         */
        @SerializedName("Other")
        OTHER
    }

    /**
     * Enums of JobState
     *
     * @since API 5
     */
    @Keep
    public enum JobState {
        /**
         * Job was completed successfully.
         *
         * @since API 5
         */
        @SerializedName("Succeeded")
        SUCCEEDED,
        /**
         * Job was partially completed.
         *
         * @since API 5
         */
        @SerializedName("PartiallySucceeded")
        PARTIALLYSUCCEEDED,
        /**
         * Job failed prior to completion.
         *
         * @since API 5
         */
        @SerializedName("Failed")
        FAILED,
        /**
         * Job was canceled.
         *
         * @since API 5
         */
        @SerializedName("Canceled")
        CANCELED
    }

    /**
     * Enums of JobPausedState
     *
     * @since API 5
     */
    @Keep
    public enum JobPausedState {
        /**
         * None
         *
         * @since API 5
         */
        @SerializedName("None")
        NONE,

        /**
         * True
         *
         * @since API 5
         */
        @SerializedName("True")
        TRUE,

        /**
         * False
         *
         * @since API 5
         */
        @SerializedName("False")
        FALSE
    }

    /**
     * Enums of JobProcessedBy
     *
     * @since API 5
     */
    @Keep
    public enum JobProcessedBy {
        /**
         * Device
         *
         * @since API 5
         */
        @SerializedName("Device")
        DEVICE,

        /**
         * Dss
         *
         * @since API 5
         */
        @SerializedName("Dss")
        DSS,

        /**
         * Other
         *
         * @since API 5
         */
        @SerializedName("Other")
        OTHER
    }

    /**
     * Enums of PaperSource
     *
     * @since API 5
     */
    @Keep
    public enum PaperSource {
        /**
         * Flatbed
         *
         * @since API 5
         */
        @SerializedName("Flatbed")
        FLATBED,

        /**
         * Adf
         *
         * @since API 5
         */
        @SerializedName("Adf")
        ADF,

        /**
         * Envelope Feed
         *
         * @since API 5
         */
        @SerializedName("EnvelopeFeed")
        ENVELOPE_FEED,

        /**
         * Manual Feed Tray
         *
         * @since API 5
         */
        @SerializedName("ManualFeedTray")
        MANUAL_FEED_TRAY,

        /**
         * Tray 1
         *
         * @since API 5
         */
        @SerializedName("Tray1")
        TRAY_1,

        /**
         * Tray 2
         *
         * @since API 5
         */
        @SerializedName("Tray2")
        TRAY_2,

        /**
         * Tray 3
         *
         * @since API 5
         */
        @SerializedName("Tray3")
        TRAY_3,

        /**
         * Tray 4
         *
         * @since API 5
         */
        @SerializedName("Tray4")
        TRAY_4,

        /**
         * Tray 5
         *
         * @since API 5
         */
        @SerializedName("Tray5")
        TRAY_5,

        /**
         * Tray 6
         *
         * @since API 5
         */
        @SerializedName("Tray6")
        TRAY_6,

        /**
         * Tray 7
         *
         * @since API 5
         */
        @SerializedName("Tray7")
        TRAY_7,

        /**
         * Tray 8
         *
         * @since API 5
         */
        @SerializedName("Tray8")
        TRAY_8,

        /**
         * Tray 9
         *
         * @since API 5
         */
        @SerializedName("Tray9")
        TRAY_9,

        /**
         * Tray 10
         *
         * @since API 5
         */
        @SerializedName("Tray10")
        TRAY_10,

        /**
         * Tray 11
         *
         * @since API 5
         */
        @SerializedName("Tray11")
        TRAY_11,

        /**
         * Tray 12
         *
         * @since API 5
         */
        @SerializedName("Tray12")
        TRAY_12,

        /**
         * Tray 13
         *
         * @since API 5
         */
        @SerializedName("Tray13")
        TRAY_13,

        /**
         * Tray 14
         *
         * @since API 5
         */
        @SerializedName("Tray14")
        TRAY_14,

        /**
         * Tray 15
         *
         * @since API 5
         */
        @SerializedName("Tray15")
        TRAY_15,

        /**
         * Tray 16
         *
         * @since API 5
         */
        @SerializedName("Tray16")
        TRAY_16,

        /**
         * Auto
         *
         * @since API 5
         */
        @SerializedName("Auto")
        AUTO,

        /**
         * Other
         *
         * @since API 5
         */
        @SerializedName("Other")
        OTHER,

        /**
         * Duplexer
         *
         * @since API 5
         */
        @SerializedName("Duplexer")
        DUPLEXER,

        /**
         * External
         *
         * @since API 5
         */
        @SerializedName("External")
        EXTERNAL,

        /**
         * External Tray 1
         *
         * @since API 5
         */
        @SerializedName("ExternalTray1")
        EXTERNAL_TRAY_1,

        /**
         * External Tray 2
         *
         * @since API 5
         */
        @SerializedName("ExternalTray2")
        EXTERNAL_TRAY_2,

        /**
         * External Tray 3
         *
         * @since API 5
         */
        @SerializedName("ExternalTray3")
        EXTERNAL_TRAY_3,

        /**
         * External Tray 4
         *
         * @since API 5
         */
        @SerializedName("ExternalTray4")
        EXTERNAL_TRAY_4,

        /**
         * External Tray 5
         *
         * @since API 5
         */
        @SerializedName("ExternalTray5")
        EXTERNAL_TRAY_5,

        /**
         * External Tray 6
         *
         * @since API 5
         */
        @SerializedName("ExternalTray6")
        EXTERNAL_TRAY_6,

        /**
         * External Tray 7
         *
         * @since API 5
         */
        @SerializedName("ExternalTray7")
        EXTERNAL_TRAY_7,

        /**
         * External Tray 8
         *
         * @since API 5
         */
        @SerializedName("ExternalTray8")
        EXTERNAL_TRAY_8,

        /**
         * External Tray 9
         *
         * @since API 5
         */
        @SerializedName("ExternalTray9")
        EXTERNAL_TRAY_9,

        /**
         * External Tray 10
         *
         * @since API 5
         */
        @SerializedName("ExternalTray10")
        EXTERNAL_TRAY_10,

        /**
         * Multipurpose Tray
         *
         * @since API 5
         */
        @SerializedName("MultipurposeTray")
        MULTIPURPOSE_TRAY,

        /**
         * Photo Tray
         *
         * @since API 5
         */
        @SerializedName("PhotoTray")
        PHOTO_TRAY,

        /**
         * Rear Manual Feed
         *
         * @since API 5
         */
        @SerializedName("RearManualFeed")
        REAR_MANUAL_FEED,

        /**
         * Roll 1
         *
         * @since API 5
         */
        @SerializedName("Roll1")
        ROLL_1,

        /**
         * Roll 2
         *
         * @since API 5
         */
        @SerializedName("Roll2")
        ROLL_2,

        /**
         * Roll 3
         *
         * @since API 5
         */
        @SerializedName("Roll3")
        ROLL_3,

        /**
         * Roll 4
         *
         * @since API 5
         */
        @SerializedName("Roll4")
        ROLL_4,

        /**
         * Envelope Feed Job Settings
         *
         * @since API 5
         */
        @SerializedName("EnvelopeFeedJobSettings")
        ENVELOPE_FEED_JOB_SETTINGS,

        /**
         * Tray 1 Job Settings
         *
         * @since API 5
         */
        @SerializedName("Tray1JobSettings")
        TRAY_1_JOB_SETTINGS
    }

    /**
     * Enums of ScanSize
     *
     * @since API 5
     */
    @Keep
    public enum ScanSize {
        /**
         * Letter (8.5inch x 11inch)
         *
         * @since API 5
         */
        @SerializedName("ANSI_A_8point5x11in")
        LETTER,

        /**
         * Letter Rotated (11inch x 8.5inch)
         *
         * @since API 5
         */
        @SerializedName("ANSI_A_Rotated_8point5x11in")
        LETTER_ROTATE,

        /**
         * Ledger (11inch x 17inch)
         *
         * @since API 5
         */
        @SerializedName("ANSI_B_11x17in")
        LEDGER,

        /**
         * ANSI C (17inch x 22inch)
         *
         * @since API 5
         */
        @SerializedName("ANSI_C_17x22in")
        ANSI_C_17X22in,

        /**
         * ANSI D (22inch x 34inch)
         *
         * @since API 5
         */
        @SerializedName("ANSI_D_22x34in")
        ANSI_D_22X34in,

        /**
         * ANSI E (34inch x 44inch)
         *
         * @since API 5
         */
        @SerializedName("ANSI_E_34x44in")
        ANSI_E_34X44in,

        /**
         * ANSI F (28inch x 40inch)
         *
         * @since API 5
         */
        @SerializedName("ANSI_F_28x40in")
        ANSI_F_28X40in,

        /**
         * Architectural A (9inch x 12inch)
         *
         * @since API 5
         */
        @SerializedName("Architectural_A_9x12in")
        ARCHITECTURAL_A_9X12in,

        /**
         * INCH12X18 (12inch x 18inch)
         *
         * @since API 5
         */
        @SerializedName("Architectural_B_12x18in")
        INCH12X18,

        /**
         * Architectural C (18inch x 24inch)
         *
         * @since API 5
         */
        @SerializedName("Architectural_C_18x24in")
        ARCHITECTURAL_C_18X24in,

        /**
         * Architectural D (24inch x 36inch)
         *
         * @since API 5
         */
        @SerializedName("Architectural_D_24x36in")
        ARCHITECTURAL_D_24X36in,

        /**
         * Architectural E (36inch x 48inch)
         *
         * @since API 5
         */
        @SerializedName("Architectural_E_36x48in")
        ARCHITECTURAL_E_36X48in,

        /**
         * Architectural E1 (30inch x 42inch)
         *
         * @since API 5
         */
        @SerializedName("Architectural_E1_30x42in")
        ARCHITECTURAL_E1_30X42in,

        /**
         * Architectural E2 (26inch x 38inch)
         *
         * @since API 5
         */
        @SerializedName("Architectural_E2_26x38in")
        ARCHITECTURAL_E2_26X38in,

        /**
         * Architectural E3 (27inch x 39inch)
         *
         * @since API 5
         */
        @SerializedName("Architectural_E3_27x39in")
        ARCHITECTURAL_E3_27X39in,

        /**
         * DIN 2A0 (1189mm x 1682mm)
         *
         * @since API 5
         */
        @SerializedName("DIN_2A0_1189x1682mm")
        DIN_2XA0_1189X1682mm,

        /**
         * DIN 4A0 (1682mm x 2378mm)
         *
         * @since API 5
         */
        @SerializedName("DIN_4A0_1682x2378mm")
        DIN_4XA0_1682X2378mm,

        /**
         * DL (99mm x 210mm)
         *
         * @since API 5
         */
        @SerializedName("DL_99x210mm")
        DL_99X210mm,

        /**
         * Envelope A2 (4.375inch x 5.75inch)
         *
         * @since API 5
         */
        @SerializedName("Envelope_A2_4point375x5point75in")
        ENVELOPE_A2,

        /**
         * Envelope Catalog1 (6inch x 9inch)
         *
         * @since API 5
         */
        @SerializedName("Envelope_Catalog1_6x9in")
        ENVELOPE_CATALOG,

        /**
         * Envelope Comm10 (4.125inch x 9.5inch)
         *
         * @since API 5
         */
        @SerializedName("Envelope_Comm10_4point125x9point5in")
        ENVELOPE_COMM10,

        /**
         * Envelope Comm6.75 (3.625inch x 6.5inch)
         *
         * @since API 5
         */
        @SerializedName("Envelope_Comm6point75_3point625x6point5in")
        ENVELOPE_COMM6,

        /**
         * Envelope DL (110mm x 220mm)
         *
         * @since API 5
         */
        @SerializedName("Envelope_DL_110x220mm")
        ENVELOPE_DL,

        /**
         * Envelope Monarch (3.875inch x 7.5inch)
         *
         * @since API 5
         */
        @SerializedName("Envelope_Monarch_3point875x7point5in")
        ENVELOPE_MONARCH,

        /**
         * Envelope 9 (3.875inch x 8.875inch)
         *
         * @since API 5
         */
        @SerializedName("Envelope_Windsor_3point875x8point875in")
        ENVELOPE_9,

        /**
         * Executive (7.25inch x 10.5inch)
         *
         * @since API 5
         */
        @SerializedName("Executive_7point25x10point5in")
        EXECUTIVE,

        /**
         * Executive Rotated (10.5inch x 7.25inch)
         *
         * @since API 5
         */
        @SerializedName("Executive_Rotated_7point25x10point5in")
        EXECUTIVE_ROTATE,

        /**
         * INCH8POINT5X13 (8.5inch x 13inch)
         *
         * @since API 5
         */
        @SerializedName("Foolscap_8point5x13in")
        INCH8POINT5X13,

        /**
         * GENERAL_10X11in (10inch x 11inch)
         *
         * @since API 5
         */
        @SerializedName("General_10x11in")
        GENERAL_10X11in,

        /**
         * GENERAL_10X13in (10inch x 13inch)
         *
         * @since API 5
         */
        @SerializedName("General_10x13in")
        GENERAL_10X13in,

        /**
         * GENERAL_10X14in (10inch x 14inch)
         *
         * @since API 5
         */
        @SerializedName("General_10x14in")
        GENERAL_10X14in,

        /**
         * GENERAL_10X15in (10inch x 15inch)
         *
         * @since API 5
         */
        @SerializedName("General_10x15in")
        GENERAL_10X15in,

        /**
         * GENERAL_11X12in (11inch x 12inch)
         *
         * @since API 5
         */
        @SerializedName("General_11x12in")
        GENERAL_11X12in,

        /**
         * GENERAL_11X14in (11inch x 14inch)
         *
         * @since API 5
         */
        @SerializedName("General_11x14in")
        GENERAL_11X14in,

        /**
         * GENERAL_11X15in (11inch x 15inch)
         *
         * @since API 5
         */
        @SerializedName("General_11x15in")
        GENERAL_11X15in,

        /**
         * GENERAL_11X19in (11inch x 19inch)
         *
         * @since API 5
         */
        @SerializedName("General_11x19in")
        GENERAL_11X19in,

        /**
         * GENERAL_12X12in (12inch x 12inch)
         *
         * @since API 5
         */
        @SerializedName("General_12x12in")
        GENERAL_12X12in,

        /**
         * GENERAL_12X14in (12inch x14inch)
         *
         * @since API 5
         */
        @SerializedName("General_12x14in")
        GENERAL_12X14in,

        /**
         * GENERAL_12X19in (12inch x 19inch)
         *
         * @since API 5
         */
        @SerializedName("General_12x19in")
        GENERAL_12X19in,

        /**
         * GENERAL_3POINT5X5in (3.5inch x 5inch)
         *
         * @since API 5
         */
        @SerializedName("General_3point5x5in")
        GENERAL_3POINT5X5in,

        /**
         * GENERAL_3X5in (3inch x 5inch)
         *
         * @since API 5
         */
        @SerializedName("General_3x5in")
        GENERAL_3X5in,

        /**
         * GENERAL_4X12in (4inch x 12inch)
         *
         * @since API 5
         */
        @SerializedName("General_4x12in")
        GENERAL_4X12in,

        /**
         * GENERAL_4X6in (4inch x 6inch)
         *
         * @since API 5
         */
        @SerializedName("General_4x6in")
        GENERAL_4X6in,

        /**
         * GENERAL_4X8in (4inch x 8inch)
         *
         * @since API 5
         */
        @SerializedName("General_4x8in")
        GENERAL_4X8in,

        /**
         * GENERAL_5X7in (5inch x 7inch)
         *
         * @since API 5
         */
        @SerializedName("General_5x7in")
        GENERAL_5X7in,

        /**
         * GENERAL_5X8in (5inch x 8inch)
         *
         * @since API 5
         */
        @SerializedName("General_5x8in")
        GENERAL_5X8in,

        /**
         * GENERAL_6X8in (6inch x 8inch)
         *
         * @since API 5
         */
        @SerializedName("General_6x8in")
        GENERAL_6X8in,

        /**
         * GENERAL_7X9in (7inch x 9inch)
         *
         * @since API 5
         */
        @SerializedName("General_7x9in")
        GENERAL_7X9in,

        /**
         * GENERAL_9X11in (9inch x 11inch)
         *
         * @since API 5
         */
        @SerializedName("General_9x11in")
        GENERAL_9X11in,

        /**
         * Government Legal (8inch x 13inch)
         *
         * @since API 5
         */
        @SerializedName("Govt_Legal_8x13in")
        GOVT_LEGAL,

        /**
         * Government Letter (8inch x 10inch)
         *
         * @since API 5
         */
        @SerializedName("Govt_Letter_8x10in")
        GOVT_LETTER,

        /**
         * Statement (5.5inch x 8.5inch)
         *
         * @since API 5
         */
        @SerializedName("Invoice_5point5x8point5in")
        STATEMENT,

        /**
         * ISO A0 (841mm x 1189mm)
         *
         * @since API 5
         */
        @SerializedName("ISO_A0_841x1189mm")
        A0,

        /**
         * ISO A1 (594mm x 841mm)
         *
         * @since API 5
         */
        @SerializedName("ISO_A1_594x841mm")
        A1,

        /**
         * ISO A2 (420mm x 594mm)
         *
         * @since API 5
         */
        @SerializedName("ISO_A2_420x594mm")
        A2,

        /**
         * ISO A3 (297mm x 420mm)
         *
         * @since API 5
         */
        @SerializedName("ISO_A3_297x420mm")
        A3,

        /**
         * ISO A4 (210mm x 297mm)
         *
         * @since API 5
         */
        @SerializedName("ISO_A4_210x297mm")
        A4,

        /**
         * ISO A4 Rotated (297mm x 210mm)
         *
         * @since API 5
         */
        @SerializedName("ISO_A4_Rotated_210x297mm")
        A4_ROTATE,

        /**
         * ISO A5 (148mm x 210mm)
         *
         * @since API 5
         */
        @SerializedName("ISO_A5_148x210mm")
        A5,

        /**
         * ISO A5 Rotated (210mm x 148mm)
         *
         * @since API 5
         */
        @SerializedName("ISO_A5_Rotated_148x210mm")
        A5_ROTATE,

        /**
         * ISO A6 (105mm x 148mm)
         *
         * @since API 5
         */
        @SerializedName("ISO_A6_105x148mm")
        A6,

        /**
         * ISO A7 (74mm x 105mm)
         *
         * @since API 5
         */
        @SerializedName("ISO_A7_74x105mm")
        A7,

        /**
         * ISO A8 (52mm x 74mm)
         *
         * @since API 5
         */
        @SerializedName("ISO_A8_52x74mm")
        A8,

        /**
         * ISO A9 (37mm x 52mm)
         *
         * @since API 5
         */
        @SerializedName("ISO_A9_37x52mm")
        A9,

        /**
         * ISO A10 (26mm x 37mm)
         *
         * @since API 5
         */
        @SerializedName("ISO_A10_26x37mm")
        A10,

        /**
         * ISO B0 (1000mm x 1414mm)
         *
         * @since API 5
         */
        @SerializedName("ISO_B0_1000x1414mm")
        B0,

        /**
         * ISO B1 (707mm x 1000mm)
         *
         * @since API 5
         */
        @SerializedName("ISO_B1_707x1000mm")
        B1,

        /**
         * ISO B2 (500mm x 707mm)
         *
         * @since API 5
         */
        @SerializedName("ISO_B2_500x707mm")
        B2,

        /**
         * ISO B3 (353mm x 500mm)
         *
         * @since API 5
         */
        @SerializedName("ISO_B3_353x500mm")
        B3,

        /**
         * ISO B4 (250mm x 353mm)
         *
         * @since API 5
         */
        @SerializedName("ISO_B4_250x353mm")
        B4,

        /**
         * ISO B5 (176mm x 250mm)
         *
         * @since API 5
         */
        @SerializedName("ISO_B5_176x250mm")
        B5,

        /**
         * ISO B6 (125mm x 176mm)
         *
         * @since API 5
         */
        @SerializedName("ISO_B6_125x176mm")
        B6,

        /**
         * ISO B7 (88mm x 125mm)
         *
         * @since API 5
         */
        @SerializedName("ISO_B7_88x125mm")
        B7,

        /**
         * ISO B8 (62mm x 88mm)
         *
         * @since API 5
         */
        @SerializedName("ISO_B8_62x88mm")
        B8,

        /**
         * ISO B9 (44mm x 62mm)
         *
         * @since API 5
         */
        @SerializedName("ISO_B9_44x62mm")
        B9,

        /**
         * ISO B10 (31mm x 44mm)
         *
         * @since API 5
         */
        @SerializedName("ISO_B10_31x44mm")
        B10,

        /**
         * ISO C0 (917mm x 1297mm)
         *
         * @since API 5
         */
        @SerializedName("ISO_C0_917x1297mm")
        C0,

        /**
         * ISO C1 (648mm x 917mm)
         *
         * @since API 5
         */
        @SerializedName("ISO_C1_648x917mm")
        C1,

        /**
         * ISO C2 (458mm x 648mm)
         *
         * @since API 5
         */
        @SerializedName("ISO_C2_458x648mm")
        C2,

        /**
         * ISO C3 (324mm x 458mm)
         *
         * @since API 5
         */
        @SerializedName("ISO_C3_324x458mm")
        C3,

        /**
         * ISO C4 (229mm x 324mm)
         *
         * @since API 5
         */
        @SerializedName("ISO_C4_229x324mm")
        C4,

        /**
         * ISO C5 (162mm x 229mm)
         *
         * @since API 5
         */
        @SerializedName("ISO_C5_162x229mm")
        C5,

        /**
         * ISO C6 (114mm x 162mm)
         *
         * @since API 5
         */
        @SerializedName("ISO_C6_114x162mm")
        C6,

        /**
         * ISO C7 (81mm x 114mm)
         *
         * @since API 5
         */
        @SerializedName("ISO_C7_81x114mm")
        C7,

        /**
         * ISO C8 (57mm x 81mm)
         *
         * @since API 5
         */
        @SerializedName("ISO_C8_57x81mm")
        C8,

        /**
         * ISO C9 (40mm x 57mm)
         *
         * @since API 5
         */
        @SerializedName("ISO_C9_40x57mm")
        C9,

        /**
         * ISO C10 (28mm x 40mm)
         *
         * @since API 5
         */
        @SerializedName("ISO_C10_28x40mm")
        C10,

        /**
         * JBusinessCard (55mm x 91mm)
         *
         * @since API 5
         */
        @SerializedName("JBusinessCard_55x91mm")
        BUSINESS_CARD,

        /**
         * Japanese Double Postcard (148mm x 200mm)
         *
         * @since API 5
         */
        @SerializedName("JDoublePostcard_148x200mm")
        JDOUBLE_POSTCARD,

        /**
         * Japanese Double Postcard Rotated (200mm x 148mm)
         *
         * @since API 5
         */
        @SerializedName("JDoublePostcard_Rotated_148x200mm")
        JDOUBLE_POSTCARD_ROTATE,

        /**
         * JIS B0 (1030mm x 1456mm)
         *
         * @since API 5
         */
        @SerializedName("JIS_B0_1030x1456mm")
        JB0,

        /**
         * JIS B1 (728mm x 1030mm)
         *
         * @since API 5
         */
        @SerializedName("JIS_B1_728x1030mm")
        JB1,

        /**
         * JIS B2 (515mm x 728mm)
         *
         * @since API 5
         */
        @SerializedName("JIS_B2_515x728mm")
        JB2,

        /**
         * JIS B3 (364mm x 515mm)
         *
         * @since API 5
         */
        @SerializedName("JIS_B3_364x515mm")
        JB3,

        /**
         * JIS B4 (257mm x 364mm)
         *
         * @since API 5
         */
        @SerializedName("JIS_B4_257x364mm")
        JB4,

        /**
         * JIS B5 (182mm x 257mm)
         *
         * @since API 5
         */
        @SerializedName("JIS_B5_182x257mm")
        JB5,

        /**
         * JIS B5 Rotated (257mm x 182mm)
         *
         * @since API 5
         */
        @SerializedName("JIS_B5_Rotated_182x257mm")
        JB5_ROTATE,

        /**
         * JIS B6 (128mm x 182mm)
         *
         * @since API 5
         */
        @SerializedName("JIS_B6_128x182mm")
        JB6,

        /**
         * JIS B7 (91mm x 128mm)
         *
         * @since API 5
         */
        @SerializedName("JIS_B7_91x128mm")
        JB7,

        /**
         * JIS B8 (64mm x 91mm)
         *
         * @since API 5
         */
        @SerializedName("JIS_B8_64x91mm")
        JB8,

        /**
         * JIS B9 (45mm x 64mm)
         *
         * @since API 5
         */
        @SerializedName("JIS_B9_45x64mm")
        JB9,

        /**
         * JIS B10 (32mm x 45mm)
         *
         * @since API 5
         */
        @SerializedName("JIS_B10_32x45mm")
        JB10,

        /**
         * JIS Chou3 (120mm x 235mm)
         *
         * @since API 5
         */
        @SerializedName("JIS_Chou3_120x235mm")
        JCHOU3,

        /**
         * JIS Chou4 (90mm x 205mm)
         *
         * @since API 5
         */
        @SerializedName("JIS_Chou4_90x205mm")
        JCHOU4,

        /**
         * JIS Exec (216mm x 330mm)
         *
         * @since API 5
         */
        @SerializedName("JIS_Exec_216x330mm")
        JEXEC,

        /**
         * JIS Kaku2 (240mm x 332mm)
         *
         * @since API 5
         */
        @SerializedName("JIS_Kaku2_240x332mm")
        JKAKU2,

        /**
         * Japanese Postcard (100mm x 148mm)
         *
         * @since API 5
         */
        @SerializedName("JPostcard_100x148mm")
        JPOSTCARD,

        /**
         * K16_184x260mm (184mm x 260mm)
         *
         * @since API 5
         */
        @SerializedName("K16_184x260mm")
        K16_184X260mm,

        /**
         * K16 (195mm x 270mm)
         *
         * @since API 5
         */
        @SerializedName("K16_195x270mm")
        K16,

        /**
         * PRC 16K (197mm x 273mm)
         *
         * @since API 5
         */
        @SerializedName("K16_197x273mm")
        PK16,

        /**
         * K8_260X368mm (260mm x 368mm)
         *
         * @since API 5
         */
        @SerializedName("K8_260x368mm")
        K8_260X368mm,

        /**
         * K8 (270mm x 390mm)
         *
         * @since API 5
         */
        @SerializedName("K8_270x390mm")
        K8,

        /**
         * PRC 8K (273mm x 394mm)
         *
         * @since API 5
         */
        @SerializedName("K8_273x394mm")
        PK8,

        /**
         * Legal (8.5inch x 14inch)
         *
         * @since API 5
         */
        @SerializedName("Legal_8point5x14in")
        LEGAL,

        /**
         * Long Scan (8.5inch x 34inch)
         *
         * @since API 5
         */
        @SerializedName("LongScan_8point5x34in")
        LONG_SCAN,

        /**
         * Mutsugiri (203mm x 254mm)
         *
         * @since API 5
         */
        @SerializedName("Mutsugiri_203x254mm")
        MUTSUGIRI,

        /**
         * Oficio (216mm x 340mm)
         *
         * @since API 5
         */
        @SerializedName("Oficio_216x340mm")
        OFICIO,

        /**
         * RA0 (860mm x 1220mm)
         *
         * @since API 5
         */
        @SerializedName("RA0_860x1220mm")
        RA0,

        /**
         * RA1 (610mm x 860mm)
         *
         * @since API 5
         */
        @SerializedName("RA1_610x860mm")
        RA1,

        /**
         * RA2 (430mm x 610mm)
         *
         * @since API 5
         */
        @SerializedName("RA2_430x610mm")
        RA2,

        /**
         * RA3 (305mm x 430mm)
         *
         * @since API 5
         */
        @SerializedName("RA3_305x430mm")
        RA3,

        /**
         * RA4 (215mm x 305mm)
         *
         * @since API 5
         */
        @SerializedName("RA4_215x305mm")
        RA4,

        /**
         * SRA0 (900mm x 1280mm)
         *
         * @since API 5
         */
        @SerializedName("RA0_900x1280mm")
        SRA0,

        /**
         * SRA1 (640mm x 900mm)
         *
         * @since API 5
         */
        @SerializedName("SRA1_640x900mm")
        SRA1,

        /**
         * SRA2 (450mm x 640mm)
         *
         * @since API 5
         */
        @SerializedName("SRA2_450x640mm")
        SRA2,

        /**
         * SRA3 (320mm x 450mm)
         *
         * @since API 5
         */
        @SerializedName("SRA3_320x450mm")
        SRA3,

        /**
         * SRA4 (225mm x 320mm)
         *
         * @since API 5
         */
        @SerializedName("SRA4_225x320mm")
        SRA4,

        /**
         * Super B (13inch x 19inch)
         *
         * @since API 5
         */
        @SerializedName("Super_B_13x19in")
        SUPER_B,

        /**
         * Any
         *
         * @since API 5
         */
        @SerializedName("Any")
        AUTO,

        /**
         * Match original scan size
         *
         * @since API 5
         */
        @SerializedName("MatchOriginal")
        MATCH_ORIGINAL,

        /**
         * Mixed Letter and Legal
         *
         * @since API 5
         */
        @SerializedName("MixedLetterLegal")
        MIXED_LETTER_LEGAL,

        /**
         * Mixed Letter and Ledger
         *
         * @since API 5
         */
        @SerializedName("MixedLetterLedger")
        MIXED_LETTER_LEDGER,

        /**
         * Mixed A3 and A4
         *
         * @since API 5
         */
        @SerializedName("MixedA4A3")
        MIXED_A3_A4,

        /**
         * Custom
         *
         * @since API 5
         */
        @SerializedName("Custom")
        CUSTOM,

        /**
         * Other
         *
         * @since API 5
         */
        @SerializedName("Other")
        OTHER,

        /**
         * An indeterminable size due to lack of sensors
         *
         * @since API 5
         */
        @SerializedName("Unknown")
        UNKNOWN,

        /**
         * An indeterminable envelope size due to lack of sensors
         *
         * @since API 5
         */
        @SerializedName("UnknownEnvelope")
        UNKNOWN_ENVELOP,

        /**
         * Index card (100mm x 150mm)
         *
         * @since API 5
         */
        @SerializedName("ISO_INDEXCARD_100x150mm")
        INDEXCARD,

        /**
         * Any custom size
         *
         * @since API 5
         */
        @SerializedName("AnyCustom")
        ANY_CUSTOM
    }

    /**
     * Enums of OutputMediaType
     *
     * @since API 5
     */
    @Keep
    public enum OutputMediaType {
        /**
         * Bond
         *
         * @since API 5
         */
        @SerializedName("Bond")
        BOND,

        /**
         * BrochureMatte
         *
         * @since API 5
         */
        @SerializedName("BrochureMatte")
        BROCHURE_MATTE,

        /**
         * Cardstock_176to220g
         *
         * @since API 5
         */
        @SerializedName("Cardstock_176to220g")
        CARD_STOCK,

        /**
         * CardstockGloss_176to220g
         *
         * @since API 5
         */
        @SerializedName("CardstockGloss_176to220g")
        CARD_STOCK_GLOSSY,

        /**
         * Color
         *
         * @since API 5
         */
        @SerializedName("Color")
        COLORED,

        /**
         * CoverMatte
         *
         * @since API 5
         */
        @SerializedName("CoverMatte")
        COVER_MATTE,

        /**
         * Envelope
         *
         * @since API 5
         */
        @SerializedName("Envelope")
        ENVELOPE,

        /**
         * ExtraHeavy_131to175g
         *
         * @since API 5
         */
        @SerializedName("ExtraHeavy_131to175g")
        EXTRA_HEAVY,

        /**
         * ExtraHeavyGloss_131to175g
         *
         * @since API 5
         */
        @SerializedName("ExtraHeavyGloss_131to175g")
        EXTRA_HEAVY_GLOSSY,

        /**
         * Heavy_111to130g
         *
         * @since API 5
         */
        @SerializedName("Heavy_111to130g")
        HEAVY,

        /**
         * HeavyEnvelope
         *
         * @since API 5
         */
        @SerializedName("HeavyEnvelope")
        HEAVY_ENVELOPE,

        /**
         * HeavyGloss_111to130g
         *
         * @since API 5
         */
        @SerializedName("HeavyGloss_111to130g")
        HEAVY_GLOSSY,

        /**
         * HeavyRough
         *
         * @since API 5
         */
        @SerializedName("HeavyRough")
        HEAVY_ROUGH,

        /**
         * HPAdvancedPhoto
         *
         * @since API 5
         */
        @SerializedName("HPAdvancedPhoto")
        HP_ADVANCED_PHOTO,

        /**
         * HPBrochureGloss_180g
         *
         * @since API 5
         */
        @SerializedName("HPBrochureGloss_180g")
        HP_BROCHURE_GLOSSY,

        /**
         * HPBrochureMatte_180g
         *
         * @since API 5
         */
        @SerializedName("HPBrochureMatte_180g")
        HP_BROCHURE_MATTE_180G,

        /**
         * HPCoverMatte_200g
         *
         * @since API 5
         */
        @SerializedName("HPCoverMatte_200g")
        HP_COVER_MATTE_200G,

        /**
         * HPEcoFFICIENT
         *
         * @since API 5
         */
        @SerializedName("HPEcoFFICIENT")
        HP_ECOFFICIENT,

        /**
         * HPEverydayPhotoMatte
         *
         * @since API 5
         */
        @SerializedName("HPEverydayPhotoMatte")
        HP_EVERYDAY_PHOTO_MATTE,

        /**
         * HPGloss_130g
         *
         * @since API 5
         */
        @SerializedName("HPGloss_130g")
        HP_GLOSSY_120G,

        /**
         * HPGloss_160g
         *
         * @since API 5
         */
        @SerializedName("HPGloss_160g")
        HP_GLOSSY_150G,

        /**
         * HPGloss_220g
         *
         * @since API 5
         */
        @SerializedName("HPGloss_220g")
        HP_GLOSSY_200G,

        /**
         * HPMatte_90g
         *
         * @since API 5
         */
        @SerializedName("HPMatte_90g")
        HP_MATTE_90G,

        /**
         * HPMatte_105g
         *
         * @since API 5
         */
        @SerializedName("HPMatte_105g")
        HP_MATTE_105G,

        /**
         * HPMatte_120g
         *
         * @since API 5
         */
        @SerializedName("HPMatte_120g")
        HP_MATTE_120G,

        /**
         * HPMatte_160g
         *
         * @since API 5
         */
        @SerializedName("HPMatte_160g")
        HP_MATTE_150G,

        /**
         * HPMatteBrochureAndFlyer_180g
         *
         * @since API 5
         */
        @SerializedName("HPMatteBrochureAndFlyer_180g")
        HP_MATTE_BROCHURE_AND_FLYER_180G,

        /**
         * HPPhoto
         *
         * @since API 5
         */
        @SerializedName("HPPhoto")
        HP_PHOTO,

        /**
         * HPPhotoPlus
         *
         * @since API 5
         */
        @SerializedName("HPPhotoPlus")
        HP_PHOTO_PLUS,

        /**
         * HPPremiumInkjetTransparency
         *
         * @since API 5
         */
        @SerializedName("HPPremiumInkjetTransparency")
        HP_PREMIUM_INKJET_TRANSPARENCY,

        /**
         * HPPremiumMatte_120g
         *
         * @since API 5
         */
        @SerializedName("HPPremiumMatte_120g")
        HP_PREMIUM_MATTE_120G,

        /**
         * HPSoftGloss_120g
         *
         * @since API 5
         */
        @SerializedName("HPSoftGloss_120g")
        HP_SOFT_GLOSS_120G,

        /**
         * HPTough
         *
         * @since API 5
         */
        @SerializedName("HPTough")
        HP_TOUGH,

        /**
         * HPTrifoldGloss_160g
         *
         * @since API 5
         */
        @SerializedName("HPTrifoldGloss_160g")
        HP_TRIFOLD_GLOSSY_160G,

        /**
         * Intermediate_85to95g
         *
         * @since API 5
         */
        @SerializedName("Intermediate_85to95g")
        INTERMEDIATE,

        /**
         * Labels
         *
         * @since API 5
         */
        @SerializedName("Labels")
        LABELS,

        /**
         * Letterhead
         *
         * @since API 5
         */
        @SerializedName("Letterhead")
        LETTERHEAD,

        /**
         * Light_60to74g
         *
         * @since API 5
         */
        @SerializedName("Light_60to74g")
        LIGHT,

        /**
         * Matte
         *
         * @since API 5
         */
        @SerializedName("Matte")
        MATTE,

        /**
         * Midweight_96to110g
         *
         * @since API 5
         */
        @SerializedName("Midweight_96to110g")
        MID_WEIGHT,

        /**
         * MidweightGloss_96to110g
         *
         * @since API 5
         */
        @SerializedName("MidweightGloss_96to110g")
        MID_WEIGHT_GLOSSY,

        /**
         * OpaqueFilm
         *
         * @since API 5
         */
        @SerializedName("OpaqueFilm")
        OPAQUE_FILM,

        /**
         * Photo
         *
         * @since API 5
         */
        @SerializedName("Photo")
        PHOTO,

        /**
         * Plain
         *
         * @since API 5
         */
        @SerializedName("Plain")
        PLAIN,

        /**
         * Preprinted
         *
         * @since API 5
         */
        @SerializedName("PremiumInkjet")
        HP_INKJET_MATTE_120G,

        /**
         * Preprinted
         *
         * @since API 5
         */
        @SerializedName("Preprinted")
        PREPRINTED,

        /**
         * Prepunched
         *
         * @since API 5
         */
        @SerializedName("Prepunched")
        PREPUNCHED,

        /**
         * Recycled
         *
         * @since API 5
         */
        @SerializedName("Recycled")
        RECYCLED,

        /**
         * Rough
         *
         * @since API 5
         */
        @SerializedName("Rough")
        ROUGH,

        /**
         * ShelfEdgeLabels
         *
         * @since API 5
         */
        @SerializedName("ShelfEdgeLabels")
        SHELF_EDGE_LABELS,

        /**
         * Tab
         *
         * @since API 5
         */
        @SerializedName("Tab")
        TAB,

        /**
         * ThickPlain
         *
         * @since API 5
         */
        @SerializedName("ThickPlain")
        THICK_PLAIN,

        /**
         * Transparency
         *
         * @since API 5
         */
        @SerializedName("Transparency")
        TRANSPARENCY,

        /**
         * Vellum
         *
         * @since API 5
         */
        @SerializedName("Vellum")
        VELLUM,

        /**
         * UserDefined1
         *
         * @since API 5
         */
        @SerializedName("UserDefined1")
        USER_DEFINED_1,

        /**
         * UserDefined2
         *
         * @since API 5
         */
        @SerializedName("UserDefined2")
        USER_DEFINED_2,

        /**
         * UserDefined3
         *
         * @since API 5
         */
        @SerializedName("UserDefined3")
        USER_DEFINED_3,

        /**
         * UserDefined4
         *
         * @since API 5
         */
        @SerializedName("UserDefined4")
        USER_DEFINED_4,

        /**
         * UserDefined5
         *
         * @since API 5
         */
        @SerializedName("UserDefined5")
        USER_DEFINED_5,

        /**
         * UserDefined6
         *
         * @since API 5
         */
        @SerializedName("UserDefined6")
        USER_DEFINED_6,

        /**
         * UserDefined7
         *
         * @since API 5
         */
        @SerializedName("UserDefined7")
        USER_DEFINED_7,

        /**
         * UserDefined8
         *
         * @since API 5
         */
        @SerializedName("UserDefined8")
        USER_DEFINED_8,

        /**
         * UserDefined9
         *
         * @since API 5
         */
        @SerializedName("UserDefined9")
        USER_DEFINED_9,

        /**
         * UserDefined10
         *
         * @since API 5
         */
        @SerializedName("UserDefined10")
        USER_DEFINED_10,

        /**
         * UserDefined11
         *
         * @since API 5
         */
        @SerializedName("UserDefined11")
        USER_DEFINED_11,

        /**
         * UserDefined12
         *
         * @since API 5
         */
        @SerializedName("UserDefined12")
        USER_DEFINED_12,

        /**
         * UserDefined13
         *
         * @since API 5
         */
        @SerializedName("UserDefined13")
        USER_DEFINED_13,

        /**
         * UserDefined14
         *
         * @since API 5
         */
        @SerializedName("UserDefined14")
        USER_DEFINED_14,

        /**
         * UserDefined15
         *
         * @since API 5
         */
        @SerializedName("UserDefined15")
        USER_DEFINED_15,

        /**
         * UserDefined16
         *
         * @since API 5
         */
        @SerializedName("UserDefined16")
        USER_DEFINED_16,

        /**
         * Any
         *
         * @since API 5
         */
        @SerializedName("Any")
        ANY,

        /**
         * Other
         *
         * @since API 5
         */
        @SerializedName("Other")
        OTHER,

        /**
         * HPMatte_200g
         *
         * @since API 5
         */
        @SerializedName("HPMatte_200g")
        HP_MATTE_200G,

        /**
         * LightBond
         *
         * @since API 5
         */
        @SerializedName("LightBond")
        LIGHT_BOND,

        /**
         * LightPaperboard
         *
         * @since API 5
         */
        @SerializedName("LightPaperboard")
        LIGHT_PAPERBOARD,

        /**
         * LightRough
         *
         * @since API 5
         */
        @SerializedName("LightRough")
        LIGHT_ROUGH,

        /**
         * Paperboard
         *
         * @since API 5
         */
        @SerializedName("Paperboard")
        PAPERBOARD,

        /**
         * Auto
         *
         * @since API 5
         */
        @SerializedName("Auto")
        AUTO,

        /**
         * HeavyBond
         *
         * @since API 5
         */
        @SerializedName("HeavyBond")
        HEAVY_BOND,

        /**
         * HeavyPaperboard
         *
         * @since API 5
         */
        @SerializedName("HeavyPaperboard")
        HEAVY_PAPERBOARD,

        /**
         * HPGlossyEdgeline180G
         *
         * @since API 5
         */
        @SerializedName("HPGlossyEdgeline180G")
        HP_GLOSSY_EDGELINE_180G
    }

    /**
     * Enums of OutputBin
     *
     * @since API 5
     */
    @Keep
    public enum OutputBin {
        /**
         * Accessory
         *
         * @since API 5
         */
        @SerializedName("Accessory")
        ACCESSORY,

        /**
         * Adf
         *
         * @since API 5
         */
        @SerializedName("Adf")
        ADF,

        /**
         * Auto
         *
         * @since API 5
         */
        @SerializedName("Auto")
        AUTO,

        /**
         * Booklet
         *
         * @since API 5
         */
        @SerializedName("Booklet")
        BOOKLET,

        /**
         * Default
         *
         * @since API 5
         */
        @SerializedName("Default")
        DEFAULT,

        /**
         * DocumentFeeder
         *
         * @since API 5
         */
        @SerializedName("DocumentFeeder")
        DOCUMENT_FEEDER,

        /**
         * External
         *
         * @since API 5
         */
        @SerializedName("External")
        EXTERNAL,

        /**
         * FaceDown
         *
         * @since API 5
         */
        @SerializedName("FaceDown")
        FACE_DOWN,

        /**
         * FaceDownCorrectOrder
         *
         * @since API 5
         */
        @SerializedName("FaceDownCorrectOrder")
        FACE_DOWN_CORRECT_ORDER,

        /**
         * FaceUp
         *
         * @since API 5
         */
        @SerializedName("FaceUp")
        FACE_UP,

        /**
         * FaceUpStraightestPath
         *
         * @since API 5
         */
        @SerializedName("FaceUpStraightestPath")
        FACE_UP_STRAIGHTEST_PATH,

        /**
         * Fax
         *
         * @since API 5
         */
        @SerializedName("Fax")
        FAX,

        /**
         * Folded
         *
         * @since API 5
         */
        @SerializedName("Folded")
        FOLDED,

        /**
         * Left
         *
         * @since API 5
         */
        @SerializedName("Left")
        LEFT,

        /**
         * LeftStraightestPath
         *
         * @since API 5
         */
        @SerializedName("LeftStraightestPath")
        LEFT_STRAIGHTEST_PATH,

        /**
         * Lower
         *
         * @since API 5
         */
        @SerializedName("Lower")
        LOWER,

        /**
         * LowerBooklet
         *
         * @since API 5
         */
        @SerializedName("LowerBooklet")
        LOWER_BOOKLET,

        /**
         * LowerLeft
         *
         * @since API 5
         */
        @SerializedName("LowerLeft")
        LOWER_LEFT,

        /**
         * LowerLeftHighestCapacity
         *
         * @since API 5
         */
        @SerializedName("LowerLeftHighestCapacity")
        LOWER_LEFT_HIGHEST_CAPACITY,

        /**
         * LowerStacker
         *
         * @since API 5
         */
        @SerializedName("LowerStacker")
        LOWER_STACKER,

        /**
         * MainCopier
         *
         * @since API 5
         */
        @SerializedName("MainCopier")
        MAIN_COPIER,

        /**
         * Middle
         *
         * @since API 5
         */
        @SerializedName("Middle")
        MIDDLE,

        /**
         * MiddleLeft
         *
         * @since API 5
         */
        @SerializedName("MiddleLeft")
        MIDDLE_LEFT,

        /**
         * OutputBin1
         *
         * @since API 5
         */
        @SerializedName("OutputBin1")
        OUTPUT_BIN_1,

        /**
         * OutputBin2
         *
         * @since API 5
         */
        @SerializedName("OutputBin2")
        OUTPUT_BIN_2,

        /**
         * OutputBin3
         *
         * @since API 5
         */
        @SerializedName("OutputBin3")
        OUTPUT_BIN_3,

        /**
         * OutputBin4
         *
         * @since API 5
         */
        @SerializedName("OutputBin4")
        OUTPUT_BIN_4,

        /**
         * OutputBin5
         *
         * @since API 5
         */
        @SerializedName("OutputBin5")
        OUTPUT_BIN_5,

        /**
         * OutputBin6
         *
         * @since API 5
         */
        @SerializedName("OutputBin6")
        OUTPUT_BIN_6,

        /**
         * OutputBin7
         *
         * @since API 5
         */
        @SerializedName("OutputBin7")
        OUTPUT_BIN_7,

        /**
         * OutputBin8
         *
         * @since API 5
         */
        @SerializedName("OutputBin8")
        OUTPUT_BIN_8,

        /**
         * OutputBin9
         *
         * @since API 5
         */
        @SerializedName("OutputBin9")
        OUTPUT_BIN_9,

        /**
         * OutputBin10
         *
         * @since API 5
         */
        @SerializedName("OutputBin10")
        OUTPUT_BIN_10,

        /**
         * OutputBin11
         *
         * @since API 5
         */
        @SerializedName("OutputBin11")
        OUTPUT_BIN_11,

        /**
         * OutputBin12
         *
         * @since API 5
         */
        @SerializedName("OutputBin12")
        OUTPUT_BIN_12,

        /**
         * OutputBin13
         *
         * @since API 5
         */
        @SerializedName("OutputBin13")
        OUTPUT_BIN_13,

        /**
         * OutputBin14
         *
         * @since API 5
         */
        @SerializedName("OutputBin14")
        OUTPUT_BIN_14,

        /**
         * OutputBin15
         *
         * @since API 5
         */
        @SerializedName("OutputBin15")
        OUTPUT_BIN_15,

        /**
         * OutputBin16
         *
         * @since API 5
         */
        @SerializedName("OutputBin16")
        OUTPUT_BIN_16,

        /**
         * Rear
         *
         * @since API 5
         */
        @SerializedName("Rear")
        REAR,

        /**
         * RearFaceUp
         *
         * @since API 5
         */
        @SerializedName("RearFaceUp")
        REAR_FACE_UP,

        /**
         * RearStraightestPath
         *
         * @since API 5
         */
        @SerializedName("RearStraightestPath")
        REAR_STRAIGHTEST_PATH,

        /**
         * Stacker
         *
         * @since API 5
         */
        @SerializedName("Stacker")
        STACKER,

        /**
         * Standard
         *
         * @since API 5
         */
        @SerializedName("Standard")
        STANDARD,

        /**
         * StandardCorrectOrder
         *
         * @since API 5
         */
        @SerializedName("StandardCorrectOrder")
        STANDARD_CORRECT_ORDER,

        /**
         * StandardTop
         *
         * @since API 5
         */
        @SerializedName("StandardTop")
        STANDARD_TOP,

        /**
         * Top
         *
         * @since API 5
         */
        @SerializedName("Top")
        TOP,

        /**
         * Upper
         *
         * @since API 5
         */
        @SerializedName("Upper")
        UPPER,

        /**
         * UpperFaceUp
         *
         * @since API 5
         */
        @SerializedName("UpperFaceUp")
        UPPER_FACE_UP,

        /**
         * UpperLeft
         *
         * @since API 5
         */
        @SerializedName("UpperLeft")
        UPPER_LEFT,

        /**
         * UpperLeftBins
         *
         * @since API 5
         */
        @SerializedName("UpperLeftBins")
        UPPER_LEFT_BINS,

        /**
         * UpperLeftStraightestPath
         *
         * @since API 5
         */
        @SerializedName("UpperLeftStraightestPath")
        UPPER_LEFT_STRAIGHTEST_PATH,

        /**
         * VirtualBins1To3
         *
         * @since API 5
         */
        @SerializedName("VirtualBins1To3")
        VIRTUAL_BINS_1TO3,

        /**
         * VirtualBins1To5
         *
         * @since API 5
         */
        @SerializedName("VirtualBins1To5")
        VIRTUAL_BINS_1TO5,

        /**
         * VirtualBins1To8
         *
         * @since API 5
         */
        @SerializedName("VirtualBins1To8")
        VIRTUAL_BINS_1TO8,

        /**
         * VirtualBins1To10
         *
         * @since API 5
         */
        @SerializedName("VirtualBins1To10")
        VIRTUAL_BINS_1TO10,

        /**
         * VirtualBins2To8
         *
         * @since API 5
         */
        @SerializedName("VirtualBins2To8")
        VIRTUAL_BINS_2TO8,

        /**
         * VirtualFinisherBins
         *
         * @since API 5
         */
        @SerializedName("VirtualFinisherBins")
        VIRTUAL_FINISHER_BINS,

        /**
         * VirtualLeftBins
         *
         * @since API 5
         */
        @SerializedName("VirtualLeftBins")
        VIRTUAL_LEFT_BINS,

        /**
         * Other
         *
         * @since API 5
         */
        @SerializedName("Other")
        OTHER,

        /**
         * Alternate
         *
         * @since API 5
         */
        @SerializedName("Alternate")
        ALTERNATE,

        /**
         * Bottom
         *
         * @since API 5
         */
        @SerializedName("Bottom")
        BOTTOM,

        /**
         * Center
         *
         * @since API 5
         */
        @SerializedName("Center")
        CENTER,

        /**
         * Collator
         *
         * @since API 5
         */
        @SerializedName("Collator")
        COLLATOR,

        /**
         * Duplexer
         *
         * @since API 5
         */
        @SerializedName("Duplexer")
        DUPLEXER,

        /**
         * EngineOptionalBin1
         *
         * @since API 5
         */
        @SerializedName("EngineOptionalBin1")
        ENGINE_OPTIONAL_BIN_1,

        /**
         * LargeCapacity
         *
         * @since API 5
         */
        @SerializedName("LargeCapacity")
        LARGE_CAPACITY,

        /**
         * MyMailbox
         *
         * @since API 5
         */
        @SerializedName("MyMailbox")
        MY_MAILBOX,

        /**
         * Right
         *
         * @since API 5
         */
        @SerializedName("Right")
        RIGHT,

        /**
         * Side
         *
         * @since API 5
         */
        @SerializedName("Side")
        SIDE,

        /**
         * StackerFacedown
         *
         * @since API 5
         */
        @SerializedName("StackerFacedown")
        STACKER_FACEDOWN,

        /**
         * StackerFaceUp
         *
         * @since API 5
         */
        @SerializedName("StackerFaceUp")
        STACKER_FACE_UP,

        /**
         * StackerStaples
         *
         * @since API 5
         */
        @SerializedName("StackerStaples")
        STACKER_STAPLES,

        /**
         * UniversalOutputBin
         *
         * @since API 5
         */
        @SerializedName("UniversalOutputBin")
        UNIVERSAL_OUTPUT_BIN,

        /**
         * Stapler
         *
         * @since API 5
         */
        @SerializedName("Stapler")
        STAPLER
    }

    /**
     * Enums of Plex
     *
     * @since API 5
     */
    @Keep
    public enum Plex {
        /**
         * Simplex
         *
         * @since API 5
         */
        @SerializedName("Simplex")
        SIMPLEX,

        /**
         * Duplex
         *
         * @since API 5
         */
        @SerializedName("Duplex")
        DUPLEX
    }

    /**
     * Enums of ImpressionClassification
     *
     * @since API 5
     */
    @Keep
    public enum ImpressionClassification {
        /**
         * Blank
         *
         * @since API 5
         */
        @SerializedName("Blank")
        BLANK,

        /**
         * Monochrome
         *
         * @since API 5
         */
        @SerializedName("Monochrome")
        MONOCHROME,

        /**
         * Color
         *
         * @since API 5
         */
        @SerializedName("Color")
        COLOR,

        /**
         * None
         *
         * @since API 5
         */
        @SerializedName("None")
        NONE
    }

    /**
     * Enums of Color
     *
     * @since API 5
     */
    @Keep
    public enum Color {
        /**
         * Color Black
         *
         * @since API 5
         */
        @SerializedName("Black")
        BLACK,

        /**
         * Color BlackCyanMagentaYellow
         *
         * @since API 5
         */
        @SerializedName("BlackCyanMagentaYellow")
        BLACK_CYAN_MAGENTA_YELLOW,

        /**
         * Color BlackYellow
         *
         * @since API 5
         */
        @SerializedName("BlackYellow")
        BLACK_YELLOW,

        /**
         * Color Blue
         *
         * @since API 5
         */
        @SerializedName("Blue")
        BLUE,

        /**
         * Color BlueGray
         *
         * @since API 5
         */
        @SerializedName("BlueGray")
        BLUE_GRAY,

        /**
         * Color BrightYellow
         *
         * @since API 5
         */
        @SerializedName("BrightYellow")
        BRIGHT_YELLOW,

        /**
         * Color Cyan
         *
         * @since API 5
         */
        @SerializedName("Cyan")
        CYAN,

        /**
         * Color CyanMagenta
         *
         * @since API 5
         */
        @SerializedName("CyanMagenta")
        CYAN_MAGENTA,

        /**
         * Color CyanMagentaYellow
         *
         * @since API 5
         */
        @SerializedName("CyanMagentaYellow")
        CYAN_MAGENTA_YELLOW,

        /**
         * Color DarkBlue
         *
         * @since API 5
         */
        @SerializedName("DarkBlue")
        DARK_BLUE,

        /**
         * Color DarkGray
         *
         * @since API 5
         */
        @SerializedName("DarkGray")
        DARK_GRAY,

        /**
         * Color DarkGreen
         *
         * @since API 5
         */
        @SerializedName("DarkGreen")
        DARK_GREEN,

        /**
         * Color DarkMagenta
         *
         * @since API 5
         */
        @SerializedName("DarkMagenta")
        DARK_MAGENTA,

        /**
         * Color DarkRed
         *
         * @since API 5
         */
        @SerializedName("DarkRed")
        DARK_RED,

        /**
         * Color FlourescentPink
         *
         * @since API 5
         */
        @SerializedName("FlourescentPink")
        FLOURESCENT_PINK,

        /**
         * Color FlourescentYellow
         *
         * @since API 5
         */
        @SerializedName("FlourescentYellow")
        FLOURESCENT_YELLOW,

        /**
         * Color GlossyBlack
         *
         * @since API 5
         */
        @SerializedName("GlossyBlack")
        GLOSSY_BLACK,

        /**
         * Color Gray
         *
         * @since API 5
         */
        @SerializedName("Gray")
        GRAY,

        /**
         * Color Green
         *
         * @since API 5
         */
        @SerializedName("Green")
        GREEN,

        /**
         * Color GreenGray
         *
         * @since API 5
         */
        @SerializedName("GreenGray")
        GREEN_GRAY,

        /**
         * Color LightCyan
         *
         * @since API 5
         */
        @SerializedName("LightCyan")
        LIGHT_CYAN,

        /**
         * Color LightGray
         *
         * @since API 5
         */
        @SerializedName("LightGray")
        LIGHT_GRAY,

        /**
         * Color LightMagenta
         *
         * @since API 5
         */
        @SerializedName("LightMagenta")
        LIGHT_MAGENTA,

        /**
         * Color LightYellow
         *
         * @since API 5
         */
        @SerializedName("LightYellow")
        LIGHT_YELLOW,

        /**
         * Color Magenta
         *
         * @since API 5
         */
        @SerializedName("Magenta")
        MAGENTA,

        /**
         * Color MatteBlack
         *
         * @since API 5
         */
        @SerializedName("MatteBlack")
        MATTE_BLACK,

        /**
         * Color MediumCyan
         *
         * @since API 5
         */
        @SerializedName("MediumCyan")
        MEDIUM_CYAN,

        /**
         * Color MediumGray
         *
         * @since API 5
         */
        @SerializedName("MediumGray")
        MEDIUM_GRAY,

        /**
         * Color Orange
         *
         * @since API 5
         */
        @SerializedName("Orange")
        ORANGE,

        /**
         * Color Pink
         *
         * @since API 5
         */
        @SerializedName("Pink")
        PINK,

        /**
         * Color PhotoBlack
         *
         * @since API 5
         */
        @SerializedName("PhotoBlack")
        PHOTO_BLACK,

        /**
         * Color PhotoBlackBlackCyanMagentaYellow
         *
         * @since API 5
         */
        @SerializedName("PhotoBlackBlackCyanMagentaYellow")
        PHOTO_BLACK_BLACK_CYAN_MAGENTA_YELLOW,

        /**
         * Color PhotoBlackCyanMagenta
         *
         * @since API 5
         */
        @SerializedName("PhotoBlackCyanMagenta")
        PHOTO_BLACK_CYAN_MAGENTA,

        /**
         * Color Purple
         *
         * @since API 5
         */
        @SerializedName("Purple")
        PURPLE,

        /**
         * Color PurpleGray
         *
         * @since API 5
         */
        @SerializedName("PurpleGray")
        PURPLE_GRAY,

        /**
         * Color Red
         *
         * @since API 5
         */
        @SerializedName("Red")
        RED,

        /**
         * Color RedGray
         *
         * @since API 5
         */
        @SerializedName("RedGray")
        RED_GRAY,

        /**
         * Color ReflexBlue
         *
         * @since API 5
         */
        @SerializedName("ReflexBlue")
        REFLEX_BLUE,

        /**
         * Color RhodamineRed
         *
         * @since API 5
         */
        @SerializedName("RhodamineRed")
        RHODAMINE_RED,

        /**
         * Color Sapphire
         *
         * @since API 5
         */
        @SerializedName("Sapphire")
        SAPPHIRE,

        /**
         * Color Sepia
         *
         * @since API 5
         */
        @SerializedName("Sepia")
        SEPIA,

        /**
         * Color Silver
         *
         * @since API 5
         */
        @SerializedName("Silver")
        SILVER,

        /**
         * Color Teal
         *
         * @since API 5
         */
        @SerializedName("Teal")
        TEAL,

        /**
         * Color Transparent
         *
         * @since API 5
         */
        @SerializedName("Transparent")
        TRANSPARENT,

        /**
         * Color Violet
         *
         * @since API 5
         */
        @SerializedName("Violet")
        VIOLET,

        /**
         * Color White
         *
         * @since API 5
         */
        @SerializedName("White")
        WHITE,

        /**
         * Color Yellow
         *
         * @since API 5
         */
        @SerializedName("Yellow")
        YELLOW,

        /**
         * Color Other
         *
         * @since API 5
         */
        @SerializedName("Other")
        OTHER
    }

    /**
     * A DigitalSend information
     *
     * @since API 5
     */
    public class DigitalSendInfo {
        private DeliveredFile deliveredFiles;
        private EmailInfo.Result result;
        private int totalDataSize;

        public class DeliveredFile {
            private FileInfo[] files;
            private FileInfo metdataFile; //TODO 25.2 fw issue

            /**
             * Returns files
             *
             * @return files
             *
             * @since API 5
             */
            public FileInfo[] getFiles() {
                return files;
            }

            /**
             * Set the files
             * @param files the files to set
             * @since API 5
             */
            public void setFiles(FileInfo[] files) {
                this.files = files;
            }

            /**
             * Set the metadata file
             * @param metdataFile the metdataFile to set
             * @since API 5
             */
            public void setMetdataFile(FileInfo metdataFile) {
                this.metdataFile = metdataFile;
            }
        }

        /**
         * Returns delivered Files
         *
         * @return deliveredFiles
         *
         * @since API 5
         */
        public DeliveredFile getDeliveredFiles() {
            return deliveredFiles;
        }

        /**
         * Set the delivered files
         * @param deliveredFiles the deliveredFiles to set
         * @since API 5
         */
        public void setDeliveredFiles(DeliveredFile deliveredFiles) {
            this.deliveredFiles = deliveredFiles;
        }

        /**
         * Returns result
         *
         * @return result
         *
         * @since API 5
         */
        public EmailInfo.Result getResult() {
            return result;
        }

        /**
         * Set the result
         * @param result the result to set
         * @since API 5
         */
        public void setResult(EmailInfo.Result result) {
            this.result = result;
        }

        /**
         * Returns total of data size
         *
         * @return totalDataSize
         *
         * @since API 5
         */
        public int getTotalDataSize() {
            return totalDataSize;
        }

        /**
         * Set the total data size
         * @param totalDataSize the totalDataSize to set
         * @since API 5
         */
        public void setTotalDataSize(int totalDataSize) {
            this.totalDataSize = totalDataSize;
        }
    }

    /**
     * @hide trivial
     */
    @Override
    public String toString() {
        return new StringBuilder().append("[").append("]").toString();
    }
}
