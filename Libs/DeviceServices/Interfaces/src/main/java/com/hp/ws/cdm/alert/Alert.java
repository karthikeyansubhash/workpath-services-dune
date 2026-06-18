
package com.hp.ws.cdm.alert;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;
import com.hp.ws.cdm.commonglossary.Progress;

public class Alert {

    /**
     * unique identifier of the alert
     * (Required)
     * 
     */
    @SerializedName("id")
    @Expose
    private Long id;
    /**
     * example alert categories - need to be defined by microservices.
     * 
     */
    @SerializedName("category")
    @Expose
    private Alert.Category category;
    /**
     * In case of multiplicity of parts, identifies to which of the instances this alert applies. For example : Ink cartridge 1 (black), Media tray 3
     * 
     */
    @SerializedName("instanceId")
    @Expose
    private String instanceId;
    /**
     * Internal code (e.g. engine error code) for the alert
     * 
     */
    @SerializedName("alertCode")
    @Expose
    private Integer alertCode;
    /**
     * eventcode of the alert format: xx::yy::zz
     * 
     */
    @SerializedName("code")
    @Expose
    private String code;
    /**
     * The sequence number is an incrementing identifier used to indicate order of events. This number is locally scoped to each resource.
     * 
     */
    @SerializedName("sequenceNum")
    @Expose
    private Long sequenceNum;
    /**
     * time stamp
     * 
     */
    @SerializedName("dateTime")
    @Expose
    private Date dateTime;
    /**
     * The severity of the alert
     * 
     */
    @SerializedName("severity")
    @Expose
    private Alert.Severity severity;
    /**
     * priority of the alert
     * 
     */
    @SerializedName("priority")
    @Expose
    private Integer priority;
    /**
     * Unique identifier for the message associated to this alert
     * 
     */
    @SerializedName("stringId")
    @Expose
    private Long stringId;
    /**
     * Indicates the scope of  the alert
     * 
     */
    @SerializedName("scope")
    @Expose
    private Alert.Scope scope = Alert.Scope.fromValue("public");
    /**
     * Indicates the visibility of the alert
     * 
     */
    @SerializedName("visibility")
    @Expose
    private Alert.Visibility visibility = Alert.Visibility.fromValue("all");
    /**
     * additional data of the alert
     * 
     */
    @SerializedName("data")
    @Expose
    private List<AlertData> data = new ArrayList<AlertData>();
    /**
     * option for action
     * 
     */
    @SerializedName("actions")
    @Expose
    private AlertAction actions;
    /**
     * Progress for a flow / step
     * 
     */
    @SerializedName("progress")
    @Expose
    private Progress progress;
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();

    /**
     * unique identifier of the alert
     * (Required)
     * 
     */
    public Long getId() {
        return id;
    }

    /**
     * unique identifier of the alert
     * (Required)
     * 
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * example alert categories - need to be defined by microservices.
     * 
     */
    public Alert.Category getCategory() {
        return category;
    }

    /**
     * example alert categories - need to be defined by microservices.
     * 
     */
    public void setCategory(Alert.Category category) {
        this.category = category;
    }

    /**
     * In case of multiplicity of parts, identifies to which of the instances this alert applies. For example : Ink cartridge 1 (black), Media tray 3
     * 
     */
    public String getInstanceId() {
        return instanceId;
    }

    /**
     * In case of multiplicity of parts, identifies to which of the instances this alert applies. For example : Ink cartridge 1 (black), Media tray 3
     * 
     */
    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    /**
     * Internal code (e.g. engine error code) for the alert
     * 
     */
    public Integer getAlertCode() {
        return alertCode;
    }

    /**
     * Internal code (e.g. engine error code) for the alert
     * 
     */
    public void setAlertCode(Integer alertCode) {
        this.alertCode = alertCode;
    }

    /**
     * eventcode of the alert format: xx::yy::zz
     * 
     */
    public String getCode() {
        return code;
    }

    /**
     * eventcode of the alert format: xx::yy::zz
     * 
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * The sequence number is an incrementing identifier used to indicate order of events. This number is locally scoped to each resource.
     * 
     */
    public Long getSequenceNum() {
        return sequenceNum;
    }

    /**
     * The sequence number is an incrementing identifier used to indicate order of events. This number is locally scoped to each resource.
     * 
     */
    public void setSequenceNum(Long sequenceNum) {
        this.sequenceNum = sequenceNum;
    }

    /**
     * time stamp
     * 
     */
    public Date getDateTime() {
        return dateTime;
    }

    /**
     * time stamp
     * 
     */
    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * The severity of the alert
     * 
     */
    public Alert.Severity getSeverity() {
        return severity;
    }

    /**
     * The severity of the alert
     * 
     */
    public void setSeverity(Alert.Severity severity) {
        this.severity = severity;
    }

    /**
     * priority of the alert
     * 
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * priority of the alert
     * 
     */
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    /**
     * Unique identifier for the message associated to this alert
     * 
     */
    public Long getStringId() {
        return stringId;
    }

    /**
     * Unique identifier for the message associated to this alert
     * 
     */
    public void setStringId(Long stringId) {
        this.stringId = stringId;
    }

    /**
     * Indicates the scope of  the alert
     * 
     */
    public Alert.Scope getScope() {
        return scope;
    }

    /**
     * Indicates the scope of  the alert
     * 
     */
    public void setScope(Alert.Scope scope) {
        this.scope = scope;
    }

    /**
     * Indicates the visibility of the alert
     * 
     */
    public Alert.Visibility getVisibility() {
        return visibility;
    }

    /**
     * Indicates the visibility of the alert
     * 
     */
    public void setVisibility(Alert.Visibility visibility) {
        this.visibility = visibility;
    }

    /**
     * additional data of the alert
     * 
     */
    public List<AlertData> getData() {
        return data;
    }

    /**
     * additional data of the alert
     * 
     */
    public void setData(List<AlertData> data) {
        this.data = data;
    }

    /**
     * option for action
     * 
     */
    public AlertAction getActions() {
        return actions;
    }

    /**
     * option for action
     * 
     */
    public void setActions(AlertAction actions) {
        this.actions = actions;
    }

    /**
     * Progress for a flow / step
     * 
     */
    public Progress getProgress() {
        return progress;
    }

