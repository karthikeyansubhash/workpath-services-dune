/**
 * (C) Copyright 2023 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.sdksupport;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.sdkSupport.MacroExpander;

public class MacroExpanderResourceFacade extends BaseResourceFacade implements MacroExpanderResource {

    public static final String name = "macroExpander";

    public MacroExpanderResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<MacroExpander> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, MacroExpander.class);
    }

    public CompletableFuture<MacroExpander> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }
}
