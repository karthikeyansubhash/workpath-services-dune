// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.hp.oxpdlib.accessories.OXPdAccessories;
import com.hp.oxpdlib.copy.OXPdCopy;
import com.hp.oxpdlib.deviceinfo.OXPdDeviceInfo;
import com.hp.oxpdlib.discovery.DiscoveryTree;
//import com.hp.oxpdlib.print.OXPdPrint;
import com.hp.oxpdlib.scan.Destination;
import com.hp.oxpdlib.scan.OXPdScan;
import com.hp.oxpdlib.uiconfiguration.OXPdUIConfiguration;
import com.hp.sdd.jabberwocky.chat.HttpHeader;
import com.hp.sdd.jabberwocky.chat.HttpRequest;
import com.hp.sdd.jabberwocky.chat.HttpRequestResponseContainer;
import com.hp.sdd.jabberwocky.chat.HttpResponse;
import com.hp.sdd.jabberwocky.chat.HttpUtils;
import com.hp.sdd.jabberwocky.chat.PinningTrustManager;
import com.hp.sdd.jabberwocky.utils.Chronicler;
import com.hp.sdd.jabberwocky.utils.DebugInputStream;
import com.hp.sdd.jabberwocky.xml.RestXMLNSHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLParser;
import com.hp.sdd.jabberwocky.xml.RestXMLTagHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLTagStack;
import com.hp.sdd.jabberwocky.xml.RestXMLWriter;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSocketFactory;

import fi.iki.elonen.NanoHTTPD;

/**
 * OXPd device instance that manages interfaces & calls to a device.
 */
@SuppressWarnings({"WeakerAccess", "unused", "FieldCanBeLocal"})
public class OXPdDevice implements OXPdServer.ServerRequestHandler {
    // CONSTANTS
    /** Collection of result codes
     * @see #REQUEST_RETURN_CODE__WTF
     * @see #REQUEST_RETURN_CODE__OK
     * @see #REQUEST_RETURN_CODE__NOT_SUPPORTED
     * @see #REQUEST_RETURN_CODE__NOT_IMPLEMENTED
     * @see #REQUEST_RETURN_CODE__INVALID_PARAMETERS
     * @see #REQUEST_RETURN_CODE__OUT_OF_MEMORY
     * @see #REQUEST_RETURN_CODE__FEATURE_DISABLED
     * @see #REQUEST_RETURN_CODE__PROGRAMMER_FU
     * @see #REQUEST_RETURN_CODE__FEATURE_FAILED
     * @see #REQUEST_RETURN_CODE__MISSING_IMPLEMENTATION
     * @see #REQUEST_RETURN_CODE__TRANSACTION_FAILED
     * @see #REQUEST_RETURN_CODE__DATA_NOT_FOUND
     * @see #REQUEST_RETURN_CODE__QUITTING
     * @see #REQUEST_RETURN_CODE__EXCEPTION
     */
    /* Result codes */
    public @interface RequestResult{}
    /** What a Terrible Failure */
    public static final int REQUEST_RETURN_CODE__WTF = 0xDEAD;
    /** OK */
    public static final int REQUEST_RETURN_CODE__OK = 0;
    /** Not supported */
    public static final int REQUEST_RETURN_CODE__NOT_SUPPORTED = 1;
    /** Not implemented */
    public static final int REQUEST_RETURN_CODE__NOT_IMPLEMENTED = 2;
    /** Invalid parameters */
    public static final int REQUEST_RETURN_CODE__INVALID_PARAMETERS = 3;
    /** Out of memory */
    public static final int REQUEST_RETURN_CODE__OUT_OF_MEMORY = 4;
    /** Feature disabled */
    public static final int REQUEST_RETURN_CODE__FEATURE_DISABLED = 5;
    /** Programmer error */
    public static final int REQUEST_RETURN_CODE__PROGRAMMER_FU = 6;
    /** Feature failed to initialize */
    public static final int REQUEST_RETURN_CODE__FEATURE_FAILED = 7;
    /** Feature implementation missing */
    public static final int REQUEST_RETURN_CODE__MISSING_IMPLEMENTATION = 8;
    /** Transaction failed */
    public static final int REQUEST_RETURN_CODE__TRANSACTION_FAILED = 9;
    /** Nothing found */
    public static final int REQUEST_RETURN_CODE__DATA_NOT_FOUND = 10;
    /** Shutting down */
    public static final int REQUEST_RETURN_CODE__QUITTING = 11;
    /** Exception occurred */
    public static final int REQUEST_RETURN_CODE__EXCEPTION = 12;
    /** Not Authorized */
    public static final int REQUEST_RETURN_CODE__NOT_AUTHORIZED = 13;
    /** Forbidden */
    public static final int REQUEST_RETURN_CODE__FORBIDDEN = 14;

    public static final int BUFFER_SIZE = 65536 << 3; //512KB

    /**
     * Use globally configured debugging options
     * @hide
     */
    public static final int HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL = 0;
    /**
     * Force debug on
     * @hide
     */
    public static final int HTTP_REQUEST_DEBUG_OPTION__ON = 1;
    /**
     * Force debug off
     * @hide
     */
    public static final int HTTP_REQUEST_DEBUG_OPTION__OFF = 2;

    /** Log Tag */
    private static final String TAG = "OXPdDevice";

    /** Emulator checker */
    private static Boolean isEmulator = null;

    /**
     * Collection of handler message values
     * @see #HANDLER_MSG_DEBUG
     * @see #HANDLER_MSG_IS_DEVICE_SUPPORTED
     * @see #HANDLER_MSG_QUIT
     * @see #HANDLER_MSG_PROCESS_REQUEST
     * @see #HANDLER_MSG_PROCESS_ACCEPT_NEW_CERTIFICATE
     */
    /* Message types */
    private @interface HandlerMessage{}
    /** Print out debugging information */
    private static final int HANDLER_MSG_DEBUG = 0xDEAD;
    /** Quit */
    private static final int HANDLER_MSG_QUIT = 101;
    /** Check if device is supported */
    private static final int HANDLER_MSG_IS_DEVICE_SUPPORTED = 102;
    /** Process a request */
    private static final int HANDLER_MSG_PROCESS_REQUEST = 103;
    /** Accept new certificate */
    private static final int HANDLER_MSG_PROCESS_ACCEPT_NEW_CERTIFICATE = 104;

    /**
     * Collection of OXPd feature flags
     * @see #OXPD_FEATURE_ANY
     * @see #OXPD_FEATURE_PRINT
     * @see #OXPD_FEATURE_SCAN
     * @see #OXPD_FEATURE_COPY
     * @see #OXPD_FEATURE_ALL
     */
    public @interface OXPdFeatureSet{}
    /** Any feature flag */
    public static final int OXPD_FEATURE_ANY = 0;
    /** Print feature flag */
    public static final int OXPD_FEATURE_PRINT = 1;
    /** Scan feature flag */
    public static final int OXPD_FEATURE_SCAN = (1 << 1);
    /** Scan feature flag */
    public static final int OXPD_FEATURE_COPY = (1 << 2);
    /** All feature flag */
    public static final int OXPD_FEATURE_ALL = (OXPD_FEATURE_PRINT | OXPD_FEATURE_SCAN | OXPD_FEATURE_COPY);

    /** Dummy security provider */
    private static final ExternalSecurityProvider mDummySigner = new ExternalSecurityProvider() {
        /** {@inheritDoc} */
        @Override
        public byte[] signData(Signature signature, byte[] data) {
            return new byte[0];
        }
    };

    // INTERFACES
    /**
     * Interface definition for a callback that gets invoked after a device
     * command has finished executing
     */
    public interface RequestCallback {
        /**
         * Called when a device command has finished executing
         * @param device
         *            Pointer to device instance that executed the request
         * @param message
         *            Contains the result of the command
         *            {@link Message#what Message.what} contains the requestID provided in the
         *            original call;
         *            {@link Message#arg1 Message.arg1} contains the command result;
         *            {@link Message#arg2 Message.arg2} contains additional result code,
         *            defined by the handler;
         *            {@link Message#obj Message.obj contains data for the command, defined by
         *            the handler
         */
        void requestResult(final OXPdDevice device, final Message message);
    }

    /**
     * Interface definition allowing a client application to provide their own
     * signing mechanism rather than using the built in one.
     */
    public interface ExternalSecurityProvider {
        /**
         * Called by library to request client sign the provided data
         * @param data
         *            Data library needs signed
         * @return
         *            SHA256withRSA Signature of provided data
         */
        byte[] signData(Signature signature, byte[] data);
    }

    /**
     * Callback for posting internal requests
     * @hide
     */
    public interface DeviceProcessRequestCallback {
        /**
         * Process an internal request
         * @param requestID
         *              ID associated with request
         * @param requestParams
         *              Request parameters (if any)
         * @return
         *              Message to return back to caller
         */
        Message processRequest(int requestID, Object requestParams);
    }

    /**
     * Helper interface to build up SOAP requests
     * @hide
     */
    public interface SOAPRequestBuilder {
        /** URL for request */
        URL getURL();
        List<String> getXMLSchemas();
        /** Return the SOAP operation name */
        String getSoapOperation();
        /** Return the SOAP service name */
        String getServiceName();
        /** Return the SOAP service revision */
        String getServiceRevision();
        /** Fill out the SOAP request body */
        void fillSoapOperationBody(RestXMLWriter xmlWriter);
        /** Return any additional request headers */
        List<HttpHeader> getRequestHeaders();
        /** Use admin credentials as part of request */
        boolean useAdminCredentials();
    }

    /**
     * Interface for handling incoming server requests
     */
    public interface HandleIncomingServerRequest {
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
        NanoHTTPD.Response handleRequest(String uri, Map<String, String> incomingData, Map<String, List<String>> incomingDataParameters);
    }

    /**
     * Hooks to interact with the internal HTTP server
     * @hide
     */
    public interface InternalServerHooks {
        /**
         * Register a handler for a specified URL
         * @param uri Relative URI
         * @param uriHandler Handler for specified relative URL
         */
        void registerHook(String uri, HandleIncomingServerRequest uriHandler);

        /**
         * Unregister a handler for the specified URL
         * @param uri Relative URI
         */
        void unregisterHook(String uri);
    }

    /**
     * Interface to get client information
     * @hide
     */
    public interface ClientInfoProvider {
        /**
         * Get the client's package name
         * @return Package name
         */
        String getPackageName();

        /**
         * Get the client's application ID
         * @return Application ID
         */
        String getOXPdID();

    }

    // CLASSES
    /**
     * Collections of constants used by OXPd
     * @hide
     */
    public static final class Constants {

        /** Protocol scheme for HTTP */
        public static final String SCHEME_HTTP = "http";
        /** Protocol scheme for HTTPS */
        public static final String SCHEME_HTTPS = "https";

        /** Authorization header name */
        public static final String HTTP_HEADER__AUTHORIZATION = "Authorization";
        /** Default authorization username */
        public static final String HTTP_HEADER_VALUE__AUTHORIZATION_GUEST = "guest";
        /** Default authorization system username */
        public static final String HTTP_HEADER_VALUE__AUTHORIZATION_SYS = "admin";
        /** Authorization mode value */
        public static final String HTTP_HEADER_VALUE__AUTHORIZATION_MODE = "Basic";

        public static final int HTTP_PORT = 80;

