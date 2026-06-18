/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.discovery;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.types.common.ServicesDiscoveryImpl;

public class ServicesDiscoveryResourceFacade extends BaseResourceFacade implements ServicesDiscoveryResource {


    public static final String name = "";

    public ServicesDiscoveryResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }


    @Override
    public CompletableFuture<ServicesDiscoveryImpl> getAsync(String accessToken, String queryParams) throws URISyntaxException {
       return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, ServicesDiscoveryImpl.class);

    }

    public CompletableFuture<ServicesDiscoveryImpl> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }


    public CompletableFuture<ServicesDiscoveryImpl> getAsync() throws URISyntaxException {
        return getAsync(null, null);
    }

}
