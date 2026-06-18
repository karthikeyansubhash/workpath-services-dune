/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.usbaccessories;

import com.hp.ext.clients.ResourceFacadeHelper;
import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.CollectionResourceFacade;
import com.hp.ext.service.usbAccessories.Accessories;

public class AccessoriesResourceFacade extends BaseResourceFacade implements AccessoriesResource, CollectionResourceFacade<AccessoryResourceFacade> {
    
    public static final String name = "accessories";

    public AccessoriesResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }
    
    @Override
    public CompletableFuture<Accessories> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(), 
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, Accessories.class);
    }

    public CompletableFuture<Accessories> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public AccessoryResourceFacade getMember(String id) {
        AccessoryResourceFacade accessoryResource = new AccessoryResourceFacade(getHttpClient(), getServiceUri(), getPath() + "/" + id);
        return accessoryResource;
    }

}
