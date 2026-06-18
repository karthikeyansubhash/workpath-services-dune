// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.joblet.utils;

/**
 * 
 * JobProgressConstants
 */
public final class JobProgressConstants {

    /**
     * Job ProgressLog Tag
     */

    public static final String TITLE = "Title";

    public static final String ICON = "Icon";

    public static final String SCAN_NEXT_PAGE = "SCAN_NEXT_PAGE";

    public static final String JOB_PROGRESS_SCAN_CONFIRM_ACTIVITY = "JOB_PROGRESS_SCAN_CONFIRM_ACTIVITY";

    public static final String LOG_TAG = "Joblet";

    public static final String STATUS_LOG_TAG = "StatusBarJob";

    public static final String NEW_LINE = "<br />";

    public static final String HELD = "Held";

    public static final String SYMBOL_PERCENTAGE = "%";

    public static final String SYMBOL_FORWARDSLASH = "/";

    public static final String JOB_ID_SUCCESS = "JOB_ID_SUCCESS";

    /**
     * Job ProgressActivity Option Tag
     */
    public static final String JOB_PROGRESS_OPTIONS_ACTIVITY = "JOB_PROGRESS_OPTIONS_ACTIVITY";

    /**
     * Service Scope
     */
    public static final String SERVICE_SCOPE = "ServiceScope";

    /**
     * Job ProgressParam
     */
    public static final String JOB_PROGRESS_PARAM = "JobProgressParam";

    /**
     * PREVIEW_BUILDJOB_COMPLETED
     */
    public static final String PREVIEW_BUILDJOB_COMPLETED = "PREVIEW_BUILDJOB_COMPLETED";

    /**
     * Job ProgressIntent Service
     */
    public static final String JOB_PROGRESS_INTENT_SERVICE = "JobProgressIntentService";

    /**
     * Job ProgressBroadcast Receiver i.e. Communication Job ProgressActivity ->
     * Job ProgressIntent Service
     */
    public static final String JOB_PROGRESS_ACTIVITY_BR_ACTION = "JobProgressA2SBroadcastReceiver";

    /**
     * Job ProgressIntentService Broadcast Receiver i.e. Communication Job
     * Monitor Intent Service -> Job ProgressActivity
     */
    public static final String JOB_PROGRESS_INTENT_SERVICE_BR_ACTION = "JobProgressS2ABroadcastReceiver";

    /**
     * Job ProgressIntent Service broadcast to informing clients as Job Progress
     * Service started.
     */
    public static final String JOB_PROGRESS_INTENT_SERVICE_STARTED = "JobProgressIntentServiceStarted";

    /**
     * Job Created Status to know what is Job ID
     */
    public static final String JOB_CREATED_STATUS_BR_ACTION = "JobCreatedStatusBroadcastReceiver";

    /**
     * Next Page broadcast receiver action string
     */
    public static final String JOB_NEXT_PAGE_BR_ACTION = "NextPageBroadcastReceiver";

    /**
     * Graceful complete broadcast receiver action string
     */
    public static final String JOB_GRACEFUL_COMPLETE_BR_ACTION = "JobGracefulCompleteBroadcastReceiver";

    /**
     * Job Cancel broadcast receiver action string
     */
    public static final String JOB_CANCEL_BR_ACTION = "JobCancelBroadcastReceiver";

    /**
     * Job Hold broadcast receiver action string
     */
    public static final String JOB_HOLD_BR_ACTION = "JobHoldBroadcastReceiver";

    /**
     * Job Promote broadcast receiver action string
     */
    public static final String JOB_PROMOTE_BR_ACTION = "JobPromoteBroadcastReceiver";

    /**
     * Job id to know what is Job ID
     */
    public static final String JOB_ID = "Job_Id";

    /**
     * Job Created Error Reason to know the error reason
     */
    public static final String JOB_ERROR_REASON = "JobErrorReason";

    /**
     * Result Code
     */
    public static final String JOB_RESULT_CODE = "JobResultCode";

    public static final String JOB_PROGRESS_COMPLETED_JOB_IDS = "JOB_PROGRESS_COMPLETED_JOB_IDS";

    public static final String JOB_PROGRESS_BR_ACTION_NAME = "JOB_PROGRESS_BR_ACTION_NAME";

    public static final String JOB_PROGRESS_BR_TYPE = "JOB_PROGRESS_BR_TYPE";

