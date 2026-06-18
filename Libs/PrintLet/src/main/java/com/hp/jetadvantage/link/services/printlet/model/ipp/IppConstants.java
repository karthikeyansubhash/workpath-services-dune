// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.model.ipp;

import java.nio.charset.Charset;
import java.util.Locale;

/***
 * Constants and enumerations used in the OXPd IPP classes. Mostly translated from the CUPS library's ipp.h.
 * @hide
 */
public class IppConstants
{
    /** UTF-8 character set */
    @SuppressWarnings("WeakerAccess")
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    /** Default character set */
    @SuppressWarnings("WeakerAccess")
    public static final Charset DEFAULT_CHARSET = UTF_8;
    /** Default IPP version major */
    @SuppressWarnings("WeakerAccess")
    public static final int DEFAULT_IPP_VERSION_MAJOR = 2;
    /** Default IPP version minor */
    @SuppressWarnings("WeakerAccess")
    public static final int DEFAULT_IPP_VERSION_MINOR = 0;
    /** Default Locale */
    @SuppressWarnings("WeakerAccess")
    public static final Locale DEFAULT_LOCALE = Locale.US;

    /** attributes-charset. Attribute defining the character set in which attributes are encoded */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__CHARSET = "attributes-charset";

    /** attributes-natural-language. Attribute specifying the default language of attribute text values */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__NATURAL_LANGUAGE = "attributes-natural-language";

    /** IPP mime type */
    @SuppressWarnings("WeakerAccess")
    public static final String MIME_TYPE_IPP = "application/ipp";

    /** document-format */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__DOCUMENT_FORMAT = "document-format";

    /** printer-uri */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__PRINTER_URI = "printer-uri";

    /***
     * print-uri-cookie. An opaque value passed by the client to the printer to be used to
     * populate a standard HTTP Cookie header on the HTTP request to the URI. The concatenation
     * of the provided sets must be a validly formed cookie (HTTP Cookie header syntax).
     * If this attribute is invalid or exceeds 4 sets, the device returns “client-error-bad-request,
     * putting the provided “print-uri-cookie in the Unsupported Attributes Group of the response.
     */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__PRINT_URI_COOKIE = "print-uri-cookie";

    /***
     * print-uri-password. The printer uses the password in addition to the user name for
     * HTTP Basic Authentication credentials when fetching the URI specified in this request.
     * HTTPS is recommended for both the Print-URI operation and the specified URI to ensure
     * the credentials are encrypted on the network.
     */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__PRINT_URI_PASSWORD = "print-uri-password";

    /***
     * print-uri-user-name. The printer uses this user name for HTTP Basic Authentication
     * credentials when fetching the URI specified in this request. HTTPS is recommended
     * for both the Print-URI operation and the specified URI to ensure the credentials
     * are encrypted on the network.
     */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__PRINT_URI_USERNAME = "print-uri-user-name";

    /** document-uri */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__DOCUMENT_URI = "document-uri";

    /***
     * priority-print-auth-cookie. It is an opaque value passed by the client to the printer
     * to refer to previously authenticated credentials and the mechanism used by the client
     * to authenticate its credentials to the printer is outside of IPP. To be valid, must
     * be equal to the currently valid OXPd UIContextId.
     */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__PRIORITY_PRINT_AUTH_COOKIE = "priority-print-auth-cookie";

    /** job-id */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__JOB_ID = "job-id";

    /** Job UUID */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__JOB_UUID = "job-uuid";

    /**Requested attributes */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__REQUESTED_ATTRIBUTES = "requested-attributes";

    /** Job state */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__JOB_STATE = "job-state";

    /** Job name */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__JOB_NAME = "job-name";

    /** Job originating user */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__JOB_USER = "job-originating-user-name";

    /** Job originating user */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__JOB_TIME_CREATED = "date-time-at-creation";

    /** Job originating user */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__JOB_TIME_COMPLETED = "date-time-at-completed";

    /** Duplexing */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__SIDES = "sides";

    /** Duplexing default */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__SIDES_DEFAULT = "sides-default";

    /** Copies */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__COPIES = "copies";

    /** Copies default */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__COPIES_SUPPORTED = "copies-supported";

