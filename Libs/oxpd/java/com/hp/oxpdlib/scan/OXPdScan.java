// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.os.Message;
import android.util.Pair;
import android.text.TextUtils;
import android.util.Log;

import com.hp.oxpdlib.OXPdDevice;
import com.hp.oxpdlib.SOAPFault;
import com.hp.oxpdlib.discovery.DiscoveredFeature;
import com.hp.oxpdlib.discovery.DiscoveryTree;
import com.hp.oxpdlib.uiconfiguration.UIContext;
import com.hp.sdd.jabberwocky.chat.HttpHeader;
import com.hp.sdd.jabberwocky.chat.HttpRequestResponseContainer;
import com.hp.sdd.jabberwocky.xml.RestXMLNSHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLTagHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import fi.iki.elonen.NanoHTTPD;

/** This library provides a CORS binding to the OXPd:Scan service on HP devices. */
@SuppressWarnings({"unused", "WeakerAccess"})
public class OXPdScan {
    private static final String TAG = "OXPdScan";

    // CONSTANTS
    /** SOAP scan service name */
    private static final String SOAP_SERVICE_SCAN = "IScanService";
    /** Scan XML prefix */
    private static final String XML_PREFIX_OXPD_SCAN = "scan";
    /** Scan OXPd resource name */
    private static final String OXPD_RESOURCE_TYPE__SCAN = "OXPd:Scan";
    /** Scan service revision */
    private static final String OXPD_REVISION__SCAN = "http://www.hp.com/schemas/imaging/OXPd/service/scan/2010/04/14";
    /** Scan SOAP GetScannerStatus method */
    private static final String SOAP_OP__GET_SCANNER_STATUS = "GetScannerStatus";
    /** Scan SOAP GetTransmissionModeProfile method */
    private static final String SOAP_OP__GET_TRANSMISSION_MODE_PROFILE = "GetTransmissionModeProfile";
    /** Scan SOAP GetBasicOptionsProfile method */
    private static final String SOAP_OP__GET_BASIC_OPTIONS_PROFILE = "GetBasicOptionsProfile";
    /** Scan SOAP GetBasicOptionsProfile method */
    private static final String SOAP_OP__GET_BASIC_OPTIONS_PROFILE2 = "GetBasicOptionsProfile2";
    /** Scan SOAP GetBasicOptionsProfile method */
    private static final String SOAP_OP__GET_BASIC_OPTIONS_PROFILE3 = "GetBasicOptionsProfile3";
    /** Scan SOAP GetDefaultBasicOptions method */
    private static final String SOAP_OP__GET_DEFAULT_BASIC_OPTIONS = "GetDefaultBasicOptions";
    /** Scan SOAP GetDefaultBasicOptions method */
    private static final String SOAP_OP__GET_DEFAULT_BASIC_OPTIONS2 = "GetDefaultBasicOptions2";
    /** Scan SOAP GetDefaultBasicOptions method */
    private static final String SOAP_OP__GET_DEFAULT_BASIC_OPTIONS3 = "GetDefaultBasicOptions3";
    /** Scan SOAP GetDefaultFileOptions method */
    private static final String SOAP_OP__GET_DEFAULT_FILE_OPTIONS = "GetDefaultFileOptions";
    /** Scan SOAP GetFileOptionsProfile method */
    private static final String SOAP_OP__GET_FILE_OPTIONS_PROFILE = "GetFileOptionsProfile";
    /** Scan SOAP ValidateScanTicket method */
    private static final String SOAP_OP__VALIDATE_SCAN_TICKET = "ValidateScanTicket";
    /** Scan SOAP ValidateScanTicket method */
    private static final String SOAP_OP__VALIDATE_SCAN_TICKET2 = "ValidateScanTicket2";
    /** Scan SOAP ValidateScanTicket method */
    private static final String SOAP_OP__VALIDATE_SCAN_TICKET3 = "ValidateScanTicket3";
    /** Scan SOAP CancelScanJob method */
    private static final String SOAP_OP__CANCEL_SCAN_JOB = "CancelScanJob";
    /** Scan SOAP StartScanJob method */
    private static final String SOAP_OP__START_SCAN_JOB = "StartScanJob";
    /** Scan SOAP StartScanJob method */
    private static final String SOAP_OP__START_SCAN_JOB2 = "StartScanJob2";
    /** Scan SOAP StartScanJob method */
    private static final String SOAP_OP__START_SCAN_JOB3 = "StartScanJob3";
    /** Scan SOAP StartRemoteScanJob2 method */
    private static final String SOAP_OP__START_REMOTE_SCAN_JOB = "StartRemoteScanJob2"; //TODO
    /** Scan SOAP GetScanJobStatus method */
    private static final String SOAP_OP__GET_SCAN_JOB_STATUS = "GetScanJobStatus";

    /** Composite XML prefix/revision/version for scan */
    private static final String XML_SCHEMA__OXPD_SCAN = XML_PREFIX_OXPD_SCAN + RestXMLNSHandler.XML_SEPARATOR+
            "http://www.hp.com/schemas/imaging/OXPd/service/scan/*"+RestXMLNSHandler.XML_SEPARATOR+"2010/04/14";
    /** Polling frequency in manual mode */
    private static final int STATUS_POLLING_FREQUENCY = 5000;
    /** Delay before switching to manual polling */
    private static final int STATUS_POLLING_DELAY = 60000;

    /** Minimum minor version required to support the GetUIAttributes call */
    private static final int[] VERSION2_MESSAGE_SUPPORT_MINOR_VERSION__MIN = { 1, 1 };
    private static final int[] VERSION3_MESSAGE_SUPPORT_MINOR_VERSION__MIN = { 1, 3 };
    /** Minimum minor version required to support the GetUIAttributes call */
    private static final int[] START_REMOTE_SCAN_JOB__MINOR_VERSION__MIN = { 1, 2 };

    /** Minor version of scan service */
    private static String mMinorVersion = "1.0";

    /**Scan callback receiver endpoint*/
    private static final String OXPD_SERVER__SCAN_CALLBACK = "ScanCallback";

    /**Scan files receiver endpoint*/
    private static final String OXPD_SERVER__SCAN_FILES = "ScanFiles";

