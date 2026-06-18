/**
 * (C) Copyright 2023 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.sdksupport;

import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hp.ext.clients.ExecutableResource;
import com.hp.ext.service.sdkSupport.SimulatedHidDevice_Tap;
import com.hp.ext.service.sdkSupport.TapOperation;

public interface TapSimulatedHidDeviceOperationResource extends ExecutableResource<SimulatedHidDevice_Tap> {
    CompletableFuture<SimulatedHidDevice_Tap> executeAsync(String accessToken, TapOperation resource, String queryParams)
        throws URISyntaxException, JsonProcessingException;
}
