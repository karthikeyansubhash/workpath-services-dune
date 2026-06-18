/**
 * (C) Copyright 2023 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients;

import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface PublicReplaceableResource<TResponse, TResource> {
    CompletableFuture<TResponse> replaceAsync(TResource resource, String queryParams) throws JsonProcessingException, URISyntaxException;
}