    // CLASSES
    /**
     * Container for internal OXPd:Scan constants
     * @hide
     */
    static class Constants {
        /** XML tag containing {@link DestinationType} by {@link TransmissionMode} list */
        static final String XML_TAG__SCAN__DESTINATION_TYPES_BY_TRANSMISSION_MODES = "destinationTypesByTransmissionModes";
        /** XML tag containing {@link DestinationType} by {@link TransmissionMode} entry */
        static final String XML_TAG__SCAN__DESTINATION_TYPES_BY_TRANSMISSION_MODE = "destinationTypesByTransmissionMode";
        /** XML tag containing {@link TransmissionMode} value */
        static final String XML_TAG__SCAN__TRANSMISSION_MODE = "transmissionMode";
        /** XML tag containing {@link DestinationType} list */
        static final String XML_TAG__SCAN__DESTINATION_TYPES = "destinationTypes";
        /** XML tag containing {@link DestinationType} entry */
        static final String XML_TAG__SCAN__DESTINATION_TYPE = "destinationType";
        /** XML tag containing {@link OcrLanguage} list */
        static final String XML_TAG__SCAN__OCR_LANGUAGES = "ocrLanguages";
        /** XML tag containing {@link PdfCompressionMode} list */
        static final String XML_TAG__SCAN__PDF_COMPRESSION_MODES = "pdfCompressionModes";
        /** XML tag containing {@link TiffCompressionMode} list */
        static final String XML_TAG__SCAN__TIFF_COMPRESSION_MODES = "tiffCompressionModes";
        /** XML tag containing {@link XpsCompressionMode} list */
        static final String XML_TAG__SCAN__XPS_COMPRESSION_MODES = "xpsCompressionModes";
        /** XML tag containing {@link OcrLanguage} value */
        static final String XML_TAG__SCAN__OCR_LANGUAGE = "ocrLanguage";
        /** XML tag containing {@link PdfCompressionMode} value */
        static final String XML_TAG__SCAN__PDF_COMPRESSION_MODE = "pdfCompressionMode";
        /** XML tag containing {@link TiffCompressionMode} value */
        static final String XML_TAG__SCAN__TIFF_COMPRESSION_MODE = "tiffCompressionMode";
        /** XML tag containing {@link XpsCompressionMode} value */
        static final String XML_TAG__SCAN__XPS_COMPRESSION_MODE = "xpsCompressionMode";
        /** XML tag containing value for PDF encryption support
         * @see FileOptionsProfile#isPdfEncryptionPasswordSupported
         * */
        static final String XML_TAG__SCAN__IS_PDF_ENCRYPTION_PASSWORD_SUPPORTED = "isPdfEncryptionPasswordSupported";
        /** XML tag containing PDF encryption value */
        static final String XML_TAG__SCAN__PDF_ENCRYPTION_PASSWORD = "pdfEncryptionPassword";
        /** XML tag containing {@link FileType}  value*/
        static final String XML_TAG__SCAN__FILE_TYPE = "fileType";
        /** XML tag containing {@link ColorMode}  value*/
        static final String XML_TAG__SCAN__COLOR_MODE = "colorMode";
        /** XML tag containing {@link BackgroundCleanup} value */
        static final String XML_TAG__SCAN__BACKGROUND_CLEANUP = "backgroundCleanup";
        /** XML tag containing {@link BlankImageRemovalMode} value */
        static final String XML_TAG__SCAN__BLANK_IMAGE_REMOVAL_MODE = "blankImageRemovalMode";
        /** XML tag containing {@link ColorDropoutMode} value */
        static final String XML_TAG__SCAN__COLOR_DROPOUT_MODE = "colorDropoutMode";
        /** XML tag containing {@link ContrastAdjustment} value */
        static final String XML_TAG__SCAN__CONTRAST_ADJUSTMENT = "contrastAdjustment";
        /** XML tag containing {@link CropMode} value */
        static final String XML_TAG__SCAN__CROP_MODE = "cropMode";
        /** XML tag containing {@link CropMode} value since rev 1.1 */
        static final String XML_TAG__SCAN__CROP_MODE2 = "cropMode2";
        /**
         * XML tag containing custom scam length value
         * @see BasicOptions#customLength
         * */
        static final String XML_TAG__SCAN__CUSTOM_LENGTH = "customLength";
        /**
         * XML tag containing custom scam width value
         * @see BasicOptions#customWidth
         * */
        static final String XML_TAG__SCAN__CUSTOM_WIDTH = "customWidth";
        /** XML tag containing {@link DarknessAdjustment} value */
        static final String XML_TAG__SCAN__DARKNESS_ADJUSTMENT = "darknessAdjustment";
        /** XML tag containing {@link DuplexFormat} value */
        static final String XML_TAG__SCAN__DUPLEX_FORMAT = "duplexFormat";
        /** XML tag containing {@link JobAssemblyMode} value */
        static final String XML_TAG__SCAN__JOB_ASSEMBLY_MODE = "jobAssemblyMode";
        /** XML tag containing {@link MediaOrientation} value */
        static final String XML_TAG__SCAN__MEDIA_ORIENTATION = "mediaOrientation";
        /** XML tag containing {@link MediaSize} value */
        static final String XML_TAG__SCAN__MEDIA_SIZE = "mediaSize";
        /** XML tag containing {@link MediaSource} value */
        static final String XML_TAG__SCAN__MEDIA_SOURCE = "mediaSource";
        /** XML tag containing {@link MediaWeightAdjustment} value */
        static final String XML_TAG__SCAN__MEDIA_WEIGHT_ADJUSTMENT = "mediaWeightAdjustment";
        /** XML tag containing {@link MisfeedDetectionMode} value */
        static final String XML_TAG__SCAN__MISFEED_DETECTION_MODE = "misfeedDetectionMode";
        /** XML tag containing {@link OutputQuality} value */
        static final String XML_TAG__SCAN__OUTPUT_QUALITY = "outputQuality";
        /** XML tag containing {@link PlexMode} value */
        static final String XML_TAG__SCAN__PLEXMODE = "plexMode";
        /** XML tag containing {@link PreviewMode} value */
        static final String XML_TAG__SCAN__PREVIEW_MODE = "previewMode";
        /** XML tag containing {@link ProgressDialogMode} value */
        static final String XML_TAG__SCAN__PROGRESS_DIALOG_MODE = "progressDialogMode";
        /** XML tag containing {@link Resolution} value */
        static final String XML_TAG__SCAN__RESOLUTION = "resolution";
        /** XML tag containing {@link SharpnessAdjustment} value */
        static final String XML_TAG__SCAN__SHARPNESS_ADJUSTMENT = "sharpnessAdjustment";
        /** XML tag containing {@link TextPhotoOptimization} value */
        static final String XML_TAG__SCAN__TEXT_PHOTO_OPTIMIZATION = "textPhotoOptimization";
        /** XML tag containing {@link BackgroundCleanup} list */
        static final String XML_TAG__SCAN__BACKGROUND_CLEANUPS = "backgroundCleanups";
        /** XML tag containing {@link BlankImageRemovalMode} list */
        static final String XML_TAG__SCAN__BLANK_IMAGE_REMOVAL_MODES = "blankImageRemovalModes";
        /** XML tag containing {@link ColorDropoutMode} list */
        static final String XML_TAG__SCAN__COLOR_DROPOUT_MODES = "colorDropoutModes";
        /** XML tag containing {@link ContrastAdjustment} list */
        static final String XML_TAG__SCAN__CONTRAST_ADJUSTMENTS = "contrastAdjustments";
        /** XML tag containing {@link CropMode} list */
        static final String XML_TAG__SCAN__CROP_MODES = "cropModes";
        /** XML tag containing {@link DarknessAdjustment} list */
        static final String XML_TAG__SCAN__DARKNESS_ADJUSTMENTS = "darknessAdjustments";
        /** XML tag containing {@link DuplexFormat} list */
        static final String XML_TAG__SCAN__DUPLEX_FORMATS = "duplexFormats";
        /** XML tag containing {@link JobAssemblyMode} list */
        static final String XML_TAG__SCAN__JOB_ASSEMBLY_MODES = "jobAssemblyModes";
        /** XML tag containing {@link MediaOrientation} list */
        static final String XML_TAG__SCAN__MEDIA_ORIENTATIONS = "mediaOrientations";
        /** XML tag containing {@link MediaSize} list */
        static final String XML_TAG__SCAN__MEDIA_SIZES = "mediaSizes";
        /** XML tag containing {@link MediaSource} list */
        static final String XML_TAG__SCAN__MEDIA_SOURCES = "mediaSources";
        /** XML tag containing {@link MediaWeightAdjustment} list */
        static final String XML_TAG__SCAN__MEDIA_WEIGHT_ADJUSTMENTS = "mediaWeightAdjustments";
        /** XML tag containing {@link MisfeedDetectionMode} list */
        static final String XML_TAG__SCAN__MISFEED_DETECTION_MODES = "misfeedDetectionModes";
        /** XML tag containing {@link OutputQuality} list */
        static final String XML_TAG__SCAN__OUTPUT_QUALITIES = "outputQualities";
        /** XML tag containing {@link PlexMode} list */
        static final String XML_TAG__SCAN__PLEXMODES = "plexModes";
        /** XML tag containing {@link PreviewMode} list */
        static final String XML_TAG__SCAN__PREVIEW_MODES = "previewModes";
        /** XML tag containing {@link ProgressDialogMode} list */
        static final String XML_TAG__SCAN__PROGRESS_DIALOG_MODES = "progressDialogModes";
        /** XML tag containing {@link Resolution} list */
        static final String XML_TAG__SCAN__RESOLUTIONS = "resolutions";
        /** XML tag containing {@link SharpnessAdjustment} list */
        static final String XML_TAG__SCAN__SHARPNESS_ADJUSTMENTS = "sharpnessAdjustments";
        /** XML tag containing {@link TextPhotoOptimization} list */
        static final String XML_TAG__SCAN__TEXT_PHOTO_OPTIMIZATIONS = "textPhotoOptimizations";
        /** XML tag containing minimum scan length in inches */
        static final String XML_TAG__SCAN__CUSTOM_MIN_LENGTH = "customMinLength";
        /** XML tag containing minimum scan width in inches */
        static final String XML_TAG__SCAN__CUSTOM_MIN_WIDTH = "customMinWidth";
        /** XML tag containing maximum scan length in inches */
        static final String XML_TAG__SCAN__CUSTOM_MAX_LENGTH = "customMaxLength";
        /** XML tag containing maximum scan width in inches */
        static final String XML_TAG__SCAN__CUSTOM_MAX_WIDTH = "customMaxWidth";
        /** XML tag containing  (version 1.3)*/
        static final String XML_TAG__SCAN__SPLIT_ATTACHMENT_BY_PAGES = "splitAttachmentByPages";
        /** XML tag containing  (version 1.3)*/
        static final String XML_TAG__SCAN__MAX_PAGES_PER_ATTACHMENT = "maxPagesPerAttachment";
        /** XML tag containing  (version 1.3)*/
        static final String XML_TAG__SCAN__MAX_PAGES_PER_ATTACHMENT_MIN = "maxPagesPerAttachmentMin";
        /** XML tag containing  (version 1.3)*/
        static final String XML_TAG__SCAN__MAX_PAGES_PER_ATTACHMENT_MAX = "maxPagesPerAttachmentMax";
        /** XML tag containing  (version 1.3)*/
        static final String XML_TAG__SCAN__ERASE_MARGIN_UNITS = "eraseMarginUnits";
        /** XML tag containing  (version 1.3)*/
        static final String XML_TAG__SCAN__UNIT_OF_MEASURE = "UnitOfMeasure";
        /** XML tag containing  (version 1.3)*/
        static final String XML_TAG__SCAN__ERASE_BACK_BOTTOM = "eraseBackBottom";
        /** XML tag containing  (version 1.3)*/
        static final String XML_TAG__SCAN__ERASE_BACK_BOTTOM_MIN = "eraseBackBottomMin";
        /** XML tag containing  (version 1.3)*/
        static final String XML_TAG__SCAN__ERASE_BACK_BOTTOM_MAX = "eraseBackBottomMax";
        /** XML tag containing  (version 1.3)*/
        static final String XML_TAG__SCAN__ERASE_BACK_LEFT = "eraseBackLeft";
        /** XML tag containing  (version 1.3)*/
        static final String XML_TAG__SCAN__ERASE_BACK_LEFT_MIN = "eraseBackLeftMin";
        /** XML tag containing  (version 1.3)*/
        static final String XML_TAG__SCAN__ERASE_BACK_LEFT_MAX = "eraseBackLeftMax";
        /** XML tag containing  (version 1.3)*/
        static final String XML_TAG__SCAN__ERASE_BACK_RIGHT = "eraseBackRight";
        /** XML tag containing  (version 1.3)*/
        static final String XML_TAG__SCAN__ERASE_BACK_RIGHT_MIN = "eraseBackRightMin";
        /** XML tag containing  (version 1.3)*/
        static final String XML_TAG__SCAN__ERASE_BACK_RIGHT_MAX = "eraseBackRightMax";
        /** XML tag containing  (version 1.3)*/
        static final String XML_TAG__SCAN__ERASE_BACK_TOP = "eraseBackTop";
        /** XML tag containing  (version 1.3)*/
        static final String XML_TAG__SCAN__ERASE_BACK_TOP_MIN = "eraseBackTopMin";
        /** XML tag containing  (version 1.3)*/
        static final String XML_TAG__SCAN__ERASE_BACK_TOP_MAX = "eraseBackTopMax";
        /** XML tag containing  (version 1.3)*/
        static final String XML_TAG__SCAN__ERASE_FRONT_BOTTOM = "eraseFrontBottom";
        /** XML tag containing  (version 1.3)*/
        static final String XML_TAG__SCAN__ERASE_FRONT_BOTTOM_MIN = "eraseFrontBottomMin";
        /** XML tag containing  (version 1.3)*/
        static final String XML_TAG__SCAN__ERASE_FRONT_BOTTOM_MAX = "eraseFrontBottomMax";
        /** XML tag containing  (version 1.3)*/
        static final String XML_TAG__SCAN__ERASE_FRONT_LEFT = "eraseFrontLeft";
        /** XML tag containing  (version 1.3)*/
        static final String XML_TAG__SCAN__ERASE_FRONT_LEFT_MIN = "eraseFrontLeftMin";
        /** XML tag containing  (version 1.3)*/
        static final String XML_TAG__SCAN__ERASE_FRONT_LEFT_MAX = "eraseFrontLeftMax";
        /** XML tag containing  (version 1.3)*/
        static final String XML_TAG__SCAN__ERASE_FRONT_RIGHT = "eraseFrontRight";
        /** XML tag containing  (version 1.3)*/
        static final String XML_TAG__SCAN__ERASE_FRONT_RIGHT_MIN = "eraseFrontRightMin";
        /** XML tag containing  (version 1.3)*/
        static final String XML_TAG__SCAN__ERASE_FRONT_RIGHT_MAX = "eraseFrontRightMax";
        /** XML tag containing  (version 1.3)*/
        static final String XML_TAG__SCAN__ERASE_FRONT_TOP = "eraseFrontTop";
        /** XML tag containing  (version 1.3)*/
        static final String XML_TAG__SCAN__ERASE_FRONT_TOP_MIN = "eraseFrontTopMin";
        /** XML tag containing  (version 1.3)*/
        static final String XML_TAG__SCAN__ERASE_FRONT_TOP_MAX = "eraseFrontTopMax";
        /** XML tag containing  (version 1.3)*/
        static final String XML_TAG__SCAN__CAPTURE_MODES = "captureModes";
        /** XML tag containing  (version 1.3)*/
        static final String XML_TAG__SCAN__CAPTURE_MODE = "captureMode";
        /** XML tag containing  (version 1.3)*/
        static final String XML_TAG__SCAN__AUTOMATIC_TONE_MODES = "automaticToneModes";
        /** XML tag containing  (version 1.3)*/
        static final String XML_TAG__SCAN__AUTOMATIC_TONE_MODE = "automaticToneMode";
        /** XML tag containing  (version 1.3)*/
        static final String XML_TAG__SCAN__ENABLE_DISABLE_MODE = "EnabledDisabledMode";
        /** XML tag containing  (version 1.3)*/
        static final String XML_TAG__SCAN__AUTOMATIC_STRAIGHTEN_MODES = "automaticStraightenModes";
        /** XML tag containing  (version 1.3)*/
        static final String XML_TAG__SCAN__AUTOMATIC_STRAIGHTEN_MODE = "automaticStraightenMode";