    public static final String JOB_ID_STATUS = "JOB_ID_STATUS";

    public static final String JOB_PROGRESS_BR_COMPLETED_JOB_IDS = "JOB_PROGRESS_BR_COMPLETED_JOB_IDS";

    public static final String COMMA = ";";

    public static final String EMPTY_STRING = "  ";

    /** BELOW values to be moved to Some other constants later */
    public static final String CUSTOM_SIZE_NAME = "CustomSizeName";

    public static final String CUSTOM_SIZE_X = "CustomSizeX";

    public static final String CUSTOM_SIZE_Y = "CustomSizeY";

    public static final String CUSTOM_SIZE = "CustomSize";

    public static final String CUSTOM_SIZE_ACTION = "CustomSizeAction";

    public static final String CUSTOM_SIZE_SAVE = "CustomSizeSave";

    public static final String CUSTOM_SIZE_RETRIVE = "CustomSizeRetrive";

    public static final String DOT = ".";

    public static final String JOB_PROGRESS_PREVIEW_INTENT_SERVICE = "JobProgressPreviewIntentService";

    /** Next Page Result Result Code */
    public static final int JOB_NEXT_PAGE_RESULT_CODE = 1;

    /** Graceful Complete Result Result Code */
    public static final int JOB_GRACEFUL_COMPLETE_RESULT_CODE = 2;

    /** Job Cancel Result Result Code */
    public static final int JOB_CANCEL_RESULT_CODE = 3;

    /** Job Created Status Result Result Code */
    public static final int JOB_CREATED_STATUS_RESULT_CODE = 4;

    /** Job Completed & Successful Result Result Code */
    public static final int JOB_COMPLETE_SUCCESSFUL_RESULT_CODE = 5;

    /** Job Completed & Canceling Result Result Code */
    public static final int JOB_COMPLETE_CANCELING_RESULT_CODE = 6;

    /** Job Completed & Cancelled/Error/Rejected Result Result Code */
    public static final int JOB_COMPLETE_CANCELED_ERROR_REJECTED_RESULT_CODE = 7;

    /** Job Completed & Cancelled/Error/Rejected Result Result Code */
    public static final int JOB_COMPLETE_CANCELED_ERROR_RESULT_CODE = 7;

    /** Job INtent Service shutdown Result Result Code */
    public static final int JOB_INTENT_SERVICE_SHUT_DOWN_RESULT_CODE = 8;

    /** Job TASK PROGRESS SCANNING Result Code */
    public static final int JOB_TASK_PROGRESS_SCANNING_RESULT_CODE = 9;

    /** Job TASK PROGRESS IMAGE TRANSFERRING Result Code */
    public static final int JOB_TASK_PROGRESS_IMAGE_TRANSFERRING_RESULT_CODE = 10;

    /** Job TASK PROGRESS SCANNING Result Code */
    public static final int JOB_TASK_PROGRESS_PRINTING_RESULT_CODE = 11;

    /** JOB_FAILURE Result Code */
    public static final int JOB_FAILURE_RESULT_CODE = 12;

    /** JOB_ID Result Code */
    public static final int JOB_ID_RESULT_CODE = 13;

    /** JOB_ID Result Code */
    public static final int JOB_SHUTDOWN_RESULT_CODE = 14;

    public static final int JOB_PROCESSING_RESULT_CODE = 15;

    public static final int JOB_IMAGE_PROCESSING_RESULT_CODE = 16;

    public static final int JOB_DIALING_RESULT_CODE = 17;

    public static final int JOB_RECEIVING_RESULT_CODE = 18;

    public static final int JOB_WAITING_TO_PRINT_RESULT_CODE = 19;

    public static final int JOB_OVERWRITING_RESULT_CODE = 20;

    public static final int JOB_CONNECTING_RESULT_CODE = 21;

    public static final int JOB_WAITING_FOR_LINE_RESULT_CODE = 22;

    public static final int JOB_PAUSE_RESULT_CODE = 23;

    public static final int JOB_INTERRUPTED_RESULT_CODE = 24;

    public static final int JOB_NUMBER_OF_COPIES_COUNT = 25;

    public static final int JOB_IS_COLLATED = 26;

    public static final int JOB_HELD_SAMPLE_REQUESTED_RESULT_CODE = 27;

    public static final int JOB_HELD_REQUIRED_RESOURCE_RESULT_CODE = 28;

