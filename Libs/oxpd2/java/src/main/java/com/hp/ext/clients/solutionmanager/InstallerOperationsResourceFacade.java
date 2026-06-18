/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.solutionmanager;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.CollectionResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.solutionManager.InstallerOperations;

public class InstallerOperationsResourceFacade extends BaseResourceFacade
        implements CollectionResourceFacade<InstallerOperationResourceFacade>, InstallerOperationsResource {

    public static final String name = "installerOperations";

    public InstallerOperationsResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<InstallerOperations> getAsync(String accessToken, String queryParams)
            throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, InstallerOperations.class);
    }

    public CompletableFuture<InstallerOperations> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public InstallerOperationResourceFacade getMember(String id) {
        return new InstallerOperationResourceFacade(getHttpClient(), getServiceUri(), getPath() + "/" + id);
    }
}