    /** Copies default */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__COPIES_DEFAULT = "copies-default";

    /*** Color mode */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__PRINT_COLOR_MODE = "print-color-mode";

    /** Color mode supported */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__PRINT_COLOR_MODE_SUPPORTED = "print-color-mode-supported";

    /** Color mode default */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__PRINT_COLOR_MODE_DEFAULT = "print-color-mode-default";

    /** Scaling mode */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__PRINT_SCALING_MODE = "print-scaling";

    /** Supported auto fit modes */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__PRINT_SCALING_SUPPORTED = "print-scaling-supported";

    /** Scaling mode default */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__PRINT_SCALING_MODE_DEFAULT = "print-scaling-default";

    /** Finishing collection mode */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__FINISHINGS_COL = "finishings-col";

    /** Finishing collection mode */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__FINISHINGS_COL_DEFAULT = "finishings-col-default";

    /** Finishing staple mode */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__FINISHING_TEMPLATE = "finishing-template";

    /** Supported auto fit modes */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__FINISHING_TEMPLATE_SUPPORTED = "finishing-template-supported";

    /** Printer make & model */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__PRINTER_MAKE_AND_MODEL = "printer-make-and-model";

    /** Printer state */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__PRINTER_STATE = "printer-state";

    /** Printer state reasons */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__PRINTER_STATE_REASONS = "printer-state-reasons";

    /** Supported color modes */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__COLOR_SUPPORTED = "color-supported";

    /** Supported print formats */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__DOCUMENT_FORMAT_SUPPORTED = "document-format-supported";

    /** Document format default */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__DOCUMENT_FORMAT_DEFAULT = "document-format-default";

    /** Supported duplexing modes */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__SIDES_SUPPORTED = "sides-supported";

    /** Multiple document handling */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__MULTIPLE_DOCUMENT_HANDLING = "multiple-document-handling";

    /** Job state reasons */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__JOB_STATE_REASONS = "job-state-reasons";

    /** Per-job document access errors */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__JOB_DOCUMENT_ACCESS_ERROR = "job-document-access-error";

    /** Job impressions completed */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__JOB_IMPRESSIONS_COMPLETED = "job-impressions-completed";

    /** Media sheets completed */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__JOB_MEDIA_SHEETS_COMPLETED = "job-media-sheets-completed";

    /** Media ready */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__MEDIA_READY = "media-ready";

    /** Media col ready */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__MEDIA_COL_READY = "media-col-ready";

    /** Media col default */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__MEDIA_COL_DEFAULT = "media-col-default";

    /** Media source supported */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__MEDIA_SOURCE_SUPPORTED = "media-source-supported";

    /** Media source (i.e. tray) */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__MEDIA_SOURCE = "media-source";

    /** Media */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__MEDIA = "media";

    /** Media supported */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__MEDIA_SUPPORTED = "media-supported";

    /** Media default */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__MEDIA_DEFAULT = "media-default";

    /** Media type */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__MEDIA_TYPE = "media-type";

    /** Media type supported */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__MEDIA_TYPE_SUPPORTED = "media-type-supported";

    /** Media type default */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__MEDIA_TYPE_DEFAULT = "media-type-default";

    /** Printer input tray */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__PRINTER_INPUT_TRAY = "printer-input-tray";

    /** Default character set */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_VALUE__CHARSET = "utf-8";

    /** Default language */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_VALUE__NATURAL_LANGUAGE = "en-us";

    /** Collate */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__COLLATE = "collate";

    /** Collate default */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__COLLATE_DEFAULT = "multiple-document-handling-default";

    /** Supported Collate modes */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__COLLATE_SUPPORTED = "multiple-document-handling-supported";

    /** Collate copies */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_VALUE__SEPARATE_DOCUMENT_COLLATED_COPIES = "separate-documents-collated-copies";

    /** Uncollated copies */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_VALUE__SEPARATE_DOCUMENT_UNCOLLATED_COPIES = "separate-documents-uncollated-copies";

    /** Orientation-requested-supported */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__ORIENTATION_REQUESTED_SUPPORTED = "orientation-requested-supported";

    /** Orientation default */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__ORIENTATION_REQUESTED_DEFAULT = "orientation-requested-default";

