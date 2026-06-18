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
import com.hp.ext.service.device.Scanner;

/**
 * The Facade for the Device Service Scanner status resource
 */
public class ScannerResourceFacade extends BaseResourceFacade
        implements ScannerResource {

    public static final String name = "scanner";

    public ScannerResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<Scanner> getAsync(String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), null, Scanner.class);
    }

    public CompletableFuture<Scanner> getAsync() throws URISyntaxException {
        return getAsync(null);
    }
}
