/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */

package com.hp.ext.clients.solutionmanager;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.solutionManager.Installer_UninstallSolution;
import com.hp.ext.service.solutionManager.UninstallSolutionRequest;

public class UninstallSolutionOperationResourceFacade extends BaseResourceFacade implements UninstallSolutionOperationResource {

    public static final String name = "uninstallSolution";

    public UninstallSolutionOperationResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<Installer_UninstallSolution> executeAsync(String accessToken, UninstallSolutionRequest uninstallSolutionRequest,
            String queryParams) throws URISyntaxException, JsonProcessingException {
        URI requestUri = createRequestUri(getServiceUri(), getPath(), queryParams);
        return ResourceFacadeHelper.executeResourceOperationAsync(
            getHttpClient(), requestUri, accessToken, uninstallSolutionRequest, Installer_UninstallSolution.class);
    }

    public CompletableFuture<Installer_UninstallSolution> executeAsync(String accessToken, UninstallSolutionRequest uninstallSolutionRequest)
            throws URISyntaxException, JsonProcessingException {
        return executeAsync(accessToken, uninstallSolutionRequest, null);
    }

}
