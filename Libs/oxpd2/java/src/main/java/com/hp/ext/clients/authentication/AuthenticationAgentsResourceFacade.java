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
import com.hp.ext.service.authentication.AuthenticationAgents;

public class AuthenticationAgentsResourceFacade
        extends BaseCollectionResourceFacade<AuthenticationAgentResourceFacade>
        implements AuthenticationAgentsResource {

    public static final String name = "authenticationAgents";

    public AuthenticationAgentsResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<AuthenticationAgents> getAsync(String accessToken, String queryParams)
            throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, AuthenticationAgents.class);
    }

    public CompletableFuture<AuthenticationAgents> getAsync(String accessToken)
            throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public AuthenticationAgentResourceFacade getMember(String id) {
        AuthenticationAgentResourceFacade resource = new AuthenticationAgentResourceFacade(getHttpClient(),
                getServiceUri(), getPath() + "/" + id);
        return resource;
    }

}
