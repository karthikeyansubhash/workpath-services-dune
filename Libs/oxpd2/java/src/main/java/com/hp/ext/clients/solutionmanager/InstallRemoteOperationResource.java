/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */

package com.hp.ext.clients.solutionmanager;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

import javax.mail.internet.ParseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hp.ext.clients.ExecutableResource;
import com.hp.ext.service.solutionManager.InstallRemoteRequest;
import com.hp.ext.service.solutionManager.Installer_InstallRemote;
import com.hp.ext.types.solutionManager.RemoteArchive;

public interface InstallRemoteOperationResource extends ExecutableResource<Installer_InstallRemote> {

        CompletableFuture<Installer_InstallRemote> executeAsync(String accessToken, InstallRemoteRequest installRemoteRequest,
                RemoteArchive remoteArchive, String queryParams) throws IOException, JsonProcessingException, URISyntaxException, ParseException;

}
