/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.application;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.application.ApplicationAccessPoint_InitiateLaunch;
import com.hp.ext.service.application.InitiateLaunchRequest;

public class InitiateLaunchOperationResourceFacade extends BaseResourceFacade implements InitiateLaunchOperationResource {

    public static final String name = "initiateLaunch";

    public InitiateLaunchOperationResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<ApplicationAccessPoint_InitiateLaunch> executeAsync(String accessToken,
            InitiateLaunchRequest initiateLaunchRequest, Object startIntent, String queryParams)
            throws URISyntaxException, IOException {
        URI requestUri = createRequestUri(getServiceUri(), getPath(), queryParams);
        ObjectMapper mapper = new ObjectMapper();

        if (null != startIntent) {
            MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.MIXED);
            requestBodyBuilder.addFormDataPart("content", "content.json",
                RequestBody.create(
                    mapper.writeValueAsString(initiateLaunchRequest),
                    MediaType.parse("application/json")));

            requestBodyBuilder.addFormDataPart("startIntent", "content.json",
                RequestBody.create(
                    mapper.writeValueAsString(startIntent),
                    MediaType.parse("application/json")));

            return ResourceFacadeHelper.executeResourceOperationAsync(getHttpClient(), requestUri, accessToken,
                requestBodyBuilder.build(), ApplicationAccessPoint_InitiateLaunch.class);
        } else {
            return ResourceFacadeHelper.executeResourceOperationAsync(getHttpClient(), requestUri, accessToken, initiateLaunchRequest, ApplicationAccessPoint_InitiateLaunch.class);
        }
    }

    public CompletableFuture<ApplicationAccessPoint_InitiateLaunch> executeAsync(String accessToken,
        InitiateLaunchRequest initiateLaunchRequest, Object startIntent)
        throws URISyntaxException, IOException {
            return executeAsync(accessToken, initiateLaunchRequest, startIntent, null);
    }

    public CompletableFuture<ApplicationAccessPoint_InitiateLaunch> executeAsync(String accessToken,
        InitiateLaunchRequest initiateLaunchRequest)
        throws URISyntaxException, IOException {
            return executeAsync(accessToken, initiateLaunchRequest, null, null);
    }

    public CompletableFuture<ApplicationAccessPoint_InitiateLaunch> executeAsync(String accessToken,
        InitiateLaunchRequest initiateLaunchRequest, String queryParms)
        throws URISyntaxException, IOException {
            return executeAsync(accessToken, initiateLaunchRequest, null, queryParms);
    }
}
