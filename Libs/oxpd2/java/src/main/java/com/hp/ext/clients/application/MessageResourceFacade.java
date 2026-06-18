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
import com.hp.ext.service.application.Message;
import com.hp.ext.types.base.DeleteContent;

public class MessageResourceFacade extends BaseResourceFacade
    implements MessageResource {

    public static final String name = "message";

    public MessageResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<Message> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
            createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, Message.class);
    }

    public CompletableFuture<Message> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public CompletableFuture<DeleteContent> deleteAsync(String accessToken, String queryParams) throws URISyntaxException {
        URI requestUri = createRequestUri(getServiceUri(), getPath(), queryParams);
        return ResourceFacadeHelper.deleteResourceAsync(getHttpClient(), requestUri, accessToken, DeleteContent.class);
    }

    public CompletableFuture<DeleteContent> deleteAsync(String accessToken) throws URISyntaxException {
        return deleteAsync(accessToken, null);
    }

}
