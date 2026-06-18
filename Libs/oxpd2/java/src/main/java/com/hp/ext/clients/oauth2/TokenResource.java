/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.oauth2;

import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface TokenResource {
    CompletableFuture<Token> passwordGrantAsync(PasswordGrantRequest grantRequest)
            throws URISyntaxException, JsonProcessingException;

    CompletableFuture<Token> authorizationCodeGrantAsync(AuthorizationCodeGrantRequest grantRequest)
            throws URISyntaxException, JsonProcessingException;

    CompletableFuture<Token> refreshTokenGrantAsync(RefreshTokenGrantRequest grantRequest)
            throws JsonProcessingException, URISyntaxException;
}
