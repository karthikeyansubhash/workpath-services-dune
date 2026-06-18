/**
 * (C) Copyright 2023 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.sdksupport;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.sdkSupport.SimulatedHidDevice;
import com.hp.ext.types.base.DeleteContent;

public class SimulatedHidDeviceResourceFacade extends BaseResourceFacade implements SimulatedHidDeviceResource {

    public static final String name = "simulatedHidDevice";

    private TapSimulatedHidDeviceOperationResourceFacade tap = null;

    public SimulatedHidDeviceResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
        tap = new TapSimulatedHidDeviceOperationResourceFacade(httpClient, serviceUri,
            path + "/" + TapSimulatedHidDeviceOperationResourceFacade.name);
    }

    @Override
    public CompletableFuture<SimulatedHidDevice> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, SimulatedHidDevice.class);
    }

    public CompletableFuture<SimulatedHidDevice> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public CompletableFuture<DeleteContent> deleteAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.deleteResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, DeleteContent.class);
    }

    public CompletableFuture<DeleteContent> deleteAsync(String accessToken) throws URISyntaxException {
        return deleteAsync(accessToken, null);
    }

    @Override
    public TapSimulatedHidDeviceOperationResourceFacade tap() {
        return tap;
    }
}
