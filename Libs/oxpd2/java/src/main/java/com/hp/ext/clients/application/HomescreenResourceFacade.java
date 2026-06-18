package com.hp.ext.clients.application;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.application.Homescreen;
import com.hp.ext.service.application.Homescreen_Modify;

public class HomescreenResourceFacade extends BaseResourceFacade implements HomescreenResource {

    public static final String name = "homescreen";

    public HomescreenResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<Homescreen> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, Homescreen.class);
    }

    public CompletableFuture<Homescreen> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public CompletableFuture<Homescreen> modifyAsync(String accessToken, Homescreen_Modify resource, String queryParams) throws JsonProcessingException, URISyntaxException {
        return ResourceFacadeHelper.modifyResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, resource, Homescreen.class);
    }

    public CompletableFuture<Homescreen> modifyAsync(String accessToken, Homescreen_Modify resource) throws JsonProcessingException, URISyntaxException {
        return modifyAsync(accessToken, resource, null);
    }
}
