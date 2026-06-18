/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.solutionmanager;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.OXPdHttpRequestException;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.solutionManager.Installer;

public class InstallerResourceFacade extends BaseResourceFacade implements InstallerResource {

    private InstallerOperationsResourceFacade installerOperations = null;
    private InstallSolutionOperationResourceFacade installSolution = null;
    private InstallRemoteOperationResourceFacade installRemote = null;
    private UninstallSolutionOperationResourceFacade uninstallSolution = null;

    public static final String name = "installer";

    /**
     * @param httpClient The instance of the HttpClient to use
     * @param serviceUri The fully-formed service URI of the service host
     * @param path       for Installer end point
     */
    public InstallerResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
        installerOperations = new InstallerOperationsResourceFacade(httpClient, serviceUri, path +
                "/" + InstallerOperationsResourceFacade.name);
        installSolution = new InstallSolutionOperationResourceFacade(httpClient, serviceUri, path +
                "/" + InstallSolutionOperationResourceFacade.name);
        installRemote = new InstallRemoteOperationResourceFacade(httpClient, serviceUri, path +
                "/" + InstallRemoteOperationResourceFacade.name);
        uninstallSolution = new UninstallSolutionOperationResourceFacade(httpClient, serviceUri, path +      
                "/" + UninstallSolutionOperationResourceFacade.name);

    }

    @Override
    public CompletableFuture<Installer> getAsync(String accessToken, String queryParams)
            throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, Installer.class);

    }

    public CompletableFuture<Installer> getAsync(String accessToken)
            throws InterruptedException, ExecutionException, OXPdHttpRequestException, IOException, URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public InstallerOperationsResourceFacade installerOperations() {
        return this.installerOperations;
    }

    @Override
    public InstallSolutionOperationResourceFacade installSolution() {
        return this.installSolution;
    }

    @Override   
    public InstallRemoteOperationResourceFacade installRemote() {
        return this.installRemote;
    }

    @Override
    public UninstallSolutionOperationResourceFacade uninstallSolution() {
        return this.uninstallSolution;
    }

}
