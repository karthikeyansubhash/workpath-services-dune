// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.connect;

import androidx.annotation.NonNull;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hp.jetadvantage.link.services.connect.oxpd.OXPdClient;
import com.hp.jetadvantage.link.services.connect.request.OmniRequest;
import com.hp.jetadvantage.link.services.connect.response.OmniResponse;
import com.hp.jetadvantage.link.services.connect.util.MessageCallback;
import com.hp.jetadvantage.link.services.connect.util.OmniResponseException;
import com.hp.oxpdlib.OXPdDevice;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.CacheControl;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.TlsVersion;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

@Deprecated
public class OXPdConnect {
    private static final String TAG = "OXPdConnect";

    private final int HANDLER_CONNECT = 0;
    private final int HANDLER_GET_DATA = 1;
    private final int HANDLER_DUNE_CONNECT = 2;

    private static OXPdConnect instance;
    private static final Gson gson = new Gson();

    private OkHttpClient httpClient;
    private OkHttpClient httpsClient;
    private WebSocket ws;
    private CountDownLatch connectedMonitor;

    /** Main thread to let handler run in */
    private HandlerThread handlerThread;
    private Handler handler;

    private Map<String, MessageCallback> requests = new ConcurrentHashMap<>();

    public static String SECRET_KEY = "";

    private static final String DuneEngineHostIP = "156.152.79.233";
    private static final int DuneHostSecretPot = 5000;
    private static String DuneHostSecret = "";
    private static String DuneHostToken = "";
    private static String DuneAdminToken = "";

    private OXPdConnect() {
        handlerThread = new HandlerThread(TAG + ":" + getClass().getSimpleName(),
                Process.THREAD_PRIORITY_MORE_FAVORABLE);
        handlerThread.start();

        handler = new WSMessageHandler(handlerThread.getLooper());

        httpClient = new OkHttpClient.Builder()
                .connectTimeout(OXPdDevice.Constants.OXPD_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(OXPdDevice.Constants.OXPD_REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(OXPdDevice.Constants.OXPD_REQUEST_TIMEOUT, TimeUnit.SECONDS)
                //For SDK Simulator
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Interceptor.Chain chain) throws IOException {
                        Request request = chain.request().newBuilder().addHeader("Connection", "close").build();
                        return chain.proceed(request);
                    }
                })
                .retryOnConnectionFailure(false)
                .build();

        this.httpsClient = getUnsafeOkHttpClient(null);

