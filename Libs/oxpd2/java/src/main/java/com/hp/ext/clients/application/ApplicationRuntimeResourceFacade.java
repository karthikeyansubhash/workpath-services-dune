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
import com.hp.ext.service.application.ApplicationRuntime;

public class ApplicationRuntimeResourceFacade extends BaseResourceFacade
        implements ApplicationRuntimeResource {

    private ResetOperationResourceFacade reset = null;
    private CurrentContextResourceFacade currentContext = null;

    public static final String name = "applicationRuntime";

    public ApplicationRuntimeResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
        reset = new ResetOperationResourceFacade(getHttpClient(), getServiceUri(),
                getPath() + "/" + ResetOperationResourceFacade.name);
        currentContext = new CurrentContextResourceFacade(getHttpClient(), getServiceUri(),
                getPath() + "/" + CurrentContextResourceFacade.name);
    }

    @Override
    public CompletableFuture<ApplicationRuntime> getAsync(String accessToken, String queryParams)
            throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, ApplicationRuntime.class);
    }

    public CompletableFuture<ApplicationRuntime> getAsync(String accessToken)
            throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public ResetOperationResourceFacade reset() {
        return reset;
    }

    @Override
    public CurrentContextResourceFacade currentContext() {
        return currentContext;
    }

}
