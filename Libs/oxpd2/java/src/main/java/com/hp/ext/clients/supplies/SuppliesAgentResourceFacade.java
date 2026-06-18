/**
 * (C) Copyright 2023 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.supplies;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.supplies.SuppliesAgent;

public class SuppliesAgentResourceFacade extends BaseResourceFacade implements SuppliesAgentResource {

    private SuppliesConfigurationResourceFacade suppliesConfiguration = null;
    private SuppliesInfoResourceFacade suppliesInfo = null;
    private SuppliesUsageResourceFacade suppliesUsage = null;

    public static final String name = "suppliesAgent";

    public SuppliesAgentResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
        suppliesConfiguration = new SuppliesConfigurationResourceFacade(getHttpClient(), getServiceUri(),
                getPath() + "/" + SuppliesConfigurationResourceFacade.name);
        suppliesInfo = new SuppliesInfoResourceFacade(getHttpClient(), getServiceUri(),
                getPath() + "/" + SuppliesInfoResourceFacade.name);
        suppliesUsage = new SuppliesUsageResourceFacade(getHttpClient(), getServiceUri(),
                getPath() + "/" + SuppliesUsageResourceFacade.name);
    }

    public CompletableFuture<SuppliesAgent> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, SuppliesAgent.class);
    }

    public CompletableFuture<SuppliesAgent> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    public SuppliesConfigurationResourceFacade suppliesConfiguration() {
        return suppliesConfiguration;
    }

    public SuppliesInfoResourceFacade suppliesInfo() {
        return suppliesInfo;
    }

    public SuppliesUsageResourceFacade suppliesUsage() {
        return suppliesUsage;
    }
}
