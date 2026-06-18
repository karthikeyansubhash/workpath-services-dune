/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.application;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseCollectionResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.application.ApplicationAccessPoints;

public class ApplicationAccessPointsResourceFacade
        extends BaseCollectionResourceFacade<ApplicationAccessPointResourceFacade>
        implements ApplicationAccessPointsResource {

    public static final String name = "applicationAccessPoints";

    public ApplicationAccessPointsResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<ApplicationAccessPoints> getAsync(String accessToken, String queryParams)
            throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, ApplicationAccessPoints.class);

    }

    public CompletableFuture<ApplicationAccessPoints> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public ApplicationAccessPointResourceFacade getMember(String id) {
        ApplicationAccessPointResourceFacade resource = new ApplicationAccessPointResourceFacade(getHttpClient(),
                getServiceUri(), getPath() + "/" + id);
        return resource;
    }

}
