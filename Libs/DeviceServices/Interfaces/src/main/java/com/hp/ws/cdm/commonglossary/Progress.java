
package com.hp.ws.cdm.commonglossary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Progress for a flow / step
 * 
 */
public class Progress {

    /**
     * flow steps
     * 
     */
    @SerializedName("currentStep")
    @Expose
    private Progress.FlowSteps currentStep;
    /**
     * Current step index in the current flow (step currentStepIndex of totalSteps)
     * 
     */
    @SerializedName("currentStepIndex")
    @Expose
    private Integer currentStepIndex;
    /**
     * Progress state for a flow / step, percent time remaining
     * 
     */
    @SerializedName("flowProgressState")
    @Expose
    private ProgressState flowProgressState;
    /**
     * Progress state for a flow / step, percent time remaining
     * 
     */
    @SerializedName("stepProgressState")
    @Expose
    private ProgressState stepProgressState;
    /**
     * Number of steps to be executed in happy path for the current flow / branch in flow ((step currentStepIndex of totalSteps)
     * 
     */
    @SerializedName("totalSteps")
    @Expose
    private Integer totalSteps;
    /**
     * List of valid steps for this flow
     * 
     */
    @SerializedName("validSteps")
    @Expose
    private List<Progress.FlowSteps> validSteps = new ArrayList<Progress.FlowSteps>();

    /**
     * flow steps
     * 
     */
    public Progress.FlowSteps getCurrentStep() {
        return currentStep;
    }

    /**
     * flow steps
     * 
     */
    public void setCurrentStep(Progress.FlowSteps currentStep) {
        this.currentStep = currentStep;
    }

    /**
     * Current step index in the current flow (step currentStepIndex of totalSteps)
     * 
     */
    public Integer getCurrentStepIndex() {
        return currentStepIndex;
    }

    /**
     * Current step index in the current flow (step currentStepIndex of totalSteps)
     * 
     */
    public void setCurrentStepIndex(Integer currentStepIndex) {
        this.currentStepIndex = currentStepIndex;
    }

    /**
     * Progress state for a flow / step, percent time remaining
     * 
     */
    public ProgressState getFlowProgressState() {
        return flowProgressState;
    }

    /**
     * Progress state for a flow / step, percent time remaining
     * 
     */
    public void setFlowProgressState(ProgressState flowProgressState) {
        this.flowProgressState = flowProgressState;
    }

    /**
     * Progress state for a flow / step, percent time remaining
     * 
     */
    public ProgressState getStepProgressState() {
        return stepProgressState;
    }

    /**
     * Progress state for a flow / step, percent time remaining
     * 
     */
    public void setStepProgressState(ProgressState stepProgressState) {
        this.stepProgressState = stepProgressState;
    }

    /**
     * Number of steps to be executed in happy path for the current flow / branch in flow ((step currentStepIndex of totalSteps)
     * 
     */
    public Integer getTotalSteps() {
        return totalSteps;
    }

    /**
     * Number of steps to be executed in happy path for the current flow / branch in flow ((step currentStepIndex of totalSteps)
     * 
     */
    public void setTotalSteps(Integer totalSteps) {
        this.totalSteps = totalSteps;
    }

    /**
     * List of valid steps for this flow
     * 
     */
    public List<Progress.FlowSteps> getValidSteps() {
        return validSteps;
    }

    /**
     * List of valid steps for this flow
     * 
     */
    public void setValidSteps(List<Progress.FlowSteps> validSteps) {
        this.validSteps = validSteps;
    }


    /**
     * flow steps
     * 
     */
    public enum FlowSteps {

