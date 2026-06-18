/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.scanjob;

import java.net.URI;
import java.net.URISyntaxException;

import com.hp.ext.types.base.DeleteContent;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.scanJob.ScanJob;

public class ScanJobResourceFacade extends BaseResourceFacade implements ScanJobResource {
    private CancelScanJobOperationResourceFacade cancel = null;

    public static final String name = "scanJob";

    public ScanJobResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
        cancel = new CancelScanJobOperationResourceFacade(getHttpClient(), getServiceUri(),
                getPath() + "/" + CancelScanJobOperationResourceFacade.name);
    }

    public CompletableFuture<ScanJob> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, ScanJob.class);
    }

    public CompletableFuture<DeleteContent> deleteAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.deleteResourceAsync(getHttpClient(),
            createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, DeleteContent.class);
    }

    public CompletableFuture<ScanJob> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    public CompletableFuture<DeleteContent> deleteAsync(String accessToken) throws URISyntaxException {
        return deleteAsync(accessToken, null);
    }

    @Override
    public CancelScanJobOperationResourceFacade cancel() {
        return cancel;
    }
}
