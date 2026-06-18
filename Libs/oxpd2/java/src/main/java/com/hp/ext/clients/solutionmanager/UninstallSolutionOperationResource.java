/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */

package com.hp.ext.clients.solutionmanager;

import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hp.ext.clients.ExecutableResource;
import com.hp.ext.service.solutionManager.Installer_UninstallSolution;
import com.hp.ext.service.solutionManager.UninstallSolutionRequest;

public interface UninstallSolutionOperationResource extends ExecutableResource<Installer_UninstallSolution> {
    CompletableFuture<Installer_UninstallSolution> executeAsync(String accessToken, UninstallSolutionRequest uninstallSolutionRequest, String queryParams) 
        throws URISyntaxException, JsonProcessingException;
}

