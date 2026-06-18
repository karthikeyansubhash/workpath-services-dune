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
import com.hp.ext.clients.CollectionResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.security.SecurityAgents;

public class SecurityAgentsResourceFacade extends BaseResourceFacade implements SecurityAgentsResource, CollectionResourceFacade<SecurityAgentResource> {

    public static final String name = "securityAgents";

    public SecurityAgentsResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<SecurityAgents> getAsync(String accessToken, String queryParams) throws URISyntaxException, MessagingException, IOException {
        URI requestUri = createRequestUri(getServiceUri(), getPath(), queryParams);
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(), requestUri, accessToken, SecurityAgents.class);
    }

    public CompletableFuture<SecurityAgents> getAsync(String accessToken) throws URISyntaxException, MessagingException, IOException {
        return getAsync(accessToken, null);
    }

    @Override
    public SecurityAgentResourceFacade getMember(String id) {
        SecurityAgentResourceFacade securityAgent = new SecurityAgentResourceFacade(getHttpClient(), getServiceUri(), getPath() + "/" + id);
        return securityAgent;
    }

}
