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
import com.hp.ext.service.application.CurrentContext_ResetInactivityTimer;
import com.hp.ext.service.application.ResetInactivityTimerRequest;

public class ResetInactivityTimerOperationResourceFacade extends BaseResourceFacade implements ResetInactivityTimerOperationResource {

    public static final String name = "resetInactivityTimer";

    public ResetInactivityTimerOperationResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    public CompletableFuture<CurrentContext_ResetInactivityTimer> executeAsync(String accessToken, ResetInactivityTimerRequest resource, String queryParams) throws URISyntaxException, JsonProcessingException {
        return ResourceFacadeHelper.executeResourceOperationAsync(getHttpClient(),
            createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, resource, CurrentContext_ResetInactivityTimer.class);
    }

    public CompletableFuture<CurrentContext_ResetInactivityTimer> executeAsync(String accessToken, ResetInactivityTimerRequest resource) throws URISyntaxException, JsonProcessingException {
        return executeAsync(accessToken, resource, null);
    }

    public CompletableFuture<CurrentContext_ResetInactivityTimer> executeAsync(String accessToken) throws URISyntaxException, JsonProcessingException {
        return executeAsync(accessToken, new ResetInactivityTimerRequest(), null);
    }
}
