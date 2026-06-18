/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.application;

import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hp.ext.clients.ExecutableResource;
import com.hp.ext.service.application.ApplicationAgent_Refresh;
import com.hp.ext.service.application.RefreshRequest;

public interface RefreshOperationResource extends ExecutableResource<ApplicationAgent_Refresh> {
    CompletableFuture<ApplicationAgent_Refresh> executeAsync(String accessToken, RefreshRequest request, String queryParams)
        throws URISyntaxException, JsonProcessingException;
}
