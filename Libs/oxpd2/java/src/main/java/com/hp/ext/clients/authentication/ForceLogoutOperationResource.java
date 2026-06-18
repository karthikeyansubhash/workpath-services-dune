/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.authentication;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hp.ext.clients.ExecutableResource;
import com.hp.ext.service.authentication.Session_ForceLogout;

public interface ForceLogoutOperationResource extends ExecutableResource<Session_ForceLogout> {
    CompletableFuture<Session_ForceLogout> executeAsync(String accessToken, String queryParms)
            throws URISyntaxException, JsonProcessingException, IOException;

}
