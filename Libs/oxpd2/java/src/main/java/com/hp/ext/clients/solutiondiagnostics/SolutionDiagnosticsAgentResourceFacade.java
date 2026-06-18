/**
 * (C) Copyright 2023 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.solutiondiagnostics;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.solutionDiagnostics.SolutionDiagnosticsAgent;

public class SolutionDiagnosticsAgentResourceFacade extends BaseResourceFacade implements SolutionDiagnosticsAgentResource {

    public static final String name = "solutionDiagnosticsAgent";

    private LogResourceFacade log = null;

    public SolutionDiagnosticsAgentResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
        log = new LogResourceFacade(httpClient, serviceUri, path + "/" + LogResourceFacade.name);
    }

    @Override
    public CompletableFuture<SolutionDiagnosticsAgent> getAsync(String accessToken, String queryParams)
            throws URISyntaxException {
        URI requestUri = createRequestUri(getServiceUri(), getPath(), queryParams);
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(), requestUri, accessToken, SolutionDiagnosticsAgent.class);
    }

    public CompletableFuture<SolutionDiagnosticsAgent> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public LogResourceFacade log() {
        return this.log;
    }

}
