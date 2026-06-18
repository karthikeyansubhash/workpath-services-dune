// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.jabberwocky.chat;

import android.text.TextUtils;
import android.util.Log;

import com.hp.sdd.jabberwocky.utils.Chronicler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

/**
 * HTTP utilities for processing a request and response
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class HttpUtils {

    /** Logging tag */
    private static final String TAG = "HttpUtils";
    /** HTTP character set header */
    private static final String HTTP_HEADER__CHARSET = "charset";
    /** HTTP location header */
    private static final String HTTP_HEADER__LOCATION = "Location";
    /** HTTP temporary redirect status code */
    private static final int HTTP_STATUS__TEMPORARY_REDIRECT = 307;

    /**
     * Process the provided HTTP request and get it's response
     * @param request
     *              HTTP request
     * @param tag
     *              Log tag to use
     * @param logLevel
     *              Logging level
     * @param scribe
     *              Chronicler to use
     * @return
     *              HTTP Request/Response pair
     */
    public static HttpRequestResponseContainer getHttpResponse(HttpRequest request,
                                                               String tag,
                                                               int logLevel,
                                                               Chronicler scribe) {

        if (request == null) {
            return new HttpRequestResponseContainer.Builder().build();
        }

        if (TextUtils.isEmpty(tag)) tag = TAG;
        if (scribe == null) scribe = new Chronicler();

        HttpResponse response;
        Exception requestException = null;
        int responseCode;

        try {
            /*
             * grab headers before establishing the connection otherwise
             * HttpURLConnection throws a hissy fit
             */
            List<HttpHeader> headers = request.getHeaders();

            scribe.log(
                    logLevel,
                    tag,
                    String.format(
                            Locale.US,
                            "BEGIN HTTP %s request to: %s",
                            request.getMethod(),
                            request.getURI().getPath()));
            // connect
            request.connect();

            // send data is necessary
            request.sendData();

            //get the response
            response = new HttpResponse(request);
            responseCode = response.getResponseCodeWithException();

            // lock the log
            try {
                scribe.lock();
                scribe.log(
                        logLevel,
                        tag,
                        String.format(
                                Locale.US,
                                "END HTTP %s request: %s",
                                request.getMethod(),
                                request.getURI().getPath()));
                if (scribe.isLoggable(Log.VERBOSE)) {
                    if (!headers.isEmpty()) scribe.log(Log.VERBOSE, tag, "HTTP request headers:");
                    for (HttpHeader header : headers) {
                        scribe.log(Log.VERBOSE, tag, header.toString());
                    }
                }
                String contentType = request.getContentType();
                if (request.sendingOutput()
                        && !TextUtils.isEmpty(contentType)
                        && (request.mRequestBody != null)
                        && contentType.startsWith("text/")) {
                    scribe.log(
                            logLevel,
                            tag,
                            String.format(
                                    Locale.US,
                                    "HTTP request payload\n%s\n",
                                    request.mRequestBody.toString()));
                }

                if (scribe.isLoggable(Log.VERBOSE)) {
                    // get the response headers
                    headers = response.getHeaders();
                    if (!headers.isEmpty()) scribe.log(Log.VERBOSE, tag, "HTTP response headers:");
                    for (HttpHeader header : headers) {
                        scribe.log(Log.VERBOSE, tag, header.toString());
                    }
                }
                scribe.log(
                        logLevel,
                        tag,
                        String.format(
                                Locale.US,
                                "Status code: %d",
                                responseCode));
            } finally {
                // unlock the log
                scribe.unlock();
            }

            switch(responseCode) {
                case HttpURLConnection.HTTP_MOVED_TEMP:
                case HttpURLConnection.HTTP_MOVED_PERM:
                case HttpURLConnection.HTTP_SEE_OTHER:
                case HTTP_STATUS__TEMPORARY_REDIRECT: {
                    HttpHeader header = response.getHeader(HTTP_HEADER__LOCATION);
                    if (header != null) {
                        response.disconnect();
                        return getHttpResponse(
                                request.rebuildForRedirect(header.getValue()),
                                tag,
                                logLevel,
                                scribe);
                    }
                    break;
                }

                default:
                    break;
            }

        } catch (IOException e) {
            scribe.log(Log.WARN, tag, String.format(
                    Locale.US,
                    "Http %s response: %s\nfailed with I/O exception",
                    request.getMethod(), request.getURI().getPath())
                    + " " + e.getMessage());
            requestException = e;
            response = null;
        } catch (Exception e) {
            scribe.log(Log.WARN, tag, String.format(
                    Locale.US,
                    "Http %s response: %s\nfailed with exception",
                    request.getMethod(), request.getURI().getPath())
                    + " " + e.getMessage());
            requestException = e;
            response = null;
        }

        return new HttpRequestResponseContainer.Builder(request)
                .setException(requestException)
                .setResponse(response)
                .build();
    }

    /**
     * Cleanup after the provided http request/response
     * @param responseContainer
     *              HTTP request/response pair
     */
    public static void cleanup(HttpRequestResponseContainer responseContainer) {
        if (responseContainer != null) {
            if (responseContainer.response != null) {
                responseContainer.response.disconnect();
            } else if (responseContainer.request != null) {
                responseContainer.request.abort();
            }
        }
    }

    /**
     * Return a list of HTTP headers
     * @param headerMap
     *              map of http headers
     * @return
     *              List of HttpHeader values
     */
    static List<HttpHeader> getHeadersFromMap(Map<String,List<String>> headerMap) {
        ArrayList<HttpHeader> headerList = new ArrayList<HttpHeader>();
        if (headerMap == null) return headerList;
        for(Map.Entry<String, List<String>> entry : headerMap.entrySet()) {
            String key = entry.getKey();
            List<String> valuesList = entry.getValue();
            if (TextUtils.isEmpty(key) || (valuesList == null)) continue;
            for(String value : valuesList) {
                headerList.add(HttpHeader.create(key, (value == null) ? "" : value));
            }
        }
        return headerList;
    }

    /**
     * Read the response as a string
     * @param response
     *              HTTP request/response
     * @param defaultCharSet
     *              Character set the data is encoded as
     * @return
     *              String representation of the http response payload
     * @throws UnsupportedEncodingException
     *              if the character set encoding is not supported
     * @throws IOException
     *              if an error occurs reading the data
     */
    @SuppressWarnings({"DuplicateThrows", "SameParameterValue"})
    public static String readResponseBodyAsString(HttpResponse response, String defaultCharSet) throws UnsupportedEncodingException, IOException {
        // validate input
        if (response == null) return "";
        InputStream is = response.getInputStream();
        // continue only if we have an input stream
        if (is == null) return "";
        // get the response length
        int responseLength = response.getContentLength();
        // check if response length is valid, otherwise use a "reasonable value"
        if (responseLength < 0) responseLength = 4096;
        // determine the character set to decode the string with
        String charset = null;
        // try the charset specified in the response
        HttpHeader charsetHeader = response.getHeader(HTTP_HEADER__CHARSET);
        if (charsetHeader != null) charset = charsetHeader.getValue();
        // if charset not specified in the response, try the provided one
        if (charset == null) charset = defaultCharSet;
        // if no charset provided, use the default
        if (charset == null) charset = HttpRequest.DEFAULT_TEXT_ENCODING;
        // process the input
        Reader reader = new InputStreamReader(is, charset);
        StringBuilder sb = new StringBuilder(responseLength);
        //noinspection TryFinallyCanBeTryWithResources
        try {
            int bytesRead;
            char[] buffer = new char[1024];
            while((bytesRead = reader.read(buffer)) != -1) {
                sb.append(buffer, 0, bytesRead);
            }
        } finally {
            try {
                reader.close();
            } catch(IOException ignored) {}
        }
        // return whatever we have
        return sb.toString();
    }

    /**
     * Read the response as a string
     * @param response
     *              HTTP request/response
     * @return
     *              String representation of the http response payload
     * @throws UnsupportedEncodingException
     *              if the character set encoding is not supported
     * @throws IOException
     *              if an error occurs reading the data
     */
    @SuppressWarnings("DuplicateThrows")
    public static String readResponseBodyAsString(HttpResponse response) throws UnsupportedEncodingException, IOException{
        return HttpUtils.readResponseBodyAsString(response, null);
    }

    /**
     * Create an SSL socket factory with on first use SSL pinning
     * @param trustManager Trust manager to associate with SSL socket factory
     * @return SSL Socket Factory with pinning support
     */
    @SuppressWarnings("SameParameterValue")
    public static SSLSocketFactory getSSLSocketFactory(TrustManager trustManager) {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, null);
            return sslContext.getSocketFactory();

        } catch (Exception ignored) {}

        return null;
    }

    /**
     * Create an SSL socket factory with on first use SSL pinning
     * @return SSL Socket Factory with pinning support
     */
    public static SSLSocketFactory getPinningSSLSocketFactory() {
        return getSSLSocketFactory(new PinningTrustManager(null));
    }

    /**
     * Create an SSL socket factory with on first use SSL pinning
     * @param ks KeyStore to associate with PinningSocketFactory
     * @return SSL Socket Factory with pinning support
     */
    public static SSLSocketFactory getPinningSSLSocketFactory(KeyStore ks) {
        return getSSLSocketFactory(new PinningTrustManager(ks));
    }

    /**
     * Private constructor
     */
    private HttpUtils() {}

}
