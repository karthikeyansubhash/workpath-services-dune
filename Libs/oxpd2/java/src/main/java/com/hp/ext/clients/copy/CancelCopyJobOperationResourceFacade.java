/**
 * (C) Copyright 2023 HP Development Company, L.P.
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
import com.hp.ext.service.copy.CopyJob_Cancel;

public class CancelCopyJobOperationResourceFacade extends BaseResourceFacade implements CancelCopyJobOperationResource {

    public static final String name = "cancel";

    public CancelCopyJobOperationResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    public CompletableFuture<CopyJob_Cancel> executeAsync(String accessToken, String queryParams) throws IOException, URISyntaxException {
        return ResourceFacadeHelper.executeResourceOperationAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, null, CopyJob_Cancel.class);
    }

    public CompletableFuture<CopyJob_Cancel> executeAsync(String accessToken) throws IOException, URISyntaxException {
        return executeAsync(accessToken, null);
    }
}
