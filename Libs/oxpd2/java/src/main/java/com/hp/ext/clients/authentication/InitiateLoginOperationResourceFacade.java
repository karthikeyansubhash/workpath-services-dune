/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.authentication;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.authentication.AuthenticationAccessPoint_InitiateLogin;

public class InitiateLoginOperationResourceFacade extends BaseResourceFacade
        implements InitiateLoginOperationResource {

    public static final String name = "initiateLogin";

    public InitiateLoginOperationResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<AuthenticationAccessPoint_InitiateLogin> executeAsync(String accessToken, String queryParams) throws URISyntaxException, IOException {
        URI requestUri = createRequestUri(getServiceUri(), getPath(), queryParams);
        return ResourceFacadeHelper.executeResourceOperationAsync(getHttpClient(), requestUri, accessToken, null,
            AuthenticationAccessPoint_InitiateLogin.class);
    }

    public CompletableFuture<AuthenticationAccessPoint_InitiateLogin> executeAsync(String accessToken) throws URISyntaxException, IOException {
        return executeAsync(accessToken,null);
    }

}
