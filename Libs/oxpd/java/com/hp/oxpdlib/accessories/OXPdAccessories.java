// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.accessories;

import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.hp.oxpdlib.OXPdDevice;
import com.hp.oxpdlib.OXPdDevice.RequestCallback;
import com.hp.oxpdlib.SOAPFault;
import com.hp.oxpdlib.common.Binding;
import com.hp.oxpdlib.common.WebResourceWithTimeout;
import com.hp.oxpdlib.discovery.DiscoveredFeature;
import com.hp.oxpdlib.discovery.DiscoveryTree;
import com.hp.oxpdlib.uiconfiguration.UIContext;
import com.hp.sdd.jabberwocky.chat.HttpHeader;
import com.hp.sdd.jabberwocky.chat.HttpRequestResponseContainer;
import com.hp.sdd.jabberwocky.xml.RestXMLNSHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLTagHandler;

import com.hp.sdd.jabberwocky.xml.RestXMLWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

/**
 * This library provides a CORS binding to the OXPd:Accessories service on HP devices.
 */
@SuppressWarnings({"WeakerAccess"})
public class OXPdAccessories {

    // CONSTANTS
    /** Accessories XML namespace prefix */
    private static final String XML_PREFIX_OXPD_ACCESSORIES = "accessories";
    /** Resource name for OXPd Accessories service */
    private static final String OXPD_RESOURCE_TYPE__ACCESSORIES = "OXPd:Accessories";
    /** OXPd Accessories supported revision */
    private static final String OXPD_REVISION__ACCESSORIES = "http://www.hp.com/schemas/imaging/OXPd/service/accessories/2010/04/14";
    /** Composite XML schema prefix/value/version for Accessories */
    static final String XML_SCHEMA__OXPD_ACCESSORIES = XML_PREFIX_OXPD_ACCESSORIES + RestXMLNSHandler.XML_SEPARATOR+
            "http://www.hp.com/schemas/imaging/OXPd/service/accessories/*"+RestXMLNSHandler.XML_SEPARATOR+"2010/04/14";

    /** Accessories SOAP GetSupportedAccessoryClasses method */
    private static final String SOAP_OP__GET_SUPPORTED_ACCESSORY_CLASSES = "GetSupportedAccessoryClasses";

    /** Accessories SOAP EnumerateAccessories method */
    private static final String SOAP_OP__ENUMERATE_ACCESSORIES = "EnumerateAccessories";

    /** Accessories SOAP GetOwnedAccessoryRecords method */
    private static final String SOAP_OP__GET_OWNED_ACCESSORY_RECORDS = "GetOwnedAccessoryRecords";

    /** Accessories SOAP GetSharedAccessoryRecords method */
    private static final String SOAP_OP__GET_SHARED_ACCESSORY_RECORDS = "GetSharedAccessoryRecords";

    /** Accessories SOAP RegisterOwnedAccessory method */
    private static final String SOAP_OP__REGISTER_OWNED_ACCESSORY = "RegisterOwnedAccessory";

    /** Accessories SOAP RegisterSharedAccessory method */
    private static final String SOAP_OP__REGISTER_SHARED_ACCESSORY = "RegisterSharedAccessory";

    /** Accessories SOAP UnregisterAccessory method */
    private static final String SOAP_OP__UNREGISTER_ACCESSORY = "UnregisterAccessory";

    /** Accessories SOAP ResendOwnedAccessoryEvent method */
    private static final String SOAP_OP__RESEND_OWNED_ACCESSORY_EVENT = "ResendOwnedAccessoryEvent";

    /** Accessories SOAP ReserveSharedAccessoryContext method */
    private static final String SOAP_OP__RESERVE_SHARED_ACCESSORY_CONTEXT  = "ReserveSharedAccessoryContext";

    /** Accessories SOAP ReleaseSharedAccessoryContext method */
    private static final String SOAP_OP__RELEASE_SHARED_ACCESSORY_CONTEXT = "ReleaseSharedAccessoryContext";

    /** Accessories SOAP HidOpen method */
    private static final String SOAP_OP__HID_OPEN = "HidOpen";

    /** Accessories SOAP HidOpen method */
    private static final String SOAP_OP__HID_GET_INFO = "HidGetInfo";

    /** Accessories SOAP HidClose method */
    private static final String SOAP_OP__HID_CLOSE = "HidClose";

    /** Accessories SOAP HidStartReading method */
    private static final String SOAP_OP__HID_START_READING = "HidStartReading";

    /** Accessories SOAP HidStopReading method */
    private static final String SOAP_OP__HID_STOP_READING = "HidStopReading";

    /** Accessories SOAP HidReadReport  method */
    private static final String SOAP_OP__HID_READ_REPORT = "HidReadReport";

    /** Accessories SOAP HidWriteReport method */
    private static final String SOAP_OP__HID_WRITE_REPORT = "HidWriteReport";

    /** OXPd common revision */
    private static final String OXPD_REVISION__COMMON = "http://www.hp.com/schemas/imaging/OXPd/common/2010/04/14";

    /** SOAP service name */
    private static final String SOAP_SERVICE_ACCESSORIES = "IAccessoriesService";

    /** Accessories callback receiver endpoint*/
    private static final String OXPD_SERVER__ACCESSORIES_CALLBACK = "AccessoriesCallback";

    /** Hooks to register with internal server */
    private final OXPdDevice.InternalServerHooks mServerHooks;
    /** Status callback */
    private Map<Integer, OXPdDevice.RequestCallback> mEventCallbacks = new HashMap<>();

    // CLASSES

    static class Constants {

        static final String XML_TAG__ACCESSORIES__GET_SUPPORTED_ACCESSORY_CLASSES_RESULT = "GetSupportedAccessoryClassesResult";
        static final String XML_TAG__ACCESSORIES__ACCESSORY_CLASS = "AccessoryClass";

        static final String XML_TAG__ACCESSORIES__GET_OWNED_ACCESSORY_RECORD_RESULT = "GetOwnedAccessoryRecordsResult";
        static final String XML_TAG__ACCESSORIES__OWNED_ACCESSORY_RECORD = "OwnedAccessoryRecord";

        static final String XML_TAG__ACCESSORIES__GET_SHARED_ACCESSORY_RECORD_RESULT = "GetSharedAccessoryRecordsResult";
        static final String XML_TAG__ACCESSORIES__SHARED_ACCESSORY_RECORD = "SharedAccessoryRecord";

        static final String XML_TAG__ACCESSORIES__ENUMERATE_ACCESSORIES_REQUEST = "EnumerateAccessoriesRequest";
        static final String XML_TAG__ACCESSORIES__ENUMERATE_ACCESSORIES_RESULT = "EnumerateAccessoriesResult";
        static final String XML_TAG__ACCESSORIES__ACCESSORY = "Accessory";

