/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.jobstatistics;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.jobStatistics.JobStatisticsAgent;

public class JobStatisticsAgentResourceFacade extends BaseResourceFacade implements JobStatisticsAgentResource {

    private JobsResourceFacade jobs = null;

    public static final String name = "jobStatisticsAgent";

    public JobStatisticsAgentResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
        jobs = new JobsResourceFacade(getHttpClient(), getServiceUri(),
                getPath() + "/" + JobsResourceFacade.name);
    }

    public CompletableFuture<JobStatisticsAgent> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, JobStatisticsAgent.class);
    }

    public CompletableFuture<JobStatisticsAgent> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public JobsResourceFacade jobs() {
        return jobs;
    }
}
