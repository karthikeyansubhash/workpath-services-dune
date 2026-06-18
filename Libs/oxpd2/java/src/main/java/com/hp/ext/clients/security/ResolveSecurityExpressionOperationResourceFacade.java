/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.security;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.security.ResolveSecurityExpressionRequest;
import com.hp.ext.service.security.SecurityAgent_ResolveSecurityExpression;

public class ResolveSecurityExpressionOperationResourceFacade extends BaseResourceFacade implements ResolveSecurityExpressionOperationResource {
    public static final String name = "resolveSecurityExpression";

    public ResolveSecurityExpressionOperationResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    public CompletableFuture<SecurityAgent_ResolveSecurityExpression> executeAsync(String accessToken, ResolveSecurityExpressionRequest resource, String queryParams) throws URISyntaxException, JsonProcessingException {
        return ResourceFacadeHelper.executeResourceOperationAsync(getHttpClient(),
            createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, resource, SecurityAgent_ResolveSecurityExpression.class);
    }

    public CompletableFuture<SecurityAgent_ResolveSecurityExpression> executeAsync(String accessToken, ResolveSecurityExpressionRequest resource) throws URISyntaxException, JsonProcessingException {
        return executeAsync(accessToken, resource, null);
    }
}
