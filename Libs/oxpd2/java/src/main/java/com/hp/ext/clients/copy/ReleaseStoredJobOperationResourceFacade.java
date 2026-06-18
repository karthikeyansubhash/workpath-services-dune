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
import com.hp.ext.service.copy.ReleaseStoredJobRequest;
import com.hp.ext.service.copy.StoredJob_Release;

public class ReleaseStoredJobOperationResourceFacade extends BaseResourceFacade implements ReleaseStoredJobOperationResource {

    public static final String name = "release";

    public ReleaseStoredJobOperationResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    public CompletableFuture<StoredJob_Release> executeAsync(String accessToken, ReleaseStoredJobRequest releaseStoredJobRequest, String queryParams) throws IOException, URISyntaxException {
        return ResourceFacadeHelper.executeResourceOperationAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, releaseStoredJobRequest, StoredJob_Release.class);
    }

    public CompletableFuture<StoredJob_Release> executeAsync(String accessToken, ReleaseStoredJobRequest releaseStoredJobRequest) throws IOException, URISyntaxException {
        return executeAsync(accessToken, releaseStoredJobRequest, null);
    }
}
