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
import com.hp.ext.service.application.I18nAsset;

public class I18nAssetResourceFacade extends BaseResourceFacade
    implements I18nAssetResource {

    public static final String name = "i18asset";

    public I18nAssetResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
    super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<I18nAsset> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
            createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, I18nAsset.class);
    }

    public CompletableFuture<I18nAsset> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }
}
