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
import com.hp.ext.service.supplies.SuppliesUsage;

public class SuppliesUsageResourceFacade extends BaseResourceFacade implements SuppliesUsageResource {

    public static final String name = "suppliesUsage";

    public SuppliesUsageResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    public CompletableFuture<SuppliesUsage> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, SuppliesUsage.class);
    }

    public CompletableFuture<SuppliesUsage> getAsync(String accessToken) throws URISyntaxException  {
        return getAsync(accessToken, null);
    }
}
