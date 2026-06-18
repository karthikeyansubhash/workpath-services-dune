/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.clients;


import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * CDM HTTP Client class
 */
public class CDMClient extends DeviceClient {
    private boolean isTls = true;
    private int port = 0;

    public CDMClient(OkHttpClient httpclient, String deviceIpAddress, String bearerToken) {
        this.TAG = TAG + "/CDM";
        this.httpClient = httpclient;
        this.deviceIpAddress = deviceIpAddress;
        this.token = bearerToken;
    }

    public CDMClient(OkHttpClient httpclient, String deviceIpAddress, String bearerToken, boolean isTls, int port) {
        this.TAG = TAG + "/CDM";
        this.httpClient = httpclient;
        this.deviceIpAddress = deviceIpAddress;
        this.token = bearerToken;
        this.isTls = isTls;
        this.port = port;
    }

    /**
     * send HTTP GET Request and return response
     *
     * @param url target URL
     * @return a pair of HTTP Status code (Integer) and Response String
     * @throws IOException if the device responds with a http error code
     */
    public CDMResponse<String> sendGetRequest(String url) throws IOException {
        Request.Builder request = new Request.Builder()
                .header("Content-Type", "application/json")
                .get();

        synchronized (lock) {
            request.url(DeviceConnectorHelper.generateHttpPrefixURL(this.deviceIpAddress, this.port, this.isTls) + url);
            request.addHeader("Authorization", "Bearer " + token);
        }
        return send(request.build(), false);
    }

    /**
     * send HTTP GET Request and return response
     *
     * @param url target URL
     * @param httpErrorResponseCodeRequired true if Http Error Status code is required, false otherwise
     * @return a pair of HTTP Status code (Integer) and Response String
     * @throws IOException if the device responds with a http error code
     */
    public CDMResponse<String> sendGetRequest(String url, boolean httpErrorResponseCodeRequired) throws IOException {
        Request.Builder request = new Request.Builder()
                .header("Content-Type", "application/json")
                .get();

        synchronized (lock) {
            request.url(DeviceConnectorHelper.generateHttpPrefixURL(this.deviceIpAddress, this.port, this.isTls) + url);
            request.addHeader("Authorization", "Bearer " + token);
        }
        return send(request.build(), httpErrorResponseCodeRequired);
    }

    /**
     * send HTTP PATCH Request and return response
     *
     * @param url         target URL
     * @param requestBody request body - json string
     * @return a pair of HTTP Status code (Integer) and Response String
     * @throws IOException if the device responds with a http error code
     */
    public CDMResponse<String> sendPatchRequest(String url, String requestBody) throws IOException {
        Request.Builder request = new Request.Builder()
                .header("Content-Type", "application/json")
                .patch(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestBody));

        synchronized (lock) {
            request.url(DeviceConnectorHelper.generateHttpPrefixURL(this.deviceIpAddress, this.port, this.isTls) + url);
            request.addHeader("Authorization", "Bearer " + this.token);
        }
        return send(request.build(), false);
    }

    /**
     * send HTTP POST Request and return response
     *
     * @param url         target URL
     * @param requestBody request body - json string
     * @return a pair of HTTP Status code (Integer) and Response String
     * @throws IOException if the device responds with a http error code
     */
    public CDMResponse<String> sendPostRequest(String url, String requestBody) throws IOException {
        Request.Builder request = new Request.Builder()
                .header("Content-Type", "application/json")
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestBody));

        synchronized (lock) {
            request.url(DeviceConnectorHelper.generateHttpPrefixURL(this.deviceIpAddress, this.port, this.isTls) + url);
            request.addHeader("Authorization", "Bearer " + this.token);
        }
        return send(request.build(), false);
    }

    /**
     * send HTTP PUT Request and return response
     *
     * @param url         target URL
     * @param requestBody request body - json string
     * @return a pair of HTTP Status code (Integer) and Response String
     * @throws IOException if the device responds with a http error code
     */
    public CDMResponse<String> sendPutRequest(String url, String requestBody) throws IOException {
        Request.Builder request = new Request.Builder()
                .header("Content-Type", "application/json")
                .put(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestBody));

        synchronized (lock) {
            request.url(DeviceConnectorHelper.generateHttpPrefixURL(this.deviceIpAddress,this.port, this.isTls) + url);
            request.addHeader("Authorization", "Bearer " + this.token);
        }
        return send(request.build(), false);
    }

    /**
     * send HTTP DELETE Request and return response
     *
     * @param url target URL
     * @return a pair of HTTP Status code (Integer) and Response String
     * @throws IOException if the device responds with a http error code
     */
    public CDMResponse<String> sendDeleteRequest(String url) throws IOException {
        Request.Builder request = new Request.Builder()
                .header("Content-Type", "application/json")
                .delete();

        synchronized (lock) {
            request.url(DeviceConnectorHelper.generateHttpPrefixURL(this.deviceIpAddress, this.port, this.isTls) + url);
            request.header("Authorization", "Bearer " + this.token);
        }
        return send(request.build(), false);
    }
}
