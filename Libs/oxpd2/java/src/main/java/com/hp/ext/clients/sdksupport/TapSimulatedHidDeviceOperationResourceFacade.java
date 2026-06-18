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
import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.sdkSupport.SimulatedHidDevice_Tap;
import com.hp.ext.service.sdkSupport.TapOperation;

public class TapSimulatedHidDeviceOperationResourceFacade extends BaseResourceFacade implements TapSimulatedHidDeviceOperationResource {

    public static final String name = "tap";

    public TapSimulatedHidDeviceOperationResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    public CompletableFuture<SimulatedHidDevice_Tap> executeAsync(String accessToken, TapOperation resource, String queryParams) throws URISyntaxException, JsonProcessingException {
        return ResourceFacadeHelper.executeResourceOperationAsync(getHttpClient(),
            createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, resource, SimulatedHidDevice_Tap.class);
    }

    public CompletableFuture<SimulatedHidDevice_Tap> executeAsync(String accessToken, TapOperation resource) throws URISyntaxException, JsonProcessingException {
        return executeAsync(accessToken, resource, null);
    }
}
