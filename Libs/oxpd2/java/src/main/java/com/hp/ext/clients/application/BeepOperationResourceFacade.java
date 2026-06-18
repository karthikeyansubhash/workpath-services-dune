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
import com.hp.ext.service.application.BeepRequest;
import com.hp.ext.service.application.CurrentContext_Beep;

public class BeepOperationResourceFacade extends BaseResourceFacade implements BeepOperationResource {

    public static final String name = "beep";
    
    public BeepOperationResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    public CompletableFuture<CurrentContext_Beep> executeAsync(String accessToken, BeepRequest resource, String queryParams) throws URISyntaxException, JsonProcessingException {
        return ResourceFacadeHelper.executeResourceOperationAsync(getHttpClient(),
            createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, resource, CurrentContext_Beep.class);
    }

    public CompletableFuture<CurrentContext_Beep> executeAsync(String accessToken, BeepRequest resource) throws URISyntaxException, JsonProcessingException {
        return executeAsync(accessToken, resource, null);
    }

    public CompletableFuture<CurrentContext_Beep> executeAsync(String accessToken) throws URISyntaxException, JsonProcessingException {
        return executeAsync(accessToken, new BeepRequest(), null);
    }
}
