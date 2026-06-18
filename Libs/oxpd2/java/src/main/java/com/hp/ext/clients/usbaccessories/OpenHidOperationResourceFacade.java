/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.usbaccessories;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.usbAccessories.Accessories_Accessory_Hid_Open_Params;
import com.hp.ext.service.usbAccessories.Hid_Open;

public class OpenHidOperationResourceFacade extends BaseResourceFacade implements OpenHidOperationResource {

    public static final String name = "open";

    public OpenHidOperationResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    public CompletableFuture<Hid_Open> executeAsync(String accessToken, Accessories_Accessory_Hid_Open_Params resource, String queryParams) throws URISyntaxException, JsonProcessingException {
        return ResourceFacadeHelper.executeResourceOperationAsync(getHttpClient(),
            createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, resource, Hid_Open.class);
    }

    public CompletableFuture<Hid_Open> executeAsync(String accessToken, Accessories_Accessory_Hid_Open_Params resource) throws URISyntaxException, JsonProcessingException {
        return executeAsync(accessToken, resource, null);
    }
}
