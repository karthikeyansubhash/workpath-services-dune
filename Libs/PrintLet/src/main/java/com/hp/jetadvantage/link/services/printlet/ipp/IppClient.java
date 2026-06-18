package com.hp.jetadvantage.link.services.printlet.ipp;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Message;
import android.print.PrintJobId;
import android.text.TextUtils;
import android.util.Log;

import com.hp.jetadvantage.link.device.services.interfaces.IDevicePrintJobService;
import com.hp.jetadvantage.link.device.services.standard.StandardDevicePrintJobService;
import com.hp.jetadvantage.link.services.printlet.model.Error;
import com.hp.jetadvantage.link.services.printlet.model.ErrorName;
import com.hp.jetadvantage.link.services.printlet.model.Finishings;
import com.hp.jetadvantage.link.services.printlet.model.JobAttributes;
import com.hp.jetadvantage.link.services.printlet.model.MediaSize;
import com.hp.jetadvantage.link.services.printlet.model.PrintJobID;
import com.hp.jetadvantage.link.services.printlet.model.PrintTicket;
import com.hp.jetadvantage.link.services.printlet.model.PrinterAttributes;
import com.hp.jetadvantage.link.services.printlet.model.ipp.IppCollection;
import com.hp.jetadvantage.link.services.printlet.model.ipp.IppCollectionAttribute;
import com.hp.jetadvantage.link.services.printlet.model.ipp.IppConstants;
import com.hp.jetadvantage.link.services.printlet.model.ipp.IppIntegerAttribute;
import com.hp.jetadvantage.link.services.printlet.model.ipp.IppRange;
import com.hp.jetadvantage.link.services.printlet.model.ipp.IppRangeAttribute;
import com.hp.jetadvantage.link.services.printlet.model.ipp.IppRequest;
import com.hp.jetadvantage.link.services.printlet.model.ipp.IppResponse;
import com.hp.jetadvantage.link.services.printlet.model.ipp.IppStringAttribute;
import com.hp.jetadvantage.link.services.printlet.model.PrintJobParams;
import com.hp.oxpdlib.OXPdDevice;
import com.hp.oxpdlib.uiconfiguration.UIContext;
import com.hp.sdd.jabberwocky.chat.HttpHeader;
import com.hp.sdd.jabberwocky.chat.HttpRequest;
import com.hp.sdd.jabberwocky.chat.HttpRequestResponseContainer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class IppClient {

    public static final String TAG = IppClient.class.getSimpleName();

    private IppConnector mIppConnector;

    private URL mIppUrl = null;

    private static IppClient mIppClient = null;

    public static IppClient getInstance() throws Error {
        if (mIppClient == null) {
            synchronized (IppClient.class) {
                mIppClient = new IppClient();
            }
        }
        return mIppClient;
    }

    private IppClient() throws Error {
        mIppConnector = new IppConnector();
        try {
            IDevicePrintJobService devicePrintJobService = new StandardDevicePrintJobService();
            mIppUrl = new URL(devicePrintJobService.getIppEndpoint());
        } catch (MalformedURLException e) {
            throw new Error(ErrorName.Unknown, "invalid URL");
        }
    }

    /**
     * Send an IPP request
     *
     * @param ippRequest IPP request to send
     * @return IPP response
     * @throws Error When an HTTP error occurs or IPP returns an error
     */
    private IppResponse sendIppRequest(Context context, IppRequest ippRequest) throws Error {
        return sendIppRequest(context, ippRequest, null, null);
    }

    /**
     * Send an IPP request
     *
     * @param ippRequest IPP request to send
     * @param uriData    Data to send along with IPP request
     * @return IPP response
     * @throws Error When an HTTP error occurs or IPP returns an error
     */
    private IppResponse sendIppRequest(Context context, IppRequest ippRequest, Uri uriData, InputStream printStream) throws Error {
        HttpRequest httpRequest;
        try {
            httpRequest = new HttpRequest.Builder()
                    .setURL(mIppUrl)
                    .setConnectionTimeout(IppConnector.Constants.CONNECTION_TIMEOUT_120)
                    .setSocketTimeout(IppConnector.Constants.SOCKET_TIMEOUT_120)
                    .setUsesCache(false)
                    .setFollowRedirects(true)
                    .setRequestMethod(HttpRequest.HTTPRequestType.POST)
                    .setRequestOutputContentType(IppConstants.MIME_TYPE_IPP)
                    .setRequestOutputData(uriData != null ?
                            new IppHTTPOutput(ippRequest, context, uriData) :
                            new IppHTTPOutput(ippRequest, context, printStream))
                    .setRequestInputData(true)
                    .addHeader(HttpHeader.create(HttpRequest.HEADER__CONNECTION, HttpRequest.HEADER_VALUE__CONNECTION)) // add header to close connection
                    .build();
        } catch (Throwable throwable) {
            throw new Error(ErrorName.Unknown, "Unknown error (" + ((throwable.getMessage() != null)?throwable.getMessage(): "") + ")");
        }

        try {
            HttpRequestResponseContainer requestResponse = mIppConnector.getHttpResponse(httpRequest, IppConnector.Constants.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL).get();
            if (requestResponse == null || requestResponse.response == null) {
                throw new Error(ErrorName.AjaxError, "Connection failed: no response received");
            }
            switch (requestResponse.response.getResponseCode()) {
                    case HttpURLConnection.HTTP_OK:
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        InputStream is = null;
                        try {
                            is = requestResponse.response.getInputStream();
                            if (is == null) {
                                throw new Error(ErrorName.AjaxError, "Connection failed: no response stream");
                            }
                            try {
                                readResponseStream(is, baos);
                            } catch (Exception e) {
                                Log.w(TAG, "Error reading IPP response: " + e.getMessage());
                                throw new Error(ErrorName.AjaxError, "Connection lost while reading response: " + e.getMessage());
                            }
                            IppResponse ippResponse = IppResponse.decode(baos.toByteArray());
                            IppConstants.IppStatus ippStatus = ippResponse.getResponseStatus();
                            switch (ippStatus) {
                                case IPP_OK:
                                case IPP_OK_SUBST:
                                case IPP_OK_CONFLICT:
                                    break;
                                case IPP_BAD_REQUEST:
                                    throw new Error(ErrorName.BadRequest, "Client Error: Bad Request");
                                case IPP_NOT_AUTHENTICATED:
                                    throw new Error(ErrorName.NotAuthenticated, "Client Error: Not Authenticated");
                                case IPP_NOT_AUTHORIZED:
                                    throw new Error(ErrorName.NotAuthorized, "Client Error: Not Authorized");
                                case IPP_NOT_POSSIBLE:
                                    throw new Error(ErrorName.NotPossible, "Client Error: Not Possible");
                                case IPP_NOT_FOUND:
                                    throw new Error(ErrorName.NotFound, "Client Error: Not Found");
                                case IPP_REQUEST_VALUE:
                                    throw new Error(ErrorName.ParamTooLong, "Client Error: Parameter Too Long");
                                case IPP_URI_SCHEME:
                                    throw new Error(ErrorName.UnreachableUri, "Client Error: Unsupported Uri");
                                case IPP_DOCUMENT_ACCESS_ERROR:
                                    throw new Error(ErrorName.UnreachableUri, "Client Error: Unreachable Uri");
                                case IPP_PRINTER_BUSY:
                                    throw new Error(ErrorName.Busy, "Server Error: Busy");
                                default:
                                    throw new Error(ErrorName.Unknown, "Error: Unknown");
                            }
                            return ippResponse;
                        } catch (IOException e) {
                            Log.e("[SIM]",e.getMessage(),e);
                            throw new Error(ErrorName.AjaxError, "Parse failed");
                        } finally {
                            try {
                                if (is != null) is.close();
                            } catch (IOException ignored) {
                            }
                        }
                    case HttpURLConnection.HTTP_NOT_FOUND:
                        throw new Error(ErrorName.NotFound, "Requested resource not found. Ensure that IPP is enabled on the printer.");
                    case IppConnector.Constants.HTTP_SERVER_ERROR:
                        throw new Error(ErrorName.ServerError, "Internal Server Error");
                    default:
                        throw new Error(ErrorName.Unknown, "Unknown Error: " + requestResponse.response.getResponseCode());
                }
        } catch (Throwable throwable) {
            throw new Error(ErrorName.AjaxError, "Connection failed (" + throwable.getMessage() + ")");
        }
    }

    private Message getPrinterAttributes(Context context, int requestID, boolean withDefaults) {
        IppStringAttribute.Builder builder = new IppStringAttribute.Builder(
                IppConstants.IppTag.IPP_TAG_KEYWORD, IppConstants.IPP_ATTRIBUTE_NAME__REQUESTED_ATTRIBUTES)
                .addValues(IppConstants.REQUEST_SET_SUPPORTED_ATTRIBUTES);

        // if true add default values attributes to the request (can take more time to request)
        if (withDefaults) {
            builder.addValues(IppConstants.REQUEST_SET_DEFAULT_ATTRIBUTES);
        }

        IppRequest ippRequest = new IppRequest.Builder()
                .setIppOperation(IppConstants.IppOperation.IPP_GET_PRINTER_ATTRIBUTES)
                .addAttribute(IppConstants.IppTag.IPP_TAG_OPERATION,
                        new IppStringAttribute.Builder(IppConstants.IppTag.IPP_TAG_URI, IppConstants.IPP_ATTRIBUTE_NAME__PRINTER_URI)
                                .addValue(mIppUrl.toString())
                                .build()
                )
                .addAttribute(IppConstants.IppTag.IPP_TAG_OPERATION, builder.build())
                .build();

        Object data;
        int result;
        try {
            data = new PrinterAttributes(sendIppRequest(context, ippRequest));
            result = IppConnector.Constants.REQUEST_RETURN_CODE__OK;
        } catch (Error error) {
            data = error;
            result = IppConnector.Constants.REQUEST_RETURN_CODE__EXCEPTION;
        }
        return Message.obtain(null, requestID, result, 0, data);
    }

    private Message printUri(Context context, int requestID, PrintJobParams params) {
        PrintTicket printTicket = params.getPrintTicket();
        Uri uriData = null;
        InputStream printStream = null;

        IppRequest.Builder ippRequestBuilder = new IppRequest.Builder()
                .addAttribute(
                        IppConstants.IppTag.IPP_TAG_OPERATION,
                        new IppStringAttribute.Builder(IppConstants.IppTag.IPP_TAG_URI, IppConstants.IPP_ATTRIBUTE_NAME__PRINTER_URI)
                                .addValue(mIppUrl.toString())
                                .build()
                );

        if (printTicket.documentUri != null &&
                !(printTicket.documentUri.startsWith(ContentResolver.SCHEME_CONTENT)
                        || printTicket.documentUri.startsWith(ContentResolver.SCHEME_FILE))) {
            ippRequestBuilder.addAttribute(
                    IppConstants.IppTag.IPP_TAG_OPERATION,
                    new IppStringAttribute.Builder(IppConstants.IppTag.IPP_TAG_URI, IppConstants.IPP_ATTRIBUTE_NAME__DOCUMENT_URI)
                            .addValue(printTicket.documentUri)
                            .build()
            );

            if (!TextUtils.isEmpty(printTicket.userName)) {
                ippRequestBuilder.addAttribute(
                        IppConstants.IppTag.IPP_TAG_OPERATION,
                        new IppStringAttribute.Builder(IppConstants.IppTag.IPP_TAG_TEXT, IppConstants.IPP_ATTRIBUTE_NAME__PRINT_URI_USERNAME)
                                .addValue(printTicket.userName)
                                .build()
                );
            }

            if (!TextUtils.isEmpty(printTicket.password)) {
                ippRequestBuilder.addAttribute(
                        IppConstants.IppTag.IPP_TAG_OPERATION,
                        new IppStringAttribute.Builder(IppConstants.IppTag.IPP_TAG_TEXT, IppConstants.IPP_ATTRIBUTE_NAME__PRINT_URI_PASSWORD)
                                .addValue(printTicket.password)
                                .build()
                );
            }
            if (!TextUtils.isEmpty(printTicket.cookie)) {
                IppStringAttribute.Builder attributeBuilder = new IppStringAttribute.Builder(IppConstants.IppTag.IPP_TAG_TEXT, IppConstants.IPP_ATTRIBUTE_NAME__PRINT_URI_COOKIE);
                int index = 0;
                while (index < printTicket.cookie.length()) {
                    final int COOKIE_CRUMB_MAX_LENGTH = 1023;
                    attributeBuilder.addValue(printTicket.cookie.substring(index, Math.min(index + COOKIE_CRUMB_MAX_LENGTH, printTicket.cookie.length())));
                    index += COOKIE_CRUMB_MAX_LENGTH;
                }
                ippRequestBuilder.addAttribute(IppConstants.IppTag.IPP_TAG_OPERATION, attributeBuilder.build());
            }
        } else {
            if (printTicket.documentUri != null) {
                uriData = Uri.parse(printTicket.documentUri);
            } else {
                printStream = printTicket.printStream;
            }
            ippRequestBuilder.addAttribute(IppConstants.IppTag.IPP_TAG_OPERATION,
                    new IppStringAttribute.Builder(IppConstants.IppTag.IPP_TAG_MIMETYPE, IppConstants.IPP_ATTRIBUTE_NAME__DOCUMENT_FORMAT)
                            .addValue(printTicket.fileType.mValue)
                            .build()
            );
        }

        if (printTicket.jobName != null) {
            ippRequestBuilder.addAttribute(IppConstants.IppTag.IPP_TAG_OPERATION,
                    new IppStringAttribute.Builder(IppConstants.IppTag.IPP_TAG_NAME, IppConstants.IPP_ATTRIBUTE_NAME__JOB_NAME)
                            .addValue(printTicket.jobName)
                            .build()
            );
        }

        ippRequestBuilder.setIppOperation(
                ((uriData != null || printStream != null) ? IppConstants.IppOperation.IPP_PRINT_JOB : IppConstants.IppOperation.IPP_PRINT_URI));

        if (!TextUtils.isEmpty(printTicket.uiContextID)) {
            ippRequestBuilder.addAttribute(
                    IppConstants.IppTag.IPP_TAG_OPERATION,
                    new IppStringAttribute.Builder(IppConstants.IppTag.IPP_TAG_TEXT, IppConstants.IPP_ATTRIBUTE_NAME__PRIORITY_PRINT_AUTH_COOKIE)
                            .addValue(printTicket.uiContextID)
                            .build()
            );
        } else {
            Log.e(TAG, "printUri: uiContextID is empty");
        }

        ippRequestBuilder
                .addAttribute(IppConstants.IppTag.IPP_TAG_JOB,
                        new IppIntegerAttribute.Builder(IppConstants.IppTag.IPP_TAG_INTEGER, IppConstants.IPP_ATTRIBUTE_NAME__COPIES)
                                .addValue(printTicket.copies)
                                .build()
                );

        if (printTicket.collateMode != null) {
            ippRequestBuilder.addAttribute(IppConstants.IppTag.IPP_TAG_JOB,
                    new IppStringAttribute.Builder(IppConstants.IppTag.IPP_TAG_KEYWORD, IppConstants.IPP_ATTRIBUTE_NAME__MULTIPLE_DOCUMENT_HANDLING)
                            .addValue(printTicket.collateMode.mValue)
                            .build()
            );
        }

        if (printTicket.plexMode != null) {
            ippRequestBuilder.addAttribute(IppConstants.IppTag.IPP_TAG_JOB,
                    new IppStringAttribute.Builder(IppConstants.IppTag.IPP_TAG_KEYWORD, IppConstants.IPP_ATTRIBUTE_NAME__SIDES)
                            .addValue(printTicket.plexMode.mValue)
                            .build()
            );
        }

        if (printTicket.colorMode != null) {
            ippRequestBuilder.addAttribute(IppConstants.IppTag.IPP_TAG_JOB,
                    new IppStringAttribute.Builder(IppConstants.IppTag.IPP_TAG_KEYWORD,
                            IppConstants.IPP_ATTRIBUTE_NAME__PRINT_COLOR_MODE)
                            .addValue(printTicket.colorMode.mValue)
                            .build()
            );
        }

        if (printTicket.scalingMode != null) {
            ippRequestBuilder.addAttribute(IppConstants.IppTag.IPP_TAG_JOB,
                    new IppStringAttribute.Builder(IppConstants.IppTag.IPP_TAG_KEYWORD,
                            IppConstants.IPP_ATTRIBUTE_NAME__PRINT_SCALING_MODE)
                            .addValue(printTicket.scalingMode.mValue)
                            .build()
            );
        }

        if (printTicket.stapleMode != null && isStapleMode(printTicket)) {
            ippRequestBuilder.addAttribute(IppConstants.IppTag.IPP_TAG_JOB,
                    new IppCollectionAttribute.Builder(IppConstants.IPP_ATTRIBUTE_NAME__FINISHINGS_COL).addValue(
                            new IppCollection.Builder().addAttribute(
                                    new IppStringAttribute.Builder(IppConstants.IppTag.IPP_TAG_KEYWORD,
                                            IppConstants.IPP_ATTRIBUTE_NAME__FINISHING_TEMPLATE)
                                            .addValue(printTicket.stapleMode.mValue)
                                            .build()
                            ).build()
                    ).build()
            );
        }

        if (printTicket.mediaSource != null) {
            ippRequestBuilder.addAttribute(IppConstants.IppTag.IPP_TAG_JOB,
                    new IppStringAttribute.Builder(IppConstants.IppTag.IPP_TAG_KEYWORD, IppConstants.IPP_ATTRIBUTE_NAME__MEDIA_SOURCE)
                            .addValue(printTicket.mediaSource.mValue)
                            .build()
            );
        }


        if (printTicket.mediaSize != null && printTicket.mediaSize != MediaSize.Default) {
            ippRequestBuilder.addAttribute(IppConstants.IppTag.IPP_TAG_JOB,
                    new IppStringAttribute.Builder(IppConstants.IppTag.IPP_TAG_KEYWORD, IppConstants.IPP_ATTRIBUTE_NAME__MEDIA)
                            .addValue(printTicket.mediaSize.mValue)
                            .build()
            );
        }

        if (printTicket.mediaType != null) {
            ippRequestBuilder.addAttribute(IppConstants.IppTag.IPP_TAG_JOB,
                    new IppStringAttribute.Builder(IppConstants.IppTag.IPP_TAG_KEYWORD,
                            IppConstants.IPP_ATTRIBUTE_NAME__MEDIA_TYPE)
                            .addValue(printTicket.mediaType.mValue)
                            .build()
            );
        }

        if (printTicket.orientation != null) {
            ippRequestBuilder
                    .addAttribute(IppConstants.IppTag.IPP_TAG_JOB,
                            new IppIntegerAttribute.Builder(IppConstants.IppTag.IPP_TAG_ENUM, IppConstants.IPP_ATTRIBUTE_NAME__ORIENTATION_REQUESTED)
                                    .addValue(printTicket.orientation.mValue)
                                    .build()
                    );
        }

        if (printTicket.printQuality != null) {
            ippRequestBuilder
                    .addAttribute(IppConstants.IppTag.IPP_TAG_JOB,
                            new IppIntegerAttribute.Builder(IppConstants.IppTag.IPP_TAG_ENUM, IppConstants.IPP_ATTRIBUTE_NAME__PRINT_QUALITY)
                                    .addValue(printTicket.printQuality.mValue)
                                    .build()
                    );
        }
        if (printTicket.outputBin != null) {
            ippRequestBuilder
                    .addAttribute(IppConstants.IppTag.IPP_TAG_JOB,
                            new IppStringAttribute.Builder(IppConstants.IppTag.IPP_TAG_KEYWORD, IppConstants.IPP_ATTRIBUTE_NAME__OUTPUT_BIN)
                                    .addValue(printTicket.outputBin.mValue)
                                    .build()
                    );
        }
        if (printTicket.startPageRanges > 0 && printTicket.endPageRanges > 0 && (printTicket.endPageRanges >= printTicket.startPageRanges)) {
            ippRequestBuilder
                    .addAttribute(IppConstants.IppTag.IPP_TAG_JOB,
                            new IppRangeAttribute.Builder(IppConstants.IPP_ATTRIBUTE_NAME__PAGE_RANGES)
                                    .addValue(new IppRange(printTicket.startPageRanges, printTicket.endPageRanges))
                                    .build()
                    );


        }
        if (printTicket.finishingsList != null && !isStapleMode(printTicket)) {
            for (Finishings finishings : printTicket.finishingsList) {
                ippRequestBuilder
                        .addAttribute(IppConstants.IppTag.IPP_TAG_JOB,
                                new IppIntegerAttribute.Builder(IppConstants.IppTag.IPP_TAG_ENUM, IppConstants.IPP_ATTRIBUTE_NAME__FINISHINGS)
                                        .addValue(finishings.mValue)
                                        .build()
                        );
            }
        }

        IppRequest ippRequest = ippRequestBuilder.build();

        Object data;
        int result;
        try {
            IppResponse response = sendIppRequest(context, ippRequest, uriData, printStream);

            PrintJobID printJobID = new PrintJobID(response);
            data = printJobID;
            result = IppConnector.Constants.REQUEST_RETURN_CODE__OK;
        } catch (Error error) {
            data = error;
            result = IppConnector.Constants.REQUEST_RETURN_CODE__EXCEPTION;
        }
        return Message.obtain(null, requestID, result, 0, data);
    }

    public Message getJobAttributes(Context context, int requestID, PrintJobID jobID) {
        IppRequest ippRequest;
        ippRequest = new IppRequest.Builder()
                .setIppOperation(IppConstants.IppOperation.IPP_GET_JOB_ATTRIBUTES)
                .addAttribute(
                        IppConstants.IppTag.IPP_TAG_OPERATION,
                        new IppStringAttribute.Builder(IppConstants.IppTag.IPP_TAG_URI, IppConstants.IPP_ATTRIBUTE_NAME__PRINTER_URI)
                                .addValue(mIppUrl.toString())
                                .build()
                )
                .addAttribute(
                        IppConstants.IppTag.IPP_TAG_OPERATION,
                        new IppIntegerAttribute.Builder(IppConstants.IppTag.IPP_TAG_INTEGER, IppConstants.IPP_ATTRIBUTE_NAME__JOB_ID)
                                .addValue(jobID.getJobID())
                                .build()
                )
                .addAttribute(
                        IppConstants.IppTag.IPP_TAG_OPERATION,
                        new IppStringAttribute.Builder(IppConstants.IppTag.IPP_TAG_KEYWORD, IppConstants.IPP_ATTRIBUTE_NAME__REQUESTED_ATTRIBUTES)
                                .addValues(
                                        IppConstants.IPP_ATTRIBUTE_NAME__JOB_STATE,
                                        IppConstants.IPP_ATTRIBUTE_NAME__JOB_STATE_REASONS,
                                        IppConstants.IPP_ATTRIBUTE_NAME__JOB_DOCUMENT_ACCESS_ERROR,
                                        IppConstants.IPP_ATTRIBUTE_NAME__JOB_IMPRESSIONS_COMPLETED,
                                        IppConstants.IPP_ATTRIBUTE_NAME__JOB_MEDIA_SHEETS_COMPLETED,
                                        IppConstants.IPP_ATTRIBUTE_NAME__JOB_NAME,
                                        IppConstants.IPP_ATTRIBUTE_NAME__JOB_USER,
                                        IppConstants.IPP_ATTRIBUTE_NAME__JOB_UUID,
                                        IppConstants.IPP_ATTRIBUTE_NAME__JOB_TIME_CREATED,
                                        IppConstants.IPP_ATTRIBUTE_NAME__JOB_TIME_COMPLETED
                                )
                                .build()
                )
                .build();

        Object data;
        int result;
        try {
            data = new JobAttributes(sendIppRequest(context, ippRequest));
            result = IppConnector.Constants.REQUEST_RETURN_CODE__OK;
        } catch (Error error) {
            data = error;
            result = IppConnector.Constants.REQUEST_RETURN_CODE__EXCEPTION;
        }
        return Message.obtain(null, requestID, result, 0, data);
    }

    public Message cancelJob(Context context, int requestID, int jobID) {
        IppRequest ippRequest;
        ippRequest = new IppRequest.Builder()
                .setIppOperation(IppConstants.IppOperation.IPP_CANCEL_JOB)
                .addAttribute(
                        IppConstants.IppTag.IPP_TAG_OPERATION,
                        new IppStringAttribute.Builder(IppConstants.IppTag.IPP_TAG_URI, IppConstants.IPP_ATTRIBUTE_NAME__PRINTER_URI)
                                .addValue(mIppUrl.toString())
                                .build()
                )
                .addAttribute(
                        IppConstants.IppTag.IPP_TAG_OPERATION,
                        new IppIntegerAttribute.Builder(IppConstants.IppTag.IPP_TAG_INTEGER, IppConstants.IPP_ATTRIBUTE_NAME__JOB_ID)
                                .addValue(jobID)
                                .build()
                )
                .build();

        Object data;
        int result;
        try {
            sendIppRequest(context, ippRequest);
            data = null;
            result = IppConnector.Constants.REQUEST_RETURN_CODE__OK;
        } catch (Error error) {
            data = error;
            result = IppConnector.Constants.REQUEST_RETURN_CODE__EXCEPTION;
        }
        return Message.obtain(null, requestID, result, 0, data);
    }

    private Message priorityPrintEnter(Context context, int requestID, String uiContextID) {
        IppRequest ippRequest;
        ippRequest = new IppRequest.Builder()
                .setIppOperation(IppConstants.IppOperation.IPP_PRIORITY_PRINT_ENTER)
                .addAttribute(
                        IppConstants.IppTag.IPP_TAG_OPERATION,
                        new IppStringAttribute.Builder(IppConstants.IppTag.IPP_TAG_URI, IppConstants.IPP_ATTRIBUTE_NAME__PRINTER_URI)
                                .addValue(mIppUrl.toString())
                                .build()
                )
                .addAttribute(
                        IppConstants.IppTag.IPP_TAG_OPERATION,
                        new IppStringAttribute.Builder(IppConstants.IppTag.IPP_TAG_TEXT, IppConstants.IPP_ATTRIBUTE_NAME__PRIORITY_PRINT_AUTH_COOKIE)
                                .addValue(uiContextID)
                                .build()
                )
                .build();

        Object data;
        int result;
        try {
            sendIppRequest(context, ippRequest);
            data = null;
            result = IppConnector.Constants.REQUEST_RETURN_CODE__OK;
        } catch (Error error) {
            data = error;
            result = IppConnector.Constants.REQUEST_RETURN_CODE__EXCEPTION;
        }
        return Message.obtain(null, requestID, result, 0, data);
    }

    private Message priorityPrintExit(Context context, int requestID, String uiContextID) {
        IppRequest ippRequest;
        ippRequest = new IppRequest.Builder()
                .setIppOperation(IppConstants.IppOperation.IPP_PRIORITY_PRINT_EXIT)
                .addAttribute(
                        IppConstants.IppTag.IPP_TAG_OPERATION,
                        new IppStringAttribute.Builder(IppConstants.IppTag.IPP_TAG_URI, IppConstants.IPP_ATTRIBUTE_NAME__PRINTER_URI)
                                .addValue(mIppUrl.toString())
                                .build()
                )
                .addAttribute(
                        IppConstants.IppTag.IPP_TAG_OPERATION,
                        new IppStringAttribute.Builder(IppConstants.IppTag.IPP_TAG_TEXT, IppConstants.IPP_ATTRIBUTE_NAME__PRIORITY_PRINT_AUTH_COOKIE)
                                .addValue(uiContextID)
                                .build()
                )
                .build();
        Object data;
        int result;
        try {
            sendIppRequest(context, ippRequest);
            data = null;
            result = IppConnector.Constants.REQUEST_RETURN_CODE__OK;
        } catch (Error error) {
            data = error;
            result = IppConnector.Constants.REQUEST_RETURN_CODE__EXCEPTION;
        }
        return Message.obtain(null, requestID, result, 0, data);
    }

    /**
     * Retrieves the device's {@link PrinterAttributes}
     *
     * @param requestID Request id associated with this call
     */
    public synchronized Message getPrinterAttributes (Context context, int requestID) {
        return getPrinterAttributes(context, requestID, true);
    }

    /**
     * Retrieves the device's {@link PrinterAttributes}
     *
     * @param withDefaults whether to return printer state and defautls values (can take more time)
     * @param requestID    Request id associated with this call
     */
    public synchronized Message getPrinterAttributes (Context context, boolean withDefaults, int requestID) {
        return getPrinterAttributes(context, requestID, withDefaults);
    }

    /**
     * Instructs the device to retrieve a printable document from a uri and print it (when not in priority mode).
     *
     * @param printTicket Print ticket defining all the options for this print job.
     *
     */
    public synchronized Message printUri(Context context, PrintTicket printTicket, int requestID) {
        return printUri(context, printTicket, 0, requestID);
    }


    public synchronized Message printUri(Context context, PrintTicket printTicket, int statusRequestID, int jobStartRequestID) {
        if (printTicket == null) {
            return Message.obtain(null, jobStartRequestID, OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION, 0, new Error(ErrorName.BadRequest, "Invalid print ticket"));
        } else {
            return printUri(context, jobStartRequestID, new PrintJobParams(printTicket, statusRequestID));
        }
    }


    /**
     * Place the device into priority print mode.
     * Once placed into priority print mode, the device behavior is as follows:
     * <ul><li>The device will not accept jobs from other clients. An {@link Error} with
     * {@link ErrorName}.Busy will be passed to the fail function immediately to blocked clients if
     * they attempt to start new jobs.</li>
     * <li>An {@link Error} with {@link ErrorName}.Busy will be passed to the fail function if
     * subsequent attempts are made to enter priority print mode, regardless of the uiContextId
     * value provided on the subsequent attempts (even from the same client).</li>
     * <li>The device will accept connections from clients using other networking protocols such as
     * Port 9100, LPD, etc., but will hold off the processing of these print jobs until this mode
     * is exited.</li>
     * <li>The device will allow priority print jobs to print. (Priority print jobs are those
     * submitted with the currently valid OXPd UIContextId.)</li><li>The device will automatically
     * exit Priority Printing mode if its UI Inactivity Timeout is reached or the uiContextId is
     * otherwise revoked.</li></ul><br/>
     * The device can be put into priority print mode even while printing a job. If the device is
     * currently printing a job when the device is put into priority print mode, that job will
     * continue until finished.
     *
     * @param uiContextID The currently valid OXPd UIContextId
     * @param requestID   Request id associated with this call
     */
    public synchronized Message enterPriorityPrint(Context context, String uiContextID, int requestID, boolean isBackgroundJob, boolean isFirstJob) {
        if (isBackgroundJob || !isFirstJob) {
            return Message.obtain(null, requestID, IppConnector.Constants.REQUEST_RETURN_CODE__OK, 0, null);
        }

        if (uiContextID == null || uiContextID.isEmpty()) {
            return Message.obtain(null, requestID, IppConnector.Constants.REQUEST_RETURN_CODE__EXCEPTION, 0, new Error(ErrorName.BadRequest, "Invalid uiContextID"));
        } else {
            return priorityPrintEnter(context, requestID, uiContextID);
        }
    }

    /**
     * Place the device back into non-priority print mode.
     *
     * @param uiContextID The currently valid OXPd UIContextId
     * @param requestID   Request id associated with this call
     */
    public synchronized Message exitPriorityPrint(Context context, String uiContextID, int requestID, boolean isBackgroundJob, boolean isLastJob) {
        if (isBackgroundJob || !isLastJob) {
            return Message.obtain(null, requestID, IppConnector.Constants.REQUEST_RETURN_CODE__OK, 0, null);
        }

        if (uiContextID == null || uiContextID.isEmpty()) {
            return Message.obtain(null, requestID, IppConnector.Constants.REQUEST_RETURN_CODE__EXCEPTION, 0, new Error(ErrorName.BadRequest, "Invalid uiContextID"));
        } else {
            return priorityPrintExit(context, requestID, uiContextID);
        }
    }

    /**
     * Instructs the device to retrieve a printable document from a uri and print it (while in priority mode).
     *
     * @param uiContextID The currently valid OXPd UIContextId
     * @param printTicket Print ticket defining all the options for this print job.
     * @param requestID   Request id associated with this call
     */
    public Message priorityPrintUri(Context context, String uiContextID, PrintTicket printTicket, int requestID) {
        return priorityPrintUri(context, uiContextID, printTicket, 0, requestID);
    }

    /**
     * Instructs the device to retrieve a printable document from a uri and print it (while in priority mode).
     *
     * @param uiContextID             The currently valid OXPd UIContextId
     * @param printTicket             Print ticket defining all the options for this print job.
     * @param statusRequestID         Request id associated with status callback
     * @param startJobRequestID       Request id associated with this call
     */
    @SuppressWarnings("SameParameterValue")
    public Message priorityPrintUri(Context context, String uiContextID, PrintTicket printTicket, int statusRequestID, int startJobRequestID) {
        if (uiContextID == null) {
            return Message.obtain(null, startJobRequestID, IppConnector.Constants.REQUEST_RETURN_CODE__EXCEPTION, 0, new Error(ErrorName.BadRequest, "Invalid uiContextID"));
        } else if (printTicket == null) {
            return Message.obtain(null, startJobRequestID, IppConnector.Constants.REQUEST_RETURN_CODE__EXCEPTION, 0, new Error(ErrorName.BadRequest, "Invalid print ticket"));
        } else {
            return printUri(context, startJobRequestID, new PrintJobParams(new PrintTicket(printTicket, uiContextID), statusRequestID));
        }
    }

    public Message getPrintJobAttributes(Context context, int requestID, PrintJobID jobID) {
        //PrintJobID jobID = (PrintJobID) requestParams;

        IppRequest ippRequest;
        ippRequest = new IppRequest.Builder()
                .setIppOperation(IppConstants.IppOperation.IPP_GET_JOB_ATTRIBUTES)
                .addAttribute(
                        IppConstants.IppTag.IPP_TAG_OPERATION,
                        new IppStringAttribute.Builder(IppConstants.IppTag.IPP_TAG_URI, IppConstants.IPP_ATTRIBUTE_NAME__PRINTER_URI)
                                .addValue(mIppUrl.toString())
                                .build()
                )
                .addAttribute(
                        IppConstants.IppTag.IPP_TAG_OPERATION,
                        new IppIntegerAttribute.Builder(IppConstants.IppTag.IPP_TAG_INTEGER, IppConstants.IPP_ATTRIBUTE_NAME__JOB_ID)
                                .addValue(jobID.getJobID())
                                .build()
                )
                .addAttribute(
                        IppConstants.IppTag.IPP_TAG_OPERATION,
                        new IppStringAttribute.Builder(IppConstants.IppTag.IPP_TAG_KEYWORD, IppConstants.IPP_ATTRIBUTE_NAME__REQUESTED_ATTRIBUTES)
                                .addValues(
                                        IppConstants.IPP_ATTRIBUTE_NAME__JOB_STATE,
                                        IppConstants.IPP_ATTRIBUTE_NAME__JOB_STATE_REASONS,
                                        IppConstants.IPP_ATTRIBUTE_NAME__JOB_DOCUMENT_ACCESS_ERROR,
                                        IppConstants.IPP_ATTRIBUTE_NAME__JOB_IMPRESSIONS_COMPLETED,
                                        IppConstants.IPP_ATTRIBUTE_NAME__JOB_MEDIA_SHEETS_COMPLETED,
                                        IppConstants.IPP_ATTRIBUTE_NAME__JOB_NAME,
                                        IppConstants.IPP_ATTRIBUTE_NAME__JOB_USER,
                                        IppConstants.IPP_ATTRIBUTE_NAME__JOB_UUID,
                                        IppConstants.IPP_ATTRIBUTE_NAME__JOB_TIME_CREATED,
                                        IppConstants.IPP_ATTRIBUTE_NAME__JOB_TIME_COMPLETED
                                )
                                .build()
                )
                .build();

        Object data;
        int result;
        try {
            data = new JobAttributes(sendIppRequest(context, ippRequest));
            result = IppConnector.Constants.REQUEST_RETURN_CODE__OK;
        } catch (Error error) {
            data = error;
            result = IppConnector.Constants.REQUEST_RETURN_CODE__EXCEPTION;
        }
        return Message.obtain(null, requestID, result, 0, data);
    }

    private boolean isStapleMode(PrintTicket printTicket) {
        if (printTicket.finishingsList == null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Read response data from an InputStream into a ByteArrayOutputStream.
     *
     * @param is   InputStream to read from
     * @param baos ByteArrayOutputStream to write data into
     * @throws IOException if an error occurs while reading the stream
     */
    static void readResponseStream(InputStream is, ByteArrayOutputStream baos) throws IOException {
        byte[] data = new byte[4096];
        int bytesRead;
        while ((bytesRead = is.read(data)) > 0) {
            baos.write(data, 0, bytesRead);
        }
    }
}
