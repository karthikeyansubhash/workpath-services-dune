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
import com.hp.ext.service.supplies.SuppliesConfiguration;

public class SuppliesConfigurationResourceFacade extends BaseResourceFacade implements SuppliesConfigurationResource {

    public static final String name = "suppliesConfiguration";

    public SuppliesConfigurationResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    public CompletableFuture<SuppliesConfiguration> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, SuppliesConfiguration.class);
    }

    public CompletableFuture<SuppliesConfiguration> getAsync(String accessToken) throws URISyntaxException  {
        return getAsync(accessToken, null);
    }
}
