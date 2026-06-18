/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.application;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseCollectionResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.application.ApplicationAgents;

public class ApplicationAgentsResourceFacade extends BaseCollectionResourceFacade<ApplicationAgentResourceFacade>
        implements ApplicationAgentsResource {

    public static final String name = "applicationAgents";

    public ApplicationAgentsResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<ApplicationAgents> getAsync(String accessToken, String queryParams)
            throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, ApplicationAgents.class);
    }

    public CompletableFuture<ApplicationAgents> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public ApplicationAgentResourceFacade getMember(String id) {
        ApplicationAgentResourceFacade resource = new ApplicationAgentResourceFacade(getHttpClient(),
                getServiceUri(), getPath() + "/" + id);
        return resource;
    }

}
