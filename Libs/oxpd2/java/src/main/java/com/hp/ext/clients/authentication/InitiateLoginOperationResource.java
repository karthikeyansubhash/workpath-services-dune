/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.authentication;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.ExecutableResource;
import com.hp.ext.service.authentication.AuthenticationAccessPoint_InitiateLogin;

/**
 * The Facade Interface for the Authentication Service Access Point Initiate Login resource
 */
public interface InitiateLoginOperationResource extends ExecutableResource<AuthenticationAccessPoint_InitiateLogin> {
    CompletableFuture<AuthenticationAccessPoint_InitiateLogin> executeAsync(String accessToken, String queryParams) throws URISyntaxException, IOException;
}
