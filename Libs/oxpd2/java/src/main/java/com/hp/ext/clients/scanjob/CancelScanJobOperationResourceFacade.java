/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.scanjob;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.scanJob.ScanJob_Cancel;

public class CancelScanJobOperationResourceFacade extends BaseResourceFacade implements CancelScanJobOperationResource {

    public static final String name = "cancel";

    public CancelScanJobOperationResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    public CompletableFuture<ScanJob_Cancel> executeAsync(String accessToken, String queryParams) throws IOException, URISyntaxException {
        return ResourceFacadeHelper.executeResourceOperationAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, null, ScanJob_Cancel.class);
    }

    public CompletableFuture<ScanJob_Cancel> executeAsync(String accessToken) throws IOException, URISyntaxException {
        return executeAsync(accessToken, null);
    }
}