    /** Content Orientation */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__ORIENTATION_REQUESTED = "orientation-requested";

    /** Print-quality supported */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__PRINT_QUALITY_SUPPORTED = "print-quality-supported";

    /** Print-quality default */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__PRINT_QUALITY_DEFAULT = "print-quality-default";

    /** Print-quality */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__PRINT_QUALITY = "print-quality";

    /** Output-bin-supported */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__OUTPUT_BIN_SUPPORTED = "output-bin-supported";

    /** Output-bin default */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__OUTPUT_BIN_DEFAULT = "output-bin-default";

    /** Output-bin */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__OUTPUT_BIN = "output-bin";

    /** Page-ranges-supported */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__PAGE_RANGES_SUPPORTED = "page-ranges-supported";

    /** Page-ranges */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__PAGE_RANGES = "page-ranges";

    /** Finishings supported */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__FINISHINGS_SUPPORTED = "finishings-supported";

    /** Finishings default */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__FINISHINGS_DEFAULT = "finishings-default";

    /** Finishings */
    @SuppressWarnings("WeakerAccess")
    public static final String IPP_ATTRIBUTE_NAME__FINISHINGS = "finishings";

    @SuppressWarnings("WeakerAccess")
    public static final String[] REQUEST_SET_SUPPORTED_ATTRIBUTES = new String[]{
            IPP_ATTRIBUTE_NAME__PRINTER_STATE,
            IPP_ATTRIBUTE_NAME__PRINTER_STATE_REASONS,
            IPP_ATTRIBUTE_NAME__PRINTER_MAKE_AND_MODEL,
            IPP_ATTRIBUTE_NAME__COLOR_SUPPORTED,
            IPP_ATTRIBUTE_NAME__PRINT_COLOR_MODE_SUPPORTED,
            IPP_ATTRIBUTE_NAME__DOCUMENT_FORMAT_SUPPORTED,
            IPP_ATTRIBUTE_NAME__SIDES_SUPPORTED,
            IPP_ATTRIBUTE_NAME__PRINT_SCALING_SUPPORTED,
            IPP_ATTRIBUTE_NAME__FINISHING_TEMPLATE_SUPPORTED,
            IPP_ATTRIBUTE_NAME__MEDIA_SOURCE_SUPPORTED,
            IPP_ATTRIBUTE_NAME__MEDIA_SUPPORTED,
            IPP_ATTRIBUTE_NAME__MEDIA_TYPE_SUPPORTED,
            IPP_ATTRIBUTE_NAME__COPIES_SUPPORTED,
            IPP_ATTRIBUTE_NAME__COLLATE_SUPPORTED,
            IPP_ATTRIBUTE_NAME__ORIENTATION_REQUESTED_SUPPORTED,
            IPP_ATTRIBUTE_NAME__PRINT_QUALITY_SUPPORTED,
            IPP_ATTRIBUTE_NAME__OUTPUT_BIN_SUPPORTED,
            IPP_ATTRIBUTE_NAME__PAGE_RANGES_SUPPORTED,
            IPP_ATTRIBUTE_NAME__FINISHINGS_SUPPORTED
    };

    @SuppressWarnings("WeakerAccess")
    public static final String[] REQUEST_SET_DEFAULT_ATTRIBUTES = new String[]{
            IPP_ATTRIBUTE_NAME__PRINT_COLOR_MODE_DEFAULT,
            IPP_ATTRIBUTE_NAME__DOCUMENT_FORMAT_DEFAULT,
            IPP_ATTRIBUTE_NAME__SIDES_DEFAULT,
            IPP_ATTRIBUTE_NAME__PRINT_SCALING_MODE_DEFAULT,
            IPP_ATTRIBUTE_NAME__FINISHINGS_COL_DEFAULT,
            IPP_ATTRIBUTE_NAME__MEDIA_DEFAULT,
            IPP_ATTRIBUTE_NAME__MEDIA_READY,
            IPP_ATTRIBUTE_NAME__MEDIA_TYPE_DEFAULT,
            IPP_ATTRIBUTE_NAME__MEDIA_COL_READY,
            IPP_ATTRIBUTE_NAME__MEDIA_COL_DEFAULT,
            IPP_ATTRIBUTE_NAME__PRINTER_INPUT_TRAY,
            IPP_ATTRIBUTE_NAME__COPIES_DEFAULT,
            IPP_ATTRIBUTE_NAME__COLLATE_DEFAULT,
            IPP_ATTRIBUTE_NAME__ORIENTATION_REQUESTED_DEFAULT,
            IPP_ATTRIBUTE_NAME__PRINT_QUALITY_DEFAULT,
            IPP_ATTRIBUTE_NAME__OUTPUT_BIN_DEFAULT,
            IPP_ATTRIBUTE_NAME__FINISHINGS_DEFAULT
    };


