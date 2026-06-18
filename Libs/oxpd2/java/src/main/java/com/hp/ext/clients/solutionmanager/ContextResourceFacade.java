/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.solutionmanager;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.solutionManager.Context;
import com.hp.ext.service.solutionManager.Context_Modify;
import com.hp.ext.service.solutionManager.Context_Replace;

public class ContextResourceFacade extends BaseResourceFacade implements ContextResource {

    public static final String name = "context";

    public ContextResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<Context> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(), createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, Context.class);
    }

    public CompletableFuture<Context> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public CompletableFuture<Context> modifyAsync(String accessToken, Context_Modify resource, String queryParams) throws JsonProcessingException, URISyntaxException  {
        return ResourceFacadeHelper.modifyResourceAsync(getHttpClient(), createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, resource, Context.class);
    }

    public CompletableFuture<Context> modifyAsync(String accessToken, Context_Modify resource) throws JsonProcessingException, URISyntaxException {
        return modifyAsync(accessToken, resource, null);
    }

    @Override
    public CompletableFuture<Context> replaceAsync(String accessToken, Context_Replace resource, String queryParams) throws JsonProcessingException, URISyntaxException {
        return ResourceFacadeHelper.replaceResourceAsync(getHttpClient(), createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, resource, Context.class);
    }

    public CompletableFuture<Context> replaceAsync(String accessToken, Context_Replace resource) throws JsonProcessingException, URISyntaxException {
        return replaceAsync(accessToken, resource, null);
    }
}
