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
import com.hp.ext.clients.CollectionResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.solutionDiagnostics.SolutionDiagnosticsAgents;

public class SolutionDiagnosticsAgentsResourceFacade extends BaseResourceFacade implements SolutionDiagnosticsAgentsResource, CollectionResourceFacade<SolutionDiagnosticsAgentResourceFacade> {

    public static final String name = "solutionDiagnosticsAgents";

    public SolutionDiagnosticsAgentsResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<SolutionDiagnosticsAgents> getAsync(String accessToken, String queryParams)
            throws URISyntaxException {

        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, SolutionDiagnosticsAgents.class);
    }

    public CompletableFuture<SolutionDiagnosticsAgents> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public SolutionDiagnosticsAgentResourceFacade getMember(String id) {
        SolutionDiagnosticsAgentResourceFacade agentResource = new SolutionDiagnosticsAgentResourceFacade(getHttpClient(), getServiceUri(), getPath() + "/" + id);
        return agentResource;
    }

}