        connect();
    }

    private static OkHttpClient getUnsafeOkHttpClient(OkHttpClient.Builder builder) {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            ConnectionSpec spec;
            if (isDune()) {
                spec = new ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
                        .supportsTlsExtensions(true)
                        .tlsVersions(TlsVersion.TLS_1_3, TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0)
                        .cipherSuites(
                                //CipherSuite.TLS_RSA_WITH_AES_128_GCM_SHA256,
                                //CipherSuite.TLS_RSA_WITH_AES_256_GCM_SHA384,
                                //CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256, // 20+
                                //CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256, //20+
                                //CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256, //20+
                                CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA, // 11+
                                CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA, // 11+
                                CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,   // 11+
                                CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,   // 11+
                                CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA,     // 1+
                                CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA,     // 1+
                                CipherSuite.TLS_DHE_DSS_WITH_AES_128_CBC_SHA,   // 1-22
                                CipherSuite.TLS_DHE_DSS_WITH_AES_256_CBC_SHA,   // 11-22
                                CipherSuite.TLS_ECDHE_ECDSA_WITH_RC4_128_SHA,   // 11-23
                                CipherSuite.TLS_ECDHE_RSA_WITH_RC4_128_SHA,     // 11-23
                                CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA,       // 9+
                                CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA       // 9+
                        )
                        .build();
            } else {
                spec = new ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
                        .build();
            }

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(OXPdDevice.Constants.OXPD_REQUEST_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(0, TimeUnit.SECONDS)
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0])
                    .connectionSpecs(Collections.singletonList(spec))
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    }).retryOnConnectionFailure(false).build(); //For SDK Simulator

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static OXPdConnect getInstance() {
        if (instance == null) {
            synchronized (OXPdClient.class) {
                if (instance == null) {
                    instance = new OXPdConnect();
                    setIsOXPdReady(true);
                }
            }
        }
        return instance;
    }

    public boolean isConnected() {
        return ws != null;
    }

    private void connect() {
        Log.i(TAG, "Deprecated OXPDConnect API is called!! Refactor your codes NOT to use this API anymore.");
    }

    public OkHttpClient getHttpClient() {
        return httpClient;
    }

    private void open() {
        Request request = new Request.Builder().url(getOmniWebSocketURL(null, 0)).build();
        ws = httpClient.newWebSocket(request, webSocketListener);
    }

    private void reconnect() {
    }

    private void release() {
        Log.i(TAG, "Releasing connections");
        try {
            if (ws != null) {
                ws.close(1000, "Normal close");
                ws = null;
            }

            handlerThread.quit();
            handlerThread = null;

            if (httpClient != null) {
                httpClient.dispatcher().executorService().shutdown();
                httpClient = null;
            }

            if (httpsClient != null) {
                httpsClient.dispatcher().executorService().shutdown();
                httpsClient = null;
            }
        } catch (Exception e){
            Log.e(TAG, "Failed to release connection");
        }
    }


    //// Dune //////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static boolean isDune() {
        boolean isDune = false;
        java.lang.Process process = null;
        BufferedReader reader = null;
        String cmd[] = {"/system/bin/sh", "-c", "getprop | grep -w \"ro.lxc\""};
        String curLine = null;

        try {
            process = Runtime.getRuntime().exec(cmd);
            try {
                reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                curLine = reader.readLine();
                if (curLine != null && curLine.contains("gs25")) {
                    isDune = true;
                }
                reader.close();
            } catch (Exception e) {
                Log.e(TAG, "Read error:" + e.getMessage());
            }
        } catch (Throwable throwable) {
            Log.e(TAG, "Runtime error:" + throwable.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioe) {}
            }
        }
        return isDune;
    }

    public final String getDuneWebServicesHttpsRequestURL(@NonNull String host, int port) {
        if(TextUtils.isEmpty(host)) host = getDuneHostIp();
        if(port <= 0) return "https://" + host;
        return "https://" + host + ":" + port;
    }

    public String getDuneSimulatorIP() {
        if (!OXPdDevice.isEmulator() && isDune()) {
            Log.e(TAG, "Only supports the simulator.");
            return "";
        }
        String getDuneInfoUri = "/hp/device/apis/v1/duneSimulatorProviders";

        try {
            String bodyString = callOmniInternalByHTTP(getDuneInfoUri);
            JSONObject jsonObject = new JSONObject(bodyString);
            return jsonObject.getString("host");
        } catch (IOException | JSONException e) {
            Log.e(TAG, "REST call failed to execute httpClient newCall() :" + e.getMessage());
            return "";
        }
    }

    public String getDuneHostIp() {
//        return OXPdDevice.isEmulator()? getDuneSimulatorIP() : DuneEngineHostIP;
        return DuneEngineHostIP;
    }

    public boolean getDuneHostSecret() {
        Socket socket = null;
        BufferedReader reader = null;

        try {
            /*while(socket == null) {
                try {
                    socket = new Socket(host, port);
                } catch(Exception e) {
                    Log.d("DUNE","dune socket connect failed");
                    socket = null;
                    Thread.sleep(500);
                }
            }*/
            socket = new Socket();
            socket.setSoTimeout(5000);
            socket.connect(new InetSocketAddress(getDuneHostIp(), DuneHostSecretPot), 10000);
            Log.d(TAG,"dune socket connect succeed");
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));

            String line = reader.readLine();
            DuneHostSecret = line;

            Log.d(TAG,"dune socket secret response: " + line);
            return true;
        } catch (Exception e){
            Log.d(TAG,"dune socket error: " + e.getMessage(),e);
            return false;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
            }
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (Exception e) {
                Log.d(TAG, "dune socket close error: " + e.getMessage());
            }
        }
    }

    public void setDuneAdminToken(String adminToken) {
        DuneAdminToken = adminToken;
    }

    public boolean getDuneHostToken() {
        try {
            String tokenUri = "/hp/device/WSFramework/underware/v1/command";
            String url = getDuneWebServicesHttpsRequestURL(null,0) + tokenUri;
            JSONObject requestBody = new JSONObject();
            requestBody.put("version", "1.0.0");
            requestBody.put("targetService", "mainApp");
            requestBody.put("blocking", "true");
            requestBody.put("command", "OAuth2Standard PUB_testCreateWorkpathScopeToken");

            Request request = new Request.Builder()
                    .url(getDuneWebServicesHttpsRequestURL(null,0) + tokenUri)
                    .header("Content-Type", "application/json")
                    .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestBody.toString()))
                    .build();
            Response response = httpsClient.newCall(request).execute();

            ResponseBody body = response.body();
            String bodyString = (body != null) ? body.string() : null;
            Log.d(TAG, "getDuneToken response code : " + response.code() + " / bodyString : " + bodyString);

            JSONObject jsonObject = new JSONObject(bodyString);
            DuneHostToken = new String(Base64.decode(jsonObject.getString("response"), Base64.NO_WRAP));
            DuneHostToken = DuneHostToken.replaceAll("[\\r\\n]", "");

            Log.i(TAG, "DuneSimulatorToken@@@ : " + DuneHostToken);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "REST call failed to execute httpsClient newCall() :" + e.getMessage());
            return false;
        }
    }

    private String getParsedDuneHostToken() {
        return DuneHostToken != null && !DuneHostToken.isEmpty() ? DuneHostToken.replaceAll("[\\r\\n]", "") : "";
    }

    private void duneConnect(){
        //If the Dune Simulator is not running, we need to implement connection retry.
        if (DuneHostToken != null && !DuneHostToken.isEmpty()) {
            Log.d(TAG, "DuneHostToken already exist!!");
            return;
        }
        String  duneHost = getDuneHostIp();
        if(duneHost != null && !"".equals(duneHost) ){
            /*if(getDuneHostSecret()){
                if(getDuneHostToken()){
                    Log.d(TAG, "Dune Connection succeed!!");
                }else{
                    Log.d(TAG, "Dune Connection failed!!");
                }
            }*/
            if(getDuneHostToken()){
                Log.d(TAG, "Dune Connection succeed!!");
            }else{
                Log.d(TAG, "Dune Connection failed!!");
            }
        }
    }

    public String callDuneWebservicesPutByHTTPS(String url, String requestBody) throws IOException {
        Request request = new Request.Builder()
                .url(getDuneWebServicesHttpsRequestURL(null,0) + url)
                .header("Content-Type", "application/json")
                .header("Authorization","Bearer " + getParsedDuneHostToken())
                .put(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestBody))
                .build();
        Response response = httpsClient.newCall(request).execute();
        ResponseBody body = response.body();
        String bodyString = (body != null) ? body.string() : null;

        if (!response.isSuccessful()) {
            throw new IOException("REST call failed with HTTP " + response.code() + ": " + bodyString);
        }

        return bodyString;
    }

    public String callDuneWebservicesPostByHTTPS(String url, String requestBody) throws IOException {
        Request.Builder builder = new Request.Builder()
                .url(getDuneWebServicesHttpsRequestURL(null,0) + url)
                .header("Content-Type", "application/json")
                .header("Authorization","Bearer " + getParsedDuneHostToken());
        if (requestBody != null) {
            builder.post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestBody));
        }
        Request request = builder.build();
        Response response = httpsClient.newCall(request).execute();
        ResponseBody body = response.body();
        String bodyString = (body != null) ? body.string() : null;

        if (!response.isSuccessful()) {
            throw new IOException("REST call failed with HTTP " + response.code() + ": " + bodyString);
        }

        return bodyString;
    }

    public String callDuneWebservicesGetByHTTPS(String url) throws IOException {
        Request request = new Request.Builder()
                .url(getDuneWebServicesHttpsRequestURL(null,0) + url)
                .header("Content-Type", "application/json")
                .header("Authorization","Bearer " + getParsedDuneHostToken())
                .get()
                .build();
        Response response = httpsClient.newCall(request).execute();
        ResponseBody body = response.body();
        String bodyString = (body != null) ? body.string() : null;

        if (!response.isSuccessful()) {
            throw new IOException("REST call failed with HTTP " + response.code() + ": " + bodyString);
        }

        return bodyString;
    }

    public String callDuneWebservicesGetByHTTPS(String url, String token) throws IOException {
        Request request = new Request.Builder()
                .url(getDuneWebServicesHttpsRequestURL(null,0) + url)
                .header("Content-Type", "application/json")
                .header("Authorization","Bearer " + token == null? getParsedDuneHostToken(): token)
                .get()
                .build();
        Response response = httpsClient.newCall(request).execute();
        ResponseBody body = response.body();
        String bodyString = (body != null) ? body.string() : null;

        if (!response.isSuccessful()) {
            throw new IOException("REST call failed with HTTP " + response.code() + ": " + bodyString);
        }

        return bodyString;
    }

    public String callDuneWebservicesPatchByHTTPS(String url, String requestBody) throws IOException {
        Request request = new Request.Builder()
                .url(getDuneWebServicesHttpsRequestURL(null,0) + url)
                .header("Content-Type", "application/json")
                .header("Authorization","Bearer " + getParsedDuneHostToken())
                .patch(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestBody))
                .build();
        Response response = httpsClient.newCall(request).execute();
        String responseCode = String.valueOf(response.code());
        ResponseBody body = response.body();
        String bodyString = (body != null) ? body.string() : null;

        if (!response.isSuccessful()) {
            throw new IOException("REST call failed with HTTP " + response.code() + ": " + bodyString);
        }

        return responseCode;
    }

    public String callDuneWebservicesByHTTPS(Request request) throws IOException {

        Request.Builder newRequest = new Request.Builder()
                .url(request.url())
                .headers(request.headers())
                .method(request.method(), request.body());

        if (!DuneAdminToken.isEmpty()) {
            newRequest.addHeader("Authorization", "Bearer " + getParsedDuneHostToken());
        }
        Response response = httpsClient.newCall(newRequest.build()).execute();
        ResponseBody body = response.body();
        String bodyString = (body != null) ? body.string() : null;

        if (!response.isSuccessful()) {
            throw new IOException("REST call failed with HTTP " + response.code() + ": " + bodyString);
        }
        return bodyString;
    }

    public String callDuneWebservicesByHTTPS(String tokenUri) throws IOException {
        Request request = new Request.Builder()
                .url(getDuneWebServicesHttpsRequestURL(null,0) + tokenUri)
                .get()
                .build();
        Response response = httpsClient.newCall(request).execute();
        ResponseBody body = response.body();
        String bodyString = (body != null) ? body.string() : null;

        if (!response.isSuccessful()) {
            throw new IOException("REST call failed with HTTP " + response.code() + ": " + bodyString);
        }

        return bodyString;
    }

    public byte[] callDuneWebservicesBytes(Request request) throws IOException {

        Request.Builder newRequest = new Request.Builder()
                .url(request.url())
                .headers(request.headers())
                .method(request.method(), request.body());

        if (!DuneAdminToken.isEmpty()) {
            newRequest.addHeader("Authorization", "Bearer " + getParsedDuneHostToken());
        }

        Response response = httpsClient.newCall(newRequest.build()).execute();
        ResponseBody body = response.body();
        String bodyString = (body != null) ? body.string() : null;
        if (!response.isSuccessful()) {
            throw new IOException("REST call failed with HTTP " + response.code() + ": " + bodyString);
        }
        return response.body().bytes();
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Build an Authorization header
     * @param username
     *              Username to login in as
     * @param password
     *              Password associated with username
     * @return
     *              HttpHeader encoded with user credentials
     */
    private String buildAuthorizationHeader(String username, String password) {
        StringBuilder builder = new StringBuilder(OXPdDevice.Constants.HTTP_HEADER_VALUE__AUTHORIZATION_MODE);
        builder.append(' ');

        if (password == null) password = "";

        try {
            builder.append(Base64.encodeToString(String.format(Locale.US, "%s:%s", username,password).getBytes(OXPdDevice.Constants.UTF_8.displayName()), Base64.URL_SAFE | Base64.NO_WRAP));
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Failed to build authorization header"  + e.getMessage());
        }

        return builder.toString();
    }

    private static boolean isOXPdReady = false;

    public static void setIsOXPdReady(boolean value) {
        isOXPdReady = value;
        Log.d(TAG, "SetIsOXPdReady: " + isOXPdReady);
    }

    /**
     * Asynchronous call to WebSocket with provided method and url
     * @param method "GET", "POST" or "DELETE"
     * @param url URI to call
     * @param data data for POST
     * @param messageCallback callback to receive response
     * @throws IllegalStateException if connection is not opened
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    private void callOmniInternal(@NonNull final String method, @NonNull final String url, final String data,
                                 MessageCallback messageCallback) {
        if (!isConnected()) {
            if (OXPdDevice.isPanel()) {
                connect();
            } else {
                Log.w(TAG, "Omni internal API is not accessible");
                throw new IllegalStateException("Omni internal API is not accessible");
            }
        }

        OmniRequest body = getWebSocketRequest(method, url, data);
        requests.put(body.getTransactionId(), messageCallback);

        String payload = gson.toJson(body);
        Message request = handler.obtainMessage(HANDLER_GET_DATA, payload);
        handler.sendMessage(request);
    }

    /**
     * Synchronous call to WebSocket with provided method and url
     * @param method "GET", "POST" or "DELETE"
     * @param url URI to call
     * @param data data for POST
     * @return response data
     * @throws IllegalStateException if request timed out
     * @throws OmniResponseException if status is not 2xx
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public String callOmniInternal(@NonNull final String method, @NonNull final String url, final String data)
            throws IllegalStateException, OmniResponseException {
        final CountDownLatch requestWait = new CountDownLatch(1);
        final Map<Integer, String> response = new HashMap<>();

        final MessageCallback requestCallback = new MessageCallback() {
            @Override
            public void onResponse(int status, String eTag, String data) {
                response.put(status, data);
                requestWait.countDown();
            }
        };

        callOmniInternal(method, url, data, requestCallback);

        try {
            if (!requestWait.await(OXPdDevice.Constants.OXPD_REQUEST_TIMEOUT, TimeUnit.SECONDS) || response.size() == 0) {
                throw new IllegalStateException("Request timeout occurred");
            }
        } catch (InterruptedException e) {
            throw new IllegalStateException("Response waiting interrupted");
        }

        Map.Entry<Integer, String> entry = response.entrySet().iterator().next();
        Integer status = entry.getKey();
        String body = entry.getValue();
        if (status >= 200 && status < 300) {
            return body;
        } else {
            throw new OmniResponseException(status, body);
        }
    }

    /**
     * Syncronous call to WebSocket with provided method and url
     * @param method "GET", "POST" or "DELETE"
     * @param url URI to call
     * @return response data
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public String callOmniInternal(@NonNull final String method, @NonNull final String url)
            throws IllegalStateException, OmniResponseException {
        return callOmniInternal(method, url, null);
    }

    public String callOmniInternalByHTTP(String url) throws IOException {

        //Dune REST api call Sample
        if (isDune() && url == "/hp/device/apis/v1/usageDataProviders") {
            Log.d(TAG, "Dune REST call sample start");

            String duneRequestUri = "/cdm/jobTicket/v1/tickets";
            String duenRequestBody = null;
            try {
                JSONObject duenRequest = new JSONObject();
                duenRequest.put("src", new JSONObject().put("scan", new JSONObject()));
                duenRequest.put("dest", new JSONObject().put("folder", new JSONObject()));
                duenRequestBody = duenRequest.toString();
            } catch (Exception e) {
                Log.e(TAG, "An exceptoin has occurred while making a duneRequest JSONObject.");
                duenRequestBody = "{}";
            }

            String duneResponse = callDuneWebservicesPostByHTTPS(duneRequestUri, duenRequestBody);
            Log.d(TAG, "call dune post response : " + duneResponse);

            duneRequestUri = "/cdm/contacts/v1/addressBooks";
            duneResponse = callDuneWebservicesGetByHTTPS(duneRequestUri);
            Log.d(TAG, "call dune get response : " + duneResponse);

            Log.d(TAG, "Dune REST call sample end");
        }
        //////////////////////////////////////////////////////////////////////////////////////////////////////

        Request request = new Request.Builder()
                .url(getOmniHttpRequestURL(null, 0) + url)
                .header(OXPdDevice.Constants.HTTP_HEADER__AUTHORIZATION, buildAuthorizationHeader(OXPdDevice.Constants.HTTP_HEADER_VALUE__AUTHORIZATION_GUEST, null)) //guest:
                .header("Connection", "close")
                .build();
        Response response = httpClient.newCall(request).execute();
        ResponseBody body = response.body();
        String bodyString = (body != null) ? body.string() : null;

        if (!response.isSuccessful()) {
            throw new IOException("REST call failed with HTTP " + response.code() + ": " + bodyString);
        }

        return bodyString;
    }

    public String callWebservicesByHTTP(String url) throws IOException {
        Request request = new Request.Builder()
                .url(getWebServicesHttpRequestURL(null, 0) + url)
                .header(OXPdDevice.Constants.HTTP_HEADER__AUTHORIZATION, buildAuthorizationHeader(OXPdDevice.Constants.HTTP_HEADER_VALUE__AUTHORIZATION_GUEST, null)) //guest:
                .header("Connection", "close")
                .build();
        Response response = httpClient.newCall(request).execute();
        ResponseBody body = response.body();
        String bodyString = (body != null) ? body.string() : null;

        if (!response.isSuccessful()) {
            throw new IOException("REST call failed with HTTP " + response.code() + ": " + bodyString);
        }

        return bodyString;
    }

    public String callWebservicesByHTTPS(String auth, String url) throws IOException {
        if(TextUtils.isEmpty(auth)) {
            auth = buildAuthorizationHeader(OXPdDevice.Constants.HTTP_HEADER_VALUE__AUTHORIZATION_GUEST, null);
        }
        Request request = new Request.Builder()
                .url(getWebServicesHttpsRequestURL(null, 0) + url)
                .header(OXPdDevice.Constants.HTTP_HEADER__AUTHORIZATION, auth) //guest:
                .header("Connection", "close")
                .build();
        Response response = httpsClient.newCall(request).execute();
        ResponseBody body = response.body();
        String bodyString = (body != null) ? body.string() : null;

        if (!response.isSuccessful()) {
            throw new IOException("REST call failed with HTTP " + response.code() + ": " + bodyString);
        }

        return bodyString;
    }

    public String callWebservicesByHTTP(String method, String url, String mediaType, String requestBody) throws IOException {
        if(TextUtils.isEmpty(mediaType)) {
            mediaType = OXPdDevice.Constants.CONTENT_TYPE__JSON;
        }

        Request request = new Request.Builder()
                .method(method, requestBody != null ?
                        RequestBody.create(MediaType.parse(mediaType), requestBody) : null)
                .url(getWebServicesHttpRequestURL(null, 0) + url)
                .header(OXPdDevice.Constants.HTTP_HEADER__AUTHORIZATION, buildAuthorizationHeader(OXPdDevice.Constants.HTTP_HEADER_VALUE__AUTHORIZATION_GUEST, null)) //guest:
                .header("Connection", "close")
                .build();
        Response response = httpClient.newCall(request).execute();
        ResponseBody body = response.body();
        String bodyString = (body != null) ? body.string() : null;

        if (!response.isSuccessful()) {
            throw new IOException("REST call failed with HTTP " + response.code() + ": " + bodyString);
        }

        return bodyString;
    }

    public String callWebservicesByHTTPForAttestation(String method, String url, String mediaType, String requestBody) throws IOException { //HashMap<String, Object> requestBody) throws IOException {
        if(TextUtils.isEmpty(mediaType)) {
            mediaType = OXPdDevice.Constants.CONTENT_TYPE__JSON;
        }

        Request request = new Request.Builder()
                .url(getWebServicesHttpRequestURL(null, 0) + url)
                //.method("POST", RequestBody.create(MediaType.parse(mediaType), requestBody)) //UTF-8
                .method(method, RequestBody.create(MediaType.parse(mediaType+"; charset=''"), requestBody)) //TODO FW Bug, UTF-8
                .header(OXPdDevice.Constants.HTTP_HEADER__AUTHORIZATION, buildAuthorizationHeader(OXPdDevice.Constants.HTTP_HEADER_VALUE__AUTHORIZATION_GUEST, null)) //guest:
                .header("Accept-Encoding", "gzip, deflate")
                .header("Accept", "*/*")
                .header("Connection", "keep-alive")
                .header("Content-Type", mediaType)
                .cacheControl(CacheControl.FORCE_NETWORK)
                .build();

        Response response = httpClient.newCall(request).execute();
        ResponseBody body = response.body();
        String bodyString = (body != null) ? body.string() : null;

        Log.d(TAG, "result:" + response.code());

        return bodyString;
    }

    public String callWebservicesByHTTPSForAttestation(String method, String host, String url, String mediaType, String token, String mode, String requestBody) throws IOException { //HashMap<String, Object> requestBody) throws IOException {
        if(TextUtils.isEmpty(mediaType)) {
            mediaType = OXPdDevice.Constants.CONTENT_TYPE__JSON;
        }

        Request request = new Request.Builder()
                .method(method, requestBody != null ?
                        RequestBody.create(MediaType.parse(mediaType), requestBody) : null)
                .url(getWebServicesHttpsRequestURL(host, 0) + url)
                .header("Connection", "close")
                .header("Content-Type", mediaType)
                .header("x-token", token)
                .header("x-mode", mode)
                .build();
        Response response = httpsClient.newCall(request).execute();
        ResponseBody body = response.body();
        String bodyString = (body != null) ? body.string() : null;

        if (response.isSuccessful() &&
                ( response.code() == HttpURLConnection.HTTP_OK
                        || response.code() == HttpURLConnection.HTTP_CREATED
                        || response.code() == HttpURLConnection.HTTP_ACCEPTED
                        || response.code() == HttpURLConnection.HTTP_NOT_AUTHORITATIVE
                        || response.code() == HttpURLConnection.HTTP_NO_CONTENT
                        || response.code() == HttpURLConnection.HTTP_RESET
                        || response.code() == HttpURLConnection.HTTP_PARTIAL)) {
        } else {
            String message = "";
            switch (response.code()) {
                case HttpURLConnection.HTTP_NOT_FOUND:
                    message = "404 Requested resource not found";
                    break;
                case OXPdDevice.Constants.HTTP_SERVER_ERROR:
                    message = "500 Internal server error";
                    break;
                default:
                    message = "CODE:"+response.code();
                    break;
            }
            throw new IOException("REST call failed with HTTP " + message + ", " + bodyString);
        }

        return bodyString;
    }

    /* Commented 19.04.22 because no caller
    public String callOmniInternalSOAP(String url, String userName, String password, String requestContent) throws Exception {
        String fullUrl = getOXPdWebSocketURL() + url;

        if (requestContent != null) {
            requestContent = requestContent.replaceAll("<URL>", fullUrl);

            String authorization = Base64.encodeToString((userName + ":" + password).getBytes(),
                    Base64.NO_WRAP | Base64.URL_SAFE);

            Request request = new Request.Builder()
                    .url(fullUrl)
                    .header("Authorization", "Basic " + authorization)
                    .header("Connection", "close")
                    .method("POST", RequestBody.create(MediaType.parse("application/soap+xml; charset=utf-8"), requestContent))
                    .build();
            Response response = httpClient.newCall(request).execute();
            ResponseBody body = response.body();

            if(response.code() == 403) {
                throw new AuthorizationException("User is unauthorized");
            }

            Log.d(TAG, "result:" + response.code());

            return (body != null) ? body.string() : null;
        }

        return null;
    }
    */

    public void subscribe(@NonNull final String url, final MessageCallback callback) {

        // need to check whether it is dune or jedi
        final MessageCallback requestCallback = new MessageCallback() {
            @Override
            public void onResponse(int status, String eTag, String data) {
                if (callback != null) {
                    callback.onResponse(status, eTag, data);
                }

                callOmniInternal(OmniRequest.GET, getSubscribeUrl(url, eTag), null, this);
            }
        };

        callOmniInternal(OmniRequest.GET, url, null, requestCallback);
    }

    private String getSubscribeUrl(@NonNull final String url, @NonNull final String eTag) {
        return url + (!url.contains("?") ? "?" : "&") + "difffrom=" + eTag;
    }

    private OmniRequest getWebSocketRequest(String method, String url, String data) {
        String uUid = UUID.randomUUID().toString();
        OmniRequest request = new OmniRequest();
        request.setTransactionId(uUid);

        request.setVerb(method);
        request.setUri(url);
        request.setData(data);
        return request;
    }

    private final String getOmniWebSocketURL(@NonNull String host, int port) {
        if(TextUtils.isEmpty(host)) host = OXPdDevice.Constants.HOSTNAME_INTERNAL;
        if(port <= 0) port = OXPdDevice.Constants.OMNI_PORT_INTERNAL;
        //Log.i("[SIM]","getOmniWebSocketURL : "+"ws://" + host + ":"+ port +"/hp/omni");
        return "ws://" + host + ":"+ port +"/hp/omni";
    }

    /**
     * An API that generates an websocket for the Dune.
     *
     * @param builder : An OkHttpClient.Builder to build a OkHttpClient.
     * @param request : A request to open the websocket connection.
     * @param listener : An WebSocketListener that would listen events from the websocket server.
     * @return An WebSocket that is requested.
     */
    public WebSocket getDuneWebsocket(OkHttpClient.Builder builder, Request request, WebSocketListener listener) {
        if (isDune()) {
            // generates a client that does not validate certificate chains.
            OkHttpClient client = getDuneHttpClient(builder);
            WebSocket socket = client.newWebSocket(request, listener);
            Log.e(TAG, "getDuneWebsocket() DuneHostToken = " + DuneHostToken);
            // send the DuneHostToken to the websocket server
            // so that the websocket client could communicate with the server.
            socket.send("Authorization: Bearer " + DuneHostToken);
            return socket;
        }
        return null;
    }

    /**
     * An API that generates an OkHttpClient which does not validate certificate chains.
     *
     * @param builder : An OkHttpClient.Builder to build a OkHttpClient.
     * @return An OkHttpclient.
     */
    private OkHttpClient getDuneHttpClient(OkHttpClient.Builder builder) {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
                    .supportsTlsExtensions(true)
                    .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0)
                    .cipherSuites(
                            //CipherSuite.TLS_RSA_WITH_AES_128_GCM_SHA256,
                            //CipherSuite.TLS_RSA_WITH_AES_256_GCM_SHA384,
                            //CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256, // 20+
                            //CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256, //20+
                            //CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256, //20+
                            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA, // 11+
                            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA, // 11+
                            CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,   // 11+
                            CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,   // 11+
                            CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA,     // 1+
                            CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA,     // 1+
                            CipherSuite.TLS_DHE_DSS_WITH_AES_128_CBC_SHA,   // 1-22
                            CipherSuite.TLS_DHE_DSS_WITH_AES_256_CBC_SHA,   // 11-22
                            CipherSuite.TLS_ECDHE_ECDSA_WITH_RC4_128_SHA,   // 11-23
                            CipherSuite.TLS_ECDHE_RSA_WITH_RC4_128_SHA,     // 11-23
                            CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA,       // 9+
                            CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA       // 9+
                    )
                    .build();

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient okHttpClient = builder
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0])
                    .connectionSpecs(Collections.singletonList(spec))
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    }).retryOnConnectionFailure(false).build(); //For SDK Simulator

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public final String getOmniHttpRequestURL(@NonNull String host, int port) {
        if(TextUtils.isEmpty(host)) host = OXPdDevice.Constants.HOSTNAME_INTERNAL;
        if(port <=0) port = OXPdDevice.Constants.OMNI_PORT_HTTP_INTERNAL;
        return "http://" + host + ":" + port;
    }

    public final String getWebServicesHttpRequestURL(@NonNull String host, int port) {
        if(TextUtils.isEmpty(host)) host = OXPdDevice.Constants.OXPD_HOST_INTERNAL;
        if(port <=0) port = OXPdDevice.Constants.HTTP_PORT;
        return "http://" + host + ":" + port;
    }

    public final String getWebServicesHttpsRequestURL(@NonNull String host, int port) {
        if(TextUtils.isEmpty(host)) host = OXPdDevice.Constants.OXPD_HOST_INTERNAL;
        if(port <= 0) return "https://" + host;
        return "https://" + host + ":" + port;
    }

    public final String getOXPdWebServiceURL(@NonNull String host, int port) {
        if(TextUtils.isEmpty(host)) host = OXPdDevice.Constants.OXPD_HOST_INTERNAL;
        if(port <=0) port = OXPdDevice.Constants.OXPD_PORT_INTERNAL;
        return "http://" + host + ":" + port;
    }

    private class WSMessageHandler extends Handler {
        WSMessageHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_CONNECT:
                    open();
                    break;
                case HANDLER_GET_DATA:
                    if(ws != null) {
                        ws.send((String) msg.obj);
                    }
                    break;
                case HANDLER_DUNE_CONNECT:
                    duneConnect();
                    break;
            }
        }
    }

    private WebSocketListener webSocketListener = new WebSocketListener() {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            Log.d(TAG, "Omni connection opened");
            connectedMonitor.countDown();
        }

        @Override
        public void onMessage(WebSocket webSocket, String message) {
            Log.d(TAG, "Omni connection received data");

            try {
                OmniResponse response = gson.fromJson(message, OmniResponse.class);

                MessageCallback callback = requests.remove(response.getTransactionId());
                if (callback != null) {
                    callback.onResponse(response.getStatus(), response.geteTag(), response.getData());
                }
            } catch (JsonSyntaxException e) {
                Log.e(TAG, "Failed to parse response");
            }
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            Log.e(TAG, "WebSocket connection failure");
            try {
                Log.e(TAG, "Message:::" + t.getMessage());
            } catch (Throwable throwable) {
            }

            try {
                String result = (response != null)?(response.code() + ", " + response.message()):"response is null";
                Log.e(TAG, "WebSocket connection failure: " + result);
            } catch (Throwable throwable) {
            }

            for (MessageCallback callback : requests.values()) {
                callback.onResponse(500, null, null);
            }
            requests.clear();

            reconnect();
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            Log.w(TAG, "Omni connection closed");
            for (MessageCallback callback : requests.values()) {
                callback.onResponse(500, null, null);
            }
            requests.clear();

            reconnect();
        }
    };

    /*private boolean isSuccessFromHTTPStatus(Response response) throws IOException {
        boolean flag = false;
        Log.d(TAG, "result:" + response.code() + ", " + response.body().string());

        if (response.isSuccessful() &&
                (response.code() >= 200 && response.code() < 300)) {
            *//*
                 HttpURLConnection.HTTP_OK
                        || response.code() == HttpURLConnection.HTTP_CREATED
                        || response.code() == HttpURLConnection.HTTP_ACCEPTED
                        || response.code() == HttpURLConnection.HTTP_NOT_AUTHORITATIVE
                        || response.code() == HttpURLConnection.HTTP_NO_CONTENT
                        || response.code() == HttpURLConnection.HTTP_RESET
                        || response.code() == HttpURLConnection.HTTP_PARTIAL
             *//*
            flag = true;
        } else {
            String message = "";
            switch (response.code()) {
                case HttpURLConnection.HTTP_UNAUTHORIZED:
                case HttpURLConnection.HTTP_FORBIDDEN:
                    message = "User is unauthorized";
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    message = "404 Requested resource not found";
                    break;
                case OXPdDevice.Constants.HTTP_SERVER_ERROR:
                    message = "500 Internal server error";
                    break;
                default:
                    message = response.body()!=null? response.body().string(): "";
                    break;
            }
            throw new IOException(message);
        }
        return flag;
    }*/

}
