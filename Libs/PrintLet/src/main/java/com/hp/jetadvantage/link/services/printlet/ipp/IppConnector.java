package com.hp.jetadvantage.link.services.printlet.ipp;

import android.content.Context;
import android.util.Log;

import com.hp.sdd.jabberwocky.chat.HttpHostnameVerifier;
import com.hp.sdd.jabberwocky.chat.HttpRequest;
import com.hp.sdd.jabberwocky.chat.HttpRequestResponseContainer;
import com.hp.sdd.jabberwocky.chat.HttpResponse;
import com.hp.sdd.jabberwocky.chat.HttpUtils;
import com.hp.sdd.jabberwocky.chat.PinningTrustManager;
import com.hp.sdd.jabberwocky.utils.Chronicler;

import java.security.KeyStore;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

public class IppConnector {

    public static final String TAG = IppConnector.class.getSimpleName();

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

    /** Last HTTP response (thread specific)  */
    private final ThreadLocal<HttpResponse> deviceLastHttpResponse = new ThreadLocal<HttpResponse>();

    private ExecutorService mExecutorService;

    public static class Constants {
        /** Connection timeout for ipp */
        public static final int CONNECTION_TIMEOUT_120 = 120000;

        /** Connection timeout for ipp */
        public static final int SOCKET_TIMEOUT_120 = 120000;

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

        /** HTTP server error, defined here since it's deprecated in HttpUrlConnection */
        public static final int HTTP_SERVER_ERROR = 500;

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
    }

    public IppConnector() {
        mExecutorService =  Executors.newSingleThreadExecutor();
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
     * Submits an HTTP request to the device
     * @param request
     * 				HTTP request to execute
     * @param debugSettings
     * 				Debug settings to use when getting the response
     * @return
     *              HTTP response for the request
     * @hide
     */
    public Future<HttpRequestResponseContainer> getHttpResponse(HttpRequest request, int debugSettings) {
        // make sure content of a previous request is consumed
        httpConsumeContent();

        Future<HttpRequestResponseContainer> future = mExecutorService.submit(new Callable<HttpRequestResponseContainer>() {
            @Override
            public HttpRequestResponseContainer call() throws Exception {
                HttpRequestResponseContainer requestPair = HttpUtils.getHttpResponse(request, TAG, Chronicler.DEBUG_OFF, null);
                deviceLastHttpResponse.set(requestPair.response);
                return requestPair;
            }
        });

        return future;
    }
}
