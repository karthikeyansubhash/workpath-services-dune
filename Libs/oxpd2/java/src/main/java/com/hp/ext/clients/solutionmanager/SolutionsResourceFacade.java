/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.solutionmanager;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.CollectionResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.solutionManager.Solutions;

public class SolutionsResourceFacade extends BaseResourceFacade
implements SolutionsResource, CollectionResourceFacade<SolutionResource> {

    public static final String name = "solutions";

    /**
     * @param httpClient The instance of the HttpClient to use
     * @param serviceUri The fully-formed service URI of the service host
     * @param path for solutions end point
     */
    public SolutionsResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<Solutions> getAsync(String accessToken, String queryParams)
            throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, Solutions.class);
    }

    public CompletableFuture<Solutions> getAsync(String accessToken)
            throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public SolutionResourceFacade getMember(String id) {
        SolutionResourceFacade solutionResourceFacade = new SolutionResourceFacade(getHttpClient(),
                getServiceUri(), getPath() + "/" + id);
        return solutionResourceFacade;
    }
}
