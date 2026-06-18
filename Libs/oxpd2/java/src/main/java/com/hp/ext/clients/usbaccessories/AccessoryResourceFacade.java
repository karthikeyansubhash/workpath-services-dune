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
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.usbAccessories.Accessory;

public class AccessoryResourceFacade extends BaseResourceFacade implements AccessoryResource {

    public static final String name = "accessory";
    
    private HidResourceFacade hid = null;

    public AccessoryResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
        hid = new HidResourceFacade(httpClient, serviceUri, path + "/" + HidResourceFacade.name);
    }

    @Override
    public CompletableFuture<Accessory> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(), 
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, Accessory.class);
    }

    public CompletableFuture<Accessory> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null); 
    }

    @Override
    public HidResourceFacade hidResource() {
        return this.hid;
    }

}
