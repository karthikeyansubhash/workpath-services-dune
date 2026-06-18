/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.copy;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.copy.StoredJob;

public class StoredJobResourceFacade extends BaseResourceFacade implements StoredJobResource {
    private ReleaseStoredJobOperationResourceFacade release = null;
    private RemoveStoredJobOperationResourceFacade remove = null;

    public static final String name = "storedJob";

    public StoredJobResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
        release = new ReleaseStoredJobOperationResourceFacade(getHttpClient(), getServiceUri(),
                getPath() + "/" + ReleaseStoredJobOperationResourceFacade.name);
        remove = new RemoveStoredJobOperationResourceFacade(getHttpClient(), getServiceUri(),
                getPath() + "/" + RemoveStoredJobOperationResourceFacade.name);
    }

    public CompletableFuture<StoredJob> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, StoredJob.class);
    }

    public CompletableFuture<StoredJob> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public ReleaseStoredJobOperationResourceFacade release() {
        return release;
    }
        @Override
    public RemoveStoredJobOperationResourceFacade remove() {
        return remove;
    }
}
