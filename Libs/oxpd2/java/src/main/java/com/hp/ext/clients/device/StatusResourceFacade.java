/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.device;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.device.Status;

/**
 * The Facade for the Device Service Status resource
 */
public class StatusResourceFacade extends BaseResourceFacade
        implements StatusResource {

    public static final String name = "status";

    public StatusResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<Status> getAsync(String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), null, Status.class);
    }

    public CompletableFuture<Status> getAsync() throws URISyntaxException {
        return getAsync(null);
    }
}
