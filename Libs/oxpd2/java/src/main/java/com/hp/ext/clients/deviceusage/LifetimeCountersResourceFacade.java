/**
 * (C) Copyright 2023 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.deviceusage;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.deviceUsage.LifetimeCounters;

public class LifetimeCountersResourceFacade extends BaseResourceFacade implements LifetimeCountersResource {

    public static final String name = "lifetimeCounters";

    public LifetimeCountersResourceFacade(HttpClient httpClient, URI baseUri, String path) {
        super(httpClient, baseUri, path);
    }

    public CompletableFuture<LifetimeCounters> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, LifetimeCounters.class);
    }

    public CompletableFuture<LifetimeCounters> getAsync(String accessToken) throws URISyntaxException  {
        return getAsync(accessToken, null);
    }
}