    /**
     * Progress for a flow / step
     * 
     */
    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }


    /**
     * example alert categories - need to be defined by microservices.
     * 
     */
    public enum Category {

        @SerializedName("flatbedAddPage")
        FLATBED_ADD_PAGE("flatbedAddPage"),
        @SerializedName("flatbedStartScan")
        FLATBED_START_SCAN("flatbedStartScan"),
        @SerializedName("mdfAddPage")
        MDF_ADD_PAGE("mdfAddPage"),
        @SerializedName("mdfEjectPage")
        MDF_EJECT_PAGE("mdfEjectPage"),
        @SerializedName("calibrationInProgress")
        CALIBRATION_IN_PROGRESS("calibrationInProgress"),
        @SerializedName("cartridgeMissing")
        CARTRIDGE_MISSING("cartridgeMissing"),
        @SerializedName("cartridgeLow")
        CARTRIDGE_LOW("cartridgeLow"),
        @SerializedName("cartridgeVeryLow")
        CARTRIDGE_VERY_LOW("cartridgeVeryLow"),
        @SerializedName("tankLow")
        TANK_LOW("tankLow"),
        @SerializedName("tankVeryLow")
        TANK_VERY_LOW("tankVeryLow"),
        @SerializedName("outOfInk")
        OUT_OF_INK("outOfInk"),
        @SerializedName("tankFilled")
        TANK_FILLED("tankFilled"),
        @SerializedName("tankFull")
        TANK_FULL("tankFull"),
        @SerializedName("genuineHPsupply")
        GENUINE_H_PSUPPLY("genuineHPsupply"),
        @SerializedName("idsSafeMode")
        IDS_SAFE_MODE("idsSafeMode"),
        @SerializedName("idsDelayedBackToHp")
        IDS_DELAYED_BACK_TO_HP("idsDelayedBackToHp"),
        @SerializedName("idsDegasserMalfunction")
        IDS_DEGASSER_MALFUNCTION("idsDegasserMalfunction"),
        @SerializedName("idsBusy")
        IDS_BUSY("idsBusy"),
        @SerializedName("idsStartupUsedSupply")
        IDS_STARTUP_USED_SUPPLY("idsStartupUsedSupply"),
        @SerializedName("idsStartupRoutineFailedAltered")
        IDS_STARTUP_ROUTINE_FAILED_ALTERED("idsStartupRoutineFailedAltered"),
        @SerializedName("idsStartupRoutineFailed")
        IDS_STARTUP_ROUTINE_FAILED("idsStartupRoutineFailed"),
        @SerializedName("idsStartupBlockedLoi")
        IDS_STARTUP_BLOCKED_LOI("idsStartupBlockedLoi"),
        @SerializedName("idsStartupRoutineInProgress")
        IDS_STARTUP_ROUTINE_IN_PROGRESS("idsStartupRoutineInProgress"),
        @SerializedName("idsStartupAlteredFlow")
        IDS_STARTUP_ALTERED_FLOW("idsStartupAlteredFlow"),
        @SerializedName("idsStartupRoutineFailedAlteredFlow")
        IDS_STARTUP_ROUTINE_FAILED_ALTERED_FLOW("idsStartupRoutineFailedAlteredFlow"),
        @SerializedName("idsStartupBlockedLoiFlow")
        IDS_STARTUP_BLOCKED_LOI_FLOW("idsStartupBlockedLoiFlow"),
        @SerializedName("cartridgeWarning")
        CARTRIDGE_WARNING("cartridgeWarning"),
        @SerializedName("cartridgeRecirculationIncomplete")
        CARTRIDGE_RECIRCULATION_INCOMPLETE("cartridgeRecirculationIncomplete"),
        @SerializedName("cartridgeExpired")
        CARTRIDGE_EXPIRED("cartridgeExpired"),
        @SerializedName("cartridgeReseat")
        CARTRIDGE_RESEAT("cartridgeReseat"),
        @SerializedName("cartridgeWrongModel")
        CARTRIDGE_WRONG_MODEL("cartridgeWrongModel"),
        @SerializedName("cartridgeWrongRegion")
        CARTRIDGE_WRONG_REGION("cartridgeWrongRegion"),
        @SerializedName("cartridgeError")
        CARTRIDGE_ERROR("cartridgeError"),
        @SerializedName("cartridgeNonHp")
        CARTRIDGE_NON_HP("cartridgeNonHp"),
        @SerializedName("cartridgeAltered")
        CARTRIDGE_ALTERED("cartridgeAltered"),
        @SerializedName("cartridgeSafeStop")
        CARTRIDGE_SAFE_STOP("cartridgeSafeStop"),
        @SerializedName("cartridgeAlmostEndOfLife")
        CARTRIDGE_ALMOST_END_OF_LIFE("cartridgeAlmostEndOfLife"),
        @SerializedName("cartridgeEndOfLife")
        CARTRIDGE_END_OF_LIFE("cartridgeEndOfLife"),
        @SerializedName("cartridgeForwardIncompatible")
        CARTRIDGE_FORWARD_INCOMPATIBLE("cartridgeForwardIncompatible"),
        @SerializedName("cartridgeExpectedSetup")
        CARTRIDGE_EXPECTED_SETUP("cartridgeExpectedSetup"),
        @SerializedName("cartridgeExpectedTrade")
        CARTRIDGE_EXPECTED_TRADE("cartridgeExpectedTrade"),
        @SerializedName("cartridgeLowFlow")
        CARTRIDGE_LOW_FLOW("cartridgeLowFlow"),
        @SerializedName("cartridgeLowT2Flow")
        CARTRIDGE_LOW_T_2_FLOW("cartridgeLowT2Flow"),
        @SerializedName("cartridgeVeryLowFlow")
        CARTRIDGE_VERY_LOW_FLOW("cartridgeVeryLowFlow"),
        @SerializedName("cartridgeEmptyAtInsertionFlow")
        CARTRIDGE_EMPTY_AT_INSERTION_FLOW("cartridgeEmptyAtInsertionFlow"),
        @SerializedName("cartridgeAntiCounterfeitFlow")
        CARTRIDGE_ANTI_COUNTERFEIT_FLOW("cartridgeAntiCounterfeitFlow"),
        @SerializedName("cartridgeCounterfeitFlow")
        CARTRIDGE_COUNTERFEIT_FLOW("cartridgeCounterfeitFlow"),
        @SerializedName("cartridgeRefilledFlow")
        CARTRIDGE_REFILLED_FLOW("cartridgeRefilledFlow"),
        @SerializedName("cartridgeCounterfeitInstalledFlow")
        CARTRIDGE_COUNTERFEIT_INSTALLED_FLOW("cartridgeCounterfeitInstalledFlow"),
        @SerializedName("cartridgeUpgradeSetupFailed")
        CARTRIDGE_UPGRADE_SETUP_FAILED("cartridgeUpgradeSetupFailed"),
        @SerializedName("cartridgeUpgradableFlow")
        CARTRIDGE_UPGRADABLE_FLOW("cartridgeUpgradableFlow"),
        @SerializedName("cartridgeUpgradeSuccessFlow")
        CARTRIDGE_UPGRADE_SUCCESS_FLOW("cartridgeUpgradeSuccessFlow"),
        @SerializedName("cartridgeUpgradeFailedFlow")
        CARTRIDGE_UPGRADE_FAILED_FLOW("cartridgeUpgradeFailedFlow"),
        @SerializedName("cartridgeSelectabilityNumberChangeFlow")
        CARTRIDGE_SELECTABILITY_NUMBER_CHANGE_FLOW("cartridgeSelectabilityNumberChangeFlow"),
        @SerializedName("cartridgeLimitedInAvailabilityFlow")
        CARTRIDGE_LIMITED_IN_AVAILABILITY_FLOW("cartridgeLimitedInAvailabilityFlow"),
        @SerializedName("cartridgeSupportMessageFlow")
        CARTRIDGE_SUPPORT_MESSAGE_FLOW("cartridgeSupportMessageFlow"),
        @SerializedName("cartridgeUsingBadMfgBatchFlow")
        CARTRIDGE_USING_BAD_MFG_BATCH_FLOW("cartridgeUsingBadMfgBatchFlow"),
        @SerializedName("cartridgeBadMfgBatchError")
        CARTRIDGE_BAD_MFG_BATCH_ERROR("cartridgeBadMfgBatchError"),
        @SerializedName("cartridgePairingProblem")
        CARTRIDGE_PAIRING_PROBLEM("cartridgePairingProblem"),
        @SerializedName("cartridgeReserveModePreJobBlackOnlyFlow")
        CARTRIDGE_RESERVE_MODE_PRE_JOB_BLACK_ONLY_FLOW("cartridgeReserveModePreJobBlackOnlyFlow"),
        @SerializedName("cartridgeReserveModePreJobColorOnlyFlow")
        CARTRIDGE_RESERVE_MODE_PRE_JOB_COLOR_ONLY_FLOW("cartridgeReserveModePreJobColorOnlyFlow"),
        @SerializedName("cartridgeReserveModeMidJobBlackOnlyFlow")
        CARTRIDGE_RESERVE_MODE_MID_JOB_BLACK_ONLY_FLOW("cartridgeReserveModeMidJobBlackOnlyFlow"),
        @SerializedName("cartridgeReserveModeMidJobColorOnlyFlow")
        CARTRIDGE_RESERVE_MODE_MID_JOB_COLOR_ONLY_FLOW("cartridgeReserveModeMidJobColorOnlyFlow"),
        @SerializedName("cartridgesReadyForReplacement")
        CARTRIDGES_READY_FOR_REPLACEMENT("cartridgesReadyForReplacement"),
        @SerializedName("cartridgesNotReadyForReplacement")
        CARTRIDGES_NOT_READY_FOR_REPLACEMENT("cartridgesNotReadyForReplacement"),
        @SerializedName("printheadsAlignmentCalibrationIncomplete")
        PRINTHEADS_ALIGNMENT_CALIBRATION_INCOMPLETE("printheadsAlignmentCalibrationIncomplete"),
        @SerializedName("printheadReseat")
        PRINTHEAD_RESEAT("printheadReseat"),
        @SerializedName("printheadReplace")
        PRINTHEAD_REPLACE("printheadReplace"),
        @SerializedName("printheadWrong")
        PRINTHEAD_WRONG("printheadWrong"),
        @SerializedName("printheadReplacementIncomplete")
        PRINTHEAD_REPLACEMENT_INCOMPLETE("printheadReplacementIncomplete"),
        @SerializedName("printheadTestSeparately")
        PRINTHEAD_TEST_SEPARATELY("printheadTestSeparately"),
        @SerializedName("printheadNotPresent")
        PRINTHEAD_NOT_PRESENT("printheadNotPresent"),
        @SerializedName("printheadMonitorPq")
        PRINTHEAD_MONITOR_PQ("printheadMonitorPq"),
        @SerializedName("printheadRecover")
        PRINTHEAD_RECOVER("printheadRecover"),
        @SerializedName("printheadFailure")
        PRINTHEAD_FAILURE("printheadFailure"),
        @SerializedName("refillWaterTankFlow")
        REFILL_WATER_TANK_FLOW("refillWaterTankFlow"),
        @SerializedName("jamInPrinter")
        JAM_IN_PRINTER("jamInPrinter"),
        @SerializedName("jamInScanner")
        JAM_IN_SCANNER("jamInScanner"),
        @SerializedName("clipInScanner")
        CLIP_IN_SCANNER("clipInScanner"),
        @SerializedName("mispickInScanner")
        MISPICK_IN_SCANNER("mispickInScanner"),
        @SerializedName("scannerOutOfMemory")
        SCANNER_OUT_OF_MEMORY("scannerOutOfMemory"),
        @SerializedName("documentLoaded")
        DOCUMENT_LOADED("documentLoaded"),
        @SerializedName("scanManualDuplexSecondSide")
        SCAN_MANUAL_DUPLEX_SECOND_SIDE("scanManualDuplexSecondSide"),
        @SerializedName("scanManualDuplexSecondPage")
        SCAN_MANUAL_DUPLEX_SECOND_PAGE("scanManualDuplexSecondPage"),
        @SerializedName("sendState")
        SEND_STATE("sendState"),
        @SerializedName("insufficientMemory")
        INSUFFICIENT_MEMORY("insufficientMemory"),
        @SerializedName("printerError")
        PRINTER_ERROR("printerError"),
        @SerializedName("printerFailure")
        PRINTER_FAILURE("printerFailure"),
        @SerializedName("printerImproperShutdown")
        PRINTER_IMPROPER_SHUTDOWN("printerImproperShutdown"),
        @SerializedName("outOfMedia")
        OUT_OF_MEDIA("outOfMedia"),
        @SerializedName("allTraysEmpty")
        ALL_TRAYS_EMPTY("allTraysEmpty"),
        @SerializedName("outOfMediaMismatch")
        OUT_OF_MEDIA_MISMATCH("outOfMediaMismatch"),
        @SerializedName("doorOpen")
        DOOR_OPEN("doorOpen"),
        @SerializedName("doorOpenOobe")
        DOOR_OPEN_OOBE("doorOpenOobe"),
        @SerializedName("trayAttached")
        TRAY_ATTACHED("trayAttached"),
        @SerializedName("trayDetached")
        TRAY_DETACHED("trayDetached"),
        @SerializedName("trayOpen")
        TRAY_OPEN("trayOpen"),
        @SerializedName("trayMediaEmpty")
        TRAY_MEDIA_EMPTY("trayMediaEmpty"),
        @SerializedName("trayEmpty")
        TRAY_EMPTY("trayEmpty"),
        @SerializedName("singleTrayLop")
        SINGLE_TRAY_LOP("singleTrayLop"),
        @SerializedName("singleXTrayLop")
        SINGLE_X_TRAY_LOP("singleXTrayLop"),
        @SerializedName("allTrayLop")
        ALL_TRAY_LOP("allTrayLop"),
        @SerializedName("trayLifting")
        TRAY_LIFTING("trayLifting"),
        @SerializedName("trayLiftingError")
        TRAY_LIFTING_ERROR("trayLiftingError"),
        @SerializedName("trayOverfilled")
        TRAY_OVERFILLED("trayOverfilled"),
        @SerializedName("mediaSingleIntrayMode")
        MEDIA_SINGLE_INTRAY_MODE("mediaSingleIntrayMode"),
        @SerializedName("mediaAttendedMode")
        MEDIA_ATTENDED_MODE("mediaAttendedMode"),
        @SerializedName("mediaInterventionNeeded")
        MEDIA_INTERVENTION_NEEDED("mediaInterventionNeeded"),
        @SerializedName("mediaInputNoPrintZoneDetected")
        MEDIA_INPUT_NO_PRINT_ZONE_DETECTED("mediaInputNoPrintZoneDetected"),
        @SerializedName("mediaInputIncorrectlyLoaded")
        MEDIA_INPUT_INCORRECTLY_LOADED("mediaInputIncorrectlyLoaded"),
        @SerializedName("mediaInputIntrayOutOfMedia")
        MEDIA_INPUT_INTRAY_OUT_OF_MEDIA("mediaInputIntrayOutOfMedia"),
        @SerializedName("mediaInputIntrayAlmostOutOfMedia")
        MEDIA_INPUT_INTRAY_ALMOST_OUT_OF_MEDIA("mediaInputIntrayAlmostOutOfMedia"),
        @SerializedName("mediaInputIntrayNotCalibrated")
        MEDIA_INPUT_INTRAY_NOT_CALIBRATED("mediaInputIntrayNotCalibrated"),
        @SerializedName("mediaInputIntrayNotColorProfiled")
        MEDIA_INPUT_INTRAY_NOT_COLOR_PROFILED("mediaInputIntrayNotColorProfiled"),
        @SerializedName("mediaInputIntrayNotColorCalibrated")
        MEDIA_INPUT_INTRAY_NOT_COLOR_CALIBRATED("mediaInputIntrayNotColorCalibrated"),
        @SerializedName("mediaInputIntrayNotAdvanceCalib")
        MEDIA_INPUT_INTRAY_NOT_ADVANCE_CALIB("mediaInputIntrayNotAdvanceCalib"),
        @SerializedName("mediaInputIntrayLengthExhausted")
        MEDIA_INPUT_INTRAY_LENGTH_EXHAUSTED("mediaInputIntrayLengthExhausted"),
        @SerializedName("mediaInputIntrayIncorrectlyLoaded")
        MEDIA_INPUT_INTRAY_INCORRECTLY_LOADED("mediaInputIntrayIncorrectlyLoaded"),
        @SerializedName("mediaInputIntrayInvalidWidth")
        MEDIA_INPUT_INTRAY_INVALID_WIDTH("mediaInputIntrayInvalidWidth"),
        @SerializedName("mediaInputIntrayBadLengthPlots")
        MEDIA_INPUT_INTRAY_BAD_LENGTH_PLOTS("mediaInputIntrayBadLengthPlots"),
        @SerializedName("mediaInputIntrayBadMediaMovements")
        MEDIA_INPUT_INTRAY_BAD_MEDIA_MOVEMENTS("mediaInputIntrayBadMediaMovements"),
        @SerializedName("mediaInputIntrayHardwareError")
        MEDIA_INPUT_INTRAY_HARDWARE_ERROR("mediaInputIntrayHardwareError"),
        @SerializedName("mediaInputIntraySensitiveMedia")
        MEDIA_INPUT_INTRAY_SENSITIVE_MEDIA("mediaInputIntraySensitiveMedia"),
        @SerializedName("mediaInputIntrayCableDisconnected")
        MEDIA_INPUT_INTRAY_CABLE_DISCONNECTED("mediaInputIntrayCableDisconnected"),
        @SerializedName("mediaInputIntrayCommError")
        MEDIA_INPUT_INTRAY_COMM_ERROR("mediaInputIntrayCommError"),
        @SerializedName("mediaInputIntrayJamDrawer")
        MEDIA_INPUT_INTRAY_JAM_DRAWER("mediaInputIntrayJamDrawer"),
        @SerializedName("mediaInputDrawerOpen")
        MEDIA_INPUT_DRAWER_OPEN("mediaInputDrawerOpen"),
        @SerializedName("mediaInputDrawerJamDoorOpen")
        MEDIA_INPUT_DRAWER_JAM_DOOR_OPEN("mediaInputDrawerJamDoorOpen"),
        @SerializedName("mediaOutputDeviceDisabled")
        MEDIA_OUTPUT_DEVICE_DISABLED("mediaOutputDeviceDisabled"),
        @SerializedName("mediaOutputDeviceCableDisconected")
        MEDIA_OUTPUT_DEVICE_CABLE_DISCONECTED("mediaOutputDeviceCableDisconected"),
        @SerializedName("mediaOutputDeviceCommError")
        MEDIA_OUTPUT_DEVICE_COMM_ERROR("mediaOutputDeviceCommError"),
        @SerializedName("mediaOutputDeviceError")
        MEDIA_OUTPUT_DEVICE_ERROR("mediaOutputDeviceError"),
        @SerializedName("mediaOutputDeviceDetached")
        MEDIA_OUTPUT_DEVICE_DETACHED("mediaOutputDeviceDetached"),
        @SerializedName("mediaOutputDeviceOpen")
        MEDIA_OUTPUT_DEVICE_OPEN("mediaOutputDeviceOpen"),
        @SerializedName("mediaOutputDeviceFull")
        MEDIA_OUTPUT_DEVICE_FULL("mediaOutputDeviceFull"),
        @SerializedName("mediaOutputDeviceCollectPrintedPages")
        MEDIA_OUTPUT_DEVICE_COLLECT_PRINTED_PAGES("mediaOutputDeviceCollectPrintedPages"),
        @SerializedName("mediaOutputDeviceJobWaitingPrintedPagesCollected")
        MEDIA_OUTPUT_DEVICE_JOB_WAITING_PRINTED_PAGES_COLLECTED("mediaOutputDeviceJobWaitingPrintedPagesCollected"),
        @SerializedName("mediaOutputDeviceOutOfTabs")
        MEDIA_OUTPUT_DEVICE_OUT_OF_TABS("mediaOutputDeviceOutOfTabs"),
        @SerializedName("mediaOutputDeviceAlmostOutOfTabs")
        MEDIA_OUTPUT_DEVICE_ALMOST_OUT_OF_TABS("mediaOutputDeviceAlmostOutOfTabs"),
        @SerializedName("mediaOutputDeviceAlmostFull")
        MEDIA_OUTPUT_DEVICE_ALMOST_FULL("mediaOutputDeviceAlmostFull"),
        @SerializedName("mediaOutputDeviceUpdatingFirmware")
        MEDIA_OUTPUT_DEVICE_UPDATING_FIRMWARE("mediaOutputDeviceUpdatingFirmware"),
        @SerializedName("mediaOutputDeviceInitializing")
        MEDIA_OUTPUT_DEVICE_INITIALIZING("mediaOutputDeviceInitializing"),
        @SerializedName("mediaOutputDeviceWarmingUp")
        MEDIA_OUTPUT_DEVICE_WARMING_UP("mediaOutputDeviceWarmingUp"),
        @SerializedName("mediaOutputDeviceOfflineFold")
        MEDIA_OUTPUT_DEVICE_OFFLINE_FOLD("mediaOutputDeviceOfflineFold"),
        @SerializedName("mediaOutbinAlmostFull")
        MEDIA_OUTBIN_ALMOST_FULL("mediaOutbinAlmostFull"),
        @SerializedName("mediaOutbinFull")
        MEDIA_OUTBIN_FULL("mediaOutbinFull"),
        @SerializedName("mediaOutbinOpen")
        MEDIA_OUTBIN_OPEN("mediaOutbinOpen"),
        @SerializedName("mediaOutbinError")
        MEDIA_OUTBIN_ERROR("mediaOutbinError"),
        @SerializedName("mediaLiftMechanismsLightBarrierBlocked")
        MEDIA_LIFT_MECHANISMS_LIGHT_BARRIER_BLOCKED("mediaLiftMechanismsLightBarrierBlocked"),
        @SerializedName("maintenanceWasteContainerReplacementLevelAlmostReached")
        MAINTENANCE_WASTE_CONTAINER_REPLACEMENT_LEVEL_ALMOST_REACHED("maintenanceWasteContainerReplacementLevelAlmostReached"),
        @SerializedName("maintenanceWasteContainerReplacementLevelReached")
        MAINTENANCE_WASTE_CONTAINER_REPLACEMENT_LEVEL_REACHED("maintenanceWasteContainerReplacementLevelReached"),
        @SerializedName("maintenanceWasteContainerNotPresent")
        MAINTENANCE_WASTE_CONTAINER_NOT_PRESENT("maintenanceWasteContainerNotPresent"),
        @SerializedName("maintenanceAirflowsKitReplacementLevelAlmostReached")
        MAINTENANCE_AIRFLOWS_KIT_REPLACEMENT_LEVEL_ALMOST_REACHED("maintenanceAirflowsKitReplacementLevelAlmostReached"),
        @SerializedName("maintenanceAirflowsKitReplacementLevelReached")
        MAINTENANCE_AIRFLOWS_KIT_REPLACEMENT_LEVEL_REACHED("maintenanceAirflowsKitReplacementLevelReached"),
        @SerializedName("maintenancePrintHeadCleaningKitReplacementLevelAlmostReached")
        MAINTENANCE_PRINT_HEAD_CLEANING_KIT_REPLACEMENT_LEVEL_ALMOST_REACHED("maintenancePrintHeadCleaningKitReplacementLevelAlmostReached"),
        @SerializedName("maintenancePrintHeadCleaningKitReplacementLevelReached")
        MAINTENANCE_PRINT_HEAD_CLEANING_KIT_REPLACEMENT_LEVEL_REACHED("maintenancePrintHeadCleaningKitReplacementLevelReached"),
        @SerializedName("maintenancePrintHeadCleaningKitNotPresent")
        MAINTENANCE_PRINT_HEAD_CLEANING_KIT_NOT_PRESENT("maintenancePrintHeadCleaningKitNotPresent"),
        @SerializedName("maintenancePreventiveMaintenanceKitThresholdAlmostReached")
        MAINTENANCE_PREVENTIVE_MAINTENANCE_KIT_THRESHOLD_ALMOST_REACHED("maintenancePreventiveMaintenanceKitThresholdAlmostReached"),
        @SerializedName("maintenancePreventiveMaintenanceKitThresholdReached")
        MAINTENANCE_PREVENTIVE_MAINTENANCE_KIT_THRESHOLD_REACHED("maintenancePreventiveMaintenanceKitThresholdReached"),
        @SerializedName("maintenanceLiquidTankVeryLow")
        MAINTENANCE_LIQUID_TANK_VERY_LOW("maintenanceLiquidTankVeryLow"),
        @SerializedName("maintenanceLiquidTankEmpty")
        MAINTENANCE_LIQUID_TANK_EMPTY("maintenanceLiquidTankEmpty"),
        @SerializedName("maintenanceLiquidTankNotPresent")
        MAINTENANCE_LIQUID_TANK_NOT_PRESENT("maintenanceLiquidTankNotPresent"),
        @SerializedName("maintenanceLiquidTanktError")
        MAINTENANCE_LIQUID_TANKT_ERROR("maintenanceLiquidTanktError"),
        @SerializedName("maintenanceCondensateCollectorReplacementLevelAlmostReached")
        MAINTENANCE_CONDENSATE_COLLECTOR_REPLACEMENT_LEVEL_ALMOST_REACHED("maintenanceCondensateCollectorReplacementLevelAlmostReached"),
        @SerializedName("maintenanceCondensateCollectorReplacementLevelReached")
        MAINTENANCE_CONDENSATE_COLLECTOR_REPLACEMENT_LEVEL_REACHED("maintenanceCondensateCollectorReplacementLevelReached"),
        @SerializedName("maintenanceCondensateCollectorNotPresent")
        MAINTENANCE_CONDENSATE_COLLECTOR_NOT_PRESENT("maintenanceCondensateCollectorNotPresent"),
        @SerializedName("stallInPrinter")
        STALL_IN_PRINTER("stallInPrinter"),
        @SerializedName("binFullWarning")
        BIN_FULL_WARNING("binFullWarning"),
        @SerializedName("noFuser")
        NO_FUSER("noFuser"),
        @SerializedName("sizeError")
        SIZE_ERROR("sizeError"),
        @SerializedName("sizeErrorPrompt")
        SIZE_ERROR_PROMPT("sizeErrorPrompt"),
        @SerializedName("misprint")
        MISPRINT("misprint"),
        @SerializedName("badOptionalCassetteConnection")
        BAD_OPTIONAL_CASSETTE_CONNECTION("badOptionalCassetteConnection"),
        @SerializedName("optionalCassetteUnsupported")
        OPTIONAL_CASSETTE_UNSUPPORTED("optionalCassetteUnsupported"),
        @SerializedName("misprintPrompt")
        MISPRINT_PROMPT("misprintPrompt"),
        @SerializedName("sizeType")
        SIZE_TYPE("sizeType"),
        @SerializedName("engineFwUpgrade")
        ENGINE_FW_UPGRADE("engineFwUpgrade"),
        @SerializedName("engineFwUpgradeFailed")
        ENGINE_FW_UPGRADE_FAILED("engineFwUpgradeFailed"),
        @SerializedName("printerCoolingDown")
        PRINTER_COOLING_DOWN("printerCoolingDown"),
        @SerializedName("printerTransferBeltCleaningInProgress")
        PRINTER_TRANSFER_BELT_CLEANING_IN_PROGRESS("printerTransferBeltCleaningInProgress"),
        @SerializedName("engineRecoveringFromPowerIssue")
        ENGINE_RECOVERING_FROM_POWER_ISSUE("engineRecoveringFromPowerIssue"),
        @SerializedName("engineInitializing")
        ENGINE_INITIALIZING("engineInitializing"),
        @SerializedName("itbMissing")
        ITB_MISSING("itbMissing"),
        @SerializedName("fuserTypeMismatch")
        FUSER_TYPE_MISMATCH("fuserTypeMismatch"),
        @SerializedName("shippingLockStatus")
        SHIPPING_LOCK_STATUS("shippingLockStatus"),
        @SerializedName("engineTestPage")
        ENGINE_TEST_PAGE("engineTestPage"),
        @SerializedName("systemEventError")
        SYSTEM_EVENT_ERROR("systemEventError"),
        @SerializedName("pushButtonStatus")
        PUSH_BUTTON_STATUS("pushButtonStatus"),
        @SerializedName("pdfEncryption")
        PDF_ENCRYPTION("pdfEncryption"),
        @SerializedName("wfdPbcConnection")
        WFD_PBC_CONNECTION("wfdPbcConnection"),
        @SerializedName("wfdPinConnection")
        WFD_PIN_CONNECTION("wfdPinConnection"),
        @SerializedName("wfdAdvanced")
        WFD_ADVANCED("wfdAdvanced"),
        @SerializedName("wfdConnectionStatus")
        WFD_CONNECTION_STATUS("wfdConnectionStatus"),
        @SerializedName("mediaLoadFlow")
        MEDIA_LOAD_FLOW("mediaLoadFlow"),
        @SerializedName("mediaLoadVerifyFlow")
        MEDIA_LOAD_VERIFY_FLOW("mediaLoadVerifyFlow"),
        @SerializedName("mediaLoadAlternateFlow")
        MEDIA_LOAD_ALTERNATE_FLOW("mediaLoadAlternateFlow"),
        @SerializedName("mediaMismatchSizeFlow")
        MEDIA_MISMATCH_SIZE_FLOW("mediaMismatchSizeFlow"),
        @SerializedName("mediaMismatchSizeAlternateFlow")
        MEDIA_MISMATCH_SIZE_ALTERNATE_FLOW("mediaMismatchSizeAlternateFlow"),
        @SerializedName("mediaMismatchTypeFlow")
        MEDIA_MISMATCH_TYPE_FLOW("mediaMismatchTypeFlow"),
        @SerializedName("mediaMismatchTypeAlternateFlow")
        MEDIA_MISMATCH_TYPE_ALTERNATE_FLOW("mediaMismatchTypeAlternateFlow"),
        @SerializedName("mediaManualLoadFlow")
        MEDIA_MANUAL_LOAD_FLOW("mediaManualLoadFlow"),
        @SerializedName("mediaManualLoadVerifyFlow")
        MEDIA_MANUAL_LOAD_VERIFY_FLOW("mediaManualLoadVerifyFlow"),
        @SerializedName("mediaManualLoadAlternateFlow")
        MEDIA_MANUAL_LOAD_ALTERNATE_FLOW("mediaManualLoadAlternateFlow"),
        @SerializedName("mediaInputErrorFlow")
        MEDIA_INPUT_ERROR_FLOW("mediaInputErrorFlow"),
        @SerializedName("supplyReplacementFlow")
        SUPPLY_REPLACEMENT_FLOW("supplyReplacementFlow"),
        @SerializedName("tubesPurgeFlow")
        TUBES_PURGE_FLOW("tubesPurgeFlow"),
        @SerializedName("supplyValidationFlow")
        SUPPLY_VALIDATION_FLOW("supplyValidationFlow"),
        @SerializedName("cylonFlow")
        CYLON_FLOW("cylonFlow"),
        @SerializedName("phReplacementFlow")
        PH_REPLACEMENT_FLOW("phReplacementFlow"),
        @SerializedName("servicingPhReplacementFlow")
        SERVICING_PH_REPLACEMENT_FLOW("servicingPhReplacementFlow"),
        @SerializedName("servicingMaintenanceFlow")
        SERVICING_MAINTENANCE_FLOW("servicingMaintenanceFlow"),
        @SerializedName("phRecoveryFlow")
        PH_RECOVERY_FLOW("phRecoveryFlow"),
        @SerializedName("genuineHPSupplyFlow")
        GENUINE_HP_SUPPLY_FLOW("genuineHPSupplyFlow"),
        @SerializedName("usedHPSupplyFlow")
        USED_HP_SUPPLY_FLOW("usedHPSupplyFlow"),
        @SerializedName("nonHPSupplyFlow")
        NON_HP_SUPPLY_FLOW("nonHPSupplyFlow"),
        @SerializedName("cartridgeVeryLowContinue")
        CARTRIDGE_VERY_LOW_CONTINUE("cartridgeVeryLowContinue"),
        @SerializedName("cartridgeVeryLowStop")
        CARTRIDGE_VERY_LOW_STOP("cartridgeVeryLowStop"),
        @SerializedName("cartridgeWrongSlot")
        CARTRIDGE_WRONG_SLOT("cartridgeWrongSlot"),
        @SerializedName("cartridgeMemoryMissing")
        CARTRIDGE_MEMORY_MISSING("cartridgeMemoryMissing"),
        @SerializedName("cartridgeMemoryError")
        CARTRIDGE_MEMORY_ERROR("cartridgeMemoryError"),
        @SerializedName("cartridgeDynamicIntegrityViolationError")
        CARTRIDGE_DYNAMIC_INTEGRITY_VIOLATION_ERROR("cartridgeDynamicIntegrityViolationError"),
        @SerializedName("diagnosticPackageFlow")
        DIAGNOSTIC_PACKAGE_FLOW("diagnosticPackageFlow"),
        @SerializedName("loadRollFlow")
        LOAD_ROLL_FLOW("loadRollFlow"),
        @SerializedName("sheetLoadFlow")
        SHEET_LOAD_FLOW("sheetLoadFlow"),
        @SerializedName("OobePaperLoadingFlow")
        OOBE_PAPER_LOADING_FLOW("OobePaperLoadingFlow"),
        @SerializedName("trayLoadFlow")
        TRAY_LOAD_FLOW("trayLoadFlow"),
        @SerializedName("trayCloseFlow")
        TRAY_CLOSE_FLOW("trayCloseFlow"),
        @SerializedName("trayMediaCloseFlow")
        TRAY_MEDIA_CLOSE_FLOW("trayMediaCloseFlow"),
        @SerializedName("SizeMismatchFlow")
        SIZE_MISMATCH_FLOW("SizeMismatchFlow"),
        @SerializedName("moveAndCutMediaFlow")
        MOVE_AND_CUT_MEDIA_FLOW("moveAndCutMediaFlow"),
        @SerializedName("fwUpdateSuccess")
        FW_UPDATE_SUCCESS("fwUpdateSuccess"),
        @SerializedName("fwUpdateFailure")
        FW_UPDATE_FAILURE("fwUpdateFailure"),
        @SerializedName("longLifeConsumableLow")
        LONG_LIFE_CONSUMABLE_LOW("longLifeConsumableLow"),
        @SerializedName("longLifeConsumableVeryLow")
        LONG_LIFE_CONSUMABLE_VERY_LOW("longLifeConsumableVeryLow"),
        @SerializedName("netwdogStatus")
        NETWDOG_STATUS("netwdogStatus"),
        @SerializedName("collationNotHonoured")
        COLLATION_NOT_HONOURED("collationNotHonoured"),
        @SerializedName("pairingFailed")
        PAIRING_FAILED("pairingFailed"),
        @SerializedName("faxReceiveState")
        FAX_RECEIVE_STATE("faxReceiveState"),
        @SerializedName("faxSendState")
        FAX_SEND_STATE("faxSendState"),
        @SerializedName("ippDisplayMessageStatus")
        IPP_DISPLAY_MESSAGE_STATUS("ippDisplayMessageStatus"),
        @SerializedName("incompatibleCartridge")
        INCOMPATIBLE_CARTRIDGE("incompatibleCartridge"),
        @SerializedName("usedSupplyPrompt")
        USED_SUPPLY_PROMPT("usedSupplyPrompt"),
        @SerializedName("cartridgeUnauthorized")
        CARTRIDGE_UNAUTHORIZED("cartridgeUnauthorized"),
        @SerializedName("antiTheftEnabledSupplyError")
        ANTI_THEFT_ENABLED_SUPPLY_ERROR("antiTheftEnabledSupplyError"),
        @SerializedName("cartridgeVeryLowPrintingBlackOnly")
        CARTRIDGE_VERY_LOW_PRINTING_BLACK_ONLY("cartridgeVeryLowPrintingBlackOnly"),
        @SerializedName("awcTimeout")
        AWC_TIMEOUT("awcTimeout"),
        @SerializedName("generalSupplyError")
        GENERAL_SUPPLY_ERROR("generalSupplyError"),
        @SerializedName("generalSystemError")
        GENERAL_SYSTEM_ERROR("generalSystemError"),
        @SerializedName("faxExtensionPhoneState")
        FAX_EXTENSION_PHONE_STATE("faxExtensionPhoneState"),
        @SerializedName("blackSupplyVeryLowFlow")
        BLACK_SUPPLY_VERY_LOW_FLOW("blackSupplyVeryLowFlow"),
        @SerializedName("colorSupplyVeryLowFlow")
        COLOR_SUPPLY_VERY_LOW_FLOW("colorSupplyVeryLowFlow"),
        @SerializedName("subscribedPagesLowConnectionProblem")
        SUBSCRIBED_PAGES_LOW_CONNECTION_PROBLEM("subscribedPagesLowConnectionProblem"),
        @SerializedName("subscribedPagesLowAccountProblem")
        SUBSCRIBED_PAGES_LOW_ACCOUNT_PROBLEM("subscribedPagesLowAccountProblem"),
        @SerializedName("subscribedPagesVeryLowConnectionProblem")
        SUBSCRIBED_PAGES_VERY_LOW_CONNECTION_PROBLEM("subscribedPagesVeryLowConnectionProblem"),
        @SerializedName("subscribedPagesVeryLowAccountProblem")
        SUBSCRIBED_PAGES_VERY_LOW_ACCOUNT_PROBLEM("subscribedPagesVeryLowAccountProblem"),
        @SerializedName("subscribedPagesOutConnectionProblem")
        SUBSCRIBED_PAGES_OUT_CONNECTION_PROBLEM("subscribedPagesOutConnectionProblem"),
        @SerializedName("subscribedPagesOutAccountProblem")
        SUBSCRIBED_PAGES_OUT_ACCOUNT_PROBLEM("subscribedPagesOutAccountProblem"),
        @SerializedName("connectToOrderCartridge")
        CONNECT_TO_ORDER_CARTRIDGE("connectToOrderCartridge"),
        @SerializedName("connectToContinueSubscription")
        CONNECT_TO_CONTINUE_SUBSCRIPTION("connectToContinueSubscription"),
        @SerializedName("waitingForSupplyAssessment")
        WAITING_FOR_SUPPLY_ASSESSMENT("waitingForSupplyAssessment"),
        @SerializedName("printerNeedsWSRegistrationNow")
        PRINTER_NEEDS_WS_REGISTRATION_NOW("printerNeedsWSRegistrationNow"),
        @SerializedName("printerNeedsWSRegistrationSoon")
        PRINTER_NEEDS_WS_REGISTRATION_SOON("printerNeedsWSRegistrationSoon"),
        @SerializedName("supplyAssessmentNeedConnect")
        SUPPLY_ASSESSMENT_NEED_CONNECT("supplyAssessmentNeedConnect"),
        @SerializedName("subscribedPagesLow")
        SUBSCRIBED_PAGES_LOW("subscribedPagesLow"),
        @SerializedName("fwUpdateAvailable")
        FW_UPDATE_AVAILABLE("fwUpdateAvailable"),
        @SerializedName("servicingPowerOnFlow")
        SERVICING_POWER_ON_FLOW("servicingPowerOnFlow"),
        @SerializedName("jobStorageDisabled")
        JOB_STORAGE_DISABLED("jobStorageDisabled"),
        @SerializedName("jobStorageFormatUsb")
        JOB_STORAGE_FORMAT_USB("jobStorageFormatUsb"),
        @SerializedName("jobStorageFormatUsbCancel")
        JOB_STORAGE_FORMAT_USB_CANCEL("jobStorageFormatUsbCancel"),
        @SerializedName("jobStorageFormatUsbCancelNoFrontPort")
        JOB_STORAGE_FORMAT_USB_CANCEL_NO_FRONT_PORT("jobStorageFormatUsbCancelNoFrontPort"),
        @SerializedName("diagnosticsFlow")
        DIAGNOSTICS_FLOW("diagnosticsFlow"),
        @SerializedName("peiDiagnosticsFlow")
        PEI_DIAGNOSTICS_FLOW("peiDiagnosticsFlow"),
        @SerializedName("eboxDiagnosticsFlow")
        EBOX_DIAGNOSTICS_FLOW("eboxDiagnosticsFlow"),
        @SerializedName("jobStorageFormatUsbContinue")
        JOB_STORAGE_FORMAT_USB_CONTINUE("jobStorageFormatUsbContinue"),
        @SerializedName("jobStorageFormatUsbSuccessful")
        JOB_STORAGE_FORMAT_USB_SUCCESSFUL("jobStorageFormatUsbSuccessful"),
        @SerializedName("jobStorageFormatUsbUnsuccessful")
        JOB_STORAGE_FORMAT_USB_UNSUCCESSFUL("jobStorageFormatUsbUnsuccessful"),
        @SerializedName("jamAutoNav")
        JAM_AUTO_NAV("jamAutoNav"),
        @SerializedName("awcConnectionStatus")
        AWC_CONNECTION_STATUS("awcConnectionStatus"),
        @SerializedName("iotPairingCodeAvailable")
        IOT_PAIRING_CODE_AVAILABLE("iotPairingCodeAvailable"),
        @SerializedName("cartridgeAlienationDetectionError")
        CARTRIDGE_ALIENATION_DETECTION_ERROR("cartridgeAlienationDetectionError"),
        @SerializedName("shaidElectricalError")
        SHAID_ELECTRICAL_ERROR("shaidElectricalError"),
        @SerializedName("shaidOoiTooEarlyError")
        SHAID_OOI_TOO_EARLY_ERROR("shaidOoiTooEarlyError"),
        @SerializedName("shaidFailed")
        SHAID_FAILED("shaidFailed"),
        @SerializedName("carriageStallFlow")
        CARRIAGE_STALL_FLOW("carriageStallFlow"),
        @SerializedName("mediaJamFlow")
        MEDIA_JAM_FLOW("mediaJamFlow"),
        @SerializedName("outOfMediaFlow")
        OUT_OF_MEDIA_FLOW("outOfMediaFlow"),
        @SerializedName("outOfMediaNonPlainFlow")
        OUT_OF_MEDIA_NON_PLAIN_FLOW("outOfMediaNonPlainFlow"),
        @SerializedName("mediaTooShortToPrintFlow")
        MEDIA_TOO_SHORT_TO_PRINT_FLOW("mediaTooShortToPrintFlow"),
        @SerializedName("mediaTooShortToAutoDuplexFlow")
        MEDIA_TOO_SHORT_TO_AUTO_DUPLEX_FLOW("mediaTooShortToAutoDuplexFlow"),
        @SerializedName("mediaTooLongToAutoDuplexFlow")
        MEDIA_TOO_LONG_TO_AUTO_DUPLEX_FLOW("mediaTooLongToAutoDuplexFlow"),
        @SerializedName("allTraysEmptyPrompt")
        ALL_TRAYS_EMPTY_PROMPT("allTraysEmptyPrompt"),
        @SerializedName("endOfRollFlow")
        END_OF_ROLL_FLOW("endOfRollFlow"),
        @SerializedName("releaseMediaFlow")
        RELEASE_MEDIA_FLOW("releaseMediaFlow"),
        @SerializedName("mediaMismatchUnsupportedSize")
        MEDIA_MISMATCH_UNSUPPORTED_SIZE("mediaMismatchUnsupportedSize"),
        @SerializedName("printheadsAutomaticAlignmentCalibrationIncomplete")
        PRINTHEADS_AUTOMATIC_ALIGNMENT_CALIBRATION_INCOMPLETE("printheadsAutomaticAlignmentCalibrationIncomplete"),
        @SerializedName("printheadChangeRequiredFlow")
        PRINTHEAD_CHANGE_REQUIRED_FLOW("printheadChangeRequiredFlow"),
        @SerializedName("printheadNeedReseatFlow")
        PRINTHEAD_NEED_RESEAT_FLOW("printheadNeedReseatFlow"),
        @SerializedName("printheadFailureFlow")
        PRINTHEAD_FAILURE_FLOW("printheadFailureFlow"),
        @SerializedName("allSourcesEmptyPrompt")
        ALL_SOURCES_EMPTY_PROMPT("allSourcesEmptyPrompt"),
        @SerializedName("smartTakeUpReelFlow")
        SMART_TAKE_UP_REEL_FLOW("smartTakeUpReelFlow"),
        @SerializedName("basketOutputFlow")
        BASKET_OUTPUT_FLOW("basketOutputFlow"),
        @SerializedName("stackerOutputFlow")
        STACKER_OUTPUT_FLOW("stackerOutputFlow"),
        @SerializedName("folderOutputFlow")
        FOLDER_OUTPUT_FLOW("folderOutputFlow"),
        @SerializedName("ethCableConnectionStatus")
        ETH_CABLE_CONNECTION_STATUS("ethCableConnectionStatus"),
        @SerializedName("wifiConnectivityStatus")
        WIFI_CONNECTIVITY_STATUS("wifiConnectivityStatus"),
        @SerializedName("noInternetConnectionStatus")
        NO_INTERNET_CONNECTION_STATUS("noInternetConnectionStatus"),
        @SerializedName("duplicateIpStatus")
        DUPLICATE_IP_STATUS("duplicateIpStatus"),
        @SerializedName("inputSourceLocked")
        INPUT_SOURCE_LOCKED("inputSourceLocked"),
        @SerializedName("mediaTypeProtected")
        MEDIA_TYPE_PROTECTED("mediaTypeProtected"),
        @SerializedName("allSourcesUnAvailable")
        ALL_SOURCES_UN_AVAILABLE("allSourcesUnAvailable"),
        @SerializedName("e2extensibilityMessageCenter")
        E_2_EXTENSIBILITY_MESSAGE_CENTER("e2extensibilityMessageCenter"),
        @SerializedName("usbMountError")
        USB_MOUNT_ERROR("usbMountError"),
        @SerializedName("usbOverCurrentError")
        USB_OVER_CURRENT_ERROR("usbOverCurrentError"),
        @SerializedName("usbDeviceNotResponding")
        USB_DEVICE_NOT_RESPONDING("usbDeviceNotResponding"),
        @SerializedName("usbUnsupportedDeviceError")
        USB_UNSUPPORTED_DEVICE_ERROR("usbUnsupportedDeviceError"),
        @SerializedName("usbHubError")
        USB_HUB_ERROR("usbHubError"),
        @SerializedName("usbDeviceInserted")
        USB_DEVICE_INSERTED("usbDeviceInserted"),
        @SerializedName("jobStatus")
        JOB_STATUS("jobStatus"),
        @SerializedName("inQuietMode")
        IN_QUIET_MODE("inQuietMode"),
        @SerializedName("quietModeIgnored")
        QUIET_MODE_IGNORED("quietModeIgnored"),
        @SerializedName("dynamicSecurityCheckFailed")
        DYNAMIC_SECURITY_CHECK_FAILED("dynamicSecurityCheckFailed"),
        @SerializedName("calibrationRequired")
        CALIBRATION_REQUIRED("calibrationRequired"),
        @SerializedName("carriageJam")
        CARRIAGE_JAM("carriageJam"),
        @SerializedName("cartridgeBadMfgBatchInUse")
        CARTRIDGE_BAD_MFG_BATCH_IN_USE("cartridgeBadMfgBatchInUse"),
        @SerializedName("cartridgeCounterfeit")
        CARTRIDGE_COUNTERFEIT("cartridgeCounterfeit"),
        @SerializedName("cartridgeCounterfeitQuestion")
        CARTRIDGE_COUNTERFEIT_QUESTION("cartridgeCounterfeitQuestion"),
        @SerializedName("cartridgeLimitedInAvailability")
        CARTRIDGE_LIMITED_IN_AVAILABILITY("cartridgeLimitedInAvailability"),
        @SerializedName("cartridgeRefilled")
        CARTRIDGE_REFILLED("cartridgeRefilled"),
        @SerializedName("cartridgeSelectabilityNumberChange")
        CARTRIDGE_SELECTABILITY_NUMBER_CHANGE("cartridgeSelectabilityNumberChange"),
        @SerializedName("cartridgeSupportMessage")
        CARTRIDGE_SUPPORT_MESSAGE("cartridgeSupportMessage"),
        @SerializedName("cartridgeWarranty")
        CARTRIDGE_WARRANTY("cartridgeWarranty"),
        @SerializedName("inkSystemInitializing")
        INK_SYSTEM_INITIALIZING("inkSystemInitializing"),
        @SerializedName("scannerError")
        SCANNER_ERROR("scannerError"),
        @SerializedName("sharedSelectError")
        SHARED_SELECT_ERROR("sharedSelectError"),
        @SerializedName("singleCartridgeMode")
        SINGLE_CARTRIDGE_MODE("singleCartridgeMode"),
        @SerializedName("sizeMismatchInTray")
        SIZE_MISMATCH_IN_TRAY("sizeMismatchInTray"),
        @SerializedName("subscriptionConsumableNeedsEnrollment")
        SUBSCRIPTION_CONSUMABLE_NEEDS_ENROLLMENT("subscriptionConsumableNeedsEnrollment"),
        @SerializedName("trialConsumableCountExceeded")
        TRIAL_CONSUMABLE_COUNT_EXCEEDED("trialConsumableCountExceeded"),
        @SerializedName("usedCartridge")
        USED_CARTRIDGE("usedCartridge"),
        @SerializedName("cartridgeDoorOpen")
        CARTRIDGE_DOOR_OPEN("cartridgeDoorOpen"),
        @SerializedName("printheadReplacePreparing")
        PRINTHEAD_REPLACE_PREPARING("printheadReplacePreparing"),
        @SerializedName("printheadReplaceChecking")
        PRINTHEAD_REPLACE_CHECKING("printheadReplaceChecking"),
        @SerializedName("scannerCoverOpen")
        SCANNER_COVER_OPEN("scannerCoverOpen"),
        @SerializedName("destinationInError")
        DESTINATION_IN_ERROR("destinationInError"),
        @SerializedName("outputSizeMismatch")
        OUTPUT_SIZE_MISMATCH("outputSizeMismatch"),
        @SerializedName("outputTypeMismatch")
        OUTPUT_TYPE_MISMATCH("outputTypeMismatch"),
        @SerializedName("outputSizeAndTypeMismatch")
        OUTPUT_SIZE_AND_TYPE_MISMATCH("outputSizeAndTypeMismatch"),
        @SerializedName("outputNotExist")
        OUTPUT_NOT_EXIST("outputNotExist"),
        @SerializedName("allDestinationUnavailable")
        ALL_DESTINATION_UNAVAILABLE("allDestinationUnavailable"),
        @SerializedName("destinationFull")
        DESTINATION_FULL("destinationFull"),
        @SerializedName("autoShutdownEnabled")
        AUTO_SHUTDOWN_ENABLED("autoShutdownEnabled"),
        @SerializedName("misrSignalServicing")
        MISR_SIGNAL_SERVICING("misrSignalServicing"),
        @SerializedName("inMaintenanceMode")
        IN_MAINTENANCE_MODE("inMaintenanceMode"),
        @SerializedName("manualDuplexPrompt")
        MANUAL_DUPLEX_PROMPT("manualDuplexPrompt"),
        @SerializedName("manualFeedPrompt")
        MANUAL_FEED_PROMPT("manualFeedPrompt"),
        @SerializedName("tokenLicenseFlow")
        TOKEN_LICENSE_FLOW("tokenLicenseFlow"),
        @SerializedName("preparePrinterFlow")
        PREPARE_PRINTER_FLOW("preparePrinterFlow"),
        @SerializedName("gaiaNewPackageAvailable")
        GAIA_NEW_PACKAGE_AVAILABLE("gaiaNewPackageAvailable"),
        @SerializedName("gaiaDownloadingPackage")
        GAIA_DOWNLOADING_PACKAGE("gaiaDownloadingPackage"),
        @SerializedName("gaiaPackagePendingToInstall")
        GAIA_PACKAGE_PENDING_TO_INSTALL("gaiaPackagePendingToInstall"),
        @SerializedName("gaiaPackageDownloadFailed")
        GAIA_PACKAGE_DOWNLOAD_FAILED("gaiaPackageDownloadFailed"),
        @SerializedName("attendedModeRequired")
        ATTENDED_MODE_REQUIRED("attendedModeRequired"),
        @SerializedName("attendedModeFlow")
        ATTENDED_MODE_FLOW("attendedModeFlow"),
        @SerializedName("cleanPhRecommendationFlow")
        CLEAN_PH_RECOMMENDATION_FLOW("cleanPhRecommendationFlow"),
        @SerializedName("jobsOnHoldForConflict")
        JOBS_ON_HOLD_FOR_CONFLICT("jobsOnHoldForConflict"),
        @SerializedName("jobMismatchPenError")
        JOB_MISMATCH_PEN_ERROR("jobMismatchPenError"),
        @SerializedName("jobMismatchInkSupplyError")
        JOB_MISMATCH_INK_SUPPLY_ERROR("jobMismatchInkSupplyError"),
        @SerializedName("jobMismatchOthersSuppliesError")
        JOB_MISMATCH_OTHERS_SUPPLIES_ERROR("jobMismatchOthersSuppliesError"),
        @SerializedName("jobMismatchWhitePhNotInstalled")
        JOB_MISMATCH_WHITE_PH_NOT_INSTALLED("jobMismatchWhitePhNotInstalled"),
        @SerializedName("wifiWeakSignal")
        WIFI_WEAK_SIGNAL("wifiWeakSignal"),
        @SerializedName("printheadUsedWithNonHPInk")
        PRINTHEAD_USED_WITH_NON_HP_INK("printheadUsedWithNonHPInk"),
        @SerializedName("chadCollectorFull")
        CHAD_COLLECTOR_FULL("chadCollectorFull"),
        @SerializedName("chadCollectorMissing")
        CHAD_COLLECTOR_MISSING("chadCollectorMissing"),
        @SerializedName("chadCollectorFullOrMissing")
        CHAD_COLLECTOR_FULL_OR_MISSING("chadCollectorFullOrMissing"),
        @SerializedName("trayLockError")
        TRAY_LOCK_ERROR("trayLockError"),
        @SerializedName("acrSensorCalibrationError")
        ACR_SENSOR_CALIBRATION_ERROR("acrSensorCalibrationError"),
        @SerializedName("ctdSensorCalibrationError")
        CTD_SENSOR_CALIBRATION_ERROR("ctdSensorCalibrationError"),
        @SerializedName("chargerCleanNeeded")
        CHARGER_CLEAN_NEEDED("chargerCleanNeeded"),
        @SerializedName("factoryMode")
        FACTORY_MODE("factoryMode"),
        @SerializedName("finisherRecoverableError")
        FINISHER_RECOVERABLE_ERROR("finisherRecoverableError"),
        @SerializedName("tcCalibrationNeeded")
        TC_CALIBRATION_NEEDED("tcCalibrationNeeded"),
        @SerializedName("fuserWrapJam")
        FUSER_WRAP_JAM("fuserWrapJam"),
        @SerializedName("trayShiftError")
        TRAY_SHIFT_ERROR("trayShiftError"),
        @SerializedName("trayGateError")
        TRAY_GATE_ERROR("trayGateError"),
        @SerializedName("tonerEndSensorError")
        TONER_END_SENSOR_ERROR("tonerEndSensorError"),
        @SerializedName("tonerEndDetectionError")
        TONER_END_DETECTION_ERROR("tonerEndDetectionError"),
        @SerializedName("wrapJamSensorCalibrationError")
        WRAP_JAM_SENSOR_CALIBRATION_ERROR("wrapJamSensorCalibrationError"),
        @SerializedName("trayPreFeedError")
        TRAY_PRE_FEED_ERROR("trayPreFeedError"),
        @SerializedName("binStackSensorError")
        BIN_STACK_SENSOR_ERROR("binStackSensorError"),
        @SerializedName("wasteDuctNotInstalled")
        WASTE_DUCT_NOT_INSTALLED("wasteDuctNotInstalled"),
        @SerializedName("finisherSlowerThanEngine")
        FINISHER_SLOWER_THAN_ENGINE("finisherSlowerThanEngine"),
        @SerializedName("finisherDeviceCableDisconnected")
        FINISHER_DEVICE_CABLE_DISCONNECTED("finisherDeviceCableDisconnected"),
        @SerializedName("finisherDeviceCommError")
        FINISHER_DEVICE_COMM_ERROR("finisherDeviceCommError"),
        @SerializedName("finisherDeviceError")
        FINISHER_DEVICE_ERROR("finisherDeviceError"),
        @SerializedName("finisherDeviceOpen")
        FINISHER_DEVICE_OPEN("finisherDeviceOpen"),
        @SerializedName("finisherDeviceFull")
        FINISHER_DEVICE_FULL("finisherDeviceFull"),
        @SerializedName("finisherDeviceJam")
        FINISHER_DEVICE_JAM("finisherDeviceJam"),
        @SerializedName("finisherDeviceUpgradingFirmware")
        FINISHER_DEVICE_UPGRADING_FIRMWARE("finisherDeviceUpgradingFirmware"),
        @SerializedName("calibrateMediaAdvanceFlow")
        CALIBRATE_MEDIA_ADVANCE_FLOW("calibrateMediaAdvanceFlow"),
        @SerializedName("calibrationTypeMismatch")
        CALIBRATION_TYPE_MISMATCH("calibrationTypeMismatch"),
        @SerializedName("supplyMismatchGeneric")
        SUPPLY_MISMATCH_GENERIC("supplyMismatchGeneric"),
        @SerializedName("supplyMismatchWhiteRequired")
        SUPPLY_MISMATCH_WHITE_REQUIRED("supplyMismatchWhiteRequired"),
        @SerializedName("supplyMismatchWhiteInstalled")
        SUPPLY_MISMATCH_WHITE_INSTALLED("supplyMismatchWhiteInstalled"),
        @SerializedName("collateProblem")
        COLLATE_PROBLEM("collateProblem"),
        @SerializedName("morePagesDetectedForCollate")
        MORE_PAGES_DETECTED_FOR_COLLATE("morePagesDetectedForCollate"),
        @SerializedName("maintenanceCartridgeReplacement")
        MAINTENANCE_CARTRIDGE_REPLACEMENT("maintenanceCartridgeReplacement"),
        @SerializedName("maintenanceCartridgeMissing")
        MAINTENANCE_CARTRIDGE_MISSING("maintenanceCartridgeMissing"),
        @SerializedName("distilledWaterTankDepleted")
        DISTILLED_WATER_TANK_DEPLETED("distilledWaterTankDepleted"),
        @SerializedName("continuousInkSystemDetected")
        CONTINUOUS_INK_SYSTEM_DETECTED("continuousInkSystemDetected"),
        @SerializedName("mimoAccessoryFlow")
        MIMO_ACCESSORY_FLOW("mimoAccessoryFlow"),
        @SerializedName("launchApplication")
        LAUNCH_APPLICATION("launchApplication"),
        @SerializedName("trayBlocked")
        TRAY_BLOCKED("trayBlocked"),
        @SerializedName("trayLiftupInterrupted")
        TRAY_LIFTUP_INTERRUPTED("trayLiftupInterrupted"),
        @SerializedName("finisherDeviceUninstalled")
        FINISHER_DEVICE_UNINSTALLED("finisherDeviceUninstalled"),
        @SerializedName("printJobRecovered")
        PRINT_JOB_RECOVERED("printJobRecovered");
        private final String value;
        private final static Map<String, Alert.Category> CONSTANTS = new HashMap<String, Alert.Category>();

        static {
            for (Alert.Category c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Category(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Alert.Category fromValue(String value) {
            Alert.Category constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Indicates the scope of  the alert
     * 
     */
    public enum Scope {

        @SerializedName("publicOnly")
        PUBLIC_ONLY("publicOnly"),
        @SerializedName("public")
        PUBLIC("public"),
        @SerializedName("private")
        PRIVATE("private");
        private final String value;
        private final static Map<String, Alert.Scope> CONSTANTS = new HashMap<String, Alert.Scope>();

        static {
            for (Alert.Scope c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Scope(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Alert.Scope fromValue(String value) {
            Alert.Scope constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * The severity of the alert
     * 
     */
    public enum Severity {

        @SerializedName("information")
        INFORMATION("information"),
        @SerializedName("warning")
        WARNING("warning"),
        @SerializedName("error")
        ERROR("error"),
        @SerializedName("critical")
        CRITICAL("critical");
        private final String value;
        private final static Map<String, Alert.Severity> CONSTANTS = new HashMap<String, Alert.Severity>();

        static {
            for (Alert.Severity c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Severity(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Alert.Severity fromValue(String value) {
            Alert.Severity constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Indicates the visibility of the alert
     * 
     */
    public enum Visibility {

        @SerializedName("all")
        ALL("all"),
        @SerializedName("excludeFrontPanel")
        EXCLUDE_FRONT_PANEL("excludeFrontPanel"),
        @SerializedName("frontPanelOnly")
        FRONT_PANEL_ONLY("frontPanelOnly");
        private final String value;
        private final static Map<String, Alert.Visibility> CONSTANTS = new HashMap<String, Alert.Visibility>();

        static {
            for (Alert.Visibility c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Visibility(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Alert.Visibility fromValue(String value) {
            Alert.Visibility constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