        /** XML tag containing {@link FileTypesByColorMode} list */
        static final String XML_TAG__SCAN__FILE_TYPES_BY_COLOR_MODES = "fileTypesByColorModes";
        /** XML tag containing {@link FileTypesByColorMode} entry */
        static final String XML_TAG__SCAN__FILE_TYPES_BY_COLOR_MODE = "fileTypesByColorMode";
        /** XML tag containing {@link FileType} list */
        static final String XML_TAG__SCAN__FILE_TYPES = "fileTypes";
        /** XML tag containing {@link ScanTicket} values */
        static final String XML_TAG__SCAN__SCAN_TICKET = "scanTicket";
        /** XML tag containing {@link BasicOptions} values */
        static final String XML_TAG__SCAN__BASIC_OPTIONS = "basicOptions";
        /** XMl tag containing {@link Destination} values */
        static final String XML_TAG__SCAN__DESTINATION = "destination";
        /** XML tag containing {@link FileOptions} values */
        static final String XML_TAG__SCAN__FILE_OPTIONS = "fileOptions";
        /** XML tag containing username credentials */
        static final String XML_TAG__COMMON__USERNAME = "userName";
        /** XML tag containing password credentials */
        static final String XML_TAG__COMMON__PASSWORD = "password";
        /** XML tag containing domain credentials */
        static final String XML_TAG__COMMON__DOMAIN = "domain";
        /** XML tag containing network credentials values */
        static final String XML_TAG__COMMON__NETWORK_CREDENTIALS = "networkCredentials";
        /** XML tag containing connection timeout value */
        static final String XML_TAG__COMMON__CONNECTION_TIMEOUT = "connectionTimeout";
        /** XML tag containing response timeout value */
        static final String XML_TAG__COMMON__RESPONSE_TIMEOUT = "responseTimeout";
        /** XML tag containing retry count value */
        static final String XML_TAG__COMMON__MAX_CONSECUTIVE_RETRIES = "maxConsecutiveRetries";
        /** XML tag containing retry interval value */
        static final String XML_TAG__COMMON__RETRY_INTERVAL = "retryInterval";
        /** XML tag containing {@link Metadata} values */
        static final String XML_TAG__SCAN__METADATA = "metadata";
        /** XML tac containing {@link Metadata#fileNameExtension} value */
        static final String XML_TAG__SCAN__FILE_NAME_EXTENSION = "fileNameExtension";
        /** XML tac containing {@link Metadata#fileListDelimiter} value */
        static final String XML_TAG__SCAN__FILE_LIST_DELIMITER = "fileListDelimiter";
        /** XML tac containing {@link Metadata#fileNameBase} value */
        static final String XML_TAG__SCAN__FILE_NAME_BASE = "fileNameBase";
        /** XML tag containing {@link Metadata#formatString} value */
        static final String XML_TAG__SAN__FORMAT_STRING = "formatString";
        /** XML tag containing scan filename base */
        static final String XML_TAG__SCAN__SCAN_FILE_NAME_BASE = "scanFileNameBase";
        /** XML tag containing scan job ID */
        static final String XML_TAG__SCAN__SCAN_JOB_ID = "scanJobId";
        /** XML tag containing UI Context nonce */
        static final String XML_TAG__SCAN__UI_CONTEXT_ID = "uiContextId";
        /** XML tag containing scan callback */
        static final String XML_TAG__SCAN__CALLBACK = "callback";
        /** XML tag containing Server Context Identifier */
        static final String XML_TAG__SCAN__SERVER_CONTEXT_ID = "serverContextId";

        /**
         * Private constructor
         * @hide
         */
        private Constants(){}
    }

    /**
     * Holder for Scan Job parameters
     */
    private static class ScanJobParams {

        /** {@link UIContext} value */
        private final UIContext mUIContext;
        /** {@link ScanTicket} value */
        private final ScanTicket mScanTicket;
        /** {@link com.hp.oxpdlib.OXPdDevice.RequestCallback} status callback */
        private final OXPdDevice.RequestCallback mStatusCallback;
        /** Status callback request identifier */
        private final int mStatusRequestID;
        /** Server context */
        private final String mServerContext;

        /**
         * ScanJob parameter holder constructor
         * @param uiContext
         *              {@link UIContext} value
         * @param scanTicket
         *              {@link ScanTicket} value
         * @param statusRequestID
         *              Status request identifier
         * @param statusCallback
         *              {@link com.hp.oxpdlib.OXPdDevice.RequestCallback} status callback
         */
        private ScanJobParams(UIContext uiContext, ScanTicket scanTicket, int statusRequestID, OXPdDevice.RequestCallback statusCallback) {
            mUIContext = uiContext;
            mScanTicket = scanTicket;
            mStatusRequestID = statusRequestID;
            mStatusCallback = statusCallback;
            mServerContext = UUID.randomUUID().toString();
        }

        /**
         * ScanJob parameter holder constructor
         * @param uiContext
         *              {@link UIContext} value
         * @param scanTicket
         *              {@link ScanTicket} value
         */
        private ScanJobParams(UIContext uiContext, ScanTicket scanTicket) {
            this(uiContext, scanTicket, 0, null);
        }
    }

    /**
     * Container class of active scan jobs
     */
    private class ScanJobTracker {

        /** Internal job identifier */
        private final String mServerContext;
        /** OXPd Scan Job ID */
        private final ScanJobID mScanJobID;
        /** Status callback request identifier */
        private final int mStatusRequestID;
        /** Status callback */
        private OXPdDevice.RequestCallback mStatusCallback;
        /** Encryption Key */
        private final SecretKeySpec mSecretKey;
        /** Shared key algorithm */
        private final String mSharedKeyAlgorithm;

