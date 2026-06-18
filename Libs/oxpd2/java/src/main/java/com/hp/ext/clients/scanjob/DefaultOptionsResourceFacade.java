/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.scanjob;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.scanJob.DefaultOptions;

public class DefaultOptionsResourceFacade extends BaseResourceFacade implements DefaultOptionsResource {

    public static final String name = "defaultOptions";

    public DefaultOptionsResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<DefaultOptions> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, DefaultOptions.class);
    }

    public CompletableFuture<DefaultOptions> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }
}