        /** Port for OXPd SOAP commands */
        public static final int OXPD_PORT = 7627;
        /** Internal Port for OXPd SOAP commands */
        public static final int OXPD_PORT_INTERNAL = 57628;

        /** Internal Port for OMNI SOAP commands */
        public static final int OMNI_PORT_INTERNAL = 9110;
        /** Internal Port for OMNI HTTP commands */
        public static final int OMNI_PORT_HTTP_INTERNAL = 9111;

        /** Internal Port for OXPd SOAP commands */
        public static final String HOSTNAME_INTERNAL = isEmulator() ?
                getEmulatorInternalHost(): "fwprinter2";
        // Request by JEDI network (2019.10.28) => rejected
        //The jdiloopback forces the connection to go through REDSOCKS, MSP, and JDI.
        //The fwprinter2 will go directly over the MIPIO LAN to a JediFW CWS WS* port 57628 listener.
        public static final String OXPD_HOST_INTERNAL = isEmulator() ?
                getEmulatorInternalHost() : "jdiloopback";

        /** Default Internal host for emulator environment */
        private static final String INTERNAL_EMULATOR_HOST = "10.0.2.2";

        /** Property name for getprop to modify default emulator internal host */
        private static final String EMULATOR_HOST_PROP = "link.host";

        /** Internal Build Model to determine device panel */
        private static final String PRINTER_PANEL = "printer";
        private static final String GENERIC_SDK = "generic";

        /** Connection timeout */
        public static final int CONNECTION_TIMEOUT = 60000;
        /** Connection timeout */
        public static final int SOCKET_TIMEOUT = 60000;

        /** Connection timeout for ipp */
        public static final int CONNECTION_TIMEOUT_120 = 120000;
        /** Connection timeout for ipp */
        public static final int SOCKET_TIMEOUT_120 = 120000;

        public static final long OXPD_CONNECT_TIMEOUT = 60;
        public static final long OXPD_REQUEST_TIMEOUT = 60;

        // timeout to check whether connected device still accessible or not
        public static final int CHECK_ALIVE_TIMEOUT = 5000;

        /** SOAP content-type */
        public static final String CONTENT_TYPE__SOAP_XML = "application/soap+xml; charset=utf-8";
        /** JSON content-type */
        public static final String CONTENT_TYPE__JSON = "application/json";
        /** OCTET content-type */
        public static final String CONTENT_TYPE__OCTECT_STREAM = "application/octet-stream";
        /** www-form-urlencoded content-type */
        public static final String CONTENT_TYPE__FORM_URLENCODED = "application/x-www-form-urlencoded";

        /** Default character set */
        public static final Charset UTF_8 = Charset.forName("UTF-8");

        // XML prefix values
        /** SOAP envelope xml prefix */
        private static final String XML_SCHEMA_PREFIX__SOAP_ENVELOPE = "s";
        /** SOAP address xml prefix */
        private static final String XML_SCHEMA_PREFIX__SOAP_ADDRESSING = "a";
        /** XML Schema instance prefix */
        static final String XML_SCHEMA_PREFIX__XSI = "xsi";
        /** XML Schema definition prefix */
        static final String XML_SCHEMA_PREFIX__XSD = "xsd";
        /** OXPd common prefix */
        public static final String XML_PREFIX_OXPD_COMMON = "common";

        // XML revision values
        /** SOAP envelope revision */
        private static final String XML_SCHEMA_REVISION__SOAP_ENVELOPE = "http://www.w3.org/2003/05/soap-envelope";
        /** SOAP address revision */
        private static final String XML_SCHEMA_REVISION__SOAP_ADDRESSING = "http://schemas.xmlsoap.org/ws/2004/08/addressing";
        /** XML schema instance revision */
        static final String XML_SCHEMA_REVISION__XSI = "http://www.w3.org/2001/XMLSchema-instance";
        /** XML schema definition revision */
        static final String XML_SCHEMA_REVISION__XSD = "http://www.w3.org/2001/XMLSchema";

        // Composite XML schema prefix/revision/version values
        /** Composite XML schema prefix/revision/version for SOAP envelope */
        public static final String XML_SCHEMA__SOAP_ENVELOPE  =  XML_SCHEMA_PREFIX__SOAP_ENVELOPE+RestXMLNSHandler.XML_SEPARATOR+"http://www.w3.org/*/soap-envelope"+RestXMLNSHandler.XML_SEPARATOR+"2003/05";
        /** Composite XML schema prefix/revision/version values for SOAP address */
        public static final String XML_SCHEMA__SOAP_ADDRESSING = XML_SCHEMA_PREFIX__SOAP_ADDRESSING+RestXMLNSHandler.XML_SEPARATOR+"http://schemas.xmlsoap.org/ws/*/addressing"+RestXMLNSHandler.XML_SEPARATOR+"2004/08";
        /** Composite XML schema prefix/revision/version values for XML schema instance */
        public static final String XML_SCHEMA__XSI = XML_SCHEMA_PREFIX__XSI+RestXMLNSHandler.XML_SEPARATOR+"http://www.w3.org/*/XMLSchema-instance"+RestXMLNSHandler.XML_SEPARATOR+"2001";
        /** Composite XML schema prefix/revision/version values XML schema definition */
        public static final String XML_SCHEMA__XSD = XML_SCHEMA_PREFIX__XSD+RestXMLNSHandler.XML_SEPARATOR+"http://www.w3.org/*/XMLSchema"+RestXMLNSHandler.XML_SEPARATOR+"2001";
        /** Composite XML schema prefix/revision/version values for OXPd:Common */
        public static final String XML_SCHEMA__OXPD_COMMON = XML_PREFIX_OXPD_COMMON + RestXMLNSHandler.XML_SEPARATOR+"http://www.hp.com/schemas/imaging/OXPd/common/*"+RestXMLNSHandler.XML_SEPARATOR+"2010/04/14";

        //XML tags
        /** SOAP envelope tag */
        public static final String XML_TAG__SOAP_ENVELOPE__ENVELOPE = "Envelope";
        /** SOAP header tag */
        public static final String XML_TAG__SOAP_ENVELOPE__HEADER = "Header";
        /** SOAP body tag */
        public static final String XML_TAG__SOAP_ENVELOPE__BODY = "Body";
        /** SOAP address action */
        public static final String XML_TAG__ADDRESSING__ACTION = "Action";
        /** SOAP address message ID */
        public static final String XML_TAG__ADDRESSING__MESSAGE_ID = "MessageID";
        /** SOAP address to field */
        public static final String XML_TAG__ADDRESSING__TO = "To";
        /** SOAP address reply-to field */
        public static final String XML_TAG__ADDRESSING__REPLY_TO = "ReplyTo";
        /** SOAP address address field */
        public static final String XML_TAG__ADDRESSING__ADDRESS = "Address";
        /** Binding type */
        public static final String XML_TAG__COMMON__BINDING = "binding";
        /** Binding uri value */
        public static final String XML_TAG__COMMON__URI = "uri";
        /** XML tag containing connection timeout value */
        public static final String XML_TAG__COMMON__CONNECTION_TIMEOUT = "connectionTimeout";
        /** XML tag containing response timeout value */
        public static final String XML_TAG__COMMON__RESPONSE_TIMEOUT = "responseTimeout";
        /** XML tag containing scan callback */
        public static final String XML_TAG__COMMON__CALLBACK = "callback";
        /** Localized string element */
        public static final String XML_TAG__COMMON__LOCALIZED_STRING = "LocalizedString";
        /** Localized string locale */
        public static final String XML_TAG__COMMON__CODE = "code";
        /** Localizes string value */
        public static final String XML_TAG__COMMON__VALUE = "value";
        /** Network credentials element */
        public static final String XML_TAG__COMMON__NETWORK_CREDENTIALS = "networkCredentials";
        /** Network credentials user name */
        public static final String XML_TAG__COMMON__USERNAME = "userName";
        /** Network credentials password */
        public static final String XML_TAG__COMMON__PASSWORD = "password";
        /** Network credentials user name domain */
        public static final String XML_TAG__COMMON__DOMAIN = "domain";
        /** Key-value pair container */
        public static final String XML_TAG__COMMON__KEY_VALUE_PAIR = "KeyValuePair";
        /** Key-value pair key field */
        public static final String XML_TAG__COMMON__KEY = "key";
        /** Key-value pair value field */
        public static final String XML_TAG__COMMON__VALUE_STRING = "valueString";
        /** SOAP fault */
        public static final String XML_TAG__SOAP_FAULT = "Fault";
        /** SOAP fault code */
        public static final String XML_TAG__SOAP_CODE = "Code";
        /** SOAP fault value */
        public static final String XML_TAG__SOAP_VALUE = "Value";
        /** SOAP fault subcode value */
        public static final String XML_TAG__SOAP_SUBCODE = "Subcode";
        /** SOAP fault text */
        public static final String XML_TAG__SOAP_TEXT = "Text";
        /** SOAP fault reason */
        public static final String XML_TAG__SOAP_REASON = "Reason";
        /** Dummy tag to store SOAP parser faults */
        public static final String XML_TAG__XML_PARSE_EXCEPTION = "#PARSER_EXCEPTION#";

        // XML attributes
        /** SOAP envelope attribute */
        public static final String XML_ATTRIBUTE_SOAP_ENVELOPE_MUST_UNDERSTAND = "mustUnderstand";

        // XML values
        /** SOAP address anonymous */
        public static final String XML_VALUE__SOAP_ADDRESSING__ANONYMOUS = "http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous";
        /** Common plain binding */
        public static final String XML_VALUE__COMMON_BINDING__PLAIN = "Plain";
        /**
         * Common SOAP 1.2 binding
         * @hide
         */
        public static final String XML_VALUE__COMMON_BINDING__SOAP = "Soap12";

        /** HTTP server error, defined here since it's deprecated in HttpUrlConnection */
        public static final int HTTP_SERVER_ERROR = 500;

        /**
         * incoming SOAP request body
         * @hide
         */
        public static final String SOAP_MESSAGE_KEY = "postData";
    }

    /**
     * Device credentials holder
     * @hide
     */
    public static final class DeviceCredentials {

        /** Credentials Username */
        private String mUsername;
        /** Credentials password */
        private String mPassword;

        /**
         * Constructor
         * @param username Credentials username
         * @param password Credentials password
         */
        private DeviceCredentials(String username, String password) {
            updateCredentials(username, password);
        }

        /**
         * Update credentials
         * @param username Credentials username
         * @param password Credentials password
         */
        private void updateCredentials(String username, String password) {
            mUsername = username;
            mPassword = ((password == null) ? "" : password);
        }

        /**
         * Get credentials username
         * @return Credentials username
         */
        public String getUsername() {
            return mUsername;
        }

        /**
         * Get credentials password
         * @return Credentials password
         */
        public String getPassword() {
            return mPassword;
        }
    }

    /**
     * Container class for encapsulating a device request
     */
    private static final class DeviceProcessRequestParams {
        /** Request callback */
        private final DeviceProcessRequestCallback requestCallback;
        /** Request ID */
        private final int requestID;
        /** Request parameters */
        private final Object requestParams;
        /** Caller-provided callback */
        private final RequestCallback callback;

        /**
         * Constructor for encapsulating a device request
         * @param requestCallback
         * 				Request callback
         * @param params
         * 				Parameters needed by the handler to run the command
         * @param requestID
         * 				ID provided by the caller associated with this request
         * @param callback
         * 				Callback provided by the caller to use to report the request results
         */
        private DeviceProcessRequestParams(DeviceProcessRequestCallback requestCallback,
                                           Object params, int requestID, RequestCallback callback) {
            this.requestCallback = requestCallback;
            this.requestID = requestID;
            this.requestParams = params;
            this.callback = callback;
        }
    }