        /** Runnable to use if polling */
        private final Runnable mStatusPollingRunnable = new Runnable() {
            @Override
            public void run() {
                Message response = mGetScanJobStatusHandler.processRequest(mStatusRequestID, mScanJobID);
                if ((response != null) && (response.obj instanceof ScanJobStatus)) {
                    ScanJobStatus jobStatus = (ScanJobStatus)response.obj;
                    if (jobStatus.resultCode == ResultCode.Pending) {
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
         * @param serverContext Internal job identifier
         * @param scanJobID OXPd Scan Job ID
         * @param jobParams Scan job parameters
         */
        private ScanJobTracker(String serverContext, ScanJobID scanJobID, ScanJobParams jobParams) {
            mServerContext = serverContext;
            mScanJobID = scanJobID;
            mSecretKey = jobParams.mUIContext.getSharedSecret();
            mSharedKeyAlgorithm = jobParams.mUIContext.getSharedSecretAlgorithm();
            mStatusRequestID = jobParams.mStatusRequestID;
            mStatusCallback = jobParams.mStatusCallback;
        }
    }

    /**
     * Scan SOAP request builder
     */
    private abstract class ScanSOAPRequestBuilder implements OXPdDevice.SOAPRequestBuilder {
        /**
         * Return the ScanService URL
         * @return
         *          Scan service url
         */
        @Override
        public URL getURL() {
            return mScanURL;
        }

        /**
         * Return XML schemas used by scan
         * @return
         *          List of scan related schemas
         */
        @Override
        public List<String> getXMLSchemas() {
            return Collections.singletonList(XML_SCHEMA__OXPD_SCAN);
        }

        /**
         * Return service name
         * @return
         *          Service name
         */
        @Override
        public String getServiceName() {
            return SOAP_SERVICE_SCAN;
        }

        /**
         * Return the service revision string
         * @return
         *          Service revision
         */
        @Override
        public String getServiceRevision() {
            return OXPD_REVISION__SCAN;
        }

        /**
         * Return list of HTTP headers to include as part of request
         * @return
         *          Empty List
         */
        @Override
        public List<HttpHeader> getRequestHeaders() {
            return Collections.emptyList();
        }

        /**
         * Use admin credentials as part of request?
         * @return
         *          false, don't require credentials
         */
        @Override
        public boolean useAdminCredentials() {
            return false;
        }
    }

    /**
     * Scan callback data holder
     */
    private static class ScanCallbackData {
        /** Internal job ID */
        private final String mServerContext;
        /** SOAP Message */
        private final String mSOAPMessage;

        /**
         * Constructor
         * @param serverContext
         *          Internal job ID
         * @param body
         *          SOAP Message
         */
        private ScanCallbackData(String serverContext, String body) {
            mServerContext = serverContext;
            mSOAPMessage = body;
        }
    }

    /**
     * Scan file data container, used to pass information from the server to the request thread
     */
    private static class ScanFilesData {
        /** Internal job ID */
        private final String mServerContext;
        /** File list */
        private final List<File> mFiles;
        /** Were error detected */
        private final boolean mWithErrors;

        /**
         * Constructor
         * @param serverContext
         *          Internal job ID
         * @param files
         *          File list
         * @param withErrors
         *          Were error detected during transfer
         */
        private ScanFilesData(String serverContext, List<File> files, boolean withErrors) {
            mServerContext = serverContext;
            mFiles = Collections.unmodifiableList(files);
            mWithErrors = withErrors;
        }
    }

    /**
     * Scan file request handler
     */
    private class ScanFileRequestHandler implements OXPdDevice.HandleIncomingServerRequest {

        /** Key to decrypt data with */
        private final SecretKeySpec mCryptoKey;
        private final String mAlgorithm;

        /**
         * Constructor
         */
        private ScanFileRequestHandler() {
            this(null, null);
        }

        /**
         * Constructor
         * @param cryptoKey Key to use to decrypt data
         * @param algorithm Shared key algorithm
         */
        private ScanFileRequestHandler(SecretKeySpec cryptoKey, String algorithm) {
            mCryptoKey = cryptoKey;
            mAlgorithm = algorithm;
        }

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
            boolean errorsDetected = false;
            ArrayList<File> fileList = new ArrayList<File>();
            InputStream is = null;
            OutputStream os = null;

            try {
                String saveDir = uri.substring(uri.lastIndexOf("/")+1); // last segment of uri is UUID for job (not jobId!)
                File baseDir = mDevice.getScanTemporaryFolder();
                if (!TextUtils.isEmpty(saveDir)) {
                    baseDir = new File(baseDir, saveDir);
                    //noinspection ResultOfMethodCallIgnored
                    baseDir.mkdirs();
                }

                Cipher cipher = null;
                try {
                    mDevice.log(Log.DEBUG, ScanFileRequestHandler.class.getSimpleName(),
                            "Obtaining cipher instance for " + mAlgorithm);
                    cipher = (mCryptoKey != null) ? Cipher.getInstance(mAlgorithm) : null;
                } catch(Exception ignored) {
                    mDevice.log(Log.ERROR, ScanFileRequestHandler.class.getSimpleName(),
                            "Failed to get cipher instance for " + mAlgorithm);
                }

                byte[] buf = (cipher != null) ? new byte[65536] : null;
                byte[] iv = (cipher != null) ? new byte[16] : null;
                int ivBytes;
                boolean cipherInitialized;

                for(String key: incomingData.keySet()) {
                    // copy file since rename fails
                    File tempFile = new File(incomingData.get(key));
                    File newFile = new File(baseDir, incomingDataParameters.get(key).get(0));

                    if ((cipher == null) && tempFile.renameTo(newFile)) {
                        fileList.add(newFile);
                        continue;
                    }

                    // open files
                    is = new FileInputStream(tempFile);
                    os = new FileOutputStream(newFile);

                    ivBytes = 0;
                    cipherInitialized = false;

                    // copy data
                    int len, offset;
                    while(((len = is.read(buf)) > 0) && !errorsDetected) {
                        offset = 0;
                        if (cipher != null) {
                            if (!cipherInitialized) {
                                for( ; (ivBytes < iv.length) && (len > 0); ivBytes++, len--, offset++) {
                                    iv[ivBytes] = buf[offset];
                                }
                                if (ivBytes == iv.length) {
                                    try {
                                        mDevice.log(Log.DEBUG, ScanFileRequestHandler.class.getSimpleName(),
                                                "Initializing cipher");
                                        cipher.init(Cipher.DECRYPT_MODE, mCryptoKey, new IvParameterSpec(iv));
                                        cipherInitialized = true;
                                    } catch (Exception ignored) {
                                        mDevice.log(Log.ERROR, ScanFileRequestHandler.class.getSimpleName(),
                                                "Failed to initializing cipher");
                                        errorsDetected = true;
                                    }
                                }
                            }
                            if (cipherInitialized && (len > 0)) {
                                try {
                                    byte[] decrypted = cipher.update(buf, offset, len);
                                    if (decrypted != null) {
                                        os.write(decrypted);
                                    }
                                } catch(IllegalStateException ignored) {
                                    errorsDetected = true;
                                    mDevice.log(Log.ERROR, ScanFileRequestHandler.class.getSimpleName(),
                                            "Failure processing cipher");
                                }
                            }
                        } else {
                            os.write(buf, 0, len);
                        }
                    }
                    if (cipher != null) {
                        try {
                            os.write(cipher.doFinal());
                        } catch(Exception ignored){
                            mDevice.log(Log.ERROR, ScanFileRequestHandler.class.getSimpleName(),
                                    "Failure finishing cipher");
                            errorsDetected = true;
                        }
                    }

                    // close output
                    os.close();
                    os = null;

                    // add file to list
                    fileList.add(newFile);

                    // close input
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                Log.e(TAG, "Failed to save received file: " + e.getMessage());
                errorsDetected = true;
            } finally {
                try {
                    if (is != null) is.close();
                } catch(IOException ignored) {}
                try {
                    if (os != null) os.close();
                } catch(IOException ignored) {}
            }

            mDevice.queueRequest(mScanFilesReceivedRequestHandler, new ScanFilesData(getServerContext(uri), fileList, errorsDetected), 0, null);

            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, NanoHTTPD.MIME_PLAINTEXT, null, 0);
        }
    }

    // INTERFACES
    /**
     * Scan Files receiver callback
     */
    @SuppressWarnings("WeakerAccess")
    public interface ScanFilesReceivedCallback {
        /**
         * Files received callback
         * @param scanJobID Scan Job identifier
         * @param scanFiles Scan files received
         * @param withErrors Field received with errors?
         */
        void onFilesReceived(ScanJobID scanJobID, List<File> scanFiles, boolean withErrors);
    }


    // VARIABLES
    /** OXPd device instance */
    private final OXPdDevice mDevice;
    /** URL of scan service */
    private final URL mScanURL;

    /** Version 2 message support */
    private final boolean mSupportsVersion2Messages;
    /** Version 3 message support */
    private final boolean mSupportsVersion3Messages;
    /** Remote Scan Job support */
    private final boolean mSupportsStartRemoteScanJob;
    /** Hooks to register with internal server */
    private final OXPdDevice.InternalServerHooks mServerHooks;
    /** Scan job mapping of active jobs */
    private final HashMap<String,ScanJobTracker> mJobTrackerMap = new HashMap<String,ScanJobTracker>();
    /** Registered scan file receiver callbacks */
    private final List<ScanFilesReceivedCallback> mScanFileReceivers = new ArrayList<ScanFilesReceivedCallback>();
    /** Scan callback handler */
    private final OXPdDevice.HandleIncomingServerRequest mScanCallbackRequestHandler = new OXPdDevice.HandleIncomingServerRequest() {
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
                mDevice.queueRequest(mScanCallbackEventRequestHandler,
                        new ScanCallbackData(getServerContext(uri), soapMessageBody),
                        0, null
                );
            }

            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.ACCEPTED, OXPdDevice.Constants.CONTENT_TYPE__SOAP_XML, null, 0);
        }
    };

    private final ScanFileRequestHandler mDefaultScanFileRequestHandler = new ScanFileRequestHandler();

