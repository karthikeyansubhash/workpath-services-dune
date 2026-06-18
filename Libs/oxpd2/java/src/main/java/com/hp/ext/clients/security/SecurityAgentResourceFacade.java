/**
 * (C) Copyright 2023 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.security;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import javax.mail.MessagingException;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.security.SecurityAgent;

public class SecurityAgentResourceFacade extends BaseResourceFacade implements SecurityAgentResource {

    public static final String name = "securityAgent";

    private ResolveSecurityExpressionOperationResourceFacade resolveSecurityExpression = null;

    public SecurityAgentResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
        resolveSecurityExpression = new ResolveSecurityExpressionOperationResourceFacade(httpClient, serviceUri,
            path + "/" + ResolveSecurityExpressionOperationResourceFacade.name);
    }

    @Override
    public CompletableFuture<SecurityAgent> getAsync(String accessToken, String queryParams) throws URISyntaxException, MessagingException, IOException {
        URI requestUri = createRequestUri(getServiceUri(), getPath(), queryParams);
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(), requestUri, accessToken, SecurityAgent.class);
    }

    public CompletableFuture<SecurityAgent> getAsync(String accessToken) throws URISyntaxException, MessagingException, IOException {
        return getAsync(accessToken, null);
    }

    @Override
    public ResolveSecurityExpressionOperationResourceFacade resolveSecurityExpression() {
        return resolveSecurityExpression;
    }
}
