/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.jobstatistics;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseCollectionResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.jobStatistics.JobStatisticsAgents;

public class JobStatisticsAgentsResourceFacade extends BaseCollectionResourceFacade<JobStatisticsAgentResourceFacade>
        implements JobStatisticsAgentsResource {

    public static final String name = "jobStatisticsAgents";

    public JobStatisticsAgentsResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<JobStatisticsAgents> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, JobStatisticsAgents.class);
    }

    public CompletableFuture<JobStatisticsAgents> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public JobStatisticsAgentResourceFacade getMember(String id) {
        JobStatisticsAgentResourceFacade resource = new JobStatisticsAgentResourceFacade(getHttpClient(),
                getServiceUri(), getPath() + "/" + id);
        return resource;
    }


}
