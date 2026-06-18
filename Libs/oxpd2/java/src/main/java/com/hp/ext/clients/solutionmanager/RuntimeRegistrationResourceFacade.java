/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.solutionmanager;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.service.solutionManager.RuntimeRegistration;
import com.hp.ext.types.base.DeleteContent;
import com.hp.ext.clients.ResourceFacadeHelper;

public class RuntimeRegistrationResourceFacade extends BaseResourceFacade
        implements RuntimeRegistrationResource {
    public static final String name = "runtimeRegistration";

    public RuntimeRegistrationResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<RuntimeRegistration> getAsync(String accessToken, String queryParams)
            throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, RuntimeRegistration.class);
    }

    public CompletableFuture<RuntimeRegistration> getAsync(String accessToken)
            throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public CompletableFuture<DeleteContent> deleteAsync(String accessToken, String queryParams)
            throws URISyntaxException {
        return ResourceFacadeHelper.deleteResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, DeleteContent.class);
    }

    public CompletableFuture<DeleteContent> deleteAsync(String accessToken) throws URISyntaxException {
        return deleteAsync(accessToken, null);
    }
}
