/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.application;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hp.ext.clients.BaseCollectionResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.application.Message;
import com.hp.ext.service.application.Message_Create;
import com.hp.ext.service.application.Messages;

public class MessagesResourceFacade extends BaseCollectionResourceFacade<MessageResourceFacade>
    implements MessagesResource {

    public static final String name = "messages";

    public MessagesResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<Messages> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
            createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, Messages.class);

    }

    public CompletableFuture<Messages> getAsync(String accessToken) throws URISyntaxException {
            return getAsync(accessToken, null);
    }

    @Override
    public MessageResourceFacade getMember(String id) {
        MessageResourceFacade resource = new MessageResourceFacade(getHttpClient(),
            getServiceUri(), getPath() + "/" + id);
        return resource;
    }

    @Override
    public CompletableFuture<Message> createAsync(String accessToken, Message_Create resource, String queryParams) throws JsonProcessingException, URISyntaxException {
        return ResourceFacadeHelper.createResourceAsync(getHttpClient(),
            createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, resource, Message.class);
    }

    public CompletableFuture<Message> createAsync(String accessToken, Message_Create resource) throws JsonProcessingException, URISyntaxException {
        return createAsync(accessToken, resource, null);
    }
}
