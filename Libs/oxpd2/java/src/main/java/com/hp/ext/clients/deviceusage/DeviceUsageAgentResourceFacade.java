/**
 * (C) Copyright 2023 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.deviceusage;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.deviceUsage.DeviceUsageAgent;

public class DeviceUsageAgentResourceFacade extends BaseResourceFacade implements DeviceUsageAgentResource {

    private LifetimeCountersResourceFacade lifetimeCounters = null;

    public static final String name = "deviceUsageAgent";

    public DeviceUsageAgentResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
        lifetimeCounters = new LifetimeCountersResourceFacade(getHttpClient(), getServiceUri(),
                getPath() + "/" + LifetimeCountersResourceFacade.name);
    }

    public CompletableFuture<DeviceUsageAgent> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, DeviceUsageAgent.class);
    }

    public CompletableFuture<DeviceUsageAgent> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    public LifetimeCountersResourceFacade lifetimeCounters() {
        return lifetimeCounters;
    }
}
