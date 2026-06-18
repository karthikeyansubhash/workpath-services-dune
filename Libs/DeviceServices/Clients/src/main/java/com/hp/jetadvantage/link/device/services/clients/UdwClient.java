/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.clients;

import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Underwear HTTP client class : testing purpose only for the Dune simulator
 */
public class UdwClient extends DeviceClient {
    private String deviceHostHeader;

    public UdwClient(OkHttpClient httpclient, String ipAddress) {
        this.TAG = TAG + "/UDW";
        this.httpClient = httpclient;
        this.deviceIpAddress = ipAddress;
        this.deviceHostHeader = ipAddress;
    }

    public UdwClient(OkHttpClient httpclient, String ipAddress, String httpHostHeader) {
        this.TAG = TAG + "/UDW";
        this.httpClient = httpclient;
        this.deviceIpAddress = ipAddress;

        if(httpHostHeader.isEmpty()) {
            this.deviceHostHeader = deviceIpAddress;
        }
        else {
            this.deviceHostHeader = httpHostHeader;
        }
    }

    public String getTestToken() {
        try {
            var response = sendUnderwareCommand("1.0.0", "mainApp", "OAuth2Standard PUB_testCreateWorkpathScopeToken");
            JSONObject jsonObject = new JSONObject(response.httpBody);
            String token = new String(Base64.decode(jsonObject.getString("response"), Base64.NO_WRAP));
            token = token.replaceAll("[\\r\\n]", "");

            Log.i(TAG, "DuneSimulatorToken@@@ : " + token);
            return token;
        } catch (Exception e) {
            Log.e(TAG, "REST call failed to execute httpsClient newCall() :" + e.getMessage());
            return null;
        }
    }

    public CDMResponse<String> sendUnderwareCommand(String version, String target, String command) throws IOException {
        String tokenUri = "/hp/device/WSFramework/underware/v1/command";
        String url = DeviceConnectorHelper.generateHttpsPrefixURL(this.deviceIpAddress, 0) + tokenUri;
        JSONObject requestBody = new JSONObject();

        try {
            requestBody.put("version", version);
            requestBody.put("targetService", target);
            requestBody.put("blocking", "true");
            requestBody.put("command", command);
        } catch (JSONException e) {
            throw new RuntimeException("JSONException: Invalid argument");
        }

        Request request = new Request.Builder()
                .url(url)
                .header("Content-Type", "application/json")
                .header("Host", deviceHostHeader)
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestBody.toString()))
                .build();

        return send(request, false);
    }

    public CDMResponse<String> initiateAppLaunch(String applicationId) throws IOException {
        return initiateAppLaunch(applicationId, null);
    }

    public CDMResponse<String> initiateAppLaunch(String applicationId , String solutionToken) throws IOException {
        String uri = "/ext/application/v1/applicationAccessPoints/" + applicationId + "/initiateLaunch";
        String url = DeviceConnectorHelper.generateHttpsPrefixURL(this.deviceIpAddress, 0) + uri;
        JSONObject requestBody = new JSONObject();

        try {
            requestBody.put("content", "");
            requestBody.put("startIntent", "");
        } catch (JSONException e) {
            throw new RuntimeException("JSONException: Invalid argument");
        }

        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .header("Content-Type", "application/json")
                .header("Host", deviceHostHeader)
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestBody.toString()));

        if (solutionToken != null) {
            requestBuilder.header("Authorization", "Bearer " + solutionToken);
        }

        Request request = requestBuilder.build();
        return send(request, false);
    }

    public CDMResponse<String> closeCurrentApp() throws IOException {
        return closeCurrentApp(null);
    }
    public CDMResponse<String> closeCurrentApp(String UIContextToken) throws IOException {
        String uri = "/ext/application/v1/applicationRuntime/currentContext/exit";
        String url = DeviceConnectorHelper.generateHttpsPrefixURL(this.deviceIpAddress, 0) + uri;
        JSONObject requestBody = new JSONObject();

        try {
            requestBody.put("content", "");
            requestBody.put("startIntent", "");
        } catch (JSONException e) {
            throw new RuntimeException("JSONException: Invalid argument");
        }

        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .header("Content-Type", "application/json")
                .header("Host", deviceHostHeader)
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestBody.toString()));

        if (UIContextToken != null) {
            requestBuilder.header("Authorization", "Bearer " + UIContextToken);
        }
        Request request = requestBuilder.build();
        return send(request, false);
    }
}
