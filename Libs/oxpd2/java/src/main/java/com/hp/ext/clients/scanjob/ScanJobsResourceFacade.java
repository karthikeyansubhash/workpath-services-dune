/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.scanjob;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hp.ext.clients.BaseCollectionResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.scanJob.ScanJob;
import com.hp.ext.service.scanJob.ScanJob_Create;
import com.hp.ext.service.scanJob.ScanJobs;

public class ScanJobsResourceFacade extends BaseCollectionResourceFacade<ScanJobResourceFacade>
        implements ScanJobsResource {

    public static final String name = "scanJobs";
    public static final String localScansName = "localScans";

    public ScanJobsResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<ScanJobs> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, ScanJobs.class);
    }

    public CompletableFuture<ScanJobs> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    public CompletableFuture<ScanJob> executeAsync(String accessToken, ScanJob_Create resource, String queryParams) throws URISyntaxException, JsonProcessingException {
        return ResourceFacadeHelper.executeResourceOperationAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, resource, ScanJob.class);
    }

    public CompletableFuture<ScanJob> executeAsync(String accessToken, ScanJob_Create resource) throws URISyntaxException, JsonProcessingException {
        return executeAsync(accessToken, resource, null);
    }

    @Override
    public ScanJobResourceFacade getMember(String id) {
        ScanJobResourceFacade resource = new ScanJobResourceFacade(getHttpClient(),
                getServiceUri(), getPath() + "/" + id);
        return resource;
    }


}