    /**
     * Request handler for {@link #GetScannerStatus(int, OXPdDevice.RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mGetScannerStatusHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, Object requestParams) {

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new ScanSOAPRequestBuilder() {

                @Override
                public String getSoapOperation() {
                    return SOAP_OP__GET_SCANNER_STATUS;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, getSoapOperation(), null, null);
                }
            });

            int result;
            Object data;
            try {
                data = ScannerStatus.parseRequestResult(mDevice, requestResponse, checkHTTPResponse(requestResponse));
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Error error) {
                data = error;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            } catch (Throwable throwable) {
                data = new Error(ErrorName.Unknown, throwable.getMessage());
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            }
            return Message.obtain(null, requestID, result, 0,  data);
        }
    };

    /**
     * Request handler for {@link #GetTransmissionModeProfile(int, OXPdDevice.RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mGetTransmissionModeProfileHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, Object requestParams) {

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new ScanSOAPRequestBuilder() {

                @Override
                public String getSoapOperation() {
                    return SOAP_OP__GET_TRANSMISSION_MODE_PROFILE;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, getSoapOperation(), null, null);
                }
            });

            int result;
            Object data;
            try {
                data = TransmissionModeProfile.parseRequestResult(mDevice, requestResponse, checkHTTPResponse(requestResponse));
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Error error) {
                data = error;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            } catch (Throwable throwable) {
                data = new Error(ErrorName.Unknown, throwable.getMessage());
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            }
            return Message.obtain(null, requestID, result, 0,  data);
        }
    };

    /**
     * Request handler for {@link #GetBasicOptionsProfile(TransmissionMode, int, OXPdDevice.RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mGetBasicOptionsProfileHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new ScanSOAPRequestBuilder() {

                @Override
                public String getSoapOperation() {
                    if(mSupportsVersion3Messages) return SOAP_OP__GET_BASIC_OPTIONS_PROFILE3;
                    return (mSupportsVersion2Messages ? SOAP_OP__GET_BASIC_OPTIONS_PROFILE2 : SOAP_OP__GET_BASIC_OPTIONS_PROFILE);
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    xmlWriter.writeStartTag(XML_SCHEMA__OXPD_SCAN, getSoapOperation(), null);
                    xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__TRANSMISSION_MODE, null, "%s", ((TransmissionMode)requestParams).mValue);
                    xmlWriter.writeEndTag(XML_SCHEMA__OXPD_SCAN, getSoapOperation());
                }
            });

            int result;
            Object data;
            try {
                data = BasicOptionsProfile.parseRequestResult(mDevice, requestResponse, checkHTTPResponse(requestResponse));
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Error error) {
                data = error;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            } catch (Throwable throwable) {
                data = new Error(ErrorName.Unknown, throwable.getMessage());
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            }
            return Message.obtain(null, requestID, result, 0,  data);
        }
    };

    /**
     * Request handler for {@link #GetDefaultBasicOptions(TransmissionMode, int, OXPdDevice.RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mGetDefaultBasicOptionsHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID,final Object requestParams) {

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new ScanSOAPRequestBuilder() {

                @Override
                public String getSoapOperation() {
//                    Log.i("[SIM]","mSupportsVersion3Messages : "+mSupportsVersion3Messages);
                    if(mSupportsVersion3Messages) return SOAP_OP__GET_DEFAULT_BASIC_OPTIONS3;
                    return (mSupportsVersion2Messages ? SOAP_OP__GET_DEFAULT_BASIC_OPTIONS2 : SOAP_OP__GET_DEFAULT_BASIC_OPTIONS);
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    xmlWriter.writeStartTag(XML_SCHEMA__OXPD_SCAN, getSoapOperation(), null);
                    xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__TRANSMISSION_MODE, null, "%s", ((TransmissionMode)requestParams).mValue);
                    xmlWriter.writeEndTag(XML_SCHEMA__OXPD_SCAN, getSoapOperation());
                }
            });

            int result;
            Object data;
            try {
                data = BasicOptions.parseRequestResult(mDevice, requestResponse, checkHTTPResponse(requestResponse));

                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Error error) {
                Log.e("[SIM]",error.getMessage(),error);
                data = error;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            } catch (Throwable throwable) {
                data = new Error(ErrorName.Unknown, throwable.getMessage());
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            }
            return Message.obtain(null, requestID, result, 0,  data);
        }
    };

    /**
     *  Request handler for {@link #GetDefaultFileOptions(FileType, ColorMode, int, OXPdDevice.RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mGetDefaultFileOptionsHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new ScanSOAPRequestBuilder() {

                @Override
                public String getSoapOperation() {
                    return SOAP_OP__GET_DEFAULT_FILE_OPTIONS;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    //noinspection unchecked
                    Pair<FileType, ColorMode> requestParamsPair = (Pair<FileType, ColorMode>) requestParams;

                    xmlWriter.writeStartTag(XML_SCHEMA__OXPD_SCAN, getSoapOperation(), null);
                    xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__FILE_TYPE, null, "%s", requestParamsPair.first.mValue);
                    xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__COLOR_MODE, null, "%s", requestParamsPair.second.mValue);
                    xmlWriter.writeEndTag(XML_SCHEMA__OXPD_SCAN, getSoapOperation());
                }
            });

            int result;
            Object data;
            try {
                data = FileOptions.parseRequestResult(mDevice, requestResponse, checkHTTPResponse(requestResponse));
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Error error) {
                data = error;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            } catch (Throwable throwable) {
                data = new Error(ErrorName.Unknown, throwable.getMessage());
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            }
            return Message.obtain(null, requestID, result, 0,  data);
        }
    };

    /**
     * Request handler for {@link #GetFileOptionsProfile(FileType, ColorMode, int, OXPdDevice.RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mGetFileOptionsProfileHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new ScanSOAPRequestBuilder() {

                @Override
                public String getSoapOperation() {
                    return SOAP_OP__GET_FILE_OPTIONS_PROFILE;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    //noinspection unchecked
                    Pair<FileType, ColorMode> requestParamsPair = (Pair<FileType, ColorMode>) requestParams;
                    xmlWriter.writeStartTag(XML_SCHEMA__OXPD_SCAN, getSoapOperation(), null);
                    xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__FILE_TYPE, null, "%s", requestParamsPair.first.mValue);
                    xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__COLOR_MODE, null, "%s", requestParamsPair.second.mValue);
                    xmlWriter.writeEndTag(XML_SCHEMA__OXPD_SCAN, getSoapOperation());
                }
            });

            int result;
            Object data;
            try {
                data = FileOptionsProfile.parseRequestResult(mDevice, requestResponse, checkHTTPResponse(requestResponse));
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Error error) {
                data = error;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            } catch (Throwable throwable) {
                data = new Error(ErrorName.Unknown, throwable.getMessage());
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            }
            return Message.obtain(null, requestID, result, 0,  data);
        }
    };

    /**
     * Request handler for {@link #ValidateScanTicket(ScanTicket, int, OXPdDevice.RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mValidateScanTicketHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(final int requestID, final Object requestParams) {

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new ScanSOAPRequestBuilder() {
                @Override
                public List<String> getXMLSchemas() {
                    return Arrays.asList(XML_SCHEMA__OXPD_SCAN, OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON);
                }

                @Override
                public String getSoapOperation() {
                    if(mSupportsVersion3Messages) return SOAP_OP__VALIDATE_SCAN_TICKET3;
                    return (mSupportsVersion2Messages ? SOAP_OP__VALIDATE_SCAN_TICKET2 : SOAP_OP__VALIDATE_SCAN_TICKET);
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    xmlWriter.writeStartTag(XML_SCHEMA__OXPD_SCAN, getSoapOperation(), null);
                    addScanTicketPayload((ScanJobParams) requestParams, xmlWriter);
                    xmlWriter.writeEndTag(XML_SCHEMA__OXPD_SCAN, getSoapOperation());
                }
            });

            int result;
            Object data;
            try {
                data = ScanTicketValidation.parseRequestResult(mDevice, requestResponse, checkHTTPResponse(requestResponse));
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Error error) {
                data = error;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            } catch (Throwable throwable) {
                data = new Error(ErrorName.Unknown, throwable.getMessage());
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            }
            return Message.obtain(null, requestID, result, 0,  data);
        }
    };

    /**
     * Request handler for {@link #StartScanJob(UIContext, ScanTicket, int, OXPdDevice.RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mStartScanJobHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {

            //noinspection unchecked
            final ScanJobParams scanJobRequest = (ScanJobParams)requestParams;
            final boolean isLocalDestination = isDestinationLocal(scanJobRequest.mScanTicket.destination);
            final boolean encryptedScan = (isLocalDestination && mSupportsStartRemoteScanJob && (scanJobRequest.mUIContext.getSharedSecret() != null));
            registerJobCallbacks(scanJobRequest.mServerContext,
                    isLocalDestination,
                    (encryptedScan ?
                        scanJobRequest.mUIContext.getSharedSecret() : null),
                    scanJobRequest.mUIContext.getSharedSecretAlgorithm());

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new ScanSOAPRequestBuilder() {

                @Override
                public List<String> getXMLSchemas() {
                    return Arrays.asList(XML_SCHEMA__OXPD_SCAN, OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON);
                }

                @Override
                public String getSoapOperation() {
                    if(encryptedScan) return SOAP_OP__START_REMOTE_SCAN_JOB; //remote job

                    if(mSupportsVersion3Messages) return SOAP_OP__START_SCAN_JOB3;

                    return (mSupportsVersion2Messages ? SOAP_OP__START_SCAN_JOB2 : SOAP_OP__START_SCAN_JOB);
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    //noinspection unchecked
//                    Log.i("[SIM]","scanJobRequest.mUIContext.getUIContext() : "+scanJobRequest.mUIContext.getUIContext());
                    xmlWriter.writeStartTag(XML_SCHEMA__OXPD_SCAN, getSoapOperation(), null);
                    xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__UI_CONTEXT_ID, null, "%s", scanJobRequest.mUIContext.getUIContext());
                    xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__SERVER_CONTEXT_ID, null, "%s", scanJobRequest.mServerContext);
                    addScanTicketPayload(scanJobRequest, xmlWriter);
                    xmlWriter.writeEndTag(XML_SCHEMA__OXPD_SCAN, getSoapOperation());
                }
            });

            RestXMLWriter xmlWriter = new RestXMLWriter(mDevice.getXMLNSHandler(),
                    false,
                    OXPdDevice.Constants.XML_SCHEMA__XSI,
                    OXPdDevice.Constants.XML_SCHEMA__XSD,
                    OXPdDevice.Constants.XML_SCHEMA__SOAP_ENVELOPE,
                    OXPdDevice.Constants.XML_SCHEMA__SOAP_ADDRESSING,
                    XML_SCHEMA__OXPD_SCAN,
                    OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON);

            int result;
            Object data;
            try {
                data = ScanJobID.parseRequestResult(mDevice, requestResponse, checkHTTPResponse(requestResponse));
                ScanJobTracker tracker = new ScanJobTracker(scanJobRequest.mServerContext, (ScanJobID)data, scanJobRequest);
                mJobTrackerMap.put(tracker.mServerContext, tracker);
                mDevice.queueDelayedCallback(tracker.mStatusPollingRunnable, STATUS_POLLING_DELAY);
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Error error) {
                unregisterJobCallbacks(scanJobRequest.mServerContext);
                data = error;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            } catch (Throwable throwable) {
                data = new Error(ErrorName.Unknown, throwable.getMessage());
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            }
            return Message.obtain(null, requestID, result, 0,  data);
        }
    };

    /**
     * Request handler for {@link #CancelScanJob(UIContext, ScanJobID, int, OXPdDevice.RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mCancelScanJobHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new ScanSOAPRequestBuilder() {

                @Override
                public String getSoapOperation() {
                    return SOAP_OP__CANCEL_SCAN_JOB;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    //noinspection unchecked
                    Pair<UIContext,ScanJobID> cancelJobRequest = (Pair<UIContext, ScanJobID>)requestParams;
                    xmlWriter.writeStartTag(XML_SCHEMA__OXPD_SCAN, getSoapOperation(), null);
                    xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__UI_CONTEXT_ID, null, "%s", cancelJobRequest.first.getUIContext());
                    xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__SCAN_JOB_ID, null, "%s", cancelJobRequest.second.getScanJobID());
                    xmlWriter.writeEndTag(XML_SCHEMA__OXPD_SCAN, getSoapOperation());
                }
            });

            int result;
            Object data;
            try {
                RestXMLTagHandler tagHandler = checkHTTPResponse(requestResponse);
                mDevice.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);
                OXPdScan.faultExceptionCheck(tagHandler);
                data = null;
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Error error) {
                data = error;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            } catch (Throwable throwable) {
                data = new Error(ErrorName.Unknown, throwable.getMessage());
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            }
            return Message.obtain(null, requestID, result, 0,  data);
        }
    };

    /**
     * Request handler for {@link #ScanJobStatus(ScanJobID, int, OXPdDevice.RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mGetScanJobStatusHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new ScanSOAPRequestBuilder() {

                @Override
                public String getSoapOperation() {
                    return SOAP_OP__GET_SCAN_JOB_STATUS;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    xmlWriter.writeStartTag(XML_SCHEMA__OXPD_SCAN, getSoapOperation(), null);
                    xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__SCAN_JOB_ID, null, "%s", ((ScanJobID)requestParams).getScanJobID());
                    xmlWriter.writeEndTag(XML_SCHEMA__OXPD_SCAN, getSoapOperation());
                }
            });

            int result;
            Object data;
            try {
                data = ScanJobStatus.parseRequestResult(mDevice, requestResponse, checkHTTPResponse(requestResponse));
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Error error) {
                data = error;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            } catch (Throwable throwable) {
                data = new Error(ErrorName.Unknown, throwable.getMessage());
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            }
            return Message.obtain(null, requestID, result, 0,  data);
        }
    };

    /** Request handler for {@link #registerScanFileReceiverCallback(ScanFilesReceivedCallback)} */
    private final OXPdDevice.DeviceProcessRequestCallback mRegisterScanFileReceiverCallback = new OXPdDevice.DeviceProcessRequestCallback() {

        @Override
        public Message processRequest(int requestID, Object requestParams) {
            ScanFilesReceivedCallback callback = (ScanFilesReceivedCallback)requestParams;
            if (!mScanFileReceivers.contains(callback)) {
                mScanFileReceivers.add(callback);
            }
            return null;
        }
    };

