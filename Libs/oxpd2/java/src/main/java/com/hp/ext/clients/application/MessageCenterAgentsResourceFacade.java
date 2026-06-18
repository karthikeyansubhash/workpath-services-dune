/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.application;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseCollectionResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.application.MessageCenterAgents;

public class MessageCenterAgentsResourceFacade extends BaseCollectionResourceFacade<MessageCenterAgentResourceFacade>
        implements MessageCenterAgentsResource {

    public static final String name = "messageCenterAgents";

    public MessageCenterAgentsResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<MessageCenterAgents> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, MessageCenterAgents.class);
    }

    public CompletableFuture<MessageCenterAgents> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public MessageCenterAgentResourceFacade getMember(String id) {
        MessageCenterAgentResourceFacade resource = new MessageCenterAgentResourceFacade(getHttpClient(),
                getServiceUri(), getPath() + "/" + id);
        return resource;
    }
}
