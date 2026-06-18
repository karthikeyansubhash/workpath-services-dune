/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.authentication;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.authentication.AuthenticationAgent_Login;
import com.hp.ext.types.authentication.PrePromptResult;

public class LoginOperationResourceFacade extends BaseResourceFacade
        implements LoginOperationResource {

    public static final String name = "login";

    public LoginOperationResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<AuthenticationAgent_Login> executeAsync(String accessToken, PrePromptResult prePromptResult, String queryParams) throws URISyntaxException, JsonProcessingException {
        URI requestUri = createRequestUri(getServiceUri(), getPath(), queryParams);
        return ResourceFacadeHelper.executeResourceOperationAsync(getHttpClient(), requestUri, accessToken, prePromptResult,
                AuthenticationAgent_Login.class);
    }

    public CompletableFuture<AuthenticationAgent_Login> executeAsync(String accessToken, PrePromptResult prePromptResult) throws URISyntaxException, JsonProcessingException {
        return executeAsync(accessToken, prePromptResult, null);
    }

}
