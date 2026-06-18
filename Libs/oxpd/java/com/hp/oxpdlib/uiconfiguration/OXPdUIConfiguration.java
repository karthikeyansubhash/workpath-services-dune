// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.uiconfiguration;

import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Base64;

import com.hp.oxpdlib.OXPdDevice;
import com.hp.oxpdlib.SOAPFault;
import com.hp.oxpdlib.discovery.DiscoveredFeature;
import com.hp.oxpdlib.discovery.DiscoveryTree;
import com.hp.sdd.jabberwocky.chat.HttpHeader;
import com.hp.sdd.jabberwocky.chat.HttpRequest;
import com.hp.sdd.jabberwocky.chat.HttpRequestResponseContainer;
import com.hp.sdd.jabberwocky.xml.RestXMLNSHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLTagHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Signature;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;

/**
 * This library provides a CORS binding to the OXPd:UIConfiguration service on HP devices.
 */
@SuppressWarnings({"WeakerAccess"})
public class OXPdUIConfiguration {

    // CONSTANTS
    /** UI Configuration XML namespace prefix */
    private static final String XML_PREFIX_OXPD_UI_CONFIGURATION = "uiconfiguration";
    /** Resource name for OXPd UI Configuration service */
    private static final String OXPD_RESOURCE_TYPE__UI_CONFIGURATION = "OXPd:UIConfiguration";
    /** OXPd UI Configuration supported revision */
    private static final String OXPD_REVISION__UI_CONFIGURATION = "http://www.hp.com/schemas/imaging/OXPd/service/uiconfiguration/2010/04/14";
    /** Composite XML schema prefix/value/version for UIConfiguration */
    static final String XML_SCHEMA__OXPD_UI_CONFIGURATION = XML_PREFIX_OXPD_UI_CONFIGURATION+ RestXMLNSHandler.XML_SEPARATOR+
            "http://www.hp.com/schemas/imaging/OXPd/service/uiconfiguration/*"+RestXMLNSHandler.XML_SEPARATOR+"2010/04/14";

    /** OXPd common revision */
    private static final String OXPD_REVISION__COMMON = "http://www.hp.com/schemas/imaging/OXPd/common/2010/04/14";

    /** SOAP service name */
    private static final String SOAP_SERVICE_UI_CONFIGURATION = "IUIConfigurationService";
    /** SOAP request to reserve UI */
    private static final String SOAP_OP__RESERVE_UI_CONTEXT = "ReserveUIContext";
    /** SOAP request to release UI */
    private static final String SOAP_OP__RELEASE_UI_CONTEXT = "ReleaseUIContext";
    /** SOAP request to reset UI Inactivity Timer */
    private static final String SOAP_OP__RESET_UI_INACTIVITY_TIMER = "ResetUIInactivityTimer";
    /** SOAP request to reserve UI for remote */
    private static final String SOAP_OP__RESERVE_REMOVE_UI_CONTEXT = "ReserveRemoteUIContext";
    /** SOAP request to get UI button records **/
    private static final String SOAP_OP__GET_TOP_LEVEL_BUTTON_RECORDS = "GetTopLevelButtonRecords";
    /** SOAP request to get UI button record **/
    private static final String SOAP_OP__GET_TOP_LEVEL_BUTTON_RECORD = "GetTopLevelButtonRecord";
    /** SOAP request to remove UI button record **/
    private static final String SOAP_OP__UNREGISTER_TOP_LEVEL_BUTTON = "UnregisterTopLevelButton";
    /** SOAP request to register a UI button record **/
    private static final String SOAP_OP__REGISTER_TOP_LEVEL_BUTTON = "RegisterTopLevelButton";
    /** SOAP request to get UI attributes **/
    private static final String SOAP_OP__GET_UI_ATTRIBUTES = "GetUIAttributes";
    /** SOAP request to get UI profile **/
    private static final String SOAP_OP__GET_UI_PROFILE = "GetUIProfile";
    /** SOAP request to get Dynatmic UI attributes **/
    private static final String SOAP_OP__GET_CONFIGURABLE_UI_ATTRIBUTES = "GetConfigurableUIAttributes";
    /** SOAP request to get browser configuration **/
    private static final String SOAP_OP__GET_BROWSER_CONFIGURATION = "GetBrowserConfiguration";
    /** SOAP request to set browser configuration **/
    private static final String SOAP_OP__SET_BROWSER_CONFIGURATION = "SetBrowserConfiguration";
    /** SOAP request to get current language **/
    private static final String SOAP_OP__GET_CURRENT_LANGUAGE = "GetCurrentLanguage";

    /** XML tag to specify browser target */
    static final String XML_TAG__UI_CONFIGURATION__BROWSER_TARGET = "browserTarget";
    /** XML tag to specify initial POST query format string */
    static final String XML_TAG__UI_CONFIGURATION__POST_QUERY = "initialPostQueryFormatString";
    /** XML tag to specify web application */
    static final String XML_TAG__UI_CONFIGURATION__WEB_APPLICATION = "webApplication";
    /** XML tag to provide UI Context nonce */
    static final String XML_TAG__UI_CONFIGURATION__UI_CONTEXT_ID = "uiContextId";
    /** XML tag to provide message */
    private static final String XML_TAG__UI_CONFIGURATION__SIGNED_MESSAGE = "signedMessage";
    /** XML tag to provide JWT */
    private static final String XML_TAG__UI_CONFIGURATION__JWT = "JWT";
    /** XML tag for Button ID */
    private static final String XML_TAG__UI_CONFIGURATION__BUTTON_ID = "buttonId";

