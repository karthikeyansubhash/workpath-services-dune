/**
 * (C) Copyright 2023 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.supplies;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseCollectionResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.supplies.SuppliesAgents;

public class SuppliesAgentsResourceFacade extends BaseCollectionResourceFacade<SuppliesAgentResourceFacade>
        implements SuppliesAgentsResource {

    public static final String name = "suppliesAgents";

    public SuppliesAgentsResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<SuppliesAgents> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, SuppliesAgents.class);
    }

    public CompletableFuture<SuppliesAgents> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public SuppliesAgentResourceFacade getMember(String id) {
        SuppliesAgentResourceFacade resource = new SuppliesAgentResourceFacade(getHttpClient(),
                getServiceUri(), getPath() + "/" + id);
        return resource;
    }


}
