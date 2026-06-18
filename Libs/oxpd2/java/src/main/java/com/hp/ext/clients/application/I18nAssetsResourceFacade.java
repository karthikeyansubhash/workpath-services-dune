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
import com.hp.ext.service.application.I18nAssets;

public class I18nAssetsResourceFacade extends BaseCollectionResourceFacade<I18nAssetResourceFacade>
    implements I18nAssetsResource {

    public static final String name = "i18nAssets";

    public I18nAssetsResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<I18nAssets> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
            createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, I18nAssets.class);

    }

    public CompletableFuture<I18nAssets> getAsync(String accessToken) throws URISyntaxException {
            return getAsync(accessToken, null);
    }

    @Override
    public I18nAssetResourceFacade getMember(String id) {
        I18nAssetResourceFacade resource = new I18nAssetResourceFacade(getHttpClient(),
            getServiceUri(), getPath() + "/" + id);
        return resource;
    }
}