    /*
     * Types and structures...
     */

    /***
     * IPP operations
     */
    public enum IppOperation            
    {
        /***
         * Print a single file
         */
        IPP_PRINT_JOB(0x0002),
        /***
         * Print a single URL 
         */
        IPP_PRINT_URI(0x0003),            
        /***
         * Validate job options
         */
        IPP_VALIDATE_JOB(0x0004),            
        /***
         * Create an empty print job
         */
        IPP_CREATE_JOB(0x0005),            
        /***
         * Add a file to a job
         */
        IPP_SEND_DOCUMENT(0x0006),        
        /***
         * Add a URL to a job
         */
        IPP_SEND_URI(0x0007),                
        /***
         * Cancel a job
         */
        IPP_CANCEL_JOB(0x0008),            
        /***
         * Get job attributes
         */
        IPP_GET_JOB_ATTRIBUTES(0x0009),        
        /***
         * Get a list of jobs
         */
        IPP_GET_JOBS(0x000A),                
        /***
         * Get printer attributes
         */
        IPP_GET_PRINTER_ATTRIBUTES(0x000B),        
        /***
         * Hold a job for printing
         */
        IPP_HOLD_JOB(0x000C),                
        /***
         * Release a job for printing
         */
        IPP_RELEASE_JOB(0x000D),            
        /***
         * Reprint a job
         */
        IPP_RESTART_JOB(0x000E),            
        /***
         * Stop a printer
         */
        IPP_PAUSE_PRINTER(0x0010),        
        /***
         * Start a printer
         */
        IPP_RESUME_PRINTER(0x0011),            
        /***
         * Cancel all jobs
         */
        IPP_PURGE_JOBS(0x0012),            
        /***
         * Set printer attributes
         */
        IPP_SET_PRINTER_ATTRIBUTES(0x0013),        
        /***
         * Set job attributes
         */
        IPP_SET_JOB_ATTRIBUTES(0x0014),        
        /***
         * Get supported attribute values
         */
        IPP_GET_PRINTER_SUPPORTED_VALUES(0x0015),    
        /***
         * Create a printer subscription
         */
        IPP_CREATE_PRINTER_SUBSCRIPTION(0x0016),    
        /***
         * Create a job subscription
         */
        IPP_CREATE_JOB_SUBSCRIPTION(0x0017),    
        /***
         * Get subscription attributes
         */
        IPP_GET_SUBSCRIPTION_ATTRIBUTES(0x0018),    
        /***
         * Get list of subscriptions
         */
        IPP_GET_SUBSCRIPTIONS(0x0019),        
        /***
         * Renew a printer subscription
         */
        IPP_RENEW_SUBSCRIPTION(0x001A),        
        /***
         * Cancel a subscription
         */
        IPP_CANCEL_SUBSCRIPTION(0x001B),        
        /***
         * Get notification events
         */
        IPP_GET_NOTIFICATIONS(0x001C),        
        /***
         * Send notification events
         */
        IPP_SEND_NOTIFICATIONS(0x001D),        
        /***
         * Get resource attributes
         */
        IPP_GET_RESOURCE_ATTRIBUTES(0x001E),    
        /***
         * Get resource data
         */
        IPP_GET_RESOURCE_DATA(0x001F),        
        /***
         * Get list of resources
         */
        IPP_GET_RESOURCES(0x0020),        
        /***
         * Get printer support files
         */
        IPP_GET_PRINT_SUPPORT_FILES(0x0021),        
        /***
         * Start a printer
         */
        IPP_ENABLE_PRINTER(0x0022),        
        /***
         * Stop a printer
         */
        IPP_DISABLE_PRINTER(0x0023),    
        /***
         * Stop printer after the current job
         */
        IPP_PAUSE_PRINTER_AFTER_CURRENT_JOB(0x0024),    
        /***
         * Hold new jobs
         */
        IPP_HOLD_NEW_JOBS(0x0025),        
        /***
         * Release new jobs
         */
        IPP_RELEASE_HELD_NEW_JOBS(0x0026),    
        /***
         * Stop a printer
         */
        IPP_DEACTIVATE_PRINTER(0x0027),    
        /***
         * Start a printer
         */
        IPP_ACTIVATE_PRINTER(0x0028),    
        /***
         * Restart a printer
         */
        IPP_RESTART_PRINTER(0x0029),        
        /***
         * Turn a printer off
         */
        IPP_SHUTDOWN_PRINTER(0x002A),    
        /***
         * Turn a printer on
         */
        IPP_STARTUP_PRINTER(0x002B),    
        /***
         * Reprint a job
         */
        IPP_REPROCESS_JOB(0x002C),        
        /***
         * Cancel the current job
         */
        IPP_CANCEL_CURRENT_JOB(0x002D),    
        /***
         * Suspend the current job
         */
        IPP_SUSPEND_CURRENT_JOB(0x002E),    
        /***
         * Resume the current job
         */
        IPP_RESUME_JOB(0x002F),        
        /***
         * Promote a job to print sooner
         */
        IPP_PROMOTE_JOB(0x0030),    
        /***
         * Schedule a job to print after another
         */
        IPP_SCHEDULE_JOB_AFTER(0x0031),    
        /***
         * Cancel-Document
         */
        IPP_CANCEL_DOCUMENT(0x0033),    
        /***
         * Get-Document-Attributes
         */
        IPP_GET_DOCUMENT_ATTRIBUTES(0x0034),    
        /***
         * Get-Documents
         */
        IPP_GET_DOCUMENTS(0x0035),        
        /***
         * Delete-Document
         */
        IPP_DELETE_DOCUMENT(0x0036),    
        /***
         * Set-Document-Attributes
         */
        IPP_SET_DOCUMENT_ATTRIBUTES(0x0037),    
        /***
         * Cancel-Jobs
         */
        IPP_CANCEL_JOBS(0x0038),        
        /***
         * Cancel-My-Jobs
         */
        IPP_CANCEL_MY_JOBS(0x0039),        
        /***
         * Resubmit-Job
         */
        IPP_RESUBMIT_JOB(0x003A),        
        /***
         * Close-Job
         */
        IPP_CLOSE_JOB(0x003B),    
        /***
         * Identify-Printer
         */
        IPP_IDENTIFY_PRINTER(0x003C),        
        /***
         * Validate-Document
         */
        IPP_VALIDATE_DOCUMENT(0x003D),        
        /***
         * Reserved
         */
        IPP_PRIVATE(0x4000),            
        /***
         * OXPd Extension for Priority-Print-Enter
         */
        IPP_PRIORITY_PRINT_ENTER(0x4029),
        /***
         * OXPd Extension for Priority-Print-Exit
         */
        IPP_PRIORITY_PRINT_EXIT(0x402A);