        static final String XML_TAG__ACCESSORIES__ACCESSORY_CLASS_PARAM = "accessoryClass";
        static final String XML_TAG__ACCESSORIES__OWNED_ACCESSORY_RECORD_PARAM = "ownedAccessoryRecord";
        static final String XML_TAG__ACCESSORIES__SHARED_ACCESSORY_RECORD_PARAM = "sharedAccessoryRecord";

        static final String XML_TAG__ACCESSORIES__CALLBACK = "callback";
        static final String XML_TAG__ACCESSORIES__VENDOR_ID = "vendorId";
        static final String XML_TAG__ACCESSORIES__PRODUCT_ID = "productId";
        static final String XML_TAG__ACCESSORIES__SERIAL_NUMBER = "serialNumber";
        static final String XML_TAG__ACCESSORIES__UI_CONTEXT = "uiContextId";

        static final String XML_TAG__ACCESSORIES__ACCESSORY_CONTEXT_ID = "accessoryContextId";
        static final String XML_TAG__ACCESSORIES__SERVER_CONTEXT_ID = "serverContextId";

        static final String XML_TAG__ACCESSORIES__ORDINAL = "ordinal";
        static final String XML_TAG__ACCESSORIES__TIMESTAMP = "timestamp";
        static final String XML_TAG__ACCESSORIES__HID_REPORTS = "reports";
        static final String XML_TAG__ACCESSORIES__HID_REPORT = "HidReport";
        static final String XML_TAG__ACCESSORIES__HID_WRITE_REPORT = "hidReport";
        static final String XML_TAG__ACCESSORIES__REPORT_TYPE = "reportType";
        static final String XML_TAG__ACCESSORIES__DATA = "data";

        static final String XML_TAG__ACCESSORIES__FEATURE_REPORT_LENGTH = "featureReportLength";
        static final String XML_TAG__ACCESSORIES__INPUT_REPORT_LENGTH = "inputReportLength";
        static final String XML_TAG__ACCESSORIES__OUTPUT_REPORT_LENGTH = "outputReportLength";
        static final String XML_TAG__ACCESSORIES__IS_READING = "isReading";

        static final String XML_TAG__ACCESSORIES__DESCRIPTION = "description";
        static final String XML_TAG__ACCESSORIES__MANUFACTURER = "manufacturer";
        static final String XML_TAG__ACCESSORIES__IS_OWNED = "isOwned";
    }

    /**
     * Holder for Accessory parameters
     */
    private static class AccessoryParams {
        /** vendor id */
        private final int mVendorId;
        /** Server context */
        private final int mProductId;
        /** Server context */
        private final String mSerialNumber;
        /** UI Context */
        private final UIContext mUIContext;

        /**
         * Accessory parameters holder constructor
         * @param vendorId vendor id
         * @param productId product id
         * @param serialNumber serial number
         */
        private AccessoryParams(int vendorId, int productId, String serialNumber, UIContext uiContext) {
            mVendorId = vendorId;
            mProductId = productId;
            mSerialNumber = serialNumber;
            mUIContext = uiContext;
        }
    }

    /**
     * Holder for Accessory parameters
     */
    private static class AccessoryContextParams {
        /** Accessory context */
        private final String mAccessoryContextId;
        /** Server context */
        private final String mServerContext;
        /** Web callback */
        private final WebResourceWithTimeout mWebResourceWithTimeout;

        /**
         * Accessory parameters holder constructor
         * @param accessoryContextId accessory context id
         * @param serverContext server context
         */
        public AccessoryContextParams(String accessoryContextId, String serverContext, WebResourceWithTimeout webResourceWithTimeout) {
            mAccessoryContextId = accessoryContextId;
            mServerContext = serverContext;
            mWebResourceWithTimeout = webResourceWithTimeout;
        }
    }

    /**
     * Holder for Accessory parameters
     */
    private static class AccessoryReportParams {
        /** Accessory context */
        private final String mAccessoryContextId;
        /** Report type */
        private final HidReportType mHidReportType;
        /** Report */
        private final HidReport mHidReport;

        /**
         * Accessory parameters holder constructor
         * @param accessoryContextId accessory context id
         * @param hidReportType type of report to read
         * @param hidReport report to write
         */
        public AccessoryReportParams(String accessoryContextId, HidReportType hidReportType, HidReport hidReport) {
            this.mAccessoryContextId = accessoryContextId;
            this.mHidReportType = hidReportType;
            this.mHidReport = hidReport;
        }
    }

    /**
     * Scan callback data holder
     */
    private static class AccessoriesCallbackData {
        /** SOAP Message */
        private final String mSOAPMessage;

        /**
         * Constructor
         * @param body
         *          SOAP Message
         */
        private AccessoriesCallbackData(String body) {
            mSOAPMessage = body;
        }
    }