    /**
     * Handler to serialize all OXPd device requests
     */
    private static class DeviceHandler extends Handler {

        /** Reference to device */
        private final WeakReference<OXPdDevice> mDevice;
        /** Flag to use for checking if discovery has been run */
        private boolean mDiscoveryStarted = false;
        /** Certificate exception */
        private SSLHandshakeException mCertificateException = null;

        /**
         * Constructor for device request serializer
         * @param looper
         *              Looper to associate with handler
         * @param device
         *              OXPd device owning this serializer
         */
        DeviceHandler(Looper looper, OXPdDevice device) {
            super(looper);
            mDevice = new WeakReference<OXPdDevice>(device);
        }

        /**
         * Process a handler message
         * @param msg
         *              Message to process
         */
        @Override
        public void handleMessage(Message msg) {
            OXPdDevice device = mDevice.get();
            if (device == null) {
                getLooper().quit();
                return;
            }
/*
            // start discovery if necessary
            if (!mDiscoveryStarted && (mCertificateException == null)) {

                // create the XML parser instance
                SAXParserFactory saxFactory = SAXParserFactory
                        .newInstance();
                saxFactory.setNamespaceAware(true);
                try {
                    SAXParser saxParser = saxFactory.newSAXParser();
                    // get the xml reader
                    XMLReader xmlParser = saxParser.getXMLReader();
                    // create the LEDM rest parser
                    RestXMLParser parser = new RestXMLParser();
                    // set the handler
                    xmlParser.setContentHandler(parser);
                    // add the parser into the known threads/parser hash table
                    device.deviceXMLParsers.set(xmlParser);
                } catch (Exception e) {
                    Log.wtf(TAG, "Could not create main parser: " + e);
                    e.printStackTrace();
                }

                mDiscoveryStarted = true;

                if (mDevice != null) {
                    OXPdDiscovery discovery = new OXPdDiscovery(mDevice.get());
                    try {
                        device.mDiscoveryTree = discovery.GetOXPdDiscoveryTree();
                    } catch (Exception discoveryException) {
                        if (discoveryException instanceof SSLHandshakeException) {
                            mCertificateException = (SSLHandshakeException) discoveryException;
                            mDiscoveryStarted = false;
                        }
                    }
                } else {
                    Log.wtf(TAG, "Connected device is empty.");
                }
            }
*/
            // wtf?
            if (msg == null) return;

            // process the request
            @HandlerMessage int msgRequest = msg.what;
            switch (msgRequest) {
                case OXPdDevice.HANDLER_MSG_DEBUG:
                    // intentional fallthrough
                case HANDLER_MSG_IS_DEVICE_SUPPORTED:
                    // intentional fallthrough
                default:
                    break;
                // process a request
                case OXPdDevice.HANDLER_MSG_PROCESS_REQUEST: {
                    DeviceProcessRequestParams params = (DeviceProcessRequestParams) msg.obj;
                    if (!hasMessages(HANDLER_MSG_QUIT)) {
                        if (mCertificateException != null) {
                            if (params.callback != null)  {
                                params.callback.requestResult(device, Message.obtain(null, params.requestID, REQUEST_RETURN_CODE__EXCEPTION, 0,  mCertificateException));
                            }
                            break;
                        }
                        Message message = params.requestCallback.processRequest(params.requestID, params.requestParams);
                        // do we have a message
                        if (message != null) {
                            if (message.obj instanceof SSLHandshakeException) {
                                mCertificateException = (SSLHandshakeException)msg.obj;
                            }
                            // log the result
//                            if (device.mChronicler.isLoggable(Log.VERBOSE))
//                                device.mChronicler
//                                        .log(Log.VERBOSE,
//                                                TAG,
//                                                String.format(Locale.US,
//                                                        "Request with ID %d returned %d (%s)",
//                                                        params.requestID,
//                                                        message.arg1,
//                                                        OXPdDevice.errorCodeToString(message.arg1)));
                            // invoke the callback with the message or recycle it
                            if (params.callback != null)
                                params.callback.requestResult(device, message);
                            else
                                message.recycle();
                        }
                    } else if (params.callback != null) {
                        params.callback.requestResult(device, Message.obtain(null, params.requestID, REQUEST_RETURN_CODE__QUITTING, 0,  null));
                    }
                    break;
                }

                // accept new certificate
                case OXPdDevice.HANDLER_MSG_PROCESS_ACCEPT_NEW_CERTIFICATE:
                    if (mCertificateException != null) {
                        device.mPinningTrustManager.acceptNewCertificate();
                        mCertificateException = null;
                        if (!hasMessages(HANDLER_MSG_QUIT)) {
                            sendEmptyMessage(HANDLER_MSG_IS_DEVICE_SUPPORTED);
                        }
                    }
                    break;

                // quit
                case OXPdDevice.HANDLER_MSG_QUIT:
                    OXPdServer.removeServerClient(device);
                    getLooper().quit();
                    break;
            }
        }
    }

    /** Default client information provider */
    private static class DefaultClientInfoProvider implements ClientInfoProvider {

        /** Client package name */
        private final String mPackageName;
        /** Client OXPd application ID */
        private final String mUUID;

        /**
         * Constructor
         * @param context Client context
         */
        DefaultClientInfoProvider(Context context) {
            mPackageName = context.getPackageName();
            mUUID = context.getString(R.string.oxpd_application_id);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getPackageName() {
            return mPackageName;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getOXPdID() {
            return mUUID;
        }
    }

    // VARIABLES
    /** Admin Authorization Header lock */
    private final Object mAdminAuthHeaderLock = new Object();
    /** Device context */
    private final Context mContext;
    /** Device hostname */
    private final String mHostName;
    /** Device request serializer */
    private DeviceHandler mHandler;
    /** XML parser (thread specific) */
    private final ThreadLocal<XMLReader> deviceXMLParsers = new ThreadLocal<XMLReader>();
    /** Last HTTP response (thread specific)  */
    private final ThreadLocal<HttpResponse> deviceLastHttpResponse = new ThreadLocal<HttpResponse>();
    /** Logger */
    private Chronicler mChronicler;

    /** Flag to debug XML */
    private boolean debugXML = false;
    /** Flat to debug requests/responses */
    private boolean mIsDebuggable = true;
    /** XML namespace handler */
    private RestXMLNSHandler deviceXMLNSHandler;
    /** Discovery Tree */
    private DiscoveryTree mDiscoveryTree = null;
    /** Non-admin authorization header */
    private HttpHeader mGuestAuthHeader;
    /** Admin authorization header */
    private HttpHeader mAdminAuthHeader = null;
    /** Client information */
    private ClientInfoProvider mClientInfoProvider;
    /** Holder for device credentials */
    private DeviceCredentials mDeviceCredentials;
    /** Temporary folder for scanned files */
    private File mScannedFilesTemporaryFolder = new File(System.getProperty("java.io.tmpdir"));
    /** Interface name where we connected to network */
    private String mNetworkInterface;
    /** Local IP for server callback*/
    private String mLocalIpAddress;
    /** Request socket connect timeout */
    private int mConnectTimeout = Constants.CONNECTION_TIMEOUT;
    /** Request socket read timeout */
    private int mReadTimeout = Constants.SOCKET_TIMEOUT;

    /** Feature instance handlers
     * @see OXPdScan
     * @see OXPdDeviceInfo
     * @see OXPdUIConfiguration
     * @see OXPdAccessories
     */
    private final HashMap<Class, Object> mFeatureHandlers = new HashMap<Class,Object>();
    /** Data Signer */
    private ExternalSecurityProvider mExternalSigner;
    /** OXPd Server */
    private OXPdServer mOXPdServer;
    /** Pinning Trust Manager */
    private PinningTrustManager mPinningTrustManager;
    /** Socket factory with SSL pinning */
    private SSLSocketFactory mSSLSocketFactory;
    /** Hostname verifier */
    private HostnameVerifier mHostnameVerifier;
    /** Default Keystore */
    private static KeyStore sDefaultKeyStore = null;

    /** Map of registered server hooks */
    private final HashMap<String,HandleIncomingServerRequest> mServerHooks = new HashMap<String,HandleIncomingServerRequest>();

    /** Server hooks interface */
    private final InternalServerHooks mServerHooksIFC = new InternalServerHooks() {
        @Override
        public void registerHook(String uri, HandleIncomingServerRequest uriHandler) {
            if (TextUtils.isEmpty(uri)) return;
            synchronized (mServerHooks) {
                String hookUri = uri.startsWith("/") ? uri : getDeviceRelativeURLForPath(uri);
                mServerHooks.put(hookUri, uriHandler);
            }
        }

        @Override
        public void unregisterHook(String uri) {
            if (TextUtils.isEmpty(uri)) return;
            synchronized (mServerHooks) {
                mServerHooks.remove(getDeviceRelativeURLForPath(uri));
            }
        }
    };

    /**
     * Request handler for {@link #getOXPdDeviceInfoInstance(int, RequestCallback)}
     */
    private final DeviceProcessRequestCallback mGetDeviceInfoInstanceHandler = new DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, Object requestParams) {
            Object data;
            int result;

            try {
                data = mFeatureHandlers.get(OXPdDeviceInfo.class);
                if (data == null) {
                    data = new OXPdDeviceInfo(OXPdDevice.this, mDiscoveryTree);
                    mFeatureHandlers.put(OXPdDeviceInfo.class, data);
                }
                result = REQUEST_RETURN_CODE__OK;
            } catch (com.hp.oxpdlib.deviceinfo.Error error) {
                data = error;
                result = REQUEST_RETURN_CODE__EXCEPTION;
            }

            return Message.obtain(null, requestID, result, 0, data);
        }
    };

