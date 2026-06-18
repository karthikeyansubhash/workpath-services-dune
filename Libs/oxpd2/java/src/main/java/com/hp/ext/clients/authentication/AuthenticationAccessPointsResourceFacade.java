/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.authentication;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseCollectionResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.authentication.AuthenticationAccessPoints;

public class AuthenticationAccessPointsResourceFacade
        extends BaseCollectionResourceFacade<AuthenticationAccessPointResourceFacade>
        implements AuthenticationAccessPointsResource {

    public static final String name = "authenticationAccessPoints";

    public AuthenticationAccessPointsResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<AuthenticationAccessPoints> getAsync(String accessToken, String queryParams)
            throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken,
                AuthenticationAccessPoints.class);
    }

    public CompletableFuture<AuthenticationAccessPoints> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public AuthenticationAccessPointResourceFacade getMember(String id) {
        AuthenticationAccessPointResourceFacade resource = new AuthenticationAccessPointResourceFacade(
                getHttpClient(),
                getServiceUri(), getPath() + "/" + id);
        return resource;
    }
}
