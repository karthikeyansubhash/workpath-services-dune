/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.clients;

import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Abstract Device Client class
 * - parent class for all the device clients like CDMClient, E2Client, UwdClient
 */
public abstract class DeviceClient {

    protected final Object lock = new Object();
    protected String TAG = "[WS]DSC";
    protected OkHttpClient httpClient;
    protected String deviceIpAddress;
    protected String token;

    protected CDMResponse<String> send(Request request, boolean httpErrorResponseCodeRequired) throws IOException {
        Log.d(TAG, "send : Enter: " + request.method() + "[" + request.url() + "]");
        Log.d(TAG, "send : Request header=" + request.headers());
        Log.d(TAG, "send : Request body :" + requestBodyToString(request));
        Response response = httpClient.newCall(request).execute();
        ResponseBody body = response.body();
        String bodyString = (body != null) ? body.string() : null;

        if (!httpErrorResponseCodeRequired && !response.isSuccessful()) {
            Log.i(TAG, "HTTP " + request.method() + "[" + request.url() + "] --> " + response.code());
            throw new IOException("HTTP request failed. " + response.code() + ": " + bodyString);
        }

        Log.d(TAG, "HTTP " + request.method() + "[" + request.url() + "] --> " + response.code());
        Log.d(TAG, "Response :" + (request.url().toString().contains("servicesDiscovery") ? "..." :
                bodyString));
        CDMResponse<String> cdmResponse = CDMResponse.create(response.code(), bodyString);
        return cdmResponse;
    }

    protected CDMResponse<ResponseBody> sendRaw(Request request) throws IOException {
        Response response = httpClient.newCall(request).execute();
        ResponseBody body = response.body();

        if (!response.isSuccessful()) {
            throw new IOException("HTTP request failed." + response.code() + ": " + body.string());
        }

        Log.i(TAG, "HTTP " + request.method() + "[" + request.url() + "] --> " + response.code());
        Log.d(TAG, "HTTP Response Content-Type:" + body.contentType());
        CDMResponse<ResponseBody> cdmResponse = CDMResponse.create(
                response.code(),
                body,
                (body != null && body.contentType() != null) ? body.contentType().toString() : null
        );
        return cdmResponse;
    }

    public void updateDeviceInfo(String ipAddress, String token) {
        synchronized (lock) {
            this.deviceIpAddress = ipAddress;
            this.token = token;
        }
    }

    private String requestBodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            if (copy.body() != null && copy.body().contentLength() != -1) {
                copy.body().writeTo(buffer);
            } else {
                return "skip to read";
            }
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "Did not work";
        }
    }

    public static final class Constant {

        /**
         * Define HTTP connection request timeout in seconds
         */
        public static final long HTTP_CONNECTION_REQUEST_TIMEOUT = 60;
    }
}
