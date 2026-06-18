/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.authentication;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.authentication.Session_ForceLogout;

public class ForceLogoutOperationResourceFacade extends BaseResourceFacade implements ForceLogoutOperationResource {

    public static final String name = "forceLogout";

    public ForceLogoutOperationResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<Session_ForceLogout> executeAsync(String accessToken, String queryParms)
            throws URISyntaxException, IOException {
        URI requestUri = createRequestUri(getServiceUri(), getPath(), queryParms);
        return ResourceFacadeHelper.executeResourceOperationAsync(getHttpClient(), requestUri, accessToken, null,
                Session_ForceLogout.class);
    }

    public CompletableFuture<Session_ForceLogout> executeAsync(String accessToken)
            throws URISyntaxException, IOException {
        return executeAsync(accessToken, null);
    }

}