        /** Enum value */
        private short value;

        /**
         * Constructor
         * @param value Value associated with enum
         */
        IppOperation(int value) {
            this.value = (short) value;
        }

        /**
         * Return the value associated with enum
         * @return Value associated with enum
         */
        public short getValue() {
            return value;
        }

        /**
         * Convert an int to its {@link IppOperation} equivalent
         * @param value Value to convert
         * @return {@link IppOperation} equivalent
         */
        @SuppressWarnings("unused")
        public static IppOperation toIppOperation(int value){
            for (IppOperation ippOperation : IppOperation.values())
            {
                if (ippOperation.getValue() == value) return ippOperation;
            }
            throw new IllegalArgumentException("not a valid IppOperation value: " + value);
        }
    }

    /***
     * IPP status codes
     */
    public enum IppStatus
    {
        /***
         * successful-ok
         */
        IPP_OK(0x0000),            
        /***
         * successful-ok-ignored-or-substituted-attributes
         */
        IPP_OK_SUBST(0x0001),                
        /***
         * successful-ok-conflicting-attributes
         */
        IPP_OK_CONFLICT(0x0002),        
        /***
         * successful-ok-ignored-subscriptions
         */
        IPP_OK_IGNORED_SUBSCRIPTIONS(0x0003),        
        /***
         * successful-ok-ignored-notifications
         */
        IPP_OK_IGNORED_NOTIFICATIONS(0x0004),        
        /***
         * successful-ok-too-many-events
         */
        IPP_OK_TOO_MANY_EVENTS(0x0005),        
        /***
         * successful-ok-but-cancel-subscription
         */
        IPP_OK_BUT_CANCEL_SUBSCRIPTION(0x0006),    
        /***
         * successful-ok-events-complete
         */
        IPP_OK_EVENTS_COMPLETE(0x0007),        
        /***
         * redirection-other-site
         */
        IPP_REDIRECTION_OTHER_SITE(0x0200),    
        /***
         * client-error-bad-request
         */
        IPP_BAD_REQUEST(0x0400),        
        /***
         * client-error-forbidden
         */
        IPP_FORBIDDEN(0x0401),            
        /***
         * client-error-not-authenticated
         */
        IPP_NOT_AUTHENTICATED(0x0402),        
        /***
         * client-error-not-authorized
         */
        IPP_NOT_AUTHORIZED(0x0403),            
        /***
         * client-error-not-possible
         */
        IPP_NOT_POSSIBLE(0x0404),            
        /***
         * client-error-timeout
         */
        IPP_TIMEOUT(0x0405),                
        /***
         * client-error-not-found
         */
        IPP_NOT_FOUND(0x0406),            
        /***
         * client-error-gone
         */
        IPP_GONE(0x0407),                
        /***
         * client-error-request-entity-too-large
         */
        IPP_REQUEST_ENTITY(0x0408),            
        /***
         * client-error-request-value-too-long
         */
        IPP_REQUEST_VALUE(0x0409),            
        /***
         * client-error-document-format-not-supported
         */
        IPP_DOCUMENT_FORMAT(0x040A),            
        /***
         * client-error-attributes-or-values-not-supported
         */
        IPP_ATTRIBUTES(0x040B),            
        /***
         * client-error-uri-scheme-not-supported
         */
        IPP_URI_SCHEME(0x040C),            
        /***
         * client-error-charset-not-supported
         */
        IPP_CHARSET(0x040D),                
        /***
         * client-error-conflicting-attributes
         */
        IPP_CONFLICT(0x040E),                
        /***
         * client-error-compression-not-supported
         */
        IPP_COMPRESSION_NOT_SUPPORTED(0x040F),    
        /***
         * client-error-compression-error
         */
        IPP_COMPRESSION_ERROR(0x0410),        
        /***
         * client-error-document-format-error
         */
        IPP_DOCUMENT_FORMAT_ERROR(0x0411),        
        /***
         * client-error-document-access-error
         */
        IPP_DOCUMENT_ACCESS_ERROR(0x0412),        
        /***
         * client-error-attributes-not-settable
         */
        IPP_ATTRIBUTES_NOT_SETTABLE(0x0413),        
        /***
         * client-error-ignored-all-subscriptions
         */
        IPP_IGNORED_ALL_SUBSCRIPTIONS(0x0414),    
        /***
         * client-error-too-many-subscriptions
         */
        IPP_TOO_MANY_SUBSCRIPTIONS(0x0415),        
        /***
         * client-error-ignored-all-notifications
         */
        IPP_IGNORED_ALL_NOTIFICATIONS(0x0416),    
        /***
         * client-error-print-support-file-not-found
         */
        IPP_PRINT_SUPPORT_FILE_NOT_FOUND(0x0417),    
        /***
         * client-error-document-password-error
         */
        IPP_DOCUMENT_PASSWORD_ERROR(0x0418),        
        /***
         * client-error-document-permission-error
         */
        IPP_DOCUMENT_PERMISSION_ERROR(0x0419),    
        /***
         * client-error-document-security-error
         */
        IPP_DOCUMENT_SECURITY_ERROR(0x041A),        
        /***
         * client-error-document-unprintable-error
         */
        IPP_DOCUMENT_UNPRINTABLE_ERROR(0x041B),    

