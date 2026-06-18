/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.authentication;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.service.authentication.AuthenticationAccessPoint;

public class AuthenticationAccessPointResourceFacade extends BaseResourceFacade
        implements AuthenticationAccessPointResource {

    private InitiateLoginOperationResourceFacade initiateLogin = null;

    public static final String name = "authenticationAccessPoint";

    public AuthenticationAccessPointResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
        initiateLogin = new InitiateLoginOperationResourceFacade(getHttpClient(), getServiceUri(),
                getPath() + "/" + InitiateLoginOperationResourceFacade.name);
    }

    @Override
    public CompletableFuture<AuthenticationAccessPoint> getAsync(String accessToken, String queryParams)
            throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken,
                AuthenticationAccessPoint.class);

    }

    public CompletableFuture<AuthenticationAccessPoint> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public InitiateLoginOperationResourceFacade initiateLogin() {
        return initiateLogin;
    }
}
