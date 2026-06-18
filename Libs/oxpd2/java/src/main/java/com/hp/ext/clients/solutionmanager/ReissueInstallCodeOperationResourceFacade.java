/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.solutionmanager;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.solutionManager.Solution_ReissueInstallCode;

public class ReissueInstallCodeOperationResourceFacade extends BaseResourceFacade
        implements ReissueInstallCodeOperationResource {

    public static final String name = "reissueInstallCode";

    public ReissueInstallCodeOperationResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<Solution_ReissueInstallCode> executeAsync(String accessToken, String queryParams)
            throws URISyntaxException, IOException {
        URI requestUri = createRequestUri(getServiceUri(), getPath(), queryParams);

        return ResourceFacadeHelper.executeResourceOperationAsync(getHttpClient(), requestUri, accessToken, null,
                Solution_ReissueInstallCode.class);
    }

    public CompletableFuture<Solution_ReissueInstallCode> executeAsync(String accessToken)
            throws URISyntaxException, IOException {
        return executeAsync(accessToken, null);
    }

}
