// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.deviceinfo;

import android.os.Message;
import android.text.TextUtils;

import com.hp.oxpdlib.OXPdDevice;
import com.hp.oxpdlib.SOAPFault;
import com.hp.oxpdlib.discovery.DiscoveredFeature;
import com.hp.oxpdlib.discovery.DiscoveryTree;
import com.hp.sdd.jabberwocky.chat.HttpHeader;
import com.hp.sdd.jabberwocky.chat.HttpRequestResponseContainer;
import com.hp.sdd.jabberwocky.xml.RestXMLNSHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLTagHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLWriter;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * This library provides a CORS binding to the OXPd:DeviceInfo service on HP devices.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class OXPdDeviceInfo {

    // CONSTANTS
    /** OXPd well known resource name */
    private static final String OXPD_RESOURCE_TYPE__DEVICE_INFO = "OXPd:DeviceInfo";
    /** OXPd DeviceInfo supported version */
    private static final String OXPD_REVISION__DEVICE_INFO = "http://www.hp.com/schemas/imaging/OXPd/service/deviceinfo/2013/03/01";
    /** XML prefix to use for tags in DeviceInfo namespace */
    private static final String XML_PREFIX_OXPD_DEVICE_INFO = "devinfo";
    /** Composite XML schema prefix/value/version for DeviceInfo */
    private static final String XML_SCHEMA__OXPD_DEVICE_INFO = XML_PREFIX_OXPD_DEVICE_INFO+RestXMLNSHandler.XML_SEPARATOR+
            "http://www.hp.com/schemas/imaging/OXPd/service/deviceinfo/*"+RestXMLNSHandler.XML_SEPARATOR+"2013/03/01";
    /** SOAP service name */
    private static final String SOAP_SERVICE_DEVICE_INFO = "IDeviceInfoService";
    /** SOAP request to retrieve manufacturer info */
    private static final String SOAP_OP__GET_MANUFACTURER_INFO = "GetManufacturerInfo";
    /** SOAP request to retrieve customer info */
    private static final String SOAP_OP__GET_CUSTOMER_INFO = "GetCustomerInfo";

    // CLASSES
    /**
     * DeviceInfo SOAP request builder
     */
    private abstract class DeviceInfoSOAPRequestBuilder implements OXPdDevice.SOAPRequestBuilder {
        /**
         * Return DeviceInfo URL
         * @return
         *          URL for request
         */
        @Override
        public URL getURL() {
            return mDeviceInfoURL;
        }

        /**
         * Return XML schemas used by device info
         * @return
         *          List of XML schemas
         */
        @Override
        public List<String> getXMLSchemas() {
            return Collections.singletonList(XML_SCHEMA__OXPD_DEVICE_INFO);
        }

        /**
         * Return service revision string
         * @return
         *          Service revision
         */
        @Override
        public String getServiceRevision() {
            return OXPD_REVISION__DEVICE_INFO;
        }

        /**
         * Return service name
         * @return
         *          Service name
         */
        @Override
        public String getServiceName() {
            return SOAP_SERVICE_DEVICE_INFO;
        }

        /**
         * Return list of HTTP headers to include as part of request
         * @return
         *          List of HttpHeaders
         */
        @Override
        public List<HttpHeader> getRequestHeaders() {
            return Collections.emptyList();
        }

        /** Use admin credentials as part of request?
         * @return
         *          false
         */
        @Override
        public boolean useAdminCredentials() {
            return false;
        }
    }

    // VARIABLES
    /** OXPd device instance */
    private final OXPdDevice mDevice;
    /** device info url */
    private final URL mDeviceInfoURL;
    /**
     * Request handler for {@link #GetCustomerInfo(int, OXPdDevice.RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mGetCustomerInfoHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, Object requestParams) {

            // create soap request builder and make request
            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new DeviceInfoSOAPRequestBuilder() {

                @Override
                public String getSoapOperation() {
                    return SOAP_OP__GET_CUSTOMER_INFO;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    xmlWriter.writeTag(XML_SCHEMA__OXPD_DEVICE_INFO, getSoapOperation(), null, null);
                }
            });

            int result;
            Object data;
            try {
                // parse result
                data = CustomerInfo.parseRequestResult(mDevice, requestResponse, checkHTTPResponse(requestResponse));
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch(Error ignored) {
                // handle error
                data = ignored;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;

            }
            // return result
            return Message.obtain(null, requestID, result, 0,  data);
        }
    };

    /**
     * Request handler for {@link #GetManufacturerInfo(int, OXPdDevice.RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mGetManufacturerInfoHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, Object requestParams) {

            // create soap request builder and make request
            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new DeviceInfoSOAPRequestBuilder() {
                @Override
                public String getSoapOperation() {
                    return SOAP_OP__GET_MANUFACTURER_INFO;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    xmlWriter.writeTag(XML_SCHEMA__OXPD_DEVICE_INFO, getSoapOperation(), null, null);
                }
            });
            int result;
            Object data;
            try {
                data = ManufacturerInfo.parseRequestResult(mDevice, requestResponse, checkHTTPResponse(requestResponse));
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch(Error ignored) {
                data = ignored;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            }
            return Message.obtain(null, requestID, result, 0,  data);
        }
    };

    // CONSTRUCTORS
    /**
     * Constructor for Device Info instance
     * @param device
     *              OXPd device instance
     * @param discoveryTree
     *              Discovery tree for current OXPd device
     * @throws Error
     *              When something goes wrong
     * @hide
     */
    public OXPdDeviceInfo(OXPdDevice device, DiscoveryTree discoveryTree) throws Error {
        // save device instance
        mDevice = device;
        // verify we have discovery tree
        if (discoveryTree == null) throw new Error(ErrorName.AjaxError, "Connection failed");

        // lookup feature
        DiscoveredFeature feature =
                discoveryTree.GetDiscoveredFeatureByResourceTypeAndRevision(
                        OXPD_RESOURCE_TYPE__DEVICE_INFO,
                        OXPD_REVISION__DEVICE_INFO);
        // feature found?
        if (feature == null) {
            throw new Error(ErrorName.ServiceNotFound, "OXPd:DeviceInfo is not supported on the target device");
        }

        try {
            // create URL
            mDeviceInfoURL = device.getOXPdUrl(feature.resourceURI);
        } catch (MalformedURLException e) {
            // bad url
            throw new Error(ErrorName.Unknown, "invalid URL");
        }
        // add namespace for requests
        mDevice.getXMLNSHandler().addXMLNS(XML_PREFIX_OXPD_DEVICE_INFO, OXPD_REVISION__DEVICE_INFO);
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
     * Retrieves the target device's customer info
     * @param requestID
     *              Request id associated with this call
     * @param callback
     *              Callback to invoke when operation is finished
     */
    public void GetCustomerInfo(int requestID, OXPdDevice.RequestCallback callback) {
        mDevice.queueRequest(mGetCustomerInfoHandler, null, requestID, callback);
    }

    /**
     * Retrieves the target device's manufacturer info.
     * @param requestID
     *              Request id associated with this call
     * @param callback
     *              Callback to invoke when operation is finished
     */
    public void GetManufacturerInfo(int requestID, OXPdDevice.RequestCallback callback) {
        mDevice.queueRequest(mGetManufacturerInfoHandler, null, requestID, callback);
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
                default:
                    throw new Error(ErrorName.Unknown, "Unknown error");
            }
        } else {
            throw new Error(ErrorName.AjaxError, "Connection failed");
        }
    }
}
