/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */

package com.hp.ext.clients.solutionmanager;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

import javax.mail.internet.ParseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hp.ext.clients.ExecutableResource;
import com.hp.ext.service.solutionManager.Context_Replace;
import com.hp.ext.service.solutionManager.InstallSolutionRequest;
import com.hp.ext.service.solutionManager.Installer_InstallSolution;

public interface InstallSolutionOperationResource extends ExecutableResource<Installer_InstallSolution> {

    CompletableFuture<Installer_InstallSolution> executeAsync( String accessToken, InstallSolutionRequest installSolutionRequest, InputStream solutionBundle, 
            String solutionBundleFilename, Context_Replace context, String queryParams) throws IOException, JsonProcessingException, URISyntaxException, ParseException;
}