        /***
         * server-error-internal-error
         */
        IPP_INTERNAL_ERROR(0x0500),        
        /***
         * server-error-operation-not-supported
         */
        IPP_OPERATION_NOT_SUPPORTED(0x0501),        
        /***
         * server-error-service-unavailable
         */
        IPP_SERVICE_UNAVAILABLE(0x0502),        
        /***
         * server-error-version-not-supported
         */
        IPP_VERSION_NOT_SUPPORTED(0x0503),        
        /***
         * server-error-device-error
         */
        IPP_DEVICE_ERROR(0x0504),            
        /***
         * server-error-temporary-error
         */
        IPP_TEMPORARY_ERROR(0x0505),            
        /***
         * server-error-not-accepting-jobs
         */
        IPP_NOT_ACCEPTING(0x0506),            
        /***
         * server-error-busy
         */
        IPP_PRINTER_BUSY(0x0507),            
        /***
         * server-error-job-canceled
         */
        IPP_ERROR_JOB_CANCELED(0x0508),        
        /***
         * server-error-multiple-document-jobs-not-supported
         */
        IPP_MULTIPLE_JOBS_NOT_SUPPORTED(0x0509),    
        /***
         * server-error-printer-is-deactivated
         */
        IPP_PRINTER_IS_DEACTIVATED(0x050A),        
        /***
         * server-error-too-many-jobs
         */
        IPP_TOO_MANY_JOBS(0x050B),            
        /***
         * server-error-too-many-documents
         */
        IPP_TOO_MANY_DOCUMENTS(0x050C),        

