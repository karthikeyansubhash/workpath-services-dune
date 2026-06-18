/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.usbaccessories;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.CollectionResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.usbAccessories.Hid;

public class HidResourceFacade extends BaseResourceFacade implements HidResource, CollectionResourceFacade<OpenHidAccessoryResourceFacade> {

    private OpenHidOperationResourceFacade openHidOperation = null;

    public static final String name = "hid";

    public HidResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
        openHidOperation = new OpenHidOperationResourceFacade(getHttpClient(), getServiceUri(),
                getPath() + "/" + OpenHidOperationResourceFacade.name);
    }

    @Override
    public CompletableFuture<Hid> getAsync(String accessToken, String queryParams) throws URISyntaxException {
            return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, Hid.class);
    }

    public CompletableFuture<Hid> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public OpenHidOperationResourceFacade openHidOperation() {
        return openHidOperation;
    }

    @Override
    public OpenHidAccessoryResourceFacade getMember(String id) {
        OpenHidAccessoryResourceFacade resource = new OpenHidAccessoryResourceFacade(getHttpClient(), getServiceUri(), getPath() + "/" + id);
        return resource;
    }

}