    /** JWT header type */
    private static final String JWT_HEADER__TYPE = "typ";
    /** JWT header value for type */
    private static final String JWT_HEADER_VALUE__TYPE = "JWT";
    /** JWT header algorithm */
    private static final String JWT_HEADER__ALGORITHM = "alg";
    /** JWT header value for algorithm */
    private static final String JWT_HEADER_VALUE__ALGORITHM = "RSAwithSHA256";
    /** JWT private claim for version */
    private static final String JWT_PAYLOAD__VERSION = "version";
    /** JWT private claim value for version */
    private static final String JWT_PAYLOAD_VALUE__VERSION = "1.0";
    /** JWT private claim for package name */
    private static final String JWT_PAYLOAD__PACKAGE_NAME = "packageName";
    /** JWT private claim for application id */
    private static final String JWT_PAYLOAD__ID = "id";
    /** JWT public claim for issued at */
    private static final String JWT_PAYLOAD__ISSUED_AT = "iat";
    /** JWT private claim for username */
    private static final String JWT_PAYLOAD__USERNAME = "username";
    /** JWT private claim for password */
    private static final String JWT_PAYLOAD__PASSWORD = "password";
    /** JWT private claim for Diffie-Hellman key exchange */
    private static final String JWT_PAYLOAD__PUBLIC_KEY = "publickey";
    /** Key algorithm */
    static final String KEY_ALGORITHM = "DH";

    /** Minimum minor version required to support the RequestRemoteUIContext call */
    private static final int[] RESERVE_REMOTE_UI_CONTEXT__MINOR_VERSION__MIN = { 1, 3 };

    /** Minimum minor version required to support the GetUIAttributes call */
    private static final int[] GET_UI_ATTRIBUTES__MINOR_VERSION__MIN = { 1, 2 };

    /** Diffie-Hellman parameters */
    static final DHParameterSpec mDHParams = new DHParameterSpec(
                        new BigInteger("FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD129024E088A67CC74020BBEA63B139B22514A08798E3404DDEF9519B3CD3A431B302B0A6DF25F14374FE1356D6D51C245E485B576625E7EC6F44C42E9A637ED6B0BFF5CB6F406B7EDEE386BFB5A899FA5AE9F24117C4B1FE649286651ECE45B3DC2007CB8A163BF0598DA48361C55D39A69163FA8FD24CF5F83655D23DCA3AD961C62F356208552BB9ED529077096966D670C354E4ABC9804F1746C08CA18217C32905E462E36CE3BE39E772C180E86039B2783A2EC07A28FB5C55DF06F4C52C9DE2BCBF6955817183995497CEA956AE515D2261898FA051015728E5A8AACAA68FFFFFFFFFFFFFFFF",16),
                        BigInteger.valueOf(2)
                                );

    // CLASSES

    /**
     * UIConfiguration Wake lock class
     */
    private class SleepDeprive {
        /** UIContext the wake lock is associated with */
        private final UIContext mUIContext;
        /** UI timeout */
        private int mTimeout;
        /** Current reference count */
        private int mRefCount = 0;

        /**
         * Constructor
         * @param uiContext
         *              UIContext associated with this wake lock
         */
        private SleepDeprive(UIContext uiContext) {
            mUIContext = uiContext;
            mTimeout = mUIContext.mInactivityTimeout;
        }

        /**
         * Acquire a wake lock
         */
        private void acquire() {
            if (mRefCount++ == 0) {
                resetInactivityTimer();
            }
        }

        /**
         * Release a wake lock
         */
        private void release() {
            if (--mRefCount == 0) {
                mDevice.removeCallback(mResetTimeout);
            }
            if (mRefCount < 0) {
                throw new RuntimeException("SleepDeprive release too many times");
            }
        }

        /**
         * Return true if someone is still holding this wake lock
         * @return true if held, false otherwise
         */
        private boolean isHeld() {
            return (mRefCount > 0);
        }

        /**
         * Reset the inactivity timeout
         */
        private void resetInactivityTimer() {
            // still held & UI context is still valid?
            if (isHeld() && mUIContext.equals(OXPdUIConfiguration.this.mUIContext)) {
                // reset the timeout
                mResetUIInactivityTimerHandler.processRequest(0, mUIContext);
                if (mTimeout == 0) {
                    // try to get the non-static ui attributes
                    Message message = mGetConfigurableUIAttributesHandler.processRequest(0, null);
                    // success??
                    if (message.arg1 == OXPdDevice.REQUEST_RETURN_CODE__OK) {
                        //noinspection unchecked
                        Map<String,String> attributes = (Map<String,String>)message.obj;
                        // load inactivity timeout value
                        String timeoutValue = (attributes != null) ? attributes.get(UIAttributes.UI_ATTRIBUTE__UI_TIMEOUT) : null;
                        if (!TextUtils.isEmpty(timeoutValue)) {
                            try {
                                mTimeout = Integer.valueOf(timeoutValue);
                            } catch(NumberFormatException ignored) {
                            }
                        }
                    }
                    if (mTimeout == 0) {
                        // default to the minimum support value;
                        mTimeout = UIContext.MIN_UI_INACTIVITY_TIMEOUT;
                    }
                }
                // queue the next call
                mDevice.queueDelayedCallback(mResetTimeout, (Math.max(mTimeout, UIContext.MIN_UI_INACTIVITY_TIMEOUT) - 1) * 1000);
            }
        }

        /**
         * Runnable to reset inactivity timeout
         */
        private final Runnable mResetTimeout = new Runnable() {
            @Override
            public void run() {
                resetInactivityTimer();
            }
        };
     }

    /**
     * UI Configuration SOAP request builder
     */
    private abstract class UIConfigurationSOAPRequestBuilder implements OXPdDevice.SOAPRequestBuilder {

        /**
         * Use admin credentials as part of request?
         * @return
         *              true
         */
        @Override
        public boolean useAdminCredentials() {
            return true;
        }

        /**
         * Return the UI Configuration URL
         * @return
         *              URL for request
         */
        @Override
        public URL getURL() {
            return mUIConfigurationURL;
        }

        /**
         * Return XML schemas used by ui configuration
         * @return
         *              List of XML schemas
         */
        @Override
        public List<String> getXMLSchemas() {
            return Arrays.asList(XML_SCHEMA__OXPD_UI_CONFIGURATION, OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON);
        }

        /**
         * Return service name
         * @return
         *              Service name
         */
        @Override
        public String getServiceName() {
            return SOAP_SERVICE_UI_CONFIGURATION;
        }