    /** Request handler for {@link #unregisterScanFileReceiverCallback(ScanFilesReceivedCallback)} */
    private final OXPdDevice.DeviceProcessRequestCallback mUnregisterScanFileReceiverCallback = new OXPdDevice.DeviceProcessRequestCallback() {

        @Override
        public Message processRequest(int requestID, Object requestParams) {
            ScanFilesReceivedCallback callback = (ScanFilesReceivedCallback)requestParams;
            mScanFileReceivers.remove(callback);
            return null;
        }
    };

    // CONSTRUCTORS
    /**
     * Constructor for Device Info instance
     * @param device
     *              OXPd device instance
     * @param serverHooks
     *              ServerHooks to register URLs
     * @param discoveryTree
     *              Discovery tree for current OXPd device
     * @throws Error
     *              when something goes wrong
     * @hide
     */
    public OXPdScan(OXPdDevice device, OXPdDevice.InternalServerHooks serverHooks, DiscoveryTree discoveryTree) throws Error {
        mDevice = device;
        if (discoveryTree == null) throw new Error(ErrorName.AjaxError, "Connection failed");

        DiscoveredFeature feature =
                discoveryTree.GetDiscoveredFeatureByResourceTypeAndRevision(
                        OXPD_RESOURCE_TYPE__SCAN,
                        OXPD_REVISION__SCAN);
        if (feature == null) {
            throw new Error(ErrorName.ServiceNotFound, "OXPd:Scan is not supported on the target device");
        }

        try {
            mScanURL = device.getOXPdUrl(feature.resourceURI);
        } catch (MalformedURLException e) {
            throw new Error(ErrorName.Unknown, "invalid URL");
        }

        try {
            mMinorVersion = feature.minorRevision;
        } catch (Throwable throwable) {
        }

        Log.i("[SIM]","feature.minorRevision : "+feature.minorRevision);
        mSupportsVersion2Messages = device.versionRequirementsSatisfied(feature.minorRevision, VERSION2_MESSAGE_SUPPORT_MINOR_VERSION__MIN);
        mSupportsVersion3Messages = device.versionRequirementsSatisfied(feature.minorRevision, VERSION3_MESSAGE_SUPPORT_MINOR_VERSION__MIN);

        mSupportsStartRemoteScanJob = device.versionRequirementsSatisfied(feature.minorRevision, START_REMOTE_SCAN_JOB__MINOR_VERSION__MIN);

        mDevice.getXMLNSHandler().addXMLNS(XML_PREFIX_OXPD_SCAN, OXPD_REVISION__SCAN);
        mServerHooks = serverHooks;
    }

    // METHODS
    /**
     * Check the parsed XML handler for errors
     * @param tagHandler
     *              XML tag handler after parsing
     * @throws Error
     *              If errors are stored in tag handler
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
     * Returns true if minorVersion is supported on a printer
     * @param minorVersion
     *              minorVersion to want to verify
     */
    static boolean isSupportedScanMinorVersion(String minorVersion) {
        if (!TextUtils.isEmpty(minorVersion) && !TextUtils.isEmpty(mMinorVersion)) {
            final String versionSplitter = "\\.";
            String[] splits = mMinorVersion.split(versionSplitter);
            String[] splitsForScan = minorVersion.split(versionSplitter);
            boolean supported = (splits.length > 0 && splitsForScan.length > 0);
            for(int index = 0; supported && ((index < splits.length) || (index < splitsForScan.length)); index++) {
                int valA, valB;
                valA = valB = 0;
                try {
                    if (index < splits.length) valA = Integer.valueOf(splits[index]);
                } catch(NumberFormatException ignored) {}
                try {
                    if (index < splitsForScan.length) valB = Integer.valueOf(splitsForScan[index]);
                } catch(NumberFormatException ignored) {}

                supported = (valA >= valB);
            }
            return supported;
        }
        return false;
    }

    /**
     * Retrieves the target device's ScannerStatus
     * @param requestID
     *              Request id associated with this call
     * @param callback
     *              Callback to invoke when operation is finished
     */
    public void GetScannerStatus(int requestID, OXPdDevice.RequestCallback callback) {
        mDevice.queueRequest(mGetScannerStatusHandler, null, requestID, callback);
    }

    /**
     * Retrieves the target device's TransmissionModeProfile which list the DestinationTypes
     * supported for each TransmissionMode.
     * @param requestID Request id associated with this call
     * @param callback Callback to invoke when operation is finished
     */
    public void GetTransmissionModeProfile(int requestID, OXPdDevice.RequestCallback callback) {
        mDevice.queueRequest(mGetTransmissionModeProfileHandler, null, requestID, callback);
    }

    /**
     * Retrieves the device's BasicOptionsProfile.
     * A BasicOptionsProfile lists the values for all 'basic' scan settings (settings that apply
     * to all file types and color modes) supported by the device for a specific TransmissionMode.
     * @param mode
     *              The chosen TransmissionMode
     * @param requestID
     *              Request ID associated with this call
     * @param callback
     *              Callback to invoke when operation is finished.
     */
    public void GetBasicOptionsProfile(TransmissionMode mode, int requestID, OXPdDevice.RequestCallback callback) {
        if (mode == null) {
            if (callback  != null) {
                callback.requestResult(mDevice, Message.obtain(null, requestID, OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION, 0, new Error(ErrorName.InvalidParameter, "Invalid transmission mode")));
            }
        } else {
            mDevice.queueRequest(mGetBasicOptionsProfileHandler, mode, requestID, callback);
        }
    }

    /**
     * Retrieves the device's default BasicOptions. The BasicOptions contains all settings preset
     * to the values preferred by the device for a specific TransmissionMode
     * @param mode
     *              The chosen TransmissionMode
     * @param requestID
     *              Request ID associated with this call
     * @param callback
     *              Callback to invoke when operation is finished.
     */
    public void GetDefaultBasicOptions(TransmissionMode mode, int requestID, OXPdDevice.RequestCallback callback) {
        if (mode == null) {
            if (callback  != null) {
                callback.requestResult(mDevice, Message.obtain(null, requestID, OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION, 0, new Error(ErrorName.InvalidParameter, "Invalid transmission mode")));
            }
        } else {
            mDevice.queueRequest(mGetDefaultBasicOptionsHandler, mode, requestID, callback);
        }
    }

    /**
     * Retrieves the device's default FileOptions. FileOptions contains all settings preset to the
     * values preferred by the device for a specific FileType and ColorMode combination.
     * @param fileType
     *              The chosen FileType
     * @param colorMode
     *              The chosen ColorMode
     * @param requestID
     *              Request ID associated with this call
     * @param callback
     *              Callback to invoke when operation is finished.
     */
    public void GetDefaultFileOptions(FileType fileType, ColorMode colorMode, int requestID, OXPdDevice.RequestCallback callback) {
        if ((fileType == null) || (colorMode == null)) {
            if (callback  != null) {
                callback.requestResult(mDevice, Message.obtain(null, requestID, OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION, 0,
                        (fileType == null) ?
                                new Error(ErrorName.InvalidParameter, "Invalid file type") : new Error(ErrorName.InvalidParameter, "Invalid color mode")));
            }
        } else {
            mDevice.queueRequest(mGetDefaultFileOptionsHandler, Pair.create(fileType,colorMode), requestID, callback);
        }
    }

    /**
     * Retrieves the device's FileOptionsProfile.
     * A FileOptionsProfile lists all of the values for all scan settings supported by the device
     * that apply to a specific FileType and ColorMode combination.
     * @param fileType
     *              The chosen FileType
     * @param colorMode
     *              The chosen ColorMode
     * @param requestID
     *              Request ID associated with this call
     * @param callback
     *              Callback to invoke when operation is finished.
     */
    public void GetFileOptionsProfile(FileType fileType, ColorMode colorMode, int requestID, OXPdDevice.RequestCallback callback) {
        if ((fileType == null) || (colorMode == null)) {
            if (callback  != null) {
                callback.requestResult(mDevice, Message.obtain(null, requestID, OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION, 0,
                        (fileType == null) ?
                                new Error(ErrorName.InvalidParameter, "Invalid file type") : new Error(ErrorName.InvalidParameter, "Invalid color mode")));
            }
        } else {
            mDevice.queueRequest(mGetFileOptionsProfileHandler, Pair.create(fileType,colorMode), requestID, callback);
        }
    }

