/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.application;

import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hp.ext.clients.ExecutableResource;
import com.hp.ext.service.application.ApplicationRuntime_Reset;
import com.hp.ext.service.application.ResetRequest;

public interface ResetOperationResource extends ExecutableResource<ApplicationRuntime_Reset> {
    CompletableFuture<ApplicationRuntime_Reset> executeAsync(String accessToken, ResetRequest resource, String queryParams) throws URISyntaxException, JsonProcessingException;
}
