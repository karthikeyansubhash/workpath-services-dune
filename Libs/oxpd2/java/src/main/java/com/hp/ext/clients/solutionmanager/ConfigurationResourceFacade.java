/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */

package com.hp.ext.clients.solutionmanager;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.solutionManager.Configuration;
import com.hp.ext.service.solutionManager.Configuration_Modify;
import com.hp.ext.types.base.DeleteContent;

public class ConfigurationResourceFacade extends BaseResourceFacade implements ConfigurationResource {

    public static final String name = "configuration";

    private DataResourceFacade data = null;

    /**
     * @param httpClient
     * @param serviceUri
     * @param path
     */
    public ConfigurationResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
        data = new DataResourceFacade(httpClient, serviceUri, path + "/" + DataResourceFacade.name);
    }

    @Override
    public CompletableFuture<Configuration> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        URI requestUri = createRequestUri(getServiceUri(), getPath(), queryParams);
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(), requestUri, accessToken, Configuration.class);
    }

    public CompletableFuture<Configuration> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public CompletableFuture<Configuration> modifyAsync(String accessToken, Configuration_Modify resource, String queryParams) throws JsonProcessingException, URISyntaxException {
        URI requestUri = createRequestUri(getServiceUri(), getPath(), queryParams);
        return ResourceFacadeHelper.modifyResourceAsync(getHttpClient(), requestUri, accessToken, resource, Configuration.class);
    }

    public CompletableFuture<Configuration> modifyAsync(String accessToken, Configuration_Modify resource) throws JsonProcessingException, URISyntaxException {
        return modifyAsync(accessToken, resource, null);
    }

    @Override
    public DataResourceFacade dataResource() {
        return this.data;
    }

}
