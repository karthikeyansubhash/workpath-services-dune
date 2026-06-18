/**
 * (C) Copyright 2023 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.jobstatistics;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hp.ext.clients.BaseCollectionResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.jobStatistics.Jobs_Modify;
import com.hp.ext.service.jobStatistics.Jobs;
import com.hp.ext.service.jobStatistics.SequenceNumber;

public class JobsResourceFacade extends BaseCollectionResourceFacade<JobResourceFacade>
        implements JobsResource {

    public static final String name = "jobs";

    public JobsResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<Jobs> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, Jobs.class);
    }

    public CompletableFuture<Jobs> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    public CompletableFuture<Jobs> modifyAsync(String accessToken, Jobs_Modify resource, String queryParams) throws URISyntaxException, JsonProcessingException {
        return ResourceFacadeHelper.modifyResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, resource, Jobs.class);
    }

    public CompletableFuture<Jobs> modifyAsync(String accessToken, Jobs_Modify resource) throws URISyntaxException, JsonProcessingException {
        return modifyAsync(accessToken, resource, null);
    }

    @Override
    public JobResourceFacade getMember(String sequenceNumber) {
        Long snLong = Long.parseLong(sequenceNumber);
        JobResourceFacade resource = new JobResourceFacade(getHttpClient(),
                getServiceUri(), getPath() + "/" + snLong);
        return resource;
    }

    public JobResourceFacade getMember(SequenceNumber sequenceNumber) {
        JobResourceFacade resource = new JobResourceFacade(getHttpClient(),
                getServiceUri(), getPath() + "/" + sequenceNumber.getValue());
        return resource;
    }

    public JobResourceFacade getMember(Long sequenceNumber) {
        JobResourceFacade resource = new JobResourceFacade(getHttpClient(),
                getServiceUri(), getPath() + "/" + sequenceNumber);
        return resource;
    }
}
