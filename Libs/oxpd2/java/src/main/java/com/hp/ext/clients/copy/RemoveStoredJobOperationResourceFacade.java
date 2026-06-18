/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.copy;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.copy.RemoveStoredJobRequest;
import com.hp.ext.service.copy.StoredJob_Remove;

public class RemoveStoredJobOperationResourceFacade extends BaseResourceFacade implements RemoveStoredJobOperationResource {

    public static final String name = "remove";

    public RemoveStoredJobOperationResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    public CompletableFuture<StoredJob_Remove> executeAsync(String accessToken, RemoveStoredJobRequest removeStoredJobRequest, String queryParams) throws IOException, URISyntaxException {
        return ResourceFacadeHelper.executeResourceOperationAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, removeStoredJobRequest, StoredJob_Remove.class);
    }

    public CompletableFuture<StoredJob_Remove> executeAsync(String accessToken, RemoveStoredJobRequest removeStoredJobRequest) throws IOException, URISyntaxException {
        return executeAsync(accessToken, removeStoredJobRequest, null);
    }
}
