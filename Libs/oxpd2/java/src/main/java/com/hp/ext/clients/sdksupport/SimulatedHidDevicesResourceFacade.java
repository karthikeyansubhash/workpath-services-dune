/**
 * (C) Copyright 2023 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.sdksupport;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hp.ext.clients.BaseCollectionResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.sdkSupport.SimulatedHidDevice;
import com.hp.ext.service.sdkSupport.SimulatedHidDevice_Create;
import com.hp.ext.service.sdkSupport.SimulatedHidDevices;

public class SimulatedHidDevicesResourceFacade extends BaseCollectionResourceFacade<SimulatedHidDeviceResourceFacade> implements SimulatedHidDevicesResource {

    public static final String name = "simulatedHidDevices";

    public SimulatedHidDevicesResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<SimulatedHidDevices> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, SimulatedHidDevices.class);
    }

    public CompletableFuture<SimulatedHidDevices> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public CompletableFuture<SimulatedHidDevice> createAsync(String accessToken, SimulatedHidDevice_Create resource, String queryParams) throws URISyntaxException, JsonProcessingException {
        return ResourceFacadeHelper.createResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, resource, SimulatedHidDevice.class);
    }

    public CompletableFuture<SimulatedHidDevice> createAsync(String accessToken, SimulatedHidDevice_Create resource) throws URISyntaxException, JsonProcessingException {
        return createAsync(accessToken, resource, null);
    }

    @Override
    public SimulatedHidDeviceResourceFacade getMember(String id) {
        SimulatedHidDeviceResourceFacade resource = new SimulatedHidDeviceResourceFacade(getHttpClient(),
                getServiceUri(), getPath() + "/" + id);
        return resource;
    }
}
