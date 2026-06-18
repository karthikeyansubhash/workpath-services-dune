/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.authentication;

import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hp.ext.clients.ExecutableResource;
import com.hp.ext.service.authentication.AuthenticationAgent_Login;
import com.hp.ext.types.authentication.PrePromptResult;

/**
 * The Facade Interface for the Authentication Service Agent Login resource
 */
public interface LoginOperationResource extends ExecutableResource<AuthenticationAgent_Login> {
    CompletableFuture<AuthenticationAgent_Login> executeAsync(String accessToken, PrePromptResult prePromptResult, String queryParams) throws URISyntaxException, JsonProcessingException;
}