    /**
     * Request handler for scan job events
     */
    private final OXPdDevice.DeviceProcessRequestCallback mAccessoriesCallbackEventRequestHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {
            AccessoriesCallbackData data = (AccessoriesCallbackData)requestParams;
            String accessoriesJobEventData = data.mSOAPMessage;
            RestXMLTagHandler handler = setupDefaultHandler();
            try {
                Object obj;

                if(!TextUtils.isEmpty(accessoriesJobEventData)) {
                    Log.d("OXPd", "Received event " + accessoriesJobEventData.substring(accessoriesJobEventData.length() / 2)); //: " + accessoriesJobEventData);
                } else {
                    Log.d("OXPd", "Received event is empty.");
                }

                if (accessoriesJobEventData.contains("OwnedAccessoryEvent")) {
                    obj = OwnedAccessoryEvent.parseRequestResult(mDevice, accessoriesJobEventData, handler);
                } else {
                    obj = HidReportEvent.parseRequestResult(mDevice, accessoriesJobEventData, handler);
                }
                for (Map.Entry<Integer, OXPdDevice.RequestCallback> callback : mEventCallbacks.entrySet()) {
                    Message msg = Message.obtain(null, callback.getKey(), OXPdDevice.REQUEST_RETURN_CODE__OK, 0, obj);
                    callback.getValue().requestResult(mDevice, msg);
                }
            } catch (Error ignored) {
            }
            return null;
        }
    };

    private final OXPdDevice.HandleIncomingServerRequest mAccessoriesCallbackRequestHandler = new OXPdDevice.HandleIncomingServerRequest() {
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
                mDevice.queueRequest(mAccessoriesCallbackEventRequestHandler,
                        new AccessoriesCallbackData(soapMessageBody),
                        0, null
                );
            }

            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.ACCEPTED, NanoHTTPD.MIME_PLAINTEXT, null, 0);
        }
    };

    /**
     * UI Configuration SOAP request builder
     */
    private abstract class AccessoriesSOAPRequestBuilder implements OXPdDevice.SOAPRequestBuilder {

        /**
         * Use admin credentials as part of request?
         * @return
         *              true
         */
        @Override
        public boolean useAdminCredentials() {
            return false;
        }

        /**
         * Return the UI Configuration URL
         * @return
         *              URL for request
         */
        @Override
        public URL getURL() {
            return mAccessoriesURL;
        }

        /**
         * Return XML schemas used by ui configuration
         * @return
         *              List of XML schemas
         */
        @Override
        public List<String> getXMLSchemas() {
            return Arrays.asList(XML_SCHEMA__OXPD_ACCESSORIES, OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON);
        }

        /**
         * Return service name
         * @return
         *              Service name
         */
        @Override
        public String getServiceName() {
            return SOAP_SERVICE_ACCESSORIES;
        }

        /**
         * Return the service revision string
         * @return
         *              Service revision
         */
        @Override
        public String getServiceRevision() {
            return OXPD_REVISION__ACCESSORIES;
        }

        /**
         * Return list of HTTP headers to include as part of request
         * @return
         *              Empty list
         */
        @Override
        public List<HttpHeader> getRequestHeaders() {
            return Collections.emptyList();
        }
    }

    /**
     * Request handler for {@link #GetSupportedAccessoryClasses(int, RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mGetSupportedAccessoryClassesHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new AccessoriesSOAPRequestBuilder() {

                @Override
                public String getSoapOperation() {
                    return SOAP_OP__GET_SUPPORTED_ACCESSORY_CLASSES;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    try {
                        xmlWriter.writeStartTag(XML_SCHEMA__OXPD_ACCESSORIES, getSoapOperation(), null);
                        xmlWriter.writeEndTag(XML_SCHEMA__OXPD_ACCESSORIES, getSoapOperation());
                    } catch (Exception e) {
                        Log.e("OXPd","error",e);
                        throw e;
                    }
                }
            });

            int result;
            Object data;
            try {
                data = AccessoryClass.parseRequestResult(mDevice, requestResponse, checkHTTPResponse(requestResponse));
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Error ignored) {
                data = ignored;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
                Log.e("OXPd", "error",ignored);
            }
            return Message.obtain(null, requestID, result, 0,  data);
        }
    };

    /**
     * Request handler for {@link #EnumerateAccessories(int, RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mEnumerateAccessoriesHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new AccessoriesSOAPRequestBuilder() {

                @Override
                public String getSoapOperation() {
                    return SOAP_OP__ENUMERATE_ACCESSORIES;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    try {
                        xmlWriter.writeStartTag(XML_SCHEMA__OXPD_ACCESSORIES, getSoapOperation(), null);
                        xmlWriter.writeStartTag(XML_SCHEMA__OXPD_ACCESSORIES, Constants.XML_TAG__ACCESSORIES__ENUMERATE_ACCESSORIES_REQUEST, null);
                        xmlWriter.writeEndTag(XML_SCHEMA__OXPD_ACCESSORIES, Constants.XML_TAG__ACCESSORIES__ENUMERATE_ACCESSORIES_REQUEST);
                        xmlWriter.writeEndTag(XML_SCHEMA__OXPD_ACCESSORIES, getSoapOperation());
                    } catch (Exception e) {
                        Log.e("OXPd","error",e);
                        throw e;
                    }
                }
            });

            int result;
            Object data;
            try {
                data = Accessory.parseRequestResult(mDevice, requestResponse, checkHTTPResponse(requestResponse));
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Error ignored) {
                data = ignored;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
                Log.e("OXPd", "error",ignored);
            }
            return Message.obtain(null, requestID, result, 0,  data);
        }
    };

    /**
     * Request handler for {@link #GetOwnedAccessoryRecords(int, RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mGetOwnedAccessoryRecordsHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new AccessoriesSOAPRequestBuilder() {

                @Override
                public boolean useAdminCredentials() {
                    return true;
                }

                @Override
                public String getSoapOperation() {
                    return SOAP_OP__GET_OWNED_ACCESSORY_RECORDS;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    try {
                        xmlWriter.writeStartTag(XML_SCHEMA__OXPD_ACCESSORIES, getSoapOperation(), null);
                        xmlWriter.writeEndTag(XML_SCHEMA__OXPD_ACCESSORIES, getSoapOperation());
                    } catch (Exception e) {
                        Log.e("OXPd","error",e);
                        throw e;
                    }
                }
            });

            int result;
            Object data;
            try {
                data = OwnedAccessoryRecord.parseRequestResult(mDevice, requestResponse, checkHTTPResponse(requestResponse));
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Error ignored) {
                data = ignored;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
                Log.e("OXPd", "error",ignored);
            }
            return Message.obtain(null, requestID, result, 0,  data);
        }
    };

    /**
     * Request handler for {@link #GetSharedAccessoryRecords(int, RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mGetSharedAccessoryRecordsHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new AccessoriesSOAPRequestBuilder() {

                @Override
                public boolean useAdminCredentials() {
                    return true;
                }

                @Override
                public String getSoapOperation() {
                    return SOAP_OP__GET_SHARED_ACCESSORY_RECORDS;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    try {
                        xmlWriter.writeStartTag(XML_SCHEMA__OXPD_ACCESSORIES, getSoapOperation(), null);
                        xmlWriter.writeEndTag(XML_SCHEMA__OXPD_ACCESSORIES, getSoapOperation());
                    } catch (Exception e) {
                        Log.e("OXPd","error",e);
                        throw e;
                    }
                }
            });

            int result;
            Object data;
            try {
                data = SharedAccessoryRecord.parseRequestResult(mDevice, requestResponse, checkHTTPResponse(requestResponse));
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Error ignored) {
                data = ignored;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
                Log.e("OXPd", "error",ignored);
            }
            return Message.obtain(null, requestID, result, 0,  data);
        }
    };

    /**
     * Request handler for {@link #RegisterAccessory(int, OXPdDevice.RequestCallback, int, OXPdDevice.RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mRegisterAccessoryHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {

            //noinspection unchecked
            registerAccessoryCallbacks();

            int result;
            Object data = null;
//            try {

                result = OXPdDevice.REQUEST_RETURN_CODE__OK;

            String callBackUrl = mDevice.getDeviceAbsoluteURLForPath(OXPD_SERVER__ACCESSORIES_CALLBACK);

            data = callBackUrl;
//            } catch(Error ignored) {
//                unregisterAccessoryCallbacks(accessoriesParams.mServerContext);
//                data = ignored;
//                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
//            }
            return Message.obtain(null, requestID, result, 0,  data);
        }
    };

    /**
     * Request handler for {@link #RegisterOwnedAccessory(OwnedAccessoryRecord, int, RequestCallback, int, RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mRegisterOwnedAccessoryHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {
            final OwnedAccessoryRecord ownedAccessoryRecord = (OwnedAccessoryRecord) requestParams;

            // setting callback
            String callBackUrl = mDevice.getDeviceAbsoluteURLForPath(OXPD_SERVER__ACCESSORIES_CALLBACK);
            ownedAccessoryRecord.callback = new WebResourceWithTimeout(callBackUrl, Binding.Soap12, 60, 60, null);

            registerAccessoryCallbacks();

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new AccessoriesSOAPRequestBuilder() {

                @Override
                public boolean useAdminCredentials() {
                    return true;
                }

                @Override
                public String getSoapOperation() {
                    return SOAP_OP__REGISTER_OWNED_ACCESSORY;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    try {
                        xmlWriter.writeStartTag(XML_SCHEMA__OXPD_ACCESSORIES, getSoapOperation(), null);
                        OwnedAccessoryRecord.writeToXml(ownedAccessoryRecord, xmlWriter);
                        xmlWriter.writeEndTag(XML_SCHEMA__OXPD_ACCESSORIES, getSoapOperation());
                    } catch (Exception e) {
                        Log.e("OXPd","error",e);
                        throw e;
                    }
                }
            });

            int result;
            Object data = null;
            try {
                RestXMLTagHandler tagHandler = checkHTTPResponse(requestResponse);
                mDevice.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);
                faultExceptionCheck(tagHandler);
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Error ignored) {
                data = ignored;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
                Log.e("OXPd", "error",ignored);
            }
            return Message.obtain(null, requestID, result, 0,  data);
        }
    };

    /**
     * Request handler for {@link #RegisterSharedAccessory(SharedAccessoryRecord, int, RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mRegisterSharedAccessoryHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {
            final SharedAccessoryRecord sharedAccessoryRecord = (SharedAccessoryRecord) requestParams;
            registerAccessoryCallbacks();

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new AccessoriesSOAPRequestBuilder() {

                @Override
                public boolean useAdminCredentials() {
                    return true;
                }

                @Override
                public String getSoapOperation() {
                    return SOAP_OP__REGISTER_SHARED_ACCESSORY;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    try {
                        xmlWriter.writeStartTag(XML_SCHEMA__OXPD_ACCESSORIES, getSoapOperation(), null);
                        SharedAccessoryRecord.writeToXml(sharedAccessoryRecord, xmlWriter);
                        xmlWriter.writeEndTag(XML_SCHEMA__OXPD_ACCESSORIES, getSoapOperation());
                    } catch (Exception e) {
                        Log.e("OXPd","error",e);
                        throw e;
                    }
                }
            });

            int result;
            Object data = null;
            try {
                RestXMLTagHandler tagHandler = checkHTTPResponse(requestResponse);
                mDevice.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);
                faultExceptionCheck(tagHandler);
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Error ignored) {
                data = ignored;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
                Log.e("OXPd", "error",ignored);
            }
            return Message.obtain(null, requestID, result, 0,  data);
        }
    };

    /**
     * Request handler for {@link #UnregisterAccessory(int, int, String, int, RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mUnregisterAccessoryHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {
            final AccessoryParams accessoryParams = (AccessoryParams) requestParams;
            registerAccessoryCallbacks();

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new AccessoriesSOAPRequestBuilder() {

                @Override
                public boolean useAdminCredentials() {
                    return true;
                }

                @Override
                public String getSoapOperation() {
                    return SOAP_OP__UNREGISTER_ACCESSORY;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    try {
                        xmlWriter.writeStartTag(XML_SCHEMA__OXPD_ACCESSORIES, getSoapOperation(), null);
                        xmlWriter.writeTag(XML_SCHEMA__OXPD_ACCESSORIES, Constants.XML_TAG__ACCESSORIES__VENDOR_ID,
                                null, "%d", accessoryParams.mVendorId);
                        xmlWriter.writeTag(XML_SCHEMA__OXPD_ACCESSORIES, Constants.XML_TAG__ACCESSORIES__PRODUCT_ID,
                                null, "%d", accessoryParams.mProductId);
                        if (accessoryParams.mSerialNumber != null) {
                            xmlWriter.writeTag(XML_SCHEMA__OXPD_ACCESSORIES, Constants.XML_TAG__ACCESSORIES__SERIAL_NUMBER,
                                    null, "%s", accessoryParams.mSerialNumber);
                        }
                        xmlWriter.writeEndTag(XML_SCHEMA__OXPD_ACCESSORIES, getSoapOperation());
                    } catch (Exception e) {
                        Log.e("OXPd","error",e);
                        throw e;
                    }
                }
            });

            int result;
            Object data = null;
            try {
                RestXMLTagHandler tagHandler = checkHTTPResponse(requestResponse);
                mDevice.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);
                faultExceptionCheck(tagHandler);
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Error ignored) {
                data = ignored;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
                Log.e("OXPd", "error",ignored);
            }
            return Message.obtain(null, requestID, result, 0,  data);
        }
    };

    /**
     * Request handler for {@link #ResendOwnedAccessoryEvent(int, int, String, int, RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mResendOwnedAccessoryEventHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {
            final AccessoryParams accessoryParams = (AccessoryParams) requestParams;
            registerAccessoryCallbacks();

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new AccessoriesSOAPRequestBuilder() {

                @Override
                public String getSoapOperation() {
                    return SOAP_OP__RESEND_OWNED_ACCESSORY_EVENT;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    try {
                        xmlWriter.writeStartTag(XML_SCHEMA__OXPD_ACCESSORIES, getSoapOperation(), null);
                        xmlWriter.writeTag(XML_SCHEMA__OXPD_ACCESSORIES, Constants.XML_TAG__ACCESSORIES__VENDOR_ID,
                                null, "%d", accessoryParams.mVendorId);
                        xmlWriter.writeTag(XML_SCHEMA__OXPD_ACCESSORIES, Constants.XML_TAG__ACCESSORIES__PRODUCT_ID,
                                null, "%d", accessoryParams.mProductId);
                        if (accessoryParams.mSerialNumber != null) {
                            xmlWriter.writeTag(XML_SCHEMA__OXPD_ACCESSORIES, Constants.XML_TAG__ACCESSORIES__SERIAL_NUMBER,
                                    null, "%s", accessoryParams.mSerialNumber);
                        }
                        xmlWriter.writeEndTag(XML_SCHEMA__OXPD_ACCESSORIES, getSoapOperation());
                    } catch (Exception e) {
                        Log.e("OXPd","error",e);
                        throw e;
                    }
                }
            });

            int result;
            Object data = null;
            try {
                RestXMLTagHandler tagHandler = checkHTTPResponse(requestResponse);
                mDevice.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);
                faultExceptionCheck(tagHandler);
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Error ignored) {
                data = ignored;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
                Log.e("OXPd", "error",ignored);
            }
            return Message.obtain(null, requestID, result, 0,  data);
        }
    };

    /**
     * Request handler for {@link #ReserveSharedAccessoryContext(int, int, String, UIContext, int, RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mReserveSharedAccessoryContextHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {
            final AccessoryParams accessoryParams = (AccessoryParams) requestParams;
            registerAccessoryCallbacks();

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new AccessoriesSOAPRequestBuilder() {

                @Override
                public String getSoapOperation() {
                    return SOAP_OP__RESERVE_SHARED_ACCESSORY_CONTEXT;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    try{
                        xmlWriter.writeStartTag(XML_SCHEMA__OXPD_ACCESSORIES, getSoapOperation(), null);
                        xmlWriter.writeTag(XML_SCHEMA__OXPD_ACCESSORIES, Constants.XML_TAG__ACCESSORIES__UI_CONTEXT, null, "%s",
                                accessoryParams.mUIContext.getUIContext());
                        xmlWriter.writeTag(XML_SCHEMA__OXPD_ACCESSORIES, Constants.XML_TAG__ACCESSORIES__VENDOR_ID,
                                null, "%d", accessoryParams.mVendorId);
                        xmlWriter.writeTag(XML_SCHEMA__OXPD_ACCESSORIES, Constants.XML_TAG__ACCESSORIES__PRODUCT_ID,
                                null, "%d", accessoryParams.mProductId);
                        xmlWriter.writeTag(XML_SCHEMA__OXPD_ACCESSORIES, Constants.XML_TAG__ACCESSORIES__SERIAL_NUMBER,
                                null, "%s", accessoryParams.mSerialNumber != null ? accessoryParams.mSerialNumber : "");
                        xmlWriter.writeEndTag(XML_SCHEMA__OXPD_ACCESSORIES, getSoapOperation());
                    }catch(Exception e){
                        Log.e("OXPd","error",e);
                        throw e;
                    }

                }
            });

            int result;
            Object data;
            try {
                AccessoryContextID accessoryContextID = AccessoryContextID.parseRequestResult(mDevice, requestResponse, checkHTTPResponse(requestResponse));
                data = accessoryContextID.getAccessoryContextID();
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Error ignored) {
                data = ignored;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
                Log.e("OXPd", "error",ignored);
            }
            return Message.obtain(null, requestID, result, 0,  data);
        }
    };

    /**
     * Request handler for {@link #ReleaseSharedAccessoryContext(String, int, RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mReleaseSharedAccessoryContextHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {
            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new AccessoriesSOAPRequestBuilder() {

                @Override
                public String getSoapOperation() {
                    return SOAP_OP__RELEASE_SHARED_ACCESSORY_CONTEXT;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    try {
                        xmlWriter.writeStartTag(XML_SCHEMA__OXPD_ACCESSORIES, getSoapOperation(), null);
                        xmlWriter.writeTag(XML_SCHEMA__OXPD_ACCESSORIES, Constants.XML_TAG__ACCESSORIES__ACCESSORY_CONTEXT_ID, null, "%s", (String) requestParams);
                        xmlWriter.writeEndTag(XML_SCHEMA__OXPD_ACCESSORIES, getSoapOperation());
                    }catch(Exception e){
                        Log.e("OXPd","error",e);
                        throw e;
                    }
                }
            });

            int result;
            Object data = null;
            try {
                RestXMLTagHandler tagHandler = checkHTTPResponse(requestResponse);
                mDevice.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);
                faultExceptionCheck(tagHandler);
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Error ignored) {
                data = ignored;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
                Log.e("OXPd", "error",ignored);
            }
            return Message.obtain(null, requestID, result, 0,  data);
        }
    };

    /**
     * Request handler for {@link #HidOpen(String, int, RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mHidOpenHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new AccessoriesSOAPRequestBuilder() {

                @Override
                public String getSoapOperation() {
                    return SOAP_OP__HID_OPEN;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    try {
                        xmlWriter.writeStartTag(XML_SCHEMA__OXPD_ACCESSORIES, getSoapOperation(), null);
                        xmlWriter.writeTag(XML_SCHEMA__OXPD_ACCESSORIES, Constants.XML_TAG__ACCESSORIES__ACCESSORY_CONTEXT_ID, null, "%s", (String) requestParams);
                        xmlWriter.writeEndTag(XML_SCHEMA__OXPD_ACCESSORIES, getSoapOperation());
                    }catch(Exception e){
                        Log.e("OXPd","error",e);
                        throw e;
                    }
                }
            });

            int result;
            Object data = null;
            try {
                RestXMLTagHandler tagHandler = checkHTTPResponse(requestResponse);
                mDevice.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);
                faultExceptionCheck(tagHandler);
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Error ignored) {
                data = ignored;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
                Log.e("OXPd", "error",ignored);
            }
            return Message.obtain(null, requestID, result, 0,  data);
        }
    };

    /**
     * Request handler for {@link #HidGetInfo(String, int, RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mHidGetInfoHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new AccessoriesSOAPRequestBuilder() {

                @Override
                public String getSoapOperation() {
                    return SOAP_OP__HID_GET_INFO;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    try {
                        xmlWriter.writeStartTag(XML_SCHEMA__OXPD_ACCESSORIES, getSoapOperation(), null);
                        xmlWriter.writeTag(XML_SCHEMA__OXPD_ACCESSORIES, Constants.XML_TAG__ACCESSORIES__ACCESSORY_CONTEXT_ID, null, "%s", (String) requestParams);
                        xmlWriter.writeEndTag(XML_SCHEMA__OXPD_ACCESSORIES, getSoapOperation());
                    }catch(Exception e){
                        Log.e("OXPd","error",e);
                        throw e;
                    }
                }
            });

            int result;
            Object data;
            try {
                data = HidInfo.parseRequestResult(mDevice, requestResponse, checkHTTPResponse(requestResponse));
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Error ignored) {
                data = ignored;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
                Log.e("OXPd", "error",ignored);
            }
            return Message.obtain(null, requestID, result, 0,  data);
        }
    };

    /**
     * Request handler for {@link #HidClose(String, int, RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mHidCloseHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {
            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new AccessoriesSOAPRequestBuilder() {

                @Override
                public String getSoapOperation() {
                    return SOAP_OP__HID_CLOSE;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    try {
                        xmlWriter.writeStartTag(XML_SCHEMA__OXPD_ACCESSORIES, getSoapOperation(), null);
                        xmlWriter.writeTag(XML_SCHEMA__OXPD_ACCESSORIES, Constants.XML_TAG__ACCESSORIES__ACCESSORY_CONTEXT_ID, null, "%s", (String) requestParams);
                        xmlWriter.writeEndTag(XML_SCHEMA__OXPD_ACCESSORIES, getSoapOperation());
                    }catch(Exception e){
                        Log.e("OXPd","error",e);
                        throw e;
                    }
                }
            });

            int result;
            Object data = null;
            try {
                RestXMLTagHandler tagHandler = checkHTTPResponse(requestResponse);
                mDevice.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);
                faultExceptionCheck(tagHandler);
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Error ignored) {
                data = ignored;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
                Log.e("OXPd", "error",ignored);
            }
            return Message.obtain(null, requestID, result, 0,  data);
        }
    };

    /**
     * Request handler for {@link #HidStartReading(String, String, int, RequestCallback, int, RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mHidStartReadingHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {
            final AccessoryContextParams accessoryContextParams = (AccessoryContextParams) requestParams;

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new AccessoriesSOAPRequestBuilder() {

                @Override
                public String getSoapOperation() {
                    return SOAP_OP__HID_START_READING;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    try {
                        xmlWriter.writeStartTag(XML_SCHEMA__OXPD_ACCESSORIES, getSoapOperation(), null);
                        xmlWriter.writeTag(XML_SCHEMA__OXPD_ACCESSORIES, Constants.XML_TAG__ACCESSORIES__ACCESSORY_CONTEXT_ID, null, "%s", accessoryContextParams.mAccessoryContextId);
                        xmlWriter.writeTag(XML_SCHEMA__OXPD_ACCESSORIES, Constants.XML_TAG__ACCESSORIES__SERVER_CONTEXT_ID, null, "%s", accessoryContextParams.mServerContext);

                        if (accessoryContextParams.mWebResourceWithTimeout != null) {
                            xmlWriter.writeStartTag(XML_SCHEMA__OXPD_ACCESSORIES, OXPdDevice.Constants.XML_TAG__COMMON__CALLBACK, null);
                            WebResourceWithTimeout.writeToXML(accessoryContextParams.mWebResourceWithTimeout, xmlWriter);
                            xmlWriter.writeEndTag(XML_SCHEMA__OXPD_ACCESSORIES, OXPdDevice.Constants.XML_TAG__COMMON__CALLBACK);
                        }

                        xmlWriter.writeEndTag(XML_SCHEMA__OXPD_ACCESSORIES, getSoapOperation());
                    }catch(Exception e){
                        Log.e("OXPd","error",e);
                        throw e;
                    }
                }
            });

            int result;
            Object data = null;
            try {
                RestXMLTagHandler tagHandler = checkHTTPResponse(requestResponse);
                mDevice.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);
                faultExceptionCheck(tagHandler);
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Error ignored) {
                data = ignored;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
                Log.e("OXPd", "error",ignored);
            }
            return Message.obtain(null, requestID, result, 0,  data);
        }
    };

    /**
     * Request handler for {@link #HidStopReading(String, int, RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mHidStopReadingHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {
            final AccessoryContextParams accessoryContextParams = (AccessoryContextParams) requestParams;

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new AccessoriesSOAPRequestBuilder() {

                @Override
                public String getSoapOperation() {
                    return SOAP_OP__HID_STOP_READING;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    try {
                        xmlWriter.writeStartTag(XML_SCHEMA__OXPD_ACCESSORIES, getSoapOperation(), null);
                        xmlWriter.writeTag(XML_SCHEMA__OXPD_ACCESSORIES, Constants.XML_TAG__ACCESSORIES__ACCESSORY_CONTEXT_ID, null, "%s", accessoryContextParams.mAccessoryContextId);
                        xmlWriter.writeEndTag(XML_SCHEMA__OXPD_ACCESSORIES, getSoapOperation());
                    }catch(Exception e){
                        Log.e("OXPd","error",e);
                        throw e;
                    }
                }
            });

            int result;
            Object data = null;
            try {
                RestXMLTagHandler tagHandler = checkHTTPResponse(requestResponse);
                mDevice.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);
                faultExceptionCheck(tagHandler);
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Error ignored) {
                data = ignored;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
                Log.e("OXPd", "error",ignored);
            }
            return Message.obtain(null, requestID, result, 0,  data);
        }
    };

    /**
     * Request handler for {@link #HidReadReport(String, HidReportType, int, RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mHidReadReportHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {
            final AccessoryReportParams accessoryReportParams = (AccessoryReportParams) requestParams;

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new AccessoriesSOAPRequestBuilder() {

                @Override
                public String getSoapOperation() {
                    return SOAP_OP__HID_READ_REPORT;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    try {
                        xmlWriter.writeStartTag(XML_SCHEMA__OXPD_ACCESSORIES, getSoapOperation(), null);
                        xmlWriter.writeTag(XML_SCHEMA__OXPD_ACCESSORIES, Constants.XML_TAG__ACCESSORIES__ACCESSORY_CONTEXT_ID, null, "%s", accessoryReportParams.mAccessoryContextId);
                        xmlWriter.writeTag(XML_SCHEMA__OXPD_ACCESSORIES, Constants.XML_TAG__ACCESSORIES__REPORT_TYPE, null, "%s", accessoryReportParams.mHidReportType.value);
                        xmlWriter.writeEndTag(XML_SCHEMA__OXPD_ACCESSORIES, getSoapOperation());
                    }catch(Exception e){
                        Log.e("OXPd","error",e);
                        throw e;
                    }
                }
            });

            int result;
            Object data;
            try {
                data = HidReport.parseRequestResult(mDevice, requestResponse, checkHTTPResponse(requestResponse));
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Error ignored) {
                data = ignored;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
                Log.e("OXPd", "error",ignored);
            }
            return Message.obtain(null, requestID, result, 0,  data);
        }
    };

    /**
     * Request handler for {@link #HidWriteReport(String, HidReport, int, RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mHidWriteReportHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {
            final AccessoryReportParams accessoryReportParams = (AccessoryReportParams) requestParams;

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new AccessoriesSOAPRequestBuilder() {

                @Override
                public String getSoapOperation() {
                    return SOAP_OP__HID_WRITE_REPORT;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    try {
                        xmlWriter.writeStartTag(XML_SCHEMA__OXPD_ACCESSORIES, getSoapOperation(), null);
                        xmlWriter.writeTag(XML_SCHEMA__OXPD_ACCESSORIES, Constants.XML_TAG__ACCESSORIES__ACCESSORY_CONTEXT_ID, null, "%s", accessoryReportParams.mAccessoryContextId);
                        HidReport.writeToXML(accessoryReportParams.mHidReport, xmlWriter);
                        xmlWriter.writeEndTag(XML_SCHEMA__OXPD_ACCESSORIES, getSoapOperation());
                    }catch(Exception e){
                        Log.e("OXPd","error",e);
                        throw e;
                    }
                }
            });

            int result;
            Object data;
            try {
                data = HidReport.parseRequestResult(mDevice, requestResponse, checkHTTPResponse(requestResponse));
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Error ignored) {
                data = ignored;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
                Log.e("OXPd", "error",ignored);
            }
            return Message.obtain(null, requestID, result, 0,  data);
        }
    };

    // VARIABLES
    /** OXPd device instance */
    private final OXPdDevice mDevice;
    /** UI Configuration service URL */
    private final URL mAccessoriesURL;

    // CONSTRUCTORS
    /**
     * Constructor for Device Info instance
     * @param device
     *              OXPd device instance
     * @param discoveryTree
     *              Discovery tree for current OXPd device
     * @throws Error
     *              when something goes wrong
     */
    public OXPdAccessories(OXPdDevice device, OXPdDevice.InternalServerHooks serverHooks, DiscoveryTree discoveryTree) throws Error {
        mDevice = device;

        if (discoveryTree == null) throw new Error(ErrorName.AjaxError, "Connection failed");

        DiscoveredFeature feature =
                discoveryTree.GetDiscoveredFeatureByResourceTypeAndRevision(
                        OXPD_RESOURCE_TYPE__ACCESSORIES,
                        OXPD_REVISION__ACCESSORIES);
        if (feature == null) {
            throw new Error(ErrorName.ServiceNotFound, OXPD_RESOURCE_TYPE__ACCESSORIES + " is not supported on the target device");
        }

        try {
            mAccessoriesURL = device.getOXPdUrl(feature.resourceURI);
        } catch (MalformedURLException e) {
            throw new Error(ErrorName.Unknown, "invalid URL");
        }

        mDevice.getXMLNSHandler().addXMLNS(XML_PREFIX_OXPD_ACCESSORIES, OXPD_REVISION__ACCESSORIES);
        mDevice.getXMLNSHandler().addXMLNS(OXPdDevice.Constants.XML_PREFIX_OXPD_COMMON, OXPD_REVISION__COMMON);

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

    public void RegisterEventCallback(int requestID, OXPdDevice.RequestCallback callback) {
        registerAccessoryCallbacks();

        mEventCallbacks.put(requestID, callback);
    }

    /**
     * @param requestID
     *              Request ID associated with this call
     * @param callback
     *              Callback to invoke when operation is finished.
     */
    public void RegisterAccessory(int requestID, OXPdDevice.RequestCallback callback,
                                  int statusRequestID, OXPdDevice.RequestCallback statusCallback) {
        //mDevice.queueRequest(mGetDefaultBasicOptionsHandler, mode, requestID, callback);
        Log.d("OXPdAccessories", "Service status:" + mDevice.checkServerStatus());

        mDevice.queueRequest(mRegisterAccessoryHandler, null, requestID, callback);
    }

    /**
     * Returns the list of USB accessory classes (HID, CCID, etc.) supported by the device.
     * (Will be empty if there are no supported classes.)
     *
     * @param requestID
     *              Request ID associated with this call
     * @param callback
     *              Callback to invoke when operation is finished.
     */
    public void GetSupportedAccessoryClasses(int requestID, OXPdDevice.RequestCallback callback) {
        mDevice.queueRequest(mGetSupportedAccessoryClassesHandler, null, requestID, callback);
    }

    /**
     * Returns the list of all USB accessories currently plugged into the device and that match a registration record.
     *
     * @param requestID
     *              Request ID associated with this call
     * @param callback
     *              Callback to invoke when operation is finished.
     */
    public void EnumerateAccessories(int requestID, OXPdDevice.RequestCallback callback) {
        mDevice.queueRequest(mEnumerateAccessoriesHandler, null, requestID, callback);
    }

    /**
     * Returns the list of registration records for USB accessories registered as "owned".
     *
     * @param requestID
     *              Request ID associated with this call
     * @param callback
     *              Callback to invoke when operation is finished.
     */
    public void GetOwnedAccessoryRecords(int requestID, OXPdDevice.RequestCallback callback) {
        mDevice.queueRequest(mGetOwnedAccessoryRecordsHandler, null, requestID, callback);
    }

    /**
     * Returns the list of registration records for USB accessories registered as "shared".
     *
     * @param requestID
     *              Request ID associated with this call
     * @param callback
     *              Callback to invoke when operation is finished.
     */
    public void GetSharedAccessoryRecords(int requestID, OXPdDevice.RequestCallback callback) {
        mDevice.queueRequest(mGetSharedAccessoryRecordsHandler, null, requestID, callback);
    }

    /**
     * Registers an accessory (or set of accessories) for exclusive use by a single web application.
     * Owned accessories are not supported for Whitelisted Remote Apps.
     *
     * @param ownedAccessoryRecord
     *              Owned Accessory record
     * @param requestID
     *              Request ID associated with this call
     * @param callback
     *              Callback to invoke when operation is finished.
     * @param statusRequestID
     *              Request ID associated with context change callback
     * @param statusCallback
     *              Callback to invoke when accessory context is changed.
     */
    public void RegisterOwnedAccessory(OwnedAccessoryRecord ownedAccessoryRecord,
            int requestID, OXPdDevice.RequestCallback callback,
            int statusRequestID, OXPdDevice.RequestCallback statusCallback) {
        // commented for calls from PacMan (server should not be started in PacMan)
        // mDevice.checkServerStatus();

        mEventCallbacks.put(statusRequestID, statusCallback);

        mDevice.queueRequest(mRegisterOwnedAccessoryHandler, ownedAccessoryRecord, requestID, callback);
    }

    /**
     * Registers an accessory (or set of accessories) and allows use by any web application with a valid UI context ID.
     *
     * @param sharedAccessoryRecord
     *              Shared Accessory record
     * @param requestID
     *              Request ID associated with this call
     * @param callback
     *              Callback to invoke when operation is finished.
     */
    public void RegisterSharedAccessory(SharedAccessoryRecord sharedAccessoryRecord,
            int requestID, OXPdDevice.RequestCallback callback) {
        mDevice.queueRequest(mRegisterSharedAccessoryHandler, sharedAccessoryRecord, requestID, callback);
    }

    /**
     * Removes the registration record for this vendorId, productId, and serial number (which may be null).
     *
     * @param requestID
     *              Request ID associated with this call
     * @param vendorId
     *              Accessory Vendor ID
     * @param productId
     *              Accessory Product ID
     * @param serialNumber
     *              Accessory Serial Number
     * @param callback
     *              Callback to invoke when operation is finished.
     */
    public void UnregisterAccessory(int vendorId, int productId, String serialNumber,
            int requestID, OXPdDevice.RequestCallback callback) {
        mDevice.queueRequest(mUnregisterAccessoryHandler, new AccessoryParams(vendorId, productId, serialNumber, null), requestID, callback);
    }

    /**
     * Forces the OwnedAccessoryEvent for an "owned" accessory (or set of "owned" accessories) to be resent.
     *
     * @param requestID
     *              Request ID associated with this call
     * @param vendorId
     *              Accessory Vendor ID
     * @param productId
     *              Accessory Product ID
     * @param serialNumber
     *              Accessory Serial Number
     * @param callback
     *              Callback to invoke when operation is finished.
     */
    public void ResendOwnedAccessoryEvent(int vendorId, int productId, String serialNumber,
            int requestID, OXPdDevice.RequestCallback callback) {
        mDevice.checkServerStatus();

        mDevice.queueRequest(mResendOwnedAccessoryEventHandler, new AccessoryParams(vendorId, productId, serialNumber, null), requestID, callback);
    }

    /**
     * Creates a shared accessory context from a valid UI context ID.
     *
     * @param requestID
     *              Request ID associated with this call
     * @param vendorId
     *              Accessory Vendor ID
     * @param productId
     *              Accessory Product ID
     * @param serialNumber
     *              Accessory Serial Number
     * @param uiContext
     *              UI Context
     * @param callback
     *              Callback to invoke when operation is finished.
     */
    public void ReserveSharedAccessoryContext(int vendorId, int productId, String serialNumber, UIContext uiContext,
            int requestID, OXPdDevice.RequestCallback callback) {
        mDevice.queueRequest(mReserveSharedAccessoryContextHandler, new AccessoryParams(vendorId, productId, serialNumber, uiContext), requestID, callback);
    }

    /**
     * Releases a shared accessory context.
     *
     * @param requestID
     *              Request ID associated with this call
     * @param accessoryContextId
     *              Accessory Context ID
     * @param callback
     *              Callback to invoke when operation is finished.
     */
    public void ReleaseSharedAccessoryContext (String accessoryContextId,
            int requestID, OXPdDevice.RequestCallback callback) {
        mDevice.queueRequest(mReleaseSharedAccessoryContextHandler, accessoryContextId, requestID, callback);
    }

    /**
     * Opens an HID accessory.
     *
     * @param requestID
     *              Request ID associated with this call
     * @param accessoryContextId
     *              Accessory Context ID
     * @param callback
     *              Callback to invoke when operation is finished.
     */
    public void HidOpen(String accessoryContextId, int requestID, OXPdDevice.RequestCallback callback) {
        mDevice.queueRequest(mHidOpenHandler, accessoryContextId, requestID, callback);
    }

    /**
     * Returns detailed, HID-specific information about an individual HID accessory.
     *
     * @param requestID
     *              Request ID associated with this call
     * @param accessoryContextId
     *              Accessory Context ID
     * @param callback
     *              Callback to invoke when operation is finished.
     */
    public void HidGetInfo(String accessoryContextId, int requestID, OXPdDevice.RequestCallback callback) {
        mDevice.queueRequest(mHidGetInfoHandler, accessoryContextId, requestID, callback);
    }

    /**
     * Closes an HID accessory.
     *
     * @param requestID
     *              Request ID associated with this call
     * @param accessoryContextId
     *              Accessory Context ID
     * @param callback
     *              Callback to invoke when operation is finished.
     */
    public void HidClose(String accessoryContextId, int requestID, OXPdDevice.RequestCallback callback) {
        mDevice.queueRequest(mHidCloseHandler, accessoryContextId, requestID, callback);
    }

    /**
     * Signals the device to start processing asynchronous reads from the interrupt in pipe of an HID accessory.
     *
     * @param requestID
     *              Request ID associated with this call
     * @param accessoryContextId
     *              Accessory Context ID
     * @param callback
     *              Callback to invoke when operation is finished.
     */
    public void HidStartReading(String accessoryContextId, String serverContextId,
            int requestID, OXPdDevice.RequestCallback callback,
            int reportRequestID, OXPdDevice.RequestCallback reportCallback) {
        mDevice.checkServerStatus();

        mEventCallbacks.put(reportRequestID, reportCallback);

        mDevice.queueRequest(mHidStartReadingHandler,
                new AccessoryContextParams(
                        accessoryContextId,
                        serverContextId,
                        new WebResourceWithTimeout(
                                mDevice.getDeviceAbsoluteURLForPath(OXPD_SERVER__ACCESSORIES_CALLBACK),
                                Binding.Soap12,
                                60,
                                60,
                                null)),
                requestID,
                callback);
    }

    /**
     * Signals the device to stop processing asynchronous reads from the interrupt in pipe of an HID accessory.
     *
     * @param requestID
     *              Request ID associated with this call
     * @param accessoryContextId
     *              Accessory Context ID
     * @param callback
     *              Callback to invoke when operation is finished.
     */
    public void HidStopReading(String accessoryContextId, int requestID, OXPdDevice.RequestCallback callback) {
        //mEventCallbacks.remove();

        mDevice.queueRequest(mHidStopReadingHandler,
                new AccessoryContextParams(accessoryContextId, null, null),
                requestID,
                callback);
    }

    /**
     * Synchronously reads an input or feature report from the control pipe of an HID accessory.
     *
     * @param requestID
     *              Request ID associated with this call
     * @param accessoryContextId
     *              Accessory Context ID
     * @param hidReportType
     *              Type of report to read
     * @param callback
     *              Callback to invoke when operation is finished.
     */
    public void HidReadReport(String accessoryContextId, HidReportType hidReportType, int requestID, OXPdDevice.RequestCallback callback) {
        mDevice.queueRequest(mHidReadReportHandler,
                new AccessoryReportParams(accessoryContextId, hidReportType, null),
                requestID,
                callback);
    }

    /**
     * Synchronously writes a report to the control pipe of an HID accessory.
     *
     * @param requestID
     *              Request ID associated with this call
     * @param accessoryContextId
     *              Accessory Context ID
     * @param hidReport
     *              Report to write
     * @param callback
     *              Callback to invoke when operation is finished.
     */
    public void HidWriteReport(String accessoryContextId, HidReport hidReport, int requestID, OXPdDevice.RequestCallback callback) {
        mDevice.queueRequest(mHidWriteReportHandler,
                new AccessoryReportParams(accessoryContextId, null, hidReport),
                requestID,
                callback);
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
                    return mDevice.createSOAPFaultHandler();
                case HttpURLConnection.HTTP_NOT_FOUND:
                    throw new Error(ErrorName.NotFound, "404 Requested resource not found");
                case OXPdDevice.Constants.HTTP_SERVER_ERROR:
                    throw new Error(ErrorName.ServerError, "500 Internal server error");
                case HttpURLConnection.HTTP_UNAUTHORIZED:
                case HttpURLConnection.HTTP_FORBIDDEN:
                    throw new Error(ErrorName.AuthError, "Authorization error");
                default:
                    throw new Error(ErrorName.Unknown, "Unknown error");
            }
        } else {
            throw new Error(ErrorName.AjaxError, "Connection failed");
        }
    }

    /**
     * Register handlers for job specific functions
     */
    private void registerAccessoryCallbacks() {
        mServerHooks.registerHook(OXPD_SERVER__ACCESSORIES_CALLBACK, mAccessoriesCallbackRequestHandler);
    }

    /**
     * Unregister job specific handler
     */
    private void unregisterAccessoryCallbacks() {
        mServerHooks.unregisterHook(OXPD_SERVER__ACCESSORIES_CALLBACK);
    }

    /**
     * Configure the default handlers when processing a scan SOAP message
     * @return Default handler
     */
    private RestXMLTagHandler setupDefaultHandler() {
        return mDevice.createSOAPFaultHandler();
    }
}
