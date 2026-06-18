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
import com.hp.ext.service.device.DeploymentInformation;

/**
 * The Facade for the Device Service DeploymentInformation resource
 */
public class DeploymentInformationResourceFacade extends BaseResourceFacade
        implements DeploymentInformationResource {

    public static final String name = "deploymentInformation";

    public DeploymentInformationResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<DeploymentInformation> getAsync(String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), null, DeploymentInformation.class);
    }

    public CompletableFuture<DeploymentInformation> getAsync() throws URISyntaxException {
        return getAsync(null);
    }
}
