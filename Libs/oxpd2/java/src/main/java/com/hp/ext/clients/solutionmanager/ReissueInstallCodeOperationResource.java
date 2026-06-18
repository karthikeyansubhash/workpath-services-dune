/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.solutionmanager;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.ExecutableResource;
import com.hp.ext.service.solutionManager.Solution_ReissueInstallCode;

public interface ReissueInstallCodeOperationResource extends ExecutableResource<Solution_ReissueInstallCode> {
    CompletableFuture<Solution_ReissueInstallCode> executeAsync(String accessToken, String queryParams)
            throws URISyntaxException, IOException;
}
