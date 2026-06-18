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
import com.hp.ext.service.application.CurrentContext_Exit;
import com.hp.ext.service.application.ExitRequest;

public class ExitOperationResourceFacade extends BaseResourceFacade implements ExitOperationResource {

    public static final String name = "exit";

    public ExitOperationResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    public CompletableFuture<CurrentContext_Exit> executeAsync(String accessToken, ExitRequest resource, String queryParams) throws URISyntaxException, JsonProcessingException {
        return ResourceFacadeHelper.executeResourceOperationAsync(getHttpClient(),
            createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, resource, CurrentContext_Exit.class);
    }

    public CompletableFuture<CurrentContext_Exit> executeAsync(String accessToken, ExitRequest resource) throws URISyntaxException, JsonProcessingException {
        return executeAsync(accessToken, resource, null);
    }

    public CompletableFuture<CurrentContext_Exit> executeAsync(String accessToken) throws URISyntaxException, JsonProcessingException {
        return executeAsync(accessToken, new ExitRequest(), null);
    }
}
