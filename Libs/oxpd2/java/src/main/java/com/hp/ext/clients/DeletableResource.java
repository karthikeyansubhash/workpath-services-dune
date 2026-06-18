/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients;

import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

public interface DeletableResource<TResponse> {
    CompletableFuture<TResponse> deleteAsync(String accessToken, String queryParams) throws URISyntaxException;
}
