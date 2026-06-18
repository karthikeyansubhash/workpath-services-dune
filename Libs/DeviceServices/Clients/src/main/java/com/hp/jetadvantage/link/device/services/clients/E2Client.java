/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.clients;

import com.hp.ext.clients.InjectedHttpClient;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

/**
 * E2 Http Client class
 * - injected into the OXPd2 lib to provide a HTTP client for executing HTTP requests and obtaining HTTP responses
 */
public class E2Client extends DeviceClient implements InjectedHttpClient {
    public E2Client(OkHttpClient httpclient, String bearerToken) {
        this.TAG = TAG + "/E2";
        this.httpClient = httpclient;
        this.token = bearerToken;
    }

    @Override
    public String getResponseAsString(Request request) throws IOException {
        Request.Builder newRequest = new Request.Builder()
                .url(request.url())
                .headers(request.headers())
                .method(request.method(), request.body());

        var response = send(newRequest.build(), false);
        return response.httpBody;
    }

    @Override
    public ResponseBody getResponseBody(Request request) throws IOException {
        Request.Builder newRequest = new Request.Builder()
                .url(request.url())
                .headers(request.headers())
                .method(request.method(), request.body());

        var response = sendRaw(newRequest.build());
        if (response.httpBody != null)
            return response.httpBody;
        else
            return null;
    }
}
