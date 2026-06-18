package com.hp.ext.clients.usbaccessories;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.CollectionResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.usbAccessories.UsbAccessoriesAgents;

public class UsbAccessoriesAgentsResourceFacade extends BaseResourceFacade implements UsbAccessoriesAgentsResource, CollectionResourceFacade<UsbAccessoriesAgentResourceFacade> {

    public static final String name = "usbAccessoriesAgents";

    public UsbAccessoriesAgentsResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<UsbAccessoriesAgents> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, UsbAccessoriesAgents.class);
    }

    public CompletableFuture<UsbAccessoriesAgents> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public UsbAccessoriesAgentResourceFacade getMember(String id) {
        UsbAccessoriesAgentResourceFacade resource = new UsbAccessoriesAgentResourceFacade(getHttpClient(), getServiceUri(), getPath() + "/" + id);
        return resource;
    }

}
