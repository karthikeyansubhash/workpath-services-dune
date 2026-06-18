/**
 * (C) Copyright 2023 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.deviceusage;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseCollectionResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.deviceUsage.DeviceUsageAgents;

public class DeviceUsageAgentsResourceFacade extends BaseCollectionResourceFacade<DeviceUsageAgentResourceFacade>
        implements DeviceUsageAgentsResource {

    public static final String name = "deviceUsageAgents";

    public DeviceUsageAgentsResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<DeviceUsageAgents> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, DeviceUsageAgents.class);
    }

    public CompletableFuture<DeviceUsageAgents> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public DeviceUsageAgentResourceFacade getMember(String id) {
        DeviceUsageAgentResourceFacade resource = new DeviceUsageAgentResourceFacade(getHttpClient(),
                getServiceUri(), getPath() + "/" + id);
        return resource;
    }
}