    /**
     * Request handler for {@link #getOXPdScanInstance(int, RequestCallback)}
     */
    private final DeviceProcessRequestCallback mGetScanInstanceHandler = new DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, Object requestParams) {
            Object data;
            int result;
            try {
                data = mFeatureHandlers.get(OXPdScan.class);
                if (data == null) {
                    data = new OXPdScan(OXPdDevice.this, mServerHooksIFC, mDiscoveryTree);
                    mFeatureHandlers.put(OXPdScan.class, data);
                }
                result = REQUEST_RETURN_CODE__OK;
            } catch (com.hp.oxpdlib.scan.Error error) {
                data = error;
                result = REQUEST_RETURN_CODE__EXCEPTION;
            }

            return Message.obtain(null, requestID, result, 0, data);
        }
    };

    /**
     * Request handler for {@link #getOXPdUIConfigurationInstance(int, RequestCallback)}
     */
    private final DeviceProcessRequestCallback mGetUIConfigurationInstanceHandler = new DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, Object requestParams) {
            Object data;
            int result;

            try {
                data = mFeatureHandlers.get(OXPdUIConfiguration.class);
                if (data == null) {
                    data = new OXPdUIConfiguration(OXPdDevice.this, mClientInfoProvider, mDeviceCredentials, mDiscoveryTree);
                    mFeatureHandlers.put(OXPdUIConfiguration.class, data);
                }
                result = REQUEST_RETURN_CODE__OK;
            } catch (com.hp.oxpdlib.uiconfiguration.Error error) {
                data = error;
                result = REQUEST_RETURN_CODE__EXCEPTION;
            }

            return Message.obtain(null, requestID, result, 0, data);
        }
    };

    /**
     * Request handler for {@link #getOXPdPrintInstance(int, RequestCallback)}
     */
    private final DeviceProcessRequestCallback mGetPrintInstanceHandler = new DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, Object requestParams) {
            return new Message();
//            Object data;
//            int result;
//
//            try {
//                data = mFeatureHandlers.get(OXPdPrint.class);
//                if (data == null) {
//                    data = new OXPdPrint(OXPdDevice.this, mDiscoveryTree);
//                    mFeatureHandlers.put(OXPdPrint.class, data);
//                }
//                result = REQUEST_RETURN_CODE__OK;
//            } catch (com.hp.oxpdlib.print.Error error) {
//                data = error;
//                result = REQUEST_RETURN_CODE__EXCEPTION;
//            }
//
//            return Message.obtain(null, requestID, result, 0, data);
        }
    };

    /**
     * Request handler for {@link #getOXPdCopyInstance(int, RequestCallback)}
     */
    private final DeviceProcessRequestCallback mGetCopyInstanceHandler = new DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, Object requestParams) {
            return new Message();
//            Object data;
//            int result;
//            try {
//                data = mFeatureHandlers.get(OXPdCopy.class);
//                if (data == null) {
//                    data = new OXPdCopy(OXPdDevice.this, mServerHooksIFC, mDiscoveryTree);
//                    mFeatureHandlers.put(OXPdCopy.class, data);
//                }
//                result = REQUEST_RETURN_CODE__OK;
//            } catch (com.hp.oxpdlib.copy.Error error) {
//                data = error;
//                result = REQUEST_RETURN_CODE__EXCEPTION;
//            }
//
//            return Message.obtain(null, requestID, result, 0, data);
        }
    };

    /**
     * Request handler for {@link #getOXPdAccessoriesInstance(int, RequestCallback)}
     */
    private final DeviceProcessRequestCallback mGetAccessoriesInstanceHandler = new DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, Object requestParams) {
            Object data;
            int result;
            try {
                data = mFeatureHandlers.get(OXPdAccessories.class);
                if (data == null) {
                    data = new OXPdAccessories(OXPdDevice.this, mServerHooksIFC, mDiscoveryTree);
                    mFeatureHandlers.put(OXPdAccessories.class, data);
                }
                result = REQUEST_RETURN_CODE__OK;
            } catch (com.hp.oxpdlib.accessories.Error error) {
                data = error;
                result = REQUEST_RETURN_CODE__EXCEPTION;
            }

            return Message.obtain(null, requestID, result, 0, data);
        }
    };

    /**
     * Request handler for {@link #getOXPdPrintInstance(int, RequestCallback)}
     */
    private final DeviceProcessRequestCallback mIsOXPdSupportedHandler = new DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, Object requestParams) {
            int requiredFeatures = ((requestParams instanceof Integer) ? (Integer)requestParams : 0);
            boolean hasAllFeatures = (mFeatureHandlers.get(OXPdUIConfiguration.class) != null);
            for(int flag = 1; (hasAllFeatures && (flag < OXPD_FEATURE_ALL)); flag <<= 1) {
                if ((flag & requiredFeatures) != 0) {
                    switch (flag) {
//                        case OXPD_FEATURE_PRINT:
//                            hasAllFeatures = (mFeatureHandlers.get(OXPdPrint.class) != null);
//                            break;
                        case OXPD_FEATURE_SCAN:
                            hasAllFeatures = (mFeatureHandlers.get(OXPdScan.class) != null);
                            break;
                        case OXPD_FEATURE_COPY:
                            hasAllFeatures = (mFeatureHandlers.get(OXPdCopy.class) != null);
                            break;
                        default:
                            break;
                    }
                }
            }

            return Message.obtain(null, requestID, REQUEST_RETURN_CODE__OK, 0, hasAllFeatures);
        }
    };

    // CONSTRUCTORS
    /**
     * Create on OXPd device instance
     * @param context
     *              Context using OXPdDevice
     * @param hostName
     *              Network address of device
     * @param externalSigner
     *              External data singer
     * @throws IllegalArgumentException If hostname or username are empty
     */
    public OXPdDevice(Context context, String hostName, ExternalSecurityProvider externalSigner) throws IllegalArgumentException {
        this(context, hostName, externalSigner, new DefaultClientInfoProvider(context), true);
    }

    /**
     * Create on OXPd device instance
     * @param context
     *              Context using OXPdDevice
     * @param hostName
     *              Network address of device
     * @throws IllegalArgumentException If hostname or username are empty
     */
    OXPdDevice(Context context, String hostName) throws IllegalArgumentException {
        this(context, hostName, mDummySigner, new DefaultClientInfoProvider(context), false);
    }

    /**
     * Create on OXPd device instance
     * @param context
     *              Context using OXPdDevice
     * @param hostName
     *              Network address of device
     * @param externalSigner
     *              External data singer
     * @param clientInfoProvider
     *              Client information provider
     * @hide
     * @throws IllegalArgumentException If hostname or username are empty
     */
    public OXPdDevice(Context context, String hostName, ExternalSecurityProvider externalSigner, ClientInfoProvider clientInfoProvider) throws IllegalArgumentException {
        this(context, hostName, externalSigner, clientInfoProvider, true);
    }

    /**
     * Create on OXPd device instance
     * @param context
     *              Context using OXPdDevice
     * @param hostName
     *              Network address of device
     * @param externalSigner
     *              External data singer
     * @param clientInfoProvider
     *              Client information provider
     * @param secure
     *              Use additional security measures
     * @throws IllegalArgumentException If hostname or username are empty
     */
    @SuppressLint("SSLCertificateSocketFactoryGetInsecure")
    private OXPdDevice(Context context, String hostName, ExternalSecurityProvider externalSigner, ClientInfoProvider clientInfoProvider, boolean secure) throws IllegalArgumentException {

//        if (TextUtils.isEmpty(hostName)) throw new IllegalArgumentException("hostname cannot be empty");
//        if (clientInfoProvider == null) throw new IllegalArgumentException("clientInfoProvider cannot be null");
//        if (externalSigner == null) throw new IllegalArgumentException("externalSigner cannot be null");

        this.mContext = context;
        this.mHostName = hostName;
//        this.mClientInfoProvider = clientInfoProvider;
//        this.mExternalSigner = externalSigner;
//        this.mOXPdServer = OXPdServer.addServerClient(this);
//        //this.mPinningTrustManager = new PinningTrustManager(sDefaultKeyStore, context.getResources().getBoolean(R.bool.auto_trust_unknown_certificate));
//        this.mPinningTrustManager = new PinningTrustManager(sDefaultKeyStore, true);
//        this.mSSLSocketFactory = secure ? HttpUtils.getSSLSocketFactory(this.mPinningTrustManager) : null;
//        this.mHostnameVerifier = secure ? new HttpHostnameVerifier(this.mHostName) : null;
//
//        mDeviceCredentials = new DeviceCredentials(Constants.HTTP_HEADER_VALUE__AUTHORIZATION_GUEST, null);
//        mGuestAuthHeader = buildAuthorizationHeader(Constants.HTTP_HEADER_VALUE__AUTHORIZATION_GUEST, null);
//        mAdminAuthHeader = buildAuthorizationHeader(mDeviceCredentials.getUsername(), mDeviceCredentials.getPassword());
//        mNetworkInterface = null;

        HandlerThread thread = new HandlerThread(DeviceHandler.class.getSimpleName());
        thread.start();
        mHandler = new DeviceHandler(thread.getLooper(), this);
//        mChronicler = new Chronicler();
//        mHandler.sendMessage(mHandler.obtainMessage(HANDLER_MSG_IS_DEVICE_SUPPORTED));
//        deviceXMLNSHandler = new RestXMLNSHandler(mChronicler, mIsDebuggable);
//        deviceXMLNSHandler.addXMLNS(Constants.XML_SCHEMA_PREFIX__XSI, Constants.XML_SCHEMA_REVISION__XSI);
//        deviceXMLNSHandler.addXMLNS(Constants.XML_SCHEMA_PREFIX__XSD, Constants.XML_SCHEMA_REVISION__XSD);
//        deviceXMLNSHandler.addXMLNS(Constants.XML_SCHEMA_PREFIX__SOAP_ENVELOPE, Constants.XML_SCHEMA_REVISION__SOAP_ENVELOPE);
//        deviceXMLNSHandler.addXMLNS(Constants.XML_SCHEMA_PREFIX__SOAP_ADDRESSING, Constants.XML_SCHEMA_REVISION__SOAP_ADDRESSING);
//        getOXPdDeviceInfoInstance(0, null);
//        getOXPdPrintInstance(0, null);
//        getOXPdScanInstance(0, null);
//        getOXPdUIConfigurationInstance(0, null);
//        getOXPdCopyInstance(0, null);
        // For SDK Simulator
        //getOXPdAccessoriesInstance(0, null);
    }

    // METHODS
    /**
     * Close this device instance.
     * Any requests that haven't been processed yet will return {@link #REQUEST_RETURN_CODE__QUITTING}.
     */
    public void closeDevice() {
        mHandler.sendMessage(mHandler.obtainMessage(HANDLER_MSG_QUIT));
    }

    /**
     * Method that executes an HTTP GET
     * @param requestURL
     *              URI the request needs to be sent to
     * @param connectionTimeout
     *              HTTP Connection timeout
     * @param socketTimeout
     *              Http Socket timeout
     * @param debugSettings
     *              Debug settings to use for this request
     * @param headers
     *              Headers that need to be added to the request
     * @return
     *              HTTP request/response pair
     * @hide
     */

    public HttpRequestResponseContainer doHttpGet(URL requestURL, int connectionTimeout, int socketTimeout, int debugSettings, HttpHeader... headers) {
        // create a HTTP request/response pair & fill out the HTTP request
        HttpRequest httpRequest = fillOutHttpRequest(HttpRequest.HTTPRequestType.GET,
                requestURL, null, null, true, connectionTimeout, socketTimeout, debugSettings, headers);
        // execute the HTTP request and get the response
        // return the result
        return getHttpResponse(httpRequest, debugSettings);
    }

    /**
     * Method that executes an HTTP GET
     * @param requestURL
     * 				URI the request needs to be sent to
     * @param debugSettings
     * 				Debug settings to use for this request
     * @param headers
     * 				Headers that need to be added to the request
     * @return
     *              HTTP request/response pair
     * @hide
     */
    public HttpRequestResponseContainer doHttpGet(URL requestURL, int debugSettings, HttpHeader... headers) {
        // call the doHttpGet with default timeouts.
        return doHttpGet(requestURL, mConnectTimeout, mReadTimeout, debugSettings, headers);
    }

    /**
     * 	Method that executes an HTTP POST
     * @param requestURL
     *              URI the request needs to be sent to
     * @param contentType
     *              Description of the request content type
     * @param requestBody
     *              Data to send as part of the request, assumed to be UTF-8 encoded
     * @param expectInput
     *              Expect data as part of this request
     * @param connectionTimeout
     *              Timeout for connection
     * @param socketTimeout
     *              Socket timeout
     * @param debugSettings
     *              Debug settings to use for the request
     * @param headers
     *              Headers that need to be added to the request
     * @return
     *              HTTP request/response pair
     * @hide
     */
    @SuppressWarnings("SameParameterValue")
    public HttpRequestResponseContainer doHttpPost(URL requestURL,
                                                   String contentType, String requestBody, boolean expectInput, int connectionTimeout, int socketTimeout, int debugSettings, HttpHeader... headers) {
        // create a HTTP request/response pair & fill out the HTTP request
        HttpRequest httpRequest = fillOutHttpRequest(HttpRequest.HTTPRequestType.POST,
                requestURL, contentType, requestBody, true, connectionTimeout, socketTimeout, debugSettings, headers);
        // execute the HTTP request and get the response
        // return the result
        return getHttpResponse(httpRequest, debugSettings);
    }

    /**
     * Method that executes an HTTP POST
     * @param requestURL
     * 				URI the request needs to be sent to
     * @param contentType
     * 				Description of the request content type
     * @param requestBody
     * 				Data to send as part of the request, assumed to be UTF-8 encoded
     * @param expectInput
     *              Expect data as part of this request
     * @param debugSettings
     * 				Debug settings to use for the request
     * @param headers
     *  			Headers that need to be added to the request   @return HTTP request/response pair
     * @return
     *              HTTP request/response pair
     * @hide
     */
    @SuppressWarnings("SameParameterValue")
    public HttpRequestResponseContainer doHttpPost(URL requestURL, String contentType, String requestBody,
                                                   boolean expectInput, int debugSettings, HttpHeader... headers) {
        // call the doHttpPut with default timeouts.
        return doHttpPost(requestURL, contentType, requestBody, true, mConnectTimeout, mReadTimeout, debugSettings, headers);
    }


    /**
     * Submits an HTTP request to the device
     * @param request
     * 				HTTP request to execute
     * @param debugSettings
     * 				Debug settings to use when getting the response
     * @return
     *              HTTP response for the request
     * @hide
     */
    public HttpRequestResponseContainer getHttpResponse(HttpRequest request,
                                                         int debugSettings) {

        // make sure content of a previous request is consumed
        httpConsumeContent();

        int logLevel = Chronicler.DEBUG_OFF;
        if ((debugSettings == HTTP_REQUEST_DEBUG_OPTION__ON) ||
                ((debugSettings == HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL) && mChronicler.isLoggable(Log.DEBUG))) {
            logLevel = ((debugSettings == HTTP_REQUEST_DEBUG_OPTION__ON) ? Chronicler.DEBUG_FORCE : Log.DEBUG);
        }

        HttpRequestResponseContainer requestPair = HttpUtils.getHttpResponse(request, TAG, logLevel, mChronicler);
        deviceLastHttpResponse.set(requestPair.response);
        if (mIsDebuggable) mChronicler.log(Log.DEBUG, TAG,"getHttpResponse exit");
        return requestPair;
    }

    /**
     * Parses an HTTP Rest XML response with the provided XML tag handlers
     * @param requestResponsePair
     *              Request/Response that requires parsing
     * @param handlers
     *              XML handlers that should be invoked during parsing of the XML
     * @param debugSetting
     * 			    Debug option override
     * @hide
     */
    public void parseXMLResponse(HttpRequestResponseContainer requestResponsePair,
                          RestXMLTagHandler handlers, int debugSetting) {
        InputStream is = null;
        boolean debug;

        switch(debugSetting)
        {
            case HTTP_REQUEST_DEBUG_OPTION__ON:
            case HTTP_REQUEST_DEBUG_OPTION__OFF:
                debug = (debugSetting == HTTP_REQUEST_DEBUG_OPTION__ON);
                break;
            default:
                debug = debugXML;
        }

        // check we are running on a known thread
        XMLReader xmlReader = deviceXMLParsers.get();
        if (xmlReader == null) {
            mChronicler.log(Log.ERROR, TAG, "Trying to parser xml from unknown thread");
            return;
        }
        // check if there's something to do
        if ((requestResponsePair == null)
                || (requestResponsePair.response == null)) {
            // nothing to do
            return;
        }

        HttpResponse response = requestResponsePair.response;

        // get the http entity
        // check to see if we even have something to parse
        if (response.hasInput()) {

            boolean isXML;

            // get the content type
            String contentTypeStr = response.getContentType();

//            Log.i("[SIM]","response.getContentType() : "+response.getContentType());
//            Log.i("[SIM]","response.getContentLength() : "+response.getContentLength());

            // convert to lowercase
            if (contentTypeStr != null)
                contentTypeStr = contentTypeStr.toLowerCase(Locale.US);

            // determine if the response is XML
            // if the content type contains xml or is missing we assume it's XML
            isXML = (TextUtils.isEmpty(contentTypeStr) || contentTypeStr.contains("xml"));

            // setup the handlers
            RestXMLParser parser = (RestXMLParser) xmlReader
                    .getContentHandler();
            parser.setHandlers(handlers, deviceXMLNSHandler);
            try {
                // get the data from the http response
                is = debug ?
                        new DebugInputStream(requestResponsePair, (debug || (handlers == null)) ? mChronicler : null) :
                        requestResponsePair.response.getInputStream();
                // is this an XML response
                if (isXML)
                {
                    // Simulator OKHTTP3 EOF defense code
                    if(response.getContentType() == null){
                        xmlReader.parse(new InputSource(new InputStreamReader(is)));
                    }else{
                        StringBuffer inputString = new StringBuffer();
                        // how long is the content
                        long length = response.getContentLength();
                        byte data[] = new byte[BUFFER_SIZE];
                        // keep going until we're done
                        while(length > 0)
                        {
                            // read some data
                            int bytesRead = is.read(data);

                            inputString.append(new String(data,0,bytesRead));
//                            Log.i("[SIM]","bytesRead : "+bytesRead);

                            // did we get an error
                            if (bytesRead < 0)
                                break;

                            // reduce the amount we have left to read
                            length -= bytesRead;
//                            Log.i("[SIM2]","length : "+length);
                            if(length == 1 || length == 2){
//                                Log.i("[SIM2]","last 10 byte : "+inputString.substring(inputString.length()-10));
                                if(!inputString.toString().endsWith(">")){
                                    inputString.append(">");
                                }
                                break;
                            }
                        }

//                        if (inputString.length() > 4000) {
//
//                            int chunkCount = inputString.length() / 4000;     // integer division
//                            for (int i = 0; i <= chunkCount; i++) {
//                                int max = 4000 * (i + 1);
//                                if (max >= inputString.length()) {
//                                    Log.i("[SIM]", inputString.toString().substring(4000 * i));
//                                } else {
//                                    Log.v("[SIM]", inputString.toString().substring(4000 * i, max));
//                                }
//                            }
//                        } else {
//                            Log.i("[SIM]",inputString.toString());
//                        }




                        xmlReader.parse(new InputSource(new InputStreamReader(new ByteArrayInputStream(inputString.toString().getBytes()))));

                    }


                    // parse the xml
//                    xmlReader.parse(new InputSource(new InputStreamReader(is)));
                }
                else
                {
                    // Not XML, read the data so we can print it out (hopefully)

                    // how long is the content
                    long length = response.getContentLength();
                    byte data[] = new byte[BUFFER_SIZE];



                    // keep going until we're done
                    while(length > 0)
                    {
                        // read some data
                        int bytesRead = is.read(data, 0, (int)Math.min(data.length, length));
//                        Log.i("[SIM]","bytesRead : "+bytesRead);
                        // did we get an error
                        if (bytesRead < 0)
                            break;

                        // reduce the amount we have left to read
                        length -= bytesRead;
                    }
                }
            } catch (Throwable e) {
                Log.e("[SIM]",e.getMessage(),e);
                // clean up if something went wrong
                if (handlers != null) {
                    handlers.cleanupData();
                    handlers.setTagData(Constants.XML_TAG__XML_PARSE_EXCEPTION, Boolean.TRUE);
                }
            } finally{
                try {
                    // close our input stream,
                    // if we had debug enabled this will dump the
                    // contents to the log
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException ignored) {
                }
            }

        }
    }

    /**
     * Parses an HTTP Rest XML response with the provided XML tag handlers
     * @param xmlResponse
     *              Request/Response that requires parsing
     * @param handlers
     *              XML handlers that should be invoked during parsing of the XML
     * @param debugSetting
     * 			    Debug option override
     * @hide
     */
    public void parseXMLResponse(String xmlResponse,
                                 RestXMLTagHandler handlers, int debugSetting) {

        // anything to do?
        if (TextUtils.isEmpty(xmlResponse)) {
            return;
        }

        // debug the data
        boolean debug;
        switch(debugSetting)
        {
            case HTTP_REQUEST_DEBUG_OPTION__ON:
            case HTTP_REQUEST_DEBUG_OPTION__OFF:
                debug = (debugSetting == HTTP_REQUEST_DEBUG_OPTION__ON);
                break;
            default:
                debug = debugXML;
        }

        if (debug) {
            mChronicler.log(Log.DEBUG, TAG, String
                    .format("XML data: \n%s\n", xmlResponse));

        }

        // check we are running on a known thread
        XMLReader xmlReader = deviceXMLParsers.get();
        if (xmlReader == null) {
            mChronicler.log(Log.ERROR, TAG, "Trying to parser xml from unknown thread");
            return;
        }

        // setup the handlers
        RestXMLParser parser = (RestXMLParser) xmlReader
                .getContentHandler();
        parser.setHandlers(handlers, deviceXMLNSHandler);
        // parse the xml
        try {
            xmlReader.parse(new InputSource(new StringReader(xmlResponse)));
        } catch (Exception ignored) {
            // clean up if something went wrong
            if (handlers != null) {
                handlers.cleanupData();
                handlers.setTagData(Constants.XML_TAG__XML_PARSE_EXCEPTION, Boolean.TRUE);
            }
        }
    }

    /**
     * Get the OXPd device hostname
     * @return
     *              hostname configured for OXPd device instance
     */
    public String getHostName() {
        return mHostName;
    }

    /**
     * Get the OXPd device credentials
     * @return
     *              cdevice credentials for OXPd device instance
     */
    public DeviceCredentials getDeviceCredentials() {
        return mDeviceCredentials;
    }

    /**
     * Get the device URL
     * @return
     *              url for discovery tree or IPP
     * @hide
     */
    public URL getDeviceUrl(String resource) throws MalformedURLException {
        return isPanel() ?
                isEmulator() ?
                        new URL(Constants.SCHEME_HTTP, Constants.OXPD_HOST_INTERNAL,  Constants.OXPD_PORT_INTERNAL, resource) :
                        new URL(Constants.SCHEME_HTTP, Constants.OXPD_HOST_INTERNAL, resource) :
                new URL(Constants.SCHEME_HTTPS, mHostName, resource);
    }

    /**
     * Get the OXPd url
     * @return
     *              url configured for OXPd device instance
     * @hide
     */
    public URL getOXPdUrl(String resource) throws MalformedURLException {
        return isPanel() ?
                new URL(Constants.SCHEME_HTTP, Constants.HOSTNAME_INTERNAL, Constants.OXPD_PORT_INTERNAL, resource) :
                new URL(Constants.SCHEME_HTTPS, mHostName, Constants.OXPD_PORT, resource);
    }


    /**
     * Post a request to be executed by the device instance
     * @param requestCallback
     *              Request callback to process the message
     * @param params
     *              Command parameters needed to execute the command
     * @param requestID
     *              Callback identifier provided by the caller
     * @param callback
     *              Callback to invoke then command is done executing
     * @hide
     */
    public void queueRequest(DeviceProcessRequestCallback requestCallback, Object params,
                             int requestID, RequestCallback callback) {
        // send out a process request message
        mHandler.sendMessage(mHandler.obtainMessage(
                HANDLER_MSG_PROCESS_REQUEST, new DeviceProcessRequestParams(
                        requestCallback, params, requestID, callback)));
    }

    /**
     * Post a runnable request to be executed by the device instance
     * @param callback
     *              Runnable to execute
     * @param delay
     *              Time to wait before executing
     * @hide
     */
    public void queueDelayedCallback(Runnable callback, long delay) {
        mHandler.sendMessageDelayed(Message.obtain(mHandler, callback), delay);
    }

    /**
     * Remove a Runnable from device instance
     * @param callback
     *              Runnable to remove
     * @hide
     */
    public void removeCallback(Runnable callback) {
        mHandler.removeCallbacks(callback);
    }

    /**
     * Update the device login credentials
     * @param userName
     *              Username for admin credentials
     * @param password
     *              Password for admin credentials
     */
    public void updateUserCredentials(String userName, String password) throws IllegalArgumentException{
        synchronized (mAdminAuthHeaderLock) {
            if (!TextUtils.isEmpty(userName)) {
                mDeviceCredentials.updateCredentials(userName, password);
                mAdminAuthHeader = buildAuthorizationHeader(mDeviceCredentials.getUsername(), mDeviceCredentials.getPassword());
            } else {
                // reset to default value
                mAdminAuthHeader = mGuestAuthHeader;
            }
        }
    }

    /**
     * Update the device network interface name
     * @param networkInterfaceName
     *              Network interface name
     */
    public void updateNetworkInterface(String networkInterfaceName) throws IllegalArgumentException{
        this.mNetworkInterface = networkInterfaceName;
        mLocalIpAddress = null; // reset IP, to retrieve new for provided interface
    }

    /**
     * Update request socket connect timeout
     * @param connectTimeout
     *              connect timeout value
     */
    public void updateConnectTimeout(int connectTimeout) {
        if (connectTimeout > 0) {
            mConnectTimeout = connectTimeout;
        } else {
            mConnectTimeout = Constants.CONNECTION_TIMEOUT;
        }
    }

    /**
     * Update request socket read timeout
     * @param readTimeout
     *              read timeout value
     */
    public void updateReadTimeout(int readTimeout) {
        if (readTimeout > 0) {
            mReadTimeout = readTimeout;
        } else {
            mReadTimeout = Constants.SOCKET_TIMEOUT;
        }
    }

    /**
     * Build an Authorization header
     * @param username
     *              Username to login in as
     * @param password
     *              Password associated with username
     * @return
     *              HttpHeader encoded with user credentials
     */
    private HttpHeader buildAuthorizationHeader(String username, String password) {
        StringBuilder builder = new StringBuilder(Constants.HTTP_HEADER_VALUE__AUTHORIZATION_MODE);
        builder.append(' ');

        if (password == null) password = "";

        try {
            builder.append(Base64.encodeToString(String.format(Locale.US, "%s:%s", username,password).getBytes(HttpRequest.DEFAULT_TEXT_ENCODING), Base64.URL_SAFE | Base64.NO_WRAP));
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Failed to build authorization header", e);
        }

        return HttpHeader.create(Constants.HTTP_HEADER__AUTHORIZATION, builder.toString());
    }

    /**
     * Get the device's xml namespace handler
     * @return
     *              XML Namespace handler for OXPd device instance
     * @hide
     */
    public RestXMLNSHandler getXMLNSHandler() {
        return deviceXMLNSHandler;
    }

    /**
     * Get an {@link OXPdDeviceInfo} instance
     * @param requestID
     *              ID associated with this request
     * @param callback
     *              Callback to invoke when finished
     */
    public void getOXPdDeviceInfoInstance(int requestID, RequestCallback callback) {
        queueRequest(mGetDeviceInfoInstanceHandler, null, requestID, callback);
    }

    /**
     * @param requestID
     *              ID associated with this request
     * @param callback
     *              Callback to invoke when finished
     */
    public void getOXPdPrintInstance(int requestID, RequestCallback callback) {
        queueRequest(mGetPrintInstanceHandler, null, requestID, callback);
    }

    /**
     * Get an {@link OXPdUIConfiguration} instance
     * @param requestID
     *              ID associated with this request
     * @param callback
     *              Callback to invoke when finished
     */
    public void getOXPdUIConfigurationInstance(int requestID, OXPdDevice.RequestCallback callback) {
        queueRequest(mGetUIConfigurationInstanceHandler, null, requestID, callback);
    }

    /**
     * Retrieve an {@link OXPdScan} instance
     * @param requestID
     *              ID associated with request
     * @param callback
     *              Callback to invoke when request is complete
     */
    public void getOXPdScanInstance(int requestID, OXPdDevice.RequestCallback callback) {
        queueRequest(mGetScanInstanceHandler, null, requestID, callback);
    }

    /**
     * Get an {@link OXPdCopy} instance
     * @param requestID
     *              ID associated with this request
     * @param callback
     *              Callback to invoke when finished
     */
    public void getOXPdCopyInstance(int requestID, OXPdDevice.RequestCallback callback) {
        queueRequest(mGetCopyInstanceHandler, null, requestID, callback);
    }

    /**
     * Get an {@link OXPdAccessories} instance
     * @param requestID
     *              ID associated with this request
     * @param callback
     *              Callback to invoke when finished
     */
    public void getOXPdAccessoriesInstance(int requestID, OXPdDevice.RequestCallback callback) {
        queueRequest(mGetAccessoriesInstanceHandler, null, requestID, callback);
    }

    /**
     * Determine if this is an OXPd supported device
     * @param requestID
     *              ID associated with request
     * @param requiredFeatures
     *              Required supported features
     * @param callback
     *              Callback to invoke when request is complete
     */
    @SuppressWarnings("SameParameterValue")
    public void isOXPdSupported(int requestID, @OXPdFeatureSet int requiredFeatures, RequestCallback callback) {
        queueRequest(mIsOXPdSupportedHandler, requiredFeatures, requestID, callback);
    }

    /**
     * Update logging level
     * @param logLevel logging level
     */
    public void setLogLevel(int logLevel) {
        mChronicler.lock();
        mChronicler.setLogLevel(logLevel);
        mChronicler.unlock();
    }

    /**
     * Update file to write log to
     * @param logFile File to write log into
     */
    public void setLogFile(String logFile) {
        mChronicler.lock();
        mChronicler.setLogFile(logFile);
        mChronicler.unlock();
    }

    /**
     * Update the logging level and file to write logs to
     * @param logLevel logging level
     * @param logFile log file
     */
    public void setLogLevelAndLogFile(int logLevel, String logFile) {
        mChronicler.lock();
        mChronicler.setLevelAndLogFile(logLevel, logFile);
        mChronicler.unlock();
    }

    /**
     * Log message through the {@link Chronicler}
     * @param logLevel Logging level
     * @param tag Log tag
     * @param message Log message
     */
    public void log(int logLevel, String tag, String message) {
        mChronicler.lock();
        mChronicler.log(logLevel, tag, message);
        mChronicler.unlock();
    }

    /**
     * Method to convert a request error code to a human-readable string
     * @param errorCode
     * 			    Error code to convert to a human-readable string
     * @return
     *              Human-readable description of the error code
     */
    public static String errorCodeToString(int errorCode) {
        switch (errorCode) {
            case REQUEST_RETURN_CODE__OK:
                return ("OK");
            case REQUEST_RETURN_CODE__NOT_SUPPORTED:
                return ("Not Supported");
            case REQUEST_RETURN_CODE__NOT_IMPLEMENTED:
                return ("Not Implemented");
            case REQUEST_RETURN_CODE__INVALID_PARAMETERS:
                return ("Invalid Parameters");
            case REQUEST_RETURN_CODE__OUT_OF_MEMORY:
                return ("Out of Memory");
            case REQUEST_RETURN_CODE__FEATURE_DISABLED:
                return ("Feature Disabled");
            case REQUEST_RETURN_CODE__PROGRAMMER_FU:
                return ("Programmer Screw-up");
            case REQUEST_RETURN_CODE__FEATURE_FAILED:
                return ("Feature Failed");
            case REQUEST_RETURN_CODE__MISSING_IMPLEMENTATION:
                return ("Missing Implementation");
            case REQUEST_RETURN_CODE__TRANSACTION_FAILED:
                return ("Transaction Failed");
            case REQUEST_RETURN_CODE__DATA_NOT_FOUND:
                return ("No Data Found");
            case REQUEST_RETURN_CODE__QUITTING:
                return ("Quitting");
            case REQUEST_RETURN_CODE__WTF:
                return ("What a Terrible Failure!!");
            default:
                return ("Unknown error!");
        }
    }

    /**
     * Validate the
     * @param requiredFeatures
     *          Feature set to validate
     * @return Validated flag set
     */
    static @OXPdDevice.OXPdFeatureSet int validatedFeatureSet(int requiredFeatures) {
        return requiredFeatures & OXPD_FEATURE_ALL;
    }

    /**
     * Convenience method for building & executing SOAP requests
     * @param soapRequestBuilder
     *              SOAP request builder
     * @return
     *              HTTP request/response pair
     * @hide
     */
    public HttpRequestResponseContainer performSOAPRequest(SOAPRequestBuilder soapRequestBuilder) {

        List<String> schemaList = new ArrayList<String>();

        schemaList.addAll(
                Arrays.asList(
                        OXPdDevice.Constants.XML_SCHEMA__XSI,
                        OXPdDevice.Constants.XML_SCHEMA__XSD,
                        OXPdDevice.Constants.XML_SCHEMA__SOAP_ENVELOPE,
                        OXPdDevice.Constants.XML_SCHEMA__SOAP_ADDRESSING));
        schemaList.addAll(soapRequestBuilder.getXMLSchemas());

        RestXMLWriter xmlWriter = new RestXMLWriter(getXMLNSHandler(),
                false,
                schemaList.toArray(new String[schemaList.size()]));
        RestXMLWriter.XMLAttributes attributes = new RestXMLWriter.XMLAttributes();
        xmlWriter.writeStartTag(OXPdDevice.Constants.XML_SCHEMA__SOAP_ENVELOPE, OXPdDevice.Constants.XML_TAG__SOAP_ENVELOPE__ENVELOPE, null);
        // header
        xmlWriter.writeStartTag(OXPdDevice.Constants.XML_SCHEMA__SOAP_ENVELOPE, OXPdDevice.Constants.XML_TAG__SOAP_ENVELOPE__HEADER, null);

        // action
        attributes.clear().add(OXPdDevice.Constants.XML_SCHEMA__SOAP_ENVELOPE,
                OXPdDevice.Constants.XML_ATTRIBUTE_SOAP_ENVELOPE_MUST_UNDERSTAND,
                String.valueOf(1));
        xmlWriter.writeTag(OXPdDevice.Constants.XML_SCHEMA__SOAP_ADDRESSING, OXPdDevice.Constants.XML_TAG__ADDRESSING__ACTION, attributes, "%s/%s/%s",
            soapRequestBuilder.getServiceRevision(), soapRequestBuilder.getServiceName(), soapRequestBuilder.getSoapOperation());

        // message id
        xmlWriter.writeTag(OXPdDevice.Constants.XML_SCHEMA__SOAP_ADDRESSING, OXPdDevice.Constants.XML_TAG__ADDRESSING__MESSAGE_ID, null,
                "urn:uuid:%s", UUID.randomUUID().toString());

        //reply-to
        xmlWriter.writeStartTag(OXPdDevice.Constants.XML_SCHEMA__SOAP_ADDRESSING, OXPdDevice.Constants.XML_TAG__ADDRESSING__REPLY_TO, null);
        xmlWriter.writeTag(OXPdDevice.Constants.XML_SCHEMA__SOAP_ADDRESSING, OXPdDevice.Constants.XML_TAG__ADDRESSING__ADDRESS, null,
                "%s", OXPdDevice.Constants.XML_VALUE__SOAP_ADDRESSING__ANONYMOUS);
        xmlWriter.writeEndTag(OXPdDevice.Constants.XML_SCHEMA__SOAP_ADDRESSING, OXPdDevice.Constants.XML_TAG__ADDRESSING__REPLY_TO);

        // to
        attributes.clear().add(OXPdDevice.Constants.XML_SCHEMA__SOAP_ENVELOPE,
                OXPdDevice.Constants.XML_ATTRIBUTE_SOAP_ENVELOPE_MUST_UNDERSTAND,
                String.valueOf(1));
        xmlWriter.writeTag(OXPdDevice.Constants.XML_SCHEMA__SOAP_ADDRESSING, OXPdDevice.Constants.XML_TAG__ADDRESSING__TO, attributes,
                "%s", soapRequestBuilder.getURL().toString());

        // close header
        xmlWriter.writeEndTag(OXPdDevice.Constants.XML_SCHEMA__SOAP_ENVELOPE, OXPdDevice.Constants.XML_TAG__SOAP_ENVELOPE__HEADER);

        // body
        xmlWriter.writeStartTag(OXPdDevice.Constants.XML_SCHEMA__SOAP_ENVELOPE, OXPdDevice.Constants.XML_TAG__SOAP_ENVELOPE__BODY, null);

        soapRequestBuilder.fillSoapOperationBody(xmlWriter);

        // close body
        xmlWriter.writeEndTag(OXPdDevice.Constants.XML_SCHEMA__SOAP_ENVELOPE, OXPdDevice.Constants.XML_TAG__SOAP_ENVELOPE__BODY);

        // close envelope
        xmlWriter.writeEndTag(OXPdDevice.Constants.XML_SCHEMA__SOAP_ENVELOPE, OXPdDevice.Constants.XML_TAG__SOAP_ENVELOPE__ENVELOPE);

        List<HttpHeader> headerList = new ArrayList<HttpHeader>();
        // "add" date header, if request add their own it will get used instead
        headerList.add(HttpRequest.createDateHeader());
        // add request headers
        headerList.addAll(soapRequestBuilder.getRequestHeaders());
        // add auth credentials last to ensure no other auth header gets used
        synchronized (mAdminAuthHeaderLock) {
            headerList.add(soapRequestBuilder.useAdminCredentials() ? mAdminAuthHeader : mGuestAuthHeader);
        }

        String soapMessage = xmlWriter.getXMLPayload();

        return doHttpPost(soapRequestBuilder.getURL(),
                OXPdDevice.Constants.CONTENT_TYPE__SOAP_XML,
                soapMessage,
                true, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL,
                headerList.toArray(new HttpHeader[headerList.size()]));
    }

    /**
     * Make sure all the content of a response is consumed
     */
    private void httpConsumeContent()
    {
        HttpResponse response = deviceLastHttpResponse.get();
        if (response != null) {
            response.disconnect();
            deviceLastHttpResponse.remove();
        }
    }

    /**
     * Common routine to fill out an HTTP request
     * @param request
     * 				HTTP request we are filling out
     * @param requestURL
     * 				URL the request needs to be sent to
     * @param requestContentType
     * 				Description of the content body
     * @param requestBody
     * 				Request body to send as part of the request, assumed to be UTF-8
     * @param expectInput
     *              Expect to receive data as part of this request?
     * @param connectionTimeout
     * 				Timeout waiting for connection
     * @param socketTimeout
     * 				Timeout waiting for response
     * @param debugSettings
     * 				Debug settings to use when filling out the request
     * @param headers
     *              Variable list of headers to add to the request
     */
    @SuppressWarnings("SameParameterValue")
    @SuppressLint("SSLCertificateSocketFactoryGetInsecure")
    private HttpRequest fillOutHttpRequest(HttpRequest.HTTPRequestType request,
                                           URL requestURL,
                                           String requestContentType,
                                           String requestBody,
                                           boolean expectInput,
                                           int connectionTimeout,
                                           int socketTimeout,
                                           int debugSettings,
                                           HttpHeader... headers) {

        HttpRequest httpRequest = null;

        if ((request != null) && (requestURL != null)) {

            try {
                httpRequest = new HttpRequest.Builder()
                        .setURL(requestURL)
                        .setConnectionTimeout(connectionTimeout)
                        .setSocketTimeout(socketTimeout)
                        .setUsesCache(false)
                        .setFollowRedirects(true)
                        .setRequestMethod(request)
                        .setRequestOutputContentType(requestContentType)
                        .setRequestOutputData(requestBody)
                        .setRequestInputData(expectInput)
                        .addHeaders(headers)
                        .addHeader(HttpHeader.create(HttpRequest.HEADER__CONNECTION, HttpRequest.HEADER_VALUE__CONNECTION)) // add header to close connection
                        .setSSLSocketFactory(mSSLSocketFactory)
                        .setHostNameVerifier(mHostnameVerifier)
                        .build();
            } catch (IOException e) {
                Log.e(TAG, "Failed to build HttpRequest", e);
            }

            // determine the logging level
            int logLevel = ((debugSettings == HTTP_REQUEST_DEBUG_OPTION__ON) ? 0 : Log.VERBOSE);
            boolean debugLogging = ((debugSettings == HTTP_REQUEST_DEBUG_OPTION__ON) ||
                    ((debugSettings == HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL) &&
                            mChronicler.isLoggable(Log.VERBOSE)));

            // set the uri for the request
            // lock the debug log
            mChronicler.lock();

            if (debugLogging)
                mChronicler.log(logLevel, TAG, "SOAP request: " + requestBody);

            // output to log if logging was explicitly requested or
            // if using global settings and Verbose logging is enabled
            if (debugLogging)
                mChronicler.log(logLevel, TAG,
                        String.format(Locale.US, "Creating Http %s request for: %s",
                                request.mHttpMethod, requestURL.getPath()));
            // add the headers to the request
            for (HttpHeader header : httpRequest.getHeaders()) {
                if (debugLogging)
                    mChronicler.log(logLevel, TAG,
                            String.format(Locale.US, "Http %s request header: %s",
                                    request.mHttpMethod, header.toString()));
            }

            // set the content type and request body if provided
            if ((requestContentType != null) && (requestBody != null)) {

                if (debugLogging || debugXML)
                {
                    int level = ((debugXML ^ debugLogging) ? 0 : logLevel);
                    mChronicler.log(level, TAG, String.format(Locale.US,
                            "Http %s request Content-Type: %s",
                            request.mHttpMethod, requestContentType));
                    mChronicler.log(level,
                            TAG,
                            String.format(Locale.US, "Http %s request Body:\n%s",
                                    request.mHttpMethod, requestBody));
                }
            } else if ((requestContentType != null) || (requestBody != null)) {
                mChronicler.log(Log.ERROR,
                        TAG,
                        String.format(Locale.US,
                                "Http %s request appears to be incomplete, incomplete contentType/requestBody",
                                request.mHttpMethod));
            }
            // unlock the debug log
            mChronicler.unlock();
        }
        // return the filled out request
        return httpRequest;
    }

    /**
     * Return the Context used by the device
     * @return Context used by device
     * @hide
     */
    @Override
    public Context getContext() {
        return mContext;
    }

    /**
     * Return the provided external signer
     * @return External signer
     * @hide
     */
    public ExternalSecurityProvider getExternalSigner() {
        return mExternalSigner;
    }

    /**
     * Build up a Destination entry for the local scan receiver
     * @return Destination object for the local scan receiver
     */
    public Destination getLocalScanDestination() {
        Destination destination = null;
        String host = getLocalIpAddress();
        if (!TextUtils.isEmpty(host)) {
            destination =
                    new Destination(
                            Uri.parse(getDeviceAbsoluteURLForPath(null)),
                            null,
                            null,
                            null,
                            60,
                            120,
                            0,
                            60
                    );
        }
        return destination;
    }

    /**
     * Set the temporary scan directory
     * @param folder Temporary scan directory
     */
    public void setScanTemporaryFolder(final File folder) {
        mScannedFilesTemporaryFolder = folder;
        mOXPdServer.setTempFileManagerFactory(new NanoHTTPD.TempFileManagerFactory() {
            public NanoHTTPD.TempFileManager create() {
                return new OXPdServer.OXPdServerTempFileManager(mScannedFilesTemporaryFolder);
            }
        });
    }

    /**
     * Return the temporary scan directory
     * @return temporary scan directory
     */
    public File getScanTemporaryFolder() {
        return mScannedFilesTemporaryFolder;
    }

    /**
     * Return the local IP address
     * @return IP address
     */
    String getLocalIpAddress() {
        if (mLocalIpAddress == null) {
            mLocalIpAddress = mOXPdServer.getLocalIpAddress(mNetworkInterface);
        }
        return mLocalIpAddress;
    }

    /**
     * Build up the DeviceInUse URL
     * @hide
     * @return Device in use URL
     */
    public String getInUseUrl() {
        try {
            return new URL("http", getLocalIpAddress(), mOXPdServer.getLocalPort(), "DeviceInUse.html").toString();
        } catch (MalformedURLException e) {
            Log.e(TAG, "URL is malformed", e);
            return null;
        }
    }

    /**
     * Create and setup handler for SOAP faults
     * @hide
     * @return SOAP XML tag handler
     */
    public RestXMLTagHandler createSOAPFaultHandler() {
        RestXMLTagHandler tagHandler = new RestXMLTagHandler();
        RestXMLTagHandler.XMLStartTagHandler faultCatcherStart = new RestXMLTagHandler.XMLStartTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, Attributes attributes) {
                if (TextUtils.equals(localName, OXPdDevice.Constants.XML_TAG__SOAP_FAULT)) {
                    handler.setTagData(OXPdDevice.Constants.XML_TAG__SOAP_FAULT, new SOAPFault.Builder());
                } else if (TextUtils.equals(localName, OXPdDevice.Constants.XML_TAG__SOAP_CODE)) {
                    handler.setTagData(OXPdDevice.Constants.XML_TAG__SOAP_CODE, new SOAPFaultCode.Builder());
                } else if (TextUtils.equals(localName, OXPdDevice.Constants.XML_TAG__SOAP_SUBCODE)) {
                    SOAPFaultCode.Builder builder = (SOAPFaultCode.Builder) handler.getTagData(OXPdDevice.Constants.XML_TAG__SOAP_CODE);
                    if (builder != null) builder.startSubcode();
                }
            }
        };
        RestXMLTagHandler.XMLEndTagHandler faultCatcherEnd = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {

                if (TextUtils.equals(localName, OXPdDevice.Constants.XML_TAG__SOAP_FAULT)) {
                    SOAPFault.Builder faultBuilder = (SOAPFault.Builder) handler.getTagData(OXPdDevice.Constants.XML_TAG__SOAP_FAULT);
                    if (faultBuilder != null) {
                        try {
                            handler.setTagData(OXPdDevice.Constants.XML_TAG__SOAP_FAULT, faultBuilder.build());
                        } catch(Exception e) {
                            handler.setTagData(OXPdDevice.Constants.XML_TAG__SOAP_FAULT, SOAPFault.fromException(e));
                        }
                    }
                } else if (TextUtils.equals(localName, OXPdDevice.Constants.XML_TAG__SOAP_CODE)) {
                    SOAPFault.Builder faultBuilder = (SOAPFault.Builder) handler.getTagData(OXPdDevice.Constants.XML_TAG__SOAP_FAULT);
                    SOAPFaultCode.Builder codeBuilder = (SOAPFaultCode.Builder) handler.getTagData(OXPdDevice.Constants.XML_TAG__SOAP_CODE);
                    if ((faultBuilder != null) && (codeBuilder != null)) {
                        try {
                            faultBuilder.setFaultCode(codeBuilder.build());
                        } catch(Exception e) {
                            faultBuilder.setFaultCode(SOAPFaultCode.fromException(e));
                        }
                    }
                    handler.clearTagData(OXPdDevice.Constants.XML_TAG__SOAP_CODE);
                } else if (TextUtils.equals(localName, OXPdDevice.Constants.XML_TAG__SOAP_SUBCODE)) {
                    SOAPFaultCode.Builder builder = (SOAPFaultCode.Builder) handler.getTagData(OXPdDevice.Constants.XML_TAG__SOAP_CODE);
                    if (builder != null) {
                        try {
                            builder.endSubcode();
                        } catch(Exception ignored) {
                            handler.clearTagData(OXPdDevice.Constants.XML_TAG__SOAP_CODE);
                        }
                    }
                } else if (TextUtils.equals(OXPdDevice.Constants.XML_TAG__SOAP_VALUE, localName)) {

                    SOAPFaultCode.Builder builder = (SOAPFaultCode.Builder) handler.getTagData(OXPdDevice.Constants.XML_TAG__SOAP_CODE);
                    if ((builder != null)
                            && xmlTagStack.isTagInStack(OXPdDevice.Constants.XML_SCHEMA__SOAP_ENVELOPE, OXPdDevice.Constants.XML_TAG__SOAP_CODE)
                            && xmlTagStack.isTagInStack(OXPdDevice.Constants.XML_SCHEMA__SOAP_ENVELOPE, OXPdDevice.Constants.XML_TAG__SOAP_FAULT)) {
                        if (!TextUtils.isEmpty(data)) {
                            String[] values = data.split(":");
                            builder.setValue(values[values.length - 1]);
                        }
                    }

                    if (xmlTagStack.isTagInStack(OXPdDevice.Constants.XML_SCHEMA__SOAP_ENVELOPE, OXPdDevice.Constants.XML_TAG__SOAP_SUBCODE) && xmlTagStack.isTagInStack(OXPdDevice.Constants.XML_SCHEMA__SOAP_ENVELOPE, OXPdDevice.Constants.XML_TAG__SOAP_FAULT)) {
                        if (!TextUtils.isEmpty(data)) {
                            String[] values = data.split(":");
                            handler.setTagData(localName, values[values.length - 1]);
                        }
                    }
                } else if (TextUtils.equals(OXPdDevice.Constants.XML_TAG__SOAP_TEXT, localName)) {
                    if (xmlTagStack.isTagInStack(OXPdDevice.Constants.XML_SCHEMA__SOAP_ENVELOPE, OXPdDevice.Constants.XML_TAG__SOAP_REASON)) {
                        SOAPFault.Builder faultBuilder = (SOAPFault.Builder) handler.getTagData(OXPdDevice.Constants.XML_TAG__SOAP_FAULT);
                        if (faultBuilder != null) {
                            faultBuilder.addReason(data);
                        }
                    }
                }
            }
        };

        tagHandler.setXMLHandler(OXPdDevice.Constants.XML_TAG__SOAP_CODE, faultCatcherStart, faultCatcherEnd);
        tagHandler.setXMLHandler(OXPdDevice.Constants.XML_TAG__SOAP_SUBCODE, faultCatcherStart, faultCatcherEnd);
        tagHandler.setXMLHandler(OXPdDevice.Constants.XML_TAG__SOAP_VALUE, null, faultCatcherEnd);
        tagHandler.setXMLHandler(OXPdDevice.Constants.XML_TAG__SOAP_TEXT, null, faultCatcherEnd);
        tagHandler.setXMLHandler(OXPdDevice.Constants.XML_TAG__SOAP_FAULT, faultCatcherStart, faultCatcherEnd);
        return tagHandler;
    }

    /**
     * Return the complete URL for the device internal relative URL
     * @param relativeURL Device internal URL
     * @return Complete URL used for external clients to connect to this instance
     */
    public String getDeviceAbsoluteURLForPath(String relativeURL) {
        try {
            return new URL("http", isEmulator() ? "127.0.0.1" : getLocalIpAddress(),
                    mOXPdServer.getLocalPort(), getDeviceRelativeURLForPath(relativeURL)).toString();
        } catch (MalformedURLException e) {
            Log.e(TAG, "URL is malformed", e);
            return null;
        }
    }

    /**
     * Prepend the device specific URL identifier used by the server
     * @param uri Device internal URL
     * @return Device specific relative URL
     */
    private String getDeviceRelativeURLForPath(String uri) {
        if (uri == null) uri = "";
        return "/" + mOXPdServer.getDevicePathIdentifier(OXPdDevice.this) + "/" + uri;
    }

    /**
     * Start server if it's not started yet
     * @return server alive or not
     */
    public boolean checkServerStatus() {
        mOXPdServer.startServer(); // ensure that server is started
        boolean alive = mOXPdServer.isAlive();
        Log.d(TAG, "Server status: " + alive + " at port " + mOXPdServer.getListeningPort());
        return alive;
    }

    /**
     * Process a server POST request
     * @param session HTTP session
     * @return HTTP response
     * @hide
     */
    @Override
    public NanoHTTPD.Response handleServerRequest(NanoHTTPD.IHTTPSession session) {

        // only support POST commands
        if (session.getMethod() != NanoHTTPD.Method.POST) {
            mChronicler.log(Log.ERROR, TAG, "Method " + session.getMethod() + " not supported");
            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, NanoHTTPD.MIME_PLAINTEXT, null, 0);
        }

        Map<String, List<String>> params = session.getParameters();
        Map<String, String> files = new LinkedHashMap<String, String>();

        String uri = session.getUri();
        mChronicler.log(Log.DEBUG, TAG, "POST to " + uri);
        String handlerURI = uri.substring(getDeviceRelativeURLForPath(null).length());

        HandleIncomingServerRequest requestHandler;
        synchronized (mServerHooks) {
            requestHandler = mServerHooks.get(uri);
        }

        if (requestHandler == null) {
            mChronicler.log(Log.ERROR, TAG, "Handler not found for uri " + uri);
            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.GONE, NanoHTTPD.MIME_PLAINTEXT, null, 0);
        }

        try {
            session.parseBody(files);
        } catch (Throwable e) { // For temp file IO errors NanoHTTPd throws Error
            Log.e(TAG, "Failed to parse POST request body", e);
            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, NanoHTTPD.MIME_PLAINTEXT, null, 0);
        }

        if (files.containsKey(Constants.SOAP_MESSAGE_KEY) && mChronicler.isLoggable(Log.VERBOSE)) {
            mChronicler.log(Log.VERBOSE, TAG, "Scan event: " + files.get(Constants.SOAP_MESSAGE_KEY));
        }
        mChronicler.log(Log.DEBUG, TAG, "invoking handler for " + handlerURI);
        NanoHTTPD.Response response = requestHandler.handleRequest(handlerURI, files, params);
        mChronicler.log(Log.DEBUG, TAG, "handler complete for " + handlerURI);
        return response;
    }

    /**
     * Determine if the minor version requirements are met
     * @param minorVersion feature minor version
     * @param versionRequirements version requirements
     * @return true if requirement is met, false otherwise
     * @hide
     */
    public boolean versionRequirementsSatisfied(String minorVersion, int[] versionRequirements) {

//        Log.i("[SIM]","minorVersion : "+minorVersion);


        if (TextUtils.isEmpty(minorVersion) || (versionRequirements == null)) return false;
        final String versionSplitter = "\\.";
        String[] splits = minorVersion.split(versionSplitter);
        boolean supported = (splits.length > 0);
        for(int index = 0; supported && ((index < splits.length) || (index < versionRequirements.length)); index++) {
//            Log.i("[SIM]","versionRequirements["+index+"] : "+versionRequirements[index]);
            int valA, valB;
            valA = valB = 0;
            try {
                if (index < splits.length) valA = Integer.valueOf(splits[index]);
            } catch(NumberFormatException ignored) {}
            if (index < versionRequirements.length) valB = versionRequirements[index];
            supported = (valA >= valB);
        }
        return supported;
    }

    /**
     * Default KeyStore instance
     * @param ks Default KeyStore for OXPdDevice instances
     */
    public static void setDefaultKeyStore(KeyStore ks) {
        sDefaultKeyStore = ks;
    }

    /**
     * Accept updated certificate
     */
    public void acceptUpdatedCertificate() {
        mHandler.sendEmptyMessage(HANDLER_MSG_PROCESS_ACCEPT_NEW_CERTIFICATE);
    }

    /**
     * Get the device instance SSL socket factory
     * @return Device SSL socket factory
     * @hide
     */
    public SSLSocketFactory getDeviceSSLSocketFactory() {
        return mSSLSocketFactory;
    }

    /**
     * Get the device instance hostname verifier
     * @return Device host name verifier
     * @hide
     */
    public HostnameVerifier getDeviceHostnameVerifier() {
        return mHostnameVerifier;
    }

    public InternalServerHooks getServerHooks() {
        return mServerHooksIFC;
    }


    /**
     * Check if running on front panel
     * @return
     *              true if running on front panel, false otherwise
     */
    public static boolean isPanel() {
        return Build.MODEL.toLowerCase().contains(Constants.PRINTER_PANEL) || isEmulator();
    }

    /**
     * @return Returns true if the context is Emulator
     */
    public static boolean isEmulator() {
        if (isEmulator == null) {
            String isEmulatorDevice = getProp("ro.kernel.qemu");
            isEmulator = Build.BRAND.startsWith(Constants.GENERIC_SDK)
                    || Build.FINGERPRINT.contains(Constants.GENERIC_SDK)
                    || (isEmulatorDevice!= null && isEmulatorDevice.equals("1"));
        }
        return isEmulator;
    }

    public static String getEmulatorInternalHost() {
        String host = getProp(Constants.EMULATOR_HOST_PROP);
        if (TextUtils.isEmpty(host)) host = Constants.INTERNAL_EMULATOR_HOST;
        return host;
    }

    public static String getProp(String key) {
        try {
            ClassLoader classLoader = OXPdDevice.class.getClassLoader();
            Class SystemProperties = classLoader.loadClass("android.os.SystemProperties");
            Method methodGet = SystemProperties.getMethod("get", String.class);
            return (String) methodGet.invoke(SystemProperties, key);
        } catch (Exception e) {
        }
        return null;
    }
}
