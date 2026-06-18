/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.application;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.application.ApplicationRuntime_Reset;
import com.hp.ext.service.application.ResetRequest;

public class ResetOperationResourceFacade extends BaseResourceFacade implements ResetOperationResource {
    public static final String name = "reset";

    public ResetOperationResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<ApplicationRuntime_Reset> executeAsync(String accessToken, ResetRequest resource, String queryParams)
            throws JsonProcessingException, URISyntaxException {
        URI requestUri = createRequestUri(getServiceUri(), getPath(), queryParams);
        return ResourceFacadeHelper.executeResourceOperationAsync(getHttpClient(), requestUri, accessToken, resource,
                ApplicationRuntime_Reset.class);
    }

    public CompletableFuture<ApplicationRuntime_Reset> executeAsync(String accessToken, String queryParams)
            throws JsonProcessingException, URISyntaxException {
       return executeAsync(accessToken, new ResetRequest(), queryParams);
    }

    public CompletableFuture<ApplicationRuntime_Reset> executeAsync(String accessToken)
            throws JsonProcessingException, URISyntaxException {
        return executeAsync(accessToken, new ResetRequest(), null);
    }
}
