/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.copy;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseCollectionResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.copy.StoredJobs;

public class StoredJobsResourceFacade extends BaseCollectionResourceFacade<StoredJobResourceFacade>
        implements StoredJobsResource {

    public static final String name = "storedJobs";

    public StoredJobsResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<StoredJobs> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, StoredJobs.class);
    }

    public CompletableFuture<StoredJobs> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public StoredJobResourceFacade getMember(String id) {
        StoredJobResourceFacade resource = new StoredJobResourceFacade(getHttpClient(),
                getServiceUri(), getPath() + "/" + id);
        return resource;
    }
}
