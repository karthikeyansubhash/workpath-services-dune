/**
 * (C) Copyright 2023 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.copy;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hp.ext.clients.BaseCollectionResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.copy.CopyJob;
import com.hp.ext.service.copy.CopyJob_Create;
import com.hp.ext.service.copy.CopyJobs;

public class CopyJobsResourceFacade extends BaseCollectionResourceFacade<CopyJobResourceFacade>
        implements CopyJobsResource {

    public static final String name = "copyJobs";

    public CopyJobsResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<CopyJobs> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, CopyJobs.class);
    }

    public CompletableFuture<CopyJobs> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    public CompletableFuture<CopyJob> executeAsync(String accessToken, CopyJob_Create resource, String queryParams) throws URISyntaxException, JsonProcessingException {
        return ResourceFacadeHelper.executeResourceOperationAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, resource, CopyJob.class);
    }

    public CompletableFuture<CopyJob> executeAsync(String accessToken, CopyJob_Create resource) throws URISyntaxException, JsonProcessingException {
        return executeAsync(accessToken, resource, null);
    }

    @Override
    public CopyJobResourceFacade getMember(String id) {
        CopyJobResourceFacade resource = new CopyJobResourceFacade(getHttpClient(),
                getServiceUri(), getPath() + "/" + id);
        return resource;
    }
}
