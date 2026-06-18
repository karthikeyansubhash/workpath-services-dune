/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.application;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.application.ApplicationAccessPoint;

public class ApplicationAccessPointResourceFacade extends BaseResourceFacade
        implements ApplicationAccessPointResource {

    public static final String name = "applicationAccessPoint";

    private InitiateLaunchOperationResourceFacade initiateLaunch = null;

    public ApplicationAccessPointResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
        initiateLaunch = new InitiateLaunchOperationResourceFacade(httpClient, serviceUri,
            path + "/" + InitiateLaunchOperationResourceFacade.name);
    }

    @Override
    public CompletableFuture<ApplicationAccessPoint> getAsync(String accessToken, String queryParams)
            throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, ApplicationAccessPoint.class);

    }

    public CompletableFuture<ApplicationAccessPoint> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public InitiateLaunchOperationResourceFacade initiateLaunch() {
        return initiateLaunch;
    }
}
