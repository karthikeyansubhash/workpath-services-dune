/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.application;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.application.StartIntent;

public class StartIntentResourceFacade extends BaseResourceFacade
    implements StartIntentResource {

    public static final String name = "startIntent";

    public StartIntentResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<StartIntent> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
            createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, StartIntent.class);
    }

    public CompletableFuture<StartIntent> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }
}
