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
import com.hp.ext.service.application.MessageCenterAgent;

public class MessageCenterAgentResourceFacade extends BaseResourceFacade implements MessageCenterAgentResource {

    public static final String name = "messageCenterAgent";

    private MessagesResourceFacade messages = null;

    public MessageCenterAgentResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
        messages = new MessagesResourceFacade(httpClient, serviceUri,
            path + "/" + MessagesResourceFacade.name);
    }

    public CompletableFuture<MessageCenterAgent> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, MessageCenterAgent.class);
    }

    public CompletableFuture<MessageCenterAgent> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public MessagesResourceFacade messages() {
        return messages;
    }
}