        /***
         * Authentication canceled by user
         */
        IPP_AUTHENTICATION_CANCELED(0x1000),    
        /***
         * Error negotiating a secure connection
         */
        IPP_PKI_ERROR(0x1001),            
        /***
         * TLS upgrade required
         */
        IPP_UPGRADE_REQUIRED(0x1002);

        /**
         * Enum value
         */
        private short value;

        /**
         * Constructor
         * @param value Value associated with enum
         */
        IppStatus(int value) {
            this.value = (short) value;
        }

        /**
         * Get the value associated with enum
         * @return Value associated with enum
         */
        public short getValue() {
            return value;
        }

        /**
         * Convert an integer into tis {@link IppStatus} equivalent
         * @param value Value to convert
         * @return {@link IppStatus} equivalent
         */
        public static IppStatus toIppStatus(int value){
            for (IppStatus ippStatus : IppStatus.values())
            {
                if (ippStatus.getValue() == value) return ippStatus;
            }
            throw new IllegalArgumentException("not a valid IppStatus value: " + value);
        }
    }

    /***
     * Format tags for attributes
     */
    public enum IppTag            
    {
        /***
         * Zero tag - used for separators
         */
        IPP_TAG_ZERO(0x00),            
        /***
         * Operation group
         */
        IPP_TAG_OPERATION(0x01),    
        /***
         * Job group
         */
        IPP_TAG_JOB(0x02),                
        /***
         * End-of-attributes
         */
        IPP_TAG_END(0x03),                
        /***
         * Printer group
         */
        IPP_TAG_PRINTER(0x04),            
        /***
         * Unsupported attributes group
         */
        IPP_TAG_UNSUPPORTED_GROUP(0x05),        
        /***
         * Subscription group
         */
        IPP_TAG_SUBSCRIPTION(0x06),            
        /***
         * Event group
         */
        IPP_TAG_EVENT_NOTIFICATION(0x07),        
        /***
         * Resource group
         */
        IPP_TAG_RESOURCE(0x08),            
        /***
         * Document group
         */
        IPP_TAG_DOCUMENT(0x09),            
        /***
         * Unsupported value
         */
        IPP_TAG_UNSUPPORTED_VALUE(0x10),    
        /***
         * Default value
         */
        IPP_TAG_DEFAULT(0x11),            
        /***
         * Unknown value
         */
        IPP_TAG_UNKNOWN(0x12),            
        /***
         * No-value value
         */
        IPP_TAG_NOVALUE(0x13),            
        /***
         * Not-settable value
         */
        IPP_TAG_NOTSETTABLE(0x15),        
        /***
         * Delete-attribute value
         */
        IPP_TAG_DELETEATTR(0x16),            
        /***
         * Admin-defined value
         */
        IPP_TAG_ADMINDEFINE(0x17),            
        /***
         * Integer value
         */
        IPP_TAG_INTEGER(0x21),        
        /***
         * Boolean value
         */
        IPP_TAG_BOOLEAN(0x22),            
        /***
         * Enumeration value
         */
        IPP_TAG_ENUM(0x23),                
        /***
         * Octet string value
         */
        IPP_TAG_STRING(0x30),        
        /***
         * Date/time value
         */
        IPP_TAG_DATE(0x31),                
        /***
         * Resolution value
         */
        IPP_TAG_RESOLUTION(0x32),            
        /***
         * Range value
         */
        IPP_TAG_RANGE(0x33),            
        /***
         * Beginning of collection value
         */
        IPP_TAG_BEGIN_COLLECTION(0x34),        
        /***
         * Text-with-language value
         */
        IPP_TAG_TEXTLANG(0x35),            
        /***
         * Name-with-language value
         */
        IPP_TAG_NAMELANG(0x36),            
        /***
         * End of collection value
         */
        IPP_TAG_END_COLLECTION(0x37),        
        /***
         * Text value
         */
        IPP_TAG_TEXT(0x41),            
        /***
         * Name value
         */
        IPP_TAG_NAME(0x42),                
        /***
         * Reserved for future string value
         */
        IPP_TAG_RESERVED_STRING(0x43),        
        /***
         * Keyword value
         */
        IPP_TAG_KEYWORD(0x44),            
        /***
         * URI value
         */
        IPP_TAG_URI(0x45),                
        /***
         * URI scheme value
         */
        IPP_TAG_URISCHEME(0x46),            
        /***
         * Character set value
         */
        IPP_TAG_CHARSET(0x47),            
        /***
         * Language value
         */
        IPP_TAG_LANGUAGE(0x48),            
        /***
         * MIME media type value
         */
        IPP_TAG_MIMETYPE(0x49),            
        /***
         * Collection member name value
         */
        IPP_TAG_MEMBERNAME(0x4A),            
        /***
         * Extension point for 32-bit tags
         */
        IPP_TAG_EXTENSION(0x7f);

