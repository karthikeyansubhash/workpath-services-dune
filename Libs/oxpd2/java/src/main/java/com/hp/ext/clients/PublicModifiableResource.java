/**
 * (C) Copyright 2023 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients;

import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface PublicModifiableResource<TResponse, TResource> {
    CompletableFuture<TResponse> modifyAsync(TResource resource, String queryParams) throws JsonProcessingException, URISyntaxException;
}
