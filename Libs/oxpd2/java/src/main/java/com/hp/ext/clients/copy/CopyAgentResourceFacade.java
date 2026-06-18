/**
 * (C) Copyright 2023 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.copy;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.copy.CopyAgent;

public class CopyAgentResourceFacade extends BaseResourceFacade implements CopyAgentResource {

    private CopyJobsResourceFacade copyJobs = null;
    private StoredJobsResourceFacade storedJobs = null;

    public static final String name = "copyAgent";

    public CopyAgentResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
        copyJobs = new CopyJobsResourceFacade(getHttpClient(), getServiceUri(),
                getPath() + "/" + CopyJobsResourceFacade.name);
        storedJobs = new StoredJobsResourceFacade(getHttpClient(), getServiceUri(),
                getPath() + "/" + StoredJobsResourceFacade.name);
    }

    public CompletableFuture<CopyAgent> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, CopyAgent.class);
    }

    public CompletableFuture<CopyAgent> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public CopyJobsResourceFacade copyJobs() {
        return copyJobs;
    }
    @Override
    public StoredJobsResourceFacade storedJobs() {
        return storedJobs;
    }
}