    /**
     * Validates a ScanTicket against the device.
     * @param scanTicket
     *              The ScanTicket to validate
     * @param requestID
     *              Request ID associated with this call
     * @param callback
     *              Callback to invoke when operation is finished.
     */
    public void ValidateScanTicket(ScanTicket scanTicket, int requestID, OXPdDevice.RequestCallback callback) {
        if (scanTicket == null) {
            if (callback  != null) {
                callback.requestResult(mDevice, Message.obtain(null, requestID, OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION, 0,
                        new Error(ErrorName.InvalidParameter, "Invalid scan ticket")));
            }
        } else {
            mDevice.queueRequest(mValidateScanTicketHandler, new ScanJobParams(null, scanTicket, 0, null), requestID, callback);
        }
    }

    /**
     * Validates a ScanTicket against the device.
     * @param uiContext
     *              The app's uiContextId. The app must be in possession of a valid uiContextId in
     *              order to start a scan job.
     * @param scanTicket
     *              The scan ticket defining all of the options for this scan job.
     * @param requestID
     *              Request ID associated with this call
     * @param callback
     *              Callback to invoke when operation is finished.
     */
    public void StartScanJob(UIContext uiContext, ScanTicket scanTicket, int requestID, OXPdDevice.RequestCallback callback) {
        StartScanJob(uiContext, scanTicket, 0, null, requestID, callback);
    }

    /**
     * Validates a ScanTicket against the device.
     * @param uiContext
     *              The app's uiContextId. The app must be in possession of a valid uiContextId in
     *              order to start a scan job.
     * @param scanTicket
     *              The scan ticket defining all of the options for this scan job.
     * @param statusRequestID
     *              Request ID associated with status callback
     * @param jobStatusCallback
     *              Callback to invoke for status reporting
     * @param startJobRequestID
     *              Request ID associated with this call
     * @param startJobCallback
     *              Callback to invoke when operation is finished.
     */
    public void StartScanJob(UIContext uiContext, ScanTicket scanTicket, int statusRequestID, OXPdDevice.RequestCallback jobStatusCallback, int startJobRequestID, OXPdDevice.RequestCallback startJobCallback) {
        if ((uiContext == null) || (scanTicket == null)) {
            if (startJobCallback != null) {
                startJobCallback.requestResult(mDevice, Message.obtain(null, startJobRequestID, OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION, 0,
                        (uiContext == null) ?
                                new Error(ErrorName.InvalidParameter, "Invalid ui context") :
                                new Error(ErrorName.InvalidParameter, "Invalid scan ticket")));
            }
        } else {
            // if destination is our local server or job status callback is requested - check server status and start it
            if ((isDestinationLocal(scanTicket.destination) || jobStatusCallback != null) && !mDevice.checkServerStatus()) {
                if (startJobCallback != null) {
                    startJobCallback.requestResult(mDevice, Message.obtain(null, startJobRequestID, OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION, 0,
                                    new Error(ErrorName.Unknown, "Internal server is failed to start")));
                }
            } else {
                mDevice.queueRequest(mStartScanJobHandler, new ScanJobParams(uiContext,scanTicket, statusRequestID, jobStatusCallback), startJobRequestID, startJobCallback);
            }
        }
    }

    /**
     * Cancels a specific scan job.
     * @param uiContext
     *              The app's uiContextId. The app must be in possession of a valid uiContextId in
     *              order to cancel a scan job.
     * @param scanJobID
     *              The job ID of the scan job to cancel.
     * @param requestID
     *              Request ID associated with this call
     * @param callback
     *              Callback to invoke when operation is finished.
     */
    public void CancelScanJob(UIContext uiContext, ScanJobID scanJobID, int requestID, OXPdDevice.RequestCallback callback) {
        if ((uiContext == null) || (scanJobID == null)) {
            if (callback != null) {
                callback.requestResult(mDevice, Message.obtain(null, requestID, OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION, 0,
                        (uiContext == null) ?
                                new Error(ErrorName.InvalidParameter, "Invalid ui context") :
                                new Error(ErrorName.InvalidParameter, "Invalid scan job id")));            }
        } else {
            mDevice.queueRequest(mCancelScanJobHandler, Pair.create(uiContext,scanJobID), requestID, callback);
        }
    }

    /**
     * Gets the status for the specified scan job.
     * @param scanJobID
     *              The job ID of the scan job to get status for.
     * @param requestID
     *              Request ID associated with this call
     * @param callback
     *              Callback to invoke when operation is finished.
     */
    public void ScanJobStatus(ScanJobID scanJobID, int requestID, OXPdDevice.RequestCallback callback) {
        if (scanJobID == null) {
            if (callback != null) {
                callback.requestResult(mDevice, Message.obtain(null, requestID, OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION, 0,
                        new Error(ErrorName.InvalidParameter, "Invalid scan job id")));
            }
        } else {
            mDevice.queueRequest(mGetScanJobStatusHandler, scanJobID, requestID, callback);
        }
    }

    /**
     * Register a scan file receiver callback
     * @param scanFileReceiver Callback to invoke when scan files are received
     */
    public void registerScanFileReceiverCallback(ScanFilesReceivedCallback scanFileReceiver) {
        mDevice.queueRequest(mRegisterScanFileReceiverCallback, scanFileReceiver, 0, null);
    }

    /**
     * Unregister a scan file receiver callback
     * @param scanFilesReceivedCallback Callback to remove
     */
    public void unregisterScanFileReceiverCallback(ScanFilesReceivedCallback scanFilesReceivedCallback) {
        mDevice.queueRequest(mUnregisterScanFileReceiverCallback, scanFilesReceivedCallback, 0, null);
    }

    /**
     * Configure the default handlers when processing a scan SOAP message
     * @return Default handler
     */
    private RestXMLTagHandler setupDefaultHandler() {
        return mDevice.createSOAPFaultHandler();
    }

