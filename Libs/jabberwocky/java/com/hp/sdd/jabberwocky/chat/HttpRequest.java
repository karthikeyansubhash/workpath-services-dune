// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.jabberwocky.chat;

import android.annotation.SuppressLint;
import android.net.SSLCertificateSocketFactory;
import android.os.Build;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

/**
 * HTTP request container
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public final class HttpRequest {
    /**
     * Maximum number of redirects to follow
     */
    public static final int MAX_REDIRECT_COUNT = 5;
    /**
     * Content type header name
     */
    public static final String HEADER__CONTENT_TYPE = "Content-Type";
    /**
     * Content length header name
     */
    private static final String HEADER__CONTENT_LENGTH = "Content-Length";
    /**
     * Transfer encoding header
     */
    private static final String HEADER__TRANSFER_ENCODING = "Transfer-Encoding";
    /**
     * Transfer encoding chunked
     */
    private static final String HEADER__TRANSFER_ENCODING_CHUNKED = "chunked";
    /** Default connection timeout */
    public static final int CONNECTION_TIMEOUT = 15000;
    /** Default socket timeout */
    public static final int SOCKET_TIMEOUT = 15000;
    /** Default encoding */
    public static final String DEFAULT_TEXT_ENCODING = "UTF-8";
    /**
     * Date header name
     */
    public static final String HEADER__DATE = "Date";

    /**
     * Connection header name
     */
    public static final String HEADER__CONNECTION = "Connection";
    /**
     *  Connect header value
     */
    public static final String HEADER_VALUE__CONNECTION = "close";

    /**
     * Default hostname verifier
     */
    public static final HostnameVerifier HOSTNAME_VERIFIER__ACCEPT_ALL = new HostnameVerifier() {
        @SuppressLint("BadHostnameVerifier")
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * Create a properly formatted HTTP date header
     * @return
     *              RFC-1123 formatted date header
     */
    public static HttpHeader createDateHeader() {
        DateFormat rfc1123 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
        rfc1123.setTimeZone(TimeZone.getTimeZone("UTC"));
        return HttpHeader.create(HEADER__DATE, rfc1123.format(new Date(System.currentTimeMillis())));
    }

    /**
     * HTTP request methods
     */
    public enum HTTPRequestType {
        /** HTTP Get */
        GET("GET"),
        /** HTTP Post */
        POST("POST"),
        /** HTTP Put */
        PUT("PUT"),
        /** HTTP Delete */
        DELETE("DELETE"),
        /** HTTP Options */
        OPTIONS("OPTIONS"),
        /** HTTP Head */
        HEAD("HEAD"),
        /** HTTP Trace */
        TRACE("TRACE"),
        ;
        /**
         * HTTP method name
         */
        public final String mHttpMethod;

        /**
         * Constructor
         * @param methodName
         *              HTTP method name
         */
        HTTPRequestType(String methodName) {
            mHttpMethod = methodName;
        }

        /**
         * Return the HTTP method name
         * @return
         *              HTTP method name
         */
        public String getMethodName() {
            return mHttpMethod;
        }
    }

    /**
     * Http Request builder.
     * Keep to build subsequent redirect requests if necessary.
     */
    private final Builder mBuilder;
    /**
     * HTTP Url Connection
     */
    final HttpURLConnection mURLConnection;
    /**
     * HTTP request body
     */
    final AbstractHTTPOutput mRequestBody;

    /**
     * Return the HTTP method associated with this request
     * @return
     *              HTTP method as string
     */
    public String getMethod() {
        return mURLConnection.getRequestMethod();
    }

    /**
     * Return URI for this request
     * @return
     *              URI this request is being sent to
     */
    public URL getURI() {
        return mURLConnection.getURL();
    }

    /**
     * Is this request expecting to receive data?
     * @return
     *              true if receiving data;
     *              false otherwise
     */
    public boolean receivingInput() {
        return mURLConnection.getDoInput();
    }

    /**
     * Send data, if any, for this HTTP request
     */
    public void sendData() {
        if (!mURLConnection.getDoOutput()) return;

        OutputStream os = null;

        try {
            os = new BufferedOutputStream(mURLConnection.getOutputStream());
            mRequestBody.send(os);
        } catch (IOException ignored) {
        } finally {
            try {
                if (os != null) os.close();
            } catch(IOException ignored) {
            }
        }
    }

    /**
     * Is this request sending data?
     * @return
     *              true if sending data;
     *              false otherwise
     */
    public boolean sendingOutput() {
        return mURLConnection.getDoOutput();
    }

    /**
     * Get output stream to send data with
     * @return
     *              Output stream to send data through
     * @throws IOException
     *              If an error occurs
     */
    public OutputStream getOutputStream() throws IOException {
        return (mURLConnection.getDoOutput() ? mURLConnection.getOutputStream() : null);
    }

    /**
     * Connect the HttpURLConnection
     * @throws IOException When an I/O errors occurs
     */
    public void connect() throws IOException {
        mURLConnection.connect();
    }

    /**
     * Abort the HttpURLConnection
     */
    public void abort() {
        new Thread(new Runnable() {
            public void run() {
                mURLConnection.disconnect();
            }
        }).start();
    }

    /**
     * Get the list of HttpHeader for this request
     * @return
     *              List of HttpHeaders for this request
     */
    public List<HttpHeader> getHeaders() {
        List<HttpHeader> headers = HttpUtils.getHeadersFromMap(mURLConnection.getRequestProperties());
        /*
         * HttpURLConnection automatically adds the Content-Length header when the connection
         * is established, however once the connection is established the headers can't
         * be obtained, therefore we do some additional header processing to fake it
         */
        if (mRequestBody != null) {
            long length;
            length = mRequestBody.length();
            if (length < 0) {
                headers.add(HttpHeader.create(HEADER__TRANSFER_ENCODING, HEADER__TRANSFER_ENCODING_CHUNKED));
            } else {
            headers.add(HttpHeader.create(HEADER__CONTENT_LENGTH, String.valueOf(length)));
            }
        }
        return headers;
    }


    /**
     * Get the content type of this request
     * @return
     *              Mime type of this request
     */
    public String getContentType() {
        return (mURLConnection.getDoOutput() ? mURLConnection.getRequestProperty(HEADER__CONTENT_TYPE) : null);
    }

    /**
     * Build a new HttpRequest using ths new redirect URL
     * @param redirectUrl
     *              URL for where the new request should be sent to
     * @return
     *              Copy of current HttpRequest using new URL
     * @throws IllegalStateException
     *              if too many redirect
     * @throws IllegalArgumentException
     *              if URL is empty
     * @throws IOException
     *              if an error occurs
     */
    HttpRequest rebuildForRedirect(String redirectUrl) throws IllegalStateException, IllegalArgumentException, IOException {
        return mBuilder.setRedirectURL(new URL(redirectUrl)).build();
    }

    /**
     * HttpRequest constructor and initializer
     * @param builder
     *              Builder used to create request
     * @param httpURLConnection
     *              HttpURLConnection for this request
     * @param requestOutput
     *              Request body
     */
    private HttpRequest(Builder builder,
                        HttpURLConnection httpURLConnection,
                        AbstractHTTPOutput requestOutput) {
        mBuilder = builder;
        mURLConnection = httpURLConnection;
        mRequestBody = requestOutput;
    }

    /** Builder to create HttpRequests */
    public static class Builder {

        /**
         * Constructor for HttpRequest Builder
         */
        public Builder() {
        }

        /**
         * URL where request is to be sent
         */
        private URL mURL = null;

        /**
         * Set the URL for the request
         * @param url
         *              URL where request is to be sent
         * @return
         *              This builder
         */
        public Builder setURL(URL url) {
            mURL = url;
            return this;
        }

        /**
         * HTTP method for the request.
         * Default is {@link HTTPRequestType#GET}
         */
        private HTTPRequestType mHTTPRequest = HTTPRequestType.GET;

        /**
         * Set the HTTP request type.
         * Default is {@link HTTPRequestType#GET}
         * @param method
         *              HTTP request
         * @return
         *              This builder
         */
        public Builder setRequestMethod(HTTPRequestType method) {
            mHTTPRequest = method;
            return this;
        }

        /**
         * Request content type
         */
        private String mContentType = null;

        /**
         * Set the request content type
         * @param contentType
         *              Mime type of request data
         * @return
         *              This builder
         */
        public Builder setRequestOutputContentType(String contentType) {
            mContentType = contentType;
            return this;
        }

        /**
         * Request data
         */
        private AbstractHTTPOutput mRequestOutputData = null;

        /**
         * Set the request data
         * @param data
         *              Byte data for request body
         * @return
         *              This builder
         */
        public Builder setRequestOutputData(byte[] data) {
            mRequestOutputData = AbstractHTTPOutput.wrap(data);
            return this;
        }

        /**
         * Set the file the request should read data from
         * @param data
         *              File the request should read data from
         * @return
         *              This builder
         */
        public Builder setRequestOutputData(File data) {
            mRequestOutputData = AbstractHTTPOutput.wrap(data);
            return this;
        }

        /**
         * Set the request data
         * @param data
         *              String to send as request body
         * @return
         *              This builder
         */
        public Builder setRequestOutputData(String data) {
            return setRequestOutputData(data, DEFAULT_TEXT_ENCODING);
        }

        /**
         * Set the request data
         * @param data
         *              String to send as request body
         * @param encoding
         *              Character encoding of the string data
         * @return
         *              This builder
         */
        public Builder setRequestOutputData(String data, String encoding) {
            mRequestOutputData = AbstractHTTPOutput.wrap(data, encoding);
            return this;
            }

        /**
         * Set the request data
         * @param data
         *              Data to send as request body
         * @return
         *              This builder
         */
        public Builder setRequestOutputData(AbstractHTTPOutput data) {
            mRequestOutputData = data;
            return this;
        }

        /**
         * Is the request receiving data.
         * Default is false unless {@link #setRequestMethod(HTTPRequestType)} is {@link HTTPRequestType#GET}
         */
        private boolean mRequestInputData = false;

        /**
         * Set whether or not this requests expects to receive data.
         * @param requestData
         *              Is request expecting to receive data
         * @return
         *              This builder
         */
        public Builder setRequestInputData(boolean requestData) {
            mRequestInputData = requestData;
            return this;
        }

        /**
         * Flag to disable output checks
         */
        private boolean mNoOutputData = false;

        /**
         * Disable output for requests that generally have them such as {@link HTTPRequestType#POST}
         * or {@link HTTPRequestType#PUT}
         * @param noOutput
         *              Turn off data sending
         * @return
         *              This builder
         */
        public Builder setRequestNoOutputData(boolean noOutput) {
            mNoOutputData = noOutput;
            return this;
        }

        /**
         * List of HTTP headers to send as part of this request
         */
        private LinkedHashMap<String, HttpHeader> mHeaders = new LinkedHashMap<String, HttpHeader>();

        /**
         * Add the provided HTTP header
         * @param header
         *              HTTP header to add to request
         * @return
         *              This builder
         */
        public Builder addHeader(HttpHeader header) {
            if ((header != null)
                    && !TextUtils.isEmpty(header.getName())
                    && !TextUtils.isEmpty(header.getValue())){
                mHeaders.put(header.getName(), header);
            }
            return this;
        }

        /**
         * Add an HTTP header
         * @param name
         *              HTTP header name
         * @param value
         *              HTTP header value
         * @return
         *              This builder
         */
        private Builder addHeader(String name, String value) {
            return addHeader(HttpHeader.create(name, value));
        }

        /**
         * Add the list of headers
         * @param headers
         *              HTTP headers to add to request
         * @return
         *              This builder
         */
        public Builder addHeaders(HttpHeader... headers) {
            if (headers != null) {
                for(HttpHeader header : headers) {
                    addHeader(header);
                }
            }
            return this;
        }

        /**
         * Connection timeout
         * Default is {@link #CONNECTION_TIMEOUT}
         */
        private int mConnectionTimeout = CONNECTION_TIMEOUT;

        /**
         * Set the preferred connection timeout.
         * Default is {@link #CONNECTION_TIMEOUT
         * @param timeout
         *              Timeout value
         * @return
         *              This builder
         */
        public Builder setConnectionTimeout(int timeout) {
            mConnectionTimeout = timeout;
            return this;
        }

        /**
         * Socket timeout
         * Default is {@link #SOCKET_TIMEOUT}
         */
        private int mSocketTimeout = SOCKET_TIMEOUT;

        /**
         * Set the preferred socket timeout
         * Default value is {@link #SOCKET_TIMEOUT}
         * @param timeout
         *              Socket timeout
         * @return
         *              This builder
         */
        public Builder setSocketTimeout(int timeout) {
            mSocketTimeout = timeout;
            return this;
        }

        /**
         * SSL socket factory for HTTPS requests
         */
        private SSLSocketFactory mSocketFactory = null;

        /**
         * Set the SSL socket factory for HTTPS requests
         * @param socketFactory
         *              SSL socket factory
         * @return
         *              This builder
         */
        public Builder setSSLSocketFactory(SSLSocketFactory socketFactory) {
            mSocketFactory = socketFactory;
            return this;
        }

        /**
         * Hostname verifier
         */
        private HostnameVerifier mHostNameVerifier = null;

        /**
         * Set the hostname verifier to use for this request
         * @param hostNameVerifier
         *              Hostname verifier to use
         * @return
         *              This builder
         */
        public Builder setHostNameVerifier(HostnameVerifier hostNameVerifier) {
            mHostNameVerifier = hostNameVerifier;
            return this;
        }

        /**
         * Follow redirects.
         * Default is true
         */
        private boolean mFollowRedirects = true;

        /**
         * Automatically follow redirects?
         * @param followRedirects
         *              Follow redirects?
         * @return
         *              This builder
         */
        public Builder setFollowRedirects(boolean followRedirects) {
            mFollowRedirects = followRedirects;
            return this;
        }

        /**
         * Use HTTP cache.
         * Default is false
         */
        private boolean mUsesCache = false;

        /**
         * Sets whether or not to use the HTTP cache
         * Default is false
         * @param usesCache
         *              Use cache?
         * @return
         *              This builder
         */
        public Builder setUsesCache(boolean usesCache) {
            mUsesCache = usesCache;
            return this;
        }

        /**
         * Redirect count
         */
        private int mRedirectCount = 0;

        /**
         * Set a new redirect URL
         * @param url
         *              Redirection URL
         * @return
         *              This builder
         */
        Builder setRedirectURL(URL url) {
            mRedirectCount++;
            mURL = url;
            return this;
        }

        /**
         * Build the HttpRequest
         * @return
         *              HttpRequest combining all the builder options
         * @throws IllegalStateException
         *              If too many redirects
         * @throws IllegalArgumentException
         *              If {@link #setRequestMethod(HTTPRequestType)} or {@link #setURL(URL)}
         *              have not been set
         * @throws IOException
         *              If an I/O error occurs
         */
        @SuppressLint("SSLCertificateSocketFactoryGetInsecure")
        public HttpRequest build() throws IllegalStateException, IllegalArgumentException, IOException {

            // check url
            if (mURL == null) {
                throw new IllegalArgumentException("url not set");
            }
            // check request method
            if (mHTTPRequest == null) {
                throw new IllegalArgumentException("method not set");
            }

            if (mRedirectCount >= MAX_REDIRECT_COUNT) {
                throw new IllegalStateException("dizzy from excessive redirects");
            }

            // check method specific parameters
            switch(mHTTPRequest) {
                case POST:
                case PUT:
                    if (!mNoOutputData) {
                        // check for content type
                        if (TextUtils.isEmpty(mContentType)) {
                            throw new IllegalArgumentException("content type not provided");
                        }
                        // check data was provided
                        if (mRequestOutputData == null) {
                            throw new IllegalArgumentException("no data provided for: " + mHTTPRequest.getMethodName());
                        }
                    }
                    break;
                default:
            }

            // open the connection
            URLConnection urlConnection = mURL.openConnection();
            if (!((urlConnection instanceof HttpsURLConnection) || (urlConnection instanceof HttpURLConnection))) {
                throw new IllegalArgumentException("not an http/https url");
            }

            // set SSL socket factory
            HttpURLConnection httpURLConnection = (HttpURLConnection)urlConnection;
            if (httpURLConnection instanceof HttpsURLConnection) {
                ((HttpsURLConnection)urlConnection).setSSLSocketFactory(
                        (mSocketFactory != null) ? mSocketFactory : SSLCertificateSocketFactory.getInsecure(mConnectionTimeout, null));

                ((HttpsURLConnection)urlConnection).setHostnameVerifier(
                        ((mHostNameVerifier != null) ? mHostNameVerifier: HOSTNAME_VERIFIER__ACCEPT_ALL));
            }

            // TODO: LaserJet scan/nebula multiple scan adf issue.  commenting out the following makes LaserJets work and nebula multiple page adf fail
 //           httpURLConnection.setRequestProperty(HEADER__CONNECTION, HEADER_VALUE__CONNECTION);     // Close connection to avoid recycling bug.
            httpURLConnection.setRequestMethod(mHTTPRequest.getMethodName());
            httpURLConnection.setDoInput((mHTTPRequest == HTTPRequestType.GET) || mRequestInputData);
            httpURLConnection.setDoOutput((mRequestOutputData != null));
            httpURLConnection.setConnectTimeout(mConnectionTimeout);
            httpURLConnection.setReadTimeout(mSocketTimeout);
            httpURLConnection.setUseCaches(mUsesCache);
            httpURLConnection.setInstanceFollowRedirects(mFollowRedirects);

            // add content type header if needed
            if (!TextUtils.isEmpty(mContentType)) {
                httpURLConnection.setRequestProperty(HEADER__CONTENT_TYPE, mContentType.toLowerCase(Locale.US));
            }
            // set output length
            if (mRequestOutputData != null) {
                long size = mRequestOutputData.length();
                if (size < 0) {
                    httpURLConnection.setChunkedStreamingMode(0);
                }
                else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    httpURLConnection.setFixedLengthStreamingMode((int) size);
                } else {
                    httpURLConnection.setFixedLengthStreamingMode(size);
                }
            }

            // add the remainder of the headers
            for(HttpHeader header : mHeaders.values()) {
                httpURLConnection.setRequestProperty(header.getName(), header.getValue());
            }

            return new HttpRequest(Builder.this, httpURLConnection, mRequestOutputData);
        }
    }
}
