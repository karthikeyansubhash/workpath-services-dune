/**
 * (C) Copyright 2023 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.copy;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.copy.CopyJob;

public class CopyJobResourceFacade extends BaseResourceFacade implements CopyJobResource {
    private CancelCopyJobOperationResourceFacade cancel = null;

    public static final String name = "copyJob";

    public CopyJobResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
        cancel = new CancelCopyJobOperationResourceFacade(getHttpClient(), getServiceUri(),
                getPath() + "/" + CancelCopyJobOperationResourceFacade.name);
    }

    public CompletableFuture<CopyJob> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, CopyJob.class);
    }

    public CompletableFuture<CopyJob> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public CancelCopyJobOperationResourceFacade cancel() {
        return cancel;
    }
}
