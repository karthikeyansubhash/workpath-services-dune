/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.scanjob;

import java.net.URI;
import java.net.URISyntaxException;

import com.hp.ext.service.scanJob.ScanJob;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.scanJob.ScanJobAgent;

public class ScanJobAgentResourceFacade extends BaseResourceFacade implements ScanJobAgentResource {

    private ScanJobsResourceFacade scanJobs = null;
    private ScanJobsResourceFacade localScans = null;

    public static final String name = "scanJobAgent";

    public ScanJobAgentResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
        scanJobs = new ScanJobsResourceFacade(getHttpClient(), getServiceUri(),
                getPath() + "/" + ScanJobsResourceFacade.name);
        localScans = new ScanJobsResourceFacade(getHttpClient(), getServiceUri(),
            getPath() + "/" + ScanJobsResourceFacade.localScansName);
    }

    public CompletableFuture<ScanJobAgent> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, ScanJobAgent.class);
    }

    public CompletableFuture<ScanJobAgent> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public ScanJobsResourceFacade scanJobs() {
        return scanJobs;
    }

    @Override
    public ScanJobsResourceFacade localScans() {
        return localScans;
    }
}
