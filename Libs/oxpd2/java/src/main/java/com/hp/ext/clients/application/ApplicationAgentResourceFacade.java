/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.application;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.application.ApplicationAgent;

public class ApplicationAgentResourceFacade extends BaseResourceFacade implements ApplicationAgentResource {

    private RefreshOperationResourceFacade refresh = null;

    public static final String name = "applicationAgent";

    public ApplicationAgentResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
        refresh = new RefreshOperationResourceFacade(getHttpClient(), getServiceUri(),
                getPath() + "/" + RefreshOperationResourceFacade.name);
    }

    @Override
    public CompletableFuture<ApplicationAgent> getAsync(String accessToken, String queryParams)
            throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, ApplicationAgent.class);
    }

    public CompletableFuture<ApplicationAgent> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public RefreshOperationResourceFacade refresh() {
        return refresh;
    }
}