        /**
         * Return the service revision string
         * @return
         *              Service revision
         */
        @Override
        public String getServiceRevision() {
            return OXPD_REVISION__UI_CONFIGURATION;
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

    // VARIABLES
    /** OXPd device instance */
    private final OXPdDevice mDevice;
    /** UI Configuration service URL */
    private final URL mUIConfigurationURL;
    /** Current UI Context */
    private UIContext mUIContext = null;
    /** Does OXPdUIConfiguration support ReserveRemoteUIContext request */
    private final boolean mSupportsReserveRemoteUIContext;
    /** Does OXPdUIConfiguration support GetUIAttributes request */
    private final boolean mSupportsGetUIAttributes;
    /** Device credentials */
    private final OXPdDevice.DeviceCredentials mDeviceCredentials;
    /** Client information */
    private final OXPdDevice.ClientInfoProvider mClientInfoProvider;
    /** SleepDeprive map */
    private final HashMap<UIContext, SleepDeprive> mSleepDeprivations = new HashMap<UIContext, SleepDeprive>();

    /**
     * Handler to process {@link #ReserveUIContext(int, OXPdDevice.RequestCallback)} if ReserveRemoteUIContext is supported
     */
    private final OXPdDevice.DeviceProcessRequestCallback mReserveRemoteUIContextHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {

            final KeyPair keyPair;

            try {
                KeyPairGenerator keyGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
                keyGen.initialize(mDHParams, new SecureRandom());
                keyPair = keyGen.generateKeyPair();
            } catch(Exception error) {
                return Message.obtain(null, requestID, OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION, 0,  error);
            }

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(
                    new UIConfigurationSOAPRequestBuilder() {
                        @Override
                        public boolean useAdminCredentials() {
                            return false;
                        }

                        @Override
                        public String getSoapOperation() {
                            return SOAP_OP__RESERVE_REMOVE_UI_CONTEXT;
                        }

                        @Override
                        public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                            xmlWriter.writeStartTag(XML_SCHEMA__OXPD_UI_CONFIGURATION, getSoapOperation(), null);
                            xmlWriter.writeStartTag(XML_SCHEMA__OXPD_UI_CONFIGURATION, XML_TAG__UI_CONFIGURATION__SIGNED_MESSAGE, null);

                            StringBuilder builder = new StringBuilder();
                            try {
                                JSONObject header = new JSONObject()
                                    .put(JWT_HEADER__TYPE, JWT_HEADER_VALUE__TYPE)
                                    .put(JWT_HEADER__ALGORITHM, JWT_HEADER_VALUE__ALGORITHM);
                                builder.append(Base64.encodeToString(header.toString().getBytes(HttpRequest.DEFAULT_TEXT_ENCODING), Base64.NO_WRAP));
                                builder.append('.');
                                DHPublicKey publicKey = (DHPublicKey)keyPair.getPublic();
                                JSONObject payload = new JSONObject()
                                    .put(JWT_PAYLOAD__VERSION, JWT_PAYLOAD_VALUE__VERSION)
                                    .put(JWT_PAYLOAD__PACKAGE_NAME, mClientInfoProvider.getPackageName())
                                    .put(JWT_PAYLOAD__ID, mClientInfoProvider.getOXPdID())
                                    .put(JWT_PAYLOAD__ISSUED_AT, (System.currentTimeMillis() / 1000))
                                    .put(JWT_PAYLOAD__USERNAME, mDeviceCredentials.getUsername())
                                    .put(JWT_PAYLOAD__PASSWORD, mDeviceCredentials.getPassword())
                                    .put(JWT_PAYLOAD__PUBLIC_KEY, Base64.encodeToString(publicKey.getY().toByteArray(), Base64.NO_WRAP));
                                builder.append(Base64.encodeToString(payload.toString().getBytes(HttpRequest.DEFAULT_TEXT_ENCODING), Base64.NO_WRAP));

                                byte[] signedData = mDevice.getExternalSigner().signData(Signature.getInstance("SHA256withRSA"), builder.toString().getBytes(HttpRequest.DEFAULT_TEXT_ENCODING));
                                builder.append('.');
                                builder.append(Base64.encodeToString(signedData, Base64.NO_WRAP));
                            } catch (Exception ignored) {
                            }
                            xmlWriter.writeTag(XML_SCHEMA__OXPD_UI_CONFIGURATION, XML_TAG__UI_CONFIGURATION__JWT, null, "%s", builder.toString());
                            xmlWriter.writeEndTag(XML_SCHEMA__OXPD_UI_CONFIGURATION, XML_TAG__UI_CONFIGURATION__SIGNED_MESSAGE);
                            xmlWriter.writeEndTag(XML_SCHEMA__OXPD_UI_CONFIGURATION, getSoapOperation());
                        }
                    }
            );

            int result;
            Object data;
            try {
                data = UIContext.parseRequestResult(keyPair, mDevice, requestResponse, checkHTTPResponse(requestResponse));
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
                mUIContext = (UIContext)data;
            } catch(Error ignored) {
                data = ignored;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            }
            return Message.obtain(null, requestID, result, 0,  data);
        }
    };

    /**
     * Handler to process {@link #ReserveUIContext(int, OXPdDevice.RequestCallback)} and determine
     * which variant to call
     */
    private final OXPdDevice.DeviceProcessRequestCallback mReserveUIContextWrapperHandler = new OXPdDevice.DeviceProcessRequestCallback() {

        @Override
        public Message processRequest(int requestID, Object requestParams) {
            UIContext preconfiguredContext = UIContext.loadPreconfigured(mDevice.getContext());
            if (preconfiguredContext != null) {
                mUIContext = preconfiguredContext;
                return Message.obtain(null, requestID, OXPdDevice.REQUEST_RETURN_CODE__OK, 0, preconfiguredContext);
            } else {
                return mSupportsReserveRemoteUIContext ? mReserveRemoteUIContextHandler.processRequest(requestID, requestParams) : mReserveUIContextHandler.processRequest(requestID, requestParams);
            }
        }
    };

