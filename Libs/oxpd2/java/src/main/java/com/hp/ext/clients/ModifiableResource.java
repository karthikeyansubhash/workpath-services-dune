/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients;

import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface ModifiableResource<TResponse, TResource> {
    CompletableFuture<TResponse> modifyAsync(String accessToken, TResource resource, String queryParams) throws JsonProcessingException, URISyntaxException;
}
