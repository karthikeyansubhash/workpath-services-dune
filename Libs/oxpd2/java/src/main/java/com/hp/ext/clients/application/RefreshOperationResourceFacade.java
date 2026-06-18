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
import com.hp.ext.service.application.ApplicationAgent_Refresh;
import com.hp.ext.service.application.RefreshRequest;

public class RefreshOperationResourceFacade extends BaseResourceFacade
        implements RefreshOperationResource {

    public static final String name = "refresh";

    public RefreshOperationResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<ApplicationAgent_Refresh> executeAsync(String accessToken, RefreshRequest request, String queryParams)
        throws URISyntaxException, JsonProcessingException {
        URI requestUri = createRequestUri(getServiceUri(), getPath(), queryParams);
        return ResourceFacadeHelper.executeResourceOperationAsync(getHttpClient(), requestUri, accessToken, request, ApplicationAgent_Refresh.class);
    }

    public CompletableFuture<ApplicationAgent_Refresh> executeAsync(String accessToken, RefreshRequest request)
        throws URISyntaxException, JsonProcessingException {
        return executeAsync(accessToken, request, null);
    }

    public CompletableFuture<ApplicationAgent_Refresh> executeAsync(String accessToken)
        throws URISyntaxException, JsonProcessingException {
        return executeAsync(accessToken, new RefreshRequest(), null);
    }
}