        /**
         * Enum value
         */
        private final byte value;

        /**
         * Constructor
         * @param value Value associated with enum
         */
        IppTag(int value) {
            this.value = (byte) value;
        }

        /**
         * Return the value associated with this enum
         * @return Value associated with enum
         */
        public byte getValue() {
            return value;
        }

        /**
         * Convert a value into its {@link IppTag} equivalent
         * @param value Value to convert
         * @return {@link IppTag} equivalent
         */
        public static IppTag toIppTag(int value) {
            for (IppTag ippTag : IppTag.values()) {
                if (ippTag.getValue() == value) {
                    return ippTag;
                }
            }
            return null;
        }
    }

    /**
     * IPP resolution units
     */
    @SuppressWarnings("WeakerAccess")
    public enum IppUnits {
        /** Units per inch */
        IPP_RES_PER_INCH(3),
        /** Units per centimeter */
        IPP_RES_PER_CM(4),
        /** Unknown */
        IPP_RES_UNKNOWN(100);
        /** Ipp value */
        private final byte mValue;

        /**
         * Constructor
         * @param value Enum value
         */
        IppUnits(int value) {
            mValue = (byte)value;
        }

        /**
         * Return the value associated with the enum
         * @return Value associated with the enum
         */
        public byte getValue() {
            return mValue;
        }

        /**
         * Convert an integer into its {@link IppUnits} equivalent
         * @param value Value to convert
         * @return {@link IppUnits} equivalent
         */
        public static IppUnits fromValue(int value) {
            for(IppUnits enumEntry : values()) {
                if (enumEntry.mValue == value) {
                    return enumEntry;
                }
            }
            return IPP_RES_PER_INCH;
        }
    }

}
