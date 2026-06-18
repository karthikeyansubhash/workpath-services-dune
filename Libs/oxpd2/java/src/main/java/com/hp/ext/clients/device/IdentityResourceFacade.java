/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.device;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.device.Identity;

/**
 * The Facade for the Device Service Identity resource
 */
public class IdentityResourceFacade extends BaseResourceFacade
        implements IdentityResource {

    public static final String name = "identity";

    public IdentityResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<Identity> getAsync(String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), null, Identity.class);
    }

    public CompletableFuture<Identity> getAsync() throws URISyntaxException {
        return getAsync(null);
    }
}
