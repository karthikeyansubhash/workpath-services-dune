/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

import javax.mail.MessagingException;

public interface PublicReadableResource<TResponse> {
    CompletableFuture<TResponse> getAsync(String queryParams) throws URISyntaxException, MessagingException, IOException;
}
