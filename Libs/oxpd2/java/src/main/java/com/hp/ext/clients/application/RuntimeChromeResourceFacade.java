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
import com.hp.ext.service.application.RuntimeChrome;
import com.hp.ext.service.application.RuntimeChrome_Modify;
import com.hp.ext.service.application.RuntimeChrome_Replace;

public class RuntimeChromeResourceFacade extends BaseResourceFacade implements RuntimeChromeResource {

    public static final String name = "runtimeChrome";

    public RuntimeChromeResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<RuntimeChrome> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
            createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, RuntimeChrome.class);
    }

    public CompletableFuture<RuntimeChrome> getAsync(String accessToken) throws URISyntaxException {
       return getAsync(accessToken, null);
    }

    @Override
    public CompletableFuture<RuntimeChrome> modifyAsync(String accessToken, RuntimeChrome_Modify resource, String queryParams)
        throws JsonProcessingException, URISyntaxException {
            return ResourceFacadeHelper.modifyResourceAsync(getHttpClient(), createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, resource, RuntimeChrome.class);
    }

    public CompletableFuture<RuntimeChrome> modifyAsync(String accessToken, RuntimeChrome_Modify resource)
        throws JsonProcessingException, URISyntaxException {
        return modifyAsync(accessToken, resource, null);
    }

    @Override
    public CompletableFuture<RuntimeChrome> replaceAsync(String accessToken, RuntimeChrome_Replace resource,
            String queryParams) throws JsonProcessingException, URISyntaxException {
                return ResourceFacadeHelper.replaceResourceAsync(getHttpClient(), createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, resource, RuntimeChrome.class);
    }

    public CompletableFuture<RuntimeChrome> replaceAsync(String accessToken, RuntimeChrome_Replace resource)
        throws JsonProcessingException, URISyntaxException {
        return replaceAsync(accessToken, resource, null);
    }
}
