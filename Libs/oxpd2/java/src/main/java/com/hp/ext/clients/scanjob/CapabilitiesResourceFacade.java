/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.scanjob;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.OXPdHttpRequestException;
import com.hp.ext.service.scanJob.Capabilities;

public class CapabilitiesResourceFacade extends BaseResourceFacade implements CapabilitiesResource {

    public static final String name = "capabilities";

    public CapabilitiesResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<Capabilities> getAsync(String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), null, Capabilities.class);
    }

    public CompletableFuture<Capabilities> getAsync()
            throws InterruptedException, ExecutionException, OXPdHttpRequestException, IOException, URISyntaxException {
        return getAsync(null);
    }
}
