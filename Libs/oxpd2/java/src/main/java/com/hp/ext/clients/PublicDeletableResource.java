/**
 * (C) Copyright 2023 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients;

import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

public interface PublicDeletableResource<TResponse> {
    CompletableFuture<TResponse> deleteAsync(String queryParams) throws URISyntaxException;
}
