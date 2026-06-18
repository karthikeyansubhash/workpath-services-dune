package com.hp.ext.clients.usbaccessories;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.usbAccessories.UsbAccessoriesAgent;

public class UsbAccessoriesAgentResourceFacade extends BaseResourceFacade implements UsbAccessoriesAgentResource{

    public static final String name = "usbAccessoriesAgent";

    public UsbAccessoriesAgentResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<UsbAccessoriesAgent> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, UsbAccessoriesAgent.class);
    }

    public CompletableFuture<UsbAccessoriesAgent> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken,null);
    }

}