    public static final int JOB_HELD_DEFERRED_PRINT_RESULT_CODE = 29;

    public static final int JOB_DESTINATION_LIST_RESULT_CODE = 30;

    public static final int JOB_FAX_DELAY_RESULT_CODE = 31;

    public static final int FAX_DELAY_TIME_RESULT_CODE = 32;

    public static final int JOB_HELD_USER_REQUEST_RESULT_CODE = 33;

    public static final int JOB_SCANNING_RESULT_CODE = 34;

    public static final int JOB_PRINTING_RESULT_CODE = 35;

    public static final int JOB_TRANSFERRING_RESULT_CODE = 36;

    public static final int JOB_PROGRESS_SCAN_CONFIRM_REQUEST_RESPONSE_CODE = 37;

    public static final int JOB_HELD_BY_SYSTEM_RESULT_CODE = 38;

    public static final int JOB_DETAIL_REQUEST_CODE = 39;

    public static final int JOB_STATUS_RESULT_CODE = 40;

    public static final int JOB_DELETE_REQUEST_CODE = 41;

    public static final int JOB_SCAN_SETTINGS_DATA_CODE = 42;

    public static final int JOB_PROGRESS_FILE_NAME_DATA_CODE = 43;

    public static final int JOB_DESTINATION_TASK_COMPLETE = 44;

    public static final int JOB_DESTINATION_TASK_INPROGRESS = 45;

    public static final int JOB_DESTINATION_TASK_FAILED = 46;

    public static final int JOB_PREVIEW_SEGMENT_CODE = 47;

    public static final int JOB_CREATED_STATUS = 48;

    public static final int JOB_ERROR_COMPLETED = 49;

    public static final int JOB_SUCCESSFULLY_COMPLETED = 50;

    public static final int JOB_PROGRESS_UI_FINISH = 51;

    public static final int JOB_PROGRESS_DESTINATION_DELIVERED_COUNT = 52;

    public static final int JOB_PROGRESS_COMPLETED_WITH_JOBID = 53;

    public static final int JOB_PROGRESS_JOBID = 54;

    public static final int EMAIL_DESTN = 55;

    public static final int FTP_DESTN = 56;

    public static final int SMB_DESTN = 57;

    public static final int SSIP_DESTN = 58;

    public static final int STORAGE_DESTN = 59;

    public static final int FAX_DESTN = 60;

    public static final int JOB_NEXT_SEGMENT_RESULT_CODE_BUILDJOB = 61;

    public static final int JOB_PROGRESS_CLOSE_UNSPECIFIED_REASON = 62;

    public static final int PREVIEW_BUILD_JOB = 63;
    
    public static final int OHD_JOB_STOPPED_ACTION = 64;

    public static final String NOT_IMPLEMENTED = "NOT_IMPLEMENTED";

    /*TGIF CONSTANTS */
    public static final String JOBPROGRESS_CLOSE = "JobProgress_Close";

    public static final String JOBPROGRESS_DETAIL = "JobProgress_Detail";

    public static final String JOBPROGRESS_CANCEL = "JobProgress_Cancel";

    public static final String JOBPROGRESS_TEXT_SCANNING = "JobProgress_ScanningText";

    public static final String JOBPROGRESS_TEXT_PAGES = "JobProgress_PagesText";

    public static final String JOBPROGRESS_TEXT_SCANNING_COMPLETED = "JobProgress_ScanningCompletedText";

    public static final String JOBPROGRESS_SCANNING_PROGRESS = "JobProgress_ScanningProgress";

    public static final String JOBPROGRESS_SENDING_PROGRESS = "JobProgress_SendingProgress";

    public static final String JOBPROGRESS_TEXT_SENDING = "JobProgress_SendingText";

    public static final String JOBPROGRESS_TEXT_COMPLETED = "JobProgress_CompletedText";

    public static final String JOBPROGRESS_TEXT_TOTAL = "JobProgress_TotalText";

    public static final String JOBPROGRESS_TOTAL_COMPLETED_PROGRESS = "JobProgress_TotalCompletedProgress";

    public static final String JOBPROGRESS_PROGRESSBAR = "JobProgress_ProgressBar";

    public static final String JOBPROGRESS_PREVIEW_SECTION = "JobProgress_PreviewSection";

    public static final String JOBPROGRESS_SEND_FEEDBACK = "JobProgress_SendFeedback";

