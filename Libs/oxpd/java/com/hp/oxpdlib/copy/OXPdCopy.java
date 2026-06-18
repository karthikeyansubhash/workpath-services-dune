// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.copy;

import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.hp.ext.service.copy.CopyJob;
import com.hp.ext.service.copy.CopyJobCreateContent_SolutionContext_Binding;
import com.hp.ext.service.copy.CopyJobTicket;
import com.hp.ext.service.copy.CopyJob_Create;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceCopyJobService;
import com.hp.oxpdlib.OXPdDevice;
import com.hp.oxpdlib.SOAPFault;
import com.hp.oxpdlib.discovery.DiscoveryTree;
import com.hp.oxpdlib.options.OptionDefinition;
import com.hp.oxpdlib.uiconfiguration.UIContext;
import com.hp.sdd.jabberwocky.chat.HttpHeader;
import com.hp.sdd.jabberwocky.chat.HttpRequestResponseContainer;
import com.hp.sdd.jabberwocky.xml.RestXMLNSHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLTagHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLWriter;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import fi.iki.elonen.NanoHTTPD;


/**
 * This library provides a CORS binding to the OXPd:Copy service on HP devices.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class OXPdCopy {
    private static final String TAG = "OXPdCopy";

    // CONSTANTS
    /**
     * SOAP copy service name
     */
    private static final String SOAP_SERVICE_COPY = "ICopyService";
    /**
     * Copy XML prefix
     */
    private static final String XML_PREFIX_OXPD_COPY = "copy";
    /**
     * Copy OXPd resource name
     */
    private static final String OXPD_RESOURCE_TYPE__COPY = "OXPd:Copy";
    /**
     * Copy service revision
     */
    private static final String OXPD_REVISION__COPY = "http://www.hp.com/schemas/imaging/OXPd/service/copy/2017/04/04";

    /**
     * Copy SOAP GetCopyOptionsProfile method
     */
    private static final String SOAP_OP__GET_COPY_OPTIONS_PROFILE = "GetCopyOptionsProfile";
    /**
     * Copy SOAP GetDefaultCopyOptions method
     */
    private static final String SOAP_OP__GET_DEFAULT_COPY_OPTIONS = "GetDefaultCopyOptions";
    /**
     * Copy SOAP StartCopyJob method
     */
    private static final String SOAP_OP__START_COPY_JOB = "StartCopyJob";
    /**
     * Copy SOAP ReleaseCopyJob method
     */
    private static final String SOAP_OP__RELEASE_COPY_JOB = "ReleaseCopyJob";
    /**
     * Copy SOAP CancelCopyJob method
     */
    private static final String SOAP_OP__DELETE_STORED_COPY_JOB = "DeleteStoredCopyJob";
    /**
     * Copy SOAP EnumerateStoredCopyJobs method
     */
    private static final String SOAP_OP__ENUMERATE_STORED_COPY_JOBS = "EnumerateStoredCopyJobs";
    /**
     * Copy SOAP GetCopyJobStatus method
     */
    private static final String SOAP_OP__GET_COPY_JOB_STATUS = "GetCopyJobStatus";
    /**
     * Copy SOAP CancelCopyJob method
     */
    private static final String SOAP_OP__CANCEL_COPY_JOB = "CancelCopyJob";

    /**
     * Composite XML prefix/revision/version for copy
     */
    private static final String XML_SCHEMA__OXPD_COPY = XML_PREFIX_OXPD_COPY + RestXMLNSHandler.XML_SEPARATOR +
            "http://www.hp.com/schemas/imaging/OXPd/service/copy/*" + RestXMLNSHandler.XML_SEPARATOR + "2017/04/04";

    private static final String XML_SCHEMA__OXPD_COPY_DOMAIN_OPTION = "http://www.hp.com/schemas/imaging/OXPd/domain/options/2017/04/04";

    /**
     * Polling frequency in manual mode
     */
    private static final int STATUS_POLLING_FREQUENCY = 5000;
    /**
     * Delay before switching to manual polling
     */
    private static final int STATUS_POLLING_DELAY = 60000;

    /**
     * Minimum minor version required to support the Copy call
     */
    private static final int[] STORE_COPY_MINOR_VERSION__MIN = {1, 0};

    /**
     * Copy callback receiver endpoint
     */
    private static final String OXPD_SERVER__COPY_CALLBACK = "CopyCallback";

    // CLASSES

    /**
     * Container for internal OXPd:Copy constants
     *
     * @hide
     */
    static class Constants {
        /**
         * XML tag containing {@link CopyOptionsProfile} list
         */
        static final String XML_TAG__COPY__COPY_OPTIONS_PROFILE = "CopyOptionsProfile";
        /**
         * XML tag containing {@link CopyTicket} values
         */
        static final String XML_TAG__COPY__COPY_TICKET = "copyTicket";
        /**
         * XML tag containing {@link CopyOptions} values
         */
        static final String XML_TAG__COPY__COPY_OPTIONS = "copyOptions";
        /**
         * XML tag containing {@link ReleaseCopyTicket} values
         */
        static final String XML_TAG__COPY__RELEASE_COPY_TICKET = "releaseCopyTicket";
        /**
         * XML tag containing {@link CopyOptions} values
         */
        static final String XML_TAG__COPY__RELEASE_OPTIONS = "releaseOptions";

        /**
         * XML tag containing {@link OptionDefinition} list
         */
        static final String XML_TAG__COPY__OPTION_DEFINITIONS = "optionDefinitions";

        /**
         * XML tag containing {@link OptionDefinition}
         */
        static final String XML_TAG__COPY__OPTION_DEFINITION = "OptionDefinition";

        /**
         * XML tag containing option name
         */
        static final String XML_TAG__COPY__OPTION_NAME = "optionName";

        /**
         * XML tag containing option name
         */
        static final String XML_TAG__COPY__OPTION_VALUE = "optionValue";

        /**
         * XML tag containing option availability
         */
        static final String XML_TAG__COPY__IS_AVAILABLE = "isAvailable";

        /**
         * XML tag containing option rules
         */
        static final String XML_TAG__COPY__OPTIONS_RULES = "optionRules";

        /**
         * XML tag containing range rule
         */
        static final String XML_TAG__COPY__RANGE = "Range";

        /**
         * XML tag containing copy job ID
         */
        static final String XML_TAG__COPY_COPY_JOB_ID = "copyJobId";

        /**
         * XML tag containing release copy job ID
         */
        static final String XML_TAG__COPY__RELEASE_COPY_TICKET_JOB_ID = "jobId";

        /**
         * XML tag containing UI Context nonce
         */
        static final String XML_TAG__COPY__UI_CONTEXT_ID = "uiContextId";

        /**
         * XML tag containing copy callback
         */
        static final String XML_TAG__COPY__CALLBACK = "callback";

        /**
         * XML tag containing Server Context Identifier
         */
        static final String XML_TAG__COPY__SERVER_CONTEXT_ID = "serverContextId";

        /**
         * XML tag containing validate parameter
         */
        static final String XML_TAG__COPY__VALIDATE = "validate";

        /**
         * XML tag containing copy job PasswordType
         */
        static final String XML_TAG__COPY__PASSWORD_TYPE = "PasswordType";

        /**
         * XML tag containing enumerate stored job request and response
         */
        static final String XML_TAG__COPY__ENUMERATE_STORED_COPY_JOB_REQUEST = "EnumerateStoredCopyJobsRequest";
        static final String XML_TAG__COPY__ENUMERATE_STORED_COPY_JOB_RESULT = "EnumerateStoredCopyJobsResult";
        static final String XML_TAG__COPY__STORED_COPY_JOB = "StoredCopyJob";

        /**
         * XML tag containing valid values rule
         */
        static final String XML_TAG__COPY__VALID_VALUES = "ValidValues";
        static final String XML_TAG__COPY__INVALID_VALUES = "InvalidValues";
        static final String XML_TAG__COPY__DISABLE = "Disable";
        static final String XML_TAG__COPY__FORCESET = "ForceSet";

        static final String XML_TAG__COPY__RANGE_LOWER_BOUND = "lowerBoundValue";
        static final String XML_TAG__COPY__RANGE_UPPER_BOUND = "upperBoundValue";
        static final String XML_TAG__COPY__OPTION_CONDITION = "Condition";
        static final String XML_TAG__COPY__OPTION_CONDITION_AND = "And";
        static final String XML_TAG__COPY__OPTION_CONDITION_OR = "Or";
        static final String XML_TAG__COPY__OPTION_CONDITION_EQUALS = "Equals";
        static final String XML_TAG__COPY__OPTION_CONDITION_NOT_EQUALS = "NotEquals";
        static final String XML_TAG__COPY__OPTION_CONDITION_LESS_THAN = "LessThan";
        static final String XML_TAG__COPY__OPTION_CONDITION_GREATER_THAN = "GreaterThan";
        static final String XML_TAG__COPY__OPTION_VALUES = "optionValues";
        static final String XML_TAG__COPY__CONDITION_OPERATORS = "conditionOperators";
        static final String XML_TAG__COPY__COPIES = "copies";
        static final String XML_TAG__COPY__TOTAL_PAGES = "totalPages";
        static final String XML_TAG__COPY__ORIGINAL_SIDES = "originalSides";
        static final String XML_TAG__COPY__ORIGINAL_DUPLEX_FORMAT = "originalDuplexFormat";
        static final String XML_TAG__COPY__OUTPUT_SIDES = "outputSides";
        static final String XML_TAG__COPY__OUTPUT_DUPLEX_FORMAT = "outputDuplexFormat";
        static final String XML_TAG__COPY__COLOR_MODE = "colorMode";
        static final String XML_TAG__COPY__ORIGINAL_MEDIA_SIZE = "originalMediaSize";
        static final String XML_TAG__COPY__ORIGINAL_CUSTOM_SIZE_X = "originalCustomSizeX";
        static final String XML_TAG__COPY__ORIGINAL_CUSTOM_SIZE_Y = "originalCustomSizeY";
        static final String XML_TAG__COPY__OUTPUT_MEDIA_SIZE = "outputMediaSize";
        static final String XML_TAG__COPY__OUTPUT_CUSTOM_SIZE_X = "outputCustomSizeX";
        static final String XML_TAG__COPY__OUTPUT_CUSTOM_SIZE_Y = "outputCustomSizeY";
        static final String XML_TAG__COPY__OUTPUT_MEDIA_TYPE = "outputMediaType";
        static final String XML_TAG__COPY__OUTPUT_MEDIA_TRAY = "outputMediaTray";
        static final String XML_TAG__COPY__CONTENT_ORIENTATION = "contentOrientation";
        static final String XML_TAG__COPY__SHARPNESS_ADJUSTMENT = "sharpnessAdjustment";
        static final String XML_TAG__COPY__DARKNESS_ADJUSTMENT = "darknessAdjustment";
        static final String XML_TAG__COPY__CONTRAST_ADJUSTMENT = "contrastAdjustment";
        static final String XML_TAG__COPY__BACKGROUND_CLEANUP = "backgroundCleanup";
        static final String XML_TAG__COPY__TEXT_GRAPHIC_OPTIMIZATION = "textGraphicsOptimization";
        static final String XML_TAG__COPY__OUTPUT_COLLATION = "outputCollation";
        static final String XML_TAG__COPY__OUTPUT_BIN = "outputBin";
        static final String XML_TAG__COPY__JOB_ASSEMBLY = "jobAssembly";
        static final String XML_TAG__COPY__JOB_PREVIEW = "jobPreview";
        static final String XML_TAG__COPY__REDUCE_ENLARGE = "reduceEnlarge";
        static final String XML_TAG__COPY__REDUCE_ENLARGE_MARGINS_INCLUDED = "reduceEnlargeMarginsIncluded";
        static final String XML_TAG__COPY__REDUCE_ENLARGE_PERCENT = "reduceEnlargePercent";
        static final String XML_TAG__COPY__SCAN_SOURCE = "scanSource";
        static final String XML_TAG__COPY__JOB_EXECUTION_MODE = "jobExecutionMode";
        static final String XML_TAG__COPY__STORE_JOB_EXECUTION_MODE = "jobExecutionMode";
        static final String XML_TAG__COPY__NUMBER_UP_COUNT = "numberUpCount";
        static final String XML_TAG__COPY__NUMBER_UP_DIRECTION = "numberUpDirection";
        static final String XML_TAG__COPY__STORE_JOB_FOLDER_NAME = "storeJobFolderName";
        static final String XML_TAG__COPY__STORE_JOB_NAME = "storeJobName";
        static final String XML_TAG__COPY__STORE_JOB_USER_NAME = "storeJobUserName";
        static final String XML_TAG__COPY__STORE_JOB_PASSWORD_TYPE = "storeJobPasswordType";
        static final String XML_TAG__COPY__STORE_JOB_PASSWORD = "storeJobPassword";
        static final String XML_TAG__COPY__STORE_DELETE_ON_POWER_CYCLE = "storeJobDeleteOnPowerCycle";
        static final String XML_TAG__COPY__STORE_DELETE_ON_RELEASE = "storeJobDeleteOnRelease";
        static final String XML_TAG__COPY__STORE_JOB_TIMESTAMP = "storeJobTimestamp";
        static final String XML_TAG__COPY_STORE_JOB_PASSWORD = "storeJobPassword";
        // SDK 1.5 Copy
        static final String XML_TAG__COPY__PROGRESS_DIALOG_MODE = "progressDialogMode";
        // SDK 1.6
        static final String XML_TAG__COPY__ERASE_MARGIN_UNIT = "eraseMarginUnits";
        static final String XML_TAG__COPY__ERASE_BACK_BOTTOM = "eraseBackBottom";
        static final String XML_TAG__COPY__ERASE_BACK_LEFT = "eraseBackLeft";
        static final String XML_TAG__COPY__ERASE_BACK_RIGHT = "eraseBackRight";
        static final String XML_TAG__COPY__ERASE_BACK_TOP = "eraseBackTop";
        static final String XML_TAG__COPY__ERASE_FRONT_BOTTOM = "eraseFrontBottom";
        static final String XML_TAG__COPY__ERASE_FRONT_LEFT = "eraseFrontLeft";
        static final String XML_TAG__COPY__ERASE_FRONT_RIGHT = "eraseFrontRight";
        static final String XML_TAG__COPY__ERASE_FRONT_TOP = "eraseFrontTop";
        static final String XML_TAG__COPY__CAPTURE_MODE = "captureMode";
        static final String XML_TAG__COPY__IMAGE_SHIFT_REDUCE_TO_FIT = "imageShiftReduceToFit";
        static final String XML_TAG__COPY__IMAGE_SHIFT_UNITS = "imageShiftUnits";
        static final String XML_TAG__COPY__IMAGE_SHIFT_X_FRONT = "imageShiftXFront";
        static final String XML_TAG__COPY__IMAGE_SHIFT_Y_FRONT = "imageShiftYFront";
        static final String XML_TAG__COPY__IMAGE_SHIFT_X_BACK = "imageShiftXBack";
        static final String XML_TAG__COPY__IMAGE_SHIFT_Y_BACK = "imageShiftYBack";
        static final String XML_TAG__COPY__BOOKLET_BORDERS_EACH_PAGE = "bookletBordersEachPage";
        static final String XML_TAG__COPY__BOOKLET_FINISHING_OPTION = "bookletFinishingOption";
        static final String XML_TAG__COPY__BOOKLET_FORMAT = "bookletFormat";
        static final String XML_TAG__COPY__STAPLE_OPTION = "stapleLocation";
        static final String XML_TAG__COPY__PUNCH_MODE = "punchOption";
        static final String XML_TAG__COPY__FOLD_MODE = "foldOption";

        static final String XML_TAG__COPY__STAMP_TYPE = "type";
        static final String XML_TAG__COPY__STAMP_TEXT = "text";
        static final String XML_TAG__COPY__STAMP_POLICY_TYPE = "policyType";
        static final String XML_TAG__COPY__STAMP_FORMAT = "format";
        static final String XML_TAG__COPY__STAMP_FORMAT_FONT = "font";
        static final String XML_TAG__COPY__STAMP_FORMAT_TEXT_SIZE = "textSize";
        static final String XML_TAG__COPY__STAMP_FORMAT_TEXT_COLOR = "textColor";
        static final String XML_TAG__COPY__STAMP_FORMAT_WHITE_BACKGROUND = "whiteBackground";
        static final String XML_TAG__COPY__STAMP_FORMAT_STARTING_PAGE = "startingPage";

        static final String XML_TAG__COPY__STAMP_TOP_LEFT = "stampTopLeft";
        static final String XML_TAG__COPY__STAMP_TOP_CENTER = "stampTopCenter";
        static final String XML_TAG__COPY__STAMP_TOP_RIGHT = "stampTopRight";
        static final String XML_TAG__COPY__STAMP_BOTTOM_LEFT = "stampBottomLeft";
        static final String XML_TAG__COPY__STAMP_BOTTOM_CENTER = "stampBottomCenter";
        static final String XML_TAG__COPY__STAMP_BOTTOM_RIGHT = "stampBottomRight";

        // SDK 1.6
        static final String XML_TAG__COPY__WATERMARK_TEXT_SIZE = "watermarkTextSize";
        static final String XML_TAG__COPY__WATERMARK_TRANSPARENCY = "watermarkTransparency";
        static final String XML_TAG__COPY__WATERMARK_BACKGROUND_COLOR = "watermarkBackgroundColor";
        static final String XML_TAG__COPY__WATERMARK_FONT = "watermarkFont";
        static final String XML_TAG__COPY__WATERMARK_TEXT_COLOR = "watermarkTextColor";
        static final String XML_TAG__COPY__WATERMARK_ONLY_FIRST_PAGE = "watermarkOnlyFirstPage";
        static final String XML_TAG__COPY__WATERMARK_DARKNESS = "watermarkDarkness";
        static final String XML_TAG__COPY__WATERMARK_TEXT = "watermarkText";
        static final String XML_TAG__COPY__WATERMARK_ROTATE45 = "watermarkRotate45";
        static final String XML_TAG__COPY__WATERMARK_TYPE = "watermarkType";
        static final String XML_TAG__COPY__WATERMARK_MESSAGETYPE = "watermarkMessageType";
        static final String XML_TAG__COPY__WATERMARK_BACKGROUND_PATTERN = "watermarkBackgroundPattern";
    }

    /**
     * Holder for Copy Job parameters
     */
    private static class CopyJobParams {

        /**
         * {@link UIContext} value
         */
        protected final UIContext mUIContext;
        /**
         * {@link CopyTicket} value
         */
        protected final CopyJobTicket mCopyJobTicket;
        /**
         * {@link com.hp.oxpdlib.OXPdDevice.RequestCallback} status callback
         */
        protected final OXPdDevice.RequestCallback mStatusCallback;
        /**
         * Status callback request identifier
         */
        protected final int mStatusRequestID;
        /**
         * Server context
         */
        protected final String mServerContext;

        protected final String mPackageName;
        protected CopyJobCreateContent_SolutionContext_Binding mSolutionContextBinding;

        /**
         * CopyJob parameter holder constructor
         *
         * @param uiContext       {@link UIContext} value
         * @param copyJobTicket   {@link CopyTicket} value
         * @param statusRequestID Status request identifier
         * @param statusCallback  {@link com.hp.oxpdlib.OXPdDevice.RequestCallback} status callback
         */
        private CopyJobParams(UIContext uiContext, CopyJobTicket copyJobTicket, int statusRequestID, OXPdDevice.RequestCallback statusCallback, String packageName) {
            mUIContext = uiContext;
            mCopyJobTicket = copyJobTicket;
            mStatusRequestID = statusRequestID;
            mStatusCallback = statusCallback;
            mServerContext = UUID.randomUUID().toString();
            mPackageName = packageName;
        }

        /**
         * CopyJob parameter holder constructor
         *
         * @param uiContext     {@link UIContext} value
         * @param copyJobTicket {@link CopyTicket} value
         */
        private CopyJobParams(UIContext uiContext, CopyJobTicket copyJobTicket) {
            this(uiContext, copyJobTicket, 0, null, null);
        }
    }

    /**
     * Holder for Release Copy Job parameters
     */
    private static class ReleaseCopyJobParams extends CopyJobParams {
        /**
         * {@link ReleaseCopyTicket} value
         */
        private final ReleaseCopyTicket mReleaseCopyTicket;

        /**
         * CopyJob parameter holder constructor
         *
         * @param uiContext         {@link UIContext} value
         * @param releaseCopyTicket {@link ReleaseCopyTicket} ticket
         * @param statusRequestID   Status request identifier
         * @param statusCallback    {@link com.hp.oxpdlib.OXPdDevice.RequestCallback} status callback
         */
        private ReleaseCopyJobParams(UIContext uiContext, ReleaseCopyTicket releaseCopyTicket, int statusRequestID, OXPdDevice.RequestCallback statusCallback, String pacakgeName) {
            super(uiContext, null, statusRequestID, statusCallback, pacakgeName);
            mReleaseCopyTicket = releaseCopyTicket;
        }

        /**
         * CopyJob parameter holder constructor
         *
         * @param releaseCopyTicket {@link ReleaseCopyTicket} ticket
         */
        private ReleaseCopyJobParams(UIContext uiContext, ReleaseCopyTicket releaseCopyTicket) {
            this(uiContext, releaseCopyTicket, 0, null, null);
        }
    }

    /**
     * Holder for Delete Job parameters
     */
    private static class DeleteCopyJobParams {

        /**
         * {@link UIContext} value
         */
        private final UIContext mUIContext;

        private final String mJobId;

        private final String mJobPassword;

        /**
         * CopyJob parameter holder constructor
         *
         * @param uiContext   {@link UIContext} value
         * @param jobId       job id
         * @param jobPassword job id
         */
        private DeleteCopyJobParams(UIContext uiContext, String jobId, String jobPassword) {
            mUIContext = uiContext;
            mJobId = jobId;
            mJobPassword = jobPassword;
        }
    }

    // VARIABLES
    /**
     * OXPd device instance
     */
    // TODO it will be removed
    private OXPdDevice mDevice;

    private IDeviceCopyJobService mCopyJobService;

    /**
     * URL of Copy service
     */
    // TODO it will be removed
    private URL mCopyURL;

    /**
     * Hooks to register with internal server
     */
    // TODO it will be removed
    private OXPdDevice.InternalServerHooks mServerHooks;

    /**
     * Copy job mapping of active jobs
     */
    private final Map<String, CopyJobTracker> mJobTrackerMap = new HashMap<>();

    /**
     * Store Copy Job support
     */
    private boolean mSupportsStoreCopyJob;

    /**
     * Container class of active copy jobs
     */
    private class CopyJobTracker {

        /**
         * Internal job identifier
         */
        private final String mServerContext;
        /**
         * OXPd Copy Job ID
         */
        private final CopyJobID mCopyJobID;
        /**
         * Status callback request identifier
         */
        private final int mStatusRequestID;
        /**
         * Status callback
         */
        private OXPdDevice.RequestCallback mStatusCallback;

        /**
         * Runnable to use if polling
         */
        private final Runnable mStatusPollingRunnable = new Runnable() {
            @Override
            public void run() {
                Message response = mGetCopyJobStatusHandler.processRequest(mStatusRequestID, mCopyJobID);
                if ((response != null) && (response.obj instanceof CopyJobStatus)) {
                    CopyJobStatus jobStatus = (CopyJobStatus) response.obj;
                    if (jobStatus.resultCode == JobResultCode.Active) {
                        mDevice.queueDelayedCallback(mStatusPollingRunnable, STATUS_POLLING_FREQUENCY);
                    } else {
                        unregisterJobCallbacks(mServerContext);
                        mJobTrackerMap.remove(mServerContext);
                    }
                }
                if ((mStatusCallback != null) && (response != null)) {
                    mStatusCallback.requestResult(mDevice, response);
                }
            }
        };

        /**
         * Constructor
         *
         * @param serverContext   Internal job identifier
         * @param copyJobID       OXPd Copy Job ID
         * @param requestId       request id
         * @param requestCallback request callback
         */
        private CopyJobTracker(String serverContext, CopyJobID copyJobID, int requestId, OXPdDevice.RequestCallback requestCallback) {
            mServerContext = serverContext;
            mCopyJobID = copyJobID;
            mStatusRequestID = requestId;
            mStatusCallback = requestCallback;
        }
    }

    /**
     * Copy SOAP request builder
     */
    private abstract class CopySOAPRequestBuilder implements OXPdDevice.SOAPRequestBuilder {
        /**
         * Return the CopyService URL
         *
         * @return Copy service url
         */
        @Override
        public URL getURL() {
            return mCopyURL;
        }

        /**
         * Return XML schemas used by Copy
         *
         * @return List of Copy related schemas
         */
        @Override
        public List<String> getXMLSchemas() {
            return Collections.singletonList(XML_SCHEMA__OXPD_COPY);
        }

        /**
         * Return service name
         *
         * @return Service name
         */
        @Override
        public String getServiceName() {
            return SOAP_SERVICE_COPY;
        }

        /**
         * Return the service revision string
         *
         * @return Service revision
         */
        @Override
        public String getServiceRevision() {
            return OXPD_REVISION__COPY;
        }

        /**
         * Return list of HTTP headers to include as part of request
         *
         * @return Empty List
         */
        @Override
        public List<HttpHeader> getRequestHeaders() {
            return Collections.emptyList();
        }

        /**
         * Use admin credentials as part of request?
         *
         * @return false, don't require credentials
         */
        @Override
        public boolean useAdminCredentials() {
            return false;
        }
    }


    /**
     * Copy callback data holder
     */
    private static class CopyCallbackData {
        /**
         * Internal job ID
         */
        private final String mServerContext;
        /**
         * SOAP Message
         */
        private final String mSOAPMessage;

        /**
         * Constructor
         *
         * @param serverContext Internal job ID
         * @param body          SOAP Message
         */
        private CopyCallbackData(String serverContext, String body) {
            mServerContext = serverContext;
            mSOAPMessage = body;
        }
    }

    private final OXPdDevice.HandleIncomingServerRequest mCopyCallbackRequestHandler = new OXPdDevice.HandleIncomingServerRequest() {
        /**
         * Handle a server POST request
         * @param uri
         *          Relative URI
         * @param incomingData
         *          Incoming data
         * @param incomingDataParameters
         *          Incoming data parameters
         * @return
         *          HTTP response code to return
         */
        @Override
        public NanoHTTPD.Response handleRequest(String uri, Map<String, String> incomingData, Map<String, List<String>> incomingDataParameters) {
            String soapMessageBody = incomingData.get(OXPdDevice.Constants.SOAP_MESSAGE_KEY);
            if (!TextUtils.isEmpty(soapMessageBody)) {
                mDevice.queueRequest(mCopyCallbackEventRequestHandler,
                        new CopyCallbackData(getServerContext(uri), soapMessageBody),
                        0, null
                );
            }

            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.ACCEPTED, NanoHTTPD.MIME_PLAINTEXT, null, 0);
        }
    };

    /**
     * Request handler for copy job events
     */
    private final OXPdDevice.DeviceProcessRequestCallback mCopyCallbackEventRequestHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {
            CopyCallbackData data = (CopyCallbackData) requestParams;
            String copyJobEventData = data.mSOAPMessage;
            RestXMLTagHandler handler = setupDefaultHandler();
            CopyJobTracker tracker = mJobTrackerMap.get(data.mServerContext);
            if (tracker == null) return null;
            try {
                CopyJobEvent event = CopyJobEvent.parseRequestResult(mDevice, copyJobEventData, handler);
                mDevice.removeCallback(tracker.mStatusPollingRunnable);
                if (event.mJobStatus.resultCode == JobResultCode.Active) {
                    mDevice.queueDelayedCallback(tracker.mStatusPollingRunnable, STATUS_POLLING_DELAY);
                } else {
                    mJobTrackerMap.remove(tracker.mServerContext);
                    unregisterJobCallbacks(tracker.mServerContext);
                }
                if (tracker.mStatusCallback != null) {
                    Message msg = Message.obtain(null, tracker.mStatusRequestID, OXPdDevice.REQUEST_RETURN_CODE__OK, 0, event);
                    tracker.mStatusCallback.requestResult(mDevice, msg);
                }
            } catch (Error ignored) {
            }
            return null;
        }
    };

    /**
     * Request handler for {@link #GetCopyOptionsProfile(int, OXPdDevice.RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mGetCopyOptionsProfileHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new CopySOAPRequestBuilder() {

                @Override
                public String getSoapOperation() {
                    return SOAP_OP__GET_COPY_OPTIONS_PROFILE;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    xmlWriter.writeStartTag(XML_SCHEMA__OXPD_COPY, getSoapOperation(), null);
                    xmlWriter.writeEndTag(XML_SCHEMA__OXPD_COPY, getSoapOperation());
                }
            });

            int result;
            Object data;
            try {
                try {
                    Thread.sleep(500);
                } catch (Throwable throwable) {
                }
                data = CopyOptionsProfile.parseRequestResult(mDevice, requestResponse, checkHTTPResponse(requestResponse));
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Error error) {
                data = error;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            } catch (Throwable throwable) {
                data = new Error(ErrorName.Unknown, throwable.getMessage());
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            }
            return Message.obtain(null, requestID, result, 0, data);
        }
    };

    /**
     * Add a copy ticket to the XML writer
     * @param jobParams
     *              Copy job parameters
     * @param xmlWriter
     *              XML writer to add copy ticket to
     */
    // TODO It will be removed.
    /*
    private void addCopyTicketPayload(CopyJobParams jobParams, RestXMLWriter xmlWriter) {

        CopyTicket copyTicket = jobParams.mCopyJobTicket;

        xmlWriter.writeStartTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__COPY_TICKET, null);
        if (copyTicket.copyOptions != null) {
            xmlWriter.writeStartTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__COPY_OPTIONS, null);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__COPIES, null, "%d", copyTicket.copyOptions.copies);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__ORIGINAL_SIDES, null, "%s", copyTicket.copyOptions.originalSides.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__ORIGINAL_DUPLEX_FORMAT, null, "%s", copyTicket.copyOptions.originalDuplexFormat.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__OUTPUT_SIDES, null, "%s", copyTicket.copyOptions.outputSides.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__OUTPUT_DUPLEX_FORMAT, null, "%s", copyTicket.copyOptions.outputDuplexFormat.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__COLOR_MODE, null, "%s", copyTicket.copyOptions.colorMode.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__ORIGINAL_MEDIA_SIZE, null, "%s", copyTicket.copyOptions.originalMediaSize.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__ORIGINAL_CUSTOM_SIZE_X, null, "%f", copyTicket.copyOptions.originalCustomSizeX);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__ORIGINAL_CUSTOM_SIZE_Y, null, "%f", copyTicket.copyOptions.originalCustomSizeY);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__OUTPUT_MEDIA_SIZE, null, "%s", copyTicket.copyOptions.outputMediaSize.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__OUTPUT_CUSTOM_SIZE_X, null, "%f", copyTicket.copyOptions.outputCustomSizeX);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__OUTPUT_CUSTOM_SIZE_Y, null, "%f", copyTicket.copyOptions.outputCustomSizeY);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__OUTPUT_MEDIA_TYPE, null, "%s", copyTicket.copyOptions.outputMediaType.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__OUTPUT_MEDIA_TRAY, null, "%s", copyTicket.copyOptions.outputMediaTray.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__CONTENT_ORIENTATION, null, "%s", copyTicket.copyOptions.contentOrientation.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__SHARPNESS_ADJUSTMENT, null, "%s", copyTicket.copyOptions.sharpnessAdjustment.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__DARKNESS_ADJUSTMENT, null, "%s", copyTicket.copyOptions.darknessAdjustment.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__CONTRAST_ADJUSTMENT, null, "%s", copyTicket.copyOptions.contrastAdjustment.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__BACKGROUND_CLEANUP, null, "%s", copyTicket.copyOptions.backgroundCleanup.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__TEXT_GRAPHIC_OPTIMIZATION, null, "%s", copyTicket.copyOptions.textGraphicsOptimization.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__OUTPUT_COLLATION, null, "%s", copyTicket.copyOptions.outputCollation.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__OUTPUT_BIN, null, "%s", copyTicket.copyOptions.outputBin.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__JOB_ASSEMBLY, null, "%s", copyTicket.copyOptions.jobAssembly.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__JOB_PREVIEW, null, "%s", copyTicket.copyOptions.jobPreview.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__REDUCE_ENLARGE, null, "%s", copyTicket.copyOptions.reduceEnlarge.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__REDUCE_ENLARGE_MARGINS_INCLUDED, null, "%s", copyTicket.copyOptions.reduceEnlargeMarginsIncluded);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__REDUCE_ENLARGE_PERCENT, null, "%d", copyTicket.copyOptions.reduceEnlargePercent);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__SCAN_SOURCE, null, "%s", copyTicket.copyOptions.scanSource.mValue);
            if (mSupportsStoreCopyJob) {
                xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__JOB_EXECUTION_MODE, null, "%s", copyTicket.copyOptions.jobExecutionMode.mValue);
                xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__STORE_JOB_FOLDER_NAME, null, "%s", copyTicket.copyOptions.storeJobFolderName);
                xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__STORE_JOB_NAME, null, "%s", copyTicket.copyOptions.storeJobName);
                xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__STORE_JOB_PASSWORD_TYPE, null, "%s", copyTicket.copyOptions.storeJobPasswordType.mValue);
                xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__STORE_JOB_PASSWORD, null, "%s", copyTicket.copyOptions.storeJobPassword);
                xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__STORE_DELETE_ON_POWER_CYCLE, null, "%s", copyTicket.copyOptions.storeJobDeleteOnPowerCycle);
                xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__STORE_DELETE_ON_RELEASE, null, "%s", copyTicket.copyOptions.storeJobDeleteOnRelease);
                xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__NUMBER_UP_COUNT, null, "%s", copyTicket.copyOptions.numberUpCount.mValue);
                xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__NUMBER_UP_DIRECTION, null, "%s", copyTicket.copyOptions.numberUpDirection.mValue);

                // other new parameters
            }
            if (copyTicket.copyOptions.progressDialogMode != null) {
                xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__PROGRESS_DIALOG_MODE, null, "%s", copyTicket.copyOptions.progressDialogMode.mValue);
            }

            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__ERASE_MARGIN_UNIT,null,"%s", copyTicket.copyOptions.eraseMarginUnit.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__ERASE_BACK_BOTTOM, null, "%f", copyTicket.copyOptions.eraseBackBottom);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__ERASE_BACK_LEFT, null, "%f", copyTicket.copyOptions.eraseBackLeft);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__ERASE_BACK_RIGHT, null, "%f", copyTicket.copyOptions.eraseBackRight);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__ERASE_BACK_TOP, null, "%f", copyTicket.copyOptions.eraseBackTop);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__ERASE_FRONT_BOTTOM, null, "%f", copyTicket.copyOptions.eraseFrontBottom);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__ERASE_FRONT_LEFT, null, "%f", copyTicket.copyOptions.eraseFrontLeft);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__ERASE_FRONT_RIGHT, null, "%f", copyTicket.copyOptions.eraseFrontRight);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__ERASE_FRONT_TOP, null, "%f", copyTicket.copyOptions.eraseFrontTop);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__CAPTURE_MODE, null, "%s", copyTicket.copyOptions.captureMode.mValue);

            if(copyTicket.copyOptions.stapleOption != null){
                xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__STAPLE_OPTION,null,"%s",copyTicket.copyOptions.stapleOption.mValue);
            }
            if(copyTicket.copyOptions.punchMode != null){
                xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY,Constants.XML_TAG__COPY__PUNCH_MODE,null,"%s",copyTicket.copyOptions.punchMode.mValue);
            }

            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__WATERMARK_TEXT, null, "%s", copyTicket.copyOptions.watermarkText);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__WATERMARK_TYPE, null, "%s", copyTicket.copyOptions.watermarkType.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__WATERMARK_TEXT_SIZE, null, "%d", copyTicket.copyOptions.watermarkTextSize);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__WATERMARK_FONT, null, "%s", copyTicket.copyOptions.watermarkFont);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__WATERMARK_MESSAGETYPE, null, "%s", copyTicket.copyOptions.watermarkMessageType.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__WATERMARK_TEXT_COLOR, null, "%s", copyTicket.copyOptions.watermarkTextColor);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__WATERMARK_ONLY_FIRST_PAGE, null, "%s", copyTicket.copyOptions.watermarkOnlyFirstPage.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__WATERMARK_TRANSPARENCY, null, "%d", copyTicket.copyOptions.watermarkTransparency);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__WATERMARK_BACKGROUND_COLOR, null, "%s", copyTicket.copyOptions.watermarkBackgroundColor);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__WATERMARK_BACKGROUND_PATTERN, null, "%s", copyTicket.copyOptions.watermarkBackgroundPattern.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__WATERMARK_ROTATE45, null, "%s", copyTicket.copyOptions.watermarkRotate45.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__WATERMARK_DARKNESS, null, "%d", copyTicket.copyOptions.watermarkDarkness);



            RestXMLWriter.XMLAttributes attributes = new RestXMLWriter.XMLAttributes();
            // action
            attributes.clear().add(null,
                    "xmlns",
                    XML_SCHEMA__OXPD_COPY_DOMAIN_OPTION);

            // stamp options
            for(StampPosition stampPosition : StampPosition.values()) {
                StampOption stampOption = copyTicket.copyOptions.stampOptionMap.get(stampPosition);
                String stampPositionTagName = "";
                switch(stampPosition){
                    case TOP_LEFT:
                        stampPositionTagName = Constants.XML_TAG__COPY__STAMP_TOP_LEFT;
                        break;
                    case TOP_CENTER:
                        stampPositionTagName = Constants.XML_TAG__COPY__STAMP_TOP_CENTER;
                        break;
                    case TOP_RIGHT:
                        stampPositionTagName = Constants.XML_TAG__COPY__STAMP_TOP_RIGHT;
                        break;
                    case BOTTOM_LEFT:
                        stampPositionTagName = Constants.XML_TAG__COPY__STAMP_BOTTOM_LEFT;
                        break;
                    case BOTTOM_CENTER:
                        stampPositionTagName = Constants.XML_TAG__COPY__STAMP_BOTTOM_CENTER;
                        break;
                    case BOTTOM_RIGHT:
                        stampPositionTagName = Constants.XML_TAG__COPY__STAMP_BOTTOM_RIGHT;
                        break;
                }
                if(stampOption == null){
                    stampOption = new StampOption();
                }

                xmlWriter.writeStartTag(XML_SCHEMA__OXPD_COPY, stampPositionTagName, null);

                xmlWriter.writeTag(null, Constants.XML_TAG__COPY__STAMP_TYPE, attributes, "%s", stampOption.type.mValue);


                xmlWriter.writeTag(null, Constants.XML_TAG__COPY__STAMP_TEXT, attributes, "%s", stampOption.text);


                xmlWriter.writeStartTag(null, Constants.XML_TAG__COPY__STAMP_FORMAT, attributes);
                xmlWriter.writeTag(null, Constants.XML_TAG__COPY__STAMP_FORMAT_FONT, null, "%s", stampOption.format.font);
                xmlWriter.writeTag(null, Constants.XML_TAG__COPY__STAMP_FORMAT_TEXT_COLOR, null, "%s", stampOption.format.textColor);
                xmlWriter.writeTag(null, Constants.XML_TAG__COPY__STAMP_FORMAT_TEXT_SIZE, null, "%s", stampOption.format.textSize);
                xmlWriter.writeTag(null, Constants.XML_TAG__COPY__STAMP_FORMAT_WHITE_BACKGROUND, null, "%s", stampOption.format.whiteBackground);
                xmlWriter.writeTag(null, Constants.XML_TAG__COPY__STAMP_FORMAT_STARTING_PAGE, null, "%s", stampOption.format.startingPage);
                xmlWriter.writeEndTag(null, Constants.XML_TAG__COPY__STAMP_FORMAT);

                xmlWriter.writeTag(null, Constants.XML_TAG__COPY__STAMP_POLICY_TYPE, attributes, "%s", stampOption.policyType.mValue);
                xmlWriter.writeEndTag(XML_SCHEMA__OXPD_COPY, stampPositionTagName);
            }


            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__IMAGE_SHIFT_UNITS, null, "%s", copyTicket.copyOptions.imageShiftUnits.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__IMAGE_SHIFT_X_FRONT, null, "%f", copyTicket.copyOptions.imageShiftXFront);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__IMAGE_SHIFT_Y_FRONT, null, "%f", copyTicket.copyOptions.imageShiftYFront);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__IMAGE_SHIFT_X_BACK, null, "%f", copyTicket.copyOptions.imageShiftXBack);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__IMAGE_SHIFT_Y_BACK, null, "%f", copyTicket.copyOptions.imageShiftYBack);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__IMAGE_SHIFT_REDUCE_TO_FIT, null, "%s", copyTicket.copyOptions.imageShiftReduceToFit.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__BOOKLET_FORMAT, null, "%s", copyTicket.copyOptions.bookletFormat.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__BOOKLET_BORDERS_EACH_PAGE, null, "%s", copyTicket.copyOptions.bookletBordersEachPage.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__BOOKLET_FINISHING_OPTION, null, "%s", copyTicket.copyOptions.bookletFinishingOption.mValue);
            if(copyTicket.copyOptions.foldMode != null){
                xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY,Constants.XML_TAG__COPY__FOLD_MODE,null,"%s",copyTicket.copyOptions.foldMode.mValue);
            }

            xmlWriter.writeEndTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__COPY_OPTIONS);
        }

        xmlWriter.writeStartTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__CALLBACK, null);
        xmlWriter.writeTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, OXPdDevice.Constants.XML_TAG__COMMON__URI, null, "%s", mDevice.getDeviceAbsoluteURLForPath(OXPD_SERVER__COPY_CALLBACK + "/" + jobParams.mServerContext ));
        xmlWriter.writeTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, OXPdDevice.Constants.XML_TAG__COMMON__BINDING, null, "%s", OXPdDevice.Constants.XML_VALUE__COMMON_BINDING__SOAP);
        xmlWriter.writeTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, OXPdDevice.Constants.XML_TAG__COMMON__CONNECTION_TIMEOUT, null, "%d", 30);
        xmlWriter.writeTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, OXPdDevice.Constants.XML_TAG__COMMON__RESPONSE_TIMEOUT, null, "%d", 30);
        xmlWriter.writeEndTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__CALLBACK);

        xmlWriter.writeEndTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__COPY_TICKET);
    }
*/

    /**
     * Add a release copy ticket to the XML writer
     *
     * @param jobParams Copy job parameters
     * @param xmlWriter XML writer to add copy ticket to
     */
    private void addReleaseCopyTicketPayload(ReleaseCopyJobParams jobParams, RestXMLWriter xmlWriter) {

        ReleaseCopyTicket copyTicket = jobParams.mReleaseCopyTicket;

        xmlWriter.writeStartTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__RELEASE_COPY_TICKET, null);
        xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__RELEASE_COPY_TICKET_JOB_ID, null, "%s", copyTicket.jobId);

        if (copyTicket.releaseCopyOptions != null) {
            xmlWriter.writeStartTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__RELEASE_OPTIONS, null);

            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__COPIES, null, "%d", copyTicket.releaseCopyOptions.copies);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__STORE_JOB_PASSWORD, null, "%s", copyTicket.releaseCopyOptions.storeJobPassword);

            xmlWriter.writeEndTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__RELEASE_OPTIONS);
        }

        xmlWriter.writeStartTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__CALLBACK, null);
        xmlWriter.writeTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, OXPdDevice.Constants.XML_TAG__COMMON__URI, null, "%s", mDevice.getDeviceAbsoluteURLForPath(OXPD_SERVER__COPY_CALLBACK + "/" + jobParams.mServerContext));
        xmlWriter.writeTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, OXPdDevice.Constants.XML_TAG__COMMON__BINDING, null, "%s", OXPdDevice.Constants.XML_VALUE__COMMON_BINDING__SOAP);
        xmlWriter.writeTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, OXPdDevice.Constants.XML_TAG__COMMON__CONNECTION_TIMEOUT, null, "%d", 30);
        xmlWriter.writeTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, OXPdDevice.Constants.XML_TAG__COMMON__RESPONSE_TIMEOUT, null, "%d", 30);
        xmlWriter.writeEndTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__CALLBACK);

        xmlWriter.writeEndTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__RELEASE_COPY_TICKET);
    }

    /**
     * Request handler for {@link #GetDefaultCopyOptions(int, String, OXPdDevice.RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mGetDefaultCopyOptionsHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new CopySOAPRequestBuilder() {

                @Override
                public String getSoapOperation() {
                    return SOAP_OP__GET_DEFAULT_COPY_OPTIONS;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    xmlWriter.writeStartTag(XML_SCHEMA__OXPD_COPY, getSoapOperation(), null);
                    xmlWriter.writeEndTag(XML_SCHEMA__OXPD_COPY, getSoapOperation());
                }
            });

            int result;
            Object data;
            try {
                String packageName = (String) requestParams;
                data = mCopyJobService.getDefaultOptions(packageName);
//                data = CopyOptions.parseRequestResult(mDevice, requestResponse, checkHTTPResponse(requestResponse));
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Throwable throwable) {
                Log.e("[SIM]", throwable.getMessage(), throwable);
                data = new Error(ErrorName.Unknown, throwable.getMessage());
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            }
            return Message.obtain(null, requestID, result, 0, data);
        }
    };

    /**
     * Holder for Enumerate Stored Copy Job parameters
     */
    private static class EnumerateStoredCopyJobParams extends CopyJobParams {
        /**
         * CopyJob parameter holder constructor
         *
         * @param uiContext {@link UIContext} value
         */
        private EnumerateStoredCopyJobParams(UIContext uiContext, int statusRequestID, OXPdDevice.RequestCallback statusCallback, String packageName) {
            super(uiContext, null, 0, null, packageName);
        }
    }

    /**
     * Request handler for {@link #EnumerateStoredCopyJobs(UIContext, int, OXPdDevice.RequestCallback, String)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mEnumerateStoredCopyJobsHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {
            //noinspection unchecked
            final EnumerateStoredCopyJobParams enumerateStoredCopyJobRequest = (EnumerateStoredCopyJobParams) requestParams;

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new CopySOAPRequestBuilder() {
                @Override
                public String getSoapOperation() {
                    return SOAP_OP__ENUMERATE_STORED_COPY_JOBS;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    xmlWriter.writeStartTag(XML_SCHEMA__OXPD_COPY, getSoapOperation(), null);
                    xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__UI_CONTEXT_ID, null, "%s", enumerateStoredCopyJobRequest.mUIContext.getUIContext());
                    xmlWriter.writeEndTag(XML_SCHEMA__OXPD_COPY, getSoapOperation());
                }
            });

            int result;
            Object data;
            try {
                try {
                    Thread.sleep(500);
                } catch (Throwable throwable) {
                }
                data = StoredCopyJob.parseRequestResult(mDevice, requestResponse, checkHTTPResponse(requestResponse));
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Error error) {
                data = error;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            } catch (Throwable throwable) {
                data = new Error(ErrorName.Unknown, throwable.getMessage());
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            }
            return Message.obtain(null, requestID, result, 0, data);
        }
    };

    /**
     * Request handler for {@link #StartCopyJob(UIContext, CopyJobTicket, int, OXPdDevice.RequestCallback, int, OXPdDevice.RequestCallback, String)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mStartCopyJobHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {

            //noinspection unchecked
            final CopyJobParams copyJobRequest = (CopyJobParams) requestParams;
            final CopyJobTicket copyJobTicket = copyJobRequest.mCopyJobTicket;
            final String packageName = copyJobRequest.mPackageName;

            CopyJob_Create copyJobCreate = new CopyJob_Create();
            copyJobCreate.setTicket(copyJobRequest.mCopyJobTicket);

            // TODO copyJobRequest.mSolutionContextBinding needs to be set.
            copyJobCreate.setSolutionContext(copyJobRequest.mSolutionContextBinding);

            // Not use call back
//            registerJobCallbacks(copyJobRequest.mServerContext);


            int result;
            Object data;
            try {
                CopyJob copyJob = mCopyJobService.createCopyJob(copyJobRequest.mPackageName, copyJobCreate);

//                data = CopyJobID.parseRequestResult(mDevice, requestResponse, checkHTTPResponse(requestResponse));
//                CopyJobTracker tracker = new CopyJobTracker(copyJobRequest.mServerContext, (CopyJobID)data,
//                        copyJobRequest.mStatusRequestID, copyJobRequest.mStatusCallback);
//                mJobTrackerMap.put(tracker.mServerContext, tracker);
//                mDevice.queueDelayedCallback(tracker.mStatusPollingRunnable, STATUS_POLLING_DELAY);

                data = copyJob;

                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Throwable throwable) {
                data = new Error(ErrorName.Unknown, throwable.getMessage());
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            }
            return Message.obtain(null, requestID, result, 0, data);
        }
    };

    /**
     * Request handler for {@link #ReleaseCopyJob(UIContext, ReleaseCopyTicket, int, OXPdDevice.RequestCallback, int, OXPdDevice.RequestCallback, String)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mReleaseCopyJobHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {

            //noinspection unchecked
            final ReleaseCopyJobParams releaseCopyJobRequest = (ReleaseCopyJobParams) requestParams;

            registerJobCallbacks(releaseCopyJobRequest.mServerContext);

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new CopySOAPRequestBuilder() {

                @Override
                public List<String> getXMLSchemas() {
                    return Arrays.asList(XML_SCHEMA__OXPD_COPY, OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON);
                }

                @Override
                public String getSoapOperation() {
                    return SOAP_OP__RELEASE_COPY_JOB;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    //noinspection unchecked
                    xmlWriter.writeStartTag(XML_SCHEMA__OXPD_COPY, getSoapOperation(), null);
                    xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__UI_CONTEXT_ID, null, "%s", releaseCopyJobRequest.mUIContext.getUIContext());
                    xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__SERVER_CONTEXT_ID, null, "%s", releaseCopyJobRequest.mServerContext);
                    addReleaseCopyTicketPayload(releaseCopyJobRequest, xmlWriter);
                    xmlWriter.writeEndTag(XML_SCHEMA__OXPD_COPY, getSoapOperation());
                }
            });

            RestXMLWriter xmlWriter = new RestXMLWriter(mDevice.getXMLNSHandler(),
                    false,
                    OXPdDevice.Constants.XML_SCHEMA__XSI,
                    OXPdDevice.Constants.XML_SCHEMA__XSD,
                    OXPdDevice.Constants.XML_SCHEMA__SOAP_ENVELOPE,
                    OXPdDevice.Constants.XML_SCHEMA__SOAP_ADDRESSING,
                    XML_SCHEMA__OXPD_COPY,
                    OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON);

            int result;
            Object data;
            try {
                data = CopyJobID.parseRequestResult(mDevice, requestResponse, checkHTTPResponse(requestResponse));
                CopyJobTracker tracker = new CopyJobTracker(releaseCopyJobRequest.mServerContext, (CopyJobID) data,
                        releaseCopyJobRequest.mStatusRequestID, releaseCopyJobRequest.mStatusCallback);
                mJobTrackerMap.put(tracker.mServerContext, tracker);
                mDevice.queueDelayedCallback(tracker.mStatusPollingRunnable, STATUS_POLLING_DELAY);
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Error error) {
                unregisterJobCallbacks(releaseCopyJobRequest.mServerContext);
                data = error;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            } catch (Throwable throwable) {
                data = new Error(ErrorName.Unknown, throwable.getMessage());
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            }
            return Message.obtain(null, requestID, result, 0, data);
        }
    };

    /**
     * Request handler for {@link #GetCopyJobStatus(CopyJobID, int, OXPdDevice.RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mGetCopyJobStatusHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new CopySOAPRequestBuilder() {

                @Override
                public String getSoapOperation() {
                    return SOAP_OP__GET_COPY_JOB_STATUS;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    xmlWriter.writeStartTag(XML_SCHEMA__OXPD_COPY, getSoapOperation(), null);
                    xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY_COPY_JOB_ID, null, "%s", ((CopyJobID) requestParams).getCopyJobID());
                    xmlWriter.writeEndTag(XML_SCHEMA__OXPD_COPY, getSoapOperation());
                }
            });

            int result;
            Object data;
            try {
                try {
                    Thread.sleep(500);
                } catch (Throwable throwable) {
                }
                data = CopyJobStatus.parseRequestResult(mDevice, requestResponse, checkHTTPResponse(requestResponse));
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Error error) {
                data = error;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            } catch (Throwable throwable) {
                data = new Error(ErrorName.Unknown, throwable.getMessage());
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            }
            return Message.obtain(null, requestID, result, 0, data);
        }
    };

    /**
     * Request handler for {@link #CancelCopyJob(UIContext, CopyJobID, int, OXPdDevice.RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mCancelCopyJobHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new CopySOAPRequestBuilder() {

                @Override
                public String getSoapOperation() {
                    return SOAP_OP__CANCEL_COPY_JOB;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    //noinspection unchecked
                    Pair<UIContext, CopyJobID> cancelJobRequest = (Pair<UIContext, CopyJobID>) requestParams;
                    xmlWriter.writeStartTag(XML_SCHEMA__OXPD_COPY, getSoapOperation(), null);
                    xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__UI_CONTEXT_ID, null, "%s", cancelJobRequest.first.getUIContext());
                    xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY_COPY_JOB_ID, null, "%s", cancelJobRequest.second.getCopyJobID());
                    xmlWriter.writeEndTag(XML_SCHEMA__OXPD_COPY, getSoapOperation());
                }
            });

            int result;
            Object data;
            try {
                RestXMLTagHandler tagHandler = checkHTTPResponse(requestResponse);
                mDevice.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);
                OXPdCopy.faultExceptionCheck(tagHandler);
                data = null;
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Error error) {
                data = error;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            } catch (Throwable throwable) {
                data = new Error(ErrorName.Unknown, throwable.getMessage());
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            }
            return Message.obtain(null, requestID, result, 0, data);
        }
    };

    /**
     * Request handler for {@link #DeleteStoredCopyJob(UIContext, String, String, int, OXPdDevice.RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mDeleteStoredCopyJobHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new CopySOAPRequestBuilder() {

                @Override
                public String getSoapOperation() {
                    return SOAP_OP__DELETE_STORED_COPY_JOB;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    //noinspection unchecked
                    DeleteCopyJobParams deleteJobRequest = (DeleteCopyJobParams) requestParams;
                    xmlWriter.writeStartTag(XML_SCHEMA__OXPD_COPY, getSoapOperation(), null);
                    xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY__UI_CONTEXT_ID, null, "%s", deleteJobRequest.mUIContext.getUIContext());
                    xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY_COPY_JOB_ID, null, "%s", deleteJobRequest.mJobId);
                    xmlWriter.writeTag(XML_SCHEMA__OXPD_COPY, Constants.XML_TAG__COPY_STORE_JOB_PASSWORD, null, "%s", deleteJobRequest.mJobPassword);
                    xmlWriter.writeEndTag(XML_SCHEMA__OXPD_COPY, getSoapOperation());
                }
            });

            int result;
            Object data;
            try {
                RestXMLTagHandler tagHandler = checkHTTPResponse(requestResponse);
                mDevice.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);
                OXPdCopy.faultExceptionCheck(tagHandler);
                data = null;
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Error error) {
                data = error;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            } catch (Throwable throwable) {
                data = new Error(ErrorName.Unknown, throwable.getMessage());
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            }
            return Message.obtain(null, requestID, result, 0, data);
        }
    };

    // CONSTRUCTORS

    /**
     * Constructor for Device Info instance
     *
     * @param device        OXPd device instance
     * @param discoveryTree Discovery tree for current OXPd device
     * @throws Error when something goes wrong
     * @hide
     */
    public OXPdCopy(OXPdDevice device, OXPdDevice.InternalServerHooks serverHooks, DiscoveryTree discoveryTree) throws Error {
        mDevice = device;
/*
        if (discoveryTree == null) throw new Error(ErrorName.AjaxError, "Connection failed");

        DiscoveredFeature feature =
                discoveryTree.GetDiscoveredFeatureByResourceTypeAndRevision(
                        OXPD_RESOURCE_TYPE__COPY,
                        OXPD_REVISION__COPY);
        if (feature == null) {
            throw new Error(ErrorName.ServiceNotFound, "OXPd:Copy is not supported on the target device");
        }

        try {
            mCopyURL = device.getOXPdUrl(feature.resourceURI);
        } catch (MalformedURLException e) {
            throw new Error(ErrorName.Unknown, "invalid URL");
        }
        mDevice.getXMLNSHandler().addXMLNS(XML_PREFIX_OXPD_COPY, OXPD_REVISION__COPY);
        mServerHooks = serverHooks;
        mSupportsStoreCopyJob = device.versionRequirementsSatisfied(feature.minorRevision, STORE_COPY_MINOR_VERSION__MIN);

 */
    }

    public OXPdCopy(IDeviceCopyJobService copyJobService) {
        this.mCopyJobService = copyJobService;
        mSupportsStoreCopyJob = true;
    }

    // METHODS

    /**
     * Check the parsed XML handler for errors
     *
     * @param tagHandler XML tag handler after parsing
     * @throws Error If errors are stored in tag handler
     */
    static void faultExceptionCheck(RestXMLTagHandler tagHandler) throws Error {
        if (tagHandler.getTagData(OXPdDevice.Constants.XML_TAG__XML_PARSE_EXCEPTION) != null) {
            throw new Error(ErrorName.AjaxError, "Parse failed");
        }

        SOAPFault fault = (SOAPFault) tagHandler.getTagData(OXPdDevice.Constants.XML_TAG__SOAP_FAULT);
        if (fault != null) {
            throw new Error(ErrorName.Unknown, fault);
        }
    }

    /**
     * Retrieves the device's GetCopyOptionsProfile.
     * A CopyOptionsProfile lists the values for all copy settings supported by the device
     *
     * @param requestID Request ID associated with this call
     * @param callback  Callback to invoke when operation is finished.
     */
    public void GetCopyOptionsProfile(int requestID, OXPdDevice.RequestCallback callback) {
        mDevice.queueRequest(mGetCopyOptionsProfileHandler, null, requestID, callback);
    }

    /**
     * Retrieves the device's GetDefaultCopyOptions.
     * A CopyOptions lists the values for a default copy job supported by the device
     *
     * @param requestID Request ID associated with this call
     * @param callback  Callback to invoke when operation is finished.
     */
    public void GetDefaultCopyOptions(int requestID, String packageName, OXPdDevice.RequestCallback callback) {
        mDevice.queueRequest(mGetDefaultCopyOptionsHandler, packageName, requestID, callback);
    }

    /**
     * Retrieves the device's GetCopyJobStatus.
     * A CopyJobStatus represents details of copy job with provided job ID.
     *
     * @param requestID Request ID associated with this call
     * @param callback  Callback to invoke when operation is finished.
     */
    public void GetCopyJobStatus(CopyJobID copyJobId, int requestID, OXPdDevice.RequestCallback callback) {
        mDevice.queueRequest(mGetCopyJobStatusHandler, copyJobId, requestID, callback);
    }

    /**
     * Retrieves the list of stored copy jobs.
     *
     * @param requestID Request ID associated with this call
     * @param callback  Callback to invoke when operation is finished.
     */
    public void EnumerateStoredCopyJobs(UIContext uiContext, int requestID, OXPdDevice.RequestCallback callback, String packageName) {
        mDevice.queueRequest(mEnumerateStoredCopyJobsHandler, new EnumerateStoredCopyJobParams(uiContext, requestID, callback, packageName), requestID, callback);
    }

    /**
     * Start a copy job with CopyTicket.
     *
     * @param uiContext         The app's uiContextId. The app must be in possession of a valid uiContextId in
     *                          order to start a copy job.
     * @param copyJobTicket     The copy ticket defining all of the options for this copy job.
     * @param statusRequestID   Request ID associated with status callback
     * @param jobStatusCallback Callback to invoke for status reporting
     * @param startJobRequestID Request ID associated with this call
     * @param startJobCallback  Callback to invoke when operation is finished.
     */
    public void StartCopyJob(UIContext uiContext, CopyJobTicket copyJobTicket,
                             int statusRequestID, OXPdDevice.RequestCallback jobStatusCallback,
                             int startJobRequestID, OXPdDevice.RequestCallback startJobCallback, String packageName) {
        if ((uiContext == null) || (copyJobTicket == null)) {
            if (startJobCallback != null) {
                startJobCallback.requestResult(mDevice, Message.obtain(null, startJobRequestID, OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION, 0,
                        (uiContext == null) ?
                                new Error(ErrorName.InvalidParameter, "Invalid ui context") :
                                new Error(ErrorName.InvalidParameter, "Invalid copy ticket")));
            }
        } else {
            // if destination is our local server - check server status and start it
            if (jobStatusCallback != null && !mDevice.checkServerStatus()) {
                if (startJobCallback != null) {
                    startJobCallback.requestResult(mDevice, Message.obtain(null, startJobRequestID, OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION, 0,
                            new Error(ErrorName.Unknown, "Internal server is failed to start")));
                }
            } else {
                mDevice.queueRequest(mStartCopyJobHandler, new CopyJobParams(uiContext, copyJobTicket, statusRequestID, jobStatusCallback, packageName), startJobRequestID, startJobCallback);
            }
        }
    }

    /**
     * Release a stored copy job with specified jobId.
     *
     * @param uiContext         The app's uiContextId. The app must be in possession of a valid uiContextId in
     *                          order to start a copy job.
     * @param releaseCopyTicket The ticket to release stored job
     * @param statusRequestID   Request ID associated with status callback
     * @param jobStatusCallback Callback to invoke for status reporting
     * @param startJobRequestID Request ID associated with this call
     * @param startJobCallback  Callback to invoke when operation is finished.
     */
    public void ReleaseCopyJob(UIContext uiContext, ReleaseCopyTicket releaseCopyTicket,
                               int statusRequestID, OXPdDevice.RequestCallback jobStatusCallback,
                               int startJobRequestID, OXPdDevice.RequestCallback startJobCallback, String packageName) {
        if (uiContext == null || releaseCopyTicket == null || releaseCopyTicket.jobId == null) {
            if (startJobCallback != null) {
                startJobCallback.requestResult(mDevice, Message.obtain(null, startJobRequestID, OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION, 0,
                        (uiContext == null) ?
                                new Error(ErrorName.InvalidParameter, "Invalid ui context") :
                                (releaseCopyTicket != null) ?
                                        new Error(ErrorName.InvalidParameter, "Invalid copy ticket") :
                                        new Error(ErrorName.InvalidParameter, "Invalid stored copy job ID")));
            }
        } else {
            // if destination is our local server - check server status and start it
            if (jobStatusCallback != null && !mDevice.checkServerStatus()) {
                if (startJobCallback != null) {
                    startJobCallback.requestResult(mDevice, Message.obtain(null, startJobRequestID, OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION, 0,
                            new Error(ErrorName.Unknown, "Internal server is failed to start")));
                }
            } else {
                mDevice.queueRequest(mReleaseCopyJobHandler, new ReleaseCopyJobParams(uiContext, releaseCopyTicket, statusRequestID, jobStatusCallback, packageName), startJobRequestID, startJobCallback);
            }
        }
    }

    /**
     * Delete a specific copy job.
     *
     * @param uiContext         The app's uiContextId. The app must be in possession of a valid uiContextId in
     *                          order to cancel a copy job.
     * @param copyJobID         The job ID of the copy job to delete.
     * @param storedJobPassword The job password.
     * @param requestID         Request ID associated with this call
     * @param callback          Callback to invoke when operation is finished.
     */
    public void DeleteStoredCopyJob(UIContext uiContext, String copyJobID, String storedJobPassword, int requestID, OXPdDevice.RequestCallback callback) {
        if ((uiContext == null) || (copyJobID == null)) {
            if (callback != null) {
                callback.requestResult(mDevice, Message.obtain(null, requestID, OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION, 0,
                        (uiContext == null) ?
                                new Error(ErrorName.InvalidParameter, "Invalid ui context") :
                                new Error(ErrorName.InvalidParameter, "Invalid copy job id")));
            }
        } else {
            mDevice.queueRequest(mDeleteStoredCopyJobHandler, new DeleteCopyJobParams(uiContext, copyJobID, storedJobPassword), requestID, callback);
        }
    }

    /**
     * Cancels a specific copy job.
     *
     * @param uiContext The app's uiContextId. The app must be in possession of a valid uiContextId in
     *                  order to cancel a copy job.
     * @param copyJobID The job ID of the copy job to cancel.
     * @param requestID Request ID associated with this call
     * @param callback  Callback to invoke when operation is finished.
     */
    public void CancelCopyJob(UIContext uiContext, CopyJobID copyJobID, int requestID, OXPdDevice.RequestCallback callback) {
        if ((uiContext == null) || (copyJobID == null)) {
            if (callback != null) {
                callback.requestResult(mDevice, Message.obtain(null, requestID, OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION, 0,
                        (uiContext == null) ?
                                new Error(ErrorName.InvalidParameter, "Invalid ui context") :
                                new Error(ErrorName.InvalidParameter, "Invalid copy job id")));
            }
        } else {
            mDevice.queueRequest(mCancelCopyJobHandler, Pair.create(uiContext, copyJobID), requestID, callback);
        }
    }

    /**
     * Configure the default handlers when processing a Copy SOAP message
     *
     * @return Default handler
     */
    private RestXMLTagHandler setupDefaultHandler() {
        return mDevice.createSOAPFaultHandler();
    }

    /**
     * Check the HTTP response for the request
     *
     * @param requestResponse HTTP request/response container
     * @return XML tag handler pre-configured to detect faults
     * @throws Error If HTTP response had an error
     */
    private RestXMLTagHandler checkHTTPResponse(HttpRequestResponseContainer requestResponse) throws Error {
        if (requestResponse.response != null) {
//            Log.i("[SIM]","requestResponse.response : "+requestResponse.response.getResponseCode());
            switch (requestResponse.response.getResponseCode()) {
                case HttpURLConnection.HTTP_OK:
                    return setupDefaultHandler();
                case HttpURLConnection.HTTP_NOT_FOUND:
                    throw new Error(ErrorName.NotFound, "404 Requested resource not found");
                case OXPdDevice.Constants.HTTP_SERVER_ERROR:
                    throw new Error(ErrorName.ServerError, "500 Internal server error");
                default:
                    throw new Error(ErrorName.Unknown, "Unknown error");
            }
        } else {
            throw new Error(ErrorName.AjaxError, "Connection failed");
        }
    }

    /**
     * Register handlers for job specific functions
     *
     * @param serverContext Internal job identifier
     */
    private void registerJobCallbacks(String serverContext) {
        mServerHooks.registerHook(OXPD_SERVER__COPY_CALLBACK + "/" + serverContext, mCopyCallbackRequestHandler);
    }

    /**
     * Unregister job specific handler
     *
     * @param serverContext Internal job identifier
     */
    private void unregisterJobCallbacks(String serverContext) {
        mServerHooks.unregisterHook(OXPD_SERVER__COPY_CALLBACK + "/" + serverContext);
    }

    /**
     * Return the server context portion of the uri
     *
     * @param uri Relative URI
     * @return Internal job identifier portion of uri
     */
    private String getServerContext(String uri) {
        String[] parts = uri.split("/");
        try {
            // verify it server context is a UUID
            return UUID.fromString(parts[parts.length - 1]).toString();
        } catch (Exception ignored) {
            return null;
        }
    }
}