    /**
     * Check the HTTP response for the request
     * @param requestResponse
     *              HTTP request/response container
     * @return
     *              XML tag handler pre-configured to detect faults
     * @throws Error
     *              If HTTP response had an error
     */
    private RestXMLTagHandler checkHTTPResponse(HttpRequestResponseContainer requestResponse) throws Error {
        if (requestResponse.response != null) {
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
     * Add a scan ticket to the XML writer
     * @param jobParams
     *              Scan job parameters
     * @param xmlWriter
     *              XML writer to add scan ticket to
     */
    private void addScanTicketPayload(ScanJobParams jobParams, RestXMLWriter xmlWriter) {

        ScanTicket scanTicket = jobParams.mScanTicket;

        xmlWriter.writeStartTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__SCAN_TICKET, null);

        if (scanTicket.basicOptions != null) {
            xmlWriter.writeStartTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__BASIC_OPTIONS, null);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__BACKGROUND_CLEANUP, null, "%s", scanTicket.basicOptions.backgroundCleanup.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__BLANK_IMAGE_REMOVAL_MODE, null, "%s", scanTicket.basicOptions.blankImageRemovalMode.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__COLOR_DROPOUT_MODE, null, "%s", scanTicket.basicOptions.colorDropoutMode.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__COLOR_MODE, null, "%s", scanTicket.basicOptions.colorMode.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__CONTRAST_ADJUSTMENT, null, "%s", scanTicket.basicOptions.contrastAdjustment.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__CROP_MODE, null, "%s",
                    CropMode.getVersionAppropriate(scanTicket.basicOptions.cropMode, mSupportsVersion2Messages).mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__CUSTOM_LENGTH, null, "%f", scanTicket.basicOptions.customLength);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__CUSTOM_WIDTH, null, "%f", scanTicket.basicOptions.customWidth);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__DARKNESS_ADJUSTMENT, null, "%s", scanTicket.basicOptions.darknessAdjustment.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__DUPLEX_FORMAT, null, "%s", scanTicket.basicOptions.duplexFormat.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__FILE_TYPE, null, "%s", scanTicket.basicOptions.fileType.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__JOB_ASSEMBLY_MODE, null, "%s", scanTicket.basicOptions.jobAssemblyMode.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__MEDIA_ORIENTATION, null, "%s", scanTicket.basicOptions.mediaOrientation.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__MEDIA_SIZE, null, "%s", scanTicket.basicOptions.mediaSize.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__MEDIA_SOURCE, null, "%s", scanTicket.basicOptions.mediaSource.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__MEDIA_WEIGHT_ADJUSTMENT, null, "%s", scanTicket.basicOptions.mediaWeightAdjustment.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__MISFEED_DETECTION_MODE, null, "%s", scanTicket.basicOptions.misfeedDetectionMode.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__OUTPUT_QUALITY, null, "%s", scanTicket.basicOptions.outputQuality.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__PLEXMODE, null, "%s", scanTicket.basicOptions.plexMode.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__PREVIEW_MODE, null, "%s", scanTicket.basicOptions.previewMode.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__PROGRESS_DIALOG_MODE, null, "%s",
                    ((jobParams.mUIContext.getSharedSecret() == null)
                            ? scanTicket.basicOptions.progressDialogMode.mValue
                            : ProgressDialogMode.Off.mValue));
            xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__RESOLUTION, null, "%s", scanTicket.basicOptions.resolution.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__SHARPNESS_ADJUSTMENT, null, "%s", scanTicket.basicOptions.sharpnessAdjustment.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__TEXT_PHOTO_OPTIMIZATION, null, "%s", scanTicket.basicOptions.textPhotoOptimization.mValue);
            // ScanTicket3

            if (mSupportsVersion3Messages) {
                if (scanTicket.basicOptions.splitAttachmentByPage != null) xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__SPLIT_ATTACHMENT_BY_PAGES, null, "%s", scanTicket.basicOptions.splitAttachmentByPage.mValue);
                xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__MAX_PAGES_PER_ATTACHMENT, null, "%d", scanTicket.basicOptions.maxPagesPerAttachment);
                if (scanTicket.basicOptions.eraseMarginUnit != null) xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__ERASE_MARGIN_UNITS, null, "%s", scanTicket.basicOptions.eraseMarginUnit.mValue);
                xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__ERASE_BACK_BOTTOM, null, "%f", scanTicket.basicOptions.eraseBackBottom);
                xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__ERASE_BACK_LEFT, null, "%f", scanTicket.basicOptions.eraseBackLeft);
                xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__ERASE_BACK_RIGHT, null, "%f", scanTicket.basicOptions.eraseBackRight);
                xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__ERASE_BACK_TOP, null, "%f", scanTicket.basicOptions.eraseBackTop);
                xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__ERASE_FRONT_BOTTOM, null, "%f", scanTicket.basicOptions.eraseFrontBottom);
                xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__ERASE_FRONT_LEFT, null, "%f", scanTicket.basicOptions.eraseFrontLeft);
                xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__ERASE_FRONT_RIGHT, null, "%f", scanTicket.basicOptions.eraseFrontRight);
                xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__ERASE_FRONT_TOP, null, "%f", scanTicket.basicOptions.eraseFrontTop);
                if (scanTicket.basicOptions.captureMode != null) xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__CAPTURE_MODE, null, "%s", scanTicket.basicOptions.captureMode.mValue);
                if (scanTicket.basicOptions.automaticToneMode != null) xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__AUTOMATIC_TONE_MODE, null, "%s", scanTicket.basicOptions.automaticToneMode.mValue);
                if (scanTicket.basicOptions.automaticStraightenMode != null) xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__AUTOMATIC_STRAIGHTEN_MODE, null, "%s", scanTicket.basicOptions.automaticStraightenMode.mValue);
            }
            xmlWriter.writeEndTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__BASIC_OPTIONS);
        }

        xmlWriter.writeStartTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__CALLBACK, null);
        xmlWriter.writeTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, OXPdDevice.Constants.XML_TAG__COMMON__URI, null, "%s", mDevice.getDeviceAbsoluteURLForPath(OXPD_SERVER__SCAN_CALLBACK + "/" + jobParams.mServerContext ));
        xmlWriter.writeTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, OXPdDevice.Constants.XML_TAG__COMMON__BINDING, null, "%s", OXPdDevice.Constants.XML_VALUE__COMMON_BINDING__SOAP);
        xmlWriter.writeTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, Constants.XML_TAG__COMMON__CONNECTION_TIMEOUT, null, "%d", 30);
        xmlWriter.writeTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, Constants.XML_TAG__COMMON__RESPONSE_TIMEOUT, null, "%d", 30);
        xmlWriter.writeEndTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__CALLBACK);


        if (scanTicket.destination != null) {
            xmlWriter.writeStartTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__DESTINATION, null);
            xmlWriter.writeTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, OXPdDevice.Constants.XML_TAG__COMMON__URI, null, "%s",
                    isDestinationLocal(scanTicket.destination) ?
                    mDevice.getDeviceAbsoluteURLForPath(OXPD_SERVER__SCAN_FILES + "/" + jobParams.mServerContext) : scanTicket.destination.uri.toString());
            xmlWriter.writeTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, OXPdDevice.Constants.XML_TAG__COMMON__BINDING, null, "%s", OXPdDevice.Constants.XML_VALUE__COMMON_BINDING__PLAIN);

            if (!TextUtils.isEmpty(scanTicket.destination.domain)
                    || !TextUtils.isEmpty(scanTicket.destination.userName)
                    || !TextUtils.isEmpty(scanTicket.destination.password)) {
                xmlWriter.writeStartTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, Constants.XML_TAG__COMMON__NETWORK_CREDENTIALS, null);
                if (!TextUtils.isEmpty(scanTicket.destination.userName)) {
                    xmlWriter.writeTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, Constants.XML_TAG__COMMON__USERNAME, null, "%s", scanTicket.destination.userName);
                }
                if (!TextUtils.isEmpty(scanTicket.destination.password)) {
                    xmlWriter.writeTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, Constants.XML_TAG__COMMON__PASSWORD, null, "%s", scanTicket.destination.password);
                }
                if (!TextUtils.isEmpty(scanTicket.destination.domain)) {
                    xmlWriter.writeTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, Constants.XML_TAG__COMMON__DOMAIN, null, "%s", scanTicket.destination.domain);
                }
                xmlWriter.writeEndTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, Constants.XML_TAG__COMMON__NETWORK_CREDENTIALS);
            }
            xmlWriter.writeTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, Constants.XML_TAG__COMMON__CONNECTION_TIMEOUT, null, "%d", scanTicket.destination.connectionTimeout);
            xmlWriter.writeTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, Constants.XML_TAG__COMMON__RESPONSE_TIMEOUT, null, "%d", scanTicket.destination.responseTimeout);
            xmlWriter.writeTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, Constants.XML_TAG__COMMON__MAX_CONSECUTIVE_RETRIES, null, "%d", scanTicket.destination.maxConsecutiveRetries);
            xmlWriter.writeTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, Constants.XML_TAG__COMMON__RETRY_INTERVAL, null, "%d", scanTicket.destination.retryInterval);
            xmlWriter.writeEndTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__DESTINATION);
        }

        if (scanTicket.fileOptions != null) {
            xmlWriter.writeStartTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__FILE_OPTIONS, null);
            if (!TextUtils.isEmpty(scanTicket.fileOptions.pdfEncryptionPassword)) {
                xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__PDF_ENCRYPTION_PASSWORD, null, "%s", scanTicket.fileOptions.pdfEncryptionPassword);
            }
            xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__OCR_LANGUAGE, null, "%s", scanTicket.fileOptions.ocrLanguage.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__PDF_COMPRESSION_MODE, null, "%s", scanTicket.fileOptions.pdfCompressionMode.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__TIFF_COMPRESSION_MODE, null, "%s", scanTicket.fileOptions.tiffCompressionMode.mValue);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__XPS_COMPRESSION_MODE, null, "%s", scanTicket.fileOptions.xpsCompressionMode.mValue);
            xmlWriter.writeEndTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__FILE_OPTIONS);
        }

        if (scanTicket.metadata != null) {
            xmlWriter.writeStartTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__METADATA, null);
            if (!TextUtils.isEmpty(scanTicket.metadata.fileNameExtension)) {
                xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__FILE_NAME_EXTENSION, null, "%s", scanTicket.metadata.fileNameExtension);
            }
            xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__FILE_LIST_DELIMITER, null, "%s", scanTicket.metadata.fileListDelimiter);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__FILE_NAME_BASE, null, "%s", scanTicket.metadata.fileNameBase);
            xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SAN__FORMAT_STRING, null, "%s", scanTicket.metadata.formatString);
            xmlWriter.writeEndTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__METADATA);
        }

        xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__SCAN_FILE_NAME_BASE, null, "%s", scanTicket.scanFileNameBase);
        xmlWriter.writeTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__TRANSMISSION_MODE, null, "%s", scanTicket.transmissionMode.mValue);

        xmlWriter.writeEndTag(XML_SCHEMA__OXPD_SCAN, Constants.XML_TAG__SCAN__SCAN_TICKET);
    }

    /**
     *
     * @param destination Scan ticket destination
     * @return true if destination is local, false otherwise
     */
    private boolean isDestinationLocal(Destination destination) {
        return ((destination != null) && mDevice.getLocalScanDestination().uri.equals(destination.uri));
    }

    /**
     * Request handler for scan job events
     */
    private final OXPdDevice.DeviceProcessRequestCallback mScanCallbackEventRequestHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {
            ScanCallbackData data = (ScanCallbackData)requestParams;
            String scanJobEventData = data.mSOAPMessage;
            RestXMLTagHandler handler = setupDefaultHandler();
            ScanJobTracker tracker = mJobTrackerMap.get(data.mServerContext);
            if (tracker == null) return null;
            try {
                ScanJobEvent event = ScanJobEvent.parseRequestResult(mDevice, scanJobEventData, handler, tracker.mSecretKey, tracker.mSharedKeyAlgorithm);
                mDevice.removeCallback(tracker.mStatusPollingRunnable);
                if (event.mJobStatus.resultCode == ResultCode.Pending) {
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
     * Request handler for scan job events
     */
    private final OXPdDevice.DeviceProcessRequestCallback mScanFilesReceivedRequestHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {
            ScanFilesData scanFileData = (ScanFilesData)requestParams;
            ScanJobTracker tracker = mJobTrackerMap.get(scanFileData.mServerContext);
            if (tracker != null) {
                for(ScanFilesReceivedCallback callback : mScanFileReceivers) {
                    callback.onFilesReceived(tracker.mScanJobID, scanFileData.mFiles, scanFileData.mWithErrors);
                }
            }
            return null;
        }
    };

    /**
     * Register handlers for job specific functions
     * @param serverContext Internal job identifier
     */
    private void registerJobCallbacks(String serverContext, boolean registerFileReceiver, SecretKeySpec cryptoKey, String cryptoAlgorithm ) {
        mServerHooks.registerHook(OXPD_SERVER__SCAN_CALLBACK + "/" + serverContext, mScanCallbackRequestHandler);
        if (registerFileReceiver) {
            mServerHooks.registerHook(OXPD_SERVER__SCAN_FILES + "/" + serverContext,
                    ((cryptoKey != null) ? new ScanFileRequestHandler(cryptoKey, cryptoAlgorithm) : mDefaultScanFileRequestHandler));
        }
    }

    /**
     * Unregister job specific handler
     * @param serverContext Internal job identifier
     */
    private void unregisterJobCallbacks(String serverContext) {
        mServerHooks.unregisterHook(OXPD_SERVER__SCAN_CALLBACK + "/" + serverContext);
        mServerHooks.unregisterHook(OXPD_SERVER__SCAN_FILES + "/" + serverContext);
    }

    /**
     * Return the server context portion of the uri
     * @param uri Relative URI
     * @return
     *          Internal job identifier portion of uri
     */
    private String getServerContext(String uri) {
        String[] parts = uri.split("/");
        try {
            // verify it server context is a UUID
            return UUID.fromString(parts[parts.length - 1]).toString();
        } catch(Exception ignored) {
            return null;
        }
    }
}