    /**
     * Handler to process {@link #ReserveUIContext(int, OXPdDevice.RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mReserveUIContextHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(
                    new UIConfigurationSOAPRequestBuilder() {
                        @Override
                        public boolean useAdminCredentials() {
                            return !mSupportsReserveRemoteUIContext;
                        }

                        @Override
                        public String getSoapOperation() {
                            return SOAP_OP__RESERVE_UI_CONTEXT;
                        }

                        @Override
                        public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                            xmlWriter.writeStartTag(XML_SCHEMA__OXPD_UI_CONFIGURATION, getSoapOperation(), null);

                            xmlWriter.writeStartTag(XML_SCHEMA__OXPD_UI_CONFIGURATION, XML_TAG__UI_CONFIGURATION__BROWSER_TARGET, null);
                            xmlWriter.writeStartTag(XML_SCHEMA__OXPD_UI_CONFIGURATION, XML_TAG__UI_CONFIGURATION__WEB_APPLICATION, null);

                            xmlWriter.writeTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, OXPdDevice.Constants.XML_TAG__COMMON__URI, null, "%s", mDevice.getInUseUrl());
                            xmlWriter.writeTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, OXPdDevice.Constants.XML_TAG__COMMON__BINDING, null, "%s", OXPdDevice.Constants.XML_VALUE__COMMON_BINDING__PLAIN);

                            xmlWriter.writeEndTag(XML_SCHEMA__OXPD_UI_CONFIGURATION, XML_TAG__UI_CONFIGURATION__WEB_APPLICATION);
                            xmlWriter.writeEndTag(XML_SCHEMA__OXPD_UI_CONFIGURATION, XML_TAG__UI_CONFIGURATION__BROWSER_TARGET);
                            xmlWriter.writeEndTag(XML_SCHEMA__OXPD_UI_CONFIGURATION, getSoapOperation());
                        }
                    }
            );

            int result;
            Object data;
            try {
                data = UIContext.parseRequestResult(null, mDevice, requestResponse, checkHTTPResponse(requestResponse));
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
                mUIContext = (UIContext)data;
            } catch(Error ignored) {
                data = ignored;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            }
            return Message.obtain(null, requestID, result, 0,  data);
        }
    };

    /**
     * Handler to process {@link #ReleaseUIContext(UIContext, int, OXPdDevice.RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mReleaseUIContextHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {

            if (requestParams == null) {
                return Message.obtain(null, requestID, OXPdDevice.REQUEST_RETURN_CODE__INVALID_PARAMETERS, 0, null);
            } else if (requestParams.equals(mUIContext)) {
                int result;
                Object data = null;
                if (!mUIContext.mPreconfiguredContext) {
                    HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new UIConfigurationSOAPRequestBuilder() {
                        @Override
                        public boolean useAdminCredentials() {
                            return !mSupportsReserveRemoteUIContext;
                        }

                        @Override
                        public List<String> getXMLSchemas() {
                            return Collections.singletonList(XML_SCHEMA__OXPD_UI_CONFIGURATION);
                        }

                        @Override
                        public String getSoapOperation() {
                            return SOAP_OP__RELEASE_UI_CONTEXT;
                        }

                        @Override
                        public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                            UIContext uiContext = (UIContext) requestParams;

                            xmlWriter.writeStartTag(XML_SCHEMA__OXPD_UI_CONFIGURATION, getSoapOperation(), null);
                            xmlWriter.writeTag(XML_SCHEMA__OXPD_UI_CONFIGURATION, XML_TAG__UI_CONFIGURATION__UI_CONTEXT_ID, null, "%s",
                                    uiContext.getUIContext());
                            xmlWriter.writeEndTag(XML_SCHEMA__OXPD_UI_CONFIGURATION, getSoapOperation());
                        }
                    });

                    try {
                        RestXMLTagHandler tagHandler = checkHTTPResponse(requestResponse);
                        mDevice.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);
                        mUIContext = null;
                        faultExceptionCheck(tagHandler);
                        result = OXPdDevice.REQUEST_RETURN_CODE__OK;
                    } catch (Error ignored) {
                        data = ignored;
                        result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
                    }
                } else {
                    mUIContext = null;
                    data = null;
                    result = OXPdDevice.REQUEST_RETURN_CODE__OK;
                }
                return Message.obtain(null, requestID, result, 0, data);
            } else {
                return Message.obtain(null, requestID, OXPdDevice.REQUEST_RETURN_CODE__OK, 0, null);
            }
        }
    };

    /**
     * Handler to process {@link #ReleaseUIContext(UIContext, int, OXPdDevice.RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mResetUIInactivityTimerHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {

            if (requestParams == null) {
                return Message.obtain(null, requestID, OXPdDevice.REQUEST_RETURN_CODE__INVALID_PARAMETERS, 0, null);
            } else if (requestParams.equals(mUIContext)) {

                if (!mSupportsReserveRemoteUIContext) {
                    return Message.obtain(null, requestID, OXPdDevice.REQUEST_RETURN_CODE__NOT_SUPPORTED, 0, null);
                }

                HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new UIConfigurationSOAPRequestBuilder() {
                    @Override
                    public boolean useAdminCredentials() {
                        return false;
                    }

                    @Override
                    public List<String> getXMLSchemas() {
                        return Collections.singletonList(XML_SCHEMA__OXPD_UI_CONFIGURATION);
                    }

                    @Override
                    public String getSoapOperation() {
                        return SOAP_OP__RESET_UI_INACTIVITY_TIMER;
                    }

                    @Override
                    public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                        UIContext uiContext = (UIContext) requestParams;

                        xmlWriter.writeStartTag(XML_SCHEMA__OXPD_UI_CONFIGURATION, getSoapOperation(), null);
                        xmlWriter.writeTag(XML_SCHEMA__OXPD_UI_CONFIGURATION, XML_TAG__UI_CONFIGURATION__UI_CONTEXT_ID, null, "%s",
                                uiContext.getUIContext());
                        xmlWriter.writeEndTag(XML_SCHEMA__OXPD_UI_CONFIGURATION, getSoapOperation());
                    }
                });

                int result;
                Object data = null;
                try {
                    RestXMLTagHandler tagHandler = checkHTTPResponse(requestResponse);
                    mDevice.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);
                    mUIContext = null;
                    faultExceptionCheck(tagHandler);
                    result = OXPdDevice.REQUEST_RETURN_CODE__OK;
                } catch (Error ignored) {
                    data = ignored;
                    result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
                }
                return Message.obtain(null, requestID, result, 0, data);
            } else {
                return Message.obtain(null, requestID, OXPdDevice.REQUEST_RETURN_CODE__OK, 0, null);
            }
        }
    };


    /**
     * Handler to process {@link #GetCurrentUIContext(int, OXPdDevice.RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mGetCurrentUIContextHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {
            return Message.obtain(null, requestID, OXPdDevice.REQUEST_RETURN_CODE__OK, 0,  mUIContext);
        }
    };

    /**
     * Handler to process {@link #GetTopLevelButtonRecords(int, OXPdDevice.RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mGetTopLevelButtonRecordsHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new UIConfigurationSOAPRequestBuilder() {
                @Override
                public List<String> getXMLSchemas() {
                    return Collections.singletonList(XML_SCHEMA__OXPD_UI_CONFIGURATION);
                }

                @Override
                public String getSoapOperation() {
                    return SOAP_OP__GET_TOP_LEVEL_BUTTON_RECORDS;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    xmlWriter.writeStartTag(XML_SCHEMA__OXPD_UI_CONFIGURATION, getSoapOperation(), null);
                    xmlWriter.writeEndTag(XML_SCHEMA__OXPD_UI_CONFIGURATION, getSoapOperation());
                }
            });

            int result;
            Object data;
            try {
                RestXMLTagHandler tagHandler = checkHTTPResponse(requestResponse);
                data = TopLevelButtonRecord.parseGetTopLevelButtonRecords(mDevice, requestResponse, tagHandler);
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Error ignored) {
                data = ignored;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            }
            return Message.obtain(null, requestID, result, 0, data);
        }
    };

    /**
     * Handler to process {@link #GetCurrentLanguage(int, OXPdDevice.RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mGetCurrentLanguageHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new UIConfigurationSOAPRequestBuilder() {
                @Override
                public boolean useAdminCredentials() {
                    return false;
                }

                @Override
                public List<String> getXMLSchemas() {
                    return Collections.singletonList(XML_SCHEMA__OXPD_UI_CONFIGURATION);
                }

                @Override
                public String getSoapOperation() {
                    return SOAP_OP__GET_CURRENT_LANGUAGE;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    xmlWriter.writeStartTag(XML_SCHEMA__OXPD_UI_CONFIGURATION, getSoapOperation(), null);
                    xmlWriter.writeEndTag(XML_SCHEMA__OXPD_UI_CONFIGURATION, getSoapOperation());
                }
            });

            int result;
            Object data;
            try {
                RestXMLTagHandler tagHandler = checkHTTPResponse(requestResponse);
                data = CurrentLanguage.parseCurrentLanguageResponse(mDevice, requestResponse, tagHandler);
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Error ignored) {
                data = ignored;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            }
            return Message.obtain(null, requestID, result, 0, data);
        }
    };

    /**
     * Handler to process {@link #GetTopLevelButtonRecord(UUID, int, OXPdDevice.RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mGetTopLevelButtonRecordHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {

            if (!(requestParams instanceof UUID)) {
                return Message.obtain(null, requestID, OXPdDevice.REQUEST_RETURN_CODE__INVALID_PARAMETERS, 0, null);
            }
            final UUID buttonUUID = (UUID)requestParams;

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new UIConfigurationSOAPRequestBuilder() {
                @Override
                public List<String> getXMLSchemas() {
                    return Collections.singletonList(XML_SCHEMA__OXPD_UI_CONFIGURATION);
                }

                @Override
                public String getSoapOperation() {
                    return SOAP_OP__GET_TOP_LEVEL_BUTTON_RECORD;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    xmlWriter.writeStartTag(XML_SCHEMA__OXPD_UI_CONFIGURATION, getSoapOperation(), null);
                    xmlWriter.writeTag(XML_SCHEMA__OXPD_UI_CONFIGURATION, XML_TAG__UI_CONFIGURATION__BUTTON_ID, null, "%s", buttonUUID.toString());
                    xmlWriter.writeEndTag(XML_SCHEMA__OXPD_UI_CONFIGURATION, getSoapOperation());
                }
            });

            int result;
            Object data;
            try {
                RestXMLTagHandler tagHandler = checkHTTPResponse(requestResponse);
                List<TopLevelButtonRecord> records = TopLevelButtonRecord.parseGetTopLevelButtonRecords(mDevice, requestResponse, tagHandler);
                data = (!records.isEmpty() ? records.get(0) : null);
                result = (data != null) ? OXPdDevice.REQUEST_RETURN_CODE__OK : OXPdDevice.REQUEST_RETURN_CODE__DATA_NOT_FOUND;
            } catch (Error ignored) {
                data = ignored;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            }
            return Message.obtain(null, requestID, result, 0, data);
        }
    };

    /**
     * Handler to process {@link #RegisterTopLevelButtonRecord(TopLevelButtonRecord, int, OXPdDevice.RequestCallback)}
     */
    private final OXPdDevice.DeviceProcessRequestCallback mRegisterTopLevelButtonHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, Object requestParams) {
            if (!(requestParams instanceof TopLevelButtonRecord)) {
                return Message.obtain(null, requestID, OXPdDevice.REQUEST_RETURN_CODE__INVALID_PARAMETERS, 0, null);
            }
            final TopLevelButtonRecord buttonRecord = (TopLevelButtonRecord)requestParams;

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new UIConfigurationSOAPRequestBuilder() {

                @Override
                public String getSoapOperation() {
                    return SOAP_OP__REGISTER_TOP_LEVEL_BUTTON;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    xmlWriter.writeStartTag(XML_SCHEMA__OXPD_UI_CONFIGURATION, getSoapOperation(), null);
                    TopLevelButtonRecord.writeToXML(buttonRecord, xmlWriter);
                    xmlWriter.writeEndTag(XML_SCHEMA__OXPD_UI_CONFIGURATION, getSoapOperation());
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
            }
            return Message.obtain(null, requestID, result, 0, data);
        }
    };

    /**
     * Handler to process {@link #UnregisterTopLevelButtonRecord(UUID, int, OXPdDevice.RequestCallback)} requests
     */
    private final OXPdDevice.DeviceProcessRequestCallback mUnregisterTopLevelButtonHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {

            if (!(requestParams instanceof UUID)) {
                return Message.obtain(null, requestID, OXPdDevice.REQUEST_RETURN_CODE__INVALID_PARAMETERS, 0, null);
            }
            final UUID buttonUUID = (UUID)requestParams;

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new UIConfigurationSOAPRequestBuilder() {
                @Override
                public List<String> getXMLSchemas() {
                    return Collections.singletonList(XML_SCHEMA__OXPD_UI_CONFIGURATION);
                }

                @Override
                public String getSoapOperation() {
                    return SOAP_OP__UNREGISTER_TOP_LEVEL_BUTTON;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    xmlWriter.writeStartTag(XML_SCHEMA__OXPD_UI_CONFIGURATION, getSoapOperation(), null);
                    xmlWriter.writeTag(XML_SCHEMA__OXPD_UI_CONFIGURATION, XML_TAG__UI_CONFIGURATION__BUTTON_ID, null, "%s", buttonUUID.toString());
                    xmlWriter.writeEndTag(XML_SCHEMA__OXPD_UI_CONFIGURATION, getSoapOperation());
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
            }
            return Message.obtain(null, requestID, result, 0, data);
        }
    };

    /**
     * Handler to process {@link #GetUIAttributes(int, OXPdDevice.RequestCallback)} requests
     */
    private final OXPdDevice.DeviceProcessRequestCallback mGetUIAttributesHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new UIConfigurationSOAPRequestBuilder() {
                @Override
                public List<String> getXMLSchemas() {
                    return Collections.singletonList(XML_SCHEMA__OXPD_UI_CONFIGURATION);
                }

                @Override
                public String getSoapOperation() {
                    return mSupportsGetUIAttributes ? SOAP_OP__GET_UI_ATTRIBUTES : SOAP_OP__GET_UI_PROFILE;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    xmlWriter.writeStartTag(XML_SCHEMA__OXPD_UI_CONFIGURATION, getSoapOperation(), null);
                    xmlWriter.writeEndTag(XML_SCHEMA__OXPD_UI_CONFIGURATION, getSoapOperation());
                }
            });

            int result;
            Object data;
            try {
                RestXMLTagHandler tagHandler = checkHTTPResponse(requestResponse);
                data = UIAttributes.parseUIAttributes(mDevice, requestResponse, tagHandler);
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Error ignored) {
                data = ignored;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            }
            return Message.obtain(null, requestID, result, 0, data);
        }
    };

    /**
     * Handler to process {@link #GetConfigurableUIAttributes(int, OXPdDevice.RequestCallback)}  requests
     */
    private final OXPdDevice.DeviceProcessRequestCallback mGetConfigurableUIAttributesHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new UIConfigurationSOAPRequestBuilder() {
                @Override
                public List<String> getXMLSchemas() {
                    return Collections.singletonList(XML_SCHEMA__OXPD_UI_CONFIGURATION);
                }

                @Override
                public String getSoapOperation() {
                    return SOAP_OP__GET_CONFIGURABLE_UI_ATTRIBUTES;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    xmlWriter.writeStartTag(XML_SCHEMA__OXPD_UI_CONFIGURATION, getSoapOperation(), null);
                    xmlWriter.writeEndTag(XML_SCHEMA__OXPD_UI_CONFIGURATION, getSoapOperation());
                }
            });

            int result;
            Object data;
            try {
                RestXMLTagHandler tagHandler = checkHTTPResponse(requestResponse);
                data = UIAttributes.parseUIAttributes(mDevice, requestResponse, tagHandler);
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Error ignored) {
                data = ignored;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            }
            return Message.obtain(null, requestID, result, 0, data);
        }
    };

     /**
     * Handler to process {@link #acquireWakeLock(UIContext, int, OXPdDevice.RequestCallback)} requests
     */
    private final OXPdDevice.DeviceProcessRequestCallback mAcquireWakeLockHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {
            UIContext uiContext = (UIContext)requestParams;
            if (uiContext == null) {
                // invalid UI context
                return Message.obtain(null, requestID, OXPdDevice.REQUEST_RETURN_CODE__INVALID_PARAMETERS, 0, null);
            }
            if (!uiContext.equals(mUIContext)) {
                // old UI context?
                return Message.obtain(null, requestID, OXPdDevice.REQUEST_RETURN_CODE__DATA_NOT_FOUND, 0, null);
            }

            // get wake lock
            SleepDeprive keepAwake = mSleepDeprivations.get(uiContext);
            if (keepAwake == null) {
                // make a new one and store it
                keepAwake = mSleepDeprivations.put(uiContext, new SleepDeprive(uiContext));

            }
            // acquire wake lock
            keepAwake.acquire();
            return Message.obtain(null, requestID, OXPdDevice.REQUEST_RETURN_CODE__OK, 0, null);
        }
    };

    /**
     * Handler to process {@link #releaseWakeLock(UIContext, int, OXPdDevice.RequestCallback)} requests
     */
    private final OXPdDevice.DeviceProcessRequestCallback mReleaseWakeLockHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {
            UIContext uiContext = (UIContext)requestParams;
            // find wake lock
            SleepDeprive keepAwake = mSleepDeprivations.get(uiContext);
            if (keepAwake != null) {
                // release it
                keepAwake.release();
                // still active?
                if (!keepAwake.isHeld()) {
                    // remove since no longer active
                    mSleepDeprivations.remove(uiContext);
                }
            }
            // success! always success!
            return Message.obtain(null, requestID, OXPdDevice.REQUEST_RETURN_CODE__OK, 0, null);
        }
    };

    /**
     * Handler to process {@link #GetBrowserConfiguration(int, OXPdDevice.RequestCallback)} requests
     */
    private final OXPdDevice.DeviceProcessRequestCallback mGetBrowserConfigurationHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new UIConfigurationSOAPRequestBuilder() {
                @Override
                public List<String> getXMLSchemas() {
                    return Collections.singletonList(XML_SCHEMA__OXPD_UI_CONFIGURATION);
                }

                @Override
                public String getSoapOperation() {
                    return SOAP_OP__GET_BROWSER_CONFIGURATION;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    xmlWriter.writeStartTag(XML_SCHEMA__OXPD_UI_CONFIGURATION, getSoapOperation(), null);
                    xmlWriter.writeEndTag(XML_SCHEMA__OXPD_UI_CONFIGURATION, getSoapOperation());
                }
            });

            int result;
            Object data;
            try {
                RestXMLTagHandler tagHandler = checkHTTPResponse(requestResponse);
                data = BrowserConfiguration.parseXMLResponse(mDevice, requestResponse, tagHandler);
                result = OXPdDevice.REQUEST_RETURN_CODE__OK;
            } catch (Error ignored) {
                data = ignored;
                result = OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION;
            }
            return Message.obtain(null, requestID, result, 0, data);
        }
    };

    /**
     * Handler to process {@link #SetBrowserConfiguration(BrowserConfiguration, int, OXPdDevice.RequestCallback)} requests
     */
    private final OXPdDevice.DeviceProcessRequestCallback mSetBrowserConfigurationHandler = new OXPdDevice.DeviceProcessRequestCallback() {
        @Override
        public Message processRequest(int requestID, final Object requestParams) {
            if (!(requestParams instanceof BrowserConfiguration)) {
                return Message.obtain(null, requestID, OXPdDevice.REQUEST_RETURN_CODE__INVALID_PARAMETERS, 0, null);
            }
            final BrowserConfiguration browserConfiguration = (BrowserConfiguration)requestParams;

            HttpRequestResponseContainer requestResponse = mDevice.performSOAPRequest(new UIConfigurationSOAPRequestBuilder() {
                @Override
                public List<String> getXMLSchemas() {
                    return Collections.singletonList(XML_SCHEMA__OXPD_UI_CONFIGURATION);
                }

                @Override
                public String getSoapOperation() {
                    return SOAP_OP__SET_BROWSER_CONFIGURATION;
                }

                @Override
                public void fillSoapOperationBody(RestXMLWriter xmlWriter) {
                    xmlWriter.writeStartTag(XML_SCHEMA__OXPD_UI_CONFIGURATION, getSoapOperation(), null);
                    BrowserConfiguration.writeToXML(browserConfiguration, xmlWriter);
                    xmlWriter.writeEndTag(XML_SCHEMA__OXPD_UI_CONFIGURATION, getSoapOperation());
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
            }
            return Message.obtain(null, requestID, result, 0, data);
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
     *              when something goes wrong
     * @hide
     */
    public OXPdUIConfiguration(OXPdDevice device, OXPdDevice.ClientInfoProvider clientInfoProvider, OXPdDevice.DeviceCredentials deviceCredentials, DiscoveryTree discoveryTree) throws Error {
        mDevice = device;
        mClientInfoProvider = clientInfoProvider;
        mDeviceCredentials = deviceCredentials;

        if (discoveryTree == null) throw new Error(ErrorName.AjaxError, "Connection failed");

        DiscoveredFeature feature =
                discoveryTree.GetDiscoveredFeatureByResourceTypeAndRevision(
                        OXPD_RESOURCE_TYPE__UI_CONFIGURATION,
                        OXPD_REVISION__UI_CONFIGURATION);
        if (feature == null) {
            throw new Error(ErrorName.ServiceNotFound, "OXPd:UIConfiguration is not supported on the target device");
        }

        //mSupportsReserveRemoteUIContext = device.versionRequirementsSatisfied(feature.minorRevision, RESERVE_REMOTE_UI_CONTEXT__MINOR_VERSION__MIN);
        boolean legacyMode = PreferenceManager.getDefaultSharedPreferences(mDevice.getContext().getApplicationContext())
                .getBoolean(mDevice.getContext().getPackageName() + "#oxpd_legacy_mode", false);
        mSupportsReserveRemoteUIContext = device.versionRequirementsSatisfied(feature.minorRevision, RESERVE_REMOTE_UI_CONTEXT__MINOR_VERSION__MIN);
        mSupportsGetUIAttributes = device.versionRequirementsSatisfied(feature.minorRevision, GET_UI_ATTRIBUTES__MINOR_VERSION__MIN);

        if (!mSupportsReserveRemoteUIContext && !legacyMode) {
            throw new Error(ErrorName.UnsupportedVersion, "OXPd:UIConfiguration unsupported version");
        }
        try {
            mUIConfigurationURL = device.getOXPdUrl(feature.resourceURI);
        } catch (MalformedURLException e) {
            throw new Error(ErrorName.Unknown, "invalid URL");
        }

        mDevice.getXMLNSHandler().addXMLNS(XML_PREFIX_OXPD_UI_CONFIGURATION, OXPD_REVISION__UI_CONFIGURATION);
        mDevice.getXMLNSHandler().addXMLNS(OXPdDevice.Constants.XML_PREFIX_OXPD_COMMON, OXPD_REVISION__COMMON);
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
     * Reserves the target device's UI
     * @param requestID
     *              Request id associated with this call
     * @param callback
     *              Callback to invoke when operation is finished
     */
    public void ReserveUIContext(int requestID, OXPdDevice.RequestCallback callback) {
        boolean remote = mSupportsReserveRemoteUIContext && !OXPdDevice.isPanel();

        // if we use local "device in use" page - check server status
        if (!remote && !mDevice.checkServerStatus()) {
            if (callback != null) {
                callback.requestResult(mDevice, Message.obtain(null, requestID, OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION, 0,
                        new Error(ErrorName.Unknown, "Internal server is failed to start")));
            }
        } else {
            mDevice.queueRequest(mReserveUIContextWrapperHandler, null, requestID, callback);
        }
    }

    /**
     * Release the target device's UI
     * @param uiContext
     *              Current UI Context
     * @param requestID
     *              Request id associated with this call
     * @param callback
     *              Callback to invoke when operation is finished
     */
    public void ReleaseUIContext(UIContext uiContext, int requestID, OXPdDevice.RequestCallback callback) {
        mDevice.queueRequest(mReleaseUIContextHandler, uiContext, requestID, callback);
    }

    /**
     * Return the current UI context
     * @param requestID
     *              Request id associated with this call
     * @param callback
     *              Callback to invoke when operation is finished
     */
    public void GetCurrentUIContext(int requestID, OXPdDevice.RequestCallback callback) {
        mDevice.queueRequest(mGetCurrentUIContextHandler, null, requestID, callback);
    }

    /**
     * Return the list of currently configured buttons
     * @param requestID
     *              Request id associated with this call
     * @param callback
     *              Callback to invoke when operation is finished
     */
    @SuppressWarnings("unused")
    public void GetTopLevelButtonRecords(int requestID, OXPdDevice.RequestCallback callback) {
        mDevice.queueRequest(mGetTopLevelButtonRecordsHandler, null, requestID, callback);
    }

    /**
     * Reset the device UI Inactivity Timer
     * @param uiContext
     *              Current UI Context
     * @param requestID
     *              Request id associated with this call
     * @param callback
     *              Callback to invoke when operation is finished
     */
    @SuppressWarnings("unused")
    public void ResetUIInactivityTimer(UIContext uiContext, int requestID, OXPdDevice.RequestCallback callback) {
        mDevice.queueRequest(mResetUIInactivityTimerHandler, uiContext, requestID, callback);
    }

    /**
     * Return the list of currently configured buttons
     * @param buttonUUID
     *              UUID of button to query
     * @param requestID
     *              Request id associated with this call
     * @param callback
     *              Callback to invoke when operation is finished
     */
    @SuppressWarnings("unused")
    public void GetTopLevelButtonRecord(UUID buttonUUID, int requestID, OXPdDevice.RequestCallback callback) {
        mDevice.queueRequest(mGetTopLevelButtonRecordHandler, buttonUUID, requestID, callback);
    }

    /**
     * Register a button with OMNI
     * @param buttonRecord
     *              Button to register
     * @param requestID
     *              Request id associated with this call
     * @param callback
     *              Callback to invoke when operation is finished
     */
    @SuppressWarnings("unused")
    public void RegisterTopLevelButtonRecord(TopLevelButtonRecord buttonRecord, int requestID, OXPdDevice.RequestCallback callback) {
        mDevice.queueRequest(mRegisterTopLevelButtonHandler, buttonRecord, requestID, callback);
    }

    /**
     * Unregister the specified button
     * @param buttonUUID
     *              UUID of button to query
     * @param requestID
     *              Request id associated with this call
     * @param callback
     *              Callback to invoke when operation is finished
     */
    @SuppressWarnings("unused")
    public void UnregisterTopLevelButtonRecord(UUID buttonUUID, int requestID, OXPdDevice.RequestCallback callback) {
        mDevice.queueRequest(mUnregisterTopLevelButtonHandler, buttonUUID, requestID, callback);
    }

    /**
     * Obtains a set of UI related attributes
     * @param requestID
     *              Request id associated with this call
     * @param callback
     *              Callback to invoke when operation is finished
     */
    @SuppressWarnings("unused")
    public void GetUIAttributes(int requestID, OXPdDevice.RequestCallback callback) {
        mDevice.queueRequest(mGetUIAttributesHandler, null, requestID, callback);
    }

    /**
     * Obtains a set of UI related attributes that can change at any time
     * @param requestID
     *              Request id associated with this call
     * @param callback
     *              Callback to invoke when operation is finished
     */
    @SuppressWarnings("unused")
    public void GetConfigurableUIAttributes(int requestID, OXPdDevice.RequestCallback callback) {
        mDevice.queueRequest(mGetConfigurableUIAttributesHandler, null, requestID, callback);
    }

    /**
     * Prevent the UI inactivity timer from going off
     * @param uiContext
     *              Current UI Context
     * @param requestID
     *              Request id associated with this call
     * @param callback
     *              Callback to invoke when operation is finished
     */
    @SuppressWarnings("unused")
    public void acquireWakeLock(UIContext uiContext, int requestID, OXPdDevice.RequestCallback callback) {
        mDevice.queueRequest(mAcquireWakeLockHandler, uiContext, requestID, callback);
    }

    /**
     * Allow the UI inactivity timer to off if wake lock is no longer held
     * @param uiContext
     *              Current UI Context
     * @param requestID
     *              Request id associated with this call
     * @param callback
     *              Callback to invoke when operation is finished
     */
    @SuppressWarnings("unused")
    public void releaseWakeLock(UIContext uiContext, int requestID, OXPdDevice.RequestCallback callback) {
        mDevice.queueRequest(mReleaseWakeLockHandler, uiContext, requestID, callback);
    }

    /**
     * Obtains a browser configuration
     * @param requestID
     *              Request id associated with this call
     * @param callback
     *              Callback to invoke when operation is finished
     */
    @SuppressWarnings("unused")
    public void GetBrowserConfiguration(int requestID, OXPdDevice.RequestCallback callback) {
        mDevice.queueRequest(mGetBrowserConfigurationHandler, null, requestID, callback);
    }

    /**
     * Updates a browser configuration
     * @param requestID
     *              Request id associated with this call
     * @param callback
     *              Callback to invoke when operation is finished
     */
    @SuppressWarnings("unused")
    public void SetBrowserConfiguration(BrowserConfiguration browserConfiguration, int requestID, OXPdDevice.RequestCallback callback) {
        mDevice.queueRequest(mSetBrowserConfigurationHandler, browserConfiguration, requestID, callback);
    }

    /**
     * Return the current language
     * @param requestID
     *              Request id associated with this call
     * @param callback
     *              Callback to invoke when operation is finished
     */
    @SuppressWarnings("unused")
    public void GetCurrentLanguage(int requestID, OXPdDevice.RequestCallback callback) {
        mDevice.queueRequest(mGetCurrentLanguageHandler, null, requestID, callback);
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
}
