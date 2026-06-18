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
import com.hp.ext.clients.CollectionResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.solutionManager.RuntimeRegistrations;
import com.hp.ext.service.solutionManager.RuntimeRegistration;
import com.hp.ext.service.solutionManager.RuntimeRegistration_Create;

public class RuntimeRegistrationsResourceFacade extends BaseResourceFacade
implements RuntimeRegistrationsResource, CollectionResourceFacade<RuntimeRegistrationResource> {

    public static final String name = "runtimeRegistrations";

    public RuntimeRegistrationsResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<RuntimeRegistrations> getAsync(String accessToken, String queryParams)
            throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, RuntimeRegistrations.class);
    }

    public CompletableFuture<RuntimeRegistrations> getAsync(String accessToken)
            throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public RuntimeRegistrationResourceFacade getMember(String id) {
        RuntimeRegistrationResourceFacade runtimeRegistrationResourceFacade = new RuntimeRegistrationResourceFacade(
                getHttpClient(), getServiceUri(), getPath() + "/" + id);
        return runtimeRegistrationResourceFacade;
    }

    @Override
    public CompletableFuture<RuntimeRegistration> createAsync(String accessToken, RuntimeRegistration_Create resource, String queryParams) throws JsonProcessingException, URISyntaxException {
        return ResourceFacadeHelper.createResourceAsync(getHttpClient(),
            createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, resource, RuntimeRegistration.class);
    }

    public CompletableFuture<RuntimeRegistration> createAsync(String accessToken, RuntimeRegistration_Create resource) throws JsonProcessingException, URISyntaxException {
        return createAsync(accessToken, resource, null);
    }
}
