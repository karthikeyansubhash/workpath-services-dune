package com.hp.ext.clients.sdksupport;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.hp.net.http.HttpClient;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.sdkSupport.KeyPress;
import com.hp.ext.service.sdkSupport.ControlPanel_PressKey;

public class PressKeyResourceFacade extends BaseResourceFacade implements PressKeyResource {
    public static final String name = "pressKey";

    protected PressKeyResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    public CompletableFuture<ControlPanel_PressKey> executeAsync(String accessToken, KeyPress resource,
            String queryParams) throws JsonProcessingException, URISyntaxException {
        return ResourceFacadeHelper.executeResourceOperationAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, resource,
                ControlPanel_PressKey.class);
    }

    public CompletableFuture<ControlPanel_PressKey> executeAsync(String accessToken, KeyPress resource)
            throws JsonProcessingException, URISyntaxException {
        return executeAsync(accessToken, resource, null);
    }
}
