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
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.solutionManager.InstallerOperation;

public class InstallerOperationResourceFacade extends BaseResourceFacade implements InstallerOperationResource {

    public static final String name = "installerOperation";

    public InstallerOperationResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<InstallerOperation> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
            createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, InstallerOperation.class);
    }

    public CompletableFuture<InstallerOperation> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }
}