    public static final String JOBPROGRESS_SCAN_SETTINGS = "JobProgress_ScanSettings";

    public static final String JOBPROGRESS_DESTINATION_ICON = "Jobprogress_DestinationIcon";

    public static final String JOBPROGRESS_DESTINATION_VALUE = "Jobprogress_DestinationValue";

    public static final String JOBPROGRESS_DESTINATION_CONFIRM = "Jobprogress_DestinationConfirm";

    public static final String JOBPROGRESS_FILENAME = "Jobprogress_Filename";

    public static final String DELETE_CUSTOM_SIZE = "DELETE_CUSTOM_SIZE";

    public static final String EDIT_CUSTOM_SIZE = "EDIT_CUSTOM_SIZE";

    public static final String PREVIOUS_CUSTOM_SIZE_NAME = "PREVIOUS_CUSTOM_SIZE_NAME";

    public static final String TASK_ID = "TASK_ID";

    public static final String JOBPROGRESS_PREVIEW_READY = "JOBPROGRESS_PREVIEW_READY";

    public static final String JOBPROGRESS_SCAN_ANOTHER_PAGE = "JOBPROGRESS_SCAN_ANOTHER_PAGE";

    public static final String JOBPROGRESS_SCAN_ANOTHER_IMAGE = "JOBPROGRESS_SCAN_ANOTHER_IMAGE";

    public static final String JOBPROGRESS_SCAN_ANOTHER_TEXT = "JOBPROGRESS_SCAN_ANOTHER_TEXT";

    /**
     * 
     * DESTINATION_TYPES
     */
    public enum DESTINATION_TYPES{
        EMAIL, SMB, FTP, FAX, BOX, PC, USB
    }

    /**
     * 
     * TaskStatusType
     */
    public enum TaskStatusType{
        COMPLETED, FAILED, INPROGRESS
    }

    public static final String FAX_STANDARD = "Standard";

    public static final String FAX_FINE = "Fine";

    public static final String FAX_SUPERFINE = "Super_Fine";

    public static final String FAX_ULTRAFINE = "Ultra_Fine";

    public static final String COLOR_MONO = "Mono";

    public static final String COLOR_GRAY = "Gray";

    public static final String COLOR_COLOR = "Color";

    public static final String PLEX_SIMPLEX = "1_Sided";

    public static final String PLEX_DUPLEX_LEB = "2_Sided_Book";

    public static final String PLEX_DUPLEX_SEB = "2_Sided_Calendar";

    public static final String PLEX_DUPLEX_BOOK = "2_Sided_Book";

    public static final String PLEX_DUPLEX_CALENDER = "2_Sided_Calendar";

    public static final String DPI_100 = "100";

    public static final String DPI_200 = "200";

    public static final String DPI_300 = "300";

    public static final String DPI_600 = "600";

    public static final String FILEFORMAT_JPEG = "image/jpeg";

    public static final String FILEFORMAT_PDF = "application/pdf";

    public static final String FILEFORMAT_TIFF = "image/tiff";

    public static final String FILEFORMAT_XPS = "application/vnd.ms-xpsdocument";

    public static final String USB_TYPE = "usb";

    public static final int NOTIFICATION_ID = 1503405;

    public static final int FAXLINE_NOTIFICATION_ID = 1505609;

    public static final int FAXLINE_TWO_NOTIFICATION_ID = 1505906;

    public static final String EMPTY_SPACE = " ";

    public static final String BACKSLASH = "/";

    public static final String ACTION_SYSTEM_TIMEOUT_ENABLE = "net.xoaframework.intent.action.SYSTEM_TIMEOUT_ENABLE";

    public static final String SERVICENAME = "servicename";

    public static final String JOBPROGRESS_SERVICE = "JOBPROGRESS_SERVICE";

    public static final String CONSTANTS_SYSTEM_TIMEOUT = "systemtimeout";

    public static final String COLON = ":";

    public static final String ISOSTRING = "ISOTime: ";

    public static final String NEXTLINE = "\n";
    
    public static final String ACTION_AUTOLOGOUT_DISABLE="net.xoaframework.intent.action.AUTOLOGOUT_DISABLE";
    
    public static final String ACTION_AUTOLOGOUT_ENABLE="net.xoaframework.intent.action.AUTOLOGOUT_ENABLE";

    public static final String BUILD_COMPLETE = "BUILD_COMPLETE";

}