        @SerializedName("preparing")
        PREPARING("preparing"),
        @SerializedName("checking")
        CHECKING("checking"),
        @SerializedName("cancelling")
        CANCELLING("cancelling"),
        @SerializedName("finishing")
        FINISHING("finishing"),
        @SerializedName("waitForDoorOpen")
        WAIT_FOR_DOOR_OPEN("waitForDoorOpen"),
        @SerializedName("waitForDoorClose")
        WAIT_FOR_DOOR_CLOSE("waitForDoorClose"),
        @SerializedName("cancelled")
        CANCELLED("cancelled"),
        @SerializedName("finished")
        FINISHED("finished"),
        @SerializedName("notReady")
        NOT_READY("notReady"),
        @SerializedName("ready")
        READY("ready"),
        @SerializedName("error")
        ERROR("error"),
        @SerializedName("resuming")
        RESUMING("resuming"),
        @SerializedName("selectMedia")
        SELECT_MEDIA("selectMedia"),
        @SerializedName("helpMessage")
        HELP_MESSAGE("helpMessage"),
        @SerializedName("howToReplace")
        HOW_TO_REPLACE("howToReplace"),
        @SerializedName("liveReporter")
        LIVE_REPORTER("liveReporter"),
        @SerializedName("recirculationNeeded")
        RECIRCULATION_NEEDED("recirculationNeeded"),
        @SerializedName("recirculationWaiting")
        RECIRCULATION_WAITING("recirculationWaiting"),
        @SerializedName("hostEndOfLifeNotification")
        HOST_END_OF_LIFE_NOTIFICATION("hostEndOfLifeNotification"),
        @SerializedName("hostEndOfLifeWarning")
        HOST_END_OF_LIFE_WARNING("hostEndOfLifeWarning"),
        @SerializedName("traitAndHostEndOfLifeWarning")
        TRAIT_AND_HOST_END_OF_LIFE_WARNING("traitAndHostEndOfLifeWarning"),
        @SerializedName("traitTooLowForNewHostWarning")
        TRAIT_TOO_LOW_FOR_NEW_HOST_WARNING("traitTooLowForNewHostWarning"),
        @SerializedName("printheadInserted")
        PRINTHEAD_INSERTED("printheadInserted"),
        @SerializedName("printheadCoolingDown")
        PRINTHEAD_COOLING_DOWN("printheadCoolingDown"),
        @SerializedName("coolingFinished")
        COOLING_FINISHED("coolingFinished"),
        @SerializedName("hotWhitePenExtracted")
        HOT_WHITE_PEN_EXTRACTED("hotWhitePenExtracted"),
        @SerializedName("insertWhitePrintheads")
        INSERT_WHITE_PRINTHEADS("insertWhitePrintheads"),
        @SerializedName("wheelInfo")
        WHEEL_INFO("wheelInfo"),
        @SerializedName("wheelWarning")
        WHEEL_WARNING("wheelWarning"),
        @SerializedName("doorOpen")
        DOOR_OPEN("doorOpen"),
        @SerializedName("outOfSubstrate")
        OUT_OF_SUBSTRATE("outOfSubstrate"),
        @SerializedName("selectSubstrateType")
        SELECT_SUBSTRATE_TYPE("selectSubstrateType"),
        @SerializedName("selectSubstrateProperties")
        SELECT_SUBSTRATE_PROPERTIES("selectSubstrateProperties"),
        @SerializedName("feedDone")
        FEED_DONE("feedDone"),
        @SerializedName("loadingRoll")
        LOADING_ROLL("loadingRoll"),
        @SerializedName("userVerify")
        USER_VERIFY("userVerify"),
        @SerializedName("loadMedia")
        LOAD_MEDIA("loadMedia"),
        @SerializedName("useAnotherInput")
        USE_ANOTHER_INPUT("useAnotherInput"),
        @SerializedName("requestRemovePrintheads")
        REQUEST_REMOVE_PRINTHEADS("requestRemovePrintheads"),
        @SerializedName("requestInstallPurgers")
        REQUEST_INSTALL_PURGERS("requestInstallPurgers"),
        @SerializedName("verifyInksInitialization")
        VERIFY_INKS_INITIALIZATION("verifyInksInitialization"),
        @SerializedName("verifyInksInitializationNoRetry")
        VERIFY_INKS_INITIALIZATION_NO_RETRY("verifyInksInitializationNoRetry"),
        @SerializedName("requestRemovePurgers")
        REQUEST_REMOVE_PURGERS("requestRemovePurgers"),
        @SerializedName("finishingSystemInitialization")
        FINISHING_SYSTEM_INITIALIZATION("finishingSystemInitialization"),
        @SerializedName("inkSystemStartupDone")
        INK_SYSTEM_STARTUP_DONE("inkSystemStartupDone"),
        @SerializedName("requestRemoveWhiteSupply")
        REQUEST_REMOVE_WHITE_SUPPLY("requestRemoveWhiteSupply"),
        @SerializedName("requestInstallDisposalBag")
        REQUEST_INSTALL_DISPOSAL_BAG("requestInstallDisposalBag"),
        @SerializedName("verifyDisposalBagFilled")
        VERIFY_DISPOSAL_BAG_FILLED("verifyDisposalBagFilled"),
        @SerializedName("requestRemoveDisposalBag")
        REQUEST_REMOVE_DISPOSAL_BAG("requestRemoveDisposalBag"),
        @SerializedName("requestRemoveDisposal")
        REQUEST_REMOVE_DISPOSAL("requestRemoveDisposal"),
        @SerializedName("cylonAcceptHpGenuineNew")
        CYLON_ACCEPT_HP_GENUINE_NEW("cylonAcceptHpGenuineNew"),
        @SerializedName("cylonAcceptHpGenuinePreviouslyUsed")
        CYLON_ACCEPT_HP_GENUINE_PREVIOUSLY_USED("cylonAcceptHpGenuinePreviouslyUsed"),
        @SerializedName("cylonAcceptOem")
        CYLON_ACCEPT_OEM("cylonAcceptOem"),
        @SerializedName("cylonAcceptExpired")
        CYLON_ACCEPT_EXPIRED("cylonAcceptExpired"),
        @SerializedName("cylonAcceptOutOfWarranty")
        CYLON_ACCEPT_OUT_OF_WARRANTY("cylonAcceptOutOfWarranty"),
        @SerializedName("cylonAcceptNonHpGenuine")
        CYLON_ACCEPT_NON_HP_GENUINE("cylonAcceptNonHpGenuine"),
        @SerializedName("cylonAcceptAlteredInfo")
        CYLON_ACCEPT_ALTERED_INFO("cylonAcceptAlteredInfo"),
        @SerializedName("cylonAcceptAlteredWarrantyNote")
        CYLON_ACCEPT_ALTERED_WARRANTY_NOTE("cylonAcceptAlteredWarrantyNote"),
        @SerializedName("cylonAcceptAlteredConfirm")
        CYLON_ACCEPT_ALTERED_CONFIRM("cylonAcceptAlteredConfirm"),
        @SerializedName("cylonAcceptAlteredFinished")
        CYLON_ACCEPT_ALTERED_FINISHED("cylonAcceptAlteredFinished"),
        @SerializedName("cylonCancelAlteredInstallation")
        CYLON_CANCEL_ALTERED_INSTALLATION("cylonCancelAlteredInstallation"),
        @SerializedName("cylonAcceptBackToHpGenuine")
        CYLON_ACCEPT_BACK_TO_HP_GENUINE("cylonAcceptBackToHpGenuine"),
        @SerializedName("cylonSafeStopCissDetected")
        CYLON_SAFE_STOP_CISS_DETECTED("cylonSafeStopCissDetected"),
        @SerializedName("cylonIncompatibleSupply")
        CYLON_INCOMPATIBLE_SUPPLY("cylonIncompatibleSupply"),
        @SerializedName("cylonWrongRegion")
        CYLON_WRONG_REGION("cylonWrongRegion"),
        @SerializedName("cylonReplaceOrReseatSupply")
        CYLON_REPLACE_OR_RESEAT_SUPPLY("cylonReplaceOrReseatSupply"),
        @SerializedName("cylonOutOfInkOverride")
        CYLON_OUT_OF_INK_OVERRIDE("cylonOutOfInkOverride"),
        @SerializedName("cylonUserBeliveGenuineSupply")
        CYLON_USER_BELIVE_GENUINE_SUPPLY("cylonUserBeliveGenuineSupply"),
        @SerializedName("cylonSecureUpgradeDetected")
        CYLON_SECURE_UPGRADE_DETECTED("cylonSecureUpgradeDetected"),
        @SerializedName("cylonSecureUpgradeSuccess")
        CYLON_SECURE_UPGRADE_SUCCESS("cylonSecureUpgradeSuccess"),
        @SerializedName("cylonEmptyOverride")
        CYLON_EMPTY_OVERRIDE("cylonEmptyOverride"),
        @SerializedName("cylonEmptyOverrideWarningInfoNonhp")
        CYLON_EMPTY_OVERRIDE_WARNING_INFO_NONHP("cylonEmptyOverrideWarningInfoNonhp"),
        @SerializedName("cylonEmptyOverrideWarningInfoEmptySupply")
        CYLON_EMPTY_OVERRIDE_WARNING_INFO_EMPTY_SUPPLY("cylonEmptyOverrideWarningInfoEmptySupply"),
        @SerializedName("cylonEmptyOverrideWarrantyConfirmation")
        CYLON_EMPTY_OVERRIDE_WARRANTY_CONFIRMATION("cylonEmptyOverrideWarrantyConfirmation"),
        @SerializedName("cylonEmptyOverrideFinalConfirmation")
        CYLON_EMPTY_OVERRIDE_FINAL_CONFIRMATION("cylonEmptyOverrideFinalConfirmation"),
        @SerializedName("cylonEmptyOverrideFinished")
        CYLON_EMPTY_OVERRIDE_FINISHED("cylonEmptyOverrideFinished"),
        @SerializedName("cylonNotPresentSupply")
        CYLON_NOT_PRESENT_SUPPLY("cylonNotPresentSupply"),
        @SerializedName("load")
        LOAD("load"),
        @SerializedName("unload")
        UNLOAD("unload"),
        @SerializedName("feed")
        FEED("feed"),
        @SerializedName("formFeedAndCut")
        FORM_FEED_AND_CUT("formFeedAndCut"),
        @SerializedName("askMediaInfo")
        ASK_MEDIA_INFO("askMediaInfo"),
        @SerializedName("skewHandling")
        SKEW_HANDLING("skewHandling"),
        @SerializedName("manualRemove")
        MANUAL_REMOVE("manualRemove"),
        @SerializedName("coreAttached")
        CORE_ATTACHED("coreAttached"),
        @SerializedName("rollSensitiveKeepCoverOpen")
        ROLL_SENSITIVE_KEEP_COVER_OPEN("rollSensitiveKeepCoverOpen"),
        @SerializedName("selectInput")
        SELECT_INPUT("selectInput"),
        @SerializedName("incoming")
        INCOMING("incoming"),
        @SerializedName("connecting")
        CONNECTING("connecting"),
        @SerializedName("receiving")
        RECEIVING("receiving"),
        @SerializedName("success")
        SUCCESS("success"),
        @SerializedName("partialSuccess")
        PARTIAL_SUCCESS("partialSuccess"),
        @SerializedName("printing")
        PRINTING("printing"),
        @SerializedName("scanning")
        SCANNING("scanning"),
        @SerializedName("dialing")
        DIALING("dialing"),
        @SerializedName("sending")
        SENDING("sending"),
        @SerializedName("redialSingleDest")
        REDIAL_SINGLE_DEST("redialSingleDest"),
        @SerializedName("redialMultiDest")
        REDIAL_MULTI_DEST("redialMultiDest"),
        @SerializedName("scheduledFax")
        SCHEDULED_FAX("scheduledFax"),
        @SerializedName("faxSendFail")
        FAX_SEND_FAIL("faxSendFail"),
        @SerializedName("faxReceiveFail")
        FAX_RECEIVE_FAIL("faxReceiveFail"),
        @SerializedName("callBlocked")
        CALL_BLOCKED("callBlocked"),
        @SerializedName("veryLowContinueOption")
        VERY_LOW_CONTINUE_OPTION("veryLowContinueOption"),
        @SerializedName("communicationFailure")
        COMMUNICATION_FAILURE("communicationFailure"),
        @SerializedName("manualPurgeWarningSyringe")
        MANUAL_PURGE_WARNING_SYRINGE("manualPurgeWarningSyringe"),
        @SerializedName("manualPurgeWaitDuringPurge")
        MANUAL_PURGE_WAIT_DURING_PURGE("manualPurgeWaitDuringPurge"),
        @SerializedName("manualPurgeCheckPurgeResult")
        MANUAL_PURGE_CHECK_PURGE_RESULT("manualPurgeCheckPurgeResult"),
        @SerializedName("manualPurgeConnectSyringe")
        MANUAL_PURGE_CONNECT_SYRINGE("manualPurgeConnectSyringe"),
        @SerializedName("manualPurgeVerifySyringeTinted")
        MANUAL_PURGE_VERIFY_SYRINGE_TINTED("manualPurgeVerifySyringeTinted"),
        @SerializedName("manualPurgeVerifySyringeTintedNoRetry")
        MANUAL_PURGE_VERIFY_SYRINGE_TINTED_NO_RETRY("manualPurgeVerifySyringeTintedNoRetry"),
        @SerializedName("manualPurgeRemoveSyringeFromIt")
        MANUAL_PURGE_REMOVE_SYRINGE_FROM_IT("manualPurgeRemoveSyringeFromIt"),
        @SerializedName("veryLowPromptAgain")
        VERY_LOW_PROMPT_AGAIN("veryLowPromptAgain"),
        @SerializedName("warning")
        WARNING("warning"),
        @SerializedName("aligning")
        ALIGNING("aligning"),
        @SerializedName("processing")
        PROCESSING("processing"),
        @SerializedName("waitUserConfirmation")
        WAIT_USER_CONFIRMATION("waitUserConfirmation"),
        @SerializedName("servicingBeginMCReplacement")
        SERVICING_BEGIN_MC_REPLACEMENT("servicingBeginMCReplacement"),
        @SerializedName("waitForMCExtraction")
        WAIT_FOR_MC_EXTRACTION("waitForMCExtraction"),
        @SerializedName("waitForMCInsertion")
        WAIT_FOR_MC_INSERTION("waitForMCInsertion"),
        @SerializedName("servicingEndMCReplacement")
        SERVICING_END_MC_REPLACEMENT("servicingEndMCReplacement"),
        @SerializedName("diagnosticsScreen")
        DIAGNOSTICS_SCREEN("diagnosticsScreen"),
        @SerializedName("simpleMessageScreen")
        SIMPLE_MESSAGE_SCREEN("simpleMessageScreen"),
        @SerializedName("actionOngoingScreen")
        ACTION_ONGOING_SCREEN("actionOngoingScreen"),
        @SerializedName("stepsScreen")
        STEPS_SCREEN("stepsScreen"),
        @SerializedName("resultScreen")
        RESULT_SCREEN("resultScreen"),
        @SerializedName("clearMispickJamInTray")
        CLEAR_MISPICK_JAM_IN_TRAY("clearMispickJamInTray"),
        @SerializedName("clearJamInsidePrinter")
        CLEAR_JAM_INSIDE_PRINTER("clearJamInsidePrinter"),
        @SerializedName("clearJamInOutputArea")
        CLEAR_JAM_IN_OUTPUT_AREA("clearJamInOutputArea"),
        @SerializedName("askConfigurationInfo")
        ASK_CONFIGURATION_INFO("askConfigurationInfo"),
        @SerializedName("executeLiquidDispenserPurge")
        EXECUTE_LIQUID_DISPENSER_PURGE("executeLiquidDispenserPurge"),
        @SerializedName("windingHandling")
        WINDING_HANDLING("windingHandling"),
        @SerializedName("accesorryHandling")
        ACCESORRY_HANDLING("accesorryHandling"),
        @SerializedName("servicePersonnelDisclaimer")
        SERVICE_PERSONNEL_DISCLAIMER("servicePersonnelDisclaimer"),
        @SerializedName("mediaReleased")
        MEDIA_RELEASED("mediaReleased"),
        @SerializedName("eorSubstrateJamDetected")
        EOR_SUBSTRATE_JAM_DETECTED("eorSubstrateJamDetected"),
        @SerializedName("eorSubstrateCoreAttached")
        EOR_SUBSTRATE_CORE_ATTACHED("eorSubstrateCoreAttached"),
        @SerializedName("eorSubstrateCoreDetached")
        EOR_SUBSTRATE_CORE_DETACHED("eorSubstrateCoreDetached"),
        @SerializedName("userIntervention")
        USER_INTERVENTION("userIntervention"),
        @SerializedName("resumingLoadInfo")
        RESUMING_LOAD_INFO("resumingLoadInfo"),
        @SerializedName("drawerBatchLoadSuccess")
        DRAWER_BATCH_LOAD_SUCCESS("drawerBatchLoadSuccess"),
        @SerializedName("drawerBatchLoadDetails")
        DRAWER_BATCH_LOAD_DETAILS("drawerBatchLoadDetails"),
        @SerializedName("useAnotherDestination")
        USE_ANOTHER_DESTINATION("useAnotherDestination"),
        @SerializedName("jamResolvedSuccessfully")
        JAM_RESOLVED_SUCCESSFULLY("jamResolvedSuccessfully"),
        @SerializedName("jamChecking")
        JAM_CHECKING("jamChecking"),
        @SerializedName("scanAxisJamHowToSolve")
        SCAN_AXIS_JAM_HOW_TO_SOLVE("scanAxisJamHowToSolve"),
        @SerializedName("drawerJamHowToSolve")
        DRAWER_JAM_HOW_TO_SOLVE("drawerJamHowToSolve"),
        @SerializedName("cleanOutJamHowToSolve")
        CLEAN_OUT_JAM_HOW_TO_SOLVE("cleanOutJamHowToSolve"),
        @SerializedName("scanAxisJamModal")
        SCAN_AXIS_JAM_MODAL("scanAxisJamModal"),
        @SerializedName("drawerJamModal")
        DRAWER_JAM_MODAL("drawerJamModal"),
        @SerializedName("cleanOutJamModal")
        CLEAN_OUT_JAM_MODAL("cleanOutJamModal"),
        @SerializedName("colorFaxNotSupported")
        COLOR_FAX_NOT_SUPPORTED("colorFaxNotSupported"),
        @SerializedName("offHookDialing")
        OFF_HOOK_DIALING("offHookDialing"),
        @SerializedName("lowMemory")
        LOW_MEMORY("lowMemory"),
        @SerializedName("inputLockedBySmartTur")
        INPUT_LOCKED_BY_SMART_TUR("inputLockedBySmartTur"),
        @SerializedName("mediaCannotBeCut")
        MEDIA_CANNOT_BE_CUT("mediaCannotBeCut"),
        @SerializedName("gremlinservicingscreen")
        GREMLINSERVICINGSCREEN("gremlinservicingscreen"),
        @SerializedName("attendedMode")
        ATTENDED_MODE("attendedMode"),
        @SerializedName("drawerOpenWarning")
        DRAWER_OPEN_WARNING("drawerOpenWarning"),
        @SerializedName("waitRemovePrintMedia")
        WAIT_REMOVE_PRINT_MEDIA("waitRemovePrintMedia"),
        @SerializedName("endJob")
        END_JOB("endJob"),
        @SerializedName("none")
        NONE("none"),
        @SerializedName("calibratingMediaAdvance")
        CALIBRATING_MEDIA_ADVANCE("calibratingMediaAdvance"),
        @SerializedName("mediaAdvanceCalibrationNotPossible")
        MEDIA_ADVANCE_CALIBRATION_NOT_POSSIBLE("mediaAdvanceCalibrationNotPossible"),
        @SerializedName("clearJamInSecondExitOrDuplexPath")
        CLEAR_JAM_IN_SECOND_EXIT_OR_DUPLEX_PATH("clearJamInSecondExitOrDuplexPath"),
        @SerializedName("removeMediaFromTray1")
        REMOVE_MEDIA_FROM_TRAY_1("removeMediaFromTray1"),
        @SerializedName("detachLct")
        DETACH_LCT("detachLct"),
        @SerializedName("attachLct")
        ATTACH_LCT("attachLct"),
        @SerializedName("openRightDoor")
        OPEN_RIGHT_DOOR("openRightDoor"),
        @SerializedName("clearJamAndCloseRightDoor")
        CLEAR_JAM_AND_CLOSE_RIGHT_DOOR("clearJamAndCloseRightDoor"),
        @SerializedName("openDcfDoor")
        OPEN_DCF_DOOR("openDcfDoor"),
        @SerializedName("clearJamAndCloseDcfDoor")
        CLEAR_JAM_AND_CLOSE_DCF_DOOR("clearJamAndCloseDcfDoor"),
        @SerializedName("openTray2")
        OPEN_TRAY_2("openTray2"),
        @SerializedName("clearJamAndCloseTray2")
        CLEAR_JAM_AND_CLOSE_TRAY_2("clearJamAndCloseTray2"),
        @SerializedName("openTray3")
        OPEN_TRAY_3("openTray3"),
        @SerializedName("clearJamAndCloseTray3")
        CLEAR_JAM_AND_CLOSE_TRAY_3("clearJamAndCloseTray3"),
        @SerializedName("openTray4")
        OPEN_TRAY_4("openTray4"),
        @SerializedName("clearJamAndCloseTray4")
        CLEAR_JAM_AND_CLOSE_TRAY_4("clearJamAndCloseTray4"),
        @SerializedName("openTray5")
        OPEN_TRAY_5("openTray5"),
        @SerializedName("clearJamAndCloseTray5")
        CLEAR_JAM_AND_CLOSE_TRAY_5("clearJamAndCloseTray5"),
        @SerializedName("openLctTopDoor")
        OPEN_LCT_TOP_DOOR("openLctTopDoor"),
        @SerializedName("clearJamAndCloseLctTopDoor")
        CLEAR_JAM_AND_CLOSE_LCT_TOP_DOOR("clearJamAndCloseLctTopDoor"),
        @SerializedName("openHcfRightDoor")
        OPEN_HCF_RIGHT_DOOR("openHcfRightDoor"),
        @SerializedName("clearJamAndCloseHcfRightDoor")
        CLEAR_JAM_AND_CLOSE_HCF_RIGHT_DOOR("clearJamAndCloseHcfRightDoor"),
        @SerializedName("openHcf")
        OPEN_HCF("openHcf"),
        @SerializedName("clearJamAndCloseHcf")
        CLEAR_JAM_AND_CLOSE_HCF("clearJamAndCloseHcf"),
        @SerializedName("openInnerFinisherMiddleDoor")
        OPEN_INNER_FINISHER_MIDDLE_DOOR("openInnerFinisherMiddleDoor"),
        @SerializedName("closeInnerFinisherMiddleDoor")
        CLOSE_INNER_FINISHER_MIDDLE_DOOR("closeInnerFinisherMiddleDoor"),
        @SerializedName("openInnerFinisherBridgeCover")
        OPEN_INNER_FINISHER_BRIDGE_COVER("openInnerFinisherBridgeCover"),
        @SerializedName("closeInnerFinisherBridgeCover")
        CLOSE_INNER_FINISHER_BRIDGE_COVER("closeInnerFinisherBridgeCover"),
        @SerializedName("openInnerFinisherTopDoor")
        OPEN_INNER_FINISHER_TOP_DOOR("openInnerFinisherTopDoor"),
        @SerializedName("closeInnerFinisherTopDoor")
        CLOSE_INNER_FINISHER_TOP_DOOR("closeInnerFinisherTopDoor"),
        @SerializedName("openInnerFinisherFrontDoor")
        OPEN_INNER_FINISHER_FRONT_DOOR("openInnerFinisherFrontDoor"),
        @SerializedName("closeInnerFinisherFrontDoor")
        CLOSE_INNER_FINISHER_FRONT_DOOR("closeInnerFinisherFrontDoor"),
        @SerializedName("openFinisherFrontDoorWithPullingStapler")
        OPEN_FINISHER_FRONT_DOOR_WITH_PULLING_STAPLER("openFinisherFrontDoorWithPullingStapler"),
        @SerializedName("closeInnerFinisherFrontDoorWithReattachingStapler")
        CLOSE_INNER_FINISHER_FRONT_DOOR_WITH_REATTACHING_STAPLER("closeInnerFinisherFrontDoorWithReattachingStapler"),
        @SerializedName("closeInnerFinisherMiddleCover")
        CLOSE_INNER_FINISHER_MIDDLE_COVER("closeInnerFinisherMiddleCover"),
        @SerializedName("openInnerFinisherJamDoor")
        OPEN_INNER_FINISHER_JAM_DOOR("openInnerFinisherJamDoor"),
        @SerializedName("removeExitTray1Media")
        REMOVE_EXIT_TRAY_1_MEDIA("removeExitTray1Media"),
        @SerializedName("closeInnerFinisherJamDoor")
        CLOSE_INNER_FINISHER_JAM_DOOR("closeInnerFinisherJamDoor"),
        @SerializedName("openHcfRightDoorAndOpenHcf")
        OPEN_HCF_RIGHT_DOOR_AND_OPEN_HCF("openHcfRightDoorAndOpenHcf"),
        @SerializedName("closeHcfRightDoor")
        CLOSE_HCF_RIGHT_DOOR("closeHcfRightDoor"),
        @SerializedName("clearJamInFuserUnit")
        CLEAR_JAM_IN_FUSER_UNIT("clearJamInFuserUnit"),
        @SerializedName("powerOff")
        POWER_OFF("powerOff"),
        @SerializedName("clearJobsInOutputArea")
        CLEAR_JOBS_IN_OUTPUT_AREA("clearJobsInOutputArea"),
        @SerializedName("userInterventionToAttach")
        USER_INTERVENTION_TO_ATTACH("userInterventionToAttach"),
        @SerializedName("requestToRemoveWhiteCartridge")
        REQUEST_TO_REMOVE_WHITE_CARTRIDGE("requestToRemoveWhiteCartridge"),
        @SerializedName("installationConfirmation")
        INSTALLATION_CONFIRMATION("installationConfirmation"),
        @SerializedName("installing")
        INSTALLING("installing"),
        @SerializedName("installationFails")
        INSTALLATION_FAILS("installationFails"),
        @SerializedName("installationSuccess")
        INSTALLATION_SUCCESS("installationSuccess"),
        @SerializedName("uninstallationConfirmation")
        UNINSTALLATION_CONFIRMATION("uninstallationConfirmation"),
        @SerializedName("uninstallationSuccess")
        UNINSTALLATION_SUCCESS("uninstallationSuccess"),
        @SerializedName("configureOutput")
        CONFIGURE_OUTPUT("configureOutput"),
        @SerializedName("configureFoldingStyle")
        CONFIGURE_FOLDING_STYLE("configureFoldingStyle"),
        @SerializedName("updatingFirmware")
        UPDATING_FIRMWARE("updatingFirmware"),
        @SerializedName("moveFrontEdgeManuallyToCutLine")
        MOVE_FRONT_EDGE_MANUALLY_TO_CUT_LINE("moveFrontEdgeManuallyToCutLine"),
        @SerializedName("selectPrinthead")
        SELECT_PRINTHEAD("selectPrinthead");
        private final String value;
        private final static Map<String, Progress.FlowSteps> CONSTANTS = new HashMap<String, Progress.FlowSteps>();

        static {
            for (Progress.FlowSteps c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        FlowSteps(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Progress.FlowSteps fromValue(String value) {
            Progress.FlowSteps constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
