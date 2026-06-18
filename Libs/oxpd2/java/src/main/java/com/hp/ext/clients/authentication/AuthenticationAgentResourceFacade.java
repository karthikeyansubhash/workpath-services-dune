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
import com.hp.ext.service.authentication.AuthenticationAgent;

public class AuthenticationAgentResourceFacade extends BaseResourceFacade
        implements AuthenticationAgentResource {

    private LoginOperationResourceFacade login = null;

    public static final String name = "authenticationAgent";

    public AuthenticationAgentResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
        login = new LoginOperationResourceFacade(getHttpClient(), getServiceUri(),
                getPath() + "/" + LoginOperationResourceFacade.name);
    }

    @Override
    public CompletableFuture<AuthenticationAgent> getAsync(String accessToken, String queryParams)
            throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, AuthenticationAgent.class);
    }

    public CompletableFuture<AuthenticationAgent> getAsync(String accessToken)
            throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public LoginOperationResourceFacade login() {
        return login;
    }
}
